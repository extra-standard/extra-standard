--------------------------------------------------------
-- Create Database DDL.
-- Oracle Database.
--------------------------------------------------------

-- DROP

--DROP TABLE EXECUTION CASCADE CONSTRAINTS;
--DROP TABLE COMMUNICATION_PROTOCOL CASCADE CONSTRAINTS;
--DROP TABLE MANDATOR CASCADE CONSTRAINTS;
--DROP TABLE PHASE_CONNECTION CASCADE CONSTRAINTS;
--DROP TABLE PROCEDURE CASCADE CONSTRAINTS;
--DROP TABLE PROCEDURE_PHASE_CONFIGURATION CASCADE CONSTRAINTS;
--DROP TABLE PROCEDURE_TYPE CASCADE CONSTRAINTS;
--DROP TABLE PROCESS_TRANSITION CASCADE CONSTRAINTS;
--DROP TABLE STATUS CASCADE CONSTRAINTS;


-- CREATE

CREATE TABLE EXECUTION 
    ( 
     id NUMBER (15)  NOT NULL , 
     start_time DATE  NOT NULL , 
     end_time DATE , 
     parameters CLOB , 
     phase VARCHAR2 (100) , 
     procedure_id NUMBER (15)  NOT NULL , 
     last_transition_id NUMBER (15) , 
     error_code VARCHAR2 (255) , 
     error_message CLOB 
    )
;


CREATE INDEX execution_ix_procedure ON EXECUTION 
    ( 
     procedure_id ASC 
    ) 
;

ALTER TABLE EXECUTION 
    ADD CONSTRAINT execution_PK PRIMARY KEY ( id ) ;


CREATE TABLE COMMUNICATION_PROTOCOL 
    ( 
     id NUMBER (15)  NOT NULL , 
     input_identifier VARCHAR2 (255) , 
     output_identifier VARCHAR2 (255) , 
     hashcode VARCHAR2 (100) , 
     request_id VARCHAR2 (255) , 
     response_id VARCHAR2 (255) , 
     return_code VARCHAR2 (255) , 
     return_text CLOB , 
     execution_id NUMBER (15)  NOT NULL , 
     next_phase_connection_id NUMBER (12) , 
     current_phase_connection_id NUMBER (12),
     input_data_qualifier VARCHAR2 (100),
     status_id NUMBER (10)
    ) 
;


CREATE INDEX comm_prot_idx_execution ON COMMUNICATION_PROTOCOL 
    ( 
     execution_id ASC 
    ) 
;
CREATE INDEX comm_prot_ix_request ON COMMUNICATION_PROTOCOL 
    ( 
     request_id ASC 
    ) 
;

ALTER TABLE COMMUNICATION_PROTOCOL 
    ADD CONSTRAINT communication_protocol_PK PRIMARY KEY ( id ) ;


CREATE TABLE MANDATOR 
    ( 
     id NUMBER (15)  NOT NULL , 
     name VARCHAR2 (250 CHAR) 
    ) 
;



ALTER TABLE MANDATOR 
    ADD CONSTRAINT "mandant PK" PRIMARY KEY ( id ) ;

ALTER TABLE MANDATOR 
    ADD CONSTRAINT mandant_un_name UNIQUE ( name ) ;


CREATE TABLE PHASE_CONNECTION 
    ( 
     id NUMBER (12)  NOT NULL , 
     next_phase_qualifier VARCHAR2 (20) , 
     source_com_protocol_id NUMBER (15)  NOT NULL , 
     target_com_protocol_id NUMBER (15) , 
     status_id NUMBER (10)  NOT NULL 
    ) 
;


CREATE INDEX phase_conn_idx_phase_status ON PHASE_CONNECTION 
    ( 
     next_phase_qualifier ASC , 
     status_id ASC 
    ) 
;
CREATE INDEX phase_conn_idx_target_com_prot ON PHASE_CONNECTION 
    ( 
     target_com_protocol_id ASC 
    ) 
;
CREATE INDEX phase_conn_idx_source_com_prot ON PHASE_CONNECTION 
    ( 
     source_com_protocol_id ASC 
    ) 
;

ALTER TABLE PHASE_CONNECTION 
    ADD CONSTRAINT phase_connection_pk PRIMARY KEY ( id ) ;


CREATE TABLE PROCEDURE 
    ( 
     id NUMBER (15)  NOT NULL , 
     name VARCHAR2 (255 CHAR)  NOT NULL , 
     short_key VARCHAR2 (20)  NOT NULL , 
     mandator_id NUMBER (15)  NOT NULL , 
     procedure_type_id NUMBER (10)  NOT NULL 
    ) 
;



