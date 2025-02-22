alter table chapters
    add approved_at timestamp without time zone;

alter table chapters
    add chapter_number integer;

alter table chapters
    add status varchar(255);