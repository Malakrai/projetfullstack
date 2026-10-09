INSERT INTO docteur (nom, specialite)
SELECT 'Martin', 'Medecine generale'
WHERE NOT EXISTS (SELECT 1 FROM docteur WHERE nom = 'Martin');
INSERT INTO patient (nom, age, medecin_traitant_id)
SELECT 'Malak', 22, (SELECT MIN(id) FROM docteur WHERE nom = 'Martin')
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE nom = 'Malak');
INSERT INTO patient (nom, age, medecin_traitant_id)
SELECT 'Dupont', 35, (SELECT MIN(id) FROM docteur WHERE nom = 'Martin')
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE nom = 'Dupont');

-- Les deux relations sont independantes : on remplit aussi la patientele.
INSERT INTO docteur_patients (docteur_id, patient_id)
SELECT medecin_traitant_id, id FROM patient
WHERE nom IN ('Malak', 'Dupont') AND medecin_traitant_id IS NOT NULL
AND NOT EXISTS (
    SELECT 1 FROM docteur_patients WHERE patient_id = patient.id
);
