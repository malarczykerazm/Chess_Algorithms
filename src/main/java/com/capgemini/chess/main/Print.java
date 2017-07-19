package com.capgemini.chess.main;

import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.piece.Piece;

public class Print {

	public static void printTheBoard() {
		
	}

	public static void printSinglePiecesMoves(Piece piece, Coordinate from) {
		String[][] board = setSinglePieceOnBoard(piece, from);
		
		System.out.print(" |");
		for(int i = 0; i < board.length; i++) {
			System.out.print(i + "|");			
		}
		System.out.println();
		
		for(int i = 0; i < board.length; i++) {
			System.out.print(i + "|");
			for(int j = 0; j < board[i].length; j++) {
				if(board[i][j] == null) {
					System.out.print(" ");
				} else {					
					System.out.print(board[i][j]);
				}
				System.out.print("|");					
			}
			System.out.println();
		}
	}
	
		private static String[][] setSinglePieceOnBoard(Piece piece, Coordinate from) {
			List<Coordinate> list = piece.possibleCaptureMoves(from);
			String[][] board = new String[Board.SIZE][Board.SIZE];
			
			for(Coordinate coord : list) {
				System.out.print(coord.getX() + "_");
				System.out.print(coord.getY());
				System.out.print(", ");
			}
			System.out.println();
			System.out.println();
			
			for(int i = 0; i < 8; i++) {
				for(int j = 0; j < 8; j++) {
					for(Coordinate coord : list) {
						if(coord.getX() == i && coord.getY() == j) {
							board[i][j] = "#";
						} else if(i != from.getX() || j != from.getY()){
						}
					}
					if(i == from.getX() && j == from.getY()) {
						board[i][j] = "+";													
					}
				}
			}
			return board;
		}
		
}
