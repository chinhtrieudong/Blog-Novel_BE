package com.blognovel.blognovel.service;

import com.blognovel.blognovel.dto.request.AuthorRequest;
import com.blognovel.blognovel.dto.response.AuthorResponse;
import com.blognovel.blognovel.model.Author;

import java.util.List;

public interface AuthorService {
    Author findOrCreateAuthor(String authorName);

    List<AuthorResponse> getAllAuthors();

    Author getAuthorById(Long id);

    AuthorResponse getAuthorResponseById(Long id);

    AuthorResponse createAuthor(AuthorRequest request);

    AuthorResponse updateAuthor(Long id, AuthorRequest request);

    void deleteAuthor(Long id);
}
