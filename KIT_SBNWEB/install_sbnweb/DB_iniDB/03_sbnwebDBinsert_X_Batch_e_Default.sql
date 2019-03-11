BEGIN;

/* pulizia precedenti caricamenti */ 
DELETE FROM "sbnweb"."tbf_default";
DELETE FROM "sbnweb"."tbf_gruppi_default";
DELETE FROM "sbnweb"."tbf_config_default";
DELETE FROM "sbnweb"."tbf_modelli_stampe";
DELETE FROM "sbnweb"."tbf_batch_servizi";
DELETE FROM "sbnweb"."tbf_coda_jms";

----- inizializza sequence tbf_coda_jms_id_coda_seq-----
ALTER SEQUENCE "sbnweb"."tbf_coda_jms_id_coda_seq"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  RESTART 1
    CACHE 1  NO CYCLE;
/* Data for the `sbnweb.tbf_coda_jms` table  (Records 1 - 3) */
INSERT INTO "sbnweb"."tbf_coda_jms" ("nome_jms", "sincrona", "id_descrizione", "id_descr_orario_attivazione", "id_orario_di_attivazione", "cron_expression")
VALUES ('queue/sbnBatch_Input', 'S', 'dalle 00 alle 24 ogni 30 sec', NULL, NULL, '0/30 * * * * ?');
INSERT INTO "sbnweb"."tbf_coda_jms" ("nome_jms", "sincrona", "id_descrizione", "id_descr_orario_attivazione", "id_orario_di_attivazione", "cron_expression")
VALUES ('queue/sbnBatch_Input', 'S', 'dalle 00 alle 24 ogni 5 min', NULL, NULL, '0 0/5 * * * ?');
INSERT INTO "sbnweb"."tbf_coda_jms" ("nome_jms", "sincrona", "id_descrizione", "id_descr_orario_attivazione", "id_orario_di_attivazione", "cron_expression")
VALUES ('queue/sbnBatch_Input', 'S', 'dalle 16 alle 20 ogni 30 sec', NULL, NULL, '5/35 * 17-19 * * ? *');

----- inizializza sequence tbf_batch_servizi_id_batch_servizi_seq-----
ALTER SEQUENCE "sbnweb"."tbf_batch_servizi_id_batch_servizi_seq"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  RESTART 1
    CACHE 1  NO CYCLE;
/* Data for the `sbnweb.tbf_batch_servizi` table  (Records 1 - 34) */
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.allineamenti.AdeguamentoCalcoliBatch', 'A3200', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.allineamenti.RifiutoSuggerimentiBatch', 'A6200', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'B', 'it.iccu.sbn.batch.allineamenti.AccettaSuggerimentiBatch', 'A9300', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.stampe.StampaRegistroIngressoBatch', 'AC100', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.stampe.StampaOrdiniBatch', 'AC200', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.stampe.StampaBuoniCaricoBatch', 'AC300', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.stampe.StampaFornitoriBatch', 'AR970', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.stampe.RipartizioneSpeseBatch', 'AZ100', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.stampe.StampaBollettarioBatch', 'AZ200', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.allineamenti.OperazioniSuOrdineBatch', 'AZ800', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.stampe.StampaStatisticheBatch', 'AZ900', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.stampe.StampaRegistroTopograficoBatch', 'C3000', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.stampe.StampaSchedeCatalograficheBatch', 'C5020', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.stampe.StampaRegistroConservazioneBatch', 'C6000', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.SpostamentoCollocazioniBatch', 'C7000', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.ScaricoInventarialeBatch', 'C9200', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.stampe.StampaEtichetteBatch', 'CD000', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.AggiornamentoDisponibilitaBatch', 'CE000', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.allineamenti.AllineamentiBatch', 'IA000', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.allineamenti.AllineamentiBatch', 'IA004', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.allineamenti.AllineamentiBatch', 'IA005', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.allineamenti.AllineamentiBatch', 'IA006', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.unimarc.ExportUnimarcBatch', 'IE001', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.allineamenti.AllineamentiBatch', 'IF010', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'B', 'it.iccu.sbn.batch.allineamenti.RinnovoDirittiBatch', 'LD001', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.allineamenti.AggiornaDirittiUtentiBatch', 'LD002', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'B', 'it.iccu.sbn.batch.stampe.StampaUtentiBatch', 'LR940', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'B', 'it.iccu.sbn.batch.servizi.ArchiviazioneMovLocBatch', 'LS331', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'B', 'it.iccu.sbn.batch.stampe.StampaServiziCorrentiBatch', 'LS334', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'B', 'it.iccu.sbn.batch.stampe.StampaServiziStoricoBatch', 'LS335', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.servizi.ServiziBatch', 'LS337', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.semantica.CancellazioneSoggettiInutilizzatiBatch', 'SSCDI', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.semantica.CancellazioneSoggettiInutilizzatiBatch', 'SSCSI', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.stampe.StampaSchedeCatalograficheBatch', 'ZG200', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.semantica.StampeSemanticaBatch', 'ZS431', 'N');
INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.statistiche.StatisticheBatch', 'T0001', 'N');

INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.unimarc.ExportAuthorityBatch', 'IE002', 'N');

INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.unimarc.ExportUnimarcBatch', 'IE003', 'N');

INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.unimarc.ImportUnimarcBatch', 'II001', 'N');

INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.servizi.BatchImportaUtentiEsse3', 'LRI01', 'N');

INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.periodici.StampaListaFascicoliBatch', 'PZ001', 'N');

INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.semantica.StampeSemanticaBatch', 'ZS432', 'N');

INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.stampe.StampaStrumentiPatrimonioBatch', 'CS000', 'N');

INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.stampe.StampaOrdiniBatch', 'AZ700', 'N');

INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.AggiornamentoDisponibilitaBatch', 'II150', 'N');

INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.stampe.StampaTitoliEditoreBatch', 'IRZ01', 'N');

INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.allineamenti.AllineamentiBatch', 'IA007', 'N');

INSERT INTO "sbnweb"."tbf_batch_servizi" ("id_coda_input", "nome_coda_output", "visibilita", "class_name", "cd_attivita", "fl_canc")
VALUES (1, 'queue/sbnBatch_Output2', 'P', 'it.iccu.sbn.batch.servizi.ServiziBatch', 'LS340', 'N');

----- inizializza sequence tbf_batch_servizi_id_batch_servizi_seq-----
ALTER SEQUENCE "sbnweb"."tbf_modelli_stampe_id_modello_seq"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  RESTART 1
    CACHE 1  NO CYCLE;
