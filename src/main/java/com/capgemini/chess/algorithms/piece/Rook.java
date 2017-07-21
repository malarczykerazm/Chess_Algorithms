package com.capgemini.chess.algorithms.piece;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;

/**
 * an object representation of a rook chess piece
 * @author EMALARCZ
 *
 */
public class Rook extends Piece {

	private final PieceType type = PieceType.ROOK;

	public Rook(Color color) {
		super(color);
	}

	/**
	 * checks where the piece could possible go in the next turn of the game 
	 * in case of the attack type of move
	 */
	@Override
	public List<Coordinate> possibleAttackMoves(Coordinate from) {
		List<Coordinate> allPossibleMoves = new ArrayList<Coordinate>();
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
	 * checks if the way of the piece is free to go
	 */
	@Override
	public boolean isTheWayFreeToGo(Board board, Coordinate from, Coordinate to) {
		return (this.isTheWayFreeDirX(board, from, to) && this.isTheWayFreeDirY(board, from, to));
	}

	private List<Coordinate> possibleAttackMovesDirY(Coordinate from) {
		List<Coordinate> allPossibleMovesDirY = new ArrayList<Coordinate>();
		for (int i = (-1) * (Board.SIZE - 1); i < Board.SIZE; i++) {
			if (i != 0) {
				Coordinate to = new Coordinate(from.getX(), from.getY() + i);
				allPossibleMovesDirY = addIfValid(allPossibleMovesDirY, to);
				;
			}
		}
		return allPossibleMovesDirY;
	}

	private List<Coordinate> possibleAttackMovesDirX(Coordinate from) {
		List<Coordinate> allPossibleMovesDirX = new ArrayList<Coordinate>();
		for (int i = (-1) * (Board.SIZE - 1); i < Board.SIZE; i++) {
			if (i != 0) {
				Coordinate to = new Coordinate(from.getX() + i, from.getY());
				allPossibleMovesDirX = addIfValid(allPossibleMovesDirX, to);
			}
		}
		return allPossibleMovesDirX;
	}

}
