package com.capgemini.chess.algorithms.piece;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;

public class EnPassantPawn extends Piece{
	
	public EnPassantPawn(Color color) {
		super(color);
	}

	@Override
	public List<Coordinate> possibleAttackMoves(Coordinate from) {
		return new ArrayList<Coordinate>();
	}
	
	@Override
	public List<Coordinate> possibleCaptureMoves(Coordinate from) {
		return new ArrayList<Coordinate>();
	}
	
}
