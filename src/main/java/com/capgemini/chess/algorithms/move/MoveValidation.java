package com.capgemini.chess.algorithms.move;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.KingInCheckException;
import com.capgemini.chess.algorithms.piece.Bishop;
import com.capgemini.chess.algorithms.piece.NoPiece;
import com.capgemini.chess.algorithms.piece.Piece;
import com.capgemini.chess.algorithms.piece.Queen;
import com.capgemini.chess.algorithms.piece.Rook;

public class MoveValidation {
	
	private Board board = new Board();
	
	public MoveValidation(Board board) {
		this.board = board;
	}
	
	public Move validateMove(Coordinate from, Coordinate to) throws InvalidMoveException, KingInCheckException {
		Piece movedPiece = this.board.getPieceAt(from);
		List<Coordinate> possibleAttacks = movedPiece.possibleAttackMoves(from);
		if(checkIfAttPossForPiece(to, possibleAttacks) && checkIfAttPossForToSquare(to)) {
			if(checkIfTheWayIsFree(from, to)) {
				return new AttackMove(from, to, movedPiece);				
			}
		}
		List<Coordinate> possibleCaptures = movedPiece.possibleCaptureMoves(from);
		if(checkIfCaptPossForPiece(to, possibleCaptures) && checkIfCaptPossForToSquare(from, to)) {
			if(checkIfTheWayIsFree(from, to)) {
				return new CaptureMove(from, to, movedPiece);
			}
		}
		
		// TODO please add implementation here
		return null;
	}

	private boolean checkIfAttPossForPiece(Coordinate to, List<Coordinate> possibleAttacks) {
		for(Coordinate coord : possibleAttacks) {
			if(coord == to) {
				return true;
			} 
		}
		return false;
	}
	
	private boolean checkIfAttPossForToSquare(Coordinate to) {
		Piece pieceOnToSquare = this.board.getPieceAt(to);
		if(pieceOnToSquare.getClass() == NoPiece.class) {
			return true; 
		}
		return false;
	}
	
	private boolean checkIfCaptPossForPiece(Coordinate to, List<Coordinate> possibleCaptures) {
		for(Coordinate coord : possibleCaptures) {
			if(coord == to) {
				return true;
			} 
		}
		return false;
	}
	
	private boolean checkIfCaptPossForToSquare(Coordinate from, Coordinate to) {
		Piece movedPiece = this.board.getPieceAt(from);
		Piece pieceOnToSquare = this.board.getPieceAt(to);
		if(movedPiece.getColor() != pieceOnToSquare.getColor()) {
			return true; 
		}
		return false;
	}
	
	private boolean checkIfTheWayIsFree(Coordinate from, Coordinate to) {
		Piece movedPiece = this.board.getPieceAt(from);
		if (movedPiece.getClass() == Bishop.class) {
			return checkIfTheWayIsFreeDiagonal(from, to);
		}
		if (movedPiece.getClass() == Rook.class) {
			if(checkIfTheWayIsFreeDirX(from, to)) {
				return checkIfTheWayIsFreeDirY(from, to);				
			}
		}
		if (movedPiece.getClass() == Queen.class) {
			if(checkIfTheWayIsFreeDirX(from, to) && checkIfTheWayIsFreeDirY(from, to)) {
				return checkIfTheWayIsFreeDiagonal(from, to);
			}
		}
		return false;
	}
	
	private boolean checkIfTheWayIsFreeDirX(Coordinate from, Coordinate to) {
		int distX = to.getX() - from.getX();
		for(int i = distX / Math.abs(distX); i < distX; i++) {
			Coordinate squareOnTheWay = new Coordinate(from.getX() + i, from.getY());
			Piece pieceOnTheWay = this.board.getPieceAt(squareOnTheWay);
			if(pieceOnTheWay.getClass() != NoPiece.class) {
				return false;
			}				
		}
		return true;
	}

	private boolean checkIfTheWayIsFreeDirY(Coordinate from, Coordinate to) {
		int distY = to.getY() - from.getY();
		for(int i = distY / Math.abs(distY); i < distY; i++) {
			Coordinate squareOnTheWay = new Coordinate(from.getX(), from.getY() + i);
			Piece pieceOnTheWay = this.board.getPieceAt(squareOnTheWay);
			if(pieceOnTheWay.getClass() != NoPiece.class) {
				return false;
			}				
		}
		return true;
	}
	
	private boolean checkIfTheWayIsFreeDiagonal(Coordinate from, Coordinate to) {
		int distX = to.getX() - from.getX();
		int distY = to.getY() - from.getY();
		for(int i = distX / Math.abs(distX); i < distX; i++) {
			Coordinate squareOnTheWay = new Coordinate(from.getX() + i, from.getY() + (distY / Math.abs(distY)));
			Piece pieceOnTheWay = this.board.getPieceAt(squareOnTheWay);
			if(pieceOnTheWay.getClass() != NoPiece.class) {
				return false;
			}				
		}
		return true;
	}
		
}
