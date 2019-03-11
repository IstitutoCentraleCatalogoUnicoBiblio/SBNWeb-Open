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
package it.finsiel.sbn.polo.orm.intf;

public interface Cartografia {

	void setCD_LIVELLO(String value);

	String getCD_LIVELLO();

	void setTP_PUBB_GOV(String value);

	String getTP_PUBB_GOV();

	void setCD_COLORE(String value);

	String getCD_COLORE();

	void setCD_MERIDIANO(String value);

	String getCD_MERIDIANO();

	void setCD_SUPPORTO_FISICO(String value);

	String getCD_SUPPORTO_FISICO();

	void setCD_TECNICA(String value);

	String getCD_TECNICA();

	void setCD_FORMA_RIPR(String value);

	String getCD_FORMA_RIPR();

	void setCD_FORMA_PUBB(String value);

	String getCD_FORMA_PUBB();

	void setCD_ALTITUDINE(String value);

	String getCD_ALTITUDINE();

	void setCD_TIPOSCALA(String value);

	String getCD_TIPOSCALA();

	void setTP_SCALA(String value);

	String getTP_SCALA();

	void setSCALA_ORIZ(String value);

	String getSCALA_ORIZ();

	void setSCALA_VERT(String value);

	String getSCALA_VERT();

	void setLONGITUDINE_OVEST(String value);

	String getLONGITUDINE_OVEST();

	void setLONGITUDINE_EST(String value);

	String getLONGITUDINE_EST();

	void setLATITUDINE_NORD(String value);

	String getLATITUDINE_NORD();

	void setLATITUDINE_SUD(String value);

	String getLATITUDINE_SUD();

	void setTP_IMMAGINE(String value);

	String getTP_IMMAGINE();

	void setCD_FORMA_CART(String value);

	String getCD_FORMA_CART();

	void setCD_PIATTAFORMA(String value);

	String getCD_PIATTAFORMA();

	void setCD_CATEG_SATELLITE(String value);

	String getCD_CATEG_SATELLITE();

	void setBID(String value);

	String getBID();

	String getTP_PROIEZIONE();

	void setTP_PROIEZIONE(String value);

}
