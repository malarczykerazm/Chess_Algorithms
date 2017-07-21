package com.capgemini.chess.algorithms.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.BoardState;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
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
import com.capgemini.chess.algorithms.piece.Bishop;
import com.capgemini.chess.algorithms.piece.King;
import com.capgemini.chess.algorithms.piece.Knight;
import com.capgemini.chess.algorithms.piece.Pawn;
import com.capgemini.chess.algorithms.piece.Piece;
import com.capgemini.chess.algorithms.piece.Queen;
import com.capgemini.chess.algorithms.piece.Rook;

/**
 * Test class for testing {@link BoardManager}
 * 
 * @author Michal Bejm
 *
 */
public class BoardManagerTest {

	@Test
	public void testGenerateBoardInitialPosition() {
		// given
		List<Move> moves = new ArrayList<>();

		// when
		BoardManager boardManager = new BoardManager(moves);

		// then
		for (int x = 0; x < Board.SIZE; x++) {
			for (int y = 0; y < Board.SIZE; y++) {
				if (y > 1 && y < 6) {
					assertNull(boardManager.getBoard().getPieceAt(new Coordinate(x, y)));
				} else {
					assertNotNull(boardManager.getBoard().getPieceAt(new Coordinate(x, y)));
				}
			}
		}
		assertEquals(new Pawn(Color.WHITE), boardManager.getBoard().getPieceAt(new Coordinate(5, 1)));
		assertEquals(new King(Color.WHITE), boardManager.getBoard().getPieceAt(new Coordinate(4, 0)));
		assertEquals(new Bishop(Color.WHITE), boardManager.getBoard().getPieceAt(new Coordinate(5, 0)));
		assertEquals(new Rook(Color.BLACK), boardManager.getBoard().getPieceAt(new Coordinate(0, 7)));
		assertEquals(new Knight(Color.BLACK), boardManager.getBoard().getPieceAt(new Coordinate(1, 7)));
		assertEquals(new Queen(Color.BLACK), boardManager.getBoard().getPieceAt(new Coordinate(3, 7)));
		assertEquals(32, calculateNumberOfPieces(boardManager.getBoard()));
	}

	@Test
	public void testGenerateBoardAttack() {
		// given
		List<Move> moves = new ArrayList<>();
		Move move = new AttackMove(null, null);
		move.setFrom(new Coordinate(5, 1));
		move.setTo(new Coordinate(5, 3));
		moves.add(move);

		// when
		BoardManager boardManager = new BoardManager(moves);

		// then
		assertNull(boardManager.getBoard().getPieceAt(new Coordinate(5, 1)));
		assertNotNull(boardManager.getBoard().getPieceAt(new Coordinate(5, 3)));
		assertEquals(32, calculateNumberOfPieces(boardManager.getBoard()));
	}

	@Test
	public void testGenerateBoardCapture() {
		// given
		List<Move> moves = new ArrayList<>();
		Move move = new CaptureMove(null, null);
		move.setFrom(new Coordinate(0, 0));
		move.setTo(new Coordinate(0, 6));
		moves.add(move);

		// when
		BoardManager boardManager = new BoardManager(moves);

		// then
		assertNull(boardManager.getBoard().getPieceAt(new Coordinate(0, 0)));
		assertNotNull(boardManager.getBoard().getPieceAt(new Coordinate(0, 6)));
		assertEquals(31, calculateNumberOfPieces(boardManager.getBoard()));
	}

	@Test
	public void testGenerateBoardCastling() {
		// given
		List<Move> moves = new ArrayList<>();
		Move move = new CastlingMove(null, null);
		move.setFrom(new Coordinate(4, 0));
		move.setTo(new Coordinate(2, 0));
		moves.add(move);

		// when
		BoardManager boardManager = new BoardManager(moves);

		// then
		assertNull(boardManager.getBoard().getPieceAt(new Coordinate(4, 0)));
		assertNotNull(boardManager.getBoard().getPieceAt(new Coordinate(2, 0)));
		assertEquals(new King(Color.WHITE), boardManager.getBoard().getPieceAt(new Coordinate(2, 0)));
		assertEquals(new Rook(Color.WHITE), boardManager.getBoard().getPieceAt(new Coordinate(3, 0)));
	}

