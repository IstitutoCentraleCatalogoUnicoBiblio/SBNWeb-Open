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

import it.iccu.sbn.ejb.SbnBusinessSessionRemote;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsemplareDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsemplareVO;
import it.iccu.sbn.ejb.vo.documentofisico.FormatiSezioniVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloDefaultVO;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloEtichetteVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriDiInventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.ProvenienzaInventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.SerieVO;
import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.TitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.web.keygenerator.GeneraChiave;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJBObject;

public interface GestioneDocumentoFisico extends EJBObject, SbnBusinessSessionRemote {

	public List<TitoloVO> getTitolo(String bid, String ticket) throws RemoteException,ValidationException, DataException ;
//sezioni di collocazione
	public DescrittoreBloccoVO getListaSezioni(String codPolo, String codiceBiblioteca, String ticket, int nRec) throws RemoteException, ValidationException, DaoManagerException;
	//public List getListaSezioni(String codPolo, String codiceBiblioteca, String ticket) throws RemoteException;
	public SezioneCollocazioneVO getSezioneDettaglio(String codPolo, String codBib, String codSez, String ticket) throws RemoteException, ValidationException;
	public boolean insertSezione(SezioneCollocazioneVO sezione, String ticket) throws RemoteException, DataException, ValidationException;
	public boolean deleteSezione(SezioneCollocazioneVO sezione, String ticket) throws RemoteException, ValidationException;
	public boolean updateSezione(SezioneCollocazioneVO sezione, String ticket) throws RemoteException, ValidationException;

//formati di sezione
	public List getListaFormatiSezioni(String codPolo, String codiceBiblioteca, String codiceSezione, String ticket) throws RemoteException, ValidationException;
	public DescrittoreBloccoVO getListaFormatiBib(String codPolo, String codiceBiblioteca, String codiceSezione, String ticket, int nRec) throws DataException, ValidationException, RemoteException;
	public DescrittoreBloccoVO getListaFormatiSezioni(String codPolo, String codiceBiblioteca, String codiceSezione, String ticket, int nRec) throws DataException, ValidationException, RemoteException;
	public DescrittoreBloccoVO getListaFormatiSezioni(String codPolo, String codiceBiblioteca, String ticket, int nRec) throws DataException, ValidationException, RemoteException;
	public FormatiSezioniVO getFormatiSezioniDettaglio(String codPolo, String codBib, String codSez, String codFormato, String ticket) throws RemoteException, ValidationException;
	public boolean insertFormatiSezioni(FormatiSezioniVO formatiSezioni, String ticket) throws RemoteException, ValidationException;
	public boolean updateFormatiSezioni(FormatiSezioniVO formatiSezioni, String ticket) throws RemoteException, ValidationException;

//codici provenienze
//	public List getListaProvenienze(String codPolo, String codiceBiblioteca, String ticket) throws RemoteException;
	public DescrittoreBloccoVO getListaProvenienze(String codPolo, String codiceBiblioteca, String ticket, int nRec, String filtroProvenienza) throws RemoteException;
	public boolean insertProvenienza(ProvenienzaInventarioVO provenienza, String ticket) throws RemoteException, ValidationException;
	public boolean updateProvenienza(ProvenienzaInventarioVO provenienza, String ticket) throws RemoteException, ValidationException;
	public ProvenienzaInventarioVO getProvenienza(String codPolo, String codBib, String codProven, String ticket) throws RemoteException;

//serie inventariali
	public List getListaSerie(String codPolo, String codiceBiblioteca, String ticket) throws RemoteException, ValidationException;
	public List getListaSerieDate(String codPolo, String codiceBiblioteca, String ticket) throws RemoteException, ValidationException;
	public DescrittoreBloccoVO getListaSerie(String codPolo, String codiceBiblioteca, String ticket, int nRec) throws RemoteException, ValidationException;
//	public List getListaSerie(String codPolo, String codBib, String ticket) throws RemoteException, ValidationException;
//	public List getListaSerie(String codBib, String codSerie) throws RemoteException;
	public SerieVO getSerieDettaglio(String codPolo, String codBib, String codSez, String ticket) throws RemoteException, DataException, ValidationException;
	public boolean insertSerie(SerieVO serie, String ticket) throws RemoteException, ValidationException;
	public boolean updateSerie(SerieVO serie, String ticket) throws RemoteException, ValidationException;

//	Gestione Inventari
	public String getChiaviTitoloAutore(String tipo, String bidVid, String ticket) throws RemoteException, ValidationException, DataException;
	public DescrittoreBloccoVO getListaInventariTitolo(String codPolo, String codBib, String bid, String ticket, int nRec) throws RemoteException, ValidationException, DataException;
	public DescrittoreBloccoVO getListaInventariTitolo(String codPolo, List<String> listaBiblio, String bid, String ticket, int nRec) throws RemoteException, ValidationException, DataException;
	public DescrittoreBloccoVO getListaInventariNonCollocati(EsameCollocRicercaVO paramRicerca, String ticket, int nRec) throws RemoteException;
	public DescrittoreBloccoVO getListaInventariDiCollocazione(EsameCollocRicercaVO paramRicerca, String ticket, int nRec) throws RemoteException;
	public DescrittoreBloccoVO getListaInventariOrdine(String codPolo, String codBib, String codBibO, String codTipOrd, int annoOrd, int codOrd, String codBibF, Locale locale, String ticket, int nRec) throws RemoteException;
	public DescrittoreBloccoVO getListaInventariOrdini(List listaOrdini, Locale locale, String ticket, int nRec) throws RemoteException;
	public DescrittoreBloccoVO getListaInventariOrdiniNonFatturati(String codPolo, String codBib, String codBibO, String codTipOrd, int annoOrd, int codOrd, String codBibF, Locale locale, String ticket, int nRec) throws RemoteException;
	public DescrittoreBloccoVO getListaInventariFattura(String codPolo, String codBibFattura, int annoFattura, int progrFattura, Locale locale, String ticket, int nRec) throws RemoteException;
	public DescrittoreBloccoVO getListaInventariRigaFattura(String codPolo, String codBib, String codBibO, String codTipOrd, int annoOrd, int codOrd, String codBibF,  int annoF, int prgF, int rigaF, Locale locale, String ticket, int nRec) throws RemoteException;
	public DescrittoreBloccoVO getListaInventariPossessori(String codPolo, String codBib, String pid, String ticket, int nRec, EsameCollocRicercaVO paramRicerca) throws RemoteException;
	public List insertInventario(InventarioVO inventario, String tipoOperazione, int nInv, Locale locale, String ticket) throws RemoteException, ValidationException;
	public int updateInvColl(InventarioVO recInvColl, CollocazioneVO recColl, String tipoOperazione, String ticket) throws RemoteException, DataException, ValidationException;;
	public boolean deleteInventario(InventarioVO inventario, CollocazioneVO recColl, String tipoOperazione, String ticket) throws RemoteException, DataException, ValidationException;;
	public boolean aggiornaInventarioFattura(InventarioVO inventario, String topoOp, String ticket) throws RemoteException, DataException, ValidationException;;
	public DatiBibliograficiCollocazioneVO getAnaliticaPerCollocazione(String bid, String bibliotecaOperante, String ticket) throws RemoteException, DataException, ValidationException;
	public InventarioVO getInventario(String codPolo, String codBib, String serie, int codInv, Locale locale, String ticket) throws RemoteException,ValidationException, DataException, DaoManagerException ;
	public InventarioDettaglioVO getInventarioDettaglio(String codPolo, String codBib, String serie, int codInv, Locale locale, String ticket) throws RemoteException, ValidationException ;
	public List getListaInventariCollocatiDa(String cobPolo, String codBib, String codUtente, String dataDa, String dataA, String ticket) throws RemoteException;
	public boolean getSerieNumeroCarico(String codPolo, String codBib, String codSerie, String numCarico, String ticket) throws RemoteException;


// Gestione Collocazioni
	public boolean updateCollocazioneScolloca(InventarioVO inventario, CollocazioneVO recColl, String tipoOperazione, String ticket) throws RemoteException, DataException, ValidationException;;
	public DescrittoreBloccoVO getListaCollocazioniSezione(EsameCollocRicercaVO paramRicerca, String tipoRichiesta, String ticket, int nRec) throws RemoteException, ApplicationException, DataException, ValidationException;
	public DescrittoreBloccoVO getListaCollocazioniReticolo(String codPolo, String codBib, CollocazioneTitoloVO[] reticolo, String tipoOperazione, String ticket, int nRec) throws RemoteException, ApplicationException, ValidationException;
	public boolean updateCollocazione(CollocazioneDettaglioVO recColl, boolean datiMappaModificati, boolean collocazioneEsistente, String ticket) throws RemoteException, ApplicationException, ValidationException;
//	public boolean updateCollocazione(CollocazioneDettaglioVO recColl, boolean collocazioneEsistente, String ticket) throws RemoteException, ApplicationException, ValidationException;
	public CollocazioneVO getCollocazione(CollocazioneVO recColl, String ticket) throws RemoteException, ValidationException, DaoManagerException;
	public CollocazioneVO getCollocazione(int keyLoc, String ticket) throws RemoteException,ValidationException ;
	public CollocazioneDettaglioVO getCollocazioneDettaglio(int keyLoc, String ticket) throws RemoteException, DataException, ValidationException;
	public boolean [] getDatiPosseduto(String codPolo, String codBib, String bid, String ticket) throws RemoteException;
	public List getAltreBib(String bid, String codBib, String ticket) throws RemoteException;
	public DescrittoreBloccoVO getListaCollocazioniTitolo(String codPolo, String codBib, String bid, String ticket, int nRec) throws RemoteException, ValidationException, DataException;


//	esemplare
	public EsemplareDettaglioVO getEsemplareDettaglio(String codPolo, String codBib, String bid, int codDoc, String ticket) throws RemoteException, ApplicationException, ValidationException;
	public DescrittoreBloccoVO getListaEsemplariReticolo(String codPolo, String codBib, CollocazioneTitoloVO[] reticolo, int nRec, String ticket) throws RemoteException, ApplicationException, ValidationException;
	public boolean insertEsemplare(EsemplareVO recEsempl, int keyLoc, String ticket) throws RemoteException, ApplicationException, ValidationException;
	public boolean updateEsemplare(EsemplareVO recEsempl, int keyLoc, String tipoOper, String ticket) throws RemoteException, ApplicationException, ValidationException;
	public DescrittoreBloccoVO getListaEsemplariDiCollocazione(String codPolo, String codBib, String bibDoc, String ticket, int nRec) throws RemoteException, ValidationException;
	public DescrittoreBloccoVO getListaEsemplariTitolo(String codPolo, String codBib, String bid, String ticket, int nRec) throws RemoteException, ValidationException, DataException;

