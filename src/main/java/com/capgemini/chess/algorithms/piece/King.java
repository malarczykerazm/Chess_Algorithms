package com.capgemini.chess.algorithms.piece;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;

public class King  extends Piece {

	private final PieceType type = PieceType.KING;

	public King(Color color) {
		super(color);
	}
	
	@Override
	public List<Coordinate> possibleAttackMoves(Coordinate from) {
		List<Coordinate> allPossibleMoves = new ArrayList<Coordinate>();
		for(Coordinate move : possibleAttackMovesDiagonal(from)) {
			allPossibleMoves.add(move);
		}
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
	
	private List<Coordinate> possibleAttackMovesDiagonal(Coordinate from) {
		List<Coordinate> allPossibleMovesDiagonal = new ArrayList<Coordinate>();
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				if(i != 0 && j != 0) {
					Coordinate to = new Coordinate(from.getX() + i, from.getY() + i*j);
					allPossibleMovesDiagonal.add(to);
				}				
			}
		}
		return allPossibleMovesDiagonal;
	}
	
	private List<Coordinate> possibleAttackMovesDirY(Coordinate from) {
		List<Coordinate> allPossibleMovesDirY = new ArrayList<Coordinate>();
		for(int i = -1; i < 2; i++) {
			if(i != 0) {
				Coordinate to = new Coordinate(from.getX(), from.getY() + 1 * i);
				allPossibleMovesDirY.add(to);		
			}
		}
		return allPossibleMovesDirY;
	}
	
	private List<Coordinate> possibleAttackMovesDirX(Coordinate from) {
		List<Coordinate> allPossibleMovesDirX = new ArrayList<Coordinate>();
		for(int i = -1; i < 2; i++) {
			if(i != 0) {
				Coordinate to = new Coordinate(from.getX() + 1 * i, from.getY());
				allPossibleMovesDirX.add(to);		
			}
		}
		return allPossibleMovesDirX;
	}
	
}
