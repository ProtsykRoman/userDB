package ua.com.userdb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.userdb.dao.RankRepository;
import ua.com.userdb.model.Rank;

@Service
public class RankServiceImpl implements RankService{
	private RankRepository rankRepository;
	
	public RankServiceImpl(RankRepository rankRepository) {
		this.rankRepository = rankRepository;
	}
	
	@Override
	public List<Rank> findAll() {
		return rankRepository.findAll();
	}

	@Override
	public Optional<Rank> findRankById(Integer id) {
		return rankRepository.findById(id);
	}

	@Override
	public Optional<Rank> findRankByName(String name) {
		return rankRepository.findByName(name);
	}

	@Override
	public Rank createRank(Rank rank) {
		return rankRepository.save(rank);
	}

	@Override
	public Optional<Rank> updateRank(Integer id, Rank rank) {
		Optional<Rank> exitingRank = rankRepository.findById(id);
		if(exitingRank.isPresent()) {
			Rank updated = exitingRank.get();
			updated.setName(rank.getName());
			updated.setIsActive(rank.getIsActive());
			rankRepository.save(updated);
			return Optional.of(updated);
			}else {
			return Optional.empty();
		}
	}

	@Override
	public boolean deleteRank(Integer id) {
		if(rankRepository.existsById(id)) {
			rankRepository.deleteById(id);
			return true;
		}else {
			return false;
		}
	}

}
