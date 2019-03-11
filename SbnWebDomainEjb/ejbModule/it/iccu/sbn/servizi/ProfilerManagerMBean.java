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
package it.iccu.sbn.servizi;

import it.iccu.sbn.ejb.model.unimarcmodel.SbnProfileType;

import org.jboss.system.ServiceMBean;

/**
 * @author Antonio
 * @author almaviva
 */
public interface ProfilerManagerMBean extends ServiceMBean {

    public void reload() throws Exception;

    public SbnProfileType getProfiloIndice(boolean force);

	public void reloadCodici();
	public void rimuoviProfiliSbnMarc();

	public void setSbnWebLoggingLevel(String level) throws Exception;
	public String getSbnWebLoggingLevel();

	public void setSbnMarcLoggingLevel(String level) throws Exception;
	public String getSbnMarcLoggingLevel();

	public void setHibernateLoggingLevel(String level) throws Exception;
	public String getHibernateLoggingLevel();

	public String printXMLProfiloIndice() throws Exception;

	public boolean isRedeployWarningActive() throws Exception;
	public void setRedeployWarningActive(boolean flag) throws Exception;

	public void resetRootPassword() throws Exception;

	public void testMailServer(String toAddress) throws Exception;
	public void testSMSProvider(String rcvNumber, String text) throws Exception;

	public String printConfiguration() throws Exception;

	public String getServerStartupTimestamp();

	//almaviva5_20140303 evolutive google3
	public boolean isIndiceAvailable() throws Exception;
	public void setIndiceAvailable(boolean avail) throws Exception;

	//almaviva5_20140910
	public void setConfigurationProperty(String key, String value) throws Exception;

}
