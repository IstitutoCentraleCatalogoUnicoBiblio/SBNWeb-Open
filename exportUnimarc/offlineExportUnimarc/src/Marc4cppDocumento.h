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
12 * Marc4cppDocumento.h
 *
 *  Created on: 29-dic-2008
 *      Author: Arge
 */


#ifndef MARC4CPPDOCUMENTO_H_
#define MARC4CPPDOCUMENTO_H_

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
#include "TrTitMar.h"
#include "TrTitBib.h"
#include "../include/library/CKeyValueVector.h"
#include "TbfBiblioteca.h"
#include "TbNota.h"
#include "TbNota300.h"
#include "TbNota321.h"
#include "TsLinkWeb.h"
#include "TbAudiovideo.h"
#include "TbDiscoSonoro.h"
#include "TbTitSet1.h"
#include "TbTitSet2.h"
#include "TrBidAltroid.h"


class Marc4cppDocumento {
	MarcRecord 		*marcRecord;
	TbTitolo 		*tbTitolo;
	TbImpronta 		*tbImpronta;
	TbNumeroStd 	*tbNumeroStandard;
	TbGrafica 		*tbGrafica;
	TbCartografia 	*tbCartografia;
	TbMusica 		*tbMusica;
	TrTitMar		*trTitMarRel;
	TrTitBib		*trTitBib;
	TbfBiblioteca 	*tbfBiblioteca;
	TbNota 			*tbNota;
	TbNota300		*tbNota300;
	TbNota321		*tbNota321;
	TsLinkWeb		*tsLinkWeb;
	TbAudiovideo 	*tbAudiovideo; 	// 16/02/2015
	TbDiscoSonoro 	*tbDiscoSonoro; // 19/02/2015
	TbTitSet1 		*tbTitset1; 	// 09/03/2015
	TbTitSet2 		*tbTitset2; 	// 31/03/2016
	TrBidAltroid	*trBidAltroId;	// 13/10/2016

	int poloId;
	int tagsToGenerate;
	char *tagsToGenerateBufferPtr;
	char *descPolo;
	CString polo; //char *polo;1
	CString bibliotecaRichiedenteScarico;

    ControlField * creaTag001_IdRecord();
    ControlField * creaTag003_Permalink(); // 21/01/2016
    ControlField * creaTag005_IdentificatoreVersione();

    DataField *creaTag011_Issn();
    void creaTag012_Impronta();

    DataField* creaTag010_Isbn(char tipoNumeroStd);
    DataField* creaTag013_Ismn();
    DataField* creaTag015_Isrn();
    DataField* creaTag016_Isrc();
    DataField* creaTag017_OtherStandardIdentifier();
    DataField* creaTag020_NumeroDiBibliografiaNazionale();
    DataField* creaTag022_NumeroDiPubblicazioneGovernativa();
    DataField* creaTag035();

    DataField* creaTag071_NumeroEditoreNonStandard(char tipoNumeroStd);
    DataField* creaTag072_Upc();
    DataField* creaTag073_Ean();
    DataField* creaTag090_NumeroDiBibliografiaCubi();
    DataField* creaTag100_CartaDIdentita();
    DataField* creaTag101_LinguaPubblicazione();
    DataField* creaTag102_PaeseDiPubblicazione();
    DataField* creaTag105_DatiCodificati(); // bool recordInTbTitset1
    DataField* creaTag110_Periodici(char natura);
    DataField* creaTag115_audiovisivo();
    DataField* creaTag116_ArteGrafica();
    DataField* creaTag120_MaterialiCartografici();
    DataField* creaTag121_MaterialeCartografico_CaratteristicheFisiche();
    DataField* creaTag123_MaterialeCartografico_ScalaECoordinate();
    DataField* creaTag124_MaterialeCartografico_MaterialeSpecifico();
    DataField* creaTag125_RegistrazioniSonoreMusicaAStampa(char *bid);
    DataField* creaTag126_DiscoSonoro();
    DataField* creaTag127_DiscoSonoroDurata();
    DataField* creaTag128_EsecuzioniMusicaliEPartiture(char *bid);

    DataField* creaTag181_Area0_DatiCodificati(char *bid);
    DataField* creaTag182_Area0_TipoMediazione(char *bid);
    DataField* creaTag183_Area0_TipoSupporto(char *bid);

