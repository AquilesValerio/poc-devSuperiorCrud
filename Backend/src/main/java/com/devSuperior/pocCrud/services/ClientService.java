package com.devSuperior.pocCrud.services;

import com.devSuperior.pocCrud.DTO.ClientDto;
import com.devSuperior.pocCrud.entities.Client;
import com.devSuperior.pocCrud.exceptions.DatabaseException;
import com.devSuperior.pocCrud.exceptions.ResourceNotFoundException;
import com.devSuperior.pocCrud.repositories.ClientRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;


@Service
public class ClientService {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;

    }

    @Transactional(readOnly = true)
    public Page<ClientDto> findAll(PageRequest pageRequest) {
        Page<Client> list = repository.findAll(pageRequest);
        return list.map(x -> new ClientDto(x));
    }

    @Transactional(readOnly = true)
    public ClientDto findById(long id) {
        Optional<Client> obj = repository.findById(id);
        Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ClientDto(entity);
    }

    @Transactional
    public ClientDto insert(ClientDto dto) {
        Client entity = new Client();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ClientDto(entity);
    }

    @Transactional
    public ClientDto update(long id, ClientDto dto) {

        try {
            Client entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ClientDto(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }


    }

    public void delete(long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(ClientDto dto, Client client) {
        client.setName(dto.getName());
        client.setCpf(dto.getCpf());
        client.setIncome(dto.getIncome());
        client.setBirthDate(dto.getBirthDate());
        client.setChildren(dto.getChildren());

    }


}
