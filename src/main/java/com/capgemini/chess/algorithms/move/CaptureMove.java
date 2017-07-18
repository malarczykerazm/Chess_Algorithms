package com.capgemini.chess.algorithms.move;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.piece.Piece;

public class CaptureMove extends Move {
	
	private final MoveType type = MoveType.CAPTURE;

	public CaptureMove(Coordinate from, Coordinate to, Piece movedPiece) {
		super(from, to, movedPiece);
	}
	
	@Override
	public MoveType getType() {
		return type;
	}
	
}
