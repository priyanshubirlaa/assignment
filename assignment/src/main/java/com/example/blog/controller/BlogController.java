package com.example.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.entity.Blog;
import com.example.blog.jwt.JwtUtil;
import com.example.blog.service.BlogService;
import com.example.blog.service.GeminiService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/blogs")
@Tag(name = "Blog APIs", description = "APIs for managing blogs")
public class BlogController {
    
    @Autowired
    private BlogService blogService;
    
    @Autowired
    private GeminiService geminiService; 
    
    private JwtUtil jwtUtil = new JwtUtil();

    


    @Operation(summary = "Get all blogs (paginated)")
    @GetMapping
    public ResponseEntity<Page<Blog>> getAllBlogs(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(blogService.getAllBlogs(PageRequest.of(page, size)));
    }

    @Operation(summary = "Get a blog by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Blog> getBlogById(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.getBlogById(id));
    }

    @Operation(summary = "Create a new blog")
    @PostMapping
    public ResponseEntity<Blog> createBlog(@RequestBody Blog blog) {
        return new ResponseEntity<>(blogService.createBlog(blog), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a blog")
    @PutMapping("/{id}")
    public ResponseEntity<Blog> updateBlog(@PathVariable Long id, @RequestBody Blog blog) {
        return ResponseEntity.ok(blogService.updateBlog(id, blog));
    }

    @Operation(summary = "Delete a blog")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Generate summary for a blog using Gemini AI")
    @GetMapping("/{id}/summary")
    public ResponseEntity<String> getBlogSummary(@PathVariable Long id) {
        Blog blog = blogService.getBlogById(id);
        
        if (blog == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blog not found"); // ✅ Correctly returns 404
        }

        String summary = geminiService.generateSummary(blog.getContent());
        return ResponseEntity.ok(summary);
    }
    

    @Operation(summary = "Api to generate jwt token")
    @GetMapping("/generate-token")
    public String generateToken() {
        // You can generate a token for any identifier (no username or password needed)
        String subject = "anonymous"; // You can set any identifier here
        return jwtUtil.generateToken(subject);
    }
    
    
  

 

	/*
	 * @GetMapping("/generateToken") public String generateToken() { return
	 * jwtUtil.generateToken(); }
	 */
}
