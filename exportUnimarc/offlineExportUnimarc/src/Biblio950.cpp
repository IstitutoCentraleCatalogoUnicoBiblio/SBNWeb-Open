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
 * Biblio950.cpp
 *
 *  Created on: 2-feb-2009
 *      Author: Arge
 */


#include "Biblio950.h"
#include "MarcGlobals.h"

#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif

extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);

Biblio950::Biblio950(const char *idBiblioteca, const char *descBiblioteca) {
	this->idBiblioteca = idBiblioteca;

	tb950InvNonCollocatiVect = new ATTValVector<Tb950Inv *>();
	tb950InvCollocatiVect = new ATTValVector<Tb950Inv *>(); // 27/02/2015
//	collocazione950SenzaEsemplareVectKV = new CKeyValueVector(tKVSTRING, tKVVOID);
	collocazione950AnyVectKV = new CKeyValueVector(tKVSTRING, tKVVOID);
	esemplare950VectKV = new CKeyValueVector(tKVSTRING, tKVSTRING);


	this->descBiblioteca = descBiblioteca;
}



char *Biblio950::getIdBiblioteca()
{
	return idBiblioteca.data();
}


char *Biblio950::getDescBiblioteca()
{
	return descBiblioteca.data();
}
ATTValVector<Tb950Inv *> *Biblio950::getTb950InvNonCollocatiVect()
{
	return tb950InvNonCollocatiVect;
}

ATTValVector<Tb950Inv *> *Biblio950::getTb950InvCollocatiVect()
{
	return tb950InvCollocatiVect;
}


//CKeyValueVector *Biblio950::getCollocazione950SenzaInventariVectKV()
//{
//	return collocazione950SenzaInventariVectKV;
//}

//CKeyValueVector *Biblio950::getCollocazione950SenzaEsemplareVectKV()
//{
//	return collocazione950SenzaEsemplareVectKV;
//}
CKeyValueVector *Biblio950::getCollocazione950VectKV()
{
	return collocazione950AnyVectKV;
}

CKeyValueVector *Biblio950::getEsemplare950VectKV()
{
	return esemplare950VectKV;
}

Biblio950::~Biblio950() {
	stop();
} // End ~Biblio950()

