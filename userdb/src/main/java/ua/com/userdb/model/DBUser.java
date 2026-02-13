package ua.com.userdb.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name="dbusers", schema="userdb")
public class DBUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "name", nullable = false)
	private String name;
	@ManyToOne
	@JoinColumn(name = "rank_id", referencedColumnName = "id")
	private Rank rank;
	@ManyToOne
	@JoinColumn(name = "department_id", referencedColumnName = "id", nullable = false)
	private Department department;
	@Column(name = "identification_number")
	private Long identificationNumber;
	@Column(name = "is_active")
	private Boolean isActive = true;
	@OneToMany(mappedBy = "dbUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DBUserAccess> dbUserAccesses = new ArrayList<>();
    @OneToMany(mappedBy = "dbUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DBUserCertificate> dbUserCertificates = new ArrayList<>();
    @OneToMany(mappedBy = "dbUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DBUserRole> dbUserRoles = new ArrayList<>();
}
