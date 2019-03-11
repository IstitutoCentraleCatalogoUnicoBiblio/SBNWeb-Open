#!/bin/sh
GIO=`date +%a`
data="date +%d/%m/%Y"
GIORNO=`date +%A`
JOBLOG=/tmp/logs/tar-${GIORNO}.log
cat /dev/null > ${JOBLOG}
chmod 666 ${JOBLOG}
echo "#------------------------------------------------------#" >> ${JOBLOG}
echo "INIZIO TAR SU NASTRO DEL GIORNO: "`${data}`          >> ${JOBLOG}
echo "#------------------------------------------------------#" >> ${JOBLOG}
echo "                                                          " >> ${JOBLOG}
#
/bin/tar cvf /dev/st0 /usr/local /db/pgsql8.3.5 /backup /home >/tmp/logs/tar-${GIO}.out 2>&1
#
RetC_tar=$?
  if [ $RetC_tar -eq 0 ]
  then
    echo " - Esito del tar - OK" >> ${JOBLOG}
    echo "                                                          " >> ${JOBLOG}
  else
    echo " - Esito del tar - FALLITO! " >> ${JOBLOG}
    echo "                                                          " >> ${JOBLOG}
    echo "   --->  Controllare l'output nel file tar-${GIO}.out     " >> ${JOBLOG}
  fi
#
echo "#------------------------------------------------------#" >> ${JOBLOG}
echo "FINE TAR SU NASTRO DEL GIORNO: "`${data}`            >> ${JOBLOG}
echo "#------------------------------------------------------#" >> ${JOBLOG}
