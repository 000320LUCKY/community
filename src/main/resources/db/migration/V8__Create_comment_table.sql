create table comment
(
    parent_id bigint not null comment '父类id',
    type int not null comment '父类类型',
    id bigint not null,
    commentator int not null comment '评论人id',
    gmt_create bigint not null comment '创建时间',
    gmt_modified bigint not null comment '修改时间',
    like_count bigint default 0 null,
    constraint comment_pk
        primary key (id)
);