	//possessori
	public DescrittoreBloccoVO getListaPossessori(String codPolo,  String codBib,PossessoriRicercaVO possRic,  String ticket , int nRec,GeneraChiave key) throws RemoteException, ValidationException, DataException;
	public DescrittoreBloccoVO getListaPossessori_1(String codPolo,  String codBib,PossessoriRicercaVO possRic,  String ticket , int nRec,GeneraChiave key) throws RemoteException, ValidationException, DataException;
	public String inserisciPossessori(PossessoriDettaglioVO possDett , String codPolo, String uteFirma, String ticket,GeneraChiave key ) throws RemoteException, ApplicationException, ValidationException;
	public String modificaPossessori(PossessoriDettaglioVO possDett , String codPolo, String uteFirma, String ticket,GeneraChiave key ) throws RemoteException, ApplicationException, ValidationException;
	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO creaRichiestaAnaliticoPossessoriPid(AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass, String polo ,String bib , String ticket)	throws RemoteException;
	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO cancellaLegamePossessore(String pid_padre,String pid_figlio, String polo ,String bib , String ticket)	throws RemoteException;
	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO cancellaLegameInventario(String pid, String codiceInventario, String polo ,String bib, String firma, String ticket)	throws RemoteException;

	public String fondiPossessori(PossessoriDettaglioVO possDett , String codPolo, String uteFirma, String ticket, GeneraChiave key) throws RemoteException, ApplicationException, ValidationException;



