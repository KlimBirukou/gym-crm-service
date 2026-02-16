WITH u_data AS (
    INSERT INTO gym_schema.user (first_name, last_name, username, password, is_active)
    VALUES
        ('Richard', 'Roe', 'Richard.Roe', 'pass0', true),
        ('Richard', 'Roe', 'Richard.Roe.1', 'pass1', true),
        ('Richard', 'Roe', 'Richard.Roe.2', 'pass2', true),
        ('Richard', 'Roe', 'Richard.Roe.3', 'pass3', true),
        ('Richard', 'Roe', 'Richard.Roe.4', 'pass4', false)
    RETURNING uid, username
)
INSERT INTO gym_schema.trainer (uid, user_uid, training_type_uid)
SELECT t.uid, ud.uid, tt.uid
FROM (VALUES
    ('40000000-0000-0000-0000-000000000000'::UUID, 'Richard.Roe', 'Strength'),
    ('40000000-0000-0000-0000-000000000001'::UUID, 'Richard.Roe.1', 'Cardio'),
    ('40000000-0000-0000-0000-000000000002'::UUID, 'Richard.Roe.2', 'Functional'),
    ('40000000-0000-0000-0000-000000000003'::UUID, 'Richard.Roe.3', 'Stretching'),
    ('40000000-0000-0000-0000-000000000004'::UUID, 'Richard.Roe.4', 'Hiit')
) AS t(uid, uname, spec_name)
JOIN u_data ud ON ud.username = t.uname
JOIN gym_schema.training_type tt ON tt.name = t.spec_name;