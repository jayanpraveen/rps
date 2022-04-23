package com.example.rps.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Document
@Getter
@Setter
@AllArgsConstructor
public class Room {

	@Id
	private String roomId;
	private int roomSize;
	private int totalMovesLeft;
	private Map<String, List<String>> currentPlayerMove;
	private Map<String, String> currentPlayerStatus;
	private Map<String, String> playerStatus;

	public Room() {
		this.currentPlayerMove = new HashMap<>();
		this.currentPlayerMove.put("rock", new ArrayList<>());
		this.currentPlayerMove.put("paper", new ArrayList<>());
		this.currentPlayerMove.put("scissor", new ArrayList<>());

		this.totalMovesLeft = roomSize;
	}

	public void reduceMovesLeft() {
		this.totalMovesLeft = this.totalMovesLeft - 1;
	}

}
