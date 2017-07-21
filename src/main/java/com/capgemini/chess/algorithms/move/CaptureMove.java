package com.capgemini.chess.algorithms.move;

import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidColorException;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.piece.Piece;

/**
 * an object representation of the capture type move, allows to create the move
 * and validate it without considering causing the KingInCheck Exception
 * @author EMALARCZ
 *
 */
public class CaptureMove extends Move {

	private final MoveType type = MoveType.CAPTURE;

	public CaptureMove(Coordinate from, Coordinate to) {
		super(from, to);
	}

	@Override
	public MoveType getType() {
		return type;
	}

	/**
	 * it validates the move without considering the check of the king possibly caused by the move
	 */
	@Override
	public Move validateMoveWithoutConsideringCheck(Board board) throws InvalidMoveException, InvalidColorException {
		Piece movedPiece = board.getPieceAt(this.getFrom());
		List<Coordinate> possibleCaptures = movedPiece.possibleCaptureMoves(this.getFrom());

		for (Coordinate square : possibleCaptures) {
			if (square.equals(this.getTo())) {
				if (isMovePossibleForDestination(board)
						&& movedPiece.isTheWayFreeToGo(board, this.getFrom(), this.getTo())) {
					return this;
				}
			}
		}
		throw new InvalidMoveException();
	}

	/**
	 * it checks if the move is possible according to what is on the end point of the move
	 * @param board current situation on the board
	 * @return true in case of the move is possible, false otherwise
	 */
	private boolean isMovePossibleForDestination(Board board) {
		Piece movedPiece = board.getPieceAt(this.getFrom());
		Piece pieceOnDestinationSquare = board.getPieceAt(this.getTo());
		if (pieceOnDestinationSquare != null && movedPiece.getColor() != pieceOnDestinationSquare.getColor()) {
			return (pieceOnDestinationSquare.getType() != PieceType.EN_PASSANT_PAWN);
		}
		return false;
	}

}
