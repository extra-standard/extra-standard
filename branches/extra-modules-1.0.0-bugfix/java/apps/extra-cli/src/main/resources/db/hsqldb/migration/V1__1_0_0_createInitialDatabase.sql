    create table COMMUNICATION_PROTOCOL (
        id bigint generated by default as identity (start with 1),
        hashcode varchar(255),
        input_data_qualifier varchar(255),
        input_identifier varchar(255),
        output_identifier varchar(255),
        request_id varchar(255),
        response_id varchar(255),
        return_code varchar(255),
        return_text varchar(255),
        subquery varchar(255),
        current_phase_connection_id bigint,
        execution_id bigint,
        next_phase_connection_id bigint,
        status_id bigint,
        primary key (id)
    );

    create table EXECUTION (
        id bigint generated by default as identity (start with 1),
        end_time timestamp,
        error_code varchar(255),
        error_message varchar(10000),
        parameters varchar(255),
        phase varchar(255),
        start_time timestamp,
        last_transition_id bigint,
        procedure_id bigint,
        primary key (id)
    );

    create table MANDATOR (
        id bigint generated by default as identity (start with 1),
        name varchar(255) not null,
        primary key (id),
        unique (name)
    );

    create table PHASE_CONNECTION (
        id bigint generated by default as identity (start with 1),
        next_phase_qualifier varchar(255) not null,
        source_com_protocol_id bigint,
        status_id bigint,
        target_com_protocol_id bigint,
        primary key (id)
    );

    create table PROCEDURE (
        id bigint generated by default as identity (start with 1),
        name varchar(255) not null,
        short_key varchar(30) not null,
        mandator_id bigint,
        procedure_type_id bigint,
        primary key (id),
        unique (name),
        unique (short_key)
    );

    create table PROCEDURE_PHASE_CONFIGURATION (
        id bigint generated by default as identity (start with 1),
        phase varchar(255),
        next_phase_configuration_id bigint,
        procedure_type_id bigint,
        primary key (id)
    );

    create table PROCEDURE_TYPE (
        id bigint generated by default as identity (start with 1),
        name varchar(255),
        primary key (id),
        unique (name)
    );

    create table PROCESS_TRANSITION (
        id bigint generated by default as identity (start with 1),
        duration bigint,
        transition_date timestamp,
        current_status_id bigint,
        execution_id bigint,
        prev_status_id bigint,
        primary key (id)
    );

    create table STATUS (
        id bigint not null,
        name varchar(255) not null,
        primary key (id),
        unique (name)
    );

    create index comm_prot_idx_execution on COMMUNICATION_PROTOCOL (execution_id);

    create index comm_prot_idx_request on COMMUNICATION_PROTOCOL (request_id);

    alter table COMMUNICATION_PROTOCOL 
        add constraint FK775715014836409 
        foreign key (current_phase_connection_id) 
        references PHASE_CONNECTION;

    alter table COMMUNICATION_PROTOCOL 
        add constraint FK775715017587B083 
        foreign key (next_phase_connection_id) 
        references PHASE_CONNECTION;

    alter table COMMUNICATION_PROTOCOL 
        add constraint FK77571501B40E23FC 
        foreign key (status_id) 
        references STATUS;

    alter table COMMUNICATION_PROTOCOL 
        add constraint FK775715019AB423F8 
        foreign key (execution_id) 
        references EXECUTION;

    create index execution_idx_proc on EXECUTION (procedure_id);

    alter table EXECUTION 
        add constraint FK65111AF844C1C618 
        foreign key (procedure_id) 
        references PROCEDURE;

    alter table EXECUTION 
        add constraint FK65111AF8B65AA2BE 
        foreign key (last_transition_id) 
        references PROCESS_TRANSITION;

    create index phase_conn_idx_source_protokol on PHASE_CONNECTION (source_com_protocol_id);

    create index phase_conn_idx_phase_status on PHASE_CONNECTION (next_phase_qualifier, status_id);

    create index phase_conn_idx_target_protocol on PHASE_CONNECTION (target_com_protocol_id);

    alter table PHASE_CONNECTION 
        add constraint FKB384D8428C0BB082 
        foreign key (target_com_protocol_id) 
        references COMMUNICATION_PROTOCOL;

    alter table PHASE_CONNECTION 
        add constraint FKB384D842B40E23FC 
        foreign key (status_id) 
        references STATUS;

    alter table PHASE_CONNECTION 
        add constraint FKB384D8423530C 
        foreign key (source_com_protocol_id) 
        references COMMUNICATION_PROTOCOL;

    create index proc_idx_short_key on PROCEDURE (short_key);

    alter table PROCEDURE 
        add constraint FK64D092B3A55645BF 
        foreign key (procedure_type_id) 
        references PROCEDURE_TYPE;

    alter table PROCEDURE 
        add constraint FK64D092B3ED3124BC 
        foreign key (mandator_id) 
        references MANDATOR;

    alter table PROCEDURE_PHASE_CONFIGURATION 
        add constraint FK3EEB3D66A55645BF 
        foreign key (procedure_type_id) 
        references PROCEDURE_TYPE;

    alter table PROCEDURE_PHASE_CONFIGURATION 
        add constraint FK3EEB3D669565A340 
        foreign key (next_phase_configuration_id) 
        references PROCEDURE_PHASE_CONFIGURATION;

    create index transition_idx_status_date on PROCESS_TRANSITION (current_status_id, transition_date);

    alter table PROCESS_TRANSITION 
        add constraint FKF4ECD451CCB18B6 
        foreign key (current_status_id) 
        references STATUS;

    alter table PROCESS_TRANSITION 
        add constraint FKF4ECD45D30DCF0 
        foreign key (prev_status_id) 
        references STATUS;

    alter table PROCESS_TRANSITION 
        add constraint FKF4ECD459AB423F8 
        foreign key (execution_id) 
        references EXECUTION;

    create index status_idx_name on STATUS (name);