	@Test
	public void testGenerateBoardEnPassant() {
		// given
		List<Move> moves = new ArrayList<>();
		Move move1 = new AttackMove(null, null);
		move1.setFrom(new Coordinate(1, 1));
		move1.setTo(new Coordinate(1, 4));

		moves.add(move1);
		Move move2 = new AttackMove(null, null);
		move2.setFrom(new Coordinate(2, 6));
		move2.setTo(new Coordinate(2, 4));

		moves.add(move2);
		Move move3 = new EnPassantMove(null, null);
		move3.setFrom(new Coordinate(1, 4));
		move3.setTo(new Coordinate(2, 5));

		moves.add(move3);

		// when
		BoardManager boardManager = new BoardManager(moves);

		// then
		assertNull(boardManager.getBoard().getPieceAt(new Coordinate(2, 4)));
		assertNull(boardManager.getBoard().getPieceAt(new Coordinate(1, 4)));
		assertNotNull(boardManager.getBoard().getPieceAt(new Coordinate(2, 5)));
		assertEquals(new Pawn(Color.WHITE), boardManager.getBoard().getPieceAt(new Coordinate(2, 5)));
		assertEquals(31, calculateNumberOfPieces(boardManager.getBoard()));
	}

	@Test
	public void testGenerateBoardPromotion() {
		// given
		List<Move> moves = new ArrayList<>();
		Move move = new CaptureMove(null, null);
		move.setFrom(new Coordinate(1, 6));
		move.setTo(new Coordinate(1, 0));
		moves.add(move);

		// when
		BoardManager boardManager = new BoardManager(moves);

		// then
		assertEquals(new Queen(Color.BLACK), boardManager.getBoard().getPieceAt(new Coordinate(1, 0)));
	}

