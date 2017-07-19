package com.capgemini.chess.algorithms.piece;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;

public class Bishop extends Piece {
	
	private final PieceType type = PieceType.BISHOP;

	public Bishop(Color color) {
		super(color);
	}

	@Override
	public List<Coordinate> possibleAttackMoves(Coordinate from) {
		List<Coordinate> allPossibleMoves = new ArrayList<Coordinate>();
		for(int i = (-1) * (Board.SIZE - 1); i < Board.SIZE; i++) {
			for(int j = -1; j < 2; j++) {
				if(i != 0 && j != 0) {
					Coordinate to = new Coordinate(from.getX() + i, from.getY() + i*j);
					allPossibleMoves.add(to);					
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
