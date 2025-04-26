create table chapter_authors
(
    id                uuid    not null,
    created_at        timestamp without time zone,
    persisted_at      timestamp without time zone,
    updated_at        timestamp without time zone,
    deleted_at        timestamp without time zone,
    chapter_id        uuid    not null,
    contributor_id    uuid    not null,
    account_id        uuid    not null,
    is_current_writer boolean not null,
    constraint pk_chapter_authors primary key (id)
);

alter table chapter_texts
    add contributor_id uuid;

alter table chapter_texts
    add status varchar(255);

alter table chapter_texts
    alter column contributor_id set not null;

alter table contributor_requests
    add rejected_at timestamp without time zone;

alter table chapter_authors
    add constraint fk_chapter_author_chapter foreign key (chapter_id) references chapters (id);

alter table contributors
    drop column is_current_writer;

alter table chapter_texts
    alter column account_id set not null;

alter table contributor_requests
    alter column account_id set not null;

alter table contributors
    alter column account_id set not null;

alter table chapter_texts
    alter column chapter_id set not null;

alter table contributor_requests
    alter column contributor_group_id set not null;

alter table contributors
    alter column contributor_group_id set not null;

alter table chapters
    alter column novel_id set not null;

alter table contributor_groups
    alter column novel_id set not null;

alter table story_arcs
    alter column novel_id set not null;
