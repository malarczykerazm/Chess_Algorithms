package com.capgemini.chess.algorithms.piece;

import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidColorException;

/**
 * an object representation of a chess piece
 * @author EMALARCZ
 *
 */
public abstract class Piece {

	private final Color color;

	public Piece(Color color) {
		this.color = color;
	}

	public abstract PieceType getType();

	/**
	 * checks where the piece could possible go in the next turn of the game 
	 * in case of the attack type of move without considering the current state of the board
	 * @param from start point
	 * @return list of possible end points
	 * @throws InvalidColorException in case of the piece has no valid colour
	 */
	public abstract List<Coordinate> possibleAttackMoves(Coordinate from) throws InvalidColorException;

	/**
	 * checks where the piece could possible go in the next turn of the game 
	 * in case of the capture type of move without considering the current state of the board
	 * @param from
	 * @return
	 * @throws InvalidColorException
	 */
	public List<Coordinate> possibleCaptureMoves(Coordinate from) throws InvalidColorException {
		return possibleAttackMoves(from);
	}

	/**
	 * checks if the way of the piece is free to go with accordance to the piece type,
	 * start and end point and the current situation on the board without considering
	 * the check of the king possibly caused by the move
	 * @param board current situation on the board
	 * @param from start coordinate (start point)
	 * @param to end coordinate (end point)
	 * @return true if the way is free to go and false otherwise
	 */
	public abstract boolean isTheWayFreeToGo(Board board, Coordinate from, Coordinate to);

	public Color getColor() {
		return this.color;
	}

	/**
	 * adds the coordinate to the parameter list of coordinates if the parameter coordinate is valid
	 * due to the size of the considered board
	 * @param coordinates list of coordinates
	 * @param square considered coordinate
	 * @return adjusted list of coordianates
	 */
	public List<Coordinate> addIfValid(List<Coordinate> coordinates, Coordinate square) {
		if (square.isValid()) {
			coordinates.add(square);
		}
		return coordinates;
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
		Piece other = (Piece) obj;
		if (color != other.color)
			return false;
		return true;
	}

	protected boolean isTheWayFreeDirX(Board board, Coordinate from, Coordinate to) {
		int start = from.getX();
		int stop = to.getX();
		if (start == stop) {
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
		if (start == stop) {
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
		if (startX == stopX || startY == stopY) {
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