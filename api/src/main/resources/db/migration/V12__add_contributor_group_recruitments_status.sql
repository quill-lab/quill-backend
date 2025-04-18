alter table contributor_group_recruitments
    add status varchar(255);

create index idx_account_email on accounts (email);