/*
bool Biblio950::addCollocazione(Tb950Coll *tb950Coll, Tb950Ese *tb950Ese)
{
	Collocazione950 *collocazione950;
	Esemplari950 *esemplare950;
	//char *bidColl;

	// Instanzia collocazone e salva


//printf ("\nCrea collocazione Biblio950::addCollocazione(Tb950Coll *tb950Coll, Tb950Ese *tb950Ese)");

	Tb950Coll *tb950CollNew = new Tb950Coll(tb950Coll);
	collocazione950 = new Collocazione950(tb950CollNew);

	// Questa collocazione appartiene ad un esemplare gia' instanziato?
	char* bidDoc = tb950Coll->getField(tb950Coll->tbcol_bid_doc);
	char *cdDoc = tb950Coll->getField(tb950Coll->tbcol_cd_doc);
	char *cdBib = tb950Coll->getField(tb950Coll->cd_biblioteca_sezione);
	CString esemplare = bidDoc;
	esemplare.AppendString(tb950Coll->getFieldString(tb950Coll->tbcol_cd_doc));
	esemplare.AppendString(tb950Coll->getFieldString(tb950Coll->cd_biblioteca_sezione));
	esemplare950 = findEsemplare950(esemplare);
	if (!esemplare950)
	{
		// Nuovo esemplare
		//================
		esemplare950 = creaEsemplare950(tb950Ese, bidDoc, cdDoc, cdBib, esemplare.data()); // Instanzia esemplare
		if (!esemplare950)
		{ // Collocazione non legata ad un esemplare
			//char *bidColl = tb950Coll->getField(tb950Coll->tbcol_bid_doc);
			char *bidColl = tb950Coll->getField(tb950Coll->tbcol_bid_doc); // 23/02/2010 12.39
			collocazione950SenzaEsemplareVectKV->Add(bidColl, collocazione950);
		}
		else
		{
			// Aggiungiamo la collocazione all'esemplare
			esemplare950->addCollocazione(collocazione950);

			// Aggiungi esemplare all'elenco degli esemplari
			esemplare950VectKV->Add(esemplare.data(),esemplare950);
		}
	}
// 28/05/09
	else
	{
		// Aggiungiamo la collocazione all'esemplare
		esemplare950->addCollocazione(collocazione950);
	}



	// Aggiungiamo la collocazione all'elenco delle collocazioni
	//collocazione950VectKV->Add(bidColl, collocazione950); // Serve per evitare  di ciclare sugli esemplari
	return true;
}
*/
void Biblio950::stop()
{
	// TO DO VERY CAREFULLY per via delle collocazioni presenti sia nella tabella delle collocazioni che degli esemplari
	Esemplari950* esemplare950;
	Collocazione950 *collocazione950;

	tb950InvNonCollocatiVect->DeleteAndClear();
	delete tb950InvNonCollocatiVect;
	tb950InvCollocatiVect->DeleteAndClear();
	delete tb950InvCollocatiVect;

//	//Rimuovi le collocazioni senza inventari
//	for (int i=0; i < collocazione950SenzaInventariVectKV->Length(); i++)
//	{
////printf ("\nRimuovi collocazione senza inventari");
//		collocazione950 = (Collocazione950*) collocazione950SenzaInventariVectKV->GetValue(i);
//
//		delete collocazione950;
//	}
//	delete collocazione950SenzaInventariVectKV;

//	// Rimuovi le collocazioni senza esemplare
//	for (int i=0; i < collocazione950SenzaEsemplareVectKV->Length(); i++)
//	{
////printf ("\nRimuovi collocazione senza esempalre");
//		collocazione950 = (Collocazione950*) collocazione950SenzaEsemplareVectKV->GetValue(i);
//		delete collocazione950;
//	}
//	delete collocazione950SenzaEsemplareVectKV;


	// Rimuovi le collocazioni
	for (int i=0; i < collocazione950AnyVectKV->Length(); i++)
	{
		collocazione950 = (Collocazione950*) collocazione950AnyVectKV->GetValue(i);
		delete collocazione950;
	}
	delete collocazione950AnyVectKV;



	// Rimuoviamo gli esemplari
//	for (int i=0; i < esemplare950VectKV->Length(); i++)
//	{
////printf ("\nRimuovi esempalre");
//		esemplare950 = (Esemplari950*)esemplare950VectKV->GetValue(i);
//		delete esemplare950;
//	}

	delete esemplare950VectKV;

	// Rimuoviamo le collocazioni
	// Teniamo presente che i valori (void *) sono gia' stati rimossi dalla rimozione degli esemplari
	// o dalle collocazioni senza esemplare


} // End stop



//Collocazione950 *Biblio950::findCollocazione950(char *bidColl)
Collocazione950 *Biblio950::findCollocazione950(char *keyLoc)
{
//	Collocazione950 *collocazione950  = (Collocazione950 *)collocazione950VectKV->GetValueFromKey(bidColl);
	Collocazione950 *collocazione950  = (Collocazione950 *)collocazione950AnyVectKV->GetValueFromKey(keyLoc);
	return collocazione950;
}



