create table users_scores(
    score_id bigserial primary key,
    user_id bigint references users not null,
    score numeric(8, 2) not null,
    score_date timestamp not null
);