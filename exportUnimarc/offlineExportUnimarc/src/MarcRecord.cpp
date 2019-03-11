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
 * MarcRecord.cpp
 *
 *  Created on: 3-dic-2008
 *      Author: Arge
 */


#include "MarcRecord.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif

extern void SignalAnError(const OrsChar *Module, OrsInt Line,		const OrsChar * MsgFmt, ...);
extern void SignalAWarning(const OrsChar *Module, OrsInt Line,		const OrsChar * MsgFmt, ...);

MarcRecord::MarcRecord() {
	init();
}

void MarcRecord::init() {
	controlFieldsVector = new ATTValVector<ControlField*>();
	dataFieldsVector = new ATTValVector<DataField*>();
	leader = new Leader();
}

MarcRecord::~MarcRecord() {
	stop();
}

void MarcRecord::stop() {
	if (controlFieldsVector)
	{
		controlFieldsVector->DeleteAndClear();
		delete controlFieldsVector;
	}
	if (dataFieldsVector)
	{
		dataFieldsVector->DeleteAndClear();
		delete dataFieldsVector;
	}
	if (leader)
		delete leader;
}

long MarcRecord::getId() const
{
    return id;
}

void MarcRecord::setId(long id)
{
    this->id = id;
}

Leader *MarcRecord::getLeader() const
{
    return leader;
}


ATTValVector<ControlField*> *MarcRecord::getControlFieldsVector() const
{
    return controlFieldsVector;
}

void MarcRecord::setControlFieldsVector(ATTValVector<ControlField*> *controlFieldsVector)
{
    this->controlFieldsVector = controlFieldsVector;
}

ATTValVector<DataField*> * MarcRecord::getDataFieldsVector() const
{
    return dataFieldsVector;
}

void MarcRecord::setDataFieldsVector(ATTValVector<DataField*> *dataFieldsVector)
{
    this->dataFieldsVector = dataFieldsVector;
}

char* MarcRecord::getType()
{
    return type.data();
}

void MarcRecord::setType(char* type)
{
    this->type = type;
}


void MarcRecord::addControlField(ControlField* cf)
{
	controlFieldsVector->Add(cf);
}
void MarcRecord::addDataField(DataField* df)
{
	dataFieldsVector->Add(df);
}



void MarcRecord::clear()
{
	id=-1;
	leader->clear();
	controlFieldsVector->DeleteAndClear();
	dataFieldsVector->DeleteAndClear();
	type.Clear();
	stringed.Clear();
}
/*
void MarcRecord::setLeader(Leader *leader)
{
	if (this->leader)
		delete this->leader;
    this->leader = leader;
}
*/



long MarcRecord::toStringLength()
{
	return stringed.Length();
}


char *MarcRecord::toXml()
{
	ControlField *cf;
	DataField *df;


	stringed = "\n<rec>";

	// Prepare the leader
	stringed.AppendString(leader->toCStringXml());

	// Cycle through the control fields
	for (int i=0; i < controlFieldsVector->Length(); i++)
	{
		cf = controlFieldsVector->Entry(i);
		stringed.AppendString(cf->toCStringXml(1));
	}

	// Cycle through the data fields
	for (int i=0; i < dataFieldsVector->Length(); i++)
	{
		df = dataFieldsVector->Entry(i);
		if (df)
			stringed.AppendString(df->toCStringXml(1));
	}

	stringed.AppendString((char *)"\n</rec>", 7);

	return stringed.Data();
}

long MarcRecord::toXmlLength()
{
	return stringed.Length();
}



/*
 * Ordina output per tag
 * Bubble sort
 */