Esemplari950 *Biblio950::creaEsemplare950(Tb950Ese *tb950Ese, const char *bidDoc, const char *cdDoc, const char *cdBib, const char *idEsemplare)
{

	// Esemplare da instanziare
	// tramite il bidDoc e cdDoc ricerchiamo l'esemplare
	Esemplari950 *esemplare950 = 0;

	//const char *cdDocEse = 0;
	CString  cdDocEse;


//	const char *cdBibEse = 0;

//if (!strcmp(bidDoc, "SBL0135988"))
//{
//	printf ("\nBiblio950::creaEsemplare950");
//	printf ("\nFile=%s", ((CFile*)tb950Ese->getTbIn())->GetName());
//}

	if (tb950Ese->existsRecord(bidDoc))
	{
		bool inEsemplare = false;

		// Cicliamo sui bidese cercando il cdDoc ed il codice biblioteca
		while (tb950Ese->loadNextRecord(bidDoc))
		{
//if (!strcmp(bidDoc, "SBL0135988"))
//	printf ("\nStringRecord=%s", tb950Ese->getStringRecordData());

			if (!inEsemplare)
			{
				cdDocEse.assign(tb950Ese->getFieldString(tb950Ese->tbese_cd_doc));
				if (!strcmp(cdDocEse.data(), cdDoc))
					inEsemplare = true;
				else
					continue;
			}
			else // ero in esemplare. Sto uscendo?
			{
				if (strcmp(cdDocEse.data(), cdDoc))
				{
//					if (!strcmp(bidDoc, "SBL0135988"))
//						printf ("\nBREAK cdDocEse='%s', cdDoc='%s'", cdDocEse.data(), cdDoc);

					break;
				}
			}

			// Nostra biblioteca?
//			cdBibEse =  tb950Ese->getField(tb950Ese->cd_biblioteca);

//if (!strcmp(bidDoc, "SBL0135988"))
//	printf ("\ncdBib=%s, cdBibEse=%s, ", cdBib, tb950Ese->getField(tb950Ese->cd_biblioteca));

			if (!strcmp(cdBib, tb950Ese->getField(tb950Ese->cd_biblioteca))) // cdBibEse
			{// Trovata la biblioteca

				Tb950Ese *tb950EseNew = new Tb950Ese(tb950Ese);
				esemplare950 = new Esemplari950(tb950EseNew);
				return esemplare950;
			}
		}
	}
	else
	{
//		if (!strcmp(bidDoc, "SBL0135988"))
//			printf ("\nRecord non esiste");
	}

	return 0; // Fallito a creare esemplare
}



