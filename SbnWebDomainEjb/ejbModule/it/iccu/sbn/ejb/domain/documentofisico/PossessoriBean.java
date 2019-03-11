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

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoreListeVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriDiInventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriLegameVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriRicercaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_possessore_provenienzaDao;
import it.iccu.sbn.persistence.dao.documentofisico.Trc_poss_prov_inventariDao;
import it.iccu.sbn.persistence.dao.documentofisico.Trc_possessori_possessoriDao;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_possessore_provenienza;
import it.iccu.sbn.polo.orm.documentofisico.Trc_poss_prov_inventari;
import it.iccu.sbn.polo.orm.documentofisico.Trc_possessori_possessori;
import it.iccu.sbn.util.validation.ValidazioniDocumentoFisico;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.keygenerator.GeneraChiave;
import it.iccu.sbn.web.vo.TreeElementView.KeyDisplayMode;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 *
 * <!-- begin-user-doc --> A generated session bean <!-- end-user-doc -->
 *
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="Inventario" description="A session bean named Inventario"
 *           display-name="Inventario" jndi-name="sbnWeb/Inventario"
 *           type="Stateless" transaction-type="Container" view-type = "remote"
 * @ejb.util generate="no"
 *
 *
 * <!-- end-xdoclet-definition -->
 * @generated
 */
public abstract class PossessoriBean implements SessionBean {

	private static final long serialVersionUID = 8724788180488377473L;

	private Trc_poss_prov_inventariDao daoPossessori;
	private Tbc_possessore_provenienzaDao daoPossessoriProv;
	private Trc_possessori_possessoriDao daoPossPoss ;

	private ValidazioniDocumentoFisico valida;


	public PossessoriBean() {
		valida = new ValidazioniDocumentoFisico();
	}

	public void ejbCreate() {
		return;
	}

