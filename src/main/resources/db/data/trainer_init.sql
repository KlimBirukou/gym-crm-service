WITH u_data AS (
    INSERT INTO gym_crm_schema.user (first_name, last_name, username, password, is_active)
    VALUES
        ('Vesemir', 'Oldman', 'Vesemir.Oldman', '$2a$12$.Gc3o.dussXb8yPnGs5HrelRhxqayZVbx9lnDp7mw//usBcL1Dji2', true), -- password8
        ('Master', 'Yoda', 'Master.Yoda', '$2a$12$ZTMedwCtbwL7FL1bFfpD1e7DIl6SY3Z2I89kG3TqRLQpGUBbKih8y', true), -- password9
        ('Syrio', 'Forel', 'Syrio.Forel', '$2a$12$CgVVuzMBPvewnLWxsPu62O4S.qtLQKzv1zhy7p7hmePIFSq/DUt4m', true), --password10
        ('Jaqen', 'Hghar', 'Jaqen.Hghar', '$2a$12$ai4bqwj9BTW80WXLIWTn6ehaK6797I5LGcfLHtPVac3TvImqC8gsq', true),  --password11
        ('John', 'Wick', 'John.Wick', '$2a$12$BfCn9buk2Z1aPLmbdUN/o.w3sYVSREAXIPUIcyZV6.ygIecZK2IXO', false)  --password12
    RETURNING uid, username
)
INSERT INTO gym_crm_schema.trainer (uid, user_uid, training_type_uid)
SELECT t.uid, ud.uid, tt.uid
FROM (VALUES
    ('40000000-0000-0000-0000-000000000001'::UUID, 'Vesemir.Oldman', 'Martial Arts'),
    ('40000000-0000-0000-0000-000000000002'::UUID, 'Master.Yoda', 'Meditation'),
    ('40000000-0000-0000-0000-000000000003'::UUID, 'Syrio.Forel', 'Fencing'),
    ('40000000-0000-0000-0000-000000000004'::UUID, 'Jaqen.Hghar', 'Shapeshifting'),
    ('40000000-0000-0000-0000-000000000005'::UUID, 'John.Wick', 'Martial Arts')
) AS t(uid, uname, spec_name)
JOIN u_data ud ON ud.username = t.uname
JOIN gym_crm_schema.training_type tt ON tt.name = t.spec_name;
