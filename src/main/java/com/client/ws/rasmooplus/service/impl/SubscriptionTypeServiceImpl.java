package com.client.ws.rasmooplus.service.impl;

import com.client.ws.rasmooplus.dto.SubscriptionTypeDto;
import com.client.ws.rasmooplus.exception.BadRequestException;
import com.client.ws.rasmooplus.exception.NotFoundException;
import com.client.ws.rasmooplus.mapper.SubscriptiontypeMapper;
import com.client.ws.rasmooplus.model.jpa.SubscriptionType;
import com.client.ws.rasmooplus.repository.jpa.SubscriptionTypeRepository;
import com.client.ws.rasmooplus.service.SubscriptionTypeService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SubscriptionTypeServiceImpl implements SubscriptionTypeService {

    private final SubscriptionTypeRepository subscriptionTypeRepository;

    public SubscriptionTypeServiceImpl(SubscriptionTypeRepository subscriptionTypeRepository) {
        this.subscriptionTypeRepository = subscriptionTypeRepository;
    }

    @Override
    @Cacheable(value = "subscriptionType")
    public List<SubscriptionType> findAll() {
        return subscriptionTypeRepository.findAll();
    }

    @Override
    @Cacheable(value = "subscriptionType", key = "#id")
    public SubscriptionType findById(Long id) {
        Optional<SubscriptionType> optionalSubscriptionType = subscriptionTypeRepository.findById(id);

        if (optionalSubscriptionType.isEmpty()) {
            throw new NotFoundException("SubscriptionType não encontrado");
        }
        return optionalSubscriptionType.get();
    }

    @Override
    @CacheEvict(value = "subscriptionType", allEntries = true)
    public SubscriptionType create(SubscriptionTypeDto subscriptionTypeDto) {
        if (Objects.nonNull(subscriptionTypeDto.getId())) {
            throw new BadRequestException("Id deve ser Nulo");
        }
        return subscriptionTypeRepository.save(SubscriptiontypeMapper.fromDtoToEntity(subscriptionTypeDto));
    }

    @Override
    @CacheEvict(value = "subscriptionType", allEntries = true)
    public SubscriptionType update(Long id, SubscriptionTypeDto subscriptionTypeDto) {
        this.findById(id);
        subscriptionTypeDto.setId(id);
        return subscriptionTypeRepository.save(SubscriptiontypeMapper.fromDtoToEntity(subscriptionTypeDto));
    }

    @Override
    @CacheEvict(value = "subscriptionType", allEntries = true, key = "id")
    public void delete(Long id) {
        this.findById(id);
        subscriptionTypeRepository.deleteById(id);
    }
}
