package com.capgemini.chess.algorithms.piece;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidColorException;

/**
 * an object representation of a pawn chess piece after its first move
 * @author EMALARCZ
 *
 */
public class PawnAfterFirstMove extends Pawn {

	public PawnAfterFirstMove(Color color) {
		super(color);
	}

	/**
	 * checks where the piece could possible go in the next turn of the game 
	 * in case of the attack type of move
	 */
	@Override
	public List<Coordinate> possibleAttackMoves(Coordinate from) throws InvalidColorException {
		if (this.getColor() == Color.WHITE) {
			return possibleAttackMovesWhite(from);
		} else if (this.getColor() == Color.BLACK) {
			return possibleAttackMovesBlack(from);
		}
		throw new InvalidColorException("The Pawn has an invalid color!");
	}
	
	/**
	 * checks if the way of the piece is free to go (always free to go for a pawn after its first move)
	 */
	@Override
	public boolean isTheWayFreeToGo(Board board, Coordinate from, Coordinate to) {
		return this.isTheWayFreeDirY(board, from, to);
	}

	private List<Coordinate> possibleAttackMovesWhite(Coordinate from) {
		List<Coordinate> allPossibleAttackMovesWhite = new ArrayList<Coordinate>();
		Coordinate to = new Coordinate(from.getX(), from.getY() + 1);
		allPossibleAttackMovesWhite = addIfValid(allPossibleAttackMovesWhite, to);
		return allPossibleAttackMovesWhite;
	}

	private List<Coordinate> possibleAttackMovesBlack(Coordinate from) {
		List<Coordinate> allPossibleAttackMovesBlack = new ArrayList<Coordinate>();
		Coordinate to = new Coordinate(from.getX(), from.getY() - 1);
		allPossibleAttackMovesBlack = addIfValid(allPossibleAttackMovesBlack, to);
		return allPossibleAttackMovesBlack;
	}

}