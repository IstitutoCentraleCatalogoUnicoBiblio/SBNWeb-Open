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
package it.iccu.sbn.ejb.domain.documentofisico;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.services.bibliografica.ServiziBibliografici;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.ForSezMiscVO;
import it.iccu.sbn.ejb.vo.documentofisico.TitoloVO;
import it.iccu.sbn.ejb.vo.gestionestampe.EtichettaDettaglioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampeOnlineVO;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_inventarioDao;
import it.iccu.sbn.persistence.dao.documentofisico.Trc_formati_sezioniDao;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_collocazione;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_sezione_collocazione;
import it.iccu.sbn.polo.orm.documentofisico.Trc_formati_sezioni;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.util.TitoloHibernate;
import it.iccu.sbn.util.validation.ValidazioniDocumentoFisico;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.naming.NamingException;
/**
 *
 * <!-- begin-user-doc -->
 * A generated session bean
 * <!-- end-user-doc -->
 *
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="DocumentoFisicoCommon" description="A session bean named DocumentoFisicoCommon"
 * 			display-name="DocumentoFisicoCommon"
 *          jndi-name="sbnWeb/DocumentoFisicoCommon" type="Stateless"
 *          transaction-type="Container" view-type = "remote"
 * @ejb.util generate="no"
 *
 *
 * <!-- end-xdoclet-definition -->
 * @generated
 */
public abstract class DocumentoFisicoCommonBean extends TicketChecker implements DocumentoFisicoCommon {

	private static final long serialVersionUID = -1458815380883832540L;
	private Tbc_inventarioDao daoInv;
	private Trc_formati_sezioniDao daoForSez;
    private ServiziBibliografici servizi;
    private ValidazioniDocumentoFisico valida;
	private Inventario inventario;
    Connection connection;
    private SessionContext session;

    public DocumentoFisicoCommonBean()   {
        valida = new ValidazioniDocumentoFisico();
       connection = null;
    }

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.create-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
    public void ejbCreate()
    {
    }

