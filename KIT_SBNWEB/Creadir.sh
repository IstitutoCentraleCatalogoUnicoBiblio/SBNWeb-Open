# Script di creazione delle strutture dell'ambiente Sbnweb

chown postgres:root /db
chmod 777 /db
mkdir /db/pgsql8.3.5
  chmod 755 /db/pgsql8.3.5
mkdir /db/pgsql8.3.5/data
  chmod 700 /db/pgsql8.3.5/data
  chown -R postgres:postgres /db/pgsql8.3.5
mkdir /db/migrazione
  chmod 777 /db/migrazione
  chown jboss:jboss /db/migrazione

chmod 755 /export
chown export:jboss /export
#mkdir /export/exportUnimarc
#mkdir /export/exportUnimarc/db_export
#mkdir /export/exportUnimarc/dp
#  chmod -R 775 /export/exportUnimarc
#  chown -R export:jboss /export/exportUnimarc
mkdir /export/Trasf
mkdir /export/Trasf/dep
mkdir /export/Trasf/Storico
mkdir /export/Trasf/logs
  chmod -R 775 /export/Trasf
  chown -R export:export /export/Trasf

chmod 755 /backup
mkdir /backup/DUMP_DB
  chmod 755 /backup/DUMP_DB
  chown postgres /backup/DUMP_DB
mkdir /backup/DUMPSYS
  chmod 755 /backup/DUMPSYS
mkdir /backup/logs
  chmod 777 /backup/logs

mkdir /home/SCRIPTS
  chmod 755 /home/SCRIPTS
mkdir /home/SCRIPTS/installazione_ambienti
  chmod 755 /home/SCRIPTS/installazione_ambienti
mkdir /home/SCRIPTS/installazione_ambienti/lavoro
  chmod 777 /home/SCRIPTS/installazione_ambienti/lavoro
