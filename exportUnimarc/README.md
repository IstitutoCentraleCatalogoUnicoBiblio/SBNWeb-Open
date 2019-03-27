# Generatore di UNIMARC in modalità offline per SBNWeb

## Licenza

[![License: AGPL v3](https://img.shields.io/badge/License-AGPL%20v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)

## Descrizione

Il file di configurazione *offlineExportUnimarc.linux.cfg* può essere rinominato ed è usato come 
primo parametro a seguire il nome del file eseguibile, eg.

    ./offlineExportUnimarc offlineExportUnimarc.linux.cfg

## Dipendenze

### Compilazione

|Requisito|Versione |Descrizione|
|---|---|---|
|GCC| >= 4.1.1|Compilatore GNU C++|
|make| | Utility di compilazione|

Per compilare:

- portarsi nella cartella **Debug**
- eseguire make

Verrà prodotto il file eseguibile **offlineExportUnimarc**.

### Esecuzione

|Requisito| Versione | Descrizione|
|---|---|---|
|libstdc++6| >= 3.4|Libreria Standard C++|
