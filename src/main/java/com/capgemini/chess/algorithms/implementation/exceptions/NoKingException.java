package com.capgemini.chess.algorithms.implementation.exceptions;

/**
 * an exception that shows there was no king found on the board (a violation of chess rules)
 * @author EMALARCZ
 *
 */
public class NoKingException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * an exception with the initial message
	 */
	public NoKingException() {
		super("There is no king on the board!");
	}

}
