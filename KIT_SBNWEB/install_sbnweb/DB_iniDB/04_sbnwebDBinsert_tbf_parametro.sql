
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
DELETE from "sbnweb"."tbf_polo";
DELETE from "sbnweb"."tbf_par_sem";
DELETE from "sbnweb"."tbf_par_mat";
DELETE from "sbnweb"."tbf_par_auth";
DELETE from "sbnweb"."tbf_parametro";

----- inizializza sequence id_gruppo_attivita_polo -----
ALTER SEQUENCE "sbnweb"."tbf_parametro_id_parametro_seq"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  RESTART 1
    CACHE 1  NO CYCLE;

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
/*Data for the `sbnweb.tbf_parametro` table  (Records 1 - 1) */
INSERT INTO "sbnweb"."tbf_parametro" ("cd_livello", "tp_ret_doc", "tp_all_pref", "cd_liv_ade", "fl_spogli", "fl_aut_superflui", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "sololocale")
VALUES ('96', '001', '2', '6', 'S', 'S', 'inidb', now(), 'inidb', now(), 'N', 'N');

/* Data for the `sbnweb.tbf_par_mat` table  (Records 1 - 5) */
INSERT INTO "sbnweb"."tbf_par_mat" ("cd_par_mat", "id_parametro", "tp_abilitaz", "cd_contr_sim", "fl_abil_forzat", "cd_livello", "sololocale")
VALUES ('C', 1, 'S', '1', 'S', '90', 'N');
INSERT INTO "sbnweb"."tbf_par_mat" ("cd_par_mat", "id_parametro", "tp_abilitaz", "cd_contr_sim", "fl_abil_forzat", "cd_livello", "sololocale")
VALUES ('E', 1, 'S', '1', 'S', '90', 'N');
INSERT INTO "sbnweb"."tbf_par_mat" ("cd_par_mat", "id_parametro", "tp_abilitaz", "cd_contr_sim", "fl_abil_forzat", "cd_livello", "sololocale")
VALUES ('G', 1, 'S', '1', 'S', '90', 'N');
INSERT INTO "sbnweb"."tbf_par_mat" ("cd_par_mat", "id_parametro", "tp_abilitaz", "cd_contr_sim", "fl_abil_forzat", "cd_livello", "sololocale")
VALUES ('M', 1, 'S', '1', 'S', '90', 'N');
INSERT INTO "sbnweb"."tbf_par_mat" ("cd_par_mat", "id_parametro", "tp_abilitaz", "cd_contr_sim", "fl_abil_forzat", "cd_livello", "sololocale")
VALUES ('U', 1, 'S', '1', 'S', '90', 'N');
INSERT INTO "sbnweb"."tbf_par_mat" ("cd_par_mat", "id_parametro", "tp_abilitaz", "cd_contr_sim", "fl_abil_forzat", "cd_livello", "sololocale")
VALUES ('H', 1, 'N', '1', 'S', '90', 'N');
INSERT INTO "sbnweb"."tbf_par_mat" ("cd_par_mat", "id_parametro", "tp_abilitaz", "cd_contr_sim", "fl_abil_forzat", "cd_livello", "sololocale")
VALUES ('L', 1, 'N', '1', 'S', '90', 'N');

/* Data for the `sbnweb.tbf_par_sem` table  (Records 1 - 3) */
INSERT INTO "sbnweb"."tbf_par_sem" ("tp_tabella_codici", "cd_tabella_codici", "id_parametro", "sololocale")
VALUES ('SOGG', 'FIR', 1, 'N');
INSERT INTO "sbnweb"."tbf_par_sem" ("tp_tabella_codici", "cd_tabella_codici", "id_parametro", "sololocale")
VALUES ('SCLA', 'D', 1, 'N');

