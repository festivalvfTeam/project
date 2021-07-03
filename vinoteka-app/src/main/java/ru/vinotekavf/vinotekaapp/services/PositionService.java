package ru.vinotekavf.vinotekaapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vinotekavf.vinotekaapp.entities.Position;
import ru.vinotekavf.vinotekaapp.repos.PositionRepository;

@Service
public class PositionService {
    @Autowired
    private PositionRepository positionRepository;

    public void save(Position position) {
        positionRepository.save(position);
    }
}
