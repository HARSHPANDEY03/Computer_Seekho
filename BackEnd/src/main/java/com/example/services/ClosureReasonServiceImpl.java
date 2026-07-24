package com.example.services; // Or com.example.services

import com.example.entities.ClosureReason;
import com.example.repositories.ClosureReasonRepository;
import com.example.services.ClosureReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // <-- Make sure this is imported

import java.util.List;
import java.util.Optional;

@Service // <-- This annotation is mandatory for Spring to detect the bean
public class ClosureReasonServiceImpl implements ClosureReasonService {

    @Autowired
    private ClosureReasonRepository closureReasonRepository;

    @Override
    public List<ClosureReason> getAllClosureReasons() {
        return closureReasonRepository.findAll();
    }

    @Override
    public Optional<ClosureReason> getClosureReasonById(Integer id) {
        return closureReasonRepository.findById(id);
    }

    @Override
    public ClosureReason saveClosureReason(ClosureReason closureReason) {
        return closureReasonRepository.save(closureReason);
    }

    @Override
    public void deleteClosureReason(Integer id) {
        closureReasonRepository.deleteById(id);
    }
}