package com.capgemini.chess.algorithms.move;

import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.piece.Piece;

public class AttackMove extends Move {

	private final MoveType type = MoveType.ATTACK;
	
	public AttackMove(Coordinate from, Coordinate to) {
		super(from, to);
	}
	
	@Override
	public MoveType getType() {
		return type;
	}
	
	@Override
	public boolean isMoveValidWithoutConsideringCheck(Board board) {
		Piece movedPiece = board.getPieceAt(this.getFrom());
		List<Coordinate> possibleAttacks = movedPiece.possibleAttackMoves(this.getFrom());
		
		if(isThisTheFirstMoveOfThePawn(board)) {
			possibleAttacks = movedPiece.possibleAttackMovesForFirstMove(this.getFrom());
		}
		
		for (Coordinate square : possibleAttacks) {
			if(square.equals(this.getTo())) {
				if (isMovePossibleForDestination(board) && isTheWayFreeToGo(board)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isMovePossibleForDestination(Board board) {
		Piece pieceOnDestinationSquare = board.getPieceAt(this.getTo());
		return (pieceOnDestinationSquare == null );
	}
	
	protected boolean isThisTheFirstMoveOfThePawn(Board board) {
		if(board.getPieceAt(this.getFrom()).getType() != PieceType.PAWN) {
			return false;
		}
		if(this.getFrom().getY() == Board.SIZE - 2 || this.getFrom().getY() == 1) {
			return !(wasThePieceMoved(board));				
		}
		return false;
	}
		
	protected boolean wasThePieceMoved(Board board) {
		for(Move performedMove : board.getMoveHistory()) {
			if(performedMove.getFrom().equals(this.getFrom())) {
				return true;				
			}
		}
		return false;
	}
	
}
