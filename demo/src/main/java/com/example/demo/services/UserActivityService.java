package com.example.demo.services;

import com.example.demo.entities.DataChange;
import com.example.demo.enumeration.Action;
import com.example.demo.enumeration.FunctionDESC;
import com.example.demo.repository.ActivityRepository;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserActivityService {

    private final ObjectMapper objectMapper;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public DataChange registerNewActivity(FunctionDESC functionDESC, Object prevValue, Object currValue, Action action, String tableName) throws JsonProcessingException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long id = userRepository.findByUsername(username).orElseThrow().getId();
        DataChange dataChange = new DataChange();
        dataChange.setAuditMU(id);
        dataChange.setAuditMD(LocalDateTime.now());
        dataChange.setPrevValue(objectMapper.writeValueAsString(prevValue));
        dataChange.setCurrValue(objectMapper.writeValueAsString(currValue));
        dataChange.setActionTypeEntity(action);
        dataChange.setTableName(tableName);
        dataChange.setFunctionDESC(functionDESC);

        return activityRepository.save(dataChange);
    }

    public List<DataChange> listChanges() {
        return activityRepository.findAll();
    }

    public List<DataChange> listChangesByType(Action type) {
        return activityRepository.findByActionTypeEntity(type);
    }

}
