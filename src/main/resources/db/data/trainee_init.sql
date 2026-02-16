WITH u_data AS (
    INSERT INTO gym_schema.user (first_name, last_name, username, password, is_active)
    VALUES 
        ('John', 'Doe', 'John.Doe', 'pass0', true),
        ('John', 'Doe', 'John.Doe.1', 'pass1', true),
        ('John', 'Doe', 'John.Doe.2', 'pass2', true),
        ('John', 'Doe', 'John.Doe.3', 'pass3', true),
        ('John', 'Doe', 'John.Doe.4', 'pass4', true),
        ('John', 'Doe', 'John.Doe.5', 'pass5', true),
        ('John', 'Doe', 'John.Doe.6', 'pass6', true),
        ('John', 'Doe', 'John.Doe.7', 'pass7', false),
        ('John', 'Doe', 'John.Doe.8', 'pass8', false),
        ('John', 'Doe', 'John.Doe.9', 'pass9', false)
    RETURNING uid, username
)
INSERT INTO gym_schema.trainee (uid, user_uid, address, birthdate)
SELECT t.uid, ud.uid, t.addr, t.bday::DATE
FROM (VALUES
    ('30000000-0000-0000-0000-000000000000'::UUID, 'John.Doe', 'Address', '2000-01-01'),
    ('30000000-0000-0000-0000-000000000001'::UUID, 'John.Doe.1', 'Address', '2000-01-02'),
    ('30000000-0000-0000-0000-000000000002'::UUID, 'John.Doe.2', 'Address', '2000-01-03'),
    ('30000000-0000-0000-0000-000000000003'::UUID, 'John.Doe.3', 'Address', '2000-01-04'),
    ('30000000-0000-0000-0000-000000000004'::UUID, 'John.Doe.4', 'Address', '2000-01-05'),
    ('30000000-0000-0000-0000-000000000005'::UUID, 'John.Doe.5', 'Address', '2000-01-06'),
    ('30000000-0000-0000-0000-000000000006'::UUID, 'John.Doe.6', 'Address', '2000-01-07'),
    ('30000000-0000-0000-0000-000000000007'::UUID, 'John.Doe.7', 'Address', '2000-01-08'),
    ('30000000-0000-0000-0000-000000000008'::UUID, 'John.Doe.8', 'Address', '2000-01-09'),
    ('30000000-0000-0000-0000-000000000009'::UUID, 'John.Doe.9', 'Address', '2000-01-10')
) AS t(uid, uname, addr, bday) JOIN u_data ud ON ud.username = t.uname;
