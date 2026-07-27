package com.example.controllers;

import com.example.entities.ClosureReason;
import com.example.services.ClosureReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/closure-reasons")
public class ClosureReasonController {

    @Autowired
    private ClosureReasonService closureReasonService;

    @GetMapping
    public ResponseEntity<List<ClosureReason>> getAllClosureReasons() {
        return ResponseEntity.ok(closureReasonService.getAllClosureReasons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClosureReason> getClosureReasonById(@PathVariable Integer id) {
        return closureReasonService.getClosureReasonById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<ClosureReason> createClosureReason(@RequestBody ClosureReason closureReason) {
        ClosureReason savedReason = closureReasonService.saveClosureReason(closureReason);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReason);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClosureReason(@PathVariable Integer id) {
        if (closureReasonService.getClosureReasonById(id).isPresent()) {
            closureReasonService.deleteClosureReason(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}