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
package it.iccu.sbn.servizi.batch;

import it.iccu.sbn.ejb.exception.BatchInterruptedException;

import java.io.File;
import java.util.Set;

import org.jboss.system.ServiceMBean;
import org.quartz.Scheduler;

public interface BatchManagerMBean extends ServiceMBean {

	public void reload() throws Exception;
	public Scheduler getScheduler() throws Exception;
	public void deleteBatchId(String id) throws Exception;

	public void addRunningJob(String id, String threadId) throws Exception;
	public void removeRunningJob(String id) throws Exception;
	public Set<Integer> getRunningJobs() throws Exception;

	public String getCleaningAgeThreshold() throws Exception;

	public boolean isCleaningDeleteOutputs() throws Exception;

	public boolean isUserDeletionDeleteOutputs() throws Exception;

	public int getDeletedCount() throws Exception;
	public void incrementDeletedCount(int deleted) throws Exception;

	public String dumpRunningJobThread(String id) throws Exception;

	public void checkForInterruption(String id) throws BatchInterruptedException;

	public void markForDeletion(File f) throws Exception;

	public void forceBatchStart(String id) throws Exception;

}
