package com.example.rps.Repository;

import com.example.rps.Model.Room;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {
	Room findByRoomId(String roomId);
}
