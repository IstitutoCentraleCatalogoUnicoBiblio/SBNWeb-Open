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
#include "../include/library/CString.h"
/*
 * TbCodici.h
 *
 *  Created on: 16-feb-2009
 *      Author: Arge
 */


#ifndef TBCODICI_H_
#define TBCODICI_H_

class TbCodici {
	CString *tpTabella;
	CString *cdTabella;
	CString *dsTabella;
	CString *cdUnimarc;
	CString *cdMarc_21;
	CString *tpMateriale;
	CString *dtFineValidita;

	void stop();
public:
	TbCodici();
	TbCodici(char *cdTabella, char *cdUnimarc);
	virtual ~TbCodici();

	CString *getTpTabella();
	CString *getCdTabella();
	CString *getDsTabella();
	CString *getCdUnimarc();
	CString *getCdMarc_21();
	CString *getTpMateriale();
	CString *getDtFineValidita();

	void setTpTabella(CString * s);
	void setCdTabella(CString * s);
	void setDsTabella(CString * s);
	void setCdUnimarc(CString * s);
	void setCdMarc_21(CString * s);
	void setTpMateriale(CString * s);
	void setDtFineValidita(CString * s);

};

#endif /* TBCODICI_H_ */
