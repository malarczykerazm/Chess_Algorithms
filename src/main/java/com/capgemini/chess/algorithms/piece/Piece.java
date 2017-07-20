package com.capgemini.chess.algorithms.piece;

import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;

public abstract class Piece {
	
	private final Color color;
	
	public Piece(Color color) {
		this.color = color;
	}
	
	public abstract PieceType getType();
	
	public abstract List<Coordinate> possibleAttackMoves(Coordinate from);
	
	public List<Coordinate> possibleCaptureMoves(Coordinate from) {
		return possibleAttackMoves(from);
	}
	
	public List<Coordinate> possibleAttackMovesForFirstMove(Coordinate from) {
		return possibleAttackMoves(from);
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public List<Coordinate> addIfValid(List<Coordinate> coordinates, Coordinate square) {
		if(square.isValid()) {
			coordinates.add(square);
		}
		return coordinates;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Piece other = (Piece) obj;
		if (color != other.color)
			return false;
		return true;
	}

}