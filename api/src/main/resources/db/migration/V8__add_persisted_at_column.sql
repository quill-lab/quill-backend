alter table accounts
    add persisted_at timestamp without time zone;

alter table chapter_texts
    add persisted_at timestamp without time zone;

alter table chapters
    add persisted_at timestamp without time zone;

alter table characters
    add persisted_at timestamp without time zone;

alter table contributor_group_recruitments
    add persisted_at timestamp without time zone;

alter table contributor_groups
    add persisted_at timestamp without time zone;

alter table contributor_requests
    add persisted_at timestamp without time zone;

alter table contributors
    add persisted_at timestamp without time zone;

alter table novel_tags
    add persisted_at timestamp without time zone;

alter table novels
    add persisted_at timestamp without time zone;

alter table story_arcs
    add persisted_at timestamp without time zone;