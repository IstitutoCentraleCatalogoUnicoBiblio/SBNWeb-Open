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
package it.finsiel.sbn.util;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;

public class PostgresCHAR extends DataTypeCHAR  {

	public PostgresCHAR() {
		super();
		// TODO Auto-generated constructor stub
	}
     public int[] sqlTypes() {
          return new int[] { Types.CHAR };
       }

       public Class returnedClass() {
          return String.class;
       }

       public boolean equals(Object x, Object y) throws HibernateException {
          return (x == y) || (x != null && y != null && (x.equals(y)));
       }

       public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
          String val = rs.getString(names[0]);
          if (null == val)
          {
             return null;
          }
//        else
//        {
//             // NON FACCIO IL TRIM PER LUNGHEZZE uguali a 1
//            if (val.length() > 1){
//               String trimmed = StringUtils.stripEnd(val, " ");
//               if (trimmed.equals(""))
//               {
//                  return null;
//               }
//               else
//               {
//                  return trimmed;
//               }
//            }
            else{
                return val;
            }
          }
       //}

       public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
          if (value == null)
          {
             st.setNull(index, Types.CHAR);
          }
          else
          {
             st.setObject(index, value, Types.CHAR);
          }
       }

       public Object deepCopy(Object value) throws HibernateException {
          if (value == null) return null;
            return new String((String)value);
       }

       public boolean isMutable() {
          return false;
       }
    public int hashCode(Object arg0) throws HibernateException {
        // TODO Auto-generated method stub
        return 0;
    }
    public Serializable disassemble(Object arg0) throws HibernateException {
        // TODO Auto-generated method stub
        return null;
    }
    public Object assemble(Serializable arg0, Object arg1) throws HibernateException {
        // TODO Auto-generated method stub
        return null;
    }
    public Object replace(Object arg0, Object arg1, Object arg2) throws HibernateException {
        // TODO Auto-generated method stub
        return null;
    }
}
