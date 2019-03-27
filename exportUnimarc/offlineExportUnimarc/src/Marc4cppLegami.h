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
 * Marc4cppLegami.h
 *
 *  Created on: 29-dic-2008
 *      Author: Arge

Campi delle relazioni trTitTitRelIn
	bid_coll		char(10)
	tp_legame		char(2)
	tp_legame_musica	char(1)
	cd_natura_base	char(1)
	cd_natura_coll	char(1)
	sequenza		char(10)


Campi delle relazioni trTitAutRelIn
	vid					char(10)
	tp_responsabilita	char(1)
	cd_relazione		char(3)
	fl_incerto			char(1)
	fl_superfluo		char(1)


 */


#ifndef MARC4CPPLEGAMI_H_
#define MARC4CPPLEGAMI_H_

#include "library/CFile.h"
#include "library/CKeyValueVector.h"
#include "library/tree.hh"
#include "library/tvvector.h"
#include "Marc4cppDocumento.h"
#include "MarcRecord.h"
#include "TbaOrdini.h"
#include "TbAutore.h"
#include "TbClasse.h"
#include "TbCodici.h"
#include "TbComposizione.h"
#include "TbIncipit.h"
#include "TbLuogo.h"
#include "TbMarca.h"
#include "TbPersonaggio.h"
#include "TbRappresent.h"
#include "TbSoggetto.h"
#include "TbTitolo.h"
#include "TrTitAut.h"
#include "TrTitBib.h"
#include "TrTitMar.h"
#include "TrTitTit.h"
#include "TrAutAut.h"
#include "TbReticoloTit.h"
#include "TrTitCla.h"
#include "TrTitLuo.h"
#include "TrTitAutRel.h"

#include "TbRepertorio.h"
#include "TrRepMar.h"
#include "TrPerInt.h"
#include "TsLinkMultim.h"
#include "TbParola.h"
#include "Tb950Inv.h"
#include "Tb950Coll.h"
#include "Tb977.h"

#include "TbTermineTesauro.h"
#include "TbDescrittore.h" // 28/06/2016
#include "TrSogDes.h" // 28/06/2016
#include "TrDesDes.h" // 28/06/2016


class Marc4cppLegami {
#define SOGGETTARIO_NON_DEFINITO 0
#define SOGGETTARIO_VECCHIO 1
#define SOGGETTARIO_NUOVO 2

	CString polo;
    bool legameTitoloTitolo_312_done;
    bool notaAlLegame_311;
    CFile* trTitSogBibIn;
    CFile* trTitSogBibOffsetIn;
    CFile* trTitTitInvRelIn;
    CFile* trTitTitInvRelOffsetIn;
    CFile* trTitTitRelIn;
    CFile* trTitTitRelOffsetIn;
    char idBuffer[10+1];
    char rootBid[10+1];
    char* offsetBufferTrTitSogBibPtr;
    char* offsetBufferTrTitTitInvRelPtr;
    char* offsetBufferTrTitTitRelPtr;
    CKeyValueVector *bibliotecheDaNonMostrareIn950KV;
    CKeyValueVector *legamiVectorKV;
    CKeyValueVector *tbCodiciKV;
    int classeKeyPlusOffsetPlusLfLength;
    int keyPlusOffsetPlusLfLength;
    int key_length;
    int trKeyPlusOffsetPlusLfLength;
    long elementsTrTitSogBib;
    long elementsTrTitTitInvRel;
    long elementsTrTitTitRel;
    Marc4cppDocumento *marc4cppDocumento;
    MarcRecord      *marcRecord;
    tree<std::string> reticolo;
    tree<std::string>::sibling_iterator siArg; // causa problemi di memoria provo a passare non tramite argument

    TbaOrdini       *tbaOrdini;
    TbAutore        *tbAutore;
    TbClasse        *tbClasse;
    TbComposizione  *tbComposizione;
    TbfBiblioteca   *tbfBiblioteca;

    Tb950Inv		*tb950Inv;	// 12/09/14
	Tb950Coll		*tb950Coll;
	Tb977 			*tb977; // 16/12/14

    TbIncipit       *tbIncipit;
    TbLuogo         *tbLuogo;
    TbMarca         *tbMarca;
    TbMusica        *tbMusica;
    TbNumeroStd     *tbNumeroStandard;
    TbParola        *tbParola;
    TbPersonaggio   *tbPersonaggio;
    TbRappresent    *tbRappresent;
    TbRepertorio    *tbRepertorio;
    TbSoggetto      *tbSoggetto;
    TbTitolo        *tbTitolo;
    TrAutAut        *trAutAutInv;
    TrPerInt        *trPerInt;
    TrRepMar        *trRepMar;
    TrTitAut        *trTitAut;
    TrTitAutRel     *trTitAutRel;
    TrTitBib        *trTitBib;
    TrTitCla        *trTitCla;
    TrTitLuo        *trTitLuo;
    TrTitMar        *trTitMarRel;
    TrTitMar        *trTitMarTb;
    TrTitTit        *trTitTit;
    TsLinkMultim    *tsLinkMultim;
    TbTermineTesauro *tbTermineTesauro;
    TbDescrittore	*tbDescrittore; // 28/06/2016
	TrSogDes 		*trSogDes; // 28/06/2016
	TrDesDes 		*trDesDes; // 28/06/2016

