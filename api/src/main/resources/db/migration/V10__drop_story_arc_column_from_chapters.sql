alter table chapters
    drop constraint fk_chapter_story_arc;

alter table chapters
    drop column story_arc_id;
