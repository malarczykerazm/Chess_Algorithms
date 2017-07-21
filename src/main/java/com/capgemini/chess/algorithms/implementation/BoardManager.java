package com.capgemini.chess.algorithms.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.BoardState;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidColorException;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.KingInCheckException;
import com.capgemini.chess.algorithms.implementation.exceptions.NoKingException;
import com.capgemini.chess.algorithms.move.CaptureMove;
import com.capgemini.chess.algorithms.move.Move;
import com.capgemini.chess.algorithms.piece.EnPassantPawn;
import com.capgemini.chess.algorithms.piece.PawnAfterFirstMove;
import com.capgemini.chess.algorithms.piece.Piece;
import com.capgemini.chess.algorithms.piece.Queen;

/**
 * Class for managing of basic operations on the Chess Board.
 *
 * @author Michal Bejm
 *
 */
public class BoardManager {

	private Board board = new Board();

	public BoardManager() {
		new InitBoard(this.board).initBoard();
	}

	public BoardManager(List<Move> moves) {
		new InitBoard(this.board).initBoard();
		for (Move move : moves) {
			addMove(move);
		}
	}

	public BoardManager(Board board) {
		this.board = board;
	}

	/**
	 * Getter for generated board
	 *
	 * @return board
	 */
	public Board getBoard() {
		return this.board;
	}

	/**
	 * Performs move of the chess piece on the chess board from one field to
	 * another.
	 *
	 * @param from
	 *            coordinates of 'from' field
	 * @param to
	 *            coordinates of 'to' field
	 * @return move object which includes moved piece and move type
	 * @throws InvalidMoveException
	 *             in case move is not valid
	 */
	public Move performMove(Coordinate from, Coordinate to)
			throws InvalidMoveException, InvalidColorException, NoKingException {

		Move move = validateMove(from, to);
		addMove(move);

		return move;
	}

	/**
	 * Calculates state of the chess board.
	 *
	 * @return state of the chess board
	 */
	public BoardState updateBoardState() throws InvalidColorException, NoKingException {

		Color nextMoveColor = calculateNextMoveColor();

		boolean isKingInCheck = isKingInCheck(nextMoveColor);
		boolean isAnyMoveValid = isAnyMoveValid(nextMoveColor);

		BoardState boardState;
		if (isKingInCheck) {
			if (isAnyMoveValid) {
				boardState = BoardState.CHECK;
			} else {
				boardState = BoardState.CHECK_MATE;
			}
		} else {
			if (isAnyMoveValid) {
				boardState = BoardState.REGULAR;
			} else {
				boardState = BoardState.STALE_MATE;
			}
		}
		this.board.setState(boardState);
		return boardState;
	}

	/**
	 * Checks threefold repetition rule (one of the conditions to end the chess
	 * game with a draw).
	 *
	 * @return true if current state repeated at list two times, false otherwise
	 */
	public boolean checkThreefoldRepetitionRule() {

		// there is no need to check moves that where before last capture/en
		// passant/castling
		int lastNonAttackMoveIndex = findLastNonAttackMoveIndex();
		List<Move> omittedMoves = this.board.getMoveHistory().subList(0, lastNonAttackMoveIndex);
		BoardManager simulatedBoardManager = new BoardManager(omittedMoves);

		int counter = 0;
		for (int i = lastNonAttackMoveIndex; i < this.board.getMoveHistory().size(); i++) {
			Move moveToAdd = this.board.getMoveHistory().get(i);
			simulatedBoardManager.addMove(moveToAdd);
			boolean areBoardsEqual = Arrays.deepEquals(this.board.getPieces(),
					simulatedBoardManager.getBoard().getPieces());
			if (areBoardsEqual) {
				counter++;
			}
		}

		return counter >= 2;
	}