/* Data for the `sbnweb.tbf_par_auth` table  (Records 1 - 11) */
INSERT INTO "sbnweb"."tbf_par_auth" ("cd_par_auth", "id_parametro", "tp_abil_auth", "fl_abil_legame", "fl_leg_auth", "cd_livello", "cd_contr_sim", "fl_abil_forzat", "sololocale")
VALUES ('AU', 1, 'S', 'S', 'S', '90', '1', 'S', 'N');
INSERT INTO "sbnweb"."tbf_par_auth" ("cd_par_auth", "id_parametro", "tp_abil_auth", "fl_abil_legame", "fl_leg_auth", "cd_livello", "cd_contr_sim", "fl_abil_forzat", "sololocale")
VALUES ('CL', 1, 'S', 'S', 'S', '90', '1', 'S', 'N');
INSERT INTO "sbnweb"."tbf_par_auth" ("cd_par_auth", "id_parametro", "tp_abil_auth", "fl_abil_legame", "fl_leg_auth", "cd_livello", "cd_contr_sim", "fl_abil_forzat", "sololocale")
VALUES ('DE', 1, 'S', 'S', 'S', '90', '1', 'S', 'N');
INSERT INTO "sbnweb"."tbf_par_auth" ("cd_par_auth", "id_parametro", "tp_abil_auth", "fl_abil_legame", "fl_leg_auth", "cd_livello", "cd_contr_sim", "fl_abil_forzat", "sololocale")
VALUES ('LU', 1, 'S', 'S', 'S', '90', '1', 'S', 'N');
INSERT INTO "sbnweb"."tbf_par_auth" ("cd_par_auth", "id_parametro", "tp_abil_auth", "fl_abil_legame", "fl_leg_auth", "cd_livello", "cd_contr_sim", "fl_abil_forzat", "sololocale")
VALUES ('MA', 1, 'S', 'S', 'S', '90', '1', 'S', 'N');
INSERT INTO "sbnweb"."tbf_par_auth" ("cd_par_auth", "id_parametro", "tp_abil_auth", "fl_abil_legame", "fl_leg_auth", "cd_livello", "cd_contr_sim", "fl_abil_forzat", "sololocale")
VALUES ('PP', 1, 'S', 'S', 'S', '90', '1', 'S', 'S');
INSERT INTO "sbnweb"."tbf_par_auth" ("cd_par_auth", "id_parametro", "tp_abil_auth", "fl_abil_legame", "fl_leg_auth", "cd_livello", "cd_contr_sim", "fl_abil_forzat", "sololocale")
VALUES ('RE', 1, 'N', 'S', 'S', '90', '1', 'N', 'N');
INSERT INTO "sbnweb"."tbf_par_auth" ("cd_par_auth", "id_parametro", "tp_abil_auth", "fl_abil_legame", "fl_leg_auth", "cd_livello", "cd_contr_sim", "fl_abil_forzat", "sololocale")
VALUES ('SO', 1, 'S', 'S', 'S', '90', '1', 'S', 'N');
INSERT INTO "sbnweb"."tbf_par_auth" ("cd_par_auth", "id_parametro", "tp_abil_auth", "fl_abil_legame", "fl_leg_auth", "cd_livello", "cd_contr_sim", "fl_abil_forzat", "sololocale")
VALUES ('TH', 1, 'S', 'S', 'S', '90', '1', 'S', 'S');
INSERT INTO "sbnweb"."tbf_par_auth" ("cd_par_auth", "id_parametro", "tp_abil_auth", "fl_abil_legame", "fl_leg_auth", "cd_livello", "cd_contr_sim", "fl_abil_forzat", "sololocale")
VALUES ('TU', 1, 'S', 'S', 'S', '90', '1', 'S', 'N');
INSERT INTO "sbnweb"."tbf_par_auth" ("cd_par_auth", "id_parametro", "tp_abil_auth", "fl_abil_legame", "fl_leg_auth", "cd_livello", "cd_contr_sim", "fl_abil_forzat", "sololocale")
VALUES ('UM', 1, 'S', 'S', 'S', '96', '1', 'S', 'N');

COMMIT;
