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
import com.capgemini.chess.algorithms.move.AttackMove;
import com.capgemini.chess.algorithms.move.CaptureMove;
import com.capgemini.chess.algorithms.move.CastlingMove;
import com.capgemini.chess.algorithms.move.EnPassantMove;
import com.capgemini.chess.algorithms.move.Move;
import com.capgemini.chess.algorithms.move.MoveValidation;
import com.capgemini.chess.algorithms.piece.Bishop;
import com.capgemini.chess.algorithms.piece.EnPassantPawn;
import com.capgemini.chess.algorithms.piece.King;
import com.capgemini.chess.algorithms.piece.Knight;
import com.capgemini.chess.algorithms.piece.Pawn;
import com.capgemini.chess.algorithms.piece.Piece;
import com.capgemini.chess.algorithms.piece.Queen;
import com.capgemini.chess.algorithms.piece.Rook;

/**
 * Class for managing of basic operations on the Chess Board.
 *
 * @author Michal Bejm
 *
 */
public class BoardManager {

	private Board board = new Board();

	public BoardManager() {
		initBoard();
	}

	public BoardManager(List<Move> moves) {
		initBoard();
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
	public Move performMove(Coordinate from, Coordinate to) throws InvalidMoveException, InvalidColorException, NoKingException {

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

	private void initBoard() {

		this.board.setPieceAt(new Rook(Color.BLACK), new Coordinate(0, 7));
		this.board.setPieceAt(new Knight(Color.BLACK), new Coordinate(1, 7));
		this.board.setPieceAt(new Bishop(Color.BLACK), new Coordinate(2, 7));
		this.board.setPieceAt(new Queen(Color.BLACK), new Coordinate(3, 7));
		this.board.setPieceAt(new King(Color.BLACK), new Coordinate(4, 7));
		this.board.setPieceAt(new Bishop(Color.BLACK), new Coordinate(5, 7));
		this.board.setPieceAt(new Knight(Color.BLACK), new Coordinate(6, 7));
		this.board.setPieceAt(new Rook(Color.BLACK), new Coordinate(7, 7));

		for (int x = 0; x < Board.SIZE; x++) {
			this.board.setPieceAt(new Pawn(Color.BLACK), new Coordinate(x, 6));
		}

		this.board.setPieceAt(new Rook(Color.WHITE), new Coordinate(0, 0));
		this.board.setPieceAt(new Knight(Color.WHITE), new Coordinate(1, 0));
		this.board.setPieceAt(new Bishop(Color.WHITE), new Coordinate(2, 0));
		this.board.setPieceAt(new Queen(Color.WHITE), new Coordinate(3, 0));
		this.board.setPieceAt(new King(Color.WHITE), new Coordinate(4, 0));
		this.board.setPieceAt(new Bishop(Color.WHITE), new Coordinate(5, 0));
		this.board.setPieceAt(new Knight(Color.WHITE), new Coordinate(6, 0));
		this.board.setPieceAt(new Rook(Color.WHITE), new Coordinate(7, 0));

		for (int x = 0; x < Board.SIZE; x++) {
			this.board.setPieceAt(new Pawn(Color.WHITE), new Coordinate(x, 1));
		}
	}

	private void addMove(Move move) {

		addRegularMove(move);

		if (move.getType() == MoveType.CASTLING) {
			addCastling(move);
		} else if (move.getType() == MoveType.EN_PASSANT) {
			addEnPassant(move);
		}

		this.board.getMoveHistory().add(move);
		
		if(isThereAnEnPassantPawnAnywhere()) {
			getRidOfTheEnPassantPawn();
		}
		
		Move lastMove = this.board.getMoveHistory().get(this.board.getMoveHistory().size() - 1);
		int lastMoveLength = lastMove.getTo().getY() - lastMove.getFrom().getY();
		if(lastMove.getType() == MoveType.ATTACK
				&& lastMove.getMovedPiece() != null
				&& lastMove.getMovedPiece().getType() == PieceType.PAWN
				&& Math.abs(lastMoveLength) == 2) {
			Piece shadowPawn = new EnPassantPawn(lastMove.getMovedPiece().getColor());
			Coordinate shadowPawnLocation = new Coordinate(lastMove.getFrom().getX(), lastMove.getFrom().getY() + lastMoveLength / 2);
			this.board.setPieceAt(shadowPawn, shadowPawnLocation);
		}
//TODO Naprawic babole? Wyrzucac constanty do osobnej klasy i tu tylko importowac? 
		
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

	private Move validateMove(Coordinate from, Coordinate to) throws InvalidMoveException, KingInCheckException, InvalidColorException, NoKingException {
		//TODO coordinate validation z wbudowanym rzucaniem wyjatkiem?
		
		if(!(from.isValid())) { throw new InvalidMoveException("The start square is out of the board!"); }
		
		if(!(to.isValid())) { throw new InvalidMoveException("The destination square is out of the board!"); }

		Piece movedPiece = this.board.getPieceAt(from);
		
		if(movedPiece == null || movedPiece.getType() == PieceType.EN_PASSANT_PAWN) { throw new InvalidMoveException("The start square is empty!"); }
	
		if(movedPiece.getColor() != calculateNextMoveColor()) {
			throw new InvalidMoveException("Next color to perform move is" + calculateNextMoveColor() + "!");
		}
		
		//Move move = createTheMove(from, to);
		
		MoveValidation moveVal = new MoveValidation(this.board);
		
		if(moveVal.isAttackValidWithoutConsideringCheck(from, to)) {
			Move consideredMove = new AttackMove(from, to);
			consideredMove.setMovedPiece(movedPiece);
			tempPiecesSwap(from, to);
			if(isKingInCheck(this.board.getPieceAt(to).getColor())) {
				tempPiecesSwap(to, from);
				throw new KingInCheckException();
			}
			tempPiecesSwap(to, from);
			return consideredMove;
		}
		
		if(moveVal.isCaptureValidWithoutConsideringCheck(from, to)) {
			Move consideredMove = new CaptureMove(from, to);
			consideredMove.setMovedPiece(movedPiece);
			tempPiecesSwap(from, to);
			if(isKingInCheck(this.board.getPieceAt(to).getColor())) {
				tempPiecesSwap(to, from);
				throw new KingInCheckException();
			}
			tempPiecesSwap(to, from);
			return consideredMove;
		}
		
		if(moveVal.isCastlingValidWithoutConsideringCheck(from, to)) {
			if(isKingInCheck(this.board.getPieceAt(from).getColor())) {
				throw new KingInCheckException();
			}
			
			Move consideredMove = new CastlingMove(from, to);
			consideredMove.setMovedPiece(movedPiece);
			
			int start = from.getX();
			int stop = to.getX();
		
			int absDistance = Math.abs(stop - start);
			int direction = (stop - start) / absDistance;
			for (int i = 1; i <= absDistance; i++) {
				Coordinate squareOnTheWay = new Coordinate(from.getX() + i * direction, from.getY());
				tempPiecesSwap(from, squareOnTheWay);
				if(isKingInCheck(this.board.getPieceAt(squareOnTheWay).getColor())) {
					tempPiecesSwap(squareOnTheWay, from);
					throw new KingInCheckException();
				}
				tempPiecesSwap(squareOnTheWay, from);
			}
			return consideredMove;
		}
		
		if(moveVal.isEnPassantValidWithoutConsideringCheck(from, to)) {
			Move consideredMove = new EnPassantMove(from, to);
			consideredMove.setMovedPiece(movedPiece);
			tempPiecesSwap(from, to);
			if(isKingInCheck(this.board.getPieceAt(to).getColor())) {
				tempPiecesSwap(to, from);
				throw new KingInCheckException();
			}
			tempPiecesSwap(to, from);
			return consideredMove;
		}
		
		throw new InvalidMoveException();
	}

	private Move createTheMove(Coordinate from, Coordinate to) throws InvalidMoveException {
		Move testMove = new AttackMove(from, to);
		
		if(testMove.isMoveValidWithoutConsideringCheck(this.board)) {
			return testMove;
		}
		
		testMove = (CaptureMove) testMove;
		if(testMove.isMoveValidWithoutConsideringCheck(this.board)) {
			return testMove;
		}
		
		testMove = (CastlingMove) testMove;
		if(testMove.isMoveValidWithoutConsideringCheck(this.board)) {
			return testMove;
		}
		
		testMove = (EnPassantMove) testMove;
		if(testMove.isMoveValidWithoutConsideringCheck(this.board)) {
			return testMove;
		}
		
		throw new InvalidMoveException();
	}
	
	private void tempPiecesSwap(Coordinate from, Coordinate to) {
		this.board.setPieceAt(this.board.getPieceAt(from), to);
		this.board.setPieceAt(null, from);
	}
	
	private boolean isKingInCheck(Color kingColor) throws InvalidColorException, NoKingException {
	MoveValidation moveVal = new MoveValidation(this.board);
	Coordinate positionOfKing = findTheKing(kingColor);
		for(Coordinate square : findAllPiecesOfColor(oppositeColor(kingColor))) {
			if(moveVal.isCaptureValidWithoutConsideringCheck(square, positionOfKing)) {
				return true;
			}
		}
		return false;
		// TODO złapać wyjątek koloru?
	}
	
	private List<Coordinate> findAllPiecesOfColor(Color color) {
		//TODO DODANO
		List<Coordinate> allPiecesOfColor = new ArrayList<Coordinate>();
		for(int i = 0; i < Board.SIZE; i++) {
			for(int j = 0; j < Board.SIZE; j++) {
				Coordinate square = new Coordinate(i, j);
				Piece currentPiece = this.board.getPieceAt(square);
				if(currentPiece != null && currentPiece.getColor().equals(color)) {
					allPiecesOfColor.add(square);
				}
				if(allPiecesOfColor.size() == 2 * Board.SIZE) {
					return allPiecesOfColor;
				}
			}
		}
		return allPiecesOfColor;
	}
	
	private Coordinate findTheKing(Color color) throws NoKingException {
		//TODO DODADNO
		for(int i = 0; i < Board.SIZE; i++) {
			for(int j = 0; j < Board.SIZE; j++) {
				Coordinate square = new Coordinate(i, j);
				Piece currentPiece = this.board.getPieceAt(square);
				if(currentPiece != null && currentPiece.getType().equals(PieceType.KING)
						&& currentPiece.getColor().equals(color)) {
					return square;
				}				
			}
		}
		throw new NoKingException();
	}
	
	private Color oppositeColor(Color color) throws InvalidColorException {
		//TODO DODANO
		if(color == Color.WHITE) {
			return Color.BLACK;
		}
		if(color == Color.BLACK) {
			return Color.WHITE;
		}
	throw new InvalidColorException();
	}

	private boolean isAnyMoveValid(Color nextMoveColor) throws InvalidColorException, NoKingException {
		List<Coordinate> locationOfPieces = findAllPiecesOfColor(nextMoveColor);
		for(Coordinate pieceLocation : locationOfPieces) {
			for(int i = 0; i < Board.SIZE; i++) {
				for(int j = 0; j < Board.SIZE; j++) {
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
		for(int i = 0; i < Board.SIZE; i++) {
			for(int j = 0; j < Board.SIZE; j++) {
				Coordinate square = new Coordinate (i, j);
				if(this.board.getPieceAt(square) != null
						&& this.board.getPieceAt(square).getType() == PieceType.EN_PASSANT_PAWN) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void getRidOfTheEnPassantPawn() {
		for(int i = 0; i < Board.SIZE; i++) {
			for(int j = 0; j < Board.SIZE; j++) {
				Coordinate square = new Coordinate (i, j);
				if(this.board.getPieceAt(square) != null
						&& this.board.getPieceAt(square).getType() == PieceType.EN_PASSANT_PAWN) {
					this.board.setPieceAt(null, square);
					break;
				}
			}
		}
	}

}
