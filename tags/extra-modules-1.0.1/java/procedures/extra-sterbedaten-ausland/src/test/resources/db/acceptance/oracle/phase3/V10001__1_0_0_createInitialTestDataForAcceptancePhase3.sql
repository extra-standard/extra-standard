--------------------------------------------------------
-- Initiale Testdaten für Phase 3 In dem Scenario Sterbedatenabgleich für Akzeptanstest.
-- Oracle Database.
--------------------------------------------------------

Insert into EXECUTION
   (ID, START_TIME, END_TIME, PARAMETERS, PHASE, PROCEDURE_ID, LAST_TRANSITION_ID)
 Values
   (1, TO_DATE('01/17/2013 13:26:51', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/17/2013 13:26:52', 'MM/DD/YYYY HH24:MI:SS'), 'D:\eclipse-workspaces\extra\eXTra Full\procedures\extra-sterbedaten-ausland\target\test-classes\conf\phase2', 'PHASE2', 
    1, null);

Insert into COMMUNICATION_PROTOCOL
   (ID, INPUT_IDENTIFIER, OUTPUT_IDENTIFIER, HASHCODE, REQUEST_ID, RESPONSE_ID, RETURN_CODE, RETURN_TEXT, EXECUTION_ID, NEXT_PHASE_CONNECTION_ID, INPUT_DATA_QUALIFIER, STATUS_ID, SUBQUERY)
 Values
   (1, 'GT: 0', 'FIKELA.PRD.DS.10001', '27120928', '1', 
    '10001', 'C00', 'O.K.', 1, null, 
    'QUERY_CRITERIA', 7, 'IT');
    

    
    
Insert into COMMUNICATION_PROTOCOL
   (ID, INPUT_IDENTIFIER, OUTPUT_IDENTIFIER, HASHCODE, REQUEST_ID, RESPONSE_ID, RETURN_CODE, RETURN_TEXT, EXECUTION_ID, NEXT_PHASE_CONNECTION_ID, INPUT_DATA_QUALIFIER, STATUS_ID, SUBQUERY)
 Values
   (2, 'GT: 0', 'FIKELA.PRD.DS.10002', '1542096898', '1', 
    '10002', 'C00', 'O.K.', 1, null, 
    'QUERY_CRITERIA', 7, 'IT');


Insert into PHASE_CONNECTION
   (ID, NEXT_PHASE_QUALIFIER, SOURCE_COM_PROTOCOL_ID, STATUS_ID)
 Values
   (1, 'PHASE3', 1, 1);
   
Insert into PHASE_CONNECTION
   (ID, NEXT_PHASE_QUALIFIER, SOURCE_COM_PROTOCOL_ID, STATUS_ID)
 Values
   (2, 'PHASE3', 2, 1);
   
update COMMUNICATION_PROTOCOL 
set NEXT_PHASE_CONNECTION_ID = 
(select PHASE_CONNECTION.ID from PHASE_CONNECTION where PHASE_CONNECTION.SOURCE_COM_PROTOCOL_ID=1 )
where COMMUNICATION_PROTOCOL.ID = 1;

update COMMUNICATION_PROTOCOL 
set NEXT_PHASE_CONNECTION_ID = 
(select PHASE_CONNECTION.ID from PHASE_CONNECTION where PHASE_CONNECTION.SOURCE_COM_PROTOCOL_ID=2 )
where COMMUNICATION_PROTOCOL.ID = 2;

SELECT seq_execution_id.NEXTVAL FROM dual;      

SELECT seq_communication_protocol_id.NEXTVAL FROM dual;
SELECT seq_communication_protocol_id.NEXTVAL FROM dual;

SELECT seq_phase_connection_id.NEXTVAL FROM dual;
SELECT seq_phase_connection_id.NEXTVAL FROM dual;
    