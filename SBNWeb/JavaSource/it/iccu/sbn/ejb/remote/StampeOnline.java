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
package it.iccu.sbn.ejb.remote;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.OutputStampaVo;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaBibliotecheVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaFornitoriVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaSoggettarioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaUtentiVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampeOnlineVO;
import it.iccu.sbn.ejb.vo.stampe.StampaOnLineVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJBObject;

public interface StampeOnline extends EJBObject {

    public StampeOnlineVO stampeOnline(String ticket, StampaType tipoStampa,
			List<?> parametri) throws RemoteException, ApplicationException,
			DataException, ValidationException;


//	public String stampaCataloghi(StampaCataloghiVO stampaCataloghiVO) throws RemoteException;
//  public Collection<StampaSoggettarioVO> stampaSoggettario_(StampaSoggettarioVO stampaSoggettarioVO) throws RemoteException;
//  public Collection<StampaUtentiVO> stampaUtenti_(StampaUtentiVO stampaUtentiVO) throws RemoteException;
//	public String stampaBibliotecari(StampaBibliotecariVO stampaBibliotecariVO) throws RemoteException;
//	public String stampaBuoniCarico(StampaBuoniCaricoVO stampaBuoniCaricoVO) throws RemoteException;
//	public String stampaClassificazione(StampaClassificazioneVO stampaClassificazioneVO) throws RemoteException;
//	public String stampaRegistroConservazione(StampaRegistroConservazioneVO stampaRegistroConservazioneVO) throws RemoteException;
//	public String stampaListe(StampaListeVO stampaListeVO) throws RemoteException;
//	public String stampaOrdini(StampaOrdiniVO stampaOrdiniVO) throws RemoteException;
//	public String stampaPeriodici(StampaPeriodiciVO stampaPeriodiciVO) throws RemoteException;
//	public String stampaRepertori(StampaRepertoriVO stampaRepertoriVO) throws RemoteException;
//	public String stampaRichiesteIll(StampaRichiesteIllVO stampaRichiesteIllVO) throws RemoteException;
//	public String stampaRichiesteLoc(StampaRichiesteLocVO stampaRichiesteLocVO) throws RemoteException;
//  public Collection<StampaSchedeVO> stampaSchede_(StampaSchedeVO stampaSchedeVO) throws RemoteException;
//	public String stampaSolleciti(StampaSollecitiVO stampaSollecitiVO) throws RemoteException;
//	public String stampaStoricoIll(StampaStoricoIllVO stampaStoricoIllVO) throws RemoteException;
//	public String stampaStoricoLoc(StampaStoricoLocVO stampaStoricoLocVO) throws RemoteException;
//	public String stampaTabelle(StampaTabelleVO stampaTabelleVO) throws RemoteException;
//	public String stampaThesauroPolo(StampaThesauroPoloVO stampaThesauroPoloVO) throws RemoteException;
//	public String stampaRegistroTopografico(StampaRegistroTopograficoVO stampaRegistroTopograficoVO) throws RemoteException;