	/**
	 * Checks 50-move rule (one of the conditions to end the chess game with a
	 * draw).
	 *
	 * @return true if no pawn was moved or not capture was performed during
	 *         last 50 moves, false otherwise
	 */
	public boolean checkFiftyMoveRule() {

		// for this purpose a "move" consists of a player completing his turn
		// followed by his opponent completing his turn
		if (this.board.getMoveHistory().size() < 100) {
			return false;
		}

		for (int i = this.board.getMoveHistory().size() - 1; i >= this.board.getMoveHistory().size() - 100; i--) {
			Move currentMove = this.board.getMoveHistory().get(i);
			PieceType currentPieceType = currentMove.getMovedPiece().getType();
			if (currentMove.getType() != MoveType.ATTACK || currentPieceType == PieceType.PAWN) {
				return false;
			}
		}

		return true;
	}

	// PRIVATE

	private void addMove(Move move) {

		addRegularMove(move);

		if (move.getType() == MoveType.CASTLING) {
			addCastling(move);
		} else if (move.getType() == MoveType.EN_PASSANT) {
			addEnPassant(move);
		}

		this.board.getMoveHistory().add(move);

		if (isThereAnEnPassantPawnAnywhere()) {
			getRidOfTheEnPassantPawn();
		}

		setNewEnPassantPawnIfNeeded();

		changeFirstMovePawnIntoRegularPawnIfNeeded();

	}

	private void addRegularMove(Move move) {
		Piece movedPiece = this.board.getPieceAt(move.getFrom());
		this.board.setPieceAt(null, move.getFrom());
		this.board.setPieceAt(movedPiece, move.getTo());

		performPromotion(move, movedPiece);
	}

	private void performPromotion(Move move, Piece movedPiece) {
		if (movedPiece.getType() == PieceType.PAWN
				&& ((move.getTo().getY() == (Board.SIZE - 1)) || (move.getTo().getY() == 0))) {
			this.board.setPieceAt(new Queen(movedPiece.getColor()), move.getTo());
		}
	}

	private void addCastling(Move move) {
		if (move.getFrom().getX() > move.getTo().getX()) {
			Piece rook = this.board.getPieceAt(new Coordinate(0, move.getFrom().getY()));
			this.board.setPieceAt(null, new Coordinate(0, move.getFrom().getY()));
			this.board.setPieceAt(rook, new Coordinate(move.getTo().getX() + 1, move.getTo().getY()));
		} else {
			Piece rook = this.board.getPieceAt(new Coordinate(Board.SIZE - 1, move.getFrom().getY()));
			this.board.setPieceAt(null, new Coordinate(Board.SIZE - 1, move.getFrom().getY()));
			this.board.setPieceAt(rook, new Coordinate(move.getTo().getX() - 1, move.getTo().getY()));
		}
	}

	private void addEnPassant(Move move) {
		Move lastMove = this.board.getMoveHistory().get(this.board.getMoveHistory().size() - 1);
		this.board.setPieceAt(null, lastMove.getTo());
	}

	/**
	 * validates the move with accordance to chess rules
	 * @param from start coordinate of the considering
	 * @param to end coordinate of the considering move
	 * @return the move if considered as valid
	 * @throws InvalidMoveException in case the move is not valid
	 * @throws KingInCheckException in case of the check on the own king caused by considered move
	 * @throws InvalidColorException in case of a colour of a piece other then black or white
	 * @throws NoKingException in case there is no king of a certain colour on the board
	 */
	private Move validateMove(Coordinate from, Coordinate to)
			throws InvalidMoveException, KingInCheckException, InvalidColorException, NoKingException {

		preValidateMove(from, to);

		Move consideredMove = Move.generateMove(this.board, from, to);
		consideredMove.setMovedPiece(this.board.getPieceAt(from));

		consideredMove.validateMoveWithoutConsideringCheck(this.board);

		if (consideredMove.getType() == MoveType.CASTLING) {
			checkIfKingWouldBeInCheckOnItsWayForCastling(consideredMove);
		} else {
			checkIfKingWouldBeInCheck(consideredMove);
		}

		return consideredMove;
	}

