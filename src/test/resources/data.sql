INSERT INTO station (id, name) VALUES (1, '테스트역1')
INSERT INTO station (id, name) VALUES (2, '테스트역2')
INSERT INTO station (id, name) VALUES (3, '테스트역3')
INSERT INTO station (id, name) VALUES (4, '테스트역4')

INSERT INTO member (id, email, name, password) VALUES (1, 'test@test.com', 'testUser', 'testPassword')
INSERT INTO member (id, email, name, password) VALUES (2, 'test2@test2.com', 'testUser2', 'testPassword2')

-- testUser
INSERT INTO favorite (id, member_id, source_id, target_id) VALUES (1, 1, 1, 2)
INSERT INTO favorite (id, member_id, source_id, target_id) VALUES (2, 1, 1, 3)
INSERT INTO favorite (id, member_id, source_id, target_id) VALUES (3, 1, 1, 4)

-- testUser2
INSERT INTO favorite (id, member_id, source_id, target_id) VALUES (4, 2, 2, 3)
INSERT INTO favorite (id, member_id, source_id, target_id) VALUES (5, 2, 2, 4)