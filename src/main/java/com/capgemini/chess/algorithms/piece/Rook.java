package com.capgemini.chess.algorithms.piece;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;

public class Rook  extends Piece {

	private final PieceType type = PieceType.ROOK;
	
	public Rook(Color color) {
		super(color);
	}

	@Override
	public List<Coordinate> possibleAttackMoves(Coordinate from) {
		List<Coordinate> allPossibleMoves = new ArrayList<Coordinate>();
		for(Coordinate move : possibleAttackMovesDirY(from)) {
			allPossibleMoves.add(move);
		}
		for(Coordinate move : possibleAttackMovesDirX(from)) {
			allPossibleMoves.add(move);
		}
		return allPossibleMoves;
	}
	
	@Override
	public PieceType getType() {
		return type;
	}
	
	private List<Coordinate> possibleAttackMovesDirY(Coordinate from) {
		List<Coordinate> allPossibleMovesDirY= new ArrayList<Coordinate>();
		for(int i = (-1) * (Board.SIZE - 1); i < Board.SIZE; i++) {
			if(i != 0) {
				Coordinate to = new Coordinate(from.getX(), from.getY() + i);
				allPossibleMovesDirY.add(to);		
			}
		}
		return allPossibleMovesDirY;
	}
	
	private List<Coordinate> possibleAttackMovesDirX(Coordinate from) {
		List<Coordinate> allPossibleMovesDirX = new ArrayList<Coordinate>();
		for(int i = (-1) * (Board.SIZE - 1); i < Board.SIZE; i++) {
			if(i != 0) {
				Coordinate to = new Coordinate(from.getX() + i, from.getY());
				allPossibleMovesDirX.add(to);		
			}
		}
		return allPossibleMovesDirX;
	}

}
