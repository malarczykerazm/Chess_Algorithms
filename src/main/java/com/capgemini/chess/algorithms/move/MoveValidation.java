package com.capgemini.chess.algorithms.move;

import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.piece.Piece;

public class MoveValidation {

	private Board board = new Board();

	public MoveValidation(Board board) {
		this.board = board;
	}
	
	public boolean isAttackValidNotConsideringCheck(Coordinate from, Coordinate to) {
		for(Coordinate square : findAllValidatedAttacks(from)) {
			if(square == to) {
				return true;
			}
		}
		return false;
	}
	
	
	public boolean isCaptureValidNotConsideringCheck(Coordinate from, Coordinate to) {
		for(Coordinate square : findAllValidatedCaptures(from)) {
			if(square == to) {
				return true;
			}
		}
		return false;
	}

	private List<Coordinate> findAllValidatedAttacks(Coordinate from) {
		Piece movedPiece = this.board.getPieceAt(from);
		List<Coordinate> possibleAttacks = movedPiece.possibleAttackMoves(from);
		for (Coordinate to : possibleAttacks) {
			if (checkIfAttPossForPiece(to, possibleAttacks) && checkIfAttPossForToSquare(to)) {
				if (checkIfTheWayIsFree(from, to)) {
				} else {
					possibleAttacks.remove(to);
				}
			}
		}
		return possibleAttacks;
	}
			
	private List<Coordinate> findAllValidatedCaptures(Coordinate from) {
		Piece movedPiece = this.board.getPieceAt(from);
		List<Coordinate> possibleCaptures = movedPiece.possibleCaptureMoves(from);
		for (Coordinate to : possibleCaptures) {
			if (checkIfCaptPossForPiece(to, possibleCaptures) && checkIfCaptPossForToSquare(from, to)) {
				if (checkIfTheWayIsFree(from, to)) {
				} else {
					possibleCaptures.remove(to);
				}
			}
		}
		return possibleCaptures;
	}

	private boolean checkIfAttPossForPiece(Coordinate to, List<Coordinate> possibleAttacks) {
		for (Coordinate coord : possibleAttacks) {
			if (coord == to) {
				return true;
			}
		}
		return false;
	}

	private boolean checkIfAttPossForToSquare(Coordinate to) {
		Piece pieceOnToSquare = this.board.getPieceAt(to);
		if (pieceOnToSquare == null || pieceOnToSquare.getType() == PieceType.EN_PASSANT_PAWN) {
			return true;
		}
		return false;
	}

	private boolean checkIfCaptPossForPiece(Coordinate to, List<Coordinate> possibleCaptures) {
		for (Coordinate coord : possibleCaptures) {
			if (coord == to) {
				return true;
			}
		}
		return false;
	}

	private boolean checkIfCaptPossForToSquare(Coordinate from, Coordinate to) {
		Piece movedPiece = this.board.getPieceAt(from);
		Piece pieceOnToSquare = this.board.getPieceAt(to);
		if (movedPiece.getColor() != pieceOnToSquare.getColor()) {
			return true;
		}
		return false;
	}

	private boolean checkIfTheWayIsFree(Coordinate from, Coordinate to) {
		Piece movedPiece = this.board.getPieceAt(from);
		if (movedPiece.getType() == PieceType.BISHOP) {
			return checkIfTheWayIsFreeDiagonal(from, to);
		}
		if (movedPiece.getType() == PieceType.ROOK) {
			if (checkIfTheWayIsFreeDirX(from, to)) {
				return checkIfTheWayIsFreeDirY(from, to);
			}
		}
		if (movedPiece.getType() == PieceType.QUEEN) {
			if (checkIfTheWayIsFreeDirX(from, to) && checkIfTheWayIsFreeDirY(from, to)) {
				return checkIfTheWayIsFreeDiagonal(from, to);
			}
		}
		return false;
	}

	private boolean checkIfTheWayIsFreeDirX(Coordinate from, Coordinate to) {
		int distX = to.getX() - from.getX();
		for (int i = distX / Math.abs(distX); i < distX; i++) {
			Coordinate squareOnTheWay = new Coordinate(from.getX() + i, from.getY());
			Piece pieceOnTheWay = this.board.getPieceAt(squareOnTheWay);
			if (pieceOnTheWay != null && pieceOnTheWay.getType() != PieceType.EN_PASSANT_PAWN) {
				return false;
			}
		}
		return true;
	}

	private boolean checkIfTheWayIsFreeDirY(Coordinate from, Coordinate to) {
		int distY = to.getY() - from.getY();
		for (int i = distY / Math.abs(distY); i < distY; i++) {
			Coordinate squareOnTheWay = new Coordinate(from.getX(), from.getY() + i);
			Piece pieceOnTheWay = this.board.getPieceAt(squareOnTheWay);
			if (pieceOnTheWay != null) {
				return false;
			}
		}
		return true;
	}

	private boolean checkIfTheWayIsFreeDiagonal(Coordinate from, Coordinate to) {
		int distX = to.getX() - from.getX();
		int distY = to.getY() - from.getY();
		for (int i = distX / Math.abs(distX); i < distX; i++) {
			Coordinate squareOnTheWay = new Coordinate(from.getX() + i, from.getY() + (distY / Math.abs(distY)));
			Piece pieceOnTheWay = this.board.getPieceAt(squareOnTheWay);
			if (pieceOnTheWay != null) {
				return false;
			}
		}
		return true;
	}

}
