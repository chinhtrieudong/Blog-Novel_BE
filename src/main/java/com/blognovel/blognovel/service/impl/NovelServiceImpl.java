package com.blognovel.blognovel.service.impl;

import com.blognovel.blognovel.dto.request.NovelRequest;
import com.blognovel.blognovel.dto.response.NovelResponse;
import com.blognovel.blognovel.dto.response.PagedResponse;
import com.blognovel.blognovel.exception.AppException;
import com.blognovel.blognovel.exception.ErrorCode;
import com.blognovel.blognovel.mapper.GenreMapper;
import com.blognovel.blognovel.mapper.NovelMapper;
import com.blognovel.blognovel.model.Author;
import com.blognovel.blognovel.model.Novel;
import com.blognovel.blognovel.model.NovelFavorite;
import com.blognovel.blognovel.model.NovelLike;
import com.blognovel.blognovel.model.NovelRating;
import com.blognovel.blognovel.model.User;
import com.blognovel.blognovel.repository.GenreRepository;
import com.blognovel.blognovel.repository.NovelFavoriteRepository;
import com.blognovel.blognovel.repository.NovelLikeRepository;
import com.blognovel.blognovel.repository.NovelRatingRepository;
import com.blognovel.blognovel.repository.NovelRepository;
import com.blognovel.blognovel.repository.UserRepository;
import com.blognovel.blognovel.service.AuthorService;
import com.blognovel.blognovel.service.CloudinaryService;
import com.blognovel.blognovel.service.NovelService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NovelServiceImpl implements NovelService {

        private final NovelRepository novelRepository;
        private final UserRepository userRepository;
        private final GenreRepository genreRepository;
        private final NovelLikeRepository novelLikeRepository;
        private final NovelFavoriteRepository novelFavoriteRepository;
        private final NovelRatingRepository novelRatingRepository;
        private final AuthorService authorService;
        private final NovelMapper novelMapper;
        private final GenreMapper genreMapper;
        private final CloudinaryService cloudinaryService;

        @Override
        public PagedResponse<NovelResponse> getAllNovels(String title, Long genreId, String author, Pageable pageable) {
                if (StringUtils.hasText(author)) {
                        // Search by author name
                        Page<Novel> novels = novelRepository.findByAuthorName(author, pageable);
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

                // Search by title and genre using Specification
                Specification<Novel> spec = (root, query, criteriaBuilder) -> {
                        List<Predicate> predicates = new ArrayList<>();
                        if (StringUtils.hasText(title)) {
                                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")),
                                                "%" + title.toLowerCase() + "%"));
                        }
                        if (genreId != null) {
                                predicates.add(criteriaBuilder.equal(root.join("genres").get("id"), genreId));
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
        public PagedResponse<NovelResponse> getNovelsByAuthorName(String authorName, Pageable pageable) {
                Page<Novel> novels = novelRepository.findByAuthorName(authorName, pageable);
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
        public PagedResponse<NovelResponse> getNovelsByCreator(Long creatorId, Pageable pageable) {
                Page<Novel> novels = novelRepository.findByCreatedBy(creatorId, pageable);
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
        public PagedResponse<NovelResponse> getRelatedNovels(Long novelId, Pageable pageable) {
                Novel novel = novelRepository.findById(novelId)
                                .orElseThrow(() -> new AppException(ErrorCode.NOVEL_NOT_FOUND));

                // Get novels with same genre, excluding current novel
                Specification<Novel> spec = (root, query, criteriaBuilder) -> {
                        List<Predicate> predicates = new ArrayList<>();
                        // Same genres
                        predicates.add(criteriaBuilder.equal(root.join("genres").get("id"),
                                        novel.getGenres().iterator().next().getId()));
                        // Exclude current novel
                        predicates.add(criteriaBuilder.notEqual(root.get("id"), novelId));
                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                };

                Page<Novel> relatedNovels = novelRepository.findAll(spec, pageable);

                List<NovelResponse> novelResponses = relatedNovels.getContent().stream()
                                .map(novelMapper::toResponse)
                                .collect(Collectors.toList());

                return PagedResponse.<NovelResponse>builder()
                                .content(novelResponses)
                                .page(relatedNovels.getNumber())
                                .size(relatedNovels.getSize())
                                .totalElements(relatedNovels.getTotalElements())
                                .totalPages(relatedNovels.getTotalPages())
                                .last(relatedNovels.isLast())
                                .build();
        }

        @Override
        @Transactional
        public NovelResponse createNovel(NovelRequest novelRequest, Long currentUserId) {
                System.out.println("=== NOVEL CREATION DEBUG ===");
                System.out.println("authorIds: " + novelRequest.getAuthorIds());
                System.out.println("genreIds: " + novelRequest.getGenreIds());
                System.out.println("title: " + novelRequest.getTitle());
                System.out.println("description: " + novelRequest.getDescription());
                System.out.println("currentUserId: " + currentUserId);

                // Validate required fields
                if (novelRequest.getTitle() == null || novelRequest.getTitle().trim().isEmpty()) {
                        throw new AppException(ErrorCode.INVALID_REQUEST);
                }
                if (novelRequest.getDescription() == null || novelRequest.getDescription().trim().isEmpty()) {
                        throw new AppException(ErrorCode.INVALID_REQUEST);
                }
                if (novelRequest.getAuthorIds() == null || novelRequest.getAuthorIds().isEmpty()) {
                        throw new AppException(ErrorCode.INVALID_REQUEST);
                }
                if (novelRequest.getGenreIds() == null || novelRequest.getGenreIds().isEmpty()) {
                        throw new AppException(ErrorCode.INVALID_REQUEST);
                }

                Author author = authorService.getAuthorById(novelRequest.getAuthorIds().get(0));
                System.out.println("Found author: " + author.getName() + " (ID: " + author.getId() + ")");

                Novel novel = novelMapper.toEntity(novelRequest, cloudinaryService);
                // Set audit fields
                novel.setCreatedBy(currentUserId);
                novel.setUpdatedBy(currentUserId);
                System.out.println("Novel entity created, author set to: " + novel.getAuthor().getName());

                Novel savedNovel = novelRepository.save(novel);
                System.out.println("Novel saved with ID: " + savedNovel.getId());
                System.out.println("=== END DEBUG ===");

                return novelMapper.toResponse(savedNovel);
        }

        @Override
        public NovelResponse updateNovel(Long id, NovelRequest novelRequest, Long currentUserId) {
                Novel novel = novelRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.NOVEL_NOT_FOUND));

                novelMapper.updateNovelFromDto(novelRequest, novel, cloudinaryService);
                // Set audit field
                novel.setUpdatedBy(currentUserId);

                // Remove manual updatedAt setting - let @UpdateTimestamp handle it
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
        @Transactional
        public void likeNovel(Long novelId, Long userId) {
                Novel novel = novelRepository.findById(novelId)
                                .orElseThrow(() -> new AppException(ErrorCode.NOVEL_NOT_FOUND));
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

                // Check if user already liked this novel
                if (novelLikeRepository.existsByNovelIdAndUserId(novelId, userId)) {
                        // Unlike: remove the like
                        NovelLike like = novelLikeRepository.findByNovelIdAndUserId(novelId, userId).orElseThrow();
                        novelLikeRepository.delete(like);
                } else {
                        // Like: create new like
                        NovelLike like = NovelLike.builder()
                                        .novel(novel)
                                        .user(user)
                                        .build();
                        novelLikeRepository.save(like);
                }
        }

        @Override
        @Transactional
        public void favoriteNovel(Long novelId, Long userId) {
                Novel novel = novelRepository.findById(novelId)
                                .orElseThrow(() -> new AppException(ErrorCode.NOVEL_NOT_FOUND));
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

                // Check if user already favorited this novel
                if (novelFavoriteRepository.existsByNovelIdAndUserId(novelId, userId)) {
                        // Unfavorite: remove the favorite
                        NovelFavorite favorite = novelFavoriteRepository.findByNovelIdAndUserId(novelId, userId)
                                        .orElseThrow();
                        novelFavoriteRepository.delete(favorite);
                } else {
                        // Favorite: create new favorite
                        NovelFavorite favorite = NovelFavorite.builder()
                                        .novel(novel)
                                        .user(user)
                                        .build();
                        novelFavoriteRepository.save(favorite);
                }
        }

        @Override
        public Object getAllGenres() {
                List<com.blognovel.blognovel.model.Genre> genres = genreRepository.findAll();
                return genres.stream()
                                .map(genreMapper::toResponse)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public void rateNovel(Long novelId, int rating, Long userId) {
                Novel novel = novelRepository.findById(novelId)
                                .orElseThrow(() -> new AppException(ErrorCode.NOVEL_NOT_FOUND));
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

                // Validate rating range
                if (rating < 1 || rating > 5) {
                        throw new AppException(ErrorCode.INVALID_REQUEST);
                }

                // Check if user already rated this novel
                NovelRating existingRating = novelRatingRepository.findByNovelIdAndUserId(novelId, userId).orElse(null);

                if (existingRating != null) {
                        // Update existing rating
                        existingRating.setRating(rating);
                        novelRatingRepository.save(existingRating);
                } else {
                        // Create new rating
                        NovelRating newRating = NovelRating.builder()
                                        .novel(novel)
                                        .user(user)
                                        .rating(rating)
                                        .build();
                        novelRatingRepository.save(newRating);
                }

                // Recalculate average rating
                Double averageRating = novelRatingRepository.findAverageRatingByNovelId(novelId);
                if (averageRating != null) {
                        novel.setAvgRating(averageRating.floatValue());
                } else {
                        novel.setAvgRating(0F);
                }
                novelRepository.save(novel);
        }

        @Override
        public NovelResponse updateNovelStatus(Long id, String status) {
                Novel novel = novelRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.NOVEL_NOT_FOUND));

                try {
                        com.blognovel.blognovel.enums.NovelStatus newStatus = com.blognovel.blognovel.enums.NovelStatus
                                        .valueOf(status.toUpperCase());
                        novel.setStatus(newStatus);

                        Novel updatedNovel = novelRepository.save(novel);
                        return novelMapper.toResponse(updatedNovel);
                } catch (IllegalArgumentException e) {
                        throw new AppException(ErrorCode.INVALID_STATUS);
                } catch (Exception e) {
                        throw new AppException(ErrorCode.UPLOAD_FAILED);
                }
        }
}
