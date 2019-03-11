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

import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

public interface GestioneCodici extends EJBObject {

	public List getCodiceTitoloStudio() throws RemoteException;
	public List getTipoLegameTraDescrittori() throws RemoteException;
	public List getCodiceTipoMaterialeInventariale() throws RemoteException;
	public List getCodiceMotivoScarico() throws RemoteException;
	public List getCodiceMotivoCarico() throws RemoteException;
	public List getCodiceStatoConservazione() throws RemoteException;
	public List getCodiceTipoSezione() throws RemoteException;
	public List getCodiceTipoCollocazione() throws RemoteException;
	public List getCodiceProfessioni() throws RemoteException;
	public List getCodiceNonDisponibilita() throws RemoteException;
	public List getCodiceCategoriaDiFruizione() throws RemoteException;
	public List getCodiceAgenziaCatalogazione() throws RemoteException;
	public List getCodiceAltitudineSensore() throws RemoteException;
	public List getCodiceCarattereImmagine() throws RemoteException;
	public List getCodiceCategoriaSatellite() throws RemoteException;
	public List getCodiceColore116() throws RemoteException;
	public List getCodiceColore120() throws RemoteException;
	public List getCodiceDesignazioneFunzione() throws RemoteException;
	public List getCodiceEdizioneClasse() throws RemoteException;
	public List getCodiceElaborazione() throws RemoteException;
	public List getCodiceFormaDocumentoCartografico() throws RemoteException;
	public List getCodiceFormaMusicale() throws RemoteException;
	public List getCodiceFormaPubblicazione() throws RemoteException;
	public List getCodiceFormaRiproduzione() throws RemoteException;
	public List getCodiceGenereMaterialeUnimarc() throws RemoteException;
	public List getCodiceGenerePubblicazione() throws RemoteException;
	public List getCodiceGenereRappresentazione() throws RemoteException;
	public List getCodiceIndicatoreIncipit() throws RemoteException;
	public List getCodiceIndicatoreTiposcala() throws RemoteException;
	public List getCodiceLegameNaturaTitoloTitolo() throws RemoteException;
	public List getCodiceLegameTitoloAutore() throws RemoteException;
	public List getCodiceLegameTitoloLuogo() throws RemoteException;
	public List getCodiceLegameTitoloMusica() throws RemoteException;
	public List getCodiceLegameTitoloTitolo() throws RemoteException;
	public List getCodiceLingua() throws RemoteException;
	public List getCodiceLivelloAutorita() throws RemoteException;
	public List getCodiceMateria() throws RemoteException;
	public List getCodiceMaterialeBibliografico() throws RemoteException;
	public List getCodiceMaterialeGrafico() throws RemoteException;
	public List getCodiceMeridianoOrigine() throws RemoteException;
	public List getCodiceNaturaBibliografica() throws RemoteException;
	public List getCodicePaese() throws RemoteException;
	public List getCodicePiattaforma() throws RemoteException;
	public List getCodicePresentazione() throws RemoteException;
	public List getCodicePubblicazioneGovernativa() throws RemoteException;
	public List getCodiceResponsabilita() throws RemoteException;
	public List getCodiceSistemaClasse() throws RemoteException;
	public List getCodiceSoggettario() throws RemoteException;
	public List getCodiceStesura() throws RemoteException;
	public List getCodiceStrumentiVociOrganico() throws RemoteException;
	public List getCodiceSupportoFisicoCartografico() throws RemoteException;
	public List getCodiceSupportoFisicoGrafica() throws RemoteException;
	public List getCodiceTecnicaCartografia() throws RemoteException;
	public List getCodiceTecnicaDisegni() throws RemoteException;
	public List getCodiceTecnicaStampe() throws RemoteException;
	public List getCodiceTipoAuthority() throws RemoteException;
	public List getCodiceTipoAutore() throws RemoteException;
	public List getCodiceTipoDataPubblicazione() throws RemoteException;
	public List getCodiceTipiBiblioteca() throws RemoteException;
	public List getCodiceTipoNumeroStandard() throws RemoteException;
	public List getCodiceTipoTestoLetterario() throws RemoteException;
	public List getCodiceTiposcala() throws RemoteException;
	public List getCodiceTonalita() throws RemoteException;
	public List getCodiceMaterialeBibliografico2() throws RemoteException;
	public List getCodiceSiNo() throws RemoteException;
	public List getCodiceTipoDigitalizzazione() throws RemoteException;
	public List getCodiceMaterialeBibliograficoTutti() throws RemoteException;
	public List getCodiceValuta() throws RemoteException;
	public List getCodiceStatoMessaggi() throws RemoteException;
	public List getCodiceStatoOrdine() throws RemoteException;
	public List getCodiceStatoSuggerimento() throws RemoteException;
	public List getCodiceTipoFattura() throws RemoteException;
	public List getCodiceIva() throws RemoteException;
	public List getCodiceTipoInvio() throws RemoteException;
	public List getCodiceTipiMessaggio() throws RemoteException;
	public List getCodiceTipoOrdine() throws RemoteException;
	public List getCodiceStatoPartecipantiGara() throws RemoteException;
	public List getCodiceProvenienzeOfferte() throws RemoteException;
	public List getCodiceTipoUrgenza() throws RemoteException;
	public List getCodiceTipoMateriale() throws RemoteException;
	public List getCodiceNatura() throws RemoteException;
	public List getCodiceNaturaOrdine() throws RemoteException;
	public List getCodiceOrdinamentoListaOrdini() throws RemoteException;
	public List getCodiceStatiContinuativo() throws RemoteException;
	public List getCodiceProvince() throws RemoteException;
	public List getCodiceTipoPartner() throws RemoteException;
	public List getCodiceMinMax() throws RemoteException;
	public List getCodiceSpecificita() throws RemoteException;
	public List getCodiceNordSud() throws RemoteException;
	public List getCodiceEstOvest() throws RemoteException;
	public List getCodiceSessoUtente() throws RemoteException;
	public List getCodiceOrdinamentoTitoli() throws RemoteException;
	public List getCodiceOrdinamentoAutori() throws RemoteException;
	public List getCodiceOrdinamentoMarche() throws RemoteException;
	public List getCodiceOrdinamentoLuoghi() throws RemoteException;
	public List getCodiceLivelloRicercaLocale() throws RemoteException;
	public List getCodiceTipoLocalizzazione(String tipologia) throws RemoteException;
	public List getCodiceThesauro() throws RemoteException;
	public List getCodiceProvenienza() throws RemoteException;
	public List getCodiceTipoPersonaGiuridica() throws RemoteException;
	public List getCodiceTipoDocumentoDiRiconoscimento() throws RemoteException;
	public List getCodiceProvincia() throws RemoteException;
	public List getCodiceAteneo() throws RemoteException;