	private void preValidateMove(Coordinate from, Coordinate to) throws InvalidMoveException {
		from.coordinateValidator();
		to.coordinateValidator();
		startSquareValidator(from);
		nextTurnColorValidator(from);
	}

	private void nextTurnColorValidator(Coordinate from) throws InvalidMoveException {
		if (this.board.getPieceAt(from).getColor() != calculateNextMoveColor()) {
			throw new InvalidMoveException("Next color to perform move is" + calculateNextMoveColor() + "!");
		}
	}

	private void startSquareValidator(Coordinate from) throws InvalidMoveException {
		if (this.board.getPieceAt(from) == null || this.board.getPieceAt(from).getType() == PieceType.EN_PASSANT_PAWN) {
			throw new InvalidMoveException("The start square is empty!");
		}
	}

	private void tempPiecesSwap(Coordinate from, Coordinate to) {
		this.board.setPieceAt(this.board.getPieceAt(from), to);
		this.board.setPieceAt(null, from);
	}

	private void checkIfKingWouldBeInCheck(Move move)
			throws InvalidColorException, NoKingException, InvalidMoveException {
		tempPiecesSwap(move.getFrom(), move.getTo());
		if (isKingInCheck(move.getMovedPiece().getColor())) {
			tempPiecesSwap(move.getTo(), move.getFrom());
			throw new KingInCheckException();
		}
		tempPiecesSwap(move.getTo(), move.getFrom());
	}

	private void checkIfKingWouldBeInCheckOnItsWayForCastling(Move move)
			throws InvalidMoveException, InvalidColorException, NoKingException {
		Coordinate tempTo = new Coordinate(move.getFrom().getX(), move.getFrom().getY());
		int direction = (move.getTo().getX() - move.getFrom().getX())
				/ Math.abs(move.getTo().getX() - move.getFrom().getX());

		for (int i = 1; i <= Math.abs(move.getTo().getX() - move.getFrom().getX()); i++) {
			tempTo.setX(move.getFrom().getX() + i * direction);
			tempPiecesSwap(move.getFrom(), tempTo);
			if (isKingInCheck(this.board.getPieceAt(tempTo).getColor())) {
				tempPiecesSwap(tempTo, move.getFrom());
				throw new KingInCheckException();
			}
			tempPiecesSwap(tempTo, move.getFrom());
		}
	}

	private boolean isKingInCheck(Color kingColor) throws InvalidColorException, NoKingException {
		Coordinate positionOfKing = findTheKing(kingColor);
		for (Coordinate square : findAllPiecesOfColor(oppositeColor(kingColor))) {
			Move move = new CaptureMove(square, positionOfKing);
			try {
				move.validateMoveWithoutConsideringCheck(this.board);
				return true;
			} catch (InvalidMoveException e) {
				continue;
			}
		}
		return false;
	}

	private List<Coordinate> findAllPiecesOfColor(Color color) {
		List<Coordinate> allPiecesOfColor = new ArrayList<Coordinate>();
		for (int i = 0; i < Board.SIZE; i++) {
			for (int j = 0; j < Board.SIZE; j++) {
				Coordinate square = new Coordinate(i, j);
				Piece currentPiece = this.board.getPieceAt(square);
				if (currentPiece != null && currentPiece.getColor().equals(color)) {
					allPiecesOfColor.add(square);
				}
				if (allPiecesOfColor.size() == 2 * Board.SIZE) {
					return allPiecesOfColor;
				}
			}
		}
		return allPiecesOfColor;
	}

	private Coordinate findTheKing(Color color) throws NoKingException {
		for (int i = 0; i < Board.SIZE; i++) {
			for (int j = 0; j < Board.SIZE; j++) {
				Coordinate square = new Coordinate(i, j);
				Piece currentPiece = this.board.getPieceAt(square);
				if (currentPiece != null && currentPiece.getType().equals(PieceType.KING)
						&& currentPiece.getColor().equals(color)) {
					return square;
				}
			}
		}
		throw new NoKingException();
	}

