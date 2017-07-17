package com.capgemini.chess.algorithms.move;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.piece.Piece;

public class AttackMove extends Move {

	public AttackMove(Coordinate from, Coordinate to, Piece movedPiece) {
		super(from, to, movedPiece);
	}
	

}
