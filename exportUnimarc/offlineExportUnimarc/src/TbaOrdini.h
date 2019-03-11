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
 * TbaOrdini.h
 *
 *  Created on: 7-mag-2009
 *      Author: Arge
 */


#ifndef TBAORDINI_H_
#define TBAORDINI_H_

#include "Tb.h"

class TbaOrdini: public Tb {
public:
	enum fieldId {
		bid, //id_ordine, swapped for unix sort
		cd_polo,
		cd_bib,
		cod_tip_ord,
		anno_ord,
		cod_ord,
		cod_fornitore,
		id_sez_acquis_bibliografiche,
		id_valuta,
		id_capitoli_bilanci,
		data_ins,
		data_agg,
		data_ord,
		note,
		num_copie,
		continuativo,
		stato_ordine,
		tipo_doc_lett,
		cod_doc_lett,
		tipo_urgenza,
		cod_rich_off,
		bid_p,
		note_forn,
		tipo_invio,
		anno_1ord,
		cod_1ord,
		prezzo,
		paese,
		cod_bib_sugg,
		cod_sugg_bibl,
		id_ordine, // bid,
		stato_abb,
		cod_per_abb,
		prezzo_lire,
		reg_trib,
		anno_abb,
		num_fasc,
		data_fasc,
		annata,
		num_vol_abb,
		natura,
		data_fine,
		stampato,
		rinnovato,
		data_chiusura_ord,
		tbb_bilancicod_mat,
		ute_ins,
		ute_var,
		ts_ins,
		ts_var,
		fl_canc
		};

	TbaOrdini(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbaOrdini();
	bool loadRecord(const char *key);
};

#endif /* TBAORDINI_H_ */
