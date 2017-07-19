package com.capgemini.chess.main;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.BoardManager;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidColorException;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.NoKingException;
import com.capgemini.chess.algorithms.piece.*;

public class ChessMain {

	public static void main(String[] args) throws InvalidMoveException, InvalidColorException, NoKingException {

		Board board = new Board();
		Coordinate from = new Coordinate(5, 5);
		Coordinate to = new Coordinate(3, 3);
		board.setPieceAt(new Queen(Color.WHITE), from);
		//board.setPieceAt(new Knight(Color.BLACK), new Coordinate(3, 3));
		board.setPieceAt(new Pawn(Color.BLACK), to);
		
		BoardManager boardMan = new BoardManager(board);
		
		Print.printSinglePiecesMoves(board.getPieceAt(from), from);
		
		System.out.println(board.getPieceAt(from).getType());
		System.out.println(board.getPieceAt(from).getColor());
		System.out.println(boardMan.validateMove(from, to));
		
		/*
		BoardManager boardMan = new BoardManager();
		Coordinate from = new Coordinate(0, 0);
		Board board = boardMan.getBoard();
		board.setPieceAt(null, new Coordinate(1, 0));
		board.setPieceAt(null, new Coordinate(2, 0));
		board.setPieceAt(new Bishop(Color.WHITE), new Coordinate(3, 0));
		//board.setPieceAt(new Queen(Color.WHITE), new Coordinate(3, 0));
		board.setPieceAt(new Pawn(Color.BLACK), new Coordinate(4, 0));
		Print.printSinglePiecesMoves(board.getPieceAt(from), from);
		
		System.out.println(board.getPieceAt(from).getType());
		System.out.println(board.getPieceAt(from).getColor());
		System.out.println(boardMan.validateMove(from, new Coordinate(4, 0)));
		*/
		
	}
	
}