/*
bool Biblio950::addInventario(Tb950Inv *tb950Inv, Tb950Coll *tb950Coll, Tb950Ese *tb950Ese)
{
	// L'inventario non e' collocato?
//	long offset;
	Collocazione950 *collocazione950;
	Esemplari950 *esemplare950;
	char *bidColl = 0;
	bool retb;

	const char *padding [] = {
	"",
	"00000000",
	"0000000",
	"000000",
	"00000",
	"0000",
	"000",
	"00",
	"0",
	""
	};

//printf("\ntb950Inv->bid = %s", tb950Inv->getField(tb950Inv->bid));
//printf("\ntb950Inv->tbinv_key_loc = %s", tb950Inv->getField(tb950Inv->tbinv_key_loc));


	CString *keyLoc = new CString();
	keyLoc->assign(tb950Inv->getFieldString(tb950Inv->tbinv_key_loc));

//if (tb950Inv->getFieldString(tb950Inv->bid)->isEqual("SBL0135988"))
//{
//	printf ("\nBiblio950::addInventario keyLoc=%s", keyLoc->data());
//	tb950Inv->dumpRecord();
//}
	// L'Inventario e' collocato?
	// ==========================
//	if (keyLoc->GetFirstChar() == 'n' || keyLoc->GetFirstChar() == 'N') // null
	if (keyLoc->IsEmpty())
	{
		// Aggiungiamo a vettore degli inventari non collocati
		// Instanzia inventario da quello letto e salva
		Tb950Inv *tb950InvNew = new Tb950Inv(tb950Inv);
		tb950InvNonCollocatiVect->Add(tb950InvNew);
	}
	else
	{
		// L'Inventario e' collocato
		// =========================

		if (keyLoc->Length() < KEY_LOC_SIZE)
			keyLoc->PrependString(padding[keyLoc->Length()]);


		retb = tb950Coll->loadRecordByKeyLoc(keyLoc->data()); // leggiamo la collocazione
		if (!retb)
		{
			SignalAnError(__FILE__, __LINE__, "Collocazione non trovata per KeyLoc %s (inventario %s)", keyLoc->data(), tb950Inv->getField(tb950Inv->tbinv_cd_inven));
			delete keyLoc;
			return false;
		}

//if (tb950Inv->getFieldString(tb950Inv->bid)->isEqual("SBL0135988"))
//{
//	printf ("\ntb950Coll");
//	tb950Coll->dumpRecord();
//}

		bidColl = tb950Coll->getField(tb950Coll->bid); // Prendiamo il bidcoll

		// Abbiamo gia' instanziato la collocazione?
//		collocazione950 = findCollocazione950(bidColl);
		collocazione950 = findCollocazione950(keyLoc->data()); // 07/07/09

		// Tramite il keyLoc troviamo il bidColl
		if (!collocazione950) // lastkeyLoc->Compare(keyLoc)
		{   // Nuova collocazione
			// ==================
			// Instanziamo la collocazione per questo inventario
//			if (tb950Coll->loadRecordByKeyLoc(keyLoc))
//			{
				// Instanzia collocazone e salva

//printf ("\nCrea collocazione Biblio950::addInventario(Tb950Coll *tb950Coll, Tb950Ese *tb950Ese)");

				Tb950Coll *tb950CollNew = new Tb950Coll(tb950Coll);
				collocazione950 = new Collocazione950(tb950CollNew);
				// Agganciamo l'inventario alla collocazione
				Tb950Inv *tb950InvNew = new Tb950Inv(tb950Inv);

//printf("Argepf tb950Inv");
//tb950Inv->dumpRecord();
//
//printf("Argepf tb950InvNew");
//tb950InvNew->dumpRecord();

				collocazione950->addInventario(tb950InvNew);
				// Questa collocazione appartiene ad un esemplare gia' instanziato?
				char* bidDoc = tb950Coll->getField(tb950Coll->tbcol_bid_doc);
				char *cdDoc = tb950Coll->getField(tb950Coll->tbcol_cd_doc);
				char *cdBib = tb950Inv->getField(tb950Inv->tbinv_cd_bib);
				CString esemplare;esemplare = bidDoc;
				esemplare.AppendString(tb950Coll->getFieldString(tb950Coll->tbcol_cd_doc));
				esemplare.AppendString(tb950Inv->getFieldString(tb950Inv->tbinv_cd_bib));


if (*cdDoc != 0) //  && strcmp(cdDoc, "null")
{
//if (esemplare.StartsWith("SBL0135988"))
//	printf ("\nCerca esemplare %s", esemplare.data());
				esemplare950 = findEsemplare950(esemplare);
				if (!esemplare950)
				{
					// Nuovo esemplare
					//================
					esemplare950 = creaEsemplare950(tb950Ese, bidDoc, cdDoc, cdBib, esemplare.data()); // Instanzia esemplare
					if (!esemplare950)
					{ // Collocazione non legata ad un esemplare
						//char *bidColl = tb950Coll->getField(tb950Coll->tbcol_bid_doc);
						collocazione950SenzaEsemplareVectKV->Add(bidColl, collocazione950);
//if (esemplare.StartsWith("SBL0135988"))
//	printf ("\nCollocazione non legata ad un esemplare");

					}
					else
					{
						// Aggiungiamo la collocazione all'esemplare
						esemplare950->addCollocazione(collocazione950);

						// Aggiungi esemplare all'elenco degli esemplari
//if (esemplare.StartsWith("SBL0135988"))
//	printf ("\nAggiungiamo la collocazione all'esemplare %s", esemplare.data());

						esemplare950VectKV->Add(esemplare.data(),esemplare950);
					}
				}
				else
				{
					// Aggiungiamo la collocazione all'esemplare esistente
					esemplare950->addCollocazione(collocazione950);
//if (esemplare.StartsWith("SBL0135988"))
//	printf ("\nAggiungiamo la collocazione all'esemplare esistente");

				}
} // end if legame all'esemplare
else
{
	// Collocazione senza legame ad esemplare
	collocazione950SenzaEsemplareVectKV->Add(bidColl, collocazione950); // 29/09/2009 16.26
	//int i = 0; // for breakpoint
}
				// Aggiungiamo la collocazione all'elenco delle collocazioni
				collocazione950AnyVectKV->Add(bidColl, collocazione950); // Serve per evitare  di ciclare sugli esemplari


		}
			else
			{
				// Aggiungiamo l'inventario alla collocazione esistente
				Tb950Inv *tb950InvNew = new Tb950Inv(tb950Inv);
				collocazione950->addInventario(tb950InvNew);
			}
		}

		// Troviamo o creiamo una collcoazione
//		char *bidColl = Tb950Inv->getField(Tb950Inv->);
	delete keyLoc;
	return true;
} // End Biblio950::addInventario
*/


