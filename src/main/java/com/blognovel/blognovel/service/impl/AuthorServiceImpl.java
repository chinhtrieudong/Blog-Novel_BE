package com.blognovel.blognovel.service.impl;

import com.blognovel.blognovel.dto.request.AuthorRequest;
import com.blognovel.blognovel.dto.response.AuthorResponse;
import com.blognovel.blognovel.exception.AppException;
import com.blognovel.blognovel.exception.ErrorCode;
import com.blognovel.blognovel.mapper.AuthorMapper;
import com.blognovel.blognovel.model.Author;
import com.blognovel.blognovel.repository.AuthorRepository;
import com.blognovel.blognovel.service.AuthorService;
import com.blognovel.blognovel.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public Author findOrCreateAuthor(String authorName) {
        return authorRepository.findByName(authorName)
                .orElseGet(() -> {
                    Author newAuthor = Author.builder()
                            .name(authorName)
                            .build();
                    return authorRepository.save(newAuthor);
                });
    }

    @Override
    @Transactional
    public AuthorResponse createAuthor(AuthorRequest request) {
        if (authorRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        Author author = Author.builder()
                .name(request.getName())
                .bio(request.getBio())
                .avatarUrl(uploadImage(request.getAvatarImage()))
                .build();

        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toResponse(savedAuthor);
    }

    @Override
    public List<AuthorResponse> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(authorMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public AuthorResponse getAuthorResponseById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return authorMapper.toResponse(author);
    }

    @Override
    @Transactional
    public AuthorResponse updateAuthor(Long id, AuthorRequest request) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        author.setName(request.getName());
        author.setBio(request.getBio());
        if (request.getAvatarImage() != null && !request.getAvatarImage().isEmpty()) {
            author.setAvatarUrl(uploadImage(request.getAvatarImage()));
        }

        Author updatedAuthor = authorRepository.save(author);
        return authorMapper.toResponse(updatedAuthor);
    }

    @Override
    @Transactional
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        authorRepository.deleteById(id);
    }

    private String uploadImage(org.springframework.web.multipart.MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            return cloudinaryService.uploadImage(file);
        } catch (IOException e) {
            throw new AppException(ErrorCode.UPLOAD_FAILED);
        }
    }
}
