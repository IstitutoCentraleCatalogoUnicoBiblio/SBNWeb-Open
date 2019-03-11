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
 * TbClasse.h
 *
 *  Created on: 25-feb-2009
 *      Author: Arge
 */


#ifndef TBCLASSE_H_
#define TBCLASSE_H_

//#include "library/tvvector.h"
//#include "library/CString.h"
#include "Tb.h"

#define TB_CLASSE_FIELDS 19

class TbClasse : public Tb{
//	CFile *tbClasseIn;
//	CFile *tbClasseOffsetIn;
//	char *offsetBufferTbClassePtr;
//	long elementsTbClasse;
//	int keyPlusOffsetPlusLfLength;
//	int key_length;
//
//	ATTValVector<CString*> *fieldsVector;
//
//	void init(CFile *tbClasseIn, CFile *tbClasseOffsetIn, char *offsetBufferTbClassePtr, long elementsTbClasse, int keyPlusOffsetPlusLfLength, int key_length);
public:
	void initDereferencing();

	enum fieldId { // 19
		cd_sistema,
		cd_edizione,
		classe,
		ds_classe,
		cd_livello,
		fl_costruito,
		fl_speciale,
		ky_classe_ord,
		suffisso,
		ult_term,
		fl_condiviso,
		ute_condiviso,
		ts_condiviso,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc,
		tidx_vector
	};
    enum fieldIdIndice { // 12
        cd_sistema_indice,
        cd_edizione_indice,
        classe_indice,
        cd_livello_indice,
        fl_costruito_indice,
        fl_speciale_indice,
        ds_classe_indice,
        ute_ins_indice,
        ts_ins_indice,
        ute_var_indice,
        ts_var_indice,
		fl_canc_indice
    };





	TbClasse(CFile *tbClasseIn, CFile *tbClasseOffsetIn, char *offsetBufferTbClassePtr, long elementsTbClasse, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbClasse();
	bool loadRecord(const char *key);

};

#endif /* TBCLASSE_H_ */
