package ru.vinotekavf.vinotekaapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vinotekavf.vinotekaapp.entities.Provider;
import ru.vinotekavf.vinotekaapp.repos.ProviderRepository;

@Service
public class ProviderService {
    @Autowired
    private ProviderRepository providerRepository;

    public void save(Provider provider) {
        providerRepository.save(provider);
    }

    public Provider getProviderById(Long id) {
        return providerRepository.findById(id).orElse(null);
    }
}
