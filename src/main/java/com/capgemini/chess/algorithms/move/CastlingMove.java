package com.capgemini.chess.algorithms.move;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;

public class CastlingMove extends Move {
	
	private final MoveType type = MoveType.CASTLING;

	public CastlingMove(Coordinate from, Coordinate to) {
		super(from, to);
	}

	@Override
	public MoveType getType() {
		return type;
	}
	
}
