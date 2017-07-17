package com.capgemini.chess.algorithms.piece;

import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;

public abstract class Piece {
	
	private final Color color;
	
	public Piece(Color color) {
		this.color = color;
	}
	
	public abstract List<Coordinate> possibleAttackMoves(Coordinate from);
	
	public abstract List<Coordinate> possibleCaptureMoves(Coordinate from);
	
	public static List<Coordinate> addIfValid(List<Coordinate> list, Coordinate square) {
		if(square.isValid()) {
			list.add(square);
		}
		return list;
	}
	
	public Color getColor() {
		return this.color;
	}

}