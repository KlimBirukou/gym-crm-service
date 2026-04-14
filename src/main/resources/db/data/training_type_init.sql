INSERT INTO gym_crm_schema.training_type (name) VALUES
('Martial Arts'),
('Meditation'),
('Fencing'),
('Shapeshifting')
ON CONFLICT (name) DO NOTHING;