bool Biblio950::addInventario_coll_noColl(Tb950Inv *tb950Inv, Tb950Coll *tb950Coll, Tb950Ese *tb950Ese)
{
	// L'inventario non e' collocato?
	Collocazione950 *collocazione950;
	Esemplari950 *esemplare950;
	char *bidColl = 0, *bidEse=0;
	bool retb;

	const char *padding [] = {
	"",
	"00000000",
	"0000000",
	"000000",
	"00000",
	"0000",
	"000",
	"00",
	"0",
	""
	};

	char *bid = tb950Inv->getField(tb950Inv->bid);

	CString *keyLoc = new CString();
	keyLoc->assign(tb950Inv->getFieldString(tb950Inv->tbinv_key_loc));

	// L'Inventario e' collocato?
	// ==========================
	if (keyLoc->IsEmpty())
	{
		// Aggiungiamo a vettore degli inventari non collocati
		// Instanzia inventario da quello letto e salva
		Tb950Inv *tb950InvNew = new Tb950Inv(tb950Inv);
		tb950InvNonCollocatiVect->Add(tb950InvNew);
	}
	else
	{
		// L'Inventario e' collocato
		// =========================
		if (keyLoc->Length() < KEY_LOC_SIZE)
			keyLoc->PrependString(padding[keyLoc->Length()]);

		retb = tb950Coll->loadRecordByKeyLoc(keyLoc->data()); // leggiamo la collocazione

//printf ("addInventario_coll_noColl");
//tb950Coll->dumpRecord();

		if (!retb)
		{
			SignalAnError(__FILE__, __LINE__, "Collocazione non trovata per KeyLoc %s (inventario %s)", keyLoc->data(), tb950Inv->getField(tb950Inv->tbinv_cd_inven));
			delete keyLoc;
			return false;
		}


		Tb950Inv *tb950InvNew = new Tb950Inv(tb950Inv);

		// Bid coll = al bid di inventario?
		bidColl = tb950Coll->getField(tb950Coll->bid); // Prendiamo il bid di collocazione
		bidEse = tb950Coll->getField(tb950Coll->tbcol_bid_doc); // Prendiamo il bid di esemplare


		if (strcmp(bid, bidColl))
		{ // 10/03/2015 Mail Mery Bid di collocazione diverso da bid di inventario

			// Aggiungiamo la collocazione per bid di collocazione diverso
			addCollocazione_any(tb950Coll, tb950Ese);

			// Aggiungiamo l'esemplare associato alla collocazione con bid diverso (se esiste)
			// Troviamo l'esemplare
			char *eseCdDoc, *eseCdBib;
			char *colCdBib = tb950Coll->getField(tb950Coll->cd_biblioteca_sezione);
			char *colCdDoc = tb950Coll->getField(tb950Coll->tbcol_cd_doc); // cd_doc

			if (tb950Ese->existsRecord(bidEse))
			{
				// Cicliamo sui bidese cercando il cdDoc ed il codice biblioteca
				while (tb950Ese->loadNextRecord(bidColl))
				{
//tb950Ese->dumpRecord();

					eseCdDoc = tb950Ese->getField(tb950Ese->tbese_cd_doc);
					if (strcmp(eseCdDoc, colCdDoc)) // stesso cdDoc?
						continue; // Not found


					eseCdBib =  tb950Ese->getField(tb950Ese->cd_biblioteca);
					if (strcmp(eseCdBib, colCdBib))
						continue;

					CString eseKey = eseCdDoc;
					eseKey.AppendString(eseCdBib);
					addEsemplare(eseKey.data(), tb950Ese->getField(tb950Ese->tbese_cons_doc));
					break;
				}
			}

		} // end bid di inventario diverso da bid di collocazione
//		else
			tb950InvCollocatiVect->Add(tb950InvNew);
	}
	delete keyLoc;
	return true;

} // End Biblio950::addInventario_coll_noColl

