package com.blognovel.blognovel.service.impl;

import com.blognovel.blognovel.dto.request.NovelRequest;
import com.blognovel.blognovel.dto.response.NovelResponse;
import com.blognovel.blognovel.dto.response.PagedResponse;
import com.blognovel.blognovel.exception.AppException;
import com.blognovel.blognovel.exception.ErrorCode;
import com.blognovel.blognovel.mapper.GenreMapper;
import com.blognovel.blognovel.mapper.NovelMapper;
import com.blognovel.blognovel.model.Novel;
import com.blognovel.blognovel.model.User;
import com.blognovel.blognovel.repository.GenreRepository;
import com.blognovel.blognovel.repository.NovelRepository;
import com.blognovel.blognovel.repository.UserRepository;
import com.blognovel.blognovel.service.CloudinaryService;
import com.blognovel.blognovel.service.NovelService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NovelServiceImpl implements NovelService {

    private final NovelRepository novelRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final NovelMapper novelMapper;
    private final GenreMapper genreMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    public PagedResponse<NovelResponse> getAllNovels(String title, Long genreId, String author, Pageable pageable) {
        Specification<Novel> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(title)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")),
                        "%" + title.toLowerCase() + "%"));
            }
            if (genreId != null) {
                predicates.add(criteriaBuilder.equal(root.get("genreId"), genreId));
            }
            if (StringUtils.hasText(author)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("author")),
                        "%" + author.toLowerCase() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Novel> novels = novelRepository.findAll(spec, pageable);
        List<NovelResponse> novelResponses = novels.getContent().stream()
                .map(novelMapper::toResponse)
                .collect(Collectors.toList());

        return PagedResponse.<NovelResponse>builder()
                .content(novelResponses)
                .page(novels.getNumber())
                .size(novels.getSize())
                .totalElements(novels.getTotalElements())
                .totalPages(novels.getTotalPages())
                .last(novels.isLast())
                .build();
    }

    @Override
    public PagedResponse<NovelResponse> getNovelsByAuthor(Long authorId, Pageable pageable) {
        Page<Novel> novels = novelRepository.findByAuthorId(authorId, pageable);
        List<NovelResponse> novelResponses = novels.getContent().stream()
                .map(novelMapper::toResponse)
                .collect(Collectors.toList());

        return PagedResponse.<NovelResponse>builder()
                .content(novelResponses)
                .page(novels.getNumber())
                .size(novels.getSize())
                .totalElements(novels.getTotalElements())
                .totalPages(novels.getTotalPages())
                .last(novels.isLast())
                .build();
    }

    @Override
    public NovelResponse getNovelById(Long id) {
        Novel novel = novelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOVEL_NOT_FOUND));
        return novelMapper.toResponse(novel);
    }

    @Override
    public NovelResponse createNovel(NovelRequest novelRequest, Long currentUserId) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Novel novel = novelMapper.toEntity(novelRequest);

        novel.setAuthor(user);
        if (novelRequest.getCoverImage() != null && !novelRequest.getCoverImage().isEmpty()) {
            try {
                String imageUrl = cloudinaryService.uploadImage(novelRequest.getCoverImage());
                novel.setCoverImage(imageUrl);
            } catch (IOException e) {
                throw new AppException(ErrorCode.UPLOAD_FAILED);
            }
        }

        Novel savedNovel = novelRepository.save(novel);
        return novelMapper.toResponse(savedNovel);
    }

    @Override
    public NovelResponse updateNovel(Long id, NovelRequest novelRequest) {
        Novel novel = novelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOVEL_NOT_FOUND));

        novelMapper.updateNovelFromDto(novelRequest, novel);

        if (novelRequest.getCoverImage() != null && !novelRequest.getCoverImage().isEmpty()) {
            try {
                String imageUrl = cloudinaryService.uploadImage(novelRequest.getCoverImage());
                novel.setCoverImage(imageUrl);
            } catch (IOException e) {
                throw new AppException(ErrorCode.UPLOAD_FAILED);
            }
        }

        Novel updatedNovel = novelRepository.save(novel);
        return novelMapper.toResponse(updatedNovel);
    }

    @Override
    public void deleteNovel(Long id) {
        Novel novel = novelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOVEL_NOT_FOUND));
        novelRepository.delete(novel);
    }

    @Override
    public void likeNovel(Long novelId, Long userId) {
        Novel novel = novelRepository.findById(novelId)
                .orElseThrow(() -> new AppException(ErrorCode.NOVEL_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

    }

    @Override
    public void favoriteNovel(Long novelId, Long userId) {
        Novel novel = novelRepository.findById(novelId)
                .orElseThrow(() -> new AppException(ErrorCode.NOVEL_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public Object getAllGenres() {
        List<com.blognovel.blognovel.model.Genre> genres = genreRepository.findAll();
        return genres.stream()
                .map(genreMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void rateNovel(Long novelId, int rating, Long userId) {
        Novel novel = novelRepository.findById(novelId)
                .orElseThrow(() -> new AppException(ErrorCode.NOVEL_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
