BEGIN;

/*ATTENZIONE: prima di eseguire sostituire al valore XXX il codice del polo per il quale si sta generando il DB*/ 
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

/* Data for the `sbnweb.tbf_polo` table  (Records 1 - 1) */
INSERT INTO "sbnweb"."tbf_polo" ("id_parametro", "cd_polo", "url_indice", "username", "password", "denominazione", "ute_var", "ts_ins", "ute_ins", "ts_var", "url_polo", "username_polo", "email")
VALUES (1, 'XXX', 'http://193.206.221.21/indice/servlet/serversbnmarc', 'XXXAMM000032', 'mitras43', 'Ambiente COLLAUDO - Polo XXX', 'IniDB', now(), 'IniDB', now(), 'http://localhost:8080/SbnMarcWeb/SbnMarcTest', '000001', 'mailgestorepolo');

COMMIT;
