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
package it.iccu.sbn.servizi.z3950.jzkit;

import java.util.Iterator;

import org.marc4j.marc.Record;

class Z3950RecordIterator implements Iterator<Record>
{
    JZKitClient client;
    String resultSetName;
    int numInResultSet;
    int currentCursor;
    String remapStr = null;

    public Z3950RecordIterator(JZKitClient client, String resultSetName, int numInResultSet)
    {
        this.client = client;
        this.resultSetName = resultSetName;
        this.numInResultSet = numInResultSet;
        currentCursor = 0;
    }

    public Z3950RecordIterator(JZKitClient zClient, String resultSetName, int numInResultSet, String remapStr)
    {
        this(zClient, resultSetName, numInResultSet);
        this.remapStr = remapStr;
    }


    public boolean hasNext()
    {
        if (currentCursor < numInResultSet) return(true);
        return false;
    }


    public Record next()
    {
        Record rec = client.getRecord(++this.currentCursor, this.resultSetName);
        return(rec);
    }

	public void remove() {
		return;
	}

}
