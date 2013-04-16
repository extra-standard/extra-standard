--------------------------------------------------------
-- Initiale Datenbankbefuellung Sterbedatenabgleich
-- Oracle Database.
--------------------------------------------------------

Insert into MANDATOR (ID, NAME) Values (seq_mandator_id.nextval, 'DRV');
   
Insert into PROCEDURE_TYPE (ID, NAME)
 Values (seq_procedure_type_id.nextval, 'STERBEMELDUNG_AUSLAND');
   
Insert into PROCEDURE_PHASE_CONFIGURATION (ID, PHASE, PROCEDURE_TYPE_ID)
 Values
   (seq_procephase_config_id.nextval,
    'PHASE1', 
    (select id from procedure_type where name = 'STERBEMELDUNG_AUSLAND') );
   
Insert into PROCEDURE_PHASE_CONFIGURATION (ID, PHASE, PROCEDURE_TYPE_ID)
 Values
   (seq_procephase_config_id.nextval,
    'PHASE3', 
    (select id from procedure_type where name = 'STERBEMELDUNG_AUSLAND') );
   
Insert into PROCEDURE_PHASE_CONFIGURATION
   (ID, PHASE, PROCEDURE_TYPE_ID, NEXT_PHASE_CONFIGURATION_ID)
 Values
   (seq_procephase_config_id.nextval,
    'PHASE2', 
    (select id from procedure_type where name = 'STERBEMELDUNG_AUSLAND'),
     (select id from PROCEDURE_PHASE_CONFIGURATION 
         where PROCEDURE_PHASE_CONFIGURATION.PROCEDURE_TYPE_ID =
        (select id from PROCEDURE_TYPE where PROCEDURE_TYPE.NAME = 'STERBEMELDUNG_AUSLAND')
        and   PROCEDURE_PHASE_CONFIGURATION.PHASE = 'PHASE3'  ));
     
Insert into PROCEDURE
   (ID, NAME, SHORT_KEY, MANDATOR_ID, PROCEDURE_TYPE_ID)
 Values
   (seq_procedure_id.nextval, 'SterbemeldungAusland', 'STMELD_AUSL', 
   (select id from MANDATOR where name = 'DRV'),
    (select id from PROCEDURE_TYPE where name = 'STERBEMELDUNG_AUSLAND'));
    