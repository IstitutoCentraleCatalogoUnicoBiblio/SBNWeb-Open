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
 * Marc4cppDocumento.h
 *
 *  Created on: 29-dic-2008
 *      Author: Arge
 */


#ifndef MARC4CPPDOCUMENTO_AUTHORITY_H_
#define MARC4CPPDOCUMENTO_AUTHORITY_H_

#include "MarcConstants.h"
#include "MarcRecord.h"
#include "TbNumeroStd.h"
#include "TbTitolo.h"
#include "C200.h"
#include "C205.h"
#include "C210.h"
#include "TbImpronta.h"
#include "TbGrafica.h"
#include "TbCartografia.h"
#include "TbMusica.h"
#include "TbAutore.h"
#include "TbSoggetto.h"
#include "TbDescrittore.h"
#include "TbTitSet2.h"






class Marc4cppDocumentoAuthority {
	MarcRecord 		*marcRecord;
	TbAutore		*tbAutore;
	TbSoggetto		*tbSoggetto;
	TbDescrittore *tbDescrittore;
	TbTitolo 		*tbTitolo;
	TbTitSet2 		*tbTitSet2;

	char *tagsToGenerateBufferPtr;
	int tagsToGenerate;
	int authority;

	char *polo;
    ControlField * creaTag001_IdentificatoreRecord();
    ControlField * creaTag005_IdentificatoreVersione();
    DataField * creaTag015_Isadn();
    DataField * creaTag010_Isni();

    DataField * creaTag100();
    DataField * creaTag101_autore();
    DataField * creaTag101_titolo();
    DataField * creaTag102();
    DataField * creaTag152_autore();
    DataField * creaTag152_titolo();

    DataField * creaTag200();
    DataField * creaTag210();
    DataField * creaTag230TitoloUniforme();
	DataField * creaTag231TitoloDellOpera();

    DataField * creaTag250Soggetti();
    DataField * creaTag260LuogoDiPubblicazioneNormalizzzato();


    DataField * creaTag300Note();
    DataField * creaTag305(char *areaStartPtr, char *areaEndPtr);

    DataField * creaTag676();
    DataField * creaTag686();

	DataField * creaTag801FonteDiProvenienza();
	DataField * creaTag830NoteCatalogatoreAutore();
	DataField * creaTag830NoteCatalogatoreTitolo();

	DataField * creaTag856();

    DataField * creaTag928(char *areaStartPtr, char *areaEndPtr);
    DataField * creaTag930(char *areaStartPtr, char *areaEndPtr);
    DataField * creaTag931(char *areaStartPtr, char *areaEndPtr);

    void creaSottocampiQualificazione(DataField *df, CString *csPtr);

//	bool elabra200(DataField * df, C200 *c200);
//	DataField * creaTag205(char *areaStartPtr, char *areaEndPtr);
//	void creaTag205Antico(C205 *c205, char *area205);
//	void creaTag205NonAntico(C205 *c205, char *area205);
//	void elabora205(DataField *df, C205 *c205);


//	void estraiUnicoElemento210(CString &s, C210 * c210);
	bool contieneAnno(CString &stringa);
	bool isAnnoBeforeSeparatore(CString &stringa);

//	void elaboraDatiTipoNumeroStandard();

//	bool isAntico();
//	bool isModerno();
	char getQualificazioneType(CString *parteDiQualificazione);


//	void ricalcolaAreeIsbdUtf8(const char *isbd, ATTValVector<CString *>  *areeVect);

	void init(MarcRecord *marcRecord, char *polo, char *tagsToGenerateBufferPtr, int tagsToGenerate, int authority);

public:

	Marc4cppDocumentoAuthority(MarcRecord *marcRecord, TbAutore *tbAutore, char *polo, char *tagsToGenerateBufferPtr, int tagsToGenerate, int authority);
	Marc4cppDocumentoAuthority(MarcRecord *marcRecord, TbSoggetto *tbSoggetto, TbDescrittore *tbDescrittore, char *polo, char *tagsToGenerateBufferPtr, int tagsToGenerate, int authority);
	Marc4cppDocumentoAuthority(MarcRecord *marcRecord, TbAutore *tbAutore, TbTitolo *tbTitolo, TbTitSet2 *tbTitSet2, char *polo, char *tagsToGenerateBufferPtr, int tagsToGenerate, int authority); // 04/04/2016

	virtual ~Marc4cppDocumentoAuthority();

	bool elaboraDatiDocumento(bool isTitoloOpera);
//	bool elaboraAltriDatiDocumento();
	bool isTagToGenerate(const char *nomeTag);

};

#endif /* MARC4CPPDOCUMENTO_AUTHORITY_H_ */
