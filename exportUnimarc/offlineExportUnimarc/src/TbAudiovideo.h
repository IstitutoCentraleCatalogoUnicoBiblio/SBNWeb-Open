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
#ifndef TBAUDIOVIDEO_H_
#define TBAUDIOVIDEO_H_

#include "Tb.h"

class TbAudiovideo : public Tb{

	void initDereferencing();


public:
/*
	enum fieldId {
		bid_polo,
		tp_mater_audiovis_polo,
		lunghezza_polo,
		cd_colore_polo,
		cd_suono_polo,
		tp_media_suono_polo,
		cd_dimensione_polo,
		cd_forma_video_polo,
		cd_tecnica_polo,
		tp_formato_film_polo,
		cd_mat_accomp_polo,
		cd_forma_regist_polo,
		tp_formato_video_polo,
		cd_materiale_base_polo,
		cd_supporto_second_polo,
		cd_broadcast_polo,
		tp_generazione_polo,
		cd_elementi_polo,
		cd_categ_colore_polo,
		cd_polarita_polo,
		cd_pellicola_polo,
		tp_suono_polo,
		tp_stampa_film_polo,
		cd_deteriore_polo,
		cd_completo_polo,
		dt_ispezione_polo,
		ute_ins_polo,
		ts_ins_polo,
		ute_var_polo,
		ts_var_polo,
		fl_canc_polo
	};
*/
	enum fieldId { // Indice
		bid,
		cd_livello,
		tp_mater_audiovis,
		lunghezza,
		cd_colore,
		cd_suono,
		tp_media_suono,
		cd_dimensione,
		cd_forma_video,
		cd_tecnica,
		tp_formato_film,
		cd_mat_accomp_1,
		cd_mat_accomp_2,
		cd_mat_accomp_3,
		cd_mat_accomp_4,
		cd_forma_regist,
		tp_formato_video,
		cd_materiale_base,
		cd_supporto_second,
		cd_broadcast,
		tp_generazione,
		cd_elementi,
		cd_categ_colore,
		cd_polarita,
		cd_pellicola,
		tp_suono,
		tp_stampa_film,
		cd_deteriore,
		cd_completo,
		dt_ispezione,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc
	};


	TbAudiovideo(CFile *TbAudiovideoIn, CFile *TbAudiovideoOffsetIn, char *offsetBufferTbAudiovideoPtr, long elementsTbAudiovideo, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbAudiovideo();
	bool loadRecord(const char *key);
};





#endif /* TBAUDIOVIDEO_H_ */
