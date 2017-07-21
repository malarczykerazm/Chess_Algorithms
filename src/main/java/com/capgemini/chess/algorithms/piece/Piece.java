package com.capgemini.chess.algorithms.piece;

import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidColorException;

public abstract class Piece {
	
	private final Color color;
	
	public Piece(Color color) {
		this.color = color;
	}
	
	public abstract PieceType getType();
	
	public abstract List<Coordinate> possibleAttackMoves(Coordinate from) throws InvalidColorException;

	public List<Coordinate> possibleCaptureMoves(Coordinate from) throws InvalidColorException {
		return possibleAttackMoves(from);
	}
	
	public List<Coordinate> possibleAttackMovesForFirstMove(Coordinate from) throws InvalidColorException {
		return possibleAttackMoves(from);
	}
	
	public abstract boolean isTheWayFreeToGo(Board board, Coordinate from, Coordinate to);

	public Color getColor() {
		return this.color;
	}
	
	public List<Coordinate> addIfValid(List<Coordinate> coordinates, Coordinate square) {
		if(square.isValid()) {
			coordinates.add(square);
		}
		return coordinates;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Piece other = (Piece) obj;
		if (color != other.color)
			return false;
		return true;
	}
	
	protected boolean isTheWayFreeDirX(Board board, Coordinate from, Coordinate to) {
		int start = from.getX();
		int stop = to.getX();
		if(start == stop) {
			return true;
		}
		int absDistance = Math.abs(stop - start);
		int direction = (stop - start) / absDistance;
		for (int i = 1; i < absDistance; i++) {
			Coordinate squareOnTheWay = new Coordinate(from.getX() + i * direction, from.getY());
			Piece pieceOnTheWay = board.getPieceAt(squareOnTheWay);
			if (pieceOnTheWay != null && pieceOnTheWay.getType() != PieceType.EN_PASSANT_PAWN) {
				return false;
			}
		}
		return true;
	}

	protected boolean isTheWayFreeDirY(Board board, Coordinate from, Coordinate to) {
		int start = from.getY();
		int stop = to.getY();
		if(start == stop) {
			return true;
		}
		int absDistance = Math.abs(stop - start);
		int direction = (stop - start) / absDistance;
		for (int i = 1; i < absDistance; i++) {
			Coordinate squareOnTheWay = new Coordinate(from.getX(), from.getY() + i * direction);
			Piece pieceOnTheWay = board.getPieceAt(squareOnTheWay);
			if (pieceOnTheWay != null && pieceOnTheWay.getType() != PieceType.EN_PASSANT_PAWN) {
				return false;
			}
		}
		return true;
	}

	protected boolean isTheWayFreeDiagonal(Board board, Coordinate from, Coordinate to) {
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
			Piece pieceOnTheWay = board.getPieceAt(squareOnTheWay);
			if (pieceOnTheWay != null && pieceOnTheWay.getType() != PieceType.EN_PASSANT_PAWN) {
				return false;
			}
		}
		return true;
	}

}