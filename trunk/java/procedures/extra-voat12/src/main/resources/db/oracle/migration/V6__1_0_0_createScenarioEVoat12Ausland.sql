--------------------------------------------------------
-- Initiale Datenbankbefuellung Sterbedatenabgleich
-- Oracle Database.
--------------------------------------------------------


Insert into PROCEDURE_TYPE (ID, NAME)
 Values (seq_procedure_type_id.nextval, 'HollSendeBetrieb');
   
 
Insert into PROCEDURE_PHASE_CONFIGURATION
   (ID, PHASE, PROCEDURE_TYPE_ID, NEXT_PHASE_CONFIGURATION_ID)
 Values
   (seq_procephase_config_id.nextval,
    'PHASE1', 
    (select id from procedure_type where name = 'HollSendeBetrieb'),
    null);
     
Insert into PROCEDURE
   (ID, NAME, SHORT_KEY, MANDATOR_ID, PROCEDURE_TYPE_ID)
 Values
   (seq_procedure_id.nextval, 'Elektronischer Antrag auf Vorschusszahlung', 'Voat12', 
   (select id from MANDATOR where name = 'DRV'),
    (select id from PROCEDURE_TYPE where name = 'HollSendeBetrieb'));
    