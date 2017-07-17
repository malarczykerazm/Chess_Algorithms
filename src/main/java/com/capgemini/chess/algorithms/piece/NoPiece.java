package com.capgemini.chess.algorithms.piece;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;

public class NoPiece extends Piece {

	public NoPiece(Color color) {
		super(color);
	}

	@Override
	public List<Coordinate> possibleAttackMoves(Coordinate from) {
		//TODO WrongMoveException ?
		return new ArrayList<Coordinate>();
	}
	
	@Override
	public List<Coordinate> possibleCaptureMoves(Coordinate from) {
		return possibleAttackMoves(from);
	}

}
