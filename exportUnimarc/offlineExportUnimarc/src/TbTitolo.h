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
 * TbTitolo.h
 *
 *  Created on: 15-May-2009
 *      Author: argentino
 */


#ifndef TBTITOLO_H_
#define TBTITOLO_H_

#include "Tb.h"
#include "MarcConstants.h"

class TbTitolo: public Tb {
private:
		bool ricalcolaIndiceAree();
public:
	enum tbTitoloFieldId {
		bid,
		isadn,
		tp_materiale,
		tp_record_uni,
		cd_natura, 	/*	C Collezione
						A Titolo di raggruppamento controllato
						M monografia
						S periodico
						N titolo analitico
						B titolo di raggruppamento non controllato
						P titolo parallelo
						T titolo subordinato
						D titolo sviluppato o estrapolato
						W vol. particolare privo di tit. significativo */
		cd_paese,
		cd_lingua_1,
		cd_lingua_2,
		cd_lingua_3,
		aa_pubb_1,
		aa_pubb_2,
		tp_aa_pubb,
		cd_genere_1,
		cd_genere_2,
		cd_genere_3,
		cd_genere_4,
		ky_cles1_t,
		ky_cles2_t,
		ky_clet1_t,
		ky_clet2_t,
		ky_cles1_ct,
		ky_cles2_ct,
		ky_clet1_ct,
		ky_clet2_ct,
		cd_livello,
		fl_speciale,
		isbd,
		indice_isbd,
		ky_editore,
		cd_agenzia,
		cd_norme_cat,
		nota_inf_tit,
		nota_cat_tit,
		bid_link,
		tp_link,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		ute_forza_ins,
		ute_forza_var,
		fl_canc,
		cd_periodicita,
		fl_condiviso,
		ute_condiviso,
		ts_condiviso
	} ; // tbTitoloFieldId_type

	TbTitolo(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length);
	TbTitolo();
	TbTitolo(TbTitolo *tbTitolo);

	virtual ~TbTitolo();
	bool loadRecord(const char *key);
	bool loadRecordFromString(char *aStringRecord, int length);
	void ricalcolaAreeIsbdUtf8(const char *isbd, ATTValVector<CString *>  *areeVect);

};

#endif /* TBTITOLO_H_ */
