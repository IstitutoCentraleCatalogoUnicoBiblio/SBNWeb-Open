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

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.OperazioneSuOrdiniMassivaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SchedaInventarioInputVO;
import it.iccu.sbn.periodici.vo.SchedaAbbonamentoVO;
import it.iccu.sbn.periodici.vo.SchedaInventarioVO;
import it.iccu.sbn.periodici.vo.SchedaPeriodicoVO;
import it.iccu.sbn.periodici.vo.SchedaSintesiVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.hibernate.exception.DataException;

public class Picos extends AcquisizioniBaseDAO {

	static Logger log = Logger.getLogger(Picos.class);

	public List<SchedaPeriodicoVO> picosPeriodici(String kbibl,
			String kordi, String kanno) throws DataException,
			ApplicationException, ValidationException {

		List<SchedaPeriodicoVO> arrScheda = new ArrayList<SchedaPeriodicoVO>();

		boolean valRitorno=false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// formattazione del codice biblioteca nel formato di 3 caratteri con aggiunta di spazi in testa
	 	if (kbibl!=null && kbibl.trim().length()>0)
	 	{
	 		if (kbibl.length()<3)
	 		{
	 			String cbibl="   " + kbibl;
	 			int posizStart = cbibl.length()-3;
	 			cbibl=cbibl.substring(posizStart,posizStart+3);
	 			//String cbibl=kbibl.substring(kbibl.length()-3, kbibl.length());
		 		kbibl=cbibl;
	 		}
	 	}
		// formattazione dell'anno se arriva troncato
	 	if (kanno!=null && kanno.trim().length()==2)
	 	{
			String canno="20"+kanno;
	 		kanno=canno;
	 	}
		// kordi= deve essere tipo ordine+3chr dell'anno + poi le cifre significative dell'ordine (ricondotte a 6 posizioni) es 2007 A 5984 deve diventare A007005984
	 	//scomposizione
		String tipoOrdNew="";
		int annoOrdNew=0;
		int numOrdNew=0;
		String annoOrdNewStr="";
		String numOrdNewStr="";

		if (kordi!=null && kordi.trim().length()>0)
	 	{
	 		tipoOrdNew=kordi.substring(0,1);
	 		annoOrdNewStr=kordi.substring(1,4);
	 		if (annoOrdNewStr.substring(0,1).equals("0"))
	 		{
	 			annoOrdNewStr="2"+annoOrdNewStr;
	 		}
	 		else
	 		{
	 			annoOrdNewStr="1"+annoOrdNewStr;
	 		}
	 		annoOrdNew=Integer.parseInt(annoOrdNewStr);

	 		numOrdNewStr=kordi.substring(4, kordi.length());
	 		numOrdNew=Integer.parseInt(numOrdNewStr);
	 	}

		try {

				connection = getConnection();

				String sql="select  " ;

				if (kanno!=null && kanno.trim().length()>0 )
				{
					sql += " inv.cd_bib,inv.bid as invbid, inv.cd_serie, inv.cd_inven, inv.cd_mat_inv, inv.tipo_acquisizione, inv.valore, inv.cd_frui " ;
					sql += ", inv.data_ingresso, inv.precis_inv, inv.anno_abb, inv.seq_coll, coll.cd_biblioteca_sezione, coll.cd_sez, coll.cd_loc ";
					sql += ", coll.spec_loc, ese.cons_doc, ord.cd_bib as ordcdbib, ord.anno_ord, ord.cod_tip_ord, ord.cod_ord,ord.cod_fornitore,  ord.anno_1ord ";
					//sql += ",  forn.nom_fornitore ";
					sql += ", ord.cod_1ord, ord.anno_abb as ordannoabb, ";
				}
				sql += " tit.bid as titbid, tit.cd_natura, tit.cd_genere_1, tit.cd_lingua_1, tit.cd_paese, tit.tp_aa_pubb ";
				sql += ", tit.aa_pubb_1, tit.aa_pubb_2, tit.cd_periodicita, tit.isbd, tit.ky_cles1_t, tit.ky_cles2_t, nstd.numero_std ";
				sql += " from tba_ordini ord ";
				if (kanno!=null && kanno.trim().length()>0 )
				{
					sql += " inner join tbc_inventario inv           on inv.cd_bib_ord = ord.cd_bib ";
					sql += "                                         and inv.cd_polo    = ord.cd_polo ";
					sql += "                                         and inv.anno_ord   = ord.anno_ord ";
					sql += "    			 		                 and inv.cd_tip_ord = ord.cod_tip_ord ";
					sql += "                                         and inv.cd_ord     = ord.cod_ord ";
					if (kanno!=null && kanno.trim().length()>0 )
					{
						//sql += "                                         and inv.anno_abb   = " + Integer.valueOf("20"+kanno);
						sql += "                                         and inv.anno_abb   = ?" ;

					}
					sql += "                                         and inv.fl_canc   <> 'S' ";
					sql += " left outer join tbc_collocazione    coll on coll.key_loc   = inv.key_loc ";
					sql += "                                         and coll.fl_canc  <> 'S' ";
					sql += " left outer join tbc_esemplare_titolo ese on ese.bid        = coll.bid_doc";
					sql += "                                         and ese.cd_doc     = coll.cd_doc ";
					sql += "                                         and ese.cd_biblioteca  = coll.cd_biblioteca_doc ";
					sql += "                                         and ese.cd_polo    = coll.cd_polo_doc ";
					sql += "                                         and ese.fl_canc   <>'S' ";
					//sql += " left outer join tbr_fornitori forn       on forn.cod_fornitore = ord.cod_fornitore ";
					//sql += "                                         and forn.fl_canc  <> 'S' ";
					sql += " inner join tb_titolo tit                 on tit.bid        = inv.bid";
				}
				else
				{
					sql += " inner join tb_titolo tit                 on tit.bid        = ord.bid";
				}
				sql += "                                         and tit.fl_canc<>'S' ";
				sql += " left outer join tb_numero_std nstd       on nstd.bid=tit.bid ";
				sql += "                                         and nstd.tp_numero_std='J'  ";
				sql += "                                         and nstd.fl_canc<>'S' ";
				sql += " where ord.cd_polo    = (select cd_polo from tbf_polo)";
				sql += "   and ord.cd_bib     = ?";
				sql += "   and ((ord.anno_ord =? and ord.cod_ord = ?) or ";
				sql += "   (ord.anno_1ord= ? and ord.cod_1ord=?))";
				sql += "   and ord.cod_tip_ord = ?";
				sql += "   and ord.fl_canc<>'S'";

				pstmt = connection.prepareStatement(sql);
				int i=1;
				if (kanno!=null && kanno.trim().length()>0 )
				{
					pstmt.setInt(i, Integer.valueOf(kanno));
					i++;
				}
				pstmt.setString(i, kbibl);
				i++;

				//pstmt.setInt(i, Integer.valueOf("20" + kordi.substring(0,2)));  // anno ord
				pstmt.setInt(i, Integer.valueOf(annoOrdNewStr));  // anno ord
				i++;

				pstmt.setInt(i, Integer.valueOf(numOrdNewStr)); //cod ord
				i++;

				pstmt.setInt(i, Integer.valueOf(annoOrdNewStr));  // anno ord
				i++;

				pstmt.setInt(i, Integer.valueOf(numOrdNewStr)); //cod ord
				i++;

				pstmt.setString(i, tipoOrdNew);  //cod_tip_ord

				i++;

				rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
				int numrig=0;
				log.debug("Debug: xabb");
				log.debug("Debug: " + sql);

				while (rs.next()) {
						numrig=numrig +1;
					 	SchedaPeriodicoVO scheda=new SchedaPeriodicoVO();

						if (rs.getString("titbid") != null)
					 	{
						 	scheda.setKnide(rs.getString("titbid"));
					 	}
						if (kanno!=null && kanno.trim().length()>0 )
						{
							if (rs.getString("invbid") != null)
						 	{
							 	scheda.setBidsbn(rs.getString("invbid"));
						 	}
						}
						else
						{
							if (rs.getString("titbid") != null)
						 	{
							 	scheda.setBidsbn(rs.getString("titbid"));
						 	}
						}

					 	if (rs.getString("cd_natura") != null)
					 	{
							scheda.setNatura(rs.getString("cd_natura"));
					 	}
					 	if (rs.getString("numero_std") != null)
					 	{
							scheda.setIssn(rs.getString("numero_std"));
					 	}

					 	if (rs.getString("cd_lingua_1") != null)
					 	{
							scheda.setLingua(rs.getString("cd_lingua_1"));
					 	}

					 	if (rs.getString("cd_paese") != null)
					 	{
							scheda.setPaese(rs.getString("cd_paese"));
					 	}

					 	if (rs.getString("tp_aa_pubb") != null)
					 	{
							scheda.setTipod(rs.getString("tp_aa_pubb"));
					 	}

					 	//almaviva5_20150209
					 	String data1 = rs.getString("aa_pubb_1");
					 	scheda.setAnno(data1);
					 	String data2 = rs.getString("aa_pubb_2");
						scheda.setAnno2(data2);

					 	if (rs.getString("cd_genere_1") != null)
					 	{
							scheda.setGenere(rs.getString("cd_genere_1").trim());
					 	}

					 	if (rs.getString("cd_periodicita") != null)
					 	{
							scheda.setPeriodic(rs.getString("cd_periodicita").trim());
					 	}

					 	if (rs.getString("isbd") != null)
					 	{
							scheda.setIsbd(rs.getString("isbd"));
					 	}

						String appo_cles="";
					 	String appo_ky_cles1_t="";
						String appo_ky_cles2_t="";
					 	if (rs.getString("ky_cles1_t") != null)
					 	{
					 		appo_ky_cles1_t=rs.getString("ky_cles1_t");
					 		appo_cles=appo_cles+appo_ky_cles1_t;
					 	}
					 	if (rs.getString("ky_cles2_t") != null )
					 	{
					 		appo_ky_cles2_t=rs.getString("ky_cles2_t");
					 		appo_cles=appo_cles+appo_ky_cles2_t;
					 	}
				 		scheda.setCles(appo_cles.trim());

						scheda.setSchedaInventario(new ArrayList<SchedaInventarioVO>());
						scheda.setSchedaSintesi(new ArrayList<SchedaSintesiVO>());
						scheda.setSchedaAbbonamento(new ArrayList<SchedaAbbonamentoVO>());

				 		if (kanno!=null && kanno.trim().length()>0 )
						{
					 		// SCHEDA INVENTARIO
							//scheda.setSchedaInventgario(new ArrayList<SchedaInventarioVO>());

				 			SchedaInventarioVO schedaInventario=new SchedaInventarioVO();
						 	if (rs.getString("cd_bib") != null )
						 	{
						 		schedaInventario.setKbibl(rs.getString("cd_bib").trim());
						 	}

							String appo_ninvent="";
						 	String appo_cd_serie="";
							String appo_cd_inven="";
						 	if (rs.getString("cd_serie") != null  )
						 	{
						 		appo_cd_serie=rs.getString("cd_serie").trim();
								appo_ninvent=appo_ninvent + appo_cd_serie ;
						 	}
						 	if (rs.getString("cd_inven") != null )
						 	{
						 		appo_cd_inven=rs.getString("cd_inven");
								appo_ninvent=appo_ninvent + appo_cd_inven ;
						 	}
							//schedaInventario.setNinvent(appo_ninvent);
							schedaInventario.setNinvent(appo_cd_inven); //20.10.2010
							schedaInventario.setKserie(appo_cd_serie); //20.10.2010
							if (rs.getString("cd_sez") != null)
						 	{
								schedaInventario.setSezione((rs.getString("cd_bib").trim() + rs.getString("cd_sez")).trim());
						 	}

						 	if (rs.getString("cd_loc") != null)
						 	{
								schedaInventario.setColloc(rs.getString("cd_loc").trim());
						 	}

						 	if (rs.getString("spec_loc") != null)
						 	{
								schedaInventario.setSpecific(rs.getString("spec_loc").trim());
						 	}

						 	if (rs.getString("seq_coll") != null)
						 	{
								schedaInventario.setSequenza(rs.getString("seq_coll").trim());
						 	}

							SimpleDateFormat format1 = new SimpleDateFormat("yyMMdd");

							if (rs.getString("data_ingresso") != null)
						 	{
								String dataFormattata = rs.getString("data_ingresso");
								try {
									dataFormattata = format1.format(rs.getDate("data_ingresso"));
								} catch (Exception e) {
									log.error("", e);
								}
						 		schedaInventario.setData(dataFormattata);
						 	}

						 	if (rs.getString("tipo_acquisizione") != null)
						 	{
								schedaInventario.setTipoprov(rs.getString("tipo_acquisizione"));
						 	}

						 	if (rs.getString("cd_mat_inv") != null)
						 	{
								schedaInventario.setTipomat(rs.getString("cd_mat_inv"));
						 	}

						 	if (rs.getString("cd_frui") != null)
						 	{
								schedaInventario.setTipocirc(rs.getString("cd_frui").trim());
						 	}

						 	if (rs.getString("precis_inv") != null)
						 	{
								schedaInventario.setPrecis(rs.getString("precis_inv"));
						 	}

						 	String valoreStr="";
						 	if (rs.getString("valore") != null)
						 	{
							    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
							    dfs.setGroupingSeparator(',');
							    dfs.setDecimalSeparator('.');
							    // controllo formattazione con virgola separatore dei decimali
							    try {
							    	 DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
							    	 // importo
							    	String stringa  = df.format(rs.getDouble("valore"));
							    	 valoreStr=stringa;
							    } catch (Exception e) {
								    	log.error("", e);
								}
						 	}
						 	schedaInventario.setValore(valoreStr);

						 	scheda.getSchedaInventario().add(schedaInventario);
					 		// SCHEDA SINTESI
							//scheda.setSchedaSintesi(new ArrayList<SchedaSintesiVO>());
							SchedaSintesiVO schedaSintesi=new SchedaSintesiVO();
						 	if (rs.getString("cd_biblioteca_sezione") != null)
						 	{
								schedaSintesi.setKbibl(rs.getString("cd_biblioteca_sezione").trim());
						 	}

						 	if (rs.getString("anno_abb") != null)
						 	{
								schedaSintesi.setKsint(rs.getString("anno_abb"));
						 	}

							String appo_cd_sez="";
						 	String appo_cd_loc="";
							String appo_spec_loc="";
							String appo_seq_coll="";
							String appo_segnatura="";
						 	if (rs.getString("cd_sez") != null )
						 	{
						 		appo_cd_sez=rs.getString("cd_sez").trim();
						 		appo_segnatura=appo_segnatura + appo_cd_sez;
						 	}
						 	if ( rs.getString("cd_loc") != null  )
						 	{
						 		appo_cd_loc=rs.getString("cd_loc").trim();
						 		appo_segnatura=appo_segnatura + appo_cd_loc;

						 	}
						 	if ( rs.getString("spec_loc") != null  )
						 	{
						 		appo_spec_loc=rs.getString("spec_loc").trim();
						 		appo_segnatura=appo_segnatura + appo_spec_loc;

						 	}
						 	if (rs.getString("seq_coll") != null )
						 	{
						 		appo_seq_coll=rs.getString("seq_coll").trim();
						 		if (appo_seq_coll.length()>0)
						 		{
							 		appo_segnatura=appo_segnatura +" "+ appo_seq_coll;
						 		}
						 	}
						 	if (rs.getString("cd_biblioteca_sezione") != null )
						 	{
							 	schedaSintesi.setSegnatura(rs.getString("cd_biblioteca_sezione").trim() + appo_segnatura);
						 	}


					 		// MAIL ROSSANA 16 APRILE
					 		schedaSintesi.setDescr("Collocazione priva di esemplare titolo");

						 	if (rs.getString("cons_doc") != null)
						 	{
								schedaSintesi.setDescr(rs.getString("cons_doc"));
						 	}

						 	// riempimento schedasintesi alimentato + oltre per aggiungere il kordi
							//scheda.getSchedaSintesi().add(schedaSintesi);

					 		// SCHEDA ABBONAMENTO
							//scheda.setSchedaAbbonamento(new ArrayList<SchedaAbbonamentoVO>());
							SchedaAbbonamentoVO schedaAbbonamento=new SchedaAbbonamentoVO();
							if (rs.getString("ordcdbib") != null )
							{
								schedaAbbonamento.setKbibl(rs.getString("ordcdbib").trim());
							}

						 	String appo_cod_tip_ord="";
							String appo_anno_1ord="";
							String appo_cod_1ord="";
							String appo_anno_ord="";
							String appo_cod_ord="";
							String appo_kordi="";


							if (rs.getInt("anno_1ord")>0 )
							{
								if (rs.getString("cod_tip_ord") != null )
								{
									appo_cod_tip_ord=rs.getString("cod_tip_ord");
									appo_kordi=appo_kordi+appo_cod_tip_ord;
								}
								//old appo_anno_1ord=String.valueOf(rs.getInt("anno_1ord")).substring(2,4); // ultimi 2
								appo_anno_1ord=String.valueOf(rs.getInt("anno_1ord")).substring(1,4); // ultimi 3
								appo_kordi=appo_kordi + appo_anno_1ord;

								appo_cod_1ord="000000";

								if (rs.getString("cod_1ord") != null)
								{
									appo_cod_1ord=String.valueOf(rs.getInt("cod_1ord"));
									// formattazione del codice ordine nel formato di 6 caratteri con aggiunta di zeri in testa
								 	if (appo_cod_1ord!=null && appo_cod_1ord.trim().length()>0)
								 	{
								 		if (appo_cod_1ord.length()<6)
								 		{
								 			String c_cod_ord="000000" + appo_cod_1ord;
								 			int posizStart = c_cod_ord.length()-6;
								 			c_cod_ord=c_cod_ord.substring(posizStart,posizStart+6);
								 			//String cbibl=kbibl.substring(kbibl.length()-3, kbibl.length());
								 			appo_cod_1ord=c_cod_ord;
								 		}
								 		else
								 		{
								 			appo_cod_1ord=appo_cod_1ord.substring(0,6);
								 		}
								 	}

									appo_kordi=appo_kordi+appo_cod_1ord;
								}

								schedaAbbonamento.setKordi(appo_kordi);
							}
							else
							{
								if (rs.getString("cod_tip_ord") != null )
								{
									appo_cod_tip_ord=rs.getString("cod_tip_ord");
									appo_kordi=appo_kordi+appo_cod_tip_ord;
								}

								if (rs.getInt("anno_ord")>0 )
								{
									//appo_anno_ord=String.valueOf(rs.getInt("anno_ord")).substring(2,4);
									appo_anno_ord=String.valueOf(rs.getInt("anno_ord")).substring(1,4);
									appo_kordi=appo_kordi + appo_anno_ord;
								}

								appo_cod_ord="000000";

								if (rs.getString("cod_ord") != null)
								{
									appo_cod_ord=String.valueOf(rs.getInt("cod_ord"));
									// formattazione del codice ordine nel formato di 6 caratteri con aggiunta di zeri in testa
								 	if (appo_cod_ord!=null && appo_cod_ord.trim().length()>0)
								 	{
								 		if (appo_cod_ord.length()<6)
								 		{
								 			String c_cod_ord="000000" + appo_cod_ord;
								 			int posizStart = c_cod_ord.length()-6;
								 			c_cod_ord=c_cod_ord.substring(posizStart,posizStart+6);
								 			//String cbibl=kbibl.substring(kbibl.length()-3, kbibl.length());
								 			appo_cod_ord=c_cod_ord;
								 		}
								 		else
								 		{
								 			appo_cod_ord=appo_cod_ord.substring(0,6);
								 		}
								 	}

									appo_kordi=appo_kordi+appo_cod_ord;
								}

								schedaAbbonamento.setKordi(appo_kordi);
							}

						 	//riempimento scheda sintesi di cui sopra utili
							schedaSintesi.setKsint(appo_kordi+ rs.getString("anno_abb"));
							scheda.getSchedaSintesi().add(schedaSintesi);


						if (rs.getString("anno_abb") != null)
						 	{
								schedaAbbonamento.setKanno(rs.getString("anno_abb"));
						 	}


						 	if (rs.getString("cod_fornitore") != null)
						 	{
								schedaAbbonamento.setKforn(rs.getString("cod_fornitore").trim());
						 	}
						 	schedaAbbonamento.setLivello(null);
							scheda.getSchedaAbbonamento().add(schedaAbbonamento);

						}


					 arrScheda.add(scheda);
				}
				rs.close();
				pstmt.close();
				connection.close();

		} catch (Exception e) {

			valRitorno=false;
			log.error("", e);
		} finally {
			close(connection);
		}

		return arrScheda;
	}

