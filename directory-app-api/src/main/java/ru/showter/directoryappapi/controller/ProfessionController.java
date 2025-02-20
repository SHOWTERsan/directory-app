package ru.showter.directoryappapi.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.showter.directoryappapi.dto.ProfessionDto;
import ru.showter.directoryappapi.service.ProfessionService;

@RestController
@RequestMapping("/api/professions")
@RequiredArgsConstructor
public class ProfessionController {

    private final ProfessionService professionService;

    @GetMapping
    public ResponseEntity<List<ProfessionDto>> getAllProfessions() {
        return ResponseEntity.ok(professionService.getAllProfessions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessionDto> getProfessionById(@PathVariable Long id) {
        return ResponseEntity.ok(professionService.getProfessionById(id));
    }

    @PostMapping
    public ResponseEntity<ProfessionDto> createProfession(@Valid @RequestBody ProfessionDto professionDto) {
        return new ResponseEntity<>(professionService.createProfession(professionDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessionDto> updateProfession(@PathVariable Long id, @Valid @RequestBody ProfessionDto professionDto) {
        return ResponseEntity.ok(professionService.updateProfession(id, professionDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfession(@PathVariable Long id) {
        professionService.deleteProfession(id);
        return ResponseEntity.noContent().build();
    }
}
