create table chapter_publication_requests
(
    id           uuid         not null,
    created_at   timestamp without time zone,
    persisted_at timestamp without time zone,
    updated_at   timestamp without time zone,
    deleted_at   timestamp without time zone,
    chapter_id   uuid         not null,
    requester_id uuid         not null,
    status       varchar(255) not null,
    reviewer_id  uuid,
    comment      varchar(255),
    reviewed_at  timestamp without time zone,
    constraint pk_chapter_publication_requests primary key (id)
);
