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
package it.iccu.sbn.ejb.vo;

import java.util.Date;

/**
 * @author Calli
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class VersioneVO extends BaseVO {

	private static final long serialVersionUID = -650567907655618820L;

	public VersioneVO() {
    }

    private int versione;

    private Date rowCreatedTime;

    private String rowCreatedUser;

    private Date RowUpdatedTime;

    private String rowUpdatedUser;

    public Date getRowCreatedTime() {
        return rowCreatedTime;
    }

    public void setRowCreatedTime(Date rowCreatedTime) {
        this.rowCreatedTime = rowCreatedTime;
    }

    public String getRowCreatedUser() {
        return rowCreatedUser;
    }

    public void setRowCreatedUser(String rowCreatedUser) {
        this.rowCreatedUser = rowCreatedUser;
    }

    public Date getRowUpdatedTime() {
        return RowUpdatedTime;
    }

    public void setRowUpdatedTime(Date rowUpdatedTime) {
        RowUpdatedTime = rowUpdatedTime;
    }

    public String getRowUpdatedUser() {
        return rowUpdatedUser;
    }

    public void setRowUpdatedUser(String rowUpdatedUser) {
        this.rowUpdatedUser = rowUpdatedUser;
    }

    public int getVersione() {
        return versione;
    }

    public void setVersione(int versione) {
        this.versione = versione;
    }
}
