package ru.vinotekavf.vinotekaapp.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vinotekavf.vinotekaapp.entities.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {

    Position findByFvVendorCode(String fvVendorCode);
}