	@Test
	public void testPerformMoveBishopAttack() throws InvalidMoveException, InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.setPieceAt(new Bishop(Color.WHITE), new Coordinate(0, 6));
		// King added to make the test possible to run
		board.setPieceAt(new King(Color.WHITE), new Coordinate(3, 1));

		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(0, 6), new Coordinate(6, 0));

		// then
		assertEquals(MoveType.ATTACK, move.getType());
		assertEquals(new Bishop(Color.WHITE), move.getMovedPiece());
	}

	@Test
	public void testPerformMovePawnAttack() throws InvalidMoveException, InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(new Pawn(Color.BLACK), new Coordinate(4, 6));
		// King added to make the test possible to run
		board.setPieceAt(new King(Color.BLACK), new Coordinate(7, 7));

		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(4, 6), new Coordinate(4, 4));

		// then
		assertEquals(MoveType.ATTACK, move.getType());
		assertEquals(new Pawn(Color.BLACK), move.getMovedPiece());
	}

	@Test
	public void testPerformMoveKingAttack() throws InvalidMoveException, InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.setPieceAt(new King(Color.WHITE), new Coordinate(4, 0));

		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(4, 0), new Coordinate(4, 1));

		// then
		assertEquals(MoveType.ATTACK, move.getType());
		assertEquals(new King(Color.WHITE), move.getMovedPiece());
	}

	@Test
	public void testPerformMoveKnightCapture() throws InvalidMoveException, InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(new Knight(Color.BLACK), new Coordinate(3, 4));
		board.setPieceAt(new Rook(Color.WHITE), new Coordinate(2, 6));
		// King added to make the test possible to run
		board.setPieceAt(new King(Color.BLACK), new Coordinate(0, 0));

		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(3, 4), new Coordinate(2, 6));

		// then
		assertEquals(MoveType.CAPTURE, move.getType());
		assertEquals(new Knight(Color.BLACK), move.getMovedPiece());
	}

	@Test
	public void testPerformMoveQueenCapture() throws InvalidMoveException, InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.setPieceAt(new Queen(Color.WHITE), new Coordinate(5, 0));
		board.setPieceAt(new Pawn(Color.BLACK), new Coordinate(7, 2));
		// King added to make the test possible to run
		board.setPieceAt(new King(Color.WHITE), new Coordinate(0, 0));

		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(5, 0), new Coordinate(7, 2));

		// then
		assertEquals(MoveType.CAPTURE, move.getType());
		assertEquals(new Queen(Color.WHITE), move.getMovedPiece());
	}

	@Test
	public void testPerformMoveRookCapture() throws InvalidMoveException, InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(new Rook(Color.BLACK), new Coordinate(1, 4));
		board.setPieceAt(new Knight(Color.WHITE), new Coordinate(5, 4));
		// King added to make the test possible to run
		board.setPieceAt(new King(Color.BLACK), new Coordinate(7, 7));

		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(1, 4), new Coordinate(5, 4));

		// then
		assertEquals(MoveType.CAPTURE, move.getType());
		assertEquals(new Rook(Color.BLACK), move.getMovedPiece());
	}

	@Test
	public void testPerformMoveCastling() throws InvalidMoveException, InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.setPieceAt(new King(Color.WHITE), new Coordinate(4, 0));
		board.setPieceAt(new Rook(Color.WHITE), new Coordinate(7, 0));

		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(4, 0), new Coordinate(6, 0));

		// then
		assertEquals(MoveType.CASTLING, move.getType());
		assertEquals(new King(Color.WHITE), move.getMovedPiece());
	}

	@Test
	public void testPerformMoveEnPassant() throws InvalidMoveException, InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);

		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(new Pawn(Color.WHITE), new Coordinate(1, 4));
		board.setPieceAt(new Pawn(Color.BLACK), new Coordinate(2, 6));
		// King added to make the test possible to run
		board.setPieceAt(new King(Color.WHITE), new Coordinate(7, 7));
		board.setPieceAt(new King(Color.BLACK), new Coordinate(7, 0));
		boardManager.performMove(new Coordinate(2, 6), new Coordinate(2, 4));

		// when
		Move move = boardManager.performMove(new Coordinate(1, 4), new Coordinate(2, 5));

		// then
		assertEquals(MoveType.EN_PASSANT, move.getType());
		assertEquals(new Pawn(Color.WHITE), move.getMovedPiece());
	}

	@Test
	public void testPerformMoveInvalidIndexOutOfBound() throws InvalidColorException, NoKingException {
		// given
		BoardManager boardManager = new BoardManager();

		// when
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(8, 6), new Coordinate(7, 6));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void testPerformMoveInvalidMoveOrder() throws InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.setPieceAt(new King(Color.BLACK), new Coordinate(0, 7));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(0, 7), new Coordinate(1, 6));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void testPerformMoveInvalidEmptySpot() throws InvalidColorException, NoKingException {
		// given
		Board board = new Board();

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(0, 7), new Coordinate(1, 6));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void testPerformMoveInvalidSameSpot() throws InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.setPieceAt(new King(Color.WHITE), new Coordinate(0, 0));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(0, 0), new Coordinate(0, 0));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void testPerformMoveInvalidPawnBackwardMove() throws InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.setPieceAt(new Pawn(Color.WHITE), new Coordinate(1, 2));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 2), new Coordinate(1, 1));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void testPerformMoveInvalidPawnAttackDestination() throws InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.setPieceAt(new Pawn(Color.WHITE), new Coordinate(1, 2));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 2), new Coordinate(0, 3));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void testPerformMoveInvalidPawnAttackDistance() throws InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		Piece movedPiece = new Pawn(Color.WHITE);
		board.setPieceAt(movedPiece, new Coordinate(1, 1));
		Move changeOfThePawn = new AttackMove(new Coordinate(1, 1), new Coordinate(1, 2));
		changeOfThePawn.setMovedPiece(board.getPieceAt(changeOfThePawn.getFrom()));
		board.getMoveHistory().add(changeOfThePawn);
		board.getMoveHistory().add(createDummyMove(board));
		// King added to make the test possible to run
		board.setPieceAt(new King(Color.WHITE), new Coordinate(7, 7));

		// when
		BoardManager boardManager = new BoardManager(board.getMoveHistory());
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 2), new Coordinate(1, 4));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void testPerformMoveInvalidPawnCaptureDestination() throws InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.setPieceAt(new Pawn(Color.WHITE), new Coordinate(1, 2));
		board.setPieceAt(new Pawn(Color.BLACK), new Coordinate(1, 3));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 2), new Coordinate(1, 3));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void testPerformMoveInvalidKingDistance() throws InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.setPieceAt(new King(Color.WHITE), new Coordinate(4, 0));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(4, 0), new Coordinate(4, 2));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void testPerformMoveInvalidKnightDestination() throws InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.setPieceAt(new Knight(Color.WHITE), new Coordinate(1, 1));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 1), new Coordinate(3, 3));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void testPerformMoveInvalidBishopDestination() throws InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.setPieceAt(new Bishop(Color.WHITE), new Coordinate(1, 1));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 1), new Coordinate(1, 2));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void testPerformMoveInvalidQueenLeapsOver() throws InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.setPieceAt(new Queen(Color.WHITE), new Coordinate(1, 1));
		board.setPieceAt(new Pawn(Color.BLACK), new Coordinate(4, 4));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 1), new Coordinate(6, 6));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void testPerformMoveInvalidRookLeapsOver() throws InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.setPieceAt(new Rook(Color.WHITE), new Coordinate(3, 0));
		board.setPieceAt(new Pawn(Color.WHITE), new Coordinate(3, 2));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(3, 0), new Coordinate(3, 7));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void testPerformMoveInvalidOwnPieceCapture() throws InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.setPieceAt(new Knight(Color.WHITE), new Coordinate(5, 6));
		board.setPieceAt(new Pawn(Color.WHITE), new Coordinate(3, 5));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(5, 6), new Coordinate(3, 5));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void testPerformMoveInvalidCastlingPiecesMoved()
			throws InvalidMoveException, InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);

		board.setPieceAt(new King(Color.WHITE), new Coordinate(4, 0));
		board.setPieceAt(new Rook(Color.WHITE), new Coordinate(7, 0));
		boardManager.performMove(new Coordinate(4, 0), new Coordinate(3, 0));
		board.getMoveHistory().add(createDummyMove(board));
		boardManager.performMove(new Coordinate(3, 0), new Coordinate(4, 0));
		board.getMoveHistory().add(createDummyMove(board));

		// when
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(4, 0), new Coordinate(6, 0));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void testPerformMoveInvalidCastlingWithPiecesBetween() throws InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.setPieceAt(new King(Color.WHITE), new Coordinate(4, 0));
		board.setPieceAt(new Rook(Color.WHITE), new Coordinate(7, 0));
		board.setPieceAt(new Bishop(Color.WHITE), new Coordinate(5, 0));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(4, 0), new Coordinate(6, 0));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void testPerformMoveInvalidCastlingKingUnderCheck() throws InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.setPieceAt(new King(Color.WHITE), new Coordinate(4, 0));
		board.setPieceAt(new Rook(Color.WHITE), new Coordinate(7, 0));
		board.setPieceAt(new Rook(Color.BLACK), new Coordinate(5, 7));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(4, 0), new Coordinate(6, 0));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void testPerformMoveInvalidKingWouldBeChecked() throws InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.setPieceAt(new King(Color.WHITE), new Coordinate(4, 0));
		board.setPieceAt(new Bishop(Color.WHITE), new Coordinate(4, 5));
		board.setPieceAt(new Rook(Color.BLACK), new Coordinate(4, 7));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(4, 5), new Coordinate(7, 2));
		} catch (InvalidMoveException e) {
			exceptionThrown = e instanceof KingInCheckException;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void testUpdateBoardStateRegular() throws InvalidMoveException, InvalidColorException, NoKingException {
		// given
		BoardManager boardManager = new BoardManager();

		// when
		BoardState boardState = boardManager.updateBoardState();

		// then
		assertEquals(BoardState.REGULAR, boardState);
	}

	@Test
	public void testUpdateBoardStateCheck() throws InvalidMoveException, InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(new Bishop(Color.WHITE), new Coordinate(1, 3));
		board.setPieceAt(new King(Color.BLACK), new Coordinate(4, 0));

		// when
		BoardManager boardManager = new BoardManager(board);
		BoardState boardState = boardManager.updateBoardState();

		// then
		assertEquals(BoardState.CHECK, boardState);
	}

	@Test
	public void testUpdateBoardStateCheckMate() throws InvalidMoveException, InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(new Rook(Color.WHITE), new Coordinate(0, 1));
		board.setPieceAt(new Rook(Color.WHITE), new Coordinate(1, 0));
		board.setPieceAt(new King(Color.BLACK), new Coordinate(4, 0));

		// when
		BoardManager boardManager = new BoardManager(board);
		BoardState boardState = boardManager.updateBoardState();

		// then
		assertEquals(BoardState.CHECK_MATE, boardState);
	}

	@Test
	public void testUpdateBoardStateStaleMate() throws InvalidMoveException, InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(new King(Color.BLACK), new Coordinate(7, 0));
		board.setPieceAt(new Queen(Color.WHITE), new Coordinate(5, 1));
		board.setPieceAt(new King(Color.WHITE), new Coordinate(6, 2));

		// when
		BoardManager boardManager = new BoardManager(board);
		BoardState boardState = boardManager.updateBoardState();

		// then
		assertEquals(BoardState.STALE_MATE, boardState);
	}

	@Test
	public void testCheckThreefoldRepetitionRuleSuccessful() {
		// given
		List<Move> moves = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			Move move1 = new AttackMove(null, null);
			move1.setFrom(new Coordinate(5, 1));
			move1.setTo(new Coordinate(5, 3));
			// move1.setType(MoveType.ATTACK);
			moves.add(move1);

			Move move2 = new AttackMove(null, null);
			move2.setFrom(new Coordinate(5, 6));
			move2.setTo(new Coordinate(5, 4));
			// move2.setType(MoveType.ATTACK);
			moves.add(move2);

			Move move3 = new AttackMove(null, null);
			move3.setFrom(new Coordinate(5, 3));
			move3.setTo(new Coordinate(5, 1));
			// move3.setType(MoveType.ATTACK);
			moves.add(move3);

			Move move4 = new AttackMove(null, null);
			move4.setFrom(new Coordinate(5, 4));
			move4.setTo(new Coordinate(5, 6));
			// move4.setType(MoveType.ATTACK);
			moves.add(move4);
		}
		BoardManager boardManager = new BoardManager(moves);

		// when
		boolean isThreefoldRepetition = boardManager.checkThreefoldRepetitionRule();

		// then
		assertTrue(isThreefoldRepetition);
	}

	@Test
	public void testCheckThreefoldRepetitionRuleUnsuccessful() {
		// given
		List<Move> moves = new ArrayList<>();
		Move move1 = new AttackMove(null, null);
		move1.setFrom(new Coordinate(5, 1));
		move1.setTo(new Coordinate(5, 3));
		// move1.setType(MoveType.ATTACK);
		moves.add(move1);

		Move move2 = new AttackMove(null, null);
		move2.setFrom(new Coordinate(5, 6));
		move2.setTo(new Coordinate(5, 4));
		// move2.setType(MoveType.ATTACK);
		moves.add(move2);

		Move move3 = new AttackMove(null, null);
		move3.setFrom(new Coordinate(5, 3));
		move3.setTo(new Coordinate(5, 1));
		// move3.setType(MoveType.ATTACK);
		moves.add(move3);

		Move move4 = new AttackMove(null, null);
		move4.setFrom(new Coordinate(5, 4));
		move4.setTo(new Coordinate(5, 6));
		// move4.setType(MoveType.ATTACK);
		moves.add(move4);
		BoardManager boardManager = new BoardManager(moves);

		// when
		boolean isThreefoldRepetition = boardManager.checkThreefoldRepetitionRule();

		// then
		assertFalse(isThreefoldRepetition);
	}

	@Test
	public void testCheckFiftyMoveRuleSuccessful() {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		for (int i = 0; i < 100; i++) {
			board.getMoveHistory().add(createDummyMove(board));
		}

		// when
		boolean areFiftyMoves = boardManager.checkFiftyMoveRule();

		// then
		assertTrue(areFiftyMoves);
	}

	@Test
	public void testCheckFiftyMoveRuleUnsuccessfulNotEnoughMoves() {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		for (int i = 0; i < 99; i++) {
			board.getMoveHistory().add(createDummyMove(board));
		}

		// when
		boolean areFiftyMoves = boardManager.checkFiftyMoveRule();

		// then
		assertFalse(areFiftyMoves);
	}

	@Test
	public void testCheckFiftyMoveRuleUnsuccessfulPawnMoved() {
		// given
		BoardManager boardManager = new BoardManager(new Board());

		Move move = new AttackMove(null, null);
		boardManager.getBoard().setPieceAt(new Pawn(Color.WHITE), new Coordinate(0, 0));
		move.setMovedPiece(new Pawn(Color.WHITE));
		move.setFrom(new Coordinate(0, 0));
		move.setTo(new Coordinate(0, 0));
		// move.setType(MoveType.ATTACK);
		boardManager.getBoard().setPieceAt(null, new Coordinate(0, 0));
		boardManager.getBoard().getMoveHistory().add(move);

		for (int i = 0; i < 99; i++) {
			boardManager.getBoard().getMoveHistory().add(createDummyMove(boardManager.getBoard()));
		}

		// when
		boolean areFiftyMoves = boardManager.checkFiftyMoveRule();

		// then
		assertFalse(areFiftyMoves);
	}

	private Move createDummyMove(Board board) {

		Move move = new AttackMove(null, null);

		if (board.getMoveHistory().size() % 2 == 0) {
			board.setPieceAt(new Rook(Color.WHITE), new Coordinate(0, 0));
			move.setMovedPiece(new Rook(Color.WHITE));
		} else {
			board.setPieceAt(new Rook(Color.BLACK), new Coordinate(0, 0));
			move.setMovedPiece(new Rook(Color.BLACK));
		}
		move.setFrom(new Coordinate(0, 0));
		move.setTo(new Coordinate(0, 0));
		// move.setType(MoveType.ATTACK);
		board.setPieceAt(null, new Coordinate(0, 0));
		return move;
	}

	private int calculateNumberOfPieces(Board board) {
		int counter = 0;
		for (int x = 0; x < Board.SIZE; x++) {
			for (int y = 0; y < Board.SIZE; y++) {
				if (board.getPieceAt(new Coordinate(x, y)) != null) {
					counter++;
				}
			}
		}
		return counter;
	}

	@Test
	public void shouldPerformMoveCastlingInTheNegativeXDirection()
			throws InvalidMoveException, InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.setPieceAt(new King(Color.WHITE), new Coordinate(4, 0));
		board.setPieceAt(new Rook(Color.WHITE), new Coordinate(0, 0));

		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(4, 0), new Coordinate(2, 0));

		// then
		assertEquals(MoveType.CASTLING, move.getType());
		assertEquals(new King(Color.WHITE), move.getMovedPiece());
	}

	@Test(expected = KingInCheckException.class)
	public void shouldThrowExceptionDueToKingInCheckDuringCastlingInTheNegativeXDirection()
			throws InvalidMoveException, InvalidColorException, NoKingException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(new King(Color.BLACK), new Coordinate(4, 7));
		board.setPieceAt(new Rook(Color.BLACK), new Coordinate(0, 7));
		board.setPieceAt(new Bishop(Color.WHITE), new Coordinate(7, 3));

		// when
		BoardManager boardManager = new BoardManager(board);
		boardManager.performMove(new Coordinate(4, 7), new Coordinate(2, 7));

		// then
	}
}
