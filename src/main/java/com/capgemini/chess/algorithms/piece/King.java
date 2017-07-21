package com.capgemini.chess.algorithms.piece;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;

/**
 * an object representation of a king chess piece
 * @author EMALARCZ
 *
 */
public class King extends Piece {

	private final PieceType type = PieceType.KING;

	public King(Color color) {
		super(color);
	}

	/**
	 * checks where the piece could possible go in the next turn of the game 
	 * in case of the attack type of move
	 */
	@Override
	public List<Coordinate> possibleAttackMoves(Coordinate from) {
		List<Coordinate> allPossibleMoves = new ArrayList<Coordinate>();
		for (Coordinate to : possibleAttackMovesDiagonal(from)) {
			allPossibleMoves = addIfValid(allPossibleMoves, to);
		}
		for (Coordinate to : possibleAttackMovesDirY(from)) {
			allPossibleMoves = addIfValid(allPossibleMoves, to);
		}
		for (Coordinate to : possibleAttackMovesDirX(from)) {
			allPossibleMoves = addIfValid(allPossibleMoves, to);
		}
		return allPossibleMoves;
	}

	@Override
	public PieceType getType() {
		return type;
	}

	/**
	 * checks if the way of the piece is free to go (always free to go for a king)
	 */
	@Override
	public boolean isTheWayFreeToGo(Board board, Coordinate from, Coordinate to) {
		return true;
	}

	/**
	 * gets the list of possible coordinates where the king can go to castle
	 * @param from start coordinate
	 * @return list of coordinates where the king can go to castle
	 */
	public List<Coordinate> possibleCastlingMoves(Coordinate from) {
		List<Coordinate> allPossibleCastlingMoves = new ArrayList<Coordinate>();
		for (int i = -1; i < 2; i++) {
			if (i != 0) {
				Coordinate to = new Coordinate(from.getX() + 2 * i, from.getY());
				allPossibleCastlingMoves = addIfValid(allPossibleCastlingMoves, to);
			}
		}
		return allPossibleCastlingMoves;
	}

	private List<Coordinate> possibleAttackMovesDiagonal(Coordinate from) {
		List<Coordinate> allPossibleMovesDiagonal = new ArrayList<Coordinate>();
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (i != 0 && j != 0) {
					Coordinate to = new Coordinate(from.getX() + i, from.getY() + i * j);
					allPossibleMovesDiagonal = addIfValid(allPossibleMovesDiagonal, to);
				}
			}
		}
		return allPossibleMovesDiagonal;
	}

	private List<Coordinate> possibleAttackMovesDirY(Coordinate from) {
		List<Coordinate> allPossibleMovesDirY = new ArrayList<Coordinate>();
		for (int i = -1; i < 2; i++) {
			if (i != 0) {
				Coordinate to = new Coordinate(from.getX(), from.getY() + i);
				allPossibleMovesDirY = addIfValid(allPossibleMovesDirY, to);
			}
		}
		return allPossibleMovesDirY;
	}

	private List<Coordinate> possibleAttackMovesDirX(Coordinate from) {
		List<Coordinate> allPossibleMovesDirX = new ArrayList<Coordinate>();
		for (int i = -1; i < 2; i++) {
			if (i != 0) {
				Coordinate to = new Coordinate(from.getX() + i, from.getY());
				allPossibleMovesDirX = addIfValid(allPossibleMovesDirX, to);
			}
		}
		return allPossibleMovesDirX;
	}

}
