package com.example.blog.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.blog.entity.Blog;
import com.example.blog.exceptionHandler.ResourceNotFoundException;
import com.example.blog.repository.BlogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;



@Service
@Slf4j
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper; // Autowire ObjectMapper for deserialization

    private static final String BLOG_CACHE_PREFIX = "BLOG_";

    public Page<Blog> getAllBlogs(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    public Blog getBlogById(Long id) {
        String cacheKey = BLOG_CACHE_PREFIX + id;
        Object cachedObject = redisTemplate.opsForValue().get(cacheKey);

        if (cachedObject != null) {
            log.info("Cache hit for blog id {}", id);
            return objectMapper.convertValue(cachedObject, Blog.class);
        }

        return blogRepository.findById(id).orElse(null); // Return null instead of throwing an exception
    }


    public Blog createBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    public Blog updateBlog(Long id, Blog updatedBlog) {
        Blog blog = getBlogById(id);
        blog.setTitle(updatedBlog.getTitle());
        blog.setContent(updatedBlog.getContent());
        return blogRepository.save(blog);
    }

    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
        redisTemplate.delete(BLOG_CACHE_PREFIX + id);
    }
}