	public DescrittoreBloccoVO getListaPossessoriDiInventario(String codSerie, String codInv , String codBib,String codPolo , String ticket ,int nRec) throws RemoteException, ValidationException, DataException;
	public String inserisciLegameAlPossessori(PossessoriDettaglioVO possDett , String codPolo, String uteFirma, String ticket,GeneraChiave key ,String notaLegame,String tipoLegame) throws RemoteException, ApplicationException, ValidationException;
	public String modificaLegameAlPossessori(PossessoriDettaglioVO possDett , String codPolo, String uteFirma, String ticket,GeneraChiave key ,String notaLegame,String tipoLegame,String pidOrigine) throws RemoteException, ApplicationException, ValidationException;
	public String modificaNotaLegame(String pid_padre,String pid_figlio,String notaLegame,String uteFirma) throws RemoteException;
	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO scambioLegame(String pid_padre,String pid_figlio,String uteFirma) throws RemoteException;

	public String legamePossessoreInventario(PossessoriDiInventarioVO possInv ) throws RemoteException, ApplicationException, ValidationException;
	public String modificaLegamePossessoreInventario(PossessoriDiInventarioVO possInv ) throws RemoteException, ApplicationException, ValidationException; // almaviva7 02/07/2008

	public ModelloDefaultVO getModelloDefault(String codPolo, String codBib, String ticket) throws RemoteException, DataException, ValidationException;
	public boolean insertModelloDefault(ModelloDefaultVO modello, String ticket) throws RemoteException, DataException, ValidationException;
	public DescrittoreBloccoVO getListaModelli(String codPolo, String codiceBiblioteca, String ticket, int nRec) throws RemoteException, DataException, ValidationException;
	public ModelloEtichetteVO getModello(String codPolo, String codBib, String codModello, String ticket) throws RemoteException, DataException, ValidationException;
	public boolean insertModello(ModelloEtichetteVO modello, String ticket) throws RemoteException, DataException, ValidationException;
	public boolean deleteModello(ModelloEtichetteVO modello, String ticket) throws RemoteException, DataException, ValidationException;
	public boolean updateModello(ModelloEtichetteVO modello, String ticket) throws RemoteException, DataException, ValidationException;

}