/* Data for the `sbnweb.tbf_modelli_stampe` table  (Records 1 - 36) */
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'registro di ingresso1', 'F', 'default_reg_ingresso1', 'DBini', 'now()', 'DBini', 'now()', 'N', 'senza dati fattura', NULL, 'AC100', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'registro di ingresso2', 'F', 'default_reg_ingresso2', 'DBini', 'now()', 'DBini', 'now()', 'N', 'con dati fattura', NULL, 'AC100', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Registro di ingresso - statist', 'F', 'default_statistiche_reg_ingresso', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Statistiche registro di ingresso', NULL, 'AC100', 'default_statistiche_sub1,default_statistiche_sub2', NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Lettera semplice', 'F', 'default_buoni_ordine2', 'DBini', 'now()', 'DBini', 'now()', 'N', 'modello per test', NULL, 'AC200', 'default_buoni_ordine_sub1,default_buoni_ordine_sub2', NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Buono di carico', 'F', 'default_buono_carico',   'DBini', 'now()', 'DBini', 'now()', 'N', 'Buoni di carico', NULL, 'AC300', 'default_buono_carico_sub1', NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'fornitori1', 'F', 'default_fornitori1', 'DBini', 'now()', 'DBini', 'now()', 'N', 'formato tabellare', NULL, 'AR970', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'fornitori2', 'F', 'default_fornitori2', 'DBini', 'now()', 'DBini', 'now()', 'N', 'formato testuale', NULL, 'AR970', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Ripartizione spese', 'F', 'default_riparto_spese1', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Ripartizione per tipologia', NULL, 'AZ100', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Ripartizione spese', 'F', 'default_riparto_spese2', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Ripartizione per sezione', NULL, 'AZ100', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Ripartizione spese', 'F', 'default_riparto_spese3', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Ripartizione per fornitore', NULL, 'AZ100', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Ripartizione spese', 'F', 'default_riparto_spese4', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Ripartizione per ordini', NULL, 'AZ100', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Bollettario', 'F', 'default_bollettario1', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Bollettario', NULL, 'AZ200', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Fattura', 'F', 'default_fattura1', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Fattura Compatta', NULL, 'AZ300', 'default_fattura1_sub', NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Fattura', 'F', 'default_fattura2', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Fattura Estesa', NULL, 'AZ300', 'default_fattura1_sub', NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Comunicazione', 'F', 'default_comunicazione1', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Comunicazione', NULL, 'AZ400', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Suggerimenti bibliotecario', 'F', 'default_suggerimenti_bibl', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Suggerimenti bibliotecario', NULL, 'AZ500', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Suggerimenti lettore', 'F', 'default_suggerimenti_lett', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Suggerimenti lettore', NULL, 'AZ600', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Statistiche sui tempi', 'F', 'default_statistiche_tempi1', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Statistiche tempi', NULL, 'AZ900', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'registro topografico', 'F', 'default_registro_topografico', 'DBini', 'now()', 'DBini', 'now()', 'N', 'registro topografico', NULL, 'C3000', 'default_registro_topograficoColl,default_registro_topograficoInv', NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Scheda catalogo', 'F', 'scheda_template', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Scheda catalogo', NULL, 'C5020', 'scheda_subreport', NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'registro di conservazione', 'F', 'default_registro_conservazione', 'DBini', 'now()', 'DBini', 'now()', 'N', 'registro di conservazione', NULL, 'C6000', 'default_registro_conservazione_sub1', NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Etichette solo barcode', 'F', 'default_etichette_solo_barcode', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Etichette solo barcode', NULL, 'CD000', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'biblioteche1', 'F', 'default_biblioteche1', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Lista delle biblioteche1', NULL, 'FBIA0', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'biblioteche2', 'F', 'default_biblioteche2', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Lista delle biblioteche2', NULL, 'FBIA0', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Bibliotecari', 'F', 'default_bibliotecari', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Bibliotecari', NULL, 'FUIA0', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Stampa richiesta', 'F', 'default_richiesta', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Stampa richiesta', NULL, 'LR339', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'utenti1', 'F', 'default_utenti1', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Lista utenti: formato tabellare', NULL, 'LR940', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'utenti2', 'F', 'default_utenti2', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Lista utenti: formato testuale', NULL, 'LR940', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Cartellino', 'F', 'cartellino_template', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Cartellino', NULL, 'LRTES', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'StampaServiziCorrenti', 'F', 'default_servizi_correnti', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Stampa servizi correnti', NULL, 'LS334', 'default_servizi_correnti', NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'StampaStoricoServizi', 'F', 'default_servizi_storico', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Stampa storico servizi', NULL, 'LS335', 'default_servizi_storico', NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Stampa sollecito', 'F', 'default_sollecito', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Stampa del Sollecito', 'CSW', 'LS337', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Stampa Registro Catalografico', 'C', 'default_catalografico', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Stampa registro catalografico', 'SBW', 'ZG200', 'default_catalografico_sub1,default_catalografico_sub2', NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, E'Stampa Soggettario', E'C', E'default_soggettario', E'initdb', now(), E'initdb', now(), E'N', E'Stampa soggettario', NULL, E'ZS431', E'default_soggettario_sub1_soggetti,default_soggettario_sub2_legamiDescrittori,default_soggettario_sub2_legamiTitoli', E'');
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'termini di thesauro1', 'F', 'default_terminithesauro1', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Lista dei termini di thesauro1', NULL, 'ZSTH1', 'default_terminithesauro_sub1,default_terminithesauro_sub2', NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'termini di thesauro2', 'F', 'default_terminithesauro2', 'DBini', 'now()', 'DBini', 'now()', 'N', 'Lista dei termini di thesauro2', NULL, 'ZSTH1', 'default_terminithesauro_sub1,default_terminithesauro_sub2', NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Lista bibliotecari', 'F', 'default_bibliotecari', 'DBini', 'now()', 'DBini', 'now()', 'N', NULL, NULL, NULL, NULL, NULL);

INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'SERVIZI', 'C', '<p style=\"text-align: center\">\r\n<strong>GESTIONE PASSWORD PERSONALE</strong><br/>\r\nIl sistema richiede una password di almeno 8 caratteri.<br/>\r\nI vecchi utenti, dotati di una password con un numero di caratteri inferiore, dovranno adattarla ai nuovi requisiti alla prima autenticazione.<br/>\r\nIn caso di necessità di recupero della password, cliccando sull''apposito pulsante nella presente pagina, se ne riceverà via e-mail una generata in automatico dal sistema (richiesta la presenza di un valido indirizzo e-mail nell''anagrafica).<br/>\nNel caso si richieda un azzeramento della password agli operatori della biblioteca, questa verrà impostata sul proprio codice fiscale (richiesta la presenza di un valido codice fiscale nell''anagrafica).<br/>\r\n<br/>\r\nPer accedere ai servizi é necessaria l''identificazione dell''utente.<br/>\r\n<autoreg>Gli utenti non ancora registrati possono utilizzare il modulo di <autoreglink>.</autoreg></p>', 'DBini', 'now()', 'DBini', 'now()', 'N', 'modulo web welcome', NULL, 'L0000', NULL, NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, E'Stampa Descrittori', E'C', E'default_descrittori', E'initdb', now(), E'initdb', now(), E'N', E'Stampa Descrittori', NULL, E'ZS432', E'default_descrittori_sub1_descrittori,default_descrittori_sub2_legamiDescrittori,default_descrittori_sub3_legamiSoggetti', E'');
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'registro di conservazione_xls', 'F', 'default_registro_conservazione_xls', 'DBini', 'now()', 'DBini', 'now()', 'N', 'registro di conservazione xls', NULL, 'C6000', 'default_registro_cons_xls_sub1', NULL);
INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Lista fascicoli', 'F', 'default_lista_fascicoli', 'inidb', now(), 'inidb', now(), 'N', 'Lista fascicoli', NULL, 'PZ001', 'default_lista_fascicoli_sub1', NULL);

INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'bollettino nuovi titoli', 'F', 'default_bollettino', 'inidb', now(), 'inidb', now(), 'N', 'Bollettino nuove accessioni', NULL, 'CS000', 'default_bollettino_sub1,default_bollettino_soggetti_sub2,default_bollettino_classi_sub2', NULL);

INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Modello prelievo', 'F', 'default_moduloPrelievo', 'inidb', now(), 'inidb', now(), 'N', 'Modello prelievo', NULL, 'CMP00', NULL, NULL);

INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Registro  controllo patrimonio', 'F', 'default_strumenti_patrimonio', 'inidb', now(), 'inidb', now(), 'N', 'Registro di controllo patrimonio', NULL, 'CS000', 'default_strumenti_patrimonio_sub1', NULL);

INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Registro posseduto', 'F', 'default_posseduto', 'inidb', now(), 'inidb', now(), 'N', 'Registro posseduto', NULL, 'CS000', 'default_posseduto_sub1', NULL);

INSERT INTO "sbnweb"."tbf_modelli_stampe" ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, 'Titoli per editore', 'F', 'default_titoli_editore', 'INIDB', now(), 'INIDB', now(), 'N', 'titoli per editore', NULL, 'IRZ01', 'default_titoli_editore_sub1', NULL);

INSERT INTO sbnweb.tbf_modelli_stampe ("cd_bib", "modello", "tipo_modello", "campi_valori_del_form", "ute_ins", "ts_ins", "ute_var", "ts_var", "fl_canc", "descr", "cd_polo", "cd_attivita", "subreport", "descr_bib")
VALUES (NULL, E'Modulo prelievo', E'F', E'default_moduloPrelievoMov', E'DBini', now(), E'DBini', now(), E'N', E'Modulo prelievo', NULL, E'LR339', NULL, NULL);


update "sbnweb"."tbf_modelli_stampe" 
set modello = 'Registro controllo patrimonio',
descr = 'Registro di controllo patrimonio'
where  cd_attivita = 'CS000'
and campi_valori_del_form = 'default_strumenti_patrimonio'; 

----- inizializza sequence tbf_config_default_id_config_seq-----
ALTER SEQUENCE "sbnweb"."tbf_config_default_id_config_seq"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  RESTART 1
    CACHE 1  NO CYCLE;
