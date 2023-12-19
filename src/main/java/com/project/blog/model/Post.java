package com.project.blog.model;

import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "published")
	private boolean published;

	@Column(name = "access")
	private int access;

	@CreationTimestamp(source = SourceType.DB)
	private Instant createdOn;

	@UpdateTimestamp(source = SourceType.DB)
	private Instant lastUpdatedOn;

	public Post(String title, String description, boolean published) {
		this.title = title;
		this.description = description;
		this.published = published;
		this.access = 0;
	}
}
