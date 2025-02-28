create table story_arcs
(
    id               uuid         not null,
    created_at       timestamp without time zone,
    updated_at       timestamp without time zone,
    deleted_at       timestamp without time zone,
    description      text,
    phase            varchar(255) not null,
    start_chapter_id uuid,
    end_chapter_id   uuid,
    novel_id         uuid         not null,
    constraint pk_story_arcs primary key (id)
);

alter table chapters
    add story_arc_id uuid;

alter table story_arcs
    add constraint uc_story_arcs_end_chapter unique (end_chapter_id);

alter table story_arcs
    add constraint uc_story_arcs_start_chapter unique (start_chapter_id);

alter table chapters
    add constraint fk_chapter_story_arc foreign key (story_arc_id) references story_arcs (id);

alter table story_arcs
    add constraint fk_story_arcs_on_end_chapter foreign key (end_chapter_id) references chapters (id);

alter table story_arcs
    add constraint fk_story_arcs_on_novel foreign key (novel_id) references novels (id);

alter table story_arcs
    add constraint fk_story_arcs_on_start_chapter foreign key (start_chapter_id) references chapters (id);