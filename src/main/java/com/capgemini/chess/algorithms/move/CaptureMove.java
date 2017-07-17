package com.capgemini.chess.algorithms.move;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.piece.Piece;

public class CaptureMove extends Move {

	public CaptureMove(Coordinate from, Coordinate to, Piece movedPiece) {
		super(from, to, movedPiece);
	}

}
