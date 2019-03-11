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
 * TrTitoloCollocazione.h
 *
 *  Created on: 19-gen-2009
 *      Author: Arge
 */


#ifndef TB950INV_H_
#define TB950INV_H_

//#include "library/CString.h"
#include "Tb.h"


//#define TB950INV_FIELDS 17

class Tb950Inv :public Tb {

//	CFile* tb950InvIn;
//	CFile *tb950InvOffsetIn;
//	char *offsetBufferTb950InvPtr;
//	long elementsTb950Inv;
//	int keyPlusOffsetPlusLfLength;
//	int key_length;
//
//
//
//	ATTValVector<CString*> *fieldsVector;
//	void init();
//	void stop();

	CString curBid;
public:
	enum tb950InvFieldId {
		bid,
		tbinv_key_loc,
		nomeTabella,
		tbinv_cd_bib,
		tbinv_cd_serie,
		tbinv_cd_inven,

		tbinv_cd_sit, 	// 24/09/2009 11.29 tbinv_flg_disp,
		tbinv_data_ingresso, 	// 24/09/2009 11.30 tbinv_data_scarico,

		tbinv_seq_coll,
		tbinv_precis_inv,
// per le nuove etichette (960)
		tbinv_stato_con,
		tbinv_cd_frui,
		tbinv_sez_old,
		tbinv_loc_old,
		tbinv_spec_old,
//		tbinv_cd_supporto, Mantis 5037 non piu' usato. Al posto usare cd_mat_inv 22/08/2012
		tbinv_cd_mat_inv,

		tbinv_cd_riproducibilita,
		tbinv_cod_no_disp,
		tbinv_ts_ins_prima_coll, // 19/11/09

		// 30/03/2010
	    tbinv_digitalizzazione,
	    tbinv_disp_copia_digitale,
	    tbinv_id_accesso_remoto,
	    tbinv_rif_teca_digitale


	} ; // tbTitoloFieldId_type

	Tb950Inv(CFile* tb950InvIn, CFile *tb950InvOffsetIn, char *offsetBufferTb950InvPtr, long elementsTb950Inv, int keyPlusOffsetPlusLfLength, int key_length);
	Tb950Inv(Tb950Inv* Tb950Inv);
	virtual ~Tb950Inv();
};

#endif /* TB950INV_H_ */
