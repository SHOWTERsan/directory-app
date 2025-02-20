package ru.showter.directoryappapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.showter.directoryappapi.model.Profession;

@Repository
public interface ProfessionRepository extends JpaRepository<Profession, Long> {
}
