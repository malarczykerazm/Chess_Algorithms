package com.capgemini.chess.algorithms.move;

import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.piece.King;

/**
 * an object representation of the castling type move, allows to create the move
 * and validate it without considering causing the KingInCheck Exception
 * @author EMALARCZ
 *
 */
public class CastlingMove extends Move {

	private final MoveType type = MoveType.CASTLING;

	public CastlingMove(Coordinate from, Coordinate to) {
		super(from, to);
	}

	@Override
	public MoveType getType() {
		return type;
	}

	/**
	 * it validates the move without considering the check of the king possibly caused by the move
	 */
	@Override
	public Move validateMoveWithoutConsideringCheck(Board board) throws InvalidMoveException {

		if (!(isThisTheFirstMoveOfTheKing(board, this.getFrom()))) {
			throw new InvalidMoveException();
		}

		List<Coordinate> possibleMoves = ((King) board.getPieceAt(this.getFrom()))
				.possibleCastlingMoves(this.getFrom());

		int i = 0;
		for (Coordinate square : possibleMoves) {
			if (square.equals(this.getTo())) {
				i++;
			}
		}

		if (i == 0) {
			throw new InvalidMoveException();
		}

		if (!(isTheWayFreeForCastling(board))) {
			throw new InvalidMoveException();
		}

		if (this.getTo().getX() >= this.getFrom().getX()) {
			if (isThisTheFirstMoveOfTheRook(board, new Coordinate(Board.SIZE - 1, this.getFrom().getY()))) {
				return this;
			} else {
				throw new InvalidMoveException();
			}
		} else {
			if (board.getPieceAt(new Coordinate(1, this.getFrom().getY())) != null) {
				throw new InvalidMoveException();
			}
			if (isThisTheFirstMoveOfTheRook(board, new Coordinate(0, this.getFrom().getY()))) {
				return this;
			} else {
				throw new InvalidMoveException();
			}
		}
	}

	/**
	 * checks if the king was ever moved during the game
	 * (needed to validate castling move)
	 * @param board current situation on the board
	 * @param kingLocation location of the king
	 * @return true if the move is going to be the first move of the king, false otherwise
	 */
	private boolean isThisTheFirstMoveOfTheKing(Board board, Coordinate kingLocation) {
		if (board.getPieceAt(this.getFrom()).getType() != PieceType.KING) {
			return false;
		}
		if (!(this.getFrom().equals(new Coordinate(4, 0)) || this.getFrom().equals(new Coordinate(4, 7)))) {
			return false;
		}
		return !(wasThePieceMoved(board, kingLocation));
	}

	/**
	 * checks if the rook was ever moved during the game
	 * (needed to validate castling move)
	 * @param board current situation on the board
	 * @param rookLocation location of the king
	 * @return true if the move is going to be the first move of the rook, false otherwise
	 */
	private boolean isThisTheFirstMoveOfTheRook(Board board, Coordinate rookLocation) {
		if (board.getPieceAt(rookLocation) == null || board.getPieceAt(rookLocation).getType() != PieceType.ROOK) {
			return false;
		}
		return !(wasThePieceMoved(board, rookLocation));
	}

	/**
	 * checks if there are no pieces that could make the king and the rook stop from castling
	 * @param board current situation on the board
	 * @return true in case of the way is free to go, false otherwise
	 */
	private boolean isTheWayFreeForCastling(Board board) {
		int start = this.getFrom().getX();
		int stop = this.getTo().getX();
		if (start == stop) {
			return true;
		}
		int absDistance = Math.abs(stop - start);
		int direction = (stop - start) / absDistance;
		for (int i = 1; i <= absDistance; i++) {
			Coordinate squareOnTheWay = new Coordinate(this.getFrom().getX() + i * direction, this.getFrom().getY());
			if (board.getPieceAt(squareOnTheWay) != null) {
				return false;
			}
		}
		return true;

	}

}
