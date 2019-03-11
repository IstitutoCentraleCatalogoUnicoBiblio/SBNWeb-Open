# SBNWeb Open

Applicativo LMS integrato, sviluppato da ICCU, realizzato in architettura J2EE e che utilizza esclusivamente software free ed open-source (SO Linux, Application Server JBoss, RDBMS PostgreSQL).
L’accesso è web-based, l'utente può accedere alle funzioni applicative per mezzo di una intranet o attraverso la Rete Internet, utilizzando come terminale normali web browser (es. Firefox, Chrome, Opera, Edge).
SBNWeb gestisce un unico livello di base dati, relativo ad un raggruppamento di Biblioteche (Polo), nella quale i dati privati sono localizzati dalla Biblioteca e/o all’utente proprietari e la loro visibilità è controllata applicativamente, consentendo a ciascuno di accedere soltanto ai propri dati e a quelli condivisi.

## Licenza

[![License: AGPL v3](https://img.shields.io/badge/License-AGPL%20v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)

## Requisiti di installazione e compilazione di progetto

|Requisito| Versione | Descrizione|
|---|---|---|
|[Java](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)| >= 1.5|Development Kit|
|[Eclipse IDE](https://www.eclipse.org/ide/)| >= 4.5 (Mars) | Ambiente di sviluppo|
|[PostgreSQL](https://www.postgresql.org/)|>= 8.3.5| RDBMS di Polo

## Predisposizione del server

Per preparare la macchina all'installazione di SBNWeb seguire le [istruzioni](docs/PredisposizioneAmbiente.md).

## Installazione

Per installare SBNWeb fare riferimento al documento di [installazione](KIT_SBNWEB/INSTALL.md).
