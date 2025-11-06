package ua.com.userdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="users", schema="userdb")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name="username", unique = true, nullable = false)
	private String username;
	@Column(name="role", nullable = false)
	private Role role;
	@Column(name="password", nullable = false)
	private String password;
	@Column(name="is_active")
	private Boolean isActive = true;
	@ManyToOne
	@JoinColumn(name="department_id", referencedColumnName = "id")
	private Department department;
	@Transient
    private boolean changePassword;
    @Transient
    private String oldPassword;
    @Transient
    private String newPassword;
    @Transient
    private String confirmPassword;
}
