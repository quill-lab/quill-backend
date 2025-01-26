-- First, add UUID columns to all tables
ALTER TABLE accounts ADD COLUMN uuid UUID;
ALTER TABLE chapters ADD COLUMN uuid UUID;
ALTER TABLE characters ADD COLUMN uuid UUID;
ALTER TABLE contributor_groups ADD COLUMN uuid UUID;
ALTER TABLE contributor_requests ADD COLUMN uuid UUID;
ALTER TABLE contributors ADD COLUMN uuid UUID;
ALTER TABLE novel_tags ADD COLUMN uuid UUID;
ALTER TABLE novels ADD COLUMN uuid UUID;
ALTER TABLE chapter_texts ADD COLUMN uuid UUID;

-- Generate UUIDs for existing records
UPDATE accounts SET uuid = gen_random_uuid();
UPDATE chapters SET uuid = gen_random_uuid();
UPDATE characters SET uuid = gen_random_uuid();
UPDATE contributor_groups SET uuid = gen_random_uuid();
UPDATE contributor_requests SET uuid = gen_random_uuid();
UPDATE contributors SET uuid = gen_random_uuid();
UPDATE novel_tags SET uuid = gen_random_uuid();
UPDATE novels SET uuid = gen_random_uuid();
UPDATE chapter_texts SET uuid = gen_random_uuid();

-- Make UUID columns NOT NULL
ALTER TABLE accounts ALTER COLUMN uuid SET NOT NULL;
ALTER TABLE chapters ALTER COLUMN uuid SET NOT NULL;
ALTER TABLE characters ALTER COLUMN uuid SET NOT NULL;
ALTER TABLE contributor_groups ALTER COLUMN uuid SET NOT NULL;
ALTER TABLE contributor_requests ALTER COLUMN uuid SET NOT NULL;
ALTER TABLE contributors ALTER COLUMN uuid SET NOT NULL;
ALTER TABLE novel_tags ALTER COLUMN uuid SET NOT NULL;
ALTER TABLE novels ALTER COLUMN uuid SET NOT NULL;
ALTER TABLE chapter_texts ALTER COLUMN uuid SET NOT NULL;

-- Drop old ID columns and constraints
ALTER TABLE chapters DROP CONSTRAINT chapters_pkey CASCADE;
ALTER TABLE characters DROP CONSTRAINT characters_pkey CASCADE;
ALTER TABLE contributor_groups DROP CONSTRAINT contributor_groups_pkey CASCADE;
ALTER TABLE contributor_requests DROP CONSTRAINT contributor_requests_pkey CASCADE;
ALTER TABLE contributors DROP CONSTRAINT contributors_pkey CASCADE;
ALTER TABLE novel_tags DROP CONSTRAINT novel_tags_pkey CASCADE;
ALTER TABLE novels DROP CONSTRAINT novels_pkey CASCADE;
ALTER TABLE chapter_texts DROP CONSTRAINT chapter_texts_pkey CASCADE;
ALTER TABLE accounts DROP CONSTRAINT accounts_pkey CASCADE;

-- Drop old ID columns
ALTER TABLE accounts DROP COLUMN id;
ALTER TABLE chapters DROP COLUMN id;
ALTER TABLE characters DROP COLUMN id;
ALTER TABLE contributor_groups DROP COLUMN id;
ALTER TABLE contributor_requests DROP COLUMN id;
ALTER TABLE contributors DROP COLUMN id;
ALTER TABLE novel_tags DROP COLUMN id;
ALTER TABLE novels DROP COLUMN id;
ALTER TABLE chapter_texts DROP COLUMN id;

-- Rename UUID columns to id
ALTER TABLE accounts RENAME COLUMN uuid TO id;
ALTER TABLE chapters RENAME COLUMN uuid TO id;
ALTER TABLE characters RENAME COLUMN uuid TO id;
ALTER TABLE contributor_groups RENAME COLUMN uuid TO id;
ALTER TABLE contributor_requests RENAME COLUMN uuid TO id;
ALTER TABLE contributors RENAME COLUMN uuid TO id;
ALTER TABLE novel_tags RENAME COLUMN uuid TO id;
ALTER TABLE novels RENAME COLUMN uuid TO id;
ALTER TABLE chapter_texts RENAME COLUMN uuid TO id;

-- Add primary key constraints
ALTER TABLE accounts ADD CONSTRAINT accounts_pkey PRIMARY KEY (id);
ALTER TABLE chapters ADD CONSTRAINT chapters_pkey PRIMARY KEY (id);
ALTER TABLE characters ADD CONSTRAINT characters_pkey PRIMARY KEY (id);
ALTER TABLE contributor_groups ADD CONSTRAINT contributor_groups_pkey PRIMARY KEY (id);
ALTER TABLE contributor_requests ADD CONSTRAINT contributor_requests_pkey PRIMARY KEY (id);
ALTER TABLE contributors ADD CONSTRAINT contributors_pkey PRIMARY KEY (id);
ALTER TABLE novel_tags ADD CONSTRAINT novel_tags_pkey PRIMARY KEY (id);
ALTER TABLE novels ADD CONSTRAINT novels_pkey PRIMARY KEY (id);
ALTER TABLE chapter_texts ADD CONSTRAINT chapter_texts_pkey PRIMARY KEY (id);

-- Update foreign key references
ALTER TABLE chapters ADD CONSTRAINT fk_chapters_novel FOREIGN KEY (novel_id) REFERENCES novels(id);
ALTER TABLE characters ADD CONSTRAINT fk_characters_novel FOREIGN KEY (novel_id) REFERENCES novels(id);
ALTER TABLE contributor_requests ADD CONSTRAINT fk_contributor_request_contributor_group FOREIGN KEY (contributor_group_id) REFERENCES contributor_groups(id);
ALTER TABLE contributors ADD CONSTRAINT fk_contributors_contributor_group FOREIGN KEY (contributor_group_id) REFERENCES contributor_groups(id);
ALTER TABLE novel_tags ADD CONSTRAINT fk_novel_tags_novel FOREIGN KEY (novel_id) REFERENCES novels(id);
