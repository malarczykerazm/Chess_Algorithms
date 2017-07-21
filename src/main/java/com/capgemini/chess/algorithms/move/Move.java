package com.capgemini.chess.algorithms.move;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidColorException;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.piece.Piece;

public abstract class Move {

	private Coordinate from;
	private Coordinate to;
	private Piece movedPiece;
	
	public abstract MoveType getType();

	public abstract Move validateMoveWithoutConsideringCheck(Board board) throws InvalidMoveException, InvalidColorException;

	public Move(Coordinate from, Coordinate to) {
		this.from = from;
		this.to = to;
	}
	
	public static Move generateMove(Board board, Coordinate from, Coordinate to) {
		if(board.getPieceAt(to) == null) {
			if(board.getPieceAt(from).getType() == PieceType.KING && Math.abs(to.getX() - from.getX()) == 2) {
				return new CastlingMove(from, to);
			} else {
				return new AttackMove(from, to);
			}
		} else if (board.getPieceAt(to).getType() == PieceType.EN_PASSANT_PAWN){
			return new EnPassantMove(from, to);
		} else {
			return new CaptureMove(from, to);
		}
	}
	
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

	protected boolean wasThePieceMoved(Board board, Coordinate pieceLocation) {
		for(Move performedMove : board.getMoveHistory()) {
			if(performedMove.getFrom().equals(pieceLocation)) {
				return true;				
			}
		}
		return false;
	}

}