    bool haResponsabilita1o2(const tree<std::string>& tr);
    bool haResponsabilitaPrincipale(const tree<std::string>& tr);
    bool haUnAutore(const tree<std::string>& tr, char *respPtr);
    bool isAntico();
    bool isAnticoE(const char *bid);
    bool isAutore(const char *id);
    bool isModerno();
    char* getSottoLivelloTitolo410(const tree<std::string> &reticolo, int curSottolivello, int sottoLivello);
    DataField * creaLegameAutoreAutore(char *entryReticoloPtr, int pos, char *vidPadre);
    DataField * creaLegameTitoloAutore(char *entryReticoloPtr, int pos);
    DataField * creaLegameTitoloAutore(char *vid, char tipoLegame, CString * cdRelazione, CString *cdStrumentoMusicale);
    DataField * creaLegameTitoloClasse(char *entryReticoloPtr, int pos);
    DataField * creaLegameTitoloLuogo(char *entryReticoloPtr, int pos);
    DataField * creaLegameTitoloMarca(char *entryReticoloPtr, int pos);
    DataField * creaLegameTitoloSoggetto(char *entryReticoloPtr, int pos);
    DataField * creaLegameDescrittoreDescrittore();
    void creaLegameSoggettoVariante();	// 15/01/2018
    DataField * creaSoggettoVarianteSS(CString *key_des,  CKeyValueVector *variantiKV, DataField *df, int idx, char subfieldID); // 03/04/2018
//    void creaSoggettoCompostoNonPreferito(CKeyValueVector *scomposizioniKVV); // , DataField * df

    DataField * creaSoggettoCompostoNonPreferito(CKeyValueVector *scomposizioniKVV, DataField *df);
    DataField * creaSoggettoCNP1(CKeyValueVector *scomposizioniKVV, DataField *df, int i, int ctr);
    DataField * creaCompostoNonPreferito(CKeyValueVector *scomposizioniKVV, DataField *df);




    DataField * creaSubtag200(DataField *df);
    DataField * creaSubtag200Area1(DataField *df, bool fieldTag, bool keepNoSort);
    int getVidAutoreResponsabilitaPrincipale(const tree<std::string>& tr, CString &autorePrincipale);
    int getVidFirstAutore(const tree<std::string>& tr, CString &autore);
    void addAutoreConResponsabilitaPrincipale(DataField *df, const tree<std::string>& reticolo);
    void addAutoreFormaAccettata(DataField *df, char tipoLegame, char *vidPadre);
    void appendSubtag200Area1DolA(CString *destS, bool fieldTag, bool keepNoSort);
    void clearLegamiVector();
    void clearTbCodiciVEctor();
    void contaOccorrenzeBidInReticoloPer410(const tree<std::string> &reticolo, char *bidToSearch, int *occorrenze, CKeyValueVector *legamiTitTit410KV, bool root);
    void contaSottoLivelliTitolo(const tree<std::string>& reticolo, int *sottolivello);
    void creaLegamiAutoreAutore();

    void creaLegamiTitoloAutore();
    void creaLegamiTitoloClassificazione();
    void creaLegamiTitoloIncipit();
    void creaLegamiTitoloLuogo();
    void creaLegamiTitoloMarca();
    void creaLegamiTitoloPersonaggi();
    void creaLegamiTitoloSoggetto();
    void creaLegamiTitoloTitolo();
//    void creaLegamiRaccolteFattizie(char *bid);

    void creaLegamiTitoliTesauro();
    DataField * creaLegameTitoloTesauro(char *entryReticoloPtr, int pos);

    void creaSubtag7xx(DataField *df);
    void creaTag410_InCascata_Indice(int sottoLivelli, const tree<std::string> &reticolo, CKeyValueVector *legamiTitTit410KV, CKeyValueVector *sequenzeTitTit410KV);
    void creaTag410_InCascata_Polo(int sottoLivelli, const tree<std::string> &reticolo, CKeyValueVector *legamiTitTit410KV, CKeyValueVector *sequenzeTitTit410KV);
//    void creaTag71x(DataField *df, CString *cdRelazione);
//    void creaTag71x(DataField *df, CString *cdRelazione, char tipoResponsabilita); // 26/04/2011
    void creaTag71x(DataField *df, CString *cdRelazione, char tipoResponsabilita, CString *cdStrumentoMusicale); // 26/01/2015