	public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
		daoPossessori = new Trc_poss_prov_inventariDao();
		daoPossessoriProv = new Tbc_possessore_provenienzaDao() ;
		daoPossPoss = new Trc_possessori_possessoriDao();
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
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public List getListaPossessori(String codPolo, String codBib,
			PossessoriRicercaVO possRic,int ric,GeneraChiave key) throws RemoteException,
			ApplicationException, DataException, ValidationException {
		List listaPossPerPID = new ArrayList();
		TreeElementViewTitoli rootPossessori = new TreeElementViewTitoli();
		List listaPoss = new ArrayList();
		List listaPossRet = new ArrayList();
		PossessoreListeVO rec = null;
		Trc_poss_prov_inventari possessori = null;
		try {
			CodiceVO poss = new CodiceVO();
			poss.setCodice(codPolo);
			poss.setDescrizione(codBib);
			valida.validaPossessori(poss);
			listaPoss = daoPossessoriProv.getListaPossessori(codPolo, codBib ,possRic,key);
			if ( listaPoss != null && listaPoss.size() > 0) {
				for (int index = 0; index < listaPoss.size(); index++) {
					rec = new PossessoreListeVO();
					Tbc_possessore_provenienza recResult = (Tbc_possessore_provenienza)listaPoss.get(index);
					if (recResult.getTp_forma_pp() == 'R'){
						List rinvio = daoPossPoss.getListaRinviiFigli(recResult.getPid());//rosa
						Trc_possessori_possessori recFormaAccettata = ((Trc_possessori_possessori)rinvio.get(0));//rosa
						String formaAccettata = new String(recFormaAccettata.getPid_base().getPid() + "   " + recFormaAccettata.getPid_base().getDs_nome_aut());

						rec.setNomeFormaAccettata(formaAccettata);
					}else{
						rec.setNomeFormaAccettata(null);
					}
					rec.setNome(recResult.getDs_nome_aut());
					rec.setPrg( ""+ (index + 1));
					rec.setPid(recResult.getPid());
					rec.setForma(""+recResult.getTp_forma_pp());
					rec.setTipoNome(""+recResult.getTp_nome_pp());
					rec.setLivello(""+recResult.getCd_livello());
					listaPossRet.add(rec);
				}
			} else if (listaPoss == null || listaPoss.size() == 0) {
				throw new DataException("noPoss");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {
			throw new DataException(e);
		} catch (Exception e) {
			throw new DataException(e);
		}
		return listaPossRet;
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
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public List getListaPossessori_1(String codPolo, String codBib,
			PossessoriRicercaVO possRic,int ric,GeneraChiave key) throws RemoteException,
			ApplicationException, DataException, ValidationException {
		List listaPoss = new ArrayList();
		List listaPossRet = new ArrayList();
		PossessoreListeVO rec = null;
		Trc_poss_prov_inventari possessori = null;
		try {
			CodiceVO poss = new CodiceVO();
			poss.setCodice(codPolo);
			poss.setDescrizione(codBib);
			valida.validaPossessori(poss);
			listaPoss = daoPossessoriProv.getListaPossessori_1(codPolo, codBib ,possRic,key);
			if ( listaPoss != null && listaPoss.size() > 0) {
				for (int index = 0; index < listaPoss.size(); index++) {
					rec = new PossessoreListeVO();
					Tbc_possessore_provenienza recResult = (Tbc_possessore_provenienza)listaPoss.get(index);
					if (recResult.getTp_forma_pp() == 'R'){
						List rinvio = daoPossPoss.getListaRinviiFigli(recResult.getPid());//rosa
						Trc_possessori_possessori recFormaAccettata = ((Trc_possessori_possessori)rinvio.get(0));//rosa
						String formaAccettata = new String(recFormaAccettata.getPid_base().getPid() + "   " + recFormaAccettata.getPid_base().getDs_nome_aut());

						rec.setNomeFormaAccettata(formaAccettata);
					}else{
						rec.setNomeFormaAccettata(null);
					}
					rec.setNome(recResult.getDs_nome_aut());
					rec.setPrg( ""+ (index + 1));
					rec.setPid(recResult.getPid());
					rec.setForma(""+recResult.getTp_forma_pp());
					rec.setTipoNome(""+recResult.getTp_nome_pp());
					rec.setLivello(""+recResult.getCd_livello());
					listaPossRet.add(rec);
				}
			} else if (listaPoss == null || listaPoss.size() == 0) {
				throw new DataException("noPoss");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {
			throw new DataException(e);
		} catch (Exception e) {
			throw new DataException(e);
		}
		return listaPossRet;
	}

	public String inserisciPossessori(PossessoriDettaglioVO possDett,String polo, String uteFirma , String ticket,GeneraChiave key )
		throws RemoteException,	ApplicationException,  ValidationException  {
		String PID = "" ;
		boolean possTrovato ;
		String risultatoIns = null ;
		// rimettere
		//valida.validaPossessori(possDett);
		try {
			// genera PID per inserimento nuovo possessore
			PID = daoPossessoriProv.calcolaCodPID(polo);
			possDett.setPid(PID);
			if (PID != null) {
				// verifica esistenza del possessore con il PID appena generato
				possTrovato = ((daoPossessoriProv.getCountPossessoreByPid(PID)) > 0) ?true:false;
				if (!possTrovato) {
					// il Pid generato è univoco posso inserire
					daoPossessoriProv.setSessionCurrentCfg();
					risultatoIns = daoPossessoriProv.inserimentoPossessore(possDett, uteFirma,key);
					//List rinvio = daoPossPoss.getListaRinvii(PID);

				}

			} else {
				throw new ValidationException("erratoInserimento",ValidationException.errore);
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DaoManagerException e) {
			throw new DataException(e);
		}  catch (Exception e) {
			e.printStackTrace();
			throw new DataException(e);
		}
		return risultatoIns;
	}

	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO creaRichiestaAnaliticoPossessoriPid(AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass, String polo ,String bib , String ticket)	throws RemoteException {
		String pid_da_sintetica = areaDatiPass.getBidRicerca();
		List listaPossPerPID = new ArrayList();
		PossessoriRicercaVO possRic = new PossessoriRicercaVO();
		possRic.setPid(pid_da_sintetica);
		possRic.setChkTipoNomeA(false);
		possRic.setChkTipoNomeB(false);
		possRic.setChkTipoNomeC(false);
		possRic.setChkTipoNomeD(false);
		possRic.setChkTipoNomeE(false);
		possRic.setChkTipoNomeR(false);
		possRic.setChkTipoNomeG(false);
		possRic.setFormaAutore("");
		possRic.setNome("");
		possRic.setTipoOrdinamSelez("1");
		TreeElementViewTitoli rootPossessori = new TreeElementViewTitoli();

		try {
			//estraggo la lista dei possessori per il singolo pid selezionato dalla sintetica di partenza
			listaPossPerPID = daoPossessoriProv.getListaPossessori(polo, bib ,possRic,null);
			if (listaPossPerPID.size() > 0) {
				Tbc_possessore_provenienza recResult = (Tbc_possessore_provenienza)listaPossPerPID.get(0);
				if (recResult.getTp_forma_pp() == 'A'){
					// il pid da sintetica era una forma A - Accettata e quindi è la radice e posso crearla da subito
					int invCount = (daoPossessori.countInventariPerPossessore(pid_da_sintetica)).intValue() ;
					//in questo modo nel treeView setto un area dati relativa al dettaglio del nodo corrente
					rootPossessori.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setNome(recResult.getDs_nome_aut());
					//rootPossessori.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setDataIns(recResult.getTs_ins());
					//rootPossessori.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setDataVar(recResult.getTs_var());
					rootPossessori.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setDataIns(DateUtil.formattaData(recResult.getTs_ins().getTime()));
					rootPossessori.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setDataVar(DateUtil.formattaData(recResult.getTs_var().getTime()));

					rootPossessori.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setLivello(recResult.getCd_livello());
					rootPossessori.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setForma(""+recResult.getTp_forma_pp());
					rootPossessori.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setTipoTitolo(""+recResult.getTp_nome_pp());
					rootPossessori.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setPid(recResult.getPid());
					rootPossessori.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setNota(recResult.getNote());
					rootPossessori.setKey(pid_da_sintetica);
					rootPossessori.setText(recResult.getDs_nome_aut());
					rootPossessori.setDescription(recResult.getDs_nome_aut());
					rootPossessori.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
					rootPossessori.setTipoAuthority(SbnAuthority.PP);
					if (invCount < 1)
						rootPossessori.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
					else
						rootPossessori.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
					//(se il possessore ha inventari associati questo valore deve diventare TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO)
					rootPossessori.setRadioVisible(true);
					rootPossessori.setFlagCondiviso(false);
					// fine della radice dei possessori
					// ora verifico se esistono rinvii
					List rinvio = daoPossPoss.getListaRinvii(pid_da_sintetica);
					if (rinvio.size()>0){
						//esistono rinvii e quindi devo estrarre tutti i possessori che hanno il PID =  alla lista estratta
						String codiciPid = "(";
						for (int index = 0; index < rinvio.size(); index++) {
							Trc_possessori_possessori possPoss = (Trc_possessori_possessori)rinvio.get(index);
							codiciPid += "'" + possPoss.getPid_coll()+"',";
						}
						codiciPid = codiciPid.substring(0, codiciPid.length()-1);
						codiciPid +=")";
						//getListaPossessoriPerCodiciPid
						List listaPossPerPIDRinvio  = daoPossessoriProv.getListaPossessoriPerCodiciPid(codiciPid);
						for (int index = 0; index < listaPossPerPIDRinvio.size(); index++) {
							Tbc_possessore_provenienza possProvRinvio = (Tbc_possessore_provenienza)listaPossPerPIDRinvio.get(index);
							TreeElementViewTitoli figlio = (TreeElementViewTitoli)rootPossessori.addChild();
							figlio.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setNome(possProvRinvio.getDs_nome_aut());
							figlio.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setDataIns(DateUtil.formattaData(possProvRinvio.getTs_ins().getTime()));
							figlio.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setDataVar(DateUtil.formattaData(possProvRinvio.getTs_var().getTime()));
							figlio.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setLivello(possProvRinvio.getCd_livello());
							figlio.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setForma(""+possProvRinvio.getTp_forma_pp());
							figlio.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setTipoTitolo(""+possProvRinvio.getTp_nome_pp());
							figlio.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setPid(possProvRinvio.getPid());
							figlio.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setNota(possProvRinvio.getNote());
							// della lista poss_poss estratta ,per ogni poss_prov ,determino il relativo poss_poss e valorizzo altri attributi del figlio corrente
							for (int index1 = 0; index1 < rinvio.size(); index1++) {
								Trc_possessori_possessori possPoss = (Trc_possessori_possessori)rinvio.get(index1);
								if (possPoss.getPid_coll().getPid().trim().equals(possProvRinvio.getPid())){
									figlio.getAreaDatiDettaglioOggettiVO().getPossessoreLegameVO().setNotaAlLegame(possPoss.getNota());
									figlio.getAreaDatiDettaglioOggettiVO().getPossessoreLegameVO().setTipoLegame(""+possPoss.getTp_legame());
								}
							}

							figlio.setKey(possProvRinvio.getPid());
							figlio.setText(possProvRinvio.getDs_nome_aut());
							figlio.setDescription(possProvRinvio.getDs_nome_aut());
							figlio.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
							figlio.setTipoAuthority(SbnAuthority.PP);
							figlio.setPossessoreFormaRinvio(true);
							figlio.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
							//(se il possessore ha inventari associati questo valore deve diventare TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO)
							figlio.setRadioVisible(true);
							figlio.setFlagCondiviso(false);
						}
					}

				} else if (recResult.getTp_forma_pp() == 'R'){
					List rinvio = daoPossPoss.getListaRinviiFigli(pid_da_sintetica);
					String pid_padre = ((Trc_possessori_possessori)rinvio.get(0)).getPid_base().getPid();
					possRic = new PossessoriRicercaVO();
					possRic.setPid(pid_padre);
					possRic.setChkTipoNomeA(false);
					possRic.setChkTipoNomeB(false);
					possRic.setChkTipoNomeC(false);
					possRic.setChkTipoNomeD(false);
					possRic.setChkTipoNomeE(false);
					possRic.setChkTipoNomeR(false);
					possRic.setChkTipoNomeG(false);
					possRic.setFormaAutore("");
					possRic.setNome("");
					possRic.setTipoOrdinamSelez("1");
					listaPossPerPID = daoPossessoriProv.getListaPossessori(polo, bib ,possRic,null);
					Tbc_possessore_provenienza recResultApp = (Tbc_possessore_provenienza)listaPossPerPID.get(0);
					if (recResultApp.getTp_forma_pp() == 'A'){
						//in questo modo nel treeView setto un area dati relativa al dettaglio del nodo corrente
						rootPossessori.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setNome(recResultApp.getDs_nome_aut());
						rootPossessori.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setDataIns(DateUtil.formattaData(recResultApp.getTs_ins().getTime()));
						rootPossessori.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setDataVar(DateUtil.formattaData(recResultApp.getTs_var().getTime()));
						rootPossessori.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setLivello(recResultApp.getCd_livello());
						rootPossessori.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setForma(""+recResultApp.getTp_forma_pp());
						rootPossessori.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setTipoTitolo(""+recResultApp.getTp_nome_pp());
						rootPossessori.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setPid(recResultApp.getPid());
						rootPossessori.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setNota(recResultApp.getNote());
						// creata la radice della forma di rinvio interrogata
						int invCount = (daoPossessori.countInventariPerPossessore(pid_padre)).intValue() ;
						rootPossessori.setKey(pid_padre);
						rootPossessori.setText(recResultApp.getDs_nome_aut());
						rootPossessori.setDescription(recResultApp.getDs_nome_aut());
						rootPossessori.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
						rootPossessori.setTipoAuthority(SbnAuthority.PP);
						if (invCount < 1)
							rootPossessori.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
						else
							rootPossessori.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
						//rootPossessori.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
						//(se il possessore ha inventari associati questo valore deve diventare TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO)
						rootPossessori.setRadioVisible(true);
						rootPossessori.setFlagCondiviso(false);
						List rinvioApp = daoPossPoss.getListaRinvii(pid_padre);
						if (rinvioApp.size()>0){
							//esistono rinvii e quindi devo estrarre tutti i possessori che hanno il PID =  alla lista estratta
							String codiciPid = "(";
							for (int index = 0; index < rinvioApp.size(); index++) {
								Trc_possessori_possessori possPoss = (Trc_possessori_possessori)rinvioApp.get(index);
								codiciPid += "'" + possPoss.getPid_coll()+"',";
							}
							codiciPid = codiciPid.substring(0, codiciPid.length()-1);
							codiciPid +=")";
							//getListaPossessoriPerCodiciPid
							List listaPossPerPIDRinvio  = daoPossessoriProv.getListaPossessoriPerCodiciPid(codiciPid);
							for (int index = 0; index < listaPossPerPIDRinvio.size(); index++) {
								Tbc_possessore_provenienza possProvRinvio = (Tbc_possessore_provenienza)listaPossPerPIDRinvio.get(index);
								TreeElementViewTitoli figlio = (TreeElementViewTitoli)rootPossessori.addChild();
								//in questo modo nel treeView setto un area dati relativa al dettaglio del nodo corrente
								figlio.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setNome(possProvRinvio.getDs_nome_aut());
								figlio.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setDataIns(DateUtil.formattaData(possProvRinvio.getTs_ins().getTime()));
								figlio.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setDataVar(DateUtil.formattaData(possProvRinvio.getTs_var().getTime()));
								figlio.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setLivello(possProvRinvio.getCd_livello());
								figlio.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setForma(""+possProvRinvio.getTp_forma_pp());
								figlio.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setTipoTitolo(""+possProvRinvio.getTp_nome_pp());
								figlio.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setPid(possProvRinvio.getPid());
								figlio.getAreaDatiDettaglioOggettiVO().getPossessoreDettaglioVO().setNota(possProvRinvio.getNote());
								for (int index1 = 0; index1 < rinvioApp.size(); index1++) {
									Trc_possessori_possessori possPoss = (Trc_possessori_possessori)rinvioApp.get(index1);
									if (possPoss.getPid_coll().getPid().trim().equals(possProvRinvio.getPid())){
										figlio.getAreaDatiDettaglioOggettiVO().getPossessoreLegameVO().setNotaAlLegame(possPoss.getNota());
										figlio.getAreaDatiDettaglioOggettiVO().getPossessoreLegameVO().setTipoLegame(""+possPoss.getTp_legame());
									}
								}

								figlio.setKey(possProvRinvio.getPid());
								figlio.setText(possProvRinvio.getDs_nome_aut());
								figlio.setDescription(possProvRinvio.getDs_nome_aut());
								figlio.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
								figlio.setTipoAuthority(SbnAuthority.PP);
								figlio.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
								figlio.setPossessoreFormaRinvio(true);
								//(se il possessore ha inventari associati questo valore deve diventare TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO)
								figlio.setRadioVisible(true);
								figlio.setFlagCondiviso(false);
							}
						}

					}
				}

			}
		} catch (DaoManagerException e) {
			throw new DataException(e);
		} catch (Exception e) {
			throw new DataException(e);
		}
		rootPossessori.open();
		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO result = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO();
		result.setTreeElementViewTitoli(rootPossessori);
		result.setCodErr("0000");
		return result;
	}


	public String inserisciLegameAlPossessori(PossessoriDettaglioVO dettaglioPos ,String polo, String uteFirma , String ticket,GeneraChiave key,String notaLegame,String tipoLegame)
		throws RemoteException,	ApplicationException,  ValidationException  {
		String Pid_figlio = "" ;
		boolean possTrovato ;
		String Pid_padre="";
		String risultatoIns = null ;
		// rimettere
		//valida.validaPossessori(possDett);
		try {
			// genera PID per inserimento nuovo possessore
			PossessoriDettaglioVO possDett = dettaglioPos.copy();
			Pid_padre = possDett.getPid(); // pid a cui agganciare il legame che vado a creare
			Pid_figlio = daoPossessoriProv.calcolaCodPID(polo);
			possDett.setPid(Pid_figlio);
			if (Pid_figlio != null) {
				// verifica esistenza del possessore con il PID appena generato
				possTrovato = (daoPossessoriProv.getCountPossessoreByPid(Pid_figlio) > 0) ?true:false;
				if (!possTrovato) {
					// il Pid generato è univoco posso inserire
					risultatoIns = daoPossessoriProv.inserimentoPossessore(possDett, uteFirma,key);
					List rinvio = daoPossPoss.getListaRinvii(Pid_figlio);
					if (rinvio.size() == 0){
						PossessoriLegameVO possLegame = new PossessoriLegameVO();
						possLegame.setNotaAlLegame(notaLegame);
						possLegame.setTipoLegame(tipoLegame);
						possLegame.setPidPadre(Pid_padre);
						possLegame.setPidFiglio(Pid_figlio);
						risultatoIns = daoPossPoss.inserimentoLegamePossessore(possLegame, uteFirma);
					}

				}

			} else {
				throw new ValidationException("erratoInserimento",ValidationException.errore);
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DaoManagerException e) {
			throw new DataException(e);
		}  catch (Exception e) {

			throw new DataException(e);
		}
		return risultatoIns;
	}


	public String modificaNotaLegame(String pid_padre,String pid_figlio ,String notaLegame,String uteFirma)throws RemoteException {
		String ret="";
		try {
			List possposs = daoPossPoss.getPossessorePossessore(pid_figlio, pid_padre);
			Trc_possessori_possessori pos_pos = (Trc_possessori_possessori) possposs.get(0);
			pos_pos.setNota(notaLegame);
			ret = daoPossPoss.modificaNotaLegame(pid_padre, pid_figlio, notaLegame,uteFirma , pos_pos);
		} catch (Exception e) {
			throw new DataException(e);
		}
		return ret;
	}
	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO scambioLegame(String pid_padre,String pid_figlio ,String uteFirma)throws RemoteException {
		String ret="";
		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO result = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO();
		try {
			List<Tbc_possessore_provenienza> possposs = daoPossessoriProv.getPossessoreByPid(pid_padre);
			Tbc_possessore_provenienza possessore_A = possposs.get(0);
			Tbc_possessore_provenienza possessore_Appoggio = new Tbc_possessore_provenienza();

			possessore_Appoggio.setTp_forma_pp(possessore_A.getTp_forma_pp());
			possessore_Appoggio.setKy_auteur(possessore_A.getKy_auteur());
			possessore_Appoggio.setKy_cautun(possessore_A.getKy_cautun());
			possessore_Appoggio.setKy_cles1_a(possessore_A.getKy_cles1_a());
			possessore_Appoggio.setKy_cles2_a(possessore_A.getKy_cles2_a());
			possessore_Appoggio.setKy_el1(possessore_A.getKy_el1());
			possessore_Appoggio.setKy_el2(possessore_A.getKy_el2());
			possessore_Appoggio.setKy_el3(possessore_A.getKy_el3());
			possessore_Appoggio.setKy_el4(possessore_A.getKy_el4());
			possessore_Appoggio.setKy_el5(possessore_A.getKy_el5());
			possessore_Appoggio.setNote(possessore_A.getNote());
			possessore_Appoggio.setCd_livello(possessore_A.getCd_livello());
			possessore_Appoggio.setTot_inv(possessore_A.getTot_inv());
			possessore_Appoggio.setCd_livello(possessore_A.getCd_livello());
			possessore_Appoggio.setFl_speciale(possessore_A.getFl_speciale());
			possessore_Appoggio.setDs_nome_aut(possessore_A.getDs_nome_aut());
			possessore_Appoggio.setFl_canc(possessore_A.getFl_canc());

			possposs.clear();
			possposs = daoPossessoriProv.getPossessoreByPid(pid_figlio);
			Tbc_possessore_provenienza possessore_B = possposs.get(0);
			String codice_Pid_Poss_A = possessore_A.getPid();
			String codice_Pid_Poss_B = possessore_B.getPid();

			//imposto in modo invertito i pid sui possessori determinati
			//possessore_A.setTp_forma_pp(possessore_B.getTp_forma_pp());
			possessore_A.setKy_auteur(possessore_B.getKy_auteur());
			possessore_A.setKy_cautun(possessore_B.getKy_cautun());
			possessore_A.setKy_cles1_a(possessore_B.getKy_cles1_a());
			possessore_A.setKy_cles2_a(possessore_B.getKy_cles2_a());
			possessore_A.setKy_el1(possessore_B.getKy_el1());
			possessore_A.setKy_el2(possessore_B.getKy_el2());
			possessore_A.setKy_el3(possessore_B.getKy_el3());
			possessore_A.setKy_el4(possessore_B.getKy_el4());
			possessore_A.setKy_el5(possessore_B.getKy_el5());
			possessore_A.setNote(possessore_B.getNote());
			possessore_A.setCd_livello(possessore_B.getCd_livello());
			//possessore_A.setTot_inv(possessore_B.getTot_inv());
			possessore_A.setFl_speciale(possessore_B.getFl_speciale());
			possessore_A.setDs_nome_aut(possessore_B.getDs_nome_aut());
			//possessore_A.setFl_canc(possessore_B.getFl_canc());
			possessore_A.setUte_var(uteFirma);

			//possessore_B.setTp_forma_pp(possessore_Appoggio.getTp_forma_pp());
			possessore_B.setKy_auteur(possessore_Appoggio.getKy_auteur());
			possessore_B.setKy_cautun(possessore_Appoggio.getKy_cautun());
			possessore_B.setKy_cles1_a(possessore_Appoggio.getKy_cles1_a());
			possessore_B.setKy_cles2_a(possessore_Appoggio.getKy_cles2_a());
			possessore_B.setKy_el1(possessore_Appoggio.getKy_el1());
			possessore_B.setKy_el2(possessore_Appoggio.getKy_el2());
			possessore_B.setKy_el3(possessore_Appoggio.getKy_el3());
			possessore_B.setKy_el4(possessore_Appoggio.getKy_el4());
			possessore_B.setKy_el5(possessore_Appoggio.getKy_el5());
			possessore_B.setNote(possessore_Appoggio.getNote());
			possessore_B.setCd_livello(possessore_Appoggio.getCd_livello());
			//possessore_B.setTot_inv(possessore_Appoggio.getTot_inv());
			possessore_B.setCd_livello(possessore_Appoggio.getCd_livello());
			possessore_B.setFl_speciale(possessore_Appoggio.getFl_speciale());
			possessore_B.setDs_nome_aut(possessore_Appoggio.getDs_nome_aut());
			//possessore_B.setFl_canc(possessore_Appoggio.getFl_canc());
			possessore_B.setUte_var(uteFirma);

			daoPossessoriProv.modificaPossessorePerScambioForma(possessore_A);
			daoPossessoriProv.modificaPossessorePerScambioForma(possessore_B);

//			Trc_possessori_possessori poss_poss = ((Trc_possessori_possessori)(daoPossPoss.getPossessorePossessore(codice_Pid_Poss_B, codice_Pid_Poss_A).get(0)));
//
//			Trc_possessori_possessori poss_poss_appo = new Trc_possessori_possessori();
//			poss_poss_appo.setFl_canc(poss_poss.getFl_canc());
//			poss_poss_appo.setNota(poss_poss.getNota());
//			poss_poss_appo.setTp_legame(poss_poss.getTp_legame());
//			poss_poss_appo.setTs_ins(poss_poss.getTs_ins());
//			poss_poss_appo.setUte_ins(poss_poss.getUte_ins());
//
//			daoPossPoss.eliminaLegamePossessore(poss_poss.getPid_coll().getPid(), poss_poss.getPid_base().getPid(), uteFirma, poss_poss);
////			daoPossPoss.cancellaLegamePossessore(poss_poss.getPid_coll().getPid(), poss_poss.getPid_base().getPid(), uteFirma, poss_poss);
//			daoPossPoss.inserisciNuovoLegameDopoScambio(codice_Pid_Poss_B, codice_Pid_Poss_A, uteFirma, poss_poss_appo);
//

			result.setCodErr("0000");

		} catch (Exception e) {
			result.setCodErr("0101");
			result.setTestoProtocollo("Impossibile effettuare lo scambio di forma.");
			throw new DataException(e);
		}
		return result;
	}
	public String modificaPossessori(PossessoriDettaglioVO possDett,String polo, String uteFirma , String ticket,GeneraChiave key )
		throws RemoteException,	ApplicationException,  ValidationException  {
		String PID = "" ;
		boolean possTrovato ;
		String risultatoIns = null ;
		try {
			possTrovato = ((daoPossessoriProv.getCountPossessoreByPid(possDett.getPid())) > 0) ?true:false;
			if (possTrovato) {
				Tbc_possessore_provenienza possProv = (daoPossessoriProv.getPossessoreByPid(possDett.getPid()).get(0));
				risultatoIns = daoPossessoriProv.modificaPossessore(possDett, uteFirma,key,possProv);
			}
		} catch (DaoManagerException e) {
			throw new DataException(e);
		}  catch (Exception e) {
			throw new DataException(e);
		}
		return risultatoIns;
	}

	// almaviva2 Aprile 2015: gestione nuova funzionalità per fusione fra Possessori in uscita da una richiesta di
	// variazioneDescrizione (nuove funzionalità: Torna al dettaglio; Fondi oggetti;)
	public String fondiPossessori(PossessoriDettaglioVO possDett, String polo, String uteFirma , String ticket, GeneraChiave key)
	throws RemoteException,	ApplicationException,  ValidationException  {

		String retFusione = "0000";
		String retCanc = "";
		int retSposta = 0;
		int retCancResidui = 0;
		int retAggProvenienze = 0;
		try {
		// sequenza delle variazioni:
		// 1. cancellazione del possessore (tabella Tbc_possessore_provenienza - cancellaPossessore)
		// 2. spostamento legami ad inventario via aggiornamento del pidPartenza con PidArrivo (tabella Trc_poss_prov_inventari - spostaLegamiPerInventario)
		// 3. cancellazione legami tipo POSSESSORE che creerebbero duplicazione in arrivo (tabella Trc_poss_prov_inventari - cancellaLegamePossInventPerPid)
		// 4. aggiornamento legami tipo POSSESSORE IN PROVENIENZA  (tabella Trc_poss_prov_inventari - aggiornaTipoLegamePossProv)

			retCanc = daoPossessoriProv.cancellaPossessore(possDett.getPid(), uteFirma);
			if (retCanc.length() > 0) {
				retFusione = retFusione + "<br/>" + "Cancellazione possessore " + possDett.getPid() + ": OK;" + "<br/>";
				retSposta = daoPossessori.spostaLegamiPerInventario(possDett.getPid(), possDett.getPidArrivoDiFusione(), uteFirma);
				retFusione = retFusione + "Spostamento di "+ String.valueOf(retSposta) +" legami ad inventari da " + possDett.getPid() +
							" a " + possDett.getPidArrivoDiFusione() + ": OK;" + "<br/>";
				retCancResidui = daoPossessori.cancellaLegamePossInventPerPid(possDett.getPid(), uteFirma);
				retFusione = retFusione + "Cancellazione di "+ String.valueOf(retCancResidui) +" legami residuali per duplicazione: OK;" + "<br/>";
				retAggProvenienze = daoPossessori.aggiornaTipoLegamePossProv(possDett.getPid(), possDett.getPidArrivoDiFusione(), uteFirma);
				retFusione = retFusione + "Aggiornamento di "+ String.valueOf(retAggProvenienze) +" legami dal tipo POSSESSORE al tipo PROVENIENZA: OK;" + "<br/>";
			}

		} catch (DaoManagerException e) {
			//throw new DataException(e);
			retFusione = "9999" + e.getMessage();

		}  catch (Exception e) {
			//throw new DataException(e);
			retFusione = "9999" + e.getMessage();
	}
	return retFusione;
}


	public String modificaLegameAlPossessori(PossessoriDettaglioVO possDett,String polo, String uteFirma ,
			String ticket,GeneraChiave key,String notaLegame,String tipoLegame,String pidOrigine)
	throws RemoteException,	ApplicationException,  ValidationException  {
	String Pid_figlio = "" ;
	boolean possTrovato ;
	String Pid_padre="";
	String risultatoIns = null ;

	try {
		// verifica esistenza del possessore
		possTrovato = (daoPossessoriProv.getCountPossessoreByPid(possDett.getPid()) > 0) ?true:false;
		if (possTrovato) {

			Tbc_possessore_provenienza possProv = (daoPossessoriProv.getPossessoreByPid(possDett.getPid())).get(0) ;
			Trc_possessori_possessori possPoss =  (Trc_possessori_possessori)(daoPossPoss.getPossessorePossessore(possDett.getPid(), pidOrigine)).get(0);

			//posso fare update di questa riga
			daoPossessoriProv.modificaPossessore(possDett, uteFirma, key, possProv);
			daoPossPoss.modificaNotaLegame(pidOrigine, possDett.getPid(), notaLegame, uteFirma, possPoss);

		}
	}  catch (DaoManagerException e) {
		throw new DataException(e);
	}  catch (Exception e) {

		throw new DataException(e);
	}
		return risultatoIns;
	}

		public List getListaPossessoriDiInventario(String codSerie, String codInv , String codBib,String codPolo , String ticket ,int nRec) throws RemoteException,
				ApplicationException, DataException, ValidationException {
			List listaPoss = new ArrayList();
			List listaPossRet = new ArrayList();
			PossessoriDiInventarioVO rec = null;
			Trc_poss_prov_inventari possessori = null;
			try {

//				listaPoss = daoPossessori.getListaPossessoriInventario(codPolo, codBib, codInv);
				listaPoss = daoPossessori.getListaPossessoriDiInventario(codPolo, codBib, codSerie, Integer.parseInt(codInv) );
				//getListaPossessoriDiInventario(codPolo, codBib, codSerie, codInv);
				if (listaPoss.size() > 0) {
					for (int index = 0; index < listaPoss.size(); index++) {
						Object[] obj = (Object[])listaPoss.get(index);
						Trc_poss_prov_inventari possProvInv = (Trc_poss_prov_inventari)obj[0];
						Tbc_possessore_provenienza possProv = (Tbc_possessore_provenienza)obj[1];

						rec = new PossessoriDiInventarioVO();
						rec.setPid(possProv.getPid());
						rec.setNome(possProv.getDs_nome_aut());
						rec.setCodBib(possProvInv.getCd_polo().getCd_serie().getCd_polo().getCd_biblioteca());
						rec.setCodInv(""+possProvInv.getCd_polo().getCd_inven());
						rec.setCodPolo(possProvInv.getCd_polo().getCd_serie().getCd_polo().getCd_polo().getCd_polo());
						rec.setCodSerie(possProvInv.getCd_polo().getCd_serie().getCd_serie());
						rec.setNotaLegame(possProvInv.getNota());
						rec.setCodLegame(""+possProvInv.getCd_legame());
						rec.setUteAgg(possProvInv.getUte_var());
						rec.setUteIns(possProvInv.getUte_ins());
						rec.setDataAgg(possProvInv.getTs_var());
						rec.setDataIns(possProvInv.getTs_ins());
						if (possProvInv.getCd_legame() == 'P'){
							rec.setForma("Possessore");
						} else if (possProvInv.getCd_legame() == 'R'){
							rec.setForma("Provenienza");
						}
						listaPossRet.add(rec);
					}
				} else if (listaPoss.size() == 0) {
					throw new DataException("noPoss");
				}
			} catch (DataException e) {
				throw e;
			} catch (DaoManagerException e) {
				throw new DataException(e);
			} catch (Exception e) {
				throw new DataException(e);
			}
			return listaPossRet;
		}

	//legamePossessoreInventario (PossessoriDiInventarioVO possInv)
		public String legamePossessoreInventario (PossessoriDiInventarioVO possInv)
			throws RemoteException,	ApplicationException,  ValidationException  {

		String risultatoIns = null ;
		int ct=0;
		try {//getListaInventariPossessori(String codPolo, String codBib, String pid)
			List <Trc_poss_prov_inventari> listaPossInvPid = daoPossessori.getListaInventariPossessori(possInv.getCodPolo(),possInv.getCodBib(),possInv.getPid());
			for (int i=0;i<listaPossInvPid.size();i++){
				Trc_poss_prov_inventari poss_prov_inventari = listaPossInvPid.get(i);
				if (possInv.getCodInv().trim().equals(""+poss_prov_inventari.getCd_polo().getCd_inven()  ) ){
					throw new DataException("error.documentofisico.PossessoriInventarioDuplicato");
				}
			}

			risultatoIns = daoPossessori.legamePossessoreInventario(possInv, null);
		}  catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {
			throw new DataException(e);
		}  catch (Exception e) {

			throw new DataException(e);
		}
			return risultatoIns;
		}

public String modificaLegamePossessoreInventario (PossessoriDiInventarioVO possInv)
		throws RemoteException,	ApplicationException,  ValidationException  {

	String risultatoIns = null ;
	int ct=0;
	try {//getListaInventariPossessori(String codPolo, String codBib, String pid)
		/*
		List <Trc_poss_prov_inventari> listaPossInvPid = daoPossessori.getListaInventariPossessori(possInv.getCodPolo(),possInv.getCodBib(),possInv.getPid());
		for (int i=0;i<listaPossInvPid.size();i++){
			Trc_poss_prov_inventari poss_prov_inventari = (Trc_poss_prov_inventari)listaPossInvPid.get(i);
			if (possInv.getCodInv().trim().equals(""+poss_prov_inventari.getCd_polo().getCd_inven()  ) ){
				throw new DataException("error.documentofisico.PossessoriInventarioDuplicato");
			}
		}
		 */
		//almaviva2 14/01/2010
		int invLegameProv = 0;
		if (possInv.getCodLegame() != null){
			invLegameProv = daoPossessori.countPossessoriPerInventario(possInv.getCodPolo(), possInv.getCodBib(),
					possInv.getCodSerie(), possInv.getCodInv(),  possInv.getCodLegame());
		}
		Trc_poss_prov_inventari poss_prov_inventari = null;
		if (invLegameProv > 0){
			if (possInv != null && !possInv.getCodLegame().equals("P")){
				List <Trc_poss_prov_inventari> listaPossInvPid = daoPossessori.getListaInventariPossessoriBib(possInv.getCodPolo(),possInv.getCodBib(),
						possInv.getCodSerie(), possInv.getCodInv(), possInv.getCodLegame());
				if (listaPossInvPid.size() == 1){
					poss_prov_inventari = listaPossInvPid.get(0);
					if (possInv != null && possInv.getCodLegame().equals("P")){

					}else if (possInv != null && possInv.getCodLegame().equals("R")){
						if (!possInv.getPid().equals(poss_prov_inventari.getP().getPid())){
							throw new DataException("error.documentofisico.ProvenienzaInventarioDuplicato");
						}
					}
				}
			}
		}
		risultatoIns = daoPossessori.legamePossessoreInventario(possInv, poss_prov_inventari);
	}  catch (DataException e) {
		throw e;
	} catch (DaoManagerException e) {
		throw new DataException(e);
	}  catch (Exception e) {

		throw new DataException(e);
	}
		return risultatoIns;
	}


// almaviva7 03/07/2008 11.17
// Logica di cancellazione rivista (era toppata).
public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO cancellaLegamePossessore(String pid_padre,
		String pid_figlio , String polo ,String bib , String uteFirma)	throws RemoteException {
	AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO result = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO();
	try {
		/*
 Alessandro
		int numInvPoss = (daoPossessori.countInventariPerPossessore(bib, polo, pid_padre)).intValue();
		if (numInvPoss==0){
			if (pid_figlio.trim().equals("")){
				//la cancellazione avviene partendo dalla radice libera da inventari e quindi elimino lei e le sue forme di rinvio se esistono
				if (daoPossessoriProv.getPossessoreByPid(pid_padre).size() == 0) {
					result.setCodErr("0101");
					result.setTestoProtocollo("Impossibile eliminare il possessore selezionato.Disallineamento sulla base dati.");
					return result ;
				}
				//cancello la forma ACCETTATA
				daoPossessoriProv.cancellaPossessore(pid_padre, uteFirma);

				//daoPossPoss.cancellaLegamePossessore(PidDaCancellare, PidPadre, uteFirma)
				List listaRinvii = daoPossPoss.getListaRinvii(pid_padre);
				if (listaRinvii.size()>0){
					//esistono rinvii e quindi devo estrarre tutti i possessori che hanno il PID =  alla lista estratta
					String codiciPid = "(";
					for (int index = 0; index < listaRinvii.size(); index++) {
						Trc_possessori_possessori possPoss = (Trc_possessori_possessori)listaRinvii.get(index);
						codiciPid += "'" + possPoss.getPid_coll()+"',";
					}
					codiciPid = codiciPid.substring(0, codiciPid.length()-1);
					codiciPid +=")";
					//cancello le forme RINVIO
					if ( daoPossessoriProv.getListaPossessoriPerCodiciPid(codiciPid).size() != listaRinvii.size() )  {
						result.setCodErr("0101");
						result.setTestoProtocollo("Impossibile eliminare il possessore selezionato.Disallineamento sulla base dati.");
						return result ;
					}

					daoPossessoriProv.cancellaPossessore(codiciPid, uteFirma);
					daoPossPoss.cancellaPossPoss(codiciPid, uteFirma);
				}
			} else {
				if (daoPossessoriProv.getPossessoreByPid(pid_figlio).size() == 0) {
					result.setCodErr("0101");
					result.setTestoProtocollo("Impossibile eliminare il possessore selezionato.Disallineamento sulla base dati.");
					return result ;
				}
				if (daoPossessoriProv.getPossessoreByPid(pid_padre).size() == 0) {
					result.setCodErr("0101");
					result.setTestoProtocollo("Impossibile eliminare il possessore selezionato.Disallineamento sulla base dati.");
					return result ;
				}
				List possposs = daoPossPoss.getPossessorePossessore(pid_figlio, pid_padre);
				Trc_possessori_possessori pos_pos = (Trc_possessori_possessori) possposs.get(0);
				daoPossessoriProv.cancellaPossessore(pid_figlio, uteFirma);
				daoPossPoss.cancellaLegamePossessore(pid_figlio, pid_padre, uteFirma,pos_pos);
			}
			result.setCodErr("0000");
		} else {
			result.setCodErr("0101");
			result.setTestoProtocollo("Impossibile eliminare il possessore selezionato.");
		}
		 */
		// Se pid_figlio esiste cancelliamo il pid_figlio e la sua relazione con il pid_padre
		// in quanto non ci sono vincoli
		if (pid_figlio.length() > 0){
			if (daoPossessoriProv.getPossessoreByPid(pid_figlio).size() == 0) {
				result.setTestoProtocollo("Impossibile eliminare il possessore selezionato. Non presente in base dati.");
				result.setCodErr("0101");
				return result ;
			}
			List possposs = daoPossPoss.getPossessorePossessore(pid_figlio, pid_padre);
			Trc_possessori_possessori pos_pos = (Trc_possessori_possessori) possposs.get(0);
			daoPossessoriProv.cancellaPossessore(pid_figlio, uteFirma);
			daoPossPoss.cancellaLegamePossessore(pid_figlio, pid_padre, uteFirma,pos_pos);
			result.setCodErr("0000");
		} else {

			// Se pid_figlio NON esiste si intende cancellare la forma accettata del possessore.
			// In questo caso bisogna controllare che non vi siano legami agli inventari per poter rimuovere la forma accettata

//			int numInvPoss = (daoPossessori.countInventariPerPossessore(polo, bib, pid_padre)).intValue();//almaviva2 23/11/2009
			int numInvPoss = daoPossessori.countInventariPerPossessore(((pid_padre)), polo, bib);//almaviva2 23/11/2009
			if (numInvPoss==0){
				if (pid_figlio.trim().equals("")){
					//la cancellazione avviene partendo dalla radice libera da inventari e quindi elimino lei e le sue forme di rinvio se esistono
					if (daoPossessoriProv.getPossessoreByPid(pid_padre).size() == 0) {
						result.setCodErr("0101");
						result.setTestoProtocollo("Impossibile eliminare il possessore selezionato.Disallineamento sulla base dati.");
						return result ;
					}

					// Prima cancello tutti i RINVII
					List listaRinvii = daoPossPoss.getListaRinvii(pid_padre);
					if (listaRinvii.size()>0){
						//esistono rinvii e quindi devo estrarre tutti i possessori che hanno il PID =  alla lista estratta
						String codiciPid = "(";
						for (int index = 0; index < listaRinvii.size(); index++) {
							Trc_possessori_possessori possPoss = (Trc_possessori_possessori)listaRinvii.get(index);
							codiciPid += "'" + possPoss.getPid_coll()+"',";
						}
						codiciPid = codiciPid.substring(0, codiciPid.length()-1);
						codiciPid +=")";
						//cancello le forme RINVIO
						if ( daoPossessoriProv.getListaPossessoriPerCodiciPid(codiciPid).size() != listaRinvii.size() )  {
							result.setCodErr("0101");
							result.setTestoProtocollo("Impossibile eliminare il possessore selezionato.Disallineamento sulla base dati.");
							return result ;
						}

						daoPossessoriProv.cancellaPossessore(codiciPid, uteFirma);
						daoPossPoss.cancellaPossPoss(codiciPid, uteFirma);
					}
					//Poi cancello la forma ACCETTATA
					daoPossessoriProv.cancellaPossessore(pid_padre, uteFirma);
				}
			} else {
				// Ci sono deli inventari legati al possessore e finche' non vengono riimossi non si puo' cancellare il possessore
				result.setTestoProtocollo("Non si puo' cancellare un possessore in forma accettata se ha dei legami con inventari.");
				result.setCodErr("0101");
				return result ;
			}

		}

	} catch (Exception e) {
		throw new DataException(e);
	}
	return result;
}

// almaviva7 03/07/2008 12.46
public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO cancellaLegameInventario(String pid, String codiceInventario, String polo ,String bib , String uteFirma)	throws RemoteException {
	AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO result = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO();
	try {
		daoPossessori.cancellaLegamePossessoreInventario(bib, pid, codiceInventario, uteFirma);
		result.setCodErr("0000");
	} catch (Exception e) {
		throw new DataException(e);
	}
	return result;
}




} // End PossessoriBean



