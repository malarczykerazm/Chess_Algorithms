package com.capgemini.chess.algorithms.move;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.piece.Piece;

public abstract class Move {

	private Coordinate from;
	private Coordinate to;
	private Piece movedPiece;
	
	public abstract MoveType getType();

	public Move(Coordinate from, Coordinate to) {
		this.from = from;
		this.to = to;
	}

	public Coordinate getFrom() {
		return from;
	}

	public void setFrom(Coordinate from) {
		this.from = from;
	}

	public Coordinate getTo() {
		return to;
	}

	public void setTo(Coordinate to) {
		this.to = to;
	}

	public Piece getMovedPiece() {
		return movedPiece;
	}

	public void setMovedPiece(Piece movedPiece) {
		this.movedPiece = movedPiece;
	}

}