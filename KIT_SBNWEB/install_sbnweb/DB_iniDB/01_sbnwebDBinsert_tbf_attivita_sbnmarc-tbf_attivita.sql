BEGIN;

------ pulizia precedente caricamento ------
DELETE from "sbnweb"."tbf_attivita";
DELETE from "sbnweb"."tbf_attivita_sbnmarc";

----- inizializza sequence id_attivita -----
ALTER SEQUENCE "sbnweb"."tbf_attivita_sbnmarc_id_attivita_sbnmarc_seq"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  RESTART 1
    CACHE 1  NO CYCLE;

/* Data for the `sbnweb.tbf_attivita_sbnmarc` table  (Records 1 - 198) */


INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione Acquisizioni', 'Attivita POLO', 1, 'A0000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Interrogazione Ordini', 'Attivita POLO', 1, 'A1100', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione ordini', 'Attivita POLO', 1, 'A1200', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione ordini acquisto-visione trattenuta', 'Attivita POLO', 1, 'A1210', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione ordini deposito legale-dono', 'Attivita POLO', 1, 'A1220', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione ordini scambio', 'Attivita POLO', 1, 'A1230', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione ordini rilegatura', 'Attivita POLO', 1, 'A1240', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Accessionamento ordini', 'Attivita POLO', 1, 'A1400', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Interrogazione gare d''acquisto', 'Attivita POLO', 1, 'A2100', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione gare d''acquisto', 'Attivita POLO', 1, 'A2200', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Interrogazione bilancio', 'Attivita POLO', 1, 'A3100', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione bilancio', 'Attivita POLO', 1, 'A3200', NULL);


INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Interrogazione fatture', 'Attivita POLO', 1, 'A4100', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione fatture', 'Attivita POLO', 1, 'A4200', NULL);


INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Interrogazione comunicazioni', 'Attivita POLO', 1, 'A5100', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione comunicazioni', 'Attivita POLO', 1, 'A5200', NULL);


INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Interrogazione suggerimento lettore', 'Attivita POLO', 1, 'A6100', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione suggerimento lettore', 'Attivita POLO', 1, 'A6200', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Interrogazione sezione acquisizioni', 'Attivita POLO', 1, 'A8100', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione sezione acquisizioni', 'Attivita POLO', 1, 'A8200', NULL);


INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Interrogazione suggerimento bibliotecario', 'Attivita POLO', 1, 'A9100', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Aggiornamento suggerimento bibliotecario', 'Attivita POLO', 1, 'A9200', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Accettazione/Rifiuto suggerimento bibliotecario', 'Attivita POLO', 1, 'A9300', NULL);


INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Interrogazione profili di acquisto', 'Attivita POLO', 1, 'AA100', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione profili di acquisto', 'Attivita POLO', 1, 'AA200', NULL);


INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Interrogazione cambi', 'Attivita POLO', 1, 'AB100', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione cambi', 'Attivita POLO', 1, 'AB200', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampa registro di ingresso', 'Attivita POLO', 1, 'AC100', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampa ordine', 'Attivita POLO', 1, 'AC200', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampa buoni di carico', 'Attivita POLO', 1, 'AC300', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Configurazione', 'Attivita POLO', 1, 'AE000', NULL);


INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Interrogazione buoni d''ordine', 'Attivita POLO', 1, 'AF100', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione buoni d''ordine', 'Attivita POLO', 1, 'AF200', NULL);


INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Interrogazione fornitori', 'Attivita POLO', 1, 'AR110', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione fornitori', 'Attivita POLO', 1, 'AR120', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampa fornitori di polo', 'Attivita POLO', 1, 'AR960', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampa fornitori di biblioteca', 'Attivita POLO', 1, 'AR970', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampa ripartizione spese', 'Attivita POLO', 1, 'AZ100', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampa elenco opere in rilegatura', 'Attivita POLO', 1, 'AZ200', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Operazioni su ordini', 'Attivita POLO', 1, 'AZ800', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampa report tempi di lavorazione/accessionamento', 'Attivita POLO', 1, 'AZ900', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Elaborazioni differite: stato delle richieste', 'Attivita POLO', NULL, 'B0000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione Documento Fisico', 'Attivita POLO', 1, 'C0000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Sezioni di collocazione', 'Attivita POLO', 1, 'C1000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Serie inventariali', 'Attivita POLO', 1, 'C2000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampa registro topografico', 'Attivita POLO', 1, 'C3000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Esame collocazioni', 'Attivita POLO', 1, 'C4000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione inventari e collocazione', 'Attivita POLO', 1, 'C5000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampa schede catalografiche', 'Attivita POLO', 1, 'C5020', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampa registro di conservazione', 'Attivita POLO', 1, 'C6000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Spostamento collocazioni', 'Attivita POLO', 1, 'C7000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Codici di provenienza', 'Attivita POLO', 1, 'C9100', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Dismissione inventari', 'Attivita POLO', 1, 'C9200', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Configurazione', 'Attivita POLO', NULL, 'C9300', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Possessori', 'Attivita POLO', 1, 'C9400', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Modelli etichette', 'Attivita POLO', 1, 'CC000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampa etichette', 'Attivita POLO', 1, 'CD000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Aggiornamento disponibilità', 'Attivita POLO', 1, 'CE000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Amministrazione sistema', 'Attivita di amministrazione', 0, 'F0000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Anagrafica biblioteca', 'Attivita di amministrazione', 1, 'FB000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Anagrafica biblioteca - Gestione anagrafica', 'Attivita di amministrazione', 1, 'FBGA0', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Default biblioteca', 'Attivita POLO', 1, 'FBGD0', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Anagrafica biblioteca - Gestione abilitazioni', 'Attivita di amministrazione', 1, 'FBGF0', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Anagrafica biblioteca - Interrogazione anagrafica', 'Attivita di amministrazione', 1, 'FBIA0', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Anagrafica biblioteca - Interrogazione abilitazioni (Funzioni)', 'Attivita di amministrazione', 1, 'FBIF0', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Codici di validazione', 'Attivita di amministrazione', 1, 'FCV00', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Codici di validazione - Gestione', 'Attivita di amministrazione', 1, 'FCVG0', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Codici di validazione - Interrogazione', 'Attivita di amministrazione', 1, 'FCVI0', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Profilazione polo', 'Attivita di amministrazione', 1, 'FP000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Anagrafica utente', 'Attivita di amministrazione', 1, 'FU000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Anagrafica utente - Gestione anagrafica', 'Attivita di amministrazione', 1, 'FUGA0', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Default utente', 'Attivita di amministrazione', 1, 'FUGD0', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Anagrafica utente - Gestione abilitazioni', 'Attivita di amministrazione', 1, 'FUGF0', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Anagrafica utente - Interrogazione anagrafica', 'Attivita di amministrazione', 1, 'FUIA0', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Anagrafica utente - Interrogazione abilitazioni (Funzioni)', 'Attivita di amministrazione', 1, 'FUIF0', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1032, 'S', 'Allineamenti', 'Attivita SBNMARC', 0, 'IA000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1033, 'S', 'Chiedi allinea documenti', 'Attivita SBNMARC', 1, 'IA001', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1035, 'S', 'Comunica allineati', 'Attivita SBNMARC', 1, 'IA003', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Cattura massiva', 'Attivita POLO', NULL, 'IA004', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Allineamento repertori', 'Attivita POLO', NULL, 'IA005', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Localizzazione massiva', 'Attivita POLO', 1, 'IA006', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1001, 'S', 'Cerca', 'Attivita SBNMARC', 0, 'IC000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1002, 'S', 'Cerca documento', 'Attivita SBNMARC', 1, 'IC001', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1003, 'S', 'Cerca elemento di authority', 'Attivita SBNMARC', 1, 'IC002', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1004, 'S', 'Cerca proposte di correzione', 'Attivita SBNMARC', 1, 'IC003', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1005, 'S', 'Cerca localizzazioni di posseduto', 'Attivita SBNMARC', 1, 'IC004', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1006, 'S', 'Cerca localizzazioni per gestione', 'Attivita SBNMARC', 1, 'IC005', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1015, 'S', 'Crea', 'Attivita SBNMARC', 0, 'ICR00', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1016, 'S', 'Crea documento', 'Attivita SBNMARC', 1, 'ICR01', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1017, 'S', 'Crea elemento di authority', 'Attivita SBNMARC', 1, 'ICR02', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1018, 'S', 'Crea proposta di correzione', 'Attivita SBNMARC', 1, 'ICR03', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1011, 'S', 'Delocalizza', 'Attivita SBNMARC', 0, 'ID000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1012, 'S', 'Delocalizza per posseduto', 'Attivita SBNMARC', 1, 'ID001', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1014, 'S', 'Delocalizza per gestione', 'Attivita SBNMARC', 1, 'ID003', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1039, 'S', 'Esporta', 'Attivita SBNMARC', 0, 'IE000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1040, 'S', 'Esporta documenti', 'Attivita SBNMARC', 1, 'IE001', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1041, 'S', 'Esporta voci di Autorità', 'Attivita SBNMARC', 1, 'IE002', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (NULL, 'N', 'Estrai lista identificativi documento', 'Attivita POLO', 1, 'IE003', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1027, 'S', 'Fonde Elementi di authority', 'Attivita SBNMARC', 0, 'IF000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1202, 'S', 'Fonde Soggetto per tutte le biblioteche', 'Attivita SBNMARC per Monitoraggio', 1, 'IF001', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1269, 'S', 'Fonde Titolo Uniforme Musicale', 'Attivita SBNMARC per Monitoraggio', 1, 'IF002', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1268, 'S', 'Fonde Titolo Uniforme', 'Attivita SBNMARC per Monitoraggio', 1, 'IF003', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1270, 'S', 'Fonde Autore', 'Attivita SBNMARC per Monitoraggio', 1, 'IF004', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1274, 'S', 'Fonde Luogo', 'Attivita SBNMARC per Monitoraggio', 1, 'IF005', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1273, 'S', 'Fonde Classe', 'Attivita SBNMARC per Monitoraggio', 1, 'IF006', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1272, 'S', 'Fonde Soggetto', 'Attivita SBNMARC per Monitoraggio', 1, 'IF007', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1271, 'S', 'Fonde Marca', 'Attivita SBNMARC per Monitoraggio', 1, 'IF008', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1300, 'S', 'Fonde Thesauro', 'Attivita SBNMARC POLO', 1, 'IF009', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Fusione massiva', 'Attivita POLO', 1, 'IF010', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1036, 'S', 'Importa', 'Attivita SBNMARC', 0, 'II000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1037, 'S', 'Importa documenti', 'Attivita SBNMARC', 1, 'II001', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1038, 'S', 'Importa elementi di authority', 'Attivita SBNMARC', 1, 'II002', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Importa file liste per confronto', 'Attivita POLO', 1, 'II100', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1007, 'S', 'Localizza', 'Attivita SBNMARC', 0, 'IL000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1008, 'S', 'Localizza per posseduto', 'Attivita SBNMARC', 1, 'IL001', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1009, 'S', 'Localizza per gestione', 'Attivita SBNMARC', 1, 'IL002', NULL);


INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1019, 'S', 'Modifica', 'Attivita SBNMARC', 0, 'IM000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1020, 'S', 'Modifica tipo materiale documento', 'Attivita SBNMARC', 1, 'IM001', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1022, 'S', 'Modifica natura titolo di accesso/titolo uniforme', 'Attivita SBNMARC', 1, 'IM003', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1023, 'S', 'Modifica documento', 'Attivita SBNMARC', 1, 'IM004', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1024, 'S', 'Fonde documenti', 'Attivita SBNMARC', 1, 'IM005', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1025, 'S', 'Cancella documento', 'Attivita SBNMARC', 1, 'IM006', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1200, 'S', 'Fonde documenti di raggruppamento', 'Attivita SBNMARC', 1, 'IM007', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1201, 'S', 'Fonde documenti di raggruppamento con link', 'Attivita SBNMARC', 1, 'IM008', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Modifica localizzazione per gestione', 'Attivita POLO', 1, 'IM011', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1026, 'S', 'Modifica elemento di authority', 'Attivita SBNMARC', 0, 'IMA00', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1028, 'S', 'Cancella elemento di authority', 'Attivita SBNMARC', 1, 'IMA01', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1267, 'S', 'Modifica Luogo', 'Attivita SBNMARC per Monitoraggio', 1, 'IMA02', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1262, 'S', 'Modifica Titolo Uniforme Musicale', 'Attivita SBNMARC per Monitoraggio', 1, 'IMA03', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1263, 'S', 'Modifica Autore', 'Attivita SBNMARC per Monitoraggio', 1, 'IMA04', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1264, 'S', 'Modifica Marca', 'Attivita SBNMARC per Monitoraggio', 1, 'IMA05', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1266, 'S', 'Modifica Classe', 'Attivita SBNMARC per Monitoraggio', 1, 'IMA06', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1265, 'S', 'Modifica Soggetto', 'Attivita SBNMARC per Monitoraggio', 1, 'IMA07', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1261, 'S', 'Modifica Titolo Uniforme', 'Attivita SBNMARC per Monitoraggio', 1, 'IMA08', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1029, 'S', 'Scambio forma Autore', 'Attivita SBNMARC', 1, 'IMA09', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1301, 'S', 'Modifica Thesauro', 'Attivita SBNMARC POLO', 1, 'IMA10', NULL);


INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Servizi', 'Attivita POLO', NULL, 'L0000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione diritti utente', 'Attivita POLO', 1, 'LD001', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Modifica autorizzazioni ai servizi', 'Attivita POLO', 1, 'LD002', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampa utenti lettori', 'Attivita POLO', 1, 'LR940', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Importa utenti', 'Attivita POLO', 1, 'LRI01', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Archiviazione movimenti locali', 'Attivita POLO', 1, 'LS331', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampa servizi correnti', 'Attivita POLO', 1, 'LS334', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampa storico servizi', 'Attivita POLO', 1, 'LS335', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Solleciti', 'Attivita POLO', 1, 'LS337', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Periodici', 'Attivita POLO', 1, 'P0000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Descrizione fascicoli', 'Attivita POLO', 1, 'P0001', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione amministrativa', 'Attivita POLO', 1, 'P0002', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampa fascicoli', 'Attivita POLO', 1, 'PZ001', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Cancellazione descrittori non utilizzati', 'Attivita POLO', 1, 'SSCDI', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Cancellazione soggetti non utilizzati', 'Attivita POLO', 1, 'SSCSI', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampe', 'Attivita POLO', NULL, 'Z0000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampa Cataloghi', 'Attivita POLO', 1, 'ZG200', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampa Soggettario', 'Attivita POLO', 1, 'ZS431', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampa Descrittori', 'Attivita POLO', 1, 'ZS432', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Stampa fattura', 'Atiivita SBNMARC', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1247, 'N', 'Cerca Classi', 'Attivita SBNMARC per Monitoraggio', 0, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1248, 'N', 'Cerca Luoghi', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1249, 'N', 'Cerca Titoli Uniformi', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1250, 'N', 'Cerca Titoli Uniformi Musicali', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1251, 'N', 'Cerca Autori', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1252, 'N', 'Cerca Marche', 'Attivita SBNMARC per Monitoraggio', 0, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1253, 'N', 'Cerca Soggetti', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1254, 'N', 'Crea Titolo Uniforme', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1255, 'N', 'Crea Titolo Uniforme Musicale', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1256, 'N', 'Crea Autore', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1257, 'N', 'Crea Marca', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1258, 'N', 'Crea Soggetto', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1259, 'N', 'Crea Classe', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1260, 'N', 'Crea Luogo', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1275, 'N', 'Cancella Titolo Uniforme', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1276, 'N', 'Cancella Titolo Uniforme Musicale', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1277, 'N', 'Cancella Autore', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1278, 'N', 'Cancella Marca', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1279, 'N', 'Cancella Soggetto', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1280, 'N', 'Cancella Classe', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1281, 'N', 'Cancella Luogo', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1282, 'N', 'Cancella Descrittore', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1283, 'N', 'Cerca Descrittore', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (1284, 'N', 'Crea Descrittore', 'Attivita SBNMARC per Monitoraggio', 1, NULL, NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Configurazione servizi - Interrogazione', 'Attivita POLO', 1, 'LC001', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Configurazione servizi - Gestione', 'Attivita POLO', 1, 'LC002', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Erogazione', 'Attivita POLO', 1, 'LE001', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Gestione anagrafica utenti', 'Attivita POLO', 1, 'LR001', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Statistiche', 'Attivita POLO', 1, 'T0001', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Strumenti di controllo sul patrimonio', 'Attivita POLO', 1, 'CS000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Modulo prelievo', 'Attivita POLO', 1, 'CMP00', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (NULL, 'N', 'Stampa Shipping Manifest', 'Attivita POLO', 1, 'AZ700', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Monitoraggio', 'Attivita POLO', 1, 'T0100', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'N', 'Importa URI copia digitale', 'Attivita POLO', 1, 'II150', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'S', 'Produzione editoriale', 'Attivita POLO', 0, 'IR000', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (NULL, 'S', 'Stampa titoli per editore', 'Attivita POLO', 1, 'IRZ01', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("id_tipo_attivita", "tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES (0, 'S', 'Gestione editori', 'Attivita POLO', 1, 'IR001', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES ('S', 'Salvataggio file XML Indice per allineamento', 'Attivita SBNMARC', 1, 'IA007', NULL);

INSERT INTO "sbnweb"."tbf_attivita_sbnmarc" ("tp_attivita", "ds_attivita", "nota_tipo_attivita", "livello", "codice_attivita", "id_modulo")
VALUES ('S', 'Cancella prenotazioni non fruite', 'Attivita SBNMARC', 1, 'LS340', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "fl_titolo", "fl_autore", "fl_marca", "fl_luogo", "fl_soggetto", "fl_classe", "liv_autorita_da", "liv_autorita_a", "gestione_in_indice", "gestione_in_polo", "natura_a", "natura_b", "natura_c", "natura_d", "natura_m", "natura_n", "natura_p", "natura_s", "natura_t", "natura_w", "natura_r", "natura_x", "forma_accettata", "forma_rinvio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('LS340', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'LS340'), 'N', 409008, 'L0000', 'N', 'N', 'L0000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "fl_titolo", "fl_autore", "fl_marca", "fl_luogo", "fl_soggetto", "fl_classe", "liv_autorita_da", "liv_autorita_a", "gestione_in_indice", "gestione_in_polo", "natura_a", "natura_b", "natura_c", "natura_d", "natura_m", "natura_n", "natura_p", "natura_s", "natura_t", "natura_w", "natura_r", "natura_x", "forma_accettata", "forma_rinvio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IA007', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IA007'), 'N', 110060, 'IA000', 'N', 'N', 'L0000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A0000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A0000'), 'N', 200000, NULL, 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A1100', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A1100'), 'N', 200030, 'A0000', 'N', 'N', 'A1000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A1200', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A1200'), 'N', 200040, 'A0000', 'N', 'N', 'A1000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A1210', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A1210'), 'N', 200050, 'A0000', 'N', 'N', 'A1200', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A1220', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A1220'), 'N', 200060, 'A0000', 'N', 'N', 'A1200', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A1230', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A1230'), 'N', 200070, 'A0000', 'N', 'N', 'A1200', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A1240', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A1240'), 'N', 200080, 'A0000', 'N', 'N', 'A1200', 'IniDB', now(), 'IniDB', now(), 'N', NULL);


INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A1400', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A1400'), 'N', 200100, 'A0000', 'N', 'N', 'A1000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);


INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A2100', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A2100'), 'N', 200010, 'A0000', 'N', 'N', 'A2000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A2200', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A2200'), 'N', 200020, 'A0000', 'N', 'N', 'A2000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);


INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A3100', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A3100'), 'N', 200240, 'A0000', 'N', 'N', 'A3000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A3200', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A3200'), 'N', 200250, 'A0000', 'N', 'N', 'A3000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);


INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A4100', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A4100'), 'N', 200130, 'A0000', 'N', 'N', 'A4000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A4200', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A4200'), 'N', 200140, 'A0000', 'N', 'N', 'A4000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);


INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A5100', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A5100'), 'N', 200150, 'A0000', 'N', 'N', 'A5000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A5200', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A5200'), 'N', 200160, 'A0000', 'N', 'N', 'A5000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);


INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A6100', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A6100'), 'N', 200200, 'A0000', 'N', 'N', 'A6000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A6200', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A6200'), 'N', 200210, 'A0000', 'N', 'N', 'A6000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A8100', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A8100'), 'N', 200260, 'A0000', 'N', 'N', 'A8000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A8200', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A8200'), 'N', 200270, 'A0000', 'N', 'N', 'A8000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A9100', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A9100'), 'N', 200170, 'A0000', 'N', 'N', 'A9000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A9200', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A9200'), 'N', 200180, 'A0000', 'N', 'N', 'A9000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('A9300', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'A9300'), 'N', 200190, 'A0000', 'N', 'N', 'A9000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('AA100', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'AA100'), 'N', 200280, 'A0000', 'N', 'N', 'AA000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('AA200', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'AA200'), 'N', 200290, 'A0000', 'N', 'N', 'AA000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('AB100', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'AB100'), 'N', 200300, 'A0000', 'N', 'N', 'AB000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('AB200', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'AB200'), 'N', 200310, 'A0000', 'N', 'N', 'AB000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('AC100', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'AC100'), 'N', 314000, 'C0000', 'N', 'N', 'C0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('AC200', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'AC200'), 'N', 200330, 'A0000', 'N', 'N', 'A0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('AC300', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'AC300'), 'N', 200340, 'A0000', 'N', 'N', 'A0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('AE000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'AE000'), 'N', 200320, 'A0000', 'N', 'N', 'A0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('AF100', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'AF100'), 'N', 200110, 'A0000', 'N', 'N', 'AF000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('AF200', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'AF200'), 'N', 200120, 'A0000', 'N', 'N', 'AF000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('AR110', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'AR110'), 'N', 200220, 'A0000', 'N', 'N', 'A0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('AR120', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'AR120'), 'N', 200230, 'A0000', 'N', 'N', 'A0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('AR960', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'AR960'), 'N', 200350, 'A0000', 'N', 'N', 'A0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('AR970', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'AR970'), 'N', 200360, 'A0000', 'N', 'N', 'A0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('AZ100', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'AZ100'), 'N', 200380, 'A0000', 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('AZ200', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'AZ200'), 'N', 200370, 'A0000', 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('AZ800', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'AZ800'), 'N', 200090, 'A0000', 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('AZ900', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'AZ900'), 'N', 200390, 'A0000', 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('B0000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'B0000'), 'N', 500000, NULL, 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('C0000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'C0000'), 'N', 300000, NULL, 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('C1000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'C1000'), 'N', 303000, 'C0000', 'N', 'N', 'C0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('C2000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'C2000'), 'N', 304000, 'C0000', 'N', 'N', 'C0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('C3000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'C3000'), 'N', 315000, 'C0000', 'N', 'N', 'C0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('C4000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'C4000'), 'N', 301000, 'C0000', 'N', 'N', 'C0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('C5000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'C5000'), 'N', 302000, 'C0000', 'N', 'N', 'C0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('C5020', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'C5020'), 'N', 317000, 'C0000', 'N', 'N', 'C0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('C6000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'C6000'), 'N', 316000, 'Z0000', 'N', 'N', 'C0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('C7000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'C7000'), 'N', 311000, 'C0000', 'N', 'N', 'C0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('C9100', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'C9100'), 'N', 305000, 'C0000', 'N', 'N', 'C0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('C9200', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'C9200'), 'N', 310000, 'C0000', 'N', 'N', 'C0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('C9300', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'C9300'), 'N', 308000, 'C0000', 'N', 'N', 'C0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('C9400', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'C9400'), 'N', 306000, 'C0000', 'N', 'N', 'C0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('CC000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'CC000'), 'N', 307000, 'C0000', 'N', 'N', 'C0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('CD000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'CD000'), 'N', 313000, 'C0000', 'N', 'N', 'C0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('CE000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'CE000'), 'N', 309000, 'C0000', 'N', 'N', 'C0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('F0000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'F0000'), 'N', 600000, NULL, 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('FB000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'FB000'), 'N', 602000, 'F0000', 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('FBGA0', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'FBGA0'), 'N', 602030, 'F0000', 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('FBGD0', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'FBGD0'), 'N', 602050, 'F0000', 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('FBGF0', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'FBGF0'), 'N', 602040, 'F0000', 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('FBIA0', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'FBIA0'), 'N', 602010, 'F0000', 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('FBIF0', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'FBIF0'), 'N', 602020, 'F0000', 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('FCV00', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'FCV00'), 'N', 604010, 'F0000', 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('FCVG0', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'FCVG0'), 'N', 604030, 'F0000', 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('FCVI0', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'FCVI0'), 'N', 604020, 'F0000', 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('FP000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'FP000'), 'N', 601000, 'F0000', 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('FU000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'FU000'), 'N', 603000, 'F0000', 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('FUGA0', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'FUGA0'), 'N', 603030, 'F0000', 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('FUGD0', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'FUGD0'), 'N', 603050, 'F0000', 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('FUGF0', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'FUGF0'), 'N', 603040, 'F0000', 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('FUIA0', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'FUIA0'), 'N', 603010, 'F0000', 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('FUIF0', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'FUIF0'), 'N', 603020, 'F0000', 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IA000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IA000'), 'N', 110000, NULL, 'N', 'N', 'I0000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.ALLINEAMENTI_1032');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IA001', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IA001'), 'N', 110010, 'IA000', 'N', 'N', 'IA000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.CHIEDI_ALLINEA_DOCUMENTI_1033');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IA003', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IA003'), 'N', 110030, 'IA000', 'N', 'N', 'IA000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.COMUNICA_ALLINEATI_1035');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IA004', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IA004'), 'N', 110040, 'IA000', 'N', 'N', 'IA004', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IA005', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IA005'), 'N', 110050, NULL, 'N', 'N', 'IA005', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IA006', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IA006'), 'N', 110060, 'IA000', 'N', 'N', 'IA006', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IC000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IC000'), 'N', 101000, NULL, 'N', 'N', 'I0000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.CERCA_1001');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IC001', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IC001'), 'N', 101010, 'IC000', 'N', 'N', 'IC000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.CERCA_DOCUMENTO_1002');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IC002', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IC002'), 'N', 101020, 'IC000', 'N', 'N', 'IC000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.CERCA_ELEMENTO_AUTHORITY_1003');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IC003', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IC003'), 'N', 101030, 'IC000', 'N', 'N', 'IC000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.CERCA_PROPOSTE_DI_CORREZIONE_1004');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IC004', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IC004'), 'N', 101040, 'IC000', 'N', 'N', 'IC000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.CERCA_LOCALIZZAZIONI_DI_POSSEDUTO_1005');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IC005', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IC005'), 'N', 101050, 'IC000', 'N', 'N', 'IC000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.CERCA_LOCALIZZAZIONI_PER_GESTIONE_1006');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('ICR00', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'ICR00'), 'N', 102000, NULL, 'N', 'N', 'I0000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.CREA_1015');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('ICR01', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'ICR01'), 'N', 102010, 'ICR00', 'N', 'N', 'ICR00', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.CREA_DOCUMENTO_1016');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('ICR02', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'ICR02'), 'N', 102020, 'ICR00', 'N', 'N', 'ICR00', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.CREA_ELEMENTO_DI_AUTHORITY_1017');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('ICR03', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'ICR03'), 'N', 102030, 'ICR00', 'N', 'N', 'ICR00', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.CREA_PROPOSTA_CORREZIONE_1018');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('ID000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'ID000'), 'N', 107000, NULL, 'N', 'N', 'I0000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.DELOCALIZZA_1011');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('ID001', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'ID001'), 'N', 107010, 'ID000', 'N', 'N', 'ID000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.DELOCALIZZA_PER_POSSEDUTO_1012');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('ID003', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'ID003'), 'N', 107030, 'ID000', 'N', 'N', 'ID000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.DELOCALIZZA_PER_GESTIONE_1014');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IE000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IE000'), 'N', 108000, NULL, 'N', 'N', 'I0000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.ESPORTA_1039');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IE001', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IE001'), 'N', 108010, 'IE000', 'N', 'N', 'IE000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.ESPORTA_DOCUMENTI_1040');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IE002', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IE002'), 'N', 108020, 'IE000', 'N', 'N', 'IE000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.ESPORTA_ELEMENTI_DI_AUTHORITY_1041');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IE003', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IE003'), 'N', 108030, 'IE000', 'N', 'N', 'IE000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IF000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IF000'), 'N', 105000, NULL, 'N', 'N', 'I0000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.FONDE_ELEMENTI_DI_AUTHORITY_1027');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IF001', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IF001'), 'N', 105070, 'IF000', 'N', 'N', 'IF000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.FONDE_ELEMENTI_DI_AUTHORITY_CON_LINK_1202');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IF002', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IF002'), 'N', 105030, 'IF000', 'N', 'N', 'IF000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.FONDE_TITOLO_UNIFORME_MUSICA_1269');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IF003', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IF003'), 'N', 105020, 'IF000', 'N', 'N', 'IF000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.FONDE_TITOLO_UNIFORME_1268');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IF004', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IF004'), 'N', 105010, 'IF000', 'N', 'N', 'IF000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.FONDE_AUTORE_1270');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IF005', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IF005'), 'N', 105050, 'IF000', 'N', 'N', 'IF000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.FONDE_LUOGO_1274');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IF006', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IF006'), 'N', 105080, 'IF000', 'N', 'N', 'IF000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.FONDE_CLASSE_1273');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IF007', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IF007'), 'N', 105060, 'IF000', 'N', 'N', 'IF000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.FONDE_SOGGETTO_1272');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IF008', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IF008'), 'N', 105040, 'IF000', 'N', 'N', 'IF000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.FONDE_MARCA_1271');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IF009', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IF009'), 'N', 105090, 'IF000', 'N', 'N', 'IF000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.FONDE_THESAURO_1300');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IF010', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IF010'), 'N', 103110, 'IM000', 'N', 'N', 'IF000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('II000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'II000'), 'N', 109000, NULL, 'N', 'N', 'I0000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.IMPORTA_1036');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('II001', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'II001'), 'N', 109010, 'II000', 'N', 'N', 'II000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.IMPORTA_DOCUMENTI_1037');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('II002', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'II002'), 'N', 109020, 'II000', 'N', 'N', 'II000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.IMPORTA_ELEMENTI_DI_AUTHORITY_1038');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('II100', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'II100'), 'N', 109100, 'II000', 'N', 'N', 'II000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IL000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IL000'), 'N', 106000, NULL, 'N', 'N', 'I0000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.LOCALIZZA_1007');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IL001', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IL001'), 'N', 106010, 'IL000', 'N', 'N', 'IL000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.LOCALIZZA_PER_POSSEDUTO_1008');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IL002', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IL002'), 'N', 106020, 'IL000', 'N', 'N', 'IL000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.LOCALIZZA_PER_GESTIONE_1009');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IM000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IM000'), 'N', 103000, NULL, 'N', 'N', 'I0000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.MODIFICA_1019');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IM001', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IM001'), 'N', 103010, 'IM000', 'N', 'N', 'IM000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.MODIFICA_TIPO_MATERIALE_DOCUMENTO_1020');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IM003', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IM003'), 'N', 103030, 'IM000', 'N', 'N', 'IM000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.MODIFICA_NATURA_TITOLO_DI_ACCESSO_TITOLO_UNIFORME_1022');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IM004', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IM004'), 'N', 103040, 'IM000', 'N', 'N', 'IM000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.MODIFICA_DOCUMENTO_1023');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IM005', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IM005'), 'N', 103050, 'IM000', 'N', 'N', 'IM000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.FONDE_DOCUMENTI_1024');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IM006', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IM006'), 'N', 103060, 'IM000', 'N', 'N', 'IM000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.CANCELLA_DOCUMENTO_1025');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IM007', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IM007'), 'N', 103070, 'IM000', 'N', 'N', 'IM000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.FONDE_DOCUMENTI_DI_RAGGRUPPAMENTO_1200');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IM008', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IM008'), 'N', 103080, 'IM000', 'N', 'N', 'IM000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.FONDE_DOCUMENTI_DI_RAGGRUPPAMENTO_CON_LINK_1201');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IM011', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IM011'), 'N', 103100, 'IM000', 'N', 'N', 'IM011', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IMA00', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IMA00'), 'N', 104000, NULL, 'N', 'N', 'I0000', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.MODIFICA_ELEMENTO_DI_AUTHORITY_1026');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IMA01', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IMA01'), 'N', 104010, 'IMA00', 'N', 'N', 'IMA00', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.CANCELLA_ELEMENTO_AUTHORITY_1028');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IMA02', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IMA02'), 'N', 104020, 'IMA00', 'N', 'N', 'IMA00', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.MODIFICA_LUOGO_1267');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IMA03', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IMA03'), 'N', 104030, 'IMA00', 'N', 'N', 'IMA00', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.MODIFICA_TITOLO_UNIFORME_MUSICA_1262');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IMA04', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IMA04'), 'N', 104040, 'IMA00', 'N', 'N', 'IMA00', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.MODIFICA_AUTORE_1263');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IMA05', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IMA05'), 'N', 104050, 'IMA00', 'N', 'N', 'IMA00', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.MODIFICA_MARCA_1264');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IMA06', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IMA06'), 'N', 104060, 'IMA00', 'N', 'N', 'IMA00', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.MODIFICA_CLASSE_1266');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IMA07', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IMA07'), 'N', 104070, 'IMA00', 'N', 'N', 'IMA00', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.MODIFICA_SOGGETTO_1265');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IMA08', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IMA08'), 'N', 104080, 'IMA00', 'N', 'N', 'IMA00', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.MODIFICA_TITOLO_UNIFORME_1261');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IMA09', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IMA09'), 'N', 104090, 'IMA00', 'N', 'N', 'IMA00', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.SCAMBIO_FORMA_1029');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IMA10', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IMA10'), 'N', 104100, 'IMA00', 'N', 'N', 'IMA00', 'IniDB', now(), 'IniDB', now(), 'N', 'it.finsiel.sbn.util.CodiciAttivita.MODIFICA_THESAURO_1301');

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('L0000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'L0000'), 'N', 400000, NULL, 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('LD001', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'LD001'), 'N', 401001, 'L0000', 'N', 'N', 'L0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('LD002', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'LD002'), 'N', 401002, 'L0000', 'N', 'N', 'L0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('LR940', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'LR940'), 'N', 402001, 'L0000', 'N', 'N', 'L0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('LRI01', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'LRI01'), 'N', 409007, 'L0000', 'N', 'N', 'II000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('LS334', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'LS334'), 'N', 409002, 'L0000', 'N', 'N', 'L0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('LS335', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'LS335'), 'N', 409003, 'L0000', 'N', 'N', 'LS335', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('LS337', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'LS337'), 'N', 409007, 'L0000', 'N', 'N', 'L0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('P0000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'P0000'), 'N', 350000, NULL, 'N', 'N', 'P0000', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('P0001', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'P0001'), 'N', 350001, 'P0000', 'N', 'N', 'P0001', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('P0002', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'P0002'), 'N', 350002, 'P0000', 'N', 'N', 'P0002', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('PZ001', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'PZ001'), 'N', 350003, 'P0000', 'N', 'N', 'P0002', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('SSCDI', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'SSCDI'), 'N', 104102, 'IMA00', 'N', 'N', 'SSCDI', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('SSCSI', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'SSCSI'), 'N', 104101, 'IMA00', 'N', 'N', 'SSCSI', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('Z0000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'Z0000'), 'N', 150000, NULL, 'N', 'N', NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('ZG200', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'ZG200'), 'N', 150001, 'Z0000', 'N', 'N', 'ZG200', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('ZS431', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'ZS431'), 'N', 150002, 'Z0000', 'N', 'N', 'ZS431', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('ZS432', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'ZS432'), 'N', 150003, 'Z0000', 'N', 'N', 'ZS432', 'inidb', now(), 'inidb', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('LC001', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'LC001'), 'N', 400101, 'L0000', 'N', 'N', 'LC001', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('LC002', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'LC002'), 'N', 400102, 'L0000', 'N', 'N', 'LC002', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('LE001', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'LE001'), 'N', 400201, 'L0000', 'N', 'N', 'LE001', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('LR001', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'LR001'), 'N', 400900, 'L0000', 'N', 'N', 'LR001', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('T0001', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'T0001'), 'N', 550000, null, 'N', 'N', 'T0001', 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('CS000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'CS000'), 'N', 312000, 'C0000', 'N', 'N', 'C0000', 'inidb', now(), 'inidb', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('CMP00', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'CMP00'), 'N', 312000, 'C0000', 'N', 'N', 'C0000', 'inidb', now(), 'inidb', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('AZ700', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'AZ700'), 'N', 200400, 'A0000', 'N', 'N', 'IE000', 'inidb', now(), 'inidb', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "fl_titolo", "fl_autore", "fl_marca", "fl_luogo", "fl_soggetto", "fl_classe", "liv_autorita_da", "liv_autorita_a", "gestione_in_indice", "gestione_in_polo", "natura_a", "natura_b", "natura_c", "natura_d", "natura_m", "natura_n", "natura_p", "natura_s", "natura_t", "natura_w", "natura_r", "natura_x", "forma_accettata", "forma_rinvio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('T0100', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'T0100'), 'N', 550013, NULL, 'N', 'N', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "fl_titolo", "fl_autore", "fl_marca", "fl_luogo", "fl_soggetto", "fl_classe", "liv_autorita_da", "liv_autorita_a", "gestione_in_indice", "gestione_in_polo", "natura_a", "natura_b", "natura_c", "natura_d", "natura_m", "natura_n", "natura_p", "natura_s", "natura_t", "natura_w", "natura_r", "natura_x", "forma_accettata", "forma_rinvio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('LS331', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'LS331'), 'N', 409001, 'L0000', 'N', 'N', 'L0000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "fl_titolo", "fl_autore", "fl_marca", "fl_luogo", "fl_soggetto", "fl_classe", "liv_autorita_da", "liv_autorita_a", "gestione_in_indice", "gestione_in_polo", "natura_a", "natura_b", "natura_c", "natura_d", "natura_m", "natura_n", "natura_p", "natura_s", "natura_t", "natura_w", "natura_r", "natura_x", "forma_accettata", "forma_rinvio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('II150', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'II150'), 'N', 109200, 'II000', 'N', 'N', 'II150', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'IniDB', now(), 'IniDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "fl_titolo", "fl_autore", "fl_marca", "fl_luogo", "fl_soggetto", "fl_classe", "liv_autorita_da", "liv_autorita_a", "gestione_in_indice", "gestione_in_polo", "natura_a", "natura_b", "natura_c", "natura_d", "natura_m", "natura_n", "natura_p", "natura_s", "natura_t", "natura_w", "natura_r", "natura_x", "forma_accettata", "forma_rinvio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IR000', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IR000'), 'N', 575000, NULL, 'N', 'N', 'IR000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'INIDB', now(), 'INIDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "fl_titolo", "fl_autore", "fl_marca", "fl_luogo", "fl_soggetto", "fl_classe", "liv_autorita_da", "liv_autorita_a", "gestione_in_indice", "gestione_in_polo", "natura_a", "natura_b", "natura_c", "natura_d", "natura_m", "natura_n", "natura_p", "natura_s", "natura_t", "natura_w", "natura_r", "natura_x", "forma_accettata", "forma_rinvio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IR001', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IR001'), 'N', 575100, 'IR000', 'N', 'N', 'IR001', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'INIDB', now(), 'INIDB', now(), 'N', NULL);

INSERT INTO "sbnweb"."tbf_attivita" ("cd_attivita", "id_attivita_sbnmarc", "flg_menu", "prg_ordimanento", "cd_funzione_parent", "fl_auto_abilita_figli", "fl_assegna_a_cds", "url_servizio", "fl_titolo", "fl_autore", "fl_marca", "fl_luogo", "fl_soggetto", "fl_classe", "liv_autorita_da", "liv_autorita_a", "gestione_in_indice", "gestione_in_polo", "natura_a", "natura_b", "natura_c", "natura_d", "natura_m", "natura_n", "natura_p", "natura_s", "natura_t", "natura_w", "natura_r", "natura_x", "forma_accettata", "forma_rinvio", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "classe_java_sbnmarc")
VALUES ('IRZ01', (select _asbnm.id_attivita_sbnmarc from "sbnweb"."tbf_attivita_sbnmarc" _asbnm where _asbnm.codice_attivita = 'IRZ01'), 'N', 575200, 'IR000', 'N', 'N', 'IRZ01', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'INIDB', now(), 'INIDB', now(), 'N', NULL);

COMMIT;