	public List<SchedaInventarioVO> picosPeriodiciAskInv(String kbibl,
			String kordi, String kanno, String tipomat, String tipocirc,
			String precis, String valore, Boolean flacoll, String consisDoc,
			int annoBil, String tipoPrezzo, String prezzoBil, String kserie,
			String ticket) throws DataException, ApplicationException,
			ValidationException {

			kserie = ValidazioneDati.trimOrEmpty(kserie);

		 	List<SchedaInventarioVO> arrScheda=new ArrayList<SchedaInventarioVO>();
				Connection connection = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				PreparedStatement pstmt1 = null;
				ResultSet rs1 = null;
				PreparedStatement pstmt2 = null;
				ResultSet rs2 = null;
				PreparedStatement pstmt3 = null;
				ResultSet rs3 = null;

				boolean trovatoInv=false;
				OperazioneSuOrdiniMassivaVO richiesta = new OperazioneSuOrdiniMassivaVO();

				// formattazione del codice biblioteca nel formato di 3 caratteri con aggiunta di spazi in testa
			 	if (kbibl!=null && kbibl.trim().length()>0)
			 	{
			 		if (kbibl.length()<3)
			 		{
			 			String cbibl="   " + kbibl;
			 			int posizStart = cbibl.length()-3;
			 			cbibl=cbibl.substring(posizStart,posizStart+3);
			 			//String cbibl=kbibl.substring(kbibl.length()-3, kbibl.length());
				 		kbibl=cbibl;
			 		}
			 	}
				// formattazione dell'anno se arriva troncato
			 	if (kanno!=null && kanno.trim().length()==2)
			 	{
		 			String canno="20"+kanno;
			 		kanno=canno;
			 	}
				// kordi= deve essere tipo ordine+3chr dell'anno + poi le cifre significative dell'ordine (ricondotte a 6 posizioni) es 2007 A 5984 deve diventare A007005984
			 	//scomposizione
				String tipoOrdNew="";
				int annoOrdNew=0;
				int numOrdNew=0;
				String annoOrdNewStr="";
				String numOrdNewStr="";

				if (kordi!=null && kordi.trim().length()>0)
			 	{
			 		tipoOrdNew=kordi.substring(0,1);
			 		annoOrdNewStr=kordi.substring(1,4);
			 		if (annoOrdNewStr.substring(0,1).equals("0"))
			 		{
			 			annoOrdNewStr="2"+annoOrdNewStr;
			 		}
			 		else
			 		{
			 			annoOrdNewStr="1"+annoOrdNewStr;
			 		}
			 		annoOrdNew=Integer.parseInt(annoOrdNewStr);

			 		numOrdNewStr=kordi.substring(4, kordi.length());
			 		numOrdNew=Integer.parseInt(numOrdNewStr);
			 	}

			 try {
					connection = getConnection();
					// controllo esistenza primo ordine passato e la sua continuità
					String sql0="select cd_polo,id_ordine, ute_var, anno_abb ";
					sql0 += " from  tba_ordini ";
					sql0 += " where  ";
					sql0 += " cd_polo = (select cd_polo from tbf_polo)";
					if (kbibl !=null &&  kbibl.trim().length()>0)
					{
						sql0 = this.struttura(sql0);
						sql0 += " cd_bib='" + kbibl +"'";
					}
					if (!tipoOrdNew.equals("") && annoOrdNew!=0 && numOrdNew!=0)
					{
						sql0 = this.struttura(sql0);
						sql0 += " cod_tip_ord='" + tipoOrdNew +"'";
						sql0 = this.struttura(sql0);
						sql0 += " anno_ord=" + annoOrdNew;
						sql0 = this.struttura(sql0);
						sql0 += " cod_ord=" + numOrdNew;
					}
					sql0 = this.struttura(sql0);
					sql0 += " fl_canc<>'S'";
					sql0 = this.struttura(sql0);
					sql0 += " continuativo='1' ";
					sql0 = this.struttura(sql0);
					sql0 += " stato_ordine <> 'N' ";

					pstmt = connection.prepareStatement(sql0);
					rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
					// numero di righe del resultset
					int numRighe=0;
					String cdPolo="";
					int idOrdine=0;
					int annoAbbIord=0;
					String uteVar="";
					while (rs.next()) {
						numRighe=numRighe+1;
						if (numRighe==1)
						{
							 cdPolo=rs.getString("cd_polo");
							 idOrdine=rs.getInt("id_ordine");
							 uteVar=rs.getString("ute_var");
							 annoAbbIord=rs.getInt("anno_abb");
						}
					}
					rs.close();
					pstmt.close();
					//connection.close();

					if (numRighe!=1)
					{
						log.info("primo ordine passato inesistente");
						throw new Exception("ordine inesistente");
						//return arrScheda;
					}

					// ricostruzione della catena dei rinnovi
					ChiaveOrdine parCatena = new ChiaveOrdine(); // tipo, anno, cod  anno_abb, id_ordine
					parCatena.tipo = (tipoOrdNew);
					parCatena.anno = (String.valueOf(annoOrdNew));
					parCatena.cod = (String.valueOf(numOrdNew));
					// almaviva5_20151221
					parCatena.bib = (kbibl);
					//almaviva5_20160531
					parCatena.id_ordine = String.valueOf(idOrdine);
					List<ChiaveOrdine> catenaRinnovi=null; // tipo, anno, cod, anno_abb, id_ordine
					try {
						catenaRinnovi = this.catenaRinnovi(0, parCatena);
					} catch (Exception e) {

				 		log.error("", e);
				 	}

					// considerare l'ultimo della catena


					//rossana 01.04.09
					// si individua nella catena dei rinnovi quello relativo all'anno_abb=kanno parametro passato
					Boolean giaRinnovato=false;
					Boolean ultimoOrdRinnovabile=false;

					// va escluso il caso in cui è stato già effettuato un rinnovo con lo stesso anno abbonamento (iterazione della chiamata)

					ChiaveOrdine  ultimorinnovo = new ChiaveOrdine(); // tipo, anno, cod, anno_abb, id_ordine
					String queryIN="";
					Boolean bucoDiCatenaRinnovi=false;
					Boolean inventariPrecedentiPrimoOrdine=false;

					if (ValidazioneDati.size(catenaRinnovi) > 1)
					{
						//preimposto l'ultimo della catena
						ultimorinnovo= catenaRinnovi.get(catenaRinnovi.size()-1);

						// gestione riempimento buchi di rinnovo in tal caso si imposta il rinnovato che precede kanno
						if (ultimorinnovo.anno_abb!=null && ultimorinnovo.anno_abb.trim().length()>0 && Integer.parseInt(ultimorinnovo.anno_abb)> Integer.parseInt(kanno))
						{
							// la condizione di sopra del codice4 valorizzato implica che esiste nella catena almeno un rinnovo oltre al primo ordine
							//devo fissare in ultimorinnovo l'ultimo ordine (anche se con rinnovato=true) con anno_abb<kanno
							if (catenaRinnovi.get(0) != null && catenaRinnovi.get(0).anno!=null && catenaRinnovi.get(0).anno.trim().length()>0   && Integer.parseInt(catenaRinnovi.get(0).anno)< Integer.parseInt(kanno))
							{
								bucoDiCatenaRinnovi=true;
								ultimorinnovo= catenaRinnovi.get(0); // preimposto il primo ordine confrontando l'anno ordine (invece di anno_abb) con kanno
							}

							for (int i=0;  i <  catenaRinnovi.size(); i++)
							{
								if(catenaRinnovi.get(i) != null && catenaRinnovi.get(i).anno_abb!=null && catenaRinnovi.get(i).anno_abb.trim().length()>0   && Integer.parseInt(catenaRinnovi.get(i).anno_abb)< Integer.parseInt(kanno))
								{
									bucoDiCatenaRinnovi=true;
									ultimorinnovo= catenaRinnovi.get(i);
								}
							}
						}

						queryIN = String.valueOf(idOrdine); // id del primo ordine
						for (int i=0;  i <  catenaRinnovi.size(); i++)
						{
							if (catenaRinnovi.get(i) != null && catenaRinnovi.get(i).tipo!=null &&  catenaRinnovi.get(i).anno!=null && catenaRinnovi.get(i).cod!=null && catenaRinnovi.get(i).anno_abb!=null && catenaRinnovi.get(i).anno_abb.equals(kanno))
							{
								ultimorinnovo= catenaRinnovi.get(i);
								log.info("ordine trovato nella catena con stesso anno abb");
							}
							if(catenaRinnovi.get(i) != null && catenaRinnovi.get(i).id_ordine!=null && catenaRinnovi.get(i).id_ordine.trim().length()>0)
							{
								if (ValidazioneDati.isFilled(queryIN))
								{
									queryIN += ",";
								}
								queryIN += catenaRinnovi.get(i).id_ordine;

							}
						}
						if (annoAbbIord>0 && annoAbbIord>=Integer.parseInt(kanno) ) // annoAbbIord annoOrdNew
						{
							// preimposto il primo ordine per kanno precedente l'anno del primo ordine
							ultimorinnovo= catenaRinnovi.get(0);
							inventariPrecedentiPrimoOrdine=true;
						}

					}
					else if (catenaRinnovi!=null && catenaRinnovi.size()==1)
					{
						// l'ordine passato è l'origine della catena e dovrà essere rinnovato
						ultimorinnovo=parCatena;
					}
					else
					{
						// l'ordine passato è l'origine della catena e dovrà essere rinnovato
						ultimorinnovo=parCatena;
					}
					// controllo esistenza ultimo ordine rinnovato e recupero informazioni
					String sql1="select ord.cd_polo,ord.id_ordine, ord.ute_var,ord.bid, ord.cod_fornitore, ord.anno_abb, inv.anno_abb as anno_abb_inv ,ord.rinnovato";
					sql1 += " from  tba_ordini ord ";
					// evolutiva 31.05.10 MESSE LE DUE SOTTOSTANTI-- sql1 += " left join tbc_inventario inv on inv.cd_serie=' ' and  inv.cd_tip_ord=ord.cod_tip_ord and inv.anno_ord=ord.anno_ord and inv.cd_ord=ord.cod_ord and inv.cd_bib_ord=ord.cd_bib and inv.fl_canc<>'S'";
					sql1 += " left join tbc_inventario inv ";
					//sql1 += " on trim(inv.cd_serie)='" + kserie + "'" ;
					sql1 += " on  inv.cd_tip_ord=ord.cod_tip_ord and inv.anno_ord=ord.anno_ord and inv.cd_ord=ord.cod_ord and inv.cd_bib_ord=ord.cd_bib and inv.fl_canc<>'S'";
					if (!inventariPrecedentiPrimoOrdine)
					{
						sql1 += " and inv.anno_abb=ord.anno_abb ";
					}
					else
					{
						// inventariPrecedentiPrimoOrdine=true
						// recupero dell'inventario relativo all'anno_abb =kanno che precede l'anno del primo ordine
						sql1 += " and inv.anno_abb= " + Integer.parseInt(kanno);
					}

					sql1 += " where  ";
					sql1 += " ord.cd_polo = (select cd_polo from tbf_polo)";

					if (kbibl !=null &&  kbibl.trim().length()>0)
					{
						sql1 = this.struttura(sql1);
						sql1 += " ord.cd_bib='" + kbibl +"'";
					}
					if (!ultimorinnovo.tipo.equals("") && !ultimorinnovo.anno.equals("") && !ultimorinnovo.cod.equals(""))
					{
						sql1 = this.struttura(sql1);
						sql1 += " ord.cod_tip_ord='" + ultimorinnovo.tipo +"'";
						sql1 = this.struttura(sql1);
						sql1 += " ord.anno_ord=" + Integer.parseInt(ultimorinnovo.anno);
						sql1 = this.struttura(sql1);
						sql1 += " ord.cod_ord=" + Integer.parseInt(ultimorinnovo.cod);
					}
					sql1 = this.struttura(sql1);
					sql1 += " ord.fl_canc<>'S'";
					sql1 = this.struttura(sql1);
					sql1 += " ord.continuativo='1' ";
					sql1 = this.struttura(sql1);
					sql1 += " ord.stato_ordine <> 'N' ";

					//connection = getConnection();
					pstmt1 = connection.prepareStatement(sql1);
					rs1 = pstmt1.executeQuery(); // va in errore se non può restituire un recordset
					// numero di righe del resultset
					int numRighe1=0;
					String cdPolo1="";
					int idOrdine1=0;
					String uteVar1="";
					int annoAbb1=0;
					int annoAbbInv=0;
					int id_fornitoreAppo=0;
					String bidAppo="";

					boolean rinnovatoAppo=true;
					while (rs1.next()) {
						numRighe1=numRighe1+1;
						if (numRighe1==1)
						{
							 cdPolo1=rs1.getString("cd_polo");
							 idOrdine1=rs1.getInt("id_ordine");
							 uteVar1=rs1.getString("ute_var");
							 annoAbb1=rs1.getInt("anno_abb");

							 bidAppo=rs1.getString("bid");
							 id_fornitoreAppo=rs1.getInt("cod_fornitore");
							 if (rs1.getString("anno_abb_inv") != null && rs1.getInt("anno_abb_inv")>0)
							 {
								 annoAbbInv=rs1.getInt("anno_abb_inv");
							 }
							 rinnovatoAppo=rs1.getBoolean("rinnovato");
							if (!rinnovatoAppo)
							{
								ultimoOrdRinnovabile=true;
								log.info("ordine ultimo della catena e quindi rinnovabile");
							}
						}
					}
					rs1.close();
					pstmt1.close();
					//connection.close();

					// rossana 07.04.09
					if (numRighe>0 && annoAbb1>=Integer.parseInt(kanno)) // evolutiva 31.05.10 era ==1 // TODO ORA METTERE annoAabb1 invece di Integer.parseInt(kanno)
					{
						log.info("caso di rinnovo di un ordine per gli anni precedenti al suo primo inserimento");
						//throw new Exception("non è consentito il rinnovo di un ordine per gli anni precedenti al suo primo inserimento");
						//return arrScheda;
					}

					// oppure inventariPrecedentiPrimoOrdine
					if (numRighe1>0 && (annoAbb1==Integer.parseInt(kanno) || (annoAbbInv==Integer.parseInt(kanno) && inventariPrecedentiPrimoOrdine) ) ) // && rinnovatoAppo bucoDiCatenaRinnovi  // evolutiva 31.05.10 era ==1
					{
						log.info("ordine già rinnovato");
						giaRinnovato=true;
					}

					if (numRighe1==0)
					{
						log.info("ultimo ordine rinnovato inesistente ord=" + ultimorinnovo.tipo.trim() + ultimorinnovo.anno.trim() + ultimorinnovo.cod.trim());
						throw new Exception("ultimo ordine rinnovato inesistente ord=" + ultimorinnovo.tipo.trim() +" "+ ultimorinnovo.anno.trim()+" " + ultimorinnovo.cod.trim());
						//return arrScheda;
					}
					Boolean ordineRinnovatoPrivoDiInventari=false;
					if (numRighe1==1)
					{
						if (annoAbbInv==0 && annoAbb1==Integer.parseInt(kanno))
						{
							//rossana 01.04.09
							log.info("ordine rinnovato privo di inventari per l'anno abb - ord=" + ultimorinnovo.tipo.trim() + ultimorinnovo.anno.trim() + ultimorinnovo.cod.trim());
							//throw new Exception("non è stato trovato alcun inventario legato all'ordine in rinnovo ord=" + ultimorinnovo.getCodice1().trim() + ultimorinnovo.getCodice2().trim() + ultimorinnovo.getCodice3().trim());
							ordineRinnovatoPrivoDiInventari=true;
						}
					}

	// 26.03		if (operaz.isEsitoRinnovoPicos() || giaRinnovato)
					// UN RINNOVO DEVE ESSERE OBBLIGATORIAMENTE PER L'ANNO SUCCESSIVO
					//if (annoAbb1==(Integer.parseInt(kanno)-1))
					//{
						String key_loc=null;
						int numOrdineRinnovo=0;
						int id_fornitore=0;
						String serie="";
						String bidInv="";
						String tipoAcq="";
						String sequenza="";

						Boolean esistenzaInvCollocatiInCatenaRinnovi=false;
						SchedaInventarioVO schedaInvAppo=new SchedaInventarioVO();
						if (!ordineRinnovatoPrivoDiInventari)
						{
							//String sqlINV = " select inv.*, ord.cod_fornitore, ord.id_ordine ";
							String sqlINV="select inv.key_loc, inv.bid, inv.cd_bib,inv.bid as invbid, inv.cd_serie, inv.cd_inven, inv.cd_mat_inv, inv.tipo_acquisizione, inv.valore, inv.cd_frui" ;
							sqlINV += " ,inv.data_ingresso, inv.precis_inv, inv.anno_abb, inv.seq_coll, coll.cd_biblioteca_sezione, coll.cd_sez, coll.cd_loc ";
							sqlINV += " ,coll.spec_loc, ese.cons_doc, ord.cd_bib as ordcdbib, ord.anno_ord, ord.cod_tip_ord, ord.cod_ord, ord.cod_fornitore, ord.anno_1ord, ord.id_ordine  ";
							//sql += ",  forn.nom_fornitore ";
							sqlINV += " ,ord.cod_1ord, ord.anno_abb as ordannoabb, tit.bid as titbid, tit.cd_natura, tit.cd_genere_1, tit.cd_lingua_1, tit.cd_paese, tit.tp_aa_pubb ";
							sqlINV += " ,tit.aa_pubb_1, tit.aa_pubb_2, tit.cd_periodicita, tit.isbd, tit.ky_cles1_t, tit.ky_cles2_t, nstd.numero_std ";
							sqlINV += " from tbc_inventario inv ";
							sqlINV += " join tba_ordini ord on ord.cd_bib  = inv.cd_bib_ord ";
							sqlINV += "                                         and ord.cd_polo =inv.cd_polo ";
							sqlINV += "                                         and ord.anno_ord=inv.anno_ord ";
							sqlINV += "                                         and ord.cod_tip_ord=inv.cd_tip_ord ";
							sqlINV += "                                         and ord.cod_ord=inv.cd_ord ";
							sqlINV += "                                         and ord.fl_canc<>'S' ";
							sqlINV += "                                         and ord.stato_ordine <> 'N' ";
							//sqlINV += "                                         and ord.anno_abb=inv.anno_abb ";
							if (!inventariPrecedentiPrimoOrdine)
							{
								sqlINV += "                                     and ord.anno_abb=inv.anno_abb ";
							}
								sqlINV += "left outer join tbc_collocazione    coll on coll.key_loc=inv.key_loc ";
								sqlINV += "                                         and coll.fl_canc<>'S' ";
								sqlINV += " left outer join tbc_esemplare_titolo ese on ese.bid=coll.bid_doc ";
								sqlINV += "                                         and ese.cd_doc=coll.cd_doc ";
								sqlINV += "                                         and ese.cd_biblioteca=coll.cd_biblioteca_doc ";
								sqlINV += "                                         and ese.cd_polo=coll.cd_polo_doc ";
								sqlINV += "                                         and ese.fl_canc<>'S' ";
								sqlINV += " inner join tb_titolo tit                 on tit.bid=inv.bid ";
								sqlINV += "                                         and tit.fl_canc<>'S' ";
								sqlINV += " left outer join tb_numero_std nstd       on nstd.bid=tit.bid ";
								sqlINV += "                                         and nstd.tp_numero_std='J'  ";
								sqlINV += "                                         and nstd.fl_canc<>'S' ";

							sqlINV += " where inv.cd_polo=(select cd_polo from tbf_polo) ";
							sqlINV += "    and inv.cd_bib='"+ kbibl + "' ";  //FI
							sqlINV += "    and inv.anno_ord= " + Integer.parseInt(ultimorinnovo.anno);  // 2008
							sqlINV += "    and inv.cd_tip_ord='"+ ultimorinnovo.tipo + "' "; // A
							sqlINV += "    and inv.cd_ord=" + Integer.parseInt(ultimorinnovo.cod); // 14

							if (!inventariPrecedentiPrimoOrdine)
							{
								sqlINV += "    and inv.anno_abb=" + annoAbb1;
							}
							else
							{
								sqlINV += "    and inv.anno_abb=" + Integer.parseInt(kanno);  // oppure annoAbbInv

							}
							sqlINV += "    and inv.fl_canc<>'S'  ";

							pstmt2 = connection.prepareStatement(sqlINV);
							rs2 = pstmt2.executeQuery(); // va in errore se non può restituire un recordset

							//boolean trovatoInv=false;
							while (rs2.next()) {
								trovatoInv=true;

								if (flacoll)
								{
									key_loc=String.valueOf(rs2.getInt("key_loc"));
								}
								numOrdineRinnovo=rs2.getInt("id_ordine");
								id_fornitore=rs2.getInt("cod_fornitore");
								serie="   ";
								if (rs2.getString("cd_serie") != null && !rs2.getString("cd_serie").equals(""))
								{
									serie=rs2.getString("cd_serie");
								}
								bidInv=rs2.getString("bid");
								tipoAcq=rs2.getString("tipo_acquisizione");
								// RIEMPIMENTO DELLA SCHEDA INVENTARIO
							 	if (rs2.getString("cd_bib") != null)
							 	{
							 		schedaInvAppo.setKbibl(rs2.getString("cd_bib").trim());
							 	}
								String appo_ninvent="";
							 	String appo_cd_serie="";
								String appo_cd_inven="";
							 	if (rs2.getString("cd_serie") != null  )
							 	{
							 		appo_cd_serie=rs2.getString("cd_serie").trim();
									appo_ninvent=appo_ninvent + appo_cd_serie ;
							 	}
							 	if (rs2.getString("cd_inven") != null )
							 	{
							 		appo_cd_inven=rs2.getString("cd_inven");
									appo_ninvent=appo_ninvent + appo_cd_inven ;
							 	}
							 	//schedaInvAppo.setNinvent(appo_ninvent);
							 	schedaInvAppo.setKserie(appo_cd_serie); //20.10.2010
							 	schedaInvAppo.setNinvent(appo_cd_inven); //20.10.2010

								if (rs2.getString("cd_sez") != null)
							 	{
									schedaInvAppo.setSezione(rs2.getString("cd_bib").trim()+rs2.getString("cd_sez").trim());
							 	}

							 	if (rs2.getString("cd_loc") != null)
							 	{
							 		schedaInvAppo.setColloc(rs2.getString("cd_loc").trim());
							 	}

							 	if (rs2.getString("spec_loc") != null)
							 	{
							 		schedaInvAppo.setSpecific(rs2.getString("spec_loc").trim());
							 	}

							 	if (rs2.getString("seq_coll") != null)
							 	{
							 		schedaInvAppo.setSequenza(rs2.getString("seq_coll").trim());
							 		sequenza=rs2.getString("seq_coll").trim();
							 	}

								SimpleDateFormat format1 = new SimpleDateFormat("yyMMdd");

								if (rs2.getString("data_ingresso") != null)
							 	{
									String dataFormattata = rs2.getString("data_ingresso");
									try {
										dataFormattata = format1.format(rs2.getDate("data_ingresso"));
									} catch (Exception e) {
										log.error("", e);
									}
									schedaInvAppo.setData(dataFormattata);
							 	}

							 	if (rs2.getString("tipo_acquisizione") != null)
							 	{
							 		schedaInvAppo.setTipoprov(rs2.getString("tipo_acquisizione"));
							 	}

							 	if (rs2.getString("cd_mat_inv") != null)
							 	{
							 		schedaInvAppo.setTipomat(rs2.getString("cd_mat_inv"));
							 	}

							 	if (rs2.getString("cd_frui") != null)
							 	{
							 		schedaInvAppo.setTipocirc(rs2.getString("cd_frui").trim());
							 	}

							 	if (rs2.getString("precis_inv") != null)
							 	{
							 		schedaInvAppo.setPrecis(rs2.getString("precis_inv"));
							 	}
							 	String valoreStr="";
							 	if (rs2.getString("valore") != null)
							 	{
								    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
								    dfs.setGroupingSeparator(',');
								    dfs.setDecimalSeparator('.');
								    // controllo formattazione con virgola separatore dei decimali
								    try {
								    	 DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
								    	 // importo
								    	String stringa  = df.format(rs2.getDouble("valore"));
								    	 valoreStr=stringa;
								    } catch (Exception e) {
									    	log.error("", e);
									}
							 	}
							 	schedaInvAppo.setValore(valoreStr);
							}
							rs2.close();
							pstmt2.close();
						}
						// prova test
						if ((key_loc==null || key_loc.trim().equals("") || key_loc.trim().equals("0")) && flacoll && queryIN!=null && queryIN.trim().length()>0  ) // && trovatoInv && !ordineRinnovatoPrivoDiInventari
						{
							// ricerca inventario con collocazione nella catena dei rinnovi
							String sqlINV="select inv.key_loc, inv.bid, inv.cd_bib,inv.bid as invbid, inv.cd_serie, inv.cd_inven, inv.cd_mat_inv, inv.tipo_acquisizione, inv.valore, inv.cd_frui ,inv.data_ingresso, inv.precis_inv, inv.anno_abb, inv.seq_coll, coll.cd_biblioteca_sezione, coll.cd_sez, coll.cd_loc  ,coll.spec_loc, ese.cons_doc, ord.cd_bib as ordcdbib, ord.anno_ord, ord.cod_tip_ord, ord.cod_ord, ord.cod_fornitore, ord.anno_1ord, ord.id_ordine   ,ord.cod_1ord, ord.anno_abb as ordannoabb, tit.bid as titbid, tit.cd_natura, tit.cd_genere_1, tit.cd_lingua_1, tit.cd_paese, tit.tp_aa_pubb  ,tit.aa_pubb_1, tit.aa_pubb_2, tit.cd_periodicita, tit.isbd, tit.ky_cles1_t, tit.ky_cles2_t, nstd.numero_std";
							sqlINV += " from tbc_inventario inv ";
							sqlINV += " join tba_ordini ord on ord.cd_bib = inv.cd_bib_ord and ord.cd_polo =inv.cd_polo and  ord.fl_canc<>'S' and ord.anno_ord=inv.anno_ord  and ord.cod_tip_ord=inv.cd_tip_ord  and ord.cod_ord=inv.cd_ord  and ord.stato_ordine <> 'N'";
							sqlINV += " left outer join tbc_collocazione    coll on coll.key_loc=inv.key_loc  and coll.fl_canc<>'S' ";
							sqlINV += " left outer join tbc_esemplare_titolo ese on ese.bid=coll.bid_doc  and ese.cd_doc=coll.cd_doc  and ese.cd_biblioteca=coll.cd_biblioteca_doc  and ese.cd_polo=coll.cd_polo_doc and ese.fl_canc<>'S' ";
							sqlINV += " inner join tb_titolo tit on tit.bid=inv.bid  and tit.fl_canc<>'S' ";
							sqlINV += " left outer join tb_numero_std nstd on nstd.bid=tit.bid  and nstd.tp_numero_std='J'  and nstd.fl_canc<>'S' ";
							sqlINV += " where inv.cd_polo=(select cd_polo from tbf_polo) ";
							sqlINV += " and inv.cd_bib='"+ kbibl + "' ";  //FI
							sqlINV += " and ord.id_ordine IN (" + queryIN + ")";
							sqlINV += " and inv.key_loc is not null ";
							sqlINV += " and inv.fl_canc<>'S'";
							sqlINV += " order by inv.ts_ins desc ";

							pstmt3 = connection.prepareStatement(sqlINV);
							rs3 = pstmt3.executeQuery(); // va in errore se non può restituire un recordset
							int contaInv=0;
							while (rs3.next()) {
								esistenzaInvCollocatiInCatenaRinnovi=true;
								contaInv=contaInv +1;
								if (contaInv==1)
								{
									key_loc=String.valueOf(rs3.getInt("key_loc"));
								}
							}
							rs3.close();
							pstmt3.close();

						}

						// va escluso il caso in cui è stato già effettuato un rinnovo con lo stesso anno abbonamento (iterazione della chiamata)
						if (!giaRinnovato || ordineRinnovatoPrivoDiInventari) //rossana 01.04.09
						{
							// creazione array id ordini da rinnovare (uno solo)
							List<Integer> listaIDOrdini = new ArrayList<Integer>();
							listaIDOrdini.add(idOrdine1);
							richiesta.setDatiInput(listaIDOrdini);
							richiesta.setCodPolo(cdPolo1);
							richiesta.setCodBib(kbibl);

							//String basePath=this.servlet.getServletContext().getRealPath(File.separator);
							//operaz.setBasePath(basePath);
							String downloadPath = StampeUtil.getBatchFilesPath();
							//log.info("download path: " + downloadPath);
							String downloadLinkPath = "/";
							richiesta.setDownloadPath(downloadPath);
							richiesta.setDownloadLinkPath(downloadLinkPath);
							//almaviva5_20141205 #5672
							richiesta.setTicket(ticket);
							richiesta.setUser(DaoManager.getFirmaUtente(ticket));

							richiesta.setTipoOperazione("R");
							richiesta.setServizioPicos(true);
							if (ordineRinnovatoPrivoDiInventari || inventariPrecedentiPrimoOrdine ) //rossana 01.04.09
							{
								richiesta.setSoloCreazioneInventari(true);
							}
							if (bucoDiCatenaRinnovi)
							{
								richiesta.setAggiornamentoRinnovo(false);
							}

							Double importo=0.00;
						    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
						    dfs.setGroupingSeparator('.');
						    dfs.setDecimalSeparator(',');
						    // controllo formattazione con virgola separatore dei decimali
						    try {
						    	 DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
							    	df.setMaximumFractionDigits(2);
									df.setMinimumFractionDigits(2);
								    // controllo formato euro
									NumberFormat formatIT = NumberFormat.getCurrencyInstance();
								    String strCurrency="\u20AC ";
								    strCurrency=strCurrency+prezzoBil;
								    Number imp=formatIT.parse(strCurrency); // va in errore se non è riconosciuto come formato euro
								    String importStrEuro=formatIT.format(imp);
								    importStrEuro=importStrEuro.substring(2); // elimina il simbolo
								    Double importoDoubleEuro =  df.parse(importStrEuro).doubleValue();
								    BigDecimal bd = new BigDecimal(importoDoubleEuro);
						   			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP) ;
						   			importo=bd.doubleValue();
								    // importo
							} catch (Exception e) {

								log.error("", e);
							}

							richiesta.setPrezzoBil(importo);
							richiesta.setKanno(kanno);
							// vedi mail 24.4.09 di rossana nuova specifica annobil=20097531 che corrisponde a esercizio 2009 capitolo 7531.
							richiesta.setAnnoBil(annoBil);
							if(annoBil>0 && String.valueOf(annoBil).trim().length()>4)
							{
							  // la parte restante viene interpretata come capitolo di bilancio
						 		String annoBilancioStr=String.valueOf(annoBil).trim().substring(0,4);
						 		String capitoloBilancioStr=String.valueOf(annoBil).trim().substring(4,String.valueOf(annoBil).trim().length());
						 		int annoBilancio=Integer.parseInt(annoBilancioStr);
						 		int capitoloBilancio=Integer.parseInt(capitoloBilancioStr);
								richiesta.setAnnoBil(annoBilancio);
								richiesta.setCapitoloBil(capitoloBilancio);
							}

						}
						SchedaPeriodicoVO schedaPeriodico=new SchedaPeriodicoVO();

						schedaPeriodico.setSchedaInventario(new ArrayList<SchedaInventarioVO>());
						SchedaInventarioVO schedaInventario=new SchedaInventarioVO();
						SchedaInventarioInputVO schedaInventarioInput=new SchedaInventarioInputVO();

						schedaPeriodico.getSchedaInventario().add(schedaInventario);

						SchedaInventarioInputVO passaggioDati=new SchedaInventarioInputVO();
						passaggioDati.setCodPolo(cdPolo1);
						passaggioDati.setCodBib(kbibl);
						passaggioDati.setCodBibO(kbibl);
						passaggioDati.setKanno(kanno);

						passaggioDati.setNroOrdineRinnovo(String.valueOf(numOrdineRinnovo));

						passaggioDati.setCodTipoOrd(ultimorinnovo.tipo);
						passaggioDati.setAnnoOrd(ultimorinnovo.anno);
						passaggioDati.setCodOrd(ultimorinnovo.cod);


						passaggioDati.setIdFornitore(String.valueOf(id_fornitoreAppo));
						if (bidAppo!=null)
						{
							passaggioDati.setBidInv(bidAppo);
						}
						if (key_loc!=null)
						{
							passaggioDati.setKeyLoc(key_loc);
						}
						if (serie!=null)
						{
							passaggioDati.setSerie(serie);
							if (kserie!=null && kserie.trim().length()>0)  // evolutiva 31.05.10
							{
								passaggioDati.setSerie(kserie);
							}

						}
						if (sequenza!=null)
						{
							passaggioDati.setSequenza(sequenza);
						}
						if (tipomat!=null)
						{
							passaggioDati.setTipoMat(tipomat);
						}

						if (tipocirc!=null)
						{
							passaggioDati.setTipoCirc(tipocirc);
						}

						if (precis!=null)
						{
							passaggioDati.setPrecis(precis);
						}

						if (valore!=null)
						{
							passaggioDati.setValore(valore);
						}
						if (tipoPrezzo!=null)
						{
							passaggioDati.setTipoPrezzo(tipoPrezzo);
						}
						if (consisDoc!=null)
						{
							passaggioDati.setConsisDoc(consisDoc);
						}

						passaggioDati.setPrezzoBil("0");
						if (prezzoBil!=null)
						{
							passaggioDati.setPrezzoBil(prezzoBil);
						}

						Locale locale=Locale.getDefault();

						SchedaInventarioVO schedaInv=new SchedaInventarioVO();
						if (giaRinnovato && !ordineRinnovatoPrivoDiInventari)
						{
							schedaInv=schedaInvAppo;
						}
						else // if (trovatoInv || ordineRinnovatoPrivoDiInventari)  //rossana 01.04.09
						{
							log.info("trovato inventario");
							schedaInv= DomainEJBFactory.getInstance().getAcquisizioniBMT().picosRinnovoOrdine(richiesta, passaggioDati, ticket);
						}
						if (schedaInv!=null)
						{
							arrScheda.add(schedaInv);
						}
					connection.close();

					if (arrScheda!=null && arrScheda.size()==1 && arrScheda.get(0).getNinvent() != null && arrScheda.get(0).getNinvent().trim().length()==0 )
					{
						arrScheda=null;

					}




			 } catch (Exception e) {

				log.error("", e);
				//throw new ValidationException(e);
				throw new ValidationException(e);

			} finally {
				close(connection);
			}

