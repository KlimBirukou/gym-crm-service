-- Training sessions
INSERT INTO gym_crm_schema.training (uid, trainee_uid, trainer_uid, name, date, duration, training_type_uid)
VALUES
(
    '60000000-0000-0000-0000-000000000001',
    '30000000-0000-0000-0000-000000000001',
    '40000000-0000-0000-0000-000000000001',
    'Silver Sword Techniques',
    '2026-05-01',
    120,
    (SELECT uid FROM gym_crm_schema.training_type WHERE name = 'Martial Arts')
),
(
    '60000000-0000-0000-0000-000000000002',
    '30000000-0000-0000-0000-000000000002',
    '40000000-0000-0000-0000-000000000001',
    'Monster Combat Training',
    '2026-05-02',
    90,
    (SELECT uid FROM gym_crm_schema.training_type WHERE name = 'Martial Arts')
),
(
    '60000000-0000-0000-0000-000000000003',
    '30000000-0000-0000-0000-000000000003',
    '40000000-0000-0000-0000-000000000001',
    'Signs and Swordplay',
    '2026-05-03',
    110,
    (SELECT uid FROM gym_crm_schema.training_type WHERE name = 'Martial Arts')
),
(
    '60000000-0000-0000-0000-000000000004',
    '30000000-0000-0000-0000-000000000004',
    '40000000-0000-0000-0000-000000000002',
    'Force Sensitivity Training',
    '2026-05-04',
    60,
    (SELECT uid FROM gym_crm_schema.training_type WHERE name = 'Meditation')
),
(
    '60000000-0000-0000-0000-000000000005',
    '30000000-0000-0000-0000-000000000005',
    '40000000-0000-0000-0000-000000000002',
    'Jedi Code and Meditation',
    '2026-05-05',
    75,
    (SELECT uid FROM gym_crm_schema.training_type WHERE name = 'Meditation')
),
(
    '60000000-0000-0000-0000-000000000006',
    '30000000-0000-0000-0000-000000000006',
    '40000000-0000-0000-0000-000000000002',
    'Lightsaber Forms Meditation',
    '2026-05-06',
    80,
    (SELECT uid FROM gym_crm_schema.training_type WHERE name = 'Meditation')
),
(
    '60000000-0000-0000-0000-000000000007',
    '30000000-0000-0000-0000-000000000007',
    '40000000-0000-0000-0000-000000000003',
    'Water Dancing Basics',
    '2026-05-07',
    60,
    (SELECT uid FROM gym_crm_schema.training_type WHERE name = 'Fencing')
),
(
    '60000000-0000-0000-0000-000000000008',
    '30000000-0000-0000-0000-000000000007',
    '40000000-0000-0000-0000-000000000003',
    'Balance and Footwork',
    '2026-05-08',
    55,
    (SELECT uid FROM gym_crm_schema.training_type WHERE name = 'Fencing')
),
(
    '60000000-0000-0000-0000-000000000009',
    '30000000-0000-0000-0000-000000000007',
    '40000000-0000-0000-0000-000000000004',
    'Face Changing Ritual',
    '2026-05-10',
    90,
    (SELECT uid FROM gym_crm_schema.training_type WHERE name = 'Shapeshifting')
),
(
    '60000000-0000-0000-0000-000000000010',
    '30000000-0000-0000-0000-000000000007',
    '40000000-0000-0000-0000-000000000004',
    'A Girl Has No Name',
    '2026-05-12',
    120,
    (SELECT uid FROM gym_crm_schema.training_type WHERE name = 'Shapeshifting')
);
