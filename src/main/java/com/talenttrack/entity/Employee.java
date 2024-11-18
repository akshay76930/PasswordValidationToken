package com.talenttrack.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor

@Builder
public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(length = 15)
	private String contact;

	@Column(length = 10)
	private String gender;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private LocalDateTime expirationDate;

	@Column(nullable = false)
	private LocalDateTime dateOfJoining;

	@Column(length = 255)
	private String address;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private User user;

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", contact=" + contact + ", gender=" + gender + ", email="
				+ email + ", dateOfJoining=" + dateOfJoining + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Employee employee = (Employee) o;
		return Objects.equals(id, employee.id) && Objects.equals(email, employee.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, email);
	}

	public Employee(Long id, String name, String contact, String gender, String email, String password,
			LocalDateTime expirationDate, LocalDateTime dateOfJoining, String address, User user) {
		super();
		this.id = id;
		this.name = name;
		this.contact = contact;
		this.gender = gender;
		this.email = email;
		this.password = password;
		this.expirationDate = expirationDate;
		this.dateOfJoining = dateOfJoining;
		this.address = address;
		this.user = user;
	}
}
