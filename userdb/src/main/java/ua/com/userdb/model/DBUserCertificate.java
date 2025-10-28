package ua.com.userdb.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name="db_users_certificates")
public class DBUserCertificate {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name="dbuser_id", nullable = false)
	private DBUser dbUser;
	@ManyToOne
	@JoinColumn(name="certificate_type_id", nullable = false)
	private CertificateType certificateType;
	@Temporal(TemporalType.DATE)
	private Date expirationDate;
	@Column(name="number")
	private String number;
	@Column(name="is_blocked")
	private Boolean isBlocked;
}