    public void setSessionContext(SessionContext sc)
        throws EJBException, RemoteException
    {
		session = sc;
		daoInv = new Tbc_inventarioDao();
        try {
        	servizi = DomainEJBFactory.getInstance().getSrvBibliografica();

        } catch(NamingException e) {
            e.printStackTrace();
        } catch(CreateException e) {
            e.printStackTrace();
        }
    }

//	/**
//	 *
//	 * <!-- begin-xdoclet-definition -->
//	 *
//	 * @throws ResourceNotFoundException
//	 * @throws ApplicationException
//	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
//	 * @generated
//	 *
//	 * //TODO: Must provide implementation for bean create stub
//	 */
//    public List getTitolo(String bid, String ticket)
//        throws DataException, ApplicationException, ValidationException
//    {
//        TitoloVO recTit = new TitoloVO();
//        List lista = new ArrayList();
//        try {
//            AreaDatiServizioInterrTitoloCusVO risposta = servizi.servizioDatiTitoloPerBid(bid, ticket);
//            recTit.setBid(risposta.getBid());
//            recTit.setIsbd(risposta.getTitoloResponsabilita());
//            recTit.setNatura(risposta.getNatura());
//            if(risposta.getCodErr().equals("0000")) {
//                lista.add(recTit);
//            } else {
//                return null;
//            }
//        } catch(RemoteException e1) {
//            e1.printStackTrace();
//        } catch(DAOException e1){
//            throw new DataException(e1);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new DataException (e);
//       }
//        return lista;
//    }
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
    public List<TitoloVO> getTitolo(String bid, String ticket)
    throws DataException, ApplicationException, ValidationException    {
    	List<TitoloVO> lista = new ArrayList<TitoloVO>(); // output di un singolo titolo ricavato
		daoInv = new Tbc_inventarioDao();
    	TitoloVO recTit = null;
    	String tit_isbd = "";
    	try	{
    		if (bid == null || (bid!=null && bid.trim().length()==0)){
    			throw new ValidationException("validaCollBidObbligatorio", ValidationException.errore);
    		}
    		if (bid != null &&  bid.length()!=0)	{
    			if (bid.length()>10)	{
    				throw new ValidationException("validaInvBidEccedente", ValidationException.errore);
    			}
    		}
    		Tb_titolo titolo = daoInv.getTitoloDF1(bid);
    		if (titolo == null) {
    			throw new DataException("titoloNonTrovatoSuBDPolo");
    		}else{
    			recTit = new TitoloVO();
    			tit_isbd=titolo.getIsbd();
    			recTit.setBid(titolo.getBid());
    			recTit.setIsbd(titolo.getIsbd());
    			//
   				String indice_isbd=titolo.getIndice_isbd();
   				String[] indice = indice_isbd.split(";");

				String tit_terzaParte = null;
				String[] isbd_composto=tit_isbd.split("\\. -");
				// se si splitta (lunghe>0) allora considero la prima parte, altrimenti tutto
				String[] indice_isbd_composto=indice_isbd.split("\\;");
				if (indice.length > 1){
					for (int y = 1; y < indice.length; y++) {
						if (indice[y].startsWith("215")){
							String[] iDueValori = indice[y].split("-");
							String secondoValore = iDueValori[1];
							int numeroAree = indice.length;
							int posizioneSuccessiva = y + 1;
							if (posizioneSuccessiva < numeroAree){
								String[] gliAltriDueValori = indice[posizioneSuccessiva].split("-");
								String altroSecondoValore = gliAltriDueValori[1];
								tit_terzaParte = ". - " + titolo.getIsbd().substring((Integer.parseInt(secondoValore) - 1), (Integer.parseInt(altroSecondoValore) - 3));
							}else{
								tit_terzaParte = ". - " + titolo.getIsbd().substring(Integer.parseInt(secondoValore) - 1);
							}
						}
					}
					String tit_primaParte=isbd_composto[0];
					String tit_secondaParte = "";
					if (isbd_composto.length > 1){
						tit_secondaParte=isbd_composto[1];
					}
					String  tit_secondaParte_finale="";
					String  tit_primaParte_finale="";
					String  tit_isbd_finale="";
					if  (!tit_isbd.equals("") &&  tit_isbd.length() > 200){
						// gestione composizione titolo

						if (indice_isbd_composto.length > 0){
							if (indice_isbd_composto[0]!=null && indice_isbd_composto[0].length()!=0){
								String primaParte=indice_isbd_composto[0];
								String[] primaParte_composto=primaParte.split("-");
								String pos_primaParte=primaParte_composto[1];

								if (tit_primaParte.length()>100){
									tit_primaParte_finale=tit_primaParte.substring(0,100);
								}else{
									tit_primaParte_finale=tit_primaParte;
								}
							}
							if (indice_isbd_composto.length>1){
								String secondaParte=indice_isbd_composto[1];
								String[] secondaParte_composto=secondaParte.split("\\-");
								String pos_secondaParte=secondaParte_composto[1];
								if (tit_secondaParte.length()>100){
									tit_secondaParte_finale=tit_secondaParte.substring(0,100);
								}else{
									tit_secondaParte_finale=tit_secondaParte;
								}
							}
						}
						if (!tit_primaParte_finale.equals("") &&  tit_primaParte_finale.length()>0){
							tit_isbd_finale=tit_primaParte_finale.trim();
							if (!tit_secondaParte_finale.equals("") &&  tit_secondaParte_finale.length() > 0){
								tit_isbd_finale=tit_isbd_finale + ". - " +  tit_secondaParte_finale.trim();
							}
						}
						if (!tit_isbd_finale.equals("") &&  tit_isbd_finale.length()>0)	{
							recTit.setIsbd(tit_isbd_finale);
						}
					}
					if (tit_terzaParte != null){
						if (!tit_isbd_finale.equals("") &&  tit_isbd_finale.length()>0){
							recTit.setIsbd(tit_isbd_finale + tit_terzaParte);
						}
					}
					recTit.setTerzaParte(tit_terzaParte);
				}

    			recTit.setNatura(String.valueOf(titolo.getCd_natura()));
    			if (String.valueOf(titolo.getFl_condiviso()).equalsIgnoreCase("S")){
           			recTit.setFlagCondiviso(true);
    			}else{
           			recTit.setFlagCondiviso(false);
    			}
       			recTit.setTipoMateriale(String.valueOf(titolo.getTp_materiale()));


    		}
    		lista.add(recTit);

    	} catch (ValidationException e) {
    		throw e;
    	} catch (DataException e) {
    		throw e;
    	} catch (DaoManagerException e) {
    		e.printStackTrace();
    		throw new DataException(e);
    	} catch (Exception e) {
    		e.printStackTrace();
    		throw new DataException(e);
    	}
    	return lista;
    }
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
    public List getTitoloHib(Tb_titolo titolo, String ticket)
    throws DataException, ApplicationException, ValidationException    {
    	return TitoloHibernate.getTitoloHib(titolo, ticket);
    }
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public void assegnaSerieProgr(CollocazioneVO recColl, Timestamp ts,
			Tbc_sezione_collocazione sezione, int serie, int num, String codMatInv)
	throws DataException, ApplicationException, ValidationException {
		Trc_formati_sezioni formatiSezione;
		try{
			//almaviva5_20151013 controllo sez. a spazio
			boolean isSezSpazio = ValidazioneDati.trimOrEmpty(sezione.getCd_sez()).length() == 0;
			valida.validaFormatiSezioni(recColl.getCodPolo(), recColl.getCodBib(),
					recColl.getRecFS().getCodSez(), recColl.getRecFS().getCodFormato(), isSezSpazio);
			daoForSez = new Trc_formati_sezioniDao();
			formatiSezione = daoForSez.getFormatiSezioni4update(recColl.getRecFS().getCodPolo(), recColl.getRecFS().getCodBib(),
					recColl.getRecFS().getCodSez(),	recColl.getRecFS().getCodFormato());
			if (formatiSezione != null){
				if (recColl.getRecFS().getProgSerie() == -1 && recColl.getRecFS().getProgNum() == -1){
					//calcola numero automaticamente
					if (sezione.getCd_colloc() == DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO.charAt(0)){
						//						formatiSezione.setProg_num(formatiSezione.getProg_num() + 1);
						serie = (formatiSezione.getProg_serie());
						num =(formatiSezione.getProg_num() + 1);
					}else{
						if (ValidazioneDati.in(codMatInv, "OP", "MO")) {//codMatInv = null modifica collocazione
								if (formatiSezione.getProg_num() >= formatiSezione.getN_pezzi()){
									//progNum al limite: incremento serie e progNum a 1
									serie = (formatiSezione.getProg_serie() + 1);
									num = 1;
								}else{
									serie = (formatiSezione.getProg_serie());
									num = (formatiSezione.getProg_num() + 1);
								}
								ForSezMiscVO fsmVO =  this.calcolaProgrMiscellanea(serie, num, formatiSezione);
								if (fsmVO != null){
									serie = fsmVO.getNum1();
									num = fsmVO.getNum2();
									formatiSezione = fsmVO.getRec();
									if (fsmVO.getMsg() != null && !fsmVO.getMsg().equals("")){
										recColl.setMsg(fsmVO.getMsg());
										recColl.setSerieIncompleta(String.valueOf(fsmVO.getSerie()));
										recColl.setNumMancanti(String.valueOf(fsmVO.getMsg()));
									}
								}
//							}
						}else{ //non opuscolo
							if (formatiSezione.getProg_num() >= formatiSezione.getN_pezzi()){
								//progNum al limite: incremento serie e progNum a 1
								serie = (formatiSezione.getProg_serie() + 1);
								num = 1;
							}else{
								serie = (formatiSezione.getProg_serie());
								num = (formatiSezione.getProg_num() + 1);
							}
						}
					}
				}else{  //recupero collocazione cancellata
					if (sezione.getCd_colloc() == DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO.charAt(0)){
						//segnalato da rossana dopo rilascio
						String formato = recColl.getCodColloc().substring(0, 2);
						String coll = recColl.getCodColloc().substring(2);
						String spec = recColl.getSpecColloc();
						if (formato.equals(formatiSezione.getCd_formato())){
							//						if (formatiSezione.getProgr_num1_misc() >  0 && formatiSezione.getProgr_num2_misc() > 0){
							if (((Integer.valueOf(coll) == (formatiSezione.getProg_serie_num1_misc())) ||
									(Integer.valueOf(coll) == formatiSezione.getProg_serie_num2_misc())) &&
									//almaviva5_20110708 #4561 recupero coll. coincidente con ultima miscellanea collocata
									//((Integer.valueOf(spec) > formatiSezione.getProgr_num1_misc() &&
									((Integer.valueOf(spec) > formatiSezione.getProgr_num1_misc() &&
											Integer.valueOf(spec) <= formatiSezione.getProgr_num2_misc()))){
								throw new ValidationException("numeroRiservatoAIntervalloMiscellanea",
										ValidationException.errore);
							}
							//						}
						}
					}
					//
					//inserisce un progressivo non ancora assegnato ma minore dell'ultimo
					//assegnato automaticamente (buco nella numerazione)
						if (sezione.getCd_colloc() == DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO.charAt(0)){
							//						formatiSezione.setProg_num(recColl.getRecFS().getProgNum());
							serie = (formatiSezione.getProg_serie());
							num = (recColl.getRecFS().getProgNum());

						}else{ //tipo coll. formato o continuazione
							serie = (recColl.getRecFS().getProgSerie());
							num = (recColl.getRecFS().getProgNum());
							/*
							if (formatiSezione.getProg_num() >= formatiSezione.getN_pezzi()){
								//progNum al limite: incremento serie e progNum a 1
								serie = (recColl.getRecFS().getProgSerie() + 1);
								num = 1;
							}else{
								serie = (recColl.getRecFS().getProgSerie());
								num = (recColl.getRecFS().getProgNum());
							}

							if (ValidazioneDati.in(codMatInv, "OP", "MO")) {//codMatInv = null modifica collocazione
								ForSezMiscVO fsmVO =  this.calcolaProgrMiscellanea(serie, num, formatiSezione);
								if (fsmVO != null){
									serie = fsmVO.getNum1();
									num = fsmVO.getNum2();
									formatiSezione = fsmVO.getRec();
									if (fsmVO.getMsg() != null && !fsmVO.getMsg().equals("")){
										recColl.setMsg(fsmVO.getMsg());
									}
								}
							}
							*/
						}
//					{
//
//						if (codMatInv != null && codMatInv != null && codMatInv.equals("OP")){//codMatInv = null modifica collocazione
//							if (formatiSezione.getProg_num() >= formatiSezione.getN_pezzi()){
//								//progNum al limite: incremento serie e progNum a 1
//								serie = (recColl.getRecFS().getProgSerie() + 1);
//								num = 1;
//							}else{
//								serie = (recColl.getRecFS().getProgSerie());
//								num = (recColl.getRecFS().getProgNum());
//							}
//						}else{
//							if (formatiSezione.getProg_num() >= formatiSezione.getN_pezzi()){
//								//progNum al limite: incremento serie e progNum a 1
//								serie = (recColl.getRecFS().getProgSerie() + 1);
//								num = 1;
//							}else{
//								serie = (recColl.getRecFS().getProgSerie());
//								num = (recColl.getRecFS().getProgNum()+1);
//							}
//						}
//						if (codMatInv != null && codMatInv != null && codMatInv.equals("OP")){//codMatInv = null modifica collocazione
//							ForSezMiscVO fsmVO =  this.calcolaProgrMiscellanea(serie, num, formatiSezione);
//							if (fsmVO != null){
//								serie = fsmVO.getNum1();
//								num = fsmVO.getNum2();
//								formatiSezione = fsmVO.getRec();
//								if (fsmVO.getMsg() != null && !fsmVO.getMsg().equals("")){
//									recColl.setMsg(fsmVO.getMsg());
//								}
//							}
//						}
//					}
				}
			}
			if (sezione.getCd_colloc() == DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO.charAt(0)){
				recColl.setCodColloc(String.valueOf(num));
				recColl.setSpecColloc("");
			}else if(sezione.getCd_colloc() == DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO.charAt(0)
					|| sezione.getCd_colloc() == DocumentoFisicoCostant.COD_CONTINUAZIONE.charAt(0)){
				recColl.setCodColloc(recColl.getRecFS().getCodFormato() + String.valueOf(serie));
				recColl.setSpecColloc(String.valueOf(num));
			}
			if (recColl.getRecFS().getProgSerie() == -1 && recColl.getRecFS().getProgNum() == -1){
				if((sezione.getCd_colloc() == DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO.charAt(0)
						|| sezione.getCd_colloc() == DocumentoFisicoCostant.COD_CONTINUAZIONE.charAt(0))
				&&  (codMatInv != null && (codMatInv.equals("OP") || codMatInv.equals("MO")))){
				}else{
					formatiSezione.setProg_serie(serie);
					formatiSezione.setProg_num(num);
				}
				formatiSezione.setUte_ins(recColl.getRecFS().getUteAgg());
				formatiSezione.setTs_var(ts);
				sezione.getTrc_formati_sezioni().add(formatiSezione);
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DaoManagerException e) {
			e.printStackTrace();
			throw new DataException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}

	/**
	 * @param serie
	 * @param num
	 * @param formatiSezione
	 * @throws ValidationException
	 */
	private ForSezMiscVO calcolaProgrMiscellanea(int serie, int num,
			Trc_formati_sezioni formatiSezione) throws ValidationException {
		int n1 = 0;
		int n2 = 0;
		int x = 0;
		int appoSerie = 0;
		//appoSerie serve per riportare nel msg di segnalazione
		//la serie abbandonata su cui si è creato il buco nella numerazione
		ForSezMiscVO rec = new ForSezMiscVO();
		if (formatiSezione.getN_pezzi_misc() > 0){
			if ((formatiSezione.getProg_serie_num1_misc() == formatiSezione.getProg_serie_num2_misc()) &&
					formatiSezione.getProgr_num1_misc() == formatiSezione.getProgr_num2_misc()){
				formatiSezione.setProg_serie_num1_misc(0);
				formatiSezione.setProg_serie_num2_misc(0);
				formatiSezione.setProgr_num1_misc(0);
				formatiSezione.setProgr_num2_misc(0);
			}
			if (formatiSezione.getProgr_num1_misc() == 0 && formatiSezione.getProgr_num2_misc() == 0){
				n1 = num;
				n2 = (n1 + formatiSezione.getN_pezzi_misc()) - 1;
				if (n2 <= formatiSezione.getN_pezzi()){
					formatiSezione.setProgr_num1_misc(n1);
					formatiSezione.setProg_serie_num1_misc(serie);
					formatiSezione.setProgr_num2_misc(n2);
					formatiSezione.setProg_serie_num2_misc(serie);
					formatiSezione.setProg_num(n2);
					formatiSezione.setProg_serie(serie);
					serie = (formatiSezione.getProg_serie());
					num = n1;
				}else{
					appoSerie = serie;
					//trattamento con scatola spezzata su due serie
					// in questo caso x = n.pezzi che cadono nella nuova serie
//					x = (n2 - formatiSezione.getN_pezzi()); //
//					formatiSezione.setProgr_num1_misc(n1);
//					//formatiSezione.setProg_serie_num1_misc non cambia
//					formatiSezione.setProgr_num2_misc(x);
//					formatiSezione.setProg_serie_num2_misc(serie + 1);
//
//					formatiSezione.setProg_serie(formatiSezione.getProg_serie_num2_misc());
//					formatiSezione.setProg_num(formatiSezione.getProgr_num2_misc());
//					serie = (formatiSezione.getProg_serie_num1_misc());
//					num = n1;
					//fine trattamento con scatola spezzata su due serie
					//
					//trattamento con scatola su serie nuova
					// in questo caso x = n.pezzi che mancano al completamento della serie corrente
					x = formatiSezione.getN_pezzi() - formatiSezione.getProg_num();
					if (n2 > formatiSezione.getN_pezzi() && x < formatiSezione.getN_pezzi_misc()){
						formatiSezione.setProg_serie_num1_misc(0);
						formatiSezione.setProg_serie_num2_misc(0);
						formatiSezione.setProgr_num1_misc(0);
						formatiSezione.setProgr_num2_misc(0);

						formatiSezione.setProg_serie_num1_misc(serie + 1);
						formatiSezione.setProgr_num1_misc(1);
						formatiSezione.setProg_serie_num2_misc(formatiSezione.getProg_serie_num1_misc());
//						formatiSezione.setProgr_num2_misc(formatiSezione.getN_pezzi_misc() - 1);
						formatiSezione.setProgr_num2_misc(formatiSezione.getN_pezzi_misc());//segnalato da rossana dopo rilascio

						formatiSezione.setProg_serie(formatiSezione.getProg_serie_num2_misc());
						formatiSezione.setProg_num(formatiSezione.getProgr_num2_misc());

						serie = (formatiSezione.getProg_serie_num1_misc());
						num = formatiSezione.getProgr_num1_misc();

//						throw new ValidationException("numPezziDisponibiliMinoreDiNumeroPezziDaRiservare",
//								ValidationException.errore, "numPezziDisponibiliMinoreDiNumeroPezziDaRiservare",
//								String.valueOf(serie -1), String.valueOf(x));
					}
					//fine trattamento con scatola su serie nuova


				}
			}else{
				formatiSezione.setProgr_num1_misc(formatiSezione.getProgr_num1_misc() + 1);
				if (formatiSezione.getProgr_num1_misc() > formatiSezione.getN_pezzi()){
					formatiSezione.setProgr_num1_misc(0);
					formatiSezione.setProgr_num1_misc(formatiSezione.getProgr_num1_misc() + 1);
					formatiSezione.setProg_serie_num1_misc(formatiSezione.getProg_serie());
					serie = (formatiSezione.getProg_serie_num1_misc());
					num = formatiSezione.getProgr_num1_misc();
				}else{
					serie = (formatiSezione.getProg_serie_num1_misc());
					num = formatiSezione.getProgr_num1_misc();
				}
			}

		}else{
			if (formatiSezione.getProgr_num1_misc() > 0 || formatiSezione.getProgr_num2_misc() > 0){
				//errore
				throw new ValidationException("numPezziMiscNonImpostatoEONum1eoNum2Maggiori0",
						ValidationException.errore);
			}

		}
		rec.setNum1(serie);
		rec.setNum2(num);
		rec.setRec(formatiSezione);
		if (x > 0){
			rec.setMsg("SI");
			rec.setSerie(appoSerie);
			rec.setNumMancanti(x);
		}
		return rec;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws RemoteException
	 * @throws ApplicationException
	 * @throws DataException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public StampeOnlineVO stampeOnline(String ticket, StampaType tipoStampa,
			List parametri) throws RemoteException, ApplicationException,
			DataException, ValidationException {

		StampeOnlineVO stampeOnline = new StampeOnlineVO();
		stampeOnline.setParametriInput(parametri);

		switch (tipoStampa) {
		case STAMPA_REGISTRO_CONSERVAZIONE:
			return eseguiStampaRegistroConservazione(stampeOnline);
		case STAMPA_REGISTRO_TOPOGRAFICO:
			return eseguiStampaRegistroTopografico(stampeOnline);
		}

		return stampeOnline;
	}

	private StampeOnlineVO eseguiStampaRegistroTopografico(
			StampeOnlineVO stampeOnline) {
		List nomiCampi = new ArrayList();
		nomiCampi.add("polo");
		nomiCampi.add("biblioteca");
		nomiCampi.add("sezione");
		nomiCampi.add("collocazione");
		nomiCampi.add("specificazione");
		nomiCampi.add("totale inventari");
		nomiCampi.add("isbd");
		nomiCampi.add("n. inv.");

		stampeOnline.setDescrizioneCampi(nomiCampi);

		List righeDati = new ArrayList();

		List riga1 = new ArrayList();
		riga1.add("CSW");
		riga1.add(" FI");
		riga1.add("111");
		riga1.add("prima collocazione");
		riga1.add("prima spec");
		riga1.add("1");

		riga1.add("Il nome della rosa");
		riga1.add("3");

		righeDati.add(riga1);

		// List subRiga = new ArrayList();
		// subRiga.add(getTitoloInventario("Il nome della rosa", "3"));
		// riga1.add(subRiga);
		// righeDati.add(riga1);

		List riga2 = new ArrayList();
		riga2.add("CSW");
		riga2.add(" FI");
		riga2.add("111");
		riga2.add("seconda collocazione");
		riga2.add("seconda spec");
		riga2.add("2");
		riga2.add("Il nome della violetta");
		riga2.add("2223");

		righeDati.add(riga2);

		List riga3 = new ArrayList();
		riga3.add("CSW");
		riga3.add(" FI");
		riga3.add("111");
		riga3.add("seconda collocazione");
		riga3.add("seconda spec");
		riga3.add("2");
		riga3
				.add("Il nome della dalia ncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndn");
		riga3.add("1113");

		righeDati.add(riga3);
		stampeOnline.setRigheDatiDB(righeDati);
		return stampeOnline;
	}

	private StampeOnlineVO eseguiStampaRegistroConservazione(
			StampeOnlineVO stampeOnline) {
		Tbc_inventarioDao daoInv = new Tbc_inventarioDao();
		EsameCollocRicercaVO parametriRicerca = (EsameCollocRicercaVO) stampeOnline
				.getParametriInput().get(0);
		try {
			stampeOnline.setRigheDatiDB(daoInv
					.getListaInventari(parametriRicerca));
		} catch (DaoManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stampeOnline;
	}

	/**
	 * @param listaEtichette
	 * @param inventario
	 */
	public void scriviDatiEtichetta(List listaEtichette, Tbc_inventario inventario, String tipoOperazione, String descrBibEtichetta)
	throws RemoteException{
		try{
			EtichettaDettaglioVO outputDettaglio = new EtichettaDettaglioVO();
			if (descrBibEtichetta == null){
				outputDettaglio.setBiblioteca("Descrizione Temporanea");
			}else{
				outputDettaglio.setBiblioteca(descrBibEtichetta);
			}
//			if (inventario.getCd_serie().getCd_polo().getDs_biblioteca() == null){
//				outputDettaglio.setBiblioteca("Descrizione Temporanea");
//			}else{
//				outputDettaglio.setBiblioteca(inventario.getCd_serie().getCd_polo().getId_biblioteca().getNom_biblioteca().trim());
//			}
			outputDettaglio.setSerie(inventario.getCd_serie().getCd_serie());
			outputDettaglio.setInventario(String.valueOf(inventario.getCd_inven()));
			outputDettaglio.setSequenza(inventario.getSeq_coll());
//			if (inventario.getKey_loc() == null || inventario.getCd_sit() != '2'){
//				outputDettaglio.setSezione("");
//				outputDettaglio.setCollocazione("Inventario non collocato");
//				outputDettaglio.setSpecificazione("");
//			}else{
				Tbc_collocazione coll = inventario.getKey_loc();
				outputDettaglio.setSezione(coll.getCd_sez().getCd_sez());
				outputDettaglio.setCollocazione(coll.getCd_loc());
				outputDettaglio.setSpecificazione(coll.getSpec_loc());
				//almaviva5_20110914 #4621 aggiunti campi ordinamento collocazione
				outputDettaglio.setOrd_loc(coll.getOrd_loc());
				outputDettaglio.setOrd_spec(coll.getOrd_spec());
//			}
			listaEtichette.add(outputDettaglio);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

