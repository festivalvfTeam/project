package ru.vinotekavf.vinotekaapp.services;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vinotekavf.vinotekaapp.entities.Provider;
import ru.vinotekavf.vinotekaapp.repos.ProviderRepository;

@Service
public class ProviderService {
    @Autowired
    private ProviderRepository providerRepository;

    public void save(Provider provider) {
        Provider providerFromDb = providerRepository.findByName(provider.getName());
        if (ObjectUtils.isNotEmpty(providerFromDb)) {
            providerFromDb.setManagerName(provider.getManagerName());
            providerFromDb.setPhone(provider.getPhone());
            providerRepository.save(providerFromDb);
        } else {
            providerRepository.save(provider);
        }
    }

    public Provider getProviderById(Long id) {
        return providerRepository.findById(id).orElse(null);
    }

    public Provider getProviderByName(String name) {
        return providerRepository.findByName(name);
    }
}
