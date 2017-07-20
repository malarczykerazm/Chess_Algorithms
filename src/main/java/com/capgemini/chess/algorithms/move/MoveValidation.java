package com.capgemini.chess.algorithms.move;

import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.piece.King;
import com.capgemini.chess.algorithms.piece.Piece;

public class MoveValidation {

	private Board board;

	public MoveValidation(Board board) {
		this.board = board;
	}
	
	public boolean isAttackValidWithoutConsideringCheck(Coordinate from, Coordinate to) {
		Piece movedPiece = this.board.getPieceAt(from);
		List<Coordinate> possibleAttacks = movedPiece.possibleAttackMoves(from);
		
		if(isThisTheFirstMoveOfThePawn(from)) {
			possibleAttacks = movedPiece.possibleAttackMovesForFirstMove(from);
		}
		
		for (Coordinate square : possibleAttacks) {
			if(square.equals(to)) {
				if (isAttackPossibleForDestination(to) && isTheWayFreeToGo(from, to)) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	public boolean isCaptureValidWithoutConsideringCheck(Coordinate from, Coordinate to) {
		Piece movedPiece = this.board.getPieceAt(from);
		List<Coordinate> possibleCaptures = movedPiece.possibleCaptureMoves(from);
		
		for (Coordinate square : possibleCaptures) {
			if(square.equals(to)) {
				if (isCapturePossibleForDestination(from, to) && isTheWayFreeToGo(from, to)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isCastlingValidWithoutConsideringCheck(Coordinate from, Coordinate to) {

		if(!(isThisTheFirstMoveOfTheKing(from))) {
			return false;
		}
		
		List<Coordinate> possibleMoves = ((King) this.board.getPieceAt(from)).possibleCastlingMoves(from);
		
		int i = 0;
		for (Coordinate square : possibleMoves) {
			if(square.equals(to)) {
				i++;
			}
		}
		
		if(i == 0) {
			return false;
		}
		
		if(!(isTheWayFreeForCastling(from, to))) {
			return false;
		}
		
		if(to.getX() >= from.getX()) {
			return isThisTheFirstMoveOfTheRook(new Coordinate((Board.SIZE - 1), from.getY()));
		} else {
			if(this.board.getPieceAt(new Coordinate(1, from.getY())) != null) {
				return false;
			}
			return isThisTheFirstMoveOfTheRook(new Coordinate(0, from.getY()));
		}
	}
	
	public boolean isEnPassantValidWithoutConsideringCheck(Coordinate from, Coordinate to) {
		Piece movedPiece = this.board.getPieceAt(from);
		List<Coordinate> possibleCaptures = movedPiece.possibleCaptureMoves(from);
		
		for (Coordinate square : possibleCaptures) {
			if(square.equals(to)) {
				if(isEnPassantPossibleForDestination(from, to)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isAttackPossibleForDestination(Coordinate to) {
		Piece pieceOnDestinationSquare = this.board.getPieceAt(to);
		return (pieceOnDestinationSquare == null );
	}

	private boolean isCapturePossibleForDestination(Coordinate from, Coordinate to) {
		Piece movedPiece = this.board.getPieceAt(from);
		Piece pieceOnDestinationSquare = this.board.getPieceAt(to);
		if (pieceOnDestinationSquare != null && movedPiece.getColor() != pieceOnDestinationSquare.getColor()) {
			return (pieceOnDestinationSquare.getType() != PieceType.EN_PASSANT_PAWN);
		}
		return false;
	}
	
	private boolean isEnPassantPossibleForDestination(Coordinate from, Coordinate to) {
		Piece movedPiece = this.board.getPieceAt(from);
		Piece pieceOnDestinationSquare = this.board.getPieceAt(to);
		if (pieceOnDestinationSquare != null && movedPiece.getColor() != pieceOnDestinationSquare.getColor()) {
			return (pieceOnDestinationSquare.getType() == PieceType.EN_PASSANT_PAWN);
		}
		return false;
	}

	private boolean isTheWayFreeToGo(Coordinate from, Coordinate to) {
		Piece movedPiece = this.board.getPieceAt(from);
		if (movedPiece.getType() == PieceType.BISHOP) {
			return isTheWayFreeDiagonal(from, to);
		}
		if (movedPiece.getType() == PieceType.ROOK) {
			if (isTheWayFreeDirX(from, to) 
				&& isTheWayFreeDirY(from, to)) {
				return true;
			} else {
				return false;
			}
		}
		if (movedPiece.getType() == PieceType.QUEEN) {
			if (isTheWayFreeDirX(from, to)
					&& isTheWayFreeDirY(from, to) 
					&& isTheWayFreeDiagonal(from, to)) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	private boolean isTheWayFreeDirX(Coordinate from, Coordinate to) {
		int start = from.getX();
		int stop = to.getX();
		if(start == stop) {
			return true;
		}
		int absDistance = Math.abs(stop - start);
		int direction = (stop - start) / absDistance;
		for (int i = 1; i < absDistance; i++) {
			Coordinate squareOnTheWay = new Coordinate(from.getX() + i * direction, from.getY());
			Piece pieceOnTheWay = this.board.getPieceAt(squareOnTheWay);
			if (pieceOnTheWay != null && pieceOnTheWay.getType() != PieceType.EN_PASSANT_PAWN) {
				return false;
			}
		}
		return true;
	}

	private boolean isTheWayFreeDirY(Coordinate from, Coordinate to) {
		int start = from.getY();
		int stop = to.getY();
		if(start == stop) {
			return true;
		}
		int absDistance = Math.abs(stop - start);
		int direction = (stop - start) / absDistance;
		for (int i = 1; i < absDistance; i++) {
			Coordinate squareOnTheWay = new Coordinate(from.getX(), from.getY() + i * direction);
			Piece pieceOnTheWay = this.board.getPieceAt(squareOnTheWay);
			if (pieceOnTheWay != null && pieceOnTheWay.getType() != PieceType.EN_PASSANT_PAWN) {
				return false;
			}
		}
		return true;
	}

	private boolean isTheWayFreeDiagonal(Coordinate from, Coordinate to) {
		int startX = from.getX();
		int stopX = to.getX();
		int startY = from.getY();
		int stopY = to.getY();
		if(startX == stopX || startY == stopY) {
			return true;
		}
		int absDistanceX = Math.abs(stopX - startX);
		int directionX = (stopX - startX) / absDistanceX;
		int absDistanceY = Math.abs(stopY - startY);
		int directionY = (stopY - startY) / absDistanceY;
		
		for (int i = 1; i < absDistanceX; i++) {
			Coordinate squareOnTheWay = new Coordinate(from.getX() + i * directionX, from.getY() + i * directionY);
			Piece pieceOnTheWay = this.board.getPieceAt(squareOnTheWay);
			if (pieceOnTheWay != null && pieceOnTheWay.getType() != PieceType.EN_PASSANT_PAWN) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isThisTheFirstMoveOfThePawn(Coordinate from) {
		if(this.board.getPieceAt(from).getType() != PieceType.PAWN) {
			return false;
		}
		if(from.getY() == Board.SIZE - 2 || from.getY() == 1) {
			return !(wasThePieceMoved(from));				
		}
		return false;
	}
	
	private boolean isThisTheFirstMoveOfTheKing(Coordinate from) {
		if(this.board.getPieceAt(from).getType() != PieceType.KING) {
			return false;
		}
		if(!(from.equals(new Coordinate(4, 0)) || from.equals(new Coordinate(4, 7)))) {
			return false;
		}
		return !(wasThePieceMoved(from));
	}
	
	private boolean isThisTheFirstMoveOfTheRook(Coordinate from) {
		if(this.board.getPieceAt(from) == null || this.board.getPieceAt(from).getType() != PieceType.ROOK) {
			return false;
		}
		return !(wasThePieceMoved(from));
	}
	
	private boolean isTheWayFreeForCastling(Coordinate from, Coordinate to) {
		int start = from.getX();
		int stop = to.getX();
		if(start == stop) {
			return true;
		}
		int absDistance = Math.abs(stop - start);
		int direction = (stop - start) / absDistance;
		for (int i = 1; i <= absDistance; i++) {
			Coordinate squareOnTheWay = new Coordinate(from.getX() + i * direction, from.getY());
			if (this.board.getPieceAt(squareOnTheWay) != null) {
				return false;
			}
		}
		return true;
		
	}
	
	private boolean wasThePieceMoved(Coordinate from) {
		for(Move performedMove : this.board.getMoveHistory()) {
			if(performedMove.getFrom().equals(from)) {
				return true;				
			}
		}
		return false;
	}

}
