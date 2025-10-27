package ua.com.userdb.service;

import java.util.List;
import java.util.Optional;

import ua.com.userdb.model.Rank;

public interface RankService {
	List<Rank> findAll();
	Optional<Rank> findRankById(Integer id);
	Optional<Rank> findRankByName(String name);
	Rank createRank(Rank rank);
	Optional<Rank> updateRank(Integer id, Rank rank);
	boolean deleteRank(Integer id);
}
