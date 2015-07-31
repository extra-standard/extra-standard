--------------------------------------------------------
-- Update Voat12. Die Quitung Nachricht einrichten als Phase 2
-- Oracle Database.
--------------------------------------------------------
insert into PROCEDURE_PHASE_CONFIGURATION (ID, PHASE, PROCEDURE_TYPE_ID)
 Values
   (seq_procephase_config_id.nextval,
    'PHASE2', 
    (select id from procedure_type where name = 'HollSendeBetrieb') ); 
    
-- Update Phase 1. Als NEXT_PHASE_CONFIGURATION_ID die Pahese 2 setzen
update PROCEDURE_PHASE_CONFIGURATION
    set NEXT_PHASE_CONFIGURATION_ID = 
        (select id from PROCEDURE_PHASE_CONFIGURATION 
         where PROCEDURE_PHASE_CONFIGURATION.PROCEDURE_TYPE_ID = 
                (select ID from PROCEDURE_TYPE where name = 'HollSendeBetrieb')
         and   PROCEDURE_PHASE_CONFIGURATION.PHASE = 'PHASE2')
    where PROCEDURE_PHASE_CONFIGURATION.PHASE = 'PHASE1'
    and PROCEDURE_PHASE_CONFIGURATION.PROCEDURE_TYPE_ID = 
        (select id from procedure_type where name = 'HollSendeBetrieb')