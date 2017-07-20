package com.capgemini.chess.algorithms.move;

import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.piece.King;

public class CastlingMove extends Move {
	
	private final MoveType type = MoveType.CASTLING;

	public CastlingMove(Coordinate from, Coordinate to) {
		super(from, to);
	}

	@Override
	public MoveType getType() {
		return type;
	}
	
	@Override
	public boolean isMoveValidWithoutConsideringCheck(Board board) {

		if(!(isThisTheFirstMoveOfTheKing(board))) {
			return false;
		}
		
		List<Coordinate> possibleMoves = ((King) board.getPieceAt(this.getFrom())).possibleCastlingMoves(this.getFrom());
		
		int i = 0;
		for (Coordinate square : possibleMoves) {
			if(square.equals(this.getTo())) {
				i++;
			}
		}
		
		if(i == 0) {
			return false;
		}
		
		if(!(isTheWayFreeForCastling(board))) {
			return false;
		}
		
		if(this.getTo().getX() >= this.getFrom().getX()) {
			return isThisTheFirstMoveOfTheRook(board);
		} else {
			if(board.getPieceAt(new Coordinate(1, this.getFrom().getY())) != null) {
				return false;
			}
			return isThisTheFirstMoveOfTheRook(board);
		}
	}
	
	private boolean isThisTheFirstMoveOfTheKing(Board board) {
		if(board.getPieceAt(this.getFrom()).getType() != PieceType.KING) {
			return false;
		}
		if(!(this.getFrom().equals(new Coordinate(4, 0)) || this.getFrom().equals(new Coordinate(4, 7)))) {
			return false;
		}
		return !(wasThePieceMoved(board));
	}
	
	private boolean isThisTheFirstMoveOfTheRook(Board board) {
		if(board.getPieceAt(this.getFrom()) == null || board.getPieceAt(this.getFrom()).getType() != PieceType.ROOK) {
			return false;
		}
		return !(wasThePieceMoved(board));
	}
	
	private boolean isTheWayFreeForCastling(Board board) {
		int start = this.getFrom().getX();
		int stop = this.getTo().getX();
		if(start == stop) {
			return true;
		}
		int absDistance = Math.abs(stop - start);
		int direction = (stop - start) / absDistance;
		for (int i = 1; i <= absDistance; i++) {
			Coordinate squareOnTheWay = new Coordinate(this.getFrom().getX() + i * direction, this.getFrom().getY());
			if (board.getPieceAt(squareOnTheWay) != null) {
				return false;
			}
		}
		return true;
		
	}
	
}
