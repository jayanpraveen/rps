package com.example.rps.Repository;

import java.util.List;

import com.example.rps.Model.Player;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {
	List<Player> findByRoomId(String roomId);
}
