package com.capgemini.chess.algorithms.piece;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidColorException;

public class Pawn extends Piece {
	
	private final PieceType type = PieceType.PAWN;
	
	public Pawn(Color color) {
		super(color);
	}

	@Override
	public List<Coordinate> possibleAttackMoves(Coordinate from) throws InvalidColorException {
		if(this.getColor() == Color.WHITE) {
			return possibleAttackMovesForFirstMoveWhite(from);
		} else if(this.getColor() == Color.BLACK) {
			return possibleAttackMovesForFirstMoveBlack(from);
		}
		throw new InvalidColorException("The Pawn has an invalid color!");
	}
	
	@Override
	public List<Coordinate> possibleCaptureMoves(Coordinate from) throws InvalidColorException {
		if(this.getColor() == Color.WHITE) {
			return possibleCaptureMovesWhite(from);
		} else if(this.getColor() == Color.BLACK) {
			return possibleCaptureMovesBlack(from);
		}
		throw new InvalidColorException("The Pawn has an invalid color!");
	}
	
	@Override
	public boolean isTheWayFreeToGo(Board board, Coordinate from, Coordinate to) {
		return true;
	}
	
	@Override
	public PieceType getType() {
		return type;
	}
	
	private List<Coordinate> possibleAttackMovesForFirstMoveWhite(Coordinate from) {
		List<Coordinate> allPossibleAttackMovesWhite = new ArrayList<Coordinate>();
		Coordinate to1 = new Coordinate (from.getX(), from.getY() + 1);
		Coordinate to2 = new Coordinate (from.getX(), from.getY() + 2);
		allPossibleAttackMovesWhite = addIfValid(allPossibleAttackMovesWhite, to1);
		allPossibleAttackMovesWhite = addIfValid(allPossibleAttackMovesWhite, to2);
		return allPossibleAttackMovesWhite;
	}
	
	private List<Coordinate> possibleAttackMovesForFirstMoveBlack(Coordinate from) {
		List<Coordinate> allPossibleAttackMovesBlack = new ArrayList<Coordinate>();
		Coordinate to1 = new Coordinate (from.getX(), from.getY() - 1);
		Coordinate to2 = new Coordinate (from.getX(), from.getY() - 2);
		allPossibleAttackMovesBlack = addIfValid(allPossibleAttackMovesBlack, to1);
		allPossibleAttackMovesBlack = addIfValid(allPossibleAttackMovesBlack, to2);
		return allPossibleAttackMovesBlack;
	}	
	
	private List<Coordinate> possibleCaptureMovesWhite(Coordinate from) {
		List<Coordinate> allPossibleCaptureMovesWhite = new ArrayList<Coordinate>();
		Coordinate toRight = new Coordinate(from.getX() + 1, from.getY() + 1);
		allPossibleCaptureMovesWhite = addIfValid(allPossibleCaptureMovesWhite, toRight);
		Coordinate toLeft = new Coordinate(from.getX() - 1, from.getY() + 1);
		allPossibleCaptureMovesWhite = addIfValid(allPossibleCaptureMovesWhite, toLeft);;
		return allPossibleCaptureMovesWhite;
	}
	
	private List<Coordinate> possibleCaptureMovesBlack(Coordinate from) {
		List<Coordinate> allPossibleCaptureMovesBlack = new ArrayList<Coordinate>();
		Coordinate toRight = new Coordinate(from.getX() - 1, from.getY() - 1);
		allPossibleCaptureMovesBlack = addIfValid(allPossibleCaptureMovesBlack, toRight);
		Coordinate toLeft = new Coordinate(from.getX() + 1, from.getY() - 1);
		allPossibleCaptureMovesBlack = addIfValid(allPossibleCaptureMovesBlack, toLeft);
		return allPossibleCaptureMovesBlack;
	}
	
}