/* Data for the `sbnweb.tbf_config_default` table  (Records 1 - 39) */
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('00_GestBib', NULL, 1, 'IC000', NULL, 0);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GestBibTitolo', '00_GestBib', 1, 'IC001', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GestBibCreaTitolo', '00_GestBib', 2, 'ICR01', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GestBibAutori', '00_GestBib', 3, 'IC002', 'AU', NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GestBibCreaAutore', '00_GestBib', 4, 'ICR02', 'AU', NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GestBibMarca', '00_GestBib', 5, 'IC002', 'MA', NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GestBibCreaMarca', '00_GestBib', 6, 'ICR02', 'MA', NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GestBibLuogo', '00_GestBib', 7, 'IC002', 'LU', NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GestBibCreaLuogo', '00_GestBib', 8, 'ICR02', 'LU', NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('00_GestSem', NULL, 2, 'IC002', NULL, 0);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GestSemSoggetto', '00_GestSem', 1, 'IC002', 'SO', NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GestSemCreaSoggetto', '00_GestSem', 2, 'ICR02', 'SO', NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GestSemDescrittore', '00_GestSem', 3, 'IC002', 'DE', NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GestSemCreaDescrittore', '00_GestSem', 4, 'ICR02', 'DE', NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GestSemClasse', '00_GestSem', 5, 'IC002', 'CL', NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GestSemCreaClasse', '00_GestSem', 6, 'ICR02', 'CL', NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GestSemThesauro', '00_GestSem', 7, 'IC002', 'TH', NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GestSemCreaThesauro', '00_GestSem', 8, 'ICR02', 'TH', NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('00_GDocFis', NULL, 3, 'C0000', NULL, 0);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GDocFisInventario', '00_GDocFis', 1, 'C5000', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GDocFisCollocaInv', '00_GDocFis', 2, 'C5000', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GDocFisModificaInv', '00_GDocFis', 3, 'C5000', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GDocFisEsameColloc', '00_GDocFis', 4, 'C4000', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('00_GAcq', NULL, 4, 'A0000', NULL, 0);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GAcqCercaOrdine', '00_GAcq', 1, 'A1100', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GAcqCreaOrdineAForn', '00_GAcq', 3, 'A1210', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GAcqCreaOrdineVForn', '00_GAcq', 4, 'A1210', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GAcqCreaOrdineLForn', '00_GAcq', 5, 'A1220', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GAcqCreaOrdineDForn', '00_GAcq', 6, 'A1220', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GAcqCreaOrdineCForn', '00_GAcq', 7, 'A1230', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GAcqCreaOrdineRForn', '00_GAcq', 8, 'A1240', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GAcqCreaOrdine', '00_GAcq', 2, 'A1200', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GDocFisCercaPosses', '00_GDocFis', 6, 'C0000', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GDocFisCreaPosses', '00_GDocFis', 7, 'C9400', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GDocFisStampaEtich', '00_GDocFis', 5, 'CD000', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('00_GSer', NULL, 5, 'L0000', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GSerErogazione', '00_GSer', 1, 'L0000', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('02_GSerCercaUtenti', '00_GSer', 2, 'L0000', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('03_GSerCreaUtenti', '00_GSer', 3, 'L0000', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('00_GPeriodici', NULL, 6, 'P0000', NULL, NULL);
INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('01_GPer_Kardex', '00_GPeriodici', 1, 'P0000', NULL, NULL);

----- inizializza sequence tbf_gruppi_default_id_seq-----
ALTER SEQUENCE "sbnweb"."tbf_gruppi_default_id_seq"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  RESTART 1
    CACHE 1  NO CYCLE;
/* Data for the `sbnweb.tbf_gruppi_default` table  (Records 1 - 18) */
INSERT INTO "sbnweb"."tbf_gruppi_default" ("etichetta", "bundle")
VALUES ('label.TipoRicercaPerTitolo', 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_gruppi_default" ("etichetta", "bundle")
VALUES ('ricerca.livelloRicerca', 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_gruppi_default" ("etichetta", "bundle")
VALUES ('label.TipoRicercaPerNome', 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_gruppi_default" ("etichetta", "bundle")
VALUES ('ricerca.tiponome', 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_gruppi_default" ("etichetta", "bundle")
VALUES ('ricerca.forma', 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_gruppi_default" ("etichetta", "bundle")
VALUES ('ricerca.livelloRicerca', 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_gruppi_default" ("etichetta", "bundle")
VALUES ('ricerca.livelloRicerca', 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_gruppi_default" ("etichetta", "bundle")
VALUES ('ricerca.livelloRicerca', 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_gruppi_default" ("etichetta", "bundle")
VALUES ('gestionesemantica.soggetto.livelloDiRicerca', 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_gruppi_default" ("etichetta", "bundle")
VALUES ('gestionesemantica.soggetto.TipoRicercaPerTesto', 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_gruppi_default" ("etichetta", "bundle")
VALUES ('gestionesemantica.soggetto.TipoRicercaPerTesto', 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_gruppi_default" ("etichetta", "bundle")
VALUES ('ricerca.livelloDiRicerca', 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_gruppi_default" ("etichetta", "bundle")
VALUES ('ricercapersimbolo.puntuale', 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_gruppi_default" ("etichetta", "bundle")
VALUES ('gestionesemantica.thesauro.termine', 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_gruppi_default" ("etichetta", "bundle")
VALUES ('documentofisico.tiponome', 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_gruppi_default" ("etichetta", "bundle")
VALUES ('documentofisico.forma', 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_gruppi_default" ("etichetta", "bundle")
VALUES ('documentofisico.soloBib', 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_gruppi_default" ("etichetta", "bundle")
VALUES ('ordine.label.stato', 'acquisizioniLabels');

----- inizializza sequence tbf_default_id_default_seq-----
ALTER SEQUENCE "sbnweb"."tbf_default_id_default_seq"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  RESTART 1
    CACHE 1  NO CYCLE;
/* Data for the `sbnweb.tbf_default` table  (Records 1 - 121) */
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_TIT_PUNTUALE', 'Check', NULL, 'ricerca.puntuale', 'IC004', NULL, 1, 1, 2, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_TIT_ELEMENTI_BLOCCHI', 'String', 5, 'ricerca.elementiBlocco', 'IC004', NULL, 2, NULL, 2, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_TIT_ORDINAMENTO', 'Opzione', NULL, 'ricerca.ordinamento', 'IC004', 'OLTI', 3, NULL, 2, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_TIT_FORMATO_LISTA', 'Opzione', NULL, 'ricerca.formatoLista', 'IC004', 'TLMM', 4, NULL, 2, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_TIT_LIVELLO_POLO', 'Check', NULL, 'ricerca.polo', 'IC004', NULL, 5, 2, 2, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_TIT_LIVELLO_INDICE', 'Check', NULL, 'ricerca.indice', 'IC004', NULL, 6, 2, 2, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('CREA_TIT_NATURA1', 'Opzione', NULL, 'ricerca.natura', 'IC004', 'NABI', 1, NULL, 3, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('CREA_TIT_TIPO_MATERIALE', 'Opzione', NULL, 'catalogazione.tipMateriale', 'IC004', 'MATE', 2, NULL, 3, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('CREA_TIT_TIPO_RECORD', 'Opzione', NULL, 'ricerca.tipoRecord', 'IC004', 'GEUN', 3, NULL, 3, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('CREA_TIT_LIVELLO_AUTORITA', 'Opzione', NULL, 'ricerca.livelloAutorita', 'IC004', 'LIVE', 4, NULL, 3, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('CREA_TIT_PAESE', 'Opzione', NULL, 'ricerca.paese', 'IC004', 'PAES', 5, NULL, 3, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('CREA_TIT_LINGUA', 'Opzione', NULL, 'ricerca.lingua', 'IC004', 'LING', 6, NULL, 3, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('CREA_TIT_TIPO_NUMERO_STANDARD', 'Opzione', NULL, 'ricerca.num.standard', 'IC004', 'NSTD', 7, NULL, 3, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('CREA_SOG_TIPO_SOGG', 'Opzione', NULL, 'crea.tipoDiSoggetto', 'ICR02', 'SCSO', 1, NULL, 12, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('CREA_SOG_LIVELLO_AUTORITA', 'Opzione', NULL, 'crea.statoDiControllo', 'ICR02', 'LIVE', 2, NULL, 12, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('CREA_DES_DES_FORMA_TERMINE', 'Opzione', NULL, 'gestione.formaNome', 'ICR02', 'FODE', 1, NULL, 14, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('CREA_DES_DES_TIPO_LEGAME', 'Opzione', NULL, 'inserisci.tipoLegame', 'ICR02', 'LEDD', 2, NULL, 14, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('CREA_CLA_LIVELLO_AUTORITA', 'Opzione', NULL, 'ricerca.livello', 'ICR02', 'LIVE', 6, NULL, 16, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('CREA_THE_LIVELLO_AUTORITA', 'Opzione', NULL, 'ricerca.livello', 'ICR02', 'LIVE', 1, NULL, 18, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('CREA_THE_FORMA_TERMINE', 'Opzione', NULL, 'gestione.formaNome', 'ICR02', 'FODE', 1, NULL, 18, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('CREA_THE_THE_TIPO_LEGAME', 'Opzione', NULL, 'inserisci.tipoLegame', 'ICR02', 'STLT', 2, NULL, 18, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_TP_PRG_INV', 'Opzione', NULL, 'documentofisico.progressivoT', 'C5000', 'CTNI', 1, NULL, 20, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_CD_SERIE', 'String', 3, 'documentofisico.serieT', 'C5000', NULL, 2, NULL, 20, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_SEZ_COLL', 'String', 10, 'documentofisico.sezioneT', 'C5000', 'CTNI', 1, NULL, 21, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_CAT_FRUIZIONE', 'Opzione', NULL, 'documentofisico.tipoFruizioneT', 'C5000', 'LCFR', 1, NULL, 22, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_TP_ACQUISIZIONE', 'Opzione', NULL, 'documentofisico.tipoAcquisizioneT', 'C5000', 'CTAC', 1, NULL, 22, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_STATO_CONSERVAZIONE', 'Opzione', NULL, 'documentofisico.tipoStatoConT', 'C5000', 'CSCO', 2, NULL, 22, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_CD_NO_DISPONIBILITA', 'Opzione', NULL, 'documentofisico.nonDisponibilePerT', 'C5000', 'CCND', 1, NULL, 22, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_CD_PROVENIENZA', 'String', 5, 'documentofisico.provenienzaT', 'C5000', NULL, 2, NULL, 22, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_ESA_COLL_ELEM_BLOCCHI', 'String', 5, 'documentofisico.elementiBlocco', 'C4000', NULL, 1, NULL, 23, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_ESA_COLL_ORDINAMENTO', 'Opzione', NULL, 'documentofisico.ordinamento', 'C4000', 'OCCO', 2, NULL, 23, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_RIC_POSS_TESTO_NOME', 'Opzione', NULL, 'label.TipoRicercaPerNome', 'C0000', 'ORTT', 1, NULL, 24, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_RIC_POSS_TIPO_NOME_TUTTI', 'Radio', NULL, 'documentofisico.tuttiNomi', 'C0000', NULL, 2, 15, 24, 'DocumentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_RIC_POSS_TIPO_NOME_PERSONALE', 'Radio', NULL, 'documentofisico.nomePersonale', 'C0000', NULL, 3, 15, 24, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_RIC_POSS_TIPO_NOME_COLLETTIVO', 'Radio', NULL, 'documentofisico.nomeCollettivo', 'C0000', NULL, 4, 15, 24, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_RIC_POSS_FORMA_TUTTI', 'Radio', NULL, 'documentofisico.tuttiNomi', 'C0000', NULL, 5, 16, 24, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_RIC_POSS_FORMA_ACCETTATA', 'Radio', NULL, 'documentofisico.autore', 'C0000', NULL, 6, 16, 24, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_RIC_POSS_FORMA_RINVIO', 'Radio', NULL, 'documentofisico.rinvio', 'C0000', NULL, 7, 16, 24, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_RIC_SOLO_DI_BIBLIOTECA', 'Check', NULL, 'documentofisico.soloBib_true', 'CO000', NULL, 8, 17, 24, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_RIC_POSS_ELEM_BLOCCHI', 'String', 5, 'documentofisico.elementiBlocco', 'CO000', NULL, 9, NULL, 24, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_RIC_POSS_ORDINAMENTO', 'Opzione', NULL, 'documentofisico.ordinamento', 'C0000', 'OIAU', 10, NULL, 24, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_CREA_POSS_LIVELLO_AUTORITA', 'Opzione', NULL, 'documentofisico.dettaglio.livello', 'C9400', 'LIVE', 1, NULL, 25, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_CREA_POSS_TIPO_NOME', 'Opzione', NULL, 'documentofisico.dettaglio.tipoNome', 'C9400', 'AUTO', 2, NULL, 25, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('CREA_LUO_LIVELLO_AUTORITA', 'Opzione', NULL, 'ricerca.livelloAutorita', 'IC003', 'LIVE', 1, NULL, 9, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GA_CREA_ORD_ESERCIZIO', 'String', 4, 'ordine.label.esercizio', 'A1200', NULL, 1, NULL, 34, 'acquisizioniLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GA_CREA_ORD_CAPITOLO', 'String', 16, 'ordine.label.capitolo', 'A1200', NULL, 2, NULL, 34, 'acquisizioniLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GA_CREA_ORD_SEZIONE', 'String', 7, 'ricerca.label.codSezione', 'A1200', NULL, 3, NULL, 34, 'acquisizioniLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GA_RIC_ORD_ORDINAMENTO', 'Opzione', NULL, 'ricerca.label.ordinamento', 'A1100', 'OAOR', 1, NULL, 27, 'acquisizioniLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GA_RIC_ORD_ELEM_BLOCCHI', 'String', 5, 'ricerca.label.elembloccoshort', 'A1100', NULL, 2, NULL, 27, 'acquisizioniLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GA_CREA_ORD_FORN_TIPO_A', 'String', 10, 'ricerca.label.codiceForn', 'A1210', NULL, 1, NULL, 28, 'acquisizioniLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GA_CREA_ORD_FORN_TIPO_V', 'String', 10, 'ricerca.label.codiceForn', 'A1210', NULL, 1, NULL, 29, 'acquisizioniLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GA_CREA_ORD_FORN_TIPO_L', 'String', 10, 'ricerca.label.codiceForn', 'A1220', NULL, 1, NULL, 30, 'acquisizioniLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GA_CREA_ORD_FORN_TIPO_D', 'String', 10, 'ricerca.label.codiceForn', 'A1220', NULL, 1, NULL, 31, 'acquisizioniLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GA_CREA_ORD_FORN_TIPO_C', 'String', 10, 'ricerca.label.codiceForn', 'A1230', NULL, 1, NULL, 32, 'acquisizioniLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GA_CREA_ORD_FORN_TIPO_R', 'String', 10, 'ricerca.label.codiceForn', 'A1240', NULL, 1, NULL, 33, 'acquisizioniLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GA_RIC_ORD_STATO_APERTO', 'Check', NULL, 'ordine.label.aperto', 'A1100', NULL, 3, 18, 27, 'acquisizioniLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GA_RIC_ORD_STATO_CHIUSO', 'Check', NULL, 'ordine.label.chiuso', 'A1100', NULL, 4, 18, 27, 'acquisizioniLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GA_RIC_ORD_STATO_ANNULLATO', 'Check', NULL, 'ordine.label.annullato', 'A1100', NULL, 5, 18, 27, 'acquisizioniLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_MODELLO_ETICH', 'string', 30, 'documentofisico.nomeModelloT', 'CD000', NULL, 1, NULL, 35, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('GDF_NRO_COPIE_ETICH', 'string', 2, 'documentofisico.numCopieT', 'CD000', NULL, 2, NULL, 35, 'documentoFisicoLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_AUT_NOME_INIZIO', 'Radio', NULL, 'ricerca.inizio', 'IC002', NULL, 1, 3, 4, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_AUT_NOME_PUNTUALE', 'Radio', NULL, 'ricerca.intero', 'IC002', NULL, 2, 3, 4, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_AUT_NOME_PAROLE', 'Radio', NULL, 'riverca.parole', 'IC002', NULL, 3, 3, 4, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RICERCA_TUTTI_NOMI', 'Radio', NULL, 'ricerca.tuttiNomi', 'IC002', NULL, 4, 4, 4, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RICERCA_NOME_PERSONALE', 'Radio', NULL, 'ricerca.nomePersonale', 'IC002', NULL, 5, 4, 4, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RICERCA_NOME_COLLETTIVO', 'Radio', NULL, 'ricerca.nomeCollettivo', 'IC002', NULL, 6, 4, 4, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_AUT_FORMA_TUTTI', 'Radio', NULL, 'ricerca.tuttiNomi', 'IC002', NULL, 7, 5, 4, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_AUT_FORMA_ACCETTATA', 'Radio', NULL, 'ricerca.autore', 'IC002', NULL, 8, 5, 4, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_AUT_FORMA_RINVIO', 'Radio', NULL, 'ricerca.rinvio', 'IC002', NULL, 9, 5, 4, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_AUT_ELEMENTI_BLOCCHI', 'String', 5, 'ricerca.elementiBlocco', 'IC002', NULL, 10, NULL, 4, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_AUT_ORDINAMENTO', 'Opzione', NULL, 'ricerca.ordinamento', 'IC002', 'OIAU', 11, NULL, 4, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_AUT_LIVELLO_POLO', 'Check', NULL, 'ricerca.polo', 'IC002', NULL, 12, 6, 4, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_AUT_LIVELLO_INDICE', 'Check', NULL, 'ricerca.indice', 'IC002', NULL, 13, 6, 4, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('CREA_AUT_LIVELLO_AUTORITA', 'Opzione', NULL, 'ricerca.livelloAutorita', 'ICR02', 'LIVE', 1, NULL, 5, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('CREA_AUT_TIPO_NOME', 'Opzione', NULL, 'sintetica.tiponome', 'ICR02', 'AUTO', 2, NULL, 5, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_MAR_ELEMENTI_BLOCCHI', 'String', 5, 'ricerca.elementiBlocco', 'IC002', NULL, 1, NULL, 6, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_MAR_ORDINAMENTO', 'Opzione', NULL, 'ricerca.ordinamento', 'IC002', 'OIMA', 2, NULL, 6, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_MAR_LIVELLO_POLO', 'Check', NULL, 'ricerca.polo', 'IC002', NULL, 3, 7, 6, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_MAR_LIVELLO_INDICE', 'Check', NULL, 'ricerca.indice', 'IC002', NULL, 4, 7, 6, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('CREA_MAR_LIVELLO_AUTORITA', 'Opzione', NULL, 'ricerca.livelloAutorita', 'ICR02', 'LIVE', 1, NULL, 7, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_LUO_ELEMENTI_BLOCCHI', 'String', 5, 'ricerca.elementiBlocco', 'IC002', NULL, 1, NULL, 8, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_LUO_ORDINAMENTO', 'Opzione', NULL, 'ricerca.ordinamento', 'IC002', 'OIMA', 2, NULL, 8, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_LUO_LIVELLO_POLO', 'Check', NULL, 'ricerca.polo', 'IC002', NULL, 3, 8, 8, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_LUO_LIVELLO_INDICE', 'Check', NULL, 'ricerca.indice', 'IC002', NULL, 4, 8, 8, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_LUO_PUNTUALE', 'Check', NULL, 'ricerca.puntuale', 'IC002', NULL, 5, NULL, 8, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('CREA_LUO_PAESE', 'Opzione', NULL, 'ricerca.paese', 'ICR02', 'PAES', 2, NULL, 9, 'gestioneBibliograficaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_SOG_TESTO_INIZIALE', 'Radio', NULL, 'gestionesemantica.soggetto.iniziale', 'IC002', NULL, 1, 10, 11, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_SOG_TESTO_INTERO', 'Radio', NULL, 'gestionesemantica.soggetto.intero', 'IC002', NULL, 2, 10, 11, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_SOG_TESTO_PAROLA', 'Radio', NULL, 'gestionesemantica.soggetto.parole', 'IC002', NULL, 3, 10, 11, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_SOG_POS_DESCR', 'Opzione', NULL, 'gestionesemantica.soggetto.PosizioneDescrittoreNelSoggetto', 'IC002', 'OSPO', 4, NULL, 11, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_SOG_ELEMENTI_BLOCCHI', 'String', 5, 'gestionesemantica.soggetto.elementoPerBlocco', 'IC002', NULL, 5, NULL, 11, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_SOG_ORDINAMENTO', 'Opzione', NULL, 'gestionesemantica.soggetto.ordinamento', 'IC002', 'OSSO', 6, NULL, 11, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_SOG_LIVELLO_POLO', 'Radio', NULL, 'gestionesemantica.soggetto.polo', 'IC002', NULL, 7, 9, 11, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_SOG_LIVELLO_INDICE', 'Radio', NULL, 'gestionesemantica.soggetto.indice', 'IC002', NULL, 8, 9, 11, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_DES_TESTO_INIZIALE', 'Radio', NULL, 'gestionesemantica.soggetto.iniziale', 'IC002', NULL, 1, 11, 13, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_DES_TESTO_INTERO', 'Radio', NULL, 'gestionesemantica.soggetto.intero', 'IC002', NULL, 2, 11, 13, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_CLA_SIMBOLO_PUNTUALE', 'Check', NULL, 'ricerca.puntuale', 'IC002', NULL, 1, 13, 15, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_CLA_ELEMENTI_BLOCCHI', 'String', 5, 'ricerca.elementoPerBlocco', 'IC002', NULL, 2, NULL, 15, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_CLA_SIMBOLO_ORDINAMENTO', 'Opzione', NULL, 'ricerca.ordinamento', 'IC002', 'OSCL', 3, NULL, 15, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_CLA_LIVELLO_POLO', 'Radio', NULL, 'ricerca.polo', 'IC002', NULL, 4, 12, 15, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_CLA_LIVELLO_INDICE', 'Radio', NULL, 'ricerca.indice', 'IC002', NULL, 5, 12, 15, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_THE_TERMINE_PUNTUALE', 'Check', NULL, 'gestionesemantica.thesauro.puntuale', 'IC002', NULL, 1, 14, 17, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_THE_ELEMENTI_BLOCCHI', 'String', 5, 'ricerca.elementoPerBlocco', 'IC002', NULL, 2, NULL, 17, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('RIC_THE_ORDINAMENTO', 'Opzione', NULL, 'ricerca.ordinamento', 'IC002', 'OSSO', 3, NULL, 17, 'gestioneSemanticaLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('SER_FOLDER_EROGAZIONE', 'Opzione', 2, 'servizi.erogazione.folder', 'L0000', 'LVES', 1, NULL, (select _cfgdef.id_config from "sbnweb"."tbf_config_default" _cfgdef where _cfgdef.id_area_sezione='01_GSerErogazione'), 'serviziLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('SER_RIC_MOV_SVOLGIMENTO', 'Opzione', 1, 'servizi.erogazione.contesto', 'L0000', 'LTSS', 4, NULL, (select _cfgdef.id_config from "sbnweb"."tbf_config_default" _cfgdef where _cfgdef.id_area_sezione='01_GSerErogazione'), 'serviziLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('SER_RIC_MOV_STATO_RICH', 'Opzione', 1, 'servizi.erogazione.statoRichiesta', 'L0000', 'LSTS', 3, NULL, (select _cfgdef.id_config from "sbnweb"."tbf_config_default" _cfgdef where _cfgdef.id_area_sezione='01_GSerErogazione'), 'serviziLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('SER_RIC_MOV_TIPO_SERV', 'Opzione', 2, 'servizi.erogazione.tipoServizio', 'L0000', 'LTSE', 5, NULL, (select _cfgdef.id_config from "sbnweb"."tbf_config_default" _cfgdef where _cfgdef.id_area_sezione='01_GSerErogazione'), 'serviziLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('SER_RIC_MOV_STATO_MOV', 'Opzione', 1, 'servizi.erogazione.statoMovimento', 'L0000', 'LSTM', 2, NULL, (select _cfgdef.id_config from "sbnweb"."tbf_config_default" _cfgdef where _cfgdef.id_area_sezione='01_GSerErogazione'), 'serviziLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('SER_RIC_MOV_ATTIVITA', 'Opzione', 2, 'servizi.erogazione.attivita', 'L0000', 'LATT', 6, NULL, (select _cfgdef.id_config from "sbnweb"."tbf_config_default" _cfgdef where _cfgdef.id_area_sezione='01_GSerErogazione'), 'serviziLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('SER_RIC_MOV_MOD_EROG', 'Opzione', 1, 'servizi.erogazione.modErogazione', 'L0000', 'LMER', 7, NULL, (select _cfgdef.id_config from "sbnweb"."tbf_config_default" _cfgdef where _cfgdef.id_area_sezione='01_GSerErogazione'), 'serviziLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('SER_RIC_MOV_ORD_LISTA', 'Opzione', 1, 'servizi.erogazione.ord.listeTematiche', 'L0000', 'OLMO', 8, NULL, (select _cfgdef.id_config from "sbnweb"."tbf_config_default" _cfgdef where _cfgdef.id_area_sezione='01_GSerErogazione'), 'serviziLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('SER_RIC_ORD_LISTA_PRENOTAZIONI', 'Opzione', 20, 'servizi.erogazione.ord.Prenotazioni', 'L0000', 'OLPR', 9, NULL, (select _cfgdef.id_config from "sbnweb"."tbf_config_default" _cfgdef where _cfgdef.id_area_sezione='01_GSerErogazione'), 'serviziLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('SER_RIC_ORD_LISTA_GIACENZE', 'Opzione', 20, 'servizi.erogazione.ord.Giacenze', 'L0000', 'OLGI', 10, NULL, (select _cfgdef.id_config from "sbnweb"."tbf_config_default" _cfgdef where _cfgdef.id_area_sezione='01_GSerErogazione'), 'serviziLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('SER_RIC_ORD_LISTA_PROROGHE', 'Opzione', 20, 'servizi.erogazione.ord.Proroghe', 'L0000', 'OLPO', 11, NULL, (select _cfgdef.id_config from "sbnweb"."tbf_config_default" _cfgdef where _cfgdef.id_area_sezione='01_GSerErogazione'), 'serviziLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('SER_RIC_ORD_LISTA_SOLLECITI', 'Opzione', 20, 'servizi.erogazione.ord.Solleciti', 'L0000', 'OLSO', 12, NULL, (select _cfgdef.id_config from "sbnweb"."tbf_config_default" _cfgdef where _cfgdef.id_area_sezione='01_GSerErogazione'), 'serviziLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('SER_RIC_MOV_ELEM_BLOCCHI', 'String', 5, 'servizi.label.elementiPerBlocco', 'L0000', NULL, 12, NULL, (select _cfgdef.id_config from "sbnweb"."tbf_config_default" _cfgdef where _cfgdef.id_area_sezione='01_GSerErogazione'), 'serviziLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('SER_RIC_UTE_ELEM_BLOCCHI', 'String', 5, 'servizi.label.elementiPerBlocco', 'L0000', NULL, 1, NULL, (select _cfgdef.id_config from "sbnweb"."tbf_config_default" _cfgdef where _cfgdef.id_area_sezione='02_GSerCercaUtenti'), 'serviziLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('SER_RIC_UTE_ORD_LISTA', 'Opzione', 20, 'servizi.label.ordinamento', 'L0000', 'OLUT', 2, NULL, (select _cfgdef.id_config from "sbnweb"."tbf_config_default" _cfgdef where _cfgdef.id_area_sezione='02_GSerCercaUtenti'), 'serviziLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('SER_INS_UTE_NAZIONE', 'Opzione', 20, 'servizi.utenti.nazionalitaCittadinanza', 'L0000', 'PAES', 1, NULL, (select _cfgdef.id_config from "sbnweb"."tbf_config_default" _cfgdef where _cfgdef.id_area_sezione='03_GSerCreaUtenti'), 'serviziLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('SER_INS_UTE_TIPO_AUT', 'String', 3, 'servizi.utenti.tipoAutorizzazione', 'L0000', NULL, 2, NULL, (select _cfgdef.id_config from "sbnweb"."tbf_config_default" _cfgdef where _cfgdef.id_area_sezione='03_GSerCreaUtenti'), 'serviziLabels');
INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('PER_KARDEX_ELEM_BLOCCHI', 'String', 5, 'servizi.label.elementiPerBlocco', 'P0000', NULL, 1, NULL, (select _cfgdef.id_config from "sbnweb"."tbf_config_default" _cfgdef where _cfgdef.id_area_sezione='01_GPer_Kardex'), 'serviziLabels');

INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('SER_RIC_MOV_SEZIONE_COLL', 'String', 10, 'documentofisico.sezioneT', 'L0000', NULL, 14, NULL, (select _cfgdef.id_config from "sbnweb"."tbf_config_default" _cfgdef where _cfgdef.id_area_sezione='01_GSerErogazione'), 'documentoFisicoLabels');



/*AGGIUSTA ABBINAMENTI TRA DEFAULT E RELATIVI CONFIG*/
update "sbnweb"."tbf_default" set tbf_config_default__id_config =
(select _cd.id_config from  "sbnweb"."tbf_config_default" _cd 
 where  
    case not "sbnweb"."tbf_default".key isnull 
	when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_TIT_' then _cd.id_area_sezione='01_GestBibTitolo' and _cd.codice_attivita='IC001'
	when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_AUT_' or substring("sbnweb"."tbf_default".key,1,8) = 'RICERCA_' then _cd.parametro_attivita='AU' and _cd.codice_attivita='IC002'
	when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_MAR_' then _cd.parametro_attivita='MA' and _cd.codice_attivita='IC002'
    when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_LUO_' then _cd.parametro_attivita='LU' and _cd.codice_attivita='IC002'
	when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_SOG_' then _cd.parametro_attivita='SO' and _cd.codice_attivita='IC002'
    when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_DES_' then _cd.parametro_attivita='DE' and _cd.codice_attivita='IC002'
	when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_CLA_' then _cd.parametro_attivita='CL' and _cd.codice_attivita='IC002'
    when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_THE_' then _cd.parametro_attivita='TH' and _cd.codice_attivita='IC002'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_TIT' then _cd.codice_attivita='ICR01'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_AUT' then _cd.parametro_attivita='AU' and _cd.codice_attivita='ICR02'
    When substring("sbnweb"."tbf_default".key,1,8) = 'CREA_MAR' then _cd.parametro_attivita='MA' and _cd.codice_attivita='ICR02'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_LUO' then _cd.parametro_attivita='LU' and _cd.codice_attivita='ICR02'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_SOG' then _cd.parametro_attivita='SO' and _cd.codice_attivita='ICR02'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_DES' then _cd.parametro_attivita='DE' and _cd.codice_attivita='ICR02'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_CLA' then _cd.parametro_attivita='CL' and _cd.codice_attivita='ICR02'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_THE' then _cd.parametro_attivita='TH' and _cd.codice_attivita='ICR02'
   	when substring("sbnweb"."tbf_default".key,1,6) = 'GA_RIC'   then _cd.codice_attivita = 'A1100'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_A' then _cd.id_area_sezione = '01_GAcqCreaOrdineAForn'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_C' then _cd.id_area_sezione = '01_GAcqCreaOrdineCForn'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_D' then _cd.id_area_sezione = '01_GAcqCreaOrdineDForn'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_L' then _cd.id_area_sezione = '01_GAcqCreaOrdineLForn'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_R' then _cd.id_area_sezione = '01_GAcqCreaOrdineRForn'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_V' then _cd.id_area_sezione = '01_GAcqCreaOrdineVForn'
   	when substring("sbnweb"."tbf_default".key,1,12) = 'GA_CREA_ORD_'     then _cd.id_area_sezione = '01_GAcqCreaOrdine'
   	when substring("sbnweb"."tbf_default".key,1,8)  = 'GDF_RIC_'         then _cd.id_area_sezione = '01_GDocFisCercaPosses'
   	when substring("sbnweb"."tbf_default".key,1,13) = 'GDF_CREA_POSS'    then _cd.id_area_sezione = '01_GDocFisCreaPosses'
    when "sbnweb"."tbf_default".key IN ('GDF_TP_PRG_INV','GDF_CD_SERIE') then _cd.id_area_sezione = '01_GDocFisInventario'
    when "sbnweb"."tbf_default".key = 'GDF_SEZ_COLL'                     then _cd.id_area_sezione = '01_GDocFisCollocaInv'
    when "sbnweb"."tbf_default".codice_attivita = 'C4000' then _cd.codice_attivita = 'C4000'
    when "sbnweb"."tbf_default".codice_attivita = 'CD000' then _cd.codice_attivita = 'CD000'
    when "sbnweb"."tbf_default".codice_attivita = 'C5000' and not "sbnweb"."tbf_default".key IN ('GDF_TP_PRG_INV','GDF_CD_SERIE')
                                                               then _cd.id_area_sezione = '01_GDocFisModificaInv'
   	when substring("sbnweb"."tbf_default".key,1,12) = 'SER_RIC_UTE_' then _cd.id_area_sezione = '02_GSerCercaUtenti'
   	when substring("sbnweb"."tbf_default".key,1,12) = 'SER_INS_UTE_' then _cd.id_area_sezione = '03_GSerCreaUtenti'
 	when substring("sbnweb"."tbf_default".key,1,4) = 'SER_' and not substring("sbnweb"."tbf_default".key,8,5) ='_UTE_' then _cd.id_area_sezione = '01_GSerErogazione'
    
end )
where tbf_config_default__id_config <> 
(select _cd.id_config 
 from  "sbnweb"."tbf_config_default" _cd  
 where  
    case not "sbnweb"."tbf_default".key isnull 
	when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_TIT_' then _cd.id_area_sezione='01_GestBibTitolo' and _cd.codice_attivita='IC001'
	when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_AUT_' or substring("sbnweb"."tbf_default".key,1,8) = 'RICERCA_' then _cd.parametro_attivita='AU' and _cd.codice_attivita='IC002'
	when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_MAR_' then _cd.parametro_attivita='MA' and _cd.codice_attivita='IC002'
    when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_LUO_' then _cd.parametro_attivita='LU' and _cd.codice_attivita='IC002'
	when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_SOG_' then _cd.parametro_attivita='SO' and _cd.codice_attivita='IC002'
    when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_DES_' then _cd.parametro_attivita='DE' and _cd.codice_attivita='IC002'
	when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_CLA_' then _cd.parametro_attivita='CL' and _cd.codice_attivita='IC002'
    when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_THE_' then _cd.parametro_attivita='TH' and _cd.codice_attivita='IC002'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_TIT' then _cd.codice_attivita='ICR01'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_AUT' then _cd.parametro_attivita='AU' and _cd.codice_attivita='ICR02'
    When substring("sbnweb"."tbf_default".key,1,8) = 'CREA_MAR' then _cd.parametro_attivita='MA' and _cd.codice_attivita='ICR02'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_LUO' then _cd.parametro_attivita='LU' and _cd.codice_attivita='ICR02'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_SOG' then _cd.parametro_attivita='SO' and _cd.codice_attivita='ICR02'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_DES' then _cd.parametro_attivita='DE' and _cd.codice_attivita='ICR02'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_CLA' then _cd.parametro_attivita='CL' and _cd.codice_attivita='ICR02'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_THE' then _cd.parametro_attivita='TH' and _cd.codice_attivita='ICR02'
   	when substring("sbnweb"."tbf_default".key,1,6) = 'GA_RIC'   then _cd.codice_attivita = 'A1100'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_A' then _cd.id_area_sezione = '01_GAcqCreaOrdineAForn'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_C' then _cd.id_area_sezione = '01_GAcqCreaOrdineCForn'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_D' then _cd.id_area_sezione = '01_GAcqCreaOrdineDForn'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_L' then _cd.id_area_sezione = '01_GAcqCreaOrdineLForn'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_R' then _cd.id_area_sezione = '01_GAcqCreaOrdineRForn'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_V' then _cd.id_area_sezione = '01_GAcqCreaOrdineVForn'
   	when substring("sbnweb"."tbf_default".key,1,12) = 'GA_CREA_ORD_'     then _cd.id_area_sezione = '01_GAcqCreaOrdine'
   	when substring("sbnweb"."tbf_default".key,1,8)  = 'GDF_RIC_'         then _cd.id_area_sezione = '01_GDocFisCercaPosses'
   	when substring("sbnweb"."tbf_default".key,1,13) = 'GDF_CREA_POSS'    then _cd.id_area_sezione = '01_GDocFisCreaPosses'
    when "sbnweb"."tbf_default".key IN ('GDF_TP_PRG_INV','GDF_CD_SERIE') then _cd.id_area_sezione = '01_GDocFisInventario'
    when "sbnweb"."tbf_default".key = 'GDF_SEZ_COLL'                     then _cd.id_area_sezione = '01_GDocFisCollocaInv'
    when "sbnweb"."tbf_default".codice_attivita = 'C4000' then _cd.codice_attivita = 'C4000'
    when "sbnweb"."tbf_default".codice_attivita = 'CD000' then _cd.codice_attivita = 'CD000'
    when "sbnweb"."tbf_default".codice_attivita = 'C5000' and not "sbnweb"."tbf_default".key IN ('GDF_TP_PRG_INV','GDF_CD_SERIE')
                                                               then _cd.id_area_sezione = '01_GDocFisModificaInv'
   	when substring("sbnweb"."tbf_default".key,1,12) = 'SER_RIC_UTE_' then _cd.id_area_sezione = '02_GSerCercaUtenti'
   	when substring("sbnweb"."tbf_default".key,1,12) = 'SER_INS_UTE_' then _cd.id_area_sezione = '03_GSerCreaUtenti'
 	when substring("sbnweb"."tbf_default".key,1,4) = 'SER_' and not substring("sbnweb"."tbf_default".key,8,5) ='_UTE_' then _cd.id_area_sezione = '01_GSerErogazione'
    
end);


/*AGGIUSTAMENTO FINALE DEI DEFAULT*/
/*
select _tbf_default."key",_tbf_default.codice_attivita, _tbf_default.id_etichetta, _tbf_default.tbf_config_default__id_config, 
 _cd.id_config 
 from "sbnweb"."tbf_default" _tbf_default
inner join "sbnweb"."tbf_config_default" _cd ON SUBSTRING(_cd.codice_attivita,1,1)=SUBSTRING(_tbf_default.codice_attivita,1,1)
where 
case not _tbf_default.key isnull 
	when substring(_tbf_default.key,1,8) = 'RIC_TIT_' then _cd.id_area_sezione='01_GestBibTitolo' and _cd.codice_attivita='IC001'
	when substring(_tbf_default.key,1,8) = 'RIC_AUT_' or substring(_tbf_default.key,1,8) = 'RICERCA_' then _cd.parametro_attivita='AU' and _cd.codice_attivita='IC002'
	when substring(_tbf_default.key,1,8) = 'RIC_MAR_' then _cd.parametro_attivita='MA' and _cd.codice_attivita='IC002'
    when substring(_tbf_default.key,1,8) = 'RIC_LUO_' then _cd.parametro_attivita='LU' and _cd.codice_attivita='IC002'
	when substring(_tbf_default.key,1,8) = 'RIC_SOG_' then _cd.parametro_attivita='SO' and _cd.codice_attivita='IC002'
    when substring(_tbf_default.key,1,8) = 'RIC_DES_' then _cd.parametro_attivita='DE' and _cd.codice_attivita='IC002'
	when substring(_tbf_default.key,1,8) = 'RIC_CLA_' then _cd.parametro_attivita='CL' and _cd.codice_attivita='IC002'
    when substring(_tbf_default.key,1,8) = 'RIC_THE_' then _cd.parametro_attivita='TH' and _cd.codice_attivita='IC002'
    when substring(_tbf_default.key,1,8) = 'CREA_TIT' then _cd.codice_attivita='ICR01'
    when substring(_tbf_default.key,1,8) = 'CREA_AUT' then _cd.parametro_attivita='AU' and _cd.codice_attivita='ICR02'
    When substring(_tbf_default.key,1,8) = 'CREA_MAR' then _cd.parametro_attivita='MA' and _cd.codice_attivita='ICR02'
    when substring(_tbf_default.key,1,8) = 'CREA_LUO' then _cd.parametro_attivita='LU' and _cd.codice_attivita='ICR02'
    when substring(_tbf_default.key,1,8) = 'CREA_SOG' then _cd.parametro_attivita='SO' and _cd.codice_attivita='ICR02'
    when substring(_tbf_default.key,1,8) = 'CREA_DES' then _cd.parametro_attivita='DE' and _cd.codice_attivita='ICR02'
    when substring(_tbf_default.key,1,8) = 'CREA_CLA' then _cd.parametro_attivita='CL' and _cd.codice_attivita='ICR02'
    when substring(_tbf_default.key,1,8) = 'CREA_THE' then _cd.parametro_attivita='TH' and _cd.codice_attivita='ICR02'
   	when substring(_tbf_default.key,1,6) = 'GA_RIC'   then _cd.codice_attivita = 'A1100'
   	when _tbf_default.key = 'GA_CREA_ORD_FORN_TIPO_A' then _cd.id_area_sezione = '01_GAcqCreaOrdineAForn'
   	when _tbf_default.key = 'GA_CREA_ORD_FORN_TIPO_C' then _cd.id_area_sezione = '01_GAcqCreaOrdineCForn'
   	when _tbf_default.key = 'GA_CREA_ORD_FORN_TIPO_D' then _cd.id_area_sezione = '01_GAcqCreaOrdineDForn'
   	when _tbf_default.key = 'GA_CREA_ORD_FORN_TIPO_L' then _cd.id_area_sezione = '01_GAcqCreaOrdineLForn'
   	when _tbf_default.key = 'GA_CREA_ORD_FORN_TIPO_R' then _cd.id_area_sezione = '01_GAcqCreaOrdineRForn'
   	when _tbf_default.key = 'GA_CREA_ORD_FORN_TIPO_V' then _cd.id_area_sezione = '01_GAcqCreaOrdineVForn'
   	when substring(_tbf_default.key,1,12) = 'GA_CREA_ORD_'     then _cd.id_area_sezione = '01_GAcqCreaOrdine'
   	when substring(_tbf_default.key,1,8)  = 'GDF_RIC_'         then _cd.id_area_sezione = '01_GDocFisCercaPosses'
   	when substring(_tbf_default.key,1,13) = 'GDF_CREA_POSS'    then _cd.id_area_sezione = '01_GDocFisCreaPosses'
    when _tbf_default.key IN ('GDF_TP_PRG_INV','GDF_CD_SERIE') then _cd.id_area_sezione = '01_GDocFisInventario'
    when _tbf_default.key = 'GDF_SEZ_COLL'                     then _cd.id_area_sezione = '01_GDocFisCollocaInv'
    when _tbf_default.codice_attivita = 'C4000' then _cd.codice_attivita = 'C4000'
    when _tbf_default.codice_attivita = 'CD000' then _cd.codice_attivita = 'CD000'
    when _tbf_default.codice_attivita = 'C5000' and not _tbf_default.key IN ('GDF_TP_PRG_INV','GDF_CD_SERIE')
                                                               then _cd.id_area_sezione = '01_GDocFisModificaInv'
   	when substring(_tbf_default.key,1,12) = 'SER_RIC_UTE_' then _cd.id_area_sezione = '02_GSerCercaUtenti'
   	when substring(_tbf_default.key,1,12) = 'SER_INS_UTE_' then _cd.id_area_sezione = '03_GSerCreaUtenti'
 	when substring(_tbf_default.key,1,4) = 'SER_' and not substring(_tbf_default.key,8,5) ='_UTE_' then _cd.id_area_sezione = '01_GSerErogazione'
    
end 
and _tbf_default.tbf_config_default__id_config<> _cd.id_config 
order by _tbf_default.key;
*/



update "sbnweb"."tbf_default" set tbf_config_default__id_config =
(select _cd.id_config from  "sbnweb"."tbf_config_default" _cd 
 where  
    case not "sbnweb"."tbf_default".key isnull 
	when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_TIT_' then _cd.id_area_sezione='01_GestBibTitolo' and _cd.codice_attivita='IC001'
	when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_AUT_' or substring("sbnweb"."tbf_default".key,1,8) = 'RICERCA_' then _cd.parametro_attivita='AU' and _cd.codice_attivita='IC002'
	when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_MAR_' then _cd.parametro_attivita='MA' and _cd.codice_attivita='IC002'
    when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_LUO_' then _cd.parametro_attivita='LU' and _cd.codice_attivita='IC002'
	when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_SOG_' then _cd.parametro_attivita='SO' and _cd.codice_attivita='IC002'
    when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_DES_' then _cd.parametro_attivita='DE' and _cd.codice_attivita='IC002'
	when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_CLA_' then _cd.parametro_attivita='CL' and _cd.codice_attivita='IC002'
    when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_THE_' then _cd.parametro_attivita='TH' and _cd.codice_attivita='IC002'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_TIT' then _cd.codice_attivita='ICR01'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_AUT' then _cd.parametro_attivita='AU' and _cd.codice_attivita='ICR02'
    When substring("sbnweb"."tbf_default".key,1,8) = 'CREA_MAR' then _cd.parametro_attivita='MA' and _cd.codice_attivita='ICR02'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_LUO' then _cd.parametro_attivita='LU' and _cd.codice_attivita='ICR02'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_SOG' then _cd.parametro_attivita='SO' and _cd.codice_attivita='ICR02'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_DES' then _cd.parametro_attivita='DE' and _cd.codice_attivita='ICR02'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_CLA' then _cd.parametro_attivita='CL' and _cd.codice_attivita='ICR02'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_THE' then _cd.parametro_attivita='TH' and _cd.codice_attivita='ICR02'
   	when substring("sbnweb"."tbf_default".key,1,6) = 'GA_RIC'   then _cd.codice_attivita = 'A1100'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_A' then _cd.id_area_sezione = '01_GAcqCreaOrdineAForn'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_C' then _cd.id_area_sezione = '01_GAcqCreaOrdineCForn'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_D' then _cd.id_area_sezione = '01_GAcqCreaOrdineDForn'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_L' then _cd.id_area_sezione = '01_GAcqCreaOrdineLForn'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_R' then _cd.id_area_sezione = '01_GAcqCreaOrdineRForn'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_V' then _cd.id_area_sezione = '01_GAcqCreaOrdineVForn'
   	when substring("sbnweb"."tbf_default".key,1,12) = 'GA_CREA_ORD_'     then _cd.id_area_sezione = '01_GAcqCreaOrdine'
   	when substring("sbnweb"."tbf_default".key,1,8)  = 'GDF_RIC_'         then _cd.id_area_sezione = '01_GDocFisCercaPosses'
   	when substring("sbnweb"."tbf_default".key,1,13) = 'GDF_CREA_POSS'    then _cd.id_area_sezione = '01_GDocFisCreaPosses'
    when "sbnweb"."tbf_default".key IN ('GDF_TP_PRG_INV','GDF_CD_SERIE') then _cd.id_area_sezione = '01_GDocFisInventario'
    when "sbnweb"."tbf_default".key = 'GDF_SEZ_COLL'                     then _cd.id_area_sezione = '01_GDocFisCollocaInv'
    when "sbnweb"."tbf_default".codice_attivita = 'C4000' then _cd.codice_attivita = 'C4000'
    when "sbnweb"."tbf_default".codice_attivita = 'CD000' then _cd.codice_attivita = 'CD000'
    when "sbnweb"."tbf_default".codice_attivita = 'C5000' and not "sbnweb"."tbf_default".key IN ('GDF_TP_PRG_INV','GDF_CD_SERIE')
                                                               then _cd.id_area_sezione = '01_GDocFisModificaInv'
   	when substring("sbnweb"."tbf_default".key,1,12) = 'SER_RIC_UTE_' then _cd.id_area_sezione = '02_GSerCercaUtenti'
   	when substring("sbnweb"."tbf_default".key,1,12) = 'SER_INS_UTE_' then _cd.id_area_sezione = '03_GSerCreaUtenti'
 	when substring("sbnweb"."tbf_default".key,1,4) = 'SER_' and not substring("sbnweb"."tbf_default".key,8,5) ='_UTE_' then _cd.id_area_sezione = '01_GSerErogazione'
    
end )
where tbf_config_default__id_config <> 
(select _cd.id_config 
 from  "sbnweb"."tbf_config_default" _cd  
 where  
    case not "sbnweb"."tbf_default".key isnull 
	when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_TIT_' then _cd.id_area_sezione='01_GestBibTitolo' and _cd.codice_attivita='IC001'
	when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_AUT_' or substring("sbnweb"."tbf_default".key,1,8) = 'RICERCA_' then _cd.parametro_attivita='AU' and _cd.codice_attivita='IC002'
	when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_MAR_' then _cd.parametro_attivita='MA' and _cd.codice_attivita='IC002'
    when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_LUO_' then _cd.parametro_attivita='LU' and _cd.codice_attivita='IC002'
	when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_SOG_' then _cd.parametro_attivita='SO' and _cd.codice_attivita='IC002'
    when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_DES_' then _cd.parametro_attivita='DE' and _cd.codice_attivita='IC002'
	when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_CLA_' then _cd.parametro_attivita='CL' and _cd.codice_attivita='IC002'
    when substring("sbnweb"."tbf_default".key,1,8) = 'RIC_THE_' then _cd.parametro_attivita='TH' and _cd.codice_attivita='IC002'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_TIT' then _cd.codice_attivita='ICR01'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_AUT' then _cd.parametro_attivita='AU' and _cd.codice_attivita='ICR02'
    When substring("sbnweb"."tbf_default".key,1,8) = 'CREA_MAR' then _cd.parametro_attivita='MA' and _cd.codice_attivita='ICR02'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_LUO' then _cd.parametro_attivita='LU' and _cd.codice_attivita='ICR02'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_SOG' then _cd.parametro_attivita='SO' and _cd.codice_attivita='ICR02'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_DES' then _cd.parametro_attivita='DE' and _cd.codice_attivita='ICR02'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_CLA' then _cd.parametro_attivita='CL' and _cd.codice_attivita='ICR02'
    when substring("sbnweb"."tbf_default".key,1,8) = 'CREA_THE' then _cd.parametro_attivita='TH' and _cd.codice_attivita='ICR02'
   	when substring("sbnweb"."tbf_default".key,1,6) = 'GA_RIC'   then _cd.codice_attivita = 'A1100'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_A' then _cd.id_area_sezione = '01_GAcqCreaOrdineAForn'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_C' then _cd.id_area_sezione = '01_GAcqCreaOrdineCForn'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_D' then _cd.id_area_sezione = '01_GAcqCreaOrdineDForn'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_L' then _cd.id_area_sezione = '01_GAcqCreaOrdineLForn'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_R' then _cd.id_area_sezione = '01_GAcqCreaOrdineRForn'
   	when "sbnweb"."tbf_default".key = 'GA_CREA_ORD_FORN_TIPO_V' then _cd.id_area_sezione = '01_GAcqCreaOrdineVForn'
   	when substring("sbnweb"."tbf_default".key,1,12) = 'GA_CREA_ORD_'     then _cd.id_area_sezione = '01_GAcqCreaOrdine'
   	when substring("sbnweb"."tbf_default".key,1,8)  = 'GDF_RIC_'         then _cd.id_area_sezione = '01_GDocFisCercaPosses'
   	when substring("sbnweb"."tbf_default".key,1,13) = 'GDF_CREA_POSS'    then _cd.id_area_sezione = '01_GDocFisCreaPosses'
    when "sbnweb"."tbf_default".key IN ('GDF_TP_PRG_INV','GDF_CD_SERIE') then _cd.id_area_sezione = '01_GDocFisInventario'
    when "sbnweb"."tbf_default".key = 'GDF_SEZ_COLL'                     then _cd.id_area_sezione = '01_GDocFisCollocaInv'
    when "sbnweb"."tbf_default".codice_attivita = 'C4000' then _cd.codice_attivita = 'C4000'
    when "sbnweb"."tbf_default".codice_attivita = 'CD000' then _cd.codice_attivita = 'CD000'
    when "sbnweb"."tbf_default".codice_attivita = 'C5000' and not "sbnweb"."tbf_default".key IN ('GDF_TP_PRG_INV','GDF_CD_SERIE')
                                                               then _cd.id_area_sezione = '01_GDocFisModificaInv'
   	when substring("sbnweb"."tbf_default".key,1,12) = 'SER_RIC_UTE_' then _cd.id_area_sezione = '02_GSerCercaUtenti'
   	when substring("sbnweb"."tbf_default".key,1,12) = 'SER_INS_UTE_' then _cd.id_area_sezione = '03_GSerCreaUtenti'
 	when substring("sbnweb"."tbf_default".key,1,4) = 'SER_' and not substring("sbnweb"."tbf_default".key,8,5) ='_UTE_' then _cd.id_area_sezione = '01_GSerErogazione'
    
end);

INSERT INTO "sbnweb"."tbf_config_default" ("id_area_sezione", "parent", "seq_ordinamento", "codice_attivita", "parametro_attivita", "codice_modulo")
VALUES ('04_GSerServiziILL', '00_GSer', 4, 'L0000', NULL, NULL);

INSERT INTO "sbnweb"."tbf_default" ("key", "tipo", "lunghezza", "id_etichetta", "codice_attivita", "codice_tabella_validazione", "seq_ordinamento", "tbf_gruppi_defaultid", "tbf_config_default__id_config", "bundle")
VALUES ('SER_RIC_ILL_FOLDER', 'Opzione', 20, 'servizi.erogazione.ord.Solleciti', 'L0000', 'LIRB', 12, NULL, (select _cfgdef.id_config from "sbnweb"."tbf_config_default" _cfgdef where _cfgdef.id_area_sezione='04_GSerServiziILL'), 'serviziLabels');

COMMIT;