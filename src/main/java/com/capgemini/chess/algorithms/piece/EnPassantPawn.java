package com.capgemini.chess.algorithms.piece;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;

public class EnPassantPawn extends Piece{
	
	private final PieceType type = PieceType.EN_PASSANT_PAWN;

	public EnPassantPawn(Color color) {
		super(color);
	}

	@Override
	public List<Coordinate> possibleAttackMoves(Coordinate from) {
		return new ArrayList<Coordinate>();
	}
	
	@Override
	public PieceType getType() {
		return type;
	}
	
}
