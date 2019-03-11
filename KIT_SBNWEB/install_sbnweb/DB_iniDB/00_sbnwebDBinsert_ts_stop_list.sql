/* Data for the `sbnweb.ts_stop_list` table  (Records 1 - 500) (totale 1112) */

BEGIN;

DELETE FROM "sbnweb"."ts_stop_list";
----- inizializza sequence id_stop_list-----
ALTER SEQUENCE "sbnweb"."ts_stop_list_id_stop_list_seq"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  RESTART 1
    CACHE 1  NO CYCLE;

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SONATINA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SONATINE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'ITA', 'IL', 'Articoli', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'ITA', 'I', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'ITA', 'LO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'ITA', 'GLI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'ITA', 'L', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'ITA', 'LA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'ITA', 'UN', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'ITA', 'UNA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'ENG', 'THE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'ENG', 'A', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'ENG', 'AN', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'ED', 'editori', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'EDITEUR', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'EDITOR', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'EDITORE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'EDITRICE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'EDIZIONI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'EDITIONS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'PUBLISHER', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'PUBLISHING', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'VERLAG', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'VERLAG HAUS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'VERLEGER', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'HERAUSGEBER', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'CASA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'COMPANY', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'CO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'SOCIETA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'SOC', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'COOP', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'TIP', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'TIPOGRAFIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'PUBLISHED', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'BY', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'DISTRIBUTED', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('E', 'ITA', 'DISTRIBUTORE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('D', 'ITA', 'IL', 'Articoli e preposizioni', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'AGNUS DEI', 'forme musicali', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('V', 'ITA', 'MINISTERO', 'voci autore scartate', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ALLELUIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ANTE EVANGELIUM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ANTIFONA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BENEDICTUS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTICO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CLAUSOLA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'COMMUNIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'COMPIETA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CONDUCTUS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CONFRATTORIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CREDO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DEVOZIONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DOSSOLOGIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ELEVAZIONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GLORIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GRADUALE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'IMPROPERA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INGRESSA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INNO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INTONAZIONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INTROITO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INVITATORIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'KYRIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LAMENTAZIONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LITANIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LITURGIA DELLE ORE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LODI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LUCERNARIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MATTUTINO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MESSA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MESSA DA REQUIEM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OFFERTORIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'POST EVANGELIUM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'POST LECTIONEM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'POSTCOMMUNIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'REQIUEM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RESPONSORIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SALMO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SANCTUS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SEQUENZA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SYMBOLUM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TRATTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TROPO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'UFFICIO DI REQUIEM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'UFFICIO DIVINO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VERSETTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VERSICOLO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VESPRO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ACCLAMAZIONI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ALBE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ARIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ASSOLUZIONI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ANTIFONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'AZIONI SACRE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'AZIONI TEATRALI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BACCANALI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BARCAROLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BATTAGLIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BASSE DANZE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BAGATELLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BALLI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BALLATE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BOLERI CUBANI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BOLERI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BALLETTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CACCE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CAPRICCI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CADENZE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CALATE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTI FUNEBRI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTI GOLIARDICI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CONCERTI GROSSI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CIACCONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CLAUSOLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'COMMEDIE MUSICALI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANONI BIZANTINI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CONGHE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANONI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANZONI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CODICI LITURGICI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CONCERTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'COPLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CORI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CORRENTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CONTRAPPUNTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CORALI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTI CARNASCIALESCHI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CAROLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTI SACRI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CORI DI SOLDATI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTICI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTATE SACRE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTATE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTIGHE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTILENE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CAVATINE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANZONI DI GUERRA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANZONI INFANTILI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANZONETTE LEGGERE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANZONI SPIRITUALI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANZONETTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DANZE MACABRE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DANZE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DEVOZIONI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DIALOGHI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DITIRAMBI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DIVERTIMENTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DRAMMI LITURGICI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DRAMMI LIRICI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DRAMMI PER MUSICA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DRAMMI SACRI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DRAMMI SCOLASTICI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DUETTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ELEGIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ELEVAZIONI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FAVOLE PER MUSICA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FINALI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FIORETTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FLAMINCHI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FANFARE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FOLLIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FURLANE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FROTTOLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FANTASIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FUGHE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GAGLIARDE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GAVOTTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GIGHE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GIUSTINIANE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GLORIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GREGHESCHE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'IMENEI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'IMPLORAZIONI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INNI NAZIONALI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INGRESSI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INNI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INTRODUZIONI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INVENZIONI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INNI PATRIOTTICI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INTERLUDI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INTONAZIONI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INTROITI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INTERMEZZI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INVOCAZIONI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INVITATORI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LAMENTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LAUDE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LITURGIE DELLE ORE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LETTURE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LICENZE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LITANIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LAMENTAZIONI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LUCERNARI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MACCHIETTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MADRIGALI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MASCHERATE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MATTACCINI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MARCE FUNEBRI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MARCE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MARCE MILITARI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MELODRAMMI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MELODRAMMI TRAGICI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MELODIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MESSE PER ORGANO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MELODIE RELIGIOSE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MESSE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MUSICHE DA FILM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MINUETTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MILONGHE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MISTERI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MARCE NUZIALI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MOMENTI MUSICALI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MOTTETTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MARCE PROFESSIONALI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MORESCHE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MUSICHE DI SCENA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'METODI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MOTI PERPETUI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MATTUTINI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MUTANZE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'NENIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'NONETTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'NOTTURNI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'NOVELLETTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ODI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OFFERTORI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPERETTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPERE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPERE RADIOFONICHE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPERE TELEVISIVE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ORATORI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OTTETTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PADOVANE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PANTOMIME', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PAVANE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PROFEZIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PIVE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PRELUDI CORALI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PROLOGHI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'POLI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'POEMI SINFONICI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'POLACCHE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PARODIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PREGHIERE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PRELUDI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PASSAMEZZI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PASSIONI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PASTICCI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PASTORALI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PARTIMENTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PARTITE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PEZZI DI CONCERTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'QUINTETTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'QUADRIGLIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RADE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RAPSODIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RASPE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RONDE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RECITATIVI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RESPONSORI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RISPETTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RICERCARI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RITIRATE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RIVISTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ROMANZE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ROMANESCHE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ROTTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ROMANZE SENZA PAROLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RITORNELLI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RUMBE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SACRE RAPPRESENTAZIONI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SAMBE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SARABANDE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SCENE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SCHERZI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SINFONIE CONCERTANTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SEQUENZE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SERENATE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SESTETTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SETTIMINI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SINFONIETTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SICILIANE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SINFONIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SINFONIE A PROGRAMMA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SALMI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SOLILOQUI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SALSE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SALTARELLI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SALMELLI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SOLFEGGI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SONATE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SARDANE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'STORNELLI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'STRAMBOTTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SALTARELLI TEDESCHI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'STUDI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TANGHI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TARANTELLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TENZONI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TERZETTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TIRANE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TOCCATE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TORNADILLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TORNEI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TROPI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TRATTATI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TREZZE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TRESCHE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TRATTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TUMBE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'UFFICI DIVINI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'UFFICI DA REQUIEM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'UMORESCHE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VARIAZIONI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VERSICOLI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VENEZIANE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VERSETTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VESPRI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VILLANELLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VOCALIZZI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VOLTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ZAMBE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ZOPPE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('S', 'ITA', 'CFI9090', 'elenco poli che possono soggettare: cd_polo+livello di controllo soggetti+livello di controllo classi', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('S', 'ITA', 'MIL8585', 'elenco poli che possono soggettare: cd_polo+livello di controllo soggetti+livello di controllo classi', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('S', 'ITA', 'RAV8080', 'elenco poli che possono soggettare: cd_polo+livello di controllo soggetti+livello di controllo classi', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('S', 'ITA', 'LO17070', 'elenco poli che possono soggettare: cd_polo+livello di controllo soggetti+livello di controllo classi', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('S', 'ITA', 'BVE6060', 'elenco poli che possono soggettare: cd_polo+livello di controllo soggetti+livello di controllo classi', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('S', 'ITA', 'VEA5050', 'elenco poli che possono soggettare: cd_polo+livello di controllo soggetti+livello di controllo classi', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('S', 'ITA', 'PUV4040', 'elenco poli che possono soggettare: cd_polo+livello di controllo soggetti+livello di controllo classi', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('S', 'ITA', 'UFI3030', 'elenco poli che possono soggettare: cd_polo+livello di controllo soggetti+livello di controllo classi', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('S', 'ITA', 'UBO2525', 'elenco poli che possono soggettare: cd_polo+livello di controllo soggetti+livello di controllo classi', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('S', 'ITA', 'NAP2020', 'elenco poli che possono soggettare: cd_polo+livello di controllo soggetti+livello di controllo classi', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('S', 'ITA', 'IEI1010', 'elenco poli che possono soggettare: cd_polo+livello di controllo soggetti+livello di controllo classi', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'AMENER', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ANAGRAMMA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ARABESQUE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ARIETTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ARIOSO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'APPLAUSO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'AUBADE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'AURRESKU', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'AZIONE COREOGRAFICA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'AZIONE GIOCOSA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'AZIONE MUSICALE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'AZIONE TEATRALE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BACCANALE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BADINAGE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'AZIONE SACRA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BAIAO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BANKELGESANG', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BALLAD OPERA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BALLO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BARCAROLA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BARRIERA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BASSA DANZA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BATTAGLIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BARFORM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BEGUINE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BERCEUSE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BERGAMASCA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BERGERETTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BICINIUM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BLACK BOTTOM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BLUETTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BERGREICHEN', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BOLERO CUBANO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BOOGIE-WOOGIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BOP', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BOSTON', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BOURREE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BRANLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BRUNETTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BUGAKU', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BOUTADE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CABALETTA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CACCIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CACHUCHA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CADENZA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CAKEWALK', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CALYPSO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANARIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANCAN', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CALATA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANONE BIZANTINO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANSO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTATA SACRA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTIGA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTO CARNASCIALESCO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTO FUNEBRE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTILENA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTUS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTUS PLANUS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANZONA SPIRITUALE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANZONE ALLA FRANCESE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANZONE DI GUERRA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANZONE LEGGERA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANZONETTA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CAPRICCIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANZONE INFANTILE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CARMAGNOLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CARMEN', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CAROLA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CAROSELLO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CASSAZIONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CAVATA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CAVATINA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CENTONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CATCH', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CHANSON', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CHANSON DE GESTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CHANSON DE TOILE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CHANSON SENTENCIEUSE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CHANSON SPIRITUELLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CHARAKTERSTUCK', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CHARLESTON', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CHASSE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CHANTING', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CIACCONA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CLAUSULA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'COLINDA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'COMEDIE-BALLET', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'COMMEDIA PER MUSICA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'COMIC OPERA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CONGA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CONTACIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CONTRADDANZA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CONTRAFACTUM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'COPLA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CORO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CORRENTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CONTRAPPUNTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'COTILLON', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'COUNTRY DANCE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'COUPLET', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CSARDAS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DANZA MACABRA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DECIMINO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DESCORT', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CUECA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DIALOGO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DISGUISINGS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DITIRAMBO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DIVERTISSEMENT', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DRAME LYRIQUE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DRAMMA EROICO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DRAMMA GIOCOSO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DOUBLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DRAMMA LITURGICO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DRAMMA PASTORALE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DRAMMA PER MUSICA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DRAMMA SACRO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DRAMMA SCOLASTICO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DREHER', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DUETTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DUMKA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DRAMMA SEMISERIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'EGLOGA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ELEGIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ENGLISH WALTZ', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ENSALADA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'EPITALAMIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'EPOS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ESTAMPIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ENTREE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FACKELTANZ', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FADO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FALSOBORDONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FANDANGO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FANFARA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FARANDOLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FARSA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FAVOLA PER MUSICA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FESTA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FINALE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FIORETTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FLAMENCO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FOLLIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FOXTROT', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FRICASSEE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FROTTOLA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FURIANT', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FURLANA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GAGLIARDA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GALOP', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GASSENHAUER', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GAVOTTA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GEISSLERLIEDER', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GENERO CHICO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'AIR', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GIGA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GIUSTINIANA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GLEE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GRAND OPIRA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GREGHESCA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GYMEL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'HABANERA', '', 'inidb', now(), 'inidb', now(), '');




/* Data for the `sbnweb.ts_stop_list` table  (Records 501 - 1000) */

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'HADUTANC', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'HALLING', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'HOPAK', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'HOQUETUS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'HORNPIPE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'IMENEO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'IMPLORAZIONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'IMPROMPTU', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'IMPROPERIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INNO NAZIONALE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INNO PATRIOTTICO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INTERLUDIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INTERMEDIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INTRADA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INTRODUZIONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INTRODUZIONE DRAMMATICA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INVENZIONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INVOCAZIONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'IPORCHEMA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'JEU PARTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'JIG', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'JIGG', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'JOTA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'JUBILEE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'KOLEDA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'KOLO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'KRAKOWIAK', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'KUJAWIAK', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LAI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LANDLER', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LAMENTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LANGAUS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LAUDA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LEISE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LETTURA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LICENZA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LIEDERSPIEL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LINDY', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LIRICA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LOURE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MACCHIETTA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MADRIGALE RAPPRESENTATIVO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MADRIGALE SPIRITUALE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MAGGIOLATA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MALAGUENA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MAMBO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MARCIA FUNEBRE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MARCIA MILITARE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MARCIA NUZIALE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MARCIA PROCESSIONALE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MASCHERATA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MASQUE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MATTACCINO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MAXIXE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MAZUR', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MILODIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MELODIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MELODIA RELIGIOSA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MELODRAMMA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MELODRAMMA TRAGICO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MELOLOGO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MELOS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MESSA DI REQUIEM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MESSA PER ORGANO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'METAMORFOSI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'METODO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MILONGA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MIMODRAMMA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MISTERO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MODINHA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MOMENTO MUSICALE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MONFERRINA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MORALITA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MORCEAU DE SALON', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MORESCA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MORRIS DANCE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MOTO PERPETUO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MUMMING', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MUNEIRA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MUSETTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MUSICA DI SCENA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MUSICAL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MUSIQUE MESURIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MUTANZA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'NACHTANZ', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'NAUBA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'NEGRO SPIRITUAL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'NENIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'NINNA NANNA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'NOEL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'NOMOS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'NONETTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'NOVELLETTA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OBEREK', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ODE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ONESTEP', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPERA-BALLET', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPERA-COMIQUE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPERA COMICA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ANTHEM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPERA DA CAMERA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPERA MULTIMEDIALE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPERA PER BAMBINI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPERA PER MARIONETTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPERA RADIOFONICA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPERA TELEVISIVA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPIRA-COMIQUE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPERETTA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ORGANUM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OTTETTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PADOVANA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PALOTAS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PANTOMIMA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PARAFRASI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PARODIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PARTIMENTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PARTITA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PAS DE DEUX', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PAS REDOUBLI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PASO DOBLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PASSAMEZZO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PASTICCIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PASTORALE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PASTOURELLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PATER NOSTER', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PAVANIGLIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PEANA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PENILLON', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PERIGORDINO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PEZZO DA CONCERTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PIBROCH', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PIVA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PLAINTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PLAISANTERIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PLANCTUS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PLANTATION SONG', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'POLKA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'POLKA MAZURKA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'POLO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'POSTLUDIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'POTPOURRI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PRAEAMBULUM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PRAEFATIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PREGHIERA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PRELUDIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PROEMIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PROFEZIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PROLOGO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'QUADRIGLIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'QUARTETTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'QUINTETTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'QUODLIBET', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RADA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RANZ DES VACHES', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RASPA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RECIT', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RIJOUISSANCE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RECITATIVO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'REDOWA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'REEL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'REIGENLIED', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'REJDOVAK', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RHEINLANDER', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'REJOUISSANCE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RISPETTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RITIRATA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RITORNELLO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ROMANCE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ROMANESCA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ROMANZA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ROMANZA SENZA PAROLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RONDO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ROTA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ROTRUENGE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ROTTA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RUEDA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RUGGERO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RUGGIERO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SACRA RAPPRESENTAZIONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SAETA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SAINETE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SALLENDA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SALMELLO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SALSA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SALTARELLO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SALTARELLO TEDESCO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SAMBA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SARABANDA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ARIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SARDANA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SARUM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SCAT', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SCENA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SCHERZO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SCHERZO DRAMMATICO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SCHNADAHUPFL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SCHOTTISCH', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SCHOTTISH', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SCHUHPLATTLER', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SEGUIDILLA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SEISES', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SERENATA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SESTETTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SETTIMINO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SEVILLANA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SHANTY', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SHIMMY', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SICILIANA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SINFONIA CONCERTANTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SINFONIA DA CAMERA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SINFONIETTA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SINGSPIEL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SIRVENTES', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SKETCH', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SOLFEGGIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SOLILOQUIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'STORNELLO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'STRAMBOTTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'STRATSPEY', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SYMPHONIE DRAMATIQUE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SYOMYO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TAFELMUSIK', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TAMBOURIN', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TANGO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TARANTELLA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TENSO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TERZETTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'THRENOS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TIRANA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TOMBEAU', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TONADILLA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TORNEO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TOURDION', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TRAGEDIE LYRIQUE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TRANSITORIUM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TRAQUENARD', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TRATTATO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TRECANUM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TRENCHMORE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TRESCA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TREZZA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TRICINIUM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TRICOTET', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TRIODION', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TRISHAGION', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TUMBA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'UMORESCA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VARSOVIENNE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VAUDEVILLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VENEZIANA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VILLANCICO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VILLANELLA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VILLOTA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VIRELAI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VOCALIZZO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VOLTA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VOLUNTARY', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'YARAVI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ZAMACUECA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ZAMBA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ZAPATEADO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ZARZUELA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ZOPPA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ZORTZIKO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ZWIEFACHER', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BALLATA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BLUEGRASS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BLUES', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'BALLETTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANZONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTO SACRO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CONCERTO GROSSO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CORALE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PRELUDIO CORALE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CONCERTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CHANSONS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CAROL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CHANCE COMPOSITIONS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTATA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'COUNTRY MUSIC', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SONATA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DANZA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'DIVERTIMENTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FUGA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CANTO POPOLARE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FANTASIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'GOSPEL SONG', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'INTERMEZZO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'JAZZ', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LIED', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RIVISTA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MADRIGALE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MINUETTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MOTTETTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MUSICA DA FILM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MARCIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MULTIPLE FORMS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MAZURKA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'NOTTURNO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPERA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ORATORIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OUVERTURE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PRELUDIO E FUGA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MUSICA A PROGRAMMA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PASSIONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'POLACCA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'POPULAR MUSIC', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PASSACAGLIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SONG', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PAVANA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ROCK MUSIC', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RONDEAU', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RAGTIME', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RICERCARE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'RAPSODIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'REQUIEM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SINFONIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'POEMA SINFONICO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SQUARE DANCE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'STUDIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'SUITE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TOCCATA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TRIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FORMA NON SIGNIFICATIVA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'VARIAZIONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'WALZER', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OTHER FORMS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'I', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'A', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'AFFINCHH', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'AGL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'AGLI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'AI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'AL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'ALL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'ALLA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'ALLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'ALLO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'ANZICHH', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'AVERE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'BENSL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'CHE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'CHI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'CIOH', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'COME', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'COMUNQUE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'CON', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'CONTRO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'COSA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DACHH', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DAGL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DAGLI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DAI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DAL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DALL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DALLA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DALLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DALLO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DEGL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DEGLI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DEI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DEL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DELL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DELLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DELLO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DOPO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DOVE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DUNQUE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'DURANTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'E', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'EGLI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'EPPURE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'ESSERE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'ESSI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'FINCHI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'FINO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'FRA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'GIACCHE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'GL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'GLI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'GRAZIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'IL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'IN', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'INOLTRE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'IO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'L', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'LA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'LE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'LO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'LORO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'MA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'MENTRE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'MIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'NE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'NEANCHE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'NEGL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'NEGLI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'NEI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'NEL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'NELL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'NELLA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'NELLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'NELLO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'NEMMENO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'NEPPURE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'NOI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'NONCHE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'NONDIMENO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'NOSTRO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'O', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'ONDE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'OPPURE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'OSSIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'OVVERO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'PER', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'PERCHH', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'PERCIR', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'PERR', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'POICHH', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'PRIMA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'PURCHH', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'QUAND ANCHE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'QUANDO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'QUANTUNQUE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'QUASI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'QUINDI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'SE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'SEBBENE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'SENNONCHH', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'SENZA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'SEPPURE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'SI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'SICCOME', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'SOPRA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'SOTTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'SU', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'SUBITO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'SUGL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'SUGLI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'SUI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'SUL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'SULL', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'SULLA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'SULLE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'SULLO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'SUO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'TALCHH', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'TU', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'TUO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'TUTTAVIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'TUTTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'UN', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'UNA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'UNO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'VOI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('P', 'ITA', 'VOSTRO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'GER', 'DAS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'GER', 'DER', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'GER', 'DIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'GER', 'EIN', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'GER', 'EINE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'FRE', 'DES', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'FRE', 'L', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'FRE', 'LA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'FRE', 'LE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'FRE', 'LES', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'FRE', 'UN', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'FRE', 'UNE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'AGNUS DEI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'ALLELUIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'ANTE EVANGELIUM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'ANTIFONA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'BENEDICTUS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'CANTICO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'CLAUSULA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'COMMUNIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'COMPIETA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'CONDUCTUS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'CONFRATTORIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'CREDO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'DEVOZIONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'DOSSOLOGIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'ELEVAZIONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'GLORIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'GRADUALE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'IMPROPERIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'INGRESSA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'INNO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'INTONAZIONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'INTROITO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'INVITATORIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'KYRIE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'LAMENTAZIONE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'LITANIA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'LITURGIA DELLE ORE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'LODI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'LUCERNARIO', '', 'inidb', now(), 'inidb', now(), '');




/* Data for the `sbnweb.ts_stop_list` table  (Records 1001 - 1112) */

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'MATTUTINO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'MESSA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'MESSA DI REQUIEM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'OFFERTORIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'POST EVANGELIUM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'POST LECTIONEM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'POSTCOMMUNIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'REQUIEM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'RESPONSORIO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'SALMO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'SANCTUS', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'SEQUENZA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'SYMBOLUM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'TRATTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'TROPO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'UFFICIO DI REQUIEM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'UFFICIO DIVINO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'VERSETTO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('U', 'ITA', 'VERSICOLO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'ITA', 'LE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'Il', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'lo', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'la', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'i', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'gl', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'gli', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'li', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'le', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'l', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'del', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'della', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'delle', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'dei', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'degli', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'degl', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'dello', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'vn', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'un', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'vno', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'uno', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'vna', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'una', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'une', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'vne', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'in', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'ad', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'bolla', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'breve', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'breue', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'nova', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'noua', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'ITA', 'sommario', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'SPA', 'les', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'SPA', 'los', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'SPA', 'las', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'SPA', 'el', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'in', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'ad', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'super', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'ampliatio', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'bulla', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'confirmatio', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'constitutio', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'bolla', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'breve', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'breue', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'declaratio', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'Iubileum', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'literae', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'litterae', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'prorogatio', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'reductio', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'extensio', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'indulgenze', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'revocatio', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'reuocatio', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'indulgentia', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'innovatio', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'innouatio', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'nova', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'decretum', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'indictio', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'erectio', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'moderatio', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'facultas', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('Z', 'LAT', 'praeceptum', 'UTILIZZATA DA BIS DuT013 DuT014', 'inidb', now(), 'inidb', now(), 'N');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('A', 'ITA', 'UNO', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'QUARTETTI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ALLELUJA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CASSAZIONI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'CONTRADDANZE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'EGLOGHE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FARSE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'FESTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'ENTREES', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'LIEDER', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MADRIGALI RAPPRESENTATIVI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MADRIGALI SPIRITUALI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MELOLOGHI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MESSE DA REQUIEM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MESSE DI REQUIEM', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MIMODRAMMI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'MORCEAUX DE SALON', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPERE COMICHE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPERE DA CAMERA', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPERE MULTIMEDIALI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPERE PER BAMBINI', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OPERE PER MARIONETTE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'OUVERTURES', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'PRELUDI E FUGHE', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO "sbnweb"."ts_stop_list" ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES ('F', 'ITA', 'TRII', '', 'inidb', now(), 'inidb', now(), '');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'ACCLAMAZIONE', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'AKATHISTOS HYMNOS', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'ANTHEM', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'ASSOLUZIONE', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'CANONE BIZANTINO', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'CANTUS PLANUS', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'CHANTING', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'CORALE', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'IMPLORAZIONE', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'INVOCAZIONE', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'LEISE', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'LETTURA', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'MOTTETTO', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'ORGANUM', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'PATER NOSTER', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'PRAEAMBULUM', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'PRAEFATIO', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'SALLENDA', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'SALMELLO', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'TRANSITORIUM', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'TRECANUM', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'TRIODION', E'', E'inidb', now(), E'inidb', now(), E'');

INSERT INTO sbnweb.ts_stop_list ("tp_stop_list", "cd_lingua", "parola", "nota_stop_list", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc")
VALUES (E'U', E'ITA', E'TRISHAGION', E'', E'inidb', now(), E'inidb', now(), E'');

COMMIT;