CREATE INDEX fk_post_author ON post (author_id);
CREATE INDEX fk_post_wall ON post (wall_id);
CREATE INDEX fk_likes_post ON likes (post_id);
CREATE INDEX fk_likes_user ON likes (user_id);
