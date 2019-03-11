BEGIN;

--elimina dati caricati in precedenza ---
DELETE from "sbnweb"."tbf_contatore";
DELETE from "sbnweb"."tbf_bibliotecario";
DELETE from "sbnweb"."trf_profilo_biblioteca";
DELETE from "sbnweb"."tbf_profilo_abilitazione";
DELETE from "sbnweb"."tbf_utenti_professionali_web"; 
DELETE from "sbnweb"."trf_utente_professionale_biblioteca";
DELETE from "sbnweb"."tbf_anagrafe_utenti_professionali";
DELETE FROM "sbnweb"."tbf_biblioteca_in_polo";
DELETE FROM "sbnweb"."tbf_biblioteca";
DELETE from "sbnweb"."trf_gruppo_attivita_polo";
DELETE from "sbnweb"."trf_attivita_polo" ;
UPDATE "sbnweb"."tbf_polo" SET id_gruppo_attivita = NULL;
DELETE from "sbnweb"."tbf_gruppo_attivita";

----- inizializza sequence id_gruppo_attivita_polo -----
ALTER SEQUENCE "sbnweb"."tbf_gruppo_attivita_id_gruppo_attivita_polo_seq"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  RESTART 1
    CACHE 1  NO CYCLE;

----- inizializza sequence id_attivita_polo -----
ALTER SEQUENCE "sbnweb"."trf_attivita_polo_id_attivita_polo_seq"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  RESTART 1
    CACHE 1  NO CYCLE;

----- inizializza sequence id_biblioteca_seq -----
ALTER SEQUENCE "sbnweb"."tbf_biblioteca_id_biblioteca_seq"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  RESTART 1
    CACHE 1  NO CYCLE;

----- inizializza sequence id_utente_professionale_seq -----
ALTER SEQUENCE "sbnweb"."tbf_anagrafe_utenti_professionali_id_utente_professionale_seq"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  RESTART 1
    CACHE 1  NO CYCLE;

----- inizializza sequence id_gruppo_attivita_polo -----

ALTER SEQUENCE "sbnweb"."tbf_profilo_abilitazione_id_profilo_abilitazione_seq"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  RESTART 1
    CACHE 1  NO CYCLE;

ALTER SEQUENCE "sbnweb"."trf_profilo_biblioteca_id_profilo_abilitazione_biblioteca_seq"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  RESTART 1
    CACHE 1  NO CYCLE;
    
----------
/* Data for the `sbnweb.tbf_gruppo_attivita` table  (Records 1 - 1) */

INSERT INTO "sbnweb"."tbf_gruppo_attivita" ("cd_polo", "ds_name")
VALUES ((select _po.cd_polo from "sbnweb"."tbf_polo" _po), 'defaul POLO');

UPDATE "sbnweb"."tbf_polo" SET id_gruppo_attivita=
(SELECT min(id_gruppo_attivita_polo) FROM "sbnweb"."tbf_gruppo_attivita");


