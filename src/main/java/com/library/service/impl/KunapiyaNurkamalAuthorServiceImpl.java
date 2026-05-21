package com.library.service.impl;

import com.library.dto.request.KunapiyaNurkamalAuthorRequest;
import com.library.dto.response.KunapiyaNurkamalAuthorResponse;
import com.library.entity.KunapiyaNurkamalAuthor;
import com.library.exception.KunapiyaNurkamalDuplicateResourceException;
import com.library.exception.KunapiyaNurkamalResourceNotFoundException;
import com.library.mapper.KunapiyaNurkamalMapper;
import com.library.repository.KunapiyaNurkamalAuthorRepository;
import com.library.service.interfaces.KunapiyaNurkamalAuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KunapiyaNurkamalAuthorServiceImpl implements KunapiyaNurkamalAuthorService {

    private final KunapiyaNurkamalAuthorRepository authorRepository;
    private final KunapiyaNurkamalMapper mapper;

    @Override
    public KunapiyaNurkamalAuthorResponse createAuthor(KunapiyaNurkamalAuthorRequest request) {
        if (authorRepository.existsByFirstNameAndLastName(request.getFirstName(), request.getLastName())) {
            throw new KunapiyaNurkamalDuplicateResourceException("Author already exists: " + request.getFirstName() + " " + request.getLastName());
        }

        KunapiyaNurkamalAuthor author = mapper.toAuthor(request);
        KunapiyaNurkamalAuthor saved = authorRepository.save(author);
        log.info("Author created with id: {}", saved.getId());
        return mapper.toAuthorResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public KunapiyaNurkamalAuthorResponse getAuthorById(Long id) {
        KunapiyaNurkamalAuthor author = authorRepository.findById(id)
                .orElseThrow(() -> new KunapiyaNurkamalResourceNotFoundException("Author not found with id: " + id));
        return mapper.toAuthorResponse(author);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KunapiyaNurkamalAuthorResponse> getAllAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable).map(mapper::toAuthorResponse);
    }

    @Override
    public KunapiyaNurkamalAuthorResponse updateAuthor(Long id, KunapiyaNurkamalAuthorRequest request) {
        KunapiyaNurkamalAuthor author = authorRepository.findById(id)
                .orElseThrow(() -> new KunapiyaNurkamalResourceNotFoundException("Author not found with id: " + id));

        author.setFirstName(request.getFirstName());
        author.setLastName(request.getLastName());
        author.setBiography(request.getBiography());
        author.setNationality(request.getNationality());

        KunapiyaNurkamalAuthor updated = authorRepository.save(author);
        return mapper.toAuthorResponse(updated);
    }

    @Override
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new KunapiyaNurkamalResourceNotFoundException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }
}