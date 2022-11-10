package com.devSuperior.pocCrud.resources;

import com.devSuperior.pocCrud.DTO.ClientDto;
import com.devSuperior.pocCrud.entities.Client;
import com.devSuperior.pocCrud.services.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/clients")
public class ClientResource {

    private final ClientService service;

    public ClientResource(ClientService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ClientDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "2") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy
    ) {

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<ClientDto> dtoList = service.findAll(pageRequest);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> findById(@PathVariable long id) {
        ClientDto clientDto = service.findById(id);
        return ResponseEntity.ok().body(clientDto);
    }

    @PostMapping
    public ResponseEntity<ClientDto> insert(@RequestBody ClientDto dto) {
        ClientDto clientDto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(clientDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> update(@PathVariable long id, @RequestBody ClientDto dto) {
        ClientDto clientDto = service.update(id, dto);
        return ResponseEntity.ok().body(clientDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
