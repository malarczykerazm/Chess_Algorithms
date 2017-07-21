package com.capgemini.chess.algorithms.move;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidColorException;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.piece.Piece;

/**
 * an object representation of a move of any type, allows to create the move
 * and validate it without considering causing the KingInCheck Exception
 * @author EMALARCZ
 *
 */
public abstract class Move {

	private Coordinate from;
	private Coordinate to;
	private Piece movedPiece;

	public abstract MoveType getType();

	/**
	 * it validates the move without considering the check of the king possibly caused by the move
	 * @param board current situation on the board
	 * @return the validated move
	 * @throws InvalidMoveException thrown in case of the move is not valid
	 * @throws InvalidColorException thrown in case of a wrong colour of the moved piece
	 */
	public abstract Move validateMoveWithoutConsideringCheck(Board board)
			throws InvalidMoveException, InvalidColorException;

	public Move(Coordinate from, Coordinate to) {
		this.from = from;
		this.to = to;
	}

	/**
	 * it generates the proper type of move according to the situation on the board,
	 * so the move can be validated afterwards
	 * @param board current situation on the board
	 * @param from start point of the move
	 * @param to end point of the move
	 * @return
	 */
	public static Move generateMove(Board board, Coordinate from, Coordinate to) {
		if (board.getPieceAt(to) == null) {
			if (board.getPieceAt(from).getType() == PieceType.KING && Math.abs(to.getX() - from.getX()) == 2) {
				return new CastlingMove(from, to);
			} else {
				return new AttackMove(from, to);
			}
		} else if (board.getPieceAt(to).getType() == PieceType.EN_PASSANT_PAWN) {
			return new EnPassantMove(from, to);
		} else {
			return new CaptureMove(from, to);
		}
	}

	/**
	 * overrides the equals method of Object class to make some tests possible to run
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (movedPiece == null) {
			if (other.movedPiece != null)
				return false;
		} else if (!movedPiece.equals(other.movedPiece))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}

	public Coordinate getFrom() {
		return this.from;
	}

	public void setFrom(Coordinate from) {
		this.from = from;
	}

	public Coordinate getTo() {
		return to;
	}

	public void setTo(Coordinate to) {
		this.to = to;
	}

	public Piece getMovedPiece() {
		return movedPiece;
	}

	public void setMovedPiece(Piece movedPiece) {
		this.movedPiece = movedPiece;
	}

	/**
	 * checks if a certain piece was ever moved during the game
	 * (needed to validate castling move)
	 * @param board current situation on the board
	 * @param pieceLocation the location of considered piece
	 * @return true in case of the piece was moved before, false otherwise
	 */
	protected boolean wasThePieceMoved(Board board, Coordinate pieceLocation) {
		for (Move performedMove : board.getMoveHistory()) {
			if (performedMove.getFrom().equals(pieceLocation)) {
				return true;
			}
		}
		return false;
	}

}
