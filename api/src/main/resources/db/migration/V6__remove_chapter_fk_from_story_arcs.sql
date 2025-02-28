alter table story_arcs
    drop constraint fk_story_arcs_on_end_chapter;

alter table story_arcs
    drop constraint fk_story_arcs_on_start_chapter;

alter table story_arcs
    add end_chapter_number integer;

alter table story_arcs
    add start_chapter_number integer;

alter table story_arcs
    drop column end_chapter_id;

alter table story_arcs
    drop column start_chapter_id;