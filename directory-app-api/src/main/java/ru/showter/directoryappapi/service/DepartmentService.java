package ru.showter.directoryappapi.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.showter.directoryappapi.dto.DepartmentDto;
import ru.showter.directoryappapi.model.Department;
import ru.showter.directoryappapi.repository.DepartmentRepository;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<DepartmentDto> getParentDepartments() {
        return departmentRepository.findDepartmentByParentIsNull().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<DepartmentDto> getChildDepartments(Long parentId) {
        return departmentRepository.findByParentId(parentId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public DepartmentDto getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));
    }

    @Transactional
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        Department department = mapToEntity(departmentDto);
        Department savedDepartment = departmentRepository.save(department);
        return mapToDto(savedDepartment);
    }

    @Transactional
    public DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto) {
        if (!departmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Department not found with id: " + id);
        }
        Department department = mapToEntity(departmentDto);
        department.setId(id);
        Department updatedDepartment = departmentRepository.save(department);
        return mapToDto(updatedDepartment);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }

    private DepartmentDto mapToDto(Department department) {
        DepartmentDto dto = new DepartmentDto();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setNotes(department.getNotes());

        if (department.getParent() != null) {
            dto.setParentId(department.getParent().getId());
        }

        if (department.getChildren() != null && !department.getChildren().isEmpty()) {
            dto.setChildren(department.getChildren().stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private Department mapToEntity(DepartmentDto dto) {
        Department department = new Department();
        department.setId(dto.getId());
        department.setName(dto.getName());
        department.setNotes(dto.getNotes());

        if (dto.getParentId() != null) {
            Department parent = departmentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent department not found with id: " + dto.getParentId()));
            department.setParent(parent);
        }

        return department;
    }
}
