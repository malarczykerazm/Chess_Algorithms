package com.capgemini.chess.algorithms.piece;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;

public class Pawn extends Piece {
	
	private final PieceType type = PieceType.PAWN;

	public Pawn(Color color) {
		super(color);
	}

	@Override
	public List<Coordinate> possibleAttackMoves(Coordinate from) {
		if(this.getColor() == Color.WHITE) {
			return possibleAttackMovesWhite(from);
		} else if(this.getColor() == Color.BLACK) {
			return possibleAttackMovesBlack(from);
		}
		//TODO WrongColorException?
		return null;
	}
	
	@Override
	public PieceType getType() {
		return type;
	}
	
	@Override
	public List<Coordinate> possibleCaptureMoves(Coordinate from) {
		if(this.getColor() == Color.WHITE) {
			return possibleCaptureMovesWhite(from);
		} else if(this.getColor() == Color.BLACK) {
			return possibleCaptureMovesBlack(from);
		}
		//TODO WrongColorException?
		return null;
	}
	
	@Override
	public List<Coordinate> possibleAttackMovesForFirstMove(Coordinate from) {
		if(this.getColor() == Color.WHITE) {
			return possibleAttackMovesForFirstMoveWhite(from);
		} else if(this.getColor() == Color.BLACK) {
			return possibleAttackMovesForFirstMoveBlack(from);
		}
		//TODO WrongColorException?
		return null;
	}
	
	private List<Coordinate> possibleAttackMovesWhite(Coordinate from) {
		List<Coordinate> allPossibleAttackMovesWhite = new ArrayList<Coordinate>();
		Coordinate to = new Coordinate (from.getX(), from.getY() + 1);
		allPossibleAttackMovesWhite.add(to);
		return allPossibleAttackMovesWhite;
	}
	
	private List<Coordinate> possibleAttackMovesBlack(Coordinate from) {
		List<Coordinate> allPossibleAttackMovesBlack = new ArrayList<Coordinate>();
		Coordinate to = new Coordinate (from.getX(), from.getY() - 1);
		allPossibleAttackMovesBlack.add(to);
		return allPossibleAttackMovesBlack;
	}	
	
	private List<Coordinate> possibleCaptureMovesWhite(Coordinate from) {
		List<Coordinate> allPossibleCaptureMovesWhite = new ArrayList<Coordinate>();
		Coordinate toRight = new Coordinate(from.getX() + 1, from.getY() + 1);
		allPossibleCaptureMovesWhite.add(toRight);
		Coordinate toLeft = new Coordinate(from.getX() - 1, from.getY() + 1);
		allPossibleCaptureMovesWhite.add(toLeft);
		return allPossibleCaptureMovesWhite;
	}
	
	private List<Coordinate> possibleCaptureMovesBlack(Coordinate from) {
		List<Coordinate> allPossibleCaptureMovesBlack = new ArrayList<Coordinate>();
		Coordinate toRight = new Coordinate(from.getX() - 1, from.getY() - 1);
		allPossibleCaptureMovesBlack.add(toRight);
		Coordinate toLeft = new Coordinate(from.getX() + 1, from.getY() - 1);
		allPossibleCaptureMovesBlack.add(toLeft);
		return allPossibleCaptureMovesBlack;
	}
	
	private List<Coordinate> possibleAttackMovesForFirstMoveWhite(Coordinate from) {
		List<Coordinate> allPossibleAttackMovesWhite = new ArrayList<Coordinate>();
		Coordinate to1 = new Coordinate (from.getX(), from.getY() + 1);
		Coordinate to2 = new Coordinate (from.getX(), from.getY() + 2);
		allPossibleAttackMovesWhite.add(to1);
		allPossibleAttackMovesWhite.add(to2);
		return allPossibleAttackMovesWhite;
	}
	
	private List<Coordinate> possibleAttackMovesForFirstMoveBlack(Coordinate from) {
		List<Coordinate> allPossibleAttackMovesBlack = new ArrayList<Coordinate>();
		Coordinate to1 = new Coordinate (from.getX(), from.getY() - 1);
		Coordinate to2 = new Coordinate (from.getX(), from.getY() - 2);
		allPossibleAttackMovesBlack.add(to1);
		allPossibleAttackMovesBlack.add(to2);
		return allPossibleAttackMovesBlack;
	}	

}