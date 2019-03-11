#!/bin/bash

ora="date +%Y-%m-%d-%H:%M:%S"
echo `${ora}` "Inizio processo di export.sh" > export.proc    

#java -jar sqlworkbench.jar -script=export.sql -profile='localhost'

java -Xmx1560m -classpath ../:postgresql-8.2-509.jdbc2.jar it.finsiel.offlineExport.DbDownload downloadSBW.txt


echo `${ora}` "Fine processo di export.sh" >> export.proc
 