    DataField* creaTag205_AreaEdizione(char *areaStartPtr, char *areaEndPtr);
    DataField* creaTag206_AreaSpecificaDelMateriale(char *areaStartPtr, char *areaEndPtr);
    DataField* creaTag207_AreaSpecificaMateriali_PeriodiciNomiDate(char *areaStartPtr, char *areaEndPtr);
    DataField* creaTag210_AreaPubblicazioneProduzioneDistribuzione(char *areaStartPtr, char *areaEndPtr);
    void creaTag210_sottoarea(DataField *df, CString *sottoarea, int type);

    DataField* creaTag215_AreaDescrizioneFisica(char *areaStartPtr, char *areaEndPtr);
    DataField* creaTag230_AreaRisorseElettroniche(char *areaStartPtr, char *areaEndPtr);
    DataField* creaTag300_NoteGenerali(char *areaStartPtr, char *areaEndPtr);
    DataField* creaTag300_321_NoteGenerali_ConRiferimenti(char * bid);

    void creaTag323_NoteAlCast();
    void creaTag327_NoteDiContenuto();
    DataField* creaTag326_NotePeriodicita();
    void creaTag330_Abstract();
    void creaTag336_NoteRisorsaElettronica();
    void creaTag337_NoteTecnicheRisorsaElettronica();


    DataField* creaTag801_FonteDiProvenienza();
    DataField* creaTag850_IstitutoDetentore();


    bool contieneAnno(CString &stringa);
    bool isAnnoBeforeSeparatore(CString &stringa);
    DataField* creaTag140_CodiciPerLibroAntico(); // bool recordInTbTitset1
    DataField* creaTag208_AreaSpecifica_MusicaAStampa(char *areaStartPtr, char *areaEndPtr);
    void addSubfield(DataField *df, char subfieldId, CString &data);

//    void creaTag200_AnticoNew(DataField *df, char *area200, int len);
//    void creaTag200_NonAnticoNew(DataField *df, char *area200, int len, char natura);
    void creaTag200_AnticoNew(DataField *df, CString *areaTitolo);	// 22/12/2015
    void creaTag200_NonAnticoNew(DataField *df, CString *areaTitolo, char natura);



    void creaTag205Antico(C205 *c205, char *area205);

    void creaTag205NonAntico(C205 *c205, char *area205);
    void elabora205(DataField *df, C205 *c205);
    void elaboraDatiTipoNumeroStandard();
    void elaboraNote(char *bid);
    void elaboraNote321(char * bid);
 //   void estraiUnicoElemento210(CString &s, C210 * c210); 12/01/2016
    void init(char *polo);

public:
//	bool isAntico();	// Test Materiale / anno di pubblicazione
//	bool isModerno();	// Test Materiale / anno di pubblicazione

    int	isAnticoModernoUndefined();	// 0=undefined 1=antico, 2=moderno
	bool isModerno105();	// Da tb_tit_set1 in base a info in s105_tp_testo_letterario


	bool isModernoE();	// Test carattere in 4ta posizione 19/05/2015
	bool isAnticoE(); 	// Test carattere in 4ta posizione


	bool isMusica();

	DataField * creaTag200_AreaTitoloEResponsabilita(CString *areaTitolo); // char *areaStartPtr, char *areaEndPtr

	Marc4cppDocumento(MarcRecord *marcRecord,
			TbTitolo *tbTitolo, TbImpronta * tbImpronta, TbNumeroStd *tbNumeroStandard, TbGrafica *tbGrafica,
			TbCartografia *tbCartografia, TbMusica *tbMusica,
			TbTitSet1 *tbTitSet1, TbTitSet2 *tbTitSet2,
			TbAudiovideo *tbAudiovideo,
			TbDiscoSonoro *tbDiscoSonoro,
			TrTitMar *trTitMar, TrTitBib *trTitBib, TbfBiblioteca *tbfBiblioteca,
			TbNota *tbNota, TbNota300 *tbNota300, TbNota321 *tbNota321, TrBidAltroid *trBidAltroId,
			TsLinkWeb *tsLinkWeb,
			char *polo, char *descPolo,	char *bibliotecaRichiedenteScarico, char *tagsToGenerateBufferPtr,
			int tagsToGenerate	);
	virtual ~Marc4cppDocumento();

	bool elaboraDatiDocumento();
	bool elaboraAltriDatiDocumento();
	bool elaboraDatiDocumentoDaLista();
//	bool isTagToGenerate(const char *nomeTag);
//	bool isTagToGenerate(int tag);

	char *getDescPolo();
	char *getPolo();
	CString *getPoloString();
	int getPoloId();
	CString *getBibliotecaRichiedenteScarico();
};

#endif /* MARC4CPPDOCUMENTO_H_ */