void MarcRecord::sortDataFields() {
    int newLowest = 0;            // index of first comparison
    int newHighest = dataFieldsVector->length()-1;  // index of last comparison
    //int top; // , bottom
    DataField *dfI, *dfI_I;

    while (newLowest < newHighest) {
        int highest = newHighest;
        int lowest  = newLowest;
        newLowest = dataFieldsVector->length();    // start higher than any legal index
//printf ("\nlowest=%d, highest=%d, newLowest=%d", lowest, highest, newLowest);

        for (int i=lowest; i<highest; i++) {
//printf ("\n\ti=%d, newLowest=%d, newHighest=%d", i, newLowest, newHighest);
			dfI = dataFieldsVector->Entry(i);
			dfI_I = dataFieldsVector->Entry(i+1);

//printf ("\ndfI->getId()=%d : tag=%s, dfI_I->getId()=%d : tag=%s", dfI->getId(), dfI->getTagString()->data(),  dfI_I->getId(), dfI_I->getTagString()->data());

			if(dfI_I && dfI->getId() > dfI_I->getId()) {
//			if(dfI->getId() > dfI_I->getId()) {
				// Swap
				dataFieldsVector->SetEntry(i, dfI_I);
				dataFieldsVector->SetEntry(i+1, dfI);
				// reposiizona
			    if (i<newLowest) {
				   newLowest = i-1;
				   if (newLowest < 0) {
					   newLowest = 0;
				   }
			    } else if (i>newHighest) {
				   newHighest = i+1;
				}
			} // end if
		}// end for
    } // end while

/**
 14/10/2010 Ordinamento preso in carico in fase di caricamento Opac

    // Ordiniamo le 899 per codice anagrafico
    //---------------------------------------
    // Troviamo la prima ed ultima 899
    newLowest = -1;
    newHighest = -1;
    for (int i=dataFieldsVector->length()-1; i > -1; i--)
    {
		dfI = dataFieldsVector->Entry(i);
		if(!strcmp(dfI->getTag(), "899"))
		{
			if (newHighest == -1)
			{
				newHighest = i;
				newLowest = i;
				top=i+1;
			}
			else
				newLowest=i;
		}
		else
		{
			if(newHighest != -1)
				break;
		}
    }
    // Trovato primo ed ultimo 899. Procediamo all'ordinamento
	if(newHighest == -1)
		return; // nessuna 899

	bottom = newLowest;
    int highest;
    int lowest;
    while (newLowest < newHighest) {
        highest = newHighest;
        lowest  = newLowest;
        newLowest = top;    // start higher than any legal index
        for (int i=lowest; i<highest; i++) {
			dfI = dataFieldsVector->Entry(i);
			dfI_I = dataFieldsVector->Entry(i+1);
			if(strcmp(dfI->getSubfield('1')->getData(), dfI_I->getSubfield('1')->getData()) > 0)
			 {
				dataFieldsVector->SetEntry(i, dfI_I);
				dataFieldsVector->SetEntry(i+1, dfI);
				// reposiizona
			    if (i<newLowest) {
				   newLowest = i-1;
				   if (newLowest < bottom) {
					   newLowest = bottom;
				   }
			    } else if (i>newHighest) {
				   newHighest = i+1;
				}
			} // end if
		}// end for
    } // end while
**/


}//fine bubbleSort


// Return the number of datafields with a given ID. 0 if none
int MarcRecord::existsDataField(int anId) const
{
DataField *df;
int ctr=0;
	for (int i=0;i< dataFieldsVector->Length(); i++ )
	{
		df = dataFieldsVector->Entry(i);
		if (!df)
			continue;
		if (df->getId() == anId)
			ctr++;
	} // End for datafields
	return ctr;
}

// Remove a data fields if it exists
int MarcRecord::removeDataFields(int anId) const
{
DataField *df;
int ctr=0;
	for (int i=0;i< dataFieldsVector->Length(); i++ )
	{
		df = dataFieldsVector->Entry(i);
		if (!df)
			continue;
		if (df->getId() == anId)
		{
			dataFieldsVector->DeleteAndRemoveByEntry(i);
			ctr++;
			i--;
		}
	} // End for datafields
return ctr;
}

// Remove a data fields if it exists
int MarcRecord::removeDataField(DataField *dfp) const
{
DataField *df;
int i;
	for (i=0;i< dataFieldsVector->Length(); i++ )
	{
		df = dataFieldsVector->Entry(i);
		if (df == dfp)
		{
			dataFieldsVector->DeleteAndRemoveByEntry(i);
			return i;
		}
	} // End for datafields

return -1;
}



char *MarcRecord::toString()
{
	toCString();
	return stringed.Data();
}


CString *MarcRecord::toCString()
{
	ControlField *cf;
	DataField *df;
	CString *sPtr;
	char * recId;

	// Prepare the leader
	stringed.assign(leader->toCString());

	// Cycle through the control fields
	for (int i=0; i < controlFieldsVector->Length(); i++)
	{
		cf = controlFieldsVector->Entry(i);
		stringed.AppendString(cf->toCString());
		if (!i)
			recId = cf->getData()->data(); // get the ID (bid,vid etc.)
	}


	// Cycle through the data fields
	for (int i=0; i < dataFieldsVector->Length(); i++)
	{
		df = dataFieldsVector->Entry(i);
		if (df)
		{
			sPtr = df->toCString(recId);
			stringed.AppendString(sPtr);
		}
		else
			SignalAnError(__FILE__, __LINE__, "ERRORE: Datafield %d (partendo da 0) non instanziato (datafields=%d)", i, dataFieldsVector->Length());

	}

	return &stringed;
}

DataField* MarcRecord::getDataField(char * fieldId) const
{
	// Cycle through the data fields
	DataField* df;
	for (int i=0; i < dataFieldsVector->Length(); i++)
	{
		df = dataFieldsVector->Entry(i);
		if (df)
		{
			CString * tag = df->getTagString();

			if (!tag->Compare((const char *)fieldId))
				return df;
		}
	}
	return 0;
}

ControlField* MarcRecord::getControlField(char * fieldId) const
{
	// Cycle through the data fields
	ControlField* cf;
	for (int i=0; i < controlFieldsVector->Length(); i++)
	{
		cf = controlFieldsVector->Entry(i);
		if (cf)
		{
			char * tag = cf->getTag();

			if (!strcmp(tag, fieldId))
				return cf;
		}
	}
	return 0;
}


