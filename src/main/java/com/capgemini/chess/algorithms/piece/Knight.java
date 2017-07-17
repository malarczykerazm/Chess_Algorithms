package com.capgemini.chess.algorithms.piece;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;

public class Knight  extends Piece {

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
					allPossibleMoves = Piece.addIfValid(allPossibleMoves, toShort);					
					Coordinate toLong = new Coordinate (from.getX() + 1 * i, from.getY() + 2 * j);
					allPossibleMoves = Piece.addIfValid(allPossibleMoves, toLong);
				}
			}
		}
		return allPossibleMoves;
	}
	
	@Override
	public List<Coordinate> possibleCaptureMoves(Coordinate from) {
		return possibleAttackMoves(from);
	}

}