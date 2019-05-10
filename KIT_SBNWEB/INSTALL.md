# Installazione dell'applicativo SBNWEB in ambiente Linux

### INDICE

1.[ Premessa](#1-premessa)

2.[ Installazione e configurazione dell'applicativo SBNWEB](#2-Installazione-e-configurazione-dellapplicativo-SBNWEB)
 - 2.1 [Installazione dei moduli EAR dell'applicativo SBNWEB](#21-Installazione-dei-moduli-EAR-dellapplicativo-SBNWEB)
 - 2.2 [Installazione dei moduli per export UNIMARC](#22-Installazione-dei-moduli-per-export-UNIMARC)
    - 2.2.1 [Struttura delle cartelle di lavoro](#221-Struttura-delle-cartelle-di-lavoro)
    - 2.2.2 [Installazione e configurazione del modulo](#222-Installazione-e-configurazione-del-modulo)
- 2.3 [Creazione del Data Base](#23-Creazione-del-Data-Base)
- 2.4 [Configurazione dell'applicativo](#24-Configurazione-dellapplicativo)
    - 241 [Personalizzazione della configurazione di JBOSS](#241-Personalizzazione-della-configurazione-di-JBOSS)
    - 2.4.2 [Configurazione delle categorie di esecuzione delle elaborazioni differite](#242-Configurazione-delle-categorie-di-esecuzione-delle-elaborazioni-differite)
- 2.5 [Installazione dei moduli per import Utenti Lettori](#25-Installazione-dei-moduli-per-import-Utenti-Lettori)
    - 2.5.1 [Struttura delle cartelle di lavoro](#251-Struttura-delle-cartelle-di-lavoro)
    - 2.5.2 [Installazione e configurazione del modulo](#252-Installazione-e-configurazione-del-modulo)

3.[ Definizione e impostazione delle politiche di backup](#3-Definizione-e-impostazione-delle-politiche-di-backup)
- 3.1 [Storicizzazione](#31-Storicizzazione)
- 3.2 [Oggetti del backup](#32-Oggetti-del-backup)
     - 3.2.1 [Il backup della base dati](#321-Il-backup-della-base-dati)
     - 3.2.2 [Il backup dell'ambiente](#322-Il-backup-dellambiente)
     - 3.2.3 [Il backup su nastro](#323-Il-backup-su-nastro)
- 3.3 [Schedulazione](#33-Schedulazione)

4.[ Conclusione attività](#4-Conclusione-attività)
- 4.1 [Le shell di backup](#41-Le-shell-di-backup)
- 4.2 [La shell per l'aggiornamento del software applicativo](#42-La-shell-per-laggiornamento-del-software-applicativo)
- 4.3 [Gli script di startup](#43-Gli-script-di-startup)
- 4.4 [Ambiente grafico](#44-Ambiente-grafico)
- 4.5 [Pulizia dei log di JBOSS](#45-Pulizia-dei-log-di-JBOSS)
- 4.6 [Autenticazione alla console di JBOSS](#46-Autenticazione-alla-console-di-JBOSS)

5.[ Allegato A](#5-ALLEGATO-A)

6.[ Allegato B](#6-ALLEGATO-B)

## 1. PREMESSA

Obiettivo del presente documento è descrivere le operazioni che occorre eseguire per l'installazione dell'applicazione SBNWEB e la sua configurazione.

La descrizione non comprende le attività relative alla configurazione del sistema operativo e dell'ambiente di base del server (definizione dei file-system, spazio disco ecc.), per le quali si rimanda all'apposito documento "Installazione dell'applicativo SBNWEB in ambiente Linux".

Si tenga altresì conto che tutti i riferimenti a file system e/o direttori in cui posizionare le componenti che costituiscono l'applicativo assumo che siano state rispettate le indicazioni fornite nel documento "Installazione dell'applicativo SBNWEB in ambiente Linux".

In attesa della realizzazione di un sistema di distribuzione delle componenti che costituiscono l'applicativo SBNWEB, i diversi moduli che lo compongono verranno resi disponibili sotto forma di directory e file compressi all'interno della directory **install\_sbnweb** (fornito in formato _tar.gz_), la cui organizzazione è riportata nell'allegato A "Contenuti del file install\_sbnweb".

Si consiglia di decomprimere il file _install\_sbnweb.tar gz_ ed estrarne la directory **install\_sbnweb** in una propria directory di lavoro, da cui successivamente prelevare le varie componenti dell'applicativo.

## 2. Installazione e configurazione dell'applicativo SBNWEB

L'applicativo è costituito da tre moduli: due distribuiti in formato Enterprise Archive file (ear) che implementano l'interfaccia web e la logica elaborativa dell'applicativo SBNWEB, un terzo  per la produzione degli export UNIMARC costituito di più file di vario tipo.

### 2.1. Installazione dei moduli EAR dell'applicativo SBNWEB

I due moduli che realizzano l'interfaccia web e la gestione della BD, distribuiti come Enterprise Archive file (ear), sono:

- **sbn.ear**         che contiene tutti i moduli web e la logica elaborativa dell'applicativo SBNWEB.
- **sbnMarc.ear**         che implementa il protocollo SBNMARC per la gestione del catalogo bibliografico di Polo.

I due EAR vanno copiati nella cartella di deploy di jboss.

Assumendo che jboss sia stato installato al path _/usr/local/jboss-4.2.3.GA,_ la cartella dove copiare i file .ear è:

_/usr/local/jboss-4.2.3.GA/**server/default/deploy**_.

### 2.2. Installazione dei moduli per export UNIMARC

Nel caso in cui si scelga di utilizzare un'architettura che utilizza server separati per l'application e il data server, l'installazione del modulo esportazione UNIMARC va fatta sul server dove sarà in esecuzione sbn.ear.

#### 2.2.1. Struttura delle cartelle di lavoro

La tabella sottostante riporta l'organizzazione delle cartelle di lavoro necessaria al corretto funzionamento delle componenti che costituiscono il modulo di esportazione del catalogo in formato UNIMARC.

L'esportazione può essere attivata, in modalità differita, da client web, o anche direttamente da connessione ssh al server; in questa seconda ipotesi l'output del processo non sarà visibile da client web, ma va recuperato dalla 'cartella di deposito dei file unimarc' (vedi Tabella 1).

| **Cartella** | **Descrizione** |
| --- | --- |
| /export/exportUnimarc/dp/export | Cartella dove risiedono gli script per la predisposizione:</br>- dei file sequenziali copia del DB </br>- della lista documenti da estrarre nel caso di esecuzione da terminale|
| /export/exportUnimarc/db_export | Cartella dove si deposita la copia su files sequenziali delle tabelle del DB |
| /export/exportUnimarc/dp | Cartella dove risiedono gli script che eseguono:</br> - la creazione degli indici dei file sequenziali </br>- la creazione del file unimarc |
| /export/exportUnimarc/dp/input | Cartella dove vengono generati gli indici |
| /export/exportUnimarc/dp/input | Cartella dove vengono generati gli indici |
| /export/exportUnimarc/dp/unimarc | Cartella dove viene generato il file animarci di output |
| /export/exportUnimarc/dp/it/finsiel/misc | Cartella dove risiedono le classi Java per funzioni di utilità |
| /export/exportUnimarc/dp/it/finsiel/offlineExport | Cartella dove risiedono le classi Java che realizzano la logica di estrazione, indicizzazione e conversione |

Tabella 1 – struttura delle cartelle per modulo esportazione UNIMARC

#### 2.2.2. Installazione e configurazione del modulo

Copiare nella cartella **/export** il file exportUnimarc.template._codpolo_.zip contenuto all'interno del file install\_sbnweb.

Eseguire l'unzip del file: l'operazione creerà automaticamente la struttura (directory) **exportUnimarc.template** con all'interno tutto l'ambiente già configurato per l'export Unimarc del polo.

Quindi procedere con i seguenti passi

- eseguire i comandi 
  
       mv exportUnimarc.template  exportUnimarc
       chown  -R  export:jboss  export Unimarc
- creare la directory _/export/exportUnimarc/dp/export/_**trasf** e assegnarle i proprietari export:export (con l'opzione –R) e i permessi 777
- rimuovere il file .zip.
- nel caso si disponga anche del server OPAC, nella _shell/export/exportUnimarc/dp/export/_**TRASF.sh** , sostituire alla stringa _xxx.xxx.xxx.xxx_ l'indirizzo IP del server OPAC, assicurandosi che abbia i permessi di esecuzione (755)

Si noti che il file _.zip_, riferendosi ad un ambiente elaborativo strettamente legato al polo, viene preliminarmente configurato in fase di assemblaggio del kit di installazione, assegnando alle seguenti variabili i valori di riferimento del polo in questione:

| **Variabile** | **Valore di template** | **Adeguamento** |
| --- | --- | --- |
| polo | PPP | Sostituire PPP con la sigla del codice polo |
| descPolo | "Polo PPP" | Sostituire con una descrizione del Polo mantenendo i doppi apici |
| bibliotecaRichiedenteScarico | IT-000000 | Sostituire la parte numerica con il codice anagrafe della biblioteca di riferimento del Polo |

Ad altre variabili, di seguito riportate, vengono assegnati i valori di default, a meno che non siano stati precedentemente comunicati, dai responsabili del polo, valori alternativi (e comunque modificabili successivamente nel file offlineExportUnimarc.linux.cfg sotto _/export/exportUnimarc/dp/_):

- Digita l'indirizzo IP del server che ospita il DB [_Default è localhost_]
- Digita la porta dove il DB sta in ascolto [_Default è 5432_]
- Digita il nome del database (sbnwebDbPPPprv) [_Default è sbnwebDb_] 
- Si vuole un record unimarc su una riga singola? (S/N) [_Default è N_]:
- Vuoi l'etichetta 314? (Si/No) [_Default è Si_]
- Vuoi l'etichetta 326? (Si/No) [_Default è Si_]
- Vuoi l'etichetta 440? (Si/No) [_Default è Si_]
- Vuoi l'etichetta 801 o 850? (801/850) [_Default è 801_]
- Vuoi l'etichetta 95x o 96x? (95x/96x) [_Default è 95x_]

Come si vede, viene fornito come nome di database, quello utilizzato per la migrazione di prova; in caso il polo non utilizzasse la suddetta migrazione, il file _.zip_ sarà creato con il nome del database di esercizio: sbnwebDb\<codpolo\>ese.

In ogni caso, in **/export/exportUnimarc/dp/export** modificare all'occorrenza i files **download&lt;polo&gt;.txt**, **downloadFileAccessori&lt;polo&gt;.txt** e **getBidList.txt** adeguando opportunamente i valori assegnati alle variabili preposte all'accesso al DataBase:

- **connectionUrl**=jdbc:postgresql:_//localhost:5432/sbnwebDbXXXese|prv_

usando il suffisso _ese_ o _prv_ a seconda del database che si intende usare

- **userName**=_sbnweb_
- **userPassword**=_sbnweb_

### 2.3. Creazione del Data Base

Come utente postgres eseguire la seguente procedura:

1. Creare le cartelle per i tablespace con i seguenti comandi

       mkdir /db/pgsql8.3.5/data/pg\_tblspc/ tbs\_indici\_ese
       mkdir /db/pgsql8.3.5/data/pg\_tblspc/ tbs\_dati\_ese

  dove **tbs\_indici\_ese** e **tbs\_dati\_ese** sono solo nomi suggeriti; se però si sceglie di cambiarli allora il nome adottato va sostituito negli sql di create del DB (file Create\_DB\_XXXese.sql) e in quello di creazione degli indici (sbnwebDbXXX\_Completo.sql).

2. Creare il **database**: copiare dal file install\_sbnweb (sottodirectory DB\_creaDB), il file _Create\_DB\_XXXese.sql_ in una cartella di lavoro e adeguare i nomi dei tablespace e del database, quindi lanciare il seguente comando

        psql -L Create\_DB\_XXXese.log -f Create\_DB\_XXXese.sql

e verificare nel file _Create\_DB\_XXXese.log_ che i tablespace e il database siano stati correttamente istanziati.

**N.B.: nel caso sia prevista la migrazione di prova, i passi 1 e 2 vanno eseguiti di nuovo sostituendo nei nomi dei tablespace e in quello del database (anche all'interno degli eseguibili sql) il suffisso _ese_ con il suffisso _prv_**.

3. Generare le strutture dati: sempre dalla sottodirectory DB\_creaDB di install\_sbnweb, copiare nella cartella di lavoro il file _sbnwebDbXXX\_Completo.sql_ e, se è stato modificato, adeguare il nome del tablespace predisposto per gli indici, quindi eseguire il seguente comando:

        psql -d \<nomedb> -L sbnwebDbXXX\_Completo.log -f sbnwebDbXXX_Completo.sql

In alternativa, copiare nella cartella di lavoro il file che crea la struttura del database senza indici _sbnwebDbXXXeseSenzaIndici.sql_ ed eseguire il seguente comando:

    psql -d \<nomedb> -L sbnwebDbXXXeseSenzaIndici.log -f sbnwebDbXXXeseSenzaIndici.sql

4. **Caricare nel DB i dati di inizializzazione**: l'attività è finalizzata al caricamento nel DB dei valori codificati, e dei codici di funzione che controllano il comportamento dell'applicativo, nonché alla creazione dell'utente '_root'_; le occorrenze da inserire si distinguono in due categorie: a valori predefiniti, e a valori da personalizzare per ciascun polo.

- Per le occorrenze che non necessitano di personalizzazione copiare nella cartella di lavoro i file di seguito elencati:

    - 0\_sbnwebDBinsert\_ts\_stop\_list.sql
    - 01\_sbnwebDBinsert\_tbf\_attivita\_sbnmarc-tbf\_attivita.sql
    - 02\_sbnwebDBinsert\_tb\_codici.sql
    - 03\_sbnwebDBinsert\_X\_Batch\_e\_Default.sql
    -  04\_sbnwebDBinsert\_tbf\_parametro.sql

E per ciascuno di essi, avendo cura di elaborare i file nell'ordine della numerazione riportata nel _nomefile_, eseguire il comando:

    psql -d \<nomedb> -L nomefile.log –f nomefile.sql

- Per le occorrenze che necessitano di personalizzazione copiare nella cartella di lavoro i file di seguito elencati:

    - 05\_Definisci\_Polo.sql**
    - 06\_sbnwebDBinsert\_AbilitaPolo\_UtenzaRoot.sql

Adeguare opportunamente il file _05\_Definisci\_Polo.sql_nel quale sono indicati i dati di identificazione del polo e le credenziali per la connessione al server INDICE; a tale scopo sostituire i valori:

    'PPP'  con Codice del Polo (3 chr)
    'XXXAMMnnnnnn' con username fornita da Indice (12 chr)
    'pwdindice' con password fornita da Indice  (varchr)
    'mailgestorepolo' con indirizzo e-mail del gestore (varchr)

quindi per ciascuno file della lista, avendo cura di elaborare i file nell'ordine della numerazione riportata nel loro nome, eseguire il comando:

    psql -d <nomedb> -L nomefile.log –f nomefile.sql

Si precisa che il template viene fornito con il puntamento all'Indice di collaudo

    "url_indice"='http://193.206.221.21/indice/servlet/serversbnmarc'

Per quanto riguarda campo "url\_polo", nel template è valorizzato ipotizzando che l'esecuzione di ambedue i moduli .ear sia controllata dalla stessa istanza di application serve e che questo sia in ascolto sulla porta 8080, se le scelte del polo fossero diverse il valore di tale campo va opportunamente adeguato all'effettivo url dell'application server che esegue il modulo sbnMarc.ear.

    "url_polo"='http://localhost:8080/SbnMarcWeb/SbnMarcTest'

Si precisa inoltre che per rendere operative variazioni apportate al record inserito nella tabella tbf\_polo è richiesto il restart dell'applicativo sbn.ear.

### 2.4. Configurazione dell'applicativo

Di seguito sono riportate le istruzioni per verificare/adeguare i parametri che realizzano la configurazione dell'applicativo SBNWEB.

Il file in cui tali parametri sono definiti è **sbnweb.conf** (in install\_sbnweb/jboss-configuration) **.**

Prima di tutto editare il file, sostituendo nelle ultime due istruzioni il codice polo (in maiuscolo) alle _XXX_ presenti nei nomi dei file di integrazione.

Per il corretto funzionamento dell'applicativo, **sbnweb.conf** va posizionato in una cartella accessibile all'utente jboss (si consiglia il posizionamento nella cartella puntata dalla variabile $HOME di jboss, _/home/jboss_), quindi il path scelto va referenziato nel file di configurazione di jboss **run.conf** (per ulteriori informazioni vedi par. 2.4.1)



Parametri predefiniti nel file **sbnweb.conf** :

| **Nome Parametro** | **Default** | **Note** |
| --- | --- | --- |
| **DATA\_SOURCE** | java:jdbc/sbnHibernatePostgresqlDS | Nome JNDI del data source usato per le query in SQL (non Hibernate). Deve essere lo stesso al quale punta il file SbnWebPostgres-ds.xml. |
| **QUEUE\_NAME** | queue/sbnWebBlocchi | Nome della coda JMS usata per memorizzare le sintetiche paginate. La coda è configurata nel file **sbnWeb-queue-service.xml** (vedi paragrafo 2.4.1). |
| **MESSAGE\_TTL**   | 21600000 | Durata massima (in millisecondi) del mantenimento di un blocco non richiesto dall'utente. |
| **SBNWEB\_BATCH\_FILES\_PATH** | /home/jboss/sbn/download | Path assoluto in cui verranno salvati gli output e i log delle elaborazioni differite. |
| **SBNWEB\_EXPORT\_UNIMARC\_HOME** | /export/exportUnimarc/dp  | Path assoluto alla cartella di installazione della procedura di Export Unimarc offline. |
| **EXPORT\_UNIMARC\_FILE\_ACCESSORI** |   | Elenco di file (separati da punto e virgola) che, se trovati nella cartella unimarc del path **SBNWEB\_EXPORT\_UNIMARC\_HOME** , verranno inseriti nel file .zip output della funzione export Unimarc |
| **CLONER\_CLASS** | it.iccu.sbn.util.cloning.impl.JDKActualCloner  | Classe standard che implementa il sistema di clonazione degli oggetti di scambio usati dall'applicativo. |
| **CLONER\_POOL\_SIZE** | 5 | Numero di istanze concorrenti delle classi indicate dal parametro CLONER\_CLASS. |
| **JBAC\_BUFFER\_SIZE** | 65536 | Dimensione (in bytes) del buffer per la serializzazione degli oggetti clonati. |
| **BATCH\_CLEANING\_AGE\_THRESHOLD** | 30 | Numero di giorni dopo il quale il sistema elimina i riferimenti ad una elaborazione differita. Se posto uguale a 0 il sistema non cancella mai alcun riferimento. |
| **BATCH\_CLEANING\_DELETE\_OUTPUTS** | true | Indica se il sistema cancella automaticamente i file di output salvati nella cartella indicata dal parametro SBNWEB\_BATCH\_FILES\_PATH. |
| **BATCH\_USER\_DELETE\_OUTPUTS** | true | Indica se una richiesta puntuale di cancellazione elimina anche i file di output salvati nella cartella indicata dal parametro SBNWEB\_BATCH\_FILES\_PATH. |
| **LOG\_LEVEL\_SBNWEB** **LOG\_LEVEL\_SBNMARC** **LOG\_LEVEL\_HIBERNATE** | DEBUG | Livello di dettaglio dei log prodotti dall'applicativo. |
| **SBNWEB\_HTTP\_SESSION\_TIMEOUT** **SERVIZI\_HTTP\_SESSION\_TIMEOUT** | 305 | Timeout (in minuti) della sessione HTTP dell'applicativo gestionale e del modulo Servizi. |
| **SIP2\_BIND\_PORT** | 7892 | Porta di ascolto del servizio di auto-prestito SIP2. |
| **SIP2\_CURR\_POLE** | Non impostato | Codice Polo assegnato al totem SIP2. |
| **SIP2\_CURR\_BIBLIO** | Non impostato | Codice Biblioteca assegnato al totem SIP2. |
| **MAX\_RESULT\_ROWS** | 4000 | Limite al numero di righe restituite dalla componente SBNMARC di Polo. |
| **SMS\_PROVIDER\_CLASS** | it.iccu.sbn.util.sms.impl.DummySMSProvider | Classe che implementa il servizio personalizzato per l'invio di messaggi SMS. |
| **SBNMARC\_INDICE\_USE\_PROXY** | False | Attiva la gestione della connessione via proxy HTTP al sistema Indice SBNMARC. |
| **SBNMARC\_INDICE\_PROXY\_URL** **SBNMARC\_INDICE\_PROXY\_PORT** **SBNMARC\_INDICE\_PROXY\_USERNAME** **SBNMARC\_INDICE\_PROXY\_PASSWORD** | Non impostati | Parametri per la connessione al proxy HTTP server. |
| **SBNMARC\_INDICE\_ALLINEA\_REQUEST\_TIMEOUT** | 3600000 | Durata massima (in millisecondi) dell'attesa per una risposta dall'Indice SBNMARC durante un'elaborazione di Allineamento. |
| **RFID\_ENABLE** | false | Se impostato a true abilita la visualizzazione del campo RFID per la lettura automatica della chiave inventario. Per dettagli sul formato ammesso per i dati immessi nel campo si veda l'ALLEGATO B |
| **CSV\_FIELD\_SEPARATOR** | virgola (\u002C) | Separatore di default per i file prodotto nel formato csv. |
| **SBNWEB\_EXPORT\_IGNORE\_FILE** | Non impostato | Percorso del file opzionale contenente la lista di BID da ignorare durante l'esportazione Unimarc. |
| **ESAME\_COLLOCAZIONI\_MAX\_RESULT\_ROWS** | 1000 | Numero massimo di righe da precaricare durante le interrogazioni della linea "Esame Collocazioni". |
| **WS\_MAX\_CONCURRENT\_CLIENTS** | 10 | Numero massimo di client ammessi sul modulo web-service. |
| **SBNWEB\_IMPORTA\_UTENTI\_HOME** | /home/jboss/sbn/lettori | Path assoluto alla cartella di installazione della procedura di importazione utenti lettori. |
| **LOC\_MASSIVA\_INDICE\_WAIT\_TIMEOUT** | 30000 | Relativo al processo differito 'Localizzazione massiva' (IA006).Durata (in millisecondi) dell'intervallo tra una richiesta di localizzazione in Indice e la successiva. |
| **SBNMARC\_SCHEMA\_VERSION** | 1.16 | Versione dello schema SBNMarc supportata dall'applicativo. |
| **INDICE\_CONNECTION\_CHECKER\_TIMEOUT** | 300000 | Durata (in millisecondi) dell'intervallo tra una richiesta di verifica della disponibilità dell'Indice e la successiva (solo nel caso in cui il flag di connessione non disponibile sia attivo). |
| **SBNMARC\_ALLINEAMENTO\_SU\_FILE\_INTERVAL** | 3600000 | Intervallo (in millisecondi) impostato per l'inserimento automatico di una nuova richiesta di allineamento a fronte di una risposta di Indice per allineamento da file. Se assente il sistema assume il valore di default (attivazione dopo un'ora dalla risposta dell'Indice) se presente, ma valorizzato a zero, la prenotazione automatica non viene effettuata. |
| **SBNWEB\_BATCH\_ALLINEAMENTO\_DA\_LOCALE** |   | Path assoluto alla cartella dove saranno salvati i file XML prodotti dal processo differito "Salvataggio file XML Indice per allineamento" (IA007). |
| **SRV\_PRENOTAZIONE\_POSTO\_UTENTI\_MULTIPLI** | false | Se impostato a true permette di aggiungere più utenti a una singola prenotazione posto (a sistema il posto occupato sarà il medesimo). |
| **ILL\_SBN\_SERVER\_URL** |   | URL che specifica il punto di ascolto del Server SBN ILL Nazionale. |
| **ILL\_SBN\_DOC\_DELIVERY\_URL** |   | URL che specifica il punto s'ascolto per il servizio di document-delivery del Server SBN ILL. |
| **OPAC\_Z3950\_SEARCH\_ENABLE**</br> **OPAC\_Z3950\_URL**</br> **OPAC\_Z3950\_DB** | false</br>opac.sbn.it:3950</br>nopac | Parametri di configurazione per l'accesso all'OPAC di Indice SBN o altro OPAC che colloquia secondo il protocollo Z39.50. |
| **SRV\_EVENTO\_ACCESSO\_MERGE\_THRESHOLD** | 60000 | intervallo minimo (in millisecondi) tra due eventi di accesso per lo stesso utente. Gli eventi che ricadono nell'intervallo saranno fusi sul primo evento registrato. |

#### 2.4.1. Personalizzazione della configurazione di JBOSS

- Configurazione del profilo di jboss

Nel file **.bash\_profile** nella directory /home/jboss, aggiungere le seguenti righe:

    PATH=/usr/local/jdk/bin:$PATH:$HOME/bin  (prima della riga "export PATH")
    export DISPLAY=localhost:1

- Path della configurazione di sbnweb

Nel file _/usr/local/jboss-4.2.3.GA/bin/_**run.conf** aggiungere l'opzione sbnweb.conf.path per la JVM:

    JAVA_OPTS="$JAVA_OPTS -Dsbnweb.conf.path={path}"
 se l'installazione avviene in modalità 64 bit

    JAVA_OPTS="$JAVA_OPTS –d64 -Dsbnweb.conf.path={path}"

dove {path} indica il percorso dove è situato il file **sbnweb.conf** (/home/jboss).

Qualora il server non disponga di un terminale video, aggiungere in fondo alla precedente istruzione, prima del doppio apice di chiusura, il parametro

    -Djava.awt.headless=true

Quindi settare la variabile **JAVA\_HOME** come segue:

    JAVA\_HOME=/usr/local/jdk

Infine, nella sezione "Specify option to pass to the Java VM", sostituire l'istruzione presente all'interno del controllo "if" con la seguente (tutta su un'unica riga):

    JAVA_OPTS="-Xms512m -Xmx1280m -XX:MaxPermSize=512m -Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000"

- Attribuzione del localhost

Nel file /usr/local/jboss-4.2.3.GA/bin/ **run.sh** aggiungere l'opzione _-b 0.0.0.0_

nelle due sezioni "_Execute the JVM in the foreground|background_", come mostrato di seguito:
  
  per la sezione _foreground_
              
       org.jboss.Main -b 0.0.0.0 "$@" 
per la sezione _background_
   
       org.jboss.Main -b 0.0.0.0 "$@" &



- Durata massima delle transazioni per elaborazioni differite

Nel file _/usr/local/jboss-4.2.3.GA/server/default/conf/_ **jboss-service.xml** portare il timeout della singola transazione a 86400 secondi (24 ore) modificando opportunamente il valore dell'attributo "TransactionTimeout":

    <!-- JBoss Transactions JTA -->
    <mbean code="com.arjuna.ats.jbossatx.jta.TransactionManagerService"
    name="jboss:service=TransactionManager">

      <attribute name="T_ransactionTimeout">86400 <attribute>
      <attribute name="ObjectStoreDir_">${jboss.server.data.dir}/tx-object-store <attribute>
    </mbean>

- Trattamento URL con codifica UTF-8

L'adeguamento va operato per consentire il corretto trattamento di eventuali caratteri speciali presenti nelle attivazioni via URL (es. attivazione del modulo servizi da OPAC).

Modificare connettore HTTP del sottosistema Tomcat usato da JBOSS.
Nel file _/usr/local/jboss-4.2.3.GA/server/default/deploy/jboss-web.deployer/_ **server.xml** aggiungere parametro URIEncoding al connettore HTTP (tag _\<Connector>_):

    <Connector port="8080" address="${jboss.bind.address}"
      maxThreads="250" maxHttpHeaderSize="8192"
      emptySessionPath="true" protocol="HTTP/1.1"
      enableLookups="false" redirectPort="8443" acceptCount="100"
      connectionTimeout="20000" disableUploadTimeout="true" URIEncoding="UTF-8">

- Configurazione dell'accesso al DataBase

L'ultimo passo riguarda i due file che contengono le informazioni per il corretto puntamento al DB da parte delle due componenti applicative. I file template sono forniti nel kit di installazione ( _install\_sbnweb/jboss-configuration/server/default/deploy_ ) con i nomi:

    SbnMarcPostgres-ds.xml _(data source per il modulo sbnmarc.ear)_
    SbnWebPostgres-ds.xml _(data source per il modulo sbn.ear)_

I due files vanno posizionati nella cartella

**/usr/local/jboss-4.2.3.GA/server/default/deploy**

Vanno quindi personalizzati riportando in \<connection-url>  l'URL del DataBase di polo.

    <connection-url>jdbc:postgresql://localhost:5432/nomedb</connection-url>

Eventualmente va adeguata anche le password delle credenziali di accesso al DB.

    <password>sbnweb</password>

Nell'esempio riportato l'url del DB è valorizzato ipotizzando che application server e data server rispondano allo stesso ip address e che l'RDBMS sia in ascolto sulla porta 5432, se le scelte adottate del polo fossero diverse il valore di tale campo va opportunamente adeguato sostituendo localhost con l'ip address del data server.

- Configurare le code JMS per l'utilizzo con l'applicativo SBNWEB:
Nella cartella _/usr/local/jboss-4.2.3.GA/server/default/deploy/jms:_

- sostituire **hsqldb-jdbc2-service.xml** (serializzazione code in memoria) con il file **postgres-jms-sbnweb-service.xml** (serializzazione code su DB) presente nel pacchetto di installazione (install\_sbnweb/jboss-configuration/server/default/deploy/jms);
- copiare il file di configurazione **sbnWeb-queue-service.xml**(configurazione personalizzata delle code). Il file contiene la configurazione minima per il corretto funzionamento dell'applicativo SBNWEB.

A seguire un elenco delle code definite dall'installazione standard:

| **Nome Coda** | **Valore** | **Note** |
| --- | --- | --- |
| **sbnMarcBlocchi** | NON MODIFICARE  | Coda che memorizza i blocchi che compongono le sintetiche prodotte dal protocollo SbnMarc di Polo. |
| **sbnWebBlocchi** | NON MODIFICARE  | Coda che memorizza i blocchi che compongono le sintetiche prodotte dall'applicativo SBNWEB. |
| **sbnBatch\_Input** | NON MODIFICARE  | Coda usata per inserire a sistema una nuova prenotazione di elaborazione differita. |
| **sbnBatch\_Output1** **sbnBatch\_Output2** |   | Code definite per contenere i riferimenti agli output delle elaborazioni differite.Definire più code di output può essere utile per rendere più veloce l'interrogazione delle stesse da applicativo, nel caso si sia scelto di non effettuare la cancellazione periodica degli output delle elaborazioni differite (parametro BATCH\_CLEANING\_AGE\_THRESHOLD = 0). |

#### 2.4.2. Configurazione delle categorie di esecuzione delle elaborazioni differite

L'installazione preliminare di un DB SBNWEB prevede una configurazione funzionante delle code destinate alle elaborazioni differite, raggruppate in un'unica categoria d'esecuzione sincrona di tipo IMMEDIATE (attivazioni di tipo seriale, 24 ore su 24).

Quanto segue permette una suddivisione del carico elaborativo su più categorie di esecuzione, rendendo possibile la discriminazione dei processi per evitare il sovraccarico del server e permettere l'elaborazione di più richieste (di tipologie diverse) contemporaneamente.

NB: Un processo differito, identificato dal suo codice attività, può appartenere a una sola categoria d'esecuzione. È possibile definire un numero illimitato di categorie, ma quelle che non hanno nessuna procedura assegnata non appariranno nella maschera di ricerca dell'applicativo SBNWEB.

Per rendere visibili all'applicativo SBNWEB le variazioni apportate alla configurazione delle code è richiesto il **riavvio** del server JBOSS.

- Definizione di una nuova categoria di esecuzione

La tabella **tbf\_coda\_jms** contiene la definizione delle singole categorie. È possibile definire una nuova categoria semplicemente inserendo una nuova riga nella tabella. A seguire una descrizione dei campi che compongono la tabella in oggetto:

| **Nome Campo** | **Default** | **Note** |
| --- | --- | --- |
| **id\_coda** | Progressivo calcolato dal sistema  | ID univoco della categoria di esecuzione |
| **nome\_jms** | queue/sbnBatch\_Input | Nome della coda JMS usata per inserire a sistema una nuova prenotazione di elaborazione differita. **Attenzione** : La coda JMS usata come default è attualmente l'unica supportata a questo scopo dall'applicativo SBNWEB. |
| **sincrona**   | S | Indica che solo un processo di elaborazione differita, tra quelli assegnati a questa categoria, potrà essere elaborato dal sistema in un dato istante (stato EXEC). |
| **id\_descrizione** |   | Nome assegnato alla categoria e che apparirà nelle maschere dell'applicativo SBNWEB. |
| **id\_descr\_orario\_attivazione** |   | NON UTILIZZATO |
| **Id\_orario\_di\_attivazione** |   | NON UTILIZZATO |
| **cron\_expression** | 0/30 \* \* \* \* ? | Espressione in sintassi unix cron che permette la personalizzazione degli orari di attivazione e disattivazione della categoria d'esecuzione. Utilizza lo scheduler open-source Quartz.Esempio: 0 0/5 14 10-15 \* 2010 Controlla la presenza di una prenotazione al primo secondo ('0'), ogni 5 minuti ('0/5'), solo dalle 14 alle 15 ('14'), dal 10 al 15 del mese ('10-15'), ogni mese ('\*') del 2010 ('2010')Per ulteriori informazioni consultare il sito del produttore: http://www.quartz-scheduler.org/docs/tutorials/crontrigger.html |

NB: L'espressione contenuta nel campo _cron\_expression_ indica la modalità con cui l'applicativo interrogherà la coda di input per verificare sia la presenza di una prenotazione in attesa che di un processo in esecuzione (nel caso si sia configurata la categoria come sincrona). Nel caso si sia scelto di attivare numerose categorie è opportuno configurare le rispettive espressioni cron per evitare troppi accessi concorrenti alle code JMS che potrebbero rallentare il server.

Esempio di configurazione:

| Tipo | Expr | Descrizione |
| --- | --- | --- |
| categoria 1 | 0/30 \* \* \* \* ? | la coda viene interrogata ogni 30 secondi, al secondo 0 e al secondo 30. |
| categoria 2 | 15/45 \* \* \* \* ? | la coda viene interrogata ogni 30 secondi, al secondo 15 e al secondo 45. |
| categoria 3 | 25/55 \* \* \* \* ? | la coda viene interrogata ogni 30 secondi, al secondo 25 e al secondo 55. |
| categoria 4 | 0 0/5 \* \* \* ? | La coda viene interrogata ogni 5 minuti, al secondo 0. |

- Assegnazione di un processo differito alla categoria d'esecuzione

I processi disponibili come procedure differite sono descritti e configurati nella tabella **tbf\_batch\_servizi**.

NB: nella sua installazione base la tabella contiene già tutti i processi previsti dall'applicativo SBNWEB. Eliminare un record dalla tabella renderà il processo cancellato non più utilizzabile.

A seguire una descrizione dei campi che compongono la tabella in oggetto:

| **Nome Campo** | **Default** | **Note** |
| --- | --- | --- |
| **id\_batch\_servizi** | Progressivo calcolato dal sistema  | ID univoco del processo di elaborazione differita |
| **cd\_attivita** | NON MODIFICABILE | Codice Attività univoco che identifica a livello di applicativo il processo di elaborazione differita. Questo valore è riportato nei profili di abilitazione dei bibliotecari e ne influisce la prenotabilità. |
| **id\_coda\_input** | Riferimento al campo **id\_coda** della tabella **tbf\_coda\_jms** | Categoria d'esecuzione a cui è assegnato questo processo di elaborazione differita. |
| **nome\_coda\_output** | Definito nel filesbnWeb-queue-service.xml | Nome della coda JMS dove saranno salvati i dettagli riguardanti le prenotazioni e l'esecuzione del processo di elaborazione differita. La coda deve essere definita a livello di server e configurata nel file **sbnWeb-queue-service.xml**. |
| **class\_name** | NON MODIFICABILE | Percorso completo della classe Java che realizza il servizio indicato da questo processo di elaborazione differita. |
| **visibilita** | P | Parametro che indica se, in fase di interrogazione da applicativo SBNWEB, i dettagli riguardanti questo processo di elaborazione differita saranno visibili a tutti gli utenti del polo (visibilita=P) oppure solo a quelli della biblioteca che effettua la prenotazione del processo (visibilita=B) |
| **fl\_canc** | NON MODIFICABILE | Flag di cancellazione logica. |

### 2.5. Installazione dei moduli per import Utenti Lettori

Il paragrafo contiene la descrizione delle operazioni necessarie per installare la procedura di importazione o aggiornamento dei dati relativi agli utenti lettori e alle corrispondenti autorizzazioni in SBNWEB.

#### 2.5.1. Struttura delle cartelle di lavoro

La procedura viene attivata, in modalità differita, da client web. Può anche essere attivata direttamente da connessione SSH al server. In questa seconda ipotesi, è necessaria l'esecuzione dello script " **importaUtenti.sh**" a cui devono essere forniti i seguenti parametri:

  - Il file di configurazione " **ImportaUtentiLettori.cfg**" da personalizzare per ciascun polo con il codice del polo stesso e i dati relativi al database a cui accedere.
  - Il nome del file di input.
  - La data di inizio validità delle autorizzazioni da assegnare nel formato dd/mm/yyyy.

#### 2.5.2. Installazione e configurazione del modulo

Copiare nella cartella **/home/jboss/sbn** il file lettori.zip contenuto all'interno del file install\_sbnweb.

Eseguire l'unzip del file: l'operazione creerà automaticamente la struttura (directory) **lettori** con all'interno tutto l'ambiente da configurare con i parametri del polo.

Quindi procedere con i seguenti passi:

- eseguire il comando 
    
        chown -R jboss:jboss  lettori
- rimuovere il file .zip.
- portarsi nella cartella **lettori** e assegnare i permessi di esecuzione alla shell **importaUtenti.sh** eseguendo il comando:

        chmod 755 importaUtenti.sh

- modificare il file di configurazione **ImportaUtentiLettori.cfg,** facendo riferimento alla seguente tabella:

| **Variabile** | **Valore di template** | **Adeguamento** |
| --- | --- | --- |
| codPolo | PPP | Sostituire PPP con la sigla del codice polo. |
| connectionUrl | jdbc:postgresql://localhost:5432/sbnwebDbPPPese | Sostituire con l'indirizzo e il nome del Db associato all'applicativo SBNWEB. |
| username | sbnweb | Nome utente per l'accesso al Db. |
| userPassword | sbnweb | Password per l'accesso al Db. |

Agli altri parametri vengono assegnati valori di default e non vanno modificati.

## 3. Definizione e impostazione delle politiche di backup

Le politiche di backup del server sono state definite in relazione ai tre aspetti più importanti da tenere in considerazione: l'oggetto del backup, per quanto tempo mantenere le informazioni (ossia la sua storicizzazione), la schedulazione.

In generale i backup vengono effettuati su disco; in coda all'intero processo, gli output prodotti sono successivamente trasferiti su nastro.

Vediamo in dettaglio i tre aspetti sopra citati.

### 3.1. Storicizzazione

Partiamo da questo aspetto della questione dal momento che le scelte effettuate si riflettono nella organizzazione delle operazioni di backup.

In generale, in mancanza di specifiche esigenze al riguardo, lo standard è quella di effettuare un backup giornaliero con ciclo settimanale; in altre parole si sceglie di mantenere le informazioni salvate per il tempo massimo di una settimana.

Solitamente i file di output prodotti dalle procedure riportano nel nome il giorno in cui vengono scritti e pertanto, al termine del ciclo, il file più vecchio viene sovrascritto. Nella maggioranza dei casi si può omettere il backup nel giorno di domenica (vedi più avanti il paragrafo 3.3 sulla schedulazione).

### 3.2. Oggetti del backup

Possiamo distinguere tali oggetti in due gruppi principali: la base dati e l'ambiente operativo (file-system e singole directory). Le shell si trovano in **/home/SCRIPTS** e l'output prodotto si trova in **/backup/DUMP\_DB** (base dati) e **/backup/DUMPSYS** (ambiente).

#### 3.2.1. Il backup della base dati

La shell preposta al backup della base dati è la **pg\_dump.sh** che sfrutta una utility di _Postgres,_ chiamata appunto _pgdump_, per effettuare un backup logico del database indicato nella relativa istruzione. L'output dell'operazione viene posto in _/backup/DUMP\_DB_ con la seguente nomenclatura:

    <giorno>.<nomedb>.gz

La procedura produce un file di log sotto la directory /backup/logs

    bckdb-<giorno>.log

che riporta l'esito del backup.

#### 3.2.2 Il backup dell'ambiente

La shell preposta al backup dell'ambiente è la **backup.sh**. Essa, tramite il comando _tar_, effettua il salvataggio delle seguenti strutture:
- file-system /usr/local
- file-system /home
- file-system /etc
- file-sytsem /export

L'output prodotto, un file _tar_ per ciascuna struttura, è posto in _/backup/DUMPSYS_ con la seguente nomenclatura:

    <nomedir>.<giorno>.tar.gz

mentre i log dell'operazione si trovano sotto _/backup/logs_ con analoga nomenclatura:

        <nomedir>.<giorno>.log

#### 3.2.3. Il backup su nastro

Come detto in precedenza, i backup sopra descritti vengono effettuati su disco. Per ulteriore sicurezza è stato previsto anche un salvataggio ridondante su nastro.

La shell preposta a questa operazione è la **tar.sh** che, tramite il comando _tar_, effettua il salvataggio su nastro (tipicamente delle cartucce DAT) delle seguenti strutture:
- file-system /usr/local
- file-system /home
- file-system /backup
- directory /db/pgsql8.3.5

L'output dell'operazione si trova sotto /backup/logs con nomenclatura:

    tar.<giorno>.out

mentre il log che riporta l'esito dell'operazione si chiama:

    tar.<giorno>.log

Ovviamente per mantenere la storicizzazione di una settimana anche per i backup su nastro, sarà necessario disporre di un numero adeguato di nastri, opportunamente etichettati con la data del giorno a cui si riferisce il contenuto, che poi dovranno giornalmente essere sostituiti.

### 3.3. Schedulazione

La schedulazione dell'esecuzione dei backup viene effettuata attraverso la _crontab_ di sistema, nella quale vengono stabiliti i giorni e l'ora di esecuzione di ogni singola shell. Generalmente l'esecuzione delle tre shell sopra descritte viene programmata dal lunedì al sabato, con orari sfalsati per non produrre accavallamenti. Uno schema di massima potrebbe essere il seguente:

    30 21 * * 1-6 /home/SCRIPTS/pg\_dump.sh  /dev/null 2&1
    20 22 * * 1-6 /home/SCRIPTS/backup.sh /dev/null 2&1
    55 23 * * 1-6 /home/SCRIPTS/tar.sh /dev/null 2&1

L'esempio è puramente indicativo, in quanto gli orari vanno stabiliti anche tenendo conto di fattori quali le dimensioni della base dati, la presenza di altri batch ecc.

Nel caso non si volesse eseguire il backup su nastro, la relativa istruzione va commentata (carattere #).

## 4. Conclusione attività

La predisposizione dell'ambiente operativo si conclude con la messa in linea delle shell di backup e l'attivazione degli script di startup automatico dei prodotti.

### 4.1 Le shell di backup

Le operazioni de effettuare sono nell'ordine:

- copiare dalla directory _script\_ambiente_ del kit di installazione le shell _pg\_dump.sh, backup.sh, tar.sh_ nella directory **/home/SCRIPTS** ;
- assicurarsi che i proprietari delle shell siano _root:root_ e i permessi settati su 755;
- configurare adeguatamente la crontab di sitema (cioè di root).

### 4.2. La shell per l'aggiornamento del software applicativo

Questa shell provvede ad effettuare in maniera automatica l'aggiornamento dei moduli _ear_ nella apposita directory di jboss. La shell, inserita in crontab, verifica se ci sono moduli aggiornati nella directory /Trasf/dep: in caso affermativp copia i moduli nella directory di sestinazione e si incarica di chiudere e riavviare jboss, altrimenti termina senza fare nulla.

- copiare dalla directory _script\_ambiente_ del kit di installazione gli script _upd\_sw.sh_ e _updjb\_sw.sh_ nella directory **/export/Trasf** ;
- assicurarsi che i proprietari delle shell siano rispettivamente _root:jboss_ e _jboss:_jboss e i permessi di entrambe settati su 755;
- nella crontab di root aggiungere la seguente riga (togliendo il carattere di commento quando lo si ritiene opportuno):

      0 7 * * 1-5 /export/Trasf/upd_sw.sh /dev/null 2>&1

### 4.3. Gli script di startup

- copiare dalla directory _script\_ambiente_ del kit di installazione gli script di startup _jboss_ e _postgresql_ nella directory **/etc/init.d** ;
- assicurarsi che i proprietari delle shell siano _root:root_ e i permessi settati su 755;
- nella directory **/etc/rc5.d** creare i seguenti link

        ln –s  ../init.d/postgresql  S64postgresql
        ln –s  ../init.d/jboss  S94jboss

N.B.: I numeri riportati sono puramente indicativi anche se in generale possono considerarsi attendibili; è comunque preferibile che, prima di creare il link, ci si assicuri che nella directory in questione i numeri scelti non siano già stati assegnati ad altri script. Si consideri infine che i due prodotti devono partire preferibilmente verso la fine del processo di boot.

- nella directory **/etc/rc3.d** creare gli stessi link sopra riportati.
- infine eseguire il comando   

        chkconfig ––add postgresql

### 4.4. Ambiente grafico

Nel file /etc/rc.local, come utente root, aggiungere in fondo le seguenti righe:

    #----------------------------------------------------#
    #Permette alle applicazioni java di generare immagini
    #----------------------------------------------------#

    /usr/bin/Xvfb :1 -screen 0 800x600x8 &;

### 4.5. Pulizia dei log di JBOSS

Nella crontab di root aggiungere in testa la seguente istruzione:

    0 1 * * * find /usr/local/jboss-4.2.3.GA/server/default/log -name "*log*" -mtime +4 -exec rm -f {} \;

In tal modo vengono cancellati i file di log di jboss piu vecchi di due giorni.

### 4.6. Autenticazione alla console di JBOSS

Questo paragrafo riguarda l'attivazione della procedura di autenticazione per l'accesso alla console di Jboss.

Occorre modificare alcuni file di configurazione, per i quali si consiglia di farsi una copia prima di effettuare le modifiche.

- Nella directory _/usr/local/jboss-4.2.3.GA/server/default/deploy/jmx-console.war/WEB-INF_ modificare il file **web.xml** nella sezione \<A security constraint that restricts access to…> come di seguito riportato:

        <!-- A security constraint that restricts access to the HTML JMX console
        to users with the role JBossAdmin. Edit the roles to what you want and
        uncomment the WEB-INF/jboss-web.xml/security-domain element to enable
        secured access to the HTML JMX console. -->
        <security-constraint>
        <web-resource-collection>
        <web-resource-name>HtmlAdaptor</web-resource-name>
        <description>An example security config that only allows users      with the
        role JBossAdmin to access the HTML JMX console web application
        </description>
        <url-pattern>/*</url-pattern>
        <http-method>GET</http-method>
        <http-method>POST</http-method>
        </web-resource-collection>
        <auth-constraint>
        <role-name>JBossAdmin</role-name>
        </auth-constraint>
        </security-constraint>
        <login-config>
        <auth-method>BASIC</auth-method>
        <realm-name>JBoss JMX Console</realm-name>
        </login-config>


Quindi **trovare e cancellare** nel file suddetto le due seguenti righe consecutive:

        <http-method>GET</http-method>
        <http-method>POST</http-method>


Sempre nella stessa directory, modificare il file **jboss-web.xml** come di seguito riportato:

    <jboss-web>
    <!-- Uncomment the security-domain to enable security. You will
    need to edit the htmladaptor login configuration to setup the
    login modules used to authentication users. -->
    <security-domain>java:/jaas/jmx-console</security-domain>
    </jboss-web>


In pratica le modifiche consistono nello spostare i caratteri di fine-commento (--) dalla loro posizione originaria nel file a quella mostrata nei due esempi sopra riportati.

- Le stesse modifiche sopra descritte vanno riportate anche negli stessi file **web.xml**   e   **jboss-web.xml** che si trovano sotto la directory:

    - _/usr/local/jboss-4.2.3.GA/server/default/deploy/management/console-mgr.sar/web-console.war/WEB-INF_

- Per quanto riguarda l'utente e la password di accesso alla console, si consiglia di lasciare l'utente di default (admin) e settare la password editando i seguenti file:

    - _/usr/local/jboss-4.2.3.GA/server/default/conf/props/_**jmx-console-users.properties**

    - _/usr/local/jboss-4.2.3.GA/server/default/deploy/management/console-mgr.sar/web-console.war/WEB-INF/classes/_**web-console-users.properties**

## 5. ALLEGATO A

**Install\_sbnweb:**
- DB\_creaDB
- DB\_iniDB
- DB\_creaDB
- exportUnimarc.template.\<data>-\<polo>.zip
- jboss-configuration
- moduliEAR
- script\_ambiente

**DB\_creaDB:**
- Create\_DB\_XXXese.sql
- sbnwebDbXXX\_Completo.sql
- sbnwebDbXXXSenzaIndici.sql

**DB\_iniDB:**
- 00\_sbnwebDBinsert\_ts\_stop\_list.sql
- 01\_sbnwebDBinsert\_tbf\_attivita\_sbnmarc-tbf\_attivita.sql
- 02\_sbnwebDBinsert\_tb\_codici.sql
- 03\_sbnwebDBinsert\_X\_Batch\_e\_Default.sql
- 04\_sbnwebDBinsert\_tbf\_parametro.sql
- 05\_Definisci\_Polo.sql
- 06\_sbnwebDBinsert\_AbilitaPolo\_UtenzaRoot.sql

**DB\_completaDB**
- 07\_aggiorna\_codana\_biblioteca\_XXX.sql
- 08\_pm1\_ABILITA\_BIBLIOTECHE\_DEL\_POLO.sql
- 09a\_elimina\_duplicazioni\_userid\_bibliotecari\_XXX.sql
- 09b\_pm2\_ABILITA\_BIBLIOTECARI\_DELLE\_BIBLIOTECHE.sql
- 10\_Crea\_Localizzazioni.sql
- 11\_aggiorna\_utenti.sql
- 12\_INI\_sequence.sql
- 13\_ConfrontaCodiciDiValidazione.sql
- 14\_catene\_rinnovi.sql
- 15\_lista\_bibliotecari\_abilitati.sql
- 16\_Aggiusta\_default.sql
- 17\_Verifica\_INI\_sequence.sql

**exportUnimarc.template.\<data>-\<polo>.zip:**
- exportUnimarc/db\_export
- exportUnimarc/dp

**exportUnimarc/db\_export:**

 **exportUnimarc/dp:**
- export
- input
- it
- makeIndici.sh
- makeUnimarc.sh
- offlineExportUnimarc
- offlineExportUnimarc.linux.cfg
- unimarc

**exportUnimarc/dp/export:**
- download\<codpolo>.txt
- downloadFileAccessori\<codpolo>.txt
- exportFileAccessori.sh
- export.sh
- getBidList.txt
- getBidList.sh
- postgresql-8.2-509.jdbc2.jar

**exportUnimarc/dp/input:**

**exportUnimarc/dp/it:**

- finsiel

**exportUnimarc/dp/it/finsiel:**
- misc
- offlineExport

**exportUnimarc/dp/it/finsiel/misc:**
- DateUtil.class
- Misc.class
- MiscString.class
- MiscStringTokenizer.class

**exportUnimarc/dp/it/finsiel/offlineExport:**
- CreateOffsetFile.class
- CreateRelationFile.class
- DbDownload.class
- RearrangeFields.class
- Select950.class

**exportUnimarc/dp/unimarc:**

**jboss-configuration:**
- bin
- sbnweb.conf
- server

**jboss-configuration/bin:**

- run.conf

**jboss-configuration/server:**

- default

**jboss-configuration/server/default:**

- conf

- deploy

**jboss-configuration/server/default/conf:**

- jboss-service.xml

**jboss-configuration/server/default/deploy:**

- jboss-web.deployer

- jms

- SbnMarcPostgres-ds.xml

- SbnWebPostgres-ds.xml

**jboss-configuration/server/default/deploy/jboss-web.deployer:**

- server.xml

**jboss-configuration/server/default/deploy/jms:**
- postgres-jms-sbnweb-service.xml
- sbnWeb-queue-service.xml

**moduliEAR:**
- sbn.ear
- sbnMarc.ear

**script\_ambiente:**
- backup.sh
- jboss
- pg\_dump.sh
- postgresql
- tar.sh
- upd\_sw.sh
- updjb\_sw.sh

## 6. ALLEGATO B

**Formato per la lettura automatica delle chiavi inventario (campo RFID)**

Attualmente l'applicativo SBNWeb gestisce i seguenti formati per la valorizzazione del campo RFID:

| **Lunghezza del campo** | **Scomposizione del campo** |
| --| --|
| \<4 caratteri | ERRORE |
| da 4 a 12 caratteri | codice serie (3 chr) </br>Inventario (max 9 chr numerici) 
| compreso tra 5 e 13 | codice serie (3 chr)</br> separatore (1  chr  a spazio)</br>Inventario (max 9 chr numerici)
 14 caratteri  |codice biblioteca (2 chr)</br> codice serie (3 chr)</br> Inventario (9 chr numerici con gli zeri non significativi a sx (0 padded a sx))
| 15 caratteri  | codice biblioteca (3 chr) (spazio in testa)</br> codice serie (3 chr)</br> Inventario (9 chr numerici con gli zeri non significativi a sx (0 padded a sx))
| 18 caratteri |codice polo(3 chr)</br>codice biblioteca (3 chr (1 spazio + 2 chr significativi))</br>codice serie(3 chr)</br>Inventario (9 chr numerici con gli zeri non significativi a sx (0 padded a sx))
| 19 caratteri |codice serie (3 chr)</br>codice serie (3 chr)</br>Inventario (10 chr numerici con gli zeri non significativi a sx (0 padded a sx))</br>codice polo(3 chr)</br>codice biblioteca (3 chr (1 spazio + 2 chr significativi))
