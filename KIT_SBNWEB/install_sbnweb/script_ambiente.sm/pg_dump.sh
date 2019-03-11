#!/bin/bash
PSQL=/usr/local/pgsql8.3.5/bin/psql
PG_DUMP=/usr/local/pgsql8.3.5/bin/pg_dump
#PREFIX=`date +%d.%m.%Y`
PREFIX=`date "+%a"`
#PREFIX=`date "+%d"`
DIR="/backup/DUMP_DB"
DATE=`date "+%d"`
day=`date "+%a"`
BZIP2=/usr/bin/bzip2

Databases=`$PSQL -U postgres -t -c "select datname from pg_database" | grep -vE '^-|^List|^Name|template[0|1]'`

data="date +%d/%m/%Y"
GIORNO=`date +%A`
JOBLOG=/backup/logs/bckdb-${GIORNO}.log
cat /dev/null > ${JOBLOG}
chmod 666 ${JOBLOG}
echo "#------------------------------------------------------#" >> ${JOBLOG}
echo "INIZIO ATTIVITA' NOTTURNA DEL GIORNO: "`${data}`          >> ${JOBLOG}
echo "#------------------------------------------------------#" >> ${JOBLOG}
echo "                                                          " >> ${JOBLOG}

renice 20 $$

echo "Backup started ..."  >> ${JOBLOG}

for db in `echo $Databases`
do
        TIME=`date  +%H:%M:%S`
        echo "  time: $TIME - Backup of $db in progress ..."  >> ${JOBLOG}
        su - postgres -c "${PG_DUMP} $db -U postgres| ${BZIP2} -9c > ${DIR}/$db.${day}.bzdump"
        RetC_bck=$?
          if [ $RetC_bck -eq 0 ]
          then
            TIME=`date  +%H:%M:%S`
            echo "  time: $TIME - Backup of $db finished ..."  >> ${JOBLOG}
            echo "   - Esito del backup - OK" >> ${JOBLOG}
            echo "                                                          " >> ${JOBLOG}
          else
            echo "   - Esito del backup - FALLITO! " >> ${JOBLOG}
            echo "                                                          " >> ${JOBLOG}
            fi
         md5sum ${DIR}/$db.${day}.bzdump > ${DIR}/$db.${day}.bzdump.md5
done
echo "Single db dump finished"  >> ${JOBLOG}

echo "                                                          " >> ${JOBLOG}
echo "#------------------------------------------------------#" >> ${JOBLOG}
echo "FINE ATTIVITA' NOTTURNA DEL GIORNO: "`${data}`            >> ${JOBLOG}
echo "#------------------------------------------------------#" >> ${JOBLOG}

#echo "Removing +8 days old backups!";
#find $BACKUP_DIR -mtime +8 -exec rm {} \;
