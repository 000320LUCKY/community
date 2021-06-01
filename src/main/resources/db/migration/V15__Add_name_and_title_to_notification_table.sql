alter table notification
    add notifier_name varchar(100) null after notifier;

alter table notification
    add outer_title varchar(256) null;

