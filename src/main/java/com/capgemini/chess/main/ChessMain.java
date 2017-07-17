package com.capgemini.chess.main;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.piece.*;

public class ChessMain {

	public static void main(String[] args) {
		Piece pawn = new Knight(Color.BLACK);
		Coordinate from = new Coordinate(4, 4);
		Print.printSinglePiecesMoves(pawn, from);
	}
	
}
