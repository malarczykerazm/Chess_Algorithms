package com.capgemini.chess.algorithms.piece;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;

/**
 * an object representation of an artificial chess piece called en passant pawn
 * (it allows the en passant type of move)
 * @author EMALARCZ
 *
 */
public class EnPassantPawn extends Piece {

	private final PieceType type = PieceType.EN_PASSANT_PAWN;

	public EnPassantPawn(Color color) {
		super(color);
	}

	/**
	 * checks where the piece could possible go in the next turn of the game 
	 * (no possible moves for an en passant artificial piece)
	 */
	@Override
	public List<Coordinate> possibleAttackMoves(Coordinate from) {
		return new ArrayList<Coordinate>();
	}

	/**
	 * checks where the piece could possible go in the next turn of the game 
	 * (no possible moves for an en passant artificial piece)
	 */
	@Override
	public List<Coordinate> possibleCaptureMoves(Coordinate from) {
		return new ArrayList<Coordinate>();
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
		return true;
	}

}
