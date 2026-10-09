package org.polytech.spring;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public List<PatientDto> findAll(@RequestParam(name = "nom", required = false) String nom) {
        return patientService.findPatients(nom);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> findById(
        @PathVariable("id") Long id,
        @RequestParam(name = "avecMedecin", defaultValue = "false") boolean avecMedecin
    ) {
        Optional<PatientDto> patient = patientService.findPatient(id, avecMedecin);
        if (patient.isPresent()) {
            return ResponseEntity.ok(patient.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PatientDto> create(@RequestBody PatientCreationDto dto) {
        PatientDto patient = patientService.savePatient(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(patient);
    }
}
