package com.example.services;

import com.example.entities.ClosureReason;
import java.util.List;
import java.util.Optional;

public interface ClosureReasonService {
    List<ClosureReason> getAllClosureReasons();
    Optional<ClosureReason> getClosureReasonById(Integer id);
    ClosureReason saveClosureReason(ClosureReason closureReason);
    void deleteClosureReason(Integer id);
}