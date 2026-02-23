INSERT INTO gym_schema.training (uid, trainee_uid, trainer_uid, name, date, duration, training_type_uid)
VALUES
(
    '60000000-0000-0000-0000-000000000000',
    '30000000-0000-0000-0000-000000000000',
    '40000000-0000-0000-0000-000000000000',
    'Heavy Lifting',
    '2026-05-01',
    120,
    (SELECT uid FROM gym_schema.training_type WHERE name = 'Strength')
),
(
    '60000000-0000-0000-0000-000000000001',
    '30000000-0000-0000-0000-000000000001',
    '40000000-0000-0000-0000-000000000001',
    'Speed Run',
    '2026-05-02',
    60,
    (SELECT uid FROM gym_schema.training_type WHERE name = 'Cardio')
);