bool Biblio950::addCollocazione_any(Tb950Coll *tb950Coll, Tb950Ese *tb950Ese)
{
	Collocazione950 *collocazione950;
	Esemplari950 *esemplare950;
	//char *bidColl;

	// Instanzia collocazione e salva


	// Collocazione gia' presente tra gli inventari?
	char *KeyLoc = tb950Coll->getField(tb950Coll->tbcol_key_loc);
//	Tb950Inv * tb950Inv;


	// Collocazione con o senza inventario
	// -----------------------------
//	Tb950Coll *tb950CollNew = new Tb950Coll(tb950Coll); // 14/01/2016 memory leak
//	collocazione950 = new Collocazione950(tb950CollNew);
	char *bidDoc = tb950Coll->getField(tb950Coll->tbcol_bid_doc); // bid_doc, legame ad esemplare

	// 01/07/2015 mail Mery (MO1 TSA0976170). Se bidDoc e KeyLoc gia' presenti non inserire!
	Collocazione950 * coll959KV;
	CKeyValue *ckv;
	for (int i=0; i < collocazione950AnyVectKV->Length(); i++)
	{
		ckv = collocazione950AnyVectKV->Get(i);
		if (!strcmp(ckv->Key.p, bidDoc) )
		{ // Stessa chiave

			// Stesso keyloc?
			coll959KV = (Collocazione950 *)ckv->Val.v;
			char * kl = coll959KV->getTb950Coll()->getField(tb950Coll->tbcol_key_loc);
			if (!strcmp(kl, KeyLoc) )
			{
				return true; // Duplicato. Non inserire

			}

		}
	}

	Tb950Coll *tb950CollNew = new Tb950Coll(tb950Coll); // 14/01/2016
	collocazione950 = new Collocazione950(tb950CollNew);
	collocazione950AnyVectKV->Add(bidDoc, collocazione950);


	return true;
} // End addCollocazione_any



