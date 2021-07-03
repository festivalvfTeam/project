package ru.vinotekavf.vinotekaapp.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vinotekavf.vinotekaapp.entities.Provider;

public interface ProviderRepository extends JpaRepository<Provider, Long> {
}
