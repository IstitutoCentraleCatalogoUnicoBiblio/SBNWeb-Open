#  La shell serve per preparare al momento il kit da distribuire per
#  una eventuale installazione da parte di un nuovo polo

clear
echo "Indicare il valore del Codice Polo (in maiuscolo): "
read qualepolo
if [ ! $qualepolo ]
 then
  echo " - ATTENZIONE: non risulta digitato il codice polo"
  echo " - Non si prosegue."
  exit
fi
#
RISP=SI
#
echo " "
echo " "
echo "Si vuole preparare il kit per un polo che prevede la manutenzione ? ( SI / [NO] ): "
echo " "
read conferma
if [ $conferma ]
then
  case $conferma in
    (NO|N|no|n) RISP=NO
                ;;
    (SI|S|si|s)
                ;;
                (*) echo " "
                    echo " Risposta non congruente - Non si prosegue."
                    echo " "
                    exit
                    ;;
  esac
fi

# Asscuriamoci che il polo sia uppercase
polo="`echo $qualepolo|tr '[:lower:]' '[:upper:]'`"

#  Copia nella directory install_sbnweb il file dell'ambiente exportUnimarc cancellando il precedente
rm -f /backup/KIT_SBNWEB/install_sbnweb/exportUnimarc.template.*.zip
if [ -s /export/exportUnimarc.template.*-$polo.zip ]
  then
   cp -p /export/exportUnimarc.template.*-$polo.zip /backup/KIT_SBNWEB/install_sbnweb
  else
   echo '  '
   echo 'ATTENZIONE!!    NON risulta generato il file exportUnimarc.template per il polo  ' $polo
   echo '  '
   echo '                                 NON  SI  PROSEGUE  '
   echo '  '
   exit
fi

#  Copia nella directory install_sbnweb l'ultima versione dei moduli ear
cp -p /export/Trasf/Ultimo/sbn.ear /backup/KIT_SBNWEB/install_sbnweb/moduliEAR
cp -p /export/Trasf/Ultimo/sbnMarc.ear /backup/KIT_SBNWEB/install_sbnweb/moduliEAR
chown jboss:jboss /backup/KIT_SBNWEB/install_sbnweb/moduliEAR/*

#  Esegue il tar della directory install_sbnweb e successivamente la compressione
if [ -s /backup/KIT_SBNWEB/install_sbnweb.tar.gz ]
  then
   rm -f /backup/KIT_SBNWEB/install_sbnweb.tar.gz
fi
#
cd /backup/KIT_SBNWEB
#
if [ $RISP = SI  ]
  then
#     echo " "
#     echo " Procedo per un polo con manutenzione - RISP= " ${RISP}
     tar cvf ./install_sbnweb.tar ./install_sbnweb --exclude script_ambiente.sm
  else
#     echo " "
#     echo " Procedo per un polo senza manutenzione - RISP= " ${RISP}
     tar cvf ./install_sbnweb.tar ./install_sbnweb --exclude script_ambiente
fi
#
gzip install_sbnweb.tar
cp -p install_sbnweb.tar.gz Storico/install_sbnweb.tar-$polo.gz
