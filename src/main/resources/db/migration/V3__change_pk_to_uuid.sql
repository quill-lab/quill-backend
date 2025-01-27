-- First, add UUID columns to all tables
alter table accounts
    add column uuid uuid;
alter table chapters
    add column uuid uuid;
alter table characters
    add column uuid uuid;
alter table contributor_groups
    add column uuid uuid;
alter table contributor_requests
    add column uuid uuid;
alter table contributors
    add column uuid uuid;
alter table novel_tags
    add column uuid uuid;
alter table novels
    add column uuid uuid;
alter table chapter_texts
    add column uuid uuid;

-- Generate UUIDs for existing records
update accounts
set uuid = gen_random_uuid();
update chapters
set uuid = gen_random_uuid();
update characters
set uuid = gen_random_uuid();
update contributor_groups
set uuid = gen_random_uuid();
update contributor_requests
set uuid = gen_random_uuid();
update contributors
set uuid = gen_random_uuid();
update novel_tags
set uuid = gen_random_uuid();
update novels
set uuid = gen_random_uuid();
update chapter_texts
set uuid = gen_random_uuid();

-- Make UUID columns NOT NULL
alter table accounts
    alter column uuid set not null;
alter table chapters
    alter column uuid set not null;
alter table characters
    alter column uuid set not null;
alter table contributor_groups
    alter column uuid set not null;
alter table contributor_requests
    alter column uuid set not null;
alter table contributors
    alter column uuid set not null;
alter table novel_tags
    alter column uuid set not null;
alter table novels
    alter column uuid set not null;
alter table chapter_texts
    alter column uuid set not null;

-- Drop old ID columns and constraints
alter table chapters
    drop constraint chapters_pkey cascade;
alter table characters
    drop constraint characters_pkey cascade;
alter table contributor_groups
    drop constraint contributor_groups_pkey cascade;
alter table contributor_requests
    drop constraint contributor_requests_pkey cascade;
alter table contributors
    drop constraint contributors_pkey cascade;
alter table novel_tags
    drop constraint novel_tags_pkey cascade;
alter table novels
    drop constraint novels_pkey cascade;
alter table chapter_texts
    drop constraint chapter_texts_pkey cascade;
alter table accounts
    drop constraint accounts_pkey cascade;

-- Drop old ID columns
alter table accounts
    drop column id;
alter table chapters
    drop column id;
alter table characters
    drop column id;
alter table contributor_groups
    drop column id;
alter table contributor_requests
    drop column id;
alter table contributors
    drop column id;
alter table novel_tags
    drop column id;
alter table novels
    drop column id;
alter table chapter_texts
    drop column id;

-- Rename UUID columns to id
alter table accounts
    rename column uuid to id;
alter table chapters
    rename column uuid to id;
alter table characters
    rename column uuid to id;
alter table contributor_groups
    rename column uuid to id;
alter table contributor_requests
    rename column uuid to id;
alter table contributors
    rename column uuid to id;
alter table novel_tags
    rename column uuid to id;
alter table novels
    rename column uuid to id;
alter table chapter_texts
    rename column uuid to id;

-- Add primary key constraints
alter table accounts
    add constraint accounts_pkey primary key (id);
alter table chapters
    add constraint chapters_pkey primary key (id);
alter table characters
    add constraint characters_pkey primary key (id);
alter table contributor_groups
    add constraint contributor_groups_pkey primary key (id);
alter table contributor_requests
    add constraint contributor_requests_pkey primary key (id);
alter table contributors
    add constraint contributors_pkey primary key (id);
alter table novel_tags
    add constraint novel_tags_pkey primary key (id);
alter table novels
    add constraint novels_pkey primary key (id);
alter table chapter_texts
    add constraint chapter_texts_pkey primary key (id);

-- Update foreign key references
alter table chapters
    add constraint fk_chapters_novel foreign key (novel_id) references novels (id);
alter table characters
    add constraint fk_characters_novel foreign key (novel_id) references novels (id);
alter table contributor_requests
    add constraint fk_contributor_request_contributor_group foreign key (contributor_group_id) references contributor_groups (id);
alter table contributors
    add constraint fk_contributors_contributor_group foreign key (contributor_group_id) references contributor_groups (id);
alter table novel_tags
    add constraint fk_novel_tags_novel foreign key (novel_id) references novels (id);
