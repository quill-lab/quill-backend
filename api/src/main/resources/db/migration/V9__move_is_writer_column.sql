alter table contributors
    add is_current_writer boolean;

alter table contributor_groups
    drop column active_contributor_id;