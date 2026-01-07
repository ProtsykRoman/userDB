package ua.com.userdb.service;

import java.util.List;
import java.util.Optional;
import ua.com.userdb.model.DBUser;

public interface DBUserService {
    List<DBUser> findAll();
    Optional<DBUser> findById(Integer id);
    Optional<DBUser> findByName(String name);

    DBUser createDBUser(DBUser dbUser);
    DBUser updateDBUser(Integer id, DBUser dbUser);

    boolean deleteDBUser(Integer id);
    
    boolean existsByIdentificationNumber(Integer identificationNumber);
}