	private Color oppositeColor(Color color) throws InvalidColorException {
		if (color == Color.WHITE) {
			return Color.BLACK;
		}
		if (color == Color.BLACK) {
			return Color.WHITE;
		}
		throw new InvalidColorException();
	}

	private boolean isAnyMoveValid(Color nextMoveColor) throws InvalidColorException, NoKingException {
		List<Coordinate> locationOfPieces = findAllPiecesOfColor(nextMoveColor);
		for (Coordinate pieceLocation : locationOfPieces) {
			for (int i = 0; i < Board.SIZE; i++) {
				for (int j = 0; j < Board.SIZE; j++) {
					try {
						validateMove(pieceLocation, new Coordinate(i, j));
						return true;
					} catch (InvalidMoveException e) {
						continue;
					}
				}
			}
		}

		return false;
	}

	private Color calculateNextMoveColor() {
		if (this.board.getMoveHistory().size() % 2 == 0) {
			return Color.WHITE;
		} else {
			return Color.BLACK;
		}
	}

	private int findLastNonAttackMoveIndex() {
		int counter = 0;
		int lastNonAttackMoveIndex = 0;

		for (Move move : this.board.getMoveHistory()) {
			if (move.getType() != MoveType.ATTACK) {
				lastNonAttackMoveIndex = counter;
			}
			counter++;
		}
		return lastNonAttackMoveIndex;
	}

	private boolean isThereAnEnPassantPawnAnywhere() {
		for (int i = 0; i < Board.SIZE; i++) {
			for (int j = 0; j < Board.SIZE; j++) {
				Coordinate square = new Coordinate(i, j);
				if (this.board.getPieceAt(square) != null
						&& this.board.getPieceAt(square).getType() == PieceType.EN_PASSANT_PAWN) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * deletes the EnPassantPawn object if it was not captured in one turn of the game
	 */
	private void getRidOfTheEnPassantPawn() {
		for (int i = 0; i < Board.SIZE; i++) {
			for (int j = 0; j < Board.SIZE; j++) {
				Coordinate square = new Coordinate(i, j);
				if (this.board.getPieceAt(square) != null
						&& this.board.getPieceAt(square).getType() == PieceType.EN_PASSANT_PAWN) {
					this.board.setPieceAt(null, square);
					break;
				}
			}
		}
	}

	/**
	 * sets an artificial representation of a piece on the board, 
	 * so it can be captured in next turn in case of ae En Passant move
	 */
	private void setNewEnPassantPawnIfNeeded() {
		Move lastMove = this.board.getMoveHistory().get(this.board.getMoveHistory().size() - 1);
		int lastMoveLength = lastMove.getTo().getY() - lastMove.getFrom().getY();
		if (lastMove.getType() == MoveType.ATTACK && lastMove.getMovedPiece() != null
				&& lastMove.getMovedPiece().getType() == PieceType.PAWN && Math.abs(lastMoveLength) == 2) {
			Piece shadowPawn = new EnPassantPawn(lastMove.getMovedPiece().getColor());
			Coordinate shadowPawnLocation = new Coordinate(lastMove.getFrom().getX(),
					lastMove.getFrom().getY() + lastMoveLength / 2);
			this.board.setPieceAt(shadowPawn, shadowPawnLocation);
		}
	}

	/**
	 * change the Pawn object, that can perform a two steps attack 
	 * into a regular pawn after its first move
	 */
	private void changeFirstMovePawnIntoRegularPawnIfNeeded() {
		Move lastMove = this.board.getMoveHistory().get(this.board.getMoveHistory().size() - 1);
		if (lastMove.getType() == MoveType.ATTACK && lastMove.getMovedPiece() != null
				&& lastMove.getMovedPiece().getType() == PieceType.PAWN) {
			this.board.setPieceAt(new PawnAfterFirstMove(lastMove.getMovedPiece().getColor()), lastMove.getTo());
		}
	}

}
