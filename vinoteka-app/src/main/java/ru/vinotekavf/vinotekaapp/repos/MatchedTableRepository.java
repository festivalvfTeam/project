package ru.vinotekavf.vinotekaapp.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vinotekavf.vinotekaapp.entities.MatchedTable;

public interface MatchedTableRepository extends JpaRepository<MatchedTable, Long> {
}
