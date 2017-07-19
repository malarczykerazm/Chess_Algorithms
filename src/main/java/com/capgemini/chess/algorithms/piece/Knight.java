package com.capgemini.chess.algorithms.piece;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;

public class Knight  extends Piece {

	private final PieceType type = PieceType.KNIGHT;

	public Knight(Color color) {
		super(color);
	}
	
	@Override
	public List<Coordinate> possibleAttackMoves(Coordinate from) {
		List<Coordinate> allPossibleMoves = new ArrayList<Coordinate>();
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				if(i != 0 && j != 0) {
					Coordinate toShort = new Coordinate (from.getX() + 2 * i, from.getY() + 1 * j);
					allPossibleMoves.add(toShort);					
					Coordinate toLong = new Coordinate (from.getX() + 1 * i, from.getY() + 2 * j);
					allPossibleMoves.add(toLong);
				}
			}
		}
		return allPossibleMoves;
	}
	
	@Override
	public List<Coordinate> possibleCaptureMoves(Coordinate from) {
		return possibleAttackMoves(from);
	}
	
	@Override
	public PieceType getType() {
		return type;
	}

}