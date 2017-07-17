package com.capgemini.chess.algorithms.move;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.piece.Piece;

public class CastlingMove extends Move {

	public CastlingMove(Coordinate from, Coordinate to, Piece movedPiece) {
		super(from, to, movedPiece);
	}

}
