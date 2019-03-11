#!/bin/sh
GIO=`date +%a`
(cd /usr/; /bin/tar cvfz /backup/DUMPSYS/local-${GIO}.tar.gz local) >/backup/logs/local-${GIO}.log 2>&1
#(/bin/tar cvfz /backup/DUMPSYS/opt-${GIO}.tar.gz /opt) >/backup/logs/opt-${GIO}.log 2>&1
(/bin/tar cvfz /backup/DUMPSYS/home-${GIO}.tar.gz /home) >/backup/logs/home-${GIO}.log 2>&1
(/bin/tar cvfz /backup/DUMPSYS/etc-${GIO}.tar.gz /etc) >/backup/logs/etc-${GIO}.log 2>&1
(/bin/tar cvfz /backup/DUMPSYS/export-${GIO}.tar.gz /export --exclude /export/Trasf/Storico) >/backup/logs/export-${GIO}.log 2>&1
#(cd /db; /bin/tar cvfz /backup/DUMPSYS/migrazione-${GIO}.tar.gz migrazione) >/backup/logs/migrazione-${GIO}.log 2>&1
