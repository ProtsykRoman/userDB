package ua.com.userdb.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.com.userdb.dto.ReportRowDto;
import ua.com.userdb.model.DBUser;

@Repository
public interface ReportsRepository extends JpaRepository<DBUser, Integer> {

    // ===================== CERTIFICATES =====================

    @Query("""
        SELECT new ua.com.userdb.dto.ReportRowDto(
            u.id,
            u.identificationNumber,
            u.name,
            d.name,
            u.isActive,
            null,
            null,
            ct.name,
            c.expirationDate
        )
        FROM DBUserCertificate c
            JOIN c.dbUser u
            JOIN u.department d
            JOIN c.certificateType ct
        WHERE u.isActive = true
          AND (c.blocked = false OR c.blocked IS NULL)
          AND (:departmentId IS NULL OR d.id = :departmentId)
          AND (:certificateTypeId IS NULL OR ct.id = :certificateTypeId)
    """)
    List<ReportRowDto> findCertificatesNoDate(
        @Param("departmentId") Integer departmentId,
        @Param("certificateTypeId") Integer certificateTypeId
    );

    @Query("""
        SELECT new ua.com.userdb.dto.ReportRowDto(
            u.id,
            u.identificationNumber,
            u.name,
            d.name,
            u.isActive,
            null,
            null,
            ct.name,
            c.expirationDate
        )
        FROM DBUserCertificate c
            JOIN c.dbUser u
            JOIN u.department d
            JOIN c.certificateType ct
        WHERE u.isActive = true
          AND (c.blocked = false OR c.blocked IS NULL)
          AND (:departmentId IS NULL OR d.id = :departmentId)
          AND (:certificateTypeId IS NULL OR ct.id = :certificateTypeId)
          AND c.expirationDate <= :expTo
    """)
    List<ReportRowDto> findCertificatesWithDate(
        @Param("departmentId") Integer departmentId,
        @Param("certificateTypeId") Integer certificateTypeId,
        @Param("expTo") LocalDate expTo
    );

    // ===================== ACCESS BY DATABASE =====================

    @Query("""
        SELECT new ua.com.userdb.dto.ReportRowDto(
            u.id,
            u.identificationNumber,
            u.name,
            d.name,
            u.isActive,
            db.name,
            dr.name,
            null,
            a.accessExpirationDate
        )
        FROM DBUserAccess a
            JOIN a.dbUser u
            JOIN u.department d
            JOIN a.database db
            LEFT JOIN DBUserRole ur
                ON ur.dbUser = u
            LEFT JOIN ur.databaseRole dr
                ON dr.database = db
        WHERE u.isActive = true
          AND (a.isBlocked = false OR a.isBlocked IS NULL)
          AND (:departmentId IS NULL OR d.id = :departmentId)
          AND db.id = :databaseId
    """)
    List<ReportRowDto> findAccessesByDatabaseNoDate(
        @Param("departmentId") Integer departmentId,
        @Param("databaseId") Integer databaseId
    );

    @Query("""
        SELECT new ua.com.userdb.dto.ReportRowDto(
            u.id,
            u.identificationNumber,
            u.name,
            d.name,
            u.isActive,
            db.name,
            dr.name,
            null,
            a.accessExpirationDate
        )
        FROM DBUserAccess a
            JOIN a.dbUser u
            JOIN u.department d
            JOIN a.database db
            LEFT JOIN DBUserRole ur
                ON ur.dbUser = u
            LEFT JOIN ur.databaseRole dr
                ON dr.database = db
        WHERE u.isActive = true
          AND (a.isBlocked = false OR a.isBlocked IS NULL)
          AND (:departmentId IS NULL OR d.id = :departmentId)
          AND db.id = :databaseId
          AND a.accessExpirationDate <= :expTo
    """)
    List<ReportRowDto> findAccessesByDatabaseWithDate(
        @Param("departmentId") Integer departmentId,
        @Param("databaseId") Integer databaseId,
        @Param("expTo") LocalDate expTo
    );

