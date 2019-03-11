#!/bin/bash
#*----------------------------------------------------------------*
#      PROCEDURA DI AGGIORNAMENTO SOFTWARE PER L'APPLICATIVO SBNWEB
# +-------------------------------------------------------------+
# | 1. 
# | 2. 
# | 3. 
# | 4. 
# +-------------------------------------------------------------+
#
DIRLAV=/export/Trasf/dep
FILE=*.ear
export FILE
FILG=.ear
export FILG
c_data=`date +"%Y%m%d%H%M"`
data=`date +"%d-%m-%Y"`
ora=`date +"%H:%M"`
JOBLOG=/export/Trasf/logs/updsw_${c_data}.log
export JOBLOG
#
echo "                                " >> ${JOBLOG}
echo "*----------------------------------------------------------------*"   >> ${JOBLOG}
echo "*  Aggiornamento software del  " ${data} " alle ore  " ${ora}"   *"   >> ${JOBLOG}
echo "*----------------------------------------------------------------*"   >> ${JOBLOG}
#*----------------------------------------------------------------*
#      Test di esistenza dei file .ear nell'apposita directory di lavoro
#*----------------------------------------------------------------*
echo "                                " >> ${JOBLOG}
cd ${DIRLAV}
if [ "$(ls -A $DIRLAV)" ]
  then
     echo "--- File .ear presenti ---                                 " >> ${JOBLOG}
     echo "                                                           " >> ${JOBLOG}
     echo "    Controllo la presenza o meno di batch in corso...      " >> ${JOBLOG}
     echo "                                                           " >> ${JOBLOG}
     if [ -s /export/Trasf/flag.txt ]
        then
         echo "     ATTENZIONE!!   CI SONO BATCH IN CORSO" >> ${JOBLOG}
         echo "                                                           " >> ${JOBLOG}
         echo "              NON  SI  PROSEGUE           " >> ${JOBLOG}
         echo "                                                           " >> ${JOBLOG}
         echo "        AGGIORNAMENTO  NON  EFFETTUATO           "           >> ${JOBLOG}
         echo "                                                           " >> ${JOBLOG}
         exit
        else
         echo "     Non ci sono batch in corso, si prosegue con l'aggiornamento " >> ${JOBLOG}
     fi
     echo "                                                           " >> ${JOBLOG}
     echo "--- Copia dei file .ear nella directory di jboss ---"        >> ${JOBLOG}
     echo "                                " >> ${JOBLOG}
     #
     #-------------------------------------------------------------------------------------------------------------------------
     #  copia la directory dep nello storico rinominandola con la data 
     cp -pr /export/Trasf/dep  /export/Trasf/Storico/dep_${c_data}
     #
     #-------------------------------------------------------------------------------------------------------------------------
     #  copia i file eseguibili nell'apposita directory di jboss 
     #chown jboss:jboss ${FILE}
     cp -p ${FILE} /usr/local/jboss-4.2.3.GA/server/default/deploy 
     #
     #-------------------------------------------------------------------------------------------------------------------------
     rm -f ${DIRLAV}/${FILE}
     #-------------------------------------------------------------------------------------------------------------------------
     echo "                                " >> ${JOBLOG}
     echo "*----------------------------------------------------------------*"   >> ${JOBLOG}
     echo "*  ===>  Chiusura e riavvio di JBOSS                             *"   >> ${JOBLOG}
     echo "*----------------------------------------------------------------*"   >> ${JOBLOG}
     echo "                                " >> ${JOBLOG}
     for i in `ps -efa|grep "jboss"|grep -v grep|grep -v "root"|awk '{print $2}' `
      do
       kill -30 $i
       sleep 5
      done
     sh /etc/init.d/jboss start
     sleep 5
     # 
     COUNT_PROC=`ps -efa|grep "jboss"|grep -v grep|grep -v "root"|wc -l `
     #echo 'Numero processi = ' $COUNT
     if [ $COUNT_PROC -eq 0 ]
     then
       echo "           "  >> ${JOBLOG}
       echo ${ora}      "                --> ATTENZIONE!! JBOSS non attivo"  >> ${JOBLOG}
       echo "           "  >> ${JOBLOG}
       echo ${ora}      "   FINE NON CORRETTA DELLA PROCEDURA DI AGGIORNAMENTO  " >> ${JOBLOG}
       exit
     else
       echo "           "  >> ${JOBLOG}
       echo ${ora}      "   JBOSS attivo  " >> ${JOBLOG}
       echo "           "  >> ${JOBLOG}
     fi
  else
     echo "--- File .ear non presenti ---"    >> ${JOBLOG}
     echo "                                " >> ${JOBLOG}
fi
#
echo "                                " >> ${JOBLOG}
echo "*----------------------------------------------------------------*"   >> ${JOBLOG}
echo "*  Fine aggiornamento software del  " ${data} "                  *"   >> ${JOBLOG}
echo "*----------------------------------------------------------------*"   >> ${JOBLOG}
