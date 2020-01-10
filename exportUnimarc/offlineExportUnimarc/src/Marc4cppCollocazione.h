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
 * Marc4cppCollocazione.h
 *
 *  Created on: 3-gen-2009
 *      Author: Arge
 */

#ifndef MARC4CPPCOLLOCAZIONE_H_
#define MARC4CPPCOLLOCAZIONE_H_

#include "MarcRecord.h"
#include "TbTitolo.h"
#include "Tb950Inv.h"
#include "Tb950Coll.h"
#include "Tb950Ese.h"
#include "Biblio950.h"
#include "library/tvvector.h"
#include "library/CKeyValueVector.h"
#include "Marc4cppDocumento.h"
#include "Marc4cppLegami.h"
#include "TbfBiblioteca.h"
#include "TbcNotaInv.h"
#include "TbcPossessoreProvenienza.h"
#include "TrcPossProvInventari.h"
#include "TbcSezioneCollocazione.h"

#define TIPO_RECORD_ESEMPLARE 		1
#define TIPO_RECORD_COLLOCAZIONE 	2
#define TIPO_RECORD_INVENTARIO 		3
#define TIPO_RECORD_SCONOSCIUTO		4

#define DOING_TAG_950	950
#define DOING_TAG_960	960

class Marc4cppCollocazione {
    Biblio950 *curBiblio950;
    char livelloGerarchico;
    CKeyValueVector *bibliotecheDaNonMostrareIn950KV;
    CKeyValueVector *bibliotecheKV; // idBiblioteca / Biblio950
    //CKeyValueVector *codiciNotaKV;
    CKeyValueVector *sezioniDiCollocazioneDaNonMostrareIn960KV;
    CKeyValueVector *tbcSezioneCollocazioneQuadreKV;
    CString curCdBib;
    CString ultimaBibliotecaInventari;
    DataField *df956;
    int doingTag;
    int ultimoTipoRecordLetto;
    Marc4cppDocumento *marc4cppDocumento;
    Marc4cppLegami *marc4cppLegami;
    MarcRecord  *marcRecord;
    TbfBiblioteca *tbfBiblioteca;

    Tb950Coll   *tb950Coll;
    Tb950Ese    *tb950Ese;
    Tb950Inv    *tb950Inv;
    TbcNotaInv  *tbcNotaInv;
    TbcPossessoreProvenienza    *tbcPossessoreProvenienza;
    TbcSezioneCollocazione      *tbcSezioneCollocazione;
    TbTitolo    *tbTitolo;
    TrcPossProvInventari        *trcPossProvInventari;


    bool addBiblioteca(char *cdBiblioteca);
    bool addFields956(Tb950Inv * tb950Inv, Tb950Coll *tb950CollPtr);
    bool creaPossessore(Tb950Inv * tb950InvPtr, Tb950Coll *tb950CollPtr, char *keyInvetarioPossProv);
    //bool creaTag960InventariNonCollocati(char *cdBib, Biblio950 *biblio950);
    bool init();
    char elaboraLivelloGerarchicoNotizia(char *bid);
    void clearBiblio950Vect();
    void costruisciInventarioE(Tb950Inv * tb950InvPtr, Tb950Coll *tb950CollPtr, CString &inventario, DataField *df);
    void costruisciInventarioE960(Tb950Inv * tb950InvPtr,   CString &inventario, DataField *df950);

    void crea960CollocazioneNormalizzata(CString &s, Tb950Inv * tb950Inv, Tb950Coll *tb950Coll); // , CString *sPtr
    void creaTags950Collocazione(Collocazione950 *collocazione950, DataField *df);

    void creaTags950Collocazione_noInv(Collocazione950 *collocazione950, DataField *df); // 03/03/2015

    void creaTags950Inventari(ATTValVector<Tb950Inv *> *tb950InvVect, DataField *df, Tb950Coll *tb950Coll);
//    void creaTags950Inventario(Tb950Inv *tb950Inv, DataField *df);
    void creaTags950Inventario(Tb950Inv *tb950Inv, Tb950Coll *tb950Coll, DataField *df); // 01/04/2015

    void stop();

    bool creaTag950_Gestionale(char *bid);
    bool creaTag960_Gestionale(char *bid);
    bool creaTag956_digitale(char *bid);

public:
	Marc4cppCollocazione(
			MarcRecord *marcRecord, Marc4cppDocumento *marc4cppDocumento,
			Marc4cppLegami *marc4cppLegami,
//			CKeyValueVector *tbfBibliotecaKV,
			CKeyValueVector *tbcSezioneCollocazioneQuadreKV,
			TbTitolo *tbTitolo, Tb950Inv *tb950Inv, Tb950Coll *tb950Coll, Tb950Ese *tb950Ese,
			TbfBiblioteca *tbfBiblioteca,
			TbcNotaInv *tbcNotaInv,
			TbcPossessoreProvenienza 	*tbcPossessoreProvenienza,
			TrcPossProvInventari 		*trcPossProvInventari,
			TbcSezioneCollocazione 		*tbcSezioneCollocazione,
			CKeyValueVector *sezioniDiCollocazioneDaNonMostrareIn960KV,
			CKeyValueVector *bibliotecheDaNonMostrareIn950KV





	);
	virtual ~Marc4cppCollocazione();

	bool elaboraDatiCollocazione(char *bid, char livelloGerarchico);
	void creaTag316_NotaAllaCopiaPosseduta(Tb950Inv * tb950InvPtr, DataField *df950);


};

#endif /* MARC4CPPCOLLOCAZIONE_H_ */