	public List getCodiceStatoMovimento() throws RemoteException;
	public List getCodiceAttivita() throws RemoteException;
	public List getCodiceModalitaErogazione() throws RemoteException;
	public List getCodiceTipoServizio() throws RemoteException;
	public List getCodiceTipoSupporto() throws RemoteException;


	//////////////////////////////////////////////////////////////////////////////////////

	public List getCodici(CodiciType key) throws RemoteException;
	public List getCodici(CodiciType key, boolean soloAttivi) throws RemoteException;

	public List<TB_CODICI> getCodiciCross(CodiciType tpTabellaP, String codiceP, boolean soloAttivi) throws RemoteException;
	public List<TB_CODICI> getCodiciCross(CodiciType tpTabellaP, String codiceP) throws RemoteException;

	public boolean tipoCodiceEffettivo(CodiciType tipoCodice) throws RemoteException;
	public List getLICR(String suffisso) throws RemoteException;
	public List getLICR(String prefisso, String suffisso) throws RemoteException;
	public String SBNToUnimarc(CodiciType tipoCodice, String codiceSBN)	throws RemoteException;
	public String unimarcToSBN(CodiciType tipoCodice, String codiceUnimarc)	throws RemoteException;
	public TB_CODICI cercaCodice(String codiceRicerca, CodiciType tipoCodice, CodiciRicercaType tipoRicerca) throws RemoteException;
	public String getDescrizioneCodiceTabella(CodiciType tipoCodice, String codiceUnimarc) throws RemoteException;
	public String getDescrizioneCodice(CodiciType tipoCodice, String codiceUnimarc) throws RemoteException;
	public String getDescrizioneCodiceSBN(CodiciType tipoCodice, String codiceSBN) throws RemoteException;
	public List getListaCodice(CodiciType tipoCodice) throws RemoteException;
	public List getListaCodice(CodiciType tipoCodice, SbnMateriale tipoMateriale) throws RemoteException;
	public List getListaCodice(CodiciType tipoCodice, String codice) throws RemoteException;
	public List getListaCodiceDiversiDa(CodiciType tipoCodice, List codici) throws RemoteException;
	public List getListaCodice(CodiciType tipoCodice, List codici) throws RemoteException;

	public List getVettoreCodici(CodiciType tipoCodice) throws RemoteException;
	public String cercaDescrizioneCodice(String codiceRicerca, CodiciType tipoCodice, CodiciRicercaType tipoRicerca) throws RemoteException;
	public void reload() throws RemoteException;

	public List<ModelloStampaVO> getModelliStampaPerAttivita(String codAttivita) throws RemoteException;
	public ModelloStampaVO getModelloStampa(int idModello) throws RemoteException;
	public ModelloStampaVO getModelloStampa(String jrxml) throws RemoteException;

	public List getCodiceSupportoCopia() throws RemoteException;
	public List getCodiceTipiAmmessiPerDocum() throws RemoteException;

}