ALTER TABLE PROCEDURE 
    ADD CONSTRAINT "procedure_pk" PRIMARY KEY ( id ) ;

ALTER TABLE PROCEDURE 
    ADD CONSTRAINT procedure_un_name UNIQUE ( name ) ;

ALTER TABLE PROCEDURE 
    ADD CONSTRAINT procedure_un_key UNIQUE ( short_key ) ;


CREATE TABLE PROCEDURE_PHASE_CONFIGURATION 
    ( 
     id NUMBER (10)  NOT NULL , 
     phase VARCHAR2 (100)  NOT NULL , 
     procedure_type_id NUMBER (10)  NOT NULL , 
     next_phase_configuration_id NUMBER (10) 
    ) 
;



ALTER TABLE PROCEDURE_PHASE_CONFIGURATION 
    ADD CONSTRAINT proc_phase_config_pk PRIMARY KEY ( id ) ;


CREATE TABLE PROCEDURE_TYPE 
    ( 
     id NUMBER (10)  NOT NULL , 
     name VARCHAR2 (100) 
    ) 
;



ALTER TABLE PROCEDURE_TYPE 
    ADD CONSTRAINT procedury_tape_pk PRIMARY KEY ( id ) ;

ALTER TABLE PROCEDURE_TYPE 
    ADD CONSTRAINT "procedure-type_name_un" UNIQUE ( name ) ;


CREATE TABLE PROCESS_TRANSITION 
    ( 
     id NUMBER (15)  NOT NULL , 
     transition_date DATE  NOT NULL , 
     duration NUMBER (8) , 
     current_status_id NUMBER (10)  NOT NULL , 
     prev_status_id NUMBER (10) , 
     execution_id NUMBER (15)  NOT NULL 
    ) 
;


CREATE INDEX transition_idx_status_date ON PROCESS_TRANSITION 
    ( 
     current_status_id ASC , 
     transition_date ASC 
    ) 
;

ALTER TABLE PROCESS_TRANSITION 
    ADD CONSTRAINT process_transition_pk PRIMARY KEY ( id ) ;


CREATE TABLE STATUS 
    ( 
     id NUMBER (10)  NOT NULL , 
     name VARCHAR2 (50)  NOT NULL 
    ) 
;


CREATE INDEX status_idx_name ON STATUS 
    ( 
     name ASC 
    ) 
;

ALTER TABLE STATUS 
    ADD CONSTRAINT status_pk PRIMARY KEY ( id ) ;



ALTER TABLE EXECUTION 
    ADD CONSTRAINT REL_EXECUTION_PROCESS_TRANS_FK FOREIGN KEY 
    ( 
     last_transition_id
    ) 
    REFERENCES PROCESS_TRANSITION 
    ( 
     id
    ) 
    ON DELETE SET NULL 
;


ALTER TABLE PROCESS_TRANSITION 
    ADD CONSTRAINT REL_TRANSITION_CURR_STATUS FOREIGN KEY 
    ( 
     current_status_id
    ) 
    REFERENCES STATUS 
    ( 
     id
    ) 
;


ALTER TABLE PROCESS_TRANSITION 
    ADD CONSTRAINT REL_TRANSITION_PREV_STATUS FOREIGN KEY 
    ( 
     prev_status_id
    ) 
    REFERENCES STATUS 
    ( 
     id
    ) 
;


ALTER TABLE COMMUNICATION_PROTOCOL 
    ADD CONSTRAINT rel_data_phasen_con FOREIGN KEY 
    ( 
     next_phase_connection_id
    ) 
    REFERENCES PHASE_CONNECTION 
    ( 
     id
    ) 
    ON DELETE SET NULL 
;

ALTER TABLE COMMUNICATION_PROTOCOL 
    ADD CONSTRAINT rel_comprot_conn_status FOREIGN KEY 
    ( 
     status_id
    ) 
    REFERENCES STATUS 
    ( 
     id
    ) 
;

ALTER TABLE EXECUTION 
    ADD CONSTRAINT rel_execution_procedure FOREIGN KEY 
    ( 
     procedure_id
    ) 
    REFERENCES PROCEDURE 
    ( 
     id
    ) 
;


ALTER TABLE COMMUNICATION_PROTOCOL 
    ADD CONSTRAINT rel_input_data_curr_phase_conn FOREIGN KEY 
    ( 
     current_phase_connection_id
    ) 
    REFERENCES PHASE_CONNECTION 
    ( 
     id
    ) 
    ON DELETE SET NULL 
;


ALTER TABLE COMMUNICATION_PROTOCOL 
    ADD CONSTRAINT rel_input_data_execution FOREIGN KEY 
    ( 
     execution_id
    ) 
    REFERENCES EXECUTION 
    ( 
     id
    ) 
