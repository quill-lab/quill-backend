create table accounts
(
    id         uuid not null,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    email      varchar(255),
    password   varchar(255),
    name       varchar(255),
    constraint pk_accounts primary key (id)
);

create table chapter_texts
(
    id         uuid not null,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    content    text,
    chapter_id uuid not null,
    account_id uuid not null,
    constraint pk_chapter_texts primary key (id)
);

create table chapters
(
    id          uuid         not null,
    created_at  timestamp without time zone,
    updated_at  timestamp without time zone,
    deleted_at  timestamp without time zone,
    title       varchar(255) not null,
    description text,
    novel_id    uuid         not null,
    constraint pk_chapters primary key (id)
);

create table characters
(
    id              uuid         not null,
    created_at      timestamp without time zone,
    updated_at      timestamp without time zone,
    deleted_at      timestamp without time zone,
    name            varchar(255) not null,
    description     text,
    profile_image   varchar(255),
    last_updated_by uuid,
    novel_id        uuid         not null,
    priority        integer,
    constraint pk_characters primary key (id)
);

create table contributor_groups
(
    id                    uuid not null,
    created_at            timestamp without time zone,
    updated_at            timestamp without time zone,
    deleted_at            timestamp without time zone,
    max_contributor_count integer,
    contributor_count     integer,
    status                smallint,
    novel_id              uuid not null,
    completed_at          timestamp without time zone,
    active_contributor_id uuid,
    constraint pk_contributor_groups primary key (id)
);

create table contributor_requests
(
    id                   uuid not null,
    created_at           timestamp without time zone,
    updated_at           timestamp without time zone,
    deleted_at           timestamp without time zone,
    contributor_group_id uuid not null,
    novel_id             uuid not null,
    account_id           uuid not null,
    status               varchar(255),
    constraint pk_contributor_requests primary key (id)
);

create table contributors
(
    id                   uuid not null,
    created_at           timestamp without time zone,
    updated_at           timestamp without time zone,
    deleted_at           timestamp without time zone,
    account_id           uuid not null,
    role                 varchar(255),
    writing_order        integer,
    contributor_group_id uuid not null,
    constraint pk_contributors primary key (id)
);

create table novel_tags
(
    id         uuid         not null,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    name       varchar(255) not null,
    novel_id   uuid         not null,
    constraint pk_novel_tags primary key (id)
);

create table novels
(
    id          uuid         not null,
    created_at  timestamp without time zone,
    updated_at  timestamp without time zone,
    deleted_at  timestamp without time zone,
    title       varchar(255) not null,
    description text,
    cover_image varchar(255),
    synopsis    text,
    category    varchar(255) not null,
    constraint pk_novels primary key (id)
);

create index idx_account_email on accounts (email);

alter table chapters
    add constraint fk_chapter_novel foreign key (novel_id) references novels (id);

alter table chapter_texts
    add constraint fk_chapter_text_chapter foreign key (chapter_id) references chapters (id);

alter table characters
    add constraint fk_characters_on_novel foreign key (novel_id) references novels (id);

alter table contributors
    add constraint fk_contributor_contributor_group foreign key (contributor_group_id) references contributor_groups (id);

alter table contributor_requests
    add constraint fk_contributor_request_contributor_group foreign key (contributor_group_id) references contributor_groups (id);

alter table novel_tags
    add constraint fk_novel_tag_novel foreign key (novel_id) references novels (id);