/* Data for the `sbnweb.trf_attivita_polo` table  (Records 1 - 167) */
INSERT INTO "sbnweb"."trf_attivita_polo" ("cd_polo", "cd_attivita", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
SELECT (select _po.cd_polo from "sbnweb"."tbf_polo" _po) as "_cd_polo",
"cd_attivita", 'IniDB' as _ute_ins, now() as _ts_ins, 'IniDB' as _ute_var, now() as _ts_var, 'N' as "_fl_canc"
FROM "sbnweb"."tbf_attivita"
ORDER BY "cd_attivita";

/* Data for the `sbnweb.tbf_gruppo_attivita_polo`  */
INSERT INTO "sbnweb"."trf_gruppo_attivita_polo" ("id_attivita_polo","id_gruppo_attivita_polo", "fl_include")
SELECT id_attivita_polo, 1, 'S' FROM "sbnweb"."trf_attivita_polo"  ;

--
--CREAZIONE BIBLIOTECA DI AMMINISTRAZIONE POLO --
--
--
-- Data for table sbnweb.tbf_biblioteca (OID = 22410) (LIMIT 0,1)
--
INSERT INTO "sbnweb"."tbf_biblioteca" ( "cd_ana_biblioteca", "cd_polo", "cd_bib", "nom_biblioteca", "unit_org", "indirizzo", "cpostale", "cap", "telefono", "fax", "note", "p_iva", "cd_fiscale", "e_mail", "tipo_biblioteca", "paese", "provincia", "cd_bib_cs", "cd_bib_ut", "cd_utente", "flag_bib", "localita", "chiave_bib", "chiave_ente", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "tidx_vector_nom_biblioteca", "tidx_vector_indirizzo")
VALUES ('CD_ANA', (select _po.cd_polo from "sbnweb"."tbf_polo" _po), ' __', 'Biblioteca di startup polo', 'unita org', 'Indirizzo', '04011', '12344', '23131', '12313', 'note', 'p.iva', 'cd.fiscale', 'e@mail', 'B', 'CD', 'CD', ' __', ' __', 12, 'C', 'ASDASD', 'ASDASD', 'ASASDASD', 'inidb', now(), 'inidb', now(), 'N', '''pol'':4 ''startup'':3 ''bibliotec'':1', '''indirizz'':1');

--
-- Data for table sbnweb.tbf_biblioteca_in_polo (OID = 22420) (LIMIT 0,1)
--
INSERT INTO "sbnweb"."tbf_biblioteca_in_polo" ("cd_biblioteca", "id_parametro", "cd_polo", "id_gruppo_attivita_polo", "id_biblioteca", "ky_biblioteca", "cd_ana_biblioteca", "ds_biblioteca", "ds_citta", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "cd_sistema_metropolitano")
VALUES (' __', 1, (select _po.cd_polo from "sbnweb"."tbf_polo" _po), 1, 1, NULL, '', 'Root', NULL, 'inidb', now(), 'inidb', now(), 'N', NULL);

--
-- Data for table sbnweb.tbf_profilo_abilitazione (OID = 22554) (LIMIT 0,1)
--
INSERT INTO "sbnweb"."tbf_profilo_abilitazione" ( "cd_prof", "cd_biblioteca", "cd_polo", "dsc_profilo", "nota_profilo", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ( '001', ' __', (select _po.cd_polo from "sbnweb"."tbf_polo" _po), 'Abilitazioni del Super utente', 'Default', 'inidb', now(), 'inidb', now(), 'N');


/* Data for the `sbnweb.trf_profilo_biblioteca` table  (Records 1 - 15) */
/* per biblioteca fittizia __ cui fa capo l'utente root */

INSERT INTO "sbnweb"."trf_profilo_biblioteca" ( "cd_attivita", "id_profilo_abilitazione", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F0000', 1, 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."trf_profilo_biblioteca" ("cd_attivita", "id_profilo_abilitazione", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('FB000', 1, 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."trf_profilo_biblioteca" ("cd_attivita", "id_profilo_abilitazione", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('FBGA0', 1, 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."trf_profilo_biblioteca" ("cd_attivita", "id_profilo_abilitazione", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('FBGF0', 1, 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."trf_profilo_biblioteca" ("cd_attivita", "id_profilo_abilitazione", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('FBIA0', 1, 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."trf_profilo_biblioteca" ( "cd_attivita", "id_profilo_abilitazione", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('FBIF0', 1, 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."trf_profilo_biblioteca" ("cd_attivita", "id_profilo_abilitazione", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('FP000', 1, 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."trf_profilo_biblioteca" ("cd_attivita", "id_profilo_abilitazione", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('FU000', 1, 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."trf_profilo_biblioteca" ("cd_attivita", "id_profilo_abilitazione", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('FUGA0', 1, 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."trf_profilo_biblioteca" ("cd_attivita", "id_profilo_abilitazione", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ( 'FUGF0', 1, 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."trf_profilo_biblioteca" ("cd_attivita", "id_profilo_abilitazione", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ( 'FUIA0', 1, 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."trf_profilo_biblioteca" ("cd_attivita", "id_profilo_abilitazione", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('FUIF0', 1, 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."trf_profilo_biblioteca" ("cd_attivita", "id_profilo_abilitazione", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('FCV00', 1, 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."trf_profilo_biblioteca" ( "cd_attivita", "id_profilo_abilitazione", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('FCVG0', 1, 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."trf_profilo_biblioteca" ( "cd_attivita", "id_profilo_abilitazione", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('FCVI0', 1, 'inidb', now(), 'inidb', now(), 'N');


--
--CREAZIONE UTENZA DI AMMINISTRAZIONE POLO --
--

-- Data for table sbnweb.tbf_anagrafe_utenti_professionali 
--
INSERT INTO "sbnweb"."tbf_anagrafe_utenti_professionali" ( "nome", "cognome", "ts_ins", "ts_var", "ute_ins", "ute_var", "fl_canc")
VALUES ('root', 'root', now(), now(), 'inidb', 'inidb', 'N');

--
-- Data for table sbnweb.trf_utente_professionale_biblioteca 
--
INSERT INTO "sbnweb"."trf_utente_professionale_biblioteca" ("id_utente_professionale", "cd_polo", "cd_biblioteca", "tp_ruolo", "note_competenze", "ufficio_appart")
VALUES (1, (select _po.cd_polo from "sbnweb"."tbf_polo" _po), ' __', 'root', 'super user', NULL);

--
-- Data for table sbnweb.tbf_bibliotecario 
--
INSERT INTO "sbnweb"."tbf_bibliotecario" ("id_parametro", "id_utente_professionale", "id_profilo_abilitazione", "cd_livello_gbantico", "cd_livello_gbmoderno", "cd_livello_gssoggetti", "cd_livello_gsclassi", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (1, 1, 1, NULL, NULL, NULL, NULL, 'inidb', now(), 'inidb', now(), 'N');

/* Data for the `sbnweb.tbf_utenti_professionali_web` table  (Records 1 - 1) */
/*password temporanea rootroot*/

INSERT INTO "sbnweb"."tbf_utenti_professionali_web" ("id_utente_professionale", "userid", "password", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "change_password", "last_access")
VALUES ( 1, 'root', 'wJY5Wng7Logzzr2JGcc/7Q==', 'inidb', now(), 'inidb', now(), 'N', 'S', now());

/* Data for the `sbnweb.tbf_contatore` table  (Records 1 - 4) */

INSERT INTO "sbnweb"."tbf_contatore" ("cd_polo", "cd_biblioteca", "cd_cont", "anno", "key1", "ultimo_prg", "lim_max", "attivo", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ((SELECT _po.cd_polo FROM "sbnweb"."tbf_polo" _po), ' __', 'USR', 0, '', 0, 0, NULL, (SELECT _po.cd_polo FROM "sbnweb"."tbf_polo" _po)||'inidb', now(), (SELECT _po.cd_polo FROM "sbnweb"."tbf_polo" _po)||'inidb', now(), 'N');

INSERT INTO "sbnweb"."tbf_contatore" ("cd_polo", "cd_biblioteca", "cd_cont", "anno", "key1", "ultimo_prg", "lim_max", "attivo", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ((SELECT _po.cd_polo FROM "sbnweb"."tbf_polo" _po), ' __', 'PWD', 0, '', 0, 0, NULL, (SELECT _po.cd_polo FROM "sbnweb"."tbf_polo" _po)||'inidb', now(), (SELECT _po.cd_polo FROM "sbnweb"."tbf_polo" _po)||'inidb', now(), 'N');

INSERT INTO "sbnweb"."tbf_contatore" ("cd_polo", "cd_biblioteca", "cd_cont", "anno", "key1", "ultimo_prg", "lim_max", "attivo", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ((SELECT _po.cd_polo FROM "sbnweb"."tbf_polo" _po), ' __', 'USL', 0, '', 0, 360, NULL, (SELECT _po.cd_polo FROM "sbnweb"."tbf_polo" _po)||'inidb', now(), (SELECT _po.cd_polo FROM "sbnweb"."tbf_polo" _po)||'inidb', now(), 'N');

INSERT INTO "sbnweb"."tbf_contatore" ("cd_polo", "cd_biblioteca", "cd_cont", "anno", "key1", "ultimo_prg", "lim_max", "attivo", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ((SELECT _po.cd_polo FROM "sbnweb"."tbf_polo" _po), ' __', 'PWL', 0, '', 0, 180, NULL, (SELECT _po.cd_polo FROM "sbnweb"."tbf_polo" _po)||'inidb', now(), (SELECT _po.cd_polo FROM "sbnweb"."tbf_polo" _po)||'inidb', now(), 'N');

COMMIT;