		return arrScheda;
		}

	public List<SchedaPeriodicoVO> picosPeriodiciInv(String kbibl,
			String kinv, String kserie) throws DataException,
			ApplicationException, ValidationException {
		List<SchedaPeriodicoVO> arrScheda = new ArrayList<SchedaPeriodicoVO>();

		boolean valRitorno=false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		kserie = ValidazioneDati.trimOrEmpty(kserie);


		// formattazione del codice biblioteca nel formato di 3 caratteri con aggiunta di spazi in testa
		if (kbibl!=null && kbibl.trim().length()>0)
	 	{
	 		if (kbibl.length()<3)
	 		{
	 			String cbibl="   " + kbibl;
	 			int posizStart = cbibl.length()-3;
	 			cbibl=cbibl.substring(posizStart,posizStart+3);

		 		kbibl=cbibl;
	 		}
	 	}

		try {

				connection = getConnection();

				String sql="select inv.cd_bib,inv.bid as invbid, inv.cd_serie, inv.cd_inven, inv.cd_mat_inv, inv.tipo_acquisizione, inv.valore, inv.cd_frui" ;
				sql += " ,inv.data_ingresso, inv.precis_inv, inv.anno_abb, inv.seq_coll, coll.cd_biblioteca_sezione, coll.cd_sez, coll.cd_loc ";
				sql += " ,coll.spec_loc, ese.cons_doc, ord.cd_bib as ordcdbib, ord.anno_ord, ord.cod_tip_ord, ord.cod_ord, ord.cod_fornitore, ord.anno_1ord ";

				sql += " ,ord.cod_1ord, ord.anno_abb as ordannoabb, tit.bid as titbid, tit.cd_natura, tit.cd_genere_1, tit.cd_lingua_1, tit.cd_paese, tit.tp_aa_pubb ";
				sql += " ,tit.aa_pubb_1, tit.aa_pubb_2, tit.cd_periodicita, tit.isbd, tit.ky_cles1_t, tit.ky_cles2_t, nstd.numero_std ";
				sql += " from tbc_inventario inv ";
				sql += " left outer join tbc_collocazione    coll on coll.key_loc=inv.key_loc ";
				sql += "                                         and coll.fl_canc<>'S' ";
				sql += " left outer join tbc_esemplare_titolo ese on ese.bid=coll.bid_doc ";
				sql += "                                         and ese.cd_doc=coll.cd_doc ";
				sql += "                                         and ese.cd_biblioteca=coll.cd_biblioteca_doc ";
				sql += "                                         and ese.cd_polo=coll.cd_polo_doc ";
				sql += "                                         and ese.fl_canc<>'S' ";
				sql += " left outer join tba_ordini ord           on ord.cd_bib  = inv.cd_bib_ord ";
				sql += "                                         and ord.cd_polo =inv.cd_polo ";
				sql += "                                         and ord.anno_ord=inv.anno_ord ";
				sql += "                                         and ord.cod_tip_ord=inv.cd_tip_ord ";
				sql += "                                         and ord.cod_ord=inv.cd_ord ";
				sql += "                                         and ord.fl_canc<>'S' ";
				sql += "                                         and ord.stato_ordine <> 'N'";
				sql += " inner join tb_titolo tit                 on tit.bid=inv.bid ";
				sql += "                                         and tit.fl_canc<>'S' ";
				sql += " left outer join tb_numero_std nstd       on nstd.bid=tit.bid ";
				sql += "                                         and nstd.tp_numero_std='J'  ";
				sql += "                                         and nstd.fl_canc<>'S' ";
				sql += " where inv.cd_polo=(select cd_polo from tbf_polo)";
				sql += "   and inv.cd_bib=? ";
				sql += "   and trim(inv.cd_serie)= '" + kserie.trim() + "'"; // EVOLUTIVA 31.05.10
				sql += "   and inv.cd_inven=? ";
				sql += "   and inv.fl_canc<>'S'";

				pstmt = connection.prepareStatement(sql);

				int i=1;
				pstmt.setString(i, kbibl);
				i++;

				//almaviva5_20090325 cd_inven è un integer su DB
				//pstmt.setString(i, kinv);
				pstmt.setInt(i, Integer.valueOf(kinv) );
				i++;


				rs = pstmt.executeQuery(); // va in errore se non può restituire un recordset
				log.debug("Debug: xinv");
				log.debug("Debug: " + sql);

				while (rs.next()) {
					 SchedaPeriodicoVO scheda=new SchedaPeriodicoVO();
					 	if (rs.getString("titbid") != null)
					 	{
						 	scheda.setKnide(rs.getString("titbid"));
					 	}
					 	if (rs.getString("invbid") != null)
					 	{
						 	scheda.setBidsbn(rs.getString("invbid"));
					 	}
					 	if (rs.getString("cd_natura") != null)
					 	{
							scheda.setNatura(rs.getString("cd_natura"));
					 	}
					 	if (rs.getString("numero_std") != null)
					 	{
							scheda.setIssn(rs.getString("numero_std"));
					 	}

					 	if (rs.getString("cd_lingua_1") != null)
					 	{
							scheda.setLingua(rs.getString("cd_lingua_1"));
					 	}

					 	if (rs.getString("cd_paese") != null)
					 	{
							scheda.setPaese(rs.getString("cd_paese"));
					 	}

					 	if (rs.getString("tp_aa_pubb") != null)
					 	{
							scheda.setTipod(rs.getString("tp_aa_pubb"));
					 	}


					 	//almaviva5_20150209
					 	String data1 = rs.getString("aa_pubb_1");
					 	scheda.setAnno(data1);
					 	String data2 = rs.getString("aa_pubb_2");
						scheda.setAnno2(data2);

					 	if (rs.getString("cd_genere_1") != null)
					 	{
							scheda.setGenere(rs.getString("cd_genere_1").trim());
					 	}

					 	if (rs.getString("cd_periodicita") != null)
					 	{
							scheda.setPeriodic(rs.getString("cd_periodicita").trim());
					 	}

					 	if (rs.getString("isbd") != null)
					 	{
							scheda.setIsbd(rs.getString("isbd"));
					 	}

						String appo_cles="";
					 	String appo_ky_cles1_t="";
						String appo_ky_cles2_t="";
					 	if (rs.getString("ky_cles1_t") != null)
					 	{
					 		appo_ky_cles1_t=rs.getString("ky_cles1_t");
					 		appo_cles=appo_cles+appo_ky_cles1_t;
					 	}
					 	if (rs.getString("ky_cles2_t") != null )
					 	{
					 		appo_ky_cles2_t=rs.getString("ky_cles2_t");
					 		appo_cles=appo_cles+appo_ky_cles2_t;
					 	}
				 		scheda.setCles(appo_cles.trim());

				 		//SCHEDA INVENTARIO
						scheda.setSchedaInventario(new ArrayList<SchedaInventarioVO>());
						SchedaInventarioVO schedaInventario=new SchedaInventarioVO();

						if (rs.getString("cd_bib") != null)
					 	{
							schedaInventario.setKbibl(rs.getString("cd_bib").trim());
					 	}

						String appo_ninvent="";
					 	String appo_cd_serie="";
						String appo_cd_inven="";
					 	if (rs.getString("cd_serie") != null  )
					 	{
					 		appo_cd_serie=rs.getString("cd_serie").trim();
							appo_ninvent=appo_ninvent + appo_cd_serie ;
					 	}
					 	if (rs.getString("cd_inven") != null )
					 	{
					 		appo_cd_inven=rs.getString("cd_inven");
							appo_ninvent=appo_ninvent + appo_cd_inven ;
					 	}

					 	//schedaInventario.setNinvent(appo_ninvent);
						schedaInventario.setKserie(appo_cd_serie); //20.10.2010
						schedaInventario.setNinvent(appo_cd_inven); //20.10.2010


						if (rs.getString("cd_sez") != null)
					 	{
							schedaInventario.setSezione(rs.getString("cd_bib").trim()+rs.getString("cd_sez").trim());
					 	}

					 	if (rs.getString("cd_loc") != null)
					 	{
							schedaInventario.setColloc(rs.getString("cd_loc").trim());
					 	}

					 	if (rs.getString("spec_loc") != null)
					 	{
							schedaInventario.setSpecific(rs.getString("spec_loc").trim());
					 	}

					 	if (rs.getString("seq_coll") != null)
					 	{
							schedaInventario.setSequenza(rs.getString("seq_coll").trim());
					 	}

						SimpleDateFormat format1 = new SimpleDateFormat("yyMMdd");

						if (rs.getString("data_ingresso") != null)
					 	{
							String dataFormattata = rs.getString("data_ingresso");
							try {
								dataFormattata = format1.format(rs.getDate("data_ingresso"));
							} catch (Exception e) {
								log.error("", e);
							}
					 		schedaInventario.setData(dataFormattata);
					 	}

					 	if (rs.getString("tipo_acquisizione") != null)
					 	{
							schedaInventario.setTipoprov(rs.getString("tipo_acquisizione"));
					 	}

					 	if (rs.getString("cd_mat_inv") != null)
					 	{
							schedaInventario.setTipomat(rs.getString("cd_mat_inv"));
					 	}

					 	if (rs.getString("cd_frui") != null)
					 	{
							schedaInventario.setTipocirc(rs.getString("cd_frui").trim());
					 	}

					 	if (rs.getString("precis_inv") != null)
					 	{
							schedaInventario.setPrecis(rs.getString("precis_inv"));
					 	}
					 	String valoreStr="";
					 	if (rs.getString("valore") != null)
					 	{
						    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
						    dfs.setGroupingSeparator(',');
						    dfs.setDecimalSeparator('.');
						    // controllo formattazione con virgola separatore dei decimali
						    try {
						    	 DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
						    	 // importo
						    	String stringa  = df.format(rs.getDouble("valore"));
						    	 valoreStr=stringa;
						    } catch (Exception e) {
							    	log.error("", e);
							}

					 		//String valoreStr=Pulisci.VisualizzaImporto(rs.getDouble("valore"));
					 		//schedaInventario.setValore(rs.getString("valore"));
					 	}
					 	schedaInventario.setValore(valoreStr);

						scheda.getSchedaInventario().add(schedaInventario);

				 		//SCHEDA SINTESI
						scheda.setSchedaSintesi(new ArrayList<SchedaSintesiVO>());
						SchedaSintesiVO schedaSintesi=new SchedaSintesiVO();
					 	if (rs.getString("cd_biblioteca_sezione") != null)
					 	{
							schedaSintesi.setKbibl(rs.getString("cd_biblioteca_sezione").trim());
					 	}

					 	if (rs.getString("anno_abb") != null)
					 	{
							schedaSintesi.setKsint(rs.getString("anno_abb"));
					 	}

						String appo_cd_sez="";
					 	String appo_cd_loc="";
						String appo_spec_loc="";
						String appo_seq_coll="";
						String appo_segnatura="";
					 	if (rs.getString("cd_sez") != null )
					 	{
					 		appo_cd_sez=rs.getString("cd_sez").trim();
					 		appo_segnatura=appo_segnatura + appo_cd_sez;
					 	}
					 	if ( rs.getString("cd_loc") != null  )
					 	{
					 		appo_cd_loc=rs.getString("cd_loc").trim();
					 		appo_segnatura=appo_segnatura + appo_cd_loc;

					 	}
					 	if ( rs.getString("spec_loc") != null  )
					 	{
					 		appo_spec_loc=rs.getString("spec_loc").trim();
					 		appo_segnatura=appo_segnatura + appo_spec_loc;

					 	}
					 	if (rs.getString("seq_coll") != null )
					 	{
					 		appo_seq_coll=rs.getString("seq_coll").trim();
					 		if (appo_seq_coll.length()>0)
					 		{
						 		appo_segnatura=appo_segnatura +" "+ appo_seq_coll;
					 		}
					 	}
					 	if (rs.getString("cd_biblioteca_sezione") != null )
					 	{
					 		schedaSintesi.setSegnatura(rs.getString("cd_biblioteca_sezione").trim()+ appo_segnatura);
					 	}

				 		// MAIL ROSSANA 16 APRILE
				 		schedaSintesi.setDescr("Collocazione priva di esemplare titolo");
				 		if (rs.getString("cons_doc") != null)
					 	{
							schedaSintesi.setDescr(rs.getString("cons_doc"));
					 	}


						scheda.getSchedaSintesi().add(schedaSintesi);

				 		//SCHEDA ABBONAMENTO
						scheda.setSchedaAbbonamento(new ArrayList<SchedaAbbonamentoVO>());
						SchedaAbbonamentoVO schedaAbbonamento=new SchedaAbbonamentoVO();
						if (rs.getString("ordcdbib") != null )
						{
							schedaAbbonamento.setKbibl(rs.getString("ordcdbib").trim());
						}
					 	String appo_cod_tip_ord="";
						String appo_anno_1ord="";
						String appo_cod_1ord="";
						String appo_anno_ord="";
						String appo_cod_ord="";
						String appo_kordi="";


						if (rs.getInt("anno_1ord")>0 )
						{
							if (rs.getString("cod_tip_ord") != null )
							{
								appo_cod_tip_ord=rs.getString("cod_tip_ord");
								appo_kordi=appo_kordi+appo_cod_tip_ord;
							}

							//appo_anno_1ord=String.valueOf(rs.getInt("anno_1ord")).substring(2,4);
							appo_anno_1ord=String.valueOf(rs.getInt("anno_1ord")).substring(1,4);
							appo_kordi=appo_kordi + appo_anno_1ord;

							appo_cod_1ord="000000";

							if (rs.getString("cod_1ord") != null)
							{
								appo_cod_1ord=String.valueOf(rs.getInt("cod_1ord"));
								// formattazione del codice ordine nel formato di 6 caratteri con aggiunta di zeri in testa
							 	if (appo_cod_1ord!=null && appo_cod_1ord.trim().length()>0)
							 	{
							 		if (appo_cod_1ord.length()<6)
							 		{
							 			String c_cod_ord="000000" + appo_cod_1ord;
							 			int posizStart = c_cod_ord.length()-6;
							 			c_cod_ord=c_cod_ord.substring(posizStart,posizStart+6);
							 			//String cbibl=kbibl.substring(kbibl.length()-3, kbibl.length());
							 			appo_cod_1ord=c_cod_ord;
							 		}
							 		else
							 		{
							 			appo_cod_1ord=appo_cod_1ord.substring(0,6);
							 		}
							 	}
								appo_kordi=appo_kordi+appo_cod_1ord;
							}

							schedaAbbonamento.setKordi(appo_kordi);
						}
						else
						{
							if (rs.getString("cod_tip_ord") != null )
							{
								appo_cod_tip_ord=rs.getString("cod_tip_ord");
								appo_kordi=appo_kordi+appo_cod_tip_ord;
							}
							if (rs.getInt("anno_ord")>0 )
							{
								//appo_anno_ord=String.valueOf(rs.getInt("anno_ord")).substring(2,4);
								appo_anno_ord=String.valueOf(rs.getInt("anno_ord")).substring(1,4);
								appo_kordi=appo_kordi + appo_anno_ord;
							}

							appo_cod_ord="000000";
							if (rs.getString("cod_ord") != null)
							{
								appo_cod_ord=String.valueOf(rs.getInt("cod_ord"));
								// formattazione del codice ordine nel formato di 6 caratteri con aggiunta di zeri in testa
							 	if (appo_cod_ord!=null && appo_cod_ord.trim().length()>0)
							 	{
							 		if (appo_cod_ord.length()<6)
							 		{
							 			String c_cod_ord="000000" + appo_cod_ord;
							 			int posizStart = c_cod_ord.length()-6;
							 			c_cod_ord=c_cod_ord.substring(posizStart,posizStart+6);
							 			//String cbibl=kbibl.substring(kbibl.length()-3, kbibl.length());
							 			appo_cod_ord=c_cod_ord;
							 		}
							 		else
							 		{
							 			appo_cod_ord=appo_cod_ord.substring(0,6);
							 		}
							 	}
								appo_kordi=appo_kordi+appo_cod_ord;
							}

							schedaAbbonamento.setKordi(appo_kordi);
						}
					 	if (rs.getString("anno_abb") != null)
					 	{
							schedaAbbonamento.setKanno(rs.getString("anno_abb"));
					 	}

					 	if (rs.getString("cod_fornitore") != null)
					 	{
							schedaAbbonamento.setKforn(rs.getString("cod_fornitore").trim());
					 	}

					 	schedaAbbonamento.setLivello(null);
						scheda.getSchedaAbbonamento().add(schedaAbbonamento);

					 arrScheda.add(scheda);
				}
				rs.close();
				pstmt.close();
				connection.close();

		} catch (Exception e) {

			valRitorno=false;
			log.error("", e);
		} finally {
			close(connection);
		}
		return arrScheda;
	}

}
