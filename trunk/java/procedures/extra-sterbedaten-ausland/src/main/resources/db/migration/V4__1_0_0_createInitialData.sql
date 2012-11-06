--------------------------------------------------------
-- Initiale Datenbankbefuellung Sterbedatenabgleich
-- Oracle Database.
--------------------------------------------------------

Insert into MANDATOR (ID, NAME) Values (seq_mandator_id.nextval, 'DRV');
   
Insert into PROCEDURE_TYPE (ID, NAME)
 Values (seq_procedure_type_id.nextval, 'STERBEDATENAUS1');
Insert into PROCEDURE_TYPE (ID, NAME)
 Values (seq_procedure_type_id.nextval, 'STERBEDATENAUS2');
   
Insert into PROCEDURE_PHASE_CONFIGURATION (ID, PHASE, PROCEDURE_TYPE_ID)
 Values
   (seq_procephase_config_id.nextval,
    'PHASE1', 
    (select id from procedure_type where name = 'STERBEDATENAUS1') );
   
Insert into PROCEDURE_PHASE_CONFIGURATION (ID, PHASE, PROCEDURE_TYPE_ID)
 Values
   (seq_procephase_config_id.nextval,
    'PHASE2', 
    (select id from procedure_type where name = 'STERBEDATENAUS2') );
   
Insert into PROCEDURE_PHASE_CONFIGURATION
   (ID, PHASE, PROCEDURE_TYPE_ID, NEXT_PHASE_CONFIGURATION_ID)
 Values
   (seq_procephase_config_id.nextval,
    'PHASE1', 
    (select id from procedure_type where name = 'STERBEDATENAUS2'),
     (select id from PROCEDURE_PHASE_CONFIGURATION 
         where PROCEDURE_PHASE_CONFIGURATION.PROCEDURE_TYPE_ID =
        (select id from PROCEDURE_TYPE where PROCEDURE_TYPE.NAME = 'STERBEDATENAUS2')
        and   PROCEDURE_PHASE_CONFIGURATION.PHASE = 'PHASE2'  ));
     
Insert into PROCEDURE
   (ID, NAME, SHORT_KEY, MANDATOR_ID, PROCEDURE_TYPE_ID)
 Values
   (seq_procedure_id.nextval, 'Sterbedaten Ausland 1', 'PROC_STERBEDATENAUS1', 
   (select id from MANDATOR where name = 'DRV'),
    (select id from PROCEDURE_TYPE where name = 'STERBEDATENAUS1'));
    
Insert into PROCEDURE
   (ID, NAME, SHORT_KEY, MANDATOR_ID, PROCEDURE_TYPE_ID)
 Values
   (seq_procedure_id.nextval, 'Sterbedaten Ausland 2', 'PROC_STERBEDATENAUS2', 
   (select id from MANDATOR where name = 'DRV'),
    (select id from PROCEDURE_TYPE where name = 'STERBEDATENAUS2'));