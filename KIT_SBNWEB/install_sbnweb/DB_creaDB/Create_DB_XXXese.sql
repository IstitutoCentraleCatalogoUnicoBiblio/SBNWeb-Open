/*accedere come utente postgres/postgresadm e aprire il db postgres*/
-- se e' gia' stato creato il db di prova e' stato creato anche l'utente sbnweb
CREATE ROLE "sbnweb" SUPERUSER CREATEDB CREATEROLE LOGIN;
ALTER ROLE "sbnweb" PASSWORD 'sbnweb';
---
/*Creazione del database*/
CREATE DATABASE "sbnwebDbXXXese"
  WITH OWNER = "sbnweb"
    ENCODING = 'UTF8';

------- fine attività da fare come postgres ----


