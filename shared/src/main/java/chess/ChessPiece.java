package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return type + " " + pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();

        if (board.getPiece(myPosition).getPieceType() == type.BISHOP){
            //UP RIGHT
            manyMoves(board, possibleMoves, myPosition, 1, 1);
            //DOWN RIGHT
            manyMoves(board, possibleMoves, myPosition, -1, 1);
            //DOWN LEFT
            manyMoves(board, possibleMoves, myPosition, -1, -1);
            //UP LEFT
            manyMoves(board, possibleMoves, myPosition, 1, -1);
        }

        if (board.getPiece(myPosition).getPieceType() == type.ROOK){
            //UP
            manyMoves(board, possibleMoves, myPosition, 1, 0);
            //RIGHT
            manyMoves(board, possibleMoves, myPosition, 0, 1);
            //DOWN
            manyMoves(board, possibleMoves, myPosition, -1, 0);
            //LEFT
            manyMoves(board, possibleMoves, myPosition, 0, -1);
        }

        if (board.getPiece(myPosition).getPieceType() == type.QUEEN){
            //UP
            manyMoves(board, possibleMoves, myPosition, 1, 0);
            //UP RIGHT
            manyMoves(board, possibleMoves, myPosition, 1, 1);
            //RIGHT
            manyMoves(board, possibleMoves, myPosition, 0, 1);
            //DOWN RIGHT
            manyMoves(board, possibleMoves, myPosition, -1, 1);
            //DOWN
            manyMoves(board, possibleMoves, myPosition, -1, 0);
            //DOWN LEFT
            manyMoves(board, possibleMoves, myPosition, -1, -1);
            //LEFT
            manyMoves(board, possibleMoves, myPosition, 0, -1);
            //UP LEFT
            manyMoves(board, possibleMoves, myPosition, 1, -1);
        }

        if (board.getPiece(myPosition).getPieceType() == type.KNIGHT){
            //UP RIGHT
            knightMoves(board, possibleMoves, myPosition, 2, 1);
            //RIGHT UP
            knightMoves(board, possibleMoves, myPosition, 1, 2);
            //RIGHT DOWN
            knightMoves(board, possibleMoves, myPosition, -1, 2);
            //DOWN RIGHT
            knightMoves(board, possibleMoves, myPosition, -2, 1);
            //DOWN LEFT
            knightMoves(board, possibleMoves, myPosition, -2, -1);
            //LEFT DOWN
            knightMoves(board, possibleMoves, myPosition, -1, -2);
            //LEFT UP
            knightMoves(board, possibleMoves, myPosition, 1, -2);
            //UP LEFT
            knightMoves(board, possibleMoves, myPosition, 2, -1);
        }

        if (board.getPiece(myPosition).getPieceType() == type.KING){
            //UP
            kingMoves(board, possibleMoves, myPosition, 1, 0);
            //UP RIGHT
            kingMoves(board, possibleMoves, myPosition, 1, 1);
            //RIGHT
            kingMoves(board, possibleMoves, myPosition, 0, 1);
            //DOWN RIGHT
            kingMoves(board, possibleMoves, myPosition, -1, 1);
            //DOWN
            kingMoves(board, possibleMoves, myPosition, -1, 0);
            //DOWN LEFT
            kingMoves(board, possibleMoves, myPosition, -1, -1);
            //LEFT
            kingMoves(board, possibleMoves, myPosition, 0, -1);
            //UP LEFT
            kingMoves(board, possibleMoves, myPosition, 1, -1);
        }

        if (board.getPiece(myPosition).getPieceType() == type.PAWN) {
            pawnMoves(board, possibleMoves, myPosition, 1, 0);
        }


        return possibleMoves;
    }

    public void manyMoves(ChessBoard board, Collection<ChessMove> possibleMoves,
                          ChessPosition myPosition, int rowChange, int colChange){

        ChessPosition newPos = myPosition.changePostion(rowChange,colChange);
        while (newPos.getRow() >= 1 && newPos.getColumn() >= 1 &&
                newPos.getRow() <= 8 && newPos.getColumn() <= 8) {
            if (board.getPiece(newPos) == null) {
                ChessMove newMove = new ChessMove(myPosition, newPos, null);
                possibleMoves.add(newMove);
            }
            if (board.getPiece(newPos) != null) {
                if (board.getPiece(newPos).pieceColor != board.getPiece(myPosition).pieceColor){
                    ChessMove newMove = new ChessMove(myPosition, newPos, null);
                    possibleMoves.add(newMove);
                }
                break;
            }
            newPos = newPos.changePostion(rowChange,colChange);
        }
    }

    public void knightMoves(ChessBoard board, Collection<ChessMove> possibleMoves,
                            ChessPosition myPosition, int rowChange, int colChange){

        ChessPosition newPos = myPosition.changePostion(rowChange,colChange);
        if (newPos.getRow() >= 1 && newPos.getColumn() >= 1 &&
                newPos.getRow() <= 8 && newPos.getColumn() <= 8){
            if (board.getPiece(newPos) == null) {
                ChessMove newMove = new ChessMove(myPosition, newPos, null);
                possibleMoves.add(newMove);
            }
            if (board.getPiece(newPos) != null) {
                if (board.getPiece(newPos).pieceColor != board.getPiece(myPosition).pieceColor){
                    ChessMove newMove = new ChessMove(myPosition, newPos, null);
                    possibleMoves.add(newMove);
                }
            }
        }
    }

    public void pawnMoves(ChessBoard board, Collection<ChessMove> possibleMoves,
                          ChessPosition myPosition, int rowChange, int colChange) {
        ChessPosition newPos = myPosition.changePostion(1, 0);
        if (board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE){
            if (board.getPiece(newPos) == null){
                if (newPos.getRow() == 8){
                    possibleMoves.add(new ChessMove(myPosition, newPos, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, newPos, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, newPos, PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(myPosition, newPos, PieceType.QUEEN));
                }
                else {
                    ChessMove newMove = new ChessMove(myPosition, newPos, null);
                    possibleMoves.add(newMove);
                    newPos = myPosition.changePostion(2,0);
                    if (myPosition.getRow() == 2 && board.getPiece(newPos) == null){
                        newMove = new ChessMove(myPosition, newPos, null);
                        possibleMoves.add(newMove);
                    }
                }
            }
            ChessPosition upRight = myPosition.changePostion(1,1);
            ChessPosition upLeft = myPosition.changePostion(1,-1);
            //UP RIGHT CHECK
            if (upRight.getRow() <= 8 && upRight.getColumn() <= 8 && board.getPiece(upRight) != null &&
                    board.getPiece(upRight).pieceColor != board.getPiece(myPosition).pieceColor){
                if (upRight.getRow() == 8){
                    possibleMoves.add(new ChessMove(myPosition, upRight, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, upRight, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, upRight, PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(myPosition, upRight, PieceType.QUEEN));
                }
                else {
                    ChessMove newMove = new ChessMove(myPosition, upRight, null);
                    possibleMoves.add(newMove);
                }
            }
            //UP LEFT CHECK
            if (upLeft.getRow() <= 8 && upLeft.getColumn() >= 1 && board.getPiece(upLeft) != null &&
                    board.getPiece(upLeft).pieceColor != board.getPiece(myPosition).pieceColor){
                if (upLeft.getRow() == 8){
                    possibleMoves.add(new ChessMove(myPosition, upLeft, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, upLeft, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, upLeft, PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(myPosition, upLeft, PieceType.QUEEN));
                }
                else {
                    ChessMove newMove = new ChessMove(myPosition, upLeft, null);
                    possibleMoves.add(newMove);
                }
            }
        } //BLACK MOVING DOWN BOARD
        if (board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.BLACK) {
            newPos = myPosition.changePostion(-1, 0);
            ChessPosition myPosition2 = myPosition;
            if (board.getPiece(newPos) == null) {
                if (newPos.getRow() == 1) {
                    possibleMoves.add(new ChessMove(myPosition2, newPos, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition2, newPos, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition2, newPos, PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(myPosition2, newPos, PieceType.QUEEN));
                } else {
                    ChessMove newMove = new ChessMove(myPosition, newPos, null);
                    possibleMoves.add(newMove);
                    //PAWN CAN MOVE TWICE FROM STARTING POSITION
                    newPos = myPosition.changePostion(-2, 0);
                    if (myPosition.getRow() == 7 && board.getPiece(newPos) == null) {
                        newMove = new ChessMove(myPosition, newPos, null);
                        possibleMoves.add(newMove);
                    }
                }
            }
            ChessPosition downRight = myPosition.changePostion(-1, 1);
            ChessPosition downLeft = myPosition.changePostion(-1, -1);
            //DOWN RIGHT CHECK
            if (downRight.getRow() >= 1 && downRight.getColumn() <= 8 && board.getPiece(downRight) != null &&
                    board.getPiece(downRight).pieceColor != board.getPiece(myPosition).pieceColor) {
                if (downRight.getRow() == 1) {
                    possibleMoves.add(new ChessMove(myPosition, downRight, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, downRight, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, downRight, PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(myPosition, downRight, PieceType.QUEEN));
                } else {
                    ChessMove newMove = new ChessMove(myPosition, downRight, null);
                    possibleMoves.add(newMove);
                }
            } //DOWN LEFT CHECK
            if (downLeft.getRow() >= 1 && downLeft.getColumn() >= 1 && board.getPiece(downLeft) != null &&
                    board.getPiece(downLeft).pieceColor != board.getPiece(myPosition).pieceColor) {
                if (downLeft.getRow() == 1) {
                    possibleMoves.add(new ChessMove(myPosition, downLeft, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, downLeft, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, downLeft, PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(myPosition, downLeft, PieceType.QUEEN));
                } else {
                    ChessMove newMove = new ChessMove(myPosition, downLeft, null);
                    possibleMoves.add(newMove);
                }
            }
        }
    }

    public void kingMoves(ChessBoard board, Collection<ChessMove> possibleMoves,
                          ChessPosition myPosition, int rowChange, int colChange){
        ChessPosition myPosition5 = myPosition;
        ChessPosition newPos = myPosition.changePostion(rowChange,colChange);
        if (newPos.getRow() >= 1 && newPos.getColumn() >= 1 &&
                newPos.getRow() <= 8 && newPos.getColumn() <= 8){

            if (board.getPiece(newPos) == null) {
                ChessMove newMove = new ChessMove(myPosition, newPos, null);
                possibleMoves.add(newMove);
            }
            else if (board.getPiece(newPos) != null) {
                if (board.getPiece(newPos).pieceColor != board.getPiece(myPosition).pieceColor){
                    ChessMove newMove = new ChessMove(myPosition5, newPos, null);
                    possibleMoves.add(newMove);
                }
            }
        }
    }


}