    void creatTag7xxNidificato(DataField *df, CString *bid);
    void elabora42x(char *bid);
    void elabora44x(char *bid);
    void elabora46x(char *bid, bool bidHaPadre); // , char *sequenza
    void elabora46y(char *bid, TbReticoloTit *tbReticoloTit);
    void elaboraOrdini();
    void getEntityId(char *entryReticolo, char *bid);
    void init();
    void initLicr();
    void print_tree(const tree<std::string>& tr, tree<std::string>::pre_order_iterator it, tree<std::string>::pre_order_iterator end);
    void dump_reticolo(const tree<std::string>& tr, tree<std::string>::pre_order_iterator it, tree<std::string>::pre_order_iterator end);

    void stop();

    void creaTag225_AreaCollezione(int sottoLivelli, const tree<std::string> &reticolo, CKeyValueVector *legamiTitTit410KV, CKeyValueVector *sequenzeTitTit410KV);
    DataField * creaTag311_NotaLegame(char *bid, char *bidColl, char naturaBase, CString *tpLegame, char naturaColl);
    DataField * creaTag312_NotaLegameVariante(char *bid, char *bidColl, char naturaBase, CString * tpLegame, char naturaColl);
    DataField * creaTag314_NotaLegameTitoloAutore(char *bid, char *entryReticoloPtr, int pos);
    DataField * creaTag421_SupplementoDi();
    DataField * creaTag422_PadreSupplemento();
    DataField * creaTag423_PubblicatoCon(tree<std::string>::sibling_iterator ch);
    DataField * creaTag430_ContinuazioneDi();
    DataField * creaTag431_ContinuaInParteIn();
    DataField * creaTag434_AssorbitoDa();
    void creaTag410_TitoliCollane(CKeyValueVector *sottolivelliTitTit410KV, CKeyValueVector *legamiTitTit410KV,CKeyValueVector *sequenzeTitTit410KV);
    DataField * creaTag440_ContinuatoCon();
    DataField * creaTag441_HaPerContinuazioneParziale();

    DataField * creaTag447_SiFondeConPerFormare();
    DataField * creaTag451_AltraEdizioneStessoSupporto();
    DataField * creaTag454_TraduzioneDi(const tree<std::string>& tr);
    DataField * creaTag461_FaParteDi_NotiziaSuperiore(CString *sequenza); //
    DataField * creaTag462_LegameALivelloIntermedio(CString *bid, CString *sequenza);
    DataField * creaTag463_PezzoFisico(CString *bid, CString *sequenza);
    DataField * creaTag464_AnaliticaSpoglio(CString *bid, CString *sequenza);
    DataField * creaTag488_ContinuazioneDi(char *bid, char *bidColl, char naturaBase, CString *tpLegame, char naturaColl);
    DataField * creaTag500_TitoloUniforme(const tree<std::string>& tr, bool responsabilitaM1o2);
    DataField * creaTag_TitoloDellOpera(const tree<std::string>& tr, bool responsabilitaM1o2); // 20/06/2017
    DataField * creaTag506_TitoloDellOpera(); // const tree<std::string>& tr, bool responsabilitaM1o2
    DataField * creaTag576_TitoloDellOpera(CString *bid_coll); // 20/06/2017 , const tree<std::string>& tr, bool responsabilitaM1o2

    DataField * creaTag510_TitoloParallelo();
    DataField * creaTag517_AltreVariantiTitolo(CString *tp_legame_musica);
    DataField * creaTag520_ContinuazioneDiPeriodici(char *bid, char *bidColl, char naturaBase, CString *tpLegame, char naturaColl);
    DataField * creaTag530_TitoloChiavePeriodici(const tree<std::string>& tr);
    DataField * creaTag532_TitoloEstrapolato(char *bid, char *bidColl, char naturaBase, CString *tpLegame, char naturaColl);

    DataField * creaTag560_RaccoltaFattizia(char *bid_base); // 21/07/2014

    DataField * creaTag606_SoggettiOrdinari();
    DataField * creaTag620_LuogoDiPubblicazione();
    DataField * creaTag676_ClassificazioneDecimaleDewey();
    DataField * creaTag686_AltraClassificazione();
    DataField * creaTag689_Tesauro();

