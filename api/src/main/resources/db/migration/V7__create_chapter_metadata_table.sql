create table chapter_metadata
(
    id            uuid not null,
    created_at    timestamp without time zone,
    persisted_at  timestamp without time zone,
    updated_at    timestamp without time zone,
    deleted_at    timestamp without time zone,
    likes         integer,
    views         integer,
    comment_count integer,
    constraint pk_chapter_metadata primary key (id)
);