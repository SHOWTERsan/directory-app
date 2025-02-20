package ru.showter.directoryappapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import ru.showter.directoryappapi.dto.ProfessionDto;
import ru.showter.directoryappapi.model.Profession;
import ru.showter.directoryappapi.repository.ProfessionRepository;

@Service
@RequiredArgsConstructor
public class ProfessionService {

    private final ProfessionRepository professionRepository;

    public List<ProfessionDto> getAllProfessions() {
        return professionRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ProfessionDto getProfessionById(Long id) {
        return professionRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Profession not found with id: " + id));
    }

    @Transactional
    public ProfessionDto createProfession(ProfessionDto professionDto) {
        Profession profession = mapToEntity(professionDto);
        Profession savedProfession = professionRepository.save(profession);
        return mapToDto(savedProfession);
    }

    @Transactional
    public ProfessionDto updateProfession(Long id, ProfessionDto professionDto) {
        if (!professionRepository.existsById(id)) {
            throw new EntityNotFoundException("Profession not found with id: " + id);
        }
        Profession profession = mapToEntity(professionDto);
        profession.setId(id);
        Profession updatedProfession = professionRepository.save(profession);
        return mapToDto(updatedProfession);
    }

    @Transactional
    public void deleteProfession(Long id) {
        if (!professionRepository.existsById(id)) {
            throw new EntityNotFoundException("Profession not found with id: " + id);
        }
        professionRepository.deleteById(id);
    }

    private ProfessionDto mapToDto(Profession profession) {
        ProfessionDto dto = new ProfessionDto();
        dto.setId(profession.getId());
        dto.setName(profession.getName());
        dto.setNotes(profession.getNotes());
        return dto;
    }

    private Profession mapToEntity(ProfessionDto dto) {
        Profession profession = new Profession();
        profession.setId(dto.getId());
        profession.setName(dto.getName());
        profession.setNotes(dto.getNotes());
        return profession;
    }
}
