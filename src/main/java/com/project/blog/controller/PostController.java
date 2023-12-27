package com.project.blog.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.blog.model.Post;
import com.project.blog.repository.PostRepository;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class PostController {

	@Autowired
	PostRepository postRepository;

	@GetMapping("/posts")
	public ResponseEntity<Map<String, Object>> getAllPosts(@RequestParam(required = false) String title,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		try {

			List<Post> posts = new ArrayList<Post>();
			Pageable paging = PageRequest.of(page, size);

			Page<Post> postsPaging;

			if (title == null) {

				postsPaging = postRepository.findAll(paging);

			} else {
				postsPaging = postRepository.findByTitleContaining(title, paging);
			}

			if (postsPaging.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			posts = postsPaging.getContent();

			Map<String, Object> response = new HashMap<>();
			response.put("tutorials", posts);
			response.put("currentPage", postsPaging.getNumber());
			response.put("totalItems", postsPaging.getTotalElements());
			response.put("totalPages", postsPaging.getTotalPages());

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/posts/{id}")
	public ResponseEntity<Post> getPostById(@PathVariable("id") long id) {
		Optional<Post> postData = postRepository.findById(id);

		if (postData.isPresent()) {
			postData.get().setAccess(postData.get().getAccess() + 1);
			updatePost(id, postData.get());
			return new ResponseEntity<>(postData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/posts")
	public ResponseEntity<Post> createPost(@RequestBody Post post) {
		try {
			Post _post = postRepository
					.save(new Post(post.getTitle(), post.getDescription(), true));
			return new ResponseEntity<>(_post, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/posts/{id}")
	public ResponseEntity<Post> updatePost(@PathVariable("id") long id, @RequestBody Post post) {
		Optional<Post> postData = postRepository.findById(id);

		if (postData.isPresent()) {
			Post _post = postData.get();
			_post.setTitle(post.getTitle());
			_post.setDescription(post.getDescription());
			_post.setPublished(_post.isPublished());
			return new ResponseEntity<>(postRepository.save(_post), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/posts/status/{id}")
	public ResponseEntity<Post> updatePostVisibility(@PathVariable("id") long id, @RequestBody Post published) {
		Optional<Post> postData = postRepository.findById(id);

		if (postData.isPresent()) {
			Post _post = postData.get();
			_post.setPublished(published.isPublished());
			return new ResponseEntity<>(postRepository.save(_post), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/posts/{id}")
	public ResponseEntity<HttpStatus> deletePost(@PathVariable("id") long id) {
		try {
			postRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/posts")
	public ResponseEntity<HttpStatus> deleteAllPosts() {
		try {
			postRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/posts/published/{published}")
	public ResponseEntity<Map<String, Object>> findByPublished(@RequestParam(required = true) int published,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		try {

			List<Post> posts = new ArrayList<Post>();
			Pageable paging = PageRequest.of(page, size);

			Page<Post> postsPaging;

			if (published == 0) {

				postsPaging = postRepository.findByPublished(false, paging);

			} else {
				postsPaging = postRepository.findByPublished(true, paging);
			}

			if (postsPaging.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			posts = postsPaging.getContent();

			Map<String, Object> response = new HashMap<>();
			response.put("tutorials", posts);
			response.put("currentPage", postsPaging.getNumber());
			response.put("totalItems", postsPaging.getTotalElements());
			response.put("totalPages", postsPaging.getTotalPages());

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
