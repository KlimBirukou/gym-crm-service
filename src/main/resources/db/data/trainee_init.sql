WITH u_data AS (
    INSERT INTO gym_schema.user (first_name, last_name, username, password, is_active)
    VALUES
        ('Geralt', 'zRivii', 'Geralt.zRivii', '$2a$12$wL8CIZWnaCpWgHzsUZOQEOkoskrNRHuCvrX/8TA29/zcZo3VcrzBK', true), -- password1
        ('Lambert', 'Walrus', 'Lambert.Walrus', '$2a$12$t9HJbTt/9JOwwJhRUbn3EeEXrRqTWdJGk9ALcVf1s4rWyohY0R5pe', true), -- password2
        ('Eskel', 'Kaminsky', 'Eskel.Kaminsky', '$2a$12$CapcURs2r4DdJBnaAAWGIeRlxhPhtw764str3.aEudM5hWUO4k8Um', true), -- password3
        ('Anakin', 'Skywalker', 'Anakin.Skywalker', '$2a$12$zfSJ6wqe24jaYwyHcnGn3ORP1uHyKO7WWBd8axjq4wX1hgzkYWv3a', true), -- password4
        ('Obi-Wan', 'Kenobi', 'Obi-Wan.Kenobi', '$2a$12$80v21ofT4sqt11NsdlC7oulfGMngslXTUe8kDZtMMRftKyu/lPCi.', true), -- password5
        ('Ahsoka', 'Tano', 'Ahsoka.Tano', '$2a$12$6TLtbiuumJJwdHtbyG5j7.3snaLRdQLDJesudZZBQvLHZYCk8vEXS', true), -- password6
        ('Arya', 'Stark', 'Arya.Stark', '$2a$12$kYIf/CO68h6XUmirppHebuT2P2wkt7QEbToOrFgdih80twuyD8z6.', true), -- password7
        ('Brienne', 'Tarth', 'Brienne.Tarth', '$2a$12$.0DA4lKHjqh8IEsF.nDBveAwRz8oDNAvQYuQrmpvwxJ7a0l.nPk2C', false) -- password13
    RETURNING uid, username
)
INSERT INTO gym_schema.trainee (uid, user_uid, address, birthdate)
SELECT t.uid, ud.uid, t.addr, t.bday::DATE
FROM (VALUES
    ('30000000-0000-0000-0000-000000000001'::UUID, 'Geralt.zRivii', 'Kaer Morhen Fortress, Blue Mountains', '1160-01-01'),
    ('30000000-0000-0000-0000-000000000002'::UUID, 'Lambert.Walrus', 'Kaer Morhen Fortress, Blue Mountains', '1180-03-15'),
    ('30000000-0000-0000-0000-000000000003'::UUID, 'Eskel.Kaminsky', 'Kaer Morhen Fortress, Blue Mountains', '1165-07-22'),
    ('30000000-0000-0000-0000-000000000004'::UUID, 'Anakin.Skywalker', 'Jedi Temple, Coruscant', '1990-03-01'),
    ('30000000-0000-0000-0000-000000000005'::UUID, 'Obi-Wan.Kenobi', 'Jedi Temple, Coruscant', '1985-04-20'),
    ('30000000-0000-0000-0000-000000000006'::UUID, 'Ahsoka.Tano', 'Jedi Temple, Coruscant', '1995-05-08'),
    ('30000000-0000-0000-0000-000000000007'::UUID, 'Arya.Stark', 'House of Black and White, Braavos', '2000-04-14'),
    ('30000000-0000-0000-0000-000000000008'::UUID, 'Brienne.Tarth', 'Evenfall Hall, Isle of Tarth, Stormlands', '2000-04-14')
) AS t(uid, uname, addr, bday)
JOIN u_data ud ON ud.username = t.uname;
