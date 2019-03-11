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
/*
 * TbCodici.cpp
 *
 *  Created on: 16-feb-2009
 *      Author: Arge
 */


#include "TbCodici.h"

TbCodici::TbCodici() {
	tpTabella = 0;
	cdTabella = 0;
	dsTabella = 0;
	cdUnimarc = 0;
	cdMarc_21 = 0;
	tpMateriale = 0;
	dtFineValidita = 0;
}

TbCodici::TbCodici(char *cdTabella, char *cdUnimarc) {
	tpTabella = 0;
	this->cdTabella = new CString (cdTabella);
	dsTabella = 0;
	this->cdUnimarc = new CString (cdUnimarc);
	cdMarc_21 = 0;
	tpMateriale = 0;
	dtFineValidita = 0;
}







TbCodici::~TbCodici() {
	stop();
} // end

void TbCodici::stop()
{
	if (tpTabella)
	    delete tpTabella;
	if (cdTabella)
	    delete cdTabella;
	if (dsTabella)
	    delete dsTabella;
	if (cdUnimarc)
	    delete cdUnimarc;
	if (cdMarc_21)
	    delete cdMarc_21;
	if (tpMateriale)
	    delete tpMateriale;
	if (dtFineValidita)
	    delete dtFineValidita;
}


CString *TbCodici::getTpTabella()
{
	return tpTabella;
}
CString *TbCodici::getCdTabella()
{
	return cdTabella;
}

CString *TbCodici::getDsTabella()
{
	return dsTabella;
}

CString *TbCodici::getCdUnimarc()
{
	return cdUnimarc;
}

CString *TbCodici::getCdMarc_21()
{
	return cdMarc_21;
}
CString *TbCodici::getTpMateriale()
{
	return tpMateriale;
}
CString *TbCodici::getDtFineValidita()
{
	return dtFineValidita;
}

void TbCodici::setTpTabella(CString * s)
{
	tpTabella = s;
}
void TbCodici::setCdTabella(CString * s)
{
	cdTabella = s;
}
void TbCodici::setDsTabella(CString * s)
{
	dsTabella = s;
}
void TbCodici::setCdUnimarc(CString * s)
{
	cdUnimarc = s;
}
void TbCodici::setCdMarc_21(CString * s)
{
	cdMarc_21 = s;
}
void TbCodici::setTpMateriale(CString * s)
{
	tpMateriale=s;
}
void TbCodici::setDtFineValidita(CString * s)
{
	dtFineValidita = s;
}
