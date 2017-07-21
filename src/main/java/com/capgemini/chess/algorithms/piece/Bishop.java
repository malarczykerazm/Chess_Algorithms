package com.capgemini.chess.algorithms.piece;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;

/**
 * an object representation of a bishop chess piece
 * @author EMALARCZ
 *
 */
public class Bishop extends Piece {

	private final PieceType type = PieceType.BISHOP;

	public Bishop(Color color) {
		super(color);
	}

	/**
	 * checks where the piece could possible go in the next turn of the game 
	 * in case of the attack type of move
	 */
	@Override
	public List<Coordinate> possibleAttackMoves(Coordinate from) {
		List<Coordinate> allPossibleMoves = new ArrayList<Coordinate>();
		for (int i = (-1) * (Board.SIZE - 1); i < Board.SIZE; i++) {
			for (int j = -1; j < 2; j++) {
				if (i != 0 && j != 0) {
					Coordinate to = new Coordinate(from.getX() + i, from.getY() + i * j);
					allPossibleMoves = addIfValid(allPossibleMoves, to);
				}
			}
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
		return this.isTheWayFreeDiagonal(board, from, to);
	}

}
