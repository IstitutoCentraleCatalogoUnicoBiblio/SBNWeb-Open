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

import org.hibernate.dialect.Oracle9Dialect;

//import sun.management.PlatformMXBeanInvocationHandler;

public class OwnOracleDialect extends Oracle9Dialect {
    public OwnOracleDialect() {
        super();
        registerColumnType( 999, "it.finsiel.sbn.util.OracleCHAR" );
//        registerColumnType( 999, "it.finsiel.sbn.util.OracleCHAR" );
      }
}
