package com.example.rps.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Document
@Getter
@Setter
@AllArgsConstructor
public class Player {

	@Id
	private String playerName;
	private String roomId;
	private boolean hasPlayerMoved;

}