    // ===================== ACCESS BY ROLE =====================

    @Query("""
        SELECT new ua.com.userdb.dto.ReportRowDto(
            u.id,
            u.identificationNumber,
            u.name,
            d.name,
            u.isActive,
            db.name,
            dr.name,
            null,
            a.accessExpirationDate
        )
        FROM DBUserRole ur
            JOIN ur.dbUser u
            JOIN u.department d
            JOIN ur.databaseRole dr
            JOIN dr.database db
            JOIN DBUserAccess a
                ON a.dbUser = u AND a.database = db
        WHERE u.isActive = true
          AND (a.isBlocked = false OR a.isBlocked IS NULL)
          AND (:departmentId IS NULL OR d.id = :departmentId)
          AND dr.id = :roleId
    """)
    List<ReportRowDto> findAccessesByRoleNoDate(
        @Param("departmentId") Integer departmentId,
        @Param("roleId") Integer roleId
    );

    @Query("""
        SELECT new ua.com.userdb.dto.ReportRowDto(
            u.id,
            u.identificationNumber,
            u.name,
            d.name,
            u.isActive,
            db.name,
            dr.name,
            null,
            a.accessExpirationDate
        )
        FROM DBUserRole ur
            JOIN ur.dbUser u
            JOIN u.department d
            JOIN ur.databaseRole dr
            JOIN dr.database db
            JOIN DBUserAccess a
                ON a.dbUser = u AND a.database = db
        WHERE u.isActive = true
          AND (a.isBlocked = false OR a.isBlocked IS NULL)
          AND (:departmentId IS NULL OR d.id = :departmentId)
          AND dr.id = :roleId
          AND a.accessExpirationDate <= :expTo
    """)
    List<ReportRowDto> findAccessesByRoleWithDate(
        @Param("departmentId") Integer departmentId,
        @Param("roleId") Integer roleId,
        @Param("expTo") LocalDate expTo
    );
    
    @Query("""
    	    SELECT new ua.com.userdb.dto.ReportRowDto(
    	        u.id,
    	        u.identificationNumber,
    	        u.name,
    	        d.name,
    	        u.isActive,
    	        db.name,
    	        dr.name,
    	        null,
    	        a.accessExpirationDate
    	    )
    	    FROM DBUserAccess a
    	        JOIN a.dbUser u
    	        JOIN u.department d
    	        JOIN a.database db
    	        LEFT JOIN DBUserRole ur
    	            ON ur.dbUser = u
    	        LEFT JOIN ur.databaseRole dr
    	            ON dr.database = db
    	    WHERE u.isActive = true
    	      AND (a.isBlocked = false OR a.isBlocked IS NULL)
    	      AND (:departmentId IS NULL OR d.id = :departmentId)
    	""")
    	List<ReportRowDto> findAccessesByDepartmentNoDate(
    	    @Param("departmentId") Integer departmentId
    	);

    	@Query("""
    	    SELECT new ua.com.userdb.dto.ReportRowDto(
    	        u.id,
    	        u.identificationNumber,
    	        u.name,
    	        d.name,
    	        u.isActive,
    	        db.name,
    	        dr.name,
    	        null,
    	        a.accessExpirationDate
    	    )
    	    FROM DBUserAccess a
    	        JOIN a.dbUser u
    	        JOIN u.department d
    	        JOIN a.database db
    	        LEFT JOIN DBUserRole ur
    	            ON ur.dbUser = u
    	        LEFT JOIN ur.databaseRole dr
    	            ON dr.database = db
    	    WHERE u.isActive = true
    	      AND (a.isBlocked = false OR a.isBlocked IS NULL)
    	      AND (:departmentId IS NULL OR d.id = :departmentId)
    	      AND a.accessExpirationDate <= :expTo
    	""")
    	List<ReportRowDto> findAccessesByDepartmentWithDate(
    	    @Param("departmentId") Integer departmentId,
    	    @Param("expTo") LocalDate expTo
    	);
}
