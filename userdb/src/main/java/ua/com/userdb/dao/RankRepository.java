package ua.com.userdb.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.userdb.model.Rank;

public interface RankRepository extends JpaRepository<Rank, Integer>{
	Optional<Rank> findByName(String name);
}
