package com.capgemini.chess.algorithms.implementation.exceptions;

public class InvalidColorException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public InvalidColorException() {
		super("The color of the piece is invalid!");
	}
	
}
