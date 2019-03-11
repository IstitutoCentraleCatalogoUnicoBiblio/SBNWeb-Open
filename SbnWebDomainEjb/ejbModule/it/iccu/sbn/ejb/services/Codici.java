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
package it.iccu.sbn.ejb.services;

import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.EJBObject;

public interface Codici extends EJBObject {

	public static final String JNDI_MODELLI_STAMPA = "sbn.Codici.modelliStampa";

	/**
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public TB_CODICI cercaCodice(String codiceRicerca, CodiciType tipoCodice,
			CodiciRicercaType tipoRicerca) throws RemoteException;

	/**
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public String cercaDescrizioneCodice(String codiceRicerca,
			CodiciType tipoCodice, CodiciRicercaType tipoRicerca)
			throws RemoteException;

	/**
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public List getCodici(CodiciType TB_TABELLA) throws RemoteException;

	/**
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public List getCodici(CodiciType TB_TABELLA, boolean soloAttivi) throws RemoteException;

	public List<TB_CODICI> getCodiciCross(CodiciType tpTabellaP,
			String codiceP, boolean soloAttivi) throws RemoteException;

	/**
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public String getDescrizioneCodice(CodiciType tipoCodice,
			String codiceUnimarc) throws RemoteException;

	/**
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public String getDescrizioneCodiceSBN(CodiciType tipoCodice,
			String codiceSBN) throws RemoteException;

	/**
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public String getDescrizioneCodiceTabella(CodiciType tipoCodice,
			String codiceUnimarc) throws RemoteException;

	/**
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public List getLICR(String suffisso) throws RemoteException;

	/**
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public List getLICR(String prefisso, String suffisso)
			throws RemoteException;

	/**
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public boolean initialize(boolean force) throws RemoteException;

	/**
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public String SBNToUnimarc(CodiciType tipoCodice, String codiceSBN)
			throws RemoteException;

	/**
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public boolean tipoCodiceEffettivo(CodiciType tipoCodice)
			throws RemoteException;

	/**
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public String unimarcToSBN(CodiciType tipoCodice, String codiceUnimarc)
			throws RemoteException;

	public List<ModelloStampaVO> getModelliStampaPerAttivita(String codAttivita) throws RemoteException;
	public ModelloStampaVO getModelloStampa(int idModello) throws RemoteException;
	public ModelloStampaVO getModelloStampa(String jrxml) throws RemoteException;

}
