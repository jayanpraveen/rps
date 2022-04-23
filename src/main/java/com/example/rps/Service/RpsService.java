package com.example.rps.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import com.example.rps.RpsApplication;
import com.example.rps.Model.Move;
import com.example.rps.Model.Player;
import com.example.rps.Model.PlayerDTO;
import com.example.rps.Model.Room;
import com.example.rps.Repository.RoomRepository;
import com.example.rps.Repository.PlayerRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RpsService {

	private final PlayerRepository playerRepo;
	private final RoomRepository roomRepo;

	public Map<String, String> createRoom(int size) {
		Room room = new Room();
		var uuid = UUID.randomUUID().toString().split("-"); // use a strong random generator in production
		room.setRoomId(String.valueOf(uuid[3]));
		room.setRoomSize(size);
		room.setTotalMovesLeft(size);
		roomRepo.save(room);
		return Map.of("roomId", uuid[3]);
	}

	public void addPlayerToRoom(PlayerDTO dto, String roomId, HttpSession session) {

		String playerName = dto.getPlayerName();

		if (isPlayerNameTakenForRoom(playerName, roomId) || isRoomIsFull(roomId)) {
			return;
		}

		if (playerName == "" || playerName == null)
			playerName = "Default Name";

		session.setAttribute(RpsApplication.SESSION_ID, dto.getPlayerName());

		playerRepo.save(new Player(playerName, roomId, false));
	}

	public Map<String, String> playMove(Move move) {

		String currMove = move.getHand();
		String playerName = move.getPlayerName();
		String roomId = move.getRoomId();
		Room currRoom = roomRepo.findByRoomId(roomId);

		if (!isPlayerInRoom(playerName, roomId) || hasPlayerMoved(playerName)) {
			return null;
		}

		setPlayerHasMoved(playerName);
		currRoom.reduceMovesLeft();

		System.out.println("current room: " + currRoom.getTotalMovesLeft());

		Map<String, String> playerStatus = getAllPlayersInRoomAsMap(roomId);
		Map<String, List<String>> playerMove = currRoom.getCurrentPlayerMove();

		List<String> playerMoveList = playerMove.get(currMove);
		playerMoveList.add(playerName);
		playerMove.put(currMove, playerMoveList);

		playerStatus = calculateResult(playerMove, playerStatus, currMove);

		System.out.println(playerStatus);

		currRoom.setCurrentPlayerMove(playerMove);
		roomRepo.save(currRoom);

		return playerStatus;
	}

	public boolean doesRoomExist(String roomId) {
		return roomRepo.findById(roomId).isPresent();
	}

	private boolean isRoomIsFull(String roomId) {
		Optional<Room> room = roomRepo.findById(roomId);
		if (room.isPresent())
			return room.get().getRoomSize() == playerRepo.findByRoomId(roomId).size();
		return true;
	}

	private void setPlayerHasMoved(String playerName) {
		Optional<Player> player = playerRepo.findById(playerName);
		if (player.isPresent()) {
			Player p = player.get();
			p.setHasPlayerMoved(true);
			playerRepo.save(p);
		}
	}

	private boolean hasPlayerMoved(String playerName) {
		var player = playerRepo.findById(playerName);
		if (player.isPresent())
			return player.get().isHasPlayerMoved();
		return false;
	}

	private boolean isPlayerInRoom(String playerName, String roomId) {
		return !playerRepo.findByRoomId(roomId).isEmpty();
	}

	private boolean isPlayerNameTakenForRoom(String playerName, String roomId) {
		long count = playerRepo.findByRoomId(roomId)
				.stream()
				.filter(p -> p.getPlayerName().equals(playerName))
				.count();

		return count > 0;
	}

	private Map<String, String> calculateResult(Map<String, List<String>> playerMove,
			Map<String, String> playerStatus, String currMove) {

		if (!playerMove.get("rock").isEmpty() &&
				!playerMove.get("paper").isEmpty() &&
				!playerMove.get("scissor").isEmpty())
			return null;

		if (!playerMove.get("rock").isEmpty()) {
			List<String> playerRockList = playerMove.get("scissor");
			if (!playerRockList.isEmpty())
				for (var player : playerRockList)
					playerStatus.put(player, "lost");
			return playerStatus;
		}

		if (!playerMove.get("paper").isEmpty()) {
			List<String> playerRockList = playerMove.get("rock");
			if (!playerRockList.isEmpty())
				for (var player : playerRockList)
					playerStatus.put(player, "lost");
			return playerStatus;
		}

		if (!playerMove.get("scissor").isEmpty()) {
			List<String> playerRockList = playerMove.get("paper");
			if (!playerRockList.isEmpty())
				for (var player : playerRockList)
					playerStatus.put(player, "lost");
			return playerStatus;
		}

		return null;
	}

	private HashMap<String, String> getAllPlayersInRoomAsMap(String roomId) {

		HashMap<String, String> playerMap = new HashMap<>();
		List<Player> playerList = playerRepo.findByRoomId(roomId);

		// initailly sets every player to "won".
		for (var player : playerList)
			playerMap.put(player.getPlayerName(), "won");

		return playerMap;
	}
}
