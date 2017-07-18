package com.capgemini.chess.algorithms.move;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;

public class EnPassantMove extends Move {
	
	private final MoveType type = MoveType.EN_PASSANT;

	public EnPassantMove(Coordinate from, Coordinate to) {
		super(from, to);
	}
	
	@Override
	public MoveType getType() {
		return type;
	}

}
