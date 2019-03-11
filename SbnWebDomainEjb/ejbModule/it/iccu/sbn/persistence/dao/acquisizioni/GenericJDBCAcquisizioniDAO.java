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

import static org.hamcrest.Matchers.equalTo;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.dao.DAOException;
import it.iccu.sbn.ejb.domain.acquisizioni.AcquisizioniBMT;
import it.iccu.sbn.ejb.domain.documentofisico.Inventario;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;
import it.iccu.sbn.ejb.services.Codici;
import it.iccu.sbn.ejb.services.bibliografica.ServiziBibliografici;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioDettVO;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.BuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.CalcoliVO;
import it.iccu.sbn.ejb.vo.acquisizioni.CambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneBOVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.DatiFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.DocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.GaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppCambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppDocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppGaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOffertaFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSpeseVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OffertaFornitoreAutoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OffertaFornitoreClassificazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OffertaFornitoreIsbdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OffertaFornitoreNoteEdiVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OffertaFornitoreSerieVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OffertaFornitoreSoggettoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OffertaFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ParametriExcelVO;
import it.iccu.sbn.ejb.vo.acquisizioni.PartecipantiGaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.RigheVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StatisticheVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaInventariOrdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.TitoloACQAreeIsbdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.TitoloACQVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.FormulaIntroOrdineRVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaQuater;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaQuinquies;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioListeVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.common.HibernateUtil;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_cambi_ufficiali;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_ordini;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_ordine_carrello_spedizione;
import it.iccu.sbn.polo.orm.amministrazione.Tb_codici;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.polo.orm.statistiche.Tb_statistiche;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.vo.custom.acquisizioni.ordini.OrdineCarrelloSpedizioneDecorator;
import it.iccu.sbn.vo.custom.bibliografica.AreaDatiServizioInterrTitoloCusVO;
import it.iccu.sbn.vo.validators.acquisizioni.Validazione;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.transaction.TransactionRolledbackException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.DataException;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;

public class GenericJDBCAcquisizioniDAO extends AcquisizioniBaseDAO {

	private static Codici codici;
	private static ServiziBibliografici servizi;
	private static Inventario inventario;
	static Logger log = Logger.getLogger(GenericJDBCAcquisizioniDAO.class);

	private AcquisizioniBMT acquisizioniBMT;

	static {
		try {
			DomainEJBFactory factory = DomainEJBFactory.getInstance();
			codici = factory.getCodici();
			servizi = factory.getSrvBibliografica();
			inventario = factory.getInventario();

		} catch (NamingException e) {

			log.error("", e);
		} catch (RemoteException e) {

			log.error("", e);
		} catch (CreateException e) {

			log.error("", e);
		}
	}

	private static final String trimAndQuote(String field, boolean nullable) {
		if (ValidazioneDati.isFilled(field)) {
			field = field.trim().replace("'","''");
			return "'" + field + "'";
		}
		return nullable ? null : " ";
	}

	private static final String trimAndQuote(String field) {
		return trimAndQuote(field, true);
	}


    public AcquisizioniBMT getAcquisizioniBMTBean() throws EJBException, ValidationException {

    	if (acquisizioniBMT != null)
    		return acquisizioniBMT;
    	try{
    		this.acquisizioniBMT = DomainEJBFactory.getInstance().getAcquisizioniBMT();
		} catch (NamingException e) {

			log.error("", e);
		} catch (CreateException e) {

			log.error("", e);
        } catch (DataException e) {
            throw new EJBException(e);
		} catch (ValidationException e) {

      	  throw e;

		} catch (ApplicationException e) {
            throw new EJBException(e);
        } catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
        }

		return acquisizioniBMT;
    }




	public List getTitolo(List listaBid, String ticket)
	throws ValidationException, DataException, ApplicationException  {

		boolean ret = false;
		//TitoloACQVO recTit = new TitoloACQVO();
		List lista = new ArrayList(); // output di un singolo titolo ricavato
		int codRet=0;
		if (listaBid!=null && listaBid.size()>0)
		{
			for (int i=0;  i <  listaBid.size(); i++)
			{
				TitoloACQVO recTit = new TitoloACQVO();
				String bid=(String)listaBid.get(i);
				try {
					AreaDatiServizioInterrTitoloCusVO risposta = servizi.servizioDatiTitoloPerBid(bid ,ticket);
					recTit.setBid(risposta.getBid());
					recTit.setIsbd(risposta.getTitoloResponsabilita());
					recTit.setNatura(risposta.getNatura());
					recTit.setArrCodLingua(risposta.getArrCodLingua());
					recTit.setCodPaese(risposta.getCodPaese());

					if (risposta.getCodErr().equals("0000")) {
						//trovato
						ret=true;
						//lista.add(recTit);
					}else{
						codRet = 1;
					}
				} catch (RemoteException e1) {

					e1.printStackTrace();
				} catch (DAOException e) {

					log.error("", e);
				}
				lista.add(recTit);
			}
		}
		return lista;

	}

	public List<StrutturaInventariOrdVO> getInvOrdRil(int IDOrd)
	throws ValidationException, DataException, ApplicationException  {
		Connection connectionSub = null;
		PreparedStatement pstmtSub = null;
		ResultSet rsSub = null;

		List<StrutturaInventariOrdVO> listaInv = null;
		try {
			// subquery per test di esistenza inventari: da sostituire con metodo ancora da scrivere in this
			String sqlSub="select inv.*,inventari.bid, TO_CHAR(inv.data_uscita,'dd/MM/yyyy') as data_uscita_str,TO_CHAR(inv.data_rientro,'dd/MM/yyyy') as data_rientro_str, TO_CHAR(inv.data_rientro_presunta,'dd/MM/yyyy') as data_rientro_presunta_str from tra_ordine_inventari inv  ";
			//sqlSub=sqlSub + " inv.fl_canc<>'S'";
			sqlSub=sqlSub + " join tbc_inventario inventari on inventari.cd_polo=inv.cd_polo and inventari.cd_bib=inv.cd_bib and inventari.cd_serie=inv.cd_serie and inventari.cd_inven=inv.cd_inven and inventari.fl_canc<>'S'";
			sqlSub=this.struttura(sqlSub);
			sqlSub=sqlSub + " inv.id_ordine='" + IDOrd + "'";
			//almaviva5_20121107 evolutive google
			sqlSub += " order by inv.posizione,inv.cd_serie,inv.cd_inven";
			connectionSub = getConnection();
			pstmtSub = connectionSub.prepareStatement(sqlSub);
			rsSub = pstmtSub.executeQuery();
	        int righ=0;
			while (rsSub.next()) {
				righ++;
				if (righ==1)
				{
					listaInv=new ArrayList();
				}
				StrutturaInventariOrdVO eleInv = new StrutturaInventariOrdVO();
				eleInv.setCodBibl(rsSub.getString("cd_bib"));
				eleInv.setCodPolo(rsSub.getString("cd_polo"));
				eleInv.setBid(rsSub.getString("bid"));
				eleInv.setIDOrd(IDOrd);
				eleInv.setSerie(rsSub.getString("cd_serie"));
				eleInv.setNumero(String.valueOf(rsSub.getInt("cd_inven")));
				eleInv.setDataRientro(rsSub.getString("data_rientro_str"));
				eleInv.setDataUscita(rsSub.getString("data_uscita_str"));
				eleInv.setDataRientroPresunta(rsSub.getString("data_rientro_presunta_str"));
				eleInv.setNote(rsSub.getString("ota_fornitore"));
				short pos = rsSub.getShort("posizione");
				eleInv.setPosizione((short) (ValidazioneDati.isFilled(pos) ? pos : righ));
				short vol = rsSub.getShort("volume");
				eleInv.setVolume((short) (ValidazioneDati.isFilled(vol) ? vol : righ));


				String isbd="";
				String bid="";
				String nStandard="";
				List<StrutturaTerna> nStandardArr=null;
				if (eleInv.getBid()!=null && eleInv.getBid().trim().length()!=0)
				{
				// solo per test

/*						bid=rs.getString("bid");
					isbd="titolo di test";
*/
					bid=eleInv.getBid();

					try {
						TitoloACQVO recTit=null;
						//recTit = this.getTitoloRox(rs.getString("bid"));
						Tba_cambi_ufficialiDao cambiDao= new Tba_cambi_ufficialiDao();
						//List risposta = cambiDao.getTitoloOrdine(rs.getString("bid"));
						//recTit= cambiDao.getTitoloOrdineBis(rs.getString("bid"));
						recTit= this.getTitoloOrdineTer(eleInv.getBid());
						if (recTit!=null && recTit.getIsbd()!=null) {
							isbd=recTit.getIsbd();
						}
						if (recTit!=null && recTit.getNumStandard()!=null) {
							nStandard=recTit.getNumStandard();
						}
						if (recTit!=null && recTit.getNumStandardArr()!=null && recTit.getNumStandardArr().size()>0) {
							nStandardArr=recTit.getNumStandardArr();
						}

					} catch (Exception e) {
						isbd="";
					}

				}
				eleInv.setTitolo(isbd);

				listaInv.add(eleInv);
			}
			rsSub.close();
			pstmtSub.close();
			connectionSub.close();
		} catch (Exception e) {

			log.error("", e);
		} finally {
			close(connectionSub);
		}

		return listaInv;


	}


	public TitoloACQVO getTitoloRox(String bidPassato)
	throws ValidationException, DataException, ApplicationException, DaoManagerException  {

		boolean ret = false;
		//TitoloACQVO recTit = new TitoloACQVO();
		List lista = new ArrayList(); // output di un singolo titolo ricavato
		String[] arrLingua=new String[3];
		TitoloACQVO recTit=null;
		int codRet=0;
		String tit_isbd="";
		if (bidPassato!=null )
		{
			try
			{
				Tba_cambi_ufficialiDao cambiDao= new Tba_cambi_ufficialiDao();
				String bid=bidPassato;
				//List risposta = cambiDao.getTitoloOrdine(bid);
				List risposta = cambiDao.getTitoloOrdine(bid);

				if (risposta!=null && risposta.size()>0)
				{
					recTit = new TitoloACQVO();

					Object[] arrivo =null;
					arrivo = (Object[]) risposta.get(0);
					Tb_titolo tit= (Tb_titolo) arrivo[0];
					// il numero standard serve per la stampa del buono d’ordine (campo isbn/issn)
					recTit.setNumStandard((String) arrivo[1]); //  numero standard

					//Tb_titolo tit= (Tb_titolo)risposta.get(0);

					tit_isbd=tit.getIsbd();
					recTit.setIsbd(tit.getIsbd());
					if  (!tit_isbd.equals("") &&  tit_isbd.length()>200)
					{
						// gestione composizione titolo
						String indice_isbd=tit.getIndice_isbd();

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
							//if (indice_isbd_composto.length>1)
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
							recTit.setIsbd(tit_isbd_finale);
						}
					}
					recTit.setNatura(String.valueOf(tit.getCd_natura()));
					if (tit!=null &&  tit.getCd_lingua_1() != null) {
							arrLingua[0] = tit.getCd_lingua_1().toString();
							if (tit.getCd_lingua_2() != null)
							{
								arrLingua[1] = tit.getCd_lingua_2().toString();
							}
							if (tit.getCd_lingua_3() != null)
							{
								arrLingua[2] = tit.getCd_lingua_3().toString();
							}
					}
					recTit.setArrCodLingua(arrLingua);
					recTit.setCodPaese(tit.getCd_paese());
				}
			} catch (Exception e) {

				log.error("", e);
			}
		}
		return recTit;
	}


	public TitoloACQAreeIsbdVO getAreeIsbdTitolo(String bidPassato) throws DaoManagerException {
		List results = null;
		TitoloACQAreeIsbdVO tit=null;
		Connection connectionTer = null;
		PreparedStatement pstmtTer = null;
		ResultSet rsInter = null;
		if (bidPassato==null || (bidPassato!=null && bidPassato.trim().length()==0))
		{
			return tit;
		}

		try {

			String sqlString="select t.bid, t.isbd, t.indice_isbd, 'base' as seq" ; //t.cd_natura,t.cd_paese,t.cd_lingua_1,t.cd_lingua_2,t.cd_lingua_3,n.tp_numero_std,  n.numero_std , tcod.ds_tabella " ;
			sqlString=sqlString + " from Tb_titolo t ";
			sqlString=sqlString +" where t.fl_canc<>'S'"; //ok

			if (bidPassato!=null)
			{
				//sqlString=sqlString +" where  n.b=t ";
				sqlString=sqlString +" and t.bid='"+ bidPassato +"'"; //ok
				sqlString=sqlString + " union ";
				sqlString=sqlString + " ( ";
				sqlString=sqlString + " select titc.bid, titc.isbd,  titc.indice_isbd, tt.sequenza as seq ";
				sqlString=sqlString + " from tr_tit_tit tt ";
				sqlString=sqlString + " inner join tb_titolo titc on titc.bid=tt.bid_coll ";
				sqlString=sqlString + " where tt.bid_base='" + bidPassato +"'" ;
				sqlString=sqlString + " and  tt.cd_natura_coll='C' and tt.tp_legame='01' and tt.fl_canc<>'S' ";
				sqlString=sqlString + " )";

			}

			//sqlString=sqlString + " order by  v.valuta";
			connectionTer = getConnection();

			pstmtTer = connectionTer.prepareStatement(sqlString);
			rsInter = pstmtTer.executeQuery();
			int numRighe=0;
			List arrivo =new ArrayList();
			String[] arrLingua=new String[3];
			results=new ArrayList();
			List<StrutturaTerna> componiNumStdArr=new ArrayList<StrutturaTerna>() ;
			StrutturaTerna componiNumStd=new StrutturaTerna();
			String collezione="";

			while (rsInter.next())
				{
				numRighe=numRighe+1;
				if (rsInter.getString("seq")!=null && rsInter.getString("seq").equals("base"))
				{
					tit=new TitoloACQAreeIsbdVO();
					tit.setBid(rsInter.getString("bid"));
					tit.setIsbd(rsInter.getString("isbd"));
					tit.setIndiceISBD(rsInter.getString("indice_isbd"));
				}
				else
				{
					//collezione="";
					String sequenza="";
					String area200Collana="";
					//
					String[] array_indice_isbd_collana=rsInter.getString("indice_isbd").split("\\;");

					if (array_indice_isbd_collana.length>0 )
					{
						for (int x = 0; x < array_indice_isbd_collana.length; x++)
						{
							String areaIntera=array_indice_isbd_collana[x];
							String[] array_areaIntera=areaIntera.split("-");
							String codiceArea=array_areaIntera[0];
							String startArea=array_areaIntera[1];
							int lunghezza=0;
							if ((x+1) < array_indice_isbd_collana.length)
							{
								String areaInteraSucc=array_indice_isbd_collana[x+1];
								String[] array_areaInteraSucc=areaInteraSucc.split("-");
								String codiceAreaSucc=array_areaInteraSucc[0];
								String startAreaSucc=array_areaInteraSucc[1];
								if (Integer.valueOf(startAreaSucc)>Integer.valueOf(startArea))
								{
									//lunghezza=Integer.valueOf(startAreaSucc)-Integer.valueOf(startArea);
									lunghezza=Integer.valueOf(startAreaSucc);
									if ((x+1) < array_indice_isbd_collana.length)
									{
										lunghezza=lunghezza-4; // si sottrae la lunghezza del separatore successivo
									}
								}
							}
							if (lunghezza>0)
							{
								String appo=rsInter.getString("isbd").substring(Integer.valueOf(startArea)-1,lunghezza-1);
								if(appo!=null && appo.trim().length()>0)
								{
									area200Collana=appo;
									area200Collana=area200Collana.replace("*", ""); // tck 2559
									break;
								}
							}
							else
							{
								area200Collana=rsInter.getString("isbd").substring(Integer.valueOf(startArea)-1);
								area200Collana=area200Collana.replace("*", ""); // tck 2559
							}
						}
					}
					//
					if (rsInter.getString("seq")!=null && rsInter.getString("seq").trim().length()>0)
					{
						sequenza=" ; " +rsInter.getString("seq").trim(); // tck 2559
					}
					if (!collezione.equals(""))
					{
						collezione=collezione + " ";  // vedi mail 07.07.09 Tra una collezione e l'altra solo lo spazio; es. . - (Piccola Biblioteca Einaudi ; 16) (Quaderni / CNR ; 22)

					}
					collezione=collezione + "(" + area200Collana + sequenza + ")";
				}
			}
			rsInter.close();
			connectionTer.close();
			tit.setCollezione(collezione);
			String[] array_indice_isbd_composto=tit.getIndiceISBD().split("\\;");
			boolean boolArea200Titolo=false;
			boolean boolArea205Edizione=false;
			boolean boolArea208Musica=false;
			boolean boolArea207Numerazione=false;
			boolean boolArea206DatiMat=false;
			boolean boolArea210Pubblicazione=false;
			boolean boolArea215DescrFisica=false;
			boolean boolArea300Note=false;

			if (array_indice_isbd_composto.length>0 )
			{
				for (int y = 0; y < array_indice_isbd_composto.length; y++)
				{
					String areaIntera=array_indice_isbd_composto[y];
					String[] array_areaIntera=areaIntera.split("-");
					String codiceArea=array_areaIntera[0];
					String startArea=array_areaIntera[1];
					int lunghezza=0;
					if ((y+1) < array_indice_isbd_composto.length)
					{
						String areaInteraSucc=array_indice_isbd_composto[y+1];
						String[] array_areaInteraSucc=areaInteraSucc.split("-");
						String codiceAreaSucc=array_areaInteraSucc[0];
						String startAreaSucc=array_areaInteraSucc[1];
						if (Integer.valueOf(startAreaSucc)>Integer.valueOf(startArea))
						{
							//lunghezza=Integer.valueOf(startAreaSucc)-Integer.valueOf(startArea);
							lunghezza=Integer.valueOf(startAreaSucc);
							if ((y+1) < array_indice_isbd_composto.length)
							{
								lunghezza=lunghezza-4; // si sottrae la lunghezza del separatore successivo
							}
						}
					}
					if (codiceArea.equals("200"))
					{
						boolArea200Titolo=true;
						if (lunghezza>0)
						{
							String appo=tit.getIsbd().substring(Integer.valueOf(startArea)-1,lunghezza-1);
							if(appo!=null && appo.trim().length()>0)
							{
								tit.setArea200Titolo(appo);
								String titol=tit.getArea200Titolo().replace("*", ""); // tck 2559
								tit.setArea200Titolo(titol);
							}
						}
						else
						{
							tit.setArea200Titolo(tit.getIsbd().substring(Integer.valueOf(startArea)-1));
							String titol=tit.getArea200Titolo().replace("*", ""); // tck 2559
							tit.setArea200Titolo(titol);
						}

					}

					if (codiceArea.equals("205"))
					{
						boolArea205Edizione=true;
						if (lunghezza>0)
						{
							String appo=tit.getIsbd().substring(Integer.valueOf(startArea)-1,lunghezza-1);
							if(appo!=null && appo.trim().length()>0)
							{
								tit.setArea205Edizione(appo);
							}
						}
						else
						{
							tit.setArea205Edizione(tit.getIsbd().substring(Integer.valueOf(startArea)-1));
						}

					}
					if (codiceArea.equals("208"))
					{
						boolArea208Musica=true;
						if (lunghezza>0)
						{
							String appo=tit.getIsbd().substring(Integer.valueOf(startArea)-1,lunghezza-1);
							if(appo!=null && appo.trim().length()>0)
							{
								tit.setArea208Musica(appo);
							}
						}
						else
						{
							tit.setArea208Musica(tit.getIsbd().substring(Integer.valueOf(startArea)-1));
						}

					}

					if (codiceArea.equals("206"))
					{
						boolArea206DatiMat=true;
						if (lunghezza>0)
						{
							String appo=tit.getIsbd().substring(Integer.valueOf(startArea)-1,lunghezza-1);
							if(appo!=null && appo.trim().length()>0)
							{
								tit.setArea206DatiMat(appo);
							}
						}
						else
						{
							tit.setArea206DatiMat(tit.getIsbd().substring(Integer.valueOf(startArea)-1));
						}

					}
					if (codiceArea.equals("207"))
					{
						boolArea207Numerazione=true;
						if (lunghezza>0)
						{
							String appo=tit.getIsbd().substring(Integer.valueOf(startArea)-1,lunghezza-1);
							if(appo!=null && appo.trim().length()>0)
							{
								tit.setArea207Numerazione(appo);
							}
						}
						else
						{
							tit.setArea207Numerazione(tit.getIsbd().substring(Integer.valueOf(startArea)-1));
						}

					}
					if (codiceArea.equals("210"))
					{
						boolArea210Pubblicazione=true;
						if (lunghezza>0)
						{
							String appo=tit.getIsbd().substring(Integer.valueOf(startArea)-1,lunghezza-1);
							if(appo!=null && appo.trim().length()>0)
							{
								tit.setArea210Pubblicazione(appo);
							}
						}
						else
						{
							tit.setArea210Pubblicazione(tit.getIsbd().substring(Integer.valueOf(startArea)-1));
						}

					}
					if (codiceArea.equals("215"))
					{
						boolArea215DescrFisica=true;
						if (lunghezza>0)
						{
							String appo=tit.getIsbd().substring(Integer.valueOf(startArea)-1,lunghezza-1);
							if(appo!=null && appo.trim().length()>0)
							{
								tit.setArea215DescrFisica(appo);
							}
						}
						else
						{
							tit.setArea215DescrFisica(tit.getIsbd().substring(Integer.valueOf(startArea)-1));
						}

					}
					if (codiceArea.equals("300"))
					{
						boolArea300Note=true;
						if (lunghezza>0)
						{
							String appo=tit.getIsbd().substring(Integer.valueOf(startArea)-1,lunghezza-1);
							if(appo!=null && appo.trim().length()>0)
							{
								tit.setArea300Note(appo);
							}
						}
						else
						{
							tit.setArea300Note(tit.getIsbd().substring(Integer.valueOf(startArea)-1));
						}

					}

				}
			}

		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connectionTer);
		}
		return tit;
	}




	public TitoloACQVO getTitoloOrdineTer(String bidPassato) throws DaoManagerException {
		List results = null;
		TitoloACQVO tit=null;
		Connection connectionTer = null;
		PreparedStatement pstmtTer = null;
		ResultSet rsInter = null;

		try {

			String sqlString="select t.bid, t.isbd, t.cd_natura,t.cd_paese,t.cd_lingua_1,t.cd_lingua_2,t.cd_lingua_3,t.indice_isbd,n.tp_numero_std,  n.numero_std , tcod.ds_tabella " ;
			sqlString=sqlString + " from Tb_titolo t ";
			sqlString=sqlString + " left join Tb_numero_std  n on  n.bid=t.bid and n.fl_canc<>'S' and n.tp_numero_std in ('I','J','M','L','E','G') "; //ok
			sqlString=sqlString + " left join tb_codici tcod on tcod.tp_tabella='NSTD' and  n.tp_numero_std=tcod.cd_tabella"  ; // ok la property b  è definita nel mapping
			sqlString=sqlString +" where t.fl_canc<>'S'"; //ok

			//bidPassato="BVE0036760"; // RAV0028330 CFI0232619 CFI0117938(ha 3 numeri standard)

			if (bidPassato!=null)
			{
				//sqlString=sqlString +" where  n.b=t ";
				sqlString=sqlString +" and t.bid='"+ bidPassato +"'"; //ok

			}
			//sqlString=sqlString + " order by  v.valuta";
			connectionTer = getConnection();

			pstmtTer = connectionTer.prepareStatement(sqlString);
			rsInter = pstmtTer.executeQuery();
			int numRighe=0;
			List arrivo =new ArrayList();
			String[] arrLingua=new String[3];
			results=new ArrayList();
			List<StrutturaTerna> componiNumStdArr=new ArrayList<StrutturaTerna>() ;
			StrutturaTerna componiNumStd=new StrutturaTerna();
			while (rsInter.next()) {
				numRighe=numRighe+1;
				TitoloACQVO arrivoUno=new TitoloACQVO();
				if (rsInter.getString("cd_lingua_1")!=null ) {
					arrLingua[0] =rsInter.getString("cd_lingua_1").toString();
					if (rsInter.getString("cd_lingua_2")!= null)
					{
						arrLingua[1] = rsInter.getString("cd_lingua_2").toString();
					}
					if (rsInter.getString("cd_lingua_3") != null)
					{
						arrLingua[2] = rsInter.getString("cd_lingua_3").toString();
					}
				}
				arrivoUno.setIsbd(rsInter.getString("isbd"));
				arrivoUno.setIndiceISBD(rsInter.getString("indice_isbd"));
				arrivoUno.setArrCodLingua(arrLingua);
				arrivoUno.setNatura(rsInter.getString("cd_natura"));
				arrivoUno.setCodPaese("");
				if (rsInter.getString("cd_paese")!=null)
				{
					arrivoUno.setCodPaese(rsInter.getString("cd_paese"));
				}
				arrivoUno.setBid(rsInter.getString("bid"));
				arrivoUno.setNumStandard("");
				if (rsInter.getString("numero_std")!=null && rsInter.getString("tp_numero_std")!=null && rsInter.getString("tp_numero_std").length()>0)
				{
					arrivoUno.setNumStandard(rsInter.getString("tp_numero_std") + rsInter.getString("numero_std"));
					//compongo il numero standard
					componiNumStd=new StrutturaTerna("","","");
					if (rsInter.getString("tp_numero_std")!=null)
					{
						componiNumStd.setCodice1(rsInter.getString("tp_numero_std").trim());
					}
					if (rsInter.getString("numero_std")!=null)
					{
						componiNumStd.setCodice2(rsInter.getString("numero_std").trim());
					}
					if (rsInter.getString("ds_tabella")!=null)
					{
						componiNumStd.setCodice3(rsInter.getString("ds_tabella").trim());
					}
					componiNumStdArr.add(componiNumStd);
				}
				//arrivoUno.setNumStandardArr(componiNumStdArr);
				results.add(arrivoUno);
			}
			rsInter.close();
			connectionTer.close();
			// la molteplicità dei numeri standard va ridotta au un unico elemento con array dei numeristandard
			if (results!=null && results.size()>0)
			{
				List results2=new ArrayList();
/*				if (componiNumStd!=null && componiNumStd.length()>0)
				{
					((TitoloACQVO)results.get(0)).setNumStandard(componiNumStd);
				}
*/				if (componiNumStdArr!=null && componiNumStdArr.size()>0)
				{
					((TitoloACQVO)results.get(0)).setNumStandardArr(componiNumStdArr);
				}
				results2.add(results.get(0)); // considero solo il primo e compongo il numero standard

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
				log.error("", e);
			} finally {
				close(connectionTer);
			}
			return tit;
	}



	public StrutturaTerna gestioneRinnovato(int idOrdRinn, String primoCod, String primoAnno, String gerarchia) throws DataException, ApplicationException, ValidationException // P=precedente, O=originario, N=nessuno,  S=successivo
	{
		StrutturaTerna  ordineRinnovato=null;
		Connection connection = null;
		try {

			PreparedStatement pstmt = null;
			ResultSet rs = null;
			ordineRinnovato=new StrutturaTerna("", "","");	// tipo, anno, cod

			if (!gerarchia.equals("N"))
			{
				connection = getConnection();
				// riceve un id di un ordine che se rinnovato restituisce i campi fondamentali dell'ordine precedente o originario  o successivo
				String sql=" select ord.id_ordine, ord.cod_tip_ord, ord.anno_ord, ord.cod_ord , ord.anno_1ord, ord.cod_1ord , ord.rinnovato";
				sql +=" from  tba_ordini ord ";
				// sql +=" and ord.id_ordine= " + idOrdRinn  ;
				sql +=" where  ord.fl_canc<>'S' ";
				sql +=" and ord.cod_1ord= " + Integer.valueOf(primoCod)  ;
				sql +=" and ord.anno_1ord= " + Integer.valueOf(primoAnno)  ;
				sql +=" and ord.continuativo='1' ";
				sql +=" order by ord.ts_ins, ord.id_ordine ";
				pstmt = connection.prepareStatement(sql);
				rs = pstmt.executeQuery();
				int numRighe=0;
				String tipoOrdAppo="";
				String annoOrdAppo="";
				String codOrdAppo="";
				String anno1OrdAppo="";
				String cod1OrdAppo="";
				Boolean rinnAppo=false;
				int numRigheTot=0;

				while (rs.next())
				{
					numRighe=numRighe+1;
					// si considera solo il primo record
					rinnAppo=rs.getBoolean("rinnovato");

					if (numRighe==1)
					{
						tipoOrdAppo=rs.getString("cod_tip_ord");
						anno1OrdAppo=String.valueOf(rs.getInt("anno_1ord"));
						cod1OrdAppo=String.valueOf(rs.getInt("cod_1ord"));
					}

					if (gerarchia.equals("P"))
					{
						if (rs.getInt("id_ordine")==idOrdRinn)
						{
							break;
						}
					}

					if (gerarchia.equals("S")) // successivo
					{
						if (rs.getInt("id_ordine")==idOrdRinn)
						{
							numRigheTot=numRighe+1;
						}
					}

					annoOrdAppo=String.valueOf(rs.getInt("anno_ord"));
					codOrdAppo=String.valueOf(rs.getInt("cod_ord"));

					if (gerarchia.equals("S")) // successivo
					{
						if (numRighe==numRigheTot)
						{
							break;
						}
					}
				} // End while
				rs.close();
				pstmt.close();
				connection.close();

				if (gerarchia.equals("P"))
				{
					ordineRinnovato.setCodice1(tipoOrdAppo);
					ordineRinnovato.setCodice2(annoOrdAppo);
					ordineRinnovato.setCodice3(codOrdAppo);
				}
				if (gerarchia.equals("S"))
				{
					ordineRinnovato.setCodice1(tipoOrdAppo);
					ordineRinnovato.setCodice2(annoOrdAppo);
					ordineRinnovato.setCodice3(codOrdAppo);
				}

				if (gerarchia.equals("O"))
				{
					ordineRinnovato.setCodice1(tipoOrdAppo);
					ordineRinnovato.setCodice2(anno1OrdAppo);
					ordineRinnovato.setCodice3(cod1OrdAppo);
				}
				if ( numRighe==1 &&  gerarchia.equals("P") ) // c'è stato un unico rinnovo il precedente coincide con l'origine
				{
					if (anno1OrdAppo!=null && anno1OrdAppo.trim().length()>0 && cod1OrdAppo!=null && cod1OrdAppo.trim().length()>0)
					{
						ordineRinnovato.setCodice1(tipoOrdAppo);
						ordineRinnovato.setCodice2(anno1OrdAppo);
						ordineRinnovato.setCodice3(cod1OrdAppo);
					}
				}

				if ( numRighe<numRigheTot &&  gerarchia.equals("S") && !rinnAppo ) // è l'ultimo
				{
						ordineRinnovato.setCodice1("");
						ordineRinnovato.setCodice2("");
						ordineRinnovato.setCodice3("");
				}

			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}
		return ordineRinnovato;
	}

	public List<ChiaveOrdine> costruisciCatenaRinnovati(StrutturaTerna ordRinn, String codBibl) throws DataException, ApplicationException, ValidationException // P=precedente, O=originario, N=nessuno,  S=successivo
	{

		List<ChiaveOrdine> arrayCatenaOrdiniRinnovati=null;
		Connection connection = null;
		try {
			PreparedStatement pstmt = null;
			ResultSet rs = null;


			connection = getConnection();
			// riceve un ordine che se rinnovato restituisce i campi fondamentali dell'ordine originario
			String sql="select ord.id_ordine, ord.cod_tip_ord, ord.anno_ord, ord.cod_ord ,ord.anno_abb, ord.anno_1ord, ord.cod_1ord , ord.rinnovato, ord.cd_bib";
			sql +=" from  tba_ordini ord ";
			// sql +=" and ord.id_ordine= " + idOrdRinn  ;
			sql +=" where  ord.fl_canc<>'S' ";
			sql += "   and ord.cd_bib ='"+ codBibl + "'";
			sql += "   and ord.cd_polo    = (select cd_polo from tbf_polo)";
			sql += "   and ((ord.anno_ord =" + Integer.valueOf(ordRinn.getCodice2()) + " and ord.cod_ord =" +Integer.valueOf(ordRinn.getCodice3()) + ") or ";
			sql += "   (ord.anno_1ord="+Integer.valueOf(ordRinn.getCodice2())+ " and ord.cod_1ord="+ Integer.valueOf(ordRinn.getCodice3())+"))";
			sql += "   and ord.cod_tip_ord ='" + ordRinn.getCodice1() + "'";
			sql += "   and ord.fl_canc<>'S'";
			sql += "  and ord.stato_ordine <> 'N'";
			sql +=" order by ord.ts_ins, ord.id_ordine";
			pstmt = connection.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int numRighe=0;
			String tipoOrdAppo="";
			String annoOrdAppo="";
			String codOrdAppo="";
			String anno1OrdAppo="";
			String cod1OrdAppo="";

			Boolean rinnAppo=false;
			int numRigheTot=0;
			String annoAbbAppo="";
			String idAppo="";
			arrayCatenaOrdiniRinnovati = new ArrayList<ChiaveOrdine>();
			StrutturaTerna primoOrdine=new StrutturaTerna("", "","");	// tipo, anno, cod
			while (rs.next())
			{
				ChiaveOrdine eleCatenaOrdineRinnovato = new ChiaveOrdine();	// tipo, anno, cod , anno_abb, id_ordine
				numRighe=numRighe+1;
				tipoOrdAppo=rs.getString("cod_tip_ord");
				annoOrdAppo=String.valueOf(rs.getInt("anno_ord"));
				codOrdAppo=String.valueOf(rs.getInt("cod_ord"));
				annoAbbAppo=String.valueOf(rs.getInt("anno_abb"));
				anno1OrdAppo=String.valueOf(rs.getInt("anno_1ord"));
				cod1OrdAppo=String.valueOf(rs.getInt("cod_1ord"));
				idAppo=String.valueOf(rs.getInt("id_ordine"));
				if (tipoOrdAppo!=null && tipoOrdAppo.trim().length()>0 && anno1OrdAppo!=null && anno1OrdAppo.trim().length()>0 && !anno1OrdAppo.trim().equals("0") && cod1OrdAppo!=null && cod1OrdAppo.trim().length()>0 && !cod1OrdAppo.trim().equals("0"))
				{
					primoOrdine.setCodice1(tipoOrdAppo);
					primoOrdine.setCodice2(anno1OrdAppo);
					primoOrdine.setCodice3(cod1OrdAppo);
				}
				eleCatenaOrdineRinnovato.tipo=(tipoOrdAppo);
				eleCatenaOrdineRinnovato.anno=(annoOrdAppo);
				eleCatenaOrdineRinnovato.cod=(codOrdAppo);
				eleCatenaOrdineRinnovato.anno_abb=(annoAbbAppo);
				eleCatenaOrdineRinnovato.id_ordine=(idAppo);
				eleCatenaOrdineRinnovato.bib=(rs.getString("cd_bib"));
				//arrayCatenaOrdiniRinnovati.add(eleCatenaOrdineRinnovato);
			} // End while
			rs.close();
			pstmt.close();
			connection.close();
			if (tipoOrdAppo!=null && tipoOrdAppo.trim().length()>0 && anno1OrdAppo!=null && anno1OrdAppo.trim().length()>0 && !anno1OrdAppo.trim().equals("0") && cod1OrdAppo!=null && cod1OrdAppo.trim().length()>0 && !cod1OrdAppo.trim().equals("0"))
			{
				// ricostruzione della catena dei rinnovi
				ChiaveOrdine parCatena = new ChiaveOrdine(); // tipo, anno, cod  anno_abb, id_ordine
				parCatena.tipo=(primoOrdine.getCodice1());
				parCatena.anno=(primoOrdine.getCodice2());
				parCatena.cod=(primoOrdine.getCodice3());
				//almaviva5_20151221
				parCatena.bib=(codBibl);
				//parCatena.setCodice4("");
				//List<StrutturaQuinquies> catenaRinnovi=null; // tipo, anno, cod, anno_abb, id_ordine
				try {
					arrayCatenaOrdiniRinnovati = this.catenaRinnovi(0, parCatena);
				} catch (Exception e) {

			 		log.error("", e);
			 	}
			}

		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}
		return arrayCatenaOrdiniRinnovati;
	}




	public List getInventariRox( String codPolo,String codBib,String codBibO,String codTipOrd,int annoOrd,int codOrd,String codBibF,String ticket,int nRec )
	throws ValidationException, DataException, ApplicationException , TransactionRolledbackException, Exception{

		List listaInv;
		listaInv=new ArrayList();

		try
		{
			Session session = HibernateUtil.getSession();
			//listaInv= inventario.getListaInventariOrdine(codPolo,  codBib,  codBibO, codTipOrd,  annoOrd,  codOrd,  codBibF,request.getLocale(), ticket,  nRec);

		}	catch (DataException  de) {

			// l'errore capita in questo punto
			//rb.printStackTrace();
			throw new Exception(de);

		}	catch (TransactionRolledbackException  rb) {
			HibernateUtil.closeSession();


			// l'errore capita in questo punto
			//rb.printStackTrace();
			throw new ValidationException (rb);

		}	catch (Exception e) {

			// l'errore capita in questo punto
			//log.error("", e);
			HibernateUtil.closeSession();

			throw e ;

		}
		return listaInv;

	}






	public List<FatturaVO> getRicercaListaFatture(ListaSuppFatturaVO ricercaFatture) throws ResourceNotFoundException, ApplicationException, ValidationException
	{
		Validazione.ValidaListaSuppFatturaVO (ricercaFatture);

		List<FatturaVO> listaFatture = new ArrayList<FatturaVO>();

		int ret = 0;
        // execute the search here
    		FatturaVO rec = null;
    		StrutturaFatturaVO recDett = null;

    		Connection connection = null;
			PreparedStatement pstmtCount = null;
			ResultSet rsCount = null;

    		PreparedStatement pstmt = null;
			PreparedStatement pstmt0 = null;

			ResultSet rs = null;
			ResultSet rs0 = null;
			PreparedStatement pstmtFatt = null;
			ResultSet rsFatt = null;

			try {
				// configurazione
				ConfigurazioneORDVO configurazioneORD=new ConfigurazioneORDVO();
				configurazioneORD.setCodBibl(ricercaFatture.getCodBibl());
				configurazioneORD.setCodPolo(ricercaFatture.getCodPolo());
				ConfigurazioneORDVO configurazioneORDLetta=new ConfigurazioneORDVO();
				Boolean gestBil=true;

				try {
					configurazioneORDLetta=this.loadConfigurazioneOrdini(configurazioneORD);
					if (configurazioneORDLetta!=null && !configurazioneORDLetta.isGestioneBilancio())
					{
						gestBil=false;
					}

				} catch (Exception e) {

					// l'errore capita in questo punto
					log.error("", e);
				}



				// contiene i criteri di ricerca
				connection = getConnection();

				String sql="";

				if (ricercaFatture.getIDFattNC()>0)
				{
					sql="select distinct";

				}
				else
				{
					sql="select ";

				}

				sql +=" fatt.*,fatt.ts_var as dataUpd, forn.nom_fornitore,lower(forn.nom_fornitore),forn.*,cambi.cambio, TO_CHAR(fatt.data_fattura,'dd/MM/yyyy') as data_fattura_str, TO_CHAR(fatt.data_reg,'dd/MM/yyyy') as data_reg_str, bibliot.nom_biblioteca ";
				sql +=" from  tba_fatture fatt ";
				sql +=" join tba_cambi_ufficiali cambi on cambi.cd_polo=fatt.cd_polo and cambi.cd_bib=fatt.cd_bib  and cambi.valuta=fatt.valuta  and cambi.fl_canc<>'S'  ";
				sql +=" join tbr_fornitori forn on forn.cod_fornitore=fatt.cod_fornitore and forn.fl_canc<>'S'";
				sql +=" join tbf_biblioteca bibliot on bibliot.cd_bib=fatt.cd_bib and bibliot.cd_polo=fatt.cd_polo and bibliot.fl_canc<>'S' ";


				if (ricercaFatture.getIDFattNC()>0)
				{
					sql +=" join tba_righe_fatture fattDett on fattDett.id_fattura=fatt.id_fattura AND fattDett.fl_canc<>'S' ";
				}

				sql=this.struttura(sql);
				sql +=" fatt.fl_canc<>'S'";

				if (ricercaFatture.getCodPolo() !=null &&  ricercaFatture.getCodPolo().length()!=0)
				{
					sql=this.struttura(sql);
					sql=sql  + " fatt.cd_polo='" + ricercaFatture.getCodPolo() +"'";
				}

				if (ricercaFatture.getCodBibl() !=null &&  ricercaFatture.getCodBibl().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" fatt.cd_bib='" + ricercaFatture.getCodBibl() +"'";
				}

				if (ricercaFatture.getAnnoFattura()!=null && ricercaFatture.getAnnoFattura().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" fatt.anno_fattura=" +ricercaFatture.getAnnoFattura() ;
				}

				if (ricercaFatture.getProgrFattura()!=null && ricercaFatture.getProgrFattura().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" fatt.progr_fattura=" + ricercaFatture.getProgrFattura() ;
				}

				if (ricercaFatture.getDataFattura()!=null && ricercaFatture.getDataFattura().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" fatt.data_fattura = TO_DATE('" +  ricercaFatture.getDataFattura() + "','dd/MM/yyyy')";
				}


				if (ricercaFatture.getDataFatturaDa()!=null && ricercaFatture.getDataFatturaDa().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" fatt.data_fattura >= TO_DATE('" +  ricercaFatture.getDataFatturaDa() + "','dd/MM/yyyy')";
				}

				if (ricercaFatture.getDataFatturaA()!=null && ricercaFatture.getDataFatturaA().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" fatt.data_fattura  <= TO_DATE ('" +  ricercaFatture.getDataFatturaA() + "','dd/MM/yyyy')";
				}
				if (ricercaFatture.getStatoFattura()!=null && ricercaFatture.getStatoFattura().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" fatt.stato_fattura='" + ricercaFatture.getStatoFattura()+"'" ;
				}
				if (ricercaFatture.getTipoFattura()!=null && ricercaFatture.getTipoFattura().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" fatt.tipo_fattura='" + ricercaFatture.getTipoFattura()+"'" ;
				}

				if (ricercaFatture.getFornitore()!=null && ricercaFatture.getFornitore().getCodice()!=null &&ricercaFatture.getFornitore().getCodice().length()!=0)
				{
					sql=this.struttura(sql);
					sql +="fatt.cod_fornitore =" + ricercaFatture.getFornitore().getCodice();
				}

				if (ricercaFatture.getNumFattura()!=null && ricercaFatture.getNumFattura().length()!=0)
				{
					sql=this.struttura(sql);
					sql=sql  + " fatt.num_fattura='" + ricercaFatture.getNumFattura()  +"'" ;
				}
				if (ricercaFatture.getIDFatt()>0 )
				{
					sql=this.struttura(sql);
					sql +=" fatt.id_fattura=" + ricercaFatture.getIDFatt() ;
				}
				if (ricercaFatture.getIDFattNC()>0 )
				{
					sql=this.struttura(sql);
					sql +=" fattDett.id_fattura_in_credito=" + ricercaFatture.getIDFattNC() ;
				}

				// creazione query per calcolo dei risultati con esclusione dell'order by
				String sqlXCount="";
				int pos=sql.lastIndexOf("from");
				int totFatt=0;
				if (pos>0)
				{
					sqlXCount="select count(*) as tot " +sql.substring(pos);
					pstmtCount = connection.prepareStatement(sqlXCount);
					rsCount = pstmtCount.executeQuery();
					while (rsCount.next()) {
						totFatt=rsCount.getInt("tot");
					}
					rsCount.close();
					pstmtCount.close();

				}

				// ordinamento passato
				if (ricercaFatture.getOrdinamento()==null || ( ricercaFatture.getOrdinamento()!=null && ricercaFatture.getOrdinamento().equals("")))
				{
					sql +=" order by fatt.cd_bib, fatt.anno_fattura, fatt.progr_fattura  ";
				}
				else if (ricercaFatture.getOrdinamento().equals("1"))
				{
					sql +=" order by fatt.cd_bib, fatt.anno_fattura, fatt.num_fattura ";
				}
				else if (ricercaFatture.getOrdinamento().equals("2"))
				{
					sql +=" order by fatt.cd_bib, fatt.data_fattura desc  ";
				}
				else if (ricercaFatture.getOrdinamento().equals("3"))
				{
					sql +=" order by fatt.cd_bib, fatt.stato_fattura ";
				}
				else if (ricercaFatture.getOrdinamento().equals("4"))
				{
					sql +=" order by fatt.cd_bib, lower(forn.nom_fornitore) ";
				}

				pstmt = connection.prepareStatement(sql);
				//log.debug("Debug: lettura fatture");
				//log.debug("Debug: " + sql);

				rs = pstmt.executeQuery();
				int numRighe=0;
				int progrRighe=1; // inizializzato e si incrementa solo quando si fa add all'array

				while (rs.next()) {
					//TO_CHAR(bil.budget,'999999999999999.99')
					numRighe=numRighe+1;
					rec = new FatturaVO();
					// configurazione
					rec.setGestBil(true);
					if (!gestBil)
					{
						rec.setGestBil(false);
					}
					rec.setIDFatt(rs.getInt("id_fattura"));
					rec.setCodPolo(rs.getString("cd_polo"));
					rec.setCodBibl(rs.getString("cd_bib"));
					rec.setDenoBibl(rs.getString("nom_biblioteca"));
					//rec.setProgressivo(numRighe);
					rec.setProgressivo(progrRighe);
					rec.setAnnoFattura(rs.getString("anno_fattura"));
					rec.setProgrFattura(String.valueOf(rs.getInt("progr_fattura")));
					rec.setNumFattura(rs.getString("num_fattura"));
					rec.setDataFattura(rs.getString("data_fattura_str"));
					rec.setDataRegFattura(rs.getString("data_reg_str"));
					rec.setImportoFattura(rs.getDouble("importo"));
					rec.setValutaFattura(rs.getString("valuta"));
					rec.setCambioFattura(rs.getDouble("cambio"));
					rec.setStatoFattura(rs.getString("stato_fattura"));
					rec.setDenoStatoFattura("");
					if (rec.getStatoFattura()!=null && rec.getStatoFattura().trim().length()>0)
					{
						if ( rec.getStatoFattura().trim().equals("1"))
						{
							rec.setDenoStatoFattura("Registrata");
						}
						else if (rec.getStatoFattura().trim().equals("2"))
						{
							rec.setDenoStatoFattura("Controllata");
						}
						else if (rec.getStatoFattura().trim().equals("3"))
						{
							rec.setDenoStatoFattura("Ordine di pagamento emesso");
						}
						else if (rec.getStatoFattura().trim().equals("4"))
						{
							rec.setDenoStatoFattura("Contabilizzata");
						}
					}

					rec.setTipoFattura(rs.getString("tipo_fattura"));
					rec.setFornitoreFattura(new StrutturaCombo("",""));
					rec.getFornitoreFattura().setCodice(String.valueOf(rs.getInt("cod_fornitore")));
					rec.getFornitoreFattura().setDescrizione(rs.getString("nom_fornitore"));

					// dati fornitore per stampe
					rec.setAnagFornitore(new FornitoreVO());
					rec.getAnagFornitore().setCodFornitore(String.valueOf(rs.getInt("cod_fornitore")));
					rec.getAnagFornitore().setNomeFornitore(rs.getString("nom_fornitore"));
					if (rs.getString("indirizzo")!=null && rs.getString("indirizzo").trim().length()>0 )
					{
						rec.getAnagFornitore().setIndirizzo(rs.getString("indirizzo"));
					}
					if (rs.getString("citta")!=null && rs.getString("citta").trim().length()>0 )
					{
						rec.getAnagFornitore().setCitta(rs.getString("citta"));
					}
					if (rs.getString("cap")!=null && rs.getString("cap").trim().length()>0 )
					{
						rec.getAnagFornitore().setCap(rs.getString("cap"));
					}
					if (rs.getString("paese")!=null && rs.getString("paese").trim().length()>0 )
					{
						rec.getAnagFornitore().setPaese(rs.getString("paese"));
					}

					if (rs.getString("p_iva")!=null && rs.getString("p_iva").trim().length()>0 )
					{
						rec.getAnagFornitore().setPartitaIva(rs.getString("p_iva"));
					}
					if (rs.getString("cod_fiscale")!=null && rs.getString("cod_fiscale").trim().length()>0 )
					{
						rec.getAnagFornitore().setCodiceFiscale(rs.getString("cod_fiscale"));
					}
					if (rs.getString("fax")!=null && rs.getString("fax").trim().length()>0 )
					{
						rec.getAnagFornitore().setFax(rs.getString("fax"));
					}
					if (rs.getString("telefono")!=null && rs.getString("telefono").trim().length()>0 )
					{
						rec.getAnagFornitore().setTelefono(rs.getString("telefono"));
					}
					rec.getAnagFornitore().setFornitoreBibl(new DatiFornitoreVO());


					rec.setScontoFattura(rs.getDouble("sconto"));
					rec.setDataUpd(rs.getTimestamp("dataUpd"));

					String sql0="select fatt.*,capBil.esercizio,capBil.capitolo,  TO_CHAR(fatt.data_fattura,'dd/MM/yyyy') as data_fattura_str, TO_CHAR(fatt.data_reg,'dd/MM/yyyy') as data_reg_str   ";
					sql0=sql0 + ", fattDett.*,fattDett.id_capitoli_bilanci as id_cap_bil_fattDett, ord.*, trim(TO_CHAR(importo,'999999999999999.99'))  as impFattDett  ";
					sql0=sql0 + " from  tba_fatture fatt ";
					sql0=sql0 + " join tba_righe_fatture fattDett on fattDett.id_fattura=fatt.id_fattura AND fattDett.fl_canc<>'S' ";
					sql0=sql0 + " left join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=fattDett.id_capitoli_bilanci and capBil.fl_canc<>'S' ";
					sql0=sql0 + " left join tba_ordini ord on ord.cd_polo=fattDett.cd_polo and ord.cd_bib=fattDett.cd_biblioteca and ord.fl_canc<>'S' and ord.stato_ordine <> 'N'";
					sql0=sql0 + " and ord.id_ordine=fattDett.id_ordine  ";

					sql0=this.struttura(sql0);
					sql0=sql0 + " fatt.fl_canc<>'S' ";


					if (rec.getIDFatt()!=0)
					{
						sql0=this.struttura(sql0);
						sql0=sql0 + " fatt.id_fattura=" + rec.getIDFatt() ;
					}


					if (ricercaFatture.isRicercaOrd())
					{
						if (ricercaFatture.getOrdine()!=null && ricercaFatture.getOrdine().getCodice1()!=null && ricercaFatture.getOrdine().getCodice1().length()!=0)
						{
							sql0=this.struttura(sql0);
							sql0=sql0 + "ord.cod_tip_ord ='" + ricercaFatture.getOrdine().getCodice1()+"'" ;
						}
						if (ricercaFatture.getOrdine()!=null && ricercaFatture.getOrdine().getCodice2()!=null && ricercaFatture.getOrdine().getCodice2().length()!=0)
						{
							sql0=this.struttura(sql0);
							sql0=sql0 + "ord.anno_ord =" + ricercaFatture.getOrdine().getCodice2();
						}
						if (ricercaFatture.getOrdine()!=null && ricercaFatture.getOrdine().getCodice3()!=null && ricercaFatture.getOrdine().getCodice3().length()!=0)
						{
							sql0=this.struttura(sql0);
							sql0=sql0 + "ord.cod_ord =" + ricercaFatture.getOrdine().getCodice3();
						}
					}
					sql0=sql0 + " order by fattDett.riga_fattura ";

					pstmt0 = connection.prepareStatement(sql0);
					rs0 = pstmt0.executeQuery();
					List<StrutturaFatturaVO> listaFattureDett = new ArrayList<StrutturaFatturaVO>();
					int indx=0;
					while (rs0.next()) {
						indx=indx+1;
						recDett = new StrutturaFatturaVO();
						//impFattDett
						recDett.setIDFatt(rec.getIDFatt());
						recDett.setCodBibl(rec.getCodBibl());
						recDett.setCodPolo(rec.getCodPolo());
						recDett.setProgrRigaFattura(indx); //01.02.10
						recDett.setRigaFattura(rs0.getInt("riga_fattura"));


						recDett.setIDOrd(rs0.getInt("id_ordine"));
						recDett.setIDBil(rs0.getInt("id_cap_bil_fattDett"));


						recDett.setImportoRigaFattura(rs0.getDouble("importo_riga"));
						recDett.setSconto1RigaFattura(rs0.getDouble("sconto_1"));
						recDett.setSconto2RigaFattura(rs0.getDouble("sconto_2"));
						recDett.setCodIvaRigaFattura(rs0.getString("cod_iva"));


						String isbd="";
						String bid="";

						// solo per la prima fattura dell'elenco ricavo il titolo (mi occorre solo in esamina e non sulla sintetica)
						if (numRighe==1 && totFatt==1)
						{
							if (rs0.getString("bid")!=null && rs0.getString("bid").trim().length()!=0)
							{
								bid=rs0.getString("bid");

								try {
									TitoloACQVO recTit=null;
									recTit = this.getTitoloOrdineTer(rs0.getString("bid").trim());
									if (recTit!=null && recTit.getIsbd()!=null) {
										bid=rs0.getString("bid").trim();
										isbd=recTit.getIsbd();
									}
									if (recTit==null) {
										isbd=" ";
										bid=rs0.getString("bid");
									}
								} catch (Exception e) {
										isbd=" ";
										bid=rs0.getString("bid");
								}
							}
						}
						//

						recDett.setTitOrdine(isbd);
						recDett.setBidOrdine(bid);
						recDett.setInvRigaFatt("");

						// recupero inventario associato alla riga di fattura
						if (numRighe==1 && totFatt==1 && !ricercaFatture.getProvenienza().equals("registroIngressoBatch"))
						{
							InventarioListeVO listaInv=null;
							try
							{
						         String codPolo=rec.getCodPolo();
						         String codBib=rec.getCodBibl();
						         String codBibO=rec.getCodBibl();
						         String codTipOrd=rs0.getString("cod_tip_ord");
						         int annoOrd=rs0.getInt("anno_ord");
						         int codOrd=rs0.getInt("cod_ord");
						         String codBibF=rec.getCodBibl();
						         int annoF=Integer.valueOf(rec.getAnnoFattura());
						         int progF=Integer.valueOf(rec.getProgrFattura());
						         int rigaF=recDett.getRigaFattura();
						         //Locale locale=ricercaFatture.;
						         Locale locale=Locale.getDefault(); // aggiornare con quella locale se necessario
						         String ticket=ricercaFatture.getTicket();
						         //nRec =;

								listaInv= inventario.getInventarioRigaFattura(codPolo, codBib,codBibO, codTipOrd,  annoOrd,  codOrd,codBibF, annoF, progF,  rigaF, locale, ticket);
								if (listaInv!=null)
								{
									InventarioListeVO ele=listaInv;
									recDett.setCodSerieRigaFatt(ele.getCodSerie());
									recDett.setInvRigaFatt(String.valueOf(ele.getCodInvent()));
								}else{//almaviva2 bug 0004455//in mancanza di inventari non produce la stampa
									InventarioListeVO ele=listaInv;
									recDett.setCodSerieRigaFatt("");
									recDett.setInvRigaFatt(String.valueOf(""));
								}
							} catch (Exception e) {

								// l'errore capita in questo punto
								log.error("", e);
							}

						}


						recDett.setOrdine(new StrutturaTerna("","",""));
						if (recDett.getIDOrd()>0)
						{
							recDett.getOrdine().setCodice1(rs0.getString("cod_tip_ord"));
							recDett.getOrdine().setCodice2(String.valueOf(rs0.getInt("anno_ord")));
							recDett.getOrdine().setCodice3(String.valueOf(rs0.getInt("cod_ord")));
							recDett.setPrezzoOrdine(rs0.getDouble("prezzo_lire"));
						}


						recDett.setBilancio(new StrutturaTerna("","",""));
						if (recDett.getIDBil()>0)
						{
							recDett.getBilancio().setCodice1(String.valueOf(rs0.getInt("esercizio")));
							recDett.getBilancio().setCodice2(String.valueOf(rs0.getInt("capitolo")));
							recDett.getBilancio().setCodice3(rs0.getString("cod_mat"));
							// per gestire la visualizzazione della riga altre spese nella jsp
							if (recDett.getBilancio().getCodice3().trim().equals("4"))
							{
								recDett.setCodPolo("*");

							}

						}

						recDett.setNoteRigaFattura(rs0.getString("note"));
						recDett.setFattura(new StrutturaTerna("","",""));

						// gestione note di credito
						if (rs0.getString("tipo_fattura").equals("N") && rs0.getInt("id_fattura_in_credito")>0)
						{
							String sqlfatt="select fatt.* ";
							sqlfatt=sqlfatt + " from  tba_fatture fatt ";
							sqlfatt=this.struttura(sqlfatt);
							sqlfatt=sqlfatt + " fatt.fl_canc<>'S' ";
							sqlfatt=this.struttura(sqlfatt);
							sqlfatt=sqlfatt + " fatt.id_fattura =" + rs0.getInt("id_fattura_in_credito");
							pstmtFatt = connection.prepareStatement(sqlfatt);
							rsFatt = pstmtFatt.executeQuery();
							while (rsFatt.next()) {

								if (rsFatt.getInt("anno_fattura")>0 )
								{
									recDett.getFattura().setCodice1(String.valueOf(rsFatt.getInt("anno_fattura")));
								}
								if (rsFatt.getInt("progr_fattura")>0 )
								{
									recDett.getFattura().setCodice2(String.valueOf(rsFatt.getInt("progr_fattura")));
								}
								if (rsFatt.getInt("id_fattura")>0)
								{
									recDett.setIDFattNC(rsFatt.getInt("id_fattura"));
								}

							}
							if (rs0.getInt("riga_fattura_in_credito")>0)
							{
								recDett.getFattura().setCodice3(String.valueOf(rs0.getInt("riga_fattura_in_credito")));
							}
							rsFatt.close();
							pstmtFatt.close();
						}
						listaFattureDett.add(recDett);

					}
					rs0.close();
					pstmt0.close();
					// elimina le fatture senza righe
					if (ricercaFatture.isRicercaOrd() && listaFattureDett.size()>0)
					{
						rec.setRigheDettaglioFattura(listaFattureDett);
						listaFatture.add(rec);
						progrRighe=progrRighe+1;
					}

					if (!ricercaFatture.isRicercaOrd())
					{
						rec.setRigheDettaglioFattura(listaFattureDett);
						listaFatture.add(rec);
						progrRighe=progrRighe+1;
					}

			}
				rs.close();
				pstmt.close();
				connection.close();

		}
		catch (ValidationException e) {
    	  throw e;

	} catch (Exception e) {
		log.error("", e);
	} finally {
		close(connection);
	}
		Validazione.ValidaRicercaFatture (listaFatture);

        return listaFatture;
	}




	public boolean  inserisciCambio(CambioVO cambio) throws DataException, ApplicationException , ValidationException
	{
		Validazione.ValidaCambioVO (cambio);
		// List<SezioniVO>
        boolean valRitorno=false;
    	int motivo=0;

		try {

			// Trova descrizione delle sessioni
			try {
				Session session = HibernateUtil.getSession();
				//session.getTransaction().begin();
				Criteria cr = session.createCriteria(Tba_cambi_ufficiali.class);
				//List<Tba_cambi_ufficiali> results = (List<Tba_cambi_ufficiali>) cr.list();
				if (cambio.getCodBibl()!=null && cambio.getCodBibl().length()!=0)
				{
					cr.add(Restrictions.eq("cd_bib", cambio.getCodBibl()));
				}

					if (cambio.getCodValuta()!=null && cambio.getCodValuta().length()!=0)
				{
					cr.add(Restrictions.eq("valuta", cambio.getCodValuta().trim().toUpperCase()));
				}

				List<Tba_cambi_ufficiali> results = cr.list();

				// solo se non è già esistente l'elemento lo si inserisce
				if (results.size()==0)
				{
					Tba_cambi_ufficiali cu= new Tba_cambi_ufficiali();
					// Try an insert
					// DATA di sistema
					Timestamp ts = DaoManager.now();
					//Transaction tx=session.beginTransaction();

					BigDecimal bd = new BigDecimal(1936.27);

					// modificato il 06.04.09
					cu.setTs_ins(ts);
					cu.setTs_var(ts);
					//cambio.getCodBibl()
					cu.setCd_bib(DaoManager.creaIdBib(cambio.getCodPolo(), cambio.getCodBibl()) );
					cu.setValuta(cambio.getCodValuta().trim().toUpperCase());
					//cu.setCambio(bd);
					if (cambio.getTassoCambio()==0)
					{
						motivo=3; // tasso inserimento a zero
						throw new ValidationException("cambierroreInserimentoTasso",
								ValidationExceptionCodici.cambierroreInserimentoTasso);

					}
					else
					{
						cu.setCambio(BigDecimal.valueOf(cambio.getTassoCambio()));
					}
					cu.setData_var(ts);
					session.save(cu);
					//session.getTransaction().commit();
					session.flush();
					HibernateUtil.closeSession();
					valRitorno=true;
				}
				else {
					// impostazione motivo
					motivo=1; // record già esistente

				}
			} catch (InfrastructureException e) {

				log.error("", e);
				try {
					HibernateUtil.closeSession();
				} catch (InfrastructureException ex) {
					ex.printStackTrace();
				}
			}


// END almaviva TEST

		} catch (Exception e) {

			log.error("", e);
		}
		if 	(!valRitorno) {
			if (motivo==1){
				throw new ValidationException("cambierroreInserimentoEsistenzaRecord",
					ValidationExceptionCodici.cambierroreInserimentoEsistenzaRecord);

			}
			if (motivo==3){
				throw new ValidationException("cambierroreInserimentoTasso",
						ValidationExceptionCodici.cambierroreInserimentoTasso);

			}

		}
        return valRitorno;
	}

	public boolean  modificaCambio(CambioVO cambio) throws DataException, ApplicationException, ValidationException
	{
		// List<SezioniVO>
        boolean valRitorno=false;
    	int motivo=0;


		try {

			// Trova descrizione delle sessioni
			try {

				Session session = HibernateUtil.getSession();
				//session.getTransaction().begin();
				Criteria cr = session.createCriteria(Tba_cambi_ufficiali.class);
				if (cambio.getCodBibl()!=null && cambio.getCodBibl().length()!=0)
				{
					cr.add(Restrictions.eq("cd_bib", cambio.getCodBibl()));
				}

				if (cambio.getCodValuta()!=null && cambio.getCodValuta().length()!=0)
				{
					cr.add(Restrictions.eq("valuta", cambio.getCodValuta().trim().toUpperCase()));
				}


				List<Tba_cambi_ufficiali> results = cr.list();
				Tba_cambi_ufficiali cu;
				// se esiste l'elemento
				if (results.size()==1)
				{
		    		cu = results.get(0);

					// DATA di sistema
					Timestamp ts = DaoManager.now();
					//Transaction tx=session.beginTransaction();

					BigDecimal bd = new BigDecimal(1936.27);

//					 modificato il 06.04.09
					cu.setTs_ins(ts);
					cu.setTs_var(ts);
					cu.setCd_bib(DaoManager.creaIdBib(cambio.getCodPolo(), cambio.getCodBibl()) );
					cu.setValuta(cambio.getCodValuta().trim().toUpperCase());
					//cu.setCambio(bd);
					if (cambio.getTassoCambio()==0)
					{
						motivo=3;
						throw new ValidationException("cambierroreInserimentoTasso",
								ValidationExceptionCodici.cambierroreInserimentoTasso);
					}
					else
					{
						cu.setCambio(BigDecimal.valueOf(cambio.getTassoCambio()));
					}

					cu.setData_var(ts);
					session.update(cu);
					//session.getTransaction().commit();
					//tx.commit();

					session.flush();
					HibernateUtil.closeSession();
					valRitorno=true;
				}
				else
				{
					motivo=2; // record non univoco o non esistente
				}

			} catch (InfrastructureException e) {

				log.error("", e);
				try {
					HibernateUtil.closeSession();
				} catch (InfrastructureException ex) {
					ex.printStackTrace();
				}
			}


// END almaviva TEST

		} catch (Exception e) {

			log.error("", e);
		}
		if 	(!valRitorno) {
			if (motivo==2){
				throw new ValidationException("cambierroreModificaRecordNonUnivoco",
					ValidationExceptionCodici.cambierroreModificaRecordNonUnivoco);

			}
			if (motivo==3){
				throw new ValidationException("cambierroreInserimentoTasso",
						ValidationExceptionCodici.cambierroreInserimentoTasso);

			}
		}
        return valRitorno;
	}

	public boolean  cancellaCambio(CambioVO cambio) throws DataException, ApplicationException
	{
		// List<SezioniVO>
        boolean valRitorno=false;
    	int motivo=0;

		try {

			// Trova descrizione delle sessioni
			try {
				Session session = HibernateUtil.getSession();
				//session.getTransaction().begin();
				Criteria cr = session.createCriteria(Tba_cambi_ufficiali.class);

				if (cambio.getCodBibl()!=null && cambio.getCodBibl().length()!=0)
				{
					cr.add(Restrictions.eq("cd_bib", cambio.getCodBibl()));
				}

					if (cambio.getCodValuta()!=null && cambio.getCodValuta().length()!=0)
				{
					cr.add(Restrictions.eq("valuta", cambio.getCodValuta().trim().toUpperCase()));
				}
/*				if (cambio.getTassoCambio()!=0)
				{
					cr.add(Restrictions.eq("cambio",BigDecimal.valueOf(cambio.getTassoCambio())));
				}*/

				List<Tba_cambi_ufficiali> results = cr.list();
				Tba_cambi_ufficiali cu;
				// se esiste l'elemento
				if (results.size()==1)
				{
		    		cu = results.get(0);					// DATA di sistema
					Timestamp ts = DaoManager.now();
					//Transaction tx=session.beginTransaction();

					BigDecimal bd = new BigDecimal(1936.27);

//					 modificato il 06.04.09
					cu.setTs_ins(ts);
					cu.setTs_var(ts);
					cu.setCd_bib(DaoManager.creaIdBib(cambio.getCodPolo(), cambio.getCodBibl()) );
					cu.setValuta(cambio.getCodValuta().trim().toUpperCase());
					//cu.setCambio(bd);
					cu.setCambio(BigDecimal.valueOf(cambio.getTassoCambio()));
					cu.setData_var(ts);
					session.delete(cu);
					//session.getTransaction().commit();
					session.flush();
					HibernateUtil.closeSession();
					valRitorno=true;
				}
				else
				{
					motivo=2; // record non univoco o non esistente
				}
			} catch (InfrastructureException e) {

				log.error("", e);
				try {
					HibernateUtil.closeSession();
				} catch (InfrastructureException ex) {
					ex.printStackTrace();
				}
			}


// END almaviva TEST

		} catch (Exception e) {

			log.error("", e);
		}
        return valRitorno;
	}

	public List<CambioVO> getRicercaListaCambi(ListaSuppCambioVO ricercaCambi) throws ResourceNotFoundException, ApplicationException, ValidationException
	{

		Validazione.ValidaListaSuppCambioVO (ricercaCambi);

		// List<SezioniVO>
        List<CambioVO> listaCambi = new ArrayList<CambioVO>();
    	int ret = 0;
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // execute the search here

		CambioVO rec = null;

			// Trova descrizione delle sessioni
			try {
				Session session = HibernateUtil.getSession();
				Criteria cr = session.createCriteria(Tba_cambi_ufficiali.class);


				if (ricercaCambi.getCodBibl()!=null && ricercaCambi.getCodBibl().length()!=0)
				{
					cr.add(Restrictions.eq("cd_bib", ricercaCambi.getCodBibl()));
				}

				if (ricercaCambi.getCodValuta()!=null && ricercaCambi.getCodValuta().trim().length()!=0)
				{
					cr.add(Restrictions.eq("valuta", ricercaCambi.getCodValuta().trim().toUpperCase()));
				}

				List<Tba_cambi_ufficiali> results = cr.list();

				Tba_cambi_ufficiali cu;
				Tb_codici aval;
				for (int index=0; index < results.size(); index++) {
					cu = results.get(index);
	    			rec = new CambioVO();

							// carica il resultset

					            	String descrTipoSez="";
//									 modificato il 06.04.09
					                rec.setCodBibl(cu.getCd_bib().getCd_biblioteca());
					                rec.setCodValuta(cu.getValuta().trim().toUpperCase());
									rec.setDesValuta("non trovato");
					                BigDecimal bd=cu.getCambio();
					                bd.setScale(2, BigDecimal.ROUND_HALF_UP);
					                rec.setTassoCambio(bd.doubleValue());
					                rec.setDataVariazione(String.valueOf(cu.getData_var()));
									// cerco la descrizione della valuta del codice valuta
					                try {
										descrTipoSez = codici.getDescrizioneCodiceSBN(CodiciType.CODICE_VALUTA, cu.getValuta().trim().toUpperCase());
											if (descrTipoSez!=null && !descrTipoSez.equals("") )
											{
												rec.setDesValuta(descrTipoSez);
											}
					                	} catch (RemoteException ex) {
											ex.printStackTrace();
										}
					            	if (ricercaCambi.getDesValuta()!=null && ricercaCambi.getDesValuta().trim().length()!=0)
									{
										if (descrTipoSez!=null && !descrTipoSez.equals("") && descrTipoSez.equals(ricercaCambi.getDesValuta().trim().toUpperCase()))
											{
							                listaCambi.add(rec);
											}
									}
					            	else
					            	{
						                listaCambi.add(rec);
					            	}
				}

			} catch (InfrastructureException e) {

				log.error("", e);
				try {
					HibernateUtil.closeSession();
				} catch (InfrastructureException ex) {
					ex.printStackTrace();
				}
			}


// END almaviva TEST


		// n.b. la validazione viene chiamata anche se si verifica un InfrastructureException e
		// quindi lo converte in un ValidationException: è corretto????
		Validazione.ValidaRicerca (listaCambi);
        return listaCambi;
	}

	public List<OrdiniVO> getListaOrdini() throws ResourceNotFoundException, ApplicationException
	{
		// List<SezioniVO>
        List<OrdiniVO> listaOrdini = new ArrayList<OrdiniVO>();
    	int ret = 0;
        // execute the search here
        	OrdiniVO rec = null;
			Connection connection = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {

				connection = getConnection();

				//String sql="select ord.*, forn.nom_fornitore, tit.isbd  from tba_ordini  ord ";
				String sql="select ord.*, forn.nom_fornitore   from tba_ordini  ord ";
				sql +=" join tbb_bilanci bil on bil.cd_bib=ord.cd_bib and bil.esercizio=ord.esercizio and bil.capitolo=ord.capitolo ";
				sql +=" join tbr_fornitori forn on forn.cod_fornitore=ord.cod_fornitore ";
				//sql +=" join tb_titolo tit on tit.bid=ord.bid ";
				sql +="  where ord.cd_bib='ARG' ";
				pstmt = connection.prepareStatement(sql);

				rs = pstmt.executeQuery();


				while (rs.next()) {
					rec = new OrdiniVO();
					rec.setCodPolo(null);
					rec.setCodBibl(rs.getString("cd_bib"));
					rec.setCodOrdine(rs.getString("cod_ord"));
					rec.setAnnoOrdine(String.valueOf(rs.getInt("anno_ord")));
					rec.setTipoOrdine(rs.getString("cod_tip_ord"));
					rec.setDataOrdine(String.valueOf(rs.getDate("data_ord")));
					rec.setNoteOrdine(rs.getString("note"));
					rec.setNumCopieOrdine(rs.getInt("num_copie"));
					if (rs.getString("continuativo")!=null && !rs.getString("continuativo").equals("0"))
					{
						rec.setContinuativo(true);
					}
					else
					{
						rec.setContinuativo(false);
					}
					rec.setStatoOrdine(rs.getString("stato_ordine"));
					rec.setCodDocOrdine(String.valueOf(rs.getInt("cod_doc_lett")));
					rec.setCodTipoDocOrdine(rs.getString("tipo_doc_lett"));
					rec.setCodUrgenzaOrdine(rs.getString("tipo_urgenza"));
					rec.setCodRicOffertaOrdine(rs.getString("cod_rich_off"));
					rec.setIdOffertaFornOrdine(rs.getString("bid_p"));
					rec.setFornitore(new StrutturaCombo(String.valueOf(rs.getInt("cod_fornitore")), rs.getString("nom_fornitore")));
					rec.setNoteFornitore(rs.getString("note_forn"));
					rec.setTipoInvioOrdine(rs.getString("tipo_invio"));
					rec.setBilancio(new StrutturaTerna(String.valueOf(rs.getInt("esercizio")), String.valueOf(rs.getInt("capitolo")),rs.getString("cod_mat")));
					rec.setCodPrimoOrdine(String.valueOf(rs.getInt("cod_1ord")));
					rec.setAnnoPrimoOrdine(String.valueOf(rs.getInt("anno_1ord")));
					rec.setValutaOrdine(rs.getString("valuta"));
					//BigDecimal bd=cu.getCambio();
					rec.setPrezzoOrdine(rs.getDouble("prezzo"));
					rec.setPrezzoEuroOrdine(rs.getDouble("prezzo_lire"));
					rec.setPaeseOrdine(rs.getString("paese"));
					rec.setSezioneAcqOrdine(rs.getString("cod_sezione"));
					rec.setCodBibliotecaSuggOrdine(rs.getString("cd_bib_sugg"));

					//rec.setTitolo(new StrutturaCombo(rs.getString("bid"),rs.getString("isbd")));

					rec.setStatoAbbOrdine(rs.getString("stato_abb"));
					rec.setPeriodoValAbbOrdine(rs.getString("cod_per_abb"));
					rec.setAnnoAbbOrdine(String.valueOf(rs.getInt("anno_abb")));
					rec.setNumFascicoloAbbOrdine(rs.getString("num_fasc"));
					rec.setDataPubblFascicoloAbbOrdine(String.valueOf(rs.getDate("data_fasc")));
					rec.setAnnataAbbOrdine(rs.getString("annata"));
					rec.setNumVolAbbOrdine(rs.getInt("num_vol_abb"));
					rec.setDataFineAbbOrdine(String.valueOf(rs.getDate("data_fine")));
					rec.setRegTribOrdine(rs.getString("reg_trib"));
					rec.setNaturaOrdine(rs.getString("natura"));
					rec.setStampato(false);

					//almaviva5_20121113 evolutive google
					rec.setCd_tipo_lav(rs.getString("cd_tipo_lav"));

					listaOrdini.add(rec);
				} // End while
				rs.close();
				connection.close();
			} catch (Exception e) {
				log.error("", e);
			} finally {
				close(connection);
			}
			return listaOrdini;
		}


	// getRicercaListaOrdini

	public List<OrdiniVO> getRicercaListaOrdini(
			ListaSuppOrdiniVO ricercaOrdini) throws ResourceNotFoundException,
			ApplicationException, ValidationException {

		Validazione.ValidaListaSuppOrdiniVO(ricercaOrdini);
		List<OrdiniVO> listaOrdini = new ArrayList<OrdiniVO>();

    	OrdiniVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			//ricercaOrdini contiene i criteri di ricerca
			connection = getConnection();

			ConfigurazioneBOVO configBO = new ConfigurazioneBOVO();
			configBO.setCodBibl(ricercaOrdini.getCodBibl());
			configBO.setCodPolo(ricercaOrdini.getCodPolo());

			String tipoRinnovoConfigurato = "";
			try {
				ConfigurazioneBOVO configBOletto = new ConfigurazioneBOVO();
				configBOletto = this.loadConfigurazione(configBO);
				if (configBOletto != null)
					tipoRinnovoConfigurato = configBOletto.getTipoRinnovo();

			} catch (Exception e) {
				log.error("", e);
			}

			ConfigurazioneORDVO configurazioneORD = new ConfigurazioneORDVO();
			configurazioneORD.setCodBibl(ricercaOrdini.getCodBibl());
			configurazioneORD.setCodPolo(ricercaOrdini.getCodPolo());
			ConfigurazioneORDVO configurazioneORDLetta = null;
			boolean gestBil = true;
			boolean gestSez = true;
			boolean gestProf = true;

			try {
				configurazioneORDLetta = this.loadConfigurazioneOrdini(configurazioneORD);
				gestBil = configurazioneORDLetta.isGestioneBilancio();
				gestSez = configurazioneORDLetta.isGestioneSezione();
				gestProf = configurazioneORDLetta.isGestioneProfilo();
			} catch (Exception e) {
				log.error("", e);
			}

			//String sql="select ord.*,  forn.nom_fornitore, tit.isbd, TO_CHAR(ord.data_ord,'dd/MM/yyyy') as data_ord_str,TO_CHAR(ord.data_fine,'dd/MM/yyyy') as data_fine_str, TO_CHAR(ord.data_fasc,'dd/MM/yyyy') as  data_fasc_str  from tba_ordini  ord ";
			String sql="select distinct ord.*,ord.ts_var as dataUpd, val.valuta as valCambio, forn.cod_fornitore, forn.nom_fornitore,";
			sql += "lower(forn.nom_fornitore),forn.indirizzo as fornInd, forn.citta as fornCitta, forn.cap as fornCap, forn.paese as fornPae, ";
			sql += "forn.cod_fiscale as fornCf, forn.fax as fornFax, forn.telefono as fornTel,  capBil.esercizio, capBil.capitolo, ";
			sql += "sez.cod_sezione, sez.nome, TO_CHAR(ord.data_ord,'dd/MM/yyyy') as data_ord_str, ";
			sql += "TO_CHAR(ord.data_fine,'dd/MM/yyyy') as data_fine_str, TO_CHAR(ord.data_fasc,'dd/MM/yyyy') as  data_fasc_str, ";
			sql += "TO_CHAR(data_chiusura_ord,'dd/MM/yyyy') as  data_chiusura_ord_str  from tba_ordini  ord ";

			//TODO  gestire le biblioteche affiliate a fronte della selezione del bottone relativo
			if (ricercaOrdini.getCodBiblAffil()!=null && ricercaOrdini.getCodBiblAffil().length()!=0 )
			{
				sql +=" join tra_ordini_biblioteche ordBibl on ordBibl.cod_bib_ord=ord.cd_bib and ordBibl.fl_canc<>'S' and  ordBibl.cod_tip_ord=ord.cod_tip_ord and ordBibl.anno_ord=ord.anno_ord and ordBibl.cod_ord=ord.cod_ord ";
			}
			sql +=" left join tbr_fornitori forn on forn.cod_fornitore=ord.cod_fornitore and forn.fl_canc<>'S'";
			sql +=" left join tba_sez_acquis_bibliografiche sez on sez.id_sez_acquis_bibliografiche=ord.id_sez_acquis_bibliografiche and sez.fl_canc<>'S' ";
			sql +=" left join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=ord.id_capitoli_bilanci and capBil.fl_canc<>'S' ";
			sql +=" left join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and bil.fl_canc<>'S' ";
			sql +=" left join tba_cambi_ufficiali val on val.id_valuta=ord.id_valuta  and  val.fl_canc<>'S'";
			sql +=" where ord.fl_canc<>'S'";

			if (ricercaOrdini.getBidList()!=null && ricercaOrdini.getBidList().size()!=0 )
			{
				Boolean aggiungiSQL=false;
				String sqla="";
				sqla=sqla + " ( ";
				//sql +=" ord.stato_ordine='" + ricercaOrdini.getStatoOrdine() +"'";
				for (int index = 0; index < ricercaOrdini.getBidList().size(); index++) {
					String so = ricercaOrdini.getBidList().get(index) ;
					if (so!=null && so.length()!=0)
					{
				         if (!sqla.equals(" ( ")) {
				        	 sqla=sqla + " OR ";
				         }
				        sqla=sqla + " upper(ord.bid)=?";
						aggiungiSQL=true;
					}
				}
				sqla=sqla + " ) ";
				if (aggiungiSQL)
				{
					sql=this.struttura(sql);
					sql += sqla;
				}
			}


			if (ricercaOrdini.getIdOrdList()!=null && ricercaOrdini.getIdOrdList().size()!=0 )
			{
				Boolean aggiungiSQL=false;
				String sqla="";
				sqla=sqla + " ( ";
				//sql +=" ord.stato_ordine='" + ricercaOrdini.getStatoOrdine() +"'";
				for (int index = 0; index < ricercaOrdini.getIdOrdList().size(); index++) {
					Integer so = ricercaOrdini.getIdOrdList().get(index) ;
					if (so>0)
					{
				         if (!sqla.equals(" ( ")) {
				        	 sqla=sqla + " OR ";
				         }
				        sqla=sqla + " ord.id_ordine=?";
						aggiungiSQL=true;
					}
				}
				sqla=sqla + " ) ";
				if (aggiungiSQL)
				{
					sql=this.struttura(sql);
					sql += sqla;
				}
			}

			if (ricercaOrdini.getCodBibl()!=null && ricercaOrdini.getCodBibl().length()!=0 )
			{
				sql=this.struttura(sql);
				sql +=" ord.cd_bib= ?";
			}
			//TODO  gestire le biblioteche affiliate a fronte della selezione del bottone relativo
			if (ricercaOrdini.getCodBiblAffil()!=null && ricercaOrdini.getCodBiblAffil().length()!=0 )
			{
				sql=this.struttura(sql);
				sql +=" ordBibl.cd_bib=?";
			}
			if (ricercaOrdini.getCodPolo()!=null && ricercaOrdini.getCodPolo().length()!=0 )
			{
				sql=this.struttura(sql);
				sql +=" ord.cd_polo= ?";
			}

			if (ricercaOrdini.getIDOrd()>0 )
			{
				sql=this.struttura(sql);
				sql +=" ord.id_ordine=?";
			}

			if (ricercaOrdini.getCodOrdine()!=null && ricercaOrdini.getCodOrdine().length()!=0 )
			{
				sql=this.struttura(sql);
				sql +=" ord.cod_ord=?";
			}
			if (ricercaOrdini.getTipoOrdine()!=null && ricercaOrdini.getTipoOrdine().length()!=0 )
			{
				sql=this.struttura(sql);
				sql +=" ord.cod_tip_ord=?";
			}
			if (ricercaOrdini.getAnnoOrdine()!=null && ricercaOrdini.getAnnoOrdine().length()!=0)
			{
				sql=this.struttura(sql);
				sql +=" ord.anno_ord =? ";
			}
			if (ricercaOrdini.getNaturaOrdine()!=null && ricercaOrdini.getNaturaOrdine().length()!=0 )
			{
				sql=this.struttura(sql);
				sql +=" ord.natura=?";
			}
			if (ricercaOrdini.getStatoOrdine()!=null && ricercaOrdini.getStatoOrdine().length()!=0 )
			{
				sql=this.struttura(sql);
				sql +=" ord.stato_ordine=?";
			}
			if (ricercaOrdini.getSezioneAcqOrdine()!=null && ricercaOrdini.getSezioneAcqOrdine().length()!=0 )
			{
				sql=this.struttura(sql);
				sql +=" upper(sez.cod_sezione)=?";
			}

			if (ricercaOrdini.getStatoOrdineArr()!=null && ricercaOrdini.getStatoOrdineArr().length!=0 )
			{
				Boolean aggiungiSQL=false;
				String sqla="";
				sqla=sqla + " ( ";
				//sql +=" ord.stato_ordine='" + ricercaOrdini.getStatoOrdine() +"'";
				for (int index = 0; index < ricercaOrdini.getStatoOrdineArr().length; index++) {
					String so = ricercaOrdini.getStatoOrdineArr()[index] ;
					if (so!=null && so.length()!=0)
					{
				         if (!sqla.equals(" ( ")) {
				        	 sqla=sqla + " OR ";
				         }
				        sqla=sqla + " ord.stato_ordine=?";
						aggiungiSQL=true;
					}
				}
				sqla=sqla + " ) ";
				if (aggiungiSQL)
				{
					sql=this.struttura(sql);
					sql += sqla;
				}
			}

			if (ricercaOrdini.getTipoOrdineArr()!=null && ricercaOrdini.getTipoOrdineArr().length!=0 )
			{
				Boolean aggiungiSQL=false;
				String sqla="";
				sqla=sqla + " ( ";
				for (int index = 0; index < ricercaOrdini.getTipoOrdineArr().length; index++) {
					String so = ricercaOrdini.getTipoOrdineArr()[index] ;
					if (so!=null && so.length()!=0)
					{
				         if (!sqla.equals(" ( ")) {
				        	 sqla=sqla + " OR ";
				         }
				        sqla=sqla + " ord.cod_tip_ord=?";
						aggiungiSQL=true;
					}
				}
				sqla=sqla + " ) ";
				if (aggiungiSQL)
				{
					sql=this.struttura(sql);
					sql += sqla;
				}
			}

			//LA RICERCA PER ID DEVE ESCLUDERE IL CRITERIO SU STAMPATO se attivato dalla maschera di ricerca
			if (ricercaOrdini.isAttivatoDaRicerca())
			{
				if ((ricercaOrdini.getRinnovatoStr()!=null && !ricercaOrdini.getRinnovatoStr().equals("") ) )
				{
					sql=this.struttura(sql);
					sql +=" ord.rinnovato=? ";
				}

				if ((ricercaOrdini.getStampatoStr()!=null && !ricercaOrdini.getStampatoStr().equals("") ) )
				{
					sql=this.struttura(sql);
					sql +=" ord.stampato=? ";
				}

			}
			else
			{
				if ( ricercaOrdini.isRinnovato())
				{
					sql=this.struttura(sql);
					sql +=" ord.rinnovato=? ";
				}

				if ( ricercaOrdini.isStampato())
				{
					sql=this.struttura(sql);
					sql +=" ord.stampato=? ";
				}
			}

			if (ricercaOrdini.getDataOrdineDa()!=null && ricercaOrdini.getDataOrdineDa().length()!=0)
			{
				sql=this.struttura(sql);
				sql +=" ord.data_ord >= TO_DATE(?,'dd/MM/yyyy')";
			}
			if (ricercaOrdini.getDataStampaOrdineDa()!=null && ricercaOrdini.getDataStampaOrdineDa().length()!=0)
			{
				sql=this.struttura(sql);
				sql +=" ord.stampato=? ";
				sql=this.struttura(sql);
				sql +=" ord.data_ord >= TO_DATE(?,'dd/MM/yyyy')";
			}

			if (ricercaOrdini.getDataOrdineA()!=null && ricercaOrdini.getDataOrdineA().length()!=0)
			{
				sql=this.struttura(sql);
				sql +=" ord.data_ord <= TO_DATE (?,'dd/MM/yyyy')";
			}

			if (ricercaOrdini.getDataStampaOrdineA()!=null && ricercaOrdini.getDataStampaOrdineA().length()!=0)
			{
				sql=this.struttura(sql);
				sql +=" ord.stampato=? ";
				sql=this.struttura(sql);
				sql +=" ord.data_ord <= TO_DATE(?,'dd/MM/yyyy')";
			}
			if (ricercaOrdini.getDataFineAbbOrdineDa()!=null && ricercaOrdini.getDataFineAbbOrdineDa().length()!=0)
			{
				sql=this.struttura(sql);
				sql +=" ord.data_fine >= TO_DATE (?,'dd/MM/yyyy')";
			}

			if (ricercaOrdini.getDataFineAbbOrdineA()!=null && ricercaOrdini.getDataFineAbbOrdineA().length()!=0)
			{
				sql=this.struttura(sql);
				sql +=" ord.data_fine <= TO_DATE (?,'dd/MM/yyyy')";
			}


			if (ricercaOrdini.getTipoInvioOrdine()!=null && ricercaOrdini.getTipoInvioOrdine().length()!=0 )
			{
				sql=this.struttura(sql);
				sql +=" ord.tipo_invio=?" ;
			}

			if (ricercaOrdini.getContinuativo()!=null && ricercaOrdini.getContinuativo().length()!=0 && ricercaOrdini.getContinuativo().equals("01"))
			{
				sql=this.struttura(sql);
				sql +=" ord.continuativo=?";
			}

			if (ricercaOrdini.getContinuativo()!=null && ricercaOrdini.getContinuativo().length()!=0 && ricercaOrdini.getContinuativo().equals("00"))
			{
				sql=this.struttura(sql);
				sql +=" ord.continuativo != ?";
			}
			if (ricercaOrdini.getFornitore()!=null  && ricercaOrdini.getFornitore().getDescrizione()!=null && ricercaOrdini.getFornitore().getDescrizione().trim().length()!=0 )
			{
				sql=this.struttura(sql);
				sql +=" trim(forn.nom_fornitore)=?" ;
			}
			if (ricercaOrdini.getFornitore()!=null  && ricercaOrdini.getFornitore().getCodice()!=null && ricercaOrdini.getFornitore().getCodice().length()!=0 )
			{
				sql=this.struttura(sql);
				sql +=" forn.cod_fornitore=?" ;
			}

			if (ricercaOrdini.getBilancio()!=null && ricercaOrdini.getBilancio().getCodice1()!=null && ricercaOrdini.getBilancio().getCodice1().length()!=0 )
			{
				sql=this.struttura(sql);
				sql +=" capBil.esercizio=?";
			}

			if (ricercaOrdini.getBilancio()!=null && ricercaOrdini.getBilancio().getCodice2()!=null && ricercaOrdini.getBilancio().getCodice2().length()!=0 )
			{
				sql=this.struttura(sql);
				sql +=" capBil.capitolo=?";
			}

			if (ricercaOrdini.getBilancio()!=null && ricercaOrdini.getBilancio().getCodice3()!=null && ricercaOrdini.getBilancio().getCodice3().length()!=0 )
			{
				sql=this.struttura(sql);
				sql +=" ord.tbb_bilancicod_mat=?";
			}
			if (ricercaOrdini.getTitolo()!=null && ricercaOrdini.getTitolo().getCodice()!=null && ricercaOrdini.getTitolo().getCodice().length()!=0)
			{
				sql=this.struttura(sql);
				sql +=" upper(ord.bid)=?";
			}

/*				if (ricercaOrdini.getTitolo()!=null &&  ricercaOrdini.getTitolo().getDescrizione()!=null && ricercaOrdini.getTitolo().getDescrizione().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" tit.isbd='" + ricercaOrdini.getTitolo().getDescrizione() +"'";
				}
*/

			//almaviva5_20121113 evolutive google
			InventarioVO inv = ricercaOrdini.getInventarioCollegato();
			if (inv != null) {
				sql = this.struttura(sql);
				StringBuilder buf = new StringBuilder();
				buf.append(" (");
				buf.append("(ord.cod_tip_ord='R' and exists (select 1 from tra_ordine_inventari oi ");
				buf.append(" where oi.id_ordine=ord.id_ordine and oi.cd_polo=? and oi.cd_bib=? and oi.cd_serie=? and oi.cd_inven=?) ");
				buf.append(" ) or (");
				buf.append("(ord.cod_tip_ord<>'R' and exists (select 1 from tbc_inventario i ");
				buf.append(" where i.cd_tip_ord=ord.cod_tip_ord and i.anno_ord=ord.anno_ord and i.cd_ord=ord.cod_ord ");
				buf.append(" and i.fl_canc<>'S' and i.cd_polo=? and i.cd_bib=? and i.cd_serie=? and i.cd_inven=?) ");
				buf.append(") ))");
				sql += buf.toString();
			}

			if (ricercaOrdini.getOrdinamento()==null || (ricercaOrdini.getOrdinamento()!=null && ricercaOrdini.getOrdinamento().equals("")))
			{
				sql +="    order by ord.cd_bib, ord.anno_ord desc, ord.cod_tip_ord, ord.cod_ord, ord.data_ord desc  ";
			}
			else if (ricercaOrdini.getOrdinamento().equals("1"))
			{
				sql +="  order by ord.cd_bib, ord.cod_tip_ord, ord.cod_ord ";
			}
			else if (ricercaOrdini.getOrdinamento().equals("2"))
			{
				sql +="  order by ord.cd_bib , ord.anno_ord desc, ord.cod_tip_ord , ord.cod_ord desc";
			}

			else if (ricercaOrdini.getOrdinamento().equals("3"))
			{
				sql +="  order by ord.cd_bib, lower(forn.nom_fornitore) ";
			}
			else if (ricercaOrdini.getOrdinamento().equals("4"))
			{
				sql +="  order by ord.cd_bib, ord.data_ord desc ";
			}
			else if (ricercaOrdini.getOrdinamento().equals("5"))
			{
				sql +="  order by ord.cd_bib, ord.stato_ordine ";
			}
			else if (ricercaOrdini.getOrdinamento().equals("6"))
			{
				sql +="  order by ord.cd_bib, capBil.esercizio, capBil.capitolo ";   // , bil.cod_mat (se aggiunto bisogna cambiare la select distinct per la molteplicita)
			}
			else if (ricercaOrdini.getOrdinamento().equals("7")) // per le stampe
			{
				sql +="  order by ord.cd_bib, ord.cod_fornitore, ord.cod_tip_ord , ord.cod_ord ";
			}
			else if (ricercaOrdini.getOrdinamento().equals("8")) // per le LISTE DA DOCUMENTO FISICO
			{
				sql +="  order by ord.cd_bib, ord.bid ";
			}
			else if (ricercaOrdini.getOrdinamento().equals("9")) // per le stampe con la gestione dei ristampati
			{
				sql +="  order by ord.cd_bib, ord.cod_fornitore, ord.cod_tip_ord, ord.cod_ord, ord.data_ord ";
			}
			else if (ricercaOrdini.getOrdinamento().equals("10")) // per le stampe con la gestione dei ristampati
			{
				sql +="  order by ord.cd_bib, ord.cod_fornitore, ord.cod_tip_ord, ord.cod_ord, ord.data_ord desc";
			}

			pstmt = connection.prepareStatement(sql);
			//

			int i=1;

			if (ricercaOrdini.getBidList()!=null && ricercaOrdini.getBidList().size()!=0 )
			{
				for (int index = 0; index < ricercaOrdini.getBidList().size(); index++) {
					String so = ricercaOrdini.getBidList().get(index) ;
					if (so!=null && so.length()!=0)
					{
						pstmt.setString(i, so);
						i=i+1;
					}
				}
			}

			if (ricercaOrdini.getIdOrdList()!=null && ricercaOrdini.getIdOrdList().size()!=0 )
			{
				for (int index = 0; index < ricercaOrdini.getIdOrdList().size(); index++) {
					Integer so = ricercaOrdini.getIdOrdList().get(index) ;
					if (so>0)
					{
						pstmt.setInt(i, so );
						i=i+1;
					}
				}
			}

			if (ricercaOrdini.getCodBibl()!=null && ricercaOrdini.getCodBibl().length()!=0 )
			{
				pstmt.setString(i, ricercaOrdini.getCodBibl());
				i=i+1;
			}

			//TODO  gestire le biblioteche affiliate a fronte della selezione del bottone relativo
			if (ricercaOrdini.getCodBiblAffil()!=null && ricercaOrdini.getCodBiblAffil().length()!=0 )
			{
				pstmt.setString(i, ricercaOrdini.getCodBiblAffil());
				i=i+1;
			}

			if (ricercaOrdini.getCodPolo()!=null && ricercaOrdini.getCodPolo().length()!=0 )
			{
				pstmt.setString(i, ricercaOrdini.getCodPolo());
				i=i+1;
			}
			if (ricercaOrdini.getIDOrd()>0 )
			{
				pstmt.setInt(i, ricercaOrdini.getIDOrd());
				i=i+1;
			}
			if (ricercaOrdini.getCodOrdine()!=null && ricercaOrdini.getCodOrdine().length()!=0 )
			{
				pstmt.setInt(i, Integer.valueOf(ricercaOrdini.getCodOrdine()) );
				i=i+1;
			}
			if (ricercaOrdini.getTipoOrdine()!=null && ricercaOrdini.getTipoOrdine().length()!=0 )
			{
				pstmt.setString(i, ricercaOrdini.getTipoOrdine()  );
				i=i+1;
			}
			if (ricercaOrdini.getAnnoOrdine()!=null && ricercaOrdini.getAnnoOrdine().length()!=0)
			{
				pstmt.setInt(i, Integer.valueOf(ricercaOrdini.getAnnoOrdine()));
				i=i+1;
			}
			if (ricercaOrdini.getNaturaOrdine()!=null && ricercaOrdini.getNaturaOrdine().length()!=0 )
			{
				pstmt.setString(i, ricercaOrdini.getNaturaOrdine()  );
				i=i+1;
			}
			if (ricercaOrdini.getStatoOrdine()!=null && ricercaOrdini.getStatoOrdine().length()!=0 )
			{
				pstmt.setString(i, ricercaOrdini.getStatoOrdine()  );
				i=i+1;
			}
			if (ricercaOrdini.getSezioneAcqOrdine()!=null && ricercaOrdini.getSezioneAcqOrdine().length()!=0 )
			{
				pstmt.setString(i, ricercaOrdini.getSezioneAcqOrdine().trim().toUpperCase()   );
				i=i+1;
			}
			if (ricercaOrdini.getStatoOrdineArr()!=null && ricercaOrdini.getStatoOrdineArr().length!=0 )
			{
				for (int index = 0; index < ricercaOrdini.getStatoOrdineArr().length; index++) {
					String so = ricercaOrdini.getStatoOrdineArr()[index] ;
					if (so!=null && so.length()!=0)
					{
						pstmt.setString(i, so );
						i=i+1;
					}
				}
			}

			if (ricercaOrdini.getTipoOrdineArr()!=null && ricercaOrdini.getTipoOrdineArr().length!=0 )
			{
				for (int index = 0; index < ricercaOrdini.getTipoOrdineArr().length; index++) {
					String so = ricercaOrdini.getTipoOrdineArr()[index] ;
					if (so!=null && so.length()!=0)
					{
						pstmt.setString(i, so );
						i=i+1;
					}
				}
			}

			//LA RICERCA PER ID DEVE ESCLUDERE IL CRITERIO SU STAMPATO se attivato dalla maschera di ricerca
			if (ricercaOrdini.isAttivatoDaRicerca())
			{
				if ((ricercaOrdini.getRinnovatoStr()!=null && !ricercaOrdini.getRinnovatoStr().equals("") ) )
				{
					pstmt.setBoolean(i, ricercaOrdini.isRinnovato() );
					i=i+1;
				}

				if ((ricercaOrdini.getStampatoStr()!=null && !ricercaOrdini.getStampatoStr().equals("") ) )
				{
					pstmt.setBoolean(i, ricercaOrdini.isStampato() );
					i=i+1;
				}
			}
			else
			{
				if ( ricercaOrdini.isRinnovato())
				{
					pstmt.setBoolean(i, true );
					i=i+1;
				}
				if ( ricercaOrdini.isStampato())
				{
					pstmt.setBoolean(i, true );
					i=i+1;
				}
			}

			if (ricercaOrdini.getDataOrdineDa()!=null && ricercaOrdini.getDataOrdineDa().length()!=0)
			{
				pstmt.setString(i, ricercaOrdini.getDataOrdineDa() );
				i=i+1;
			}
			if (ricercaOrdini.getDataStampaOrdineDa()!=null && ricercaOrdini.getDataStampaOrdineDa().length()!=0)
			{
				pstmt.setBoolean(i, true );
				i=i+1;

				pstmt.setString(i, ricercaOrdini.getDataStampaOrdineDa() );
				i=i+1;
			}
			if (ricercaOrdini.getDataOrdineA()!=null && ricercaOrdini.getDataOrdineA().length()!=0)
			{
				pstmt.setString(i, ricercaOrdini.getDataOrdineA() );
				i=i+1;
			}
			if (ricercaOrdini.getDataStampaOrdineA()!=null && ricercaOrdini.getDataStampaOrdineA().length()!=0)
			{
				pstmt.setBoolean(i, true );
				i=i+1;

				pstmt.setString(i, ricercaOrdini.getDataStampaOrdineA() );
				i=i+1;
			}

			if (ricercaOrdini.getDataFineAbbOrdineDa()!=null && ricercaOrdini.getDataFineAbbOrdineDa().length()!=0)
			{
				pstmt.setString(i, ricercaOrdini.getDataFineAbbOrdineDa() );
				i=i+1;
			}
			if (ricercaOrdini.getDataFineAbbOrdineA()!=null && ricercaOrdini.getDataFineAbbOrdineA().length()!=0)
			{
				pstmt.setString(i, ricercaOrdini.getDataFineAbbOrdineA() );
				i=i+1;
			}
			if (ricercaOrdini.getTipoInvioOrdine()!=null && ricercaOrdini.getTipoInvioOrdine().length()!=0 )
			{
				pstmt.setString(i, ricercaOrdini.getTipoInvioOrdine() );
				i=i+1;
			}
			if (ricercaOrdini.getContinuativo()!=null && ricercaOrdini.getContinuativo().length()!=0 && ricercaOrdini.getContinuativo().equals("01"))
			{
				pstmt.setString(i, "1");
				i=i+1;
			}

			if (ricercaOrdini.getContinuativo()!=null && ricercaOrdini.getContinuativo().length()!=0 && ricercaOrdini.getContinuativo().equals("00"))
			{
				pstmt.setString(i, "1");
				i=i+1;

			}
			if (ricercaOrdini.getFornitore()!=null  && ricercaOrdini.getFornitore().getDescrizione()!=null && ricercaOrdini.getFornitore().getDescrizione().trim().length()!=0 )
			{
				pstmt.setString(i, ricercaOrdini.getFornitore().getDescrizione().trim());
				i=i+1;
			}
			if (ricercaOrdini.getFornitore()!=null  && ricercaOrdini.getFornitore().getCodice()!=null && ricercaOrdini.getFornitore().getCodice().length()!=0 )
			{
				pstmt.setInt(i, Integer.parseInt(ricercaOrdini.getFornitore().getCodice()));
				i=i+1;
			}
			if (ricercaOrdini.getBilancio()!=null && ricercaOrdini.getBilancio().getCodice1()!=null && ricercaOrdini.getBilancio().getCodice1().length()!=0 )
			{
				//esercizio
				pstmt.setInt(i, Integer.parseInt(ricercaOrdini.getBilancio().getCodice1()));
				i=i+1;

			}
			if (ricercaOrdini.getBilancio()!=null && ricercaOrdini.getBilancio().getCodice2()!=null && ricercaOrdini.getBilancio().getCodice2().length()!=0 )
			{
				//capitolo
				pstmt.setInt(i, Integer.parseInt(ricercaOrdini.getBilancio().getCodice2()));
				i=i+1;
			}
			if (ricercaOrdini.getBilancio()!=null && ricercaOrdini.getBilancio().getCodice3()!=null && ricercaOrdini.getBilancio().getCodice3().length()!=0 )
			{
				pstmt.setString(i, ricercaOrdini.getBilancio().getCodice3() );
				i=i+1;
			}
			if (ricercaOrdini.getTitolo()!=null && ricercaOrdini.getTitolo().getCodice()!=null && ricercaOrdini.getTitolo().getCodice().length()!=0)
			{
				pstmt.setString(i, ricercaOrdini.getTitolo().getCodice().trim().toUpperCase() );
				i=i+1;
			}

			//almaviva5_20121113 evolutive google
			if (inv != null) {
				pstmt.setString(i++, inv.getCodPolo());
				pstmt.setString(i++, inv.getCodBib());
				pstmt.setString(i++, inv.getCodSerie());
				pstmt.setInt(i++, inv.getCodInvent());

				pstmt.setString(i++, inv.getCodPolo());
				pstmt.setString(i++, inv.getCodBib());
				pstmt.setString(i++, inv.getCodSerie());
				pstmt.setInt(i++, inv.getCodInvent());
			}

			//log.debug("Debug: lettura ordini");
			//log.debug("Debug: " + sql);

			rs = pstmt.executeQuery();
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;
				rec = new OrdiniVO();
				// configurazione
				rec.setGestBil(true);
				rec.setGestSez(true);
				rec.setGestProf(true);

				if (!gestBil)
				{
					rec.setGestBil(false);
				}
				if (!gestSez)
				{
					rec.setGestSez(false);
				}
				if (!gestProf)
				{
					rec.setGestProf(false);
				}
				//rec.setGestBil(true);
				//rec.setGestSez(true);
				//rec.setGestProf(true);
				// TODO lettura configurazione ordini per la bibioteca dell'ordine (se non obbligatorio mettere a false)
				// TODO NON IMPOSTARE I VALORI DI DEFAULT SE SPECIFICATI
				// e' importante leggere la configurazione per imporre il valore false, altrimenti per default è true

				if (rs.getString("fl_canc")!=null && rs.getString("fl_canc").equals("S"))
				{
					rec.setFlag_canc(true);
				}
				rec.setCodPolo(rs.getString("cd_polo"));
				rec.setCodBibl(rs.getString("cd_bib"));
				rec.setCodOrdine(rs.getString("cod_ord"));
				rec.setAnnoOrdine(String.valueOf(rs.getInt("anno_ord")));
				rec.setTipoOrdine(rs.getString("cod_tip_ord"));
				//rec.setDataOrdine(String.valueOf(rs.getDate("data_ord")));

				// prova di antonio
/*					GregorianCalendar g = new GregorianCalendar();
					g.setTime(new java.util.Date(System.currentTimeMillis()));
					SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
					//	format1.format(rs.getDate("data_ord"));
					String formato = format1.format(g);
*/
				rec.setProgressivo(numRighe);

				rec.setIDOrd(rs.getInt("id_ordine"));
				rec.setIDBil(rs.getInt("id_capitoli_bilanci"));
				rec.setIDSez(rs.getInt("id_sez_acquis_bibliografiche"));
				rec.setIDVal(rs.getInt("id_valuta"));
				rec.setDataUpd(rs.getTimestamp("dataUpd"));
				// valorizzazione del campo datastampaordine per identificare gli ordini stampati in uno stesso buono
				rec.setDataStampaOrdine("");
				if (!rs.getTimestamp("data_agg").equals(rs.getTimestamp("ts_ins")))
				{
					rec.setDataStampaOrdine(rs.getString("data_agg"));
				}

				//ordine.setValutaOrdine(rsSub.getString("valuta"));



				rec.setDataOrdine(rs.getString("data_ord_str"));
				rec.setNoteOrdine(rs.getString("note"));
				rec.setNumCopieOrdine(rs.getInt("num_copie"));
				if (rs.getString("continuativo")!=null && rs.getString("continuativo").length()!=0 && rs.getString("continuativo").equals("1"))
				{
					rec.setContinuativo(true);
				}
				else
				{
					rec.setContinuativo(false);
				}
				rec.setStatoOrdine(rs.getString("stato_ordine"));
				//if (rs.getInt("cod_doc_lett")>0)
				// provenienza sugg lett
				if (rs.getString("cod_doc_lett")==null)
				{
					rec.setCodDocOrdine("");
					rec.setCodTipoDocOrdine("");

				}
				else if (rs.getInt("cod_doc_lett")>0)
				{
					rec.setCodDocOrdine(String.valueOf(rs.getInt("cod_doc_lett")));
					rec.setCodTipoDocOrdine(rs.getString("tipo_doc_lett"));
				}
				// provenienza sugg bibl
				if (rs.getString("cod_sugg_bibl")==null)
				{
					rec.setCodSuggBiblOrdine("");

				}
				else if (rs.getInt("cod_sugg_bibl")>0)
				{
					rec.setCodSuggBiblOrdine(String.valueOf(rs.getInt("cod_sugg_bibl")));
				}


				rec.setCodUrgenzaOrdine(rs.getString("tipo_urgenza"));
				// provenienza gare
				if (rs.getString("cod_rich_off")==null)
				{
					rec.setCodRicOffertaOrdine("");

				}
				else if (rs.getInt("cod_rich_off")>0)
				{
					rec.setCodRicOffertaOrdine(String.valueOf(rs.getInt("cod_rich_off")));
				}
				// provenienza offerte fornitore
				if (rs.getString("bid_p")==null)
				{
					rec.setIdOffertaFornOrdine("");

				}
				else
				{
					rec.setIdOffertaFornOrdine(rs.getString("cod_rich_off"));
				}

				//rec.setCodRicOffertaOrdine(rs.getString("cod_rich_off"));
				//rec.setIdOffertaFornOrdine(rs.getString("bid_p"));
				rec.setFornitore(new StrutturaCombo("",""));
				rec.getFornitore().setCodice(String.valueOf(rs.getInt("cod_fornitore")));
				rec.getFornitore().setDescrizione(rs.getString("nom_fornitore"));
				if (rec.getFornitore().getCodice()==null || (rec.getFornitore().getCodice()!=null && rec.getFornitore().getCodice().trim().length()==0) || rec.getFornitore().getDescrizione()==null || (rec.getFornitore().getDescrizione()!=null && rec.getFornitore().getDescrizione().trim().length()==0))
				{
					rec.getFornitore().setDescrizione("fornitore non presente su base dati");
				}

				// dati fornitore per stampe
				rec.setAnagFornitore(new FornitoreVO());
				rec.getAnagFornitore().setCodFornitore(String.valueOf(rs.getInt("cod_fornitore")));
				rec.getAnagFornitore().setNomeFornitore(rs.getString("nom_fornitore"));
				if (rec.getFornitore().getCodice()==null || (rec.getFornitore().getCodice()!=null && rec.getFornitore().getCodice().trim().length()==0) || rec.getFornitore().getDescrizione()==null || (rec.getFornitore().getDescrizione()!=null && rec.getFornitore().getDescrizione().trim().length()==0))
				{
					rec.getAnagFornitore().setNomeFornitore("fornitore non presente su base dati");
				}

				if (rs.getString("fornInd")!=null && rs.getString("fornInd").trim().length()>0 )
				{
					rec.getAnagFornitore().setIndirizzo(rs.getString("fornInd"));
				}
				if (rs.getString("fornCitta")!=null && rs.getString("fornCitta").trim().length()>0 )
				{
					rec.getAnagFornitore().setCitta(rs.getString("fornCitta"));
				}
				if (rs.getString("fornCap")!=null && rs.getString("fornCap").trim().length()>0 )
				{
					rec.getAnagFornitore().setCap(rs.getString("fornCap"));
				}
				if (rs.getString("fornPae")!=null && rs.getString("fornPae").trim().length()>0 )
				{
					rec.getAnagFornitore().setPaese(rs.getString("fornPae"));
				}
				if (rs.getString("fornCf")!=null && rs.getString("fornCf").trim().length()>0 )
				{
					rec.getAnagFornitore().setCodiceFiscale(rs.getString("fornCf"));
				}
				if (rs.getString("fornFax")!=null && rs.getString("fornFax").trim().length()>0 )
				{
					rec.getAnagFornitore().setFax(rs.getString("fornFax"));
				}
				if (rs.getString("fornTel")!=null && rs.getString("fornTel").trim().length()>0 )
				{
					rec.getAnagFornitore().setTelefono(rs.getString("fornTel"));
				}
				rec.getAnagFornitore().setFornitoreBibl(new DatiFornitoreVO());


				rec.setNoteFornitore(rs.getString("note_forn"));
				rec.setTipoInvioOrdine(rs.getString("tipo_invio"));
				if (gestBil && (rec.getTipoOrdine().equals("A") || rec.getTipoOrdine().equals("V") || rec.getTipoOrdine().equals("R")) && rs.getInt("esercizio")>0  )
				{
					rec.setBilancio(new StrutturaTerna(String.valueOf(rs.getInt("esercizio")), String.valueOf(rs.getInt("capitolo")),rs.getString("tbb_bilancicod_mat")));
				}
				else
				{
					rec.setBilancio(new StrutturaTerna("","",""));
				}
				if (rs.getString("cod_1ord")==null || (rs.getString("cod_1ord")!=null && rs.getString("cod_1ord").equals("")))
				{
					rec.setCodPrimoOrdine("");
				}
				else
				{
					rec.setCodPrimoOrdine(String.valueOf(rs.getInt("cod_1ord")));
				}
				if (rs.getString("anno_1ord")==null || (rs.getString("anno_1ord")!=null && rs.getString("anno_1ord").equals("")))
				{
					rec.setAnnoPrimoOrdine("");
				}
				else
				{
					rec.setAnnoPrimoOrdine(String.valueOf(rs.getInt("anno_1ord")));
				}

				//rec.setValutaOrdine(rs.getString("valuta"));
				rec.setValutaOrdine(rs.getString("valCambio"));

				//BigDecimal bd=cu.getCambio();
				rec.setPrezzoOrdine(rs.getDouble("prezzo"));
				rec.setPrezzoEuroOrdine(rs.getDouble("prezzo_lire"));
				rec.setPaeseOrdine(rs.getString("paese"));
				if (gestSez && (rec.getTipoOrdine().equals("A") || rec.getTipoOrdine().equals("V")))
				{
					rec.setSezioneAcqOrdine(rs.getString("cod_sezione"));
				}
				else
				{
					rec.setSezioneAcqOrdine("");
				}

				rec.setCodBibliotecaSuggOrdine(rs.getString("cod_bib_sugg"));
				String isbd="";
				String bid="";
				String nStandard="";
				List<StrutturaTerna> nStandardArr=null;
				//try {

/*					// solo per test
					if (rec.getIDOrd()==4)
					{
						String passa="si";
					}
*/


				if (rs.getString("bid")!=null && rs.getString("bid").trim().length()!=0)
				{
				// solo per test 	07.07.09
				//TitoloACQAreeIsbdVO recTitappo=this.getAreeIsbdTitolo(rs.getString("bid"));

/*						bid=rs.getString("bid");
						isbd="titolo di test";
*/
					bid=rs.getString("bid");

					try {
						TitoloACQVO recTit=null;
						//recTit = this.getTitoloRox(rs.getString("bid"));
						Tba_cambi_ufficialiDao cambiDao= new Tba_cambi_ufficialiDao();
						//List risposta = cambiDao.getTitoloOrdine(rs.getString("bid"));
						//recTit= cambiDao.getTitoloOrdineBis(rs.getString("bid"));
						recTit= this.getTitoloOrdineTer(rs.getString("bid"));
						if (recTit!=null && recTit.getIsbd()!=null) {
							isbd=recTit.getIsbd();
						}
						if (recTit!=null && recTit.getNumStandard()!=null) {
							nStandard=recTit.getNumStandard();
						}
						if (recTit!=null && recTit.getNumStandardArr()!=null && recTit.getNumStandardArr().size()>0) {
							nStandardArr=recTit.getNumStandardArr();
						}

					} catch (Exception e) {
						isbd="titolo non trovato";
					}

				}

				rec.setTitolo(new StrutturaCombo(bid,isbd));

				rec.setTitoloIsbn("");
				rec.setTitoloIssn("");
				if (nStandard!=null && nStandard.trim().length()>0)
				{
					if (rs.getString("natura").equals("M"))
					{
						rec.setTitoloIsbn(nStandard.trim());
					}
					else if ( rs.getString("natura").equals("S") && rs.getString("continuativo")!=null && rs.getString("continuativo").length()!=0 && rs.getString("continuativo").equals("1"))
					{
						rec.setTitoloIssn(nStandard.trim());
					}
				}
				if (nStandardArr!=null && nStandardArr.size()>0)
				{
					rec.setNumStandardArr(nStandardArr);
				}

				//rec.setTitolo(new StrutturaCombo(bid,isbd));

				//String res2="";
				//List<CambioVO> res=this.getListaCambi();
				//res2 = this.getTitoloRox(rec);
				//rec.setTitolo(new StrutturaCombo(bid,res2));

				rec.setStatoAbbOrdine(rs.getString("stato_abb"));
				rec.setPeriodoValAbbOrdine(rs.getString("cod_per_abb"));
				if (rs.getString("anno_abb")==null || (rs.getString("anno_abb")!=null && rs.getString("anno_abb").equals("")) || (rs.getString("anno_abb")!=null && rs.getInt("anno_abb")==0))
				{
					rec.setAnnoAbbOrdine("");
				}
				else
				{
					rec.setAnnoAbbOrdine(String.valueOf(rs.getInt("anno_abb")));
				}

				rec.setNumFascicoloAbbOrdine(rs.getString("num_fasc"));
				//rec.setDataPubblFascicoloAbbOrdine(String.valueOf(rs.getDate("data_fasc")));
				rec.setDataPubblFascicoloAbbOrdine(rs.getString("data_fasc_str"));
				rec.setAnnataAbbOrdine(rs.getString("annata"));
				rec.setNumVolAbbOrdine(rs.getInt("num_vol_abb"));
				//rec.setDataFineAbbOrdine(String.valueOf(rs.getDate("data_fine")));
				rec.setDataFineAbbOrdine(rs.getString("data_fine_str"));
				rec.setDataChiusura(rs.getString("data_chiusura_ord_str"));
				rec.setRegTribOrdine(rs.getString("reg_trib").trim());
				rec.setNaturaOrdine(rs.getString("natura"));
				rec.setStampato(rs.getBoolean("stampato"));
				rec.setRinnovato(rs.getBoolean("rinnovato"));
				rec.setRinnovoOrigine(new StrutturaTerna("","","")); // accoglie l'ordine originario o precedente  a seconda della configurazione
				// per la stampa


				if (rec.isContinuativo() && rec.getCodPrimoOrdine()!=null && !rec.getCodPrimoOrdine().trim().equals("0") && rec.getCodPrimoOrdine().trim().length()>0 && rec.getAnnoPrimoOrdine()!=null && !rec.getAnnoPrimoOrdine().trim().equals("0") && rec.getAnnoPrimoOrdine().trim().length()>0)
				{
					rec.setRinnovoOrigine(this.gestioneRinnovato(rec.getIDOrd(),rec.getCodPrimoOrdine().trim(),rec.getAnnoPrimoOrdine().trim(), tipoRinnovoConfigurato)); //

				}

				if (rec.getCodDocOrdine()!=null && rec.getCodDocOrdine().trim().length()>0 && !rec.getCodDocOrdine().trim().equals("0"))
				{
					rec.setProvenienza(rec.getCodDocOrdine().trim()+ "-Sugg. lett.");
				}
				else if (rec.getCodSuggBiblOrdine()!=null && rec.getCodSuggBiblOrdine().trim().length()>0 && !rec.getCodSuggBiblOrdine().trim().equals("0"))
				{
					rec.setProvenienza(rec.getCodSuggBiblOrdine().trim() + "-Sugg. bibliotec.");
				}
				else if (rec.getIdOffertaFornOrdine()!=null && rec.getIdOffertaFornOrdine().trim().length()>0 && !rec.getIdOffertaFornOrdine().trim().equals("0"))
				{
					rec.setProvenienza(rec.getIdOffertaFornOrdine().trim() + "-Offerte fornitore");
				}
				else if (rec.getCodRicOffertaOrdine()!=null && rec.getCodRicOffertaOrdine().trim().length()>0 && !rec.getCodRicOffertaOrdine().trim().equals("0"))
				{
					rec.setProvenienza(rec.getCodRicOffertaOrdine().trim() + "-Gare");
				}
				else
				{
					rec.setProvenienza("");
				}

				// solo per ordini di rilegatura

				if (rec.getTipoOrdine()!=null && rec.getTipoOrdine().equals("R")) // and numRighe=1
				{
					Tba_ordiniDao dao = new Tba_ordiniDao();
					Tra_ordine_carrello_spedizione ocs = dao.getOrdineCarrelloSpedizione(rec.getIDOrd());
					if (ocs != null)
					rec.setOrdineCarrelloSpedizione(new OrdineCarrelloSpedizioneDecorator(
							ConversioneHibernateVO.toWeb().ordineCarrelloSpedizione(ocs)));
					rec.setCd_tipo_lav(rs.getString("cd_tipo_lav"));
					rec.setCd_forn_google(configurazioneORDLetta.getCd_forn_google());
			        List<StrutturaInventariOrdVO> listaInv = this.getInvOrdRil(rec.getIDOrd());
					// subquery per test di esistenza inventari: da sostituire con metodo ancora da scrivere in this
					if (listaInv!=null && listaInv.size()>0)
					{
						rec.setRigheInventariRilegatura(listaInv);
					}
				}
				// solo per il primo ordine dell'elenco cerco l'esistenza di inventari (mi occorre solo in esamina e non sulla sintetica)

				listaOrdini.add(rec);
			} // End while

			rs.close();
			connection.close();

		}catch (ValidationException e) {
			log.error("", e);
			throw e;

		} catch (Exception e) {
			log.error("", e);

		} finally {
			close(connection);
		}

		Validazione.ValidaRicercaOrdini(listaOrdini);
        return listaOrdini;
	}

	public boolean inserisciOrdine(OrdiniVO ordine) throws DataException, ApplicationException , ValidationException
	{

		Validazione.ValidaOrdiniVO(ordine);

		boolean valRitorno=false;
    	int motivo=0;
    	OrdiniVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtVal = null;
		PreparedStatement pstmtInsInv= null;
		PreparedStatement pstmtDELInv= null;
		PreparedStatement pstmtUDPSTATOIMPORTA= null;
		PreparedStatement pstmtCOTROLLO= null;
		PreparedStatement pstmtInsFornBibl= null;

		ResultSet rs = null;
		ResultSet rsSub = null;
		ResultSet rsInsTit = null;
		ResultSet rsCOTROLLO = null;


		boolean valRitornoPrep=false;
		boolean valRitornoInsTit=false;
		boolean valRitornoInsFornBibl=false;


		String isbd="";
		String natura="";
		String denoFornitore="";

		try {
			// CONTROLLI PREVENTIVI
			//try {

			connection = getConnection();
			//	String sql0="select ord.cod_ord, ord.cod_tip_ord , forn.tipo_partner, ord.natura, tit.cd_natura from tba_ordini  ord ";
			String sql0="select ord.cod_ord, ord.cod_tip_ord , forn.tipo_partner, ord.natura from tba_ordini  ord ";
			if (ordine.getTipoOrdine()!=null && !ordine.getTipoOrdine().equals("R"))
			{
				if (ordine.getTitolo()!=null && ordine.getTitolo().getCodice()!=null && ordine.getTitolo().getCodice().length()!=0  )
				{

					try {
						TitoloACQVO recTit=null;
						recTit = this.getTitoloOrdineTer(ordine.getTitolo().getCodice());
						if (recTit!=null && recTit.getIsbd()!=null) {
							isbd=recTit.getIsbd();
							natura=recTit.getNatura();
						}
						if (recTit==null) {
							valRitornoInsTit=false;
							throw new ValidationException("ordineIncongruenzaTitoloInesistente",
									ValidationExceptionCodici.ordineIncongruenzaTitoloInesistente);
						}

					} catch (Exception e) {
						valRitornoInsTit=false;
						throw new ValidationException("ordineIncongruenzaTitoloInesistente",
								ValidationExceptionCodici.ordineIncongruenzaTitoloInesistente);
					}


					// Controllo fra la natura del titolo selezionato il valore del campo natura
	 					if (ordine.getNaturaOrdine()!=null && ordine.getNaturaOrdine().length()!=0)
	 					{
							if (!natura.equals(ordine.getNaturaOrdine()))
							{
								motivo=4;
								throw new ValidationException("ordineIncongruenzaNaturaTitNaturaOrd",
										ValidationExceptionCodici.ordineIncongruenzaNaturaTitNaturaOrd);

							}
	 					}
	 					else
	 					{
	 						// se non specificato nella maschera deve essere impostato alla natura del titolo associato
	 						ordine.setNaturaOrdine(natura);
	 					}
	 					// Controllo fra la descrizione del titolo selezionato il valore del campo
	 					if (ordine.getTitolo()!=null && ordine.getTitolo().getDescrizione()!=null && ordine.getTitolo().getDescrizione().length()!=0 )
						{
							if (!isbd.equals(ordine.getTitolo().getDescrizione()))
							{
								/*
								motivo=4;
								throw new ValidationException("ordineIncongruenzaDescrTitTitOrd",
										ValidationExceptionCodici.ordineIncongruenzaDescrTitTitOrd);	*/
							}
	 					}
	 					else
	 					{
	 						// se non specificato nella maschera deve essere impostato alla natura del titolo associato
	 						ordine.getTitolo().setDescrizione(isbd);
	 					}

				}
				else
				{
					valRitornoInsTit=false;
					throw new ValidationException("ordineIncongruenzaTitoloInesistente",
							ValidationExceptionCodici.ordineIncongruenzaTitoloInesistente);
				}
			}


			//sql0=sql0 + " join tb_titolo tit on tit.bid=ord.bid ";
			if (ordine.isGestSez() &&  ordine.getSezioneAcqOrdine()!=null && ordine.getSezioneAcqOrdine().length()!=0)
			{
				// subquery per test di esistenza sezione: da sostituire con metodo ancora da scrivere in this
				String sqlSub="select * from tba_sez_acquis_bibliografiche sez where ";
				sqlSub=sqlSub + " sez.fl_canc<>'S'";
				sqlSub=sqlSub + " and  upper(sez.cod_sezione)='" +ordine.getSezioneAcqOrdine().trim().toUpperCase()+"'";
				sqlSub=sqlSub + " and sez.cd_bib='" +ordine.getCodBibl()+"'";
				sqlSub=sqlSub + " and sez.cd_polo='" +ordine.getCodPolo()+"'";
				sqlSub=sqlSub + " and (sez.data_val is null or sez.data_val>(SELECT CURRENT_DATE ))";

				pstmt = connection.prepareStatement(sqlSub);
				rsSub = pstmt.executeQuery();
				if (!rsSub.next()) {
					motivo=8;
					throw new ValidationException("ordineIncongruenzaSezioneInesistente",
							ValidationExceptionCodici.ordineIncongruenzaSezioneInesistente);
				}
				else
				{
					ordine.setIDSez(rsSub.getInt("id_sez_acquis_bibliografiche"));
				}
				rsSub.close();

				sql0=sql0 + " join tba_sez_acquis_bibliografiche sez on sez.id_sez_acquis_bibliografiche=ord.id_sez_acquis_bibliografiche and sez.fl_canc<>'S' ";
			}

			if (ordine.getValutaOrdine()==null && ordine.getIDVal()==0)
			{
				ordine.setValutaOrdine("EUR"); // SUCCESSIVAMENTE VIENE CREATA LA VALUTA SE INESISTENTE
			}

			if (ordine.getValutaOrdine()!=null && ordine.getValutaOrdine().length()!=0)
			{
				// subquery per test di esistenza valuta di biblioteca
				String sqlSub="select * from tba_cambi_ufficiali  where ";
				sqlSub=sqlSub + " fl_canc<>'S'";
				sqlSub=sqlSub + " and cd_polo='" +ordine.getCodPolo()+"'";
				sqlSub=sqlSub + " and cd_bib='" +ordine.getCodBibl()+"'";
				sqlSub=sqlSub + " and upper(valuta)='" +ordine.getValutaOrdine().trim().toUpperCase()+"'";
				pstmt = connection.prepareStatement(sqlSub);
				rsSub = pstmt.executeQuery();
				if (!rsSub.next()) {
					throw new ValidationException("ordineIncongruenzaValutaInesistente",
								ValidationExceptionCodici.ordineIncongruenzaValutaInesistente);
				} else
					ordine.setIDVal(rsSub.getInt("id_valuta"));

				rsSub.close();
				sql0=sql0 + " join tba_cambi_ufficiali val on val.id_valuta=ord.id_valuta  and val.fl_canc<>'S'";
			}

			if (ordine.getFornitore()!=null && ordine.getFornitore().getCodice()!=null && ordine.getFornitore().getCodice().length()!=0)
			{
				// subquery per test di esistenza fornitore : da sostituire con metodo ancora da scrivere in this
				String sqlSub="select *,  fornBibl.cod_fornitore as codFornBibl  from tbr_fornitori forn  ";
				sqlSub=sqlSub + " left join tbr_fornitori_biblioteche fornBibl on fornBibl.cod_fornitore=forn.cod_fornitore  and fornBibl.cd_biblioteca='" + ordine.getCodBibl()+"' and fornBibl.cd_polo='" + ordine.getCodPolo()+"' and fornBibl.fl_canc<>'S'" ;
				sqlSub=sqlSub + " where forn.fl_canc<>'S'";
				sqlSub=sqlSub + " and forn.cod_fornitore=" +ordine.getFornitore().getCodice();

				pstmt = connection.prepareStatement(sqlSub);
				rsSub = pstmt.executeQuery();
				if (!rsSub.next()) {
					motivo=6;
					throw new ValidationException("ordineIncongruenzaFornitoreInesistente",
							ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);
				}
				else
				{

					// localizzazione del fornitore in caso di utilizzo nella creazione ordine
					if (rsSub.getString("codFornBibl")==null ||  (rsSub.getString("codFornBibl")!=null && rsSub.getString("codFornBibl").trim().length()==0)  )
					{
						// esiste il fornitore in anagrafica e non esiste fra quelli di biblioteca
						String sqlFB="insert into tbr_fornitori_biblioteche values ( " ;
						Timestamp ts = DaoManager.now();
						sqlFB= sqlFB + "'" +  ordine.getCodPolo() + "'" ;  // cd_bib
						sqlFB= sqlFB + ",'" +  ordine.getCodBibl() + "'" ;  // cd_biblioteca
						sqlFB= sqlFB + "," +  ordine.getFornitore().getCodice()  ;  // cod_fornitore
						sqlFB= sqlFB  + ",''";  // tipo_pagamento
						sqlFB= sqlFB  + ",''";  // cod_cliente
						sqlFB= sqlFB  + ",''";  // nom_contatto
						sqlFB= sqlFB  + ",''";  // tel_contatto
						sqlFB= sqlFB  + ",''";  // fax_contatto
						sqlFB= sqlFB  + ",'EUR'" ;  // valuta
						sqlFB= sqlFB  + ",'" +  ordine.getCodPolo() + "'" ;  // cod_polo
						sqlFB= sqlFB  + ",' '" ;  // allinea
						sqlFB= sqlFB + ",'" + ordine.getUtente() + "'" ;   // ute_ins
						sqlFB= sqlFB + ",'" + ts + "'" ;   // ts_ins
						sqlFB= sqlFB + ",'" + ordine.getUtente() + "'" ;   // ute_var
						sqlFB= sqlFB + ",'" + ts + "'";   // ts_var
						sqlFB= sqlFB + ",'N'";   // fl_canc
						sqlFB= sqlFB  + ")" ;  // cod_polo_bib
						pstmtInsFornBibl = connection.prepareStatement(sqlFB);
						int intRetFB = 0;
						intRetFB = pstmtInsFornBibl.executeUpdate();
						pstmtInsFornBibl.close();
						// fine estrazione codice
						if (intRetFB==1){
							valRitornoInsFornBibl=true;
							//messaggio di localizzaziojne del fornitore
						} else {
							valRitornoInsFornBibl=false;
							throw new ValidationException("ordineIncongruenzaTipoFornTipoOrd",
									ValidationExceptionCodici.ordineIncongruenzaTipoFornTipoOrd);
						}
					}

					if (ordine.getTipoOrdine().equals("R") )
					{
						if (!rsSub.getString("tipo_partner").equals("R"))
						{
							motivo=3;
							throw new ValidationException("ordineIncongruenzaTipoFornTipoOrd",
									ValidationExceptionCodici.ordineIncongruenzaTipoFornTipoOrd);
						}
					}
					denoFornitore=rsSub.getString("nom_fornitore");
					ordine.getFornitore().setDescrizione(denoFornitore);
				}
				rsSub.close();

				sql0=sql0 + " join tbr_fornitori forn on forn.cod_fornitore=ord.cod_fornitore and forn.fl_canc<>'S'";
			}

			if (ordine.isGestBil() && ordine.getBilancio()!=null &&  ordine.getBilancio().getCodice1()!=null && ordine.getBilancio().getCodice1().length()!=0)
			{
				sql0=sql0 + " join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=ord.id_capitoli_bilanci and capBil.fl_canc<>'S' ";
				sql0=sql0 + " join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and bil.fl_canc<>'S' ";

				// subquery per test di esistenza bilancio : da sostituire con metodo ancora da scrivere in this
				String sqlSub="select * from tbb_capitoli_bilanci capBil  ";
				sqlSub=sqlSub + " join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and bil.fl_canc<>'S'";
				sqlSub=sqlSub + " where capBil.fl_canc<>'S'";
				sqlSub=sqlSub + " and capBil.cd_polo='" +ordine.getCodPolo()+"'";
				sqlSub=sqlSub + " and capBil.cd_bib='" +ordine.getCodBibl()+"'";
				if (ordine.getBilancio().getCodice1()!=null && ordine.getBilancio().getCodice1().length()!=0)
				{
					sqlSub=sqlSub + " and capBil.esercizio=" +ordine.getBilancio().getCodice1();
				}
				if (ordine.getBilancio().getCodice2()!=null && ordine.getBilancio().getCodice2().length()!=0)
				{
					sqlSub=sqlSub + " and capBil.capitolo=" +ordine.getBilancio().getCodice2();
				}
				if (ordine.getBilancio().getCodice3()!=null && ordine.getBilancio().getCodice3().length()!=0)
				{
					sql0=sql0 + " and bil.cod_mat=ord.tbb_bilancicod_mat ";
					sqlSub=sqlSub + " and bil.cod_mat='" +ordine.getBilancio().getCodice3()+ "'";
				}
				pstmt = connection.prepareStatement(sqlSub);
				rsSub = pstmt.executeQuery();
				if (!rsSub.next()) {
					motivo=5;
					throw new ValidationException("ordineIncongruenzaBilancioInesistente",
							ValidationExceptionCodici.ordineIncongruenzaBilancioInesistente);
				}
				else
				{
					ordine.setIDBil(rsSub.getInt("id_capitoli_bilanci"));
				}

				rsSub.close();

			}
			if (ordine.getCodSuggBiblOrdine()!=null && ordine.getCodSuggBiblOrdine().length()!=0)
			{
				sql0=sql0 + " join tba_suggerimenti_bibliografici docsugg on docsugg.cod_sugg_bibl=ord.cod_sugg_bibl and docsugg.cd_bib=ord.cd_bib and docsugg.fl_canc<>'S'";
			}
			if (ordine.getCodRicOffertaOrdine()!=null && ordine.getCodRicOffertaOrdine().length()!=0)
			{
				sql0=sql0 + " join tba_richieste_offerta richoff on richoff.cod_rich_off=ord.cod_rich_off and richoff.cd_bib=ord.cd_bib and richoff.fl_canc<>'S'";
			}
			if (ordine.getIdOffertaFornOrdine()!=null && ordine.getIdOffertaFornOrdine().length()!=0)
			{
				sql0=sql0 + " join tba_offerte_fornitore offforn on offforn.bid_p=ord.bid_p and offforn.cd_bib=ord.cd_bib and offforn.fl_canc<>'S'";
			}
			if (ordine.getCodDocOrdine()!=null && ordine.getCodDocOrdine().length()!=0)
			{
				sql0=sql0 + " join tbl_documenti_lettori doclett  on doclett.cod_doc_lett=ord.cod_doc_lett and doclett.cd_bib=ord.cd_bib and doclett.fl_canc<>'S'";
			}

			sql0=sql0 + " where  ord.fl_canc<>'S'";

			if (ordine.getCodPolo()!=null && ordine.getCodPolo().length()!=0  )
			{
				//TODO  gestire le biblioteche affiliate a fronte della selezione del bottone relativo
				sql0=this.struttura(sql0);
				sql0=sql0 + " ord.cd_polo='" + ordine.getCodPolo() +"'";
			}

			if (ordine.getCodBibl()!=null && ordine.getCodBibl().length()!=0  )
			{
				//TODO  gestire le biblioteche affiliate a fronte della selezione del bottone relativo
				sql0=this.struttura(sql0);
				sql0=sql0 + " ord.cd_bib='" + ordine.getCodBibl() +"'";
			}

			if (ordine.getCodOrdine()!=null && ordine.getCodOrdine().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " ord.cod_ord=" + ordine.getCodOrdine() ;
			}

			if (ordine.getTipoOrdine()!=null && ordine.getTipoOrdine().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " ord.cod_tip_ord='" + ordine.getTipoOrdine() +"'";
			}

			if (ordine.getNaturaOrdine()!=null && ordine.getNaturaOrdine().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " ord.natura='" + ordine.getNaturaOrdine() +"'";
			}
			if (ordine.getFornitore().getDescrizione()!=null && ordine.getFornitore().getDescrizione().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " forn.nom_fornitore='" + ordine.getFornitore().getDescrizione().replace("'","''") +"'";
			}
			if (ordine.getFornitore()!=null && ordine.getFornitore().getCodice()!=null && ordine.getFornitore().getCodice().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " forn.cod_fornitore='" + ordine.getFornitore().getCodice() +"'";
			}
			if ( ordine.isGestBil() && ordine.getBilancio()!=null  && ordine.getBilancio().getCodice1()!=null && ordine.getBilancio().getCodice1().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " capBil.esercizio=" + ordine.getBilancio().getCodice1() ;
			}
			if (ordine.isGestBil() && ordine.getBilancio()!=null  && ordine.getBilancio().getCodice2()!=null && ordine.getBilancio().getCodice2().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " capBil.capitolo=" + ordine.getBilancio().getCodice2() ;
			}
			if (ordine.isGestBil() && ordine.getBilancio()!=null  && ordine.isGestBil() && ordine.getBilancio().getCodice3()!=null && ordine.getBilancio().getCodice3().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " bil.cod_mat='" + ordine.getBilancio().getCodice3() +"'";
			}
			if (ordine.getTitolo()!=null  && ordine.getTitolo().getCodice()!=null && ordine.getTitolo().getCodice().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " ord.bid ='" + ordine.getTitolo().getCodice() +"'";
			}


			if (ordine.isGestSez() && ordine.getSezioneAcqOrdine()!=null && ordine.getSezioneAcqOrdine().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " upper(sez.cod_sezione)='" + ordine.getSezioneAcqOrdine().trim().toUpperCase() +"'";
			}
			if (ordine.getCodSuggBiblOrdine()!=null && ordine.getCodSuggBiblOrdine().length()!=0 )
			{
				String[] chiaveCompostaCodSuggBiblOrdine2=ordine.getCodSuggBiblOrdine().split("\\|");
 				if (chiaveCompostaCodSuggBiblOrdine2!=null &&  chiaveCompostaCodSuggBiblOrdine2.length>1)
 				{
 					if (chiaveCompostaCodSuggBiblOrdine2[1]!=null && chiaveCompostaCodSuggBiblOrdine2[1].length()!=0)
 					{
 						sql0=this.struttura(sql0);
 						//numerico
 						sql0=sql0 + " ord.cod_sugg_bibl=" + chiaveCompostaCodSuggBiblOrdine2[1] ;
 					}
 				}
 				if (chiaveCompostaCodSuggBiblOrdine2!=null &&  chiaveCompostaCodSuggBiblOrdine2.length==1)
 				{
						sql0=this.struttura(sql0);
	 					sql0=sql0 + " ord.cod_sugg_bibl=" + chiaveCompostaCodSuggBiblOrdine2[0] ;
				}
			}
			if (ordine.getCodRicOffertaOrdine()!=null && ordine.getCodRicOffertaOrdine().length()!=0 )
			{
				String[] chiaveCompostaCodRicOffertaOrdine2=ordine.getCodRicOffertaOrdine().split("\\|");
 				if (chiaveCompostaCodRicOffertaOrdine2!=null &&  chiaveCompostaCodRicOffertaOrdine2.length>1)
 				{
 					if (chiaveCompostaCodRicOffertaOrdine2[1]!=null && chiaveCompostaCodRicOffertaOrdine2[1].length()!=0)
 					{
 						//numerico
 						sql0=this.struttura(sql0);
 						sql0=sql0 + " ord.cod_rich_off='" + chiaveCompostaCodRicOffertaOrdine2[1] +"'";
 					}
 				}
 				if (chiaveCompostaCodRicOffertaOrdine2!=null &&  chiaveCompostaCodRicOffertaOrdine2.length==1)
 				{
 						sql0=this.struttura(sql0);
	 					sql0=sql0 + " ord.cod_rich_off='" + chiaveCompostaCodRicOffertaOrdine2[0] +"'";
 				}
			}


			if (ordine.getIdOffertaFornOrdine()!=null && ordine.getIdOffertaFornOrdine().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " ord.bid_p='" + ordine.getIdOffertaFornOrdine() +"'";
			}
			if (ordine.getCodDocOrdine()!=null && ordine.getCodDocOrdine().length()!=0)
			{
				// numerico
				sql0=this.struttura(sql0);
				sql0=sql0 + " ord.cod_doc_lett ='" + ordine.getCodDocOrdine() +"'";
			}


			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			valRitornoPrep=true; // se la esegue c'è un resultset
			// numero di righe del resultset
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;

			} //
			rs.close();
			connection.close();
			//
			// controllo preventivo di esistenza di inventari non rientrati in altri ordini di rilegatura non chiusi
			connection = getConnection();
			if (ordine.getTipoOrdine()!=null && ordine.getTipoOrdine().equals("R") )
			{
				if (ordine.getRigheInventariRilegatura()!=null && ordine.getRigheInventariRilegatura().size() >0 )
				{
					//valRitornoINSLOOP=false;
					for (int i=0; i<ordine.getRigheInventariRilegatura().size(); i++)
					{
						StrutturaInventariOrdVO 	oggettoDettFattVO=ordine.getRigheInventariRilegatura().get(i);
						// controllo preventivo su esistenza inventario su altri ordini di rilegatura che non è rientrato
						// lo stato di un ordine di rilegatura pu; essere chiuso solo se sono rientrati tutti gli inventari
						String sqlCOTROLLO="select * from tra_ordine_inventari ordInv  ";
						sqlCOTROLLO=sqlCOTROLLO + " join tba_ordini ord on ord.id_ordine=ordInv.id_ordine and ord.fl_canc<>'S' and ord.stato_ordine <> 'N'"; // stato_ordine non chiuso o annullato?
						if (ordine.getCodPolo() !=null &&  ordine.getCodPolo().length()!=0)
						{
							sqlCOTROLLO=this.struttura(sqlCOTROLLO);
							sqlCOTROLLO=sqlCOTROLLO + " ordInv.cd_polo='" + ordine.getCodPolo() +"'";
						}

						if (ordine.getCodBibl() !=null &&  ordine.getCodBibl().length()!=0)
						{
							sqlCOTROLLO=this.struttura(sqlCOTROLLO);
							sqlCOTROLLO=sqlCOTROLLO + " ordInv.cd_bib='" + ordine.getCodBibl() +"'";
						}

						sqlCOTROLLO=this.struttura(sqlCOTROLLO);
						sqlCOTROLLO=sqlCOTROLLO + "  ord.stato_ordine<>'C' and ord.stato_ordine<>'N'";

						sqlCOTROLLO=this.struttura(sqlCOTROLLO);
						sqlCOTROLLO=sqlCOTROLLO + "  ordInv.id_ordine !=" +ordine.getIDOrd() ;

						sqlCOTROLLO=this.struttura(sqlCOTROLLO);
						sqlCOTROLLO=sqlCOTROLLO + " ordInv.cd_serie='" +oggettoDettFattVO.getSerie() + "'";

						sqlCOTROLLO=this.struttura(sqlCOTROLLO);
						sqlCOTROLLO=sqlCOTROLLO + " ordInv.cd_inven=" +oggettoDettFattVO.getNumero() ;

						sqlCOTROLLO=this.struttura(sqlCOTROLLO);
						//sqlCOTROLLO=sqlCOTROLLO + " ordInv.data_rientro=null";
						sqlCOTROLLO=sqlCOTROLLO +  " ordInv.data_rientro is null";


						pstmtCOTROLLO = connection.prepareStatement(sqlCOTROLLO);
						rsCOTROLLO = pstmtCOTROLLO.executeQuery(); // va in errore se non può restituire un recordset
						// numero di righe del resultset
						Tba_ordini o = null;
						while (rsCOTROLLO.next()) {
							Tba_ordiniDao odao = new Tba_ordiniDao();
							o = odao.getOrdineById(rsCOTROLLO.getInt("id_ordine"));
						} //
						if (o != null)
						{
							ValidationException ve = new ValidationException(SbnErrorTypes.ACQ_ERRORE_INVENTARI_NON_RIENTRATI,
									oggettoDettFattVO.getChiave(),
									ConversioneHibernateVO.toWeb().ordine(o).getChiaveOrdine() );
							ve.addException(new ValidationException(SbnErrorTypes.ACQ_POSIZIONE_INVENTARIO,
										oggettoDettFattVO.getPosizione().toString(), oggettoDettFattVO.getVolume().toString()) );
							throw ve;
						}
						rsCOTROLLO.close();

					}
				}
			}
			connection.close();

			//
			if (motivo==0)
			{
				rec = new OrdiniVO();
				connection = getConnection();
				String sql="insert into tba_ordini " ;
				// istruzione errata volutamente per test
				sql +=" ( cd_polo, cd_bib, cod_tip_ord, anno_ord, cod_ord, cod_fornitore, id_sez_acquis_bibliografiche, id_valuta, id_capitoli_bilanci, data_ins, data_agg, data_ord, note, num_copie, continuativo, stato_ordine, tipo_doc_lett, cod_doc_lett, tipo_urgenza, cod_rich_off, bid_p, note_forn, tipo_invio, anno_1ord, cod_1ord, prezzo, paese, cod_bib_sugg, cod_sugg_bibl, bid, stato_abb, cod_per_abb, prezzo_lire, reg_trib, anno_abb, num_fasc, data_fasc, annata, num_vol_abb, natura, data_fine, stampato, rinnovato, data_chiusura_ord, tbb_bilancicod_mat, ute_ins, ute_var, ts_ins, ts_var, fl_canc, cd_tipo_lav) ";
				sql +=" values ( ";
				//sql +=" nextval('tba_ordini_id_ordine_seq') " ;  // id_ordine
				Timestamp ts = DaoManager.now();
				sql +=" '" +  ordine.getCodPolo() + "'" ;  // cd_polo
				sql +=", '" +  ordine.getCodBibl() + "'" ;  // cd_bib 3
				sql +=", '" + ordine.getTipoOrdine() + "'";  // cod_tip_ord 4
				sql +=", (SELECT EXTRACT(YEAR FROM TIMESTAMP '" + ts  + "'))";  // anno_ord 5
				// INIZIO SUBQUERY
				sql +=", (SELECT CASE WHEN  (MAX(tba_ordini.cod_ord) is null) THEN 1  else MAX(tba_ordini.cod_ord)+1  END " ;
				sql +=" from tba_ordini   ";
				sql +=" where tba_ordini.cd_bib='" + ordine.getCodBibl() +"'";
				sql +=" and  tba_ordini.cd_polo='" + ordine.getCodPolo() +"'";
				sql +=" and  tba_ordini.cod_tip_ord='" + ordine.getTipoOrdine() +"'";
				sql +=" and tba_ordini.anno_ord = (SELECT EXTRACT(YEAR FROM TIMESTAMP '" + ts  + "'))" ;
				sql +=" )";  // fine subquery
				if (ordine.getFornitore().getCodice()!=null && ordine.getFornitore().getCodice().length()!=0)
				{
					sql +=", " +Integer.parseInt(ordine.getFornitore().getCodice()) ;
				}
				else
				{
					sql +=", 0" ; // cod_fornitore  ?????
				}

				// condizione su gestSez

				if (ordine.isGestSez() && ordine.getSezioneAcqOrdine()!=null &&  ordine.getSezioneAcqOrdine().trim().length()!=0)
				{
					sql +=","+  ordine.getIDSez() ;  // id_sezione 26
				}
				else
				{
					sql +=",null" ; //id_sezione
				}
				if (ordine.getValutaOrdine()!=null &&  ordine.getValutaOrdine().trim().length()!=0)
				{
					sql +=",'"+ ordine.getIDVal() + "'";  // valuta 23
				}
				else
				{
					sql +=",null" ; //valuta
				}

				// condizione su gestBil
				if (ordine.isGestBil() && ordine.getIDBil()!=0)
				{
					sql +=","+  ordine.getIDBil() ;  // id_capitoli_bilanci 26

				}
				else
				{
					sql +=",null" ; //id_capitoli_bilanci 26
				}

				sql +=",'" + ts + "'";   // data_ins 1
				sql +=",'" + ts + "'" ;  // data_agg 2- 03/07/09 tale campo è utilizzato per memorizzare lo stesso timestamp degli ordini stampati di un buono d'ordine
				sql +=", (SELECT CURRENT_DATE ) ";  // data_ord



				sql +=",'"+ ordine.getNoteOrdine().replace("'","''")+ "'";  // note 6
				sql +=","+  ordine.getNumCopieOrdine() ;  // num_copie 7
				if (ordine.isContinuativo())
					{
					sql +=",'1'" ;  // continuativo 8
					}
					else
					{
					sql +=",'0'" ;  // continuativo 8
					}
				if (ordine.getStatoOrdine()!=null && ordine.getStatoOrdine().length()!=0)
				{
					sql +=",'"+ ordine.getStatoOrdine()+"' ";  // stato_ordine 9
				}
				else
				{
					sql +=",'A'";   // stato_ordine imposto in assenza ?????
				}

				if (ordine.getCodDocOrdine()!=null && ordine.getCodDocOrdine().length()!=0)
					{
						sql +=",'S'";  // tipo_doc_lett 10
						sql +=","+ Integer.parseInt(ordine.getCodDocOrdine()) ;  // cod_doc_lett 11
					}
					else
					{
						sql +=",null";  // tipo_doc_lett 10
						sql +=",null";   // cod_doc_lett  ?????
					}

 				sql +=",'"+ ordine.getCodUrgenzaOrdine()+"'";  // tipo_urgenza 12

				if (ordine.getCodRicOffertaOrdine()!=null && ordine.getCodRicOffertaOrdine().length()!=0 )
				{
					String[] chiaveCompostaCodRicOffertaOrdine=ordine.getCodRicOffertaOrdine().split("\\|");
	 				if (chiaveCompostaCodRicOffertaOrdine!=null &&  chiaveCompostaCodRicOffertaOrdine.length>1)
	 				{
		 				if (chiaveCompostaCodRicOffertaOrdine[1]!=null && chiaveCompostaCodRicOffertaOrdine[1].length()!=0)
						{
							sql +="," + Integer.parseInt(chiaveCompostaCodRicOffertaOrdine[1]) ;  // cod_rich_off 13
						}
						else
						{
							sql +=",null" ; // cod_rich_off  ?????
						}
	 				}
	 				if (chiaveCompostaCodRicOffertaOrdine!=null &&  chiaveCompostaCodRicOffertaOrdine.length==1)
	 				{
						sql +="," + Integer.parseInt(chiaveCompostaCodRicOffertaOrdine[0]) ;  // cod_rich_off 13
	 				}
				}
				else
				{
					sql +=",null" ; // cod_rich_off  ?????
				}


				//sql +=",'"+ ordine.getIdOffertaFornOrdine() +"'";  // bid_p 14

				if (ordine.getIdOffertaFornOrdine()!=null && ordine.getIdOffertaFornOrdine().length()!=0)
				{
					sql +=",'"+ ordine.getIdOffertaFornOrdine() + "'" ;  // bid_p 14
				}
				else
				{
					sql +=",null";   // bid_p 14
				}





				sql +=",'" +ordine.getNoteFornitore().replace("'","''")+ "'";  // note_forn 16
				sql +=",'" +ordine.getTipoInvioOrdine()+ "'";  // tipo_invio 17
			if (ordine.getAnnoPrimoOrdine()!=null && ordine.getAnnoPrimoOrdine().length()!=0)
					{
						sql +=","+ Integer.parseInt(ordine.getAnnoPrimoOrdine()) ;  // anno_1ord 21
					}
					else
					{
						sql +=", 0"; // anno_1ord
					}
				if (ordine.getCodPrimoOrdine()!=null && ordine.getCodPrimoOrdine().length()!=0)
					{
						sql +=","+ Integer.parseInt(ordine.getCodPrimoOrdine()) ;  // cod_1ord 22
					}
					else
					{
						sql +=",0"; // cod_1ord
					}
				//sql +=",'"+ ordine.getValutaOrdine() + "'";  // valuta 23

				sql +=","+ ordine.getPrezzoOrdine() ;  // prezzo 24

				sql +=",'"+ ordine.getPaeseOrdine() +"'";  // paese 25

				//sql +=",'"+  ordine.getSezioneAcqOrdine() + "'";  // cod_sezione 26

				if (ordine.getCodSuggBiblOrdine()!=null && ordine.getCodSuggBiblOrdine().length()!=0 )
				{
					String[] chiaveCompostaCodSuggBiblOrdine=ordine.getCodSuggBiblOrdine().split("\\|");

	 				if (chiaveCompostaCodSuggBiblOrdine!=null &&  chiaveCompostaCodSuggBiblOrdine.length>1)
	 				{
						if (chiaveCompostaCodSuggBiblOrdine[1]!=null && chiaveCompostaCodSuggBiblOrdine[1].length()!=0)
						{
							sql +=",'"+ chiaveCompostaCodSuggBiblOrdine[0]+ "'";  // cd_bib_sugg 27
							sql +=","+ Integer.parseInt(chiaveCompostaCodSuggBiblOrdine[1]);  // cod_sugg_bibl 28
						}
						else
						{
							sql +=",null"; // cd_bib_sugg
							sql +=",null"; // cod_sugg_bibl
						}
	 				}
	 				if (chiaveCompostaCodSuggBiblOrdine!=null &&  chiaveCompostaCodSuggBiblOrdine.length==1)
	 				{
						if ( chiaveCompostaCodSuggBiblOrdine[0]!=null && chiaveCompostaCodSuggBiblOrdine[0].length()!=0)
						{
							sql +=",null";  // cd_bib_sugg 27
							sql +=","+ Integer.parseInt(chiaveCompostaCodSuggBiblOrdine[0]);  // cod_sugg_bibl 28
						}
						else
						{
							sql +=",null"; // cd_bib_sugg
							sql +=",null"; // cod_sugg_bibl
						}

	 				}
				}
				else
				{
					sql +=",null"; // cd_bib_sugg
					sql +=",null"; // cod_sugg_bibl
				}





				sql +=",'"+ ordine.getTitolo().getCodice()+ "'";  // bid 29
				sql +=",'"+ ordine.getStatoAbbOrdine() +"'";  // stato_abb 30
				sql +=",'"+ ordine.getPeriodoValAbbOrdine()+"'";  // cod_per_abb 31
				sql +=", "+ ordine.getPrezzoEuroOrdine() ;  // prezzo_lire  32


				sql +=",'"+ ordine.getRegTribOrdine() +"'";  // reg_trib 33
				if (ordine.getAnnoAbbOrdine()!=null && ordine.getAnnoAbbOrdine().length()!=0)
					{
						sql +=","+ Integer.parseInt(ordine.getAnnoAbbOrdine()) ;  // anno_abb  34
					}
					else
					{
						sql +=",0";  // anno_abb
					}

				sql +=",'"+ ordine.getNumFascicoloAbbOrdine() +"'";  // num_fasc 35


				if (ordine.getDataPubblFascicoloAbbOrdine()!=null && ordine.getDataPubblFascicoloAbbOrdine().length()!=0)
					{
						sql +=", TO_DATE('" + ordine.getDataPubblFascicoloAbbOrdine()+"','dd/MM/yyyy') ";  //data_fasc; 36
					}
					else
					{
						sql +=", null"; // data_fasc
					}
				sql +=",'"+ ordine.getAnnataAbbOrdine()+"'";  // annata 37
				sql +=","+ ordine.getNumVolAbbOrdine() ;  // num_vol_abb 38
				sql +=",'"+ordine.getNaturaOrdine() +"'";  // natura 39

				if (ordine.getDataFineAbbOrdine()!=null && ordine.getDataFineAbbOrdine().length()!=0)
					{
						sql +=", TO_DATE('"+ ordine.getDataFineAbbOrdine()+ "','dd/MM/yyyy') ";  // data_fine; 40
					}
					else
					{
						sql +=",null";  // data_fine
					}
				//Stampato ????

				sql +=","+ordine.isStampato() ;  // stampato
				sql +=","+ordine.isRinnovato() ;  // rinnovato
				//sql +=", nextval('seq_tba_ordini') " ;  // id_ordine
				if (ordine.getDataChiusura()!=null && ordine.getDataChiusura().length()!=0)
				{
					sql +=", '"+  ts + "'";  // data_chiusura_ord
				}
				else
				{
					sql +=",null ";  // data_chiusura_ord
				}


				if (ordine.isGestBil() && ordine.getBilancio()!=null && ordine.getBilancio().getCodice3()!=null && ordine.getBilancio().getCodice3().trim().length()!=0)
				{
					sql +=","+ Integer.parseInt(ordine.getBilancio().getCodice3());  // tbb_bilancicod_mat 20
				}
				else
				{
					//sql +=",0" ; //  cod_mat
					sql +=",null" ;
				}
				sql +=",'" + ordine.getUtente() + "'" ;   // ute_ins
				sql +=",'" + ordine.getUtente() + "'" ;   // ute_var
				sql +=",'" + ts + "'" ;   // ts_ins
				sql +=",'" + ts + "'";   // ts_var
				sql +=",'N'";   // fl_canc

		// fine subquery

				//almaviva5_20121115 evolutive google
				sql += "," + trimAndQuote(ordine.getCd_tipo_lav());


				sql +=" )";

				//sql +=" ord.cd_bib='" + ordine.getChiave() +"'";

				pstmt = connection.prepareStatement(sql);
				log.debug("Debug: inserimento ordine");
				log.debug("Debug: " + sql);
					int intRet = 0;
					// IMPOSTAZIONE DELL'UNICO PARAMETRO
					intRet = pstmt.executeUpdate();
					//connection.commit();
					//rs.close();
					pstmt.close();
					connection.close();

					// estrazione del codice ed attribuzione del codice all'ordine visualizzato
					// fine estrazione codice
					connection = getConnection();

					if (intRet == 1) {
						valRitorno = true;
						StringBuilder sqlCodice = new StringBuilder();
						sqlCodice.append("select id_ordine,cod_ord,ts_var from tba_ordini");
						sqlCodice.append(" where ute_ins='").append(ordine.getUtente()).append("'");
						sqlCodice.append(" order by ts_ins desc limit 1");
						pstmt = connection.prepareStatement(sqlCodice.toString());
						rsSub = pstmt.executeQuery();
						if (rsSub.next()) {
							ordine.setIDOrd(rsSub.getInt("id_ordine"));
							ordine.setCodOrdine(rsSub.getString("cod_ord"));
							ordine.setDataUpd(rsSub.getTimestamp("ts_var"));
						}
						log.debug("inserito ordine: " + ordine);
						rsSub.close();
						pstmt.close();
						// controllo se l'ordine è stato creato con l'importa da
						// in tal caso vanno aggiornate le tabelle relative per il cambio di stato in ordinato
						//cod_sugg_bibl, cod_bib_sugg (sugg bibl), cod_doc_lett (sugg lett), tipo_doc_lett, cod_rich_off (gare), bid_p(offerta forn)

						// GESTIONE DELL'IMPORTA DA

						// Sugg. lett.
						if (ordine.getCodDocOrdine()!=null && ordine.getCodDocOrdine().trim().length()>0)
						{
							// update id_documenti_lettore, stato_sugg
							String sqlUDPSTATOIMPORTA="update tbl_documenti_lettori set " ;
							//sqlUDP= sqlUDP + " ts_ins = '" +  ts  + "'" ;  // ts_ins
							sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA + " ts_var = '" +  ts  + "'" ;  // ts_var
							sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA + ", ute_var = '" +  ordine.getUtente()  + "'" ;  // ute_var
							sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA + ", stato_sugg= 'O'" ;
							// CONDIZIONI
							sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA  + " where tipo_doc_lett='S'";
							sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA + " and fl_canc<>'S' ";
							sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA + " and id_documenti_lettore=" + Integer.valueOf(ordine.getCodDocOrdine());
							pstmtUDPSTATOIMPORTA = connection.prepareStatement(sqlUDPSTATOIMPORTA);
							int intRetUDPSTATOIMPORTA = 0;
							intRetUDPSTATOIMPORTA = pstmtUDPSTATOIMPORTA.executeUpdate();
							pstmtUDPSTATOIMPORTA.close();
							// fine estrazione codice
							if (intRetUDPSTATOIMPORTA==1){
								//valRitornoUDPSTATOIMPORTA=true;
							}

						}
						// Sugg. bibliotec.
						if (ordine.getCodSuggBiblOrdine()!=null && ordine.getCodSuggBiblOrdine().trim().length()>0	)
						{
							//	 update chiave cd_polo  cd_bib  cod_sugg_bibl, stato_sugg
							String sqlUDPSTATOIMPORTA="update tba_suggerimenti_bibliografici set " ;
							//sqlUDP= sqlUDP + " ts_ins = '" +  ts  + "'" ;  // ts_ins
							sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA + " ts_var = '" +  ts  + "'" ;  // ts_var
							sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA + ", ute_var = '" +  ordine.getUtente()  + "'" ;  // ute_var
							sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA + ", stato_sugg= 'O'" ;
							// CONDIZIONI
							sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA + " where fl_canc<>'S' ";
							String[] chiaveComposta=ordine.getCodSuggBiblOrdine().split("\\|");
			 				if (chiaveComposta!=null )
			 				{
								if (chiaveComposta!=null &&  chiaveComposta.length>1)
				 				{
									sqlUDPSTATOIMPORTA=this.struttura(sqlUDPSTATOIMPORTA);
									sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA   + " cd_bib='" + chiaveComposta[0] +"'";
									sqlUDPSTATOIMPORTA=this.struttura(sqlUDPSTATOIMPORTA);
									sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA    + " cod_sugg_bibl=" + chiaveComposta[1] ;
				 				}
				 				if (chiaveComposta!=null &&  chiaveComposta.length==1)
				 				{
									sqlUDPSTATOIMPORTA=this.struttura(sqlUDPSTATOIMPORTA);
									sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA    + " cod_sugg_bibl=" + chiaveComposta[0] ;
				 				}

								//sqlUDPSTATOIMPORTA=this.struttura(sqlUDPSTATOIMPORTA);
								//sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA  + " cd_polo='" + chiaveComposta[0] +"'";
								pstmtUDPSTATOIMPORTA = connection.prepareStatement(sqlUDPSTATOIMPORTA);
								int intRetUDPSTATOIMPORTA = 0;
								intRetUDPSTATOIMPORTA = pstmtUDPSTATOIMPORTA.executeUpdate();
								pstmtUDPSTATOIMPORTA.close();
								// fine estrazione codice
								if (intRetUDPSTATOIMPORTA==1){
									//valRitornoUDPSTATOIMPORTA=true;
								}
			 				}
						}
						// Offerte fornitore
						if (ordine.getIdOffertaFornOrdine()!=null && ordine.getIdOffertaFornOrdine().trim().length()>0	)
						{
							//	 update	id_offerte_fornitore, oppure bid_p stato_offerta
							String sqlUDPSTATOIMPORTA="update tba_offerte_fornitore set " ;
							//sqlUDP= sqlUDP + " ts_ins = '" +  ts  + "'" ;  // ts_ins
							sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA + " ts_var = '" +  ts  + "'" ;  // ts_var
							sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA + ", ute_var = '" +  ordine.getUtente()  + "'" ;  // ute_var
							sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA + ", stato_offerta= 'O'" ;
							// CONDIZIONI
							sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA + " where fl_canc<>'S' ";
							sqlUDPSTATOIMPORTA=this.struttura(sqlUDPSTATOIMPORTA);
							sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA    + " id_offerte_fornitore=" +  Integer.valueOf(ordine.getIdOffertaFornOrdine().trim()) ;
							pstmtUDPSTATOIMPORTA = connection.prepareStatement(sqlUDPSTATOIMPORTA);
							int intRetUDPSTATOIMPORTA = 0;
							intRetUDPSTATOIMPORTA = pstmtUDPSTATOIMPORTA.executeUpdate();
							pstmtUDPSTATOIMPORTA.close();
							// fine estrazione codice
							if (intRetUDPSTATOIMPORTA==1){
								//valRitornoUDPSTATOIMPORTA=true;
							}



						}
						// Gare
						if (ordine.getCodRicOffertaOrdine()!=null && ordine.getCodRicOffertaOrdine().trim().length()>0	)
						{
							//	 update	chiave cd_polo  cd_bib cod_rich_off	stato_rich_off

							String sqlUDPSTATOIMPORTA="update tba_richieste_offerta set " ;
							//sqlUDP= sqlUDP + " ts_ins = '" +  ts  + "'" ;  // ts_ins
							sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA + " ts_var = '" +  ts  + "'" ;  // ts_var
							sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA + ", ute_var = '" +  ordine.getUtente()  + "'" ;  // ute_var
							sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA + ", stato_rich_off=4" ;
							// CONDIZIONI
							sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA + " where fl_canc<>'S' ";
							String[] chiaveComposta=ordine.getCodRicOffertaOrdine().split("\\|");
			 				if (chiaveComposta!=null )
			 				{
								if (chiaveComposta!=null &&  chiaveComposta.length>1)
				 				{
									sqlUDPSTATOIMPORTA=this.struttura(sqlUDPSTATOIMPORTA);
									sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA   + " cd_bib='" + chiaveComposta[0] +"'";
									sqlUDPSTATOIMPORTA=this.struttura(sqlUDPSTATOIMPORTA);
									sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA    + " cod_rich_off=" + chiaveComposta[1] ;
				 				}
								if (chiaveComposta!=null &&  chiaveComposta.length==1)
				 				{
									sqlUDPSTATOIMPORTA=this.struttura(sqlUDPSTATOIMPORTA);
									sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA    + " cod_rich_off=" + chiaveComposta[0] ;
				 				}

								//sqlUDPSTATOIMPORTA=this.struttura(sqlUDPSTATOIMPORTA);
								//sqlUDPSTATOIMPORTA= sqlUDPSTATOIMPORTA  + " cd_polo='" + chiaveComposta[0] +"'";
								pstmtUDPSTATOIMPORTA = connection.prepareStatement(sqlUDPSTATOIMPORTA);
								int intRetUDPSTATOIMPORTA = 0;
								intRetUDPSTATOIMPORTA = pstmtUDPSTATOIMPORTA.executeUpdate();
								pstmtUDPSTATOIMPORTA.close();
								// fine estrazione codice
								if (intRetUDPSTATOIMPORTA==1){
									//valRitornoUDPSTATOIMPORTA=true;
								}

			 				}

						}
						// aggiornamento inventari se associati
						if (ordine.getTipoOrdine()!=null && ordine.getTipoOrdine().equals("R") )
						{
							// cancellazione fisica dei record associati
							// cancello i preesistenti ordini associati
							String sqlDEL1="delete from tra_ordine_inventari ";

							if (ordine.getCodPolo() !=null &&  ordine.getCodPolo().length()!=0)
							{
								sqlDEL1=this.struttura(sqlDEL1);
								sqlDEL1=sqlDEL1 + " cd_polo='" + ordine.getCodPolo() +"'";
							}

							if (ordine.getCodBibl() !=null &&  ordine.getCodBibl().length()!=0)
							{
								sqlDEL1=this.struttura(sqlDEL1);
								sqlDEL1=sqlDEL1 + " cd_bib='" + ordine.getCodBibl() +"'";
							}

							sqlDEL1=this.struttura(sqlDEL1);
							sqlDEL1=sqlDEL1 + " id_ordine=" +ordine.getIDOrd() ;

							pstmtDELInv = connection.prepareStatement(sqlDEL1);
							int intRetCANC = 0;
							//valRitornoCANC = pstmtDEL.execute();
							intRetCANC = pstmtDELInv.executeUpdate();
							pstmtDELInv.close();
	/*						if (intRetCANC==1){
								valRitornoCANC=true;
							}*/
							// ciclo ed inserisco le righe
							if (ordine.getRigheInventariRilegatura()!=null && ordine.getRigheInventariRilegatura().size() >0 )
							{
								//valRitornoINSLOOP=false;
								for (int i=0; i<ordine.getRigheInventariRilegatura().size(); i++)
								{
									StrutturaInventariOrdVO 	oggettoDettFattVO=ordine.getRigheInventariRilegatura().get(i);
									String sqlSub2="insert into tra_ordine_inventari values (" ;
									sqlSub2 = sqlSub2 + "" + ordine.getIDOrd() ;   // id_ordine
									sqlSub2 = sqlSub2+  ",'" + ordine.getCodPolo() + "'" ;  // cd_polo
									sqlSub2 = sqlSub2+  ",'" + ordine.getCodBibl() + "'" ;  // cd_bib
									sqlSub2 = sqlSub2+ ",'" + oggettoDettFattVO.getSerie() + "'"  ;  // cd_serie
									sqlSub2 = sqlSub2+ "," +  oggettoDettFattVO.getNumero() ;  // cd_inven
									if (oggettoDettFattVO.getDataUscita()!=null && oggettoDettFattVO.getDataUscita().trim().length()>0)
									{
										sqlSub2 = sqlSub2+ ",TO_DATE('"+ oggettoDettFattVO.getDataUscita()+"','dd/MM/yyyy')";  // data_uscita
									}
									else
									{
										sqlSub2 = sqlSub2+ ",null";  // data_uscita
									}

									if (oggettoDettFattVO.getDataRientro()!=null && oggettoDettFattVO.getDataRientro().trim().length()>0)
									{
										sqlSub2 = sqlSub2+ ",TO_DATE('"+ oggettoDettFattVO.getDataRientro()+"','dd/MM/yyyy')";  // data_rientro
									}
									else
									{
										sqlSub2 = sqlSub2+ ",null";  // data_rientro
									}

									sqlSub2 = sqlSub2+ ",'" +  oggettoDettFattVO.getNote().replace("'","''")+ "'"  ;  // ota_fornitore
									sqlSub2 = sqlSub2 + ",'" + ordine.getUtente() + "'" ;   // ute_ins
									sqlSub2 = sqlSub2 + ",'" + ordine.getUtente() + "'" ;   // ute_var
									sqlSub2 = sqlSub2 + ",'" + ts + "'" ;   // ts_ins
									sqlSub2 = sqlSub2 + ",'" + ts + "'";   // ts_var
									if (oggettoDettFattVO.getDataRientroPresunta()!=null && oggettoDettFattVO.getDataRientroPresunta().trim().length()>0)
									{
										sqlSub2 = sqlSub2+ ",TO_DATE('"+ oggettoDettFattVO.getDataRientroPresunta()+"','dd/MM/yyyy')";  // data_rientro
									}
									else
									{
										sqlSub2 = sqlSub2+ ",null";  // data_rientroprevista
									}

									//almaviva5_20121114 evolutive google
									Short ps = oggettoDettFattVO.getPosizione();
									sqlSub2 += ',' + ( (ps != null) ? ps.toString() : "null" );
									Short vol = oggettoDettFattVO.getVolume();
									sqlSub2 += ',' + ( (vol != null) ? vol.toString() : "null" );

									sqlSub2 = sqlSub2+ ")" ;
									pstmtInsInv = connection.prepareStatement(sqlSub2);
									int intRetINSLOOP = 0;
									intRetINSLOOP = pstmtInsInv.executeUpdate();
									pstmtInsInv.close();

								}
							}

						}

					}else{
						valRitorno=false;
					}
					connection.close();
					//valRitorno=true;
		} // chiude if di motivo=0
		}catch (ValidationException e) {
	       throw e;
		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

		return valRitorno;
	}


	public List  gestioneStampato(List listaOggetti, String tipoOggetti, String bo) throws DataException, ApplicationException, ValidationException
	{
		boolean valRitorno=false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmtESISTEORD = null;
		ResultSet  rsESISTEORD=null ;

		List listaIDstampabili=null;
		List listaIDriStampabili=null;
		List listaIDscartati=null;

		try {

			List<Integer> listaID=new ArrayList<Integer>();
			if(listaOggetti!=null && listaOggetti.size()>0)
			{
				// desumere la lista di id dall'array
				for (int i=0; i< listaOggetti.size(); i++)
				{
					if(tipoOggetti!=null && tipoOggetti.trim().length()>0 && tipoOggetti.trim().equals("ORD") )
					{
						listaID.add(((OrdiniVO) listaOggetti.get(i)).getIDOrd());
					}
					if(tipoOggetti!=null && tipoOggetti.trim().length()>0 && tipoOggetti.trim().equals("BUO") )
					{
						listaID.add(((BuoniOrdineVO) listaOggetti.get(i)).getIDBuonoOrd());
					}
				}
			}


			if(tipoOggetti!=null && tipoOggetti.trim().length()>0 && tipoOggetti.trim().equals("ORD") )
			{
				listaIDstampabili=new ArrayList<OrdiniVO>();
				listaIDriStampabili=new ArrayList<OrdiniVO>();
				listaIDscartati=new ArrayList<OrdiniVO>();
				connection = getConnection();
				OrdiniVO oggOrdine=new OrdiniVO();

				boolean legamiSI;
				for (int i=0; i< listaOggetti.size(); i++)
				{
					// lettura
					//ListaSuppOrdiniVO condizioniRicerca=new ListaSuppOrdiniVO();
					//condizioniRicerca.setIDOrd(listaID.get(i));
					oggOrdine= (OrdiniVO)listaOggetti.get(i);
					// condizioni di scarto

					// Gli ordini annullati non sono stampabili.
					//Gli ordini appartenenenti ad un bo non possono essere stampati da sintetica ma solo da bo.(stabilito con rossana il 02/10/08)
					// Sono da escludere gli ordini già stampati in quanto la ristampa è consentita solo puntualmente (da sintetica con un unico chek, da esamina) .(stabilito con rossana il 02/10/08)
					legamiSI=false;
					String sqlESISTEORD="select distinct ord.id_ordine , ordbo.buono_ord from tba_ordini ord  " ;
					sqlESISTEORD= sqlESISTEORD + " left join  tra_elementi_buono_ordine ordbo on ordbo.cd_polo=ord.cd_polo and  ordbo.cd_bib=ord.cd_bib and ordbo.cod_tip_ord=ord.cod_tip_ord  and ordbo.anno_ord=ord.anno_ord and ordbo.cod_ord=ord.cod_ord and ordbo.fl_canc<>'S'" ;
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " ord.fl_canc<>'S'";
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " ord.id_ordine=" + oggOrdine.getIDOrd();
					pstmtESISTEORD = connection.prepareStatement(sqlESISTEORD);
					rsESISTEORD = pstmtESISTEORD.executeQuery(); // va in errore se non può restituire un recordset
					while (rsESISTEORD.next()) {
						if (rsESISTEORD.getString("buono_ord")!=null && rsESISTEORD.getString("buono_ord").trim().length()>0)
						{
							// ESISTENZA LEGAMI
							legamiSI=true;
							break;
						}
					}
					rsESISTEORD.close();
					pstmtESISTEORD.close();
					// Tck 2559 (20/04/09): un ordine chiuso deve essere escluso dalla stampa
					//tck 2559  Si stampano solo quelli aperti, se si tratta di un ordine di acquisto; invece la lettera di ringraziamento per i doni vale sia per gli ordini aperti che per quelli chiusi.
					if(oggOrdine!=null && (!oggOrdine.getStatoOrdine().equals("C") || oggOrdine.getTipoOrdine().equals("D"))&& !oggOrdine.getStatoOrdine().equals("N") && !legamiSI ) // ordini non annullati e non legati a b.o.
					{
						// condizioni di stampabilita/ristampabilità
						if(oggOrdine.isStampato())
						{
							listaIDriStampabili.add(oggOrdine);
							// solo in caso di esamina (o di un unico ordine selezionato l'ordine può essere ristampato)(stabilito con rossana il 02/10/08)
							// controordine per il tck di contardi 2559 collaudo
							//if (listaOggetti.size()==1)
							//{
							//	listaIDstampabili.add(oggOrdine);
							//}
						}
						else
						{
							listaIDstampabili.add(oggOrdine);
						}
					}
					else
					{
						listaIDscartati.add(oggOrdine);
					}
				}
				connection.close();

			}
			if(tipoOggetti!=null && tipoOggetti.trim().length()>0 && tipoOggetti.trim().equals("BUO") )
			{
				listaIDstampabili=new ArrayList<BuoniOrdineVO>();
				listaIDriStampabili=new ArrayList<BuoniOrdineVO>();
				listaIDscartati=new ArrayList<BuoniOrdineVO>();
				connection = getConnection();
				BuoniOrdineVO oggBuonoOrdine=new BuoniOrdineVO();

				boolean legamiSI;
				for (int i=0; i< listaOggetti.size(); i++)
				{
					// lettura
					//ListaSuppOrdiniVO condizioniRicerca=new ListaSuppOrdiniVO();
					//condizioniRicerca.setIDOrd(listaID.get(i));
					oggBuonoOrdine= (BuoniOrdineVO)listaOggetti.get(i);
					// condizioni di scarto
					// Sono da escludere i buoni gli ordini già stampati in quanto la ristampa è consentita solo puntualmente (da sintetica con un unico chek, da esamina) .(stabilito con rossana il 02/10/08)

					if(oggBuonoOrdine!=null && oggBuonoOrdine.getStatoBuonoOrdine()!=null  ) // ordini non
					{
						// condizioni di stampabilita/ristampabilità
						if(oggBuonoOrdine.getStatoBuonoOrdine().equals("S"))
						{
							listaIDriStampabili.add(oggBuonoOrdine);
							// solo in caso di esamina (o di un unico buono selezionato il buono  può essere ristampato)
							if (listaOggetti.size()==1)
							{
								listaIDstampabili.add(oggBuonoOrdine);
							}

						}
						else
						{
							listaIDstampabili.add(oggBuonoOrdine);
						}
					}
					else
					{
						listaIDscartati.add(oggBuonoOrdine);
					}
				}
				connection.close();
			}


		} catch (Exception e) {
			valRitorno=false;
			log.error("", e);
		} finally {
			close(connection);
		}
		return listaIDstampabili;
	}

	public boolean  gestioneStampaOrdini(List listaOggetti, List idList, String tipoOggetti, String utente, String bo) throws DataException, ApplicationException, ValidationException
	{
		// la funzione riceve un array o una lista di id di tipo
		boolean valRitorno=false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmtSel = null;
		ResultSet rsSel = null;
		try {
			List<Integer> listaID=new ArrayList<Integer>();
			if(listaOggetti!=null && idList==null && listaOggetti.size()>0)
			{
				// desumere la lista di id dall'array
				for (int i=0; i< listaOggetti.size(); i++)
				{
					if(tipoOggetti!=null && tipoOggetti.trim().length()>0 && tipoOggetti.trim().equals("ORD") )
					{
						listaID.add(((OrdiniVO) listaOggetti.get(i)).getIDOrd());
					}
					if(tipoOggetti!=null && tipoOggetti.trim().length()>0 && tipoOggetti.trim().equals("BUO") )
					{
						listaID.add(((BuoniOrdineVO) listaOggetti.get(i)).getIDBuonoOrd());
					}
				}
			}
			if(idList!=null && listaOggetti.size()>0)
			{
				listaID=idList;
			}
			List<Integer> listaIDstampabili=new ArrayList<Integer>();

			connection = getConnection();

			Timestamp ts = DaoManager.now();

			String stringaListaIDstampabili="(";
			for (int index3 = 0; index3 < listaID.size(); index3++) {
				if (listaID.get(index3)>0)
				{
					if (index3>0)
					{
						stringaListaIDstampabili=stringaListaIDstampabili+",";
					}
					stringaListaIDstampabili=stringaListaIDstampabili+ listaID.get(index3);
				}
			}
			stringaListaIDstampabili=stringaListaIDstampabili+")";

			if(tipoOggetti!=null && tipoOggetti.trim().length()>0 && tipoOggetti.trim().equals("ORD") )
			{
				// ciclo stampati update massivo

				if (listaID.size()>0 )
				{
					String sql="update tba_ordini set ";
					//sql +=" stampato=true ";
					sql +=" stampato=CASE WHEN cod_tip_ord='R' THEN false ELSE true END";
					sql +=" , data_ord=(SELECT CURRENT_DATE )"; // da specifiche data ordine aggiornata con la data di sistema
					sql +=" , data_agg='" + ts + "'"; // n.b. stesso timestamp per tutti gli ordini stampati insieme
					sql +=" , ts_var='" + ts + "'"; //
					sql +=" , ute_var = '" +  utente + "'" ;
					sql=this.struttura(sql);
					sql +=" fl_canc<>'S'";
					sql=this.struttura(sql);
					sql +=" stato_ordine<>'N'";
					sql=this.struttura(sql);
					sql +=" stampato=false"; // stampato  stato_ordine
					sql=this.struttura(sql);
					sql += " id_ordine in "+ stringaListaIDstampabili;

					pstmt = connection.prepareStatement(sql);
					int intRetINSLOOP = 0;
					intRetINSLOOP = pstmt.executeUpdate();
					pstmt.close();
		/*				if (intRetINSLOOP!=1){
						valRitornoINSLOOP=true;
					}
		*/
				}
			}
			if(tipoOggetti!=null && tipoOggetti.trim().length()>0 && tipoOggetti.trim().equals("BUO") )
			{
				//CICLO SUI BO E SELEZIONO GLI ID DEGLI ORDINI E POI  UPDATE SUGLI ORDINI APPARTENENTI CHE PASSANO A STAMPATO
				String sqlSel=" select ord.id_ordine ";
				sqlSel=sqlSel + " from  tba_buono_ordine bor ";
				sqlSel=sqlSel + " join tra_elementi_buono_ordine ordbo on ordbo.cd_polo=bor.cd_polo and  ordbo.cd_bib=bor.cd_bib and ordbo.buono_ord=bor.buono_ord  and ordbo.fl_canc<>'S'";
				sqlSel=sqlSel + " join tba_ordini ord on ord.cd_polo=ordbo.cd_polo and ord.cd_bib=ordbo.cd_bib and ord.fl_canc<>'S' and ord.stato_ordine <> 'N'";
				sqlSel=sqlSel + " and ord.cod_tip_ord=ordbo.cod_tip_ord and ord.anno_ord=ordbo.anno_ord and ord.cod_ord=ordbo.cod_ord";
				sqlSel=sqlSel + " where bor.fl_canc<>'S'";
				sqlSel=sqlSel + " and bor.id_buono_ordine in " + stringaListaIDstampabili;
				pstmtSel = connection.prepareStatement(sqlSel);
				rsSel = pstmtSel.executeQuery();
				int numRighe=0;
				String stringaListaIDOrdStampabili="(";
				while (rsSel.next())
				{
					numRighe=numRighe+1;
					if (numRighe>1)
					{
						stringaListaIDOrdStampabili=stringaListaIDOrdStampabili+",";
					}
					stringaListaIDOrdStampabili=stringaListaIDOrdStampabili+ rsSel.getInt("id_ordine");
				} // End while
				stringaListaIDOrdStampabili=stringaListaIDOrdStampabili+")";
				rsSel.close();
				pstmtSel.close();
				//UPDATE SUGLI ORDINI APPARTENENTI CHE PASSANO A STAMPATO utilizzare la query precedente
				if (numRighe>0)
				{
					String sql="update tba_ordini set ";
					//sql +=" stampato=true ";
					sql +=" stampato=CASE WHEN cod_tip_ord='R' THEN false ELSE true END";
					sql +=" , data_ord=(SELECT CURRENT_DATE )"; // da specifiche data ordine aggiornata con la data di sistema
					sql +=" , data_agg='" + ts + "'"; // n.b. stesso timestamp per tutti gli ordini stampati insieme
					sql +=" , ts_var='" + ts + "'";
					sql +=" , ute_var = '" +  utente + "'" ;
					sql=this.struttura(sql);
					sql +=" fl_canc<>'S'";
					sql=this.struttura(sql);
					sql +=" stato_ordine<>'N'";
					sql=this.struttura(sql);
					sql +=" stampato=false"; // stampato  stato_ordine
					sql=this.struttura(sql);
					sql += " id_ordine in "+ stringaListaIDOrdStampabili;

					pstmt = connection.prepareStatement(sql);
					int intRetINSLOOP = 0;
					intRetINSLOOP = pstmt.executeUpdate();
					pstmt.close();

					sql="update tba_buono_ordine  set ";
					sql +=" stato_buono='S'";
					sql +=" , data_buono=(SELECT CURRENT_DATE )"; // da specifiche data ordine aggiornata con la data di sistema
					sql=this.struttura(sql);
					sql +=" fl_canc<>'S'";
					sql=this.struttura(sql);
					sql +=" id_buono_ordine in " + stringaListaIDstampabili;
					sql=this.struttura(sql);
					sql +=" stato_buono<>'S' "; // aggiunta il 19.07.10 per impedire l'aggiornamento della data del buono d'ordine in caso di ristampa

					pstmt = connection.prepareStatement(sql);
					intRetINSLOOP = 0;
					intRetINSLOOP = pstmt.executeUpdate();
					pstmt.close();
				}
			}
			connection.close();
			valRitorno=true;

		} catch (Exception e) {
			valRitorno=false;
			log.error("", e);
		} finally {
			close(connection);
		}
		return valRitorno;
	}




	public boolean  modificaOrdine(OrdiniVO ordine) throws DataException, ApplicationException, ValidationException
	{


		Validazione.ValidaOrdiniVO (ordine);
		String ticket="";
		ticket=ordine.getTicket();

		boolean valRitorno=false;
    	int motivo=0;
    	OrdiniVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtInsInv= null;
		PreparedStatement pstmtDELInv= null;
		PreparedStatement pstmtUPD= null;
		PreparedStatement pstmtInsFornBibl= null;
		boolean valRitornoInsFornBibl=false;


		PreparedStatement pstmtCOTROLLO= null;
		ResultSet rsCOTROLLO = null;

		ResultSet rs = null;
		ResultSet rsSub = null;
		boolean valRitornoPrep=false;
		boolean valRitornoUPD=false;
		String isbd="";
		String natura="";
		String  denoFornitore="";
		try {
			// CONTROLLI PREVENTIVI
			connection = getConnection();

			String sql0="select ord.cod_ord, ord.id_sez_acquis_bibliografiche, ord.cod_tip_ord , forn.tipo_partner, ord.natura, ord.stampato, ord.stato_ordine from tba_ordini  ord ";
			if (ordine.getTipoOrdine()!=null && !ordine.getTipoOrdine().equals("R"))
			{

				if (ordine.getTitolo()!=null &&  ordine.getTitolo().getCodice()!=null && ordine.getTitolo().getCodice().length()!=0)
				{

					try {
						TitoloACQVO recTit=null;
						recTit = this.getTitoloOrdineTer(ordine.getTitolo().getCodice());
						if (recTit!=null && recTit.getIsbd()!=null) {
							isbd=recTit.getIsbd();
							natura=recTit.getNatura();
						}
						if (recTit==null) {
							throw new ValidationException("ordineIncongruenzaTitoloInesistente",
									ValidationExceptionCodici.ordineIncongruenzaTitoloInesistente);
						}

					} catch (Exception e) {
						throw new ValidationException("ordineIncongruenzaTitoloInesistente",
								ValidationExceptionCodici.ordineIncongruenzaTitoloInesistente);
					}

					// Controllo fra la natura del titolo selezionato il valore del campo natura
	 					if (ordine.getNaturaOrdine()!=null && ordine.getNaturaOrdine().length()!=0)
	 					{
							if (!natura.equals(ordine.getNaturaOrdine()))
							{
								motivo=4;
								throw new ValidationException("ordineIncongruenzaNaturaTitNaturaOrd",
										ValidationExceptionCodici.ordineIncongruenzaNaturaTitNaturaOrd);

							}
	 					}
	 					else
	 					{
	 						// se non specificato nella maschera deve essere impostato alla natura del titolo associato
	 						ordine.setNaturaOrdine(natura);
	 					}
	 					// Controllo fra la descrizione del titolo selezionato il valore del campo
	 					if (ordine.getTitolo().getDescrizione()!=null && ordine.getTitolo().getDescrizione().length()!=0 )
						{
							if (!isbd.equals(ordine.getTitolo().getDescrizione()))
							{
	/*							motivo=4;
								throw new ValidationException("ordineIncongruenzaDescrTitTitOrd",
										ValidationExceptionCodici.ordineIncongruenzaDescrTitTitOrd);	*/
							}
	 					}
	 					else
	 					{
	 						// se non specificato nella maschera deve essere impostato alla natura del titolo associato
	 						ordine.getTitolo().setDescrizione(isbd);
	 					}

				}
				else
				{
					throw new ValidationException("ordineIncongruenzaTitoloInesistente",
							ValidationExceptionCodici.ordineIncongruenzaTitoloInesistente);
				}
			}


			Date dataValSez=null;
			if (ordine.isGestSez() && ordine.getSezioneAcqOrdine()!=null && ordine.getSezioneAcqOrdine().length()!=0)
			{
				// subquery per test di esistenza sezione: da sostituire con metodo ancora da scrivere in this

				String sqlSub="select * from tba_sez_acquis_bibliografiche sez where ";
				sqlSub=sqlSub + " sez.fl_canc<>'S'";
				sqlSub=sqlSub + " and  upper(sez.cod_sezione)='" +ordine.getSezioneAcqOrdine().trim().toUpperCase()+"'";
				sqlSub=sqlSub + " and sez.cd_bib='" +ordine.getCodBibl()+"'";
				sqlSub=sqlSub + " and sez.cd_polo='" +ordine.getCodPolo()+"'";

				//va esclusa la sezione scaduta nella modifica di un ordine che la utilizza?
				//sqlSub=sqlSub + " and (sez.data_val is null or sez.data_val>(SELECT CURRENT_DATE ))";

				pstmt = connection.prepareStatement(sqlSub);
				rsSub = pstmt.executeQuery();
				if (!rsSub.next()) {
					motivo=8;
					throw new ValidationException("ordineIncongruenzaSezioneInesistente",
							ValidationExceptionCodici.ordineIncongruenzaSezioneInesistente);
				}
				else
				{
					dataValSez=rsSub.getDate("data_val");
					ordine.setIDSez(rsSub.getInt("id_sez_acquis_bibliografiche"));
				}

				rsSub.close();
				sql0=sql0 + " join tba_sez_acquis_bibliografiche sez on sez.id_sez_acquis_bibliografiche=ord.id_sez_acquis_bibliografiche  and sez.fl_canc<>'S'";
			}

			if (ordine.getValutaOrdine()!=null && ordine.getValutaOrdine().length()!=0)
			{
				// subquery per test di esistenza valuta di biblioteca
				String sqlSub="select * from tba_cambi_ufficiali  where ";
				sqlSub=sqlSub + " fl_canc<>'S'";
				sqlSub=sqlSub + " and cd_polo='" +ordine.getCodPolo()+"'";
				sqlSub=sqlSub + " and cd_bib='" +ordine.getCodBibl()+"'";
				sqlSub=sqlSub + " and upper(valuta)='" +ordine.getValutaOrdine().trim().toUpperCase()+"'";
				pstmt = connection.prepareStatement(sqlSub);
				rsSub = pstmt.executeQuery();
				if (!rsSub.next()) {
					motivo=8;
					throw new ValidationException("ordineIncongruenzaValutaInesistente",
							ValidationExceptionCodici.ordineIncongruenzaValutaInesistente);
				}
				else
				{
					ordine.setIDVal(rsSub.getInt("id_valuta"));
					ordine.setValutaOrdine(rsSub.getString("valuta"));
				}
				rsSub.close();
				sql0=sql0 + " join tba_cambi_ufficiali val on val.id_valuta=ord.id_valuta  and  val.fl_canc<>'S'";
			}


			if (ordine.getFornitore().getCodice()!=null && ordine.getFornitore().getCodice().length()!=0)
			{
				// subquery per test di esistenza fornitore : da sostituire con metodo ancora da scrivere in this
				String sqlSub="select *, fornBibl.cod_fornitore as codFornBibl, fornBibl.fl_canc as bibl_fl_canc from tbr_fornitori forn  ";
				sqlSub=sqlSub + " left join tbr_fornitori_biblioteche fornBibl on fornBibl.cod_fornitore=forn.cod_fornitore  and fornBibl.cd_biblioteca='" + ordine.getCodBibl()+"' and fornBibl.cd_polo='" + ordine.getCodPolo()+"' and fornBibl.fl_canc<>'S'"  ; // and fornBibl.fl_canc<>'S'
				sqlSub=sqlSub + " where forn.fl_canc<>'S'";
				sqlSub=sqlSub + " and forn.cod_fornitore=" +ordine.getFornitore().getCodice();

				pstmt = connection.prepareStatement(sqlSub);
				rsSub = pstmt.executeQuery();
				if (!rsSub.next()) {
					motivo=6;
					throw new ValidationException("ordineIncongruenzaFornitoreInesistente",
							ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);
				}
				else
				{

					// localizzazione del fornitore in caso di utilizzo nella modifica ordine
					if (rsSub.getString("bibl_fl_canc")!=null && !rsSub.getString("bibl_fl_canc").equals("S"))
					{
						if (rsSub.getString("codFornBibl")==null  ||  (rsSub.getString("codFornBibl")!=null && rsSub.getString("codFornBibl").trim().length()==0)  )
						{
							// esiste il fornitore in anagrafica e non esiste fra quelli di biblioteca
							String sqlFB="insert into tbr_fornitori_biblioteche values ( " ;
							Timestamp ts = DaoManager.now();
							sqlFB= sqlFB + "'" +  ordine.getCodPolo() + "'" ;  // cd_bib
							sqlFB= sqlFB + ",'" +  ordine.getCodBibl() + "'" ;  // cd_biblioteca
							sqlFB= sqlFB + "," +  ordine.getFornitore().getCodice()  ;  // cod_fornitore
							sqlFB= sqlFB  + ",''";  // tipo_pagamento
							sqlFB= sqlFB  + ",''";  // cod_cliente
							sqlFB= sqlFB  + ",''";  // nom_contatto
							sqlFB= sqlFB  + ",''";  // tel_contatto
							sqlFB= sqlFB  + ",''";  // fax_contatto
							sqlFB= sqlFB  + ",'EUR'" ;  // valuta
							sqlFB= sqlFB  + ",'" +  ordine.getCodPolo() + "'" ;  // cod_polo
							sqlFB= sqlFB  + ",' '" ;  // allinea
							sqlFB= sqlFB + ",'" + ordine.getUtente() + "'" ;   // ute_ins
							sqlFB= sqlFB + ",'" + ts + "'" ;   // ts_ins
							sqlFB= sqlFB + ",'" + ordine.getUtente() + "'" ;   // ute_var
							sqlFB= sqlFB + ",'" + ts + "'";   // ts_var
							sqlFB= sqlFB + ",'N'";   // fl_canc
							sqlFB= sqlFB  + ")" ;  // cod_polo_bib
							pstmtInsFornBibl = connection.prepareStatement(sqlFB);
							int intRetFB = 0;
							intRetFB = pstmtInsFornBibl.executeUpdate();
							pstmtInsFornBibl.close();
							// fine estrazione codice
							if (intRetFB==1){
								valRitornoInsFornBibl=true;
								//messaggio di localizzaziojne del fornitore
							} else {
								valRitornoInsFornBibl=false;
								throw new ValidationException("ordineIncongruenzaTipoFornTipoOrd",
										ValidationExceptionCodici.ordineIncongruenzaTipoFornTipoOrd);
							}
						}
					}

					if (ordine.getTipoOrdine().equals("R") )
					{
						if (!rsSub.getString("tipo_partner").equals("R"))
						{
							motivo=3;
							throw new ValidationException("ordineIncongruenzaTipoFornTipoOrd",
									ValidationExceptionCodici.ordineIncongruenzaTipoFornTipoOrd);
						}

					}


					denoFornitore=rsSub.getString("nom_fornitore");
					ordine.getFornitore().setDescrizione(denoFornitore);

				}
				rsSub.close();

				sql0=sql0 + " join tbr_fornitori forn on forn.cod_fornitore=ord.cod_fornitore and forn.fl_canc<>'S'";
			}

			if (ordine.isGestBil() &&  ordine.getBilancio()!=null &&  ordine.getBilancio().getCodice1()!=null && ordine.getBilancio().getCodice1().length()!=0)
			{
				sql0=sql0 + " join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=ord.id_capitoli_bilanci and capBil.fl_canc<>'S' ";
				sql0=sql0 + " join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and bil.fl_canc<>'S' ";

				// subquery per test di esistenza bilancio : da sostituire con metodo ancora da scrivere in this
				String sqlSub="select * from tbb_capitoli_bilanci capBil  ";
				sqlSub=sqlSub + " join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and bil.fl_canc<>'S'";
				sqlSub=sqlSub + " where capBil.fl_canc<>'S'";
				sqlSub=sqlSub + " and capBil.cd_polo='" +ordine.getCodPolo()+"'";
				sqlSub=sqlSub + " and capBil.cd_bib='" +ordine.getCodBibl()+"'";
				if (ordine.getBilancio().getCodice1()!=null && ordine.getBilancio().getCodice1().length()!=0)
				{
					sqlSub=sqlSub + " and capBil.esercizio=" +ordine.getBilancio().getCodice1();
				}
				if (ordine.getBilancio().getCodice2()!=null && ordine.getBilancio().getCodice2().length()!=0)
				{
					sqlSub=sqlSub + " and capBil.capitolo=" +ordine.getBilancio().getCodice2();
				}
				if (ordine.getBilancio().getCodice3()!=null && ordine.getBilancio().getCodice3().length()!=0)
				{
					sql0=sql0 + " and bil.cod_mat=ord.tbb_bilancicod_mat ";
					sqlSub=sqlSub + " and bil.cod_mat='" +ordine.getBilancio().getCodice3()+ "'";
				}
				pstmt = connection.prepareStatement(sqlSub);
				rsSub = pstmt.executeQuery();
				if (!rsSub.next()) {
					motivo=5;
					throw new ValidationException("ordineIncongruenzaBilancioInesistente",
							ValidationExceptionCodici.ordineIncongruenzaBilancioInesistente);
				}
				else
				{
					ordine.setIDBil(rsSub.getInt("id_capitoli_bilanci"));
				}

				rsSub.close();

			}


			sql0=this.struttura(sql0);
			sql0=sql0 + " ord.fl_canc<>'S'";

			if (ordine.getCodPolo()!=null && ordine.getCodPolo().length()!=0 )
			{
				//TODO  gestire le biblioteche affiliate a fronte della selezione del bottone relativo
				sql0=this.struttura(sql0);
				sql0=sql0 + " ord.cd_polo='" + ordine.getCodPolo() +"'";
			}

			if (ordine.getCodBibl()!=null && ordine.getCodBibl().length()!=0 )
			{
				//TODO  gestire le biblioteche affiliate a fronte della selezione del bottone relativo
				sql0=this.struttura(sql0);
				sql0=sql0 + " ord.cd_bib='" + ordine.getCodBibl() +"'";
			}

			if (ordine.getCodOrdine()!=null && ordine.getCodOrdine().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " ord.cod_ord=" + ordine.getCodOrdine() ;
			}
			if (ordine.getTipoOrdine()!=null && ordine.getTipoOrdine().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " ord.cod_tip_ord='" + ordine.getTipoOrdine() +"'";
			}

			if (ordine.getAnnoOrdine()!=null && ordine.getAnnoOrdine().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " ord.anno_ord = " +  ordine.getAnnoOrdine() ;
			}
			//sql0=this.struttura(sql0);
			//sql0=sql0 + " ord.fl_canc<>'S'" ;

			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			valRitornoPrep=true; // se la esegue c'è un resultset
			// boolean per testare il già stampato
			boolean aggiornaDataOrdinePerStampato=false;
			boolean aggiornaDataChiusura=false;

			// numero di righe del resultset
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;

			} //
			if (numRighe > 1)
			{
				// n.b anche se esiste un ordine per lo stesso titolo si possono crearne altri
				motivo=2; // record non univoco
			}
			else if (numRighe==1)

			{
				//rs.absolute(1);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					 //

					// esistenza del titolo
					// esistenza del fornitore
					// esistenza della sezione
					// esistenza del bilancio
					// esistenza della biblioteca
					// esistenza doc_lett
					// esistenza offerta forn
					// esistenza rich off
					// esistenza sugg bibl

					// casi di incongruenza
					// controllo che il tipo di fornitore sia impostato ad fornitore su tipo ordine acquisto e visione trattenuta,
					// prefettura se deposito legale, donatore se di tipo dono.



					//sqlSub=sqlSub + " and (sez.data_val is null or sez.data_val>(SELECT CURRENT_DATE ))";

					if (rs.getInt("id_sez_acquis_bibliografiche")!=ordine.getIDSez() )
					{
						//ordine con sezione modificata: va effettuato il controllo di validità in corso
						Date oggi = new Date(System.currentTimeMillis());
						if (dataValSez!=null && oggi.after(dataValSez))
						{
							throw new ValidationException("ordineIncongruenzaSezioneInesistente",
									ValidationExceptionCodici.ordineIncongruenzaSezioneInesistente);
						}
					}
			/*	//almaviva5_20140903
					if (rs.getString("cod_tip_ord").equals("D") )
					{
						if (!rs.getString("tipo_partner").equals("C"))
						{
							motivo=3;
							throw new ValidationException("ordineIncongruenzaTipoFornTipoOrd",
									ValidationExceptionCodici.ordineIncongruenzaTipoFornTipoOrd);
						}

					}
			*/
					if (rs.getString("cod_tip_ord").equals("R") )
					{
						if (!rs.getString("tipo_partner").equals("R"))
						{
							motivo=3;
							throw new ValidationException("ordineIncongruenzaTipoFornTipoOrd",
									ValidationExceptionCodici.ordineIncongruenzaTipoFornTipoOrd);
						}

					}

					// controllo se già stampato (per aggiornare data_ordine più sotto)
					if (!rs.getBoolean("stampato") )
					{
						aggiornaDataOrdinePerStampato=true;
					}
					// controllo se già chiuso (per aggiornare data_chiusura_ord più sotto)
					if ((!rs.getString("stato_ordine").equals("C") && !rs.getString("stato_ordine").equals("N")) && ((ordine.getDataChiusura()== null) ||( ordine.getDataChiusura()!=null && ordine.getDataChiusura().trim().length()==0)) )
					{
						aggiornaDataChiusura=true;
						// controllo esistenza inventari associati per inibire la chiusura dell'ordine

					}


				}
			}
			rs.close();


			// controllo preventivo di esistenza di inventari non rientrati in altri ordini di rilegatura non chiusi
			//connection = getConnection();
			if (ordine.getTipoOrdine()!=null && ordine.getTipoOrdine().equals("R") )
			{
				if (ordine.getRigheInventariRilegatura()!=null && ordine.getRigheInventariRilegatura().size() >0 )
				{
					//valRitornoINSLOOP=false;
					for (int i=0; i<ordine.getRigheInventariRilegatura().size(); i++)
					{
						StrutturaInventariOrdVO 	oggettoDettFattVO=ordine.getRigheInventariRilegatura().get(i);
						// controllo preventivo su esistenza inventario su altri ordini di rilegatura che non è rientrato
						// lo stato di un ordine di rilegatura pu; essere chiuso solo se sono rientrati tutti gli inventari

						String sqlCOTROLLO="select * from tra_ordine_inventari ordInv  ";
						sqlCOTROLLO=sqlCOTROLLO + " join tba_ordini ord on ord.id_ordine=ordInv.id_ordine and ord.fl_canc<>'S' and ord.stato_ordine <> 'N'  "; // stato_ordine non chiuso o annullato?
						if (ordine.getCodPolo() !=null &&  ordine.getCodPolo().length()!=0)
						{
							sqlCOTROLLO=this.struttura(sqlCOTROLLO);
							sqlCOTROLLO=sqlCOTROLLO + " ordInv.cd_polo='" + ordine.getCodPolo() +"'";
						}

						if (ordine.getCodBibl() !=null &&  ordine.getCodBibl().length()!=0)
						{
							sqlCOTROLLO=this.struttura(sqlCOTROLLO);
							sqlCOTROLLO=sqlCOTROLLO + " ordInv.cd_bib='" + ordine.getCodBibl() +"'";
						}

						sqlCOTROLLO=this.struttura(sqlCOTROLLO);
						sqlCOTROLLO=sqlCOTROLLO + "  ord.stato_ordine<>'C' and ord.stato_ordine<>'N'";

						sqlCOTROLLO=this.struttura(sqlCOTROLLO);
						sqlCOTROLLO=sqlCOTROLLO + "  ordInv.id_ordine !=" +ordine.getIDOrd() ;

						sqlCOTROLLO=this.struttura(sqlCOTROLLO);
						sqlCOTROLLO=sqlCOTROLLO + " ordInv.cd_serie='" +oggettoDettFattVO.getSerie() + "'";

						sqlCOTROLLO=this.struttura(sqlCOTROLLO);
						sqlCOTROLLO=sqlCOTROLLO + " ordInv.cd_inven=" +oggettoDettFattVO.getNumero() ;

						sqlCOTROLLO=this.struttura(sqlCOTROLLO);
						//sqlCOTROLLO=sqlCOTROLLO + " ordInv.data_rientro=null";
						sqlCOTROLLO=sqlCOTROLLO +  " ordInv.data_rientro is null";


						pstmtCOTROLLO = connection.prepareStatement(sqlCOTROLLO);
						rsCOTROLLO = pstmtCOTROLLO.executeQuery(); // va in errore se non può restituire un recordset
						// numero di righe del resultset
						Tba_ordini o = null;
						while (rsCOTROLLO.next()) {
							Tba_ordiniDao odao = new Tba_ordiniDao();
							o = odao.getOrdineById(rsCOTROLLO.getInt("id_ordine"));
						} //
						if (o != null)
						{
							ValidationException ve = new ValidationException(SbnErrorTypes.ACQ_ERRORE_INVENTARI_NON_RIENTRATI,
								oggettoDettFattVO.getChiave(),
								ConversioneHibernateVO.toWeb().ordine(o).getChiaveOrdine() );
							ve.addException(new ValidationException(SbnErrorTypes.ACQ_POSIZIONE_INVENTARIO,
									oggettoDettFattVO.getPosizione().toString(), oggettoDettFattVO.getVolume().toString()) );
							throw ve;
						}
						rsCOTROLLO.close();
					}
				}
			}
			//connection.close();




			if (motivo==0)
			{
				rec = new OrdiniVO();
				//pstmt = connection
				//.prepareStatement("update tbc_formati set data_agg = ?, descr = ?, n_pezzi = ? " +
				//		"where cd_bib = ? and cod_for = ?");
				Timestamp ts = DaoManager.now();

				String sql="update tba_ordini set ";
				//sql +=" data_ins=? ";
				sql +=" ts_var='" + ts + "'";
				sql += ", id_ordine="+ ordine.getIDOrd();
				if (ordine.isGestBil() &&  ordine.getIDBil()!=0 )
				{
					sql += ", id_capitoli_bilanci="+ ordine.getIDBil();
				}
				else
				{
					sql += ", id_capitoli_bilanci=null ";
				}


				if (ordine.isGestSez() && ordine.getIDSez()!=0 )
				{
					sql += ", id_sez_acquis_bibliografiche="+ ordine.getIDSez();
				}
				else
				{
					sql += ", id_sez_acquis_bibliografiche=null ";
				}


				if ( ordine.getIDVal()!=0 )
				{
					sql += ", id_valuta="+ ordine.getIDVal();

				}

				if (ordine.isStampato() && aggiornaDataOrdinePerStampato ) // viene aggiornata  la data dell'ordine appena l'ordine viene stampato
				{
					sql +=", data_ord=(SELECT CURRENT_DATE ) ";
				}

				sql +=", note='" + ordine.getNoteOrdine().replace("'","''") + "'";
				sql +=", num_copie=" + ordine.getNumCopieOrdine();
				if (ordine.isContinuativo())
				{
					sql +=", continuativo='1' ";
				}
				else
				{
					sql +=", continuativo='0' ";
				}

				sql +=", stato_ordine= '" + ordine.getStatoOrdine() + "'";
				//sql +=", tipo_doc_lett= " + ordine.getCodTipoDocOrdine() ;

				if (ordine.getCodTipoDocOrdine()!=null && ordine.getCodTipoDocOrdine().trim().length()==1)
				{
					sql +=", tipo_doc_lett='" + ordine.getCodDocOrdine().trim()+ "'";
				}
				else
				{
					sql +=", tipo_doc_lett=null ";
				}


				if (ordine.getCodDocOrdine()!=null && ordine.getCodDocOrdine().length()!=0)
				{
					sql +=", cod_doc_lett=" + Integer.parseInt(ordine.getCodDocOrdine());
				}
				else
				{
					sql +=", cod_doc_lett=null ";
				}


				sql +=", tipo_urgenza='" +  ordine.getCodUrgenzaOrdine() + "'";

				if (ordine.getCodRicOffertaOrdine()!=null && ordine.getCodRicOffertaOrdine().length()!=0)
				{
					sql +=", cod_rich_off=" + Integer.parseInt(ordine.getCodRicOffertaOrdine());
				}
				else
				{
					sql +=", cod_rich_off=null ";
				}


				//sql +=", cod_rich_off=" + Integer.parseInt(ordine.getCodRicOffertaOrdine());
				if (ordine.getIdOffertaFornOrdine()!=null && ordine.getIdOffertaFornOrdine().length()!=0)
				{
					sql +=", bid_p='" + Integer.parseInt(ordine.getIdOffertaFornOrdine().trim()) +"'";
				}
				else
				{
					sql +=", bid_p=null";
				}

 				sql +=", cod_fornitore=" + Integer.parseInt(ordine.getFornitore().getCodice());
				sql +=", note_forn='" + ordine.getNoteFornitore().replace("'","''") + "'";
				sql +=", tipo_invio='" + ordine.getTipoInvioOrdine() + "'";

				sql += ", ute_var = '" + ordine.getUtente() +  "'" ;  // ute_var
				// da gestire la chiusura

				if ((ordine.getStatoOrdine().trim().equals("C") || ordine.getStatoOrdine().trim().equals("N")) && aggiornaDataChiusura ) // viene aggiornata  la data dell'ordine appena l'ordine viene stampato
				{
					sql +=", data_chiusura_ord=(SELECT CURRENT_DATE ) ";
				}
				//sql += ", data_chiusura_ord = ?" ;
				if ((ordine.getStatoOrdine().trim().equals("A")) && ordine.getDataChiusura()==null )// in caso di riapertura
				{
					sql +=", data_chiusura_ord=null ";
				}

				if (ordine.isGestBil() && ordine.getTipoOrdine().equals("A") || ordine.getTipoOrdine().equals("V") || ordine.getTipoOrdine().equals("R"))
				{
					sql +=", tbb_bilancicod_mat='" + ordine.getBilancio().getCodice3() + "'";
				}
				else
				{
					sql +=", tbb_bilancicod_mat=null ";
				}

				if (ordine.getAnnoPrimoOrdine()==null || (ordine.getAnnoPrimoOrdine()!=null && ordine.getAnnoPrimoOrdine().equals("")))
				{
					sql +=", anno_1ord=null ";
				}
				else
				{
					sql +=", anno_1ord=" + Integer.parseInt(ordine.getAnnoPrimoOrdine());
				}

				if (ordine.getCodPrimoOrdine()==null || (ordine.getCodPrimoOrdine()!=null && ordine.getCodPrimoOrdine().equals("")))
				{
					sql +=", cod_1ord=null ";
				}
				else
				{
					sql +=", cod_1ord=" + Integer.parseInt(ordine.getCodPrimoOrdine());
				}

				//sql +=", valuta=? ";
				sql +=", prezzo=" +  ordine.getPrezzoOrdine();
				sql +=", paese='" + ordine.getPaeseOrdine() + "'";
				//sql +=", cod_sezione=? ";
				if (ordine.getCodBibliotecaSuggOrdine()== null)
				{
					sql +=", cod_bib_sugg= '   ' ";
				}
				else
				{
					sql +=", cod_bib_sugg= ' " + ordine.getCodBibliotecaSuggOrdine().trim() + "'";
				}


				if (ordine.getCodSuggBiblOrdine()!=null && ordine.getCodSuggBiblOrdine().length()!=0)
				{
					sql +=", cod_sugg_bibl=" + Integer.parseInt(ordine.getCodSuggBiblOrdine().trim());
				}
				else
				{
					sql +=", cod_sugg_bibl=null";
				}

				sql +=", bid='" + ordine.getTitolo().getCodice() +"'";
				sql +=", stato_abb='" + ordine.getStatoAbbOrdine() + "'";
				sql +=", cod_per_abb='" + ordine.getPeriodoValAbbOrdine() + "'";
				sql +=", prezzo_lire=" + ordine.getPrezzoEuroOrdine();
				sql +=", reg_trib='" +  ordine.getRegTribOrdine() + "'"  ;
				if (ordine.getAnnoAbbOrdine()==null || (ordine.getAnnoAbbOrdine()!=null && ordine.getAnnoAbbOrdine().equals("")))
				{
					sql +=", anno_abb=null ";

				}
				else
				{
					sql +=", anno_abb=" + Integer.parseInt(ordine.getAnnoAbbOrdine());

				}

				sql +=", num_fasc='" + ordine.getNumFascicoloAbbOrdine() + "'";
				if (ordine.getDataPubblFascicoloAbbOrdine()==null || (ordine.getDataPubblFascicoloAbbOrdine()!=null && ordine.getDataPubblFascicoloAbbOrdine().equals("")))
				{
					//
				}
				else
				{
					sql +=", data_fasc=TO_DATE('" + ordine.getDataPubblFascicoloAbbOrdine()+ "','dd/MM/yyyy')";
				}


				sql +=", annata='" + ordine.getAnnataAbbOrdine() + "'";
				sql +=", num_vol_abb=" + ordine.getNumVolAbbOrdine();
				//sql +=", natura=? ";
				if (ordine.getDataFineAbbOrdine()==null || (ordine.getDataFineAbbOrdine()!=null && ordine.getDataFineAbbOrdine().equals("")))
				{
					//
				}
				else
				{
					sql +=", data_fine=TO_DATE('"+ ordine.getDataFineAbbOrdine() + "','dd/MM/yyyy')";
				}
				//Stampato ????
				sql +=", stampato=" + ordine.isStampato();
				sql +=", rinnovato=" + ordine.isRinnovato();
				//sql +=", data_agg='" + ts + "'" ; // 03/07/09 tale campo è utilizzato per memorizzare lo stesso timestamp degli ordini stampati di un buono d'ordine

				//almaviva5_20121115 evolutive google
				sql += ",cd_tipo_lav=" + trimAndQuote(ordine.getCd_tipo_lav());

				//sql +=" ord.cd_bib='" + ordine.getChiave() +"'";
				sql=this.struttura(sql);
				sql +=" fl_canc<>'S'";

				if (ordine.getCodPolo()!=null && ordine.getCodPolo().length()!=0 )
				{
					sql=this.struttura(sql);
					sql +=" cd_polo='" + ordine.getCodPolo() +"'";
				}


				if (ordine.getCodBibl()!=null && ordine.getCodBibl().length()!=0 )
				{

					sql=this.struttura(sql);
					sql +=" cd_bib='" + ordine.getCodBibl() +"'";

				}


				if (ordine.getCodOrdine()!=null && ordine.getCodOrdine().length()!=0 )
				{
					sql=this.struttura(sql);
					sql +=" cod_ord=" + ordine.getCodOrdine() ;
				}
				if (ordine.getTipoOrdine()!=null && ordine.getTipoOrdine().length()!=0 )
				{
					sql=this.struttura(sql);
					sql +=" cod_tip_ord='" + ordine.getTipoOrdine() +"'";
				}

				if (ordine.getAnnoOrdine()!=null && ordine.getAnnoOrdine().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" anno_ord = " +  ordine.getAnnoOrdine() ;
				}


				if (ordine.getDataUpd()!=null )
				{
					sql=this.struttura(sql);
					sql +=" ts_var='" + ordine.getDataUpd() + "'" ;
				}


				pstmtUPD=connection.prepareStatement(sql);
				//pstmt = connection.prepareStatement(sql);

				int intRetUDP = 0;

					// aggiornamento inventari se associati
					if (ValidazioneDati.equals(ordine.getTipoOrdine(), "R") ) {
						// cancellazione fisica dei record associati
						// cancello i preesistenti ordini associati
						String sqlDEL1="delete from tra_ordine_inventari ";

						if (ordine.getCodPolo() !=null &&  ordine.getCodPolo().length()!=0)
						{
							sqlDEL1=this.struttura(sqlDEL1);
							sqlDEL1=sqlDEL1 + " cd_polo='" + ordine.getCodPolo() +"'";
						}

						if (ordine.getCodBibl() !=null &&  ordine.getCodBibl().length()!=0)
						{
							sqlDEL1=this.struttura(sqlDEL1);
							sqlDEL1=sqlDEL1 + " cd_bib='" + ordine.getCodBibl() +"'";
						}

						sqlDEL1=this.struttura(sqlDEL1);
						sqlDEL1=sqlDEL1 + " id_ordine=" +ordine.getIDOrd() ;

						pstmtDELInv = connection.prepareStatement(sqlDEL1);
						int intRetCANC = 0;
						//valRitornoCANC = pstmtDEL.execute();
						intRetCANC = pstmtDELInv.executeUpdate();
						pstmtDELInv.close();
/*						if (intRetCANC==1){
							valRitornoCANC=true;
						}*/
						// ciclo ed inserisco le righe
						if (ordine.getRigheInventariRilegatura()!=null && ordine.getRigheInventariRilegatura().size() >0 )
						{
							//valRitornoINSLOOP=false;
							for (int i=0; i<ordine.getRigheInventariRilegatura().size(); i++)
							{
								StrutturaInventariOrdVO 	oggettoDettFattVO=ordine.getRigheInventariRilegatura().get(i);
								String sqlSub2="insert into tra_ordine_inventari values (" ;
								sqlSub2 = sqlSub2 + "" + ordine.getIDOrd() ;   // id_ordine
								sqlSub2 = sqlSub2+  ",'" + ordine.getCodPolo() + "'" ;  // cd_polo
								sqlSub2 = sqlSub2+  ",'" + ordine.getCodBibl() + "'" ;  // cd_bib
								sqlSub2 = sqlSub2+ ",'" + oggettoDettFattVO.getSerie() + "'"  ;  // cd_serie
								sqlSub2 = sqlSub2+ "," +  oggettoDettFattVO.getNumero() ;  // cd_inven
								if (oggettoDettFattVO.getDataUscita()!=null && oggettoDettFattVO.getDataUscita().trim().length()>0)
								{
									sqlSub2 = sqlSub2+ ",TO_DATE('"+ oggettoDettFattVO.getDataUscita()+"','dd/MM/yyyy')";  // data_uscita
								}
								else
								{
									sqlSub2 = sqlSub2+ ",null";  // data_uscita
								}

								if (oggettoDettFattVO.getDataRientro()!=null && oggettoDettFattVO.getDataRientro().trim().length()>0)
								{

									sqlSub2 = sqlSub2+ ",TO_DATE('"+ oggettoDettFattVO.getDataRientro()+"','dd/MM/yyyy')";  // data_rientro
								}
								else
								{
									sqlSub2 = sqlSub2+ ",null";  // data_rientro
								}
								sqlSub2 = sqlSub2+ ",'" +  oggettoDettFattVO.getNote().replace("'","''")+ "'"  ;  // ota_fornitore
								sqlSub2 = sqlSub2 + ",'" + ordine.getUtente() + "'" ;   // ute_ins
								sqlSub2 = sqlSub2 + ",'" + ordine.getUtente() + "'" ;   // ute_var
								sqlSub2 = sqlSub2 + ",'" + ts + "'" ;   // ts_ins
								sqlSub2 = sqlSub2 + ",'" + ts + "'";   // ts_var
								if (oggettoDettFattVO.getDataRientroPresunta()!=null && oggettoDettFattVO.getDataRientroPresunta().trim().length()>0)
								{
									sqlSub2 = sqlSub2+ ",TO_DATE('"+ oggettoDettFattVO.getDataRientroPresunta()+"','dd/MM/yyyy')";  // data_rientro previ
								}
								else
								{
									sqlSub2 = sqlSub2+ ",null";  // data_rientro previs
								}

								//almaviva5_20121114 evolutive google
								Short ps = oggettoDettFattVO.getPosizione();
								sqlSub2 += ',' + ( (ps != null) ? ps.toString() : "null" );
								Short vol = oggettoDettFattVO.getVolume();
								sqlSub2 += ',' + ( (vol != null) ? vol.toString() : "null" );

								sqlSub2 = sqlSub2+ ")" ;
								pstmtInsInv = connection.prepareStatement(sqlSub2);
								int intRetINSLOOP = 0;
								intRetINSLOOP = pstmtInsInv.executeUpdate();
								pstmtInsInv.close();
/*								if (intRetINSLOOP!=1){
									valRitornoINSLOOP=true;
								}
*/
							}
						}

					}
					log.debug("Debug: modifica ordine");
					log.debug("Debug: " + sql);
					intRetUDP = pstmtUPD.executeUpdate();
					//valRitornoUPD = pstmt.execute();
					if (intRetUDP==1){
						valRitornoUPD=true;
					}
					else
					{
						throw new ValidationException("operazioneInConcorrenza",
								ValidationExceptionCodici.operazioneInConcorrenza);

					}

					pstmtUPD.close();


					if (valRitornoUPD)
					{
						valRitorno=true;
					}

					//connection.commit();
					connection.close();

					//connection.commit();
					//valRitorno=true;

		} // chiude if di motivo=0
		}catch (ValidationException e) {
	      	  throw e;
		}catch (ApplicationException e) {
	      	  throw e;
		}
		 catch (Exception e) {

			log.error("", e);
		}  finally {
			close(connection);
		}

		return valRitorno;
	}

	public boolean  cancellaOrdine(OrdiniVO ordine) throws DataException, ApplicationException, ValidationException
	{

		//Validazione.ValidaOrdiniVO (ordine);
		int valRitornoInt=0;
    	int motivo=0;
    	OrdiniVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean valRitorno=false;
		boolean valRitornoPrep=false;

		try {
			// CONTROLLI PREVENTIVI
			if (ordine.getStatoOrdine().equals("A"))
			{
				throw new ValidationException("ordineerroreCancellaAperto",
						ValidationExceptionCodici.ordineerroreCancellaAperto);
			}
			if (ordine.isStampato()){
				if (ordine.getStatoOrdine().equals("N")){
				}else{
					throw new ValidationException("ordineerroreCancellaStampato",
							ValidationExceptionCodici.ordineerroreCancellaStampato);
				}
			}

			// va aggiunto il controllo se è legato ad inventari
			// se è di tipo A o V deve aggiornare il bilancio e la sezione
			if (!ordine.getStatoOrdine().equals("N"))
			{
				throw new ValidationException("ordineerroreCancellaNonAnnullato",
						ValidationExceptionCodici.ordineerroreCancellaNonAnnullato);
			}


			connection = getConnection();

			String sql0="select ord.* from tba_ordini  ord ";
			sql0=this.struttura(sql0);
			sql0=sql0 + " ord.fl_canc = 'N'"  ;
			if (ordine.getCodPolo()!=null && ordine.getCodPolo().length()!=0 )
			{
				//TODO  gestire le biblioteche affiliate a fronte della selezione del bottone relativo
				sql0=this.struttura(sql0);
				sql0=sql0 + " ord.cd_polo='" + ordine.getCodPolo() +"'";
			}

			if (ordine.getCodBibl()!=null && ordine.getCodBibl().length()!=0 )
			{
				//TODO  gestire le biblioteche affiliate a fronte della selezione del bottone relativo
				sql0=this.struttura(sql0);
				sql0=sql0 + " ord.cd_bib='" + ordine.getCodBibl() +"'";
			}
			if (ordine.getCodOrdine()!=null && ordine.getCodOrdine().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " ord.cod_ord=" + ordine.getCodOrdine() ;
			}
			if (ordine.getTipoOrdine()!=null && ordine.getTipoOrdine().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " ord.cod_tip_ord='" + ordine.getTipoOrdine() +"'";
			}

			if (ordine.getAnnoOrdine()!=null && ordine.getAnnoOrdine().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " ord.anno_ord = " +  ordine.getAnnoOrdine() ;
			}

			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			valRitornoPrep=true; // se la esegue c'è un resultset
			// numero di righe del resultset
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;

			} //
			rs.close();
			Timestamp ts = DaoManager.now();

			if (numRighe > 1)
			{
				// n.b anche se esiste un ordine per lo stesso titolo si possono crearne altri
				motivo=2; // record non univoco
				throw new ValidationException("cambierroreModificaRecordNonUnivoco",
						ValidationExceptionCodici.cambierroreModificaRecordNonUnivoco);
			}

			if (numRighe==1)
			{
				rec = new OrdiniVO();
				//connection = getConnection();
				//String sql="delete from tba_ordini ";

				String sql="update  tba_ordini set ";
				sql += " ts_var = '" +  ts  + "'" ;  // ex data_agg
				sql += ", ute_var = '" +  ordine.getUtente()  + "'" ;  // ute_var
				sql += ", fl_canc = 'S'" ;  // fl_canc
				sql=this.struttura(sql);
				sql +=" fl_canc<>'S'";

				if (ordine.getCodPolo()!=null && ordine.getCodPolo().length()!=0 )
				{
					//TODO  gestire le biblioteche affiliate a fronte della selezione del bottone relativo
					sql=this.struttura(sql);
					sql +=" cd_polo='" + ordine.getCodPolo() +"'";
				}

				if (ordine.getCodBibl()!=null && ordine.getCodBibl().length()!=0 )
				{
					sql=this.struttura(sql);
					sql +=" cd_bib='" + ordine.getCodBibl() +"'";
				}
				if (ordine.getCodOrdine()!=null && ordine.getCodOrdine().length()!=0 )
				{
					sql=this.struttura(sql);
					sql +=" cod_ord=" + ordine.getCodOrdine() ;
				}
				if (ordine.getTipoOrdine()!=null && ordine.getTipoOrdine().length()!=0 )
				{
					sql=this.struttura(sql);
					sql +=" cod_tip_ord='" + ordine.getTipoOrdine() +"'";
				}

				if (ordine.getAnnoOrdine()!=null && ordine.getAnnoOrdine().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" anno_ord = " +  ordine.getAnnoOrdine() ;
				}
				pstmt = connection.prepareStatement(sql);
				valRitornoInt = pstmt.executeUpdate();
				if (valRitornoInt==1){
					valRitorno=true;
				}else{
					valRitorno=false;
				}
			}
			connection.close();
		}catch (ValidationException e) {
	      	  throw e;
		}
		 catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}
     return valRitorno;
	}

	public List<StrutturaCombo> getRicercaBiblAffiliate(String codiceBiblioteca, String codiceAttivita) throws ResourceNotFoundException, ApplicationException, ValidationException
	{


		List<StrutturaCombo> listaBiblAff = new ArrayList<StrutturaCombo>();
    	int ret = 0;
        // execute the search here
        	StrutturaCombo rec = null;
			Connection connection = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				//ricercaOrdini contiene i criteri di ricerca
				connection = getConnection();
				String sql="select bibl.cd_bib_affiliata, bibliot.nom_biblioteca  from Trf_attivita_affiliate  bibl ";
				sql +=" join tbf_biblioteca bibliot on bibliot.cd_bib=bibl.cd_bib_affiliata and bibliot.cd_polo=bibl.cd_polo_bib_centro_sistema and bibliot.fl_canc<>'S' ";
				sql=this.struttura(sql);
				sql +=" bibl.fl_canc<>'S'";
				sql=this.struttura(sql);
				sql +=" bibl.cd_attivita='" + codiceAttivita + "'";
				sql=this.struttura(sql);
				sql +=" bibl.cd_bib_affiliata <> '" + codiceBiblioteca + "'";
				sql=this.struttura(sql);
				sql +=" bibl.cd_bib_centro_sistema = '" + codiceBiblioteca + "'";
				pstmt = connection.prepareStatement(sql);

				rs = pstmt.executeQuery();

				while (rs.next()) {
					rec = new StrutturaCombo("","");
					rec.setCodice(rs.getString("cd_bib_affiliata"));
					rec.setDescrizione(rs.getString("nom_biblioteca"));
					listaBiblAff.add(rec);
				} // End while
				rs.close();
				connection.close();


		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

		//Validazione.ValidaRicercaOrdini (listaOrdini);
        return listaBiblAff;
	}



	public List<FornitoreVO> getRicercaListaFornitori(ListaSuppFornitoreVO ricercaFornitori) throws ResourceNotFoundException, ApplicationException, ValidationException
	{
		Validazione.ValidaListaSuppFornitoreVO (ricercaFornitori);

		List<FornitoreVO> listaFornitori = new ArrayList<FornitoreVO>();
    	int ret = 0;
        // execute the search here
    		FornitoreVO rec = null;
			Connection connection = null;
			PreparedStatement pstmt = null;
			boolean datiFornBiblAssenti=false;
			ResultSet rs = null;
			ResultSet rs00 = null;
			PreparedStatement pstmt00 = null;
			PreparedStatement pstmtCount = null;
			ResultSet rsCount = null;


			try {
				// contiene i criteri di ricerca
				connection = getConnection();
				String sql="select distinct forn.*,forn.ts_var as dataUpd, lower(forn.nom_fornitore)  ";
				if (ricercaFornitori.getCodBibl()!=null && ricercaFornitori.getCodBibl().length()!=0 )
				{
					sql +=",fornBibl.cd_polo,  fornBibl.cd_biblioteca,fornBibl.tipo_pagamento,fornBibl.cod_cliente, fornBibl.nom_contatto,fornBibl.tel_contatto,fornBibl.fax_contatto, fornBibl.valuta ";
					sql +=" from tbr_fornitori forn ";

					// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
					// Nuovi campi per la gestione degli editori
					if (ValidazioneDati.notEmpty(ricercaFornitori.getIsbnEditore())) {
						sql +=" join tbr_editore_num_std editNumStd	on editNumStd.cod_fornitore=forn.cod_fornitore " +
								"and editNumStd.numero_std='" + ricercaFornitori.getIsbnEditore() +
								"' and editNumStd.fl_canc<>'S'"; //
					}

					if (ricercaFornitori.getLocale()==null)
					{
						// si assume locale
						ricercaFornitori.setLocale("1");
						//sql +=" join";
					}
					if (ricercaFornitori.getLocale()!=null && ricercaFornitori.getLocale().length()>=0 )
					{
						if (ricercaFornitori.getLocale().equals("1"))
						{
							sql +=" join";
						}
						else
						{
							sql +=" left join";
						}
					}
					sql +=" tbr_fornitori_biblioteche fornBibl on fornBibl.cod_fornitore=forn.cod_fornitore  and fornBibl.cd_biblioteca='" + ricercaFornitori.getCodBibl()+"' and fornBibl.cd_polo='" + ricercaFornitori.getCodPolo()+"' and fornBibl.fl_canc<>'S'" ;
					sql +=" left join tra_sez_acquisizione_fornitori sezAcqForn  on sezAcqForn.cod_fornitore=forn.cod_fornitore and sezAcqForn.cd_biblioteca=fornBibl.cd_biblioteca and sezAcqForn.cd_polo=fornBibl.cd_polo and  sezAcqForn.fl_canc<>'S'"; //
					sql +=" left join tba_profili_acquisto profAcq on  profAcq.cod_prac=sezAcqForn.cod_prac and  profAcq.fl_canc<>'S'"; // and profAcq.cod_sezione=sezAcqForn.";
					sql +=" left join tba_sez_acquis_bibliografiche sez  on sez.id_sez_acquis_bibliografiche=profAcq.id_sez_acquis_bibliografiche and  sez.fl_canc<>'S'";
				}
				else
				{
					sql +=" from tbr_fornitori  forn ";
				}


				if (ricercaFornitori.getNomeFornitore()!=null && ricercaFornitori.getNomeFornitore().length()!=0 )
				{
					sql=this.struttura(sql);
					if (ricercaFornitori.getTipoRicerca()!=null && ricercaFornitori.getTipoRicerca().length()!=0 )
					{

						if (ricercaFornitori.getTipoRicerca().equals("inizio") )
						{
							if (ricercaFornitori.getChiaveFor()!=null && ricercaFornitori.getChiaveFor().length()!=0)
							{
								sql +=" forn.chiave_for like '" + ricercaFornitori.getChiaveFor()+"%'";
							}
							else
							{
								sql +=" upper(forn.nom_fornitore) like '" + ricercaFornitori.getNomeFornitore().trim().replace("'","''").toUpperCase()+"%'";
							}
						}
						else if (ricercaFornitori.getTipoRicerca().equals("intero") )
						{
							if (ricercaFornitori.getChiaveFor()!=null && ricercaFornitori.getChiaveFor().length()!=0)
							{
								sql +=" forn.chiave_for = '" + ricercaFornitori.getChiaveFor()+"'";
							}
							else
							{
								sql +=" upper(forn.nom_fornitore) ='" + ricercaFornitori.getNomeFornitore().trim().replace("'","''").toUpperCase()+"'";
							}
						}
						else if (ricercaFornitori.getTipoRicerca().equals("parole") )
						{
							if (ricercaFornitori.getChiaveFor()!=null && ricercaFornitori.getChiaveFor().length()!=0)
							{
								String[] parole = ricercaFornitori.getChiaveFor().split("\\s+");
								String tmp = parole[0];
								for (int i = 1; i < parole.length; i++)
									tmp = tmp + " & " + parole[i];


								//sql +=" forn.chiave_for = '" + ricercaFornitori.getChiaveFor()+"'";
								DaoManager hibDao = new DaoManager();
								 if (hibDao.getVersion().compareTo("8.3") < 0)
								 {
									 //sql +=" tidx_vector @@ to_tsquery('default', '" + ricercaFornitori.getChiaveFor() +"')";
									sql +=" tidx_vector @@ to_tsquery('default', '" + tmp +"')";
								 }
								 else
								 {
									 //sql +=" tidx_vector @@ to_tsquery('" + ricercaFornitori.getChiaveFor() +"')";
									sql +=" tidx_vector @@ to_tsquery('" + tmp +"')";

								 }

							}
							else
							{
								//** tidx_vector @@ 'aaa'::tsquery; .trim().toUpperCase()
								//fare do while
								String appo= ricercaFornitori.getNomeFornitore().trim();
								// eliminazione degli spazi di troppo
								while (appo.indexOf("  ")>0) {
									appo = appo.replaceAll("  "," ");
								}

								String[] nomeFornComposto=appo.split(" ");
								// String tit_primaParte=isbd_composto[0];
								// 'parola1 & parola2'
								// 'parola1 | parola2'
								String paroleForn="";
								for (int i=0;  i <  nomeFornComposto.length; i++)
								{
									if (i==0)
									{
										paroleForn=nomeFornComposto[0] ;
									}
									if (i>0)
									{
										paroleForn=paroleForn + " & " + nomeFornComposto[i] ;
									}

								}
								DaoManager hibDao = new DaoManager();
								 if (hibDao.getVersion().compareTo("8.3") < 0)
								 {
									 sql +=" tidx_vector @@ to_tsquery('default', '" + paroleForn +"')";
								 }
								 else
								 {
									 sql +=" tidx_vector @@ to_tsquery('" + paroleForn +"')";
								 }
							}
						}
					}
					else
					{
						sql +=" upper(forn.nom_fornitore) like '%" + ricercaFornitori.getNomeFornitore().trim().toUpperCase()+"%'";
					}
				}
				Boolean dataInizioPicos=false;
				Boolean dataFinePicos=false;

				if (ricercaFornitori.getDataInizioPicos()!=null )
				{
					dataInizioPicos=true;
				}
				if (ricercaFornitori.getDataFinePicos()!=null)
				{
					dataFinePicos=true;
				}
				if (dataInizioPicos && dataFinePicos )
				{
					sql=this.struttura(sql);
					if (ricercaFornitori.getTipoOperazionePicos()!=null &&  !ricercaFornitori.getTipoOperazionePicos().equals("C"))
					{
						sql +="( ";
					}
					sql +=" CAST(forn.ts_var  AS date)   BETWEEN CAST('" + ricercaFornitori.getDataInizioPicos() + "' AS date) AND CAST('" + ricercaFornitori.getDataFinePicos()+ "' AS date)";
					if (ricercaFornitori.getTipoOperazionePicos()!=null &&  !ricercaFornitori.getTipoOperazionePicos().equals("C"))
					{
						sql +=" or ";
						//sql +=" forn.ts_ins  BETWEEN " + ricercaFornitori.getDataInizioPicos() + " AND " + ricercaFornitori.getDataFinePicos() + ")" ;
						sql +=" CAST(forn.ts_ins  AS date)   BETWEEN CAST('" + ricercaFornitori.getDataInizioPicos() + "' AS date) AND CAST('" + ricercaFornitori.getDataFinePicos()+ "' AS date)" + ")";

					}
				}
				else if (dataInizioPicos )
				{
					sql=this.struttura(sql);
					if (ricercaFornitori.getTipoOperazionePicos()!=null &&  !ricercaFornitori.getTipoOperazionePicos().equals("C"))
					{
						sql +="( ";
					}
					sql +=" CAST(forn.ts_var AS date) >= CAST('"+ ricercaFornitori.getDataInizioPicos()+ "' AS date)";
					if (ricercaFornitori.getTipoOperazionePicos()!=null &&  !ricercaFornitori.getTipoOperazionePicos().equals("C"))
					{
						sql +=" or ";
						sql +=" CAST(forn.ts_ins AS date)>= CAST('"+ ricercaFornitori.getDataInizioPicos() + "' AS date)" + ")" ;
					}
				}
				else if (dataFinePicos )
				{
					sql=this.struttura(sql);
					if (ricercaFornitori.getTipoOperazionePicos()!=null &&  !ricercaFornitori.getTipoOperazionePicos().equals("C"))
					{
						sql +="( ";
					}
					sql +=" CAST(forn.ts_var AS date)<= CAST('"+ ricercaFornitori.getDataFinePicos() + "' AS date)";
					if (ricercaFornitori.getTipoOperazionePicos()!=null &&  !ricercaFornitori.getTipoOperazionePicos().equals("C"))
					{
						sql +=" or ";
						sql +=" CAST(forn.ts_ins AS date)<= CAST('"+ ricercaFornitori.getDataFinePicos() + "' AS date)" + ")" ;
					}
				}

				if (ricercaFornitori.getTipoFornPicosArr()!=null && ricercaFornitori.getTipoFornPicosArr().length!=0 )
				{
					Boolean aggiungiSQL=false;
					String sqla="";
					sqla=sqla + " ( ";
					for (int index = 0; index < ricercaFornitori.getTipoFornPicosArr().length; index++) {
						String so = ricercaFornitori.getTipoFornPicosArr()[index] ;
						if (so!=null && so.length()!=0)
						{
					         if (!sqla.equals(" ( ")) {
					        	 sqla=sqla + " OR ";
					         }
					        sqla=sqla + " forn.tipo_partner='"+ so +"'";
							aggiungiSQL=true;
						}
					}
					sqla=sqla + " ) ";
					if (aggiungiSQL)
					{
						sql=this.struttura(sql);
						sql += sqla;
					}
				}

				if (ricercaFornitori.getTipoPartner()!=null && ricercaFornitori.getTipoPartner().length()!=0 )
				{
					sql=this.struttura(sql);
					sql +=" forn.tipo_partner='" + ricercaFornitori.getTipoPartner()+"'";
				}
				if (ricercaFornitori.getProvincia()!=null && ricercaFornitori.getProvincia().length()!=0 )
				{
					sql=this.struttura(sql);
					sql +=" forn.provincia='" + ricercaFornitori.getProvincia()+"'";
				}

				// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
				// Inserito nuovo metodo per la ricerca dei titoli collegati in modo esplicito o implicito all'editore selezionato
				if (ricercaFornitori.getRegione()!=null && ricercaFornitori.getRegione().length()!=0 ) {
					sql=this.struttura(sql);
					sql +=" forn.regione='" + ricercaFornitori.getRegione()+"'";
				}


				if (ricercaFornitori.getPaese()!=null && ricercaFornitori.getPaese().length()!=0 )
				{
					if (ricercaFornitori.getCodLingua()!=null && ricercaFornitori.getCodLingua().length()!=0 && ricercaFornitori.getCodSezione()!=null && ricercaFornitori.getCodSezione().length()!=0)
					{
						//ricerca forn preferenziali
						sql=this.struttura(sql);
						sql +=" profAcq.paese='" + ricercaFornitori.getPaese()+"'";
					}
					else
					{
						sql=this.struttura(sql);
						sql +=" forn.paese='" + ricercaFornitori.getPaese()+"'";
					}
				}

				if (ricercaFornitori.getCodLingua()!=null && ricercaFornitori.getCodLingua().length()!=0 )
				{
					sql=this.struttura(sql);
					sql +=" profAcq.lingua='" + ricercaFornitori.getCodLingua()+"'";
				}


				if (ricercaFornitori.getCodSezione()!=null && ricercaFornitori.getCodSezione().length()!=0 )
				{
					sql=this.struttura(sql);
					sql +=" sez.cod_sezione='" + ricercaFornitori.getCodSezione()+"'";
				}

				if (ricercaFornitori.getCodProfiloAcq()!=null && ricercaFornitori.getCodProfiloAcq().length()!=0 )
				{
					sql=this.struttura(sql);
					sql +=" profAcq.cod_prac='" + ricercaFornitori.getCodProfiloAcq()+"'";
				}
				if (ricercaFornitori.getCodFornitore()!=null && ricercaFornitori.getCodFornitore().length()!=0 )
				{
					sql=this.struttura(sql);
					sql +=" forn.cod_fornitore=" + ricercaFornitori.getCodFornitore();
				}
				if (ricercaFornitori.getTipoOperazionePicos()==null || (ricercaFornitori.getTipoOperazionePicos()!=null &&  !ricercaFornitori.getTipoOperazionePicos().equals("C")))
				{
					sql=this.struttura(sql);
					sql +=" forn.fl_canc<>'S'";

				}
				else
				{
					sql=this.struttura(sql);
					sql +=" forn.fl_canc='S'";

				}


				// creazione query per calcolo dei risultati con esclusione dell'order by
				String sqlXCount="";
				int pos=sql.lastIndexOf("from");
				int totForn=0;
				if (pos>0)
				{
					sqlXCount="select count(distinct forn.cod_fornitore) as tot " +sql.substring(pos);
					pstmtCount = connection.prepareStatement(sqlXCount);
					rsCount = pstmtCount.executeQuery();
					while (rsCount.next()) {
						totForn=rsCount.getInt("tot");
					}
					rsCount.close();
					pstmtCount.close();
					if (totForn>1000 && ricercaFornitori.getTipoOperazionePicos()==null && !ricercaFornitori.isStampaForn())
			        {
							throw new ValidationException("ricercaDaRaffinareTroppi",
				        			ValidationExceptionCodici.ricercaDaRaffinareTroppi);
			        }

				}

				//
				// ordinamento passato
				if (ricercaFornitori.getOrdinamento()==null || (ricercaFornitori.getOrdinamento()!=null && ricercaFornitori.getOrdinamento().equals("")) )
				{
					sql +="  order by forn.tipo_partner,  forn.nom_fornitore ";
				}
				else if (ricercaFornitori.getOrdinamento().equals("1"))
				{
					sql +="  order by forn.tipo_partner, forn.nom_fornitore ";
				}
/*				else if (ricercaFornitori.getOrdinamento().equals("2"))
				{
					sql +="  order by forn.cod_fornitore ";
				}
*/				else if (ricercaFornitori.getOrdinamento().equals("3"))
				{
					sql +="  order by lower(forn.nom_fornitore) ";
				}
				else if (ricercaFornitori.getOrdinamento().equals("4"))
				{
					sql +="  order by forn.unit_org, forn.nom_fornitore ";
				}
				else if (ricercaFornitori.getOrdinamento().equals("5"))
				{
					sql +="  order by  forn.paese,forn.provincia, forn.cap,forn.citta, forn.indirizzo ";
				}
				else if (ricercaFornitori.getOrdinamento().equals("6"))
				{
					sql +="  order by forn.cod_fornitore ";
				}
				else if (ricercaFornitori.getOrdinamento().equals("7"))
				{
					sql +="  order by forn.cod_fornitore desc";
				}
				else if (ricercaFornitori.getOrdinamento().equals("8"))
				{
					sql +="  order by lower(forn.nom_fornitore) desc";
				}
				pstmt = connection.prepareStatement(sql);
				//log.debug("Debug: lettura fornitori");
				//log.debug("Debug: " + sql);

				rs = pstmt.executeQuery();
				int numRighe=0;

				while (rs.next()) {
					numRighe=numRighe+1;
					rec = new FornitoreVO();
					rec.setProgressivo(numRighe);
					rec.setCodPolo(ricercaFornitori.getCodPolo());
					//rec.setCodBibl();
					if (ricercaFornitori.getCodBibl()!=null && ricercaFornitori.getCodBibl().length()!=0 ) {
						rec.setCodBibl(ricercaFornitori.getCodBibl());
					}
					else {
						rec.setCodBibl("");
					}
					rec.setCodFornitore(String.valueOf(rs.getInt("cod_fornitore")));
					rec.setNomeFornitore(rs.getString("nom_fornitore").trim());

					// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
					// Inserito nuovo metodo per la ricerca dei titoli collegati in modo esplicito o implicito all'editore selezionato
//					if (rs.getString("isbn_editore") == null) {
//						rec.setIsbnEditore("");
//					} else {
//						rec.setIsbnEditore(rs.getString("isbn_editore").trim());
//					}
					if (rs.getString("regione") == null) {
						rec.setRegione("");
					} else {
						rec.setRegione(rs.getString("regione").trim());
					}


					// modifica picos richiesta per mail da giliberto
					if (ricercaFornitori.getTipoOperazionePicos()!=null || ricercaFornitori.getDataInizioPicos()!=null || ricercaFornitori.getDataFinePicos()!=null || ricercaFornitori.getTipoFornPicosArr()!=null)
					{
						rec.setNomeFornitore(rs.getString("nom_fornitore").trim()+ " " + rs.getString("unit_org").trim());
					}
					rec.setUnitaOrg(rs.getString("unit_org").trim());
					rec.setIndirizzo(rs.getString("indirizzo").trim());
					rec.setIndirizzoComposto("");
					if (rs.getString("paese")!=null && rs.getString("paese").trim().length()>0)
					{
						String appoInd=rec.getIndirizzoComposto();
						rec.setIndirizzoComposto(appoInd + " " + rs.getString("paese").trim());
					}
					if (rs.getString("provincia")!=null && rs.getString("provincia").trim().length()>0)
					{
						String appoInd=rec.getIndirizzoComposto();
						rec.setIndirizzoComposto(appoInd + " " + rs.getString("provincia").trim());
					}
					if (rs.getString("cap")!=null && rs.getString("cap").trim().length()>0)
					{
						String appoInd=rec.getIndirizzoComposto();
						rec.setIndirizzoComposto(appoInd + " " + rs.getString("cap").trim());
					}
					if (rs.getString("citta")!=null && rs.getString("citta").trim().length()>0)
					{
						String appoInd=rec.getIndirizzoComposto();
						rec.setIndirizzoComposto(appoInd + " " + rs.getString("citta").trim());
					}
					if (rs.getString("indirizzo")!=null && rs.getString("indirizzo").trim().length()>0)
					{
						String appoInd=rec.getIndirizzoComposto();
						rec.setIndirizzoComposto(appoInd + " " + rs.getString("indirizzo").trim());
					}

					rec.setCasellaPostale(rs.getString("cpostale").trim());
					rec.setCitta(rs.getString("citta").trim());
					rec.setCap(rs.getString("cap").trim());
					rec.setTelefono(rs.getString("telefono").trim());
					rec.setFax(rs.getString("fax").trim());
					rec.setNote(rs.getString("note").trim());
					rec.setPartitaIva(rs.getString("p_iva").trim());
					rec.setCodiceFiscale(rs.getString("cod_fiscale").trim());
					rec.setEmail(rs.getString("e_mail").trim());
					rec.setPaese(rs.getString("paese"));
					rec.setTipoPartner(rs.getString("tipo_partner"));
					rec.setProvincia(rs.getString("provincia"));
					rec.setBibliotecaFornitore(rs.getString("cod_bib")); // codice della biblioteca importata come fornitore
					rec.setBibliotecaFornitoreCodPolo(rs.getString("cod_polo_bib")); // codice del polo della biblioteca importata come fornitore
					rec.setDataUpd(rs.getTimestamp("dataUpd"));

					// dati forn bibl

					datiFornBiblAssenti=false;
					if (ricercaFornitori.getCodBibl()!=null && ricercaFornitori.getCodBibl().length()!=0 )
					{
						if ((rs.getString("tipo_pagamento")==null || (rs.getString("tipo_pagamento")!=null  && rs.getString("tipo_pagamento").trim().length()==0))
							&& (rs.getString("cod_cliente")==null || (rs.getString("cod_cliente")!=null  && rs.getString("cod_cliente").trim().length()==0))
							&& (rs.getString("nom_contatto")==null || (rs.getString("nom_contatto")!=null  && rs.getString("nom_contatto").trim().length()==0))
							&& (rs.getString("tel_contatto")==null || (rs.getString("tel_contatto")!=null  && rs.getString("tel_contatto").trim().length()==0))
							&& (rs.getString("fax_contatto")==null || (rs.getString("fax_contatto")!=null  && rs.getString("fax_contatto").trim().length()==0))
							&& (rs.getString("valuta")==null || (rs.getString("valuta")!=null  &&  rs.getString("valuta").trim().length()==0)))
						{
							datiFornBiblAssenti=true;
							rec.setFornitoreBibl(null);

						}
						if (!datiFornBiblAssenti)
						{
							rec.setFornitoreBibl(new DatiFornitoreVO());
							rec.setCodPolo(rs.getString("cd_polo"));
							rec.setCodBibl(rs.getString("cd_biblioteca"));
							rec.getFornitoreBibl().setCodPolo(rs.getString("cd_polo"));
							rec.getFornitoreBibl().setCodBibl(rs.getString("cd_biblioteca"));
							rec.getFornitoreBibl().setCodFornitore(String.valueOf(rs.getInt("cod_fornitore")));
							rec.getFornitoreBibl().setTipoPagamento(rs.getString("tipo_pagamento").trim());
							rec.getFornitoreBibl().setCodCliente(rs.getString("cod_cliente").trim());
							rec.getFornitoreBibl().setNomContatto(rs.getString("nom_contatto").trim());
							rec.getFornitoreBibl().setTelContatto(rs.getString("tel_contatto").trim());
							rec.getFornitoreBibl().setFaxContatto(rs.getString("fax_contatto").trim());
							rec.getFornitoreBibl().setValuta(rs.getString("valuta"));

							// meglio lanciare la ricerca di profili
							String sql00=" select sezAcqForn.* from tbr_fornitori forn ";
							sql00=sql00 + " join tbr_fornitori_biblioteche fornBibl on fornBibl.cod_fornitore=forn.cod_fornitore and fornBibl.fl_canc<>'S'"  ;
							sql00=sql00 + " join tra_sez_acquisizione_fornitori sezAcqForn  on sezAcqForn.cod_fornitore=forn.cod_fornitore and sezAcqForn.cd_biblioteca=fornBibl.cd_biblioteca and sezAcqForn.cd_polo=fornBibl.cd_polo and  sezAcqForn.fl_canc<>'S' "; //
							//sql00=sql00 + " join tba_profili_acquisto profAcq on profAcq.cd_bib=fornBibl.cd_biblioteca and profAcq.cd_polo=fornBibl.cd_polo and profAcq.cod_prac=sezAcqForn.cod_prac and  profAcq.fl_canc<>'S' "; // and profAcq.cod_sezione=sezAcqForn.";
							sql00=sql00 + " join tba_profili_acquisto profAcq on  profAcq.cod_prac=sezAcqForn.cod_prac and  profAcq.fl_canc<>'S' "; // and profAcq.cod_sezione=sezAcqForn.";
							sql00=sql00 + " join tba_sez_acquis_bibliografiche sez  on sez.id_sez_acquis_bibliografiche=profAcq.id_sez_acquis_bibliografiche and  sez.fl_canc<>'S'";

							sql00=this.struttura(sql00);
							sql00=sql00 + " fornBibl.fl_canc<>'S'";

							if (ricercaFornitori.getCodPolo()!=null && ricercaFornitori.getCodPolo().length()!=0 )
							{
								sql00=this.struttura(sql00);
								sql00=sql00 + " fornBibl.cd_polo='" + ricercaFornitori.getCodPolo() +"'";
							}

							if (ricercaFornitori.getCodBibl()!=null && ricercaFornitori.getCodBibl().length()!=0 )
							{
								sql00=this.struttura(sql00);
								sql00=sql00 + " fornBibl.cd_biblioteca='" + ricercaFornitori.getCodBibl() +"'";
							}
							if (ricercaFornitori.getCodFornitore()!=null && ricercaFornitori.getCodFornitore().length()!=0 )
							{
								sql00=this.struttura(sql00);
								sql00=sql00 + " fornBibl.cod_fornitore=" + ricercaFornitori.getCodFornitore() ;
							}

							pstmt00 = connection.prepareStatement(sql00);
							rs00 = pstmt00.executeQuery(); // va in errore se non può restituire un recordset
							List<StrutturaProfiloVO> listaProfili = new ArrayList<StrutturaProfiloVO>();
							while (rs00.next()) {
								StrutturaProfiloVO prof=new StrutturaProfiloVO("", rs00.getString("cd_biblioteca"), new StrutturaCombo(rs00.getString("cod_prac"),""), new StrutturaCombo("",""),new StrutturaCombo("",""),new StrutturaCombo("",""),null,"");
								listaProfili.add(prof);
							} //
							rec.getFornitoreBibl().setProfiliAcq (listaProfili);
							rs00.close();
							pstmt00.close();

						}
					}
					listaFornitori.add(rec);
				} // End while

				rs.close();
				connection.close();

		} catch (ValidationException e) {
			throw e;

		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

		Validazione.ValidaRicercaFornitori (listaFornitori);
        return listaFornitori;
	}



	public boolean  inserisciFornitore(FornitoreVO fornitore) throws DataException, ApplicationException , ValidationException
	{
		Validazione.ValidaFornitoreVO (fornitore);
		boolean valRitorno=false;
    	int motivo=0;
    	//FornitoreVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSet rsSub = null;
		//ResultSet rsInsTit = null;

		PreparedStatement pstmtUDP3 = null;
		PreparedStatement pstmtINS = null;
		boolean valRitornoINSLOOP=false;


		boolean valRitornoF=false;
		boolean valRitornoFB=false;
		boolean valRitornoUPD=false;
		//boolean valRitornoInsTit=false;
		boolean esistenzaFornitore=false;
		boolean esistenzaFornitoreBiblioteca=false;
		boolean datiFornBiblAssenti=false;

		PreparedStatement pstmtCloG=null;
		ResultSet rsCloG=null;
		boolean esistenzaFornitoreCancLogic=false;
		String fornBiblCancLogic=null; // variabile che contiene il cod bibl delfornitore di biblioteca cancellato logicamente


		try {
			// CONTROLLI PREVENTIVI
			connection = getConnection();
			String sql0="select forn.cod_fornitore  from tbr_fornitori  forn ";

			sql0=this.struttura(sql0);
			sql0=sql0 + " forn.fl_canc<>'S'" ;

			if (fornitore.getCodFornitore()!=null && fornitore.getCodFornitore().length()!=0  )
			{
				//TODO  gestire le biblioteche affiliate a fronte della selezione del bottone relativo
				sql0=this.struttura(sql0);
				sql0=sql0 + " forn.cod_fornitore='" + fornitore.getCodFornitore() +"'";
			}

			// obbligatori
			if (fornitore.getNomeFornitore()!=null && fornitore.getNomeFornitore().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " forn.nom_fornitore ='" + fornitore.getNomeFornitore().replace("'","''") +"'" ;
			}
			if (fornitore.getTipoPartner()!=null && fornitore.getTipoPartner().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " forn.tipo_partner ='" + fornitore.getTipoPartner() +"'" ;
			}

			if (fornitore.getPaese()!=null && fornitore.getPaese().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " forn.paese='" + fornitore.getPaese() +"'";
			}
			if (fornitore.getProvincia()!=null && fornitore.getProvincia().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " forn.provincia ='" + fornitore.getProvincia() +"'" ;
			}
			// fine obbligatori

			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			//valRitornoPrep=true; // se la esegue c'è un resultset
			// numero di righe del resultset
			int numRighe=0;
			while (rs.next()) {
				fornitore.setCodFornitore(String.valueOf(rs.getInt("cod_fornitore")));
				numRighe=numRighe+1;

			} //
			if (numRighe >= 1)
			{
				if (numRighe == 1)
				{
					esistenzaFornitore=true;
				}

				/*				motivo=2; // record forse già esistente quindi non inseribile
								throw new ValidationException("cambierroreInserimentoEsistenzaRecord",
										ValidationExceptionCodici.cambierroreInserimentoEsistenzaRecord);
				*/
			}
			rs.close();
			//connection.close();
			//
			if (esistenzaFornitore)
			{
				String sql00="select forn.cod_fornitore from tbr_fornitori forn ";
				if (fornitore.getFornitoreBibl()!=null && fornitore.getFornitoreBibl().getCodBibl()!=null && fornitore.getFornitoreBibl().getCodBibl().length()!=0)
				{
					sql00=sql00 + " join tbr_fornitori_biblioteche fornBib on fornBib.cod_fornitore=forn.cod_fornitore  ";
					sql00=this.struttura(sql00);
					sql00=sql00 + " fornBib.fl_canc<>'S'";
					sql00=this.struttura(sql00);
					sql00=sql00 + " fornBib.cd_polo='" + fornitore.getCodPolo() +"'";
					sql00=this.struttura(sql00);
					sql00=sql00 + " fornBib.cd_biblioteca='" + fornitore.getFornitoreBibl().getCodBibl() +"'";
					if (fornitore.getCodFornitore()!=null && fornitore.getCodFornitore().length()!=0  )
					{
						sql00=this.struttura(sql00);
						sql00=sql00 + " fornBib.cod_fornitore='" + fornitore.getCodFornitore() +"'";
					}
				}
				pstmt = connection.prepareStatement(sql00);
				rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
				numRighe=0;
				while (rs.next()) {
					numRighe=numRighe+1;
				} //
				if (numRighe == 1)
				{
					esistenzaFornitoreBiblioteca=true;
				}
				rs.close();
				//connection.close();
				//rec = new FornitoreVO();
			}
/*			if (esistenzaFornitore && esistenzaFornitoreBiblioteca)
			{
				// esistono entrambi quindi si deve andare in modifica e non in inserimento
				motivo=2; // record forse già esistente quindi non inseribile
				throw new ValidationException("fornitoreInserimentoImpossibile",
						ValidationExceptionCodici.fornitoreInserimentoImpossibile);
			}
*/			if (!esistenzaFornitore && esistenzaFornitoreBiblioteca)
			{
				// ERRORE IN BASE DATI
				motivo=2; // record forse già esistente quindi non inseribile
				throw new ValidationException("fornitoreInserimentoErroreBaseDati",
						ValidationExceptionCodici.fornitoreInserimentoErroreBaseDati);
			}

			if (fornitore.getFornitoreBibl()!=null)
			{
				if ( (fornitore.getFornitoreBibl().getTipoPagamento()==null || fornitore.getFornitoreBibl().getTipoPagamento().trim().length()==0)
						&& (fornitore.getFornitoreBibl().getCodCliente()==null || fornitore.getFornitoreBibl().getCodCliente().trim().length()==0)
						&& (fornitore.getFornitoreBibl().getNomContatto()==null || fornitore.getFornitoreBibl().getNomContatto().trim().length()==0)
						&& (fornitore.getFornitoreBibl().getTelContatto()==null || fornitore.getFornitoreBibl().getTelContatto().trim().length()==0)
						&& (fornitore.getFornitoreBibl().getFaxContatto()==null || fornitore.getFornitoreBibl().getFaxContatto().trim().length()==0)
						// considerando la valuta  i dati della biblioteca operante anche se nulli vengono registrati su sottotabella
						&& (fornitore.getFornitoreBibl().getValuta()==null || fornitore.getFornitoreBibl().getValuta().length()==0)
						//&& (fornitore.getFornitoreBibl().getCodPolo()==null || fornitore.getFornitoreBibl().getCodPolo().length()==0)
					)
				{
					datiFornBiblAssenti=true;
				}
/*				else
				{
					if (fornitore.getCodBibl()!=null && fornitore.getCodBibl().trim().length()>0 )
					{
						if (fornitore.getFornitoreBibl().getCodBibl()==null || (fornitore.getFornitoreBibl().getCodBibl()!=null && fornitore.getFornitoreBibl().getCodBibl().trim().length()==0) )
						{
							fornitore.getFornitoreBibl().setCodBibl(fornitore.getCodBibl());
						}
					}
				}*/

			}


			if (!esistenzaFornitore)
			{
				//connection = getConnection();
				// inserimento fornitore
				// non esistono fornitori quindi si può procedere con l'inserimento

				// **********************
				// controllo preventivo che non sia stato cancellato logicamente: in tal caso
				// faccio update anche del flag e non insert
				String sql00C="select forn.cod_fornitore,fornBib.cd_biblioteca  from tbr_fornitori forn ";
				sql00C=sql00C + "left join tbr_fornitori_biblioteche fornBib on fornBib.cod_fornitore=forn.cod_fornitore and   fornBib.fl_canc='S'";

				if (fornitore.getCodFornitore()!=null && fornitore.getCodFornitore().length()!=0  )
				{
					sql00C=this.struttura(sql00C);
					sql00C=sql00C + " forn.cod_fornitore='" + fornitore.getCodFornitore() +"'";
				}
				if (fornitore.getNomeFornitore()!=null && fornitore.getNomeFornitore().length()!=0 )
				{
					sql00C=this.struttura(sql00C);
					sql00C=sql00C + " forn.nom_fornitore ='" + fornitore.getNomeFornitore().replace("'","''") +"'" ;
				}
				if (fornitore.getTipoPartner()!=null && fornitore.getTipoPartner().length()!=0 )
				{
					sql00C=this.struttura(sql00C);
					sql00C=sql00C + " forn.tipo_partner ='" + fornitore.getTipoPartner() +"'" ;
				}

				if (fornitore.getPaese()!=null && fornitore.getPaese().length()!=0 )
				{
					sql00C=this.struttura(sql00C);
					sql00C=sql00C + " forn.paese='" + fornitore.getPaese() +"'";
				}
				if (fornitore.getProvincia()!=null && fornitore.getProvincia().length()!=0 )
				{
					sql00C=this.struttura(sql00C);
					sql00C=sql00C + " forn.provincia ='" + fornitore.getProvincia() +"'" ;
				}

				sql00C=this.struttura(sql00C);
				sql00C=sql00C + " forn.fl_canc='S'";

				pstmtCloG = connection.prepareStatement(sql00C);
				rsCloG = pstmtCloG.executeQuery(); // va in errore se non può restituire un recordset
				int numRigheCloG=0;
				int fornCancLogic=0;

				while (rsCloG.next()) {
					numRigheCloG=numRigheCloG+1;
					fornCancLogic=rsCloG.getInt("cod_fornitore");
					if (rsCloG.getString("cod_fornitore")!=null)
					{
						fornBiblCancLogic=rsCloG.getString("cd_biblioteca");
					}

				} //
				if (numRigheCloG >0)
				{
					esistenzaFornitoreCancLogic=true;
				}
				rsCloG.close();
				// **********************
				if (!esistenzaFornitoreCancLogic)
				{
					String sql="insert into tbr_fornitori values ( " ;
					Timestamp ts = DaoManager.now();
					//sql +=" (SELECT MAX(tbr_fornitori.cod_fornitore)+1  from tbr_fornitori)   ";  // cod_fornitore
					// INIZIO SUBQUERY
					sql +=" (SELECT CASE WHEN  (MAX(tbr_fornitori.cod_fornitore) is null) THEN 1  else MAX(tbr_fornitori.cod_fornitore)+1  END " ;
					sql +=" from tbr_fornitori   ";
					//sql +=" where tbr_fornitori.fl_canc<>'S'";
					sql +=" )";  // fine subquery
					sql +=",'" +  fornitore.getNomeFornitore().trim().replace("'","''") +"'" ;  // nom_fornitore
					sql +=",'" +  fornitore.getUnitaOrg() + "'" ;  // unit_org
					sql +=",'" +  fornitore.getIndirizzo().trim().replace("'","''") + "'" ;  // indirizzo
					sql +=",'" +  fornitore.getCasellaPostale() + "'" ;  // cpostale
					sql +=",'" +  fornitore.getCitta().trim().replace("'","''") + "'" ;  // citta
					sql +=",'" +  fornitore.getCap() + "'" ;  // cap
					sql +=",'" +  fornitore.getTelefono() + "'" ;  // telefono
					sql +=",'" +  fornitore.getFax() + "'" ;  // fax
					sql +=",'" +  fornitore.getNote().trim().replace("'","''") + "'" ;  // note
					sql +=",'" +  fornitore.getPartitaIva() + "'" ;  // p_iva
					sql +=",'" +  fornitore.getCodiceFiscale() + "'" ;  // cod_fiscale
					sql +=",'" +  fornitore.getEmail() + "'" ;  // e_mail
					sql +=",'" +  fornitore.getPaese() + "'" ;  // paese
					sql +=",'" +  fornitore.getTipoPartner() + "'" ;  // tipo_partner
					sql +=",'" +  fornitore.getProvincia() + "'" ;  // provincia
					if (!fornitore.getTipoPartner().equals("B"))
					{
						fornitore.setBibliotecaFornitore("");
						fornitore.setBibliotecaFornitoreCodPolo("");
					}
					else // biblioteca come forn
					{
						if (fornitore.getBibliotecaFornitore()==null || fornitore.getBibliotecaFornitoreCodPolo()==null || fornitore.getBibliotecaFornitore().length()!=3 || fornitore.getBibliotecaFornitoreCodPolo().length()!=3)
						{
							fornitore.setBibliotecaFornitore("");
							fornitore.setBibliotecaFornitoreCodPolo("");
						}
					}

					sql +=",'" +  fornitore.getBibliotecaFornitore() + "'" ;  // cd_bib
					sql +=",'" +  fornitore.getChiaveFor() + "'" ;  // chiave_for
					sql +=",'" +  fornitore.getBibliotecaFornitoreCodPolo() + "'" ;  // cod_polo_bib
					sql +=",'" + fornitore.getUtente() + "'" ;   // ute_ins
					sql +=",'" + ts + "'" ;   // ts_ins
					sql +=",'" + fornitore.getUtente() + "'" ;   // ute_var
					sql +=",'" + ts + "'";   // ts_var
					sql +=",'N'";   // fl_canc

					sql +=",null";   // Luglio 2013-produzione editoriale-MANTIS 5338 Collaudo

					// OTTOBRE 2013 Inserimento valore regione solo se valorizzato altrimenti null
					if (fornitore.getRegione() == null) {
						sql +=",null";  // regione
					} else {
						sql +=",'" +  fornitore.getRegione() + "'" ;  // regione
					}

					sql +=")" ;  // cod_polo_bib



					pstmt = connection.prepareStatement(sql);
					log.debug("Debug: inserimento  fornitore");
					log.debug("Debug: " + sql);

					int intRet = 0;
					intRet = pstmt.executeUpdate();
					pstmt.close();
					// fine estrazione codice
					if (intRet==1){
						// estrazione del codice ed attribuzione del codice al fornitore visualizzato
						String sqlCodice="select tbr_fornitori.cod_fornitore from tbr_fornitori ";
						//sqlCodice=sqlCodice + " where ts_ins='" +ts+ "'";
						sqlCodice=sqlCodice + " where ute_ins='" + fornitore.getUtente()+ "'";
						sqlCodice=sqlCodice + " order by ts_ins desc limit 1";

						pstmt = connection.prepareStatement(sqlCodice);
						rsSub = pstmt.executeQuery();

						if (rsSub.next()) {
							fornitore.setCodFornitore(String.valueOf(rsSub.getInt("cod_fornitore")));
						}
						rsSub.close();
						pstmt.close();
						valRitornoF=true;
						}
					else
						{
							valRitornoF=false;
						}

				}
				else
				{
					// faccio update anche del flag e non insert
					// da gestire
					// modifica fornitore esistente  LOGICAMENTE CANCELLATO
					String sqlUDP="update tbr_fornitori set " ;
					Timestamp ts = DaoManager.now();
					//sql += " cd_bib_ins = '" +  fornitore.getCodBibl() + "'" ;  // cd_bib_ins
					//sql = sql + ", data_ins='" + ts + "'" ;   // data_ins
					//sqlUDP = sqlUDP + " cd_bib_agg='" +  fornitore.getCodBibl() + "'" ;  //
					sqlUDP = sqlUDP + "nom_fornitore='" +  fornitore.getNomeFornitore().trim().replace("'","''") + "'" ;  //
					sqlUDP = sqlUDP + ",unit_org='" +  fornitore.getUnitaOrg().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",indirizzo='" +  fornitore.getIndirizzo().trim().replace("'","''") + "'" ;  //
					sqlUDP = sqlUDP + ",cpostale='" +  fornitore.getCasellaPostale().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",citta='" +  fornitore.getCitta().trim().replace("'","''")  + "'" ;  //
					sqlUDP = sqlUDP + ",cap='" +  fornitore.getCap().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",telefono='" +  fornitore.getTelefono().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",fax='" +  fornitore.getFax().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",note='" +  fornitore.getNote().trim().replace("'","''") + "'" ;  //
					sqlUDP = sqlUDP + ",p_iva='" +  fornitore.getPartitaIva().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",cod_fiscale='" +  fornitore.getCodiceFiscale().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",e_mail='" +  fornitore.getEmail().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",paese='" +  fornitore.getPaese() + "'" ;  //
					sqlUDP = sqlUDP + ",tipo_partner='" +  fornitore.getTipoPartner() + "'" ;  //
					sqlUDP = sqlUDP + ",provincia='" +  fornitore.getProvincia() + "'" ;  //
					// se fornitore-biblioteca fornire il cod polo e codbib relativi
					if (!fornitore.getTipoPartner().equals("B"))
					{
						fornitore.setBibliotecaFornitore("");
						fornitore.setBibliotecaFornitoreCodPolo("");
					}
					sqlUDP = sqlUDP + " cod_bib='" + fornitore.getBibliotecaFornitore()+ "'" ;  //
					sqlUDP = sqlUDP + " cod_polo_bib='" + fornitore.getBibliotecaFornitoreCodPolo()+ "'" ;  //
					sqlUDP = sqlUDP + ", ute_var='" + fornitore.getUtente() + "'" ;   // ute_var
					sqlUDP = sqlUDP + ", ts_var='" + ts + "'";   // ts_var
					sqlUDP = sqlUDP + ", fl_canc<>'S'";   // aggiornamento flag di cancellazione
					sqlUDP = sqlUDP + ",regione='" +  fornitore.getRegione() + "'" ;  //
					if (fornCancLogic>0)
					{
						sqlUDP=this.struttura(sqlUDP);
						sqlUDP = sqlUDP + "  cod_fornitore=" + fornCancLogic;  // cod_fornitore

					}
					sqlUDP=this.struttura(sqlUDP);
					sqlUDP = sqlUDP + "   fl_canc='S'";

					pstmt = connection.prepareStatement(sqlUDP);
					int intRetUDP = 0;
					intRetUDP = pstmt.executeUpdate();
					pstmt.close();
					// fine estrazione codice
					if (intRetUDP==1){
						valRitornoUPD=true;
						fornitore.setCodFornitore(String.valueOf(fornCancLogic));
					}
					else
						{
						valRitornoUPD=false;
						}
					//connection.close();
					if (valRitornoUPD && fornBiblCancLogic!=null)
					{
						// modifica fornitore biblioteca LOGICAMENTE CANCELLATO
						String sqlFB="update tbr_fornitori_biblioteche set " ;
						sqlFB= sqlFB  + "tipo_pagamento='" +  fornitore.getFornitoreBibl().getTipoPagamento().trim() + "'" ;  //
						sqlFB= sqlFB  + ",cod_cliente='" +  fornitore.getFornitoreBibl().getCodCliente().trim() + "'" ;  //
						sqlFB= sqlFB  + ",nom_contatto='" +  fornitore.getFornitoreBibl().getNomContatto().trim() + "'" ;  //
						sqlFB= sqlFB  + ",tel_contatto='" +  fornitore.getFornitoreBibl().getTelContatto().trim() + "'" ;  //
						sqlFB= sqlFB  + ",fax_contatto='" +  fornitore.getFornitoreBibl().getFaxContatto().trim() + "'" ;  //
						sqlFB= sqlFB  + ",valuta='" +  fornitore.getFornitoreBibl().getValuta() + "'" ;  //
						sqlFB= sqlFB + ", ute_var='" + fornitore.getUtente() + "'" ;   // ute_var
						sqlFB= sqlFB + ", ts_var='" + ts + "'";   // ts_var
						sqlFB= sqlFB + ", fl_canc<>'S'" ;   // aggiornamento fl_canc
						sqlFB= sqlFB  + " where cod_fornitore='" + fornCancLogic + "'";
						sqlFB= sqlFB  + " and cd_biblioteca='" + fornBiblCancLogic + "'";
						sqlFB= sqlFB  + " and cd_polo='" + fornitore.getCodPolo() + "'";
						sqlFB= sqlFB  + "  and  fl_canc='S'" ;

						pstmt = connection.prepareStatement(sqlFB);
						int intRetFB = 0;
						intRetFB = pstmt.executeUpdate();
						pstmt.close();
						// fine estrazione codice
						if (intRetFB==1){
							valRitornoFB=true;
						} else {
							valRitornoFB=false;
						}

					}

				}

			}
			else
			{
				if (!datiFornBiblAssenti)
				{
					// modifica fornitore esistente
					String sqlUDP="update tbr_fornitori set " ;
					Timestamp ts = DaoManager.now();
					sqlUDP = sqlUDP + "nom_fornitore='" +  fornitore.getNomeFornitore().trim().replace("'","''") + "'" ;  //
					sqlUDP = sqlUDP + ",unit_org='" +  fornitore.getUnitaOrg().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",indirizzo='" +  fornitore.getIndirizzo().trim().replace("'","''") + "'" ;  //
					sqlUDP = sqlUDP + ",cpostale='" +  fornitore.getCasellaPostale().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",citta='" +  fornitore.getCitta().trim().replace("'","''")  + "'" ;  //
					sqlUDP = sqlUDP + ",cap='" +  fornitore.getCap().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",telefono='" +  fornitore.getTelefono().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",fax='" +  fornitore.getFax().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",note='" +  fornitore.getNote().trim().replace("'","''") + "'" ;  //
					sqlUDP = sqlUDP + ",p_iva='" +  fornitore.getPartitaIva().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",cod_fiscale='" +  fornitore.getCodiceFiscale().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",e_mail='" +  fornitore.getEmail().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",paese='" +  fornitore.getPaese() + "'" ;  //
					sqlUDP = sqlUDP + ",tipo_partner='" +  fornitore.getTipoPartner() + "'" ;  //
					sqlUDP = sqlUDP + ",provincia='" +  fornitore.getProvincia() + "'" ;  //
					// se fornitore-biblioteca fornire il cod polo e codbib relativi
					if (!fornitore.getTipoPartner().equals("B"))
					{
						fornitore.setBibliotecaFornitore("");
						fornitore.setBibliotecaFornitoreCodPolo("");
					}
					sqlUDP = sqlUDP + " cod_bib='" + fornitore.getBibliotecaFornitore()+ "'" ;  //
					sqlUDP = sqlUDP + " cod_polo_bib='" + fornitore.getBibliotecaFornitoreCodPolo()+ "'";  //
					sqlUDP = sqlUDP + ", ute_var='" + fornitore.getUtente() + "'" ;   // ute_var
					sqlUDP = sqlUDP + ", ts_var='" + ts + "'";   // ts_var
					sqlUDP = sqlUDP + "  where cod_fornitore='" + fornitore.getCodFornitore() + "'" ;  // cod_fornitore

					pstmt = connection.prepareStatement(sqlUDP);
					int intRetUDP = 0;
					intRetUDP = pstmt.executeUpdate();
					pstmt.close();
					// fine estrazione codice
					if (intRetUDP==1){
						valRitornoUPD=true;
						}
					else
						{
						valRitornoUPD=false;
						}
					//connection.close();
				}
			}
			if (valRitornoF || valRitornoUPD)
			{
				// ASSOCIAZIONE PROFILI ACQUISTO AL FORNITORE solo se sono fornitori di biblioteca

				// cancellazione fisica	di quelli precedentemente associati
				String sqlDEL3="delete from tra_sez_acquisizione_fornitori ";

				sqlDEL3=this.struttura(sqlDEL3);
				sqlDEL3=sqlDEL3 + " fl_canc = 'N'" ;  // fl_canc
				if (fornitore.getCodPolo() !=null &&  fornitore.getCodPolo().length()!=0)
				{
					sqlDEL3=this.struttura(sqlDEL3);
					sqlDEL3=sqlDEL3   + " cd_polo='" + fornitore.getCodPolo() +"'";
				}

				if (fornitore.getFornitoreBibl().getCodBibl() !=null &&  fornitore.getFornitoreBibl().getCodBibl().length()!=0)
				{
					sqlDEL3=this.struttura(sqlDEL3);
					sqlDEL3=sqlDEL3   + " cd_biblioteca='" + fornitore.getFornitoreBibl().getCodBibl() +"'";
				}
				if (fornitore.getCodFornitore() !=null &&   fornitore.getCodFornitore().trim().length()!=0)
				{
					sqlDEL3=this.struttura(sqlDEL3);
					sqlDEL3=sqlDEL3  + " cod_fornitore=" + fornitore.getCodFornitore().trim() ;
				}

				pstmtUDP3 = connection.prepareStatement(sqlDEL3);
				int intRetCANC2 = 0;
				intRetCANC2 = pstmtUDP3.executeUpdate();
				pstmtUDP3.close();
				// fine cancellazione associazione profili

				if (fornitore.getFornitoreBibl()!=null && fornitore.getFornitoreBibl().getProfiliAcq()!=null && fornitore.getFornitoreBibl().getProfiliAcq().size()>0 )
				{
					Timestamp ts = DaoManager.now();
					for (int i=0; i<fornitore.getFornitoreBibl().getProfiliAcq().size(); i++)
					{
						StrutturaProfiloVO oggettoDettProfilo= fornitore.getFornitoreBibl().getProfiliAcq().get(i);
						String oggettoDettProfiloCod=oggettoDettProfilo.getProfilo().getCodice().trim();
						// inserimento

						String sqlSub2="insert into tra_sez_acquisizione_fornitori values ( " ;
						sqlSub2 = sqlSub2+  "'" + fornitore.getCodPolo() + "'" ;  // cd_polo
						sqlSub2 = sqlSub2+  ",'" + fornitore.getFornitoreBibl().getCodBibl() + "'" ;  // cd_bib
						sqlSub2 = sqlSub2+  "," +  oggettoDettProfiloCod ;  // cod_prac
						sqlSub2 = sqlSub2+  "," +  fornitore.getCodFornitore().trim()  ;  // cod_fornitore
						sqlSub2 = sqlSub2 + ",'" + fornitore.getUtente() + "'" ;   // ute_ins
						sqlSub2 = sqlSub2 + ",'" + ts + "'" ;   // ts_ins
						sqlSub2 = sqlSub2 + ",'" + fornitore.getUtente() + "'" ;   // ute_var
						sqlSub2 = sqlSub2 + ",'" + ts + "'";   // ts_var
						sqlSub2 = sqlSub2 + ",'N'";   // fl_canc
						sqlSub2 = sqlSub2+ ")" ;
						pstmtINS = connection.prepareStatement(sqlSub2);
						int intRetINSLOOP = 0;
						intRetINSLOOP = pstmtINS.executeUpdate();
						pstmtINS.close();
						if (intRetINSLOOP!=1){
							valRitornoINSLOOP=true;
						}
					}
				}

			}

			if (!esistenzaFornitoreBiblioteca) {
				if (!datiFornBiblAssenti && (valRitornoF || valRitornoUPD) && !esistenzaFornitoreCancLogic)	{
					// Luglio 2013: nel caso di codice regione impostato siamo nella casistica di creazione editore quindi
					// non si devono inserire i dati nella tabella tbr_fornitori_biblioteche
					if (fornitore.getRegione() == null || fornitore.getRegione().length() == 0) {
						// inserimento fornitore biblioteca
						String sqlFB="insert into tbr_fornitori_biblioteche values ( " ;
						Timestamp ts = DaoManager.now();
						sqlFB= sqlFB + "'" +  fornitore.getCodPolo() + "'" ;  // cd_bib
						sqlFB= sqlFB + ",'" +  fornitore.getFornitoreBibl().getCodBibl() + "'" ;  // cd_biblioteca
						sqlFB= sqlFB + "," +  fornitore.getCodFornitore()  ;  // cod_fornitore
						sqlFB= sqlFB  + ",'" +  fornitore.getFornitoreBibl().getTipoPagamento().trim() + "'" ;  // tipo_pagamento
						sqlFB= sqlFB  + ",'" +  fornitore.getFornitoreBibl().getCodCliente().trim() + "'" ;  // cod_cliente
						sqlFB= sqlFB  + ",'" +  fornitore.getFornitoreBibl().getNomContatto().trim().replace("'","''") + "'" ;  // nom_contatto
						sqlFB= sqlFB  + ",'" +  fornitore.getFornitoreBibl().getTelContatto().trim() + "'" ;  // tel_contatto
						sqlFB= sqlFB  + ",'" +  fornitore.getFornitoreBibl().getFaxContatto().trim() + "'" ;  // fax_contatto
						sqlFB= sqlFB  + ",'" +  fornitore.getFornitoreBibl().getValuta() + "'" ;  // valuta
						sqlFB= sqlFB  + ",'" +  fornitore.getFornitoreBibl().getCodPolo() + "'" ;  // cod_polo
						sqlFB= sqlFB  + ",' '" ;  // allinea
						sqlFB= sqlFB + ",'" + fornitore.getUtente() + "'" ;   // ute_ins
						sqlFB= sqlFB + ",'" + ts + "'" ;   // ts_ins
						sqlFB= sqlFB + ",'" + fornitore.getUtente() + "'" ;   // ute_var
						sqlFB= sqlFB + ",'" + ts + "'";   // ts_var
						sqlFB= sqlFB + ",'N'";   // fl_canc
						sqlFB= sqlFB  + ")" ;  // cod_polo_bib
						pstmt = connection.prepareStatement(sqlFB);
						int intRetFB = 0;
						intRetFB = pstmt.executeUpdate();
						pstmt.close();
						// fine estrazione codice
						if (intRetFB==1){
							valRitornoFB=true;
						} else {
							valRitornoFB=false;
						}
					} else {
						// Luglio 2013: nel caso di codice regione impostato siamo nella casistica di creazione editore quindi
						// non si devono inserire i dati nella tabella tbr_fornitori_biblioteche e viene forzato il valore di
						// ritorno come se l'operazione fosse andata a buon fine
						valRitornoFB=true;
					}
				}
			}
			connection.close();
			// impostazione del codice di ritorno finale

			if (!esistenzaFornitoreBiblioteca)
			{
				if (!esistenzaFornitore )
				{
					if (valRitornoF && !datiFornBiblAssenti && valRitornoFB)
					{
						valRitorno=true;
					}
					else if (valRitornoF && datiFornBiblAssenti)
					{
						valRitorno=true;
					}
					else
					{
						valRitorno=false;
					}
					// caso di cancellazione logica
					if (esistenzaFornitoreCancLogic  && valRitornoUPD)
					{
						valRitorno=false;
						if (fornBiblCancLogic!=null )
						{
							if (valRitornoFB)
							{
								valRitorno=true;
							}
						}
						else  // è stato aggiornato solo il fornitore cancellato
						{
							valRitorno=true;
						}

					}
/*					else
					{
						valRitorno=false;
					}
*/
				}
				else
				{

					if (datiFornBiblAssenti)
					{
						throw new ValidationException("fornitoreInserimentoImpossibile",
								ValidationExceptionCodici.fornitoreInserimentoImpossibile);
					}
					if (valRitornoFB && valRitornoUPD)
					{
						valRitorno=true;
					}
					else
					{
						//
						valRitorno=false;
					}
				}
			}

		}catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}
        return valRitorno;
	}

	public boolean  modificaFornitore(FornitoreVO fornitore) throws DataException, ApplicationException , ValidationException
	{
		// manca la gestione della memorizzazione dei profili del fornitore

		Validazione.ValidaFornitoreVO (fornitore);
		boolean valRitorno=false;
    	int motivo=0;
    	//FornitoreVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSet rsSub = null;
		//ResultSet rsInsTit = null;

		boolean valRitornoF=false;
		boolean valRitornoFB=false;
		boolean valRitornoUPD=false;

		PreparedStatement pstmtUDP2 = null;
		PreparedStatement pstmtINS = null;
		boolean valRitornoINSLOOP=false;

		//boolean valRitornoInsTit=false;
		boolean esistenzaFornitore=false;
		boolean esistenzaFornitoreBiblioteca=false;
		boolean datiFornBiblAssenti=false;

		PreparedStatement pstmtFBIns = null;
		boolean valRitornoFBIns=false;
		PreparedStatement pstmtFBDEL = null;
		boolean valRitornoFBDEL=false;


		try {
			// CONTROLLI PREVENTIVI
			connection = getConnection();
			String sql0="select forn.cod_fornitore  from tbr_fornitori  forn ";

			sql0=this.struttura(sql0);
			sql0=sql0 + " forn.fl_canc<>'S'";

			if (fornitore.getCodFornitore()!=null && fornitore.getCodFornitore().length()!=0  )
			{
				//TODO  gestire le biblioteche affiliate a fronte della selezione del bottone relativo
				sql0=this.struttura(sql0);
				sql0=sql0 + " forn.cod_fornitore='" + fornitore.getCodFornitore() +"'";
			}

			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			//valRitornoPrep=true; // se la esegue c'è un resultset
			// numero di righe del resultset
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;

			} //
			if (numRighe >= 1)
			{
				if (numRighe == 1)
				{
					esistenzaFornitore=true;
				}

				/*				motivo=2; // record forse già esistente quindi non inseribile
								throw new ValidationException("cambierroreInserimentoEsistenzaRecord",
										ValidationExceptionCodici.cambierroreInserimentoEsistenzaRecord);
				*/
			}
			rs.close();
			//connection.close();
			//
			if (esistenzaFornitore)
			{
				String sql00="select forn.cod_fornitore from tbr_fornitori forn ";
				if (fornitore.getFornitoreBibl()!=null && fornitore.getFornitoreBibl().getCodBibl()!=null && fornitore.getFornitoreBibl().getCodBibl().length()!=0)
				{
					sql00=sql00 + " join tbr_fornitori_biblioteche fornBib on fornBib.cod_fornitore=forn.cod_fornitore  ";
					sql00=this.struttura(sql00);
					sql00=sql00 + " fornBib.cd_biblioteca='" + fornitore.getFornitoreBibl().getCodBibl() +"'";
					sql00=this.struttura(sql00);
					sql00=sql00 + " fornBib.cd_polo='" + fornitore.getCodPolo() +"'";
					sql00=this.struttura(sql00);
					sql00=sql00 + " fornBib.cod_fornitore='" + fornitore.getCodFornitore() +"'";
					sql00=this.struttura(sql00);
					sql00=sql00 + " fornBib.fl_canc<>'S'";

				}
				sql00=this.struttura(sql00);
				sql00=sql00 + " forn.fl_canc<>'S'";

				pstmt = connection.prepareStatement(sql00);
				rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
				numRighe=0;
				while (rs.next()) {
					numRighe=numRighe+1;
				} //
				if (numRighe == 1)
				{
					esistenzaFornitoreBiblioteca=true;
				}
				rs.close();
				//connection.close();
				//rec = new FornitoreVO();
			}

			if (!esistenzaFornitore )
			{
				fornitore.setCodFornitore("");
				throw new ValidationException("fornitoreInserimentoErroreBaseDati",
						ValidationExceptionCodici.fornitoreInserimentoErroreBaseDati);
			}
			if (fornitore.getFornitoreBibl()==null )
			{
				datiFornBiblAssenti=true;
			}
			else
			{
				if ( 	(fornitore.getFornitoreBibl().getTipoPagamento()==null || fornitore.getFornitoreBibl().getTipoPagamento().trim().length()==0)
						&& (fornitore.getFornitoreBibl().getCodCliente()==null || fornitore.getFornitoreBibl().getCodCliente().trim().length()==0)
						&& (fornitore.getFornitoreBibl().getNomContatto()==null || fornitore.getFornitoreBibl().getNomContatto().trim().length()==0)
						&& (fornitore.getFornitoreBibl().getTelContatto()==null || fornitore.getFornitoreBibl().getTelContatto().trim().length()==0)
						&& (fornitore.getFornitoreBibl().getFaxContatto()==null || fornitore.getFornitoreBibl().getFaxContatto().trim().length()==0)
					// considerando la valuta  i dati della biblioteca operante anche se nulli vengono registrati su sottotabella
						&& (fornitore.getFornitoreBibl().getValuta()==null || fornitore.getFornitoreBibl().getValuta().trim().length()==0)
						//&& (fornitore.getFornitoreBibl().getCodPolo()==null || fornitore.getFornitoreBibl().getCodPolo().trim().length()==0)
					)
				{
					datiFornBiblAssenti=true;
				}
			}


			// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
			// gestione del campo isbnEditore/regione
			if (esistenzaFornitore)
			{
					// modifica fornitore esistente
					String sqlUDP="update tbr_fornitori set " ;
					Timestamp ts = DaoManager.now();
					sqlUDP = sqlUDP + " nom_fornitore='" +  fornitore.getNomeFornitore().trim().replace("'","''") + "'" ;  //
					sqlUDP = sqlUDP + ",unit_org='" +  fornitore.getUnitaOrg().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",indirizzo='" +  fornitore.getIndirizzo().trim().replace("'","''") + "'" ;  //
					sqlUDP = sqlUDP + ",cpostale='" +  fornitore.getCasellaPostale().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",citta='" +  fornitore.getCitta().trim().replace("'","''")  + "'" ;  //
					sqlUDP = sqlUDP + ",cap='" +  fornitore.getCap().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",telefono='" +  fornitore.getTelefono().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",fax='" +  fornitore.getFax().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",note='" +  fornitore.getNote().trim().replace("'","''") + "'" ;  //
					sqlUDP = sqlUDP + ",p_iva='" +  fornitore.getPartitaIva().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",cod_fiscale='" +  fornitore.getCodiceFiscale() + "'" ;  //
					sqlUDP = sqlUDP + ",e_mail='" +  fornitore.getEmail().trim() + "'" ;  //
					sqlUDP = sqlUDP + ",paese='" +  fornitore.getPaese() + "'" ;  //
					sqlUDP = sqlUDP + ",tipo_partner='" +  fornitore.getTipoPartner() + "'" ;  //
					sqlUDP = sqlUDP + ",provincia='" +  fornitore.getProvincia() + "'" ;  //
					sqlUDP = sqlUDP + ",regione='" +  fornitore.getRegione() + "'" ;  //
//					sqlUDP = sqlUDP + ",isbn_editore='" +  fornitore.getIsbnEditore() + "'" ;  //
					sqlUDP = sqlUDP + ", ute_var='" + fornitore.getUtente() + "'" ;   // ute_var
					sqlUDP = sqlUDP + ", ts_var='" + ts + "'";   // ts_var
					if (!fornitore.getTipoPartner().equals("B"))
					{
						fornitore.setBibliotecaFornitore("");
						fornitore.setBibliotecaFornitoreCodPolo("");
					}
					else // biblioteca come forn
					{
						if (fornitore.getBibliotecaFornitore()==null || fornitore.getBibliotecaFornitoreCodPolo()==null || fornitore.getBibliotecaFornitore().length()!=3 || fornitore.getBibliotecaFornitoreCodPolo().length()!=3)

						{
							fornitore.setBibliotecaFornitore("");
							fornitore.setBibliotecaFornitoreCodPolo("");
						}
					}

					sqlUDP = sqlUDP + ", cod_bib='" + fornitore.getBibliotecaFornitore()+ "'" ;  // cd_bib
					sqlUDP = sqlUDP + ", chiave_for='" + fornitore.getChiaveFor()+ "'" ;  // chiave_for
					sqlUDP = sqlUDP + ", cod_polo_bib='" + fornitore.getBibliotecaFornitoreCodPolo()+ "'" ;  // cod_polo_bib
					sqlUDP = sqlUDP + "  where cod_fornitore='" + fornitore.getCodFornitore() + "'" ;  // cod_fornitore
					sqlUDP = sqlUDP + "  and  fl_canc<>'S'" ;
					if (fornitore.getDataUpd()!=null )
					{
						sqlUDP=this.struttura(sqlUDP);
						sqlUDP=sqlUDP + " ts_var='" + fornitore.getDataUpd() + "'" ;
					}

					pstmt = connection.prepareStatement(sqlUDP);
					log.debug("Debug: modifica  fornitore");
					log.debug("Debug: " + sqlUDP);

					int intRetUDP = 0;
					intRetUDP = pstmt.executeUpdate();
					pstmt.close();
					// fine estrazione codice
					if (intRetUDP==1){
						valRitornoUPD=true;
						}
					else
						{
						valRitornoUPD=false;
							throw new ValidationException("operazioneInConcorrenza",
									ValidationExceptionCodici.operazioneInConcorrenza);

						}
					//connection.close();

			}

			if (valRitornoUPD)
			{
				// cancellazione fisica	di quelli precedentemente cancellati logicamente
				String sqlDEL2="delete from tra_sez_acquisizione_fornitori ";

				//sqlDEL2=this.struttura(sqlDEL2);
				//sqlDEL2=sqlDEL2 + " fl_canc = 'N'" ;  // fl_canc
				if (fornitore.getCodPolo() !=null &&  fornitore.getCodPolo().length()!=0)
				{
					sqlDEL2=this.struttura(sqlDEL2);
					sqlDEL2=sqlDEL2   + " cd_polo='" + fornitore.getCodPolo() +"'";
				}

				//Ottobre 2013: modifica per controllo che tutta l'area FornitoreBibl sia valorizzata; in caso contrario
				// siamo in presenza di editore puro.
				if (fornitore.getFornitoreBibl() !=null) {
					if (fornitore.getFornitoreBibl().getCodBibl() !=null &&  fornitore.getFornitoreBibl().getCodBibl().length()!=0)	{
						sqlDEL2=this.struttura(sqlDEL2);
						sqlDEL2=sqlDEL2   + " cd_biblioteca='" + fornitore.getFornitoreBibl().getCodBibl() +"'";
					}
				}
					// in caso di inserimento fornitore il codice va estratto
				if (fornitore.getCodFornitore() !=null &&   fornitore.getCodFornitore().trim().length()!=0)
				{
					sqlDEL2=this.struttura(sqlDEL2);
					sqlDEL2=sqlDEL2 + " cod_fornitore=" + fornitore.getCodFornitore().trim() ;
				}

				pstmtUDP2 = connection.prepareStatement(sqlDEL2);
				int intRetCANC2 = 0;
				intRetCANC2 = pstmtUDP2.executeUpdate();
				pstmtUDP2.close();
				// fine cancellazione associazione profili


				// ASSOCIAZIONE PROFILI ACQUISTO AL FORNITORE solo se sono fornitori di biblioteca
				if (fornitore.getFornitoreBibl()!=null && fornitore.getFornitoreBibl().getProfiliAcq()!=null && fornitore.getFornitoreBibl().getProfiliAcq().size()>0 )
				{

					Timestamp ts = DaoManager.now();
					for (int i=0; i<fornitore.getFornitoreBibl().getProfiliAcq().size(); i++)
					{
						StrutturaProfiloVO oggettoDettProfilo= fornitore.getFornitoreBibl().getProfiliAcq().get(i);
						// inserimento
						String oggettoDettProfiloCod=oggettoDettProfilo.getProfilo().getCodice().trim();

						String sqlSub2="insert into tra_sez_acquisizione_fornitori values ( " ;
						sqlSub2 = sqlSub2+  "'" + fornitore.getCodPolo() + "'" ;  // cd_polo
						sqlSub2 = sqlSub2+  ",'" + fornitore.getFornitoreBibl().getCodBibl() + "'" ;  // cd_bib
						sqlSub2 = sqlSub2+  "," +  oggettoDettProfiloCod ;  // cod_prac
						sqlSub2 = sqlSub2+  "," +  fornitore.getCodFornitore().trim()  ;  // cod_fornitore
						sqlSub2 = sqlSub2 + ",'" + fornitore.getUtente() + "'" ;   // ute_ins
						sqlSub2 = sqlSub2 + ",'" + ts + "'" ;   // ts_ins
						sqlSub2 = sqlSub2 + ",'" + fornitore.getUtente() + "'" ;   // ute_var
						sqlSub2 = sqlSub2 + ",'" + ts + "'";   // ts_var
						sqlSub2 = sqlSub2 + ",'N'";   // fl_canc
						sqlSub2 = sqlSub2+ ")" ;
						pstmtINS = connection.prepareStatement(sqlSub2);
						int intRetINSLOOP = 0;
						intRetINSLOOP = pstmtINS.executeUpdate();
						pstmtINS.close();
						if (intRetINSLOOP!=1){
							valRitornoINSLOOP=true;
						}
					}
				}

			}

			if (esistenzaFornitoreBiblioteca)
			{
				if (!datiFornBiblAssenti)
				{
					// modifica fornitore biblioteca
					String sqlFB="update tbr_fornitori_biblioteche set " ;
					Timestamp ts = DaoManager.now();
					sqlFB= sqlFB  + "tipo_pagamento='" +  fornitore.getFornitoreBibl().getTipoPagamento().trim() + "'" ;  //
					sqlFB= sqlFB  + ",cod_cliente='" +  fornitore.getFornitoreBibl().getCodCliente().trim() + "'" ;  //
					sqlFB= sqlFB  + ",nom_contatto='" +  fornitore.getFornitoreBibl().getNomContatto().trim() + "'" ;  //
					sqlFB= sqlFB  + ",tel_contatto='" +  fornitore.getFornitoreBibl().getTelContatto().trim() + "'" ;  //
					sqlFB= sqlFB  + ",fax_contatto='" +  fornitore.getFornitoreBibl().getFaxContatto().trim() + "'" ;  //
					sqlFB= sqlFB  + ",valuta='" +  fornitore.getFornitoreBibl().getValuta() + "'" ;  //
					sqlFB= sqlFB + ", ute_var='" + fornitore.getUtente() + "'" ;   // ute_var
					sqlFB= sqlFB + ", ts_var='" + ts + "'";   // ts_var
					sqlFB= sqlFB  + " where cod_fornitore='" + fornitore.getCodFornitore() + "'";
					sqlFB= sqlFB  + " and cd_biblioteca='" + fornitore.getFornitoreBibl().getCodBibl() + "'";
					sqlFB= sqlFB  + " and cd_polo='" + fornitore.getCodPolo() + "'";
					sqlFB= sqlFB  + "  and  fl_canc<>'S'" ;

					pstmt = connection.prepareStatement(sqlFB);
					int intRetFB = 0;
					intRetFB = pstmt.executeUpdate();
					pstmt.close();
					// fine estrazione codice
					if (intRetFB==1){
						valRitornoFB=true;
					} else {
						valRitornoFB=false;
					}

				}
				if (datiFornBiblAssenti)
				{

					// cancellazione fisica
					String sqlFBDEL="delete from tbr_fornitori_biblioteche ";
					sqlFBDEL= sqlFBDEL  + " where cod_fornitore='" + fornitore.getCodFornitore() + "'";
					sqlFBDEL= sqlFBDEL  + " and cd_biblioteca='" + fornitore.getFornitoreBibl().getCodBibl() + "'";
					sqlFBDEL= sqlFBDEL  + " and cd_polo='" + fornitore.getCodPolo() + "'";
					sqlFBDEL= sqlFBDEL  + "  and  fl_canc<>'S'" ;

					pstmtFBDEL = connection.prepareStatement(sqlFBDEL);
					int valRitornoIntFBDEL = 0;
					valRitornoIntFBDEL = pstmtFBDEL.executeUpdate();
					if (valRitornoIntFBDEL==1){
						valRitornoFBDEL=true;
					}else{
						valRitornoFBDEL=false;
					}


				}

			}
			if (!esistenzaFornitoreBiblioteca)
			{
				if (!datiFornBiblAssenti)
				{
					// inserimento fornitore biblioteca: se i dati di contatto sono assenti getDati
					String sqlFBIns="insert into tbr_fornitori_biblioteche values ( " ;
					Timestamp ts = DaoManager.now();
					sqlFBIns= sqlFBIns + "'" +  fornitore.getCodPolo() + "'" ;  // cd_bib
					sqlFBIns= sqlFBIns + ",'" +  fornitore.getCodBibl() + "'" ;  // cd_biblioteca
					sqlFBIns= sqlFBIns + "," +  fornitore.getCodFornitore()  ;  // cod_fornitore
					sqlFBIns= sqlFBIns  + ",'" +  fornitore.getFornitoreBibl().getTipoPagamento().trim() + "'" ;  // tipo_pagamento
					sqlFBIns= sqlFBIns  + ",'" +  fornitore.getFornitoreBibl().getCodCliente().trim() + "'" ;  // cod_cliente
					sqlFBIns= sqlFBIns  + ",'" +  fornitore.getFornitoreBibl().getNomContatto().trim().replace("'","''") + "'" ;  // nom_contatto
					sqlFBIns= sqlFBIns  + ",'" +  fornitore.getFornitoreBibl().getTelContatto().trim() + "'" ;  // tel_contatto
					sqlFBIns= sqlFBIns  + ",'" +  fornitore.getFornitoreBibl().getFaxContatto().trim() + "'" ;  // fax_contatto
					sqlFBIns= sqlFBIns  + ",'" +  fornitore.getFornitoreBibl().getValuta() + "'" ;  // valuta
					if (fornitore.getFornitoreBibl()!=null && fornitore.getFornitoreBibl().getCodPolo()!=null && fornitore.getFornitoreBibl().getCodPolo().trim().length()==3)
					{
						sqlFBIns= sqlFBIns  + ",'" +  fornitore.getFornitoreBibl().getCodPolo() + "'" ;  // cod_polo
					}
					else
					{
						sqlFBIns= sqlFBIns  + ",'   '" ;  // cod_polo
					}

					sqlFBIns= sqlFBIns  + ",' '" ;  // allinea
					sqlFBIns= sqlFBIns + ",'" + fornitore.getUtente() + "'" ;   // ute_ins
					sqlFBIns= sqlFBIns + ",'" + ts + "'" ;   // ts_ins
					sqlFBIns= sqlFBIns + ",'" + fornitore.getUtente() + "'" ;   // ute_var
					sqlFBIns= sqlFBIns + ",'" + ts + "'";   // ts_var
					sqlFBIns= sqlFBIns + ",'N'";   // fl_canc
					sqlFBIns= sqlFBIns  + ")" ;  // cod_polo_bib
					pstmtFBIns = connection.prepareStatement(sqlFBIns);
					int intRetFBIns = 0;
					intRetFBIns = pstmtFBIns.executeUpdate();
					pstmtFBIns.close();
					// fine estrazione codice
					if (intRetFBIns==1){
						valRitornoFBIns=true;
					} else {
						valRitornoFBIns=false;
					}

				}
			}


			connection.close();
			// impostazione del codice di ritorno finale
			if (esistenzaFornitore)
			{
				if (!datiFornBiblAssenti)
				{
					if (esistenzaFornitoreBiblioteca )
					{
						if (valRitornoFB && valRitornoUPD)
						{
							valRitorno=true;
						}
					}
					if (!esistenzaFornitoreBiblioteca)
					{
						if (valRitornoFBIns && valRitornoUPD)
						{
							valRitorno=true;
						}
					}
				}
				else
				{

					if (valRitornoUPD)
					{
						valRitorno=true;
					}
				}
			}


		}catch (ValidationException e) {
      	  throw e;
		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}
        return valRitorno;
	}

	public List<BilancioVO> getRicercaListaBilanci(ListaSuppBilancioVO ricercaBilanci) throws ResourceNotFoundException, ApplicationException, ValidationException
	{
		Validazione.ValidaListaSuppBilancioVO(ricercaBilanci);

		List<BilancioVO> listaBilanci = new ArrayList<BilancioVO>();

		int ret = 0;
        // execute the search here
		BilancioVO rec = null;
		BilancioDettVO recDett = null;

		Connection connection = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt0 = null;

		ResultSet rs = null;
		ResultSet rs0 = null;

		try {
			// contiene i criteri di ricerca
			connection = getConnection();
			String sql="select capBil.id_capitoli_bilanci,capBil.ts_var as dataUpd, capBil.ts_var,  capBil.cd_bib, capBil.cd_polo, capBil.esercizio, capBil.capitolo, TO_CHAR(capBil.budget,'999999999999999.99') as budgetCap ";

			sql +=" from  tbb_capitoli_bilanci capBil ";
			sql=this.struttura(sql);
			sql +=" capBil.fl_canc<>'S'";

			if (ricercaBilanci.getCodPolo() !=null &&  ricercaBilanci.getCodPolo().length()!=0)
			{
				sql=this.struttura(sql);
				sql +=" capBil.cd_polo='" + ricercaBilanci.getCodPolo() +"'";
			}

			if (ricercaBilanci.getCodBibl() !=null &&  ricercaBilanci.getCodBibl().length()!=0)
			{
				sql=this.struttura(sql);
				sql +=" capBil.cd_bib='" + ricercaBilanci.getCodBibl() +"'";
			}

			if (ricercaBilanci.getEsercizio()!=null && ricercaBilanci.getEsercizio().trim().length()!=0)
			{
				sql=this.struttura(sql);
				sql +=" capBil.esercizio=" + ricercaBilanci.getEsercizio() ;
			}

			if (ricercaBilanci.getCapitolo()!=null && ricercaBilanci.getCapitolo().length()!=0)
			{
				sql=this.struttura(sql);
				sql +=" capBil.capitolo=" + ricercaBilanci.getCapitolo() ;
			}

			// ordinamento passato
			if (ricercaBilanci.getOrdinamento()==null ||  (ricercaBilanci.getOrdinamento()!=null && ricercaBilanci.getOrdinamento().equals("")))
			{
				sql +=" order by capBil.cd_bib, capBil.esercizio desc, capBil.capitolo  ";

			}
			else if (ricercaBilanci.getOrdinamento().equals("1"))
			{
				sql +=" order by capBil.cd_bib, capBil.esercizio desc, capBil.capitolo  ";
			}
			else if (ricercaBilanci.getOrdinamento().equals("2"))
			{
				sql +=" order by capBil.cd_bib, capBil.capitolo, capBil.esercizio desc  ";
			}

			pstmt = connection.prepareStatement(sql);
			//log.debug("Debug: lettura bilanci");
			//log.debug("Debug: " + sql);

			rs = pstmt.executeQuery();
			int numRighe=0;
			int progrRighe=1;

			while (rs.next()) {
				//TO_CHAR(bil.budget,'999999999999999.99')
				numRighe=numRighe+1;
				rec = new BilancioVO();
				//rec.setProgressivo(numRighe);
				rec.setProgressivo(progrRighe);
				rec.setIDBil(rs.getInt("id_capitoli_bilanci"));
				rec.setCodPolo(rs.getString("cd_polo"));
				rec.setCodBibl(rs.getString("cd_bib"));
				rec.setEsercizio(String.valueOf(rs.getInt("esercizio")));
				rec.setCapitolo(String.valueOf(rs.getLong("capitolo")));
				rec.setBudgetDiCapitolo(Double.valueOf(rs.getString("budgetCap")));
				rec.setDataUpd(rs.getTimestamp("dataUpd"));

				String sql0="select capBil.cd_bib, capBil.esercizio, capBil.capitolo, TO_CHAR(capBil.budget,'999999999999999.99') as budgetCap  ";
				sql0=sql0 + ", bil.cod_mat,trim(TO_CHAR(bil.budget,'999999999999999.99'))  as budgBil, TO_CHAR(bil.ordinato,'999999999999999.99')  as ordinatoBil,TO_CHAR(bil.fatturato,'999999999999999.99') as fatturatoBil,TO_CHAR(bil.pagato,'999999999999999.99') as pagatoBil,TO_CHAR(bil.acquisito,'999999999999999.99') as acquisitoBil  ";
				//sql0=sql0 + ", bil.cod_mat, CAST(TO_CHAR(bil.budget,'999999999999999.99')  as NUMERIC(17, 2)) as budgBil, TO_CHAR(bil.ordinato,'999999999999999.99')  as ordinatoBil,TO_CHAR(bil.fatturato,'999999999999999.99') as fatturatoBil,TO_CHAR(bil.pagato,'999999999999999.99') as pagatoBil  ";
				sql0=sql0 + " from  tbb_capitoli_bilanci capBil ";
				//sql0=sql0 + " join tbb_bilanci bil on capBil.cd_bib=bil.cd_bib and capBil.esercizio=bil.esercizio and capBil.capitolo=bil.capitolo ";
				sql0=sql0 + " join tbb_bilanci bil on capBil.id_capitoli_bilanci=bil.id_capitoli_bilanci ";

				sql0=this.struttura(sql0);
				sql0=sql0 + " capBil.fl_canc<>'S' AND bil.fl_canc<>'S'";



				if (rec.getCodBibl() !=null &&  rec.getCodBibl().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0 + " capBil.cd_bib='" + rec.getCodBibl() +"'";
				}

				if (rec.getEsercizio()!=null && rec.getEsercizio().trim().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0 + " capBil.esercizio=" + rec.getEsercizio() ;
				}

				if (rec.getCapitolo()!=null && rec.getCapitolo().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0 + " capBil.capitolo=" + rec.getCapitolo() ;
				}
				pstmt0 = connection.prepareStatement(sql0);
				rs0 = pstmt0.executeQuery();

				List<BilancioDettVO> listaBilanciDett = new ArrayList<BilancioDettVO>();
				boolean cercaImpegno=false;
				while (rs0.next()) {
					recDett = new BilancioDettVO();
					recDett.setImpegno(rs0.getString("cod_mat"));
					if (ricercaBilanci.getImpegno()!=null && ricercaBilanci.getImpegno().length()!=0)
					{
						if (ricercaBilanci.getImpegno().trim().equals(recDett.getImpegno()))
						{
							cercaImpegno=true;
						}
					}

					recDett.setBudget(Double.valueOf(rs0.getString("budgBil")));
					recDett.setBudgetStr(rs0.getString("budgBil"));
					recDett.setImpegnato(rs0.getDouble("ordinatoBil"));
					recDett.setFatturato(rs0.getDouble("fatturatoBil"));
					recDett.setPagato(rs0.getDouble("pagatoBil"));
					recDett.setAcquisito(0.00);
					if (rs0.getString("acquisitoBil")!=null && rs0.getDouble("acquisitoBil")>0)
					{
						recDett.setAcquisito(rs0.getDouble("acquisitoBil"));
					}

					recDett.setImpFatt(recDett.getImpegnato() - recDett.getFatturato());
					recDett.setDispCassa(recDett.getBudget() - recDett.getPagato());
					recDett.setDispCompetenza(recDett.getBudget() - recDett.getImpegnato());
					recDett.setDispCompetenzaAcq(recDett.getBudget() - recDett.getAcquisito());
					listaBilanciDett.add(recDett);
				}
				//almaviva5_20130604 #4757
				Collections.sort(listaBilanciDett, BilancioDettVO.TIPO_IMPEGNO);
				rec.setDettagliBilancio(listaBilanciDett);
				if (ricercaBilanci.getImpegno()!=null && ricercaBilanci.getImpegno().length()!=0)
				{
					if (cercaImpegno)
					{
						listaBilanci.add(rec);
						progrRighe=progrRighe+1;
					}
				}
				else
				{
					listaBilanci.add(rec);
					progrRighe=progrRighe+1;
				}

				//listaBilanci.add(rec);
				rs0.close();
				pstmt0.close();

			}
			//
			rs.close();
			pstmt.close();
			connection.close();

		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

		Validazione.ValidaRicercaBilanci(listaBilanci);
        return listaBilanci;
	}

	public boolean  inserisciProfilo(StrutturaProfiloVO profilo) throws DataException, ApplicationException , ValidationException
	{
		Validazione.ValidaStrutturaProfiloVO(profilo);
		boolean valRitorno=false;
    	int motivo=0;
    	//FornitoreVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSet rsSub = null;
		ResultSet rsSubCod = null;
		ResultSet rsESISTESEZ = null;
		PreparedStatement pstmtESISTESEZ  = null;
		boolean controlloESISTSEZ=false;
		ResultSet rsESISTEFORN = null;
		PreparedStatement pstmtESISTEFORN  = null;
		boolean controlloESISTFORN=false;

		PreparedStatement pstmtInsFornBibl= null;
		boolean valRitornoInsFornBibl=false;


		//ResultSet rsInsTit = null;

		boolean valRitornoINS=false;
		boolean valRitornoINSLOOP=false;
		boolean esistenzaProfilo=false;
		try {
			// CONTROLLI PREVENTIVI
			connection = getConnection();

			String sql0="select * ";
			sql0=sql0+ " from  tba_profili_acquisto prof ";
			sql0=sql0 + " join tra_sez_acquisizione_fornitori sezAcqForn on prof.cod_prac=sezAcqForn.cod_prac and sezAcqForn.fl_canc<>'S' and sezAcqForn.cd_polo='" +  profilo.getCodPolo() + "' and sezAcqForn.cd_biblioteca='" +  profilo.getCodBibl() + "'";
			sql0=sql0 + " join tba_sez_acquis_bibliografiche sez  on sez.id_sez_acquis_bibliografiche=prof.id_sez_acquis_bibliografiche and  sez.fl_canc<>'S' and sez.cd_polo='" +  profilo.getCodPolo() + "'  and sez.cd_bib='" +  profilo.getCodBibl() + "'" ;
			sql0=sql0 + " join tbr_fornitori forn on sezAcqForn.cod_fornitore=forn.cod_fornitore and forn.fl_canc<>'S'";
			sql0=sql0 + " join tbr_fornitori_biblioteche fornBibl on fornBibl.cod_fornitore=forn.cod_fornitore  and fornBibl.cd_biblioteca='" + profilo.getCodBibl()+"' and fornBibl.cd_polo='" + profilo.getCodPolo()+"' and fornBibl.fl_canc<>'S'" ;

			sql0=this.struttura(sql0);
			sql0=sql0 + " prof.fl_canc<>'S'";

			if (profilo.getSezione()!=null && profilo.getSezione().getCodice().trim().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " upper(sez.cod_sezione)='" + profilo.getSezione().getCodice().trim() + "'";
			}
			if (profilo.getProfilo()!=null && profilo.getProfilo().getCodice().trim().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " prof.cod_prac=" + profilo.getProfilo().getCodice() ;
			}

			if (profilo.getProfilo()!=null && profilo.getProfilo().getDescrizione().trim().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " upper(prof.descr) like '" + profilo.getProfilo().getDescrizione().trim().toUpperCase() +"%'";;
			}

			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			Timestamp ts = DaoManager.now();
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;
				}
			if (numRighe>0)
			{
				esistenzaProfilo=true; // record forse già esistente quindi non inseribile
				throw new ValidationException("cambierroreInserimentoEsistenzaRecord",
						ValidationExceptionCodici.cambierroreInserimentoEsistenzaRecord);
			}
			pstmt.close();
			if (!esistenzaProfilo)
			{
				// CONTROLLO ESISTENZA SEZIONE
				String sqlESISTESEZ="select * from  tba_sez_acquis_bibliografiche sez " ;
				sqlESISTESEZ=this.struttura(sqlESISTESEZ);
				sqlESISTESEZ=sqlESISTESEZ + " sez.cd_polo='" + profilo.getCodPolo() +"'";
				sqlESISTESEZ=this.struttura(sqlESISTESEZ);
				sqlESISTESEZ=sqlESISTESEZ + " sez.cd_bib='" + profilo.getCodBibl() +"'";
				sqlESISTESEZ=this.struttura(sqlESISTESEZ);
				sqlESISTESEZ=sqlESISTESEZ + " upper(sez.cod_sezione)='" + profilo.getSezione().getCodice().trim().toUpperCase()+"'" ;
				sqlESISTESEZ=this.struttura(sqlESISTESEZ);
				sqlESISTESEZ=sqlESISTESEZ + " sez.fl_canc<>'S'";
				sqlESISTESEZ=this.struttura(sqlESISTESEZ);
				sqlESISTESEZ=sqlESISTESEZ + "(sez.data_val is null or sez.data_val>(SELECT CURRENT_DATE ))";

				pstmtESISTESEZ = connection.prepareStatement(sqlESISTESEZ);
				rsESISTESEZ = pstmtESISTESEZ.executeQuery(); // va in errore se non può restituire un recordset
				int numRigheSez=0;
				int id_sezione=0;
				while (rsESISTESEZ.next()) {
					numRigheSez=numRigheSez+1;
					id_sezione=rsESISTESEZ.getInt("id_sez_acquis_bibliografiche");
				}
				rsESISTESEZ.close();
				pstmtESISTESEZ.close();
				if (numRigheSez==1)
				{
					controlloESISTSEZ=true;
					// se esiste aggiorno il campo codice sezione del profilo
					profilo.setIDSez(id_sezione);
				}
				else
				{
					controlloESISTSEZ=false;
					// procedere con il delete del buono sopra creato
					throw new ValidationException("ordineIncongruenzaSezioneInesistente",
							ValidationExceptionCodici.ordineIncongruenzaSezioneInesistente);
				}

				// INSERIMENTO
				String sqlSub="insert into tba_profili_acquisto " ;
				sqlSub=sqlSub + " ( descr, paese, lingua, ts_ins, ts_var, ute_ins, ute_var, fl_canc, id_sez_acquis_bibliografiche)";
				sqlSub=sqlSub + " values (  ";
				// fine subquery
				//sqlSub=sqlSub + " nextval('tba_profili_acquisto_cod_prac_seq') " ;  // id_sez_acquis_bibliografiche
				sqlSub=sqlSub + "'" +  profilo.getProfilo().getDescrizione().trim().toUpperCase() + "'"  ;  // descr
				sqlSub=sqlSub + ",'" +  profilo.getPaese().getCodice().trim() + "'"  ;  // paese
				sqlSub=sqlSub + ",'" +  profilo.getLingua().getCodice().trim() + "'"  ;  // lingua
				sqlSub=sqlSub + ",'" + ts + "'" ;   // ts_ins
				sqlSub=sqlSub + ",'" + ts + "'";   // ts_var
				sqlSub=sqlSub + ",'" + profilo.getUtente() + "'" ;   // ute_ins
				sqlSub=sqlSub + ",'" + profilo.getUtente() + "'" ;   // ute_var
				sqlSub=sqlSub + ",'N'";   // fl_canc
				sqlSub=sqlSub + ",'" +  profilo.getIDSez() + "'"  ;  // id_sez_acquis_bibliografiche
				sqlSub=sqlSub + ")" ;
				pstmt = connection.prepareStatement(sqlSub);
				log.debug("Debug: inserimento profilo");
				log.debug("Debug: " + sqlSub);

				int intRetINS = 0;
				intRetINS = pstmt.executeUpdate();
				pstmt.close();
				connection.close();
				connection = getConnection();
				if (intRetINS==1){
					valRitornoINS=true;
					// estrazione del codice ed attribuzione del codice alla fattura visualizzata
					// fine estrazione codice
					String sqlCodice="select cod_prac from tba_profili_acquisto ";
					//sqlCodice=sqlCodice + " where ts_ins='" +ts+ "'";
					sqlCodice=sqlCodice + " where ute_ins='" + profilo.getUtente()+ "'";
					sqlCodice=sqlCodice + " order by ts_ins desc limit 1";
					pstmt = connection.prepareStatement(sqlCodice);
					rsSubCod = pstmt.executeQuery();
					if (rsSubCod.next()) {
						profilo.getProfilo().setCodice(String.valueOf(rsSubCod.getInt("cod_prac")));
					}
					rsSubCod.close();
				}
				if (profilo.getListaFornitori()!=null && profilo.getListaFornitori().size() >0 )
				{
					valRitornoINSLOOP=false;
					for (int i=0; i<profilo.getListaFornitori().size(); i++)
					{
						StrutturaTerna 	oggettoDettVO=(StrutturaTerna)profilo.getListaFornitori().get(i);
						// controllo esistenza fornitore
						String sqlESISTEFORN="select forn.*, fornBibl.cod_fornitore as codFornBibl from tbr_fornitori forn " ;
						sqlESISTEFORN=sqlESISTEFORN + " left join tbr_fornitori_biblioteche fornBibl on fornBibl.cod_fornitore=forn.cod_fornitore and fornBibl.fl_canc<>'S'"   ;
						//sqlESISTEFORN=this.struttura(sqlESISTEFORN);
						sqlESISTEFORN=sqlESISTEFORN + " and fornBibl.cd_polo='" + profilo.getCodPolo() +"'";
						//sqlESISTEFORN=this.struttura(sqlESISTEFORN);
						sqlESISTEFORN=sqlESISTEFORN + " and fornBibl.cd_biblioteca='" + profilo.getCodBibl() +"'";
						sqlESISTEFORN=this.struttura(sqlESISTEFORN);
						sqlESISTEFORN=sqlESISTEFORN + " forn.cod_fornitore=" + oggettoDettVO.getCodice2();
						sqlESISTEFORN=this.struttura(sqlESISTEFORN);
						sqlESISTEFORN=sqlESISTEFORN + " forn.fl_canc<>'S'" ;
						pstmtESISTEFORN = connection.prepareStatement(sqlESISTEFORN);
						rsESISTEFORN = pstmtESISTEFORN.executeQuery(); // va in errore se non può restituire un recordset

						if (!rsESISTEFORN.next()) {
							controlloESISTSEZ=false;
							throw new ValidationException("ordineIncongruenzaFornitoreInesistente",
									ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);
						}
						else
						{
							controlloESISTSEZ=true;
							// localizzazione del fornitore in caso di utilizzo nella creazione ordine
							if (rsESISTEFORN.getString("codFornBibl")==null ||  (rsESISTEFORN.getString("codFornBibl")!=null && rsESISTEFORN.getString("codFornBibl").trim().length()==0)  )
							{
								// esiste il fornitore in anagrafica e non esiste fra quelli di biblioteca
								String sqlFB="insert into tbr_fornitori_biblioteche values ( " ;
								ts = DaoManager.now();
								sqlFB= sqlFB + "'" +  profilo.getCodPolo() + "'" ;  // cd_bib
								sqlFB= sqlFB + ",'" +  profilo.getCodBibl() + "'" ;  // cd_biblioteca
								sqlFB= sqlFB + "," +  oggettoDettVO.getCodice2()  ;  // cod_fornitore
								sqlFB= sqlFB  + ",''";  // tipo_pagamento
								sqlFB= sqlFB  + ",''";  // cod_cliente
								sqlFB= sqlFB  + ",''";  // nom_contatto
								sqlFB= sqlFB  + ",''";  // tel_contatto
								sqlFB= sqlFB  + ",''";  // fax_contatto
								sqlFB= sqlFB  + ",'EUR'" ;  // valuta
								sqlFB= sqlFB  + ",'" +  profilo.getCodPolo() + "'" ;  // cod_polo
								sqlFB= sqlFB  + ",' '" ;  // allinea
								sqlFB= sqlFB + ",'" + profilo.getUtente() + "'" ;   // ute_ins
								sqlFB= sqlFB + ",'" + ts + "'" ;   // ts_ins
								sqlFB= sqlFB + ",'" + profilo.getUtente() + "'" ;   // ute_var
								sqlFB= sqlFB + ",'" + ts + "'";   // ts_var
								sqlFB= sqlFB + ",'N'";   // fl_canc
								sqlFB= sqlFB  + ")" ;  // cod_polo_bib
								pstmtInsFornBibl = connection.prepareStatement(sqlFB);
								int intRetFB = 0;
								intRetFB = pstmtInsFornBibl.executeUpdate();
								pstmtInsFornBibl.close();
								// fine estrazione codice
								if (intRetFB==1){
									valRitornoInsFornBibl=true;
									//messaggio di localizzaziojne del fornitore
								} else {
									valRitornoInsFornBibl=false;
									throw new ValidationException("ordineIncongruenzaFornitoreInesistente",
											ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);
								}
							}
						}


						rsESISTEFORN.close();
						pstmtESISTEFORN.close();

						String sqlSub2="insert into tra_sez_acquisizione_fornitori values ( " ;
						//sqlSub2 = sqlSub2+ "'" + ts + "'" ;   // data_ins
						//sqlSub2 = sqlSub2+  ",'" + ts + "'";   // data_agg
						sqlSub2 = sqlSub2+  "'" + profilo.getCodPolo() + "'" ;  // cd_polo
						sqlSub2 = sqlSub2+  ",'" + profilo.getCodBibl() + "'" ;  // cd_bib
						sqlSub2 = sqlSub2+  "," +  profilo.getProfilo().getCodice() ;  // cod_prac
						sqlSub2 = sqlSub2+  "," +  oggettoDettVO.getCodice2()  ;  // cod_fornitore
						sqlSub2 = sqlSub2 + ",'" + profilo.getUtente() + "'" ;   // ute_ins
						sqlSub2 = sqlSub2 + ",'" + ts + "'" ;   // ts_ins
						sqlSub2 = sqlSub2 + ",'" + profilo.getUtente() + "'" ;   // ute_var
						sqlSub2 = sqlSub2 + ",'" + ts + "'";   // ts_var
						sqlSub2 = sqlSub2 + ",'N'";   // fl_canc
						sqlSub2 = sqlSub2+ ")" ;
						pstmt = connection.prepareStatement(sqlSub2);
						int intRetINSLOOP = 0;
						intRetINSLOOP = pstmt.executeUpdate();
						pstmt.close();
						if (intRetINSLOOP!=1){
							valRitornoINSLOOP=true;
						}
				}
			}
			rs.close();
			// impostazione del codice di ritorno finale
			if (valRitornoINS)
			{
				valRitorno=true;
				if (!valRitornoINSLOOP && profilo.getListaFornitori()!=null && profilo.getListaFornitori().size()>0)
				{
						valRitorno=true;
				}
				if (valRitornoINSLOOP)
				{
					valRitorno=false;
				}
			}
			if (valRitorno)
			{
			}
			connection.close();
		}
		}catch (ValidationException e) {
      	  	throw e;
		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

        return valRitorno;
	}


	public boolean  inserisciFattura(FatturaVO fattura) throws DataException, ApplicationException , ValidationException
	{
		Validazione.ValidaFatturaVO (fattura);
		boolean valRitorno=false;
    	int motivo=0;
    	//FornitoreVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;

		PreparedStatement pstmtESISTEORD = null;
		ResultSet rsESISTEORD = null;

		ResultSet rs = null;
		ResultSet rsSub = null;
		ResultSet rsSubCod = null;

		//ResultSet rsInsTit = null;

		boolean valRitornoINS=false;
		boolean valRitornoINSLOOP=false;
		boolean esistenzaFattura=false;
		PreparedStatement pstmtFatt = null;
		ResultSet rsFatt = null;


		try {
			// CONTROLLI PREVENTIVI
			connection = getConnection();
			String sql0="select * ";
			sql0=sql0 + " from tba_fatture fatt ";
			sql0=sql0 + " join tba_righe_fatture fattDett on fattDett.id_fattura=fatt.id_fattura AND fattDett.fl_canc<>'S' ";

			sql0=this.struttura(sql0);
			sql0=sql0 + " fatt.fl_canc<>'S' ";

			if (fattura.getCodPolo() !=null &&  fattura.getCodPolo().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " fatt.cd_polo='" + fattura.getCodPolo() +"'";
			}

			if (fattura.getCodBibl() !=null &&  fattura.getCodBibl().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " fatt.cd_bib='" + fattura.getCodBibl() +"'";
			}

			if (fattura.getAnnoFattura()!=null && fattura.getAnnoFattura().trim().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " fatt.anno_fattura=" +fattura.getAnnoFattura() ;
			}

			if (fattura.getProgrFattura()!=null && fattura.getProgrFattura().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0  + " fatt.progr_fattura=" + fattura.getProgrFattura() ;
			}
			if (fattura.getNumFattura()!=null && fattura.getNumFattura().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0  + " fatt.num_fattura='" + fattura.getNumFattura()  +"'" ;
			}
			if (fattura.getTipoFattura()!=null && fattura.getTipoFattura().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0  + " fatt.tipo_fattura='" + fattura.getTipoFattura()  +"'" ;
			}

			if (fattura.getDataFattura()!=null && fattura.getDataFattura().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0  + " fatt.data_fattura=TO_DATE ('" +  fattura.getDataFattura() + "','dd/MM/yyyy')";
			}
			if (fattura.getFornitoreFattura()!=null && fattura.getFornitoreFattura().getCodice()!=null && fattura.getFornitoreFattura().getCodice().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0  + " fatt.cod_fornitore=" +  fattura.getFornitoreFattura().getCodice() ;
			}


			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;
				}
			if (numRighe>0)
			{
				esistenzaFattura=true; // record forse già esistente quindi non inseribile
				throw new ValidationException("cambierroreInserimentoEsistenzaRecord",
						ValidationExceptionCodici.cambierroreInserimentoEsistenzaRecord);
			}
			pstmt.close();
			Timestamp ts = DaoManager.now();

			// CONTROLLI PREVENTIVI

			if (fattura.getFornitoreFattura().getCodice()!=null && fattura.getFornitoreFattura().getCodice()!=null  && fattura.getFornitoreFattura().getCodice().trim().length()!=0)
			{
				// subquery per test di esistenza fornitore : da sostituire con metodo ancora da scrivere in this
				String sqlSub="select * from tbr_fornitori forn  ";
				sqlSub=sqlSub + " join tbr_fornitori_biblioteche fornBibl on fornBibl.cod_fornitore=forn.cod_fornitore  and fornBibl.cd_biblioteca='" + fattura.getCodBibl()+"' and fornBibl.cd_polo='" + fattura.getCodPolo()+"' and fornBibl.fl_canc<>'S'" ;
				sqlSub=sqlSub + " where forn.fl_canc<>'S'";
				sqlSub=sqlSub + " and forn.cod_fornitore=" + fattura.getFornitoreFattura().getCodice().trim();

				pstmt = connection.prepareStatement(sqlSub);
				rsSub = pstmt.executeQuery();
				if (!rsSub.next()) {
					motivo=6;
					throw new ValidationException("ordineIncongruenzaFornitoreInesistente",
							ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);
				}

				rsSub.close();
			}

			if (fattura.getRigheDettaglioFattura()!=null && fattura.getRigheDettaglioFattura().size() >0 )
			{
				valRitornoINSLOOP=false;
				for (int i=0; i<fattura.getRigheDettaglioFattura().size(); i++)
				{
					StrutturaFatturaVO 	oggettoDettVO=fattura.getRigheDettaglioFattura().get(i);
					oggettoDettVO.setIDBil(0);

					if (fattura.isGestBil() && oggettoDettVO.getBilancio()!=null && oggettoDettVO.getBilancio().getCodice1()!=null && oggettoDettVO.getBilancio().getCodice1().trim().length()!=0)
					{

						if (fattura.isGestBil() && ((oggettoDettVO.getBilancio().getCodice3()!=null && oggettoDettVO.getBilancio().getCodice3().trim().equals("4")) || oggettoDettVO.getOrdine().getCodice1().equals("A") || oggettoDettVO.getOrdine().getCodice1().equals("V") || oggettoDettVO.getOrdine().getCodice1().equals("R")))
						{
							// subquery per test di esistenza bilancio : da sostituire con metodo ancora da scrivere in this
							String sqlSub="select * from tbb_capitoli_bilanci capBil  ";
							sqlSub=sqlSub + " join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and bil.fl_canc<>'S'";
							sqlSub=sqlSub + " where capBil.fl_canc<>'S'";
							sqlSub=sqlSub + " and capBil.cd_polo='" +fattura.getCodPolo()+"'";
							sqlSub=sqlSub + " and capBil.cd_bib='" +fattura.getCodBibl()+"'";
							if (oggettoDettVO.getBilancio().getCodice1()!=null && oggettoDettVO.getBilancio().getCodice1().length()!=0)
							{
								sqlSub=sqlSub + " and capBil.esercizio=" +oggettoDettVO.getBilancio().getCodice1();
							}
							if (oggettoDettVO.getBilancio().getCodice2()!=null && oggettoDettVO.getBilancio().getCodice2().length()!=0)
							{
								sqlSub=sqlSub + " and capBil.capitolo=" +oggettoDettVO.getBilancio().getCodice2();
							}
							if (oggettoDettVO.getBilancio().getCodice3()!=null && oggettoDettVO.getBilancio().getCodice3().length()!=0)
							{
								sqlSub=sqlSub + " and bil.cod_mat='" +oggettoDettVO.getBilancio().getCodice3()+ "'";
							}
							pstmt = connection.prepareStatement(sqlSub);
							rsSub = pstmt.executeQuery();
							if (!rsSub.next()) {
								motivo=5;
								throw new ValidationException("ordineIncongruenzaBilancioInesistente",
										ValidationExceptionCodici.ordineIncongruenzaBilancioInesistente);
/*								throw new ValidationException("ordineIncongruenzaBilancioInesistente" ,
										ValidationExceptionCodici.ordineIncongruenzaBilancioInesistente, "msgBil", String.valueOf(i+1), null );
*/
							}
							else
							{
								oggettoDettVO.setIDBil(rsSub.getInt("id_capitoli_bilanci"));
							}
							rsSub.close();
						}

					}
					oggettoDettVO.setIDOrd(0);

					if (oggettoDettVO.getOrdine()!=null && oggettoDettVO.getOrdine().getCodice1()!=null && oggettoDettVO.getOrdine().getCodice1().trim().length()!=0 && oggettoDettVO.getOrdine().getCodice2()!=null && oggettoDettVO.getOrdine().getCodice2().trim().length()!=0 && oggettoDettVO.getOrdine().getCodice3()!=null && oggettoDettVO.getOrdine().getCodice3().trim().length()!=0)
					{
						// controllo esistenza ordine (solo per righe fatture diverse da altre spese)

						String sqlESISTEORD="select * from tba_ordini ord " ;
						sqlESISTEORD=this.struttura(sqlESISTEORD);
						sqlESISTEORD=sqlESISTEORD + " ord.cd_bib='" + fattura.getCodBibl() +"'";
						sqlESISTEORD=this.struttura(sqlESISTEORD);
						sqlESISTEORD=sqlESISTEORD + " ord.cod_tip_ord='" + oggettoDettVO.getOrdine().getCodice1() +"'";
						sqlESISTEORD=this.struttura(sqlESISTEORD);
						sqlESISTEORD=sqlESISTEORD + " ord.anno_ord=" + oggettoDettVO.getOrdine().getCodice2() ;
						sqlESISTEORD=this.struttura(sqlESISTEORD);
						sqlESISTEORD=sqlESISTEORD + " ord.cod_ord=" +  oggettoDettVO.getOrdine().getCodice3() ;

						pstmtESISTEORD = connection.prepareStatement(sqlESISTEORD);
						rsESISTEORD = pstmtESISTEORD.executeQuery(); // va in errore se non può restituire un recordset
						if (!rsESISTEORD.next()) {
							// procedere con il delete del buono sopra creato
							throw new ValidationException("ordineNONtrovato",
									ValidationExceptionCodici.ordineNONtrovato);
						}
						else
						{
							oggettoDettVO.setIDOrd(rsESISTEORD.getInt("id_ordine"));
							// controllo che il bilancio e il fornitore dell'ordine siano quelli della fattura

							if (fattura.isGestBil() && oggettoDettVO.getIDBil()>0)
							{
								if (rsESISTEORD.getInt("id_capitoli_bilanci")!=oggettoDettVO.getIDBil())
								{
									throw new ValidationException("fatturaerroreCampoBilancioIncongruente",
									ValidationExceptionCodici.fatturaerroreCampoBilancioIncongruente);
								}
							}

							if (!rsESISTEORD.getString("cod_fornitore").equals(fattura.getFornitoreFattura().getCodice())  )
							{
								throw new ValidationException("fatturaerroreCampoFornitoreIncongruente",
										ValidationExceptionCodici.fatturaerroreCampoFornitoreIncongruente);
							}

						}
						rsESISTEORD.close();
						pstmtESISTEORD.close();


					}
					// controllo esistenza fattura per le note di credito con coincidenza di fornitore
					if (fattura.getTipoFattura().equals("N")  && oggettoDettVO.getIDFattNC()!=null && oggettoDettVO.getIDFattNC()>0)
					{
						String sqlfatt="select fatt.* ";
						sqlfatt=sqlfatt + " from  tba_fatture fatt ";
						sqlfatt=this.struttura(sqlfatt);
						sqlfatt=sqlfatt + " fatt.tipo_fattura ='F'"; // le note di credito possono essere associate solo a fatture
						sqlfatt=this.struttura(sqlfatt);
						sqlfatt=sqlfatt + " fatt.fl_canc<>'S' ";
						sqlfatt=this.struttura(sqlfatt);
						sqlfatt=sqlfatt + " fatt.id_fattura =" + oggettoDettVO.getIDFattNC();
						pstmtFatt = connection.prepareStatement(sqlfatt);
						rsFatt = pstmtFatt.executeQuery();

						if (!rsFatt.next()) {
							throw new ValidationException("fatturaNONtrovata",
									ValidationExceptionCodici.fatturaNONtrovata);
						}
						else
						{

							if (!rsFatt.getString("cod_fornitore").equals(fattura.getFornitoreFattura().getCodice())  )
							{
								throw new ValidationException("fatturaerroreCampoFornitoreIncongruenteNC",
										ValidationExceptionCodici.fatturaerroreCampoFornitoreIncongruenteNC);
							}
						}
						rsFatt.close();
						pstmtFatt.close();
					}
				} // fine for
			} // fine if

			// FINE CONTROLLI PREVENTIVI

			if (!esistenzaFattura)
			{
				// INSERIMENTO
				String sqlSub="insert into tba_fatture " ;
				sqlSub=sqlSub + " ( cod_fornitore, cd_polo, cd_bib, anno_fattura, progr_fattura, num_fattura, data_fattura, data_reg, importo, sconto, valuta, cambio, stato_fattura, tipo_fattura, ute_ins, ts_ins, ute_var, ts_var, fl_canc) ";
				sqlSub=sqlSub + " values ( ";
				sqlSub=sqlSub +   fattura.getFornitoreFattura().getCodice() ;  // cod_fornitore
				sqlSub=sqlSub + ",'" + fattura.getCodPolo() + "'" ;  // cd_polo
				sqlSub=sqlSub + ",'" + fattura.getCodBibl() + "'" ;  // cd_bib
				sqlSub=sqlSub + "," +  fattura.getAnnoFattura() ;  // anno_fattura
				// INIZIO SUBQUERY progr_fattura
				sqlSub=sqlSub + ", (SELECT CASE WHEN  (MAX(tba_fatture.progr_fattura) is null) THEN 1  else MAX(tba_fatture.progr_fattura)+1  END " ;
				sqlSub=sqlSub + " from tba_fatture   ";
				sqlSub=sqlSub + " where cd_bib='" + fattura.getCodBibl() +"'";
				sqlSub=sqlSub + " and cd_polo='" + fattura.getCodPolo() +"'";
				sqlSub=sqlSub + " and  anno_fattura='" + fattura.getAnnoFattura() +"'";
				sqlSub=sqlSub + " )";
				// fine subquery
				sqlSub=sqlSub + ",'" +  fattura.getNumFattura() + "'"  ;  // num_fattura
				sqlSub=sqlSub + ",TO_DATE('"+ fattura.getDataFattura()+"','dd/MM/yyyy') "  ;  // data_fattura
				sqlSub=sqlSub + ",(SELECT CURRENT_DATE )" ;  // data_reg
				sqlSub=sqlSub + "," +  fattura.getImportoFattura() ;  // importo
				sqlSub=sqlSub + "," +  fattura.getScontoFattura() ;  // sconto
				sqlSub=sqlSub + ",'" +  fattura.getValutaFattura() + "'" ;  // valuta
				sqlSub=sqlSub + "," +  fattura.getCambioFattura() ;  // cambio
				sqlSub=sqlSub + ",'" +  fattura.getStatoFattura() + "'" ;  // stato_fattura
				sqlSub=sqlSub + ",'" +  fattura.getTipoFattura() + "'" ;  // tipo_fattura
				sqlSub=sqlSub + ",'" + fattura.getUtente() + "'" ;   // ute_ins
				sqlSub=sqlSub + ",'" + ts + "'" ;   // ts_ins
				sqlSub=sqlSub + ",'" + fattura.getUtente() + "'" ;   // ute_var
				sqlSub=sqlSub + ",'" + ts + "'";   // ts_var
				sqlSub=sqlSub + ",'N'";   // fl_canc
				sqlSub=sqlSub + ")" ;
				pstmt = connection.prepareStatement(sqlSub);
				log.debug("Debug: inserimento  fattura");
				log.debug("Debug: " + sqlSub);

				int intRetINS = 0;
				intRetINS = pstmt.executeUpdate();
				pstmt.close();
				connection.close();
				connection = getConnection();
				if (intRetINS==1){
					valRitornoINS=true;
					// estrazione del codice ed attribuzione del codice alla fattura visualizzata
					// fine estrazione codice
					String sqlCodice="select progr_fattura from tba_fatture ";
					//sqlCodice=sqlCodice + " where ts_ins='" +ts+ "'";
					sqlCodice=sqlCodice + " where ute_ins='" + fattura.getUtente()+ "'";
					sqlCodice=sqlCodice + " order by ts_ins desc limit 1";

					pstmt = connection.prepareStatement(sqlCodice);
					rsSubCod = pstmt.executeQuery();

					if (rsSubCod.next()) {
						fattura.setProgrFattura(String.valueOf(rsSubCod.getInt("progr_fattura")));
					}
					rsSubCod.close();
					pstmt.close();

					String sqlCodice2="select id_fattura from tba_fatture ";
					//sqlCodice2=sqlCodice2 + " where ts_ins='" +ts+ "'";
					sqlCodice2=sqlCodice2 + " where ute_ins='" + fattura.getUtente()+ "'";
					sqlCodice2=sqlCodice2 + " order by ts_ins desc limit 1";

					pstmt = connection.prepareStatement(sqlCodice2);
					rsSubCod = pstmt.executeQuery();
					if (rsSubCod.next()) {
						fattura.setIDFatt(rsSubCod.getInt("id_fattura"));
					}
					rsSubCod.close();
					pstmt.close();

				}
				if (fattura.getRigheDettaglioFattura()!=null && fattura.getRigheDettaglioFattura().size() >0 )
				{
					valRitornoINSLOOP=false;
					for (int i=0; i<fattura.getRigheDettaglioFattura().size(); i++)
					{
						StrutturaFatturaVO 	oggettoDettVO=fattura.getRigheDettaglioFattura().get(i);

						String sqlSub2="insert into tba_righe_fatture values ( " ;
						sqlSub2 = sqlSub2 + "" + fattura.getIDFatt() ;   // id_fattura
						sqlSub2 = sqlSub2+  ",'" + fattura.getCodPolo() + "'" ;  // cd_polo
						sqlSub2 = sqlSub2+  ",'" + fattura.getCodBibl() + "'" ;  // cd_biblioteca
						sqlSub2 = sqlSub2+ "," + oggettoDettVO.getRigaFattura()  ;  // riga_fattura
						if (oggettoDettVO.getIDOrd()>0)
						{
							sqlSub2 = sqlSub2+ ",'" +  oggettoDettVO.getIDOrd() + "'";  // id_ordine
						}
						else
						{
							sqlSub2 = sqlSub2+ ",null";  // id_ordine
						}

						if (fattura.isGestBil() && oggettoDettVO.getIDBil()>0)
						{
							sqlSub2 = sqlSub2+ ",'" +  oggettoDettVO.getIDBil() + "'";  // id_capitoli_bilanci
						}
						else
						{
							sqlSub2 = sqlSub2+ ",null";  // id_capitoli_bilanci
						}
						if (fattura.isGestBil() && oggettoDettVO.getBilancio()!=null && oggettoDettVO.getBilancio().getCodice3()!=null && oggettoDettVO.getBilancio().getCodice3().trim().length()==1)
						{
							sqlSub2 = sqlSub2+ ",'" +  oggettoDettVO.getBilancio().getCodice3()+ "'"  ;  // cod_mat
						}
						else
						{
							sqlSub2 = sqlSub2+ ",null";  // cod_mat
						}

						if (oggettoDettVO.getNoteRigaFattura()!=null && oggettoDettVO.getNoteRigaFattura().trim().length()>0)
						{
							sqlSub2 = sqlSub2+ ",'" +  oggettoDettVO.getNoteRigaFattura().replace("'","''")+ "'"  ;  // note
						}
						else
						{
							sqlSub2 = sqlSub2+ ",''"  ;  // note
						}

						sqlSub2 = sqlSub2+ "," +  oggettoDettVO.getImportoRigaFattura() ;  // importo_riga
						sqlSub2 = sqlSub2+ "," +  oggettoDettVO.getSconto1RigaFattura();  // sconto_1
						sqlSub2 = sqlSub2+ "," +  oggettoDettVO.getSconto2RigaFattura() ;  // sconto_2
						sqlSub2 = sqlSub2+ ",'" +  oggettoDettVO.getCodIvaRigaFattura() + "'";  // cod_iva
						sqlSub2 = sqlSub2 + ",'" + fattura.getUtente() + "'" ;   // ute_ins
						sqlSub2 = sqlSub2 + ",'" + ts + "'" ;   // ts_ins
						sqlSub2 = sqlSub2 + ",'" + fattura.getUtente() + "'" ;   // ute_var
						sqlSub2 = sqlSub2 + ",'" + ts + "'";   // ts_var
						sqlSub2 = sqlSub2 + ",'N'";   // fl_canc


						// gestione note di credito
						if (fattura.getTipoFattura().equals("N")  && oggettoDettVO.getIDFattNC()!=null && oggettoDettVO.getIDFattNC()>0)
						{
							String sqlfatt="select fatt.* ";
							sqlfatt=sqlfatt + " from  tba_fatture fatt ";
							sqlfatt=this.struttura(sqlfatt);
							sqlfatt=sqlfatt + " fatt.tipo_fattura ='F'"; // le note di credito possono essere associate solo a fatture
							sqlfatt=this.struttura(sqlfatt);
							sqlfatt=sqlfatt + " fatt.fl_canc<>'S' ";
							sqlfatt=this.struttura(sqlfatt);
							sqlfatt=sqlfatt + " fatt.id_fattura =" + oggettoDettVO.getIDFattNC();
							pstmtFatt = connection.prepareStatement(sqlfatt);
							rsFatt = pstmtFatt.executeQuery();
							while (rsFatt.next()) {
								// la fattura esiste e deve essere univoca
								sqlSub2 = sqlSub2 + "," +oggettoDettVO.getIDFattNC();
								if (oggettoDettVO.getFattura()!=null && oggettoDettVO.getFattura().getCodice3()!=null && oggettoDettVO.getFattura().getCodice3().trim().length()>0 )
								{
									sqlSub2 = sqlSub2 + "," +Integer.valueOf(oggettoDettVO.getFattura().getCodice3().trim());   //numero riga fattura
								}
							}
							rsFatt.close();
							pstmtFatt.close();
						}
						//sqlSub2 = sqlSub2 + ",0" ; //  importo_tot_riga
						sqlSub2 = sqlSub2+ ")" ;
						pstmt = connection.prepareStatement(sqlSub2);
						int intRetINSLOOP = 0;
						intRetINSLOOP = pstmt.executeUpdate();
						pstmt.close();
						if (intRetINSLOOP!=1){
							valRitornoINSLOOP=true;
						}

				}
			}
			rs.close();
			// impostazione del codice di ritorno finale
			if (valRitornoINS)
			{
				valRitorno=true;
				if (!valRitornoINSLOOP && fattura.getRigheDettaglioFattura()!=null && fattura.getRigheDettaglioFattura().size()>0)
				{
						valRitorno=true;
				}
				if (valRitornoINSLOOP)
				{
					valRitorno=false;
				}
			}
			if (valRitorno)
			{
			}
			connection.close();
		}
		}catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

        return valRitorno;
	}


	public boolean  inserisciBilancio(BilancioVO bilancio) throws DataException, ApplicationException , ValidationException
	{
		Validazione.ValidaBilancioVO (bilancio);
		boolean valRitorno=false;
    	int motivo=0;
    	//FornitoreVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSet rsSub = null;
		//ResultSet rsInsTit = null;
		PreparedStatement pstmtCloG= null;
		ResultSet rsCloG= null;
		PreparedStatement pstmtUDP= null;
		PreparedStatement pstmtUDP2= null;
		PreparedStatement pstmtUDP3= null;
		boolean esistenzaBilancioCancLogic=false;
		boolean valRitornoINS=false;
		boolean valRitornoINSLOOP=false;
		boolean valRitornoUPD=false;
		boolean esistenzaBilancio=false;
		int intRetCANC = 0;
		int intRetCANC2 = 0;


		try {
			// CONTROLLI PREVENTIVI
			connection = getConnection();
			String sql0="select capBil.cd_bib, capBil.esercizio, capBil.capitolo, trim(TO_CHAR(capBil.budget,'999999999999999.99')) as budgetCap  ";
			sql0=sql0 + ", bil.cod_mat,trim(TO_CHAR(bil.budget,'999999999999999.99'))  as budgBil, TO_CHAR(bil.ordinato,'999999999999999.99')  as ordinatoBil,TO_CHAR(bil.fatturato,'999999999999999.99') as fatturatoBil,TO_CHAR(bil.pagato,'999999999999999.99') as pagatoBil  ";
			sql0=sql0 + " from  tbb_capitoli_bilanci capBil ";
			sql0=sql0 + " left join tbb_bilanci bil on capBil.id_capitoli_bilanci=bil.id_capitoli_bilanci and bil.fl_canc<>'S' ";

			sql0=this.struttura(sql0);
			sql0=sql0 + " capBil.fl_canc<>'S' ";

			if (bilancio.getCodPolo() !=null &&  bilancio.getCodPolo().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " capBil.cd_polo='" + bilancio.getCodPolo() +"'";
			}

			if (bilancio.getCodBibl() !=null &&  bilancio.getCodBibl().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " capBil.cd_bib='" + bilancio.getCodBibl() +"'";
			}

			if (bilancio.getEsercizio()!=null && bilancio.getEsercizio().trim().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " capBil.esercizio=" + bilancio.getEsercizio() ;
			}

			if (bilancio.getCapitolo()!=null && bilancio.getCapitolo().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " capBil.capitolo=" + bilancio.getCapitolo() ;
			}
			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			Timestamp ts = DaoManager.now();
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;
				}
			if (numRighe>0)
			{
				esistenzaBilancio=true; // record forse già esistente quindi non inseribile
				throw new ValidationException("cambierroreInserimentoEsistenzaRecord",
						ValidationExceptionCodici.cambierroreInserimentoEsistenzaRecord);

			}
			pstmt.close();
			if (!esistenzaBilancio)
			{



				// controllo preventivo che non sia stato cancellato logicamente: in tal caso
				// faccio update anche del flag e non insert

				String sql00C="select distinct capBil.id_capitoli_bilanci";
				sql00C=sql00C + " from  tbb_capitoli_bilanci capBil ";
				sql00C=sql00C + " left join tbb_bilanci bil on capBil.id_capitoli_bilanci=bil.id_capitoli_bilanci and bil.fl_canc<>'S'  ";

				sql00C=this.struttura(sql00C);
				sql00C=sql00C + " capBil.fl_canc='S' ";

				if (bilancio.getCodPolo() !=null &&  bilancio.getCodPolo().length()!=0)
				{
					sql00C=this.struttura(sql00C);
					sql00C=sql00C + " capBil.cd_polo='" + bilancio.getCodPolo() +"'";
				}

				if (bilancio.getCodBibl() !=null &&  bilancio.getCodBibl().length()!=0)
				{
					sql00C=this.struttura(sql00C);
					sql00C=sql00C + " capBil.cd_bib='" + bilancio.getCodBibl() +"'";
				}

				if (bilancio.getEsercizio()!=null && bilancio.getEsercizio().trim().length()!=0)
				{
					sql00C=this.struttura(sql00C);
					sql00C=sql00C + " capBil.esercizio=" + bilancio.getEsercizio() ;
				}

				if (bilancio.getCapitolo()!=null && bilancio.getCapitolo().length()!=0)
				{
					sql00C=this.struttura(sql00C);
					sql00C=sql00C + " capBil.capitolo=" + bilancio.getCapitolo() ;
				}

				pstmtCloG = connection.prepareStatement(sql00C);
				rsCloG = pstmtCloG.executeQuery(); // va in errore se non può restituire un recordset
				int numRigheCloG=0;
				int bilCancLogic=0;

				while (rsCloG.next()) {
					numRigheCloG=numRigheCloG+1;
					bilCancLogic=rsCloG.getInt("id_capitoli_bilanci");
				} //
				if (numRigheCloG >0)
				{
					esistenzaBilancioCancLogic=true;
				}
				rsCloG.close();

				if (esistenzaBilancioCancLogic)
				{
					if (bilancio.getDettagliBilancio()!=null && bilancio.getDettagliBilancio().size() >0 )
					{
						// cancellazione fisica	di tutte le righe legate al capitolo e precedentemente cancellati logicamente
						String sqlDEL2="delete from tbb_bilanci " ;
						sqlDEL2=this.struttura(sqlDEL2);
						sqlDEL2=sqlDEL2 + " fl_canc = 'S'" ;  // fl_canc
						sqlDEL2=this.struttura(sqlDEL2);
						sqlDEL2=sqlDEL2 + " id_capitoli_bilanci=" + bilCancLogic;
						pstmtUDP2 = connection.prepareStatement(sqlDEL2);
						intRetCANC2 = 0;
						intRetCANC2 = pstmtUDP2.executeUpdate();
						pstmtUDP2.close();
					}
					if (intRetCANC2>0)
					{
						// procedo con l'inserimento delle righe e l'aggiornamento del capitolo
						if (bilancio.getDettagliBilancio()!=null && bilancio.getDettagliBilancio().size() >0 )
						{
							valRitornoINSLOOP=false;
							for (int i=0; i<bilancio.getDettagliBilancio().size(); i++)
							{
								BilancioDettVO 	oggettoDettVO=bilancio.getDettagliBilancio().get(i);

								String sqlSub2="insert into tbb_bilanci values ( " ;
								sqlSub2 = sqlSub2 + "'" + oggettoDettVO.getImpegno() + "'" ;  // cod_mat
								sqlSub2 = sqlSub2 + "," + bilCancLogic ;   // id_capitoli_bilanci
								sqlSub2 = sqlSub2 + ",null"  ;   // id_buono_ordine
								sqlSub2 = sqlSub2 + "," +  oggettoDettVO.getBudget() ;  // budget
//									******************************************************************************************
//									 calcolare l'ordinato, il pagato e il fatturato con le opportune query sulle altre tabelle
//									******************************************************************************************
								sqlSub2 = sqlSub2 + ",0 "; //+  oggettoDettVO.getImpegnato() ;  // ordinato
								sqlSub2 = sqlSub2 + ",0"; //+  oggettoDettVO.getFatturato();  // fatturato
								sqlSub2 = sqlSub2 + ",0"; //+  oggettoDettVO.getPagato() ;  // pagato
								sqlSub2 = sqlSub2 + ",'" + bilancio.getUtente() + "'" ;   // ute_ins
								sqlSub2 = sqlSub2 + ",'" + ts + "'" ;   // ts_ins
								sqlSub2 = sqlSub2 + ",'" + bilancio.getUtente() + "'" ;   // ute_var
								sqlSub2 = sqlSub2 + ",'" + ts + "'";   // ts_var
								sqlSub2 = sqlSub2 + ",'N'";   // fl_canc
								sqlSub2 = sqlSub2 + ",0"; //+  oggettoDettVO.getPagato() ;  // acquisito
								//sqlSub2 = sqlSub2 + ",null"  ;   // id_buono_ordine
								sqlSub2 = sqlSub2 + ")" ;
								pstmt = connection.prepareStatement(sqlSub2);
								int intRetINSLOOP = 0;
								intRetINSLOOP = pstmt.executeUpdate();
								pstmt.close();
								if (intRetINSLOOP!=1){
									valRitornoINSLOOP=true;
								}
							}// fine for
						} // fine if size
						if (!valRitornoINSLOOP)
						{
							// aggiornamento CAPITOLO
							String sqlUDP="update tbb_capitoli_bilanci set " ;
							//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
							sqlUDP= sqlUDP + " ts_var = '" +  ts  + "'" ;  // data_agg
							sqlUDP= sqlUDP + ", ute_var = '" +  bilancio.getUtente() + "'" ;  //
							sqlUDP= sqlUDP + ", budget = " + bilancio.getBudgetDiCapitolo() ;  // budget
							sqlUDP= sqlUDP + ", fl_canc<>'S'";

							sqlUDP=this.struttura(sqlUDP);
							sqlUDP=sqlUDP + " fl_canc='S' ";
							sqlUDP=this.struttura(sqlUDP);
							sqlUDP=sqlUDP + " id_capitoli_bilanci=" + bilCancLogic ;

							pstmtUDP = connection.prepareStatement(sqlUDP);
							int intRetUDP = 0;
							intRetUDP = pstmtUDP.executeUpdate();
							pstmtUDP.close();
							// fine estrazione codice
							if (intRetUDP==1){
								valRitornoUPD=true;
								}
						}
						// impostazione del codice di ritorno finale
						if (valRitornoUPD)
						{
							valRitorno=true;
						}

					}
				} // fine cancellazione fisica di quella logica
				else
				{
					// INSERIMENTO
					String sqlSub="insert into tbb_capitoli_bilanci  " ;
					sqlSub=sqlSub + " ( cd_polo, cd_bib, esercizio, capitolo, budget, ute_ins, ts_ins, ute_var, ts_var, fl_canc) ";
					sqlSub=sqlSub + " values (";
					//sqlSub=sqlSub + " nextval('seq_tbb_capitoli_bilanci') " ;   // id_capitoli_bilanci
					sqlSub=sqlSub + "'" + bilancio.getCodPolo() + "'" ;  // cd_polo
					sqlSub=sqlSub + ",'" + bilancio.getCodBibl() + "'" ;  // cd_bib
					sqlSub=sqlSub + "," +  bilancio.getEsercizio() ;  // esercizio
					sqlSub=sqlSub + "," +  bilancio.getCapitolo()  ;  // capitolo
					sqlSub=sqlSub + "," +  bilancio.getBudgetDiCapitolo() ;  // budget
					sqlSub=sqlSub + ",'" + bilancio.getUtente() + "'" ;   // ute_ins
					sqlSub=sqlSub + ",'" + ts + "'" ;   // ts_ins
					sqlSub=sqlSub + ",'" + bilancio.getUtente() + "'" ;   // ute_var
					sqlSub=sqlSub + ",'" + ts + "'";   // ts_var
					sqlSub=sqlSub + ",'N'";   // fl_canc
					sqlSub=sqlSub + ")" ;
					pstmt = connection.prepareStatement(sqlSub);
					log.debug("Debug: inserimento bilancio");
					log.debug("Debug: " + sqlSub);
					int intRetINS = 0;
					intRetINS = pstmt.executeUpdate();
					pstmt.close();
					if (intRetINS==1){
						valRitornoINS=true;
						String sqlCodice="select id_capitoli_bilanci from tbb_capitoli_bilanci ";
						//sqlCodice=sqlCodice + " where ts_ins='" +ts+ "'";
						sqlCodice=sqlCodice + " where ute_ins='" + bilancio.getUtente()+ "'";
						sqlCodice=sqlCodice + " order by ts_ins desc limit 1";

						pstmt = connection.prepareStatement(sqlCodice);
						rsSub = pstmt.executeQuery();
						if (rsSub.next()) {
							bilancio.setIDBil(rsSub.getInt("id_capitoli_bilanci"));
						}
						rsSub.close();
					}
					if (bilancio.getDettagliBilancio()!=null && bilancio.getDettagliBilancio().size() >0 )
					{
						valRitornoINSLOOP=false;
						for (int i=0; i<bilancio.getDettagliBilancio().size(); i++)
						{
							BilancioDettVO 	oggettoDettVO=bilancio.getDettagliBilancio().get(i);

							String sqlSub2="insert into tbb_bilanci values ( " ;
							sqlSub2 = sqlSub2 + "'" + oggettoDettVO.getImpegno() + "'" ;  // cod_mat
							sqlSub2 = sqlSub2 + "," + bilancio.getIDBil() ;   // id_capitoli_bilanci
							sqlSub2 = sqlSub2 + ",null"  ;   // id_buono_ordine
							sqlSub2 = sqlSub2 + "," +  oggettoDettVO.getBudget() ;  // budget
//								******************************************************************************************
//								 calcolare l'ordinato, il pagato e il fatturato con le opportune query sulle altre tabelle
//								******************************************************************************************
							sqlSub2 = sqlSub2 + ",0 "; //+  oggettoDettVO.getImpegnato() ;  // ordinato
							sqlSub2 = sqlSub2 + ",0"; //+  oggettoDettVO.getFatturato();  // fatturato
							sqlSub2 = sqlSub2 + ",0"; //+  oggettoDettVO.getPagato() ;  // pagato
							sqlSub2 = sqlSub2 + ",'" + bilancio.getUtente() + "'" ;   // ute_ins
							sqlSub2 = sqlSub2 + ",'" + ts + "'" ;   // ts_ins
							sqlSub2 = sqlSub2 + ",'" + bilancio.getUtente() + "'" ;   // ute_var
							sqlSub2 = sqlSub2 + ",'" + ts + "'";   // ts_var
							sqlSub2 = sqlSub2 + ",'N'";   // fl_canc
							//sqlSub2 = sqlSub2 + ",null"  ;   // id_buono_ordine
							sqlSub2 = sqlSub2 + ",0"; //+  oggettoDettVO.getPagato() ;  // acquisito
							sqlSub2 = sqlSub2 + ")" ;
							pstmt = connection.prepareStatement(sqlSub2);
							int intRetINSLOOP = 0;
							intRetINSLOOP = pstmt.executeUpdate();
							pstmt.close();
							if (intRetINSLOOP!=1){
								valRitornoINSLOOP=true;
							}
						}// fine for
					} // fine if size
					// impostazione del codice di ritorno finale
					if (valRitornoINS)
					{
						valRitorno=true;
						if (!valRitornoINSLOOP && bilancio.getDettagliBilancio()!=null && bilancio.getDettagliBilancio().size()>0)
						{
								valRitorno=true;
						}
						if (valRitornoINSLOOP)
						{
							valRitorno=false;
						}

					}
				} // fine else di solo inserimento
			}	// fine if esistenzaBilancio
			rs.close();
			connection.close();

		}catch (ValidationException e) {
	        throw e;
		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

        return valRitorno;
	}


	public boolean modificaBilancio(BilancioVO bilancio, String cod_mat) throws DataException, ApplicationException, ValidationException {
		Validazione.ValidaBilancioVO(bilancio);
		boolean valRitorno = false;
		int motivo = 0;

		Connection connection = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtUDP = null;
		PreparedStatement pstmtUDP2 = null;

		ResultSet rs = null;
		ResultSet rsSub = null;

		boolean valRitornoUPD = false;
		boolean valRitornoUPDLOOP = false;
		boolean valRitornoCANC = false;
		boolean valRitornoINS = false;

		try {
			connection = getConnection();

			// CONTROLLI PREVENTIVI
			String sql="select capBil.cd_bib, capBil.esercizio, capBil.capitolo, trim(TO_CHAR(capBil.budget,'999999999999999.99')) as budgetCap  ";
			sql += ", bil.cod_mat,trim(TO_CHAR(bil.budget,'999999999999999.99'))  as budgBil, TO_CHAR(bil.ordinato,'999999999999999.99')  as ordinatoBil,TO_CHAR(bil.fatturato,'999999999999999.99') as fatturatoBil,TO_CHAR(bil.pagato,'999999999999999.99') as pagatoBil  ";
			sql += " from  tbb_capitoli_bilanci capBil ";
			sql += " join tbb_bilanci bil on capBil.id_capitoli_bilanci=bil.id_capitoli_bilanci ";
			sql=this.struttura(sql);
			sql += " capBil.fl_canc<>'S' ";

			sql=this.struttura(sql);
			sql += " bil.fl_canc<>'S' ";

			//almaviva5_20140604 #5575
			if (ValidazioneDati.isFilled(cod_mat)) {
				sql=this.struttura(sql);
				sql += " bil.cod_mat='" + cod_mat +"'";
			}

			if (bilancio.getCodPolo() !=null &&  bilancio.getCodPolo().length()!=0)
			{
				sql=this.struttura(sql);
				sql += " capBil.cd_polo='" + bilancio.getCodPolo() +"'";
			}

			if (bilancio.getCodBibl() !=null &&  bilancio.getCodBibl().length()!=0)
			{
				sql=this.struttura(sql);
				sql += " capBil.cd_bib='" + bilancio.getCodBibl() +"'";
			}

			if (bilancio.getEsercizio()!=null && bilancio.getEsercizio().trim().length()!=0)
			{
				sql=this.struttura(sql);
				sql += " capBil.esercizio=" + bilancio.getEsercizio() ;
			}

			if (bilancio.getCapitolo()!=null && bilancio.getCapitolo().length()!=0)
			{
				sql=this.struttura(sql);
				sql += " capBil.capitolo=" + bilancio.getCapitolo() ;
			}

			pstmt = connection.prepareStatement(sql);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			int numRighe = 0;
			// inizializzazione
			List<StrutturaCombo> righeOggetto = new ArrayList();
			List<BilancioDettVO> dettagliBilancio = bilancio.getDettagliBilancio();
			//almaviva5_20140604 #5575 filtro per aggiornare solo impegno variato (da ordine)
			if (ValidazioneDati.isFilled(cod_mat)) {
				BilancioDettVO p = on(BilancioDettVO.class);
				dettagliBilancio = filter(having(p.getImpegno(), equalTo(cod_mat)), dettagliBilancio);
			}
			int size = ValidazioneDati.size(dettagliBilancio);
			if (ValidazioneDati.isFilled(dettagliBilancio) ) {
				for (int i = 0; i < size; i++) {
					StrutturaCombo rigaOgg = new StrutturaCombo("", "");
					rigaOgg.setCodice(String.valueOf(i));
					rigaOgg.setDescrizione("I");
					righeOggetto.add(rigaOgg);
				}
			}
			Timestamp ts = DaoManager.now();
			while (rs.next()) {
				numRighe++;
				if (numRighe == 1) {
					String sqlUDP="update tbb_capitoli_bilanci set " ;
					//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
					sqlUDP= sqlUDP + " ts_var = '" +  ts  + "'" ;  // data_agg
					sqlUDP= sqlUDP + ", ute_var = '" +  bilancio.getUtente() + "'" ;  //
					sqlUDP= sqlUDP + ", budget = " + bilancio.getBudgetDiCapitolo() ;  // budget

					sqlUDP=this.struttura(sqlUDP);
					sqlUDP=sqlUDP + " fl_canc<>'S' ";

					if (bilancio.getCodPolo() !=null &&  bilancio.getCodPolo().length()!=0)
					{
						sqlUDP=this.struttura(sqlUDP);
						sqlUDP=sqlUDP + " cd_polo='" + bilancio.getCodPolo() +"'";
					}

					if (bilancio.getCodBibl() !=null &&  bilancio.getCodBibl().length()!=0)
					{
						sqlUDP=this.struttura(sqlUDP);
						sqlUDP=sqlUDP + " cd_bib='" + bilancio.getCodBibl() +"'";
					}

					if (bilancio.getEsercizio()!=null && bilancio.getEsercizio().trim().length()!=0)
					{
						sqlUDP=this.struttura(sqlUDP);
						sqlUDP += " esercizio=" + bilancio.getEsercizio() ;
					}

					if (bilancio.getCapitolo()!=null && bilancio.getCapitolo().length()!=0)
					{
						sqlUDP=this.struttura(sqlUDP);
						sqlUDP=sqlUDP + " capitolo=" + bilancio.getCapitolo() ;
					}
					if (bilancio.getDataUpd()!=null )
					{
						sqlUDP=this.struttura(sqlUDP);
						sqlUDP=sqlUDP + " ts_var='" + bilancio.getDataUpd() + "'" ;

					}

					pstmtUDP = connection.prepareStatement(sqlUDP);
					log.debug("Debug: modifica capitolo bilancio");
					log.debug("Debug: " + sqlUDP);

					int intRetUDP = 0;
					intRetUDP = pstmtUDP.executeUpdate();
					pstmtUDP.close();
					// fine estrazione codice
					if (intRetUDP == 1)
						valRitornoUPD = true;
					else
						throw new ValidationException("operazioneInConcorrenza",
								ValidationExceptionCodici.operazioneInConcorrenza);
				}
				if (valRitornoUPD) {
			        Locale locale=Locale.getDefault(); // aggiornare con quella locale se necessario

			        if (ValidazioneDati.isFilled(dettagliBilancio) ) {
						valRitornoUPDLOOP=false;

						for (int i=0; i<size; i++) {
							BilancioDettVO 	oggettoDettVO=dettagliBilancio.get(i);
							String impegno=oggettoDettVO.getImpegno();
							if (impegno.equals(rs.getString("cod_mat"))) {
								CalcoliVO output = this.calcola(bilancio.getCodPolo(),
										bilancio.getCodBibl(), 0, null,
										bilancio.getIDBil(),
										oggettoDettVO.getImpegno(), 0, null,
										null, null, locale);

								double imp = 0.00;
								double fatt = 0.00;
								double pag = 0.00;
								double acq = 0.00;

								if (output != null)	{
									if (output.getOrdinato()!=0)
									{
										imp=output.getOrdinato();
									}
									if (output.getFatturato()!=0)
									{
										fatt=output.getFatturato();
									}
									if (output.getPagato()!=0)
									{
										pag=output.getPagato();
									}
									if (output.getAcquisito()!=0)
									{
										acq=output.getAcquisito();
									}

									//dispStr=Pulisci.VisualizzaImporto(disp);
								}
								oggettoDettVO.setImpegnato(imp);
								oggettoDettVO.setFatturato(fatt);
								oggettoDettVO.setPagato(pag);
								oggettoDettVO.setAcquisito(acq);

								// ******FINE AGGIORNAMENTO DATI CALCOLATI DI BILANCIO



								// se esiste modifico
								valRitornoUPDLOOP=true;
								String sqlUDPLOOP="update tbb_bilanci set " ;
								//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
								sqlUDPLOOP= sqlUDPLOOP + " ts_var = '" +  ts  + "'" ;  // data_agg
								sqlUDPLOOP= sqlUDPLOOP + ", ute_var = '" +  bilancio.getUtente()  + "'" ;  // utente
								sqlUDPLOOP= sqlUDPLOOP + ", budget = " + oggettoDettVO.getBudget();  // budget
//								******************************************************************************************
//								 calcolare l'ordinato, il pagato e il fatturato con le opportune query sulle altre tabelle
//								******************************************************************************************
								sqlUDPLOOP= sqlUDPLOOP + ", ordinato = " + oggettoDettVO.getImpegnato()   ;  // ordinato
								sqlUDPLOOP= sqlUDPLOOP + ", fatturato = " + oggettoDettVO.getFatturato()   ;  // fatturato
								sqlUDPLOOP= sqlUDPLOOP + ", pagato = " + oggettoDettVO.getPagato()   ;  // pagato
								sqlUDPLOOP= sqlUDPLOOP + ", acquisito = " +  oggettoDettVO.getAcquisito()   ;  // acquisito

								sqlUDPLOOP=this.struttura(sqlUDPLOOP);
								sqlUDPLOOP=sqlUDPLOOP + " fl_canc<>'S' ";

								sqlUDPLOOP=this.struttura(sqlUDPLOOP);
								sqlUDPLOOP=sqlUDPLOOP + " id_capitoli_bilanci=" + bilancio.getIDBil();

								sqlUDPLOOP=this.struttura(sqlUDPLOOP);
								sqlUDPLOOP=sqlUDPLOOP+ " cod_mat='" + impegno + "'" ;


								pstmtUDP = connection.prepareStatement(sqlUDPLOOP);
								log.debug("Debug: modifica riga bilancio per impegno: " + impegno);
								log.debug("Debug: " + sqlUDPLOOP);
								int intRetUDPLOOP = 0;
								intRetUDPLOOP = pstmtUDP.executeUpdate();
								pstmtUDP.close();
								// fine estrazione codice
								if (intRetUDPLOOP == 1) {
									valRitornoUPDLOOP=true;
									righeOggetto.get(i).setCodice(String.valueOf(i));
									righeOggetto.get(i).setDescrizione("U"); //update
									break;
								}
							} // fine if (impegno.equals(rs.getString("cod_mat")))
						} // fine for
						if (!valRitornoUPDLOOP) // non c'è stato update
						{
							// il caso non esiste perchè il controllo della cancellazione della riga avviene dal bottone specifico
							// controllo l'esistenza dei legami  con altre tabelle ordini e fatture
							// se esistono impedisco la cancellazione della riga
							// va effettuata la cancellazione (ma non ora ) con gli impatti sulle altre tabelle ordini, fatture ecc

							// cancellazione logica
							String sqlDEL1="update tbb_bilanci set " ;
							//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
							sqlDEL1=sqlDEL1 + " ts_var = '" +  ts  + "'" ;  // ex data_agg
							sqlDEL1=sqlDEL1 + ", ute_var = '" +  bilancio.getUtente()  + "'" ;  // ute_var
							sqlDEL1=sqlDEL1 + ", fl_canc = 'S'" ;  // fl_canc
							sqlDEL1=this.struttura(sqlDEL1);
							sqlDEL1=sqlDEL1 + " fl_canc<>'S'";
							sqlDEL1=this.struttura(sqlDEL1);
							sqlDEL1=sqlDEL1 + " id_capitoli_bilanci=" + bilancio.getIDBil();
							sqlDEL1=this.struttura(sqlDEL1);
							sqlDEL1=sqlDEL1 + " cod_mat='" + rs.getString("cod_mat") + "'";

							pstmtUDP = connection.prepareStatement(sqlDEL1);
							int intRetCANC = 0;
							intRetCANC = pstmtUDP.executeUpdate();
							pstmtUDP.close();
							if (intRetCANC==1){
								valRitornoCANC=true;
							}


						}
					} // fine  	(!valRitornoUPDLOOP)
				} // fine if (bilancio.getDettagliBilancio()!=null && bilancio.getDettagliBilancio().size() >0 )
			}	// fine while


			for (int j=0; righeOggetto!=null && j<righeOggetto.size(); j++)
				{
					if (!righeOggetto.get(j).getDescrizione().equals("U"))
					{
						// inserimento del nuovo impegno
						// N.B. SE RIGA PRECEDENTMENTE CANCELLATA LOGICAMENTE VA PRIMA SFLEGGATA E MODIFICATA
						BilancioDettVO 	oggettoDettVO=dettagliBilancio.get(Integer.valueOf(righeOggetto.get(j).getCodice()));

						// cancellazione fisica	di quelli precedentemente cancellati logicamente
						String sqlDEL2="delete from tbb_bilanci " ;
						sqlDEL2=this.struttura(sqlDEL2);
						sqlDEL2=sqlDEL2 + " fl_canc = 'S'" ;  // fl_canc
						sqlDEL2=this.struttura(sqlDEL2);
						sqlDEL2=sqlDEL2 + " id_capitoli_bilanci=" + bilancio.getIDBil();
						sqlDEL2=this.struttura(sqlDEL2);
						sqlDEL2=sqlDEL2 + " cod_mat='" + oggettoDettVO.getImpegno() + "'";
						pstmtUDP2 = connection.prepareStatement(sqlDEL2);
						int intRetCANC2 = 0;
						intRetCANC2 = pstmtUDP2.executeUpdate();
						pstmtUDP2.close();

						String sqlSub="insert into tbb_bilanci values ( " ;
						sqlSub=sqlSub + "'" + oggettoDettVO.getImpegno() + "'" ;  // cod_mat
						sqlSub=sqlSub + "," + bilancio.getIDBil() ;   // id_capitoli_bilanci
						sqlSub=sqlSub + ",null"  ;   // id_buono_ordine
						sqlSub=sqlSub + "," +  oggettoDettVO.getBudget() ;  // budget
//******************************************************************************************
// calcolare l'ordinato, il pagato e il fatturato con le opportune query sulle altre tabelle
//******************************************************************************************
						sqlSub=sqlSub + ",0 "; //+  oggettoDettVO.getImpegnato() ;  // ordinato
						sqlSub=sqlSub + ",0"; //+  oggettoDettVO.getFatturato();  // fatturato
						sqlSub=sqlSub + ",0"; //+  oggettoDettVO.getPagato() ;  // pagato
						sqlSub=sqlSub + ",'" + bilancio.getUtente() + "'" ;   // ute_ins
						sqlSub=sqlSub + ",'" + ts + "'" ;   // ts_ins
						sqlSub=sqlSub + ",'" + bilancio.getUtente() + "'" ;   // ute_var
						sqlSub=sqlSub + ",'" + ts + "'";   // ts_var
						sqlSub=sqlSub + ",'N'";   // fl_canc
						sqlSub=sqlSub + ",0"; //+  oggettoDettVO.getPagato() ;  // acquisito
						sqlSub=sqlSub + ")" ;
						pstmtUDP = connection.prepareStatement(sqlSub);
						int intRetINS = 0;
						intRetINS = pstmtUDP.executeUpdate();
						pstmtUDP.close();
						if (intRetINS!=1){
							valRitornoINS=true;
						}


					}
				}
			rs.close();
			pstmt.close();
			connection.close();

			// impostazione del codice di ritorno finale
			//boolean valRitornoUPDLOOP=false;
//***********
//da fare
//***********
			if (valRitornoUPD)
			{
				valRitorno=true;
				if (dettagliBilancio!=null && (numRighe < size))
				{
					if (!valRitornoINS )
					{
						valRitorno=true;
					}
				}
				if (valRitornoINS )
				{
					valRitorno=false;
				}

			}



		}catch (ValidationException e) {
	      	throw e;
		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

        return valRitorno;
	}

	public boolean  modificaProfilo(StrutturaProfiloVO profilo) throws DataException, ApplicationException , ValidationException
	{
		Validazione.ValidaStrutturaProfiloVO(profilo);
		boolean valRitorno=false;
    	int motivo=0;
    	//FornitoreVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtUDP = null;
		PreparedStatement pstmtDEL = null;
		PreparedStatement pstmtINS = null;
		PreparedStatement pstmtUDP2 = null;
		PreparedStatement pstmtExistOrd = null;
		ResultSet rsExistOrd= null;
		ResultSet rs = null;
		ResultSet rsSub = null;
		//ResultSet rsInsTit = null;

		boolean controlloCONGR=false;
		ResultSet rsESISTEORD = null;
		PreparedStatement pstmtESISTEORD = null;

		boolean valRitornoUPD=false;
		boolean valRitornoINSLOOP=false;
		boolean valRitornoCANC=false;
		boolean valRitornoINS=false;
		//boolean valRitornoInsTit=false;

		ResultSet rsESISTEFORN = null;
		PreparedStatement pstmtESISTEFORN  = null;
		PreparedStatement pstmtInsFornBibl= null;
		boolean valRitornoInsFornBibl=false;
		Timestamp ts=null;

		try {
			connection = getConnection();
			if (profilo.getListaFornitori()!=null && profilo.getListaFornitori().size() >0 )
			{

				for (int i=0; i<profilo.getListaFornitori().size(); i++)
				{
					StrutturaTerna 	oggettoDettVO=(StrutturaTerna)profilo.getListaFornitori().get(i);
					// controllo esistenza fornitore

					String sqlESISTEFORN="select forn.*, fornBibl.cod_fornitore as codFornBibl from tbr_fornitori forn " ;
					sqlESISTEFORN=sqlESISTEFORN + " left join tbr_fornitori_biblioteche fornBibl on fornBibl.cod_fornitore=forn.cod_fornitore and fornBibl.fl_canc<>'S'"   ;
					sqlESISTEFORN=sqlESISTEFORN + " and fornBibl.cd_polo='" + profilo.getCodPolo() +"'";
					sqlESISTEFORN=sqlESISTEFORN + " and fornBibl.cd_biblioteca='" + profilo.getCodBibl() +"'";
					sqlESISTEFORN=this.struttura(sqlESISTEFORN);
					sqlESISTEFORN=sqlESISTEFORN + " forn.cod_fornitore=" + oggettoDettVO.getCodice2();
					sqlESISTEFORN=this.struttura(sqlESISTEFORN);
					sqlESISTEFORN=sqlESISTEFORN + " forn.fl_canc<>'S'" ;
					pstmtESISTEFORN = connection.prepareStatement(sqlESISTEFORN);
					rsESISTEFORN = pstmtESISTEFORN.executeQuery(); // va in errore se non può restituire un recordset

					if (!rsESISTEFORN.next()) {
						controlloCONGR=false;
						throw new ValidationException("ordineIncongruenzaFornitoreInesistente",
								ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);
					}
					else
					{
						controlloCONGR=true;
						// localizzazione del fornitore in caso di utilizzo nella creazione ordine
						if (rsESISTEFORN.getString("codFornBibl")==null ||  (rsESISTEFORN.getString("codFornBibl")!=null && rsESISTEFORN.getString("codFornBibl").trim().length()==0)  )
						{
							// esiste il fornitore in anagrafica e non esiste fra quelli di biblioteca
							String sqlFB="insert into tbr_fornitori_biblioteche values ( " ;
							//TimeStamp
							ts = DaoManager.now();
							sqlFB= sqlFB + "'" +  profilo.getCodPolo() + "'" ;  // cd_bib
							sqlFB= sqlFB + ",'" +  profilo.getCodBibl() + "'" ;  // cd_biblioteca
							sqlFB= sqlFB + "," +  oggettoDettVO.getCodice2()  ;  // cod_fornitore
							sqlFB= sqlFB  + ",''";  // tipo_pagamento
							sqlFB= sqlFB  + ",''";  // cod_cliente
							sqlFB= sqlFB  + ",''";  // nom_contatto
							sqlFB= sqlFB  + ",''";  // tel_contatto
							sqlFB= sqlFB  + ",''";  // fax_contatto
							sqlFB= sqlFB  + ",'EUR'" ;  // valuta
							sqlFB= sqlFB  + ",'" +  profilo.getCodPolo() + "'" ;  // cod_polo
							sqlFB= sqlFB  + ",' '" ;  // allinea
							sqlFB= sqlFB + ",'" + profilo.getUtente() + "'" ;   // ute_ins
							sqlFB= sqlFB + ",'" + ts + "'" ;   // ts_ins
							sqlFB= sqlFB + ",'" + profilo.getUtente() + "'" ;   // ute_var
							sqlFB= sqlFB + ",'" + ts + "'";   // ts_var
							sqlFB= sqlFB + ",'N'";   // fl_canc
							sqlFB= sqlFB  + ")" ;  // cod_polo_bib
							pstmtInsFornBibl = connection.prepareStatement(sqlFB);
							int intRetFB = 0;
							intRetFB = pstmtInsFornBibl.executeUpdate();
							pstmtInsFornBibl.close();
							// fine estrazione codice
							if (intRetFB==1){
								controlloCONGR=true;
								//messaggio di localizzaziojne del fornitore
							} else {
								controlloCONGR=false;
								throw new ValidationException("ordineIncongruenzaFornitoreInesistente",
										ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);
							}
						}
					}


					rsESISTEFORN.close();
					pstmtESISTEFORN.close();

				}
			}
			if (controlloCONGR)
			{
				// CONTROLLI PREVENTIVI
				String sql0="select * ";  //,  prof.* ";
				sql0=sql0 + " from  tba_profili_acquisto prof ";
				sql0=sql0  +" join tra_sez_acquisizione_fornitori sezAcqForn on prof.cod_prac=sezAcqForn.cod_prac and sezAcqForn.fl_canc<>'S' and sezAcqForn.cd_polo='" +  profilo.getCodPolo() + "' and sezAcqForn.cd_biblioteca='" +  profilo.getCodBibl() + "'";

				sql0=this.struttura(sql0);
				sql0=sql0 + " prof.fl_canc<>'S'";
				// attenzione è possibile che sia stata modificata la sezione
				if (profilo.getProfilo()!=null && profilo.getProfilo().getCodice().trim().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0  + " prof.cod_prac=" + profilo.getProfilo().getCodice() ;
				}
				pstmt = connection.prepareStatement(sql0);
				rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
				// numero di righe del resultset
				int numRighe=0;
				List<StrutturaCombo> righeOggetto=new ArrayList();
				//Timestamp
				ts = DaoManager.now();
				while (rs.next()) {
					numRighe=numRighe+1;
					if (numRighe==1)
					{
						String sqlUDP="update tba_profili_acquisto set " ;
						sqlUDP= sqlUDP + " ute_var = '" +  profilo.getUtente()  + "'" ;  // ute_var
						sqlUDP= sqlUDP + ", ts_var = '" +  ts  + "'" ;  // ts_var
						sqlUDP= sqlUDP + ", descr ='" + profilo.getProfilo().getDescrizione().trim().toUpperCase() + "'"   ;  // descr
						sqlUDP= sqlUDP + ", id_sez_acquis_bibliografiche ="+ profilo.getIDSez(); // id_sez_acquis_bibliografiche
						sqlUDP= sqlUDP + ", paese ='"+ profilo.getPaese().getCodice()+ "'"   ; // paese
						sqlUDP= sqlUDP + ", lingua ='"+profilo.getLingua().getCodice() + "'" ; // lingua

						sqlUDP=this.struttura(sqlUDP);
						sqlUDP=sqlUDP + " fl_canc<>'S'";
						// se cambia la sezione non effettua la modifica
						//sqlUDP=this.struttura(sqlUDP);
						//sqlUDP=sqlUDP + "id_sez_acquis_bibliografiche=" + profilo.getIDSez() ;
						if (profilo.getProfilo()!=null && profilo.getProfilo().getCodice().trim().length()!=0)
						{
							sqlUDP=this.struttura(sqlUDP);
							sqlUDP=sqlUDP   + " cod_prac=" + profilo.getProfilo().getCodice() ;
						}

						if (profilo.getDataUpd()!=null )
						{
							sqlUDP=this.struttura(sqlUDP);
							sqlUDP=sqlUDP + " ts_var='" + profilo.getDataUpd() + "'" ;
						}

						pstmtUDP = connection.prepareStatement(sqlUDP);
						log.debug("Debug: modifica profilo");
						log.debug("Debug: " + sqlUDP);

						int intRetUDP = 0;
						intRetUDP = pstmtUDP.executeUpdate();
						pstmtUDP.close();
						// fine estrazione codice
						if (intRetUDP==1){
							valRitornoUPD=true;
							}
						else
						{
							throw new ValidationException("operazioneInConcorrenza",
									ValidationExceptionCodici.operazioneInConcorrenza);

						}

					}
					if (valRitornoUPD)
					{
						if (profilo.getListaFornitori()!=null && profilo.getListaFornitori().size() >0 )
						{

							// cancellazione logica
							String sqlDEL1="update tra_sez_acquisizione_fornitori set " ;

							sqlDEL1=sqlDEL1 + " ts_var = '" +  ts  + "'" ;  // ex data_agg
							sqlDEL1=sqlDEL1 + ", ute_var = '" +  profilo.getUtente()  + "'" ;  // ute_var
							sqlDEL1=sqlDEL1 + ", fl_canc = 'S'" ;  // fl_canc
							sqlDEL1=this.struttura(sqlDEL1);
							sqlDEL1=sqlDEL1 + " fl_canc<>'S'";

							if (profilo.getCodPolo() !=null &&  profilo.getCodPolo().length()!=0)
							{
								sqlDEL1=this.struttura(sqlDEL1);
								sqlDEL1=sqlDEL1   + " cd_polo='" + profilo.getCodPolo() +"'";
							}

							if (profilo.getCodBibl() !=null &&  profilo.getCodBibl().length()!=0)
							{
								sqlDEL1=this.struttura(sqlDEL1);
								sqlDEL1=sqlDEL1   + " cd_biblioteca='" + profilo.getCodBibl() +"'";
							}
							if (profilo.getProfilo()!=null && profilo.getProfilo().getCodice().trim().length()!=0)
							{
								sqlDEL1=this.struttura(sqlDEL1);
								sqlDEL1=sqlDEL1   + " cod_prac=" + profilo.getProfilo().getCodice() ;
							}
							pstmtDEL = connection.prepareStatement(sqlDEL1);
							int intRetCANC = 0;
							intRetCANC = pstmtDEL.executeUpdate();
							pstmtDEL.close();
							if (intRetCANC>0){
								valRitornoCANC=true;
							}
							if (valRitornoCANC)
							{
								valRitornoINSLOOP=false;
								for (int i=0; i<profilo.getListaFornitori().size(); i++)
								{
									StrutturaTerna 	oggettoDettVO=(StrutturaTerna) profilo.getListaFornitori().get(i);

									String sqlEsisteOrd="select * from tra_sez_acquisizione_fornitori " ;

									sqlEsisteOrd=this.struttura(sqlEsisteOrd);
									sqlEsisteOrd=sqlEsisteOrd + " fl_canc = 'N'" ;  // fl_canc

									if (profilo.getCodPolo() !=null &&  profilo.getCodPolo().length()!=0)
									{
										sqlEsisteOrd=this.struttura(sqlEsisteOrd);
										sqlEsisteOrd=sqlEsisteOrd  + " cd_polo='" + profilo.getCodPolo() +"'";
									}
									if (profilo.getCodBibl() !=null &&  profilo.getCodBibl().length()!=0)
									{
										sqlEsisteOrd=this.struttura(sqlEsisteOrd);
										sqlEsisteOrd=sqlEsisteOrd  + " cd_biblioteca='" + profilo.getCodBibl() +"'";
									}
									if (profilo.getProfilo()!=null && profilo.getProfilo().getCodice().trim().length()!=0)
									{
										sqlEsisteOrd=this.struttura(sqlEsisteOrd);
										sqlEsisteOrd=sqlEsisteOrd  + " cod_prac=" + profilo.getProfilo().getCodice() ;
									}

									if ( oggettoDettVO.getCodice2() !=null &&   oggettoDettVO.getCodice2().trim().length()!=0)
									{
										sqlEsisteOrd=this.struttura(sqlEsisteOrd);
										sqlEsisteOrd=sqlEsisteOrd+ " cod_fornitore=" + oggettoDettVO.getCodice2().trim() ;
									}
									pstmtExistOrd = connection.prepareStatement(sqlEsisteOrd);
									rsExistOrd = pstmtExistOrd.executeQuery(); // va in errore se non può restituire un recordset
									// numero di righe del resultset
									int numRigheEsisteOrd=0;
									while (rsExistOrd.next()) {
										numRigheEsisteOrd=numRigheEsisteOrd+1;
									}
									rsExistOrd.close() ;
									pstmtExistOrd.close() ;
									if (numRigheEsisteOrd>0)
									{
										throw new ValidationException("profilierroreFornitoreRipetuto",
												ValidationExceptionCodici.profilierroreFornitoreRipetuto);
									}
									else
									{
										// sfleggo e inserisco
										//sqlDEL2=this.struttura(sqlDEL2);
										//sqlDEL2=sqlDEL2 + " fl_canc = 'S'" ;  // fl_canc
										// cancellazione fisica	di quelli precedentemente cancellati logicamente
										String sqlDEL2="delete from tra_sez_acquisizione_fornitori ";

										sqlDEL2=this.struttura(sqlDEL2);
										sqlDEL2=sqlDEL2 + " fl_canc = 'S'" ;  // fl_canc
										if (profilo.getCodPolo() !=null &&  profilo.getCodPolo().length()!=0)
										{
											sqlDEL2=this.struttura(sqlDEL2);
											sqlDEL2=sqlDEL2   + " cd_polo='" + profilo.getCodPolo() +"'";
										}

										if (profilo.getCodBibl() !=null &&  profilo.getCodBibl().length()!=0)
										{
											sqlDEL2=this.struttura(sqlDEL2);
											sqlDEL2=sqlDEL2   + " cd_biblioteca='" + profilo.getCodBibl() +"'";
										}
										if (profilo.getProfilo()!=null && profilo.getProfilo().getCodice().trim().length()!=0)
										{
											sqlDEL2=this.struttura(sqlDEL2);
											sqlDEL2=sqlDEL2   + " cod_prac=" + profilo.getProfilo().getCodice() ;
										}
										if ( oggettoDettVO.getCodice2() !=null &&   oggettoDettVO.getCodice2().trim().length()!=0)
										{
											sqlDEL2=this.struttura(sqlDEL2);
											sqlDEL2=sqlDEL2 + " cod_fornitore=" + oggettoDettVO.getCodice2().trim() ;
										}

										pstmtUDP2 = connection.prepareStatement(sqlDEL2);
										int intRetCANC2 = 0;
										intRetCANC2 = pstmtUDP2.executeUpdate();
										pstmtUDP2.close();

										// inserimento

										String sqlSub2="insert into tra_sez_acquisizione_fornitori values ( " ;
										sqlSub2 = sqlSub2+  "'" + profilo.getCodPolo() + "'" ;  // cd_polo
										sqlSub2 = sqlSub2+  ",'" + profilo.getCodBibl() + "'" ;  // cd_bib
										sqlSub2 = sqlSub2+  "," +  profilo.getProfilo().getCodice() ;  // cod_prac
										sqlSub2 = sqlSub2+  "," +  oggettoDettVO.getCodice2()  ;  // cod_fornitore
										sqlSub2 = sqlSub2 + ",'" + profilo.getUtente() + "'" ;   // ute_ins
										sqlSub2 = sqlSub2 + ",'" + ts + "'" ;   // ts_ins
										sqlSub2 = sqlSub2 + ",'" + profilo.getUtente() + "'" ;   // ute_var
										sqlSub2 = sqlSub2 + ",'" + ts + "'";   // ts_var
										sqlSub2 = sqlSub2 + ",'N'";   // fl_canc
										sqlSub2 = sqlSub2+ ")" ;
										pstmtINS = connection.prepareStatement(sqlSub2);
										int intRetINSLOOP = 0;
										intRetINSLOOP = pstmtINS.executeUpdate();
										pstmtINS.close();
										if (intRetINSLOOP!=1){
											valRitornoINSLOOP=true;
										}
									}

								}
							}
						}
					}
				else
				{
					motivo=2; // record forse già esistente quindi non inseribile
					throw new ValidationException("fornitoreInserimentoErroreBaseDati",
							ValidationExceptionCodici.fornitoreInserimentoErroreBaseDati);

				}

			}
		}
		rs.close();
		pstmt.close();
		connection.close();
		if (valRitornoUPD)
		{
			valRitorno=true;
			if (profilo.getListaFornitori()!=null && profilo.getListaFornitori().size() >0)
			{
				if (!valRitornoINSLOOP && valRitornoCANC )
				{
					valRitorno=true;
				}
				else
				{
					valRitorno=false;
				}
			}
		}
	}catch (ValidationException e) {
      	  throw e;
	} catch (Exception e) {
		log.error("", e);
	} finally {
		close(connection);
	}

	return valRitorno;
}


	public boolean  modificaFattura(FatturaVO fattura) throws DataException, ApplicationException , ValidationException
	{
		Validazione.ValidaFatturaVO (fattura);
		boolean ok = false;
    	int motivo=0;
    	//FornitoreVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtUDP = null;
		PreparedStatement pstmtUDP2 = null;

		PreparedStatement pstmtDEL = null;
		PreparedStatement pstmtINS = null;
		PreparedStatement pstmtExistOrd = null;
		ResultSet rsExistOrd= null;
		ResultSet rs = null;
		ResultSet rsSub = null;
		//ResultSet rsInsTit = null;

		boolean controlloCONGR=false;
		ResultSet rsESISTEORD = null;
		PreparedStatement pstmtESISTEORD = null;

		boolean valRitornoUPD=false;
		boolean valRitornoINSLOOP=false;
		boolean valRitornoCANC=false;
		boolean valRitornoINS=false;
		//boolean valRitornoInsTit=false;

		PreparedStatement pstmtCalcola = null;
		boolean valRitornoCalcola=false;


		PreparedStatement pstmtFatt = null;
		ResultSet rsFatt = null;

		try {
			connection = getConnection();
			if (fattura.getFornitoreFattura().getCodice()!=null && fattura.getFornitoreFattura().getCodice()!=null  && fattura.getFornitoreFattura().getCodice().trim().length()!=0)
			{
				// subquery per test di esistenza fornitore : da sostituire con metodo ancora da scrivere in this
				String sqlSub="select * from tbr_fornitori forn  ";
				sqlSub=sqlSub + " join tbr_fornitori_biblioteche fornBibl on fornBibl.cod_fornitore=forn.cod_fornitore  and fornBibl.cd_biblioteca='" + fattura.getCodBibl()+"' and fornBibl.cd_polo='" + fattura.getCodPolo()+"' and fornBibl.fl_canc<>'S'" ;
				sqlSub=sqlSub + " where forn.fl_canc<>'S'";
				sqlSub=sqlSub + " and forn.cod_fornitore=" + fattura.getFornitoreFattura().getCodice().trim();

				pstmt = connection.prepareStatement(sqlSub);
				rsSub = pstmt.executeQuery();
				if (!rsSub.next()) {
					motivo=6;
					throw new ValidationException("ordineIncongruenzaFornitoreInesistente",
							ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);
				}

				rsSub.close();
			}

			List<StrutturaFatturaVO> righeDettaglioFattura = fattura.getRigheDettaglioFattura();
			if (ValidazioneDati.isFilled(righeDettaglioFattura) ) {

				for (int i=0; i<righeDettaglioFattura.size(); i++)
				{
					StrutturaFatturaVO 	rigaFatt = righeDettaglioFattura.get(i);
					rigaFatt.setIDBil(0);
					if (fattura.isGestBil() && ((rigaFatt.getBilancio().getCodice3()!=null && rigaFatt.getBilancio().getCodice3().trim().equals("4")) || rigaFatt.getOrdine().getCodice1().equals("A") || rigaFatt.getOrdine().getCodice1().equals("V") || rigaFatt.getOrdine().getCodice1().equals("R")))
					{
						// subquery per test di esistenza bilancio : da sostituire con metodo ancora da scrivere in this
						String sqlSub="select * from tbb_capitoli_bilanci capBil  ";
						sqlSub=sqlSub + " join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and bil.fl_canc<>'S'";
						sqlSub=sqlSub + " where capBil.fl_canc<>'S'";
						sqlSub=sqlSub + " and capBil.cd_polo='" +fattura.getCodPolo()+"'";
						sqlSub=sqlSub + " and capBil.cd_bib='" +fattura.getCodBibl()+"'";
						if (rigaFatt.getBilancio().getCodice1()!=null && rigaFatt.getBilancio().getCodice1().length()!=0)
						{
							sqlSub=sqlSub + " and capBil.esercizio=" +rigaFatt.getBilancio().getCodice1();
						}
						if (rigaFatt.getBilancio().getCodice2()!=null && rigaFatt.getBilancio().getCodice2().length()!=0)
						{
							sqlSub=sqlSub + " and capBil.capitolo=" +rigaFatt.getBilancio().getCodice2();
						}
						if (rigaFatt.getBilancio().getCodice3()!=null && rigaFatt.getBilancio().getCodice3().length()!=0)
						{
							sqlSub=sqlSub + " and bil.cod_mat='" +rigaFatt.getBilancio().getCodice3()+ "'";
						}
						pstmt = connection.prepareStatement(sqlSub);
						rsSub = pstmt.executeQuery();
						if (!rsSub.next()) {
							motivo=5;
							throw new ValidationException("ordineIncongruenzaBilancioInesistente",
									ValidationExceptionCodici.ordineIncongruenzaBilancioInesistente);
						}
						else
						{
							rigaFatt.setIDBil(rsSub.getInt("id_capitoli_bilanci"));
						}
						rsSub.close();


					}


					rigaFatt.setIDOrd(0);

					if (rigaFatt.getOrdine()!=null && rigaFatt.getOrdine().getCodice1()!=null && rigaFatt.getOrdine().getCodice1().trim().length()!=0 && rigaFatt.getOrdine().getCodice2()!=null && rigaFatt.getOrdine().getCodice2().trim().length()!=0 && rigaFatt.getOrdine().getCodice3()!=null && rigaFatt.getOrdine().getCodice3().trim().length()!=0)
					{
						// controllo esistenza ordine (solo per righe fatture diverse da altre spese)
						// controllo esistenza ordine
						String sqlESISTEORD="select * from tba_ordini ord " ;
						sqlESISTEORD=this.struttura(sqlESISTEORD);
						sqlESISTEORD=sqlESISTEORD + " ord.fl_canc<>'S'";
						sqlESISTEORD=this.struttura(sqlESISTEORD);
						sqlESISTEORD=sqlESISTEORD + " ord.cd_bib='" + fattura.getCodBibl() +"'";
						sqlESISTEORD=this.struttura(sqlESISTEORD);
						sqlESISTEORD=sqlESISTEORD + " ord.cod_tip_ord='" + rigaFatt.getOrdine().getCodice1() +"'";
						sqlESISTEORD=this.struttura(sqlESISTEORD);
						sqlESISTEORD=sqlESISTEORD + " ord.anno_ord=" + rigaFatt.getOrdine().getCodice2() ;
						sqlESISTEORD=this.struttura(sqlESISTEORD);
						sqlESISTEORD=sqlESISTEORD + " ord.cod_ord=" +  rigaFatt.getOrdine().getCodice3() ;

						pstmtESISTEORD = connection.prepareStatement(sqlESISTEORD);
						rsESISTEORD = pstmtESISTEORD.executeQuery(); // va in errore se non può restituire un recordset
						int numRigheOrd=0;
						while (rsESISTEORD.next()) {
							numRigheOrd=numRigheOrd+1;
							rigaFatt.setIDOrd(rsESISTEORD.getInt("id_ordine"));

							// deve esserci un unico risultato
							// controlli congruenza
							// controllo che il bilancio e il fornitore dell'ordine siano quelli della fattura
							controlloCONGR=false;
							if (fattura.isGestBil() && rigaFatt.getIDBil()>0)
							{
								if (rsESISTEORD.getInt("id_capitoli_bilanci")!=rigaFatt.getIDBil())
								{
									throw new ValidationException("fatturaerroreCampoBilancioIncongruente",
									ValidationExceptionCodici.fatturaerroreCampoBilancioIncongruente);
								}

							}

							if (!rsESISTEORD.getString("cod_fornitore").equals(fattura.getFornitoreFattura().getCodice())  )
							{
								throw new ValidationException("fatturaerroreCampoFornitoreIncongruente",
										ValidationExceptionCodici.fatturaerroreCampoFornitoreIncongruente);
							}

						}
						rsESISTEORD.close();
						pstmtESISTEORD.close();
						if (numRigheOrd==1)
						{
							controlloCONGR=true;
						}
						else
						{
							controlloCONGR=false;
							// procedere con il delete del buono sopra creato
							throw new ValidationException("ordineNONtrovato",
									ValidationExceptionCodici.ordineNONtrovato);

						}

					}
					// controllo esistenza fattura per le note di credito con coincidenza di fornitore
					if (fattura.getTipoFattura().equals("N")  && rigaFatt.getIDFattNC()!=null && rigaFatt.getIDFattNC()>0)
					{
						String sqlfatt="select fatt.* ";
						sqlfatt=sqlfatt + " from  tba_fatture fatt ";
						sqlfatt=this.struttura(sqlfatt);
						sqlfatt=sqlfatt + " fatt.tipo_fattura ='F'"; // le note di credito possono essere associate solo a fatture
						sqlfatt=this.struttura(sqlfatt);
						sqlfatt=sqlfatt + " fatt.fl_canc<>'S' ";
						sqlfatt=this.struttura(sqlfatt);
						sqlfatt=sqlfatt + " fatt.id_fattura =" + rigaFatt.getIDFattNC();
						pstmtFatt = connection.prepareStatement(sqlfatt);
						rsFatt = pstmtFatt.executeQuery();

						if (!rsFatt.next()) {
							throw new ValidationException("fatturaNONtrovata",
									ValidationExceptionCodici.fatturaNONtrovata);
						}
						else
						{

							if (!rsFatt.getString("cod_fornitore").equals(fattura.getFornitoreFattura().getCodice())  )
							{
								throw new ValidationException("fatturaerroreCampoFornitoreIncongruenteNC",
										ValidationExceptionCodici.fatturaerroreCampoFornitoreIncongruenteNC);
							}
						}
						rsFatt.close();
						pstmtFatt.close();
					}
				} // fine for
			}	// fine if
			if (controlloCONGR)
			{
				// CONTROLLI PREVENTIVI
				String sql0="select *  ";
				sql0=sql0 + " from tba_fatture fatt ";
				sql0=sql0 + " join tba_righe_fatture fattDett on fattDett.id_fattura=fatt.id_fattura AND fattDett.fl_canc<>'S' ";

				sql0=this.struttura(sql0);
				sql0=sql0 + " fatt.fl_canc<>'S'";

				if (fattura.getCodPolo() !=null &&  fattura.getCodPolo().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0 + " fatt.cd_polo='" + fattura.getCodPolo() +"'";
				}

				if (fattura.getCodBibl() !=null &&  fattura.getCodBibl().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0 + " fatt.cd_bib='" + fattura.getCodBibl() +"'";
				}

				if (fattura.getAnnoFattura()!=null && fattura.getAnnoFattura().trim().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0 + " fatt.anno_fattura=" +fattura.getAnnoFattura() ;
				}

				if (fattura.getProgrFattura()!=null && fattura.getProgrFattura().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0  + " fatt.progr_fattura=" + fattura.getProgrFattura() ;
				}

				pstmt = connection.prepareStatement(sql0);
				rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
				// numero di righe del resultset
				int numRighe=0;
				List<StrutturaCombo> righeOggetto=new ArrayList();
				Timestamp ts = DaoManager.now();
				Boolean cambioStatoFattura=false;
				while (rs.next()) {
					numRighe=numRighe+1;
					if (numRighe==1)
					{

						if (!rs.getString("stato_fattura").equals(fattura.getStatoFattura()))
						{
							cambioStatoFattura=true;
						}

						// Impossibile la modifica di una fattura in stato di ordine emesso o contabilizzato
						if 	(rs.getString("stato_fattura").equals("3") || rs.getString("stato_fattura").equals("4"))
						{
							throw new ValidationException("fatturaPagataContabilizzata",
									ValidationExceptionCodici.fatturaPagataContabilizzata);
						}

						// Quando si modifica una fattura (già controllata) e non si sta pagando (o si rieffettua un controlla) occorre far regredire lo stato a registrata
						if 	(rs.getString("stato_fattura").equals("2") && !fattura.getStatoFattura().equals("3") && !fattura.getStatoFattura().equals("4") )
						{
							if 	(fattura.getStatoFattura().equals("2"))
							{
								fattura.setStatoFattura("1");
							}
						}
						if 	(rs.getString("stato_fattura").equals("1") && fattura.getStatoFattura().equals("3"))
						{
							fattura.setStatoFattura("1");
							throw new ValidationException("fatturaPagataDaControllare",
									ValidationExceptionCodici.fatturaPagataDaControllare);
						}
						if 	(rs.getString("stato_fattura").equals("1") && fattura.getStatoFattura().equals("4"))
						{
							fattura.setStatoFattura("1");
							throw new ValidationException("fatturaContabilizzataDaControllare",
									ValidationExceptionCodici.fatturaContabilizzataDaControllare);
						}

						String sqlUDP="update tba_fatture set " ;
						//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
						//sqlUDP= sqlUDP + " data_agg = '" +  ts  + "'" ;  // data_agg
						sqlUDP= sqlUDP  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
						sqlUDP= sqlUDP + ", ute_var = '" +  fattura.getUtente()  + "'" ;  // ute_var
						sqlUDP= sqlUDP + ", anno_fattura =" + fattura.getAnnoFattura()  ;  // anno_fattura
						//sqlUDP= sqlUDP + ", progr_fattura =" + fattura.getProgrFattura()  ;  // progr_fattura
						sqlUDP= sqlUDP + ", num_fattura ='" + fattura.getNumFattura()+ "'"  ;  // num_fattura
						sqlUDP= sqlUDP + ", data_fattura =TO_DATE('"+ fattura.getDataFattura().trim()+ "','dd/MM/yyyy') " ; // data_fattura
						sqlUDP= sqlUDP + ", data_reg =TO_DATE('"+ fattura.getDataRegFattura().trim()+ "','dd/MM/yyyy') " ; // data_reg
						sqlUDP= sqlUDP + ", importo ="+ fattura.getImportoFattura() ; // importo
						sqlUDP= sqlUDP + ", sconto ="+ fattura.getScontoFattura() ; // sconto
						sqlUDP= sqlUDP + ", valuta ='"+ fattura.getValutaFattura() + "'" ; // valuta
						sqlUDP= sqlUDP + ", tipo_fattura ='"+ fattura.getTipoFattura() + "'"; // tipo_fattura
						sqlUDP= sqlUDP + ", cod_fornitore ="+ fattura.getFornitoreFattura().getCodice() ; // cod_fornitore
						sqlUDP= sqlUDP + ", stato_fattura ='"+ fattura.getStatoFattura()+ "'" ; // stato_fattura

						/*if (buonoOrd.getNumBuonoOrdine()!=null && buonoOrd.getNumBuonoOrdine().length()!=0)
						{
							sqlUDP=this.struttura(sqlUDP);
							sqlUDP=sqlUDP + " buono_ord = TO_NUMBER('" +  buonoOrd.getNumBuonoOrdine() + "','999999999')";
						}*/
						sqlUDP=this.struttura(sqlUDP);
						sqlUDP=sqlUDP + " fl_canc<>'S'";

						if (fattura.getCodPolo() !=null &&  fattura.getCodPolo().length()!=0)
						{
							sqlUDP=this.struttura(sqlUDP);
							sqlUDP=sqlUDP + " cd_polo='" + fattura.getCodPolo() +"'";
						}

						if (fattura.getCodBibl() !=null &&  fattura.getCodBibl().length()!=0)
						{
							sqlUDP=this.struttura(sqlUDP);
							sqlUDP=sqlUDP + " cd_bib='" + fattura.getCodBibl() +"'";
						}

						if (fattura.getAnnoFattura()!=null && fattura.getAnnoFattura().trim().length()!=0)
						{
							sqlUDP=this.struttura(sqlUDP);
							sqlUDP=sqlUDP + " anno_fattura=" +fattura.getAnnoFattura() ;
						}

						if (fattura.getProgrFattura()!=null && fattura.getProgrFattura().trim().length()!=0)
						{
							sqlUDP=this.struttura(sqlUDP);
							sqlUDP=sqlUDP  + " progr_fattura=" + fattura.getProgrFattura().trim() ;
						}

						if (fattura.getDataUpd()!=null )
						{
							sqlUDP=this.struttura(sqlUDP);
							sqlUDP=sqlUDP + " ts_var='" + fattura.getDataUpd() + "'" ;
						}

						pstmtUDP = connection.prepareStatement(sqlUDP);
						log.debug("Debug: modifica  fattura");
						log.debug("Debug: " + sqlUDP);

						int intRetUDP = 0;
						intRetUDP = pstmtUDP.executeUpdate();
						pstmtUDP.close();
						// fine estrazione codice
						if (intRetUDP==1){
							valRitornoUPD=true;
							}
						else
						{
							throw new ValidationException("operazioneInConcorrenza",
									ValidationExceptionCodici.operazioneInConcorrenza);

						}

					} //if (numRighe==1)
				} // FINE WHILE
				if (valRitornoUPD)
					{
						if (ValidazioneDati.isFilled(righeDettaglioFattura)  )	{
							// cancello i preesistenti ordini associati ed associo i nuovi
							//String sqlDEL1="delete from tba_righe_fatture ";
							// cancellazione logica
							String sqlDEL1="update tba_righe_fatture set " ;
							//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
							sqlDEL1=sqlDEL1 + " ts_var = '" +  ts  + "'" ;  // ex data_agg
							sqlDEL1=sqlDEL1 + ", ute_var = '" +  fattura.getUtente()  + "'" ;  // ute_var
							sqlDEL1=sqlDEL1 + ", fl_canc = 'S'" ;  // fl_canc
							sqlDEL1=this.struttura(sqlDEL1);
							sqlDEL1=sqlDEL1 + " fl_canc<>'S'";
							if (fattura.getCodPolo() !=null &&  fattura.getCodPolo().length()!=0)
							{
								sqlDEL1=this.struttura(sqlDEL1);
								sqlDEL1=sqlDEL1 + " cd_polo='" + fattura.getCodPolo() +"'";
							}

							if (fattura.getCodBibl() !=null &&  fattura.getCodBibl().length()!=0)
							{
								sqlDEL1=this.struttura(sqlDEL1);
								sqlDEL1=sqlDEL1 + " cd_biblioteca='" + fattura.getCodBibl() +"'";
							}

							sqlDEL1=this.struttura(sqlDEL1);
							sqlDEL1=sqlDEL1 + " id_fattura=" +fattura.getIDFatt() ;

							pstmtDEL = connection.prepareStatement(sqlDEL1);
							int intRetCANC = 0;
							intRetCANC = pstmtDEL.executeUpdate();
							pstmtDEL.close();
							if (intRetCANC > 0){
								valRitornoCANC=true;
							}
							// ho dovuto chiudere e riaprire la connection per consentire la cancellazione logica
							connection.close();
							connection = getConnection();

							if (valRitornoCANC)
							{
								// controllo di righe ripetute - procedura funzionante messa in stand by
								// perchè in fattura l'ordine può essere ripetuto ma con inventari diversi
								valRitornoINSLOOP=false;
								if 	(!valRitornoINSLOOP)
								{
									for (int i=0; i<righeDettaglioFattura.size(); i++)
									{
										StrutturaFatturaVO 	rigaFatt=righeDettaglioFattura.get(i);

										// cancellazione fisica	di quelli precedentemente cancellati logicamente
										String sqlDEL2="delete from tba_righe_fatture ";

										sqlDEL2=this.struttura(sqlDEL2);
										sqlDEL2=sqlDEL2 + " fl_canc = 'S'" ;  // fl_canc
										if (fattura.getCodPolo() !=null &&  fattura.getCodPolo().length()!=0)
										{
											sqlDEL2=this.struttura(sqlDEL2);
											sqlDEL2=sqlDEL2 + " cd_polo='" + fattura.getCodPolo() +"'";
										}

										if (fattura.getCodBibl() !=null &&  fattura.getCodBibl().length()!=0)
										{
											sqlDEL2=this.struttura(sqlDEL2);
											sqlDEL2=sqlDEL2 + " cd_biblioteca='" + fattura.getCodBibl() +"'";
										}

										sqlDEL2=this.struttura(sqlDEL2);
										sqlDEL2=sqlDEL2 + " id_fattura=" +fattura.getIDFatt() ;

										pstmtUDP2 = connection.prepareStatement(sqlDEL2);
										int intRetCANC2 = 0;
										intRetCANC2 = pstmtUDP2.executeUpdate();
										pstmtUDP2.close();

										String sqlSub2="insert into tba_righe_fatture values ( " ;
										sqlSub2 = sqlSub2 + " " + fattura.getIDFatt() ;   // id_fattura
										sqlSub2 = sqlSub2+  ",'" + fattura.getCodPolo() + "'" ;  // cd_polo
										sqlSub2 = sqlSub2+  ",'" + fattura.getCodBibl() + "'" ;  // cd_biblioteca
										sqlSub2 = sqlSub2+ "," + rigaFatt.getRigaFattura()  ;  // riga_fattura

										if (rigaFatt.getIDOrd()>0)
										{
											sqlSub2 = sqlSub2+ ",'" +  rigaFatt.getIDOrd() + "'";  // id_ordine
										}
										else
										{
											sqlSub2 = sqlSub2+ ",null";  // id_ordine
										}

										if (fattura.isGestBil() && rigaFatt.getIDBil()>0)
										{
											sqlSub2 = sqlSub2+ ",'" +  rigaFatt.getIDBil() + "'";  // id_capitoli_bilanci
										}
										else
										{
											sqlSub2 = sqlSub2+ ",null";  // id_capitoli_bilanci
										}
										if (fattura.isGestBil() && rigaFatt.getBilancio()!=null && rigaFatt.getBilancio().getCodice3()!=null && rigaFatt.getBilancio().getCodice3().trim().length()==1)
										{
											sqlSub2 = sqlSub2+ ",'" +  rigaFatt.getBilancio().getCodice3()+ "'"  ;  // cod_mat
										}
										else
										{
											sqlSub2 = sqlSub2+ ",null";  // cod_mat
										}

										if (rigaFatt.getNoteRigaFattura()!=null && rigaFatt.getNoteRigaFattura().trim().length()>0)
										{
											sqlSub2 = sqlSub2+ ",'" +  rigaFatt.getNoteRigaFattura().replace("'","''")+ "'"  ;  // note
										}
										else
										{
											sqlSub2 = sqlSub2+ ",''"  ;  // note
										}
										//sqlSub2 = sqlSub2+ ",'" +  oggettoDettVO.getNoteRigaFattura().replace("'","''")+ "'"  ;  // note
										sqlSub2 = sqlSub2+ "," +  rigaFatt.getImportoRigaFattura() ;  // importo_riga
										sqlSub2 = sqlSub2+ "," +  rigaFatt.getSconto1RigaFattura();  // sconto_1
										sqlSub2 = sqlSub2+ "," +  rigaFatt.getSconto2RigaFattura() ;  // sconto_2
										sqlSub2 = sqlSub2+ ",'" +  rigaFatt.getCodIvaRigaFattura() + "'";  // cod_iva
										sqlSub2 = sqlSub2 + ",'" + fattura.getUtente() + "'" ;   // ute_ins
										sqlSub2 = sqlSub2 + ",'" + ts + "'" ;   // ts_ins
										sqlSub2 = sqlSub2 + ",'" + fattura.getUtente() + "'" ;   // ute_var
										sqlSub2 = sqlSub2 + ",'" + ts + "'";   // ts_var
										sqlSub2 = sqlSub2 + ",'N'";   // fl_canc
										// gestione note di credito
										if (fattura.getTipoFattura().equals("N") && rigaFatt.getIDFattNC()!=null && rigaFatt.getIDFattNC()>0)
										{
											String sqlfatt="select fatt.* ";
											sqlfatt=sqlfatt + " from  tba_fatture fatt ";
											sqlfatt=this.struttura(sqlfatt);
											sqlfatt=sqlfatt + " fatt.tipo_fattura ='F'"; // le note di credito possono essere associate solo a fatture
											sqlfatt=this.struttura(sqlfatt);
											sqlfatt=sqlfatt + " fatt.fl_canc<>'S' ";
											sqlfatt=this.struttura(sqlfatt);
											sqlfatt=sqlfatt + " fatt.id_fattura =" + rigaFatt.getIDFattNC();
											pstmtFatt = connection.prepareStatement(sqlfatt);
											rsFatt = pstmtFatt.executeQuery();
											while (rsFatt.next()) {
												// la fattura esiste e deve essere univoca
												sqlSub2 = sqlSub2 + "," +rigaFatt.getIDFattNC();   // fl_canc
												if (rigaFatt.getFattura()!=null && rigaFatt.getFattura().getCodice3()!=null && rigaFatt.getFattura().getCodice3().trim().length()>0 )
												{
													sqlSub2 = sqlSub2 + "," +Integer.valueOf(rigaFatt.getFattura().getCodice3().trim());   //numero riga fattura
												}
											}
											rsFatt.close();
											pstmtFatt.close();
										}
										//sqlSub2 = sqlSub2 + ",0" ; //  importo_tot_riga
										sqlSub2 = sqlSub2+ ")" ;
										pstmtINS = connection.prepareStatement(sqlSub2);
										int intRetINSLOOP = 0;
										intRetINSLOOP = pstmtINS.executeUpdate();
										pstmtINS.close();
										if (intRetINSLOOP!=1){
											valRitornoINSLOOP=true;
										}
									} // fine for

								}	//if 	(!valRitornoINSLOOP)
							} // if (valRitornoCANC)
						} // if (fattura.getRigheDettaglioFattura()!=null && fattura.getRigheDettaglioFattura().size() >0 )
					} //	if (valRitornoUPD)

				else
				{
					motivo=2; // record forse già esistente quindi non inseribile
					throw new ValidationException("fornitoreInserimentoErroreBaseDati",
							ValidationExceptionCodici.fornitoreInserimentoErroreBaseDati);

				}
			// } // fine while
			rs.close();
			pstmt.close();
			connection.close();
				// impostazione del codice di ritorno finale


				if (valRitornoUPD)
				{
					ok=true;
					if (ValidazioneDati.isFilled(righeDettaglioFattura) ) {
						if (!valRitornoINSLOOP && valRitornoCANC )
						{
							ok=true;
						}
						else
						{
							ok=false;
						}
					}
				}
				// 09.01.09 aggiornamento dati calcolati del bilancio al cambio di stato della fattura
				if (cambioStatoFattura && ok && fattura.isGestBil() )
				{
					if (ValidazioneDati.isFilled(righeDettaglioFattura)  ) {
						//almaviva5_20140610 #5575 cache bilanci
						Set<StrutturaTerna> bilanci = new HashSet<StrutturaTerna>();
				        Locale locale=Locale.getDefault(); // aggiornare con quella locale se necessario
				        for (int i=0; i<righeDettaglioFattura.size(); i++)
						{
							StrutturaFatturaVO 	rigaFatt=righeDettaglioFattura.get(i);
							// 09/01/09 ******AGGIORNAMENTO DATI CALCOLATI DI BILANCIO
							StrutturaTerna bilancio = rigaFatt.getBilancio();
							String impegno = bilancio.getCodice3();
							if (ValidazioneDati.isFilled(impegno) && !bilanci.contains(bilancio) )	{

								CalcoliVO calcoli = this.calcola(fattura.getCodPolo(),
										fattura.getCodBibl(), 0, null,
										rigaFatt.getIDBil(), impegno, 0, null,
										null, null, locale);

								double imp = 0.00;
								double fatt = 0.00;
								double pag = 0.00;
								double acq = 0.00;

								if (calcoli!=null )	{
									if (calcoli.getOrdinato()!=0)
									{
										imp=calcoli.getOrdinato();
									}
									if (calcoli.getFatturato()!=0)
									{
										fatt=calcoli.getFatturato();
									}
									if (calcoli.getPagato()!=0)
									{
										pag=calcoli.getPagato();
									}
									if (calcoli.getAcquisito()!=0)
									{
										acq=calcoli.getAcquisito();
									}
									connection = getConnection();
									String sqlCalcola="update tbb_bilanci set ";
									sqlCalcola= sqlCalcola + " ts_var = '" +  ts  + "'" ;  // data_agg
									sqlCalcola= sqlCalcola + ", ute_var = '" +  fattura.getUtente()  + "'" ;  // utente
									//sqlCalcola= sqlCalcola + ", budget = " + oggettoDettVO.getBudget()   ;  // budget
//									******************************************************************************************
//									 calcolare l'ordinato, il pagato e il fatturato con le opportune query sulle altre tabelle
//									******************************************************************************************
									sqlCalcola= sqlCalcola + ", ordinato = " + imp   ;  // ordinato
									sqlCalcola= sqlCalcola + ", fatturato = " + fatt   ;  // fatturato
									sqlCalcola= sqlCalcola + ", pagato = " + pag   ;  // pagato
									//TODO:CALCOLA da aggiungere quando esisterà il campo nuovo su tabella
									sqlCalcola= sqlCalcola + ", acquisito = " + acq   ;  // acquisito


									sqlCalcola=this.struttura(sqlCalcola);
									sqlCalcola= sqlCalcola + " fl_canc<>'S' ";

									sqlCalcola=this.struttura(sqlCalcola);
									sqlCalcola= sqlCalcola + " id_capitoli_bilanci=" + rigaFatt.getIDBil();

									sqlCalcola=this.struttura(sqlCalcola);
									sqlCalcola= sqlCalcola+ " cod_mat='" + impegno + "'" ;

									pstmtCalcola = connection.prepareStatement(sqlCalcola);
									int intRetCalcola = 0;
									intRetCalcola = pstmtCalcola.executeUpdate();
									pstmtCalcola.close();
									if (intRetCalcola ==1){
										valRitornoCalcola=true;
									}
									connection.close();
								}
								bilanci.add(bilancio);
							}

						}
					}
				}

			}
	//	}

	}catch (ValidationException e) {
		throw e;
	} catch (Exception e) {
		log.error("", e);
	} finally {
		close(connection);
	}

    return ok;
}

	public List<StrutturaProfiloVO> getRicercaListaProfili(ListaSuppProfiloVO ricercaProfili)	throws ResourceNotFoundException, ApplicationException, ValidationException
	{
		Validazione.ValidaListaSuppProfiloVO (ricercaProfili);

		List<StrutturaProfiloVO> listaProfili = new ArrayList<StrutturaProfiloVO>();

		int ret = 0;
        // execute the search here
			StrutturaProfiloVO rec = null;
    		Connection connection = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			ResultSet rsSub= null;
			PreparedStatement pstmtSub = null;

			try {
				// contiene i criteri di ricerca
				connection = getConnection();
				String sql="select *, prof.ts_var as dataUpd ";
				sql +=" from  tba_profili_acquisto prof ";
				//if (ricercaProfili.getSezione()!=null && ricercaProfili.getSezione().getCodice().trim().length()!=0)
				//{
					sql +=" join tba_sez_acquis_bibliografiche sez  on sez.id_sez_acquis_bibliografiche=prof.id_sez_acquis_bibliografiche and  sez.fl_canc<>'S' and sez.cd_polo='" +  ricercaProfili.getCodPolo() + "'  and sez.cd_bib='" +  ricercaProfili.getCodBibl() + "'" ;
				//}
				if (ricercaProfili.getFornitore()!=null  && ricercaProfili.getFornitore().getCodice()!=null && ricercaProfili.getFornitore().getCodice().trim().length()!=0)
				{
					sql +=" join tra_sez_acquisizione_fornitori sezAcqForn on prof.cod_prac=sezAcqForn.cod_prac and sezAcqForn.fl_canc<>'S' and sezAcqForn.cd_polo='" +  ricercaProfili.getCodPolo() + "' and sezAcqForn.cd_biblioteca='" +  ricercaProfili.getCodBibl() + "'";
				}

				sql=this.struttura(sql);
				sql +=" prof.fl_canc<>'S'";

				if (ricercaProfili.getSezione()!=null && ricercaProfili.getSezione().getCodice().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" upper(sez.cod_sezione)='" + ricercaProfili.getSezione().getCodice().trim().toUpperCase() +"'";
				}
				if (ricercaProfili.getProfilo()!=null && ricercaProfili.getProfilo().getCodice().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" prof.cod_prac=" + ricercaProfili.getProfilo().getCodice() ;
				}

				if (ricercaProfili.getProfilo()!=null && ricercaProfili.getProfilo().getDescrizione().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" upper(prof.descr) like '" + ricercaProfili.getProfilo().getDescrizione().trim().toUpperCase() +"%'";;
				}

				if (ricercaProfili.getFornitore()!=null  && ricercaProfili.getFornitore().getCodice()!=null && ricercaProfili.getFornitore().getCodice().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" sezAcqForn.cod_fornitore='" + ricercaProfili.getFornitore().getCodice() +"'";;
				}
				// ordinamento passato
				if (ricercaProfili.getOrdinamento()==null || (ricercaProfili.getOrdinamento()!=null && ricercaProfili.getOrdinamento().equals("")))
				{
					sql +=" order by prof.descr  "; //, prof.cod_prac
				}
				else if (ricercaProfili.getOrdinamento().equals("1"))
				{
					sql +=" order by prof.cod_prac  ";
				}
				else if (ricercaProfili.getOrdinamento().equals("2"))
				{
					sql +=" order by prof.descr ";
				}
				else if (ricercaProfili.getOrdinamento().equals("3"))
				{
					sql +=" order by sez.cod_sezione  ";
				}

				pstmt = connection.prepareStatement(sql);
				//log.debug("Debug: lettura profili");
				//log.debug("Debug: " + sql);

				rs = pstmt.executeQuery();
				int numRighe=0;
				while (rs.next()) {
					numRighe=numRighe+1;
					rec = new StrutturaProfiloVO();
					rec.setProgressivo(numRighe);
					rec.setCodPolo(ricercaProfili.getCodPolo());
					rec.setCodBibl(ricercaProfili.getCodBibl());
					rec.setIDSez(rs.getInt("id_sez_acquis_bibliografiche"));
					rec.setSezione(new StrutturaCombo("",""));
					rec.getSezione().setCodice(rs.getString("cod_sezione"));
					rec.setProfilo(new StrutturaCombo("",""));
					rec.getProfilo().setCodice(rs.getString("cod_prac"));
					rec.getProfilo().setDescrizione(rs.getString("descr").trim());
					rec.setPaese(new StrutturaCombo("",""));
					rec.getPaese().setCodice(rs.getString("paese"));
					rec.setLingua(new StrutturaCombo("",""));
					rec.getLingua().setCodice(rs.getString("lingua"));
					rec.setDataUpd(rs.getTimestamp("dataUpd"));

					//if rs.getString("lingua")!=null ciclo per riempire l'arraylist

					// ricerca dei fornitori (SOLO DI BIBLIOTECA) associati al profilo se esistenti
					String sqlSub="select sezAcqForn.cod_fornitore ,forn.nom_fornitore ";  //,  prof.* ";
					sqlSub=sqlSub + " from  tba_profili_acquisto prof ";
					sqlSub=sqlSub + " join tra_sez_acquisizione_fornitori sezAcqForn on prof.cod_prac=sezAcqForn.cod_prac and sezAcqForn.fl_canc<>'S' and sezAcqForn.cd_polo='" +  ricercaProfili.getCodPolo() + "' and sezAcqForn.cd_biblioteca='" +  ricercaProfili.getCodBibl() + "'";
					//sql +=" join tba_sez_acquis_bibliografiche sez  on sez.id_sez_acquis_bibliografiche=prof.id_sez_acquis_bibliografiche and  sez.fl_canc<>'S' and sez.cd_polo='" +  ricercaProfili.getCodPolo() + "'  and sez.cd_bib='" +  ricercaProfili.getCodBibl() + "'" ;
					sqlSub=sqlSub + " join tbr_fornitori forn on sezAcqForn.cod_fornitore=forn.cod_fornitore and forn.fl_canc<>'S'";
					sqlSub=sqlSub + " join tbr_fornitori_biblioteche fornBibl on fornBibl.cod_fornitore=forn.cod_fornitore  and fornBibl.cd_biblioteca='" + ricercaProfili.getCodBibl()+"' and fornBibl.cd_polo='" + ricercaProfili.getCodPolo()+"' and fornBibl.fl_canc<>'S'" ;

					sqlSub=this.struttura(sqlSub);
					sqlSub=sqlSub + " prof.fl_canc<>'S'";

					// prendo in considerazione la sezione specifica
					sqlSub=this.struttura(sqlSub);
					sqlSub=sqlSub  + " prof.id_sez_acquis_bibliografiche=" + rec.getIDSez() ;

					// ciclo i fornitori del profilo preso in considerazione
					if (rec.getProfilo()!=null  && rec.getProfilo().getCodice()!=null && rec.getProfilo().getCodice().trim().length()!=0)
					{
						sqlSub=this.struttura(sqlSub);
						sqlSub=sqlSub + " prof.cod_prac=" + rec.getProfilo().getCodice() ;
					}
					pstmtSub = connection.prepareStatement(sqlSub);
					rsSub = pstmtSub.executeQuery();
					List<StrutturaTerna> listForn=new ArrayList();
					int numRiga=0;
					while (rsSub.next()) {
						numRiga=numRiga+1;
						StrutturaTerna eleForn=new StrutturaTerna("","","");
						eleForn.setCodice1(String.valueOf(numRiga));
						eleForn.setCodice2(String.valueOf(rsSub.getInt("cod_fornitore")));
						eleForn.setCodice3(rsSub.getString("nom_fornitore"));
						listForn.add(eleForn);
					}
					rsSub.close();
					pstmtSub.close();
					rec.setListaFornitori(listForn);
					listaProfili.add(rec);
				} // End while

				rs.close();
				pstmt.close();
				connection.close();
			}	catch (ValidationException e) {
		    	  throw e;

		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

		Validazione.ValidaRicercaProfili (listaProfili);
        return listaProfili;
	}

	public List<SezioneVO> getRicercaListaSezioni(ListaSuppSezioneVO ricercaSezioni) throws ResourceNotFoundException, ApplicationException, ValidationException
	{
		Validazione.ValidaListaSuppSezioneVO (ricercaSezioni);

		List<SezioneVO> listaSezioni = new ArrayList<SezioneVO>();

		int ret = 0;
        // execute the search here
			SezioneVO rec = null;
    		Connection connection = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			PreparedStatement pstmt2 = null;
			ResultSet rs2 = null;

			try {
				// contiene i criteri di ricerca
				connection = getConnection();
				String sql="select *, sez.ts_var as dataUpd, TO_CHAR(sez.ts_var,'dd/MM/yyyy') as  data_var_str, TO_CHAR(sez.data_val,'dd/MM/yyyy') as  data_val_str ";
				sql +=" from  tba_sez_acquis_bibliografiche sez ";
				sql=this.struttura(sql);
				sql +=" sez.fl_canc<>'S'";

				// aggiungere per la gestione della data di validità (se si cerca per id o codice specifico tale controllo è da escludersi)
				if (ricercaSezioni.getIdSezione()==0 && (ricercaSezioni.getCodiceSezione()==null || (ricercaSezioni.getCodiceSezione()!=null && ricercaSezioni.getCodiceSezione().trim().length()==0)))
				{
					if(ricercaSezioni.isChiusura()) // si vogliono solo quelle chiuse
					{
						sql=this.struttura(sql);
						sql +="(sez.data_val is not null and  sez.data_val<=(SELECT CURRENT_DATE ))";
					}
					else
					{
						sql=this.struttura(sql);
						sql +="(sez.data_val is null or sez.data_val>(SELECT CURRENT_DATE ))";
					}
				}
				if (ricercaSezioni.getIdSezione()!=0)
				{
					sql=this.struttura(sql);
					sql +=" sez.id_sez_acquis_bibliografiche=" + ricercaSezioni.getIdSezione();
				}
				if (ricercaSezioni.getCodPolo() !=null &&  ricercaSezioni.getCodPolo().length()!=0)
				{
					sql=this.struttura(sql);
					sql=sql  + " sez.cd_polo='" + ricercaSezioni.getCodPolo() +"'";
				}


				if (ricercaSezioni.getCodBibl() !=null &&  ricercaSezioni.getCodBibl().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" sez.cd_bib='" + ricercaSezioni.getCodBibl() +"'";
				}

				if (ricercaSezioni.getCodiceSezione()!=null && ricercaSezioni.getCodiceSezione().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" upper(sez.cod_sezione)='" + ricercaSezioni.getCodiceSezione().trim().toUpperCase() +"'";
				}
				if (ricercaSezioni.getDescrizioneSezione()!=null && ricercaSezioni.getDescrizioneSezione().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" upper(sez.nome) like '" + ricercaSezioni.getDescrizioneSezione().trim().toUpperCase() +"%'";;
				}
				if (ricercaSezioni.getDescrizioneSezione()!=null && ricercaSezioni.getDescrizioneSezione().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" upper(sez.nome) like '" + ricercaSezioni.getDescrizioneSezione().trim().toUpperCase() +"%'";;
				}

				// ordinamento passato
				if (ricercaSezioni.getOrdinamento()==null || (ricercaSezioni.getOrdinamento()!=null && ricercaSezioni.getOrdinamento().equals("")))
				{
					sql +=" order by sez.cod_sezione  ";

				}
				else if (ricercaSezioni.getOrdinamento().equals("1"))
				{
					sql +=" order by sez.cod_sezione  ";
				}
				else if (ricercaSezioni.getOrdinamento().equals("2"))
				{
					sql +=" order by sez.nome  ";
				}

				pstmt = connection.prepareStatement(sql);
				//log.debug("Debug: lettura sezioni");
				//log.debug("Debug: " + sql);

				rs = pstmt.executeQuery();
				int numRighe=0;
				int idSez=0;
				while (rs.next()) {
					numRighe=numRighe+1;
					rec = new SezioneVO();
					rec.setIdSezione(rs.getInt("id_sez_acquis_bibliografiche"));
					rec.setProgressivo(numRighe);
					rec.setCodPolo(rs.getString("cd_polo"));
					rec.setCodBibl(rs.getString("cd_bib"));
					rec.setCodiceSezione(rs.getString("cod_sezione").trim());
					rec.setDescrizioneSezione(rs.getString("nome").trim());
					//rec.setBudgetDiCapitolo(Double.valueOf(rs.getString("budgetCap")));
					rec.setNoteSezione(rs.getString("note"));
					rec.setAnnoValiditaSezione(String.valueOf(rs.getInt("anno_val")));
					rec.setBudgetSezione(rs.getDouble("budget")); // tramite vo riempie anche il campo importodelta
					rec.setDataAgg(rs.getString("data_var_str"));
					rec.setDataUpd(rs.getTimestamp("dataUpd"));

					rec.setBudgetLetto(rs.getDouble("budget"));
					rec.setDataVal(rs.getString("data_val_str")); // tramite vo riempie anche il boolean chiusa

					rec.setSommaDispSezione(rs.getDouble("somma_disp"));

					listaSezioni.add(rec);
				} // End while

				rs.close();
				pstmt.close();

				int esercizio=0;
				if (ricercaSezioni.getEsercizio()!=null && ricercaSezioni.getEsercizio().trim().length()!=0)
				{
					esercizio=Integer.valueOf(ricercaSezioni.getEsercizio());
				}


				//09/01/09 i dati calcolati sono prelevati da db ma solo da elaboraspesa può essere attivata la procedura calcola

// da ripristinare inizio
				if (listaSezioni.size()==1 && (esercizio>0 || (ricercaSezioni.getDataOrdineDa()!=null && ricercaSezioni.getDataOrdineDa().trim().length()>0 ) || (ricercaSezioni.getDataOrdineA()!=null  && ricercaSezioni.getDataOrdineA().trim().length()>0 ) ))
				{
					CalcoliVO risult=new CalcoliVO();
					risult=this.calcola(listaSezioni.get(0).getCodPolo(), listaSezioni.get(0).getCodBibl(), listaSezioni.get(0).getIdSezione(),listaSezioni.get(0),0,null,esercizio,ricercaSezioni.getDataOrdineDa(),ricercaSezioni.getDataOrdineA(), ricercaSezioni.getTicket(), ricercaSezioni.getLoc());
					SezioneVO sezioneInOggetto=listaSezioni.get(0);
					double disp=sezioneInOggetto.getBudgetSezione();
					//String dispStr=Pulisci.VisualizzaImporto(listaSezioni.get(i).getBudgetSezione());
					if (risult!=null && risult.getOrdinato()!=0)
					{
						disp=sezioneInOggetto.getBudgetSezione() - risult.getOrdinato();
					    //dispStr=Pulisci.VisualizzaImporto(disp);
					}
					sezioneInOggetto.setSommaDispSezione(disp);

				}
// da ripristinare fine

				if (listaSezioni.size()==1 && ricercaSezioni.isStoria())
				{

					String sql2="select sez.id_sez_acquis_bibliografiche, sez.cd_polo, sez.cd_bib, TO_CHAR(sez.data_val,'dd/MM/yyyy') as  data_val_str,  TO_CHAR(sezAcqStoric.data_var_bdg,'dd/MM/yyyy') as  data_var_bdg_str, sezAcqStoric.importo_diff, sezAcqStoric.budget_old    ";
					sql2=sql2 + " from  tba_sez_acquis_bibliografiche sez ";
					sql2=sql2 + " join tra_sez_acq_storico sezAcqStoric on sezAcqStoric.id_sez_acquis_bibliografiche=sez.id_sez_acquis_bibliografiche and sezAcqStoric.fl_canc<>'S'";
					sql2=this.struttura(sql2);
					sql2=sql2 + " sez.fl_canc<>'S'";
					if (ricercaSezioni.getIdSezione()!=0)
					{
						sql2=this.struttura(sql2);
						sql2=sql2 + " sez.id_sez_acquis_bibliografiche=" + ricercaSezioni.getIdSezione();
					}
					if (ricercaSezioni.getCodPolo() !=null &&  ricercaSezioni.getCodPolo().length()!=0)
					{
						sql2=this.struttura(sql2);
						sql2=sql2  + " sez.cd_polo='" + ricercaSezioni.getCodPolo() +"'";
					}
					if (ricercaSezioni.getCodBibl() !=null &&  ricercaSezioni.getCodBibl().length()!=0)
					{
						sql2=this.struttura(sql2);
						sql2=sql2 + " sez.cd_bib='" + ricercaSezioni.getCodBibl() +"'";
					}
					sql2=sql2 + " order by data_var_bdg ";
					pstmt2 = connection.prepareStatement(sql2);
					rs2 = pstmt2.executeQuery();
					int numRighe2=0;
					List<StrutturaQuinquies> righeEsameStoria=new ArrayList<StrutturaQuinquies> ();
					StrutturaQuinquies rigaEsameStoria;
					while (rs2.next()) {
						rigaEsameStoria = new StrutturaQuinquies();
						rigaEsameStoria.setCodice1(rs2.getString("data_var_bdg_str"));
					 	String valoreStr="0.00";
						rigaEsameStoria.setCodice2(valoreStr);
					 	if (rs2.getString("importo_diff")!=null)
					 	{
						    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
						    dfs.setGroupingSeparator(',');
						    dfs.setDecimalSeparator('.');
						    // controllo formattazione con virgola separatore dei decimali
						    try {
						    	 DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
						    	 // importo
						    	String stringa  = df.format(rs2.getDouble("importo_diff"));
						    	 valoreStr=stringa;
						    } catch (Exception e) {
							    	log.error("", e);
							}
					 	}
						if (valoreStr!=null && !valoreStr.equals("0.00"))
						{
							rigaEsameStoria.setCodice2(valoreStr);
						}

						String bdgOldStr="0.00";
						rigaEsameStoria.setCodice3(bdgOldStr);
					 	if (rs2.getString("budget_old")!=null)
					 	{
						    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
						    dfs.setGroupingSeparator(',');
						    dfs.setDecimalSeparator('.');
						    // controllo formattazione con virgola separatore dei decimali
						    try {
						    	 DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
						    	 // importo
						    	String stringa  = df.format(rs2.getDouble("budget_old"));
						    	bdgOldStr=stringa;
						    } catch (Exception e) {
							    	log.error("", e);
							}
					 	}
						if (bdgOldStr!=null && !bdgOldStr.equals("0.00"))
						{
							rigaEsameStoria.setCodice3(bdgOldStr);
						}

						righeEsameStoria.add(rigaEsameStoria);
					} // End while
					rs2.close();
					pstmt2.close();
					if (righeEsameStoria!=null && righeEsameStoria.size()>0) // listaSezioni.size()==1
					{
						listaSezioni.get(0).setRigheEsameStoria(righeEsameStoria);
					}
				}
				connection.close();


				Validazione.ValidaRicercaSezioni (listaSezioni);

			}	catch (ValidationException e) {
		    	  throw e;

		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

        return listaSezioni;
	}

	public CalcoliVO calcola(String codP, String codB, int idSez,
			SezioneVO sez, int idBil, String impegno, int esercizio,
			String dataI, String dataF, String tick, Locale loc, int flags)
			throws DataException, ApplicationException, ValidationException {
		// almaviva5_20141027 #5661
		if (flags == 0)
			flags = Calcola.FLAG_ALL;
		return Calcola.calcola(this, codP, codB, idSez, sez, idBil, impegno, esercizio, dataI, dataF, flags);
	}

	public CalcoliVO calcola(String codP, String codB, int idSez,
			SezioneVO sez, int idBil, String impegno, int esercizio,
			String dataI, String dataF, String tick, Locale loc)
			throws DataException, ApplicationException, ValidationException {
		// almaviva5_20141027 #5661
		return Calcola.calcola(this, codP, codB, idSez, sez, idBil, impegno, esercizio, dataI, dataF, Calcola.FLAG_ALL);
	}

	public List<RigheVO>  ripartoSpese (ListaSuppSpeseVO criteriRiparto) throws DataException, ApplicationException , ValidationException
	{
		List<RigheVO> listaRisultati = null;

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		CalcoliVO calcoliAUT=new  CalcoliVO();
		try {
				connection = getConnection();
			String sql="";
			if (criteriRiparto.getTipoReport()!=null &&  criteriRiparto.getTipoReport().equals("2"))
			{
				sql +=" select CASE WHEN  (sum(ord.prezzo_lire) is null) THEN 0  else sum(ord.prezzo_lire)  END  as totACQ, sez.cod_sezione, capBil.esercizio , capBil.capitolo, bil.cod_mat  ";
				sql +=" FROM tba_ordini ord ";
				sql +=" join tba_sez_acquis_bibliografiche sez on sez.id_sez_acquis_bibliografiche=ord.id_sez_acquis_bibliografiche and sez.fl_canc<>'S' ";
				sql +=" join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=ord.id_capitoli_bilanci and capBil.fl_canc<>'S' ";
				sql +=" join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and  ord.tbb_bilancicod_mat=bil.cod_mat and bil.fl_canc<>'S' ";
			}
			else if (criteriRiparto.getTipoReport()!=null && criteriRiparto.getTipoReport().equals("3"))
			{
				sql +=" select CASE WHEN  (sum(ord.prezzo_lire) is null) THEN 0  else sum(ord.prezzo_lire)  END  as totACQ,  forn.nom_fornitore, capBil.esercizio , capBil.capitolo, bil.cod_mat  ";
				sql +=" FROM tba_ordini ord ";
				sql +=" left join tbr_fornitori forn on forn.cod_fornitore=ord.cod_fornitore and forn.fl_canc<>'S' ";
				sql +=" join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=ord.id_capitoli_bilanci and capBil.fl_canc<>'S' ";
				sql +=" join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and  ord.tbb_bilancicod_mat=bil.cod_mat and bil.fl_canc<>'S' ";
			}
			else if (criteriRiparto.getTipoReport()!=null &&  criteriRiparto.getTipoReport().equals("4"))
			{
				sql +=" select ord.*,  TO_CHAR(ord.data_ord,'dd/MM/yy') as data_ord_str,  forn.nom_fornitore, capBil.esercizio , capBil.capitolo, bil.cod_mat  ";
				sql +=" FROM tba_ordini ord ";
				sql +=" left join tbr_fornitori forn on forn.cod_fornitore=ord.cod_fornitore and forn.fl_canc<>'S' ";
				sql +=" left join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=ord.id_capitoli_bilanci and capBil.fl_canc<>'S' ";
				sql +=" left join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and  ord.tbb_bilancicod_mat=bil.cod_mat and bil.fl_canc<>'S' ";
			}

			else
			{
				//sql +=" select CASE WHEN  (sum(ord.prezzo_lire) is null) THEN 0  else sum(ord.prezzo_lire)  END  as totACQ, ord.anno_ord, ord.cod_tip_ord ";
				sql +=" select CASE WHEN  (sum(ord.prezzo_lire) is null) THEN 0  else sum(ord.prezzo_lire)  END  as totACQ, ord.anno_ord, (SELECT CASE WHEN (ord.cod_tip_ord='A'  OR ord.cod_tip_ord='V') THEN 'Acquisto' ELSE  'Legatura' END) as tipologia ";
				sql +=" FROM tba_ordini ord ";
				//if ((criteriRiparto.getEsercizio()!=null && criteriRiparto.getEsercizio().length()==4) || (criteriRiparto.getCapitolo()!=null && criteriRiparto.getCapitolo().length()!=0) ||  (criteriRiparto.getTipoImpegno()!=null && criteriRiparto.getTipoImpegno().length()!=0) )
				//{
				if ( criteriRiparto.getEsercizio()!=null && criteriRiparto.getEsercizio().trim().length()==4 )
				{
					sql +=" join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=ord.id_capitoli_bilanci and capBil.fl_canc<>'S' ";
					sql +=" left join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and  ord.tbb_bilancicod_mat=bil.cod_mat and bil.fl_canc<>'S' ";
				}
			}

			if ((criteriRiparto.getRangeDewey()!=null && criteriRiparto.getRangeDewey().length()>0) || (criteriRiparto.getTipoRecord()!=null && criteriRiparto.getTipoRecord().trim().length()==1) || (criteriRiparto.getLingua()!=null && criteriRiparto.getLingua().trim().length()==3) || (criteriRiparto.getPaese()!=null && criteriRiparto.getPaese().trim().length()==2))
			{
				if ((criteriRiparto.getRangeDewey()!=null && criteriRiparto.getRangeDewey().length()>0) )
				{
					sql +=" join tb_classe classif on  classif.fl_canc<>'S' and classif.cd_sistema='D21' ";
				}
				sql +=" join tb_titolo tit on tit.bid=ord.bid  and tit.fl_canc<>'S' ";
				if ((criteriRiparto.getRangeDewey()!=null && criteriRiparto.getRangeDewey().length()>0) )
				{
					sql +=" and classif.cd_livello=tit.cd_livello ";
				}
			}

				sql +=" where ord.fl_canc<>'S'"; // ESCLUDO I CANCELLATI
				sql +=" and ord.stato_ordine <>'N'"; // ESCLUDO GLI ANNULLATI
				//sql +=" and ord.stampato=true "; // SOLO ORDINI STAMPATI

			if (criteriRiparto.getTipoRecord()!=null && criteriRiparto.getTipoRecord().trim().length()==1)
			{
				sql +=" and tit.tp_record_uni='" + criteriRiparto.getTipoRecord().trim() +"'";
			}
			if (criteriRiparto.getLingua()!=null && criteriRiparto.getLingua().trim().length()==3)
			{
				sql +=" and (tit.cd_lingua_1='" + criteriRiparto.getLingua().trim() +"'";
				sql +=" or tit.cd_lingua_2='" + criteriRiparto.getLingua().trim() +"'";
				sql +=" or tit.cd_lingua_3='" + criteriRiparto.getLingua().trim() +"') ";
			}
			if (criteriRiparto.getPaese()!=null && criteriRiparto.getPaese().trim().length()==2)
			{
				sql +=" and tit.cd_paese='" + criteriRiparto.getPaese().trim() +"'";
			}
			if (criteriRiparto.getRangeDewey()!=null && criteriRiparto.getRangeDewey().length()>0)
			{
				if (criteriRiparto.getRangeDewey().equals("0"))
				{
					sql +=" and cast(substr(classif.classe,0,4) as integer) between 000 and 099 ";
				}
				if (criteriRiparto.getRangeDewey().equals("1"))
				{
					sql +=" and cast(substr(classif.classe,0,4) as integer) between 100 and 199 ";
				}
				if (criteriRiparto.getRangeDewey().equals("2"))
				{
					sql +=" and cast(substr(classif.classe,0,4) as integer) between 200 and 299 ";
				}
				if (criteriRiparto.getRangeDewey().equals("3"))
				{
					sql +=" and cast(substr(classif.classe,0,4) as integer) between 300 and 399 ";
				}
				if (criteriRiparto.getRangeDewey().equals("4"))
				{
					sql +=" and cast(substr(classif.classe,0,4) as integer) between 400 and 499 ";
				}
				if (criteriRiparto.getRangeDewey().equals("5"))
				{
					sql +=" and cast(substr(classif.classe,0,4) as integer) between 500 and 599 ";
				}
				if (criteriRiparto.getRangeDewey().equals("6"))
				{
					sql +=" and cast(substr(classif.classe,0,4) as integer) between 600 and 699 ";
				}
				if (criteriRiparto.getRangeDewey().equals("7"))
				{
					sql +=" and cast(substr(classif.classe,0,4) as integer) between 700 and 799 ";
				}
				if (criteriRiparto.getRangeDewey().equals("8"))
				{
					sql +=" and cast(substr(classif.classe,0,4) as integer) between 800 and 899 ";
				}
				if (criteriRiparto.getRangeDewey().equals("9"))
				{
					sql +=" and cast(substr(classif.classe,0,4) as integer) between 900 and 999 ";
				}
			}





				if (criteriRiparto.getCodBibl()!=null && criteriRiparto.getCodBibl().length()>0)
				{
					sql +=" and ord.cd_bib='"+ criteriRiparto.getCodBibl() +"'";
				}
				if (criteriRiparto.getCodPolo()!=null && criteriRiparto.getCodPolo().length()>0)
				{
					sql +=" and ord.cd_polo='"+ criteriRiparto.getCodPolo() +"'";
				}
				if (criteriRiparto.getAnno()!=null && criteriRiparto.getAnno().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" ord.anno_ord="  +  criteriRiparto.getAnno().trim() ;
				}
				if (criteriRiparto.getTipoOrdine()!=null && criteriRiparto.getTipoOrdine().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" ord.cod_tip_ord='"  +  criteriRiparto.getTipoOrdine().trim() +"'";
				}
				if (criteriRiparto.getTipoOrdineArr()!=null && criteriRiparto.getTipoOrdineArr().length!=0 )
				{
					Boolean aggiungiSQL=false;
					String sqla="";
					sqla=sqla + " ( ";
					//sql +=" ord.stato_ordine='" + ricercaOrdini.getStatoOrdine() +"'";
					for (int index = 0; index < criteriRiparto.getTipoOrdineArr().length; index++) {
						String so = criteriRiparto.getTipoOrdineArr()[index] ;
						if (so!=null && so.length()!=0)
						{
					         if (!sqla.equals(" ( ")) {
					        	 sqla=sqla + " OR ";
					         }
					        sqla=sqla + " ord.cod_tip_ord='" + so +"'";
							aggiungiSQL=true;
						}
					}
					sqla=sqla + " ) ";
					if (aggiungiSQL)
					{
						sql=this.struttura(sql);
						sql += sqla;
					}
				}
				sql=this.struttura(sql);
				sql +=" ord.cod_tip_ord<>'D'"; // ESCLUDO il tipo dono tck 2565;
				sql=this.struttura(sql);
				sql +=" ord.cod_tip_ord<>'C'"; // ESCLUDO il tipo cambio tck 2565;
				sql=this.struttura(sql);
				sql +=" ord.cod_tip_ord<>'L'"; // ESCLUDO il tipo deposito legale tck 2565;
				sql=this.struttura(sql);
				sql +=" ord.cod_tip_ord<>'R'"; // ESCLUDO il tipo rilegatura tck 3273;
				sql=this.struttura(sql);
				sql +=" ord.cod_tip_ord<>'R'"; // ESCLUDO il tipo rilegatura tck 0004377 - rp;
				sql=this.struttura(sql);
				sql +=" ord.cod_tip_ord<>'V'"; // ESCLUDO il tipo visione trattenuta tck 0004377 - rp;


				if (criteriRiparto.getDataOrdineDa()!=null && criteriRiparto.getDataOrdineDa().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" ord.data_ord >= TO_DATE ('" +  criteriRiparto.getDataOrdineDa().trim() + "','dd/MM/yyyy')";
				}
				if (criteriRiparto.getDataOrdineA()!=null && criteriRiparto.getDataOrdineA().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" ord.data_ord <= TO_DATE ('" +  criteriRiparto.getDataOrdineA().trim() + "','dd/MM/yyyy')";
				}
				if  (criteriRiparto.getIdBil()>0)
				{
					sql=this.struttura(sql);
					sql +=" ord.id_capitoli_bilanci ="+ criteriRiparto.getIdBil();
				}
				//if ((criteriRiparto.getTipoReport().equals("1") || criteriRiparto.getTipoReport().equals("2") || criteriRiparto.getTipoReport().equals("3")  ) &&  criteriRiparto.getEsercizio()!=null && criteriRiparto.getEsercizio().trim().length()==4 )
				if ( criteriRiparto.getEsercizio()!=null && criteriRiparto.getEsercizio().trim().length()==4 )
				{
					sql=this.struttura(sql);
					sql +=" capBil.esercizio = "+ criteriRiparto.getEsercizio().trim();
				}
				//if ((criteriRiparto.getTipoReport().equals("2") || criteriRiparto.getTipoReport().equals("3") ) &&  criteriRiparto.getCapitolo()!=null && criteriRiparto.getCapitolo().trim().length()==4 )
				if ( criteriRiparto.getCapitolo()!=null && criteriRiparto.getCapitolo().trim().length()==4 )
				{
					sql=this.struttura(sql);
					sql +=" capBil.capitolo = "+ criteriRiparto.getCapitolo().trim();
				}

				if (criteriRiparto.getImp()!=null && criteriRiparto.getImp().length()==1 )
				{
					sql=this.struttura(sql);
					sql +=" ord.tbb_bilancicod_mat = '"+ criteriRiparto.getImp() + "'";
				}
				if (criteriRiparto.getNaturaOrdine()!=null && criteriRiparto.getNaturaOrdine().length()!=0 )
				{
					sql=this.struttura(sql);
					sql +=" ord.natura = '"+ criteriRiparto.getNaturaOrdine() + "'";
				}
				if  (criteriRiparto.getIdForn()>0)
				{
					sql=this.struttura(sql);
					sql +=" ord.cod_fornitore ="+ criteriRiparto.getIdForn();
				}
				if  (criteriRiparto.getIdSez()>0)
				{
					sql=this.struttura(sql);
					sql +=" ord.id_sez_acquis_bibliografiche ="+ criteriRiparto.getIdSez();
				}
				//if  (criteriRiparto.getTipoReport().equals("2") && criteriRiparto.getIdListaSezioni()!=null && criteriRiparto.getIdListaSezioni().length()!=0)
				if  ( criteriRiparto.getIdListaSezioni()!=null && criteriRiparto.getIdListaSezioni().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" sez.cod_sezione in ( "+ criteriRiparto.getIdListaSezioni() + " )";
				}
				if (criteriRiparto.getOrdNOinv())
				{
					sql=this.struttura(sql);
					sql +="  NOT EXISTS ";
					sql +=" ( select null FROM tbc_inventario inv where inv.fl_canc<>'S' and inv.cd_bib_ord=ord.cd_bib and  inv.cd_tip_ord=ord.cod_tip_ord and  inv.anno_ord=ord.anno_ord and  inv.cd_ord =ord.cod_ord) ";
				}


			if (criteriRiparto.getTipoReport()!=null && criteriRiparto.getTipoReport().equals("2"))
			{
				sql +=" group by sez.cod_sezione, capBil.id_capitoli_bilanci, capBil.esercizio , capBil.capitolo,  bil.cod_mat ";
				sql +="  order by sez.cod_sezione,  capBil.id_capitoli_bilanci, capBil.esercizio , capBil.capitolo,  bil.cod_mat ";

			}
			else if (criteriRiparto.getTipoReport()!=null && criteriRiparto.getTipoReport().equals("3"))
			{
				sql +=" group by forn.nom_fornitore, capBil.esercizio , capBil.capitolo, bil.cod_mat  ";
				sql +=" order by forn.nom_fornitore, capBil.esercizio , capBil.capitolo, bil.cod_mat ";
			}
			else if (criteriRiparto.getTipoReport()!=null &&  criteriRiparto.getTipoReport().equals("4"))
			{
				if (criteriRiparto.getOrdinamento().equals("1"))
				{
					sql +="  order by  ord.data_ord ";
				}
				else if (criteriRiparto.getOrdinamento().equals("2"))
				{
					sql +="  order by  ord.data_ord desc ";
				}
				else if (criteriRiparto.getOrdinamento().equals("3"))
				{
					sql +="  order by lower(forn.nom_fornitore) ";
				}
				else if (criteriRiparto.getOrdinamento().equals("4"))
				{
					sql +="  order by ord.bid ";
				}
				else if (criteriRiparto.getOrdinamento().equals("5"))
				{
					sql +="  order by capBil.esercizio , capBil.capitolo, bil.cod_mat ";
				}

				else
				{
					sql +=" order by ord.anno_ord, ord.cod_tip_ord,cod_ord ";
				}

			}

			else
			{
				sql +=" group by ord.anno_ord, tipologia ";
				if (criteriRiparto.getOrdinamento()==null || (criteriRiparto.getOrdinamento()!=null && criteriRiparto.getOrdinamento().equals("")))
				{
					sql +="  order by ord.anno_ord ,  tipologia ";
				}
				else if (criteriRiparto.getOrdinamento().equals("1"))
				{
					sql +="  order by ord.anno_ord ,  tipologia ";
				}
				else if (criteriRiparto.getOrdinamento().equals("2"))
				{
					sql +="  order by ord.anno_ord desc,  tipologia";
				}
			}

				pstmt = connection.prepareStatement(sql);
				rs = pstmt.executeQuery();
				double imp=0.00;
				double tot=0.00;
				String natura="";
				String continuativo="";
				listaRisultati=new ArrayList<RigheVO>();
				int numRiga=0;
				while (rs.next()) {
					RigheVO eleRig=new RigheVO();
					if (!criteriRiparto.getTipoReport().equals("4"))
					{
						imp=rs.getDouble("totACQ");
					}
					else
					{
						imp=rs.getDouble("prezzo_lire");
					}
					eleRig.setImpegnato(imp);
					if (criteriRiparto.getTipoReport().equals("2"))
					{
						eleRig.setSezione(rs.getString("cod_sezione").trim());
						eleRig.setEsercizio(rs.getString("esercizio"));
						eleRig.setCapitolo(rs.getString("capitolo"));
						eleRig.setImpegno(rs.getString("cod_mat"));
					}
					else if (criteriRiparto.getTipoReport().equals("3"))
					{
						eleRig.setFornitore(rs.getString("nom_fornitore").trim());
						eleRig.setEsercizio(rs.getString("esercizio"));
						eleRig.setCapitolo(rs.getString("capitolo"));
						eleRig.setImpegno(rs.getString("cod_mat"));
					}
					else if (criteriRiparto.getTipoReport().equals("4"))
					{
						eleRig.setIDOrd(rs.getInt("id_ordine"));
						eleRig.setAnnoOrd(rs.getString("anno_ord").trim());
						eleRig.setTipoOrdine(rs.getString("cod_tip_ord").trim());
						eleRig.setCodOrdine(rs.getString("cod_ord").trim());
						eleRig.setDataOrdine(rs.getString("data_ord_str").trim());
						eleRig.setStampato("No");
						if (rs.getBoolean("stampato"))
						{
							eleRig.setStampato("Si");
						}
						eleRig.setStatoOrdine(rs.getString("stato_ordine"));
						eleRig.setBid(rs.getString("bid"));
						eleRig.setNatura(rs.getString("natura"));
						String titolo="";
						String isbd="";
						if (rs.getString("bid")!=null && rs.getString("bid").trim().length()!=0)
						{
							try {
								TitoloACQVO recTit=null;
								recTit= this.getTitoloOrdineTer(rs.getString("bid"));
								if (recTit!=null && recTit.getIsbd()!=null)
								{
									isbd=recTit.getIsbd();
								}
							} catch (Exception e) {
								isbd="";
							}
							titolo=isbd;
						}
						eleRig.setTitolo(titolo);
						eleRig.setContinuativo("No");
						if (rs.getString("continuativo")!=null && rs.getString("continuativo").length()!=0 && rs.getString("continuativo").equals("1"))
						{
							eleRig.setContinuativo("Si");
						}
						eleRig.setCodFornitore(rs.getString("cod_fornitore").trim());
						eleRig.setFornitore(rs.getString("nom_fornitore").trim());
						eleRig.setEsercizio(rs.getString("esercizio"));
						eleRig.setCapitolo(rs.getString("capitolo"));
						eleRig.setImpegno(rs.getString("cod_mat"));
					}

					else
					{
						eleRig.setAnnoOrdine(rs.getInt("anno_ord"));
						eleRig.setTipologia(rs.getString("tipologia"));
					}

					//tot=tot + imp;
					numRiga=numRiga+1;
					listaRisultati.add(eleRig);
				}
				rs.close();
				pstmt.close();

				String sqlACQ="";

				if (!criteriRiparto.getOrdNOinv())
				{
					if (criteriRiparto.getTipoReport()!=null && criteriRiparto.getTipoReport().equals("2"))
					{
						sqlACQ= sqlACQ + "select CASE WHEN  (sum(inv.importo) is null) THEN 0  else sum(inv.importo)  END  as totACQUISITO,  CASE WHEN  (sum(inv.valore) is null) THEN 0  else sum(inv.valore)  END  as totVALOREINVENTARIALE,  sez.cod_sezione, capBil.esercizio , capBil.capitolo, bil.cod_mat  ";
						sqlACQ=sqlACQ  + " FROM tbc_inventario inv ";
						sqlACQ=sqlACQ  + " join tba_ordini ord on ord.cd_bib=inv.cd_bib_ord  and ord.cod_tip_ord=inv.cd_tip_ord and ord.anno_ord=inv.anno_ord and ord.cod_ord=inv.cd_ord and ord.fl_canc<>'S'";
						sqlACQ=sqlACQ + " join tba_sez_acquis_bibliografiche sez on sez.id_sez_acquis_bibliografiche=ord.id_sez_acquis_bibliografiche and sez.fl_canc<>'S' ";
						sqlACQ=sqlACQ + " join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=ord.id_capitoli_bilanci and capBil.fl_canc<>'S' ";
						sqlACQ=sqlACQ + " join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and  ord.tbb_bilancicod_mat=bil.cod_mat and bil.fl_canc<>'S' ";
					}
					else if (criteriRiparto.getTipoReport()!=null && criteriRiparto.getTipoReport().equals("3"))
					{
						sqlACQ= sqlACQ + "select CASE WHEN  (sum(inv.importo) is null) THEN 0  else sum(inv.importo)  END  as totACQUISITO,  CASE WHEN  (sum(inv.valore) is null) THEN 0  else sum(inv.valore)  END  as totVALOREINVENTARIALE,   forn.nom_fornitore, capBil.esercizio , capBil.capitolo, bil.cod_mat  ";
						sqlACQ=sqlACQ  + " FROM tbc_inventario inv ";
						sqlACQ=sqlACQ  + " join tba_ordini ord on ord.cd_bib=inv.cd_bib_ord  and ord.cod_tip_ord=inv.cd_tip_ord and ord.anno_ord=inv.anno_ord and ord.cod_ord=inv.cd_ord and ord.fl_canc<>'S'";
						sqlACQ=sqlACQ + " left join tbr_fornitori forn on forn.cod_fornitore=ord.cod_fornitore and forn.fl_canc<>'S' ";
						sqlACQ=sqlACQ + " join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=ord.id_capitoli_bilanci and capBil.fl_canc<>'S' ";
						sqlACQ=sqlACQ + " join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and  ord.tbb_bilancicod_mat=bil.cod_mat and bil.fl_canc<>'S' ";
					}
					else if (criteriRiparto.getTipoReport()!=null && criteriRiparto.getTipoReport().equals("4"))
					{
						sqlACQ= sqlACQ + "select CASE WHEN  (sum(inv.importo) is null) THEN 0  else sum(inv.importo)  END  as totACQUISITO,  CASE WHEN  (sum(inv.valore) is null) THEN 0  else sum(inv.valore)  END  as totVALOREINVENTARIALE,   ord.id_ordine  ";
						sqlACQ=sqlACQ  + " FROM tbc_inventario inv ";
						sqlACQ=sqlACQ  + " join tba_ordini ord on ord.cd_bib=inv.cd_bib_ord  and ord.cod_tip_ord=inv.cd_tip_ord and ord.anno_ord=inv.anno_ord and ord.cod_ord=inv.cd_ord and ord.fl_canc<>'S'";
						sqlACQ=sqlACQ + " left join tbr_fornitori forn on forn.cod_fornitore=ord.cod_fornitore and forn.fl_canc<>'S' ";
						sqlACQ=sqlACQ + " left join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=ord.id_capitoli_bilanci and capBil.fl_canc<>'S' ";
						sqlACQ=sqlACQ + " left join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and  ord.tbb_bilancicod_mat=bil.cod_mat and bil.fl_canc<>'S' ";
					}

					else
					{
						//sqlACQ= sqlACQ + "select CASE WHEN  (sum(inv.importo) is null) THEN 0  else sum(inv.importo)  END  as totACQUISITO,  CASE WHEN  (sum(inv.valore) is null) THEN 0  else sum(inv.valore)  END  as totVALOREINVENTARIALE,  ord.anno_ord, ord.cod_tip_ord  ";
						sqlACQ= sqlACQ + "select CASE WHEN  (sum(inv.importo) is null) THEN 0  else sum(inv.importo)  END  as totACQUISITO,  CASE WHEN  (sum(inv.valore) is null) THEN 0  else sum(inv.valore)  END  as totVALOREINVENTARIALE,  ord.anno_ord, (SELECT CASE WHEN (ord.cod_tip_ord='A'  OR ord.cod_tip_ord='V') THEN 'Acquisto' ELSE  'Legatura' END) as tipologia  ";
						sqlACQ=sqlACQ  + " FROM tbc_inventario inv ";
						sqlACQ=sqlACQ  + " join tba_ordini ord on ord.cd_bib=inv.cd_bib_ord  and ord.cod_tip_ord=inv.cd_tip_ord and ord.anno_ord=inv.anno_ord and ord.cod_ord=inv.cd_ord and ord.fl_canc<>'S'";
						//if ((criteriRiparto.getEsercizio()!=null && criteriRiparto.getEsercizio().length()==4) || (criteriRiparto.getCapitolo()!=null && criteriRiparto.getCapitolo().length()!=0) ||  (criteriRiparto.getTipoImpegno()!=null && criteriRiparto.getTipoImpegno().length()!=0) )
						//{
						if ( criteriRiparto.getEsercizio()!=null && criteriRiparto.getEsercizio().trim().length()==4 )
						{
							sqlACQ=sqlACQ + " join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=ord.id_capitoli_bilanci and capBil.fl_canc<>'S' ";
							sqlACQ=sqlACQ + " join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and  ord.tbb_bilancicod_mat=bil.cod_mat and bil.fl_canc<>'S' ";
						}

					}

					if ((criteriRiparto.getRangeDewey()!=null && criteriRiparto.getRangeDewey().length()>0) || (criteriRiparto.getTipoRecord()!=null && criteriRiparto.getTipoRecord().trim().length()==1) || (criteriRiparto.getLingua()!=null && criteriRiparto.getLingua().trim().length()==3) || (criteriRiparto.getPaese()!=null && criteriRiparto.getPaese().trim().length()==2))
					{
						if ((criteriRiparto.getRangeDewey()!=null && criteriRiparto.getRangeDewey().length()>0) )
						{
							sqlACQ=sqlACQ + " join tb_classe classif on  classif.fl_canc<>'S' and classif.cd_sistema='D21' ";
						}
						sqlACQ=sqlACQ + " join tb_titolo tit on tit.bid=ord.bid  and tit.fl_canc<>'S' ";
						if ((criteriRiparto.getRangeDewey()!=null && criteriRiparto.getRangeDewey().length()>0) )
						{
							sqlACQ=sqlACQ + " and classif.cd_livello=tit.cd_livello ";
						}
					}

						sqlACQ=sqlACQ  + " where inv.fl_canc<>'S'"; // ESCLUDO I CANCELLATI

						sqlACQ=sqlACQ  + " and ord.stato_ordine <>'N'"; // ESCLUDO ordini  ANNULLATI

						if (criteriRiparto.getTipoRecord()!=null && criteriRiparto.getTipoRecord().trim().length()==1)
						{
							sqlACQ=sqlACQ  + " and tit.tp_record_uni='" + criteriRiparto.getTipoRecord().trim() +"'";
						}
						if (criteriRiparto.getLingua()!=null && criteriRiparto.getLingua().trim().length()==3)
						{
							sqlACQ=sqlACQ  + " and (tit.cd_lingua_1='" + criteriRiparto.getLingua().trim() +"'";
							sqlACQ=sqlACQ  + " or tit.cd_lingua_2='" + criteriRiparto.getLingua().trim() +"'";
							sqlACQ=sqlACQ  + " or tit.cd_lingua_3='" + criteriRiparto.getLingua().trim() +"') ";
						}
						if (criteriRiparto.getPaese()!=null && criteriRiparto.getPaese().trim().length()==2)
						{
							sqlACQ=sqlACQ  + " and tit.cd_paese='" + criteriRiparto.getPaese().trim() +"'";
						}


						if (criteriRiparto.getRangeDewey()!=null && criteriRiparto.getRangeDewey().length()>0)
						{

							if (criteriRiparto.getRangeDewey().equals("0"))
							{
								sqlACQ=sqlACQ + " and cast(substr(classif.classe,0,4) as integer) between 000 and 099 ";
							}
							if (criteriRiparto.getRangeDewey().equals("1"))
							{
								sqlACQ=sqlACQ + " and cast(substr(classif.classe,0,4) as integer) between 100 and 199 ";
							}
							if (criteriRiparto.getRangeDewey().equals("2"))
							{
								sqlACQ=sqlACQ + " and cast(substr(classif.classe,0,4) as integer) between 200 and 299 ";
							}
							if (criteriRiparto.getRangeDewey().equals("3"))
							{
								sqlACQ=sqlACQ + " and cast(substr(classif.classe,0,4) as integer) between 300 and 399 ";
							}
							if (criteriRiparto.getRangeDewey().equals("4"))
							{
								sqlACQ=sqlACQ + " and cast(substr(classif.classe,0,4) as integer) between 400 and 499 ";
							}
							if (criteriRiparto.getRangeDewey().equals("5"))
							{
								sqlACQ=sqlACQ + " and cast(substr(classif.classe,0,4) as integer) between 500 and 599 ";
							}
							if (criteriRiparto.getRangeDewey().equals("6"))
							{
								sqlACQ=sqlACQ + " and cast(substr(classif.classe,0,4) as integer) between 600 and 699 ";
							}
							if (criteriRiparto.getRangeDewey().equals("7"))
							{
								sqlACQ=sqlACQ + " and cast(substr(classif.classe,0,4) as integer) between 700 and 799 ";
							}
							if (criteriRiparto.getRangeDewey().equals("8"))
							{
								sqlACQ=sqlACQ + " and cast(substr(classif.classe,0,4) as integer) between 800 and 899 ";
							}
							if (criteriRiparto.getRangeDewey().equals("9"))
							{
								sqlACQ=sqlACQ + " and cast(substr(classif.classe,0,4) as integer) between 900 and 999 ";
							}
						}


						if (criteriRiparto.getCodBibl()!=null && criteriRiparto.getCodBibl().length()>0)
						{
							sqlACQ=sqlACQ  + " and ord.cd_bib='"+ criteriRiparto.getCodBibl() +"'";
						}
						if (criteriRiparto.getCodPolo()!=null && criteriRiparto.getCodPolo().length()>0)
						{
							sqlACQ=sqlACQ  + " and ord.cd_polo='"+ criteriRiparto.getCodPolo() +"'";
						}
						if (criteriRiparto.getAnno()!=null && criteriRiparto.getAnno().trim().length()!=0)
						{
							sqlACQ=sqlACQ + " and ord.anno_ord="  +  criteriRiparto.getAnno().trim() ;
						}
						if (criteriRiparto.getTipoOrdine()!=null && criteriRiparto.getTipoOrdine().trim().length()!=0)
						{
							sqlACQ=this.struttura(sqlACQ);
							sqlACQ=sqlACQ  + " ord.cod_tip_ord='"  +  criteriRiparto.getTipoOrdine().trim() +"'";
						}
						if (criteriRiparto.getTipoOrdineArr()!=null && criteriRiparto.getTipoOrdineArr().length!=0 )
						{
							Boolean aggiungiSQL=false;
							String sqla="";
							sqla=sqla + " ( ";
							//sql +=" ord.stato_ordine='" + ricercaOrdini.getStatoOrdine() +"'";
							for (int index = 0; index < criteriRiparto.getTipoOrdineArr().length; index++) {
								String so = criteriRiparto.getTipoOrdineArr()[index] ;
								if (so!=null && so.length()!=0)
								{
							         if (!sqla.equals(" ( ")) {
							        	 sqla=sqla + " OR ";
							         }
							        sqla=sqla + " ord.cod_tip_ord='" + so +"'";
									aggiungiSQL=true;
								}
							}
							sqla=sqla + " ) ";
							if (aggiungiSQL)
							{
								sqlACQ=this.struttura(sqlACQ);
								sqlACQ=sqlACQ + sqla;
							}
						}

						sqlACQ=this.struttura(sqlACQ);
						sqlACQ=sqlACQ  + " ord.cod_tip_ord<>'D'"; // ESCLUDO il tipo dono tck 2565;
						sqlACQ=this.struttura(sqlACQ);
						sqlACQ=sqlACQ  + " ord.cod_tip_ord<>'C'"; // ESCLUDO il tipo cambio tck 2565;
						sqlACQ=this.struttura(sqlACQ);
						sqlACQ=sqlACQ  + " ord.cod_tip_ord<>'L'"; // ESCLUDO il tipo deposito legale tck 2565;
						sqlACQ=this.struttura(sqlACQ);
						sqlACQ=sqlACQ  + " ord.cod_tip_ord<>'R'"; // ESCLUDO il tipo rilegatura tck 3273;

						if (criteriRiparto.getDataOrdineDa()!=null && criteriRiparto.getDataOrdineDa().trim().length()!=0)
						{
							sqlACQ=this.struttura(sqlACQ);
							sqlACQ=sqlACQ  + " ord.data_ord >= TO_DATE ('" +  criteriRiparto.getDataOrdineDa().trim() + "','dd/MM/yyyy')";
						}
						if (criteriRiparto.getDataOrdineA()!=null && criteriRiparto.getDataOrdineA().trim().length()!=0)
						{
							sqlACQ=this.struttura(sqlACQ);
							sqlACQ=sqlACQ  + " ord.data_ord <= TO_DATE ('" +  criteriRiparto.getDataOrdineA().trim() + "','dd/MM/yyyy')";
						}
						if  (criteriRiparto.getIdBil()>0)
						{
							sqlACQ=this.struttura(sqlACQ);
							sqlACQ=sqlACQ  + " ord.id_capitoli_bilanci ="+ criteriRiparto.getIdBil();
						}
						//if ((criteriRiparto.getTipoReport().equals("1") || criteriRiparto.getTipoReport().equals("2") || criteriRiparto.getTipoReport().equals("3") ) &&  criteriRiparto.getEsercizio()!=null && criteriRiparto.getEsercizio().trim().length()==4 )
						if (criteriRiparto.getEsercizio()!=null && criteriRiparto.getEsercizio().trim().length()==4 )
						{
							sqlACQ=this.struttura(sqlACQ);
							sqlACQ=sqlACQ + " capBil.esercizio = "+ criteriRiparto.getEsercizio().trim();
						}
						//if ((criteriRiparto.getTipoReport().equals("2") || criteriRiparto.getTipoReport().equals("3") ) &&  criteriRiparto.getCapitolo()!=null && criteriRiparto.getCapitolo().trim().length()==4 )
						if (criteriRiparto.getCapitolo()!=null && criteriRiparto.getCapitolo().trim().length()==4 )
						{
							sqlACQ=this.struttura(sqlACQ);
							sqlACQ=sqlACQ + " capBil.capitolo = "+ criteriRiparto.getCapitolo().trim();
						}

						if (criteriRiparto.getImp()!=null && criteriRiparto.getImp().length()==1 )
						{
							sqlACQ=this.struttura(sqlACQ);
							sqlACQ=sqlACQ  + " ord.tbb_bilancicod_mat = '"+ criteriRiparto.getImp() + "'";
						}
						if (criteriRiparto.getNaturaOrdine()!=null && criteriRiparto.getNaturaOrdine().length()!=0 )
						{
							sqlACQ=this.struttura(sqlACQ);
							sqlACQ=sqlACQ  + " ord.natura = '"+ criteriRiparto.getNaturaOrdine() + "'";
						}

						if  (criteriRiparto.getIdForn()>0)
						{
							sqlACQ=this.struttura(sqlACQ);
							sqlACQ=sqlACQ  + " ord.cod_fornitore ="+ criteriRiparto.getIdForn();
						}
						if  (criteriRiparto.getIdSez()>0)
						{
							sqlACQ=this.struttura(sqlACQ);
							sqlACQ=sqlACQ  + " ord.id_sez_acquis_bibliografiche ="+ criteriRiparto.getIdSez();
						}
						//if  (criteriRiparto.getTipoReport().equals("2") && criteriRiparto.getIdListaSezioni()!=null && criteriRiparto.getIdListaSezioni().length()!=0)
						if  ( criteriRiparto.getIdListaSezioni()!=null && criteriRiparto.getIdListaSezioni().length()!=0)
						{
							sqlACQ=this.struttura(sqlACQ);
							sqlACQ=sqlACQ + " sez.cod_sezione in ( "+ criteriRiparto.getIdListaSezioni() + " )";
						}

						if (criteriRiparto.getTipoMaterialeInv()!=null && criteriRiparto.getTipoMaterialeInv().length()!=0 )
						{
							sqlACQ=this.struttura(sqlACQ);
							sqlACQ=sqlACQ  + " inv.cd_mat_inv = '"+ criteriRiparto.getTipoMaterialeInv() + "'";
						}
						if (criteriRiparto.getSupporto()!=null && criteriRiparto.getSupporto().length()!=0 )
						{
							sqlACQ=this.struttura(sqlACQ);
							sqlACQ=sqlACQ  + " inv.cd_supporto = '"+ criteriRiparto.getSupporto() + "'";
						}


					if (criteriRiparto.getTipoReport()!=null && criteriRiparto.getTipoReport().equals("2"))
					{
						sqlACQ=sqlACQ + " group by sez.cod_sezione, capBil.id_capitoli_bilanci, capBil.esercizio , capBil.capitolo,  bil.cod_mat ";
						sqlACQ=sqlACQ + "  order by sez.cod_sezione,  capBil.id_capitoli_bilanci, capBil.esercizio , capBil.capitolo,  bil.cod_mat ";

					}

					else if (criteriRiparto.getTipoReport()!=null && criteriRiparto.getTipoReport().equals("3"))
					{
						sqlACQ=sqlACQ + " group by forn.nom_fornitore, capBil.esercizio , capBil.capitolo, bil.cod_mat ";
						sqlACQ=sqlACQ + "  order by forn.nom_fornitore, capBil.esercizio , capBil.capitolo, bil.cod_mat   ";
					}
					else if (criteriRiparto.getTipoReport()!=null && criteriRiparto.getTipoReport().equals("4"))
					{
						sqlACQ=sqlACQ + " group by ord.id_ordine ";
						sqlACQ=sqlACQ + "  order by ord.id_ordine ";
					}

					else
					{
						//sqlACQ=sqlACQ  + " group by ord.anno_ord, ord.cod_tip_ord ";
						sqlACQ=sqlACQ  + " group by ord.anno_ord, tipologia ";
						if (criteriRiparto.getOrdinamento()==null || (criteriRiparto.getOrdinamento()!=null && criteriRiparto.getOrdinamento().equals("")))
						{
							//sqlACQ=sqlACQ  + "  order by ord.anno_ord ,  ord.cod_tip_ord ";
							sqlACQ=sqlACQ  + "  order by ord.anno_ord ,  tipologia ";
						}
						else if (criteriRiparto.getOrdinamento().equals("1"))
						{
							//sqlACQ=sqlACQ  + "  order by ord.anno_ord ,  ord.cod_tip_ord ";
							sqlACQ=sqlACQ  + "  order by ord.anno_ord ,  tipologia ";
						}
						else if (criteriRiparto.getOrdinamento().equals("2"))
						{
							//sqlACQ=sqlACQ  + "  order by ord.anno_ord desc,  ord.cod_tip_ord";
							sqlACQ=sqlACQ  + "  order by ord.anno_ord desc,  tipologia ";
						}

					}


						pstmt = connection.prepareStatement(sqlACQ);
						rs = pstmt.executeQuery();
						double impACQ=0.00;
						double impVALINV=0.00;
						double totACQ=0.00;
						double totVALINV=0.00;
						numRiga=0;
						List<RigheVO> arrAcq=new ArrayList<RigheVO>();
						while (rs.next()) {
							impACQ=rs.getDouble("totACQUISITO");
							impVALINV=rs.getDouble("totVALOREINVENTARIALE");



							if (criteriRiparto.getTipoReport().equals("2"))
							{
								for (int i=0; i<listaRisultati.size(); i++)
								{
									if (listaRisultati.get(i).getSezione().equals(rs.getString("cod_sezione").trim()) && listaRisultati.get(i).getEsercizio().equals(rs.getString("esercizio")) && listaRisultati.get(i).getCapitolo().equals(rs.getString("capitolo")) && listaRisultati.get(i).getImpegno().equals(rs.getString("cod_mat")))
									{
										listaRisultati.get(i).setAcquisito(impACQ);
										listaRisultati.get(i).setValoreInventariale(impVALINV);
										break;
									}
								}

							}
							else if (criteriRiparto.getTipoReport().equals("3"))
							{
								for (int i=0; i<listaRisultati.size(); i++)
								{

									if (listaRisultati.get(i).getFornitore().trim().equals(rs.getString("nom_fornitore").trim()) && listaRisultati.get(i).getEsercizio().equals(rs.getString("esercizio")) && listaRisultati.get(i).getCapitolo().equals(rs.getString("capitolo")) && listaRisultati.get(i).getImpegno().equals(rs.getString("cod_mat")))
									{
										listaRisultati.get(i).setAcquisito(impACQ);
										listaRisultati.get(i).setValoreInventariale(impVALINV);
										break;
									}
								}

							}
							else if (criteriRiparto.getTipoReport().equals("4"))
							{
								for (int i=0; i<listaRisultati.size(); i++)
								{

									if (listaRisultati.get(i).getIDOrd()==rs.getInt("id_ordine"))
									{
										listaRisultati.get(i).setAcquisito(impACQ);
										listaRisultati.get(i).setValoreInventariale(impVALINV);
										break;
									}
								}

							}

							else
							{
								for (int i=0; i<listaRisultati.size(); i++)
								{

									//if (listaRisultati.get(i).getAnnoOrdine()==rs.getInt("anno_ord") && listaRisultati.get(i).getTipologia().equals(rs.getString("cod_tip_ord")))
									if (listaRisultati.get(i).getAnnoOrdine()==rs.getInt("anno_ord") && listaRisultati.get(i).getTipologia().equals(rs.getString("tipologia")))
									{
										listaRisultati.get(i).setAcquisito(impACQ);
										listaRisultati.get(i).setValoreInventariale(impVALINV);
										break;
									}
								}

							}

							totACQ=totACQ + impACQ;
							totVALINV=totVALINV + impVALINV;
							numRiga=numRiga+1;
						}

						rs.close();
						pstmt.close();

				}
				connection.close();

				// per le stampe
				if (listaRisultati!=null && listaRisultati.size()>0)
				{
					if (criteriRiparto.getDataOrdineDa() != null)
						listaRisultati.get(0).setDataOrdineDa(criteriRiparto.getDataOrdineDa());
					if (criteriRiparto.getDataOrdineA() != null)
						listaRisultati.get(0).setDataOrdineA(criteriRiparto.getDataOrdineA());
					if (criteriRiparto.getCodBibl() != null)
						listaRisultati.get(0).setCodBibl(criteriRiparto.getCodBibl());
					if (criteriRiparto.getDenoBibl() != null)
						listaRisultati.get(0).setDenoBibl(criteriRiparto.getDenoBibl());
					if (criteriRiparto.getCodPolo() != null)
						listaRisultati.get(0).setCodPolo(criteriRiparto.getCodPolo());
					if  (criteriRiparto.getIdBil()>0)
					{
						listaRisultati.get(0).setEsercizioDescr(criteriRiparto.getEsercizio());
						listaRisultati.get(0).setCapitoloDescr(criteriRiparto.getCapitolo());
					}
					if (criteriRiparto.getEsercizio()!=null && criteriRiparto.getEsercizio().length()!=0 )
					{
						listaRisultati.get(0).setEsercizioDescr(criteriRiparto.getEsercizio());
					}
					if (criteriRiparto.getCapitolo()!=null && criteriRiparto.getCapitolo().length()!=0 )
					{
						listaRisultati.get(0).setCapitoloDescr(criteriRiparto.getCapitolo());
					}
					if (criteriRiparto.getImp()!=null && criteriRiparto.getImp().length()==1 )
					{
						listaRisultati.get(0).setImpegnoDescr(criteriRiparto.getImp());
					}
					if (criteriRiparto.getNaturaOrdine()!=null && criteriRiparto.getNaturaOrdine().length()!=0 )
					{
						listaRisultati.get(0).setNaturaOrdine(criteriRiparto.getNaturaOrdine());
					}
					if (criteriRiparto.getTipoMaterialeInv()!=null && criteriRiparto.getTipoMaterialeInv().length()!=0 )
					{
						listaRisultati.get(0).setTipoMaterialeInvDescr(criteriRiparto.getTipoMaterialeInv());
					}
					if (criteriRiparto.getSupporto()!=null && criteriRiparto.getSupporto().length()!=0 )
					{
						listaRisultati.get(0).setSupportoDescr(criteriRiparto.getSupporto());
					}
					if (criteriRiparto.getTipoRecord()!=null && criteriRiparto.getTipoRecord().length()!=0 )
					{
						listaRisultati.get(0).setTipoRecordDescr(criteriRiparto.getTipoRecord());
					}

					if  (criteriRiparto.getIdForn()>0)
					{
						listaRisultati.get(0).setFornitoreDescr(criteriRiparto.getFornitore());
					}
					if  (criteriRiparto.getIdSez()>0)
					{
						listaRisultati.get(0).setSezione(criteriRiparto.getSezione().trim());
						listaRisultati.get(0).setSezioneDescr(criteriRiparto.getSezione().trim());
					}
					if  (criteriRiparto.getTipoReport().equals("2") && criteriRiparto.getIdListaSezioni()!=null && criteriRiparto.getIdListaSezioni().length()!=0)
					{
						listaRisultati.get(0).setSezioneDescr(criteriRiparto.getIdListaSezioni());
					}

					if (criteriRiparto.getAnno()!=null && criteriRiparto.getAnno().trim().length()!=0)
					{
						listaRisultati.get(0).setAnnoOrdineDescr(criteriRiparto.getAnno());
					}
					if (criteriRiparto.getOrdNOinv())
					{
						listaRisultati.get(0).setOrdNOinv(criteriRiparto.getOrdNOinv());
					}
					if (criteriRiparto.getLingua()!=null && criteriRiparto.getLingua().trim().length()!=0)
					{
						listaRisultati.get(0).setLinguaDescr(criteriRiparto.getLinguaDescr());
					}
					if (criteriRiparto.getPaese()!=null && criteriRiparto.getPaese().trim().length()!=0)
					{
						listaRisultati.get(0).setPaeseDescr(criteriRiparto.getPaeseDescr());
					}
					if (criteriRiparto.getRangeDewey()!=null && criteriRiparto.getRangeDewey().length()>0)
					{
						if (criteriRiparto.getRangeDewey().equals("0"))
						{
							listaRisultati.get(0).setRangeDewey("000-099");
						}
						if (criteriRiparto.getRangeDewey().equals("1"))
						{
							listaRisultati.get(0).setRangeDewey("100-199");
						}
						if (criteriRiparto.getRangeDewey().equals("2"))
						{
							listaRisultati.get(0).setRangeDewey("200-299");
						}
						if (criteriRiparto.getRangeDewey().equals("3"))
						{
							listaRisultati.get(0).setRangeDewey("300-399");
						}
						if (criteriRiparto.getRangeDewey().equals("4"))
						{
							listaRisultati.get(0).setRangeDewey("400-499");
						}
						if (criteriRiparto.getRangeDewey().equals("5"))
						{
							listaRisultati.get(0).setRangeDewey("500-599");
						}
						if (criteriRiparto.getRangeDewey().equals("6"))
						{
							listaRisultati.get(0).setRangeDewey("600-699");
						}
						if (criteriRiparto.getRangeDewey().equals("7"))
						{
							listaRisultati.get(0).setRangeDewey("700-799");
						}
						if (criteriRiparto.getRangeDewey().equals("8"))
						{
							listaRisultati.get(0).setRangeDewey("800-899");
						}
						if (criteriRiparto.getRangeDewey().equals("9"))
						{
							listaRisultati.get(0).setRangeDewey("900-999");
						}
					}
				}


		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

		return listaRisultati;
	}

	public List<StatisticheVO>  statisticheTempi (ListaSuppSpeseVO criteriRiparto) throws DataException, ApplicationException , ValidationException
	{
		List<StatisticheVO> listaRisultati = null;

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
				connection = getConnection();

				String sql=" SELECT  count(app.*) as conta, app.cd_tip_ord, app.mesi, app.giorni,app.ore ";
				sql +=" FROM ( ";

				//N.B. cambiato il confronto fra inv.ts_ins-ord.ts_ins  con inv.ts_ins-ord.data_agg ed aggiunta la condizione su stampato vedi mail di contardi del 23.09.09
				if (criteriRiparto.getTipoReport()!=null && criteriRiparto.getTipoReport().equals("10")) // tempi di accessionamento
				{

					sql +=" SELECT app1.cd_tip_ord, app1.invTsIns, app1.ordTsStampa, cast(age(app1.invTsIns,app1.ordTsStampa ) AS TEXT) AS trascorso, extract (month from age(app1.invTsIns,app1.ordTsStampa )) as mesi, extract (days from age(app1.invTsIns,app1.ordTsStampa)) as giorni, extract (hours from age(app1.invTsIns,app1.ordTsStampa )) as ore ";
					sql +=" FROM ( ";
					sql +=" SELECT  min (inv.ts_ins) as invTsIns ,   inv.cd_bib_ord,  inv.cd_tip_ord,  inv.anno_ord,  inv.cd_ord,  inv.cd_fornitore , ord.data_agg as ordTsStampa ";
					// se si vuole aggregare la tipologia A con V  occorre sostituire inv.cd_tip_ord  con (SELECT CASE WHEN ( inv.cd_tip_ord='A'  OR  inv.cd_tip_ord='V') THEN 'Acquisto' ELSE  'Legatura' END) as tipologia
					// poi occorre sostituire ovunque rs.getString("cd_tip_ord") con rs.getString("tipologia")
				}
				else // tempi di lavorazione dei documenti
				{
					sql +=" SELECT app1.cd_tip_ord, app1.invTsIns, app1.invTsPrimaColl, cast(age(app1.invTsPrimaColl,app1.invTsIns ) AS TEXT) AS trascorso, extract (month from age(app1.invTsPrimaColl,app1.invTsIns )) as mesi, extract (days from age(app1.invTsPrimaColl,app1.invTsIns)) as giorni, extract (hours from age(app1.invTsPrimaColl,app1.invTsIns )) as ore   ";
					sql +=" FROM ( ";
					sql +=" SELECT  min (inv.ts_ins) as invTsIns ,   inv.cd_bib_ord,  inv.cd_tip_ord,  inv.anno_ord,  inv.cd_ord,  inv.cd_fornitore , inv.ts_ins_prima_coll as invTsPrimaColl  ";
					// se si vuole aggregare la tipologia A con V  occorre sostituire inv.cd_tip_ord  con (SELECT CASE WHEN ( inv.cd_tip_ord='A'  OR  inv.cd_tip_ord='V') THEN 'Acquisto' ELSE  'Legatura' END) as tipologia
					// poi occorre sostituire ovunque rs.getString("cd_tip_ord") con rs.getString("tipologia")

				}

				sql +=" FROM tba_ordini ord ";
				sql +=" join tbc_inventario inv on  ";
				sql +="      ord.cd_bib=inv.cd_bib_ord ";
				sql +=" and  ord.cod_tip_ord=inv.cd_tip_ord ";
				sql +=" and  ord.anno_ord=inv.anno_ord ";
				sql +=" and  ord.cod_ord=inv.cd_ord ";
				sql +=" and  ord.cod_fornitore=inv.cd_fornitore ";
				sql +=" and  inv.fl_canc<>'S' ";
				sql +=" where ord.fl_canc<>'S'"; // ESCLUDO I CANCELLATI
				sql +=" and ord.stato_ordine <>'N'"; // ESCLUDO GLI ANNULLATI


				if (criteriRiparto.getCodBibl()!=null && criteriRiparto.getCodBibl().length()>0)
				{
					sql +=" and ord.cd_bib='"+ criteriRiparto.getCodBibl() +"'";
				}
				if (criteriRiparto.getCodPolo()!=null && criteriRiparto.getCodPolo().length()>0)
				{
					sql +=" and ord.cd_polo='"+ criteriRiparto.getCodPolo() +"'";
				}
				if (criteriRiparto.getAnno()!=null && criteriRiparto.getAnno().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" ord.anno_ord="  +  criteriRiparto.getAnno().trim() ;
				}
				if (criteriRiparto.getTipoOrdine()!=null && criteriRiparto.getTipoOrdine().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" ord.cod_tip_ord='"  +  criteriRiparto.getTipoOrdine().trim() +"'";
				}
				if (criteriRiparto.getTipoOrdineArr()!=null && criteriRiparto.getTipoOrdineArr().length!=0 )
				{
					Boolean aggiungiSQL=false;
					String sqla="";
					sqla=sqla + " ( ";
					//sql +=" ord.stato_ordine='" + ricercaOrdini.getStatoOrdine() +"'";
					for (int index = 0; index < criteriRiparto.getTipoOrdineArr().length; index++) {
						String so = criteriRiparto.getTipoOrdineArr()[index] ;
						if (so!=null && so.length()!=0)
						{
					         if (!sqla.equals(" ( ")) {
					        	 sqla=sqla + " OR ";
					         }
					        sqla=sqla + " ord.cod_tip_ord='" + so +"'";
							aggiungiSQL=true;
						}
					}
					sqla=sqla + " ) ";
					if (aggiungiSQL)
					{
						sql=this.struttura(sql);
						sql += sqla;
					}
				}
				sql=this.struttura(sql);
				sql +=" ord.stampato=true "; // solo ordini stampati; // vedi mail di contardi del 23.09.09
				sql=this.struttura(sql);
				sql +=" ord.cod_tip_ord<>'D'"; // ESCLUDO il tipo dono tck 2565;
				sql=this.struttura(sql);
				sql +=" ord.cod_tip_ord<>'C'"; // ESCLUDO il tipo cambio tck 2565;
				sql=this.struttura(sql);
				sql +=" ord.cod_tip_ord<>'L'"; // ESCLUDO il tipo cambio tck 2565;
				sql=this.struttura(sql);
				sql +=" ord.cod_tip_ord<>'R'"; // ESCLUDO il tipo rilegatura tck 0004377 - rp;
				sql=this.struttura(sql);
				sql +=" ord.cod_tip_ord<>'V'"; // ESCLUDO il tipo visione trattenuta tck 0004377 - rp;

				if (criteriRiparto.getDataOrdineDa()!=null && criteriRiparto.getDataOrdineDa().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" ord.data_ord >= TO_DATE ('" +  criteriRiparto.getDataOrdineDa().trim() + "','dd/MM/yyyy')";
				}
				if (criteriRiparto.getDataOrdineA()!=null && criteriRiparto.getDataOrdineA().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" ord.data_ord <= TO_DATE ('" +  criteriRiparto.getDataOrdineA().trim() + "','dd/MM/yyyy')";
				}
				sql +=" group by  inv.cd_bib_ord,  inv.cd_tip_ord,  inv.anno_ord,  inv.cd_ord,  inv.cd_fornitore ";

				if (criteriRiparto.getTipoReport()!=null && criteriRiparto.getTipoReport().equals("10")) // tempi di accessionamento
				{
					sql +=" , ord.data_agg  ";
				}
				else // tempi di lavorazione dei documenti
				{
					sql +=" , inv.ts_ins_prima_coll  ";
				}

				sql +=" ) AS app1 ";

			    sql +="   order by  app1.cd_tip_ord, mesi, giorni, ore ";

				sql +=" ) AS app ";
				sql=sql  + " group by  app.cd_tip_ord, app.mesi, app.giorni, app.ore ";
				sql=sql  + " order by app.cd_tip_ord, app.mesi, app.giorni, app.ore ";

				pstmt = connection.prepareStatement(sql);
				rs = pstmt.executeQuery();
				double imp=0.00;
				double tot=0.00;

				String natura="";
				String continuativo="";
				listaRisultati=new ArrayList<StatisticheVO>();
				int numRiga=0;
				String chiaveCiclo="";
				String chiaveCicloOld="";
				int totMeno7=0;
				int totIntermedio=0;
				int tot1521=0;
				int tot2228=0;
				int totPiu14=0;
				int totParziale=0;
				double media=0.00;
				int totGiorniXRiga=0;
				int totGiorniXMedia=0;

				StatisticheVO eleRig=new StatisticheVO();
				Boolean rotturaChiave=false;
				while (rs.next()) {

					if (numRiga==0)
					{
						chiaveCicloOld=rs.getString("cd_tip_ord");
					}
					chiaveCiclo=rs.getString("cd_tip_ord");

					if (chiaveCiclo.equals(chiaveCicloOld))
					{
						if (rs.getString("mesi")!=null && rs.getString("giorni")!=null &&  rs.getString("mesi").equals("0") && Integer.parseInt(rs.getString("giorni"))<7 )
						{
							totMeno7=totMeno7 + rs.getInt("conta");
						}
						if (rs.getString("mesi")!=null && rs.getString("giorni")!=null && rs.getString("mesi").equals("0") && Integer.parseInt(rs.getString("giorni"))>=7 && Integer.parseInt(rs.getString("giorni"))<=14 )
						{
							totIntermedio=totIntermedio+rs.getInt("conta");
						}
						if (rs.getString("mesi")!=null && rs.getString("giorni")!=null && rs.getString("mesi").equals("0") && (Integer.parseInt(rs.getString("giorni"))>=15 && Integer.parseInt(rs.getString("giorni"))<=21))
						{
							tot1521=tot1521+rs.getInt("conta");
						}
						if (rs.getString("mesi")!=null && rs.getString("giorni")!=null && rs.getString("mesi").equals("0") && (Integer.parseInt(rs.getString("giorni"))>=22 && Integer.parseInt(rs.getString("giorni"))<=28))
						{
							tot2228=tot2228+rs.getInt("conta");
						}
						if (rs.getString("mesi")!=null && rs.getString("giorni")!=null && ((rs.getString("mesi").equals("0") && Integer.parseInt(rs.getString("giorni"))>28) || Integer.parseInt(rs.getString("mesi"))>0))
						{
							totPiu14=totPiu14+rs.getInt("conta");
						}
						totGiorniXRiga=0;
						if (rs.getString("mesi")!=null && rs.getString("giorni")!=null )
						{
							totGiorniXRiga=rs.getInt("conta")*(Integer.parseInt(rs.getString("mesi"))*30 +Integer.parseInt(rs.getString("giorni")));
						}

						totGiorniXMedia=totGiorniXMedia+totGiorniXRiga;
						totParziale=totParziale+ rs.getInt("conta");
						eleRig.setDenoBibl(criteriRiparto.getDenoBibl());
						eleRig.setDataOrdineA(criteriRiparto.getDataOrdineA());
						eleRig.setDataOrdineDa(criteriRiparto.getDataOrdineDa());
						eleRig.setAnnoOrdineStr(criteriRiparto.getAnno());

						eleRig.setTipoOrdine(rs.getString("cd_tip_ord"));
						eleRig.setTotaleOrdXTipo(totParziale);
						eleRig.setTotCondizione1(totMeno7);
						eleRig.setTotCondizione2(totIntermedio);
						eleRig.setTotCondizione3(tot1521);
						eleRig.setTotCondizione4(tot2228);
						eleRig.setTotCondizione5(totPiu14);

						eleRig.setMedia(totGiorniXMedia/totParziale);
						rotturaChiave=false;
					}
					else
					{
						rotturaChiave=true;
						// riempimento a rottura chiave
						listaRisultati.add(eleRig); // precedente elemento di riga completato
						eleRig=new StatisticheVO();
						totParziale=0;

						totMeno7=0;
						totIntermedio=0;
						tot1521=0;
						tot2228=0;
						totPiu14=0;

						totGiorniXMedia=0;

						chiaveCicloOld=rs.getString("cd_tip_ord");

						if (chiaveCiclo.equals(chiaveCicloOld))
						{
							if (rs.getString("mesi")!=null && rs.getString("giorni")!=null && rs.getString("mesi").equals("0") && Integer.parseInt(rs.getString("giorni"))<7 )
							{
								totMeno7=totMeno7+rs.getInt("conta");
							}
							if (rs.getString("mesi")!=null && rs.getString("giorni")!=null && rs.getString("mesi").equals("0") && Integer.parseInt(rs.getString("giorni"))>=7 && Integer.parseInt(rs.getString("giorni"))<=14 )
							{
								totIntermedio=totIntermedio+rs.getInt("conta");
							}
							if (rs.getString("mesi")!=null && rs.getString("giorni")!=null && rs.getString("mesi").equals("0") && Integer.parseInt(rs.getString("giorni"))>=15 && Integer.parseInt(rs.getString("giorni"))<=21 )
							{
								tot1521=tot1521+rs.getInt("conta");
							}
							if (rs.getString("mesi")!=null && rs.getString("giorni")!=null && rs.getString("mesi").equals("0") && Integer.parseInt(rs.getString("giorni"))>=22 && Integer.parseInt(rs.getString("giorni"))<=28 )
							{
								tot2228=tot2228+rs.getInt("conta");
							}

							if (rs.getString("mesi")!=null && rs.getString("giorni")!=null && ((rs.getString("mesi").equals("0") && Integer.parseInt(rs.getString("giorni"))>28) || Integer.parseInt(rs.getString("mesi"))>0))
							{
								totPiu14=totPiu14+rs.getInt("conta");
							}

						}



						totGiorniXRiga=0;
						if (rs.getString("mesi")!=null && rs.getString("giorni")!=null )
						{
							totGiorniXRiga=rs.getInt("conta")*(Integer.parseInt(rs.getString("mesi"))*30 +Integer.parseInt(rs.getString("giorni")));
						}

						totGiorniXMedia=totGiorniXMedia+totGiorniXRiga;

						totParziale=totParziale+ rs.getInt("conta");

						eleRig.setDenoBibl(criteriRiparto.getDenoBibl());
						eleRig.setDataOrdineA(criteriRiparto.getDataOrdineA());
						eleRig.setDataOrdineDa(criteriRiparto.getDataOrdineDa());
						eleRig.setAnnoOrdineStr(criteriRiparto.getAnno());

						eleRig.setTipoOrdine(rs.getString("cd_tip_ord"));
						eleRig.setTotaleOrdXTipo(totParziale);
						eleRig.setTotCondizione1(totMeno7);
						eleRig.setTotCondizione2(totIntermedio);
						eleRig.setTotCondizione3(tot1521);
						eleRig.setTotCondizione4(tot2228);
						eleRig.setTotCondizione5(totPiu14);

						eleRig.setMedia(totGiorniXMedia/totParziale);

					}

					numRiga=numRiga+1;

				}
				//n. b aggiungere la condizione
				if (numRiga>0)
				{
					listaRisultati.add(eleRig); // scarico ultimo elemento di riga completato
				}
				rs.close();
				pstmt.close();

				connection.close();
				if (listaRisultati!=null && listaRisultati.size()>0)
				{
					listaRisultati.get(0).setIntestazione(" lavorazione dei documenti ");
					if (criteriRiparto.getTipoReport()!=null && criteriRiparto.getTipoReport().equals("10")) // tempi di accessionamento
					{
						listaRisultati.get(0).setIntestazione(" accessionamento degli ordini ");
					}

				}


		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

		return listaRisultati;
	}


	public boolean  inserisciSezione(SezioneVO sezione) throws DataException, ApplicationException , ValidationException
	{
		// nextval('seq_tba_sez_acquis_bibliografiche'::regclass)
		Validazione.ValidaSezioneVO (sezione);
		boolean valRitorno=false;
    	int motivo=0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmtCod= null;
		ResultSet rsSubCod= null;

		boolean valRitornoINS=false;
		boolean esistenzaSezione=false;

		try {
			// CONTROLLI PREVENTIVI
			connection = getConnection();
			String sql0="select * ";
			sql0=sql0 + " from  tba_sez_acquis_bibliografiche sez ";
			sql0=this.struttura(sql0);
			sql0=sql0 + " sez.fl_canc<>'S'";

			if (sezione.getCodPolo() !=null &&  sezione.getCodPolo().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0  + " sez.cd_polo='" + sezione.getCodPolo() +"'";
			}

			if (sezione.getCodBibl() !=null &&  sezione.getCodBibl().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " sez.cd_bib='" + sezione.getCodBibl() +"'";
			}

			if (sezione.getCodiceSezione()!=null && sezione.getCodiceSezione().trim().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " upper(sez.cod_sezione)='" + sezione.getCodiceSezione().trim().toUpperCase() +"'";
			}

			if (sezione.getDescrizioneSezione()!=null && sezione.getDescrizioneSezione().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " upper(sez.nome) like '" + sezione.getDescrizioneSezione().trim().toUpperCase() +"%'";;
			}
			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			Timestamp ts = DaoManager.now();
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;
				}
			if (numRighe>0)
			{
				esistenzaSezione=true; // record forse già esistente quindi non inseribile

				throw new ValidationException("cambierroreInserimentoEsistenzaRecord",
						ValidationExceptionCodici.cambierroreInserimentoEsistenzaRecord);
			}
			pstmt.close();
			if (!esistenzaSezione)
			{
				// INSERIMENTO

				String sqlSub="insert into tba_sez_acquis_bibliografiche  " ;
				sqlSub=sqlSub + "( cd_polo, cd_bib, cod_sezione, nome, note, somma_disp, budget, anno_val, ute_ins, ts_ins, ute_var, ts_var, fl_canc,data_val) ";
				//sqlSub=sqlSub + "(id_sez_acquis_bibliografiche, cd_polo, cd_bib, cod_sezione, nome, note, somma_disp, budget, anno_val, ute_ins, ts_ins, ute_var, ts_var, fl_canc) ";
				sqlSub=sqlSub + " values (  " ;  // id_sez_acquis_bibliografiche
				//sqlSub=sqlSub + " nextval('seq_tba_sez_acquis_bibliografiche') " ;  // id_sez_acquis_bibliografiche
				sqlSub=sqlSub + "'" + sezione.getCodPolo()+ "'" ;  // cd_polo
				sqlSub=sqlSub + ",'" + sezione.getCodBibl() + "'" ;  // cd_bib
				sqlSub=sqlSub + ",'" +  sezione.getCodiceSezione().trim().toUpperCase() + "'" ;  // cod_sezione
				sqlSub=sqlSub + ",'" +  sezione.getDescrizioneSezione().trim().toUpperCase() + "'" ;  // nome
				sqlSub=sqlSub + ",'" +  sezione.getNoteSezione().replace("'","''") + "'" ;  // note
				// CALCOLO DISPONIBILITA
				sqlSub=sqlSub + "," +  sezione.getSommaDispSezione() ;  // somma_disp
				sqlSub=sqlSub + "," +  sezione.getBudgetSezione() ;  // budget
				sqlSub=sqlSub + ",'" +  sezione.getAnnoValiditaSezione() + "'" ;  // anno_val
				sqlSub=sqlSub + ",'" + sezione.getUtente() + "'" ;   // ute_ins
				sqlSub=sqlSub + ",'" + ts + "'" ;   // ts_ins
				sqlSub=sqlSub + ",'" + sezione.getUtente() + "'" ;   // ute_var
				sqlSub=sqlSub + ",'" + ts + "'";   // ts_var
				sqlSub=sqlSub + ",'N'";   // fl_canc
				// caso chiusura
				sqlSub=sqlSub + ",TO_DATE('"+ sezione.getDataVal().trim()+"','dd/MM/yyyy') "  ;  // data_val
				sqlSub=sqlSub + ")" ;
				pstmt = connection.prepareStatement(sqlSub);
				log.debug("Debug: inserimento sezione");
				log.debug("Debug: " + sqlSub);

				int intRetINS = 0;
				intRetINS = pstmt.executeUpdate();
				pstmt.close();
				if (intRetINS==1){
					valRitornoINS=true;
					// deduzione del codice appena inserito per successivi insert
					String sqlCodice="select id_sez_acquis_bibliografiche from tba_sez_acquis_bibliografiche ";
					//sqlCodice=sqlCodice + " where ts_ins='" +ts+ "'";
					sqlCodice=sqlCodice + " where ute_ins='" + sezione.getUtente()+ "'";
					sqlCodice=sqlCodice + " order by ts_ins desc limit 1";

					pstmtCod = connection.prepareStatement(sqlCodice);
					rsSubCod = pstmtCod.executeQuery();
					if (rsSubCod.next()) {
						sezione.setIdSezione(rsSubCod.getInt("id_sez_acquis_bibliografiche"));
					}
					rsSubCod.close();
					pstmtCod.close();

				}
			}
			rs.close();
			connection.close();

			// impostazione del codice di ritorno finale
			if (valRitornoINS)
			{
				valRitorno=true;
			}
		}catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

        return valRitorno;
	}

	public boolean  modificaSezione(SezioneVO sezione) throws DataException, ApplicationException , ValidationException
	{
		Validazione.ValidaSezioneVO(sezione);
		boolean valRitorno = false;
		int motivo = 0;
		Connection c = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean valRitornoUPD = false;
		boolean esistenzaSezione = false;
		boolean valRitornoINS = false;
		PreparedStatement pstmtINS = null;

		try {
			log.debug("modificaSezione()");
			c = getConnection();

			// CONTROLLI PREVENTIVI
			String sql0="select * ";
			sql0=sql0 + " from  tba_sez_acquis_bibliografiche sez ";
			sql0=this.struttura(sql0);
			sql0=sql0 + " sez.fl_canc<>'S'";
			if (sezione.getCodPolo() !=null &&  sezione.getCodPolo().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0  + " sez.cd_polo='" + sezione.getCodPolo() +"'";
			}

			if (sezione.getCodBibl() !=null &&  sezione.getCodBibl().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " sez.cd_bib='" + sezione.getCodBibl() +"'";
			}

			if (sezione.getCodiceSezione()!=null && sezione.getCodiceSezione().trim().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " upper(sez.cod_sezione)='" + sezione.getCodiceSezione().trim().toUpperCase() +"'";
			}
			sql0 += " FOR UPDATE";

			pstmt = c.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			int numRighe=0;
			List<StrutturaCombo> righeOggetto=new ArrayList();
			Timestamp ts = DaoManager.now();
			while (rs.next()) {
				numRighe=numRighe+1;
			}
			rs.close();
			pstmt.close();
			if (numRighe >= 1)
			{
				if (numRighe == 1)
				{
					esistenzaSezione=true;
				}
			}
			if (!esistenzaSezione )
				throw new ValidationException("cambierroreModificaRecordNonUnivoco",
						ValidationExceptionCodici.cambierroreModificaRecordNonUnivoco);

			Locale locale=Locale.getDefault(); // aggiornare con quella locale se necessario
			// 09.01.09  calcolo disponibilità spostato su db
			CalcoliVO calcoli = new CalcoliVO();
			try	{
				//almaviva5_20141027 #5661
				calcoli = this.calcola(sezione.getCodPolo(),
					sezione.getCodBibl(), sezione.getIdSezione(),
					sezione, 0, null, 0, null, null, null, locale, Calcola.FLAG_ORDINATO);
			} catch (Exception e) {
				// l'errore capita in questo punto
				log.error("", e);
			}
			double disp = sezione.getBudgetSezione();
			//String dispStr=Pulisci.VisualizzaImporto(listaSezioni.get(i).getBudgetSezione());
			if (calcoli.getOrdinato() != 0)
				disp -= calcoli.getOrdinato();

			sezione.setSommaDispSezione(disp);
			// caso variazione di budget (registrare il record su storico)
			if (sezione.getBudgetSezione()>0 && sezione.getBudgetLetto()>0 && sezione.getBudgetSezione()!= sezione.getBudgetLetto())
			{
				String sqlSubINS="insert into tra_sez_acq_storico  " ;
				sqlSubINS=sqlSubINS + " values (  " ;
				sqlSubINS=sqlSubINS +  sezione.getIdSezione() ;  // id_sez_acquis_bibliografiche
				sqlSubINS=sqlSubINS + ",'" + ts + "'" ;   // ts_ins
				sqlSubINS=sqlSubINS + ", (SELECT CURRENT_DATE )" ;  // dataVar
				sqlSubINS=sqlSubINS + "," +  sezione.getBudgetLetto() ;  // budgetOld
				sqlSubINS=sqlSubINS + "," + sezione.getImportoDelta();  // importoDiff
				sqlSubINS=sqlSubINS + ",'" + sezione.getUtente() + "'" ;   // ute_ins
				sqlSubINS=sqlSubINS + ",'" + sezione.getUtente() + "'" ;   // ute_var
				sqlSubINS=sqlSubINS + ",'" + ts + "'";   // ts_var
				sqlSubINS=sqlSubINS + ",'N'";   // fl_canc
				sqlSubINS=sqlSubINS + ")" ;
				pstmtINS = c.prepareStatement(sqlSubINS);
				int intRetINS = 0;
				intRetINS = pstmtINS.executeUpdate();
				pstmt.close();
				if (intRetINS==1){
					valRitornoINS=true;
				}
			}

			//update sezione
			String sqlUDP="update tba_sez_acquis_bibliografiche set " ;
			sqlUDP= sqlUDP + " ts_var = '" +  ts  + "'" ;  // ex data_agg
			sqlUDP= sqlUDP + ", ute_var = '" +  sezione.getUtente()  + "'" ;  // ute_var

			if (sezione.getDescrizioneSezione()!=null && sezione.getDescrizioneSezione().length()!=0)
			{
				sqlUDP += ", nome='" + sezione.getDescrizioneSezione().trim().toUpperCase() +"'";
			}

			if (sezione.getNoteSezione()!=null && sezione.getNoteSezione().length()!=0)
			{
				sqlUDP += ", note='" + sezione.getNoteSezione().replace("'","''") +"'";
			}
			if (String.valueOf(sezione.getBudgetSezione()).length()!= 0)
			{
				sqlUDP += ", budget=" + sezione.getBudgetSezione();
				// deve essere aggiornata la somma disponibile DA CALCOLARE
				sqlUDP += ", somma_disp=" + sezione.getSommaDispSezione();
			}
			if (sezione.getAnnoValiditaSezione()!=null && sezione.getAnnoValiditaSezione().length()!=0)
			{
				sqlUDP += ", anno_val=" + sezione.getAnnoValiditaSezione() ;
			}
			// caso chiusura
			if (sezione.getDataVal()!=null && sezione.getDataVal().trim().length()>0 )
			{
				sqlUDP=sqlUDP  + ", data_val=TO_DATE('"+ sezione.getDataVal().trim()+"','dd/MM/yyyy') "  ;  // data_val
			}

			sqlUDP=this.struttura(sqlUDP);
			sqlUDP=sqlUDP + " fl_canc<>'S'";

			if (sezione.getCodPolo() !=null &&  sezione.getCodPolo().length()!=0)
			{
				sqlUDP=this.struttura(sqlUDP);
				sqlUDP=sqlUDP + " cd_polo='" + sezione.getCodPolo() +"'";
			}

			if (sezione.getCodBibl() !=null &&  sezione.getCodBibl().length()!=0)
			{
				sqlUDP=this.struttura(sqlUDP);
				sqlUDP += " cd_bib='" + sezione.getCodBibl() +"'";
			}

			if (sezione.getCodiceSezione()!=null && sezione.getCodiceSezione().trim().length()!=0)
			{
				sqlUDP=this.struttura(sqlUDP);
				sqlUDP += " cod_sezione='" + sezione.getCodiceSezione() +"'";
			}

			if (sezione.getDataUpd()!=null )
			{
				sqlUDP=this.struttura(sqlUDP);
				sqlUDP=sqlUDP + " ts_var='" + sezione.getDataUpd() + "'" ;
			}

			pstmt = c.prepareStatement(sqlUDP);
			log.debug("Debug: modifica sezione");
			log.debug("Debug: " + sqlUDP);

			int intRetUDP = 0;
			intRetUDP = pstmt.executeUpdate();
			pstmt.close();
			// fine estrazione codice
			if (intRetUDP==1){
				valRitornoUPD=true;
			}
			else
			{
				throw new ValidationException("operazioneInConcorrenza",
						ValidationExceptionCodici.operazioneInConcorrenza);

			}


			c.close();
			// impostazione del codice di ritorno finale
			if (valRitornoUPD)
			{
				valRitorno=true;
			}
		}catch (ValidationException e) {
			throw e;

		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(c);
		}

        return valRitorno;
	}

	public boolean  cancellaSezione(SezioneVO sezione) throws DataException, ApplicationException, ValidationException
	{
		//Validazione.ValidaSezioneVO (sezione);
		int valRitornoInt=0;
    	int motivo=0;
    	OrdiniVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean valRitorno=false;
		boolean valRitornoPrep=false;
		Timestamp ts = DaoManager.now();

		try {
			// CONTROLLI PREVENTIVI

			connection = getConnection();
			// Per essere cancellate non devono avere legami a ordini (in qualsiasi stato) o a suggerimenti del bibliotecario che non siano rifiutati.
			String sql0="select * ";
			sql0=sql0 + " from  tba_sez_acquis_bibliografiche sez ";

			sql0=this.struttura(sql0);
			sql0=sql0 + " sez.fl_canc<>'S'";
			if (sezione.getCodPolo() !=null &&  sezione.getCodPolo().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0= sql0+ " sez.cd_polo='" + sezione.getCodPolo() +"'";
			}

			if (sezione.getCodBibl() !=null &&  sezione.getCodBibl().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " sez.cd_bib='" + sezione.getCodBibl() +"'";
			}

			if (sezione.getCodiceSezione()!=null && sezione.getCodiceSezione().trim().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " upper(sez.cod_sezione)='" + sezione.getCodiceSezione().trim().toUpperCase() +"'";
			}


			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			valRitornoPrep=true; // se la esegue c'è un resultset
			// numero di righe del resultset
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;

			} //
			rs.close();

			if (numRighe > 1)
			{
				// n.b anche se esiste un ordine per lo stesso titolo si possono crearne altri
				motivo=2; // record non univoco
				throw new ValidationException("cambierroreModificaRecordNonUnivoco",
						ValidationExceptionCodici.cambierroreModificaRecordNonUnivoco);
			}

			if (numRighe==1)
			{

				String sql="update tba_sez_acquis_bibliografiche set " ;

				sql += " ts_var = '" +  ts  + "'" ;  // ex data_agg
				sql += ", ute_var = '" +  sezione.getUtente()  + "'" ;  // ute_var
				sql += ", fl_canc = 'S'" ;  // fl_canc
				sql=this.struttura(sql);
				sql +=" fl_canc<>'S'";

				if (sezione.getCodPolo() !=null &&  sezione.getCodPolo().length()!=0)
				{
					sql=this.struttura(sql);
					sql= sql+ " cd_polo='" + sezione.getCodPolo() +"'";
				}

				if (sezione.getCodBibl() !=null &&  sezione.getCodBibl().length()!=0)
				{
					sql=this.struttura(sql);
					sql= sql+ " cd_bib='" + sezione.getCodBibl() +"'";
				}

				if (sezione.getCodiceSezione()!=null && sezione.getCodiceSezione().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql= sql+ " cod_sezione='" + sezione.getCodiceSezione() +"'";
				}

				pstmt = connection.prepareStatement(sql);
				valRitornoInt = pstmt.executeUpdate();
				if (valRitornoInt==1){
					valRitorno=true;
				}else{
					valRitorno=false;
				}
			}
			connection.close();
		}catch (ValidationException e) {
			throw e;
		}
		 catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

     return valRitorno;
	}

	public List<BuoniOrdineVO> getRicercaListaBuoniOrd(ListaSuppBuoniOrdineVO ricercaBuoniOrd) throws ResourceNotFoundException, ApplicationException, ValidationException
	{
		Validazione.ValidaListaSuppBuoniOrdineVO (this, ricercaBuoniOrd);
		String ticket="";
		ticket=ricercaBuoniOrd.getTicket();
		List<BuoniOrdineVO> listaBuoniOrd = new ArrayList<BuoniOrdineVO>();

		int ret = 0;
        // execute the search here
			BuoniOrdineVO rec = null;
    		OrdiniVO recDett = null;
			Connection connection = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			PreparedStatement pstmt0 = null;
			ResultSet rs0 = null;
			PreparedStatement pstmtCount = null;
			ResultSet rsCount = null;
			try {
				// contiene i criteri di ricerca
				connection = getConnection();

				ConfigurazioneBOVO configBO=new ConfigurazioneBOVO();
				configBO.setCodBibl(ricercaBuoniOrd.getCodBibl());
				configBO.setCodPolo(ricercaBuoniOrd.getCodPolo());

				String tipoRinnovoConfigurato="";
				try {
					tipoRinnovoConfigurato=this.loadConfigurazione(configBO).getTipoRinnovo();
					if (tipoRinnovoConfigurato==null)
						tipoRinnovoConfigurato="";
				} catch (Exception e) {

					// l'errore capita in questo punto
					//04.12.08 log.error("", e);
					log.error(e);
				}

				ConfigurazioneORDVO configurazioneORD=new ConfigurazioneORDVO();
				configurazioneORD.setCodBibl(ricercaBuoniOrd.getCodBibl());
				configurazioneORD.setCodPolo(ricercaBuoniOrd.getCodPolo());
				ConfigurazioneORDVO configurazioneORDLetta=new ConfigurazioneORDVO();
				Boolean gestBil=true;

				try {
					configurazioneORDLetta=this.loadConfigurazioneOrdini(configurazioneORD);
					if (configurazioneORDLetta!=null && !configurazioneORDLetta.isGestioneBilancio())
					{
						gestBil=false;
					}

				} catch (Exception e) {

					// l'errore capita in questo punto
					log.error("", e);
				}



				String sql="select distinct bor.cd_polo,bor.ts_var,bor.ts_var as dataUpd, bor.cd_bib,bor.id_buono_ordine, bor.buono_ord, bor.stato_buono, bor.cod_fornitore,bor.id_capitoli_bilanci, capBil.esercizio,capBil.capitolo, bor.cod_mat, bor.data_buono,  TO_CHAR(bor.data_buono,'dd/MM/yyyy') as data_buono_str   ";
				sql +=" from  tba_buono_ordine bor ";
				sql=sql  + " join tra_elementi_buono_ordine ordbo on ordbo.cd_polo=bor.cd_polo and  ordbo.cd_bib=bor.cd_bib and ordbo.buono_ord=bor.buono_ord  and ordbo.fl_canc<>'S'";
				sql +="  left join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=bor.id_capitoli_bilanci and capBil.fl_canc<>'S' ";
				//sql +=" left join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and bil.fl_canc<>'S' ";

				sql=this.struttura(sql);
				sql +=" bor.fl_canc<>'S'";

				if (ricercaBuoniOrd.getIdBuoOrdList()!=null && ricercaBuoniOrd.getIdBuoOrdList().size()!=0 )
				{
					String listID="(";
					for (int index3 = 0; index3 < ricercaBuoniOrd.getIdBuoOrdList().size(); index3++) {
						if (ricercaBuoniOrd.getIdBuoOrdList().get(index3)>0)
						{
							if (index3>0)
							{
								listID=listID+",";
							}
							listID=listID+ ricercaBuoniOrd.getIdBuoOrdList().get(index3);
						}
					}
					listID=listID+")";
					sql=this.struttura(sql);
					sql +=" bor.id_buono_ordine in " + listID;
				}

				if (ricercaBuoniOrd.getCodPolo() !=null &&  ricercaBuoniOrd.getCodPolo().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" bor.cd_polo='" + ricercaBuoniOrd.getCodPolo() +"'";
				}
				if (ricercaBuoniOrd.getCodBibl() !=null &&  ricercaBuoniOrd.getCodBibl().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" bor.cd_bib='" + ricercaBuoniOrd.getCodBibl() +"'";
				}
				if (ricercaBuoniOrd.getNumBuonoOrdineDa()!=null && ricercaBuoniOrd.getNumBuonoOrdineDa().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" TO_NUMBER(bor.buono_ord,'999999999') >= TO_NUMBER('" +  ricercaBuoniOrd.getNumBuonoOrdineDa() + "','999999999')";
				}

				if (ricercaBuoniOrd.getNumBuonoOrdineA()!=null && ricercaBuoniOrd.getNumBuonoOrdineA().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" TO_NUMBER(bor.buono_ord,'999999999') <= TO_NUMBER ('" +  ricercaBuoniOrd.getNumBuonoOrdineA() + "','999999999')";
				}

				if (ricercaBuoniOrd.getDataBuonoOrdineDa()!=null && ricercaBuoniOrd.getDataBuonoOrdineDa().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" bor.data_buono >= TO_DATE ('" +  ricercaBuoniOrd.getDataBuonoOrdineDa() + "','dd/MM/yyyy')";
				}
				if (ricercaBuoniOrd.getDataBuonoOrdineA()!=null && ricercaBuoniOrd.getDataBuonoOrdineA().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" bor.data_buono <= TO_DATE ('" +  ricercaBuoniOrd.getDataBuonoOrdineA() + "','dd/MM/yyyy')";
				}

				if (ricercaBuoniOrd.getBilancio()!=null &&  ricercaBuoniOrd.getBilancio().getCodice1()!=null && ricercaBuoniOrd.getBilancio().getCodice1().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" capBil.esercizio=" +ricercaBuoniOrd.getBilancio().getCodice1() ;
				}

				if (ricercaBuoniOrd.getBilancio()!=null && ricercaBuoniOrd.getBilancio().getCodice2()!=null && ricercaBuoniOrd.getBilancio().getCodice2().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" capBil.capitolo=" +ricercaBuoniOrd.getBilancio().getCodice2() ;
				}
				if (ricercaBuoniOrd.getBilancio()!=null && ricercaBuoniOrd.getBilancio().getCodice3()!=null && ricercaBuoniOrd.getBilancio().getCodice3().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" bor.cod_mat='" +ricercaBuoniOrd.getBilancio().getCodice3() +"'" ;
				}
				if (ricercaBuoniOrd.getFornitore()!=null && ricercaBuoniOrd.getFornitore().getCodice()!=null && ricercaBuoniOrd.getFornitore().getCodice().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" bor.cod_fornitore=" +ricercaBuoniOrd.getFornitore().getCodice()  ;
				}
				// tipo ordine
				if (ricercaBuoniOrd.getOrdine()!=null && ricercaBuoniOrd.getOrdine().getCodice1()!=null && ricercaBuoniOrd.getOrdine().getCodice1().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" ordbo.cod_tip_ord='" +ricercaBuoniOrd.getOrdine().getCodice1() +"'" ;

				}
				// anno ord
				if (ricercaBuoniOrd.getOrdine()!=null && String.valueOf(ricercaBuoniOrd.getOrdine().getCodice2()).length()!= 0)
				{
					sql=this.struttura(sql);
					sql +=" ordbo.anno_ord=" +ricercaBuoniOrd.getOrdine().getCodice2()  ;

				}
				// codice ordine
				if (ricercaBuoniOrd.getOrdine()!=null && ricercaBuoniOrd.getOrdine().getCodice3()!=null &&  ricercaBuoniOrd.getOrdine().getCodice3().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" ordbo.cod_ord=" +ricercaBuoniOrd.getOrdine().getCodice3()  ;

				}

				// creazione query per calcolo dei risultati con esclusione dell'order by
				String sqlXCount="";
				int pos=sql.lastIndexOf("from");
				int totBO=0;
				if (pos>0)
				{
					sqlXCount="select count(distinct bor.id_buono_ordine) as tot " +sql.substring(pos);
					pstmtCount = connection.prepareStatement(sqlXCount);
					rsCount = pstmtCount.executeQuery();
					while (rsCount.next()) {
						totBO=rsCount.getInt("tot");
					}
					rsCount.close();
					pstmtCount.close();
					if (totBO>1000 ) //&& ricercaFornitori.getTipoOperazionePicos()==null && !ricercaBuoniOrd.isStampaForn()
			        {
							throw new ValidationException("ricercaDaRaffinareTroppi",
				        			ValidationExceptionCodici.ricercaDaRaffinareTroppi);
			        }

				}

				// ordinamento passato
				if (ricercaBuoniOrd.getOrdinamento()==null || (ricercaBuoniOrd.getOrdinamento()!=null && ricercaBuoniOrd.getOrdinamento().equals("")))
				{
					sql +=" order by bor.cd_bib, bor.buono_ord, bor.data_buono ";

				}
				else if (ricercaBuoniOrd.getOrdinamento().equals("1"))
				{
					sql +=" order by bor.cd_bib, bor.buono_ord ";

				}
				else if (ricercaBuoniOrd.getOrdinamento().equals("2"))
				{
					sql +=" order by bor.cd_bib, bor.data_buono desc ";
				}

				else if (ricercaBuoniOrd.getOrdinamento().equals("3"))
				{
					sql +=" order by bor.cd_bib, bor.cod_fornitore ";
				}


				pstmt = connection.prepareStatement(sql);

				rs = pstmt.executeQuery();

				//boolean ret1 =  pstmt.execute();
				int numRighe=0;
				int progrRighe=1;
				while (rs.next() && progrRighe<100  ) {  //&& progrRighe<2 per test
 					//TO_CHAR(bil.budget,'999999999999999.99')
					numRighe=numRighe+1;
					rec = new BuoniOrdineVO();
					//rec.setProgressivo(numRighe);

					rec.setGestBil(true);

					if (!gestBil)
					{
						rec.setGestBil(false);
					}


					rec.setProgressivo(progrRighe);
					rec.setIDBuonoOrd(rs.getInt("id_buono_ordine"));
					rec.setIDBil(rs.getInt("id_capitoli_bilanci"));
					rec.setCodPolo(rs.getString("cd_polo"));
					rec.setCodBibl(rs.getString("cd_bib"));
					rec.setNumBuonoOrdine(rs.getString("buono_ord"));
					rec.setDataBuonoOrdine(rs.getString("data_buono_str"));
					rec.setStatoBuonoOrdine(rs.getString("stato_buono"));
					rec.setBilancio(new StrutturaTerna("","",""));
					//rec.getBilancio().setCodice1(String.valueOf(rs.getDouble("esercizio")));
					//rec.getBilancio().setCodice1(String.valueOf(rs.getFloat("esercizio")));
					rec.getBilancio().setCodice1(String.valueOf(rs.getInt("esercizio")));
					rec.getBilancio().setCodice2(String.valueOf(rs.getInt("capitolo")));
					rec.getBilancio().setCodice3(rs.getString("cod_mat"));
					rec.setFornitore(new StrutturaCombo("",""));
					rec.getFornitore().setCodice(String.valueOf(rs.getInt("cod_fornitore")));
					rec.setDataUpd(rs.getTimestamp("dataUpd"));

					String sql0="select   ord.*, bor.cd_bib, bor.buono_ord, bor.stato_buono, bor.cod_fornitore, capBil.esercizio,capBil.capitolo, bor.cod_mat, TO_CHAR(ord.data_ord,'dd/MM/yyyy') as data_ord_str , TO_CHAR(bor.data_buono,'dd/MM/yyyy') as data_buono_str , TO_CHAR(ord.data_fine,'dd/MM/yyyy') as data_fine_str, TO_CHAR(ord.data_fasc,'dd/MM/yyyy') as  data_fasc_str ";
					sql0=sql0 + ", forn.nom_fornitore, forn.indirizzo, forn.cod_fiscale, forn.fax, forn.telefono, forn.cap, forn.citta, forn.paese ";
					sql0=sql0 + ", tblbiblio.nom_biblioteca ";
					sql0=sql0 + " from  tba_buono_ordine bor ";
					sql0=sql0  + " join tbf_biblioteca tblbiblio on tblbiblio.cd_polo=bor.cd_polo and  tblbiblio.cd_bib=bor.cd_bib and tblbiblio.fl_canc<>'S'";
					sql0=sql0  + " join tra_elementi_buono_ordine ordbo on ordbo.cd_polo=bor.cd_polo and  ordbo.cd_bib=bor.cd_bib and ordbo.buono_ord=bor.buono_ord  and ordbo.fl_canc<>'S'";
					sql0=sql0 + "  join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=bor.id_capitoli_bilanci and capBil.fl_canc<>'S' ";
					sql0=sql0 + "  join tba_ordini ord on ord.cd_polo=ordbo.cd_polo and ord.cd_bib=ordbo.cd_bib and ord.fl_canc<>'S' and ord.stato_ordine <> 'N'";
					sql0=sql0 + " and ord.cod_tip_ord=ordbo.cod_tip_ord and ord.anno_ord=ordbo.anno_ord and ord.cod_ord=ordbo.cod_ord  ";
					sql0=sql0 + " join tbr_fornitori forn on forn.cod_fornitore=bor.cod_fornitore and forn.fl_canc<>'S' ";
					sql0=sql0  + "where ordbo.fl_canc<>'S'";

					if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)
					{
						sql0=this.struttura(sql0);
						sql0=sql0  + " ordbo.cd_polo='" + rec.getCodPolo() +"'";
					}

					if (rec.getCodBibl() !=null &&  rec.getCodBibl().length()!=0)
					{
						sql0=this.struttura(sql0);
						sql0=sql0  + " ordbo.cd_bib='" + rec.getCodBibl() +"'";
					}
					if (rec.getNumBuonoOrdine()!=null && rec.getNumBuonoOrdine().length()!=0)
					{
						sql0=this.struttura(sql0);
						sql0=sql0 + " TO_NUMBER(ordbo.buono_ord,'999999999')= TO_NUMBER('" + rec.getNumBuonoOrdine()+ "','999999999')";
					}

					if (ricercaBuoniOrd.getOrdine()!=null && ricercaBuoniOrd.getOrdine().getCodice1()!=null && ricercaBuoniOrd.getOrdine().getCodice1().trim().length()!=0)
					{
						sql0=this.struttura(sql0);
						sql0=sql0 + " ordbo.cod_tip_ord='" + ricercaBuoniOrd.getOrdine().getCodice1() + "'" ;
					}
					if (ricercaBuoniOrd.getOrdine()!=null && ricercaBuoniOrd.getOrdine().getCodice2()!=null && ricercaBuoniOrd.getOrdine().getCodice2().trim().length()!=0)
					{
						sql0=this.struttura(sql0);
						sql0=sql0 + " ordbo.anno_ord=" + ricercaBuoniOrd.getOrdine().getCodice2() ;

					}
					if (ricercaBuoniOrd.getOrdine()!=null && ricercaBuoniOrd.getOrdine().getCodice3()!=null && ricercaBuoniOrd.getOrdine().getCodice3().trim().length()!=0)
					{
						sql0=this.struttura(sql0);
						sql0=sql0 + " ordbo.cod_ord=" + ricercaBuoniOrd.getOrdine().getCodice3() ;
					}

					if (ricercaBuoniOrd.getFornitore()!=null && ricercaBuoniOrd.getFornitore().getDescrizione()!=null && ricercaBuoniOrd.getFornitore().getDescrizione().trim().length()!=0)
					{
						sql0=this.struttura(sql0);
						sql0=sql0 + " forn.nom_fornitore like '" + ricercaBuoniOrd.getFornitore().getDescrizione().replace("'","''") + "%'" ;
					}

					// ordinamento passato
					if ( ricercaBuoniOrd.getOrdinamento()==null || (ricercaBuoniOrd.getOrdinamento()!=null && ricercaBuoniOrd.getOrdinamento().equals("")))
					{
						sql +=" order by bor.cd_bib, bor.buono_ord, bor.data_buono ";

					}
					else if (ricercaBuoniOrd.getOrdinamento().equals("1"))
					{
						sql +=" order by bor.cd_bib, bor.buono_ord ";

					}
					else if (ricercaBuoniOrd.getOrdinamento().equals("2"))
					{
						sql +=" order by bor.cd_bib, bor.data_buono desc ";
					}

					else if (ricercaBuoniOrd.getOrdinamento().equals("3"))
					{
						sql +=" order by bor.cd_bib, bor.cod_fornitore ";
					}


					pstmt0 = connection.prepareStatement(sql0);
					rs0 = pstmt0.executeQuery();
					List<OrdiniVO> listaOrdiniDett = new ArrayList<OrdiniVO>();
					double impParziale=0;
					int ciclo=0;
					while (rs0.next()) {
						ciclo=ciclo +1;
						if (ciclo==1)
						{
							rec.getFornitore().setDescrizione(rs0.getString("nom_fornitore"));
							// dati biblioteca per stampe
							rec.setDenoBibl(rs0.getString("nom_biblioteca"));
							// dati fornitore per stampe
							rec.setAnagFornitore(new FornitoreVO());
							rec.getAnagFornitore().setCodFornitore(String.valueOf(rs0.getInt("cod_fornitore")));
							rec.getAnagFornitore().setNomeFornitore(rs0.getString("nom_fornitore"));


							if (rs0.getString("indirizzo")!=null && rs0.getString("indirizzo").trim().length()>0 )
							{
								rec.getAnagFornitore().setIndirizzo(rs0.getString("indirizzo"));
							}
							if (rs0.getString("citta")!=null && rs0.getString("citta").trim().length()>0 )
							{
								rec.getAnagFornitore().setCitta(rs0.getString("citta"));
							}
							if (rs0.getString("cap")!=null && rs0.getString("cap").trim().length()>0 )
							{
								rec.getAnagFornitore().setCap(rs0.getString("cap"));
							}
							if (rs0.getString("paese")!=null && rs0.getString("paese").trim().length()>0 )
							{
								rec.getAnagFornitore().setPaese(rs0.getString("paese"));
							}
							if (rs0.getString("cod_fiscale")!=null && rs0.getString("cod_fiscale").trim().length()>0 )
							{
								rec.getAnagFornitore().setCodiceFiscale(rs0.getString("cod_fiscale"));
							}
							if (rs0.getString("fax")!=null && rs0.getString("fax").trim().length()>0 )
							{
								rec.getAnagFornitore().setFax(rs0.getString("fax"));
							}
							if (rs0.getString("telefono")!=null && rs0.getString("telefono").trim().length()>0 )
							{
								rec.getAnagFornitore().setTelefono(rs0.getString("telefono"));
							}
							rec.getAnagFornitore().setFornitoreBibl(new DatiFornitoreVO());

						}

						recDett = new OrdiniVO();
						recDett.setCodBibl(rs0.getString("cd_bib"));
						recDett.setIDOrd(rs0.getInt("id_ordine"));
						recDett.setTipoOrdine(rs0.getString("cod_tip_ord"));
						recDett.setAnnoOrdine(String.valueOf(rs0.getInt("anno_ord")));
						recDett.setCodOrdine(String.valueOf(rs0.getInt("cod_ord")));
						recDett.setDataOrdine(rs0.getString("data_ord_str"));
						recDett.setStatoOrdine(rs0.getString("stato_ordine"));
						recDett.setNaturaOrdine(rs0.getString("natura"));
						recDett.setIDOrd(rs0.getInt("id_ordine"));
						recDett.setNoteFornitore(rs0.getString("note_forn").trim());

						if (rs0.getString("continuativo")!=null && rs0.getString("continuativo").length()!=0 && rs0.getString("continuativo").equals("1"))
						{
							recDett.setContinuativo(true);
						}
						else
						{
							recDett.setContinuativo(false);
						}

//*********INSERIMENTO DA QUI
						String isbd="";
						String bid="";
						String nStandard="";
						List<StrutturaTerna> nStandardArr=null;
						if (rs0.getString("bid")!=null && rs0.getString("bid").trim().length()!=0)
						{
							bid=rs0.getString("bid");

							try {
								TitoloACQVO recTit=null;
								Tba_cambi_ufficialiDao cambiDao= new Tba_cambi_ufficialiDao();
								recTit= this.getTitoloOrdineTer(rs0.getString("bid"));
								if (recTit!=null && recTit.getIsbd()!=null) {
									isbd=recTit.getIsbd();
								}
								if (recTit!=null && recTit.getNumStandard()!=null) {
									nStandard=recTit.getNumStandard();
								}
								if (recTit!=null && recTit.getNumStandardArr()!=null && recTit.getNumStandardArr().size()>0) {
									nStandardArr=recTit.getNumStandardArr();
								}

							} catch (Exception e) {
								isbd="titolo non trovato";
							}

						}

						recDett.setTitolo(new StrutturaCombo(bid,isbd));

						recDett.setTitoloIsbn("");
						recDett.setTitoloIssn("");
						if (nStandard!=null && nStandard.trim().length()>0)
						{
							if (rs0.getString("natura").equals("M"))
							{
								recDett.setTitoloIsbn(nStandard.trim());
							}
							else if ( rs0.getString("natura").equals("S") && rs0.getString("continuativo")!=null && rs0.getString("continuativo").length()!=0 && rs0.getString("continuativo").equals("1"))
							{
								recDett.setTitoloIssn(nStandard.trim());
							}
						}
						if (nStandardArr!=null && nStandardArr.size()>0)
						{
							recDett.setNumStandardArr(nStandardArr);
						}


						recDett.setDataPubblFascicoloAbbOrdine(rs0.getString("data_fasc_str"));
						recDett.setAnnataAbbOrdine(rs0.getString("annata"));
						recDett.setDataFineAbbOrdine(rs0.getString("data_fine_str"));

						if (rs0.getString("cod_1ord")==null || (rs0.getString("cod_1ord")!=null && rs0.getString("cod_1ord").equals("")))
						{
							recDett.setCodPrimoOrdine("");
						}
						else
						{
							recDett.setCodPrimoOrdine(String.valueOf(rs0.getInt("cod_1ord")));
						}
						if (rs0.getString("anno_1ord")==null || (rs0.getString("anno_1ord")!=null && rs0.getString("anno_1ord").equals("")))
						{
							recDett.setAnnoPrimoOrdine("");
						}
						else
						{
							recDett.setAnnoPrimoOrdine(String.valueOf(rs0.getInt("anno_1ord")));
						}



						recDett.setRinnovoOrigine(new StrutturaTerna("","","")); // accoglie l'ordine originario o precedente  a seconda della configurazione
						// per la stampa


						if (recDett.isContinuativo() && recDett.getCodPrimoOrdine()!=null && !recDett.getCodPrimoOrdine().trim().equals("0") && recDett.getCodPrimoOrdine().trim().length()>0 && recDett.getAnnoPrimoOrdine()!=null && !recDett.getAnnoPrimoOrdine().trim().equals("0") && recDett.getAnnoPrimoOrdine().trim().length()>0)
						{
							recDett.setRinnovoOrigine(this.gestioneRinnovato(recDett.getIDOrd(),recDett.getCodPrimoOrdine().trim(),recDett.getAnnoPrimoOrdine().trim(), tipoRinnovoConfigurato)); //
						}
//*********INSERIMENTO FINO A QUI


						recDett.setPrezzoEuroOrdine(rs0.getDouble("prezzo_lire"));
						recDett.setRinnovato(rs0.getBoolean("rinnovato"));
						//
						double prezzo=recDett.getPrezzoEuroOrdine(); //almaviva2 bug 0004538
					    String prezzoStr=this.VisualizzaImporto(prezzo);//rosa

					    recDett.setPrezzoEuroOrdineStr(prezzoStr);//rosa
						//

						// fare la somma dei prezzi per desumere l'importo del buono
						impParziale=impParziale + rs0.getFloat("prezzo_lire");
						listaOrdiniDett.add(recDett);
					}
					rec.setListaOrdiniBuono(listaOrdiniDett);
					rec.setImporto(impParziale);

					// se legami con ordini inesistenti per le condizioni immesse il buono non deve essere aggiunto
					if (listaOrdiniDett.size()>0)
					{
						listaBuoniOrd.add(rec);
						progrRighe=progrRighe +1;
					}
					rs0.close();
					pstmt0.close();
				}
				rs.close();
				pstmt.close();
				connection.close();
			}catch (ValidationException e) {
	      	  throw e;

		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

		Validazione.ValidaRicercaBuoniOrd (listaBuoniOrd);
        return listaBuoniOrd;
	}

	public List<StrutturaInventariOrdVO> getInventariOrdineRilegatura(ListaSuppOrdiniVO ricercaInvOrd) throws ResourceNotFoundException, ApplicationException, ValidationException
	{
		List<StrutturaInventariOrdVO> listaInvOrd=null;
		if (ricercaInvOrd!=null &&  ricercaInvOrd.isBollettarioSTP())
		{
			//Validazione.ValidaListaSuppOrdiniVO (ricercaInvOrd);
			String ticket="";
			ticket=ricercaInvOrd.getTicket();

			listaInvOrd = new ArrayList<StrutturaInventariOrdVO>();

			int ret = 0;
	        // execute the search here
				StrutturaInventariOrdVO rec = null;
				Connection connection = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				PreparedStatement pstmt0 = null;
				ResultSet rs0 = null;

				try {
					// contiene i criteri di ricerca
					connection = getConnection();

					String sql="select distinct inv.*,TO_CHAR(inv.data_uscita,'dd/MM/yyyy') as data_uscita_str,TO_CHAR(inv.data_rientro,'dd/MM/yyyy') as data_rientro_str, TO_CHAR(inv.data_rientro_presunta,'dd/MM/yyyy') as data_rientro_presunta_str , ord.id_ordine, ord.cd_polo, ord.cd_bib, ord.anno_ord, ord.cod_tip_ord, ord.cod_ord, forn.cod_fornitore,forn.nom_fornitore, inventari.bid as bidInventario, tit.isbd , coll.cd_sez, coll.cd_loc, coll.spec_loc, inventari.seq_coll, bibliot.nom_biblioteca ";
					sql +=" from tra_ordine_inventari inv";
					sql +=" join tbc_inventario inventari on inventari.cd_polo=inv.cd_polo and inventari.cd_bib=inv.cd_bib and inventari.cd_serie=inv.cd_serie and inventari.cd_inven=inv.cd_inven and inventari.fl_canc<>'S' ";
					sql +=" left join tbc_collocazione coll on coll.key_loc=inventari.key_loc and coll.fl_canc<>'S' ";
					sql +=" join tb_titolo tit on tit.bid=inventari.bid  ";
					sql +=" join tba_ordini  ord  on ord.fl_canc<>'S'  and  ord.id_ordine=inv.id_ordine and ord.cod_tip_ord='R' and ord.stato_ordine='A'  ";
					if (ricercaInvOrd.getCodPolo()!=null && ricercaInvOrd.getCodPolo().length()>0)
					{
						sql +=" and  ord.cd_polo='" + ricercaInvOrd.getCodPolo() + "'";
					}
					if (ricercaInvOrd.getCodBibl()!=null && ricercaInvOrd.getCodBibl().length()>0)
					{
						sql +=" and ord.cd_bib='" + ricercaInvOrd.getCodBibl() + "'";
						sql +=" join tbf_biblioteca bibliot on bibliot.cd_bib=ord.cd_bib and bibliot.cd_polo=ord.cd_polo and bibliot.fl_canc<>'S' ";

					}
					sql +=" join tbr_fornitori forn on forn.cod_fornitore=ord.cod_fornitore and forn.fl_canc<>'S'  ";

					if (ricercaInvOrd.isSoloInRilegatura())
					{
						sql=this.struttura(sql);
						sql +=" inv.data_rientro is null";
					}

					if (ricercaInvOrd.getCodBibl()!=null && ricercaInvOrd.getCodBibl().length()>0)
					{
						sql=this.struttura(sql);
						sql +=" inventari.cd_bib='" + ricercaInvOrd.getCodBibl() + "'";
					}
					if (ricercaInvOrd.getCodPolo()!=null && ricercaInvOrd.getCodPolo().length()>0)
					{
						sql=this.struttura(sql);
						sql +=" inventari.cd_polo='" + ricercaInvOrd.getCodPolo() + "'";
					}

					if (ricercaInvOrd.isBollettarioSTP() && ricercaInvOrd.getIdOrdList()!=null && ricercaInvOrd.getIdOrdList().size()!=0 )
					{
						String listID="(";
						for (int index3 = 0; index3 < ricercaInvOrd.getIdOrdList().size(); index3++) {
							if (ricercaInvOrd.getIdOrdList().get(index3)>0)
							{
								if (index3>0)
								{
									listID=listID+",";
								}
								listID=listID+ ricercaInvOrd.getIdOrdList().get(index3);
							}
						}
						listID=listID+")";
						sql=this.struttura(sql);
						sql +=" ord.id_ordine in " + listID;
					}

					if (ricercaInvOrd.getDataOrdineDa()!=null && ricercaInvOrd.getDataOrdineDa().length()!=0)
					{
						sql=this.struttura(sql);
						sql +=" inv.data_uscita >= TO_DATE('" +  ricercaInvOrd.getDataOrdineDa() + "','dd/MM/yyyy')";
					}

					if (ricercaInvOrd.getDataOrdineA()!=null && ricercaInvOrd.getDataOrdineA().length()!=0)
					{
						sql=this.struttura(sql);
						sql +=" inv.data_uscita  <= TO_DATE ('" +  ricercaInvOrd.getDataOrdineA() + "','dd/MM/yyyy')";
					}

					if (ricercaInvOrd.getDataStampaOrdineDa()!=null && ricercaInvOrd.getDataStampaOrdineDa().length()!=0)
					{
						sql=this.struttura(sql);
						sql +=" inv.data_rientro >= TO_DATE ('" +  ricercaInvOrd.getDataStampaOrdineDa() + "','dd/MM/yyyy')";
					}
					if (ricercaInvOrd.getDataStampaOrdineA()!=null && ricercaInvOrd.getDataStampaOrdineA().length()!=0)
					{
						sql=this.struttura(sql);
						sql +=" inv.data_rientro <= TO_DATE('" +  ricercaInvOrd.getDataStampaOrdineA() + "','dd/MM/yyyy')";
					}

					if (ricercaInvOrd.getDataFineAbbOrdineDa()!=null && ricercaInvOrd.getDataFineAbbOrdineDa().length()!=0)
					{
						sql=this.struttura(sql);
						sql +=" inv.data_rientro_presunta >= TO_DATE ('" +  ricercaInvOrd.getDataFineAbbOrdineDa() + "','dd/MM/yyyy')";
					}
					if (ricercaInvOrd.getDataFineAbbOrdineA()!=null && ricercaInvOrd.getDataFineAbbOrdineA().length()!=0)
					{
						sql=this.struttura(sql);
						sql +=" inv.data_rientro_presunta <= TO_DATE ('" +  ricercaInvOrd.getDataFineAbbOrdineA() + "','dd/MM/yyyy')";
					}
					if (ricercaInvOrd.getOrdinamento()==null || (ricercaInvOrd.getOrdinamento()!=null && ricercaInvOrd.getOrdinamento().equals("")))
					{
						//almaviva5_20121114 evolutive google
						sql += "    order by ord.cd_bib, ord.anno_ord, ord.cod_tip_ord, ord.cod_ord, inv.posizione, inv.data_uscita  ";
					}
					else if (ricercaInvOrd.getOrdinamento().equals("1"))
					{
						sql +="   order by inv.data_uscita ";
					}
					else if (ricercaInvOrd.getOrdinamento().equals("2"))
					{
						sql +="   order by inv.data_uscita desc";
					}

					pstmt = connection.prepareStatement(sql);
					rs = pstmt.executeQuery();
					//boolean ret1 =  pstmt.execute();
					int numRighe = 0;
					while (rs.next()) {
						numRighe++;
						rec = ConversioneHibernateVO.toWeb().inventarioOrdine(rs);//new StrutturaInventariOrdVO();
						rec.setProgressivo(numRighe);
						listaInvOrd.add(rec);
					}
					rs.close();
					pstmt.close();
					connection.close();
				}catch (ValidationException e) {
					throw e;
			} catch (Exception e) {
				log.error("", e);
			} finally {
				close(connection);
			}

		}

		//Validazione.ValidaRicercaBuoniOrd (listaInvOrd);
        return listaInvOrd;
	}



	public boolean  inserisciBuonoOrd(BuoniOrdineVO buonoOrd) throws DataException, ApplicationException , ValidationException
	{
		 Validazione.ValidaBuoniOrdineVO (this, buonoOrd);

		// occorre vedere se se il buono è configurato per progressivo automatico o manuale
		boolean numAutom=false;
		ConfigurazioneBOVO configBO=new ConfigurazioneBOVO();
		configBO.setCodBibl(buonoOrd.getCodBibl());
		try {
			if (this.loadConfigurazione(configBO).isNumAutomatica())
			{
				numAutom=true;
			}

		} catch (Exception e) {

			// l'errore capita in questo punto
			log.error("", e);
		}

		boolean valRitorno=false;
    	int motivo=0;
    	//FornitoreVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSet rsSub = null;
		PreparedStatement pstmtCod= null;
		ResultSet rsSubCod= null;
		//ResultSet rsInsTit = null;
		ResultSet rsESISTEORD = null;
		PreparedStatement pstmtESISTEORD = null;

		boolean valRitornoINS=false;
		boolean valRitornoINSLOOP=false;
		boolean esistenzaBuono=false;
		boolean controlloCONGR=false;
		boolean valRitornoCONGRLOOP=false;
		boolean cancLogica=false;
		int idBOCancLog=0;
		String codBOCancLog="";
		PreparedStatement pstmtUDP= null;
		boolean valRitornoUPD=false;
		PreparedStatement pstmtUDP2= null;
		int intRetCANC2 = 0;

		try {
			// CONTROLLI PREVENTIVI
			connection = getConnection();

			String sql0="select ordbo.*, bor.cd_bib, bor.id_buono_ordine, bor.buono_ord, bor.stato_buono, bor.cod_fornitore as bor_cod_fornitore, capBil.esercizio,capBil.capitolo, bor.cod_mat,  TO_CHAR(bor.data_buono,'dd/MM/yyyy') as data_buono_str   ";
			sql0=sql0  + " from  tba_buono_ordine bor ";
			sql0=sql0  + " join tra_elementi_buono_ordine ordbo on ordbo.cd_polo=bor.cd_polo and  ordbo.cd_bib=bor.cd_bib and ordbo.buono_ord=bor.buono_ord  and ordbo.fl_canc<>'S'";
			sql0=sql0 + "  left join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=bor.id_capitoli_bilanci and capBil.fl_canc<>'S' ";

			sql0=this.struttura(sql0);
			sql0=sql0 + " bor.fl_canc<>'S'";

			if (buonoOrd.getCodPolo() !=null &&  buonoOrd.getCodPolo().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " bor.cd_polo='" + buonoOrd.getCodPolo() +"'";
			}

			if (buonoOrd.getCodBibl() !=null &&  buonoOrd.getCodBibl().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " bor.cd_bib='" + buonoOrd.getCodBibl() +"'";
			}

			if (buonoOrd.getNumBuonoOrdine()!=null && buonoOrd.getNumBuonoOrdine().length()!=0 && !numAutom)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " bor.buono_ord = '" +  buonoOrd.getNumBuonoOrdine() + "'";
			}


			// controlli preventivi di esistenza del bilancio specificato
			if (buonoOrd.isGestBil() && buonoOrd.getBilancio()!=null && buonoOrd.getBilancio().getCodice1()!=null && buonoOrd.getBilancio().getCodice1().trim().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " capBil.esercizio=" + buonoOrd.getBilancio().getCodice1();

				// subquery per test di esistenza bilancio : da sostituire con metodo ancora da scrivere in this
				String sqlSub="select * from tbb_capitoli_bilanci capBil  ";
				//sqlSub=sqlSub + " join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and bil.fl_canc<>'S'";
				sqlSub=sqlSub + " where capBil.fl_canc<>'S'";
				sqlSub=sqlSub + " and capBil.cd_polo='" +buonoOrd.getCodPolo()+"'";
				sqlSub=sqlSub + " and capBil.cd_bib='" +buonoOrd.getCodBibl()+"'";
				if (buonoOrd.getBilancio().getCodice1()!=null && buonoOrd.getBilancio().getCodice1().length()!=0)
				{
					sqlSub=sqlSub + " and capBil.esercizio=" +buonoOrd.getBilancio().getCodice1();
				}
				if (buonoOrd.getBilancio().getCodice2()!=null && buonoOrd.getBilancio().getCodice2().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0 + " capBil.capitolo=" + buonoOrd.getBilancio().getCodice2();

					sqlSub=sqlSub + " and capBil.capitolo=" +buonoOrd.getBilancio().getCodice2();
				}
				pstmt = connection.prepareStatement(sqlSub);
				rsSub = pstmt.executeQuery();
				if (!rsSub.next()) {
					motivo=5;
					throw new ValidationException("ordineIncongruenzaBilancioInesistente",
							ValidationExceptionCodici.ordineIncongruenzaBilancioInesistente);
				}
				else
				{
					buonoOrd.setIDBil(rsSub.getInt("id_capitoli_bilanci"));
				}

				rsSub.close();

			}



			if (buonoOrd.isGestBil() && buonoOrd.getBilancio()!=null && buonoOrd.getBilancio().getCodice3()!=null && buonoOrd.getBilancio().getCodice3().trim().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " bor.cod_mat='" + buonoOrd.getBilancio().getCodice3() +"'";
			}

			if (buonoOrd.getFornitore().getCodice()!=null && buonoOrd.getFornitore().getCodice()!=null  && buonoOrd.getFornitore().getCodice().trim().length()!=0)
			{

				sql0=this.struttura(sql0);
				sql0=sql0 + " bor.cod_fornitore=" +  buonoOrd.getFornitore().getCodice().trim()  ;

				// subquery per test di esistenza fornitore : da sostituire con metodo ancora da scrivere in this
				String sqlSub="select * from tbr_fornitori forn  ";
				sqlSub=sqlSub + " join tbr_fornitori_biblioteche fornBibl on fornBibl.cod_fornitore=forn.cod_fornitore  and fornBibl.cd_biblioteca='" + buonoOrd.getCodBibl()+"' and fornBibl.cd_polo='" + buonoOrd.getCodPolo()+"' and fornBibl.fl_canc<>'S'" ;
				sqlSub=sqlSub + " where forn.fl_canc<>'S'";
				sqlSub=sqlSub + " and forn.cod_fornitore=" + buonoOrd.getFornitore().getCodice().trim();

				pstmt = connection.prepareStatement(sqlSub);
				rsSub = pstmt.executeQuery();
				if (!rsSub.next()) {
					motivo=6;
					throw new ValidationException("ordineIncongruenzaFornitoreInesistente",
							ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);
				}

				rsSub.close();
			}


			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			Timestamp ts = DaoManager.now();
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;
			}
			rs.close();
			if (numRighe>0 && !numAutom ) //
			{
					esistenzaBuono=true; // record forse già esistente quindi non inseribile
					throw new ValidationException("cambierroreInserimentoEsistenzaRecord",
							ValidationExceptionCodici.cambierroreInserimentoEsistenzaRecord);

			}
			if (numRighe==0 && !numAutom )
			{
				String sql00="select id_buono_ordine, buono_ord  ";
				sql00=sql00  + " from  tba_buono_ordine  ";
				sql00=this.struttura(sql00);
				sql00=sql00 + " fl_canc='S'";
				if (buonoOrd.getNumBuonoOrdine()!=null && buonoOrd.getNumBuonoOrdine().length()!=0 && !numAutom)
				{
					sql00=this.struttura(sql00);
					sql00=sql00 + " buono_ord = '" +  buonoOrd.getNumBuonoOrdine() + "'";
				}
				pstmt = connection.prepareStatement(sql00);
				rsSub = pstmt.executeQuery();
				if (rsSub.next()) {
					// siamo nel caso di cancellazione logica
					// PRENDERE L'ID
					idBOCancLog=rsSub.getInt("id_buono_ordine");
					codBOCancLog=rsSub.getString("buono_ord");
					cancLogica=true;
				}
				rsSub.close();
			}
			pstmt.close();

			if (cancLogica) {
				// controlli congruenza
				if (buonoOrd.getListaOrdiniBuono()!=null && buonoOrd.getListaOrdiniBuono().size() >0 )
				{
					valRitornoCONGRLOOP=false;
					for (int i=0; i<buonoOrd.getListaOrdiniBuono().size(); i++)
					{
						OrdiniVO oggettoDettVO=buonoOrd.getListaOrdiniBuono().get(i);
						// controllo esistenza ordine
						String sqlESISTEORD="select distinct ord.* , ordbo.buono_ord from tba_ordini ord " ;
						sqlESISTEORD= sqlESISTEORD + " left join  tra_elementi_buono_ordine ordbo on ordbo.cd_polo=ord.cd_polo and  ordbo.cd_bib=ord.cd_bib and ordbo.cod_tip_ord=ord.cod_tip_ord  and ordbo.anno_ord=ord.anno_ord and ordbo.cod_ord=ord.cod_ord and ordbo.fl_canc<>'S'" ;
						sqlESISTEORD=this.struttura(sqlESISTEORD);
						sqlESISTEORD=sqlESISTEORD + " ord.fl_canc<>'S'";
						sqlESISTEORD=this.struttura(sqlESISTEORD);
						sqlESISTEORD=sqlESISTEORD + " ord.cd_polo='" + buonoOrd.getCodPolo() +"'";
						sqlESISTEORD=this.struttura(sqlESISTEORD);
						sqlESISTEORD=sqlESISTEORD + " ord.cd_bib='" + buonoOrd.getCodBibl() +"'";
						sqlESISTEORD=this.struttura(sqlESISTEORD);
						sqlESISTEORD=sqlESISTEORD + " ord.cod_tip_ord='" + oggettoDettVO.getTipoOrdine() +"'";
						sqlESISTEORD=this.struttura(sqlESISTEORD);
						sqlESISTEORD=sqlESISTEORD + " ord.anno_ord=" + oggettoDettVO.getAnnoOrdine() ;
						sqlESISTEORD=this.struttura(sqlESISTEORD);
						sqlESISTEORD=sqlESISTEORD + " ord.cod_ord=" +  oggettoDettVO.getCodOrdine() ;

						pstmtESISTEORD = connection.prepareStatement(sqlESISTEORD);
						rsESISTEORD = pstmtESISTEORD.executeQuery(); // va in errore se non può restituire un recordset
						numRighe=0;
						String BOCollegato="";
						String OrdBOCollegato="";
						while (rsESISTEORD.next()) {
							numRighe=numRighe+1;
							if (rsESISTEORD.getString("buono_ord")!=null && rsESISTEORD.getString("buono_ord").trim().length()>0)
							{
								BOCollegato=rsESISTEORD.getString("buono_ord");
								OrdBOCollegato=oggettoDettVO.getTipoOrdine().trim() +"-" + oggettoDettVO.getAnnoOrdine().trim() +"-" + oggettoDettVO.getCodOrdine().trim() ;

							}
							// deve esserci un unico risultato
							// controlli congruenza
							// controllo che il bilancio e il fornitore dell'ordine siano quelli del buono
							controlloCONGR=false;
							//|| !rsESISTEORD.getString("cod_mat").equals(buonoOrd.getBilancio().getCodice3().trim())
							//if (!rsESISTEORD.getString("esercizio").equals(buonoOrd.getBilancio().getCodice1().trim()) || !rsESISTEORD.getString("capitolo").equals(buonoOrd.getBilancio().getCodice2().trim())  )
							if (buonoOrd.isGestBil() && rsESISTEORD.getInt("id_capitoli_bilanci")!=buonoOrd.getIDBil() && (oggettoDettVO.getTipoOrdine().equals("A") || oggettoDettVO.getTipoOrdine().equals("V") ) )
							{
								throw new ValidationException("ordinierroreCampoBilancioIncongruente",
								ValidationExceptionCodici.ordinierroreCampoBilancioIncongruente);
							}

							if (!rsESISTEORD.getString("cod_fornitore").equals(buonoOrd.getFornitore().getCodice().trim())  )
							{
								throw new ValidationException("ordinierroreCampoFornitoreIncongruente",
										ValidationExceptionCodici.ordinierroreCampoFornitoreIncongruente);
							}

							// Gli ordini di buono devono essere in uno stato di aperto o stampato

							if (!rsESISTEORD.getString("stato_ordine").equals("A") && !rsESISTEORD.getString("stato_ordine").equals("S"))
							{
								throw new ValidationException("ordinierroreOrdineBuonoNOTApertooStampato",
										ValidationExceptionCodici.ordinierroreOrdineBuonoNOTApertooStampato);
							}
							controlloCONGR=true;
						}
						rsESISTEORD.close();
						pstmtESISTEORD.close();

						if (numRighe==1 && controlloCONGR )
						{
							if  (!BOCollegato.equals(""))
							{
								// l'ordine è legato ad un buono
								throw new ValidationException("ordinierroreOrdineBuonoOrdineLegato" ,
										ValidationExceptionCodici.ordinierroreOrdineBuonoOrdineLegato, "msgSez", OrdBOCollegato , BOCollegato);

							}
							valRitornoCONGRLOOP=false;
						}
						else
						{
							valRitornoCONGRLOOP=true;
							break;
						}

					}// fine for
				} // fine esistenza lista ordini
				if  (valRitornoCONGRLOOP)
				{
					throw new ValidationException("ordineNONtrovato",
							ValidationExceptionCodici.ordineNONtrovato);
				}
				else
				{
					// delete righe
					// cancellazione fisica	di tutte le righe legate AL BUONO
					String sqlDEL2="delete from tra_elementi_buono_ordine " ;
					sqlDEL2=this.struttura(sqlDEL2);
					sqlDEL2=sqlDEL2 + " fl_canc = 'S'" ;  // fl_canc
					sqlDEL2=this.struttura(sqlDEL2);
					sqlDEL2=sqlDEL2 + " buono_ord='" +  buonoOrd.getNumBuonoOrdine()+ "'";
					pstmtUDP2 = connection.prepareStatement(sqlDEL2);
					intRetCANC2 = 0;
					intRetCANC2 = pstmtUDP2.executeUpdate();
					pstmtUDP2.close();
					if (intRetCANC2>=0)
					{
						// insert righe
						if (buonoOrd.getListaOrdiniBuono()!=null && buonoOrd.getListaOrdiniBuono().size() >0 )
						{
							valRitornoINSLOOP=false;
							for (int i=0; i<buonoOrd.getListaOrdiniBuono().size(); i++)
							{
								OrdiniVO oggettoDettVO=buonoOrd.getListaOrdiniBuono().get(i);
								String sqlSub2="insert into tra_elementi_buono_ordine values ( " ;
								sqlSub2 = sqlSub2+  "'" + buonoOrd.getCodPolo() + "'" ;  // cod_polo
								sqlSub2 = sqlSub2+  ",'" + buonoOrd.getCodBibl() + "'" ;  // cd_bib
								sqlSub2 = sqlSub2+  ",'" +  buonoOrd.getNumBuonoOrdine() + "'" ;  // buono_ord
								sqlSub2 = sqlSub2+  ",'" +  oggettoDettVO.getTipoOrdine() + "'" ;  // cod_tip_ord
								sqlSub2 = sqlSub2+ "," + oggettoDettVO.getAnnoOrdine();  // anno_ord
								sqlSub2 = sqlSub2+ "," +  oggettoDettVO.getCodOrdine() ;  // cod_ord
								sqlSub2 = sqlSub2 + ",'" + buonoOrd.getUtente() + "'" ;   // ute_ins
								sqlSub2 = sqlSub2 + ",'" + ts + "'" ;   // ts_ins
								sqlSub2 = sqlSub2 + ",'" + buonoOrd.getUtente() + "'" ;   // ute_var
								sqlSub2 = sqlSub2 + ",'" + ts + "'";   // ts_var
								sqlSub2 = sqlSub2 + ",'N'";   // fl_canc
								sqlSub2 = sqlSub2+ ")" ;
								pstmt = connection.prepareStatement(sqlSub2);
								int intRetINSLOOP = 0;
								intRetINSLOOP = pstmt.executeUpdate();
								pstmt.close();
								if (intRetINSLOOP!=1){
									valRitornoINSLOOP=true;
								}
							}
						}
						//update bo
						if (!valRitornoINSLOOP)
						{
							// aggiornamento CAPITOLO

							String sqlUDP="update tba_buono_ordine set " ;
							//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
							sqlUDP= sqlUDP  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
							sqlUDP= sqlUDP  + ", ute_var = '" +  buonoOrd.getUtente()  + "'" ;  // ute_var
							sqlUDP= sqlUDP + ", data_buono = (SELECT CURRENT_DATE ) "  ;  // data_buono
							sqlUDP= sqlUDP + ", cod_fornitore = " + buonoOrd.getFornitore().getCodice() ;  // cod_fornitore
							sqlUDP= sqlUDP + ", stato_buono ='" + buonoOrd.getStatoBuonoOrdine().trim()  + "'"  ;  // stato
							sqlUDP= sqlUDP + ", fl_canc='N'";
							sqlUDP=this.struttura(sqlUDP);
							sqlUDP=sqlUDP + " fl_canc='S' ";
							sqlUDP=this.struttura(sqlUDP);
							sqlUDP=sqlUDP + " id_buono_ordine=" + idBOCancLog ;
							if (idBOCancLog>0)
							{
								pstmtUDP = connection.prepareStatement(sqlUDP);
								int intRetUDP = 0;
								intRetUDP = pstmtUDP.executeUpdate();
								pstmtUDP.close();
								// fine estrazione codice
								if (intRetUDP==1){
									valRitornoUPD=true;
									}
							}
						}

					}

					// impostazione del codice di ritorno finale
					if (valRitornoUPD)
					{
						valRitorno=true;
					}
				}

			}
			else
			{
				if (!esistenzaBuono)
				{
					// INSERIMENTO
					String sqlSub="insert into tba_buono_ordine  " ;
					sqlSub = sqlSub + " ( id_capitoli_bilanci, cd_polo, cd_bib, buono_ord, cod_fornitore, stato_buono, data_buono, ute_ins, ts_ins, ute_var, ts_var, fl_canc, cod_mat) " ;
					sqlSub = sqlSub + " values ( ";

					if (buonoOrd.isGestBil() && buonoOrd.getIDBil()!=0)
					{
						sqlSub=sqlSub +   buonoOrd.getIDBil();  // id_capitoli_bilanci

					}
					else
					{
						sqlSub=sqlSub + ",null" ; //id_capitoli_bilanci 26
					}
					sqlSub=sqlSub + ",'" + buonoOrd.getCodPolo() + "'" ;  // cd_polo
					sqlSub=sqlSub + ",'" + buonoOrd.getCodBibl() + "'" ;  // cd_bib
					// valutazione della configurazione del bo su automatismo progressivo
					if (numAutom)
					{
						// inizio subquery
						sqlSub=sqlSub + ", (SELECT CASE WHEN  (MAX(TO_NUMBER(buono_ord,'999999999')) is null) THEN 1  else MAX(TO_NUMBER(buono_ord,'999999999'))+1  END " ;
						sqlSub=sqlSub + " from tba_buono_ordine   ";
						sqlSub=sqlSub + " where cd_bib='" +  buonoOrd.getCodBibl() +"'";
						sqlSub=sqlSub + " and cd_polo='" +  buonoOrd.getCodPolo() +"'";
						sqlSub=sqlSub + " )";  // fine subquery
					}
					else
					{
						sqlSub=sqlSub + ",'" +  buonoOrd.getNumBuonoOrdine() + "'" ;  // buono_ord
					}
					sqlSub=sqlSub + "," +  buonoOrd.getFornitore().getCodice();  // cod_fornitore
					sqlSub=sqlSub + ",'" +  buonoOrd.getStatoBuonoOrdine() + "'" ;  // stato_buono
					sqlSub=sqlSub + ",(SELECT CURRENT_DATE )" ; //data_buono
					sqlSub=sqlSub + ",'" + buonoOrd.getUtente() + "'" ;   // ute_ins
					sqlSub=sqlSub + ",'" + ts + "'" ;   // ts_ins
					sqlSub=sqlSub + ",'" + buonoOrd.getUtente() + "'" ;   // ute_var
					sqlSub=sqlSub + ",'" + ts + "'";   // ts_var
					sqlSub=sqlSub + ",'N'";   // fl_canc
					sqlSub=sqlSub + ",null" ; //cod_mat
					sqlSub=sqlSub + ")" ;
					pstmt = connection.prepareStatement(sqlSub);
					int intRetINS = 0;
					log.debug("Debug: inserimento buono ordine");
					log.debug("Debug: " + sqlSub);

					intRetINS = pstmt.executeUpdate();
					pstmt.close();
					if (intRetINS==1){
						valRitornoINS=true;
						// deduzione del codice appena inserito per successivi insert
						String sqlCodice="select buono_ord from tba_buono_ordine ";
						sqlCodice=sqlCodice + " where ute_ins='" + buonoOrd.getUtente()+ "'";
						sqlCodice=sqlCodice + " order by ts_ins desc limit 1";

						pstmtCod = connection.prepareStatement(sqlCodice);
						rsSubCod = pstmtCod.executeQuery();
						if (rsSubCod.next()) {
							buonoOrd.setNumBuonoOrdine(rsSubCod.getString("buono_ord"));
						}
						rsSubCod.close();
						pstmtCod.close();
					}
					if (buonoOrd.getListaOrdiniBuono()!=null && buonoOrd.getListaOrdiniBuono().size() >0 )
					{
						valRitornoINSLOOP=false;
						for (int i=0; i<buonoOrd.getListaOrdiniBuono().size(); i++)
						{
							OrdiniVO oggettoDettVO=buonoOrd.getListaOrdiniBuono().get(i);
							// controllo esistenza ordine
							String sqlESISTEORD="select distinct ord.* , ordbo.buono_ord from tba_ordini ord  " ;
							sqlESISTEORD= sqlESISTEORD + " left join  tra_elementi_buono_ordine ordbo on ordbo.cd_polo=ord.cd_polo and  ordbo.cd_bib=ord.cd_bib and ordbo.cod_tip_ord=ord.cod_tip_ord  and ordbo.anno_ord=ord.anno_ord and ordbo.cod_ord=ord.cod_ord and ordbo.fl_canc<>'S'" ;
							sqlESISTEORD=this.struttura(sqlESISTEORD);
							sqlESISTEORD=sqlESISTEORD + " ord.fl_canc<>'S'";
							sqlESISTEORD=this.struttura(sqlESISTEORD);
							sqlESISTEORD=sqlESISTEORD + " ord.cd_polo='" + buonoOrd.getCodPolo() +"'";
							sqlESISTEORD=this.struttura(sqlESISTEORD);
							sqlESISTEORD=sqlESISTEORD + " ord.cd_bib='" + buonoOrd.getCodBibl() +"'";
							sqlESISTEORD=this.struttura(sqlESISTEORD);
							sqlESISTEORD=sqlESISTEORD + " ord.cod_tip_ord='" + oggettoDettVO.getTipoOrdine() +"'";
							sqlESISTEORD=this.struttura(sqlESISTEORD);
							sqlESISTEORD=sqlESISTEORD + " ord.anno_ord=" + oggettoDettVO.getAnnoOrdine() ;
							sqlESISTEORD=this.struttura(sqlESISTEORD);
							sqlESISTEORD=sqlESISTEORD + " ord.cod_ord=" +  oggettoDettVO.getCodOrdine() ;

							pstmtESISTEORD = connection.prepareStatement(sqlESISTEORD);
							rsESISTEORD = pstmtESISTEORD.executeQuery(); // va in errore se non può restituire un recordset

							numRighe=0;
							String BOCollegato="";
							String OrdBOCollegato="";
							while (rsESISTEORD.next()) {
								numRighe=numRighe+1;
								if (rsESISTEORD.getString("buono_ord")!=null && rsESISTEORD.getString("buono_ord").trim().length()>0)
								{
									BOCollegato=rsESISTEORD.getString("buono_ord");
									OrdBOCollegato=oggettoDettVO.getTipoOrdine().trim() +"-" + oggettoDettVO.getAnnoOrdine().trim() +"-" + oggettoDettVO.getCodOrdine().trim() ;
								}
								// deve esserci un unico risultato
								// controlli congruenza
								// controllo che il bilancio e il fornitore dell'ordine siano quelli del buono
								controlloCONGR=false;
								//|| !rsESISTEORD.getString("cod_mat").equals(buonoOrd.getBilancio().getCodice3().trim())
								//if (!rsESISTEORD.getString("esercizio").equals(buonoOrd.getBilancio().getCodice1().trim()) || !rsESISTEORD.getString("capitolo").equals(buonoOrd.getBilancio().getCodice2().trim())  )
								if (buonoOrd.isGestBil() && rsESISTEORD.getInt("id_capitoli_bilanci")!=buonoOrd.getIDBil() && (oggettoDettVO.getTipoOrdine().equals("A") || oggettoDettVO.getTipoOrdine().equals("V") ))
								{
									throw new ValidationException("ordinierroreCampoBilancioIncongruente",
									ValidationExceptionCodici.ordinierroreCampoBilancioIncongruente);
								}

								if (!rsESISTEORD.getString("cod_fornitore").equals(buonoOrd.getFornitore().getCodice().trim())  )
								{
									throw new ValidationException("ordinierroreCampoFornitoreIncongruente",
											ValidationExceptionCodici.ordinierroreCampoFornitoreIncongruente);
								}

								// Gli ordini di buono devono essere in uno stato di aperto o stampato

								if (!rsESISTEORD.getString("stato_ordine").equals("A") && !rsESISTEORD.getString("stato_ordine").equals("S"))
								{
									throw new ValidationException("ordinierroreOrdineBuonoNOTApertooStampato",
											ValidationExceptionCodici.ordinierroreOrdineBuonoNOTApertooStampato);
								}
								controlloCONGR=true;
							}
							rsESISTEORD.close();
							pstmtESISTEORD.close();
							if (numRighe==1)
							{

								if (controlloCONGR)
								{
									if  (!BOCollegato.equals(""))
									{
										// l'ordine è legato ad un buono
										throw new ValidationException("ordinierroreOrdineBuonoOrdineLegato" ,
												ValidationExceptionCodici.ordinierroreOrdineBuonoOrdineLegato, "msgSez", OrdBOCollegato , BOCollegato);

									}


									if (valRitornoINS)
									{
										// si procede con l'inserimento dell'ordine del buono
										String sqlSub2="insert into tra_elementi_buono_ordine values ( " ;
										sqlSub2 = sqlSub2+  "'" + buonoOrd.getCodPolo() + "'" ;  // cod_polo
										sqlSub2 = sqlSub2+  ",'" + buonoOrd.getCodBibl() + "'" ;  // cd_bib
										sqlSub2 = sqlSub2+  ",'" +  buonoOrd.getNumBuonoOrdine() + "'" ;  // buono_ord
										sqlSub2 = sqlSub2+  ",'" +  oggettoDettVO.getTipoOrdine() + "'" ;  // cod_tip_ord
										sqlSub2 = sqlSub2+ "," + oggettoDettVO.getAnnoOrdine();  // anno_ord
										sqlSub2 = sqlSub2+ "," +  oggettoDettVO.getCodOrdine() ;  // cod_ord
										sqlSub2 = sqlSub2 + ",'" + buonoOrd.getUtente() + "'" ;   // ute_ins
										sqlSub2 = sqlSub2 + ",'" + ts + "'" ;   // ts_ins
										sqlSub2 = sqlSub2 + ",'" + buonoOrd.getUtente() + "'" ;   // ute_var
										sqlSub2 = sqlSub2 + ",'" + ts + "'";   // ts_var
										sqlSub2 = sqlSub2 + ",'N'";   // fl_canc
										sqlSub2 = sqlSub2+ ")" ;
										pstmt = connection.prepareStatement(sqlSub2);
										int intRetINSLOOP = 0;
										intRetINSLOOP = pstmt.executeUpdate();
										pstmt.close();
										if (intRetINSLOOP!=1){
											valRitornoINSLOOP=true;
										}
									}
								}

							}
							else
							{
								valRitornoINSLOOP=true;
								// procedere con il delete del buono sopra creato
								throw new ValidationException("ordineNONtrovato",
										ValidationExceptionCodici.ordineNONtrovato);

							}

					}
				}
				rs.close();
				connection.close();

				// impostazione del codice di ritorno finale
				if (valRitornoINS)
				{
					valRitorno=true;
					if (!valRitornoINSLOOP && buonoOrd.getListaOrdiniBuono()!=null && buonoOrd.getListaOrdiniBuono().size()>0)
					{
							valRitorno=true;
					}
					if (valRitornoINSLOOP)
					{
						valRitorno=false;
					}
				}
			}// chiude if esistenza buono
		  }  // chiude else cancellazione logica


		}catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

		return valRitorno;
	}

	public boolean  modificaBuonoOrd(BuoniOrdineVO buonoOrd) throws DataException, ApplicationException , ValidationException
	{
		Validazione.ValidaBuoniOrdineVO (this, buonoOrd);
		boolean valRitorno=false;
    	int motivo=0;
    	//FornitoreVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtUDP = null;
		PreparedStatement pstmtUDP2 = null;
		PreparedStatement pstmtDEL = null;
		PreparedStatement pstmtINS = null;
		PreparedStatement pstmtExistOrd = null;
		PreparedStatement pstmtUPDCambioStato = null;

		ResultSet rsExistOrd= null;
		ResultSet rs = null;
		ResultSet rsSub = null;
		//ResultSet rsInsTit = null;

		PreparedStatement pstmtVecchiOrdini = null;
		ResultSet rsVecchiOrdini= null;

		boolean controlloCONGR=false;
		ResultSet rsESISTEORD = null;
		PreparedStatement pstmtESISTEORD = null;

		boolean valRitornoUPD=false;
		boolean valRitornoINSLOOP=false;
		boolean valRitornoCANC=false;
		boolean valRitornoINS=false;
		//boolean valRitornoInsTit=false;

		try {
			connection = getConnection();
			List<OrdiniVO> listaOrdiniBuono = buonoOrd.getListaOrdiniBuono();
			String numBuonoOrdine = buonoOrd.getNumBuonoOrdine();
			if (ValidazioneDati.isFilled(listaOrdiniBuono) ) {

				for (OrdiniVO ordine : listaOrdiniBuono) {
					// controllo esistenza ordine
					String sqlESISTEORD="select distinct ord.* , ordbo.buono_ord from tba_ordini ord " ;
					sqlESISTEORD= sqlESISTEORD + " left join  tra_elementi_buono_ordine ordbo on ordbo.cd_polo=ord.cd_polo and  ordbo.cd_bib=ord.cd_bib and ordbo.cod_tip_ord=ord.cod_tip_ord  and ordbo.anno_ord=ord.anno_ord and ordbo.cod_ord=ord.cod_ord and ordbo.fl_canc<>'S' " ;
					sqlESISTEORD=sqlESISTEORD + " where ord.fl_canc<>'S'";
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " ord.cd_polo='" + buonoOrd.getCodPolo() +"'";
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " ord.cd_bib='" + buonoOrd.getCodBibl() +"'";
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " ord.cod_tip_ord='" + ordine.getTipoOrdine() +"'";
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " ord.anno_ord=" + ordine.getAnnoOrdine() ;
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " ord.cod_ord=" +  ordine.getCodOrdine() ;

					pstmtESISTEORD = connection.prepareStatement(sqlESISTEORD);
					rsESISTEORD = pstmtESISTEORD.executeQuery(); // va in errore se non può restituire un recordset
					int numRigheOrd=0;
					String BOCollegato="";
					String OrdBOCollegato="";


					while (rsESISTEORD.next()) {
						numRigheOrd++;
						// SOLO SU MODIFICA BUONO: condizione su diversità di codice aggiunta  per escludere gli ordini già associati al buono in esame
						String numBuono = ValidazioneDati.trimOrEmpty(rsESISTEORD.getString("buono_ord"));
						if (!numBuono.equals(numBuonoOrdine))	{

							BOCollegato = numBuono;
							OrdBOCollegato = ordine.getChiaveOrdine();

						}
						// deve esserci un unico risultato
						// controlli congruenza
						// controllo che il bilancio e il fornitore dell'ordine siano quelli del buono
						controlloCONGR=false;
						//|| !rsESISTEORD.getString("cod_mat").equals(buonoOrd.getBilancio().getCodice3().trim())
						//if ((!rsESISTEORD.getString("esercizio").equals(buonoOrd.getBilancio().getCodice1().trim()) || !rsESISTEORD.getString("capitolo").equals(buonoOrd.getBilancio().getCodice2().trim())) && (oggettoDettVO.getTipoOrdine().equals("A") || oggettoDettVO.getTipoOrdine().equals("V") ) )
						if (buonoOrd.isGestBil() && rsESISTEORD.getInt("id_capitoli_bilanci")!=buonoOrd.getIDBil() && (ordine.getTipoOrdine().equals("A") || ordine.getTipoOrdine().equals("V") ))
						{
							throw new ValidationException("ordinierroreCampoBilancioIncongruente",
							ValidationExceptionCodici.ordinierroreCampoBilancioIncongruente);
						}

						if (!rsESISTEORD.getString("cod_fornitore").equals(buonoOrd.getFornitore().getCodice().trim())  )
						{
							throw new ValidationException("ordinierroreCampoFornitoreIncongruente",
									ValidationExceptionCodici.ordinierroreCampoFornitoreIncongruente);
						}

						// Gli ordini di buono devono essere in uno stato di aperto o stampato

						if (!rsESISTEORD.getString("stato_ordine").equals("A") && !rsESISTEORD.getString("stato_ordine").equals("S"))
						{
							throw new ValidationException("ordinierroreOrdineBuonoNOTApertooStampato",
									ValidationExceptionCodici.ordinierroreOrdineBuonoNOTApertooStampato);
						}
					}
					rsESISTEORD.close();
					pstmtESISTEORD.close();
					// SOLO SU MODIFICA BUONO: modificata la condizione su numRigheOrd>0 invece di numRigheOrd==1
					if (numRigheOrd>0)
					{
						if  (!BOCollegato.equals(""))
						{
							// l'ordine è legato ad un buono
							throw new ValidationException("ordinierroreOrdineBuonoOrdineLegato" ,
									ValidationExceptionCodici.ordinierroreOrdineBuonoOrdineLegato, "msgSez", OrdBOCollegato , BOCollegato);

						}
						controlloCONGR=true;
					}
					else
					{
						controlloCONGR=false;
						// procedere con il delete del buono sopra creato
						throw new ValidationException("ordineNONtrovato",
								ValidationExceptionCodici.ordineNONtrovato);

					}
				}
			}
			if (controlloCONGR)
			{

				if (buonoOrd.getFornitore().getCodice()!=null && buonoOrd.getFornitore().getCodice()!=null  && buonoOrd.getFornitore().getCodice().trim().length()!=0)
				{
					// subquery per test di esistenza fornitore : da sostituire con metodo ancora da scrivere in this
					String sqlSub="select * from tbr_fornitori forn  ";
					sqlSub=sqlSub + " join tbr_fornitori_biblioteche fornBibl on fornBibl.cod_fornitore=forn.cod_fornitore  and fornBibl.cd_biblioteca='" + buonoOrd.getCodBibl()+"' and fornBibl.cd_polo='" + buonoOrd.getCodPolo()+"' and fornBibl.fl_canc<>'S'" ;
					sqlSub=sqlSub + " where forn.fl_canc<>'S'";
					sqlSub=sqlSub + " and forn.cod_fornitore=" + buonoOrd.getFornitore().getCodice().trim();

					pstmt = connection.prepareStatement(sqlSub);
					rsSub = pstmt.executeQuery();
					if (!rsSub.next()) {
						motivo=6;
						throw new ValidationException("ordineIncongruenzaFornitoreInesistente",
								ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);
					}

					rsSub.close();
				}

				// CONTROLLI PREVENTIVI
				String sql0="select  bor.cd_bib, bor.buono_ord, bor.stato_buono, bor.cod_fornitore, bor.id_capitoli_bilanci, bor.cod_mat,  TO_CHAR(bor.data_buono,'dd/MM/yyyy') as data_buono_str   ";
				sql0=sql0  + " from  tba_buono_ordine bor ";
				sql0=sql0  + " join tra_elementi_buono_ordine ordbo on ordbo.cd_polo=bor.cd_polo and  ordbo.cd_bib=bor.cd_bib and ordbo.buono_ord=bor.buono_ord  and ordbo.fl_canc<>'S'";

				sql0=this.struttura(sql0);
				sql0=sql0 + " bor.fl_canc<>'S'";

				if (buonoOrd.getCodPolo() !=null &&  buonoOrd.getCodPolo().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0 + " bor.cd_polo='" + buonoOrd.getCodPolo() +"'";
				}

				if (buonoOrd.getCodBibl() !=null &&  buonoOrd.getCodBibl().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0 + " bor.cd_bib='" + buonoOrd.getCodBibl() +"'";
				}

				if (numBuonoOrdine!=null && numBuonoOrdine.length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0 + " bor.buono_ord = '" +  numBuonoOrdine + "'";
				}

				pstmt = connection.prepareStatement(sql0);
				rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
				// numero di righe del resultset
				int numRighe=0;
				List<StrutturaCombo> righeOggetto=new ArrayList();
				Timestamp ts = DaoManager.now();
				while (rs.next()) {
					numRighe=numRighe+1;
					if (numRighe==1)
					{
						String sqlUDP="update tba_buono_ordine set " ;
						//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
						sqlUDP= sqlUDP  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
						sqlUDP= sqlUDP  + ", ute_var = '" +  buonoOrd.getUtente()  + "'" ;  // ute_var
						sqlUDP= sqlUDP + ", data_buono =TO_DATE('"+ buonoOrd.getDataBuonoOrdine().trim()+"','dd/MM/yyyy') "  ;  // data_buono
						sqlUDP= sqlUDP + ", cod_fornitore = " + buonoOrd.getFornitore().getCodice() ;  // cod_fornitore
						sqlUDP= sqlUDP + ", id_capitoli_bilanci = " + buonoOrd.getIDBil() ;  // id_capitoli_bilanci
						sqlUDP= sqlUDP + ", stato_buono ='" + buonoOrd.getStatoBuonoOrdine().trim()  + "'"  ;  // stato

						sqlUDP=this.struttura(sqlUDP);
						sqlUDP=sqlUDP + " fl_canc<>'S'";

						if (buonoOrd.getCodPolo() !=null &&  buonoOrd.getCodPolo().length()!=0)
						{
							sqlUDP=this.struttura(sqlUDP);
							sqlUDP=sqlUDP + " cd_polo='" + buonoOrd.getCodPolo() +"'";
						}

						if (numBuonoOrdine!=null && numBuonoOrdine.length()!=0)
						{
							sqlUDP=this.struttura(sqlUDP);
							sqlUDP=sqlUDP + " buono_ord = '" +  numBuonoOrdine + "'";
						}

						if (buonoOrd.getDataUpd()!=null )
						{
							sqlUDP=this.struttura(sqlUDP);
							sqlUDP=sqlUDP + " ts_var='" + buonoOrd.getDataUpd() + "'" ;

						}

						pstmtUDP = connection.prepareStatement(sqlUDP);
						log.debug("Debug: modifica buono ordine");
						log.debug("Debug: " + sqlUDP);
						int intRetUDP = 0;
						intRetUDP = pstmtUDP.executeUpdate();
						pstmtUDP.close();
						// fine estrazione codice
						if (intRetUDP==1){
							valRitornoUPD=true;
							}
						else
						{
							throw new ValidationException("operazioneInConcorrenza",
									ValidationExceptionCodici.operazioneInConcorrenza);

						}

					}
					if (valRitornoUPD)
					{
						if (listaOrdiniBuono!=null && listaOrdiniBuono.size() >0 )
						{


							// cancello i preesistenti ordini associati ed associo i nuovi
							//String sqlDEL1="delete from tra_elementi_buono_ordine ";
							// cancellazione logica
							String sqlDEL1="update tra_elementi_buono_ordine set " ;
							//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
							sqlDEL1=sqlDEL1 + " ts_var = '" +  ts  + "'" ;  // ex data_agg
							sqlDEL1=sqlDEL1 + ", ute_var = '" +  buonoOrd.getUtente()  + "'" ;  // ute_var
							sqlDEL1=sqlDEL1 + ", fl_canc = 'S'" ;  // fl_canc
							sqlDEL1=this.struttura(sqlDEL1);
							sqlDEL1=sqlDEL1 + " fl_canc<>'S'";

							if (buonoOrd.getCodPolo() !=null &&  buonoOrd.getCodPolo().length()!=0)
							{
								sqlDEL1=this.struttura(sqlDEL1);
								sqlDEL1=sqlDEL1 + " cd_polo='" + buonoOrd.getCodPolo() +"'";
							}

							if (buonoOrd.getCodBibl() !=null &&  buonoOrd.getCodBibl().length()!=0)
							{
								sqlDEL1=this.struttura(sqlDEL1);
								sqlDEL1=sqlDEL1 + " cd_bib='" + buonoOrd.getCodBibl() +"'";
							}

							if (numBuonoOrdine!=null && numBuonoOrdine.length()!=0)
							{
								sqlDEL1=this.struttura(sqlDEL1);
								sqlDEL1=sqlDEL1 + " buono_ord = '" +  numBuonoOrdine + "'";
							}
							pstmtDEL = connection.prepareStatement(sqlDEL1);
							int intRetCANC = 0;
							intRetCANC = pstmtDEL.executeUpdate();
							pstmtDEL.close();
							if (intRetCANC>0){
								valRitornoCANC=true;
							}
							if (valRitornoCANC)
							{
								valRitornoINSLOOP=false;

								List<Integer> idOrdiniBOvecchi=new ArrayList<Integer>();
								if (buonoOrd.getStatoBuonoOrdine().equals("S"))
								{
									// gestione delle interrelazioni fra ordini e buoni ordine già stampati
									// valutazioni su stato ordini da cambiare se buono ordine è già stato stampato
									// effettuare i controlli dell'esistenza del nuovo ordine di riga fra quelli già esistenti(canc. logic) ovvero con flag='s'
									// se non esiste è un nuovo inserimento
									// se esiste si incrementa un contatore TDiff
									// sel il numero dei flaggati è > del contatore TDiff allora ci sono dei cancellati (tali ordini devono passare da stampato ad aperto)
									// creare degli array di id ordini cancellati

									String sqlVecchiOrdini="select ord.id_ordine, ord.cod_ord, ord.cod_tip_ord, ord.anno_ord from tba_ordini ord " ;
									sqlVecchiOrdini= sqlVecchiOrdini + " join  tra_elementi_buono_ordine ordbo on ordbo.cd_polo=ord.cd_polo and  ordbo.cd_bib=ord.cd_bib and ordbo.cod_tip_ord=ord.cod_tip_ord  and ordbo.anno_ord=ord.anno_ord and ordbo.cod_ord=ord.cod_ord and ordbo.fl_canc<>'S' " ; // and ordbo.fl_canc<>'S'
									sqlVecchiOrdini= sqlVecchiOrdini + " join  tba_buono_ordine bor on ordbo.cd_polo=bor.cd_polo and  ordbo.cd_bib=bor.cd_bib and ordbo.buono_ord=bor.buono_ord and bor.fl_canc<>'S'";
									sqlVecchiOrdini=this.struttura(sqlVecchiOrdini);
									sqlVecchiOrdini= sqlVecchiOrdini + "  ordbo.fl_canc='S'";
									sqlVecchiOrdini=this.struttura(sqlVecchiOrdini);
									sqlVecchiOrdini= sqlVecchiOrdini + " ord.cd_polo='" + buonoOrd.getCodPolo() +"'";
									sqlVecchiOrdini=this.struttura(sqlVecchiOrdini);
									sqlVecchiOrdini= sqlVecchiOrdini + " ord.cd_bib='" + buonoOrd.getCodBibl() +"'";
									sqlVecchiOrdini=this.struttura(sqlVecchiOrdini);
									sqlVecchiOrdini= sqlVecchiOrdini + " ordbo.buono_ord='" + numBuonoOrdine.trim() +"'";
									sqlVecchiOrdini=this.struttura(sqlVecchiOrdini);
									sqlVecchiOrdini= sqlVecchiOrdini + " bor.stato_buono='S'";

									pstmtVecchiOrdini = connection.prepareStatement(sqlVecchiOrdini);
									rsVecchiOrdini = pstmtVecchiOrdini.executeQuery(); // va in errore se non può restituire un recordset
									// numero di righe del resultset
									int numVecchiOrdini=0;
									//List<StrutturaQuater> idOrdiniBOvecchi=new ArrayList<StrutturaQuater>();
									idOrdiniBOvecchi=new ArrayList<Integer>();

									while (rsVecchiOrdini.next())
									{
										numVecchiOrdini=numVecchiOrdini+1;
										//eleArr=new StrutturaQuater(rsVecchiOrdini.getInt("id_ordine"), rsVecchiOrdini.getString("cod_tip_ord"), rsVecchiOrdini.getInt("anno_ord"), rsVecchiOrdini.getInt("cod_ord"));
										int eleArr=rsVecchiOrdini.getInt("id_ordine");
										idOrdiniBOvecchi.add(eleArr);
									}
									rsVecchiOrdini.close();
									pstmtVecchiOrdini.close();

								}

								for (int i=0; i<listaOrdiniBuono.size(); i++)
								{


									OrdiniVO oggettoDettVO=listaOrdiniBuono.get(i);
									// visto che gli inserimenti avvengono uno alla volta in ciclo, basta confrontare l'elemento i-esimo con l'esistenza di se stesso già sulla tabella per scoprire i ripetuti

									String sqlEsisteOrd="select * from tra_elementi_buono_ordine " ;

									sqlEsisteOrd=this.struttura(sqlEsisteOrd);
									sqlEsisteOrd=sqlEsisteOrd+ " fl_canc<>'S'";

									if (buonoOrd.getCodPolo() !=null &&  buonoOrd.getCodPolo().trim().length()!=0)
									{
										sqlEsisteOrd=this.struttura(sqlEsisteOrd);
										sqlEsisteOrd=sqlEsisteOrd+ " cd_polo='" + buonoOrd.getCodPolo().trim() +"'";
									}
									if (buonoOrd.getCodBibl() !=null &&  buonoOrd.getCodBibl().trim().length()!=0)
									{
										sqlEsisteOrd=this.struttura(sqlEsisteOrd);
										sqlEsisteOrd=sqlEsisteOrd+ " cd_bib='" + buonoOrd.getCodBibl() +"'";
									}
									if ( numBuonoOrdine !=null &&   numBuonoOrdine.trim().length()!=0)
									{
										sqlEsisteOrd=this.struttura(sqlEsisteOrd);
										sqlEsisteOrd=sqlEsisteOrd+ " buono_ord='" +  numBuonoOrdine.trim() +"'";
									}
									if ( oggettoDettVO.getTipoOrdine() !=null &&   oggettoDettVO.getTipoOrdine().trim().length()!=0)
									{
										sqlEsisteOrd=this.struttura(sqlEsisteOrd);
										sqlEsisteOrd=sqlEsisteOrd+ " cod_tip_ord='" +  oggettoDettVO.getTipoOrdine().trim() +"'";
									}
									if ( oggettoDettVO.getAnnoOrdine() !=null &&   oggettoDettVO.getAnnoOrdine().trim().length()!=0)
									{
										sqlEsisteOrd=this.struttura(sqlEsisteOrd);
										sqlEsisteOrd=sqlEsisteOrd+ " anno_ord=" +  oggettoDettVO.getAnnoOrdine().trim() ;
									}
									if ( oggettoDettVO.getCodOrdine() !=null &&   oggettoDettVO.getCodOrdine().trim().length()!=0)
									{
										sqlEsisteOrd=this.struttura(sqlEsisteOrd);
										sqlEsisteOrd=sqlEsisteOrd+ " cod_ord=" +  oggettoDettVO.getCodOrdine().trim() ;
									}
									pstmtExistOrd = connection.prepareStatement(sqlEsisteOrd);
									rsExistOrd = pstmtExistOrd.executeQuery(); // va in errore se non può restituire un recordset
									// numero di righe del resultset
									int numRigheEsisteOrd=0;
									while (rsExistOrd.next()) {
										numRigheEsisteOrd=numRigheEsisteOrd+1;
									}
									rsExistOrd.close() ;
									pstmtExistOrd.close() ;
									if (numRigheEsisteOrd>0)
									{
										throw new ValidationException("ordinierroreOrdineRipetuto",
												ValidationExceptionCodici.ordinierroreOrdineRipetuto);
									}
									else
									{
										// cancellazione fisica	di quelli precedentemente cancellati logicamente
										String sqlDEL2="delete from tra_elementi_buono_ordine ";

										sqlDEL2=this.struttura(sqlDEL2);
										sqlDEL2=sqlDEL2 + " fl_canc = 'S'" ;  // fl_canc
										if (buonoOrd.getCodPolo() !=null &&  buonoOrd.getCodPolo().length()!=0)
										{
											sqlDEL2=this.struttura(sqlDEL2);
											sqlDEL2=sqlDEL2   + " cd_polo='" + buonoOrd.getCodPolo() +"'";
										}

										if (buonoOrd.getCodBibl() !=null &&  buonoOrd.getCodBibl().length()!=0)
										{
											sqlDEL2=this.struttura(sqlDEL2);
											sqlDEL2=sqlDEL2   + " cd_bib='" + buonoOrd.getCodBibl() +"'";
										}
										if (numBuonoOrdine!=null && numBuonoOrdine.length()!=0)
										{
											sqlDEL2=this.struttura(sqlDEL2);
											sqlDEL2=sqlDEL2 + " buono_ord = '" +  numBuonoOrdine.trim() + "'";
										}


										if (oggettoDettVO.getTipoOrdine()!=null && oggettoDettVO.getTipoOrdine().trim().length()!=0)
										{
											sqlDEL2=this.struttura(sqlDEL2);
											sqlDEL2=sqlDEL2   + " cod_tip_ord='" + oggettoDettVO.getTipoOrdine().trim() +"'";
										}

										if (oggettoDettVO.getAnnoOrdine()!=null && oggettoDettVO.getAnnoOrdine().trim().length()!=0)
										{
											sqlDEL2=this.struttura(sqlDEL2);
											sqlDEL2=sqlDEL2   + " anno_ord=" + oggettoDettVO.getAnnoOrdine().trim() ;
										}
										if (oggettoDettVO.getCodOrdine()!=null && oggettoDettVO.getCodOrdine().trim().length()!=0)
										{
											sqlDEL2=this.struttura(sqlDEL2);
											sqlDEL2=sqlDEL2   + " cod_ord=" + oggettoDettVO.getCodOrdine().trim() ;
										}

										pstmtUDP2 = connection.prepareStatement(sqlDEL2);
										int intRetCANC2 = 0;
										intRetCANC2 = pstmtUDP2.executeUpdate();
										pstmtUDP2.close();

										// inserimento

										String sqlSub2="insert into tra_elementi_buono_ordine values ( " ;
										sqlSub2 = sqlSub2+  "'" + buonoOrd.getCodPolo() + "'" ;  // cod_polo
										sqlSub2 = sqlSub2+  ",'" + buonoOrd.getCodBibl() + "'" ;  // cd_bib
										sqlSub2 = sqlSub2+  ",'" +  numBuonoOrdine.trim() + "'" ;  // buono_ord
										sqlSub2 = sqlSub2+  ",'" +  oggettoDettVO.getTipoOrdine().trim() + "'" ;  // cod_tip_ord
										sqlSub2 = sqlSub2+ "," + oggettoDettVO.getAnnoOrdine().trim();  // anno_ord
										sqlSub2 = sqlSub2+ "," +  oggettoDettVO.getCodOrdine().trim() ;  // cod_ord
										sqlSub2 = sqlSub2 + ",'" + buonoOrd.getUtente() + "'" ;   // ute_ins
										sqlSub2 = sqlSub2 + ",'" + ts + "'" ;   // ts_ins
										sqlSub2 = sqlSub2 + ",'" + buonoOrd.getUtente() + "'" ;   // ute_var
										sqlSub2 = sqlSub2 + ",'" + ts + "'";   // ts_var
										sqlSub2 = sqlSub2 + ",'N'";   // fl_canc
										sqlSub2 = sqlSub2+ ")" ;
										pstmtINS = connection.prepareStatement(sqlSub2);
										int intRetINSLOOP = 0;
										intRetINSLOOP = pstmtINS.executeUpdate();
										pstmtINS.close();
										if (intRetINSLOOP!=1){
											valRitornoINSLOOP=true;
										}
									}

								} // fine del for

								if (buonoOrd.getStatoBuonoOrdine().equals("S"))
								{

									// cambio stato degli ordini cancellati del buono stampato da stampato ad aperto

									List<Integer> idOrdiniBOCancellati=new ArrayList<Integer>();
									int numCanc=0;
									String listID="(";
									for (int y=0; y<idOrdiniBOvecchi.size(); y++)
									{
										Integer eleOrdOld=idOrdiniBOvecchi.get(y);
										Boolean cancellato=true;
										for (int t=0; t<listaOrdiniBuono.size(); t++)
										{
											OrdiniVO oggettoDettVO=listaOrdiniBuono.get(t);
											if (eleOrdOld==oggettoDettVO.getIDOrd())
											{
												cancellato=false;
												break;
											}
										}
										if (cancellato)
										{
											numCanc=numCanc+1;
											if (numCanc>1)
											{
												listID=listID+",";
											}
											idOrdiniBOCancellati.add(eleOrdOld);
											listID=listID+eleOrdOld ;
										}
									}
									listID=listID+")";

									if (idOrdiniBOCancellati.size()>0 && !listID.equals("()") )
									{
										String sqlUPDCambioStato="update tba_ordini set " ;
										sqlUPDCambioStato=sqlUPDCambioStato + " ts_var = '" +  ts  + "'" ;  // ex data_agg
										sqlUPDCambioStato=sqlUPDCambioStato  + ", ute_var = '" +  buonoOrd.getUtente()  + "'" ;  // ute_var
										//sqlUPDCambioStato=sqlUPDCambioStato  + ", stato_ordine = 'A'" ;
										sqlUPDCambioStato=sqlUPDCambioStato  + ", stampato = false" ;

										sqlUPDCambioStato=this.struttura(sqlUPDCambioStato);
										sqlUPDCambioStato=sqlUPDCambioStato  + " fl_canc<>'S'";
										sqlUPDCambioStato=this.struttura(sqlUPDCambioStato);
										sqlUPDCambioStato=sqlUPDCambioStato  + " stampato=true";

										sqlUPDCambioStato=this.struttura(sqlUPDCambioStato);
										sqlUPDCambioStato=sqlUPDCambioStato + " id_ordine in " + listID ;
										pstmtUPDCambioStato = connection.prepareStatement(sqlUPDCambioStato);
										int intRetUPDCambioStato = 0;
										intRetUPDCambioStato = pstmtUPDCambioStato.executeUpdate();
										pstmtUPDCambioStato.close();

									}

								}

							}
						}
					}
				else
				{
					motivo=2; // record forse già esistente quindi non inseribile
					throw new ValidationException("fornitoreInserimentoErroreBaseDati",
							ValidationExceptionCodici.fornitoreInserimentoErroreBaseDati);

				}


			}
			rs.close();
			pstmt.close();
			connection.close();

			// impostazione del codice di ritorno finale


			if (valRitornoUPD)
			{
				valRitorno=true;
				if (listaOrdiniBuono!=null && listaOrdiniBuono.size() >0)
				{
					if (!valRitornoINSLOOP && valRitornoCANC )
					{
						valRitorno=true;
					}
					else
					{
						valRitorno=false;
					}
				}
			}
		}

	}catch (ValidationException e) {
      	  throw e;

	} catch (Exception e) {
		log.error("", e);
	} finally {
		close(connection);
	}

    return valRitorno;
}

	public boolean  cancellaBilancio(BilancioVO bilancio) throws DataException, ApplicationException, ValidationException
	{
		//Validazione.ValidaBilancioVO(bilancio);
		int valRitornoInt=0;
    	int motivo=0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtDEL = null;
		PreparedStatement pstmtDEL2 = null;
		ResultSet rs = null;
		ResultSet rsDEL = null;
		boolean valRitorno=false;
		boolean valRitornoDEL=false;
		boolean  valRitornoCANC=false;
		boolean  valRitornoCANC2=false;
		boolean  esistenzaLegami=false;
		Timestamp ts = DaoManager.now();

		try {
			connection = getConnection();
			// CONTROLLI PREVENTIVI
			String sql0="select distinct capBil.id_capitoli_bilanci";
			sql0=sql0 + " from  tbb_capitoli_bilanci capBil ";
			sql0=sql0 + " join tbb_bilanci bil on capBil.id_capitoli_bilanci=bil.id_capitoli_bilanci and bil.fl_canc<>'S'  ";

			sql0=this.struttura(sql0);
			sql0=sql0 + " capBil.fl_canc<>'S' ";

			if (bilancio.getIDBil()>0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " capBil.id_capitoli_bilanci=" + bilancio.getIDBil() ;
			}

			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;
			}
			if (numRighe==1) // esiste
			{

				// controllo esistenza legami con ordini e buoni ordine
				String sql1="select distinct capBil.id_capitoli_bilanci, ord.id_ordine, buonoOrd.id_buono_ordine ";
				sql1=sql1 + " from  tbb_capitoli_bilanci capBil ";
				sql1=sql1 + "left join tba_ordini ord on ord.id_capitoli_bilanci=capBil.id_capitoli_bilanci and ord.fl_canc<>'S'  and ord.stato_ordine <> 'N'";
				sql1=sql1 + "left join tba_buono_ordine buonoOrd on buonoOrd.id_capitoli_bilanci=capBil.id_capitoli_bilanci and  buonoOrd.fl_canc<>'S'  ";
				sql1=this.struttura(sql1);
				sql1=sql1 + " capBil.fl_canc<>'S'";

				if (bilancio.getIDBil()>0)
				{
					sql1=this.struttura(sql1);
					sql1=sql1 + " capBil.id_capitoli_bilanci=" + bilancio.getIDBil() ;
				}

				pstmt = connection.prepareStatement(sql1);
				rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
				// numero di righe del resultset
				int numRighe2=0;
				while (rs.next()) {
					numRighe2=numRighe2+1;
					if (rs.getString("id_ordine")!=null || rs.getString("id_buono_ordine")!=null  )
					{
						esistenzaLegami=true;
					}
				}
				rs.close();
				if (!esistenzaLegami && numRighe2>0) // non ci sono legami con ordini,  e BUONI
				{
					// cancello i preesistenti impegni associati
					//String sqlDEL1="delete from tba_righe_fatture ";
					// cancellazione logica
					String sqlDEL1="update tbb_bilanci set " ;
					//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
					sqlDEL1=sqlDEL1  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
					sqlDEL1=sqlDEL1  + ", ute_var = '" +  bilancio.getUtente()  + "'" ;  // ute_var
					sqlDEL1=sqlDEL1  + ", fl_canc = 'S'" ;  // fl_canc
					sqlDEL1=this.struttura(sqlDEL1);
					sqlDEL1=sqlDEL1 + " fl_canc<>'S'";
					if (bilancio.getIDBil()>0)
					{
						sqlDEL1=this.struttura(sqlDEL1);
						sqlDEL1=sqlDEL1 + " id_capitoli_bilanci=" + bilancio.getIDBil() ;
					}
				//
					pstmtDEL = connection.prepareStatement(sqlDEL1);
					int intRetCANC = 0;
					//valRitornoCANC = pstmtDEL.execute();
					intRetCANC = pstmtDEL.executeUpdate();
					pstmtDEL.close();
					if (intRetCANC>=1){
						valRitornoCANC=true;
					}

					if (valRitornoCANC)
					{
						//String sqlDEL2="delete from tba_fatture ";
						// cancellazione logica
						String sqlDEL2="update tbb_capitoli_bilanci set " ;
						//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
						sqlDEL2=sqlDEL2  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
						sqlDEL2=sqlDEL2  + ", ute_var = '" +  bilancio.getUtente()  + "'" ;  // ute_var
						sqlDEL2=sqlDEL2  + ", fl_canc = 'S'" ;  // fl_canc
						sqlDEL2=this.struttura(sqlDEL2);
						sqlDEL2=sqlDEL2 + " fl_canc<>'S'";
						if (bilancio.getIDBil()>0)
						{
							sqlDEL2=this.struttura(sqlDEL2);
							sqlDEL2=sqlDEL2 + " id_capitoli_bilanci=" + bilancio.getIDBil() ;
						}


						pstmtDEL2 = connection.prepareStatement(sqlDEL2);
						int intRetCANC2 = 0;
						intRetCANC2 = pstmtDEL2.executeUpdate();
						pstmtDEL2.close();
						// oppure == numRighe
						if (intRetCANC2==1){
							valRitornoCANC2=true;
						}
/*						else
						{
							throw new ValidationException("operazioneInConcorrenza",
									ValidationExceptionCodici.operazioneInConcorrenza);

						}*/

					}
				}
				else
				{
					// impedire la cancellazione (emettere messaggio specifico)
					throw new ValidationException("erroreCancellaBilancio",
							ValidationExceptionCodici.erroreCancellaBilancio);

				}

				if (valRitornoCANC2)
				{
					valRitorno=true;
				}
			}
			else
			{
				motivo=2; // record forse già esistente quindi non inseribile
				throw new ValidationException("fornitoreInserimentoErroreBaseDati",
						ValidationExceptionCodici.fornitoreInserimentoErroreBaseDati);

			}

			connection.close();

		}catch (ValidationException e) {
			throw e;
		}
		 catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

     return valRitorno;
	}


	public boolean  cancellaBuonoOrd(BuoniOrdineVO buonoOrd) throws DataException, ApplicationException, ValidationException
	{
		//Validazione.ValidaBuoniOrdineVO (buonoOrd);
		int valRitornoInt=0;
    	int motivo=0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtDEL = null;
		PreparedStatement pstmtDEL2 = null;
		ResultSet rs = null;
		ResultSet rsDEL = null;
		boolean valRitorno=false;
		boolean valRitornoDEL=false;
		boolean  valRitornoCANC=false;
		boolean  valRitornoCANC2=false;
		Timestamp ts = DaoManager.now();

		try {
			connection = getConnection();
			// CONTROLLI PREVENTIVI
			// CONTROLLI PREVENTIVI
			String sql0="select *   ";
			sql0=sql0  + " from  tba_buono_ordine bor ";
			sql0=sql0  + " left join tra_elementi_buono_ordine ordbo on ordbo.cd_polo=bor.cd_polo and  ordbo.cd_bib=bor.cd_bib and ordbo.buono_ord=bor.buono_ord  and ordbo.fl_canc<>'S'";

			sql0=this.struttura(sql0);
			sql0=sql0 + " bor.fl_canc<>'S' ";

			if (buonoOrd.getCodPolo() !=null &&  buonoOrd.getCodPolo().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " bor.cd_polo='" + buonoOrd.getCodPolo() +"'";
			}

			if (buonoOrd.getCodBibl() !=null &&  buonoOrd.getCodBibl().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " bor.cd_bib='" + buonoOrd.getCodBibl() +"'";
			}

			if (buonoOrd.getNumBuonoOrdine()!=null && buonoOrd.getNumBuonoOrdine().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " bor.buono_ord = '" +  buonoOrd.getNumBuonoOrdine() + "'";
			}

			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;
			}
			if (numRighe>0)
			{
				// cancello i preesistenti ordini associati ed associo i nuovi
				//String sqlDEL1="delete from tra_elementi_buono_ordine ";
				String sqlDEL1="update tra_elementi_buono_ordine set " ;
				//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
				sqlDEL1=sqlDEL1  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
				sqlDEL1=sqlDEL1  + ", ute_var = '" +  buonoOrd.getUtente()  + "'" ;  // ute_var
				sqlDEL1=sqlDEL1  + ", fl_canc = 'S'" ;  // fl_canc
				sqlDEL1=this.struttura(sqlDEL1);
				sqlDEL1=sqlDEL1 + " fl_canc<>'S'";

				//almaviva5_20130114 segnalazione NAP: controllo valorizzazione chiave in cancellazione buono ordine
				if (ValidazioneDati.isFilled(buonoOrd.getCodPolo()) )
				{
					sqlDEL1=this.struttura(sqlDEL1);
					sqlDEL1=sqlDEL1 + " cd_polo='" + buonoOrd.getCodPolo() +"'";
				} else
					throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "codPolo");

				if (ValidazioneDati.isFilled(buonoOrd.getCodBibl()) )
				{
					sqlDEL1=this.struttura(sqlDEL1);
					sqlDEL1=sqlDEL1 + " cd_bib='" + buonoOrd.getCodBibl() +"'";
				} else
					throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "codBib");

				if (ValidazioneDati.isFilled(buonoOrd.getNumBuonoOrdine()) )
				{
					sqlDEL1=this.struttura(sqlDEL1);
					sqlDEL1=sqlDEL1 + " buono_ord = '" +  buonoOrd.getNumBuonoOrdine() + "'";
				} else
					throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "numBuonoOrd");
/*				if (buonoOrd.getDataUpd()!=null )
				{
					sqlDEL1=this.struttura(sqlDEL1);
					sqlDEL1=sqlDEL1 + " ts_var='" + buonoOrd.getDataUpd() + "'" ;
				}

*/				//
				pstmtDEL = connection.prepareStatement(sqlDEL1);
				int intRetCANC = 0;
				//valRitornoCANC = pstmtDEL.execute();
				intRetCANC = pstmtDEL.executeUpdate();
				pstmtDEL.close();
				if (intRetCANC==1){
					valRitornoCANC=true;
				}

				String sqlDEL2="update tba_buono_ordine set " ;

				sqlDEL2=sqlDEL2  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
				sqlDEL2=sqlDEL2  + ", ute_var = '" +  buonoOrd.getUtente()  + "'" ;  // ute_var
				sqlDEL2=sqlDEL2  + ", fl_canc = 'S'" ;  // fl_canc
				sqlDEL2=this.struttura(sqlDEL2);
				sqlDEL2=sqlDEL2 + " fl_canc<>'S'";

				if (buonoOrd.getCodPolo() !=null &&  buonoOrd.getCodPolo().length()!=0)
				{
					sqlDEL2=this.struttura(sqlDEL2);
					sqlDEL2=sqlDEL2 + " cd_polo='" + buonoOrd.getCodPolo() +"'";
				}

				if (buonoOrd.getCodBibl() !=null &&  buonoOrd.getCodBibl().length()!=0)
				{
					sqlDEL2=this.struttura(sqlDEL2);
					sqlDEL2=sqlDEL2 + " cd_bib='" + buonoOrd.getCodBibl() +"'";
				}

				if (buonoOrd.getNumBuonoOrdine()!=null && buonoOrd.getNumBuonoOrdine().length()!=0)
				{
					sqlDEL2=this.struttura(sqlDEL2);
					sqlDEL2=sqlDEL2 + " buono_ord = '" +  buonoOrd.getNumBuonoOrdine() + "'";
				}


				pstmtDEL2 = connection.prepareStatement(sqlDEL2);
				int intRetCANC2 = 0;
				intRetCANC2 = pstmtDEL2.executeUpdate();
				pstmtDEL2.close();
				// oppure == numRighe
				if (intRetCANC2==1){
					valRitornoCANC2=true;
				}

				valRitorno=true;
			}
			else
			{
				motivo=2; // record forse già esistente quindi non inseribile
				throw new ValidationException("fornitoreInserimentoErroreBaseDati",
						ValidationExceptionCodici.fornitoreInserimentoErroreBaseDati);

			}

			connection.close();

		}catch (ValidationException e) {
			throw e;
		}
		 catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

     return valRitorno;
	}

	public boolean  cancellaFattura(FatturaVO fattura) throws DataException, ApplicationException, ValidationException
	{

		int valRitornoInt=0;
    	int motivo=0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtDEL = null;
		PreparedStatement pstmtDEL2 = null;
		ResultSet rs = null;
		ResultSet rsDEL = null;
		boolean valRitorno=false;
		boolean valRitornoDEL=false;
		boolean  valRitornoCANC=false;
		boolean  valRitornoCANC2=false;
		Timestamp ts = DaoManager.now();

		try {
			connection = getConnection();
			// CONTROLLI PREVENTIVI
			String sql0="select *  ";
			sql0=sql0 + " from tba_fatture fatt ";
			sql0=sql0 + " join tba_righe_fatture fattDett on fattDett.id_fattura=fatt.id_fattura AND fattDett.fl_canc<>'S' ";

			sql0=this.struttura(sql0);
			sql0=sql0 + " fatt.fl_canc<>'S'";

			if (fattura.getIDFatt()>0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0  + " fatt.id_fattura=" + fattura.getIDFatt() ;
			}

			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;
			}
			if (numRighe>0)
			{
				// cancello i preesistenti ordini associati
				//String sqlDEL1="delete from tba_righe_fatture ";
				// cancellazione logica
				String sqlDEL1="update tba_righe_fatture set " ;

				sqlDEL1=sqlDEL1  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
				sqlDEL1=sqlDEL1  + ", ute_var = '" +  fattura.getUtente()  + "'" ;  // ute_var
				sqlDEL1=sqlDEL1  + ", fl_canc = 'S'" ;  // fl_canc
				sqlDEL1=this.struttura(sqlDEL1);
				sqlDEL1=sqlDEL1 + " fl_canc<>'S'";
				if (fattura.getIDFatt()>0)
				{
					sqlDEL1=this.struttura(sqlDEL1);
					sqlDEL1=sqlDEL1 + " id_fattura=" + fattura.getIDFatt() ;
				}


				//
				pstmtDEL = connection.prepareStatement(sqlDEL1);
				int intRetCANC = 0;
				//valRitornoCANC = pstmtDEL.execute();
				intRetCANC = pstmtDEL.executeUpdate();
				pstmtDEL.close();
				if (intRetCANC==1){
					valRitornoCANC=true;
				}
				//String sqlDEL2="delete from tba_fatture ";
				// cancellazione logica
				String sqlDEL2="update tba_fatture set " ;
				//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
				sqlDEL2=sqlDEL2  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
				sqlDEL2=sqlDEL2  + ", ute_var = '" +  fattura.getUtente()  + "'" ;  // ute_var
				sqlDEL2=sqlDEL2  + ", fl_canc = 'S'" ;  // fl_canc
				sqlDEL2=this.struttura(sqlDEL2);
				sqlDEL2=sqlDEL2 + " fl_canc<>'S'";
				if (fattura.getIDFatt()>0)
				{
					sqlDEL2=this.struttura(sqlDEL2);
					sqlDEL2=sqlDEL2 + " id_fattura=" + fattura.getIDFatt() ;
				}

				pstmtDEL2 = connection.prepareStatement(sqlDEL2);
				int intRetCANC2 = 0;
				intRetCANC2 = pstmtDEL2.executeUpdate();
				pstmtDEL2.close();
				// oppure == numRighe
				if (intRetCANC2==1){
					valRitornoCANC2=true;
				}
				valRitorno=true;
			}
			else
			{
				motivo=2; // record forse già esistente quindi non inseribile
				throw new ValidationException("fornitoreInserimentoErroreBaseDati",
						ValidationExceptionCodici.fornitoreInserimentoErroreBaseDati);

			}

			connection.close();

		}catch (ValidationException e) {
	      	  throw e;
		}
		 catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

     return valRitorno;
	}

	public boolean  cancellaProfilo(StrutturaProfiloVO profilo) throws DataException, ApplicationException, ValidationException
	{
		//Validazione.ValidaStrutturaProfiloVO(profilo);
		int valRitornoInt=0;
    	int motivo=0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtDEL = null;
		PreparedStatement pstmtDEL2 = null;
		ResultSet rs = null;
		ResultSet rsDEL = null;
		boolean valRitorno=false;
		boolean valRitornoDEL=false;
		boolean  valRitornoCANC=false;
		boolean  valRitornoCANC2=false;
		Timestamp ts = DaoManager.now();

		try {
			connection = getConnection();
			// CONTROLLI PREVENTIVI
			String sql0="select * ";  //,  prof.* ";
			sql0=sql0 + " from  tba_profili_acquisto prof ";
			sql0=sql0  + " join tra_sez_acquisizione_fornitori sezAcqForn on prof.cod_prac=sezAcqForn.cod_prac and sezAcqForn.fl_canc<>'S'";

			sql0=this.struttura(sql0);
			sql0=sql0 + " prof.fl_canc<>'S'";
			if (profilo.getCodPolo() !=null &&  profilo.getCodPolo().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0= sql0+ " sezAcqForn.cd_polo='" + profilo.getCodPolo() +"'";
			}
			if (profilo.getCodBibl() !=null &&  profilo.getCodBibl().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0  + " sezAcqForn.cd_biblioteca='" + profilo.getCodBibl() +"'";
			}
			if (profilo.getProfilo()!=null && profilo.getProfilo().getCodice().trim().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0  + " prof.cod_prac=" + profilo.getProfilo().getCodice() ;
			}
			sql0=this.struttura(sql0);
			sql0=sql0   + " prof.id_sez_acquis_bibliografiche=" + profilo.getIDSez() ;


			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;
			}
			if (numRighe>0)
			{
				// cancello i preesistenti ordini associati
				//String sqlDEL1="delete from tra_sez_acquisizione_fornitori ";
				String sqlDEL1="update tra_sez_acquisizione_fornitori set " ;
				//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
				sqlDEL1=sqlDEL1  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
				sqlDEL1=sqlDEL1  + ", ute_var = '" +  profilo.getUtente()  + "'" ;  // ute_var
				sqlDEL1=sqlDEL1  + ", fl_canc = 'S'" ;  // fl_canc
				sqlDEL1=this.struttura(sqlDEL1);
				sqlDEL1=sqlDEL1 + " fl_canc<>'S'";

				if (profilo.getCodPolo() !=null &&  profilo.getCodPolo().length()!=0)
				{
					sqlDEL1=this.struttura(sqlDEL1);
					sqlDEL1=sqlDEL1  + " cd_polo='" + profilo.getCodPolo() +"'";
				}
				if (profilo.getCodBibl() !=null &&  profilo.getCodBibl().length()!=0)
				{
					sqlDEL1=this.struttura(sqlDEL1);
					sqlDEL1=sqlDEL1  + " cd_biblioteca='" + profilo.getCodBibl() +"'";
				}
				if (profilo.getProfilo()!=null && profilo.getProfilo().getCodice().trim().length()!=0)
				{
					sqlDEL1=this.struttura(sqlDEL1);
					sqlDEL1=sqlDEL1   + " cod_prac=" + profilo.getProfilo().getCodice() ;
				}

				//
				pstmtDEL = connection.prepareStatement(sqlDEL1);
				int intRetCANC = 0;
				//valRitornoCANC = pstmtDEL.execute();
				intRetCANC = pstmtDEL.executeUpdate();
				pstmtDEL.close();
				if (intRetCANC>1){
					valRitornoCANC=true;
				}
				//String sqlDEL2="delete from tba_profili_acquisto ";

				String sqlDEL2="update tba_profili_acquisto set " ;
				//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
				sqlDEL2=sqlDEL2  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
				sqlDEL2=sqlDEL2  + ", ute_var = '" +  profilo.getUtente()  + "'" ;  // ute_var
				sqlDEL2=sqlDEL2  + ", fl_canc = 'S'" ;  // fl_canc

				sqlDEL2=this.struttura(sqlDEL2);
				sqlDEL2=sqlDEL2 + " fl_canc<>'S'";

				if (profilo.getProfilo()!=null && profilo.getProfilo().getCodice().trim().length()!=0)
				{
					sqlDEL2=this.struttura(sqlDEL2);
					sqlDEL2=sqlDEL2   + " cod_prac=" + profilo.getProfilo().getCodice() ;
				}

				sqlDEL2=this.struttura(sqlDEL2);
				sqlDEL2=sqlDEL2   + " id_sez_acquis_bibliografiche=" + profilo.getIDSez() ;

				pstmtDEL2 = connection.prepareStatement(sqlDEL2);
				int intRetCANC2 = 0;
				intRetCANC2 = pstmtDEL2.executeUpdate();
				pstmtDEL2.close();
				// oppure == numRighe
				if (intRetCANC2==1){
					valRitornoCANC2=true;
				}
				valRitorno=true;
			}
			else
			{
				motivo=2; // record forse già esistente quindi non inseribile
				throw new ValidationException("fornitoreInserimentoErroreBaseDati",
						ValidationExceptionCodici.fornitoreInserimentoErroreBaseDati);

			}

			connection.close();

		}catch (ValidationException e) {
	      	  throw e;
		}
		 catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}
     return valRitorno;
	}

	public  boolean strIsAlfabetic(String data) {
		return (Pattern.matches("[^0-9]+", data));
	}


	public  boolean strIsNumeric(String data) {
		return (Pattern.matches("[0-9&&[^a-z]]+", data));
	}

	public List<ComunicazioneVO> getRicercaListaComunicazioni(ListaSuppComunicazioneVO ricercaComunicazioni) throws ResourceNotFoundException, ApplicationException, ValidationException
	{
		Validazione.ValidaListaSuppComunicazioneVO (ricercaComunicazioni);

		List<ComunicazioneVO> listaComunicazioni = new ArrayList<ComunicazioneVO>();

		int ret = 0;
        // execute the search here
			ComunicazioneVO rec = null;
    		Connection connection = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			PreparedStatement pstmtOrdine = null;
			ResultSet rsSubOrdine = null;

			try {
				// contiene i criteri di ricerca
				connection = getConnection();
				String sql="select messgg.*,messgg.note as noteCom, messgg.ts_var as dataUpd, TO_CHAR(messgg.data_msg,'dd/MM/yyyy') as data_msg_str, forn.nom_fornitore,  lower(forn.nom_fornitore), forn.*, codi.ds_tabella, bibliot.nom_biblioteca, bibliot.indirizzo as mittIndirizzo, bibliot.cap as mittCap, bibliot.localita as mittCitta, bibliot.telefono as mittTelefono, bibliot.fax as mittFax,  fornBibl.cod_cliente ";
				sql +=" from  tra_messaggi messgg ";
				sql +=" join tbr_fornitori forn on forn.cod_fornitore=messgg.cod_fornitore and forn.fl_canc<>'S'";
				sql +=" left join tbr_fornitori_biblioteche fornBibl on fornBibl.cod_fornitore=forn.cod_fornitore and fornBibl.cd_biblioteca=messgg.cd_bib and fornBibl.cd_polo=messgg.cd_polo and fornBibl.fl_canc<>'S'"  ;
				sql +=" join tb_codici codi on codi.tp_tabella='ATME' and codi.cd_tabella=messgg.tipo_msg" ;
				sql +=" join tbf_biblioteca bibliot on bibliot.cd_bib=messgg.cd_bib and bibliot.cd_polo=messgg.cd_polo and bibliot.fl_canc<>'S' ";


				sql=this.struttura(sql);
				sql +=" messgg.fl_canc<>'S'";
				// tipo_msg
				if (ricercaComunicazioni.getTipoMessaggio() !=null &&  ricercaComunicazioni.getTipoMessaggio().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" messgg.tipo_msg='" + ricercaComunicazioni.getTipoMessaggio() +"'";
				}

				if (ricercaComunicazioni.getCodPolo() !=null &&  ricercaComunicazioni.getCodPolo().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" messgg.cd_polo='" + ricercaComunicazioni.getCodPolo() +"'";
				}

				if (ricercaComunicazioni.getCodBibl() !=null &&  ricercaComunicazioni.getCodBibl().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" messgg.cd_bib='" + ricercaComunicazioni.getCodBibl() +"'";
				}

				if (ricercaComunicazioni.getCodiceMessaggio()!=null && ricercaComunicazioni.getCodiceMessaggio().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" messgg.cod_msg=" + ricercaComunicazioni.getCodiceMessaggio().trim() ;
				}
				if (ricercaComunicazioni.getDataComunicazioneDa()!=null && ricercaComunicazioni.getDataComunicazioneDa().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" messgg.data_msg >= TO_DATE('" +  ricercaComunicazioni.getDataComunicazioneDa() + "','dd/MM/yyyy')";
				}

				if (ricercaComunicazioni.getDataComunicazioneA()!=null && ricercaComunicazioni.getDataComunicazioneA().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" messgg.data_msg <= TO_DATE ('" +  ricercaComunicazioni.getDataComunicazioneA() + "','dd/MM/yyyy')";
				}

				if (ricercaComunicazioni.getTipoInvioComunicazione()!=null && ricercaComunicazioni.getTipoInvioComunicazione().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" messgg.tipo_invio='" + ricercaComunicazioni.getTipoInvioComunicazione() +"'";
				}

				if (ricercaComunicazioni.getFornitore().getDescrizione()!=null && ricercaComunicazioni.getFornitore().getDescrizione().length()!=0 )
				{
					sql=this.struttura(sql);
					sql +=" forn.nom_fornitore='" + ricercaComunicazioni.getFornitore().getDescrizione().replace("'","''") +"'";
					//like '" + ricercaSezioni.getDescrizioneSezione() +"%'"
				}

				if (ricercaComunicazioni.getFornitore().getCodice()!=null && ricercaComunicazioni.getFornitore().getCodice().length()!=0 )
				{
					sql=this.struttura(sql);
					sql +=" messgg.cod_fornitore=" + ricercaComunicazioni.getFornitore().getCodice() ;
				}
				if (ricercaComunicazioni.getTipoDocumento()!=null && ricercaComunicazioni.getTipoDocumento().equals("O"))
				{
					sql=this.struttura(sql);
					sql +=" messgg.anno_fattura=0"  ;
					sql=this.struttura(sql);
					sql +=" messgg.progr_fattura=0"  ;

					// tipo ordine
					if (ricercaComunicazioni.getIdDocumento().getCodice1()!=null && ricercaComunicazioni.getIdDocumento().getCodice1()!=null && ricercaComunicazioni.getIdDocumento().getCodice1().length()!=0)
					{
						sql=this.struttura(sql);
						sql +=" messgg.cod_tip_ord='" +ricercaComunicazioni.getIdDocumento().getCodice1() +"'" ;

					}
					// anno ord
					if (ricercaComunicazioni.getIdDocumento().getCodice2()!=null && ricercaComunicazioni.getIdDocumento().getCodice2()!=null && ricercaComunicazioni.getIdDocumento().getCodice2().length()!=0)
					{
						sql=this.struttura(sql);
						sql +=" messgg.anno_ord=" +ricercaComunicazioni.getIdDocumento().getCodice2()  ;

					}
					// codice ordine
					if (ricercaComunicazioni.getIdDocumento().getCodice3()!=null && ricercaComunicazioni.getIdDocumento().getCodice3()!=null && ricercaComunicazioni.getIdDocumento().getCodice3().length()!=0)
					{
						sql=this.struttura(sql);
						sql +=" messgg.cod_ord=" +ricercaComunicazioni.getIdDocumento().getCodice3()  ;
					}
				}
				if (ricercaComunicazioni.getTipoDocumento()!=null && ricercaComunicazioni.getTipoDocumento().equals("F") )
				{
					sql=this.struttura(sql);
					sql +=" messgg.anno_ord=0"  ;
					sql=this.struttura(sql);
					sql +=" messgg.cod_ord=0"  ;

					// anno fatt
					if (ricercaComunicazioni.getIdDocumento().getCodice2()!=null && ricercaComunicazioni.getIdDocumento().getCodice2()!=null && ricercaComunicazioni.getIdDocumento().getCodice2().length()!=0)
					{
						sql=this.struttura(sql);
						sql +=" messgg.anno_fattura=" +ricercaComunicazioni.getIdDocumento().getCodice2()  ;

					}
					// codice ordine
					if (ricercaComunicazioni.getIdDocumento().getCodice3()!=null && ricercaComunicazioni.getIdDocumento().getCodice3()!=null && ricercaComunicazioni.getIdDocumento().getCodice3().length()!=0)
					{
						sql=this.struttura(sql);
						sql +=" messgg.progr_fattura=" +ricercaComunicazioni.getIdDocumento().getCodice3()  ;

					}
				}
				if (ricercaComunicazioni.getStatoComunicazione()!=null && ricercaComunicazioni.getStatoComunicazione().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" messgg.stato_msg='" + ricercaComunicazioni.getStatoComunicazione() +"'";
				}

				if (ricercaComunicazioni.getDirezioneComunicazione()!=null && ricercaComunicazioni.getDirezioneComunicazione().trim().length()!=0 )
				{
					if (ricercaComunicazioni.getDirezioneComunicazione().trim().equals("A") )
					{
						sql=this.struttura(sql);
						sql +=" messgg.stato_msg <> '1'";
					}

					if (ricercaComunicazioni.getDirezioneComunicazione().trim().equals("D") )
					{
						sql=this.struttura(sql);
						sql +=" messgg.stato_msg='1'";
					}
				}

				// ordinamento passato
				if (ricercaComunicazioni.getOrdinamento()==null || ( ricercaComunicazioni.getOrdinamento()!=null && ricercaComunicazioni.getOrdinamento().equals("")))
				{
					sql +=" order by messgg.data_msg desc ";
				}
				else if (ricercaComunicazioni.getOrdinamento().equals("1"))
				{
					sql +=" order by messgg.data_msg desc  ";
				}
				else if (ricercaComunicazioni.getOrdinamento().equals("2"))
				{
					sql +=" order by messgg.data_msg ";
				}
				else if (ricercaComunicazioni.getOrdinamento().equals("3"))
				{
					sql +=" order by lower(forn.nom_fornitore)  ";
				}
				else if (ricercaComunicazioni.getOrdinamento().equals("4"))
				{
					sql +=" order by lower(codi.ds_tabella)  ";
				}
				else if (ricercaComunicazioni.getOrdinamento().equals("5"))
				{
					sql +=" order by  messgg.anno_ord "; //messgg.cod_tip_ord
				}
				else if (ricercaComunicazioni.getOrdinamento().equals("6"))
				{
					sql +=" order by messgg.cod_ord  ";
				}

				pstmt = connection.prepareStatement(sql);
				//log.debug("Debug: lettura comunicazione");
				//log.debug("Debug: " + sql);

				rs = pstmt.executeQuery();
				int numRighe=0;
				while (rs.next()) {
					rec = new ComunicazioneVO();
					numRighe=numRighe + 1;
					rec.setProgressivo(numRighe);
					rec.setCodPolo(rs.getString("cd_polo"));
					rec.setCodBibl(rs.getString("cd_bib"));
					rec.setDenoBibl(rs.getString("nom_biblioteca"));
					rec.setCodiceMessaggio(String.valueOf(rs.getInt("cod_msg")));
					rec.setDesMessaggio(rs.getString("ds_tabella"));
					rec.setDataComunicazione(rs.getString("data_msg_str"));
					rec.setNoteComunicazione(rs.getString("noteCom"));
					rec.setStatoComunicazione(rs.getString("stato_msg"));
					rec.setDataUpd(rs.getTimestamp("dataUpd"));
					rec.setMittenteCap(rs.getString("mittCap"));
					rec.setMittenteCitta(rs.getString("mittCitta"));
					rec.setMittenteFax(rs.getString("mittFax"));
					rec.setMittenteIndirizzo(rs.getString("mittIndirizzo"));
					rec.setMittenteTelefono(rs.getString("mittTelefono"));
					rec.setCodCliFornitore("");
					if (rs.getString("cod_cliente")!=null)
					{
						rec.setCodCliFornitore(rs.getString("cod_cliente"));
					}
					if (rec.getStatoComunicazione().equals("1"))
					{
						rec.setDesStato("RICEVUTO");
					}
					else if (rec.getStatoComunicazione().equals("2"))
					{
						rec.setDesStato("SPEDITO");
					}
					else if (rec.getStatoComunicazione().equals("3"))
					{
						rec.setDesStato("NON SPEDITO");
					}
					else
					{
						rec.setDesStato("");
					}



					rec.setTipoMessaggio(rs.getString("tipo_msg"));
					rec.setTipoInvioComunicazione(rs.getString("tipo_invio"));

					if (rec.getTipoInvioComunicazione().equals("F"))
					{
						rec.setTipoInvioDes("fax");
					}
					else if (rec.getTipoInvioComunicazione().equals("P"))
					{
						rec.setTipoInvioDes("posta");
					}
					else if (rec.getTipoInvioComunicazione().equals("S"))
					{
						rec.setTipoInvioDes("stampa");
					}
					else
					{
						rec.setTipoInvioDes("");
					}

					rec.setFornitore(new StrutturaCombo("",""));
					rec.getFornitore().setCodice(String.valueOf(rs.getInt("cod_fornitore")));
					rec.getFornitore().setDescrizione(rs.getString("nom_fornitore"));

					// dati fornitore per stampe
					rec.setAnagFornitore(new FornitoreVO());
					rec.getAnagFornitore().setCodFornitore(String.valueOf(rs.getInt("cod_fornitore")));
					rec.getAnagFornitore().setNomeFornitore(rs.getString("nom_fornitore"));
					rec.getAnagFornitore().setEmail(rs.getString("e_mail"));
					if (rs.getString("indirizzo")!=null && rs.getString("indirizzo").trim().length()>0 )
					{
						rec.getAnagFornitore().setIndirizzo(rs.getString("indirizzo"));
					}
					if (rs.getString("citta")!=null && rs.getString("citta").trim().length()>0 )
					{
						rec.getAnagFornitore().setCitta(rs.getString("citta"));
					}
					if (rs.getString("cap")!=null && rs.getString("cap").trim().length()>0 )
					{
						rec.getAnagFornitore().setCap(rs.getString("cap"));
					}
					if (rs.getString("paese")!=null && rs.getString("paese").trim().length()>0 )
					{
						rec.getAnagFornitore().setPaese(rs.getString("paese"));
					}
					if (rs.getString("cod_fiscale")!=null && rs.getString("cod_fiscale").trim().length()>0 )
					{
						rec.getAnagFornitore().setCodiceFiscale(rs.getString("cod_fiscale"));
					}
					if (rs.getString("fax")!=null && rs.getString("fax").trim().length()>0 )
					{
						rec.getAnagFornitore().setFax(rs.getString("fax"));
					}
					if (rs.getString("telefono")!=null && rs.getString("telefono").trim().length()>0 )
					{
						rec.getAnagFornitore().setTelefono(rs.getString("telefono"));
					}
					rec.getAnagFornitore().setFornitoreBibl(new DatiFornitoreVO());


					rec.setIdDocumento(new StrutturaTerna("","",""));

					if (rs.getString("cod_tip_ord")!=null && rs.getString("cod_tip_ord").trim().length()!=0 )
					//if (ricercaComunicazioni.getTipoDocumento().equals("F") )
					{
						rec.setTipoDocumento("O");
						rec.getIdDocumento().setCodice1(rs.getString("cod_tip_ord")); //cod_tip_ord
						rec.getIdDocumento().setCodice2(String.valueOf(rs.getInt("anno_ord"))); // anno_ord
						rec.getIdDocumento().setCodice3(String.valueOf(rs.getInt("cod_ord"))); //cod_ord
					}
					//if (ricercaComunicazioni.getTipoDocumento().equals("O") )
					else
					{
						rec.setTipoDocumento("F");
						rec.getIdDocumento().setCodice2(String.valueOf(rs.getInt("anno_fattura"))); //anno_fattura
						rec.getIdDocumento().setCodice3(String.valueOf(rs.getInt("progr_fattura"))); //progr_fattura
					}
					// a seconda del codice messaggio si desume la direzione
					// sostituire l'impostazione della direzione a seconda dello stato: se stato=1 ricevuta direz=DA
					// se stato<>1 ricevuta direz=A
					rec.setDirezioneComunicazione("A"); // assumo un valore di default
					rec.setDirezioneComunicazioneLabel("a");
					if (rs.getString("stato_msg").equals("1"))
						{
						rec.setDirezioneComunicazione("D");
						rec.setDirezioneComunicazioneLabel("da");
						}

					// dati di dettaglio dell'ordine tck 2560 (solo in esamina)


					rec.setDocORDINE(new StrutturaQuinquies("", "", "", "", "")); // anno_abb, data_fasc_str , data_fine_str, titolo, rinnovo di

					if (rec.getTipoDocumento()!=null &&  rec.getTipoDocumento().equals("O") && numRighe==1)	{
						StringBuilder sqlOrdine = new StringBuilder();
						sqlOrdine.append("select ord.*, TO_CHAR(ord.data_ord,'dd/MM/yyyy') as data_ord_str,TO_CHAR(ord.data_fine,'dd/MM/yyyy') as data_fine_str, TO_CHAR(ord.data_fasc,'dd/MM/yyyy') as  data_fasc_str ");  // , tit.isbd
						sqlOrdine.append(" from tba_ordini  ord ");
						sqlOrdine.append(" where ord.fl_canc<>'S'");
						//almaviva5_20110718 #4514
						sqlOrdine.append(" and ord.cd_polo='").append(rec.getCodPolo()).append("'");
						sqlOrdine.append(" and ord.cd_bib='").append(rec.getCodBibl()).append("'");
						//
						if (rec.getIdDocumento()!=null && rec.getIdDocumento().getCodice1()!=null && rec.getIdDocumento().getCodice1().trim().length()>0)
						{
							sqlOrdine.append(" and ord.cod_tip_ord='" + rec.getIdDocumento().getCodice1().trim() + "'");
						}
						if (rec.getIdDocumento()!=null && rec.getIdDocumento().getCodice2()!=null && rec.getIdDocumento().getCodice2().trim().length()>0)
						{
							sqlOrdine.append(" and ord.anno_ord='" + rec.getIdDocumento().getCodice2().trim() + "'");
						}
						if (rec.getIdDocumento()!=null && rec.getIdDocumento().getCodice3()!=null && rec.getIdDocumento().getCodice3().trim().length()>0)
						{
							sqlOrdine.append(" and ord.cod_ord="+ rec.getIdDocumento().getCodice3().trim());
						}
						pstmtOrdine = connection.prepareStatement(sqlOrdine.toString());
						rsSubOrdine = pstmtOrdine.executeQuery();
						int numOrd=0;
						String bidOrd="";
						String isbd="";
						String annoAbb="";
						String dataFascAbb="";
						String dataFineAbb="";
						String continuativo="0";
						String natura="";
						String rinnovo="";

						while (rsSubOrdine.next())
						{
							numOrd=numOrd+1;
							bidOrd=rsSubOrdine.getString("bid");
							if (rsSubOrdine.getInt("anno_abb")>0)
							{
								annoAbb=String.valueOf(rsSubOrdine.getInt("anno_abb"));
							}
							if (rsSubOrdine.getString("data_fasc_str")!=null)
							{
								dataFascAbb=rsSubOrdine.getString("data_fasc_str");
							}
							if (rsSubOrdine.getString("data_fine_str")!=null)
							{
								dataFineAbb=rsSubOrdine.getString("data_fine_str");
							}
							if (rsSubOrdine.getString("continuativo")!=null  &&  rsSubOrdine.getString("continuativo").equals("1"))
							{
								continuativo="1";
							}
							if (rsSubOrdine.getString("natura")!=null)
							{
								natura=rsSubOrdine.getString("natura");
							}
							if (rsSubOrdine.getInt("anno_1ord")>0 && rsSubOrdine.getInt("cod_1ord")>0 )
							{
								rinnovo=rsSubOrdine.getString("cod_tip_ord") +"-"+ String.valueOf(rsSubOrdine.getInt("anno_1ord")) +"-"+ String.valueOf(rsSubOrdine.getInt("cod_1ord"));
							}

						}
						rsSubOrdine.close();
						pstmtOrdine.close();

						String nStandard="";
						List<StrutturaTerna> nStandardArr=null;

						if (bidOrd!=null && bidOrd.trim().length()>0 && numOrd==1)
						{
							try {
								TitoloACQVO recTit=null;
								recTit= this.getTitoloOrdineTer(bidOrd);
								if (recTit!=null && recTit.getIsbd()!=null) {
									isbd=recTit.getIsbd();
								}
								if (recTit!=null && recTit.getNumStandard()!=null) {
									nStandard=recTit.getNumStandard();
								}
								if (recTit!=null && recTit.getNumStandardArr()!=null && recTit.getNumStandardArr().size()>0) {
									nStandardArr=recTit.getNumStandardArr();
								}

							} catch (Exception e) {
								log.error("", e);
							}
						}
						if (nStandardArr!=null && nStandardArr.size()>0)
						{
							rec.setNumStandardArr(nStandardArr);
							rec.leggiNumSdt();

						}

						// anno_abb, data_fasc_str , data_fine_str, titolo
						rec.getDocORDINE().setCodice1(annoAbb);
						rec.getDocORDINE().setCodice2(dataFascAbb);
						rec.getDocORDINE().setCodice3(dataFineAbb);
						rec.getDocORDINE().setCodice4(isbd);
						rec.getDocORDINE().setCodice5(rinnovo);

					}

					listaComunicazioni.add(rec);
				}
				// End while
				rs.close();
				pstmt.close();
				connection.close();

			}	catch (ValidationException e) {
		    	  throw e;
		} catch (Exception e) {

			// l'errore capita in questo punto
			log.error("", e);
		} finally {
			close(connection);
		}

		Validazione.ValidaRicercaComunicazioni (listaComunicazioni);
        return listaComunicazioni;
	}

	public boolean  inserisciComunicazione(ComunicazioneVO comunicazione) throws DataException, ApplicationException , ValidationException
	{
		Validazione.ValidaComunicazioneVO(comunicazione);
		boolean valRitorno=false;
    	int motivo=0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmtESISTEORD= null;
		ResultSet rsESISTEORD= null;
		boolean controlloCONGR=false;
		boolean valRitornoINS=false;
		boolean esistenzaComunicazione=false;
		PreparedStatement pstmtCod= null;
		ResultSet rsSubCod= null;

		try {
			// CONTROLLI PREVENTIVI
			connection = getConnection();
			String sql0="select *, TO_CHAR(messgg.data_msg,'dd/MM/yyyy') as data_msg_str ";
			sql0=sql0 + " from  tra_messaggi messgg ";
			sql0=sql0  + " join tbr_fornitori forn on forn.cod_fornitore=messgg.cod_fornitore and forn.fl_canc<>'S' ";
			if (comunicazione.getTipoDocumento().equals("O"))
			{
				sql0=sql0 + "  join tba_ordini ord on ord.cd_polo=messgg.cd_polo and ord.cd_bib=messgg.cd_bib and ord.fl_canc<>'S' and ord.stato_ordine <> 'N'";
				sql0=sql0 + " and ord.cod_tip_ord=messgg.cod_tip_ord and ord.anno_ord=messgg.anno_ord and ord.cod_ord=messgg.cod_ord  ";

			}
			if (comunicazione.getTipoDocumento().equals("F") )
			{
				sql0=sql0  + " join tba_fatture fatt on fatt.cod_fornitore=messgg.cod_fornitore and fatt.cd_polo=messgg.cd_polo and fatt.cd_bib=messgg.cd_bib and fatt.anno_fattura=messgg.anno_fattura and fatt.progr_fattura=messgg.progr_fattura  and fatt.fl_canc<>'S' ";

			}

			sql0=this.struttura(sql0);
			sql0=sql0 + " messgg.fl_canc<>'S'";

			if (comunicazione.getCodPolo() !=null &&  comunicazione.getCodPolo().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " messgg.cd_polo='" + comunicazione.getCodPolo() +"'";
			}

			if (comunicazione.getCodBibl() !=null &&  comunicazione.getCodBibl().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " messgg.cd_bib='" + comunicazione.getCodBibl() +"'";
			}
			if (comunicazione.getDataComunicazione()!=null && comunicazione.getDataComunicazione().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " messgg.data_msg = TO_DATE ('" +  comunicazione.getDataComunicazione() + "','dd/MM/yyyy')";
			}

			if (comunicazione.getTipoMessaggio()!=null && comunicazione.getTipoMessaggio().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0  + " messgg.tipo_invio='" + comunicazione.getTipoMessaggio() +"'";
			}
			if (comunicazione.getStatoComunicazione()!=null && comunicazione.getStatoComunicazione().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " messgg.stato_msg='" + comunicazione.getStatoComunicazione() +"'";
			}
			if (comunicazione.getFornitore().getCodice()!=null && comunicazione.getFornitore().getCodice().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " messgg.cod_fornitore=" + comunicazione.getFornitore().getCodice() ;
			}
			if (comunicazione.getTipoDocumento().equals("O"))
			{
				// tipo ordine
				if (comunicazione.getIdDocumento().getCodice1()!=null && comunicazione.getIdDocumento().getCodice1()!=null && comunicazione.getIdDocumento().getCodice1().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0  + " messgg.cod_tip_ord='" +comunicazione.getIdDocumento().getCodice1() +"'" ;

				}
				// anno ord
				if (comunicazione.getIdDocumento().getCodice2()!=null && comunicazione.getIdDocumento().getCodice2()!=null && comunicazione.getIdDocumento().getCodice2().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0+ " messgg.anno_ord=" +comunicazione.getIdDocumento().getCodice2()  ;

				}
				// codice ordine
				if (comunicazione.getIdDocumento().getCodice3()!=null && comunicazione.getIdDocumento().getCodice3()!=null && comunicazione.getIdDocumento().getCodice3().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0 + " messgg.cod_ord=" +comunicazione.getIdDocumento().getCodice3()  ;
				}
			}
			if (comunicazione.getTipoDocumento().equals("F") )
			{
				// anno fatt
				if (comunicazione.getIdDocumento().getCodice2()!=null && comunicazione.getIdDocumento().getCodice2()!=null && comunicazione.getIdDocumento().getCodice2().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0 + " messgg.anno_fattura=" +comunicazione.getIdDocumento().getCodice2()  ;

				}
				// codice ordine
				if (comunicazione.getIdDocumento().getCodice3()!=null && comunicazione.getIdDocumento().getCodice3()!=null && comunicazione.getIdDocumento().getCodice3().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0 + " messgg.progr_fattura=" +comunicazione.getIdDocumento().getCodice3()  ;
				}
			}

			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			Timestamp ts = DaoManager.now();
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;
				}
			if (numRighe>0)
			{
				esistenzaComunicazione=true; // record forse già esistente quindi non inseribile

				throw new ValidationException("cambierroreInserimentoEsistenzaRecord",
						ValidationExceptionCodici.cambierroreInserimentoEsistenzaRecord);
			}
			pstmt.close();
			if (!esistenzaComunicazione)
			{
				// controllo esistenza fornitore
				String sqlESISTEFORN="select * from tbr_fornitori forn " ;
				sqlESISTEFORN=sqlESISTEFORN + " left join tbr_fornitori_biblioteche fornBibl on fornBibl.cod_fornitore=forn.cod_fornitore and fornBibl.fl_canc<>'S'"   ;
				//sqlESISTEFORN=this.struttura(sqlESISTEFORN);
				sqlESISTEFORN=sqlESISTEFORN + " and fornBibl.cd_polo='" + comunicazione.getCodPolo() +"'";
				//sqlESISTEFORN=this.struttura(sqlESISTEFORN);
				sqlESISTEFORN=sqlESISTEFORN + " and fornBibl.cd_biblioteca='" + comunicazione.getCodBibl() +"'";
				sqlESISTEFORN=this.struttura(sqlESISTEFORN);
				sqlESISTEFORN=sqlESISTEFORN + " forn.cod_fornitore=" + comunicazione.getFornitore().getCodice();
				sqlESISTEFORN=this.struttura(sqlESISTEFORN);
				sqlESISTEFORN=sqlESISTEFORN + " forn.fl_canc<>'S'" ;
				pstmtESISTEORD = connection.prepareStatement(sqlESISTEFORN);
				rsESISTEORD = pstmtESISTEORD.executeQuery(); // va in errore se non può restituire un recordset
				int numRigheForn=0;
				while (rsESISTEORD.next()) {
					numRigheForn=numRigheForn+1;
				}
				rsESISTEORD.close();
				pstmtESISTEORD.close();
				if (numRigheForn==1)
				{
					controlloCONGR=true;
				}
				else
				{
					controlloCONGR=false;
					//
					throw new ValidationException("ordineIncongruenzaFornitoreInesistente",
							ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);

				}
				// fine controllo esistenza fornitore
				if (comunicazione.getTipoDocumento().equals("O"))
				{
					// controllo esistenza ordine
					String sqlESISTEORD="select * from tba_ordini ord " ;
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " ord.fl_canc<>'S'";
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " ord.cd_polo='" + comunicazione.getCodPolo() +"'";
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " ord.cd_bib='" + comunicazione.getCodBibl() +"'";
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " ord.cod_tip_ord='" + comunicazione.getIdDocumento().getCodice1() +"'";
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " ord.anno_ord=" + comunicazione.getIdDocumento().getCodice2() ;
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " ord.cod_ord=" +  comunicazione.getIdDocumento().getCodice3()  ;
					pstmtESISTEORD = connection.prepareStatement(sqlESISTEORD);
					rsESISTEORD = pstmtESISTEORD.executeQuery(); // va in errore se non può restituire un recordset

					int numRigheOrd=0;
					while (rsESISTEORD.next()) {
						numRigheOrd=numRigheOrd+1;
					}
					rsESISTEORD.close();
					pstmtESISTEORD.close();
					if (numRigheOrd==1)
					{
						controlloCONGR=true;
					}
					else
					{
						controlloCONGR=false;
						//
						throw new ValidationException("ordineNONtrovato",
								ValidationExceptionCodici.ordineNONtrovato);
					}

				}
				if (comunicazione.getTipoDocumento().equals("F") )
				{
					// controllo esistenza fattura
					sql0=sql0  + " join tba_fatture fatt on fatt.cod_fornitore=messgg.cod_fornitore and fatt.cd_polo=messgg.cd_polo and fatt.cd_bib=messgg.cd_bib and fatt.anno_fattura=messgg.anno_fattura and fatt.progr_fattura=messgg.progr_fattura  and fatt.fl_canc<>'S' ";


					String sqlESISTEFATT="select * from tba_fatture fatt " ;
					sqlESISTEFATT=this.struttura(sqlESISTEFATT);
					sqlESISTEFATT=sqlESISTEFATT + " fatt.fl_canc<>'S'";
					sqlESISTEFATT=this.struttura(sqlESISTEFATT);
					sqlESISTEFATT=sqlESISTEFATT + " fatt.cd_polo='" + comunicazione.getCodPolo() +"'";
					sqlESISTEFATT=this.struttura(sqlESISTEFATT);
					sqlESISTEFATT=sqlESISTEFATT + " fatt.cd_bib='" + comunicazione.getCodBibl() +"'";
					sqlESISTEFATT=this.struttura(sqlESISTEFATT);
					sqlESISTEFATT=sqlESISTEFATT + " fatt.anno_fattura=" + comunicazione.getIdDocumento().getCodice2() ;
					sqlESISTEFATT=this.struttura(sqlESISTEFATT);
					sqlESISTEFATT=sqlESISTEFATT + " fatt.progr_fattura=" + comunicazione.getIdDocumento().getCodice3() ;

					pstmtESISTEORD = connection.prepareStatement(sqlESISTEFATT);
					rsESISTEORD = pstmtESISTEORD.executeQuery(); // va in errore se non può restituire un recordset

					int numRigheOrd=0;
					while (rsESISTEORD.next()) {
						numRigheOrd=numRigheOrd+1;
					}
					rsESISTEORD.close();
					pstmtESISTEORD.close();
					if (numRigheOrd==1)
					{
						controlloCONGR=true;
					}
					else
					{
						controlloCONGR=false;
						//
						throw new ValidationException("fatturaNONtrovata",
								ValidationExceptionCodici.fatturaNONtrovata);
					}
				}

				if (controlloCONGR)
				{
					// INSERIMENTO
					String sqlSub="insert into tra_messaggi values ( " ;
					sqlSub=sqlSub + "'" + comunicazione.getCodPolo() + "'" ;  // cd_polo
					sqlSub=sqlSub + ",'" + comunicazione.getCodBibl() + "'" ;  // cd_bib

					// INIZIO SUBQUERY cod_msg
					sqlSub=sqlSub + ", (SELECT CASE WHEN  (MAX(tra_messaggi.cod_msg) is null) THEN 1  else MAX(tra_messaggi.cod_msg)+1  END " ;
					sqlSub=sqlSub + " from tra_messaggi   ";
					sqlSub=sqlSub + " where cd_bib='" + comunicazione.getCodBibl() +"'";
					sqlSub=sqlSub + " and cd_polo='" + comunicazione.getCodPolo() +"'";
					sqlSub=sqlSub + " )";

					sqlSub=sqlSub + ",(SELECT CURRENT_DATE ) "  ;  // data_msg
					sqlSub=sqlSub + ",'" + comunicazione.getNoteComunicazione().trim().replace("'","''") + "'" ;  // note
					if (comunicazione.getTipoDocumento().equals("O"))
					{
						sqlSub=sqlSub + ",0" ;  // anno_fattura
						sqlSub=sqlSub + ",0" ;  // progr_fattura
						sqlSub=sqlSub + ",'" + comunicazione.getIdDocumento().getCodice1() + "'" ;  // cod_tip_ord
						sqlSub=sqlSub + "," +  comunicazione.getIdDocumento().getCodice2() ;  // anno_ord
						sqlSub=sqlSub + "," +  comunicazione.getIdDocumento().getCodice3()  ;  // cod_ord
					}
					if (comunicazione.getTipoDocumento().equals("F"))
					{
						sqlSub=sqlSub + "," +  comunicazione.getIdDocumento().getCodice2() ;  // anno_fattura
						sqlSub=sqlSub + "," +  comunicazione.getIdDocumento().getCodice3() ;  // progr_fattura
						sqlSub=sqlSub + ",''"  ;  // cod_tip_ord
						sqlSub=sqlSub + ",0"  ;  // anno_ord
						sqlSub=sqlSub + ",0"  ;  // cod_ord
					}
					sqlSub=sqlSub + ",'" +  comunicazione.getStatoComunicazione() + "'" ;  // stato_msg
					sqlSub=sqlSub + ",'" +  comunicazione.getTipoMessaggio() + "'" ;  // tipo_msg
					sqlSub=sqlSub + ",'" +  comunicazione.getTipoInvioComunicazione() + "'" ;  // tipo_invio
					sqlSub=sqlSub + "," +  comunicazione.getFornitore().getCodice()  ;  // cod_fornitore
					sqlSub=sqlSub + ",'" + comunicazione.getUtente() + "'" ;   // ute_ins
					sqlSub=sqlSub + ",'" + ts + "'" ;   // ts_ins
					sqlSub=sqlSub + ",'" + comunicazione.getUtente() + "'" ;   // ute_var
					sqlSub=sqlSub + ",'" + ts + "'";   // ts_var
					sqlSub=sqlSub + ",'N'";   // fl_canc
					sqlSub=sqlSub + ")" ;
					pstmt = connection.prepareStatement(sqlSub);
					log.debug("Debug: inserimento comunicazione");
					log.debug("Debug: " + sqlSub);

					int intRetINS = 0;
					intRetINS = pstmt.executeUpdate();
					pstmt.close();
					if (intRetINS==1){
						valRitornoINS=true;
						// deduzione del codice appena inserito per successivi insert
						String sqlCodice="select cod_msg from tra_messaggi ";
						//sqlCodice=sqlCodice + " where ts_ins='" +ts+ "'";
						sqlCodice=sqlCodice + " where ute_ins='" + comunicazione.getUtente()+ "'";
						sqlCodice=sqlCodice + " order by ts_ins desc limit 1";

						pstmtCod = connection.prepareStatement(sqlCodice);
						rsSubCod = pstmtCod.executeQuery();
						if (rsSubCod.next()) {
							comunicazione.setCodiceMessaggio(String.valueOf(rsSubCod.getInt("cod_msg")));
						}
						rsSubCod.close();
						pstmtCod.close();
					}
				}
			}
			rs.close();
			connection.close();

			// impostazione del codice di ritorno finale
			if (valRitornoINS)
			{
				valRitorno=true;
			}
		}catch (ValidationException e) {
	      	  throw e;

		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

        return valRitorno;
	}

	public boolean  modificaComunicazione(ComunicazioneVO comunicazione) throws DataException, ApplicationException , ValidationException
	{
		Validazione.ValidaComunicazioneVO(comunicazione);
		boolean valRitorno=false;
    	int motivo=0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean valRitornoUPD=false;
		boolean esistenzaComunicazione=false;
		PreparedStatement pstmtESISTEORD= null;
		ResultSet rsESISTEORD= null;
		boolean controlloCONGR=false;

		try {
			connection = getConnection();

			// CONTROLLI PREVENTIVI
			String sql0="select * ";
			sql0=sql0 + " from  tra_messaggi  ";

			sql0=this.struttura(sql0);
			sql0=sql0 + " fl_canc<>'S'";

			if (comunicazione.getCodPolo() !=null &&  comunicazione.getCodPolo().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " cd_polo='" + comunicazione.getCodPolo() +"'";
			}


			if (comunicazione.getCodBibl() !=null &&  comunicazione.getCodBibl().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " cd_bib='" + comunicazione.getCodBibl() +"'";
			}
			if (comunicazione.getCodiceMessaggio()!=null && comunicazione.getCodiceMessaggio().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " cod_msg=" + comunicazione.getCodiceMessaggio() ;
			}

			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			int numRighe=0;
			List<StrutturaCombo> righeOggetto=new ArrayList();
			Timestamp ts = DaoManager.now();
			while (rs.next()) {
				numRighe=numRighe+1;
			}
			rs.close();
			pstmt.close();
			if (numRighe >= 1)
			{
				if (numRighe == 1)
				{
					esistenzaComunicazione=true;
				}
			}
			if (!esistenzaComunicazione )
			{
				throw new ValidationException("cambierroreModificaRecordNonUnivoco",
						ValidationExceptionCodici.cambierroreModificaRecordNonUnivoco);
			}
			if (esistenzaComunicazione)
				{
				// controllo esistenza fornitore
				// controllo esistenza fornitore
				String sqlESISTEFORN="select * from tbr_fornitori forn " ;
				sqlESISTEFORN=sqlESISTEFORN + " left join tbr_fornitori_biblioteche fornBibl on fornBibl.cod_fornitore=forn.cod_fornitore and fornBibl.fl_canc<>'S'"   ;
				//sqlESISTEFORN=this.struttura(sqlESISTEFORN);
				sqlESISTEFORN=sqlESISTEFORN + " and  fornBibl.cd_polo='" + comunicazione.getCodPolo() +"'";
				//sqlESISTEFORN=this.struttura(sqlESISTEFORN);
				sqlESISTEFORN=sqlESISTEFORN + " and fornBibl.cd_biblioteca='" + comunicazione.getCodBibl() +"'";
				sqlESISTEFORN=this.struttura(sqlESISTEFORN);
				sqlESISTEFORN=sqlESISTEFORN + " forn.cod_fornitore=" + comunicazione.getFornitore().getCodice();
				sqlESISTEFORN=this.struttura(sqlESISTEFORN);
				sqlESISTEFORN=sqlESISTEFORN + " forn.fl_canc<>'S'" ;
				pstmtESISTEORD = connection.prepareStatement(sqlESISTEFORN);
				rsESISTEORD = pstmtESISTEORD.executeQuery(); // va in errore se non può restituire un recordset
				int numRigheForn=0;
				while (rsESISTEORD.next()) {
					numRigheForn=numRigheForn+1;
				}
				rsESISTEORD.close();
				pstmtESISTEORD.close();
				if (numRigheForn==1)
				{
					controlloCONGR=true;
				}
				else
				{
					controlloCONGR=false;
					//
					throw new ValidationException("ordineIncongruenzaFornitoreInesistente",
							ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);

				}				// fine controllo esistenza fornitore
				if (comunicazione.getTipoDocumento().equals("O"))
				{
					// controllo esistenza ordine
					String sqlESISTEORD="select * from tba_ordini ord " ;
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " ord.fl_canc<>'S'";
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " ord.cd_polo='" + comunicazione.getCodPolo() +"'";
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " ord.cd_bib='" + comunicazione.getCodBibl() +"'";
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " ord.cod_tip_ord='" + comunicazione.getIdDocumento().getCodice1() +"'";
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " ord.anno_ord=" + comunicazione.getIdDocumento().getCodice2() ;
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " ord.cod_ord=" +  comunicazione.getIdDocumento().getCodice3()  ;
					pstmtESISTEORD = connection.prepareStatement(sqlESISTEORD);
					rsESISTEORD = pstmtESISTEORD.executeQuery(); // va in errore se non può restituire un recordset

					int numRigheOrd=0;
					while (rsESISTEORD.next()) {
						numRigheOrd=numRigheOrd+1;
					}
					rsESISTEORD.close();
					pstmtESISTEORD.close();
					if (numRigheOrd==1)
					{
						controlloCONGR=true;
					}
					else
					{
						controlloCONGR=false;
						//
						throw new ValidationException("ordineNONtrovato",
								ValidationExceptionCodici.ordineNONtrovato);
					}

				}
				if (comunicazione.getTipoDocumento().equals("F") )
				{
					// controllo esistenza fattura
					sql0=sql0  + " join tba_fatture fatt on fatt.cod_fornitore=messgg.cod_fornitore and fatt.cd_polo=messgg.cd_polo and fatt.cd_bib=messgg.cd_bib and fatt.anno_fattura=messgg.anno_fattura and fatt.progr_fattura=messgg.progr_fattura  and fatt.fl_canc<>'S' ";


					String sqlESISTEFATT="select * from tba_fatture fatt " ;
					sqlESISTEFATT=this.struttura(sqlESISTEFATT);
					sqlESISTEFATT=sqlESISTEFATT + " fatt.fl_canc<>'S'";
					sqlESISTEFATT=this.struttura(sqlESISTEFATT);
					sqlESISTEFATT=sqlESISTEFATT + " fatt.cd_polo='" + comunicazione.getCodPolo() +"'";
					sqlESISTEFATT=this.struttura(sqlESISTEFATT);
					sqlESISTEFATT=sqlESISTEFATT + " fatt.cd_bib='" + comunicazione.getCodBibl() +"'";
					sqlESISTEFATT=this.struttura(sqlESISTEFATT);
					sqlESISTEFATT=sqlESISTEFATT + " fatt.anno_fattura=" + comunicazione.getIdDocumento().getCodice2() ;
					sqlESISTEFATT=this.struttura(sqlESISTEFATT);
					sqlESISTEFATT=sqlESISTEFATT + " fatt.progr_fattura=" + comunicazione.getIdDocumento().getCodice3() ;

					pstmtESISTEORD = connection.prepareStatement(sqlESISTEFATT);
					rsESISTEORD = pstmtESISTEORD.executeQuery(); // va in errore se non può restituire un recordset

					int numRigheOrd=0;
					while (rsESISTEORD.next()) {
						numRigheOrd=numRigheOrd+1;
					}
					rsESISTEORD.close();
					pstmtESISTEORD.close();
					if (numRigheOrd==1)
					{
						controlloCONGR=true;
					}
					else
					{
						controlloCONGR=false;
						//
						throw new ValidationException("fatturaNONtrovata",
								ValidationExceptionCodici.fatturaNONtrovata);
					}
				}
				if (controlloCONGR)
				{
					// AGGIORNAMENTO
					String sqlUDP="update tra_messaggi set " ;
					//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
					//sqlUDP= sqlUDP + " data_agg = '" +  ts  + "'" ;  // data_agg
					sqlUDP= sqlUDP  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
					sqlUDP= sqlUDP + ", ute_var = '" +  comunicazione.getUtente()  + "'" ;  // ute_var

					if (comunicazione.getDataComunicazione()!=null && comunicazione.getDataComunicazione().length()!=0)
					{
						sqlUDP += ", data_msg=TO_DATE ('" +  comunicazione.getDataComunicazione() + "','dd/MM/yyyy')";
					}
					if (comunicazione.getNoteComunicazione()!=null && comunicazione.getNoteComunicazione().trim().length()!=0)
					{
						sqlUDP += ", note= '" +  comunicazione.getNoteComunicazione().trim().replace("'","''")  + "'" ;
					}
					if (comunicazione.getTipoDocumento().equals("O"))
					{
						sqlUDP += ",anno_fattura=0"  ;  // anno_fattura
						sqlUDP += ",progr_fattura=0"  ;  // progr_fattura
						if (comunicazione.getIdDocumento().getCodice1()!=null && comunicazione.getIdDocumento().getCodice1().length()!=0)
						{
							sqlUDP += ",cod_tip_ord='" + comunicazione.getIdDocumento().getCodice1() + "'" ;  // cod_tip_ord
						}
						if (comunicazione.getIdDocumento().getCodice2()!=null && comunicazione.getIdDocumento().getCodice2().length()!=0)
						{
							sqlUDP += ",anno_ord=" +  comunicazione.getIdDocumento().getCodice2() ;  // anno_ord
						}
						if (comunicazione.getIdDocumento().getCodice3()!=null && comunicazione.getIdDocumento().getCodice3().length()!=0)
						{
							sqlUDP += ",cod_ord=" +  comunicazione.getIdDocumento().getCodice3()  ;  // cod_ord
						}
					}
					if (comunicazione.getTipoDocumento().equals("F"))
					{
						if (comunicazione.getIdDocumento().getCodice2()!=null && comunicazione.getIdDocumento().getCodice2().length()!=0)
						{
							sqlUDP += ",anno_fattura=" +  comunicazione.getIdDocumento().getCodice2() ;  // anno_fattura
						}
						if (comunicazione.getIdDocumento().getCodice3()!=null && comunicazione.getIdDocumento().getCodice3().length()!=0)
						{
							sqlUDP += ",progr_fattura=" +  comunicazione.getIdDocumento().getCodice3() ;  // progr_fattura
						}
						sqlUDP += ",cod_tip_ord=''"  ;  // cod_tip_ord
						sqlUDP += ",anno_ord=0"  ;  // anno_ord
						sqlUDP += ",cod_ord=0"  ;  // cod_ord
					}
					if (comunicazione.getTipoMessaggio()!=null && comunicazione.getTipoMessaggio().length()!=0)
					{
						sqlUDP += ", tipo_msg= '" +  comunicazione.getTipoMessaggio()  + "'" ;
					}
					if (comunicazione.getTipoInvioComunicazione()!=null && comunicazione.getTipoInvioComunicazione().length()!=0)
					{
						sqlUDP += ", tipo_invio= '" +  comunicazione.getTipoInvioComunicazione()  + "'" ;
					}
					if (comunicazione.getFornitore().getCodice()!=null && comunicazione.getFornitore().getCodice().length()!=0)
					{
						sqlUDP += ", cod_fornitore= " +  comunicazione.getFornitore().getCodice()  ;
					}
					if (comunicazione.getStatoComunicazione()!=null && comunicazione.getStatoComunicazione().length()!=0)
					{
						sqlUDP += ", stato_msg='" +  comunicazione.getStatoComunicazione() + "'" ;
					}

					sqlUDP=this.struttura(sqlUDP);
					sqlUDP=sqlUDP + " fl_canc<>'S'";

					if (comunicazione.getCodPolo() !=null &&  comunicazione.getCodPolo().length()!=0)
					{
						sqlUDP=this.struttura(sqlUDP);
						sqlUDP=sqlUDP + " cd_polo='" + comunicazione.getCodPolo() +"'";
					}

					if (comunicazione.getCodBibl() !=null &&  comunicazione.getCodBibl().length()!=0)
					{
						sqlUDP=this.struttura(sqlUDP);
						sqlUDP=sqlUDP + " cd_bib='" + comunicazione.getCodBibl() +"'";
					}
					if (comunicazione.getCodiceMessaggio()!=null && comunicazione.getCodiceMessaggio().length()!=0 )
					{
						sqlUDP=this.struttura(sqlUDP);
						sqlUDP=sqlUDP + " cod_msg=" + comunicazione.getCodiceMessaggio() ;
					}


					if (comunicazione.getDataUpd()!=null )
					{
						sqlUDP=this.struttura(sqlUDP);
						sqlUDP=sqlUDP + " ts_var='" + comunicazione.getDataUpd() + "'" ;
					}


					pstmt = connection.prepareStatement(sqlUDP);
					log.debug("Debug: modifica comunicazione");
					log.debug("Debug: " + sqlUDP);

					int intRetUDP = 0;
					intRetUDP = pstmt.executeUpdate();
					pstmt.close();
					// fine estrazione codice
					if (intRetUDP==1){
						valRitornoUPD=true;
					}
					else
					{
						throw new ValidationException("operazioneInConcorrenza",
								ValidationExceptionCodici.operazioneInConcorrenza);

					}

				}
			}
			connection.close();
			// impostazione del codice di ritorno finale
			if (valRitornoUPD)
			{
				valRitorno=true;
			}
		}catch (ValidationException e) {
	      	  throw e;

		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

        return valRitorno;
	}

	public boolean  cancellaComunicazione(ComunicazioneVO comunicazione) throws DataException, ApplicationException, ValidationException
	{
		//Validazione.ValidaComunicazioneVO(comunicazione);
		int valRitornoInt=0;
    	int motivo=0;
    	OrdiniVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean valRitorno=false;
		boolean valRitornoPrep=false;
		Timestamp ts = DaoManager.now();

		try {

			connection = getConnection();

			String sql0="select * ";
			sql0=sql0 + " from  tra_messaggi  ";

			sql0=this.struttura(sql0);
			sql0=sql0 + " fl_canc<>'S'";

			if (comunicazione.getCodPolo() !=null &&  comunicazione.getCodPolo().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " cd_polo='" + comunicazione.getCodPolo() +"'";
			}

			if (comunicazione.getCodBibl() !=null &&  comunicazione.getCodBibl().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " cd_bib='" + comunicazione.getCodBibl() +"'";
			}
			if (comunicazione.getCodiceMessaggio()!=null && comunicazione.getCodiceMessaggio().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " cod_msg=" + comunicazione.getCodiceMessaggio() ;
			}


			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			valRitornoPrep=true; // se la esegue c'è un resultset
			// numero di righe del resultset
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;

			} //
			rs.close();

			if (numRighe > 1)
			{
				// n.b anche se esiste un ordine per lo stesso titolo si possono crearne altri
				motivo=2; // record non univoco
				throw new ValidationException("cambierroreModificaRecordNonUnivoco",
						ValidationExceptionCodici.cambierroreModificaRecordNonUnivoco);
			}

			if (numRighe==1)
			{

				String sqlDEL1="update tra_messaggi set " ;

				sqlDEL1=sqlDEL1  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
				sqlDEL1=sqlDEL1  + ", ute_var = '" +  comunicazione.getUtente()  + "'" ;  // ute_var
				sqlDEL1=sqlDEL1  + ", fl_canc = 'S'" ;  // fl_canc
				sqlDEL1=this.struttura(sqlDEL1);
				sqlDEL1=sqlDEL1 + " fl_canc<>'S'";

				if (comunicazione.getCodPolo() !=null &&  comunicazione.getCodPolo().length()!=0)
				{
					sqlDEL1=this.struttura(sqlDEL1);
					sqlDEL1=sqlDEL1 + " cd_polo='" + comunicazione.getCodPolo() +"'";
				}

				if (comunicazione.getCodBibl() !=null &&  comunicazione.getCodBibl().length()!=0)
				{
					sqlDEL1=this.struttura(sqlDEL1);
					sqlDEL1=sqlDEL1 + " cd_bib='" + comunicazione.getCodBibl() +"'";
				}
				if (comunicazione.getCodiceMessaggio()!=null && comunicazione.getCodiceMessaggio().length()!=0 )
				{
					sqlDEL1=this.struttura(sqlDEL1);
					sqlDEL1=sqlDEL1  + " cod_msg=" + comunicazione.getCodiceMessaggio() ;
				}
				pstmt = connection.prepareStatement(sqlDEL1);
				valRitornoInt = pstmt.executeUpdate();
				if (valRitornoInt==1){
					valRitorno=true;
				}else{
					valRitorno=false;
				}
			}
			connection.close();
		}catch (ValidationException e) {
	      	  throw e;
		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}

     return valRitorno;
	}

	public List<SuggerimentoVO> getRicercaListaSuggerimenti(ListaSuppSuggerimentoVO ricercaSuggerimenti)
		throws ResourceNotFoundException, ApplicationException, ValidationException  {
		List<SuggerimentoVO> listaSuggerimenti = new ArrayList<SuggerimentoVO>();
		Connection connection = null;
		try {

			connection = getConnection();
			listaSuggerimenti = getRicercaListaSuggerimenti(connection, ricercaSuggerimenti);
			connection.close();
		} catch (SQLException e) {
			log.error("", e);
		} finally {
			close(connection);
		}
		Validazione.ValidaRicercaSuggerimenti (listaSuggerimenti);
        return listaSuggerimenti;
	}

	public List<SuggerimentoVO> getRicercaListaSuggerimenti(Connection connection, ListaSuppSuggerimentoVO ricercaSuggerimenti)
		throws ResourceNotFoundException, ApplicationException, ValidationException  {

		Validazione.ValidaListaSuppSuggerimentoVO(ricercaSuggerimenti);
		String ticket="";
		ticket=ricercaSuggerimenti.getTicket();

		List<SuggerimentoVO> listaSuggerimenti = new ArrayList<SuggerimentoVO>();

		int ret = 0;
        // execute the search here
			SuggerimentoVO rec = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			PreparedStatement pstmtCod = null;
			ResultSet rsCod = null;
			PreparedStatement pstmtCount = null;
			ResultSet rsCount = null;


			try {
				// contiene i criteri di ricerca
				//String sql="select suggBibl.*,bibliot.cognome,bibliot.nome,sez.cod_sezione, sez.nome, TO_CHAR(suggBibl.data_sugg_bibl,'dd/MM/yyyy') as data_sugg_bibl_str ";

				String sql="select suggBibl.*,suggBibl.ts_var as dataUpd, bibliot.cognome,bibliot.nome as nomeBibl, uteprofweb.userid, sez.cod_sezione, sez.nome, TO_CHAR(suggBibl.data_sugg_bibl,'dd/MM/yyyy') as data_sugg_bibl_str " ;
				//		", codi.ds_tabella ";
				sql +=" from  tba_suggerimenti_bibliografici suggBibl ";
				sql +=" left join tbf_anagrafe_utenti_professionali bibliot on bibliot.id_utente_professionale=suggBibl.cod_bibliotecario  and bibliot.fl_canc<>'S'";
				sql +=" left join tbf_utenti_professionali_web uteprofweb on uteprofweb.id_utente_professionale=suggBibl.cod_bibliotecario and uteprofweb.fl_canc<>'S'";
				sql +=" left join tba_sez_acquis_bibliografiche sez on sez.id_sez_acquis_bibliografiche=suggBibl.id_sez_acquis_bibliografiche and  sez.cd_bib=suggBibl.cd_bib and  sez.cd_polo=suggBibl.cd_polo and  sez.fl_canc<>'S'";

				sql=this.struttura(sql);
				sql +=" suggBibl.fl_canc<>'S'";

				if (ricercaSuggerimenti.getCodPolo() !=null &&  ricercaSuggerimenti.getCodPolo().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" suggBibl.cd_polo='" + ricercaSuggerimenti.getCodPolo() +"'";
				}

				if (ricercaSuggerimenti.getCodBibl() !=null &&  ricercaSuggerimenti.getCodBibl().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" suggBibl.cd_bib='" + ricercaSuggerimenti.getCodBibl() +"'";
				}

				if (ricercaSuggerimenti.getCodiceSuggerimento()!=null && ricercaSuggerimenti.getCodiceSuggerimento().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" suggBibl.cod_sugg_bibl=" + ricercaSuggerimenti.getCodiceSuggerimento().trim() ;
				}
				if (ricercaSuggerimenti.getDataSuggerimentoDa()!=null && ricercaSuggerimenti.getDataSuggerimentoDa().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" suggBibl.data_sugg_bibl >= TO_DATE('" +  ricercaSuggerimenti.getDataSuggerimentoDa() + "','dd/MM/yyyy')";
				}

				if (ricercaSuggerimenti.getDataSuggerimentoA()!=null && ricercaSuggerimenti.getDataSuggerimentoA().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" suggBibl.data_sugg_bibl<= TO_DATE ('" +  ricercaSuggerimenti.getDataSuggerimentoA() + "','dd/MM/yyyy')";
				}

				if (ricercaSuggerimenti.getStatoSuggerimento()!=null && ricercaSuggerimenti.getStatoSuggerimento().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" suggBibl.stato_sugg='" + ricercaSuggerimenti.getStatoSuggerimento() +"'";
				}
				if (ricercaSuggerimenti.getIdSugList()!=null && ricercaSuggerimenti.getIdSugList().size()!=0 )
				{
					String listID="(";
					for (int index3 = 0; index3 < ricercaSuggerimenti.getIdSugList().size(); index3++) {
						if (ricercaSuggerimenti.getIdSugList().get(index3)>0)
						{
							if (index3>0)
							{
								listID=listID+",";
							}
							listID=listID+ ricercaSuggerimenti.getIdSugList().get(index3);
						}
					}
					listID=listID+")";
					sql=this.struttura(sql);
					sql +=" suggBibl.cod_sugg_bibl in " + listID;
				}


				if (ricercaSuggerimenti.getBibliotecario() != null && ricercaSuggerimenti.getBibliotecario().getDescrizione()!=null && ricercaSuggerimenti.getBibliotecario().getDescrizione().length()!=0 )
				{
					sql=this.struttura(sql);
					sql +=" suggBibl.cod_bibliotecario=" + ricercaSuggerimenti.getBibliotecario().getDescrizione().trim() ; // id
				}

				if (ricercaSuggerimenti.getBibliotecario() != null && ricercaSuggerimenti.getBibliotecario().getCodice()!=null && ricercaSuggerimenti.getBibliotecario().getCodice().length()!=0 )
				{
					sql=this.struttura(sql);
					sql +=" uteprofweb.userid='" + ricercaSuggerimenti.getBibliotecario().getCodice() + "'" ; // username
				}

				if (ricercaSuggerimenti.getTitolo()!=null && ricercaSuggerimenti.getTitolo().getCodice()!=null && ricercaSuggerimenti.getTitolo().getCodice().length()!=0 )
				{
					sql=this.struttura(sql);
					sql +=" suggBibl.bid='" + ricercaSuggerimenti.getTitolo().getCodice() +"'" ;
				}


				// creazione query per calcolo dei risultati con esclusione dell'order by
				String sqlXCount="";
				int pos=sql.lastIndexOf("from");
				int totSugg=0;
				if (pos>0)
				{
					sqlXCount="select count(distinct suggBibl.cod_sugg_bibl) as tot " +sql.substring(pos);
					pstmtCount = connection.prepareStatement(sqlXCount);
					rsCount = pstmtCount.executeQuery();
					if(rsCount.next()) {
						totSugg=rsCount.getInt("tot");
					}
					rsCount.close();
					pstmtCount.close();
					if (totSugg>1000 ) // aggiungi condizioni di esclusione del conteggio
			        {
				        	throw new ValidationException("ricercaDaRaffinareTroppi",
				        			ValidationExceptionCodici.ricercaDaRaffinareTroppi);
			        }

				}


				// ordinamento passato
				if (ricercaSuggerimenti.getOrdinamento()==null || (ricercaSuggerimenti.getOrdinamento()!=null &&  ricercaSuggerimenti.getOrdinamento().equals("")))
				{
					sql +=" order by suggBibl.cd_bib,suggBibl.cod_sugg_bibl  ";
				}
				else if (ricercaSuggerimenti.getOrdinamento().equals("1"))
				{
					sql +=" order by suggBibl.cd_bib,suggBibl.cod_sugg_bibl   ";
				}
				else if (ricercaSuggerimenti.getOrdinamento().equals("2"))
				{
					sql +=" order by suggBibl.cd_bib, suggBibl.stato_sugg  ";
				}
				else if (ricercaSuggerimenti.getOrdinamento().equals("3"))
				{
					sql +=" order by suggBibl.cd_bib,  suggBibl.data_sugg_bibl desc";
				}
				else if (ricercaSuggerimenti.getOrdinamento().equals("4"))
				{
					//suggBibl.cod_bibliotecario
					sql +=" order by suggBibl.cd_bib, suggBibl.ute_var  ";
				}
				else if (ricercaSuggerimenti.getOrdinamento().equals("5"))
				{
					sql +=" order by suggBibl.cd_bib, sez.cod_sezione ";
				}

				pstmt = connection.prepareStatement(sql);

				rs = pstmt.executeQuery();
				int numRighe=0;
				while (rs.next()) {
					numRighe=numRighe+1;
					rec = new SuggerimentoVO();
					rec.setCodPolo(rs.getString("cd_polo"));
					rec.setCodBibl(rs.getString("cd_bib"));
					rec.setProgressivo(numRighe);
					rec.setCodiceSuggerimento(String.valueOf(rs.getInt("cod_sugg_bibl")));
					rec.setDataSuggerimento(rs.getString("data_sugg_bibl_str"));
					rec.setNoteSuggerimento(rs.getString("note"));
					rec.setNoteBibliotecario(rs.getString("msg_per_bibl"));
					rec.setNoteFornitore(rs.getString("note_forn"));
					rec.setStatoSuggerimento(rs.getString("stato_sugg"));
					rec.setDenoStatoSuggerimento("");
					// ricerca codice da tb_codici
					String sqlCod=" select  codi.ds_tabella from tb_codici codi where codi.tp_tabella='ASTS' and codi.cd_tabella='" + rec.getStatoSuggerimento() + "'" ;
					pstmtCod = connection.prepareStatement(sqlCod);
					rsCod = pstmtCod.executeQuery();
					while (rsCod.next()) {
						rec.setDenoStatoSuggerimento(rsCod.getString("ds_tabella"));
					}
					pstmtCod.close();
					rsCod.close();

					rec.setBibliotecario(new StrutturaCombo("",""));
					if (rs.getString("userid")!=null)
					{
						rec.getBibliotecario().setCodice(rs.getString("userid"));
					}
					if (!rec.getBibliotecario().getCodice().equals("0"))
					{
						if (rs.getString("cognome")!=null ) // && rs.getString("cognome").trim().length()>0
						{
							rec.setNominativoBibliotecario(rs.getString("cognome").trim());
							if (rs.getString("nomeBibl")!=null && rs.getString("nomeBibl").trim().length()>0)
							{
								String appo=rec.getNominativoBibliotecario();
								rec.setNominativoBibliotecario(appo + " - " + rs.getString("nomeBibl").trim());
							}
						}
					}

					// temporaneamente
					rec.getBibliotecario().setDescrizione(String.valueOf(rs.getInt("cod_bibliotecario")));
					rec.setSezione(new StrutturaCombo("",""));
					rec.getSezione().setCodice(rs.getString("cod_sezione"));
					rec.setIDSez(rs.getInt("id_sez_acquis_bibliografiche"));
					rec.getSezione().setDescrizione(ValidazioneDati.trimOrEmpty(rs.getString("nome")));
					rec.setDataUpd(rs.getTimestamp("dataUpd"));

					//rec.setTitolo(new StrutturaCombo("",""));
					String isbd="";
					String bid="";
					String naturaBid="";
					//try {
//					 momentaneo
					if (rs.getString("bid")!=null && rs.getString("bid").trim().length()!=0)
					{
						try {
							bid=rs.getString("bid");

							TitoloACQVO recTit=null;
							recTit = this.getTitoloOrdineTer(rs.getString("bid").trim());
							if (recTit!=null && recTit.getIsbd()!=null) {
								bid=rs.getString("bid").trim();
								isbd=recTit.getIsbd();
								naturaBid=recTit.getNatura();
							}
							if (recTit==null) {
								isbd="titolo non trovato";
								bid=rs.getString("bid");
							}

						} catch (Exception e) {
							isbd="titolo non trovato";
							bid=rs.getString("bid");
						}
					}



					rec.setTitolo(new StrutturaCombo(bid,isbd));
					rec.setNaturaBid(naturaBid);
					listaSuggerimenti.add(rec);
				} // End while

				rs.close();
				pstmt.close();
				//connection.close();

			}	catch (ValidationException e) {

		    	  throw e;



		} catch (Exception e) {

			// l'errore capita in questo punto
			log.error("", e);
		}
		Validazione.ValidaRicercaSuggerimenti (listaSuggerimenti);
        return listaSuggerimenti;
	}

	public boolean  inserisciSuggerimento(SuggerimentoVO suggerimento) throws DataException, ApplicationException , ValidationException
	{
		Validazione.ValidaSuggerimentoVO(suggerimento);
		boolean valRitorno=false;
    	int motivo=0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		PreparedStatement pstmtESISTESEZ = null;
		ResultSet rsESISTESEZ = null;


		boolean valRitornoINS=false;
		boolean esistenzaSuggerimento=false;
		PreparedStatement pstmtCod= null;
		ResultSet rsSubCod= null;

		try {
			// CONTROLLI PREVENTIVI
			connection = getConnection();
			String sql0="select suggBibl.*,bibliot.cognome, bibliot.nome, sez.cod_sezione, sez.nome, TO_CHAR(suggBibl.data_sugg_bibl,'dd/MM/yyyy') as data_sugg_bibl_str ";
			sql0=sql0 + " from  tba_suggerimenti_bibliografici suggBibl ";
			sql0=sql0 + " left join tbf_anagrafe_utenti_professionali bibliot on bibliot.id_utente_professionale=suggBibl.cod_bibliotecario and bibliot.fl_canc<>'S' ";
			sql0=sql0 + " left join trf_utente_professionale_biblioteca utebibliot on utebibliot.id_utente_professionale=bibliot.id_utente_professionale and  utebibliot.cd_biblioteca=suggBibl.cd_bib and  utebibliot.cd_polo=suggBibl.cd_polo ";
			sql0=sql0 + " left join tba_sez_acquis_bibliografiche sez on sez.id_sez_acquis_bibliografiche=suggBibl.id_sez_acquis_bibliografiche and  sez.cd_bib=suggBibl.cd_bib and  sez.cd_polo=suggBibl.cd_polo and sez.fl_canc<>'S' ";

			sql0=this.struttura(sql0);
			sql0=sql0 + " suggBibl.fl_canc<>'S'";

			if (suggerimento.getCodPolo() !=null &&  suggerimento.getCodPolo().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " suggBibl.cd_polo='" + suggerimento.getCodPolo() +"'";
			}

			if (suggerimento.getCodBibl() !=null &&  suggerimento.getCodBibl().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " suggBibl.cd_bib='" + suggerimento.getCodBibl() +"'";
			}

			if (suggerimento.getStatoSuggerimento()!=null && suggerimento.getStatoSuggerimento().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " suggBibl.stato_sugg='" + suggerimento.getStatoSuggerimento() +"'";
			}

			if (suggerimento.getBibliotecario().getDescrizione()!=null && suggerimento.getBibliotecario().getDescrizione().length()!=0  )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " suggBibl.cod_bibliotecario=" + suggerimento.getBibliotecario().getDescrizione() ;
			}
			if (suggerimento.getTitolo().getCodice()!=null && suggerimento.getTitolo().getCodice().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " suggBibl.bid='" + suggerimento.getTitolo().getCodice() +"'" ;
			}

			if (suggerimento.getSezione().getCodice()!=null && suggerimento.getSezione().getCodice().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " upper(sez.cod_sezione)='" + suggerimento.getSezione().getCodice().trim().toUpperCase()+"'"  ;
			}

			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			Timestamp ts = DaoManager.now();
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;
				}
			if (numRighe>0)
			{
				esistenzaSuggerimento=true; // record forse già esistente quindi non inseribile

				throw new ValidationException("cambierroreInserimentoEsistenzaRecord",
						ValidationExceptionCodici.cambierroreInserimentoEsistenzaRecord);
			}

			pstmt.close();
			if (!esistenzaSuggerimento)
			{
				if (suggerimento.getSezione()!=null && suggerimento.getSezione().getCodice()!=null && suggerimento.getSezione().getCodice().trim().length()!=0)
				{
					// CONTROLLO ESISTENZA SEZIONE
					String sqlESISTESEZ="select * from  tba_sez_acquis_bibliografiche sez " ;
					sqlESISTESEZ=this.struttura(sqlESISTESEZ);
					sqlESISTESEZ=sqlESISTESEZ + " sez.cd_polo='" + suggerimento.getCodPolo() +"'";
					sqlESISTESEZ=this.struttura(sqlESISTESEZ);
					sqlESISTESEZ=sqlESISTESEZ + " sez.cd_bib='" + suggerimento.getCodBibl() +"'";
					sqlESISTESEZ=this.struttura(sqlESISTESEZ);
					sqlESISTESEZ=sqlESISTESEZ + " upper(sez.cod_sezione)='" + suggerimento.getSezione().getCodice().trim().toUpperCase()+"'" ;
					sqlESISTESEZ=this.struttura(sqlESISTESEZ);
					sqlESISTESEZ=sqlESISTESEZ + " sez.fl_canc<>'S'";
					sqlESISTESEZ=this.struttura(sqlESISTESEZ);
					sqlESISTESEZ=sqlESISTESEZ + "(sez.data_val is null or sez.data_val>(SELECT CURRENT_DATE ))";

					pstmtESISTESEZ = connection.prepareStatement(sqlESISTESEZ);
					rsESISTESEZ = pstmtESISTESEZ.executeQuery(); // va in errore se non può restituire un recordset
					int numRigheSez=0;
					int id_sezione=0;
					while (rsESISTESEZ.next()) {
						numRigheSez=numRigheSez+1;
						id_sezione=rsESISTESEZ.getInt("id_sez_acquis_bibliografiche");
					}
					rsESISTESEZ.close();
					pstmtESISTESEZ.close();
					if (numRigheSez!=1)
					{
						throw new ValidationException("ordineIncongruenzaSezioneInesistente",
								ValidationExceptionCodici.ordineIncongruenzaSezioneInesistente);
					}
				}

				// INSERIMENTO
				String sqlSub="insert into tba_suggerimenti_bibliografici values ( " ;
				sqlSub=sqlSub + "'" + suggerimento.getCodPolo() + "'" ;  // cd_polo
				sqlSub=sqlSub + ",'" + suggerimento.getCodBibl() + "'" ;  // cd_bib
				// INIZIO SUBQUERY cod_sugg_bibl
				sqlSub=sqlSub + ", (SELECT CASE WHEN  (MAX(tba_suggerimenti_bibliografici.cod_sugg_bibl) is null) THEN 1  else MAX(tba_suggerimenti_bibliografici.cod_sugg_bibl)+1  END " ;
				sqlSub=sqlSub + " from tba_suggerimenti_bibliografici   ";
				sqlSub=sqlSub + " where cd_bib='" + suggerimento.getCodBibl() +"'";
				sqlSub=sqlSub + " and cd_polo='" + suggerimento.getCodPolo() +"'";
				sqlSub=sqlSub + " )";

				sqlSub=sqlSub + ",(SELECT CURRENT_DATE ) "  ;  // data_sugg_bibl
				sqlSub=sqlSub + ",'" + suggerimento.getNoteSuggerimento().replace("'","''") + "'" ;  // note
				sqlSub=sqlSub + ",'" + suggerimento.getNoteBibliotecario().replace("'","''") + "'" ;  // msg_per_bibl
				sqlSub=sqlSub + ",'" + suggerimento.getNoteFornitore().replace("'","''") + "'" ;  // note_forn
				// TODO INSERIRE IL CODICE DELLA BIBLIOTECA CENTRO SISTEMA
				sqlSub=sqlSub + ",'" + suggerimento.getCodBibl() + "'" ;  // cd_bib_cs
				if (suggerimento.getTitolo()!=null)
				{
					sqlSub=sqlSub + ",'" + suggerimento.getTitolo().getCodice() + "'" ;  // bid
				}
				else
				{
					sqlSub=sqlSub + ",''"  ;  // bid
				}
			if (suggerimento.getBibliotecario()!=null && !suggerimento.getBibliotecario().getDescrizione().equals("") && !suggerimento.getBibliotecario().getDescrizione().equals("0") )
				{
					sqlSub=sqlSub + "," + Integer.valueOf(suggerimento.getBibliotecario().getDescrizione().trim())  ;  // cd_bibliotecario
				}
				else
				{
					sqlSub=sqlSub + ",0"  ;  // cd_bibliotecario
				}

				// temporaneamente
				sqlSub=sqlSub + ",'" + suggerimento.getStatoSuggerimento() + "'" ;  // stato_sugg
				sqlSub=sqlSub + ",'" + suggerimento.getUtente() + "'" ;   // ute_ins
				sqlSub=sqlSub + ",'" + ts + "'" ;   // ts_ins
				sqlSub=sqlSub + ",'" + suggerimento.getUtente() + "'" ;   // ute_var
				sqlSub=sqlSub + ",'" + ts + "'";   // ts_var
				sqlSub=sqlSub + ",'N'";   // fl_canc

				if (suggerimento.getSezione()!=null && suggerimento.getSezione().getCodice()!=null && suggerimento.getSezione().getCodice().trim().length()!=0)
				{
					//sqlSub=sqlSub + ",'" + suggerimento.getIDSez() + "'" ;  // id_sez_acquis_bibliografiche
					// INIZIO SUBQUERY cod_sugg_bibl
					sqlSub=sqlSub + ", (SELECT id_sez_acquis_bibliografiche " ;
					sqlSub=sqlSub + " from tba_sez_acquis_bibliografiche   ";
					sqlSub=sqlSub + " where cd_bib='" + suggerimento.getCodBibl() +"'";
					sqlSub=sqlSub + " and cd_polo='" + suggerimento.getCodPolo() +"'";
					sqlSub=sqlSub + " and upper(cod_sezione)='" + suggerimento.getSezione().getCodice().trim().toUpperCase() +"'";
					sqlSub=sqlSub + " and fl_canc<>'S'";
					sqlSub=sqlSub + " )";
				}
				else
				{
					sqlSub=sqlSub + ",null"  ;  // id_sez_acquis_bibliografiche
				}
				sqlSub=sqlSub + ")" ;
				pstmt = connection.prepareStatement(sqlSub);

				log.debug("Debug: inserimento suggerimento bibl.");
				log.debug("Debug: " + sqlSub);

				int intRetINS = 0;
				intRetINS = pstmt.executeUpdate();
				pstmt.close();
				if (intRetINS==1){
					// deduzione del codice appena inserito per successivi insert
					String sqlCodice="select cod_sugg_bibl from tba_suggerimenti_bibliografici ";
					//sqlCodice=sqlCodice + " where ts_ins='" +ts+ "'";
					sqlCodice=sqlCodice + " where ute_ins='" + suggerimento.getUtente()+ "'";
					sqlCodice=sqlCodice + " order by ts_ins desc limit 1";

					pstmtCod = connection.prepareStatement(sqlCodice);
					rsSubCod = pstmtCod.executeQuery();
					if (rsSubCod.next()) {
						suggerimento.setCodiceSuggerimento(String.valueOf(rsSubCod.getInt("cod_sugg_bibl")));
					}
					rsSubCod.close();
					pstmtCod.close();
					valRitornoINS=true;
				}
			}
			rs.close();
			connection.close();

			// impostazione del codice di ritorno finale
			if (valRitornoINS)
			{
				valRitorno=true;
			}
		}catch (ValidationException e) {

	      	  throw e;

		} catch (Exception e) {

			log.error("", e);
		} finally {
			close(connection);
		}
        return valRitorno;
	}


	public boolean modificaSuggerimento(SuggerimentoVO suggerimento)
			throws DataException, ApplicationException, ValidationException {
		Connection connection = null;
		try {
			connection = getConnection();
			Boolean modificaSuggerimento = modificaSuggerimento(connection, suggerimento);
			connection.close();
			return modificaSuggerimento;
		} catch (SQLException e) {

			log.error("", e);
			return false;
		} finally {
			close(connection);
		}
	}

	public Boolean modificaSuggerimento(Connection connection,
			SuggerimentoVO suggerimento) throws DataException, ApplicationException , ValidationException {

		Validazione.ValidaSuggerimentoVO(suggerimento);
		boolean valRitorno=false;
    	int motivo=0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean valRitornoUPD=false;
		boolean esistenzaSuggerimento=false;
		PreparedStatement pstmtESISTESEZ = null;
		ResultSet rsESISTESEZ = null;

		try {
			// CONTROLLI PREVENTIVI
			String sql0="select * ";
			sql0=sql0 + " from  tba_suggerimenti_bibliografici  ";

			sql0=this.struttura(sql0);
			sql0=sql0 + " fl_canc<>'S'";

			if (suggerimento.getCodPolo() !=null &&  suggerimento.getCodPolo().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " cd_polo='" + suggerimento.getCodPolo() +"'";
			}

			if (suggerimento.getCodBibl() !=null &&  suggerimento.getCodBibl().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " cd_bib='" + suggerimento.getCodBibl() +"'";
			}
			if (suggerimento.getCodiceSuggerimento()!=null && suggerimento.getCodiceSuggerimento().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " cod_sugg_bibl=" + suggerimento.getCodiceSuggerimento() ;
			}

			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			int numRighe=0;
			List<StrutturaCombo> righeOggetto=new ArrayList();
			Timestamp ts = DaoManager.now();
			while (rs.next()) {
				numRighe=numRighe+1;
			}
			rs.close();
			pstmt.close();
			if (numRighe >= 1)
			{
				if (numRighe == 1)
				{
					esistenzaSuggerimento=true;
				}
			}
			if (!esistenzaSuggerimento )
			{
				throw new ValidationException("cambierroreModificaRecordNonUnivoco",
						ValidationExceptionCodici.cambierroreModificaRecordNonUnivoco);
			}
			if (esistenzaSuggerimento)
				{
					if (suggerimento.getSezione()!=null && suggerimento.getSezione().getCodice()!=null && suggerimento.getSezione().getCodice().length()!=0)
					{
						// CONTROLLO ESISTENZA SEZIONE
						String sqlESISTESEZ="select * from  tba_sez_acquis_bibliografiche sez " ;
						sqlESISTESEZ=this.struttura(sqlESISTESEZ);
						sqlESISTESEZ=sqlESISTESEZ + " sez.cd_polo='" + suggerimento.getCodPolo() +"'";
						sqlESISTESEZ=this.struttura(sqlESISTESEZ);
						sqlESISTESEZ=sqlESISTESEZ + " sez.cd_bib='" + suggerimento.getCodBibl() +"'";
						sqlESISTESEZ=this.struttura(sqlESISTESEZ);
						sqlESISTESEZ=sqlESISTESEZ + " upper(sez.cod_sezione)='" + suggerimento.getSezione().getCodice().trim().toUpperCase()+"'" ;
						sqlESISTESEZ=this.struttura(sqlESISTESEZ);
						sqlESISTESEZ=sqlESISTESEZ + " sez.fl_canc<>'S'";
						sqlESISTESEZ=this.struttura(sqlESISTESEZ);
						sqlESISTESEZ=sqlESISTESEZ + "(sez.data_val is null or sez.data_val>(SELECT CURRENT_DATE ))";

						pstmtESISTESEZ = connection.prepareStatement(sqlESISTESEZ);
						rsESISTESEZ = pstmtESISTESEZ.executeQuery(); // va in errore se non può restituire un recordset
						int numRigheSez=0;
						int id_sezione=0;
						while (rsESISTESEZ.next()) {
							numRigheSez=numRigheSez+1;
							id_sezione=rsESISTESEZ.getInt("id_sez_acquis_bibliografiche");
						}
						rsESISTESEZ.close();
						pstmtESISTESEZ.close();
						if (numRigheSez!=1)
						{
							throw new ValidationException("ordineIncongruenzaSezioneInesistente",
									ValidationExceptionCodici.ordineIncongruenzaSezioneInesistente);
						}
					}

					String sqlUDP="update tba_suggerimenti_bibliografici set " ;
					//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
					sqlUDP= sqlUDP + " ts_var = '" +  ts  + "'" ;  // data_agg
					sqlUDP= sqlUDP + ", ute_var = '" +  suggerimento.getUtente()  + "'" ;  // ute_var
					if (suggerimento.getDataSuggerimento()!=null && suggerimento.getDataSuggerimento().length()!=0)
					{
						sqlUDP += ", data_sugg_bibl=TO_DATE ('" +  suggerimento.getDataSuggerimento() + "','dd/MM/yyyy')";
					}
					if (suggerimento.getNoteSuggerimento()!=null && suggerimento.getNoteSuggerimento().length()!=0)
					{
						sqlUDP += ", note= '" +  suggerimento.getNoteSuggerimento().replace("'","''")  + "'" ;
					}
					if (suggerimento.getNoteBibliotecario()!=null && suggerimento.getNoteBibliotecario().length()!=0)
					{
						sqlUDP += ", msg_per_bibl= '" +  suggerimento.getNoteBibliotecario().replace("'","''")  + "'" ;
					}
					if (suggerimento.getNoteFornitore()!=null && suggerimento.getNoteFornitore().length()!=0)
					{
						sqlUDP += ", note_forn= '" +  suggerimento.getNoteFornitore().replace("'","''")  + "'" ;
					}
					if (suggerimento.getSezione()!=null && suggerimento.getSezione().getCodice()!=null && suggerimento.getSezione().getCodice().length()!=0)
					{
						sqlUDP += ", id_sez_acquis_bibliografiche="  ;
						sqlUDP=sqlUDP + " (SELECT id_sez_acquis_bibliografiche " ;
						sqlUDP=sqlUDP + " from tba_sez_acquis_bibliografiche   ";
						sqlUDP=sqlUDP + " where cd_bib='" + suggerimento.getCodBibl() +"'";
						sqlUDP=sqlUDP + " and cd_polo='" + suggerimento.getCodPolo() +"'";
						sqlUDP=sqlUDP + " and upper(cod_sezione)='" + suggerimento.getSezione().getCodice().trim().toUpperCase() +"'";
						sqlUDP=sqlUDP + " and fl_canc<>'S'";
						//sql +=" and progr_fattura = (SELECT EXTRACT(YEAR FROM TIMESTAMP '" + ts  + "'))" ;
						sqlUDP=sqlUDP + " )";
					}
					else
					{
						sqlUDP += ", id_sez_acquis_bibliografiche=null"  ;
					}


					if (suggerimento.getTitolo()!=null && suggerimento.getTitolo().getCodice()!=null && suggerimento.getTitolo().getCodice().length()!=0)
					{
						sqlUDP += ", bid= '" +  suggerimento.getTitolo().getCodice()  + "'" ;
					}
					if (suggerimento.getStatoSuggerimento()!=null && suggerimento.getStatoSuggerimento().length()!=0)
					{
						sqlUDP += ", stato_sugg= '" +  suggerimento.getStatoSuggerimento()  + "'" ;
					}
					// CONDIZIONI
					//sqlUDP=this.struttura(sqlUDP); // c'è un where interno che crea l'errore
					sqlUDP=sqlUDP + " where fl_canc<>'S'";

					if (suggerimento.getCodPolo()!=null &&  suggerimento.getCodPolo().length()!=0)
					{
						sqlUDP=this.struttura(sqlUDP);
						sqlUDP=sqlUDP + " cd_polo='" + suggerimento.getCodPolo() +"'";
					}

					if (suggerimento.getCodBibl()!=null &&  suggerimento.getCodBibl().length()!=0)
					{
						sqlUDP=this.struttura(sqlUDP);
						sqlUDP=sqlUDP + " cd_bib='" + suggerimento.getCodBibl() +"'";
					}
					if (suggerimento.getCodiceSuggerimento()!=null && suggerimento.getCodiceSuggerimento().length()!=0 )
					{
						sqlUDP=this.struttura(sqlUDP);
						sqlUDP=sqlUDP + " cod_sugg_bibl=" + suggerimento.getCodiceSuggerimento() ;
					}
					if (suggerimento.getDataUpd()!=null )
					{
						sqlUDP=this.struttura(sqlUDP);
						sqlUDP=sqlUDP + " ts_var='" + suggerimento.getDataUpd() + "'" ;
					}

					pstmt = connection.prepareStatement(sqlUDP);
					log.debug("Debug: modifica suggerimento bibl.");
					log.debug("Debug: " + sqlUDP);

					int intRetUDP = 0;
					intRetUDP = pstmt.executeUpdate();
					pstmt.close();
					// fine estrazione codice
					if (intRetUDP==1){
						valRitornoUPD=true;
					}else{
						throw new ValidationException("operazioneInConcorrenza",
								ValidationExceptionCodici.operazioneInConcorrenza);
					}

				}
			//connection.close();
			// impostazione del codice di ritorno finale
			if (valRitornoUPD)
			{
				valRitorno=true;
			}
		}catch (ValidationException e) {

	      	  throw e;


		} catch (Exception e) {

			log.error("", e);
		}
        return valRitorno;
	}

	public boolean  cancellaSuggerimento(SuggerimentoVO suggerimento) throws DataException, ApplicationException, ValidationException
	{
		//Validazione.ValidaSuggerimentoVO(suggerimento);
		int valRitornoInt=0;
    	int motivo=0;
    	OrdiniVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean valRitorno=false;
		boolean valRitornoPrep=false;
		Timestamp ts = DaoManager.now();

		try {
			// CONTROLLI PREVENTIVI

			connection = getConnection();

			String sql0="select * ";
			sql0=sql0 + " from  tba_suggerimenti_bibliografici  ";

			sql0=this.struttura(sql0);
			sql0=sql0 + " fl_canc<>'S'";

			if (suggerimento.getCodBibl() !=null &&  suggerimento.getCodBibl().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " cd_bib='" + suggerimento.getCodBibl() +"'";
			}
			if (suggerimento.getCodPolo() !=null &&  suggerimento.getCodPolo().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " cd_polo='" + suggerimento.getCodPolo() +"'";
			}

			if (suggerimento.getCodiceSuggerimento()!=null && suggerimento.getCodiceSuggerimento().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " cod_sugg_bibl=" + suggerimento.getCodiceSuggerimento() ;
			}


			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			valRitornoPrep=true; // se la esegue c'è un resultset
			// numero di righe del resultset
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;

			} //
			rs.close();

			if (numRighe > 1)
			{
				// n.b anche se esiste un ordine per lo stesso titolo si possono crearne altri
				motivo=2; // record non univoco
				throw new ValidationException("cambierroreModificaRecordNonUnivoco",
						ValidationExceptionCodici.cambierroreModificaRecordNonUnivoco);
			}

			if (numRighe==1)
			{
				//connection = getConnection();
				//String sql="delete from tba_suggerimenti_bibliografici ";
				// cancellazione logica
				String sql="update tba_suggerimenti_bibliografici set " ;
				//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
				sql += " ts_var = '" +  ts  + "'" ;  // ex data_agg
				sql += ", ute_var = '" +  suggerimento.getUtente()  + "'" ;  // ute_var
				sql += ", fl_canc = 'S'" ;  // fl_canc
				sql=this.struttura(sql);
				sql +=" fl_canc<>'S'";

				if (suggerimento.getCodPolo() !=null &&  suggerimento.getCodPolo().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" cd_polo='" + suggerimento.getCodPolo() +"'";
				}

				if (suggerimento.getCodBibl() !=null &&  suggerimento.getCodBibl().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" cd_bib='" + suggerimento.getCodBibl() +"'";
				}
				if (suggerimento.getCodiceSuggerimento()!=null && suggerimento.getCodiceSuggerimento().length()!=0 )
				{
					sql=this.struttura(sql);
					sql +=" cod_sugg_bibl=" + suggerimento.getCodiceSuggerimento() ;
				}

				pstmt = connection.prepareStatement(sql);
				valRitornoInt = pstmt.executeUpdate();
				if (valRitornoInt==1){
					valRitorno=true;
				}else{
					valRitorno=false;
				}
			}
			connection.close();
		}catch (ValidationException e) {

	      	  throw e;

		}
		 catch (Exception e) {

			log.error("", e);
		} finally {
			close(connection);
		}
     return valRitorno;
	}

	public boolean  modificaConfigurazione(ConfigurazioneBOVO configurazione) throws DataException, ApplicationException , ValidationException
	{
		//Validazione.ValidaBuoniOrdineVO (buonoOrd);
		boolean valRitorno=false;
    	int motivo=0;
    	//FornitoreVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtUDP = null;
		PreparedStatement pstmtDEL = null;
		PreparedStatement pstmtINS = null;
		PreparedStatement pstmtExistOrd = null;
		PreparedStatement pstmtUDP2 = null;


		ResultSet rsExistOrd= null;
		ResultSet rs = null;
		ResultSet rsSub = null;

		boolean controlloCONGR=false;
		ResultSet rsESISTEORD = null;
		PreparedStatement pstmtESISTEORD = null;

		boolean valRitornoUPD=false;
		boolean valRitornoINSLOOP=false;
		boolean valRitornoCANC=false;
		boolean valRitornoINS=false;

		try {
			connection = getConnection();
			String sql0="select * ";
			sql0=sql0  + " from  tba_parametri_buono_ordine ";

			sql0=this.struttura(sql0);
			sql0=sql0 + " fl_canc<>'S'";

			if (configurazione.getCodPolo() !=null &&  configurazione.getCodPolo().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " cd_polo='" + configurazione.getCodPolo() +"'";
			}

			if (configurazione.getCodBibl() !=null &&  configurazione.getCodBibl().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " cd_biblioteca='" + configurazione.getCodBibl() +"'";
			}
			sql0=sql0  + " order by  cd_biblioteca, progr ";
			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			int numRighe=0;
			List<StrutturaCombo> righeOggetto=new ArrayList();
			Timestamp ts = DaoManager.now();
			while (rs.next()) {
				numRighe=numRighe+1;
			}
			rs.close();
			pstmt.close();

			if (numRighe>0)
			{
				//procedere con l'operazione di cancellazione record poi di inserimento
				// cancello
				//String sqlDEL1="delete from tba_parametri_buono_ordine ";
				// cancellazione logica

				String sqlDEL1="update tba_parametri_buono_ordine set " ;
				//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
				sqlDEL1=sqlDEL1 + " ts_var = '" +  ts  + "'" ;  // ex data_agg
				sqlDEL1=sqlDEL1 + ", ute_var = '" +  configurazione.getUtente()  + "'" ;  // ute_var
				sqlDEL1=sqlDEL1 + ", fl_canc = 'S'" ;  // fl_canc
				sqlDEL1=this.struttura(sqlDEL1);
				sqlDEL1=sqlDEL1 + " fl_canc<>'S'";

				if (configurazione.getCodPolo() !=null &&  configurazione.getCodPolo().length()!=0)
				{
					sqlDEL1=this.struttura(sqlDEL1);
					sqlDEL1=sqlDEL1   + " cd_polo='" + configurazione.getCodPolo() +"'";
				}

				if (configurazione.getCodBibl() !=null &&  configurazione.getCodBibl().length()!=0)
				{
					sqlDEL1=this.struttura(sqlDEL1);
					sqlDEL1=sqlDEL1 + " cd_biblioteca='" + configurazione.getCodBibl() +"'";
				}
				if (configurazione.getDataUpd()!=null )
				{
					sqlDEL1=this.struttura(sqlDEL1);
					sqlDEL1=sqlDEL1 + " ts_var='" + configurazione.getDataUpd() + "'" ;
				}

				pstmtDEL = connection.prepareStatement(sqlDEL1);

				int intRetCANC = 0;
				intRetCANC = pstmtDEL.executeUpdate();
				if (intRetCANC >0)
				{
					valRitornoCANC=true;
				}
				else
				{
					throw new ValidationException("operazioneInConcorrenza",
							ValidationExceptionCodici.operazioneInConcorrenza);

				}

				pstmtDEL.close();
				connection.close();
			}
			Integer numFoot=0;
			Integer numInt=0;

				numFoot=configurazione.getListaDatiFineStampa().length;
				numInt=configurazione.getListaDatiIntestazione().length;

				Integer dimRec=0;


 				if (numInt< numFoot)
				{
					dimRec=numFoot;
				}
				else
				{
					dimRec=numInt;
				}
				connection = getConnection();

				for (int t=0; t<dimRec; t++)
				{
					int prg=t+1;

					//
					// cancellazione fisica	di quelli precedentemente cancellati logicamente


					String sqlDEL2="delete from tba_parametri_buono_ordine ";

					sqlDEL2=this.struttura(sqlDEL2);
					sqlDEL2=sqlDEL2 + " fl_canc = 'S'" ;  // fl_canc

					if (configurazione.getCodPolo() !=null &&  configurazione.getCodPolo().length()!=0)
					{
						sqlDEL2=this.struttura(sqlDEL2);
						sqlDEL2=sqlDEL2   + " cd_polo='" + configurazione.getCodPolo() +"'";
					}

					if (configurazione.getCodBibl() !=null &&  configurazione.getCodBibl().length()!=0)
					{
						sqlDEL2=this.struttura(sqlDEL2);
						sqlDEL2=sqlDEL2 + " cd_biblioteca='" + configurazione.getCodBibl() +"'";
					}

					if (configurazione.getCodBibl() !=null &&  configurazione.getCodBibl().length()!=0)
					{
						sqlDEL2=this.struttura(sqlDEL2);
						sqlDEL2=sqlDEL2 + " progr=" + prg;
					}

					pstmtUDP2 = connection.prepareStatement(sqlDEL2);
					int intRetCANC2 = 0;
					intRetCANC2 = pstmtUDP2.executeUpdate();
					pstmtUDP2.close();



					String sqlSub2="insert into tba_parametri_buono_ordine values ( " ;
					sqlSub2 = sqlSub2+  "'" + configurazione.getCodPolo().trim() + "'" ;  // cod_polo
					sqlSub2 = sqlSub2+  ",'" + configurazione.getCodBibl() + "'" ;  // cd_bib
					sqlSub2 = sqlSub2+  "," +  prg  ;  // progr
					if (configurazione.isNumAutomatica()) // progressivo automatico
					{
						sqlSub2 = sqlSub2+  ",'A'" ;  // codice_buono
					}
					else
					{
						sqlSub2 = sqlSub2+  ",'M'" ;  // codice_buono
					}
					if (t<numInt)
					{
						sqlSub2 = sqlSub2+ ",'" + configurazione.getListaDatiIntestazione()[t].getCodice2().replace("'","''")  +"'";  // descr_test


					}
					else
					{
						sqlSub2 = sqlSub2+ ",''";  // descr_test
					}
					if (t<numFoot)
					{
						sqlSub2 = sqlSub2+ ",'" + configurazione.getListaDatiFineStampa()[t].getCodice2().replace("'","''")  +"'";  // descr_foot


					}
					else
					{
						sqlSub2 = sqlSub2+ ",''";  // descr_foot
					}

					sqlSub2=sqlSub2 + ",'" + configurazione.getUtente() + "'" ;   // ute_ins
					sqlSub2=sqlSub2 + ",'" + ts + "'" ;   // ts_ins
					sqlSub2=sqlSub2 + ",'" + configurazione.getUtente() + "'" ;   // ute_var
					sqlSub2=sqlSub2 + ",'" + ts + "'";   // ts_var
					sqlSub2=sqlSub2 + ",'N'";   // fl_canc
					// gestione nuovi campi

					//ciclo per la stringa composta
					String formulaI="";
					formulaI=configurazione.preparaStringa( configurazione.getTestoIntroduttivo(), configurazione.getTestoIntroduttivoEng(), "I");
					String oggetto="";
					oggetto=configurazione.preparaStringa( configurazione.getTestoOggetto(), configurazione.getTestoOggettoEng(), "O");
					//configurazione.leggiStringa(formulaI, "I");
					//configurazione.leggiStringa(oggetto, "O");

					if (oggetto!=null &&  oggetto.trim().length()!=0)
					{
						sqlSub2 = sqlSub2+ ",'" + oggetto.replace("'","''") +"'";  ///descr_oggetto
					}
					else
					{
						sqlSub2 = sqlSub2+ ",''";  // descr_oggetto
					}

					if (formulaI!=null &&  formulaI.trim().length()!=0)
					{
						sqlSub2 = sqlSub2+ ",'" + formulaI.replace("'","''") +"'";  ///descr_formulaIntr
					}
					else
					{
						sqlSub2 = sqlSub2+ ",''";  // descr_formulaIntr
					}
					if (configurazione.leggiAree("T"))
					{
						sqlSub2 = sqlSub2+  ",'S'" ;  // area_titolo
					}
					else
					{
						sqlSub2 = sqlSub2+  ",'N'" ;  // area_titolo
					}
					if (configurazione.leggiAree("E"))
					{
						sqlSub2 = sqlSub2+  ",'S'" ;  // area_ediz
					}
					else
					{
						sqlSub2 = sqlSub2+  ",'N'" ;  // area_ediz
					}
					if (configurazione.leggiAree("N"))
					{
						sqlSub2 = sqlSub2+  ",'S'" ;  // area_num
					}
					else
					{
						sqlSub2 = sqlSub2+  ",'N'" ;  // area_num
					}

					if (configurazione.leggiAree("P"))
					{
						sqlSub2 = sqlSub2+  ",'S'" ;  // area_pub
					}
					else
					{
						sqlSub2 = sqlSub2+  ",'N'" ;  // area_pub
					}
					if (configurazione.isPresenzaLogoImg() && configurazione.getNomeLogoImg()!=null && configurazione.getNomeLogoImg().trim().length()>0)
					{
						sqlSub2 = sqlSub2+  ",true ";  // logo
						sqlSub2 = sqlSub2+  ",'" +configurazione.getNomeLogoImg().trim().replace("\\","\\\\")+ "'";  // logo_img
					}
					else
					{
						sqlSub2 = sqlSub2+  ",false" ;  // logo
						sqlSub2 = sqlSub2+  ",null" ;  // logo_img
					}
					if (configurazione.isPresenzaPrezzo())
					{
						sqlSub2 = sqlSub2+  ",true ";  // prezzo
					}
					else
					{
						sqlSub2 = sqlSub2+  ",false" ;  // prezzo
					}
					if (configurazione.isEtichettaProtocollo())
					{
						sqlSub2 = sqlSub2+  ",true ";  // nProt
					}
					else
					{
						sqlSub2 = sqlSub2+  ",false" ;  // nProt
					}
					if (configurazione.isEtichettaDataProtocollo())
					{
						sqlSub2 = sqlSub2+  ",true ";  // dataProt
					}
					else
					{
						sqlSub2 = sqlSub2+  ",false" ;  // dataProt
					}
					if (configurazione.getTipoRinnovo()!=null &&  configurazione.getTipoRinnovo().trim().length()==1)
					{

						if (configurazione.getTipoRinnovo().equals("O") || configurazione.getTipoRinnovo().equals("P") || configurazione.getTipoRinnovo().equals("N"))
						{
							sqlSub2 = sqlSub2+  ",'" +configurazione.getTipoRinnovo()+ "'";  // rinnovo
						}
						else
						{
							sqlSub2 = sqlSub2+  ",null" ;  // rinnovo

						}
					}
					else
					{
						sqlSub2 = sqlSub2+  ",null" ;  // rinnovo
					}

					if (configurazione.isPresenzaFirmaImg() && configurazione.getNomeFirmaImg()!=null && configurazione.getNomeFirmaImg().trim().length()>0)
					{
						sqlSub2 = sqlSub2+  ",true ";  // firmaDigit
						sqlSub2 = sqlSub2+  ",'" +configurazione.getNomeFirmaImg().trim().replace("\\","\\\\")+ "'";  // firmaDigit_img
					}
					else
					{
						sqlSub2 = sqlSub2+  ",false" ;  // firmaDigit
						sqlSub2 = sqlSub2+  ",null" ;  // firmaDigit_img
					}
					if (configurazione.isIndicaRistampa())
					{
						sqlSub2 = sqlSub2+  ",true";  // ristampa
					}
					else
					{
						sqlSub2 = sqlSub2+  ",false" ;  // ristampa
					}

					//almaviva5_20121116 evolutive google
					List<FormulaIntroOrdineRVO> formulaIntroOrdineR = configurazione.getFormulaIntroOrdineR();
					if (ValidazioneDati.isFilled(formulaIntroOrdineR)) {
						sqlSub2 += ",'" + StringEscapeUtils.escapeSql(ClonePool.marshal((Serializable) formulaIntroOrdineR)) + "'";
					} else
						sqlSub2 += ",null";

					sqlSub2 = sqlSub2+ ")" ;

					pstmtINS = connection.prepareStatement(sqlSub2);
					log.debug("Debug: modifica  configurazione BO");
					log.debug("Debug: " + sqlSub2);

					int intRetINSLOOP = 0;
					intRetINSLOOP = pstmtINS.executeUpdate();
					pstmtINS.close();
					if (intRetINSLOOP!=1){
						valRitornoINSLOOP=true;
					}
				}
			connection.close();

			valRitorno=true;
			if (valRitornoINSLOOP)
			 {
				 valRitorno=false;
			 }

			if (numRighe>0)
			{
			 if (!valRitornoCANC)
			 {
				 valRitorno=false;
			 }
			}
		}
			catch (ValidationException e) {

	      	  throw e;

		} catch (Exception e) {

			log.error("", e);
		} finally {
			close(connection);
		}
	    return valRitorno;
	}

	public ConfigurazioneBOVO loadConfigurazione(ConfigurazioneBOVO configurazione) throws DataException, ApplicationException , ValidationException
	{
		//Validazione.ValidaBuoniOrdineVO (buonoOrd);
		boolean valRitorno=false;
    	int motivo=0;
		ConfigurazioneBOVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
				connection = getConnection();
				String sql0="select parBO.*, parBO.ts_var as dataUpd, bibliot.nom_biblioteca ";
				sql0=sql0  + " from  tba_parametri_buono_ordine parBO ";
				sql0=sql0  + " join tbf_biblioteca bibliot on bibliot.cd_bib=parBO.cd_biblioteca and bibliot.cd_polo=parBO.cd_polo and bibliot.fl_canc<>'S' ";
				sql0=this.struttura(sql0);
				sql0=sql0 + " parBO.fl_canc<>'S'";

				if (configurazione.getCodPolo() !=null &&  configurazione.getCodPolo().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0 + " parBO.cd_polo='" + configurazione.getCodPolo() +"'";
				}

				if (configurazione.getCodBibl() !=null &&  configurazione.getCodBibl().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0 + " parBO.cd_biblioteca='" + configurazione.getCodBibl() +"'";
				}
				sql0=sql0  + " order by  parBO.cd_biblioteca, parBO.progr ";

				pstmt = connection.prepareStatement(sql0);
				rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
				// numero di righe del resultset
				int numRighe=0;
				List<StrutturaTerna> righeRisultato=new ArrayList();
				rec = new ConfigurazioneBOVO();

				//Timestamp ts = DaoManager.now();
				while (rs.next()) {
					numRighe++;
					if (numRighe == 1) { // solo la prima volta
						rec.setCodPolo(rs.getString("cd_polo"));
						rec.setCodBibl(rs.getString("cd_biblioteca"));
						rec.setDenoBibl(rs.getString("nom_biblioteca"));
						rec.setDataUpd(rs.getTimestamp("dataUpd"));
						if (rs.getString("codice_buono").equals("A")) // progressivo automatico
						{
							rec.setNumAutomatica(true);
						}
						if (rs.getString("codice_buono").equals("M")) // progressivo manuale
						{
							rec.setNumAutomatica(false);
						}
						// campi nuovi
						if (rs.getString("descr_formulaIntr")!=null)
						{
							rec.leggiStringa( rs.getString("descr_formulaIntr"), "I");
						}
						if (rs.getString("descr_oggetto")!=null)
						{
							rec.leggiStringa( rs.getString("descr_oggetto") , "O");
						}
						rec.caricaAree(rs.getString("area_titolo"), rs.getString("area_ediz"),rs.getString("area_num"),rs.getString("area_pub"));
						if (rs.getBoolean("logo") && rs.getString("logo_img")!=null && rs.getString("logo_img").trim().length()>0)
						{
							rec.setPresenzaLogoImg(true);
							rec.setNomeLogoImg(rs.getString("logo_img").trim().replace("\\\\","\\")); //replace("\\\\","\\")
						}
						else
						{
							rec.setPresenzaLogoImg(false);
							rec.setNomeLogoImg(null);
						}
						if (rs.getBoolean("firmaDigit") && rs.getString("firmaDigit_img")!=null && rs.getString("firmaDigit_img").trim().length()>0)
						{
							rec.setPresenzaFirmaImg(true);
							rec.setNomeFirmaImg(rs.getString("firmaDigit_img").trim(). replace("\\\\","\\")); //replace("\\\\","\\")
						}
						else
						{
							rec.setPresenzaFirmaImg(false);
							rec.setNomeFirmaImg(null);
						}
						if (rs.getBoolean("prezzo"))
						{
							rec.setPresenzaPrezzo(true);
						}
						else
						{
							rec.setPresenzaPrezzo(false);
						}
						if (rs.getBoolean("nProt"))
						{
							rec.setEtichettaProtocollo(true);
						}
						else
						{
							rec.setEtichettaProtocollo(false);
						}
						if (rs.getBoolean("dataProt"))
						{
							rec.setEtichettaDataProtocollo(true);
						}
						else
						{
							rec.setEtichettaDataProtocollo(false);
						}
						if (rs.getString("rinnovo")!=null && rs.getString("rinnovo").trim().length()==1)
						{
							if (rs.getString("rinnovo").equals("O") || rs.getString("rinnovo").equals("P") || rs.getString("rinnovo").equals("N"))
							{
								rec.setTipoRinnovo(rs.getString("rinnovo").trim());
							}
							else
							{
								rec.setTipoRinnovo(null);

							}
						}
						else
						{
							rec.setTipoRinnovo(null);
						}

						if (rs.getBoolean("ristampa"))
						{
							rec.setIndicaRistampa(true);
						}
						else
						{
							rec.setIndicaRistampa(false);
						}

						//almaviva5_20121116 evolutive google
						rec.setFormulaIntroOrdineR((List<FormulaIntroOrdineRVO>) ClonePool.unmarshal(rs.getString("xml_formulaintr")));
					}
					StrutturaTerna rec2 = new StrutturaTerna("","","");

					rec2.setCodice1(rs.getString("progr"));
					rec2.setCodice2(rs.getString("descr_test"));
					rec2.setCodice3(rs.getString("descr_foot"));
					righeRisultato.add(rec2);
				}
				// caricamento int e foot nell VO
				StrutturaTerna [] righeInt=new StrutturaTerna [0];
				StrutturaTerna [] righeFoot=new StrutturaTerna [0];

				if (numRighe > 0) {
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
							int d=0;
							for (int j=0; j<numRighe; j++)
							{
								if (righeFoot[j]!=null)
								{
									rec.getListaDatiFineStampa()[d]= righeFoot[j];
									d=d+1;
								}
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
							int b=0;
							for (int z=0; z<numRighe; z++)
							{
								if (righeInt[z]!=null)
								{
									rec.getListaDatiIntestazione()[b]= righeInt[z];
									b=b+1;
								}
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
				rs.close();
				pstmt.close();
				connection.close();

	}catch (ValidationException e) {

      	  throw e;

	} catch (Exception e) {

		log.error("", e);
	} finally {
		close(connection);
	}
    return rec;
}

	public List<DocumentoVO> getRicercaListaDocumenti(ListaSuppDocumentoVO ricercaDocumenti) throws ResourceNotFoundException, ApplicationException, ValidationException
	{
		Validazione.ValidaListaSuppDocumentoVO(ricercaDocumenti);
		String ticket="";
		ticket=ricercaDocumenti.getTicket();

		List<DocumentoVO> listaDocumenti = new ArrayList<DocumentoVO>();

		int ret = 0;
        // execute the search here
		DocumentoVO rec = null;
    		Connection connection = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				// contiene i criteri di ricerca
				connection = getConnection();
				String sql="select docLet.*, docLet.cd_bib as codiceBibl, ute.cognome,ute.nome,ute.cd_bib, ute.cod_utente,ute.id_utenti, TO_CHAR(docLet.ts_var,'dd/MM/yyyy') as ts_var_str , TO_CHAR(docLet.ts_ins,'dd/MM/yyyy') as ts_ins_str , codi.ds_tabella ";
				sql +=" from  tbl_documenti_lettori docLet ";
				sql +=" join trl_utenti_biblioteca uteBibl on uteBibl.id_utenti=docLet.id_utenti and  uteBibl.cd_biblioteca=docLet.cd_bib and  uteBibl.cd_polo=docLet.cd_polo and uteBibl.fl_canc<>'S' ";
				sql +=" join tbl_utenti ute on ute.id_utenti=uteBibl.id_utenti  and ute.fl_canc<>'S' ";
				sql +=" join tb_codici codi on codi.tp_tabella='ASTS' and codi.cd_tabella=docLet.stato_sugg" ;
				sql +=" where docLet.tipo_doc_lett='S'";

				sql=this.struttura(sql);
				sql +=" docLet.fl_canc<>'S'";

				if (ricercaDocumenti.getCodPolo() !=null &&  ricercaDocumenti.getCodPolo().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" docLet.cd_polo='" + ricercaDocumenti.getCodPolo() +"'";
				}

				if (ricercaDocumenti.getCodBibl() !=null &&  ricercaDocumenti.getCodBibl().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" docLet.cd_bib='" + ricercaDocumenti.getCodBibl() +"'";
				}
				String codDocumento = ricercaDocumenti.getCodDocumento();
				boolean withFiltroIdDoc = ValidazioneDati.isFilled(codDocumento);
				if (withFiltroIdDoc)
				{
					sql=this.struttura(sql);
					sql +=" docLet.cod_doc_lett=" + codDocumento.trim() ;
				}
				if (ricercaDocumenti.getTitolo()!=null && ricercaDocumenti.getTitolo().getCodice()!=null && ricercaDocumenti.getTitolo().getCodice().length()!=0 )
				{
					sql=this.struttura(sql);
					sql +=" docLet.bid='" + ricercaDocumenti.getTitolo().getCodice() +"'" ;
				}

				if (ricercaDocumenti.getIdDocList()!=null && ricercaDocumenti.getIdDocList().size()!=0 )
				{
					String listID="(";
					for (int index3 = 0; index3 < ricercaDocumenti.getIdDocList().size(); index3++) {
						if (ricercaDocumenti.getIdDocList().get(index3)>0)
						{
							if (index3>0)
							{
								listID=listID+",";
							}
							listID=listID+ ricercaDocumenti.getIdDocList().get(index3);
						}
					}
					listID=listID+")";
					sql=this.struttura(sql);
					sql +=" docLet.id_documenti_lettore in " + listID;
				}


				if (!withFiltroIdDoc && ricercaDocumenti.getStatoSuggerimentoDocumento()!=null && ricercaDocumenti.getStatoSuggerimentoDocumento().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" docLet.stato_sugg='" + ricercaDocumenti.getStatoSuggerimentoDocumento().trim()+"'";
				}
				if (ricercaDocumenti.getUtente()!=null && ricercaDocumenti.getUtente().getCodice1()!=null && ricercaDocumenti.getUtente().getCodice1().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" ute.cd_bib='" + ricercaDocumenti.getUtente().getCodice1() +"'";
				}
				if (ricercaDocumenti.getUtente()!=null && ricercaDocumenti.getUtente().getCodice2()!=null && ricercaDocumenti.getUtente().getCodice2().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" ute.cod_utente='" + ricercaDocumenti.getUtente().getCodice2().trim() +"'" ;
				}
				if (ricercaDocumenti.getDataSuggerimentoDocDa()!=null && ricercaDocumenti.getDataSuggerimentoDocDa().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" CAST(docLet.ts_ins AS date) >= TO_DATE('" +  ricercaDocumenti.getDataSuggerimentoDocDa() + "','dd/MM/yyyy')";
				}

				if (ricercaDocumenti.getDataSuggerimentoDocA()!=null && ricercaDocumenti.getDataSuggerimentoDocA().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" CAST(docLet.ts_ins AS date) <= TO_DATE ('" +  ricercaDocumenti.getDataSuggerimentoDocA() + "','dd/MM/yyyy')";
				}

				if (ricercaDocumenti.getTitolo()!=null && ricercaDocumenti.getTitolo().getDescrizione()!=null && ricercaDocumenti.getTitolo().getDescrizione().length()!=0 )
				{
					sql=this.struttura(sql);
							//** tidx_vector @@ 'aaa'::tsquery; .trim().toUpperCase()
							//fare do while
							String appo=ricercaDocumenti.getTitolo().getDescrizione().trim();
							// eliminazione degli spazi di troppo
							while (appo.indexOf("  ")>0) {
								appo = appo.replaceAll("  "," ");
							}

							String[] nomeFornComposto=appo.split(" ");
							String paroleForn="";
							for (int i=0;  i <  nomeFornComposto.length; i++)
							{
								if (i==0)
								{
									paroleForn=nomeFornComposto[0] ;
								}
								if (i>0)
								{
									paroleForn=paroleForn + " & " + nomeFornComposto[i] ;
								}

							}
							DaoManager hibDao = new DaoManager();
							 if (hibDao.getVersion().compareTo("8.3") < 0)
							 {
								 sql +=" docLet.tidx_vector @@ to_tsquery('default', '" + paroleForn +"')";
							 }
							 else
							 {
								 sql +=" docLet.tidx_vector @@ to_tsquery('" + paroleForn +"')";
							 }
				}


				// ordinamento passato
				if (ricercaDocumenti.getOrdinamento()==null || (ricercaDocumenti.getOrdinamento()!=null && ricercaDocumenti.getOrdinamento().equals("")))
				{
					sql +=" order by docLet.cd_bib,docLet.cod_doc_lett  ";
				}
				else if (ricercaDocumenti.getOrdinamento().equals("1"))
				{
					sql +=" order by docLet.cd_bib,docLet.cod_doc_lett  ";
				}
				else if (ricercaDocumenti.getOrdinamento().equals("2"))
				{
					sql +=" order by docLet.cd_bib, docLet.stato_sugg  ";
				}
				else if (ricercaDocumenti.getOrdinamento().equals("3"))
				{
					sql +=" order by docLet.cd_bib, ts_ins desc ";
				}

				pstmt = connection.prepareStatement(sql);
				//log.debug("Debug: lettura sugg lett");
				//log.debug("Debug: " + sql);

				rs = pstmt.executeQuery();
				int numRighe=0;
				while (rs.next()) {
					numRighe=numRighe+1;
					rec = new DocumentoVO();
					rec.setProgressivo(numRighe);
					rec.setCodPolo(rs.getString("cd_polo"));
					//rec.setCodBibl(rs.getString("cd_bib"));
					rec.setCodBibl(rs.getString("codiceBibl")); // 27.07.10
					rec.setIDDoc(rs.getInt("id_documenti_lettore"));
					//rec.setUtenteCod(ricercaDocumenti.getUtente());

					rec.setCodDocumento(String.valueOf(rs.getInt("cod_doc_lett")));
					rec.setDataAgg(rs.getString("ts_var_str"));
					rec.setDataIns(rs.getString("ts_ins_str"));
					rec.setStatoSuggerimentoDocumento(rs.getString("stato_sugg"));
					rec.setDenoStatoSuggerimento(rs.getString("ds_tabella"));

					rec.setUtente(new StrutturaTerna("","",""));
					rec.getUtente().setCodice1(String.valueOf(rs.getInt("id_utenti")));
					rec.getUtente().setCodice2(rs.getString("cod_utente"));
					rec.getUtente().setCodice3(rs.getString("cognome").trim() + "-" + rs.getString("nome").trim() );

					rec.setTitolo(new StrutturaCombo("",""));
					rec.getTitolo().setCodice(rs.getString("bid"));
					rec.getTitolo().setDescrizione(rs.getString("titolo"));

					rec.setPrimoAutore(rs.getString("autore"));
					rec.setEditore(rs.getString("editore"));
					rec.setLuogoEdizione(rs.getString("luogo_edizione"));

					rec.setPaese(new StrutturaCombo("",""));
					rec.getPaese().setCodice(rs.getString("paese"));

					rec.setLingua(new StrutturaCombo("",""));
					rec.getLingua().setCodice(rs.getString("lingua"));

					rec.setLuogoEdizione(rs.getString("luogo_edizione"));
					rec.setAnnoEdizione(rs.getString("anno_edizione"));

					rec.setNoteDocumento(rs.getString("note"));
					rec.setMsgPerLettore(rs.getString("msg_lettore"));

					rec.setTipoVariazione("");

					String isbd="";
					String bid="";
					String naturaBid="";
					isbd=rs.getString("titolo");

					if (rs.getString("bid")!=null && rs.getString("bid").trim().length()!=0)
					{
						bid=rs.getString("bid");

						try {
							TitoloACQVO recTit=null;
							recTit = this.getTitoloOrdineTer(rs.getString("bid").trim());
							if (recTit!=null && recTit.getIsbd()!=null) {
								bid=rs.getString("bid").trim();
								isbd=recTit.getIsbd();
								naturaBid=recTit.getNatura();
							}
							if (recTit==null) {
								isbd=isbd + " (ERRORE: titolo-bid)";
								bid=rs.getString("bid");
								naturaBid="";
							}
						} catch (Exception e) {
								isbd=isbd + " (ERRORE: titolo-bid)";
								bid=rs.getString("bid");
								naturaBid="";
						}
					}
					if (bid.equals("") && isbd.equals("") && rs.getString("titolo")!=null && rs.getString("titolo").trim().length()>0 )
					{
						isbd=rs.getString("titolo");
					}
					rec.setTitolo(new StrutturaCombo(bid,isbd));
					if (naturaBid!=null && naturaBid.trim().length()>0 )
					{
						rec.setNaturaBid(naturaBid);
					}
					else
					{
						rec.setNaturaBid(rs.getString("natura"));
					}
					listaDocumenti.add(rec);
				} // End while

				rs.close();
				pstmt.close();
				connection.close();

			}	catch (ValidationException e) {

		    	  throw e;



		} catch (Exception e) {

			// l'errore capita in questo punto
			log.error("", e);
		} finally {
			close(connection);
		}

		Validazione.ValidaRicercaDocumenti (listaDocumenti);
        return listaDocumenti;
	}

	public boolean  modificaDocumento(DocumentoVO documento) throws DataException, ApplicationException , ValidationException
	{
		Validazione.ValidaDocumentoVO(documento);
		boolean valRitorno=false;
    	int motivo=0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean valRitornoUPD=false;
		boolean esistenzaDocumento=false;

		try {
			connection = getConnection();


			String sql0="select * ";
			sql0=sql0 + " from  tbl_documenti_lettori  ";
			sql0=sql0 + " where tipo_doc_lett='S'";


			sql0=this.struttura(sql0);
			sql0=sql0 + " fl_canc<>'S'";

			if (documento.getCodPolo() !=null &&  documento.getCodPolo().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " cd_polo='" + documento.getCodPolo() +"'";
			}

			if (documento.getCodBibl() !=null &&  documento.getCodBibl().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " cd_bib='" + documento.getCodBibl() +"'";
			}
			if (documento.getCodDocumento()!=null && documento.getCodDocumento().length()!=0 )
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " cod_doc_lett=" + documento.getCodDocumento().trim() ;
			}

			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			int numRighe=0;
			List<StrutturaCombo> righeOggetto=new ArrayList();
			Timestamp ts = DaoManager.now();
			while (rs.next()) {
				numRighe=numRighe+1;
			}
			rs.close();
			pstmt.close();
			if (numRighe >= 1)
			{
				if (numRighe == 1)
				{
					esistenzaDocumento=true;
				}
			}
			if (!esistenzaDocumento )
			{
				throw new ValidationException("cambierroreModificaRecordNonUnivoco",
						ValidationExceptionCodici.cambierroreModificaRecordNonUnivoco);
			}
			if (esistenzaDocumento)
				{

					// test di esistenza titolo
					String isbd="";
					String natura="";
					if (documento.getTitolo()!=null && documento.getTitolo().getCodice()!=null && documento.getTitolo().getCodice().length()!=0)
					{
						try {
							TitoloACQVO recTit=null;
							recTit = this.getTitoloOrdineTer(documento.getTitolo().getCodice());
							if (recTit!=null && recTit.getIsbd()!=null) {
								isbd=recTit.getIsbd();
								natura=recTit.getNatura();
							}
							if (recTit==null) {
								throw new ValidationException("ordineIncongruenzaTitoloInesistente",
										ValidationExceptionCodici.ordineIncongruenzaTitoloInesistente);
							}

						} catch (Exception e) {
							throw new ValidationException("ordineIncongruenzaTitoloInesistente",
									ValidationExceptionCodici.ordineIncongruenzaTitoloInesistente);
						}

					}

					String sqlUDP="update tbl_documenti_lettori set " ;
					sqlUDP= sqlUDP + " ts_var = '" +  ts  + "'" ;  // ts_var
					sqlUDP= sqlUDP + ", ute_var = '" +  documento.getUtenteCod()  + "'" ;  // ute_var
					if (documento.getStatoSuggerimentoDocumento()!=null && documento.getStatoSuggerimentoDocumento().trim().length()!=0)
						sqlUDP += ", stato_sugg= '" +  documento.getStatoSuggerimentoDocumento().trim()  + "'" ;

					sqlUDP += ", titolo= " +  trimAndQuote(documento.getTitolo().getDescrizione(), false);
					sqlUDP += ", luogo_edizione= " + trimAndQuote( documento.getLuogoEdizione());
					sqlUDP += ", editore= " + trimAndQuote( documento.getEditore()) ;
					String annoEdizione = documento.getAnnoEdizione();
					if (ValidazioneDati.isFilled(annoEdizione))
						sqlUDP += ", anno_edizione= " + annoEdizione;

					//almaviva5_20110922 #4037 forzato aggiornamento di tutti i campi
					sqlUDP += ", autore= " + trimAndQuote( documento.getPrimoAutore());
					sqlUDP += ", paese= " + trimAndQuote( documento.getPaese().getCodice());
					sqlUDP += ", lingua= " + trimAndQuote( documento.getLingua().getCodice());
					sqlUDP += ", bid= " + trimAndQuote( documento.getTitolo().getCodice());

					sqlUDP += ", note= " +  trimAndQuote(documento.getNoteDocumento());
					sqlUDP += ", msg_lettore= " + trimAndQuote(documento.getMsgPerLettore());

					// CONDIZIONI
					sqlUDP=sqlUDP + " where tipo_doc_lett='S'";
					sqlUDP=sqlUDP + " and fl_canc<>'S' ";

					if (documento.getCodPolo() !=null &&  documento.getCodPolo().length()!=0) {
						sqlUDP=this.struttura(sqlUDP);
						sqlUDP=sqlUDP + " cd_polo='" + documento.getCodPolo() +"'";
					}

					if (documento.getCodBibl() !=null &&  documento.getCodBibl().length()!=0)
					{
						sqlUDP=this.struttura(sqlUDP);
						sqlUDP=sqlUDP  + " cd_bib='" + documento.getCodBibl() +"'";
					}
					if (documento.getCodDocumento()!=null && documento.getCodDocumento().length()!=0 )
					{
						sqlUDP=this.struttura(sqlUDP);
						sqlUDP=sqlUDP  + " cod_doc_lett=" + documento.getCodDocumento().trim() ;
					}
					pstmt = connection.prepareStatement(sqlUDP);
					log.debug("Debug: modifica  sugg. lett.");
					log.debug("Debug: " + sqlUDP);
					int intRetUDP = 0;
					intRetUDP = pstmt.executeUpdate();
					pstmt.close();
					// fine estrazione codice
					if (intRetUDP==1){
						valRitornoUPD=true;
					}
				}
			connection.close();
			// impostazione del codice di ritorno finale
			if (valRitornoUPD)
			{
				valRitorno=true;
			}
		}catch (ValidationException e) {

	      	  throw e;


		} catch (Exception e) {

			log.error("", e);
		} finally {
			close(connection);
		}

        return valRitorno;
	}

	public List<GaraVO> getRicercaListaGare(ListaSuppGaraVO ricercaGare)	throws ResourceNotFoundException, ApplicationException, ValidationException
	{
		Validazione.ValidaListaSuppGaraVO(ricercaGare);
		String ticket="";
		ticket=ricercaGare.getTicket();

		List<GaraVO> listaGare = new ArrayList<GaraVO>();

		int ret = 0;
        // execute the search here
			GaraVO rec = null;
    		Connection connection = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			ResultSet rsSub= null;
			PreparedStatement pstmtSub = null;

			try {
				// contiene i criteri di ricerca
				connection = getConnection();
				String sql="select richOff.*,richOff.ts_var as dataUpd, lower(substr(tito.isbd,0,100)) as parzTito,tito.ky_cles1_t,tito.ky_cles2_t, TO_CHAR(richOff.data_rich_off,'dd/MM/yyyy') as data_rich_off_str  ";
				sql +=" from  tba_richieste_offerta richOff ";
				sql +=" join tb_titolo tito on tito.bid=richOff.bid and tito.fl_canc<>'S'";

				sql=this.struttura(sql);
				sql +=" richOff.fl_canc<>'S'";

				if (ricercaGare.getCodPolo() !=null &&  ricercaGare.getCodPolo().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" richOff.cd_polo='" + ricercaGare.getCodPolo() +"'";
				}

				if (ricercaGare.getCodBibl() !=null &&  ricercaGare.getCodBibl().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" richOff.cd_bib='" + ricercaGare.getCodBibl() +"'";
				}
				if (ricercaGare.getCodRicOfferta()!=null && ricercaGare.getCodRicOfferta().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" richOff.cod_rich_off=" + ricercaGare.getCodRicOfferta() ;
				}

				if (ricercaGare.getBid()!=null && ricercaGare.getBid().getCodice().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" richOff.bid='" + ricercaGare.getBid().getCodice() +"'";
				}

				if (ricercaGare.getStatoRicOfferta()!=null && ricercaGare.getStatoRicOfferta().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" richOff.stato_rich_off ='" + ricercaGare.getStatoRicOfferta() +"'";;
				}

				// ordinamento passato
				if (ricercaGare.getOrdinamento()==null || (ricercaGare.getOrdinamento()!=null &&  ricercaGare.getOrdinamento().equals("")))
				{
					sql +=" order by richOff.cd_bib, richOff.cod_rich_off  ";
				}

				else if (ricercaGare.getOrdinamento().equals("1"))
				{
					sql +=" order by ky_cles1_t,ky_cles2_t ";
				}

				else if (ricercaGare.getOrdinamento().equals("2"))
				{
					sql +=" order by richOff.data_rich_off desc";
				}
				else if (ricercaGare.getOrdinamento().equals("3"))
				{
					sql +="  order by richOff.cd_bib, richOff.data_rich_off ";
				}

				pstmt = connection.prepareStatement(sql);


				rs = pstmt.executeQuery();
				int numRighe=0;
				while (rs.next()) {
					numRighe=numRighe+1;
					rec = new GaraVO();
					rec.setProgressivo(numRighe);
					rec.setCodPolo(rs.getString("cd_polo"));
					rec.setCodBibl(rs.getString("cd_bib"));
					rec.setCodRicOfferta(String.valueOf(rs.getInt("cod_rich_off")));
					rec.setDataRicOfferta(rs.getString("data_rich_off_str"));
					//rec.setPrezzoIndGara(String.valueOf(rs.getFloat("prezzo_rich")));
					rec.setPrezzoIndGara(rs.getFloat("prezzo_rich"));
					rec.setNumCopieRicAcq(rs.getInt("num_copie"));
					rec.setNoteOrdine(rs.getString("note"));
					rec.setStatoRicOfferta(rs.getString("stato_rich_off"));
					rec.setDataUpd(rs.getTimestamp("dataUpd"));


					if (rec.getStatoRicOfferta().equals("1"))
					{
						rec.setDesStatoRicOfferta("Aperta");
					}
					else if (rec.getStatoRicOfferta().equals("2"))
					{
						rec.setDesStatoRicOfferta("Chiusa");
					}
					else if (rec.getStatoRicOfferta().equals("3"))
					{
						rec.setDesStatoRicOfferta("Annullata");
					}
					else if (rec.getStatoRicOfferta().equals("4"))
					{
						rec.setDesStatoRicOfferta("Ordinata");
					}

					//rec.setBid(new StrutturaCombo("",""));
					//rec.getBid().setCodice(rs.getString("bid"));
					String naturaBid="";
					String isbd="";
					String bid="";

					if (rs.getString("bid")!=null && rs.getString("bid").trim().length()!=0)
					{
						bid=rs.getString("bid");

						try {
							TitoloACQVO recTit=null;
							recTit = this.getTitoloOrdineTer(rs.getString("bid").trim());
							if (recTit!=null && recTit.getIsbd()!=null) {
								bid=rs.getString("bid").trim();
								isbd=recTit.getIsbd();
								naturaBid=recTit.getNatura();
							}
							if (recTit==null) {
								isbd="titolo non trovato";
								bid=rs.getString("bid");
								naturaBid="natura non trovata";
							}
						} catch (Exception e) {
								isbd="titolo non trovato";
								bid=rs.getString("bid");
								naturaBid="natura non trovata";
						}
					}
					rec.setBid(new StrutturaCombo(bid,isbd));
					rec.setNaturaBid(naturaBid);

					//if rs.getString("lingua")!=null ciclo per riempire l'arraylist

					// ricerca dei partecipanti  associati alla gara se esistenti
					String sqlSub="select partGara.* ,forn.nom_fornitore, TO_CHAR(partGara.data_invio,'dd/MM/yyyy') as data_invio_str ";  //,  prof.* ";
					sqlSub=sqlSub + " from  tra_fornitori_offerte partGara ";
					sqlSub=sqlSub + " join tbr_fornitori forn on partGara.cod_fornitore=forn.cod_fornitore and forn.fl_canc<>'S'";
					// se deve essere limitato ai soli fornitori di biblioteca
					sqlSub=sqlSub + " join tbr_fornitori_biblioteche fornBibl on fornBibl.cod_fornitore=forn.cod_fornitore  and fornBibl.cd_biblioteca='" + ricercaGare.getCodBibl()+"' and fornBibl.cd_polo='" + ricercaGare.getCodPolo()+"' and fornBibl.fl_canc<>'S'" ;
					// se non deve essere limitato ai soli fornitori di biblioteca
					//sqlSub=sqlSub + " left join tbr_fornitori_biblioteche fornBibl on fornBibl.cod_fornitore=forn.cod_fornitore  and fornBibl.cd_biblioteca='" + ricercaGare.getCodBibl()+"' and fornBibl.cd_polo='" + ricercaGare.getCodPolo()+"' and fornBibl.fl_canc<>'S'" ;

					sqlSub=this.struttura(sqlSub);
					sqlSub=sqlSub + " partGara.fl_canc<>'S'";

					if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)
					{
						sqlSub=this.struttura(sqlSub);
						sqlSub=sqlSub + " partGara.cd_polo='" + rec.getCodPolo() +"'";
					}

					if (rec.getCodBibl() !=null &&  rec.getCodBibl().length()!=0)
					{
						sqlSub=this.struttura(sqlSub);
						sqlSub=sqlSub + " partGara.cd_bib='" + rec.getCodBibl() +"'";
					}
					if (rec.getCodRicOfferta()!=null && rec.getCodRicOfferta().length()!=0)
					{
						sqlSub=this.struttura(sqlSub);
						sqlSub=sqlSub + " partGara.cod_rich_off=" + rec.getCodRicOfferta() ;
					}

					pstmtSub = connection.prepareStatement(sqlSub);
					rsSub = pstmtSub.executeQuery();
					List<PartecipantiGaraVO> listPartecip=new ArrayList();
					int numRiga=0;
					while (rsSub.next()) {
						numRiga=numRiga+1;
						PartecipantiGaraVO elePartecip=new PartecipantiGaraVO();
						elePartecip.setCodBibl(rs.getString("cd_bib"));
						elePartecip.setCodPolo(rs.getString("cd_polo"));
						elePartecip.setCodRicOfferta(rs.getString("cod_rich_off"));
						elePartecip.setCodtipoInvio(rsSub.getString("tipo_invio"));
						elePartecip.setDataInvioAlFornRicOfferta(rsSub.getString("data_invio_str"));
						elePartecip.setFornitore(new StrutturaCombo("",""));
						elePartecip.getFornitore().setCodice(String.valueOf(rsSub.getInt("cod_fornitore")));
						elePartecip.getFornitore().setDescrizione(rsSub.getString("nom_fornitore"));
						elePartecip.setMsgRispDaFornAGara(rsSub.getString("risp_da_risp"));
						elePartecip.setNoteAlFornitore(rsSub.getString("note"));
						elePartecip.setStatoPartecipante(rsSub.getString("stato_gara"));
						listPartecip.add(elePartecip);

					}
					rsSub.close();
					pstmtSub.close();
					rec.setDettaglioPartecipantiGara(listPartecip);
					listaGare.add(rec);
				} // End while

				rs.close();
				pstmt.close();
				connection.close();
			}	catch (ValidationException e) {

		    	  throw e;



		} catch (Exception e) {

			// l'errore capita in questo punto
			log.error("", e);
		} finally {
			close(connection);
		}

		Validazione.ValidaRicercaGare (listaGare);
        return listaGare;
	}

	public boolean  inserisciGara(GaraVO gara) throws DataException, ApplicationException , ValidationException
	{
		Validazione.ValidaGaraVO(gara);
		boolean valRitorno=false;
    	int motivo=0;
    	//FornitoreVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSet rsSub = null;
		ResultSet rsSubCod = null;

		ResultSet rsESISTEORD = null;
		PreparedStatement pstmtESISTEORD = null;
		boolean controlloCONGR=false;


		PreparedStatement pstmtInsFornBibl= null;
		boolean valRitornoInsFornBibl=false;


		//ResultSet rsInsTit = null;

		boolean valRitornoINS=false;
		boolean valRitornoINSLOOP=false;
		boolean esistenzaProfilo=false;

		try {
			// CONTROLLI PREVENTIVI
			connection = getConnection();

			if (gara.getDettaglioPartecipantiGara()!=null && gara.getDettaglioPartecipantiGara().size() >0 )
			{

				for (int i=0; i<gara.getDettaglioPartecipantiGara().size(); i++)
				{
					PartecipantiGaraVO 	oggettoDettVO=gara.getDettaglioPartecipantiGara().get(i);
					// controllo esistenza fornitore
					String sqlESISTEORD="select forn.*, fornBibl.cod_fornitore as codFornBibl  from tbr_fornitori forn " ;
					sqlESISTEORD=sqlESISTEORD + " left join tbr_fornitori_biblioteche fornBibl on fornBibl.cod_fornitore=forn.cod_fornitore "   ;
					sqlESISTEORD=sqlESISTEORD + " and fornBibl.fl_canc<>'S'";
					sqlESISTEORD=sqlESISTEORD + " and fornBibl.cd_polo='" + gara.getCodPolo() +"'";
					sqlESISTEORD=sqlESISTEORD + " and fornBibl.cd_biblioteca='" + gara.getCodBibl() +"'";
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " forn.fl_canc<>'S'";
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " forn.cod_fornitore=" + oggettoDettVO.getFornitore().getCodice();
					pstmtESISTEORD = connection.prepareStatement(sqlESISTEORD);
					rsESISTEORD = pstmtESISTEORD.executeQuery(); // va in errore se non può restituire un recordset
					int numRigheOrd=0;


					if (!rsESISTEORD.next()) {
						controlloCONGR=false;
						motivo=6;
						throw new ValidationException("ordineIncongruenzaFornitoreInesistente",
								ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);
					}
					else
					{

						controlloCONGR=true;
						// localizzazione del fornitore in caso di utilizzo nella creazione gara
						if (rsESISTEORD.getString("codFornBibl")==null ||  (rsESISTEORD.getString("codFornBibl")!=null && rsESISTEORD.getString("codFornBibl").trim().length()==0)  )
						{
							// esiste il fornitore in anagrafica e non esiste fra quelli di biblioteca
							String sqlFB="insert into tbr_fornitori_biblioteche values ( " ;
							Timestamp ts = DaoManager.now();
							sqlFB= sqlFB + "'" +  gara.getCodPolo() + "'" ;  // cd_bib
							sqlFB= sqlFB + ",'" +  gara.getCodBibl() + "'" ;  // cd_biblioteca
							sqlFB= sqlFB + "," +  oggettoDettVO.getFornitore().getCodice()  ;  // cod_fornitore
							sqlFB= sqlFB  + ",''";  // tipo_pagamento
							sqlFB= sqlFB  + ",''";  // cod_cliente
							sqlFB= sqlFB  + ",''";  // nom_contatto
							sqlFB= sqlFB  + ",''";  // tel_contatto
							sqlFB= sqlFB  + ",''";  // fax_contatto
							sqlFB= sqlFB  + ",'EUR'" ;  // valuta
							sqlFB= sqlFB  + ",'" +  gara.getCodPolo() + "'" ;  // cod_polo
							sqlFB= sqlFB  + ",' '" ;  // allinea
							sqlFB= sqlFB + ",'" + gara.getUtente() + "'" ;   // ute_ins
							sqlFB= sqlFB + ",'" + ts + "'" ;   // ts_ins
							sqlFB= sqlFB + ",'" + gara.getUtente() + "'" ;   // ute_var
							sqlFB= sqlFB + ",'" + ts + "'";   // ts_var
							sqlFB= sqlFB + ",'N'";   // fl_canc
							sqlFB= sqlFB  + ")" ;  // cod_polo_bib
							pstmtInsFornBibl = connection.prepareStatement(sqlFB);
							int intRetFB = 0;
							intRetFB = pstmtInsFornBibl.executeUpdate();
							pstmtInsFornBibl.close();
							// fine estrazione codice
							if (intRetFB==1){
								valRitornoInsFornBibl=true;
								//messaggio di localizzaziojne del fornitore
							} else {
								valRitornoInsFornBibl=false;
								throw new ValidationException("ordineIncongruenzaFornitoreInesistente",
										ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);
							}
						}
					}
					rsESISTEORD.close();
					pstmtESISTEORD.close();

				}
			}

			String sql0="select richOff.* ";
			sql0=sql0 + " from  tba_richieste_offerta richOff ";

			sql0=this.struttura(sql0);
			sql0=sql0 + " richOff.fl_canc<>'S'";

			if (gara.getCodPolo() !=null &&  gara.getCodPolo().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " richOff.cd_polo='" + gara.getCodPolo() +"'";
			}

			if (gara.getCodBibl() !=null &&  gara.getCodBibl().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " richOff.cd_bib='" + gara.getCodBibl() +"'";
			}

			if (gara.getBid()!=null && gara.getBid().getCodice()!=null && gara.getBid().getCodice().trim().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " richOff.bid='" + gara.getBid().getCodice() +"'";
			}
			if (String.valueOf(gara.getNumCopieRicAcq()).trim().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " richOff.num_copie=" + gara.getNumCopieRicAcq();
			}

			if (String.valueOf(gara.getPrezzoIndGara()).trim().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " richOff.prezzo_rich =" + gara.getPrezzoIndGara() ;;
			}

			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			Timestamp ts = DaoManager.now();
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;
				}
			if (numRighe>0)
			{
				esistenzaProfilo=true; // record forse già esistente quindi non inseribile
				throw new ValidationException("cambierroreInserimentoEsistenzaRecord",
						ValidationExceptionCodici.cambierroreInserimentoEsistenzaRecord);
			}
			pstmt.close();
			if (!esistenzaProfilo)
			{
				// INSERIMENTO
				String sqlSub="insert into tba_richieste_offerta values ( " ;

				sqlSub=sqlSub + "'" + gara.getCodPolo() + "'" ;  // cd_polo
				sqlSub=sqlSub + ",'" + gara.getCodBibl() + "'" ;  // cd_bib

				// INIZIO SUBQUERY cod_prac
				sqlSub=sqlSub + ", (SELECT CASE WHEN  (MAX(tba_richieste_offerta.cod_rich_off) is null) THEN 1  else MAX(tba_richieste_offerta.cod_rich_off)+1  END " ;
				sqlSub=sqlSub + " from tba_richieste_offerta   ";
				sqlSub=sqlSub + " where cd_bib='" + gara.getCodBibl() +"'";
				sqlSub=sqlSub + " and cd_polo='" + gara.getCodPolo() +"'";
				sqlSub=sqlSub + " )";
				// fine subquery
				sqlSub=sqlSub + ",(SELECT CURRENT_DATE ) "  ;  // data_rich_off
				sqlSub=sqlSub + "," +  gara.getPrezzoIndGara()  ;  // prezzo_rich
				sqlSub=sqlSub + "," +  gara.getNumCopieRicAcq()  ;  // num_copie
				sqlSub=sqlSub + ",'" +  gara.getNoteOrdine().replace("'","''") + "'"  ;  // note
				sqlSub=sqlSub + ",'" +  gara.getStatoRicOfferta() + "'"  ;	// stato_rich_off
				sqlSub=sqlSub + ",'" +  gara.getBid().getCodice() + "'"  ;	// bid
				sqlSub=sqlSub + ",'" + gara.getUtente() + "'" ;   // ute_ins
				sqlSub=sqlSub + ",'" + ts + "'" ;   // ts_ins
				sqlSub=sqlSub + ",'" + gara.getUtente() + "'" ;   // ute_var
				sqlSub=sqlSub + ",'" + ts + "'";   // ts_var
				sqlSub=sqlSub + ",'N'";   // fl_canc
				sqlSub=sqlSub + ")" ;
				pstmt = connection.prepareStatement(sqlSub);
				log.debug("Debug: inserimento gara");
				log.debug("Debug: " + sqlSub);

				int intRetINS = 0;
				intRetINS = pstmt.executeUpdate();
				pstmt.close();
				connection.close();
				connection = getConnection();
				if (intRetINS==1){
					valRitornoINS=true;
					// estrazione del codice ed attribuzione del codice alla gara visualizzata
					// fine estrazione codice
					String sqlCodice="select cod_rich_off from tba_richieste_offerta ";
					//sqlCodice=sqlCodice + " where ts_ins='" +ts+ "'";
					sqlCodice=sqlCodice + " where ute_ins='" + gara.getUtente()+ "'";
					sqlCodice=sqlCodice + " order by ts_ins desc limit 1";

					pstmt = connection.prepareStatement(sqlCodice);
					rsSubCod = pstmt.executeQuery();
					if (rsSubCod.next()) {
						gara.setCodRicOfferta(String.valueOf(rsSubCod.getInt("cod_rich_off")));
					}
					rsSubCod.close();
				}
				if (gara.getDettaglioPartecipantiGara()!=null && gara.getDettaglioPartecipantiGara().size() >0 )
				{
					valRitornoINSLOOP=false;
					for (int i=0; i<gara.getDettaglioPartecipantiGara().size(); i++)
					{
						PartecipantiGaraVO 	oggettoDettVO=gara.getDettaglioPartecipantiGara().get(i);
						String sqlSub2="insert into tra_fornitori_offerte values ( " ;
						sqlSub2 = sqlSub2+  "'" +  gara.getCodPolo() + "'"  ;  // cd_polo
						sqlSub2 = sqlSub2+  ",'" +  gara.getCodBibl() + "'"  ;  // cd_bib
						sqlSub2 = sqlSub2+  "," + oggettoDettVO.getFornitore().getCodice()  ;  // cod_fornitore
						sqlSub2 = sqlSub2+  ",'" +  gara.getCodRicOfferta()+ "'"  ;  // cod_rich_off
						sqlSub2 = sqlSub2+  ",'" +  oggettoDettVO.getNoteAlFornitore().replace("'","''") + "'"  ;  // note
						sqlSub2 = sqlSub2+  ",'" +  oggettoDettVO.getStatoPartecipante() + "'" ;  // stato_gara
						sqlSub2 = sqlSub2+  ",'" +  oggettoDettVO.getCodtipoInvio() + "'" ;  // tipo_invio
						sqlSub2 = sqlSub2+  ",TO_DATE('"+ oggettoDettVO.getDataInvioAlFornRicOfferta()+"','dd/MM/yyyy')" ;  // data_invio
						sqlSub2 = sqlSub2+  ",'" +  oggettoDettVO.getMsgRispDaFornAGara().replace("'","''") + "'" ;  // risp_da_risp
						sqlSub2 = sqlSub2+ ",'" + gara.getUtente() + "'" ;   // ute_ins
						sqlSub2 = sqlSub2+ ",'" + ts + "'" ;   // ts_ins
						sqlSub2 = sqlSub2+ ",'" + gara.getUtente() + "'" ;   // ute_var
						sqlSub2 = sqlSub2+ ",'" + ts + "'";   // ts_var
						sqlSub2 = sqlSub2+ ",'N'";   // fl_canc
						sqlSub2 = sqlSub2+ ")" ;
						pstmt = connection.prepareStatement(sqlSub2);
						int intRetINSLOOP = 0;
						intRetINSLOOP = pstmt.executeUpdate();
						pstmt.close();
						if (intRetINSLOOP!=1){
							valRitornoINSLOOP=true;
						}
				}
			}
			rs.close();
			// impostazione del codice di ritorno finale
			if (valRitornoINS)
			{
				valRitorno=true;
				if (!valRitornoINSLOOP && gara.getDettaglioPartecipantiGara()!=null && gara.getDettaglioPartecipantiGara().size()>0)
				{
						valRitorno=true;
				}
				if (valRitornoINSLOOP)
				{
					valRitorno=false;
				}
			}
			if (valRitorno)
			{
			}
			connection.close();
		}
		}catch (ValidationException e) {

	      	  throw e;

		} catch (Exception e) {

			log.error("", e);
		} finally {
			close(connection);
		}

        return valRitorno;
	}

	public boolean  modificaGara(GaraVO gara) throws DataException, ApplicationException , ValidationException
	{
		Validazione.ValidaGaraVO(gara);
		boolean valRitorno=false;
    	int motivo=0;
    	//FornitoreVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtUDP = null;
		PreparedStatement pstmtUDP2 = null;

		PreparedStatement pstmtDEL = null;
		PreparedStatement pstmtINS = null;
		PreparedStatement pstmtExistOrd = null;
		ResultSet rsExistOrd= null;
		ResultSet rs = null;
		ResultSet rsSub = null;
		//ResultSet rsInsTit = null;

		PreparedStatement pstmtInsFornBibl= null;
		boolean valRitornoInsFornBibl=false;


		boolean controlloCONGR=false;
		ResultSet rsESISTEORD = null;
		PreparedStatement pstmtESISTEORD = null;

		boolean valRitornoUPD=false;
		boolean valRitornoINSLOOP=false;
		boolean valRitornoCANC=false;
		boolean valRitornoINS=false;
		//boolean valRitornoInsTit=false;

		try {
			connection = getConnection();
			if (gara.getDettaglioPartecipantiGara()!=null && gara.getDettaglioPartecipantiGara().size() >0 )
			{

				for (int i=0; i<gara.getDettaglioPartecipantiGara().size(); i++)
				{
					PartecipantiGaraVO 	oggettoDettVO=gara.getDettaglioPartecipantiGara().get(i);
					// controllo esistenza fornitore
					String sqlESISTEORD="select *,  fornBibl.cod_fornitore as codFornBibl  from tbr_fornitori forn " ;
					sqlESISTEORD=sqlESISTEORD + " left join tbr_fornitori_biblioteche fornBibl on fornBibl.cod_fornitore=forn.cod_fornitore "   ;
					sqlESISTEORD=sqlESISTEORD + " and fornBibl.fl_canc<>'S'";
					sqlESISTEORD=sqlESISTEORD + " and fornBibl.cd_polo='" + gara.getCodPolo() +"'";
					sqlESISTEORD=sqlESISTEORD + " and fornBibl.cd_biblioteca='" + gara.getCodBibl() +"'";
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " forn.cod_fornitore=" + oggettoDettVO.getFornitore().getCodice();
					sqlESISTEORD=this.struttura(sqlESISTEORD);
					sqlESISTEORD=sqlESISTEORD + " forn.fl_canc<>'S'";

					pstmtESISTEORD = connection.prepareStatement(sqlESISTEORD);
					rsESISTEORD = pstmtESISTEORD.executeQuery(); // va in errore se non può restituire un recordset
					int numRigheOrd=0;

					if (!rsESISTEORD.next()) {
						controlloCONGR=false;
						motivo=6;
						throw new ValidationException("ordineIncongruenzaFornitoreInesistente",
								ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);
					}
					else
					{
						controlloCONGR=true;
						// localizzazione del fornitore in caso di utilizzo nella creazione gara
						if (rsESISTEORD.getString("codFornBibl")==null ||  (rsESISTEORD.getString("codFornBibl")!=null && rsESISTEORD.getString("codFornBibl").trim().length()==0)  )
						{
							// esiste il fornitore in anagrafica e non esiste fra quelli di biblioteca
							String sqlFB="insert into tbr_fornitori_biblioteche values ( " ;
							Timestamp ts = DaoManager.now();
							sqlFB= sqlFB + "'" +  gara.getCodPolo() + "'" ;  // cd_bib
							sqlFB= sqlFB + ",'" +  gara.getCodBibl() + "'" ;  // cd_biblioteca
							sqlFB= sqlFB + "," +  oggettoDettVO.getFornitore().getCodice()  ;  // cod_fornitore
							sqlFB= sqlFB  + ",''";  // tipo_pagamento
							sqlFB= sqlFB  + ",''";  // cod_cliente
							sqlFB= sqlFB  + ",''";  // nom_contatto
							sqlFB= sqlFB  + ",''";  // tel_contatto
							sqlFB= sqlFB  + ",''";  // fax_contatto
							sqlFB= sqlFB  + ",'EUR'" ;  // valuta
							sqlFB= sqlFB  + ",'" +  gara.getCodPolo() + "'" ;  // cod_polo
							sqlFB= sqlFB  + ",' '" ;  // allinea
							sqlFB= sqlFB + ",'" + gara.getUtente() + "'" ;   // ute_ins
							sqlFB= sqlFB + ",'" + ts + "'" ;   // ts_ins
							sqlFB= sqlFB + ",'" + gara.getUtente() + "'" ;   // ute_var
							sqlFB= sqlFB + ",'" + ts + "'";   // ts_var
							sqlFB= sqlFB + ",'N'";   // fl_canc
							sqlFB= sqlFB  + ")" ;  // cod_polo_bib
							pstmtInsFornBibl = connection.prepareStatement(sqlFB);
							int intRetFB = 0;
							intRetFB = pstmtInsFornBibl.executeUpdate();
							pstmtInsFornBibl.close();
							// fine estrazione codice
							if (intRetFB==1){
								valRitornoInsFornBibl=true;
								//messaggio di localizzaziojne del fornitore
							} else {
								valRitornoInsFornBibl=false;
								throw new ValidationException("ordineIncongruenzaFornitoreInesistente",
										ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);
							}
						}
					}
					rsESISTEORD.close();
					pstmtESISTEORD.close();
				}
			}

				// CONTROLLI PREVENTIVI

				String sql0="select richOff.* ";
				sql0=sql0 + " from  tba_richieste_offerta richOff ";

				sql0=this.struttura(sql0);
				sql0=sql0 + " richOff.fl_canc<>'S'";

				if (gara.getCodPolo() !=null &&  gara.getCodPolo().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0  + " richOff.cd_polo='" + gara.getCodPolo() +"'";
				}

				if (gara.getCodBibl() !=null &&  gara.getCodBibl().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0  + " richOff.cd_bib='" + gara.getCodBibl() +"'";
				}
				if (gara.getCodRicOfferta()!=null && gara.getCodRicOfferta().trim().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0  + " richOff.cod_rich_off=" + gara.getCodRicOfferta() ;
				}

				pstmt = connection.prepareStatement(sql0);
				rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
				// numero di righe del resultset
				int numRighe=0;
				List<StrutturaCombo> righeOggetto=new ArrayList();
				Timestamp ts = DaoManager.now();
				while (rs.next()) {
					numRighe=numRighe+1;
					if (numRighe==1)
					{
						String sqlUDP="update tba_richieste_offerta set " ;

						sqlUDP= sqlUDP + " ts_var = '" +  ts  + "'" ;  // ts_var
						sqlUDP= sqlUDP + ", ute_var = '" +  gara.getUtente()  + "'" ;  // ute_var
						sqlUDP= sqlUDP + ", data_rich_off =TO_DATE('"+ gara.getDataRicOfferta()+"','dd/MM/yyyy')"  ;  // data_rich_off
						sqlUDP= sqlUDP + ", prezzo_rich ="+ gara.getPrezzoIndGara()   ; // prezzo_rich
						sqlUDP= sqlUDP + ", num_copie ="+ gara.getNumCopieRicAcq()   ; // num_copie
						sqlUDP= sqlUDP + ", note ='"+ gara.getNoteOrdine().replace("'","''") + "'" ; // note
						sqlUDP= sqlUDP + ", stato_rich_off ='"+gara.getStatoRicOfferta() + "'" ; // stato_rich_off
						sqlUDP= sqlUDP + ", bid ='"+gara.getBid().getCodice() + "'" ; // bid


						sqlUDP=this.struttura(sqlUDP);
						sqlUDP=sqlUDP   + " fl_canc<>'S'";

						if (gara.getCodPolo() !=null &&  gara.getCodPolo().length()!=0)
						{
							sqlUDP=this.struttura(sqlUDP);
							sqlUDP=sqlUDP   + " cd_polo='" + gara.getCodPolo() +"'";
						}

						if (gara.getCodBibl() !=null &&  gara.getCodBibl().length()!=0)
						{
							sqlUDP=this.struttura(sqlUDP);
							sqlUDP=sqlUDP   + " cd_bib='" + gara.getCodBibl() +"'";
						}
						if (gara.getCodRicOfferta()!=null && gara.getCodRicOfferta().trim().length()!=0)
						{
							sqlUDP=this.struttura(sqlUDP);
							sqlUDP=sqlUDP   + " cod_rich_off=" + gara.getCodRicOfferta() ;
						}


						if (gara.getDataUpd()!=null )
						{
							sqlUDP=this.struttura(sqlUDP);
							sqlUDP=sqlUDP + " ts_var='" + gara.getDataUpd() + "'" ;
						}

						pstmtUDP = connection.prepareStatement(sqlUDP);
						log.debug("Debug: modifica gara");
						log.debug("Debug: " + sqlUDP);

						int intRetUDP = 0;
						intRetUDP = pstmtUDP.executeUpdate();
						pstmtUDP.close();
						// fine estrazione codice
						if (intRetUDP==1){
							valRitornoUPD=true;
							}
						else {
							throw new ValidationException("operazioneInConcorrenza",
									ValidationExceptionCodici.operazioneInConcorrenza);
						}

					}
					if (valRitornoUPD && controlloCONGR)
					{
						if (gara.getDettaglioPartecipantiGara()!=null && gara.getDettaglioPartecipantiGara().size() >0 )
						{
							// cancello i preesistenti ordini associati ed associo i nuovi
							//String sqlDEL1="delete from tra_fornitori_offerte ";

							// cancellazione logica
							String sqlDEL1="update tra_fornitori_offerte set " ;
							//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
							sqlDEL1=sqlDEL1 + " ts_var = '" +  ts  + "'" ;  // ex data_agg
							sqlDEL1=sqlDEL1 + ", ute_var = '" +  gara.getUtente()  + "'" ;  // ute_var
							sqlDEL1=sqlDEL1 + ", fl_canc = 'S'" ;  // fl_canc

							sqlDEL1=this.struttura(sqlDEL1);
							sqlDEL1=sqlDEL1 + " fl_canc<>'S'";

							if (gara.getCodPolo() !=null &&  gara.getCodPolo().length()!=0)
							{
								sqlDEL1=this.struttura(sqlDEL1);
								sqlDEL1=sqlDEL1   + " cd_polo='" + gara.getCodPolo() +"'";
							}

							if (gara.getCodBibl() !=null &&  gara.getCodBibl().length()!=0)
							{
								sqlDEL1=this.struttura(sqlDEL1);
								sqlDEL1=sqlDEL1   + " cd_bib='" + gara.getCodBibl() +"'";
							}
							if (gara.getCodRicOfferta()!=null && gara.getCodRicOfferta().trim().length()!=0)
							{
								sqlDEL1=this.struttura(sqlDEL1);
								sqlDEL1=sqlDEL1   + " cod_rich_off=" + gara.getCodRicOfferta() ;
							}

							pstmtDEL = connection.prepareStatement(sqlDEL1);
							int intRetCANC = 0;
							try {
								intRetCANC = pstmtDEL.executeUpdate();
							} catch (Exception e) {

								log.error("", e);
								intRetCANC=-1;
							}
							pstmtDEL.close();
							if (intRetCANC>=0){  // ho aggiunto il caso in cui non esistono record di dettaglio precedentemente salvati
								valRitornoCANC=true;
							}
							if (valRitornoCANC)
							{
								valRitornoINSLOOP=false;
								for (int i=0; i<gara.getDettaglioPartecipantiGara().size(); i++)
								{
									PartecipantiGaraVO 	oggettoDettVO=gara.getDettaglioPartecipantiGara().get(i);

									String sqlEsisteOrd="select * from tra_fornitori_offerte " ;

									sqlEsisteOrd=this.struttura(sqlEsisteOrd);
									sqlEsisteOrd=sqlEsisteOrd  + " fl_canc<>'S'";

									if (gara.getCodPolo()!=null &&  gara.getCodPolo().length()!=0)
									{
										sqlEsisteOrd=this.struttura(sqlEsisteOrd);
										sqlEsisteOrd=sqlEsisteOrd  + " cd_polo='" + gara.getCodPolo() +"'";
									}

									if (gara.getCodBibl()!=null &&  gara.getCodBibl().length()!=0)
									{
										sqlEsisteOrd=this.struttura(sqlEsisteOrd);
										sqlEsisteOrd=sqlEsisteOrd  + " cd_bib='" + gara.getCodBibl() +"'";
									}
									if (gara.getCodRicOfferta()!=null && gara.getCodRicOfferta().trim().length()!=0)
									{
										sqlEsisteOrd=this.struttura(sqlEsisteOrd);
										sqlEsisteOrd=sqlEsisteOrd  + " cod_rich_off=" + gara.getCodRicOfferta() ;
									}

									if ( oggettoDettVO.getFornitore() !=null && oggettoDettVO.getFornitore().getCodice() !=null &&   oggettoDettVO.getFornitore().getCodice().trim().length()!=0)
									{
										sqlEsisteOrd=this.struttura(sqlEsisteOrd);
										sqlEsisteOrd=sqlEsisteOrd+ " cod_fornitore=" + oggettoDettVO.getFornitore().getCodice() ;
									}
									pstmtExistOrd = connection.prepareStatement(sqlEsisteOrd);
									rsExistOrd = pstmtExistOrd.executeQuery(); // va in errore se non può restituire un recordset
									// numero di righe del resultset
									int numRigheEsisteOrd=0;
									while (rsExistOrd.next()) {
										numRigheEsisteOrd=numRigheEsisteOrd+1;
									}
									rsExistOrd.close() ;
									pstmtExistOrd.close() ;
									if (numRigheEsisteOrd>0)
									{
										throw new ValidationException("profilierroreFornitoreRipetuto",
												ValidationExceptionCodici.profilierroreFornitoreRipetuto);
									}
									else
									{
										// cancellazione fisica prima degli inserimenti
										//String sqlDEL1="delete from tra_fornitori_offerte ";
										// cancellazione fisica	di quelli precedentemente cancellati logicamente
										String sqlDEL2="delete from tra_fornitori_offerte ";
										sqlDEL2=this.struttura(sqlDEL2);
										sqlDEL2=sqlDEL2 + " fl_canc = 'S'" ;  // fl_canc
										if (gara.getCodPolo()!=null &&  gara.getCodPolo().length()!=0)
										{
											sqlDEL2=this.struttura(sqlDEL2);
											sqlDEL2=sqlDEL2  + " cd_polo='" + gara.getCodPolo() +"'";
										}

										if (gara.getCodBibl()!=null &&  gara.getCodBibl().length()!=0)
										{
											sqlDEL2=this.struttura(sqlDEL2);
											sqlDEL2=sqlDEL2  + " cd_bib='" + gara.getCodBibl() +"'";
										}
										if (gara.getCodRicOfferta()!=null && gara.getCodRicOfferta().trim().length()!=0)
										{
											sqlDEL2=this.struttura(sqlDEL2);
											sqlDEL2=sqlDEL2  + " cod_rich_off=" + gara.getCodRicOfferta() ;
										}

										if ( oggettoDettVO.getFornitore() !=null && oggettoDettVO.getFornitore().getCodice() !=null &&   oggettoDettVO.getFornitore().getCodice().trim().length()!=0)
										{
											sqlDEL2=this.struttura(sqlDEL2);
											sqlDEL2=sqlDEL2+ " cod_fornitore=" + oggettoDettVO.getFornitore().getCodice() ;
										}


										pstmtUDP2 = connection.prepareStatement(sqlDEL2);
										int intRetCANC2 = 0;
										intRetCANC2 = pstmtUDP2.executeUpdate();
										pstmtUDP2.close();

										//inserimento
										String sqlSub2="insert into tra_fornitori_offerte values ( " ;
										//sqlSub2 = sqlSub2+ "'" + ts + "'" ;   // data_ins
										//sqlSub2 = sqlSub2+  ",'" + ts + "'";   // data_agg
										sqlSub2 = sqlSub2+  "'" +  gara.getCodPolo() + "'"  ;  // cd_polo
										sqlSub2 = sqlSub2+  ",'" +  gara.getCodBibl() + "'"  ;  // cd_bib
										sqlSub2 = sqlSub2+  "," + oggettoDettVO.getFornitore().getCodice()  ;  // cod_fornitore
										sqlSub2 = sqlSub2+  ",'" +  gara.getCodRicOfferta()+ "'"  ;  // cod_rich_off
										sqlSub2 = sqlSub2+  ",'" +  oggettoDettVO.getNoteAlFornitore().replace("'","''") + "'"  ;  // note
										sqlSub2 = sqlSub2+  ",'" +  oggettoDettVO.getStatoPartecipante() + "'" ;  // stato_gara
										sqlSub2 = sqlSub2+  ",'" +  oggettoDettVO.getCodtipoInvio() + "'" ;  // tipo_invio
										sqlSub2 = sqlSub2+  ",TO_DATE('"+ oggettoDettVO.getDataInvioAlFornRicOfferta()+"','dd/MM/yyyy')" ;  // data_invio
										sqlSub2 = sqlSub2+  ",'" +  oggettoDettVO.getMsgRispDaFornAGara().replace("'","''") + "'" ;  // risp_da_risp
										sqlSub2 = sqlSub2+ ",'" + gara.getUtente() + "'" ;   // ute_ins
										sqlSub2 = sqlSub2+ ",'" + ts + "'" ;   // ts_ins
										sqlSub2 = sqlSub2+ ",'" + gara.getUtente() + "'" ;   // ute_var
										sqlSub2 = sqlSub2+ ",'" + ts + "'";   // ts_var
										sqlSub2 = sqlSub2+ ",'N'";   // fl_canc
										sqlSub2 = sqlSub2+ ")" ;

										pstmtINS = connection.prepareStatement(sqlSub2);
										int intRetINSLOOP = 0;
										intRetINSLOOP = pstmtINS.executeUpdate();
										pstmtINS.close();
										if (intRetINSLOOP!=1){
											valRitornoINSLOOP=true;
										}
									}	// CHIUDE IF ELSE if (numRigheEsisteOrd>0)
								} // CHIUDE FOR
							} // if (valRitornoCANC)
						} // if (gara.getDettaglioPartecipantiGara(
					} // if (valRitornoUPD)
				} // WHILE
			// } //  if (controlloCONGR)
/*			else
			{
				// potrebbe anche essere una gara salvata senza partecipanti
				motivo=2; // record forse già esistente quindi non inseribile
				throw new ValidationException("fornitoreInserimentoErroreBaseDati",
						ValidationExceptionCodici.fornitoreInserimentoErroreBaseDati);

			}
*/
			rs.close();
			pstmt.close();
			connection.close();
			// } // ROX
			// impostazione del codice di ritorno finale
			if (valRitornoUPD)
			{
				valRitorno=true;
				if (gara.getDettaglioPartecipantiGara()!=null && gara.getDettaglioPartecipantiGara().size() >0)
				{
					if (!valRitornoINSLOOP && valRitornoCANC )
					{
						valRitorno=true;
					}
					else
					{
						valRitorno=false;
					}
				}
			}


	}catch (ValidationException e) {

      	  throw e;

	} catch (Exception e) {

		log.error("", e);
	} finally {
		close(connection);
	}

    return valRitorno;
}

	public boolean   cancellaGara(GaraVO gara) throws DataException, ApplicationException, ValidationException
	{
		//Validazione.ValidaGaraVO(gara);
		int valRitornoInt=0;
    	int motivo=0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtDEL = null;
		PreparedStatement pstmtDEL2 = null;
		ResultSet rs = null;
		ResultSet rsDEL = null;
		boolean valRitorno=false;
		boolean valRitornoDEL=false;
		boolean  valRitornoCANC=false;
		boolean  valRitornoCANC2=false;
		boolean  esistenzaLegami=false;
		Timestamp ts = DaoManager.now();

		try {
			connection = getConnection();

			String sql0="select richOff.*  ";
			sql0=sql0 + " from  tba_richieste_offerta richOff ";

			sql0=this.struttura(sql0);
			sql0=sql0 + " richOff.fl_canc<>'S' ";

			if (gara.getCodBibl() !=null &&  gara.getCodBibl().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0  + " richOff.cd_bib='" + gara.getCodBibl() +"'";
			}
			if (gara.getCodPolo() !=null &&  gara.getCodPolo().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0  + " richOff.cd_polo='" + gara.getCodPolo() +"'";
			}

			if (gara.getCodRicOfferta()!=null && gara.getCodRicOfferta().trim().length()!=0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0  + " richOff.cod_rich_off=" + gara.getCodRicOfferta() ;
			}

			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;
			}
			if (numRighe==1)
			{

				// controllo esistenza legami
				// DA FARE
				// fine controllo esistenza legami

				if (!esistenzaLegami && numRighe>0) // non ci sono legami
				{
					// cancellazione logica
					String sqlDEL1="update tra_fornitori_offerte set " ;
					//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
					sqlDEL1=sqlDEL1  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
					sqlDEL1=sqlDEL1  + ", ute_var = '" +  gara.getUtente()  + "'" ;  // ute_var
					sqlDEL1=sqlDEL1  + ", fl_canc = 'S'" ;  // fl_canc
					sqlDEL1=this.struttura(sqlDEL1);
					sqlDEL1=sqlDEL1 + " fl_canc<>'S'";
					if (gara.getCodPolo() !=null &&  gara.getCodPolo().length()!=0)
					{
						sqlDEL1=this.struttura(sqlDEL1);
						sqlDEL1=sqlDEL1  + " cd_polo='" + gara.getCodPolo() +"'";
					}

					if (gara.getCodBibl() !=null &&  gara.getCodBibl().length()!=0)
					{
						sqlDEL1=this.struttura(sqlDEL1);
						sqlDEL1=sqlDEL1  + " cd_bib='" + gara.getCodBibl() +"'";
					}
					if (gara.getCodRicOfferta()!=null && gara.getCodRicOfferta().trim().length()!=0)
					{
						sqlDEL1=this.struttura(sqlDEL1);
						sqlDEL1=sqlDEL1   + " cod_rich_off=" + gara.getCodRicOfferta() ;
					}

				//
					pstmtDEL = connection.prepareStatement(sqlDEL1);
					int intRetCANC = 0;
					//valRitornoCANC = pstmtDEL.execute();
					intRetCANC = pstmtDEL.executeUpdate();
					pstmtDEL.close();
					if (intRetCANC>=1){
						valRitornoCANC=true;
					}

					if (valRitornoCANC)
					{
						// cancellazione logica
						String sqlDEL2="update tba_richieste_offerta set " ;
						//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
						sqlDEL2=sqlDEL2  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
						sqlDEL2=sqlDEL2  + ", ute_var = '" +  gara.getUtente()  + "'" ;  // ute_var
						sqlDEL2=sqlDEL2  + ", fl_canc = 'S'" ;  // fl_canc
						sqlDEL2=this.struttura(sqlDEL2);
						sqlDEL2=sqlDEL2 + " fl_canc<>'S'";
						if (gara.getCodPolo() !=null &&  gara.getCodPolo().length()!=0)
						{
							sqlDEL2=this.struttura(sqlDEL2);
							sqlDEL2=sqlDEL2   + " cd_polo='" + gara.getCodPolo() +"'";
						}

						if (gara.getCodBibl() !=null &&  gara.getCodBibl().length()!=0)
						{
							sqlDEL2=this.struttura(sqlDEL2);
							sqlDEL2=sqlDEL2  + " cd_bib='" + gara.getCodBibl() +"'";
						}
						if (gara.getCodRicOfferta()!=null && gara.getCodRicOfferta().trim().length()!=0)
						{
							sqlDEL2=this.struttura(sqlDEL2);
							sqlDEL2=sqlDEL2   + " cod_rich_off=" + gara.getCodRicOfferta() ;
						}

						pstmtDEL2 = connection.prepareStatement(sqlDEL2);
						int intRetCANC2 = 0;
						intRetCANC2 = pstmtDEL2.executeUpdate();
						pstmtDEL2.close();
						// oppure == numRighe
						if (intRetCANC2==1){
							valRitornoCANC2=true;
							valRitorno=true;
						}
					}

					//cancellazione fisica

				}
				else
				{
					// impedire la cancellazione (emettere messaggio specifico)

				}


				// cancello i preesistenti partecipanti  associati
			}
			else
			{
				motivo=2; // record forse già esistente quindi non inseribile
				throw new ValidationException("fornitoreInserimentoErroreBaseDati",
						ValidationExceptionCodici.fornitoreInserimentoErroreBaseDati);
			}

			connection.close();

		}catch (ValidationException e) {

	      	  throw e;

		}
		 catch (Exception e) {

			log.error("", e);
		} finally {
			close(connection);
		}

     return valRitorno;
	}

	public boolean   cancellaFornitore(FornitoreVO fornitore) throws DataException, ApplicationException, ValidationException
	{
		//Validazione.ValidaFornitoreVO (fornitore);
		int valRitornoInt=0;
    	int motivo=0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtDEL = null;
		PreparedStatement pstmtDEL2 = null;
		ResultSet rs = null;
		ResultSet rsDEL = null;
		boolean valRitorno=false;
		boolean valRitornoDEL=false;
		boolean  valRitornoCANC=false;
		boolean  valRitornoCANC2=false;
		boolean esisteFornitore=false;
		boolean esistenzaLegami=false;
		Timestamp ts = DaoManager.now();

		try {
			connection = getConnection();
			// CONTROLLI PREVENTIVI
			// controllo esistenza fornitore
			String sql0="select distinct forn.cod_fornitore from tbr_fornitori  forn ";
			if (fornitore.getFornitoreBibl()!=null && fornitore.getFornitoreBibl().getCodBibl()!=null && fornitore.getFornitoreBibl().getCodBibl().trim().length()>0)
			{
				sql0=sql0 + " join tbr_fornitori_biblioteche fornBib on fornBib.cod_fornitore=forn.cod_fornitore and  fornBib.fl_canc<>'S' ";
			}

			sql0=this.struttura(sql0);
			sql0=sql0 + " forn.fl_canc<>'S'";

			if (fornitore.getCodFornitore()!=null && fornitore.getCodFornitore().length()!=0  )
			{
				//TODO  gestire le biblioteche affiliate a fronte della selezione del bottone relativo
				sql0=this.struttura(sql0);
				sql0=sql0 + " forn.cod_fornitore='" + fornitore.getCodFornitore() +"'";
			}

			if (fornitore.getFornitoreBibl()!=null && fornitore.getFornitoreBibl().getCodBibl()!=null && fornitore.getFornitoreBibl().getCodBibl().trim().length()>0)
			{
				//TODO  gestire le biblioteche affiliate a fronte della selezione del bottone relativo
				sql0=this.struttura(sql0);
				sql0=sql0 + " fornBib.cd_biblioteca='" + fornitore.getFornitoreBibl().getCodBibl() +"'";
			}

			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			int numRighe=0;
			while (rs.next()) {
				numRighe=numRighe+1;
			}
			rs.close();

			if (numRighe==1) // il fornitore esiste
			{
				esisteFornitore=true;
			}
			if (esisteFornitore) // il fornitore esiste
			{


				// controllo esistenza legami con ordini, comunicazioni e gare
				String sql1="select distinct forn.cod_fornitore, ord.id_ordine, richOffForn.cod_rich_off,  com.cod_msg  from tbr_fornitori  forn ";
				sql1=sql1 + "left join tba_ordini ord on ord.cod_fornitore=forn.cod_fornitore and ord.fl_canc<>'S' and ord.stato_ordine <> 'N'  ";
				sql1=sql1 + "left join tra_fornitori_offerte richOffForn on richOffForn.cod_fornitore=forn.cod_fornitore and richOffForn.fl_canc<>'S'  ";
				sql1=sql1 + "left join tra_messaggi com on com.cod_fornitore=forn.cod_fornitore and com.fl_canc<>'S'  ";

				sql1=this.struttura(sql1);
				sql1=sql1 + " forn.fl_canc<>'S'";

				if (fornitore.getCodFornitore()!=null && fornitore.getCodFornitore().length()!=0  )
				{
					//TODO  gestire le biblioteche affiliate a fronte della selezione del bottone relativo
					sql1=this.struttura(sql1);
					sql1=sql1 + " forn.cod_fornitore='" + fornitore.getCodFornitore() +"'";
				}

				pstmt = connection.prepareStatement(sql1);
				rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
				// numero di righe del resultset
				int numRighe2=0;
				while (rs.next()) {
					numRighe2=numRighe2+1;
					if (rs.getString("id_ordine")!=null || rs.getString("cod_rich_off")!=null || rs.getString("cod_msg")!=null )
					{
						esistenzaLegami=true;
					}
				}
				rs.close();
				if (!esistenzaLegami && numRighe2>0) // non ci sono legami con ordini, comunicazioni e gare
				{
					// controllo esistenza legami con fornitori di ALTRE BIBLIOTECHE (condivisione del fornitore)
					String sql3="select forn.cod_fornitore from tbr_fornitori  forn ";
					sql3=sql3 + " join tbr_fornitori_biblioteche fornBib on fornBib.cod_fornitore=forn.cod_fornitore and  fornBib.fl_canc<>'S' ";
					sql3=this.struttura(sql3);
					sql3=sql3 + " forn.fl_canc<>'S'";
					if (fornitore.getCodFornitore()!=null && fornitore.getCodFornitore().length()!=0  )
					{
						//TODO  gestire le biblioteche affiliate a fronte della selezione del bottone relativo
						sql3=this.struttura(sql3);
						sql3=sql3 + " forn.cod_fornitore='" + fornitore.getCodFornitore() +"'";
					}
					if (fornitore.getFornitoreBibl()!=null && fornitore.getFornitoreBibl().getCodBibl()!=null && fornitore.getFornitoreBibl().getCodBibl().trim().length()>0)
					{
						// codice biblioteca diverso dal quello attivo
						sql3=this.struttura(sql3);
						sql3=sql3 + " fornBib.cd_biblioteca <> '" + fornitore.getFornitoreBibl().getCodBibl() +"'";
					}

					pstmt = connection.prepareStatement(sql3);
					rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
					// numero di righe del resultset
					int numRighe3=0;
					while (rs.next()) {
						numRighe3=numRighe3+1;
					}
					rs.close();

					if (numRighe3>0) // il fornitore è condiviso
					{
						motivo=3;

						// delocalizzo solo quello attivo e mantengo gli altri
						String sqlDEL1="update tbr_fornitori_biblioteche set " ;
						//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
						sqlDEL1=sqlDEL1  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
						sqlDEL1=sqlDEL1  + ", ute_var = '" +  fornitore.getUtente()  + "'" ;  // ute_var
						sqlDEL1=sqlDEL1  + ", fl_canc = 'S'" ;  // fl_canc
						sqlDEL1=this.struttura(sqlDEL1);
						sqlDEL1=sqlDEL1 + " fl_canc<>'S'";

						if (fornitore.getCodPolo() !=null &&  fornitore.getCodPolo().length()!=0)
						{
							sqlDEL1=this.struttura(sqlDEL1);
							sqlDEL1=sqlDEL1  + " cd_polo='" + fornitore.getCodPolo() +"'";
						}
						if (fornitore.getFornitoreBibl()!=null && fornitore.getFornitoreBibl().getCodBibl()!=null && fornitore.getFornitoreBibl().getCodBibl().trim().length()>0)
						{
							sqlDEL1=this.struttura(sqlDEL1);
							sqlDEL1=sqlDEL1  + " cd_biblioteca='" + fornitore.getFornitoreBibl().getCodBibl() +"'";
						}
						if (fornitore.getCodFornitore()!=null && fornitore.getCodFornitore().length()!=0  )
						{
							sqlDEL1=this.struttura(sqlDEL1);
							sqlDEL1=sqlDEL1   + " cod_fornitore='" + fornitore.getCodFornitore() +"'";
						}

						//
						pstmtDEL = connection.prepareStatement(sqlDEL1);
						int intRetCANC = 0;
						//valRitornoCANC = pstmtDEL.execute();
						intRetCANC = pstmtDEL.executeUpdate();
						pstmtDEL.close();
						if (intRetCANC>0){
							valRitornoCANC=true;
						}
					}
					else
					{
						// cancellazione totale
						motivo=4;
						if (fornitore.getFornitoreBibl()!=null && fornitore.getFornitoreBibl().getCodBibl()!=null && fornitore.getFornitoreBibl().getCodBibl().trim().length()>0)
						{
							// delocalizzo  quello attivo
							String sqlDEL1="update tbr_fornitori_biblioteche set " ;
							//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
							sqlDEL1=sqlDEL1  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
							sqlDEL1=sqlDEL1  + ", ute_var = '" +  fornitore.getUtente()  + "'" ;  // ute_var
							sqlDEL1=sqlDEL1  + ", fl_canc = 'S'" ;  // fl_canc
							sqlDEL1=this.struttura(sqlDEL1);
							sqlDEL1=sqlDEL1 + " fl_canc<>'S'";

							if (fornitore.getCodPolo() !=null &&  fornitore.getCodPolo().length()!=0)
							{
								sqlDEL1=this.struttura(sqlDEL1);
								sqlDEL1=sqlDEL1  + " cd_polo='" + fornitore.getCodPolo() +"'";
							}
							if (fornitore.getFornitoreBibl()!=null && fornitore.getFornitoreBibl().getCodBibl()!=null && fornitore.getFornitoreBibl().getCodBibl().trim().length()>0)
							{
								sqlDEL1=this.struttura(sqlDEL1);
								sqlDEL1=sqlDEL1  + " cd_biblioteca='" + fornitore.getFornitoreBibl().getCodBibl() +"'";
							}
							if (fornitore.getCodFornitore()!=null && fornitore.getCodFornitore().length()!=0  )
							{
								sqlDEL1=this.struttura(sqlDEL1);
								sqlDEL1=sqlDEL1   + " cod_fornitore='" + fornitore.getCodFornitore() +"'";
							}

							//
							pstmtDEL = connection.prepareStatement(sqlDEL1);
							int intRetCANC = 0;
							//valRitornoCANC = pstmtDEL.execute();
							intRetCANC = pstmtDEL.executeUpdate();
							pstmtDEL.close();
							if (intRetCANC==1){
								valRitornoCANC=true;
							}
						}

						// CANCELLO ANAGRAFICA
						// OTTOBRE 2013: La cancellazione dell'anagrafica puo avvenire solo:
						// nel caso di fornitore puro (codice regione non valorizzato) da menu acquisizioni (prodEditoriale=SI)
						// nel caso di editore puro (getFornitoreBibl() non valorizzato) da menu editore (prodEditoriale=NO)

						if ((fornitore.getRegione() == null || fornitore.getRegione().trim().equals("")) &&
								fornitore.getFornitoreBibl() != null) {
							// caso di fornitore puro (codice regione non valorizzato)
							if (fornitore.getProdEditoriale().equals("NO")) {
								String sqlDEL2="update tbr_fornitori set " ;
								//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
								sqlDEL2=sqlDEL2  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
								sqlDEL2=sqlDEL2  + ", ute_var = '" +  fornitore.getUtente()  + "'" ;  // ute_var
								sqlDEL2=sqlDEL2  + ", fl_canc = 'S'" ;  // fl_canc
								sqlDEL2=this.struttura(sqlDEL2);
								sqlDEL2=sqlDEL2 + " fl_canc<>'S'";

								if (fornitore.getCodFornitore()!=null && fornitore.getCodFornitore().length()!=0  )
								{
									sqlDEL2=this.struttura(sqlDEL2);
									sqlDEL2=sqlDEL2   + " cod_fornitore='" + fornitore.getCodFornitore() +"'";
								}

								//
								pstmtDEL = connection.prepareStatement(sqlDEL2);
								int intRetCANC2 = 0;
								//valRitornoCANC = pstmtDEL.execute();
								intRetCANC2 = pstmtDEL.executeUpdate();
								pstmtDEL.close();
								if (intRetCANC2==1){
									valRitornoCANC2=true;
								}
							}

						} else if ((fornitore.getRegione() != null || fornitore.getRegione().trim().length()> 1) &&
								fornitore.getFornitoreBibl() == null) {
							// caso di editore puro (getFornitoreBibl() non valorizzato)
							if (fornitore.getProdEditoriale().equals("SI")) {
								String sqlDEL2="update tbr_fornitori set " ;
								//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
								sqlDEL2=sqlDEL2  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
								sqlDEL2=sqlDEL2  + ", ute_var = '" +  fornitore.getUtente()  + "'" ;  // ute_var
								sqlDEL2=sqlDEL2  + ", fl_canc = 'S'" ;  // fl_canc
								sqlDEL2=this.struttura(sqlDEL2);
								sqlDEL2=sqlDEL2 + " fl_canc<>'S'";

								if (fornitore.getCodFornitore()!=null && fornitore.getCodFornitore().length()!=0  )
								{
									sqlDEL2=this.struttura(sqlDEL2);
									sqlDEL2=sqlDEL2   + " cod_fornitore='" + fornitore.getCodFornitore() +"'";
								}

								//
								pstmtDEL = connection.prepareStatement(sqlDEL2);
								int intRetCANC2 = 0;
								//valRitornoCANC = pstmtDEL.execute();
								intRetCANC2 = pstmtDEL.executeUpdate();
								pstmtDEL.close();
								if (intRetCANC2==1){
									valRitornoCANC2=true;
								}
							}

						} else {
							// Il fornitore è anche editore e viceversa o la cancellazione viene inviata dal Menu incoerente
							// non si puo cancellare l'anagrafica
						}

//						String sqlDEL2="update tbr_fornitori set " ;
//						//sqlUDP= sqlUDP + " data_ins = '" +  ts  + "'" ;  // data_ins
//						sqlDEL2=sqlDEL2  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
//						sqlDEL2=sqlDEL2  + ", ute_var = '" +  fornitore.getUtente()  + "'" ;  // ute_var
//						sqlDEL2=sqlDEL2  + ", fl_canc = 'S'" ;  // fl_canc
//						sqlDEL2=this.struttura(sqlDEL2);
//						sqlDEL2=sqlDEL2 + " fl_canc<>'S'";
//
//						if (fornitore.getCodFornitore()!=null && fornitore.getCodFornitore().length()!=0  )
//						{
//							sqlDEL2=this.struttura(sqlDEL2);
//							sqlDEL2=sqlDEL2   + " cod_fornitore='" + fornitore.getCodFornitore() +"'";
//						}
//
//						//
//						pstmtDEL = connection.prepareStatement(sqlDEL2);
//						int intRetCANC2 = 0;
//						//valRitornoCANC = pstmtDEL.execute();
//						intRetCANC2 = pstmtDEL.executeUpdate();
//						pstmtDEL.close();
//						if (intRetCANC2==1){
//							valRitornoCANC2=true;
//						}
					}

				}
				else
				{
					// impedire la cancellazione (emettere messaggio specifico)
					throw new ValidationException("erroreCancellaFornitore",
							ValidationExceptionCodici.erroreCancellaFornitore);
				}

			}
			else
			{
				motivo=2; // record forse già esistente quindi non inseribile
				throw new ValidationException("fornitoreInserimentoErroreBaseDati",
						ValidationExceptionCodici.fornitoreInserimentoErroreBaseDati);

			}
			pstmt.close();
			connection.close();
			if (motivo==3)
			{
				if (valRitornoCANC)
				{
					valRitorno=true;
				}

			}
			if (motivo==4)
			{
				if (valRitornoCANC2)
				{
					valRitorno=true;
				}

				if (fornitore.getFornitoreBibl()!=null && fornitore.getFornitoreBibl().getCodBibl()!=null && fornitore.getFornitoreBibl().getCodBibl().trim().length()>0)
				{
					if (!valRitornoCANC)
					{
						valRitorno=false;
					}
					else
					{
						valRitorno=true;
					}

				}

			}

		}catch (ValidationException e) {

	      	  throw e;

		}
		 catch (Exception e) {

			log.error("", e);
		} finally {
			close(connection);
		}

     return valRitorno;
	}


	public List<OffertaFornitoreVO> getRicercaListaOfferte(ListaSuppOffertaFornitoreVO ricercaOfferte)	throws ResourceNotFoundException, ApplicationException, ValidationException
	{
		Validazione.ValidaListaSuppOffertaFornitoreVO(ricercaOfferte);
		String ticket="";
		ticket=ricercaOfferte.getTicket();

		List<OffertaFornitoreVO> listaOfferte = new ArrayList<OffertaFornitoreVO>();

		int ret = 0;
        // execute the search here
			OffertaFornitoreVO rec = null;
    		Connection connection = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			ResultSet rsSub= null;
			PreparedStatement pstmtSub = null;

			try {
				// contiene i criteri di ricerca
				connection = getConnection();
				String sql="select offForn.*, offForn.ts_var as dataUpd, forn.nom_fornitore, TO_CHAR(offForn.data_offerta,'dd/MM/yyyy') as data_offerta_str   ";
				sql +=" from  tba_offerte_fornitore  offForn ";
				sql +=" left join tbr_fornitori forn on offForn.cod_fornitore=forn.cod_fornitore and forn.fl_canc<>'S'";

				sql=this.struttura(sql);
				sql +=" offForn.fl_canc<>'S'";

				if (ricercaOfferte.getCodPolo() !=null &&  ricercaOfferte.getCodPolo().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" offForn.cd_polo='" + ricercaOfferte.getCodPolo() +"'";
				}

				if (ricercaOfferte.getCodBibl() !=null &&  ricercaOfferte.getCodBibl().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" offForn.cd_bib='" + ricercaOfferte.getCodBibl() +"'";
				}
				if (ricercaOfferte.getIdentificativoOfferta()!=null && ricercaOfferte.getIdentificativoOfferta().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" offForn.bid_p='" + ricercaOfferte.getIdentificativoOfferta()+"'" ;
				}

				if (ricercaOfferte.getStatoOfferta()!=null && ricercaOfferte.getStatoOfferta().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" offForn.stato_offerta='" + ricercaOfferte.getStatoOfferta()+"'" ;
				}

				if (ricercaOfferte.getFornitore()!=null && ricercaOfferte.getFornitore().getCodice()!=null && ricercaOfferte.getFornitore().getCodice().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" offForn.cod_fornitore=" + ricercaOfferte.getFornitore().getCodice() ;
				}

				if (ricercaOfferte.getFornitore()!=null && ricercaOfferte.getFornitore().getDescrizione()!=null && ricercaOfferte.getFornitore().getDescrizione().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" forn.nom_fornitore  like '%" + ricercaOfferte.getFornitore().getDescrizione().replace("'","''") +"%'" ;
				}
				if (ricercaOfferte.getBid()!=null && ricercaOfferte.getBid().getCodice()!=null && ricercaOfferte.getBid().getCodice().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +=" offForn.bid='" + ricercaOfferte.getBid().getCodice() +"'";
				}

				if (ricercaOfferte.getAutore()!=null && ricercaOfferte.getAutore().getDescrizione()!=null && ricercaOfferte.getAutore().getDescrizione().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +="(";;
					sql +=" offForn.aut1 ='" + ricercaOfferte.getAutore().getDescrizione() +"'";;
					sql +=" or offForn.aut2 ='" + ricercaOfferte.getAutore().getDescrizione() +"'";;
					sql +=" or offForn.aut3 ='" + ricercaOfferte.getAutore().getDescrizione() +"'";;
					sql +=" or offForn.aut4 ='" + ricercaOfferte.getAutore().getDescrizione() +"'";;
					sql +=")";;
				}

				if (ricercaOfferte.getAutore()!=null && ricercaOfferte.getAutore().getCodice()!=null && ricercaOfferte.getAutore().getCodice().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +="(";;
					sql +=" offForn.k_aut1 ='" + ricercaOfferte.getAutore().getCodice() +"'";;
					sql +=" or offForn.k_aut2 ='" + ricercaOfferte.getAutore().getCodice() +"'";;
					sql +=" or offForn.k_aut3 ='" + ricercaOfferte.getAutore().getCodice() +"'";;
					sql +=" or offForn.k_aut4 ='" + ricercaOfferte.getAutore().getCodice() +"'";;
					sql +=")";;
				}

				if (ricercaOfferte.getClassificazione()!=null && ricercaOfferte.getClassificazione().getDescrizione()!=null && ricercaOfferte.getClassificazione().getDescrizione().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +="(";;
					sql +=" offForn.classe1 ='" + ricercaOfferte.getClassificazione().getDescrizione() +"'";;
					sql +=" or offForn.classe2 ='" + ricercaOfferte.getClassificazione().getDescrizione() +"'";;
					sql +=" or offForn.classe3 ='" + ricercaOfferte.getClassificazione().getDescrizione() +"'";;
					sql +=")";;
				}
				if (ricercaOfferte.getClassificazione()!=null && ricercaOfferte.getClassificazione().getCodice()!=null && ricercaOfferte.getClassificazione().getCodice().trim().length()!=0)
				{
					sql=this.struttura(sql);
					sql +="(";;
					sql +=" offForn.tipo1_classe ='" + ricercaOfferte.getClassificazione().getCodice() +"'";;
					sql +=" or offForn.tipo2_classe ='" + ricercaOfferte.getClassificazione().getCodice() +"'";;
					sql +=" or offForn.tipo3_classe ='" + ricercaOfferte.getClassificazione().getCodice() +"'";;
					sql +=")";;
				}

				// ordinamento passato
				if (ricercaOfferte.getOrdinamento()==null || (ricercaOfferte.getOrdinamento()!=null && ricercaOfferte.getOrdinamento().equals("")))
				{
					sql +=" order by offForn.cd_bib, offForn.bid_p  ";
				}
				else if (ricercaOfferte.getOrdinamento().equals("1"))
				{
					sql +=" order by offForn.cd_bib, offForn.bid_p  ";
				}
				else if (ricercaOfferte.getOrdinamento().equals("2"))
				{
					sql +=" order by offForn.cd_bib, offForn.cod_fornitore ";
					// forn.nom_fornitore
				}
				else if (ricercaOfferte.getOrdinamento().equals("3"))
				{
					sql +=" order by offForn.cd_bib, offForn.data_offerta desc  ";
				}

				pstmt = connection.prepareStatement(sql);
				//log.debug("Debug: lettura offerte");
				//log.debug("Debug: " + sql);

				rs = pstmt.executeQuery();
				int numRighe=0;
				while (rs.next()) {
					numRighe=numRighe+1;
					rec = new OffertaFornitoreVO();
					rec.setProgressivo(numRighe);
					rec.setIDOff(rs.getInt("id_offerte_fornitore"));
					rec.setCodPolo(rs.getString("cd_polo"));
					rec.setCodBibl(rs.getString("cd_bib"));
					rec.setBid(new StrutturaCombo("",""));
					rec.getBid().setCodice(rs.getString("bid"));
					rec.setDataUpd(rs.getTimestamp("dataUpd"));

					String isbd="";
					String bid="";
					String naturaBid="";

					if (rs.getString("bid")!=null && rs.getString("bid").trim().length()!=0)
					{
						bid=rs.getString("bid");

						try {
							TitoloACQVO recTit=null;
							recTit = this.getTitoloOrdineTer(rs.getString("bid").trim());
							if (recTit!=null && recTit.getIsbd()!=null) {
								bid=rs.getString("bid").trim();
								isbd=recTit.getIsbd();
								naturaBid=recTit.getNatura();
							}
							if (recTit==null) {
								isbd="titolo non trovato";
								bid=rs.getString("bid");
								naturaBid="";
							}
						} catch (Exception e) {
								isbd="titolo non trovato";
								bid=rs.getString("bid");
								naturaBid="";
						}
					}
					rec.setNaturaBid(naturaBid);
					rec.getBid().setDescrizione(isbd);

					rec.setChiaveTitoloIsbd(new StrutturaCombo("",""));
					//rec.getChiaveTitoloIsbd().setCodice();
					rec.getChiaveTitoloIsbd().setDescrizione(rs.getString("k_titolo"));

					rec.setCodiceStandard(rs.getString("k_titolo"));
					rec.setDataOfferta(rs.getString("data_offerta_str"));
					rec.setData(rs.getString("data1"));
					rec.setFornitore(new StrutturaCombo("",""));
					rec.getFornitore().setCodice(String.valueOf(rs.getInt("cod_fornitore")));
					rec.getFornitore().setDescrizione(rs.getString("nom_fornitore"));

					rec.setIdentificativoOfferta(rs.getString("bid_p"));
					rec.setInformazioniPrezzo(rs.getString("inf_sul_prezzo"));

					rec.setLingua(new StrutturaCombo("",""));
					rec.getLingua().setCodice(rs.getString("lingua"));
					//rec.getLingua().setDescrizione();

					rec.setPaese(new StrutturaCombo("",""));
					rec.getPaese().setCodice(rs.getString("paese").trim()); // perchè il campo della tab è di 3 char
					//rec.getPaese().setDescrizione();

					rec.setPrezzo(String.valueOf(rs.getFloat("prezzo")));
					rec.setStatoOfferta(rs.getString("stato_offerta"));
					rec.setTipoData(rs.getString("tipo_data"));
					rec.setTipoPrezzo(rs.getString("tipo_prezzo").trim()); // perchè il campo della tab è di 3 char
					rec.setTipoProvenienza(new StrutturaCombo("",""));
					rec.getTipoProvenienza().setCodice(rs.getString("tip_rec"));

					if (rec.getTipoProvenienza().getCodice().equals("L"))
					{
						rec.getTipoProvenienza().setDescrizione("LIBRAIO");

					}
					if (rec.getTipoProvenienza().getCodice().equals("E"))
					{
						rec.getTipoProvenienza().setDescrizione("EDITORE");

					}
					if (rec.getTipoProvenienza().getCodice().equals("B"))
					{
						rec.getTipoProvenienza().setDescrizione("BIBLIOGRAFIA");

					}
					rec.setValutaOfferta(rs.getString("valuta"));
					rec.setNumeroStandard(rs.getString("num_standard"));

					List arrOffFornAut=new ArrayList();
					OffertaFornitoreAutoreVO recOffFornAut=new OffertaFornitoreAutoreVO();
					if (rs.getString("k_aut1")!=null && rs.getString("k_aut1").length()>0)
					{
						recOffFornAut.setChiaveAutore(rs.getString("k_aut1"));
						recOffFornAut.setAutore(rs.getString("aut1"));
						recOffFornAut.setResponsabilitaAutore(rs.getString("resp_aut1"));
						recOffFornAut.setTipoAutore(rs.getString("tip_aut1"));
						arrOffFornAut.add(recOffFornAut);
					}
					if (rs.getString("k_aut2")!=null && rs.getString("k_aut2").trim().length()>0)
					{
						recOffFornAut=new OffertaFornitoreAutoreVO();
						recOffFornAut.setChiaveAutore(rs.getString("k_aut2"));
						recOffFornAut.setAutore(rs.getString("aut2"));
						recOffFornAut.setResponsabilitaAutore(rs.getString("resp_aut2"));
						recOffFornAut.setTipoAutore(rs.getString("tip_aut2"));
						arrOffFornAut.add(recOffFornAut);
					}
					if (rs.getString("k_aut3")!=null && rs.getString("k_aut3").trim().length()>0)
					{
						recOffFornAut=new OffertaFornitoreAutoreVO();
						recOffFornAut.setChiaveAutore(rs.getString("k_aut3"));
						recOffFornAut.setAutore(rs.getString("aut3"));
						recOffFornAut.setResponsabilitaAutore(rs.getString("resp_aut3"));
						recOffFornAut.setTipoAutore(rs.getString("tip_aut3"));
						arrOffFornAut.add(recOffFornAut);
					}
					if (rs.getString("k_aut4")!=null && rs.getString("k_aut4").trim().length()>0)
					{
						recOffFornAut=new OffertaFornitoreAutoreVO();
						recOffFornAut.setChiaveAutore(rs.getString("k_aut4"));
						recOffFornAut.setAutore(rs.getString("aut4"));
						recOffFornAut.setResponsabilitaAutore(rs.getString("resp_aut4"));
						recOffFornAut.setTipoAutore(rs.getString("tip_aut4"));
						arrOffFornAut.add(recOffFornAut);
					}
					rec.setOffFornAut(arrOffFornAut);

					List arrOffFornClass=new ArrayList();
					OffertaFornitoreClassificazioneVO recOffFornClass=new OffertaFornitoreClassificazioneVO();
					if (rs.getString("classe1")!=null && rs.getString("classe1").trim().length()>0)
					{
						recOffFornClass.setIdentificativoClasse(rs.getString("classe1"));
						recOffFornClass.setCodiceSistemaClassificazione(rs.getString("tipo1_classe"));
						arrOffFornClass.add(recOffFornClass);
					}
					if (rs.getString("classe2")!=null && rs.getString("classe2").trim().length()>0)
					{
						recOffFornClass=new OffertaFornitoreClassificazioneVO();
						recOffFornClass.setIdentificativoClasse(rs.getString("classe2"));
						recOffFornClass.setCodiceSistemaClassificazione(rs.getString("tipo2_classe"));
						arrOffFornClass.add(recOffFornClass);
					}
					if (rs.getString("classe3")!=null && rs.getString("classe3").trim().length()>0)
					{
						recOffFornClass=new OffertaFornitoreClassificazioneVO();
						recOffFornClass.setIdentificativoClasse(rs.getString("classe3"));
						recOffFornClass.setCodiceSistemaClassificazione(rs.getString("tipo3_classe"));
						arrOffFornClass.add(recOffFornClass);
					}
					rec.setOffFornClass(arrOffFornClass);

					List arrOffFornSogg=new ArrayList();
					OffertaFornitoreSoggettoVO recOffFornSogg=new OffertaFornitoreSoggettoVO();
					if (rs.getString("k_sog1")!=null && rs.getString("k_sog1").trim().length()>0)
					{
						recOffFornSogg.setChiaveSoggetto(rs.getString("k_sog1"));
						recOffFornSogg.setSoggetto(rs.getString("sog1"));
						arrOffFornSogg.add(recOffFornSogg);
					}
					if (rs.getString("k_sog2")!=null && rs.getString("k_sog2").trim().length()>0)
					{
						recOffFornSogg=new OffertaFornitoreSoggettoVO();
						recOffFornSogg.setChiaveSoggetto(rs.getString("k_sog2"));
						recOffFornSogg.setSoggetto(rs.getString("sog2"));
						arrOffFornSogg.add(recOffFornSogg);
					}
					if (rs.getString("k_sog3")!=null && rs.getString("k_sog3").trim().length()>0)
					{
						recOffFornSogg=new OffertaFornitoreSoggettoVO();
						recOffFornSogg.setChiaveSoggetto(rs.getString("k_sog3"));
						recOffFornSogg.setSoggetto(rs.getString("sog3"));
						arrOffFornSogg.add(recOffFornSogg);
					}
					rec.setOffFornSogg(arrOffFornSogg);

					List arrOffFornSerie=new ArrayList();
					OffertaFornitoreSerieVO recOffFornSerie=new OffertaFornitoreSerieVO();
					if (rs.getString("k1_serie")!=null && rs.getString("k1_serie").trim().length()>0)
					{
						recOffFornSerie.setChiaveCollana(rs.getString("k1_serie"));
						recOffFornSerie.setCollana(rs.getString("serie1"));
						recOffFornSerie.setNumCollana(rs.getString("num1_serie"));
						arrOffFornSerie.add(recOffFornSerie);
					}
					if (rs.getString("k2_serie")!=null && rs.getString("k2_serie").trim().length()>0)
					{
						recOffFornSerie=new OffertaFornitoreSerieVO();
						recOffFornSerie.setChiaveCollana(rs.getString("k2_serie"));
						recOffFornSerie.setCollana(rs.getString("serie2"));
						recOffFornSerie.setNumCollana(rs.getString("num2_serie"));
						arrOffFornSerie.add(recOffFornSerie);
					}
					if (rs.getString("k3_serie")!=null && rs.getString("k3_serie").trim().length()>0)
					{
						recOffFornSerie=new OffertaFornitoreSerieVO();
						recOffFornSerie.setChiaveCollana(rs.getString("k3_serie"));
						recOffFornSerie.setCollana(rs.getString("serie3"));
						recOffFornSerie.setNumCollana(rs.getString("num3_serie"));
						arrOffFornSerie.add(recOffFornSerie);
					}
					rec.setOffFornSerie(arrOffFornSerie);

					List arrOffFornIsbd=new ArrayList();
					OffertaFornitoreIsbdVO recOffFornIsbd=new OffertaFornitoreIsbdVO();
					if (rs.getString("k_titolo")!=null && rs.getString("k_titolo").trim().length()>0)
					{
						recOffFornIsbd.setChiaveTitoloIsbd(rs.getString("k_titolo"));
						recOffFornIsbd.setDescrizioneIsbd(rs.getString("isbd_1"));
						arrOffFornIsbd.add(recOffFornIsbd);
						recOffFornIsbd.setChiaveTitoloIsbd(rs.getString("k_titolo"));
						recOffFornIsbd.setDescrizioneIsbd(rs.getString("isbd_2"));
						arrOffFornIsbd.add(recOffFornIsbd);
					}
					rec.setOffFornIsbd(arrOffFornIsbd);


					List arrOffFornNote=new ArrayList();
					OffertaFornitoreNoteEdiVO recOffFornNote=new OffertaFornitoreNoteEdiVO();
					if (rs.getString("prot_edi")!=null && rs.getString("prot_edi").trim().length()>0)
					{
						recOffFornNote.setCoordinateEdi(rs.getString("prot_edi"));
						recOffFornNote.setNoteEdi(rs.getString("note_edi"));
						recOffFornNote.setAltriRiferimenti(rs.getString("altri_rif"));
						arrOffFornNote.add(recOffFornNote);
					}
					rec.setOffFornNote(arrOffFornNote);

					listaOfferte.add(rec);
				} // End while

				rs.close();
				pstmt.close();
				connection.close();
			}	catch (ValidationException e) {

		    	  throw e;



		} catch (Exception e) {

			// l'errore capita in questo punto
			log.error("", e);
		} finally {
			close(connection);
		}

		Validazione.ValidaRicercaOfferte (listaOfferte);
        return listaOfferte;
	}

	public ConfigurazioneORDVO loadConfigurazioneOrdini(ConfigurazioneORDVO configurazioneORD) throws DataException, ApplicationException , ValidationException
	{
		//Validazione.ValidaBuoniOrdineVO (buonoOrd);
		boolean valRitorno=false;
    	int motivo=0;
    	ConfigurazioneORDVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
				connection = getConnection();
				String sql0="select *, ts_var as dataUpd  ";
				sql0=sql0  + " from  tba_parametri_ordine ";

				sql0=this.struttura(sql0);
				sql0=sql0 + " fl_canc<>'S'";

				if (configurazioneORD.getCodBibl() !=null &&  configurazioneORD.getCodBibl().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0 + " cd_bib='" + configurazioneORD.getCodBibl() +"'";
				}
				if (configurazioneORD.getCodPolo() !=null &&  configurazioneORD.getCodPolo().length()!=0)
				{
					sql0=this.struttura(sql0);
					sql0=sql0 + " cd_polo='" + configurazioneORD.getCodPolo() +"'";
				}

				sql0=sql0  + " order by  cd_bib";

				pstmt = connection.prepareStatement(sql0);
				rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
				// numero di righe del resultset
				int numRighe=0;
				//Timestamp ts = DaoManager.now();
				while (rs.next()) {
					numRighe=numRighe+1;
					if (numRighe==1) //  solo la prima volta
					{
						rec = new ConfigurazioneORDVO();
						rec.setCodPolo(rs.getString("cd_polo"));
						rec.setCodBibl(rs.getString("cd_bib"));
						rec.setDataUpd(rs.getTimestamp("dataUpd"));
						rec.setGestioneBilancio(true);
						if (rs.getString("gest_bil").equals("N"))
						{
							rec.setGestioneBilancio(false);
						}
						rec.setGestioneSezione(true);
						if (rs.getString("gest_sez").equals("N"))
						{
							rec.setGestioneSezione(false);
						}
						rec.setGestioneProfilo(true);
						if (rs.getString("gest_prof").equals("N"))
						{
							rec.setGestioneProfilo(false);
						}

						rec.setCodiceProfilo(String.valueOf(rs.getInt("cod_prac")));
						rec.setCodiceSezione(rs.getString("cod_sezione"));
						rec.setChiaveBilancio(new StrutturaTerna("","",""));
						if (rs.getInt("esercizio")!=0)
						{
							rec.getChiaveBilancio().setCodice1(String.valueOf(rs.getInt("esercizio")));
						}
						if (rs.getInt("capitolo")!=0)
						{
							rec.getChiaveBilancio().setCodice2(String.valueOf(rs.getInt("capitolo")));
						}
						rec.getChiaveBilancio().setCodice3(rs.getString("cod_mat"));
						// il costruttore deve avere già creato la struttura e parzialmente riempita con i codici
						String[] codiciForn=new String[6];
						codiciForn[0]="";
						if (rs.getInt("cod_fornitore_a")>0)
						{
							rec.getFornitoriDefault()[0].setDescrizione(String.valueOf(rs.getInt("cod_fornitore_a")));
							codiciForn[0]=String.valueOf(rs.getInt("cod_fornitore_a"));
						}
						codiciForn[1]="";
						if (rs.getInt("cod_fornitore_l")>0)
						{
							rec.getFornitoriDefault()[1].setDescrizione(String.valueOf(rs.getInt("cod_fornitore_l")));
							codiciForn[1]=String.valueOf(rs.getInt("cod_fornitore_l"));
						}
						codiciForn[2]="";
						if (rs.getInt("cod_fornitore_d")>0)
						{
							rec.getFornitoriDefault()[2].setDescrizione(String.valueOf(rs.getInt("cod_fornitore_d")));
							codiciForn[2]=String.valueOf(rs.getInt("cod_fornitore_d"));
						}
						codiciForn[3]="";
						if (rs.getInt("cod_fornitore_v")>0)
						{
							rec.getFornitoriDefault()[3].setDescrizione(String.valueOf(rs.getInt("cod_fornitore_v")));
							codiciForn[3]=String.valueOf(rs.getInt("cod_fornitore_v"));
						}
						codiciForn[4]="";
						if (rs.getInt("cod_fornitore_c")>0)
						{
							rec.getFornitoriDefault()[4].setDescrizione(String.valueOf(rs.getInt("cod_fornitore_c")));
							codiciForn[4]=String.valueOf(rs.getInt("cod_fornitore_c"));
						}
						codiciForn[5]="";
						if (rs.getInt("cod_fornitore_r")>0)
						{
							rec.getFornitoriDefault()[5].setDescrizione(String.valueOf(rs.getInt("cod_fornitore_r")));
							codiciForn[5]=String.valueOf(rs.getInt("cod_fornitore_r"));
						}
						rec.setCodiciFornitore(codiciForn);

						rec.setOrdiniAperti(false);
						if (rs.getString("ordini_aperti").equals("Y"))
						{
							rec.setOrdiniAperti(true);
						}
						rec.setOrdiniChiusi(false);
						if  (rs.getString("ordini_chiusi").equals("Y"))
						{
							rec.setOrdiniChiusi(true);
						}
						rec.setOrdiniAnnullati(false);
						if (rs.getString("ordini_annullati").equals("Y"))
						{
							rec.setOrdiniAnnullati(true);
						}
						rec.setAllineamento(rs.getString("allineamento_prezzo"));

						//almaviva5_20121120 evolutive google
						rec.setCd_bib_google(rs.getString("cd_bib_google"));
						int cd_forn_google = rs.getInt("cd_forn_google");
						rec.setCd_forn_google(cd_forn_google > 0 ? cd_forn_google : null);
					}
				} // End while

				//
				rs.close();
				pstmt.close();
				connection.close();

	}catch (ValidationException e) {

      	  throw e;

	} catch (Exception e) {

		log.error("", e);
	} finally {
		close(connection);
	}

    return rec;
}
	public boolean  modificaConfigurazioneOrdini(ConfigurazioneORDVO configurazioneORD) throws DataException, ApplicationException , ValidationException
	{
		//Validazione.ValidaBuoniOrdineVO (buonoOrd);
		boolean valRitorno=false;
    	int motivo=0;
    	//FornitoreVO rec = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtUDP = null;
		PreparedStatement pstmtDEL = null;
		PreparedStatement pstmtINS = null;
		PreparedStatement pstmtExistOrd = null;
		ResultSet rsExistOrd= null;
		ResultSet rs = null;
		ResultSet rsSub = null;
		//ResultSet rsInsTit = null;

		boolean controlloCONGR=false;
		ResultSet rsESISTEORD = null;
		PreparedStatement pstmtESISTEORD = null;

		boolean valRitornoUPD=false;
		boolean valRitornoINSLOOP=false;
		boolean valRitornoCANC=false;
		boolean valRitornoINS=false;
		//boolean valRitornoInsTit=false;

		try {
			connection = getConnection();
			String sql0="select * ";
			sql0=sql0  + " from  tba_parametri_ordine ";

			if (configurazioneORD.getCodBibl() !=null &&  configurazioneORD.getCodBibl().length()>0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " cd_bib='" + configurazioneORD.getCodBibl() +"'";
			}
			if (configurazioneORD.getCodPolo() !=null &&  configurazioneORD.getCodPolo().length()>0)
			{
				sql0=this.struttura(sql0);
				sql0=sql0 + " cd_polo='" + configurazioneORD.getCodPolo() +"'";
			}
			sql0=sql0  + " order by  cd_bib ";
			pstmt = connection.prepareStatement(sql0);
			rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
			// numero di righe del resultset
			int numRighe=0;
			Timestamp ts = DaoManager.now();
			while (rs.next()) {
				numRighe=numRighe+1;
			}
			rs.close();
			pstmt.close();

			if (numRighe==1)
			{
				//procedere con l'operazione di modifica record

				String sqlUDP="update tba_parametri_ordine set " ;
				sqlUDP= sqlUDP  + " ts_var = '" +  ts  + "'" ;  // ex data_agg
				sqlUDP= sqlUDP + ", ute_var = '" +  configurazioneORD.getUtente()  + "'" ;  // ute_var

				if (configurazioneORD.isGestioneBilancio())
				{
					sqlUDP= sqlUDP + ", gest_bil = 'Y'" ;  // gest_bil
				}
				else
				{
					sqlUDP= sqlUDP + " ,gest_bil = 'N'" ;  // gest_bil
				}
				if (configurazioneORD.isGestioneSezione())
				{
					sqlUDP= sqlUDP + ", gest_sez = 'Y'" ;  // gest_sez
				}
				else
				{
					sqlUDP= sqlUDP + ", gest_sez = 'N'" ;  // gest_sez
				}

				if (configurazioneORD.isGestioneProfilo())
				{
					sqlUDP= sqlUDP + ", gest_prof =  'Y'" ;  // gest_prof
				}
				else
				{
					sqlUDP= sqlUDP + ", gest_prof =  'N'" ;  // gest_prof
				}
				if (configurazioneORD.getCodiceProfilo()!=null)
				{
					sqlUDP= sqlUDP + ", cod_prac = " +  configurazioneORD.getCodiceProfilo().trim()   ;  // cod_prac
				}
				if (configurazioneORD.getCodiceSezione()!=null)
				{
					sqlUDP= sqlUDP + ", cod_sezione = '" +  configurazioneORD.getCodiceSezione()  + "'" ;  // cod_sezione
				}
				if (configurazioneORD.getChiaveBilancio()!=null && configurazioneORD.getChiaveBilancio().getCodice1()!=null && configurazioneORD.getChiaveBilancio().getCodice1().length()!=0)
				{
					sqlUDP= sqlUDP + ", esercizio = " +  configurazioneORD.getChiaveBilancio().getCodice1().trim()   ;  // esercizio
				}
				if (configurazioneORD.getChiaveBilancio()!=null && configurazioneORD.getChiaveBilancio().getCodice2()!=null && configurazioneORD.getChiaveBilancio().getCodice2().length()!=0)
				{
					sqlUDP= sqlUDP + ", capitolo = " +  configurazioneORD.getChiaveBilancio().getCodice2().trim()   ;  // capitolo
				}
				if (configurazioneORD.getChiaveBilancio()!=null && configurazioneORD.getChiaveBilancio().getCodice3()!=null && configurazioneORD.getChiaveBilancio().getCodice3().length()!=0)
				{
					sqlUDP= sqlUDP + ", cod_mat = '" +  configurazioneORD.getChiaveBilancio().getCodice3().trim() + "'"  ;  // cod_mat
				}
				// carico valori assunti dalla pagina nella variabile
				String[] codiciForn=new String[6];
				for (int i=0; i<configurazioneORD.getFornitoriDefault().length; i++)
				{
					codiciForn[i]=configurazioneORD.getFornitoriDefault()[i].getDescrizione();
				}
				configurazioneORD.setCodiciFornitore(codiciForn);
				if (configurazioneORD.getCodiciFornitore()!=null && configurazioneORD.getCodiciFornitore()[0].trim().length()!=0)
				{
					sqlUDP= sqlUDP + ", cod_fornitore_a = " +  configurazioneORD.getCodiciFornitore()[0].trim()  ;  // cod_fornitore_a
				}
				if (configurazioneORD.getCodiciFornitore()!=null && configurazioneORD.getCodiciFornitore()[1].trim().length()!=0)
				{
					sqlUDP= sqlUDP + ", cod_fornitore_l = " +  configurazioneORD.getCodiciFornitore()[1].trim()  ;  // cod_fornitore_l
				}
				if (configurazioneORD.getCodiciFornitore()!=null && configurazioneORD.getCodiciFornitore()[2].trim().length()!=0)
				{
					sqlUDP= sqlUDP + ", cod_fornitore_d = " +  configurazioneORD.getCodiciFornitore()[2].trim()  ;  // cod_fornitore_d
				}
				if (configurazioneORD.getCodiciFornitore()!=null && configurazioneORD.getCodiciFornitore()[3].trim().length()!=0)
				{
					sqlUDP= sqlUDP + ", cod_fornitore_v = " +  configurazioneORD.getCodiciFornitore()[3].trim()  ;  // cod_fornitore_v
				}
				if (configurazioneORD.getCodiciFornitore()!=null && configurazioneORD.getCodiciFornitore()[4].trim().length()!=0)
				{
					sqlUDP= sqlUDP + ", cod_fornitore_c = " +  configurazioneORD.getCodiciFornitore()[4].trim()  ;  // cod_fornitore_c
				}
				if (configurazioneORD.getCodiciFornitore()!=null && configurazioneORD.getCodiciFornitore()[5].trim().length()!=0)
				{
					sqlUDP= sqlUDP + ", cod_fornitore_r = " +  configurazioneORD.getCodiciFornitore()[5].trim()  ;  // cod_fornitore_r
				}
				if (configurazioneORD.isOrdiniAperti())
				{
					sqlUDP= sqlUDP + ", ordini_aperti = 'Y'" ;  // ordini_aperti
				}
				else
				{
					sqlUDP= sqlUDP + ", ordini_aperti = 'N'" ;  // ordini_aperti
				}
				if (configurazioneORD.isOrdiniChiusi())
				{
					sqlUDP= sqlUDP + ", ordini_chiusi = 'Y'" ;  // ordini_chiusi
				}
				else
				{
					sqlUDP= sqlUDP + ", ordini_chiusi = 'N'" ;  // ordini_chiusi
				}
				if (configurazioneORD.isOrdiniAnnullati())
				{
					sqlUDP= sqlUDP + ", ordini_annullati = 'Y'" ;  // ordini_annullati
				}
				else
				{
					sqlUDP= sqlUDP + ", ordini_annullati = 'N'" ;  // ordini_annullati
				}
				sqlUDP= sqlUDP + ", allineamento_prezzo = '" +  configurazioneORD.getAllineamento()  + "'" ;  // allineamento_prezzo

				//almaviva5_20121120 evolutive google
				sqlUDP += ",cd_bib_google=" + trimAndQuote(configurazioneORD.getCd_bib_google());
				sqlUDP += ",cd_forn_google=" + (configurazioneORD.getCd_forn_google() != null ? configurazioneORD.getCd_forn_google() : "null");

				//condizione where
				if (configurazioneORD.getCodBibl() !=null &&  configurazioneORD.getCodBibl().length()!=0)
				{
					sqlUDP=this.struttura(sqlUDP);
					sqlUDP=sqlUDP + " cd_bib='" + configurazioneORD.getCodBibl() +"'";
				}
				if (configurazioneORD.getCodPolo() !=null &&  configurazioneORD.getCodPolo().length()!=0)
				{
					sqlUDP=this.struttura(sqlUDP);
					sqlUDP=sqlUDP + " cd_polo='" + configurazioneORD.getCodPolo() +"'";
				}
				if (configurazioneORD.getDataUpd()!=null )
				{
					sqlUDP=this.struttura(sqlUDP);
					sqlUDP=sqlUDP + " ts_var='" + configurazioneORD.getDataUpd() + "'" ;
				}

				pstmtUDP = connection.prepareStatement(sqlUDP);
				log.debug("Debug: modifica  configurazione ORD");
				log.debug("Debug: " + sqlUDP);
				int intRetUDP = 0;
				intRetUDP = pstmtUDP.executeUpdate();
				if (intRetUDP >0)
				{
					valRitornoUPD=true;
				}
				pstmtUDP.close();
				connection.close();
			}
			if (numRighe==0)
			{
				// inserimento
					String sqlSub2="insert into tba_parametri_ordine values ( " ;
					sqlSub2 = sqlSub2+  "'" + configurazioneORD.getCodPolo() + "'" ;  // cod_polo
					sqlSub2 = sqlSub2+  ",'" + configurazioneORD.getCodBibl() + "'" ;  // cd_bib
					if (configurazioneORD.isGestioneBilancio())
					{
						sqlSub2 = sqlSub2+  ",'Y'" ;  // gest_bil
					}
					else
					{
						sqlSub2 = sqlSub2+  ",'N'" ;  // gest_bil
					}
					if (configurazioneORD.isGestioneSezione())
					{
						sqlSub2 = sqlSub2+  ",'Y'" ;  // gest_sez
					}
					else
					{
						sqlSub2 = sqlSub2+  ",'N'" ;  // gest_sez
					}
					if (configurazioneORD.isGestioneProfilo())
					{
						sqlSub2 = sqlSub2+  ",'Y'" ; ;  // gest_prof
					}
					else
					{
						sqlSub2 = sqlSub2+  ",'N'" ; ;  // gest_prof
					}

					if (configurazioneORD.getCodiceProfilo()!=null && !configurazioneORD.getCodiceProfilo().equals("") )
					{
						sqlSub2 = sqlSub2 + "," +  configurazioneORD.getCodiceProfilo().trim()   ;  // cod_prac
					}
					else
					{
						sqlSub2 = sqlSub2 + ",null"    ;  // cod_prac
					}

					if (configurazioneORD.getCodiceSezione()!=null)
					{
						sqlSub2 = sqlSub2 + ", '" +  configurazioneORD.getCodiceSezione()  + "'" ;  // cod_sezione
					}
					else
					{
						sqlSub2 = sqlSub2 + ",null"    ;  // cod_sezione
					}

					if (configurazioneORD.getChiaveBilancio()!=null && configurazioneORD.getChiaveBilancio().getCodice1()!=null && configurazioneORD.getChiaveBilancio().getCodice1().length()!=0)
					{
						sqlSub2 = sqlSub2 + ",  " +  configurazioneORD.getChiaveBilancio().getCodice1().trim()   ;  // esercizio
					}
					else
					{
						sqlSub2 = sqlSub2 + ",null"    ;  // esercizio
					}

					if (configurazioneORD.getChiaveBilancio()!=null && configurazioneORD.getChiaveBilancio().getCodice2()!=null && configurazioneORD.getChiaveBilancio().getCodice2().length()!=0)
					{
						sqlSub2 = sqlSub2 + ",  " +  configurazioneORD.getChiaveBilancio().getCodice2().trim()   ;  // capitolo
					}
					else
					{
						sqlSub2 = sqlSub2 + ",null"    ;  // capitolo
					}
					if (configurazioneORD.getChiaveBilancio()!=null && configurazioneORD.getChiaveBilancio().getCodice3()!=null && configurazioneORD.getChiaveBilancio().getCodice3().length()!=0)
					{
						sqlSub2 = sqlSub2 + ",  '" +  configurazioneORD.getChiaveBilancio().getCodice3().trim() + "'"  ;  // cod_mat
					}
					else
					{
						sqlSub2 = sqlSub2 + ",null"    ;  // cod_mat
					}
					// carico valori assunti dalla pagina nella variabile
					String[] codiciForn=new String[6];
					for (int i=0; i<configurazioneORD.getFornitoriDefault().length; i++)
					{
						codiciForn[i]=configurazioneORD.getFornitoriDefault()[i].getDescrizione();
					}
					configurazioneORD.setCodiciFornitore(codiciForn);
					if (configurazioneORD.getCodiciFornitore()!=null && configurazioneORD.getCodiciFornitore()[0]!=null && configurazioneORD.getCodiciFornitore()[0].trim().length()!=0)
					{
						sqlSub2 = sqlSub2 + ",  " +  configurazioneORD.getCodiciFornitore()[0].trim()  ;  // cod_fornitore_a
					}
					else
					{
						sqlSub2 = sqlSub2 + ", null "  ;
					}

					if (configurazioneORD.getCodiciFornitore()!=null && configurazioneORD.getCodiciFornitore()[1]!=null && configurazioneORD.getCodiciFornitore()[1].trim().length()!=0)
					{
						sqlSub2 = sqlSub2 + ",  " +  configurazioneORD.getCodiciFornitore()[1].trim()  ;  // cod_fornitore_l
					}
					else
					{
						sqlSub2 = sqlSub2 + ", null "  ;
					}

					if (configurazioneORD.getCodiciFornitore()!=null && configurazioneORD.getCodiciFornitore()[2]!=null && configurazioneORD.getCodiciFornitore()[2].trim().length()!=0)
					{
						sqlSub2 = sqlSub2 + ",  " +  configurazioneORD.getCodiciFornitore()[2].trim()  ;  // cod_fornitore_d
					}
					else
					{
						sqlSub2 = sqlSub2 + ", null "  ;
					}

					if (configurazioneORD.getCodiciFornitore()!=null && configurazioneORD.getCodiciFornitore()[3]!=null && configurazioneORD.getCodiciFornitore()[3].trim().length()!=0)
					{
						sqlSub2 = sqlSub2 + ",  " +  configurazioneORD.getCodiciFornitore()[3].trim()  ;  // cod_fornitore_v
					}
					else
					{
						sqlSub2 = sqlSub2 + ", null "  ;
					}

					if (configurazioneORD.getCodiciFornitore()!=null && configurazioneORD.getCodiciFornitore()[4]!=null && configurazioneORD.getCodiciFornitore()[4].trim().length()!=0)
					{
						sqlSub2 = sqlSub2 + ",  " +  configurazioneORD.getCodiciFornitore()[4].trim()  ;  // cod_fornitore_c
					}
					else
					{
						sqlSub2 = sqlSub2 + ", null "  ;
					}

					if (configurazioneORD.getCodiciFornitore()!=null && configurazioneORD.getCodiciFornitore()[5]!=null && configurazioneORD.getCodiciFornitore()[5].trim().length()!=0)
					{
						sqlSub2 = sqlSub2 + ",  " +  configurazioneORD.getCodiciFornitore()[5].trim()  ;  // cod_fornitore_r
					}
					else
					{
						sqlSub2 = sqlSub2 + ", null "  ;
					}

					if (configurazioneORD.isOrdiniAperti())
					{
						sqlSub2 = sqlSub2 + ", 'Y'" ;  // ordini_aperti
					}
					else
					{
						sqlSub2 = sqlSub2 + ", 'N'" ;  // ordini_aperti
					}
					if (configurazioneORD.isOrdiniChiusi())
					{
						sqlSub2 = sqlSub2+ ", 'Y'" ;  // ordini_chiusi
					}
					else
					{
						sqlSub2 = sqlSub2 + ", 'N'" ;  // ordini_chiusi
					}
					if (configurazioneORD.isOrdiniAnnullati())
					{
						sqlSub2 = sqlSub2 + ",  'Y'" ;  // ordini_annullati
					}
					else
					{
						sqlSub2 = sqlSub2 + ", 'N'" ;  // ordini_annullati
					}
					sqlSub2 = sqlSub2 + ",  '" +  configurazioneORD.getAllineamento()  + "'" ;  // allineamento_prezzo
					sqlSub2=sqlSub2 + ", '" + configurazioneORD.getUtente() + "'" ;   // ute_ins
					sqlSub2=sqlSub2 + ", '" + ts + "'" ;   // ts_ins
					sqlSub2=sqlSub2 + ", '" + configurazioneORD.getUtente() + "'" ;   // ute_var
					sqlSub2=sqlSub2 + ", '" + ts + "'";   // ts_var
					sqlSub2=sqlSub2 + ", 'N'";   // fl_canc

					//almaviva5_20121120 evolutive google
					sqlSub2 += "," + trimAndQuote(configurazioneORD.getCd_bib_google());
					sqlSub2 += "," + (configurazioneORD.getCd_forn_google() != null ? configurazioneORD.getCd_forn_google() : "null");

					sqlSub2 = sqlSub2+ ")" ;
					pstmtINS = connection.prepareStatement(sqlSub2);
					log.debug("Debug: modifica  configurazione ORD");
					log.debug("Debug: " + sqlSub2);

					int intRetINS = 0;
					intRetINS = pstmtINS.executeUpdate();
					pstmtINS.close();
					if (intRetINS==1){
						valRitornoINS=true;
					}
			}
			connection.close();

			valRitorno=true;
			if (!valRitornoINS && numRighe==0)
			 {
				 valRitorno=false;
			 }
			 if (!valRitornoUPD && numRighe==1)
			 {
				 valRitorno=false;
			 }
		} catch (Exception e) {

			log.error("", e);
		} finally {
			close(connection);
		}

	    return valRitorno;
	}


	public boolean  cancellaCambioHib(CambioVO cambio) throws DataException, ApplicationException
	{
		// List<SezioniVO>
        boolean valRitorno=false;
    	int motivo=0;

		try {

			// Trova descrizione delle sessioni
			try {
				Session session = HibernateUtil.getSession();
				//session.getTransaction().begin();
				Criteria cr = session.createCriteria(Tba_cambi_ufficiali.class);

				if (cambio.getCodBibl()!=null && cambio.getCodBibl().length()!=0)
				{
					cr.add(Restrictions.eq("cd_bib", cambio.getCodBibl()));
				}

					if (cambio.getCodValuta()!=null && cambio.getCodValuta().length()!=0)
				{
					cr.add(Restrictions.eq("valuta", cambio.getCodValuta().trim().toUpperCase()));
				}


				List<Tba_cambi_ufficiali> results = cr.list();
				Tba_cambi_ufficiali cu;
				// se esiste l'elemento
				if (results.size()==1)
				{
		    		cu = results.get(0);					// DATA di sistema
					Timestamp ts = DaoManager.now();
					//Transaction tx=session.beginTransaction();

					BigDecimal bd = new BigDecimal(1936.27);

//					 modificato il 06.04.09
					cu.setTs_ins(ts);
					cu.setTs_var(ts);
					cu.setCd_bib(DaoManager.creaIdBib(cambio.getCodPolo(), cambio.getCodBibl()) );
					cu.setValuta(cambio.getCodValuta().trim().toUpperCase());
					//cu.setCambio(bd);
					cu.setCambio(BigDecimal.valueOf(cambio.getTassoCambio()));
					cu.setData_var(ts);
					session.delete(cu);
					//session.getTransaction().commit();
					session.flush();
					HibernateUtil.closeSession();
					valRitorno=true;
				}
				else
				{
					motivo=2; // record non univoco o non esistente
				}
			} catch (InfrastructureException e) {

				log.error("", e);
				try {
					HibernateUtil.closeSession();
				} catch (InfrastructureException ex) {
					ex.printStackTrace();
				}
			}


// END almaviva TEST

		} catch (Exception e) {

			log.error("", e);
		}
        return valRitorno;
	}

	public boolean  modificaOrdiniXFusione(OrdiniVO ordine) throws DataException, ApplicationException , ValidationException
	{
		boolean valRitorno=false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Timestamp ts = DaoManager.now();

		try {
			connection = getConnection();

				String sqlUDP="update tba_ordini set " ;
				sqlUDP= sqlUDP + " ts_var = '" +  ts  + "'" ;  // ex data_agg
				sqlUDP= sqlUDP + ", ute_var = '" +  ordine.getUtente()  + "'" ;  // ute_var
				sqlUDP= sqlUDP + ", bid='" + ordine.getTitolo().getCodice() +"'";

				sqlUDP=this.struttura(sqlUDP);
				sqlUDP=sqlUDP + " fl_canc<>'S'";

				if (ordine.getCodPolo()!=null && ordine.getCodPolo().length()!=0 )
				{
					sqlUDP=this.struttura(sqlUDP);
					sqlUDP=sqlUDP + " cd_polo='" + ordine.getCodPolo() +"'";
				}

				if (ordine.getCodBibl()!=null && ordine.getCodBibl().length()!=0 )
				{
					sqlUDP=this.struttura(sqlUDP);
					sqlUDP=sqlUDP  + " cd_bib='" + ordine.getCodBibl() +"'";
				}

				if (ordine.getCodOrdine()!=null && ordine.getCodOrdine().length()!=0 )
				{
					sqlUDP=this.struttura(sqlUDP);
					sqlUDP=sqlUDP  + " cod_ord=" + ordine.getCodOrdine() ;
				}
				if (ordine.getTipoOrdine()!=null && ordine.getTipoOrdine().length()!=0 )
				{
					sqlUDP=this.struttura(sqlUDP);
					sqlUDP=sqlUDP + " cod_tip_ord='" + ordine.getTipoOrdine() +"'";
				}

				if (ordine.getAnnoOrdine()!=null && ordine.getAnnoOrdine().length()!=0)
				{
					sqlUDP=this.struttura(sqlUDP);
					sqlUDP=sqlUDP + " anno_ord = " +  ordine.getAnnoOrdine() ;
				}
				pstmt = connection.prepareStatement(sqlUDP);
				log.debug("Debug: modifica ordine per fusione");
				log.debug("Debug: " + sqlUDP);

				int intRetUDP = 0;
				intRetUDP = pstmt.executeUpdate();
				pstmt.close();
				// fine estrazione codice
				if (intRetUDP==1){
					valRitorno=true;
				}
				connection.close();

		} catch (Exception e) {

			log.error("", e);
		} finally {
			close(connection);
		}
        return valRitorno;
	}

	public boolean  modificaGaraXFusione(GaraVO gara) throws DataException, ApplicationException , ValidationException
	{
		boolean valRitorno=false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Timestamp ts = DaoManager.now();

		try {
			connection = getConnection();

			String sqlUDP="update tba_richieste_offerta set " ;
			sqlUDP= sqlUDP + " ts_var = '" +  ts  + "'" ;  // ts_var
			sqlUDP= sqlUDP + ", ute_var = '" +  gara.getUtente()  + "'" ;  // ute_var
			sqlUDP= sqlUDP + ", bid ='"+gara.getBid().getCodice() + "'" ; // bid

			sqlUDP=this.struttura(sqlUDP);
			sqlUDP=sqlUDP   + " fl_canc<>'S'";

			if (gara.getCodPolo() !=null &&  gara.getCodPolo().length()!=0)
			{
				sqlUDP=this.struttura(sqlUDP);
				sqlUDP=sqlUDP   + " cd_polo='" + gara.getCodPolo() +"'";
			}

			if (gara.getCodBibl() !=null &&  gara.getCodBibl().length()!=0)
			{
				sqlUDP=this.struttura(sqlUDP);
				sqlUDP=sqlUDP   + " cd_bib='" + gara.getCodBibl() +"'";
			}
			if (gara.getCodRicOfferta()!=null && gara.getCodRicOfferta().trim().length()!=0)
			{
				sqlUDP=this.struttura(sqlUDP);
				sqlUDP=sqlUDP   + " cod_rich_off=" + gara.getCodRicOfferta() ;
			}

			pstmt = connection.prepareStatement(sqlUDP);
			log.debug("Debug: modifica gara per fusione");
			log.debug("Debug: " + sqlUDP);

			int intRetUDP = 0;
			intRetUDP = pstmt.executeUpdate();
			pstmt.close();
			// fine estrazione codice
			if (intRetUDP==1){
				valRitorno=true;
			}
			connection.close();

		} catch (Exception e) {

			log.error("", e);
		} finally {
			close(connection);
		}
        return valRitorno;
	}

	public boolean  modificaSuggerimentoXFusione(SuggerimentoVO suggerimento) throws DataException, ApplicationException , ValidationException
	{
		boolean valRitorno=false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Timestamp ts = DaoManager.now();

		try {
			connection = getConnection();

			String sqlUDP="update tba_suggerimenti_bibliografici set " ;
			sqlUDP= sqlUDP + " ts_var = '" +  ts  + "'" ;  // data_agg
			sqlUDP= sqlUDP + ", ute_var = '" +  suggerimento.getUtente()  + "'" ;  // ute_var

			if (suggerimento.getTitolo()!=null && suggerimento.getTitolo().getCodice()!=null && suggerimento.getTitolo().getCodice().length()!=0)
			{
				sqlUDP += ", bid= '" +  suggerimento.getTitolo().getCodice()  + "'" ;
			}
			sqlUDP=sqlUDP + " where fl_canc<>'S'";

			if (suggerimento.getCodPolo()!=null &&  suggerimento.getCodPolo().length()!=0)
			{
				sqlUDP=this.struttura(sqlUDP);
				sqlUDP=sqlUDP + " cd_polo='" + suggerimento.getCodPolo() +"'";
			}

			if (suggerimento.getCodBibl()!=null &&  suggerimento.getCodBibl().length()!=0)
			{
				sqlUDP=this.struttura(sqlUDP);
				sqlUDP=sqlUDP + " cd_bib='" + suggerimento.getCodBibl() +"'";
			}
			if (suggerimento.getCodiceSuggerimento()!=null && suggerimento.getCodiceSuggerimento().length()!=0 )
			{
				sqlUDP=this.struttura(sqlUDP);
				sqlUDP=sqlUDP + " cod_sugg_bibl=" + suggerimento.getCodiceSuggerimento() ;
			}
			pstmt = connection.prepareStatement(sqlUDP);
			log.debug("Debug: modifica suggerimento bibl. per fusione");
			log.debug("Debug: " + sqlUDP);

			int intRetUDP = 0;
			intRetUDP = pstmt.executeUpdate();
			pstmt.close();
			// fine estrazione codice
			if (intRetUDP==1){
				valRitorno=true;
			}
			connection.close();

		} catch (Exception e) {

			log.error("", e);
		} finally {
			close(connection);
		}
        return valRitorno;
	}

	public boolean  modificaDocumentoXFusione(DocumentoVO documento) throws DataException, ApplicationException , ValidationException
	{
		boolean valRitorno=false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Timestamp ts = DaoManager.now();

		try {
			connection = getConnection();

			String sqlUDP="update tbl_documenti_lettori set " ;
			sqlUDP= sqlUDP + " ts_var = '" +  ts  + "'" ;  // ts_var
			sqlUDP= sqlUDP + ", ute_var = '" +  documento.getUtenteCod()  + "'" ;  // ute_var

			if (documento.getTitolo()!=null && documento.getTitolo().getCodice()!=null && documento.getTitolo().getCodice().length()!=0)
			{
				sqlUDP += ", bid= '" +  documento.getTitolo().getCodice()  + "'" ;
			}
			// CONDIZIONI
			sqlUDP=sqlUDP + " where tipo_doc_lett='S'";
			sqlUDP=sqlUDP + " and fl_canc<>'S' ";

			if (documento.getCodPolo() !=null &&  documento.getCodPolo().length()!=0)
			{
				sqlUDP=this.struttura(sqlUDP);
				sqlUDP=sqlUDP + " cd_polo='" + documento.getCodPolo() +"'";
			}

			if (documento.getCodBibl() !=null &&  documento.getCodBibl().length()!=0)
			{
				sqlUDP=this.struttura(sqlUDP);
				sqlUDP=sqlUDP  + " cd_bib='" + documento.getCodBibl() +"'";
			}
			if (documento.getCodDocumento()!=null && documento.getCodDocumento().length()!=0 )
			{
				sqlUDP=this.struttura(sqlUDP);
				sqlUDP=sqlUDP  + " cod_doc_lett=" + documento.getCodDocumento().trim() ;
			}

			pstmt = connection.prepareStatement(sqlUDP);
			log.debug("Debug: modifica  documento per fusione");
			log.debug("Debug: " + sqlUDP);

			int intRetUDP = 0;
			intRetUDP = pstmt.executeUpdate();
			pstmt.close();
			// fine estrazione codice
			if (intRetUDP==1){
				valRitorno=true;
			}
			connection.close();

		} catch (Exception e) {

			log.error("", e);
		} finally {
			close(connection);
		}
        return valRitorno;
	}


	public void  migrazioneCStoSBNWEBcateneRinnoviBis() throws DataException, ApplicationException , ValidationException
	{
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		PreparedStatement pstmtUpd = null;

		PreparedStatement pstmtCerca = null;
		ResultSet rsCerca = null;

		PreparedStatement pstmtTest = null;
		ResultSet rsTest = null;

		try
		{
			Boolean procedi=false;
			connection = getConnection();
			// costruzione elenco id dei primi ordini
			String sql="select ord_id,ord_cod_tip,ord_anno,ord_cod from a_catene_rinnovi ";
			sql +=" where fl_primo_rinn=1 ";
			sql +=" order by cd_polo,cd_bib , ord_bid, ord_anno_abb, rin_anno, rin_cod ";


			pstmt = connection.prepareStatement(sql);

			rs = pstmt.executeQuery();
			int numRighe=0;
			String chiaveRottura="";
			String confronto="";
			List catenaRinnovi;
			StrutturaQuater eleCatenaRinnovi=new StrutturaQuater("", "", "", "");

			StrutturaTerna primoOrdine=new StrutturaTerna("", "", "");
			StrutturaQuater precedente=new StrutturaQuater("", "", "", "");
			String catenaID="";
			boolean inizioCatena=false;
			boolean catenaOK=true;
			int numeroCatene=0;
			int totaleOrdiniRinnovatiMultipli=0;
			while (rs.next()) {
				catenaRinnovi=new ArrayList();
				eleCatenaRinnovi.setCodice1(rs.getString("ord_id"));
				eleCatenaRinnovi.setCodice2(rs.getString("ord_cod_tip"));
				eleCatenaRinnovi.setCodice3(rs.getString("ord_anno"));
				eleCatenaRinnovi.setCodice4(rs.getString("ord_cod"));
				catenaRinnovi.add(eleCatenaRinnovi);
                for (int i=0; i<catenaRinnovi.size(); i++ )
                {
                	eleCatenaRinnovi=(StrutturaQuater)catenaRinnovi.get(i);
                	String sqlcerca="select ord_id,ord_cod_tip,ord_anno,ord_cod from a_catene_rinnovi ";
    				sqlcerca=sqlcerca + " where (ord_cod_tip,rin_anno,rin_cod) = ('" + eleCatenaRinnovi.getCodice2()+"'," + eleCatenaRinnovi.getCodice3() + "," + eleCatenaRinnovi.getCodice4()+")";
    				pstmtCerca = connection.prepareStatement(sqlcerca);
    				rsCerca = pstmtCerca.executeQuery();
    				numRighe=0;
    				while (rsCerca.next()) {
    					numRighe=numRighe+1;
    					precedente=new StrutturaQuater("", "", "", "");
    					precedente.setCodice1(rsCerca.getString("ord_id"));
    					precedente.setCodice2(rsCerca.getString("ord_cod_tip"));
    					precedente.setCodice3(rsCerca.getString("ord_anno"));
    					precedente.setCodice4(rsCerca.getString("ord_cod"));
    				}
    				if (numRighe==1)
    				{
    					List catenaRinnoviNew=new ArrayList(catenaRinnovi);
    					catenaRinnoviNew.add(precedente);
    					catenaRinnovi=new ArrayList(catenaRinnoviNew);
    					//catenaRinnovi.add(i,precedente);
    					//catenaRinnovi.add(precedente);

    				}
    				else
    				{
        				if (numRighe>1)
        				{
        					totaleOrdiniRinnovatiMultipli=totaleOrdiniRinnovatiMultipli+1;
        					log.error("ERRORE SU rinnovati di ORDINE con n  SUCCESSIVI per id="+ eleCatenaRinnovi.getCodice1());
        				}

    				}

    				rsCerca.close();
    				pstmtCerca.close();
                }
                //fare update su tutta la catena di rinnovi con esclusione del primo ordine
                for (int i=1; i<catenaRinnovi.size(); i++ )
                {
                	if (i>1)
                	{
                		catenaID=catenaID +",";
                	}
                	catenaID=catenaID + ((StrutturaQuater)catenaRinnovi.get(i)).getCodice1();
                }
            	if (!catenaID.equals(""))
            	{
                	String sqlTest="select count(*) as tot from sbnweb.a_catene_rinnovi where (pri_anno is not null or pri_cod is not null) and ord_id in ("+catenaID +")";
    				pstmtTest = connection.prepareStatement(sqlTest);
    				rsTest = pstmtTest.executeQuery();
    				int totConflitti=0;
    				if (rsTest.next()) {
    					totConflitti=rsTest.getInt("tot");
    				}
    				rsTest.close();
    				pstmtTest.close();
                    if (totConflitti==0)
                    {
                		String queryUpdate="update a_catene_rinnovi set pri_anno="+ ((StrutturaQuater)catenaRinnovi.get(0)).getCodice3() +", pri_cod="+  ((StrutturaQuater)catenaRinnovi.get(0)).getCodice4()+" where ord_id in ("+catenaID +")";
                        queryUpdate=queryUpdate + " and pri_anno is null and pri_cod is null "; // aggiunta per evitare sovrascritture
                        log.debug(queryUpdate);
                        numeroCatene=numeroCatene+1;
                        pstmtUpd = connection.prepareStatement(queryUpdate);
            			int intRetUDP = 0;
            			intRetUDP = pstmtUpd.executeUpdate();
            			pstmtUpd.close();
            			//scrittura su tba_ordini
                		String queryUpdateOrd="update tba_ordini set anno_1ord="+ ((StrutturaQuater)catenaRinnovi.get(0)).getCodice3() +", cod_1ord="+  ((StrutturaQuater)catenaRinnovi.get(0)).getCodice4()+" where id_ordine in ("+catenaID +")";
                        log.debug(queryUpdateOrd);
            			pstmtUpd = connection.prepareStatement(queryUpdateOrd);
            			int intRetUDP2 = 0;
            			intRetUDP2 = pstmtUpd.executeUpdate();
            			pstmtUpd.close();

                    }
                    else
                    {
                        log.error("ERRORE SOVRASCRITTURA catena interrotta primo ordine: anno="+ ((StrutturaQuater)catenaRinnovi.get(0)).getCodice3() +"  cod="+  (catenaRinnovi.get(0)));
                    }

            	}
                catenaID="";
 			}
			log.debug("totale catene ="+String.valueOf(numeroCatene));
			log.error("totale err ordini rinnovati multipli ="+String.valueOf(totaleOrdiniRinnovatiMultipli));

			rs.close();
			pstmt.close();
			connection.close();
		} catch (SQLException se) {
			throw new ValidationException("cambierroreInserimentoTasso",
					ValidationExceptionCodici.cambierroreInserimentoTasso);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			close(connection);
		}
		return ;
	}

	public ParametriExcelVO  letturaStatistiche(ParametriExcelVO passato) throws Exception
	{
		ParametriExcelVO output=null;
		Session session = HibernateUtil.getSession(); // PER SQL E HIB

		if(passato.getIdREc()>0)
		{
			try {
				Criteria cr = session.createCriteria(Tb_statistiche.class);
				cr.add(Restrictions.eq("id_stat", passato.getIdREc()));
				List list = cr.list();

				if (list.size()==1)
				{
					Tb_statistiche oggSTAT = (Tb_statistiche)list.get(0);
					String valore=oggSTAT.getQuery_str();
					if (valore!=null && valore.trim().length()>0)
					{
						output= new ParametriExcelVO();
						output.setQuerySintassi(valore);
					}
				}

			} catch (Exception e) {
				log.error("", e);
			}
		}
		return output;
	}
	public static String VisualizzaImporto(Double importo) throws Exception
	{

	    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
	    dfs.setGroupingSeparator('.');
	    dfs.setDecimalSeparator(',');

	    dfs.setDigit('#');
	    dfs.setZeroDigit('0');
	    //Locale locale=new Locale();
	   // controllo formattazione con virgola separatore dei decimali
	    try {
	    	 DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
	    	 // importo
	    	String stringa  = df.format(importo);

	    	return stringa;
		} catch (Exception e) {
		    	log.error("", e);
		    	throw new ValidationException("importoErrato",
	        			ValidationExceptionCodici.importoErrato);
		}
	}

}
// End GenericJDCBDocumentoFisicoDAO
