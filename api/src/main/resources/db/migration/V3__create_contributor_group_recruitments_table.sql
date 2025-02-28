create table contributor_group_recruitments
(
    id                   uuid not null,
    created_at           timestamp without time zone,
    updated_at           timestamp without time zone,
    deleted_at           timestamp without time zone,
    title                varchar(255),
    content              varchar(255),
    link                 varchar(255),
    contributor_group_id uuid,
    author_id            uuid,
    constraint pk_contributor_group_recruitments primary key (id)
);