package com.client.ws.rasmooplus.service.impl;

import com.client.ws.rasmooplus.model.jpa.UserType;
import com.client.ws.rasmooplus.repository.jpa.UserTypeRepository;
import com.client.ws.rasmooplus.service.UserTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTypeImpl implements UserTypeService {

    private UserTypeRepository userTypeRepository;

    public UserTypeImpl(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    @Override
    public List<UserType> findAll() {
        return userTypeRepository.findAll();
    }
}
