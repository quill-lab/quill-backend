alter table contributor_requests
    add approved_at timestamp without time zone;

alter table contributor_requests
    drop column novel_id;