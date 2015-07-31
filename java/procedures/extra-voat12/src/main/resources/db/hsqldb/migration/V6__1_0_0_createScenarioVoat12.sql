--------------------------------------------------------
-- Initiale Datenbankbefuellung Sterbedatenabgleich
-- HSQLDB Database.
--------------------------------------------------------

INSERT INTO MANDATOR VALUES(2,'DRV')

INSERT INTO PROCEDURE_TYPE VALUES(2,'HollSendeBetrieb')
INSERT INTO PROCEDURE VALUES(2,'Voat12','STMELD_AUSL',2,2)
INSERT INTO PROCEDURE_PHASE_CONFIGURATION VALUES(8,'PHASE2',NULL,2)
INSERT INTO PROCEDURE_PHASE_CONFIGURATION VALUES(7,'PHASE1',8,2)