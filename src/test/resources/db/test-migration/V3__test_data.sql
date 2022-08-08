INSERT INTO public.users (username, password, hobbies, about) VALUES
('test','$2a$10$/cjl6jS5IRwQ7Li/MJ2wt./2bRk9.cKo.WukW477vhDkiryE.1zEq', 'test hobbies', 'test about'),
('test2', '$2a$10$/cjl6jS5IRwQ7Li/MJ2wt./2bRk9.cKo.WukW477vhDkiryE.1zEq', 'test2 hobbies', 'test2 about'),
('empty', '$2a$10$/cjl6jS5IRwQ7Li/MJ2wt./2bRk9.cKo.WukW477vhDkiryE.1zEq', '', ''),
('viral', '$2a$10$/cjl6jS5IRwQ7Li/MJ2wt./2bRk9.cKo.WukW477vhDkiryE.1zEq', '', ''),
('viral2', '$2a$10$/cjl6jS5IRwQ7Li/MJ2wt./2bRk9.cKo.WukW477vhDkiryE.1zEq', '', ''),
('viral3', '$2a$10$/cjl6jS5IRwQ7Li/MJ2wt./2bRk9.cKo.WukW477vhDkiryE.1zEq', '', ''),
('viral4', '$2a$10$/cjl6jS5IRwQ7Li/MJ2wt./2bRk9.cKo.WukW477vhDkiryE.1zEq', '', ''),
('viral5', '$2a$10$/cjl6jS5IRwQ7Li/MJ2wt./2bRk9.cKo.WukW477vhDkiryE.1zEq', '', ''),
('newest', '$2a$10$/cjl6jS5IRwQ7Li/MJ2wt./2bRk9.cKo.WukW477vhDkiryE.1zEq', '', '');

INSERT INTO public.post (author_id, wall_id, text, pub_date) VALUES
(1, 1, 'post test1', now() - INTERVAL '3 days 3 minutes'),
(2, 2, 'post test2', now() - INTERVAL '3 days 2 minutes'),
(1, 2, 'test1 to test2', now() - INTERVAL '3 days 1 minute'),
(4, 4, 'viral old', now() - INTERVAL '2 days'),
(4, 4, 'more viral new', now() - INTERVAL '10 minutes'),
(4, 4, 'less viral new', now() - INTERVAL '9 minutes'),
(9, 9, 'post by newest user', now() - INTERVAL '8 minutes'),
(9, 1, 'post by newest to oldest', now() - INTERVAL '7 minutes'),
(8, 8, 'newest post', now());

INSERT INTO public.likes (post_id, user_id) VALUES
(1,1),(1,2),
(4,1),(4,2),(4,3),(4,4),(4,5),(4,6),(4,7),(4,8),(4,9),
(5,1),(5,2),(5,3),(5,4),(5,5),(5,6),
(6,1),(6,2),(6,3),(6,4);