    DataField * creaTag700_NomiPersonaliResponsabilitaPrincipale(CString  *cdRelazione, CString *cdStrumentoMusicale);
    DataField * creaTag701_NomiPersonaliResponsabilitaAlternativa(CString *cdRelazione, CString *cdStrumentoMusicale);
    DataField * creaTag702_NomiPersonaliResponsabilitaSecondaria(CString *cdRelazione, char tipoResponsabilita, CString *cdStrumentoMusicale); //
    DataField * creaTag710_NomeDiEnte_ResponsabilitaPrincipale(CString *cdRelazione, CString *cdStrumentoMusicale);
    DataField * creaTag711_NomeDiEnte_ResponsabilitaAlternativa(CString *cdRelazione, CString *cdStrumentoMusicale);
    DataField * creaTag712_NomeDiEnte_ResponsabilitaSecondaria(CString *cdRelazione, char tipoResponsabilita, CString *cdStrumentoMusicale);
    DataField * creaTag790_RinvioAutorePersonale(char tipoLegame, char *vidPadre);
    DataField * creaTag791_RinvioAutoreCollettivo(char tipoLegame, char *vidPadre);
    void creaTag856_LinkMultimediale(const char *bid);
    DataField * creaTag899_Localizzazione(bool has430, bool has440);
    DataField * creaTag977_localizzazioni(const char *bid);
    DataField * creaTag967_legamiNM(const char *bid, int mCtr, int nCtr);

    DataField * creaTag921_Marca();
    void creaTag922_Rappresentazione();
    void creaTag923_Notazione();
    void creaTag926_Incipit();
    void creaTag927_PersonaggiEInterpreti();
    void creaTag928_TitoloUniformeMusicale();
    void creaTag929_Composizione();
    bool creaTag951_Ordine();
    bool creaTag961_Ordine();

    void creaField7xxVoceStrumento(DataField *df, CString *cdStrumentoMusicale); // 26/01/2015

    void remove_225_410(const tree<std::string>& tr);

public:
	Marc4cppLegami(MarcRecord *marcRecord, Marc4cppDocumento *marc4cppDocumento,
			TbTitolo *tbTitolo, TbAutore *tbAutore, TbSoggetto* tbSoggetto, TbClasse* tbClasse,

			TbMarca *tbMarca,

			TbLuogo *tbLuogo, TbMusica *tbMusica, TbComposizione *tbComposizione, TbIncipit *tbIncipit,
			TbPersonaggio *tbPersonaggio, TbRappresent *tbRappresent, TbaOrdini *tbaOrdini,
			TrTitTit *trTitTit, //
			CFile* trTitTitRelIn, CFile* trTitTitRelOffsetIn, char* offsetBufferTrTitTitRelPtr, long elementsTrTitTitRel,
			CFile* trTitTitInvRelIn, CFile* trTitTitInvRelOffsetIn, char* offsetBufferTrTitTitInvRelPtr, long elementsTrTitTitInvRel,
			TrTitBib *trTitBib,
			TrTitAut *trTitAut,
			TrTitAutRel *trTitAutRel,
			TrTitCla *trTitCla,
			TrTitMar *trTitMarRel, TrTitMar *trTitMarTb,
			TrAutAut *trAutAutInv,
			TrTitLuo *trTitLuo,
			TbNumeroStd *tbNumeroStandard,
			TbRepertorio *tbRepertorio,
			TrRepMar *trRepMar,
			TrPerInt *trPerInt,

			TsLinkMultim *tsLinkMultim, TbParola *tbParola,

			TbfBiblioteca 	*tbfBiblioteca,

			Tb950Inv *tb950Inv,	// 12/09/14
			Tb950Coll *tb950Coll,
			Tb977 *tb977,

			TbTermineTesauro *tbTermineTesauro,
			TbDescrittore *tbDescrittore, TrSogDes *trSogDes, TrDesDes *trDesDes, // 28/06/2016

			CKeyValueVector *bibliotecheDaNonMostrareIn950KV,

			int keyPlusOffsetPlusLfLength,
			int trKeyPlusOffsetPlusLfLength,
			int key_length,
			char *polo
			);

    virtual ~Marc4cppLegami();

    bool elaboraDatiLegamiAlDocumento(const tree<std::string>& reticolo);
    bool getBidColl(char *bid, CString *bidColl);
    bool isPartenzaLegame01MW(char *bid);
    bool isRiferimentoLegame01MW(char *bid);
    bool isRiferimentoLegame01MW_2(char *bid, char *bid_base);

    char getQualificazioneType(char *parteDiQualificazione, int nth, char tipoNome);
    int contaBidBase(char *bid);
    int contaBidColl(char *bid);
    void contaBidColl_MN(char *bid, int *mCtr, int *nCtr);

    void creaLegamiTitoloBiblioteca();
//    void creaTag71xNew(DataField *df, CString *cdRelazione, CString *ds_nome, char tp_nome, CString *id);
    void creaTag71xNew(DataField *df, CString *cdRelazione, CString *ds_nome, char tp_nome, CString *id, CString *cdStrumentoMusicale); // 26/01/2015



    void creaTag70xNew(DataField *df, CString *ds_nome, char tp_nome, CString *id);

};

#endif /* MARC4CPPLEGAMI_H_ */
