package com.capgemini.chess.algorithms.move;

import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidColorException;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.piece.Piece;

public class CaptureMove extends Move {
	
	private final MoveType type = MoveType.CAPTURE;

	public CaptureMove(Coordinate from, Coordinate to) {
		super(from, to);
	}
	
	@Override
	public MoveType getType() {
		return type;
	}
	
	@Override
	public Move validateMoveWithoutConsideringCheck(Board board) throws InvalidMoveException, InvalidColorException {
		Piece movedPiece = board.getPieceAt(this.getFrom());
		List<Coordinate> possibleCaptures = movedPiece.possibleCaptureMoves(this.getFrom());
		
		for (Coordinate square : possibleCaptures) {
			if(square.equals(this.getTo())) {
				if (isMovePossibleForDestination(board) && movedPiece.isTheWayFreeToGo(board, this.getFrom(), this.getTo())) {
					return this;
				}
			}
		}
		throw new InvalidMoveException();
	}
	
	private boolean isMovePossibleForDestination(Board board) {
		Piece movedPiece = board.getPieceAt(this.getFrom());
		Piece pieceOnDestinationSquare = board.getPieceAt(this.getTo());
		if (pieceOnDestinationSquare != null && movedPiece.getColor() != pieceOnDestinationSquare.getColor()) {
			return (pieceOnDestinationSquare.getType() != PieceType.EN_PASSANT_PAWN);
		}
		return false;
	}
	
}
