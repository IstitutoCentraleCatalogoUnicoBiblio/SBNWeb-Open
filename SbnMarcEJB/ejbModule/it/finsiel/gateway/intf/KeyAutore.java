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
package it.finsiel.gateway.intf;

import java.io.Serializable;


public interface KeyAutore extends Serializable {

	/**
	 * Returns the ky_auteur.
	 * @return String
	 */
	String getKy_auteur();

	/**
	 * Returns the ky_cautun.
	 * @return String
	 */
	String getKy_cautun();

	/**
	 * Returns the ky_el1a.
	 * @return String
	 */
	String getKy_el1a();

	/**
	 * Returns the ky_el1b.
	 * @return String
	 */
	String getKy_el1b();

	/**
	 * Returns the ky_el2a.
	 * @return String
	 */
	String getKy_el2a();

	/**
	 * Returns the ky_el2b.
	 * @return String
	 */
	String getKy_el2b();

	/**
	 * Returns the ky_el3.
	 * @return String
	 */
	String getKy_el3();

	/**
	 * Returns the ky_el4.
	 * @return String
	 */
	String getKy_el4();

	/**
	 * Returns the ky_el5.
	 * @return String
	 */
	String getKy_el5();

	/**
	 * Returns the ky_cles1_A.
	 * @return String
	 */
	String getKy_cles1_A();

	/**
	 * Returns the ky_cles2_A, null se ky_cles2_A è lunga zero
	 * @return String
	 */
	String getKy_cles2_A();

	String getKy_el1();

	String getKy_el2();

}
