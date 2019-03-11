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
 * TbfBiblioteca.h
 *
 *  Created on: 6-mag-2009
 *      Author: Arge
 */


#ifndef TBFBIBLIOTECA_H_
#define TBFBIBLIOTECA_H_

#include "Tb.h"

class TbfBiblioteca : public Tb{

	void initDereferencing();


public:
	enum fieldId {
		id_biblioteca,
		cd_ana_biblioteca,
		cd_polo,
		cd_bib,
		nom_biblioteca,
		unit_org,
		indirizzo,
		cpostale,
		cap,
		telefono,
		fax,
		note,
		p_iva,
		cd_fiscale,
		e_mail,
		tipo_biblioteca,
		paese,
		provincia,
		cd_bib_cs,
		cd_bib_ut,
		cd_utente,
		flag_bib,
		localita,
		chiave_bib,
		chiave_ente,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc,
		tidx_vector_nom_biblioteca,
		tidx_vector_indirizzo
		};

	enum fieldIdIndice {
		cd_polo_indice,
		cd_biblioteca_indice,
		ky_biblioteca_indice,
		cd_ana_biblioteca_indice,
		ds_biblioteca_indice,
		ds_citta_indice,
		fl_canc_indice
	};


	TbfBiblioteca(CFile *TbfBibliotecaIn, CFile *TbfBibliotecaOffsetIn, char *offsetBufferTbfBibliotecaPtr, long elementsTbfBiblioteca, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbfBiblioteca();
	bool loadRecord(const char *key);
};





#endif /* TBFBIBLIOTECA_H_ */
