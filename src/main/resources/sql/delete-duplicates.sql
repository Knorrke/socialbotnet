DELETE FROM post WHERE post_id NOT IN 
	(SELECT MIN(post_id) FROM post GROUP BY author_id, text, wall_id)
	AND post_id NOT IN (SELECT post_id FROM likes)