package com.capgemini.chess.algorithms.implementation.exceptions;

public class NoKingException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public NoKingException() {
		super("There is no king on the board!");			
	}
	
}
