package com.blognovel.blognovel.service.impl;

import com.blognovel.blognovel.dto.request.PostRequest;
import com.blognovel.blognovel.dto.response.PagedResponse;
import com.blognovel.blognovel.dto.response.PostResponse;
import com.blognovel.blognovel.dto.response.CategoryResponse;
import com.blognovel.blognovel.dto.response.TagResponse;
import com.blognovel.blognovel.enums.PostStatus;
import com.blognovel.blognovel.exception.AppException;
import com.blognovel.blognovel.exception.ErrorCode;
import com.blognovel.blognovel.mapper.PostMapper;
import com.blognovel.blognovel.repository.PostRepository;
import com.blognovel.blognovel.repository.CategoryRepository;
import com.blognovel.blognovel.repository.TagRepository;
import com.blognovel.blognovel.repository.UserRepository;
import com.blognovel.blognovel.service.CloudinaryService;
import com.blognovel.blognovel.service.PostService;

import com.blognovel.blognovel.service.util.SlugGenerator;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.blognovel.blognovel.model.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

        private final PostRepository postRepository;
        private final CategoryRepository categoryRepository;
        private final TagRepository tagRepository;
        private final UserRepository userRepository;
        private final PostMapper postMapper;
        private final CloudinaryService cloudinaryService;

        @Override
        public PagedResponse<PostResponse> getAllPosts(String title, Long categoryId, Long tagId, String status,
                        Pageable pageable) {

                Specification<Post> spec = Specification.unrestricted();

                if (StringUtils.hasText(title)) {
                        spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("title")),
                                        "%" + title.toLowerCase() + "%"));
                }

                if (categoryId != null) {
                        spec = spec.and((root, query, cb) -> cb.isMember(
                                        categoryRepository.getReferenceById(categoryId), root.get("categories")));
                }

                if (tagId != null) {
                        spec = spec.and((root, query, cb) -> cb.isMember(tagId, root.get("tags")));
                }

                if (StringUtils.hasText(status)) {
                        spec = spec
                                        .and((root, query, cb) -> cb.equal(root.get("status"),
                                                        PostStatus.valueOf(status.toUpperCase())));
                }

                Page<Post> posts = postRepository.findAll(spec, pageable);

                List<PostResponse> postResponses = posts.getContent().stream()
                                .map(postMapper::toResponse)
                                .collect(Collectors.toList());

                return PagedResponse.<PostResponse>builder()
                                .content(postResponses)
                                .page(posts.getNumber())
                                .size(posts.getSize())
                                .totalElements(posts.getTotalElements())
                                .totalPages(posts.getTotalPages())
                                .last(posts.isLast())
                                .build();
        }

        @Override
        public PagedResponse<PostResponse> getPostsByAuthor(Long authorId, Pageable pageable) {
                Page<Post> posts = postRepository.findByAuthorId(authorId, pageable);

                List<PostResponse> postResponses = posts.getContent().stream()
                                .map(postMapper::toResponse)
                                .collect(Collectors.toList());

                return PagedResponse.<PostResponse>builder()
                                .content(postResponses)
                                .page(posts.getNumber())
                                .size(posts.getSize())
                                .totalElements(posts.getTotalElements())
                                .totalPages(posts.getTotalPages())
                                .last(posts.isLast())
                                .build();
        }

        @Override
        public PostResponse getPostById(Long id) {
                Post post = postRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

                return postMapper.toResponse(post);
        }

        @Override
        public PostResponse createPost(PostRequest request, Long authorId) {
                User author = userRepository.findById(authorId)
                                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

                Set<Category> categories = request.getCategoryIds().stream()
                                .map(categoryId -> categoryRepository.findById(categoryId)
                                                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)))
                                .collect(Collectors.toSet());

                Set<Tag> tags = request.getTagIds().stream()
                                .map(tagId -> tagRepository.findById(tagId)
                                                .orElseThrow(() -> new AppException(ErrorCode.TAG_NOT_FOUND)))
                                .collect(Collectors.toSet());

                Post post = Post.builder()
                                .title(request.getTitle())
                                .content(request.getContent())
                                .author(author)
                                .categories(categories)
                                .tags(tags)
                                .status(PostStatus.valueOf(request.getStatus().toUpperCase()))
                                .build();

                String slug = SlugGenerator.makeSlug(request.getTitle());
                post.setSlug(slug);

                if (request.getCoverImage() != null && !request.getCoverImage().isEmpty()) {
                        try {
                                String imageUrl = cloudinaryService.uploadImage(request.getCoverImage());
                                post.setCoverImage(imageUrl);
                        } catch (Exception e) {
                                // Log the error and set a default image URL
                                post.setCoverImage("https://via.placeholder.com/600x400?text=No+Image");
                        }
                }

                Post savedPost = postRepository.save(post);
                return postMapper.toResponse(savedPost);
        }

        @Override
        public PostResponse updatePost(Long id, PostRequest request) {
                Post post = postRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

                Set<Category> categories = request.getCategoryIds().stream()
                                .map(categoryId -> categoryRepository.findById(categoryId)
                                                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)))
                                .collect(Collectors.toSet());

                Set<Tag> tags = request.getTagIds().stream()
                                .map(tagId -> tagRepository.findById(tagId)
                                                .orElseThrow(() -> new AppException(ErrorCode.TAG_NOT_FOUND)))
                                .collect(Collectors.toSet());

                post.setTitle(request.getTitle());
                post.setContent(request.getContent());
                post.setCategories(categories);
                post.setTags(tags);
                post.setStatus(PostStatus.valueOf(request.getStatus().toUpperCase()));

                String slug = SlugGenerator.makeSlug(request.getTitle());
                post.setSlug(slug);

                if (request.getCoverImage() != null && !request.getCoverImage().isEmpty()) {
                        try {
                                String imageUrl = cloudinaryService.uploadImage(request.getCoverImage());
                                post.setCoverImage(imageUrl);
                        } catch (Exception e) {
                                // Log the error and set a default image URL
                                post.setCoverImage("https://via.placeholder.com/600x400?text=No+Image");
                        }
                }

                Post updatedPost = postRepository.save(post);
                return postMapper.toResponse(updatedPost);
        }

        @Override
        public void deletePost(Long id) {
                Post post = postRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
                postRepository.delete(post);
        }

        @Override
        public void incrementViewCount(Long postId) {
                postRepository.incrementViewCount(postId);
        }

        @Override
        public PostResponse updatePostStatus(Long id, String status) {
                Post post = postRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

                try {
                        PostStatus newStatus = PostStatus.valueOf(status.toUpperCase());
                        post.setStatus(newStatus);

                        Post updatedPost = postRepository.save(post);
                        return postMapper.toResponse(updatedPost);
                } catch (IllegalArgumentException e) {
                        throw new AppException(ErrorCode.INVALID_STATUS);
                } catch (Exception e) {
                        throw new AppException(ErrorCode.UPLOAD_FAILED);
                }
        }
}
