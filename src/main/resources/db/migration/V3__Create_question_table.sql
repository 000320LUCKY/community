create table question
(
    id int auto_increment,
    title varchar(50) null,
    description text null,
    gmt_create bigint null,
    gmt_modified bigint null,
    creator int null,
    comment_count int null,
    view_couny int null,
    like_count int null,
    tags varchar(256) null,
    constraint question_pk
        primary key (id)
);

