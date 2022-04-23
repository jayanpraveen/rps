package com.example.rps.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Move {

	public String playerName;
	public String roomId;
	public String hand; // hand represents hand gesture (i.e rock, paper, scissor)

}
