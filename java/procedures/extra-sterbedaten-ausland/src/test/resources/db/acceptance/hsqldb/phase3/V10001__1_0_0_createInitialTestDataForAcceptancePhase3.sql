INSERT INTO EXECUTION VALUES(1,NULL,NULL,NULL,'D:\eclipse-workspaces\extra\eXTra Full\procedures\extra-sterbedaten-ausland\target\test-classes\conf\phase2','PHASE2','2013-01-17 17:15:47.800000',NULL,2);
INSERT INTO COMMUNICATION_PROTOCOL VALUES(1,'28136053','QUERY_CRITERIA','GT: 0','FIKELA.PRD.DS.10001','1','10001','C00','O.K.','IT',NULL,1,null,7)
INSERT INTO PHASE_CONNECTION VALUES(1,'PHASE3',1,1,NULL);
INSERT INTO COMMUNICATION_PROTOCOL VALUES(2,'1542096898','QUERY_CRITERIA','GT: 0','FIKELA.PRD.DS.10002','1','10002','C00','O.K.','IT',NULL,1,null,7)
INSERT INTO PHASE_CONNECTION VALUES(2,'PHASE3',2,1,NULL);
INSERT INTO COMMUNICATION_PROTOCOL VALUES(3,'1542096899','QUERY_CRITERIA','GT: 0','FIKELA.PRD.DS.10003','1','10003','C00','O.K.','IT',NULL,1,null,7)
INSERT INTO PHASE_CONNECTION VALUES(3,'PHASE3',3,1,NULL);

update COMMUNICATION_PROTOCOL 
set NEXT_PHASE_CONNECTION_ID = 
(select PHASE_CONNECTION.ID from PHASE_CONNECTION where PHASE_CONNECTION.SOURCE_COM_PROTOCOL_ID=1 )
where COMMUNICATION_PROTOCOL.ID = 1;

update COMMUNICATION_PROTOCOL 
set NEXT_PHASE_CONNECTION_ID = 
(select PHASE_CONNECTION.ID from PHASE_CONNECTION where PHASE_CONNECTION.SOURCE_COM_PROTOCOL_ID=2 )
where COMMUNICATION_PROTOCOL.ID = 2;

update COMMUNICATION_PROTOCOL 
set NEXT_PHASE_CONNECTION_ID = 
(select PHASE_CONNECTION.ID from PHASE_CONNECTION where PHASE_CONNECTION.SOURCE_COM_PROTOCOL_ID=3 )
where COMMUNICATION_PROTOCOL.ID = 3;

