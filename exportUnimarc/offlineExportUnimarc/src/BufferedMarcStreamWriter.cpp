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
 * BufferedMarcStreamWriter.cpp
 *
 *  Created on: 27-set-2010
 *      Author: Arge
 */

#include "BufferedMarcStreamWriter.h"
#include "MarcGlobals.h"


extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);

BufferedMarcStreamWriter::BufferedMarcStreamWriter(long bufSize) {
	init(bufSize);
}
BufferedMarcStreamWriter::BufferedMarcStreamWriter(long bufSize, long resizeBy) {
	init(bufSize, resizeBy);
}


BufferedMarcStreamWriter::BufferedMarcStreamWriter(CFile *out, char* encoding, long bufSize) : MarcStreamWriter(out,encoding){
	init(bufSize);
    }

BufferedMarcStreamWriter::BufferedMarcStreamWriter(CFile * out, long bufSize)  : MarcStreamWriter(out){
	init(bufSize);
    }

BufferedMarcStreamWriter::BufferedMarcStreamWriter(CFile * out, long bufSize, long resizeBy)  : MarcStreamWriter(out){
	init(bufSize, resizeBy);
    }

void BufferedMarcStreamWriter::init(long bufSize)
{
	writesCounter = 0;
	dataWarehouse = new CBufferedData(bufSize);
}

void BufferedMarcStreamWriter::init(long bufSize, long resizeBy)
{
	writesCounter = 0;
	dataWarehouse = new CBufferedData(bufSize, resizeBy);
}








BufferedMarcStreamWriter::~BufferedMarcStreamWriter() {
	stop();
}

void BufferedMarcStreamWriter::stop()
{
//	printf ("\nByte scritti = %ld", byteScritti);

	// Scrivi la parte rimanente su file
	if (dataWarehouse->GetUsedBytes())
		writeBuffer();
	delete dataWarehouse;
}

/*
 * Writes the contents of the buffered data onto disc and resets pointers for next round
 */

bool BufferedMarcStreamWriter::writeBuffer()
{
//	printf ("\nScrivendo %ld buffered bytes\n", dataWarehouse->GetUsedBytes());

	bool retb = out->Write(dataWarehouse->Data(), dataWarehouse->GetUsedBytes());
	if (!retb)
		return retb;

	writesCounter++;

	// Riusiamo il buffer
	dataWarehouse->ResetNoFree();
	return true;
}




long BufferedMarcStreamWriter::getWritesCounter() {

	return writesCounter;
}


/**
 * Writes a <code>Record</code> object to the writer.
 *
 * @param record -
 *            the <code>Record</code> object
 */
bool BufferedMarcStreamWriter::write(MarcRecord* marcRecord) {
	bool retb;

//	printf ("\nBufferedMarcStreamWriter::write BufSize=%ld, ResizeBy=%ld, usedBytes=%ld, unusedRightBytes=%ld",dataWarehouse->GetBufSize(), dataWarehouse->GetResizeBy(), dataWarehouse->GetUsedBytes(), dataWarehouse->GetUnusedRightBytes());


    retb = prepareRecordTowrite(marcRecord);


    long recordLength = getWriteToFileRecordLength();
//	byteScritti += recordLength;

//	printf ("\nRecord len = %d", recordLength);
	if (recordLength > MAX_RECORD_SIZE) //  99999
	{
		printf ("\nRecord troppo lungo %d. Scartato!!", recordLength);
		return false;
	}



//    // Unbuffered
//	if (!retb)
//		return retb;
//	return writeToFile();

    if (recordLength > dataWarehouse->GetUnusedRightBytes())
    {
		if (!writeBuffer())
		{
			SignalAnError(__FILE__, __LINE__, "Errore nella scrittura del buffer");
			return false;
		}
//		byteScritti=recordLength;
    }
//	printf ("\nrecordLength = %ld", recordLength);
//	printf ("\nByte sum = %ld", byteScritti);

    // Scriviamo nella datawarehose
	if (!dataWarehouse->AddString(ldrStr->data()))
	{
		SignalAnError(__FILE__, __LINE__, "Errore nella scrittura di ldrStr");
		return false;
	}

	if (!dataWarehouse->AddBinaryData(directoryData->Data(), directoryData->GetUsedBytes())) //
	{
		SignalAnError(__FILE__, __LINE__, "Errore nella scrittura di directoryData");
		return false;
	}
	if (!dataWarehouse->AddBinaryData(recordData->Data(), recordData->GetUsedBytes())) //
	{
		SignalAnError(__FILE__, __LINE__, "Errore nella scrittura di recordData");
		return false;
	}

	if (!dataWarehouse->AddChar(RECORD_TERMINATOR))
	{
		SignalAnError(__FILE__, __LINE__, "Errore nella scrittura di RECORD_TERMINATOR");
		return false;
	}

	if (RECORDUNIMARCSUSINGOLARIGA == 1)
		if (!dataWarehouse->AddChar('\n'))
		{
			SignalAnError(__FILE__, __LINE__, "Errore nella scrittura di NEWLINE");
			return false;
		}

	return true;
	} // End write





