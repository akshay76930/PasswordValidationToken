package com.talenttrack.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entity representing a Department.
 *
 * <p>
 * Author: Akshay Dhere &lt;akshaydhere14@gmail.com&gt;
 * </p>
 */
@Entity
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;

	// No-argument constructor required by Hibernate
	public Department() {
	}

	// Private constructor for the builder
	private Department(DepartmentBuilder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.description = builder.description;
	}

	public static class DepartmentBuilder {
		private Long id;
		private String name;
		private String description;

		public DepartmentBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public DepartmentBuilder name(String name) {
			this.name = name;
			return this;
		}

		public DepartmentBuilder description(String description) {
			this.description = description;
			return this;
		}

		public Department build() {
			return new Department(this);
		}
	}

	// Getters
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "Department{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + '}';
	}
}
