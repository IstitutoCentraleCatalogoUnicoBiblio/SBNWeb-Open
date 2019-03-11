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
 * TbAutore.h
 *
 *  Created on: 21-dic-2008
 *      Author: Arge
 */


#ifndef TBAUTORE_H_
#define TBAUTORE_H_

//#include "library/tvvector.h"
//#include "library/CString.h"
#include "Tb.h"

//#define TB_AUTORE_FIELDS 38

class TbAutore : public Tb{
//	CFile *tbAutoreIn;
//	CFile *tbAutoreOffsetIn;
//	char *offsetBufferTbAutorePtr;
//	long elementsTbAutore;
//	int keyPlusOffsetPlusLfLength;
//	int key_length;
//
//	ATTValVector<CString*> *fieldsVector;

//    tp_forma_aut,	// Accettata, Rinvio

//    tp_nome_aut, 	// A NOME SEMPLICE
					// B NOME COMPOSTO
					// C COGNOME SEMPLICE, Ha la virgola
					// D COGNOME COMPOSTO, Ha la virgola (due cognomi)
					// E ENTE NOME
					// G ENTE GERARCHICO
					// R ENTE CONGRESSO

//	void init();
public:
	enum fieldId {
	    vid,
	    isadn,
	    tp_forma_aut,
	    ky_cautun,
	    ky_auteur,
	    ky_el1_a,
	    ky_el1_b,
	    ky_el2_a,
	    ky_el2_b,
	    tp_nome_aut,
	    ky_el3,
	    ky_el4,
	    ky_el5,
	    ky_cles1_a,
	    ky_cles2_a,
	    cd_paese,
	    cd_lingua,
	    aa_nascita,
	    aa_morte,
	    cd_livello,
	    fl_speciale,
	    ds_nome_aut,
	    cd_agenzia,
	    cd_norme_cat,
	    nota_aut,
	    nota_cat_aut,
	    vid_link,
	    ute_ins,
	    ts_ins,
	    ute_var,
	    ts_var,
	    ute_forza_ins,
	    ute_forza_var,
	    fl_canc,
	    // Campi in + rispetto alla tb_autore di indice
	    fl_condiviso,
	    ute_condiviso,
	    ts_condiviso,
	    tidx_vector



	};

	TbAutore(CFile *tbAutoreIn, CFile *tbAutoreOffsetIn, char *offsetBufferTbAutorePtr, long elementsTbAutore, int keyPlusOffsetPlusLfLength, int key_length);

	TbAutore(TbAutore *tbAutore);
	virtual ~TbAutore();

	bool loadRecord(const char *key);

};

#endif /* TBAUTORE_H_ */
