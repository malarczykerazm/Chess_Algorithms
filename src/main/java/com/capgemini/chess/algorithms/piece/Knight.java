package com.capgemini.chess.algorithms.piece;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;

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
					allPossibleMoves = addIfValid(allPossibleMoves, toShort);					
					Coordinate toLong = new Coordinate (from.getX() + 1 * i, from.getY() + 2 * j);
					allPossibleMoves = addIfValid(allPossibleMoves, toLong);
				}
			}
		}
		return allPossibleMoves;
	}
	
	@Override
	public PieceType getType() {
		return type;
	}
	
	@Override
	public boolean isTheWayFreeToGo(Board board, Coordinate from, Coordinate to) {
		return true;
	}

}