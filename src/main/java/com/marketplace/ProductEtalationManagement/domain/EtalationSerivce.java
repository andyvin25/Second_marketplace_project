package com.marketplace.ProductEtalationManagement.domain;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("EtalationService")
public class EtalationSerivce {
    
    @Autowired
    private EtalationRepository etalationRepository;

    public List<Etalation> getAllEtalation() {
        return etalationRepository.findAll();
    }

    public Optional<Etalation> getEtalationById(String id) {
        return etalationRepository.findById(id);
    }

    public Boolean hasEtalationTheSameName(String name) {
        return getAllEtalation().stream()
            .anyMatch(etalation -> etalation.getEtalationName().contains(name));
    }

    public void saveEtalation(Etalation etalation) {
        Objects.requireNonNull(etalation);
        etalationRepository.save(etalation);
    }

    public void deleteEtalation(Etalation foundEtalation) {
        etalationRepository.delete(foundEtalation);
    }

}