;


ALTER TABLE PHASE_CONNECTION 
    ADD CONSTRAINT rel_phase_conn_source_com FOREIGN KEY 
    ( 
     source_com_protocol_id
    ) 
    REFERENCES COMMUNICATION_PROTOCOL 
    ( 
     id
    ) 
;


ALTER TABLE PHASE_CONNECTION 
    ADD CONSTRAINT rel_phasen_conn_target_com FOREIGN KEY 
    ( 
     target_com_protocol_id
    ) 
    REFERENCES COMMUNICATION_PROTOCOL 
    ( 
     id
    ) 
    ON DELETE SET NULL 
;


ALTER TABLE PHASE_CONNECTION 
    ADD CONSTRAINT rel_phasen_conn_status FOREIGN KEY 
    ( 
     status_id
    ) 
    REFERENCES STATUS 
    ( 
     id
    ) 
;


ALTER TABLE PROCEDURE_PHASE_CONFIGURATION 
    ADD CONSTRAINT rel_proc_ph_proc_config_pk FOREIGN KEY 
    ( 
     procedure_type_id
    ) 
    REFERENCES PROCEDURE_TYPE 
    ( 
     id
    ) 
;


ALTER TABLE PROCEDURE 
    ADD CONSTRAINT rel_proc_to_proc_config_pk FOREIGN KEY 
    ( 
     procedure_type_id
    ) 
    REFERENCES PROCEDURE_TYPE 
    ( 
     id
    ) 
;


ALTER TABLE PROCESS_TRANSITION 
    ADD CONSTRAINT rel_proc_trans_execution FOREIGN KEY 
    ( 
     execution_id
    ) 
    REFERENCES EXECUTION 
    ( 
     id
    ) 
;


ALTER TABLE PROCEDURE 
    ADD CONSTRAINT rel_procedure_mandant FOREIGN KEY 
    ( 
     mandator_id
    ) 
    REFERENCES MANDATOR 
    ( 
     id
    ) 
;


ALTER TABLE PROCEDURE_PHASE_CONFIGURATION 
    ADD CONSTRAINT relphase_conf_follow_FK FOREIGN KEY 
    ( 
     next_phase_configuration_id
    ) 
    REFERENCES PROCEDURE_PHASE_CONFIGURATION 
    ( 
     id
    ) 
;


-- Create SEQs

CREATE SEQUENCE seq_execution_id 
    START WITH 1 
    INCREMENT BY 1 
    MAXVALUE 999999999999 
    MINVALUE 1 
    CACHE 100 
;

CREATE SEQUENCE seq_communication_protocol_id 
    START WITH 1 
    INCREMENT BY 1 
    MAXVALUE 99999999999 
    MINVALUE 1 
    CACHE 1000 
;

CREATE SEQUENCE seq_process_transition_id 
    START WITH 1 
    INCREMENT BY 1 
    MAXVALUE 9999999999 
    MINVALUE 1 
    CACHE 1000 
;

CREATE SEQUENCE seq_mandator_id 
    START WITH 1 
    INCREMENT BY 1 
    MAXVALUE 9999999 
    MINVALUE 1 
    CACHE 10 
;

CREATE SEQUENCE seq_phase_connection_id 
    START WITH 1 
    INCREMENT BY 1 
    MAXVALUE 9999999 
    MINVALUE 1 
    ORDER 
;

CREATE SEQUENCE seq_procedure_id 
    START WITH 1 
    INCREMENT BY 1 
    MAXVALUE 9999999 
    MINVALUE 1 
    CACHE 10 
;

CREATE SEQUENCE seq_procedure_type_id 
    START WITH 1 
    INCREMENT BY 1 
    MAXVALUE 99999 
    MINVALUE 1 
    CACHE 10 
;

CREATE SEQUENCE seq_procephase_config_id 
    START WITH 1 
    INCREMENT BY 1 
    MAXVALUE 99999 
    MINVALUE 1 
    CACHE 10 
;

CREATE SEQUENCE seq_status_id 
    START WITH 1 
    INCREMENT BY 1 
    MAXVALUE 9999999 
    MINVALUE 1 
    CACHE 10 
    ORDER 
;


-- Oracle SQL Developer Data Modeler Summary Report: 
-- 
-- CREATE TABLE                             9
-- CREATE INDEX                             8
-- ALTER TABLE                             28
-- CREATE VIEW                              0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           0
-- CREATE STRUCTURED TYPE                   0
-- CREATE COLLECTION TYPE                   0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          0
-- CREATE MATERIALIZED VIEW                 0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0
