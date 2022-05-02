ALTER TABLE public.post ADD likes_count integer DEFAULT 0;
UPDATE public.post SET likes_count =
(
	SELECT COUNT(*)
	FROM public.likes
	WHERE public.post.post_id = public.likes.post_id
);

CREATE OR REPLACE FUNCTION inc_likes_count() 
RETURNS TRIGGER AS $inc$
BEGIN
	UPDATE public.post SET likes_count = likes_count + 1 WHERE post_id = NEW.post_id;
	RETURN NEW;
END;
$inc$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION dec_likes_count() 
RETURNS TRIGGER AS $dec$
BEGIN
	UPDATE public.post SET likes_count = likes_count - 1 WHERE post_id = OLD.post_id;
	RETURN NEW;
END;
$dec$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER inc_likes_count AFTER INSERT ON public.likes
FOR EACH ROW
EXECUTE FUNCTION inc_likes_count();

CREATE OR REPLACE TRIGGER dec_likes_count AFTER DELETE ON public.likes
FOR EACH ROW
EXECUTE FUNCTION dec_likes_count();
