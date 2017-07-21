package com.capgemini.chess.algorithms.piece;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;

/**
 * an object representation of a chess piece
 * @author EMALARCZ
 *
 */
public class Knight extends Piece {

	private final PieceType type = PieceType.KNIGHT;

	public Knight(Color color) {
		super(color);
	}

	/**
	 * checks where the piece could possible go in the next turn of the game 
	 * in case of the attack type of move
	 */
	@Override
	public List<Coordinate> possibleAttackMoves(Coordinate from) {
		List<Coordinate> allPossibleMoves = new ArrayList<Coordinate>();
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (i != 0 && j != 0) {
					Coordinate toShort = new Coordinate(from.getX() + 2 * i, from.getY() + 1 * j);
					allPossibleMoves = addIfValid(allPossibleMoves, toShort);
					Coordinate toLong = new Coordinate(from.getX() + 1 * i, from.getY() + 2 * j);
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

	/**
	 * checks if the way of the piece is free to go (always free to go for a knight)
	 */
	@Override
	public boolean isTheWayFreeToGo(Board board, Coordinate from, Coordinate to) {
		return true;
	}

}