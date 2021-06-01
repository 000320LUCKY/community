create table notification
(
    id bigint auto_increment,
    notifier bigint null,
    receiver bigint null,
    outerId bigint null,
    type int not null,
    gmt_create bigint null,
    status int default 0 null,
    constraint notification_pk
        primary key (id)
);