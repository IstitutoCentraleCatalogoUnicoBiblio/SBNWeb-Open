
# Predisposizione di un server LINUX all'installazione dell’applicativo SBNWeb

### INDICE
1. [Premessa](#1-Premessa)

2. [Sistema Linux. Ambiente e configuraione di base](#2-Sistema-Linux-Ambiente-e-configuraione-di-base)
 
   - 2.1 [Configurazione del Sistema Operativo](#21-Configurazione-del-Sistema-Operativo)

   - 2.2 [Preparazione ambiente di base. Nomenclatura e dimensionamento delle strutture di lavoro](#22-Preparazione-ambiente-di-base-Nomenclatura-e-dimensionamento-delle-strutture-di-lavoro)

      - 2.2.1 [Directory](#221-Directory)
      - 2.2.2 [Creazione degli utenti](#222-Creazione-degli-utenti)
      - 2.2.3. [Proprietari e permessi](#223-Proprietari-e-permessi)

3. [Installazione e configurazione del software di base](#3-Installazione-e-configurazione-del-software-di-base)
   - 3.1 [Application Server JBOSS](#31-Application-Server-JBOSS)
      - 3.1.1 [JBOSS](#311-JBOSS)
      - 3.1.2 [JDK (ambiente Java)](#312-JDK-ambiente-java)
   - 3.2 [Apache](#32-Apache)
   - 3.3 [RDBMS PostgreSQL](#33-RDBMS-PostgreSQL)
      - 3.3.1 [Installazione di PostgreSQL](#331-Installazione-di-PostgreSQL)
      - 3.3.2 [Definizione del database cluster](#332-Definizione-del-database-cluster)
      - 3.3.3 [Configurazione dei file per l’accesso al database server](#333-Configurazione-dei-file-per-laccesso-al-database-server)
      - 3.3.4 [Avvio del database server](#334-Avvio-del-database-server)

# 1. Premessa

Questo documento descrive in dettaglio tutte le operazioni che occorre eseguire per la configurazione
di base di un server Linux ai fini dell’installazione dell’applicazione SBNWeb.

La descrizione prevede quindi una prima parte relativa alla configurazione del sistema operativo e
dell’ambiente di base del server (definizione dei file-system, spazio disco ecc.), e una seconda parte
che riguarda più propriamente l’installazione del software di base; per l’installazione delle componenti
applicative e la loro configurazione si rimanda al documento specifico.

L’ambiente software necessario sulla macchina Linux per ospitare l’applicazione in oggetto è
costituito da:

- Sistema Operativo Linux RedHat 5.3 Enterprise Edition con LVM (in alternativa Scientific
    Linux SL 5.X)
- Compilatore GCC (solitamente all’interno del pacchetto di installazione del S.O.)
- Application server JBOSS versione 4.2.3 GA con JDK 1.5.X
- Server HTTP Apache Versione 2.
- RDBMS PostgreSQL Versione 8.3.

Il criterio adottato per la disposizione degli oggetti che costituiscono l’ambiente software é il
seguente: tutti i prodotti di ambiente (motore del database, http server ecc.) vanno installati nei
loro path di default (tipicamente _/usr/local_), mentre gli oggetti che realizzano l’applicativo e/o
ne supportano la gestione, vanno installati sotto specifiche directory la cui creazione è
demandata alla fase di predisposizione del server.

- l’Application server JBOSS, l’ambiente java e l’RDBMS PostgreSQL installati in **/usr/local**
- i dati e le relative strutture creati in **/db**
- l’ambiente operativo per l’export UNIMARC posizionato in **/export**
- l’ambiente di backup posizionato in **/backup**

## 2. Sistema Linux. Ambiente e configurazione di base 

L’applicativo SBNWEB prevede che il server sia equipaggiato con il **Sistema Operativo Linux
RedHat 5.X Enterprise Edition**. La licenza di registrazione, pur non essendo di per sé
necessaria, è però consigliabile in quanto permette di effettuare in maniera veloce l’eventuale
installazione di pacchetti software aggiuntivi dirattamente da internet. In alternativa si
suggerisce la distribuzione **Scientific Linux SL 5.X** la quale, pur avendo le stesse caratteristiche
della RedHat, non necessita di licenza per l’aggiornamento via internet di eventuali pacchetti
software aggiuntivi.

Vediamo in dettaglio i principali prerequisiti per una corretta ed efficiente configurazione di
base del sistema.

### 2.1. Configurazione del Sistema Operativo

Il S. O. deve essere installato con le seguenti caratteristiche:

- può essere installato a 64 bit se le caratteristiche della macchina lo consentono
- deve essere installato in lingua italiana
- deve essere effettuata una installazione completa di tutti i pacchetti software, in particolare
    LVM (Logical Volume Manager) per consentire una migliore e più efficace gestione dello
    spazio disco anche successivamente all’installazione, e il compilatore C nella versione GCC
- deve essere installato il manuale in linea (man) completo
- si suggerisce di valorizzare l’hostname, meglio se con un nome legato alla funzionalità del
    server (ad esempio Sbnweb-codpolo o qualcosa di simile).

È importante verificare con il comando

    rpm –qa|grep glibc

che al termine dell’installazione del compilatore GCC sul server risultino presenti le seguenti
librerie, necessarie per il corretto funzionamento delle procedure sviluppate per l’export
UNIMARC:

Librerie C ‘glibc’ glibc-headers-2.5-x

- glibc-2.5-x
- glibc-devel-2.5-x
- glibc-common-2.5-x

Per quanto riguarda la definizione dello spazio disco, tenendo presente la dotazione standard di
un server Linux (di solito due dischi di dimensioni ridotte per il Sistema Operativo, e due dischi
di grandi dimensioni per l’applicativo e i dati), lo spazio disco deve essere così definito:

- la partizione di sistema, in RAID1, deve avere una dimensione di almeno 50 GByte;
- la partizione dedicata ai dati, in RAID1, deve avere una dimensione di almeno 100 GByte.

Nella partizione di sistema devono essere definiti i seguenti **file-system** con i relativi
dimensionamenti:
|Partizione| Dimensioni|
|---|---|
|/ (root) |1 Gbyte|
|/usr | 5 Gbyte|
|/usr/local |8 Gbyte|
|/home |5 Gbyte|
|/var |1 Gbyte|
|/tmp |1 Gbyte|
|/opt |1 Gbyte|

Nella partizione dei dati devono essere definiti i seguenti file system:
|Partizione| Dimensioni|
|---|---|
/db |40 Gbyte|
/export |20 Gbyte|
/backup |30 Gbyte|

**Nota:** i dimensionamenti sopra riportati vanno considerati come minimali in fase di partenza,
tenendo conto che lo strumento LVM consente di modificarli dinamicamente e in qualsiasi
momento a seconda delle esigenze riscontrate.

### 2.2. Preparazione ambiente di base. Nomenclatura e dimensionamento delle strutture di lavoro

In questo paragrafo affrontiamo la preparazione dell’ambiente di base, intendendo in particolare
la definizione delle directory e sotto-directory, con relativi permessi e proprietari, necessarie ad
accogliere i prodotti software e l’applicativo SBNWEB.

#### 2.2.1. Directory

Sotto la directory **/db** creare le seguenti directory (per i proprietari e i permessi vedi par. 2.2.3)

 - **pgsql8.3.5**  contiene la directory con le strutture e i dati del databases;
- **pgsql8.3.5/data** contiene le strutture e i dati del databases;
- **migrazione** contiene l’ambiente di lavoro per l’eventuale migrazione del polo;

Sotto la directory **/export** creare le seguenti sotto-directory (per i proprietari e i permessi vedi par. 2.2.3)

- **Trasf** contiene l’ambiente per la distribuzione degli aggiornamenti sw dell’applicativo
- **Trasf/dep**
- **Trasf/Storico**
- **Trasf/logs**

Sotto la directory **/backup** creare le seguenti directory (per i proprietari e i permessi vedi par. 2.2.3)

- **DUMP_DB** contiene i file di backupi del databases;
- **DUMPSYS** contiene i file di backup dell’ambiente, cioè delle directory principali;
- **logs** destinata ad accogliere i file di log dell’esecuzione delle procedure di backup;

Sotto la directory **/home** creare la seguente directory (per i proprietari e i permessi vedi par. 2.2.3)

- **SCRIPTS** contiene gli scripts di backup ed altri per la gestione del sistema;

#### 2.2.2. Creazione degli utenti

Occorre creare i seguenti utenti:

|Utente|Descrizione|
|---|---|
|jboss| utente preposto all’utilizzo dell’applicazione;|
|export |utente preposto esclusivamente alle operazioni di export UNIMARC (qualora le si voglia eseguire senza attivazione da interfaccia applicativa);|
|postgres |utente preposto alla gestione dell’ambiente database.|

Gli utenti sopra descritti vanno creati con le seguenti caratteristiche:

#### 2.2.3. Proprietari e permessi

Una volta create le directory e aver definito gli utenti, possiamo correttamente assegnare i
proprietari e i permessi.

/db (**drwxrwxrwx** postgres root)

**drwxr-xr-x** postgres postgres **pgsql8.3.**

**drwx------** postgres postgres **pgsql8.3.5/data**

**drwxrwxr-x** jboss jboss **migrazione**

/export (**drwxr-xr-x** export jboss)

**drwxrwxr-x** export export Trasf

**drwxrwxr-x** export export Trasf/dep

**drwxrwxr-x** export export Trasf/Storico

**drwxrwxr-x** export export Trasf/logs

/backup (**drwxr-xr-x** root root)

**drwxr-xr-x** postgres root **DUMP_DB**

**drwxr-xr-x** root root **DUMPSYS**

**drwxrwxrwx** root root **logs**

/home/SCRIPTS (**drwxr-xr-x** root root)

## 3. Installazione e configurazione del software di base

Esamineremo in questo capitolo le modalità di installazione e configurazione dei prodotti
software di base che costituiscono l’ambiente operativo di SBNWEB.

### 3.1. Application Server JBOSS

Trattiamo in quasto paragrafo l’installazione del prodotto JBOSS v. 4.2.3. Qualora si decida
l’installazione del prodotto nella versione 5.1, si faccia riferimento al documento "Migrazione
dell’applicativo SbnWeb in ambiente Jboss 5.1"

L’installazione dell’Application Server JBOSS consta di due passi distinti che riguardano il
prodotto JBOSS stesso e l’ambiente Java. Vediamoli in dettaglio.

#### 3.1.1. JBOSS

I passi da eseguire sono i seguenti (vanno eseguiti come utente root):

1. Aggiornare preliminarmente il file /etc/hosts aggiungendo alla riga 127.0.0.1 l’hostname del
    server
2. Scaricare da Internet il pacchetto JBOSS-4.2.3.GA.zip e posizionarlo nella directory _/usr/local_
3. Portarsi nella directory _/usr/local_ e decomprimere il pacchetto.

L’ultimo comando crea una directory jboss-4.2.3.GA sotto la quale troverà locazione tutto
l’ambiente riferibile all’Application Server.

Completata l’installazione del pacchetto **eliminare** dalla cartella /usr/local/jboss-
4.2.3.GA/server/default/lib le seguenti librerie:

- commons-codec.jar
- commons-collections.jar
- commons-httpclient.jar
- hibernate3.jar
- quartz.jar

e sostituirle con le versioni più avanzate, reperibili su internet, le cui ulteriori funzionalità sono
necessarie all’applicativo:

- commons-codec-1.3.jar
- commons-collections-3.2.jar
- commons-httpclient-3.1.jar
- hibernate-3.2.6.jar
- quartz-all-1.6.5.jar

aggiungere quindi le librerie:

- postgresql-8.3-603.jdbc2ee.jar

L’attività può considerarsi conclusa.

L’ultimo passo riguarda la personalizzazione di due file che contengono le informazioni per il
corretto puntamento al DB; tale personalizzazione va però effettuata a conclusione delle attività
di creazione del DB e a valle dell’installazione dell’applicativo; per questa ragione se ne rimanda
la trattazione al capitolo 2.2 Configurazione dell’applicativo del documento "Installazione
dell’applicativo SBNWEB in ambiente Linux".

Per le analoghe attività relative alla versione 5.1 del prodotto, si rimanda la cap. 2 del citto
documento "Migrazione dell’applicativo SbnWeb in ambiente Jboss 5.1"

#### 3.1.2. JDK (ambiente Java)

Per l’installazione di questa componente, seguire i seguenti passi come utente root

Da Internet scaricare il prodotto _JDK 1.5.X_ per piattaforme LINUX e in formato _.bin_
autoestraente; quindi posizionarlo nella directory _/usr/local_.

Posizionarsi nella _/usr/local_ ed eseguire il file _.bin_ (./<nome-del-file>.bin)

Verrà eseguita l’installazione dell’ambiente java in una directory creata appositamente con il
nome e la versione del prodotto (ad esempio: _jdk1.5.0_12_ )

Per comodità di lavoro, creare sotto _/usr/local_ il link **jdk** a **jdk1.5.0_12/** con il comando

    ln –s jdk1.5.0_12/ jdk
### 3.2. Apache

Solitamente il prodotto APACHE 2.2 viene installato insieme al Sistema Operativo nella directory
standard _/etc/httpd_.

Pertanto in questo paragrafo esamineremo in dettaglio le personalizzazioni da effettuare nel file
di configurazione **proxy_ajp.conf**.

Il file in questione si trova nella cartella _/etc/httpd/conf.d_ e va modificato come utente root:

Inserire in fondo al file **proxy_ajp.conf** le seguenti righe

    ProxyPass /sbn ajp://localhost:8009/sbn/
    ProxyPass /SbnMarcWeb ajp://localhost:8009/SbnMarcWeb/
    ProxyPass /servizi ajp://localhost:8009/servizi/
    ProxyPass /web-console ajp://localhost:8009/web-console
    ProxyPass /jmx-console ajp://localhost:8009/jmx-console

Effettuate le personalizzazioni sopra descritte, bisogna restartare il servizio ed inserire in
maniera permanente lo stop/start automatico alla ripartenza del sistema. I passi sono (sempre
come root):

fermare il servizio con il comando **/etc /init.d /httpd stop**

far ripartire il servizio con il comando **/etc/init.d/httpd start**

esguire come root il comando **chkconfig httpd on**

### 3.3. RDBMS PostgreSQL

L’installazione del prodotto richiede che alcuni passi siano fatti come utente root (installazione),
mentre la configurazione deve essere fatta come utente _postgres_ , per la creazione dell’utenza
_postgres_ si rimanda al par. 2.2.2.

Naturalmente in questa sede viene esaminato solo l’aspetto di installazione e configurazione del
prodotto, rimandando ad altro documento tutto ciò che concerne la creazione del database, delle
relative strutture dati e il loro popolamento.

#### 3.3.1. Installazione di PostgreSQL

I passi, da effettuare come utente root, sono i seguenti:

Scaricare da internet il pacchetto postgresql-8.3.5.tar.gz e posizionarlo sotto una directory di
lavoro; in ambiente di collaudo è stata scelta **_/usr/local/src_**

Spostarsi nella directory di lavoro e decomprimere il pacchetto con il comando

    tar -xzvf postgresql-8.3.5.tar.gz

Verrà quindi creata una cartella, chiamata di default come la versione di PostgreSQL che stiamo
utilizzando, a cui dovremo imporre come proprietario l'utente Postgres tramite il comando

    chown -R postgres.postgres postgresql-8.3.

Ora i sorgenti dovranno essere configurati per la compilazione tramite la seguente serie di
istruzioni:

**./configure --prefix=/usr/local/pgsql8.3.5** (directory dove verrà installato il motore)</br>
**make** (compilazione, può richiedere un certo tempo)</br>
**make check** (per controllare l’esito della compilazione)</br>
**make install** (installazione)

Infine, settare correttamente la variabile PATH nel profile dell’utente postgres (che, lo
ricordiamo, si trova nella home-directory /db): nel file .bash_profile aggiungere in fondo alla
definizione della variabile PATH la directory _/usr/local/pgsql8.3.5/bin_

    PATH=$PATH:/usr/local/pgsql8.3.5/bin

#### 3.3.2. Definizione del database cluster

Terminata la fase di installazione, passiamo alla creazione del database cluster.

Usando l’utente root posizionarsi nella cartella _/db/pgsql8.3.5_ e creare la directory **data** quindi
assegnarle postgres:postgres come proprietario


    cd /db/pgsql8.3.
    mkdir data
    chown -R postgres:postgres data

Passare a utente postgres con il comando su – postgres

Creare in PostgreSQL un nuovo database cluster con il comando

    /usr/local/pgsql8.3.5/bin/initdb --locale=C --lc-messages=it_IT.UTF- 8 - E UNICODE - D /db/pgsql8.3.5/data

#### 3.3.3. Configurazione dei file per l’accesso al database server

L’ultimo passo riguarda la personalizzazione di due importanti file di configurazione per
consentire il corretto accesso al database server.

I file si trovano sotto la directory _/db/ pgsql8.3.5/data_ e sono:

- postgresql.conf
- pg_hba.conf

Il file **_postgresql.conf_** va modificato per consentire che il database server si ponga in ascolto per
qualsiasi indirizzo (il default è ‘localhost’).

Nella sezione _"CONNECTIONS AND AUTHENTICATION"_, togliere il cancelletto di commento alla
riga

    #listen_addresses = 'localhost'

e sostituire al termine ‘localhost’ il termine '*' (mantenendo gli apici).

Sempre nel file **_postgresql.conf_** , nella sezione _"WRITE AHEAD LOG"_, togliere il cancelletto di
commento alla riga


    #checkpoint_segments = 3

e sostituire il valore **10** al valore 3.

Infine, sempre nello stesso file, nella sezione _"CLIENT CONNECTION DEFAULTS"_, alla variabile
default_text_search_config modificare il valore di default _pg_catalog.english_ con il valore

    pg_catalog.italian

Nel file **_pg_hba.conf_** vanno inserite le informazioni necessarie ad abilitare uno o più indirizzi IP
alla connessione col database server.

Gli indirizzi da abilitare vanno indicati in CIDR notation nell’apposita sezione: _"# IPv4 local
connections:"_ o _"# IPv 6 local connections:"_ dipendentemente dalla tipologia di rete in cui è
inserito il server.

Per ogni sottorete o indirizzo puntuale da cui provengono le richieste di connessione al database
server va inserita una riga del tipo:

    host all all <indirizzo IP> /32 password

Si riportano, a solo titolo di esempio, due righe da inserite in "# IPv4 local connections:" la prima
per abilitare uno specifico indirizzo (193.206.221.2), la seconda per abilitare una sottorete (da
192.168.10.0 a 192.168.10.255), in ambedue i casi l’accesso sarà condizionato alla verifica della
password.

    host all all 193.206.221.2/32 password
    host all all 1 92 .1 68 .10.0/ 24 password

#### 3.3.4. Avvio del database server

A questo punto le operazioni sono terminate e si può far partire il database server (sempre come
utente _postgres_ ).

Per avviare il database server usare il comando:

    /usr/local/pgsql8.3.5/bin/pg_ctl -D /db/pgsql8.3.5/data -l logfile start

Per fermare il database server usare il comando:

    /usr/local/pgsql8.3.5/bin/pg_ctl -D /db/pgsql8.3.5/data stop

Infine, una volta avviato postgres, occorre modificare la password dell’utente DB postgres con i
seguenti comandi (sempre come utente _postgres_)

    psql alter user postgres with password 'postgresadm'; \q

Nella fase di installazione dell’applicativo verranno forniti degli appositi files per l’attivazione
automatica del data server all’avvio della macchina.

La loro descrizione e le istruzioni per il loro posizionamento e personalizzazione verranno
forniti nel documento di installazione delle componenti applicative.


