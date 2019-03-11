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
 * MarcWriterExample.cpp
 *
 *  Created on: 11-dic-2008
 *      Author: Arge
 */

#include "WriteMarcExample.h"
#include "MarcStreamReader.h"
#include "MarcStreamWriter.h"


WriteMarcExample::WriteMarcExample() {

}

WriteMarcExample::~WriteMarcExample() {
}

void WriteMarcExample::write(char *filenameIn, char *filenameOut)
{
CFile *in = new CFile(filenameIn, AL_READ_FILE);
CFile *out = new CFile(filenameOut, AL_WRITE_FILE);

MarcStreamReader *marcStreamReader = new MarcStreamReader(in);
MarcStreamWriter *marcStreamWriter = new MarcStreamWriter(out);

MarcRecord *marcRecord;

while (marcStreamReader->hasNext()) {
	marcRecord = marcStreamReader->next();
	if (marcRecord)
	{
		printf("\n\n%s", marcRecord->toString());
		marcStreamWriter->write(marcRecord);
	}
}

delete in;
delete out;
}
