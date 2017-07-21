package com.capgemini.chess.algorithms.implementation.exceptions;

/**
 * says there is a piece of an invalid colour on the board
 * @author EMALARCZ
 *
 */
public class InvalidColorException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * shows an initial message about the wrong colour of a piece
	 */
	public InvalidColorException() {
		super("The color of the piece is invalid!");
	}
	
	/**
	 * allows to throw the exception with a custom message
	 * @param message
	 */
	public InvalidColorException(String message) {
		super(message);
	}

}
