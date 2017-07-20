package com.capgemini.chess.algorithms.move;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.piece.Piece;

public abstract class Move {

	private Coordinate from;
	private Coordinate to;
	private Piece movedPiece;
	
	public abstract MoveType getType();

	public abstract boolean isMoveValidWithoutConsideringCheck(Board board);

	public Move(Coordinate from, Coordinate to) {
		this.from = from;
		this.to = to;
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

	protected boolean wasThePieceMoved(Board board) {
		for(Move performedMove : board.getMoveHistory()) {
			if(performedMove.getFrom().equals(this.getFrom())) {
				return true;				
			}
		}
		return false;
	}
	
	
	protected boolean isTheWayFreeToGo(Board board) {
		Piece movedPiece = board.getPieceAt(this.getFrom());
		if (movedPiece.getType() == PieceType.BISHOP) {
			return isTheWayFreeDiagonal(board);
		}
		if (movedPiece.getType() == PieceType.ROOK) {
			if (isTheWayFreeDirX(board) 
				&& isTheWayFreeDirY(board)) {
				return true;
			} else {
				return false;
			}
		}
		if (movedPiece.getType() == PieceType.QUEEN) {
			if (isTheWayFreeDirX(board)
					&& isTheWayFreeDirY(board) 
					&& isTheWayFreeDiagonal(board)) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	private boolean isTheWayFreeDirX(Board board) {
		int start = this.getFrom().getX();
		int stop = this.getTo().getX();
		if(start == stop) {
			return true;
		}
		int absDistance = Math.abs(stop - start);
		int direction = (stop - start) / absDistance;
		for (int i = 1; i < absDistance; i++) {
			Coordinate squareOnTheWay = new Coordinate(this.getFrom().getX() + i * direction, this.getFrom().getY());
			Piece pieceOnTheWay = board.getPieceAt(squareOnTheWay);
			if (pieceOnTheWay != null && pieceOnTheWay.getType() != PieceType.EN_PASSANT_PAWN) {
				return false;
			}
		}
		return true;
	}

	private boolean isTheWayFreeDirY(Board board) {
		int start = this.getFrom().getY();
		int stop = this.getTo().getY();
		if(start == stop) {
			return true;
		}
		int absDistance = Math.abs(stop - start);
		int direction = (stop - start) / absDistance;
		for (int i = 1; i < absDistance; i++) {
			Coordinate squareOnTheWay = new Coordinate(this.getFrom().getX(), this.getFrom().getY() + i * direction);
			Piece pieceOnTheWay = board.getPieceAt(squareOnTheWay);
			if (pieceOnTheWay != null && pieceOnTheWay.getType() != PieceType.EN_PASSANT_PAWN) {
				return false;
			}
		}
		return true;
	}

	private boolean isTheWayFreeDiagonal(Board board) {
		int startX = this.getFrom().getX();
		int stopX = this.getTo().getX();
		int startY = this.getFrom().getY();
		int stopY = this.getTo().getY();
		if(startX == stopX || startY == stopY) {
			return true;
		}
		int absDistanceX = Math.abs(stopX - startX);
		int directionX = (stopX - startX) / absDistanceX;
		int absDistanceY = Math.abs(stopY - startY);
		int directionY = (stopY - startY) / absDistanceY;
		
		for (int i = 1; i < absDistanceX; i++) {
			Coordinate squareOnTheWay = new Coordinate(this.getFrom().getX() + i * directionX, this.getFrom().getY() + i * directionY);
			Piece pieceOnTheWay = board.getPieceAt(squareOnTheWay);
			if (pieceOnTheWay != null && pieceOnTheWay.getType() != PieceType.EN_PASSANT_PAWN) {
				return false;
			}
		}
		return true;
	}

}