void Biblio950::dump()
{
	Tb950Inv * tb950Inv;
	Tb950Coll * tb950Coll;
	Collocazione950 *collocazione950;

	// Dump INVENTARI NON COLLOCATI
	printf ("\nInventari NON collocati");
	printf ("\n-----------------------");
	if (!tb950InvNonCollocatiVect->Length())
		printf ("\n\tNessuno");
	else
	{
		for (int i=0; i < tb950InvNonCollocatiVect->Length(); i++)
		{
			tb950Inv = tb950InvNonCollocatiVect->Entry(i);
			printf ("\n\tcd_bib = '%s'", tb950Inv->getField(tb950Inv->tbinv_cd_bib));
			printf ("\tbid = '%s'", tb950Inv->getField(tb950Inv->bid));
			printf ("\ttbinv_key_loc = '%s'", tb950Inv->getField(tb950Inv->tbinv_key_loc));
			printf ("\tcd_inven = '%s'", tb950Inv->getField(tb950Inv->tbinv_cd_inven));
			printf ("\tcd_sit = '%s'", tb950Inv->getField(tb950Inv->tbinv_cd_sit));
		}
	}
	// Dump INVENTARI COLLOCATI
	printf ("\nInventari collocati");
	printf ("\n-------------------");
	if (!tb950InvCollocatiVect->Length())
		printf ("\n\tNessuno");
	else
	{
		for (int i=0; i < tb950InvCollocatiVect->Length(); i++)
		{
			tb950Inv = tb950InvCollocatiVect->Entry(i);
			printf ("\n\tcd_bib = '%s'", tb950Inv->getField(tb950Inv->tbinv_cd_bib));
			printf ("\tbid = '%s'", tb950Inv->getField(tb950Inv->bid));
			printf ("\ttbinv_key_loc = '%s'", tb950Inv->getField(tb950Inv->tbinv_key_loc));
			printf ("\tcd_inven = '%s'", tb950Inv->getField(tb950Inv->tbinv_cd_inven));
			printf ("\tcd_sit = '%s'", tb950Inv->getField(tb950Inv->tbinv_cd_sit));
		}
	}

	// Dump COLLOCAZIONI CON O SENZA INVENTARIO
	printf ("\nCollocazioni (con o senza inventario)");
	printf ("\n-------------------------------------");


	if (!collocazione950AnyVectKV->Length())
		printf ("\n\tNessuno");
	else
	{
		for (int i=0; i < collocazione950AnyVectKV->Length(); i++)
		{
			collocazione950 = (Collocazione950 *)collocazione950AnyVectKV->GetValue(i);
			tb950Coll = collocazione950->getTb950Coll();

			printf ("\n\tbid = '%s'", tb950Coll->getField(tb950Coll->bid));
			printf ("\tbid_doc (legame esemplare) = '%s'", tb950Coll->getField(tb950Coll->tbcol_bid_doc));
			printf ("\tcd_doc = '%s'", tb950Coll->getField(tb950Coll->tbcol_cd_doc));
			printf ("\tkey_loc = '%s'", tb950Coll->getField(tb950Coll->tbcol_key_loc));
			printf ("\tcd_sez = '%s'", tb950Coll->getField(tb950Coll->tbcol_cd_sez));
			printf ("\tcd_loc = '%s'", tb950Coll->getField(tb950Coll->tbcol_cd_loc));
			printf ("\tspec_loc = '%s'", tb950Coll->getField(tb950Coll->tbcol_spec_loc));
			printf ("\tconsis = '%s'", tb950Coll->getField(tb950Coll->tbcol_consis));

		}
	}

	// Dump ESEMPLARI
	printf ("\nEsemplari");
	printf ("\n---------");
	if (!esemplare950VectKV->Length())
		printf ("\n\tNessuno");
	else
	{
		for (int i=0; i < esemplare950VectKV->Length(); i++)
		{
			CKeyValue *kv =  esemplare950VectKV->Get(i);
			printf ("\n\tchiave = '%s'", kv->Key.p);
			printf ("\n\tcons_doc = '%s'", kv->Val.p);

		}
	}
} // End dump





bool Biblio950::isInvCollocato(char *keyLoc)
{
	Tb950Inv * tb950Inv;
	for (int i=0; i < tb950InvCollocatiVect->Length(); i++)
	{
		tb950Inv = tb950InvCollocatiVect->Entry(i);
		if (!strcmp(keyLoc, tb950Inv->getField(tb950Inv->tbinv_cd_bib)))
			return true;
	}
	return false;
} // End  isInvCollocato


void Biblio950::addEsemplare(char *key, char *value) // bid, consistenza di esemplare
{
	esemplare950VectKV->Add(key, value);
}

/*
 * Abbiamo un esemplare gia' instanziato con questa chiave?
 */
/*
Esemplari950 *Biblio950::findEsemplare950(CString &esemplare)
{
	Esemplari950 *esemplare950 = (Esemplari950 *)esemplare950VectKV->GetValueFromKey(esemplare.data());
	return esemplare950;
}
*/
char *Biblio950::findEsemplare950(CString &esemplare)
{
	char *esemplare950 = (char *)esemplare950VectKV->GetValueFromKey(esemplare.data());
	return esemplare950;
}
