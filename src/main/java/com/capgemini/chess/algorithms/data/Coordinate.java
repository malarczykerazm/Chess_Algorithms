package com.capgemini.chess.algorithms.data;

import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;

/**
 * Coordinates of Chess Piece.
 * 
 * @author Michal Bejm
 *
 */
public class Coordinate {

	private int x;
	private int y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
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
		Coordinate other = (Coordinate) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	/**
	 * validates the coordinates with accordance to the board size
	 * @throws InvalidMoveException in case of invalid coordinate
	 */
	public void coordinateValidator() throws InvalidMoveException {
		if (!(this.isValid())) {
			throw new InvalidMoveException("The coordinates of the move are invalid!");
		}
	}

	/**
	 * checks it the coordinate fits the bounds of the board
	 * @return a boolean (true for a coordinate in the board and false otherwise)
	 */
	public boolean isValid() {
		if (this.getX() <= Board.SIZE - 1 && this.getY() <= Board.SIZE - 1 && this.getX() >= 0 && this.getY() >= 0) {
			return true;
		}
		return false;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
