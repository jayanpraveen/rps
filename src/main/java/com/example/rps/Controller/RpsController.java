package com.example.rps.Controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.example.rps.Model.Move;
import com.example.rps.Model.PlayerDTO;
import com.example.rps.Service.RpsService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class RpsController {

	private final RpsService service;

	@PostMapping("/create")
	public ResponseEntity<Map<String, Map<String, String>>> createRoom(@RequestParam int size) {
		return new ResponseEntity<>(Map.of("result", service.createRoom(size)), HttpStatus.CREATED);
	}

	@PostMapping("/r/{roomId}")
	public ResponseEntity<HttpStatus> addPlayerToRoom(@RequestBody PlayerDTO dto, @PathVariable String roomId,
			HttpSession session) {
		if (!service.doesRoomExist(roomId))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		service.addPlayerToRoom(dto, roomId, session);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/r/{roomId}/move")
	public ResponseEntity<Map<String, Map<String, String>>> playMove(@RequestBody Move move,
			@PathVariable String roomId,
			HttpSession session) {
		if (!service.doesRoomExist(roomId))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		String playerName = (String) session.getAttribute("USER_ID");
		if (playerName == "" || playerName == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		move.setPlayerName(playerName);
		move.setRoomId(roomId);
		var status = service.playMove(move);

		if (status == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(Map.of("result", status), HttpStatus.OK);
	}

}