    ///////////////////
    // stampe batch //
    /////////////////
    public Collection<StampaFornitoriVO> stampaFornitori_(StampaFornitoriVO stampaFornitoriVO) throws RemoteException;
    public Collection<StampaBibliotecheVO> stampaBiblioteche_(StampaBibliotecheVO stampaBibliotecheVO) throws RemoteException;
    public String stampaFornitori(StampaFornitoriVO stampaFornitoriVO) throws RemoteException;
	public String stampaSoggettario(StampaSoggettarioVO stampaSoggettarioVO) throws RemoteException;
	public String stampaUtenti(StampaUtentiVO stampaUtentiVO) throws RemoteException;
	public String stampaBiblioteche(StampaBibliotecheVO stampaBibliotecheVO) throws RemoteException;
	public String stampaSchede(StampaVo stampa) throws RemoteException;
	public String stampaUtente(StampaVo stampa) throws RemoteException;
	public String stampaFornitori(StampaVo stampa) throws RemoteException;
	public String stampaBiblioteche(StampaVo stampa) throws RemoteException;
	public String stampaBuoniOrdine(StampaVo stampa) throws RemoteException;
	public String stampaTerminiThesauro(StampaVo stampa) throws RemoteException;
	public String stampaBollettario(StampaVo stampa) throws RemoteException;
	public String stampaFattura(StampaVo stampa) throws RemoteException;
	public String stampaComunicazione(StampaVo stampa) throws RemoteException;
	public String stampaSuggerimentiBibliotecario(StampaVo stampa) throws RemoteException;
	public String stampaSuggerimentiLettore(StampaVo stampa) throws RemoteException;
	public String stampaSpese(StampaVo stampa) throws RemoteException;
	//docFis
	public String stampaEtichette(StampaVo stampa) throws RemoteException;
	public String stampaRegistroIngresso(StampaVo stampa) throws RemoteException;
	public String stampaStatisticheRegistroIngresso(StampaVo stampa) throws RemoteException;
	public String stampaRegistroTopografico(StampaVo stampa) throws RemoteException;//solo .pdf
	public String stampaRegistroConservazione(StampaVo stampa) throws RemoteException;//solo .pdf
	public String stampaStrumentiPatrimonio(StampaVo stampa) throws RemoteException;//conservazione, bollettino xls
	public String stampaBollettino(StampaVo stampa) throws RemoteException;
	public String stampaBuoniCarico(StampaVo stampa) throws RemoteException;
	//servizi
	public String stampaServiziCorrenti(StampaVo stampa) throws RemoteException;
	public String stampaServiziStorico(StampaVo stampa) throws RemoteException;
	//statistiche
	public String elaboraStatistica(StampaVo stampa) throws RemoteException;
	//periodici
	public String stampaFascicoli(StampaVo stampa) throws RemoteException;
	//editori
	public String stampaTitoliEditore(StampaVo stampa) throws RemoteException;

    ////////////////////
    // stampe online //
    //////////////////
	public OutputStampaVo stampaOnlineUtente(StampaOnLineVO stampaOnLineVO) throws RemoteException;
	public OutputStampaVo stampaOnlineEtichette(StampaOnLineVO stampaOnLineVO)throws RemoteException;
	public OutputStampaVo stampaOnlineSchede(StampaOnLineVO stampaOnLineVO)throws RemoteException;
	public OutputStampaVo stampaOnlineFornitori(StampaOnLineVO stampaOnLineVO)throws RemoteException;
	public OutputStampaVo stampaOnlineBiblioteche(StampaOnLineVO stampaOnLineVO)throws RemoteException;
	public OutputStampaVo stampaOnlineTerminiThesauro(StampaOnLineVO stampaOnLineVO)throws RemoteException;
	public OutputStampaVo stampaOnlineBuoniOrdine(StampaOnLineVO stampaOnLineVO)throws RemoteException;
	public OutputStampaVo stampaOnlineBollettario(StampaOnLineVO stampaOnLineVO)throws RemoteException;
	public OutputStampaVo stampaOnlineFattura(StampaOnLineVO stampaOnLineVO)throws RemoteException;
	public OutputStampaVo stampaOnlineComunicazione(StampaOnLineVO stampaOnLineVO)throws RemoteException;
	public OutputStampaVo stampaOnlineSuggerimentiBibliotecario(StampaOnLineVO stampaOnLineVO)throws RemoteException;
	public OutputStampaVo stampaOnlineSuggerimentiLettore(StampaOnLineVO stampaOnLineVO)throws RemoteException;
	public OutputStampaVo stampaOnlineRichiesta(StampaOnLineVO stampaOnLineVO)throws RemoteException;
	public OutputStampaVo stampaModuloPrelievo(StampaOnLineVO stampaVO) throws RemoteException;
}
