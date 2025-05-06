package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

//REPRESENTS

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final PieceType pieceType;
    private final ChessGame.TeamColor pieceColor;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceType = type;
        this.pieceColor = pieceColor;
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
        return pieceType == that.pieceType && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceType, pieceColor);
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
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    //TO DO: REMOVE TEMP BOARD
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>(); //VALUE TO RETURN
        ChessPiece[][] currBoard = board.getBoard(); //TEMP BOARD TO NOT UPDATE ACTUAL BOARD
        ChessPosition newPos = myPosition; //TEMP POSITION TO UPDATE SHEEZ

        if (this.getPieceType() == pieceType.BISHOP){
            upRight(newPos, myPosition, currBoard, possibleMoves);
            upLeft(newPos, myPosition, currBoard, possibleMoves);
            downRight(newPos, myPosition, currBoard, possibleMoves);
            downLeft(newPos, myPosition, currBoard, possibleMoves);
        }

        if (this.getPieceType() == pieceType.ROOK){
            downDown(newPos, myPosition, currBoard, possibleMoves);
            leftLeft(newPos, myPosition, currBoard, possibleMoves);
            rightRight(newPos, myPosition, currBoard, possibleMoves);
            upUp(newPos, myPosition, currBoard, possibleMoves);
        }

        if (this.getPieceType() == pieceType.QUEEN){
            upRight(newPos, myPosition, currBoard, possibleMoves);
            upLeft(newPos, myPosition, currBoard, possibleMoves);
            downRight(newPos, myPosition, currBoard, possibleMoves);
            downLeft(newPos, myPosition, currBoard, possibleMoves);
            downDown(newPos, myPosition, currBoard, possibleMoves);
            leftLeft(newPos, myPosition, currBoard, possibleMoves);
            rightRight(newPos, myPosition, currBoard, possibleMoves);
            upUp(newPos, myPosition, currBoard, possibleMoves);
        }

        if (this.getPieceType() == pieceType.KNIGHT){
            knightMoves(myPosition, currBoard, possibleMoves);
        }

        if (this.getPieceType() == pieceType.KING){
            kingMoves(myPosition, currBoard, possibleMoves);
        }

        if (this.getPieceType() ==pieceType.PAWN){
            pawnMoves(myPosition, currBoard, possibleMoves);
        }


        return possibleMoves;
    }

    public boolean isEnemy(ChessPiece currPiece, ChessPiece otherPiece) {
        if (currPiece.pieceColor == otherPiece.pieceColor) {
            return false;
        }
        return true;
    }

    //CHECK ALL POSSIBLE MOVES UP RIGHT
    public void upRight(ChessPosition newPos, ChessPosition myPosition,
                        ChessPiece[][] currBoard, Collection<ChessMove> possibleMoves){
        while (newPos.getRow() < 8 && newPos.getColumn() < 8 ) {
            int nextRow = newPos.getRow() + 1;
            int nextCol = newPos.getColumn() + 1;
            ChessPiece currPiece = currBoard[myPosition.getRow()][myPosition.getColumn()];
            ChessPiece nextPiece = currBoard[nextRow][nextCol];

            if (nextPiece != null) {
                if (isEnemy(currPiece, nextPiece)){
                    ChessPosition newMove = new ChessPosition(nextRow, nextCol);
                    possibleMoves.add(new ChessMove(myPosition, newMove, null));
                }
                break;
            }

            ChessPosition newMove = new ChessPosition(nextRow, nextCol);
            possibleMoves.add(new ChessMove(myPosition, newMove, null));
            newPos = newMove;
        }
        newPos = myPosition;
    }

    //CHECK ALL POSSIBLE MOVES UP LEFT
    public void upLeft(ChessPosition newPos, ChessPosition myPosition,
                        ChessPiece[][] currBoard, Collection<ChessMove> possibleMoves){
        while (newPos.getRow() < 8 && newPos.getColumn() > 1 ) {
            int nextRow = newPos.getRow() + 1;
            int nextCol = newPos.getColumn() - 1;
            ChessPiece currPiece = currBoard[myPosition.getRow()][myPosition.getColumn()];
            ChessPiece nextPiece = currBoard[nextRow][nextCol];

            if (nextPiece != null) {
                if (isEnemy(currPiece, nextPiece)){
                    ChessPosition newMove = new ChessPosition(nextRow, nextCol);
                    possibleMoves.add(new ChessMove(myPosition, newMove, null));
                }
                break;
            }

            ChessPosition newMove = new ChessPosition(nextRow, nextCol);
            possibleMoves.add(new ChessMove(myPosition, newMove, null));
            newPos = newMove;
        }
        newPos = myPosition;
    }

    //CHECK ALL POSSIBLE MOVES DOWN RIGHT
    public void downRight(ChessPosition newPos, ChessPosition myPosition,
                        ChessPiece[][] currBoard, Collection<ChessMove> possibleMoves){
        while (newPos.getRow() > 1 && newPos.getColumn() < 8 ) {
            int nextRow = newPos.getRow() - 1;
            int nextCol = newPos.getColumn() + 1;
            ChessPiece currPiece = currBoard[myPosition.getRow()][myPosition.getColumn()];
            ChessPiece nextPiece = currBoard[nextRow][nextCol];

            if (nextPiece != null) {
                if (isEnemy(currPiece, nextPiece)){
                    ChessPosition newMove = new ChessPosition(nextRow, nextCol);
                    possibleMoves.add(new ChessMove(myPosition, newMove, null));
                }
                break;
            }

            ChessPosition newMove = new ChessPosition(nextRow, nextCol);
            possibleMoves.add(new ChessMove(myPosition, newMove, null));
            newPos = newMove;
        }
        newPos = myPosition;
    }

    //CHECK ALL POSSIBLE MOVES DOWN LEFT
    public void downLeft(ChessPosition newPos, ChessPosition myPosition,
                        ChessPiece[][] currBoard, Collection<ChessMove> possibleMoves) {
        while (newPos.getRow() > 1 && newPos.getColumn() > 1 ) {
            int nextRow = newPos.getRow() - 1;
            int nextCol = newPos.getColumn() - 1;
            ChessPiece currPiece = currBoard[myPosition.getRow()][myPosition.getColumn()];
            ChessPiece nextPiece = currBoard[nextRow][nextCol];

            if (nextPiece != null) {
                if (isEnemy(currPiece, nextPiece)){
                    ChessPosition newMove = new ChessPosition(nextRow, nextCol);
                    possibleMoves.add(new ChessMove(myPosition, newMove, null));
                }
                break;
            }

            ChessPosition newMove = new ChessPosition(nextRow, nextCol);
            possibleMoves.add(new ChessMove(myPosition, newMove, null));
            newPos = newMove;
        }
        newPos = myPosition;
    }

    //CHECK ALL POSSIBLE MOVES UP
    public void upUp(ChessPosition newPos, ChessPosition myPosition,
                         ChessPiece[][] currBoard, Collection<ChessMove> possibleMoves) {
        while (newPos.getRow() < 8) {
            int nextRow = newPos.getRow() + 1;
            ChessPiece currPiece = currBoard[myPosition.getRow()][myPosition.getColumn()];
            ChessPiece nextPiece = currBoard[nextRow][myPosition.getColumn()];

            if (nextPiece != null) {
                if (isEnemy(currPiece, nextPiece)){
                    ChessPosition newMove = new ChessPosition(nextRow, myPosition.getColumn());
                    possibleMoves.add(new ChessMove(myPosition, newMove, null));
                }
                break;
            }

            ChessPosition newMove = new ChessPosition(nextRow, myPosition.getColumn());
            possibleMoves.add(new ChessMove(myPosition, newMove, null));
            newPos = newMove;
        }
        newPos = myPosition;
    }

    //CHECK ALL POSSIBLE MOVES RIGHT
    public void rightRight(ChessPosition newPos, ChessPosition myPosition,
                     ChessPiece[][] currBoard, Collection<ChessMove> possibleMoves) {
        while (newPos.getColumn() < 8) {
            int nextCol = newPos.getColumn() + 1;
            ChessPiece currPiece = currBoard[myPosition.getRow()][myPosition.getColumn()];
            ChessPiece nextPiece = currBoard[myPosition.getRow()][nextCol];

            if (nextPiece != null) {
                if (isEnemy(currPiece, nextPiece)){
                    ChessPosition newMove = new ChessPosition(myPosition.getRow(), nextCol);
                    possibleMoves.add(new ChessMove(myPosition, newMove, null));
                }
                break;
            }

            ChessPosition newMove = new ChessPosition(myPosition.getRow(), nextCol);
            possibleMoves.add(new ChessMove(myPosition, newMove, null));
            newPos = newMove;
        }
        newPos = myPosition;
    }

    //CHECK ALL POSSIBLE MOVES DOWN
    public void downDown(ChessPosition newPos, ChessPosition myPosition,
                     ChessPiece[][] currBoard, Collection<ChessMove> possibleMoves) {
        while (newPos.getRow() > 1) {
            int nextRow = newPos.getRow() - 1;
            ChessPiece currPiece = currBoard[myPosition.getRow()][myPosition.getColumn()];
            ChessPiece nextPiece = currBoard[nextRow][myPosition.getColumn()];

            if (nextPiece != null) {
                if (isEnemy(currPiece, nextPiece)){
                    ChessPosition newMove = new ChessPosition(nextRow, myPosition.getColumn());
                    possibleMoves.add(new ChessMove(myPosition, newMove, null));
                }
                break;
            }

            ChessPosition newMove = new ChessPosition(nextRow, myPosition.getColumn());
            possibleMoves.add(new ChessMove(myPosition, newMove, null));
            newPos = newMove;
        }
        newPos = myPosition;
    }

    //CHECK ALL POSSIBLE MOVES RIGHT
    public void leftLeft(ChessPosition newPos, ChessPosition myPosition,
                           ChessPiece[][] currBoard, Collection<ChessMove> possibleMoves) {
        while (newPos.getColumn() > 1) {
            int nextCol = newPos.getColumn() - 1;
            ChessPiece currPiece = currBoard[myPosition.getRow()][myPosition.getColumn()];
            ChessPiece nextPiece = currBoard[myPosition.getRow()][nextCol];

            if (nextPiece != null) {
                if (isEnemy(currPiece, nextPiece)){
                    ChessPosition newMove = new ChessPosition(myPosition.getRow(), nextCol);
                    possibleMoves.add(new ChessMove(myPosition, newMove, null));
                }
                break;
            }

            ChessPosition newMove = new ChessPosition(myPosition.getRow(), nextCol);
            possibleMoves.add(new ChessMove(myPosition, newMove, null));
            newPos = newMove;
        }
        newPos = myPosition;
    }

    public void knightMoves(ChessPosition myPosition, ChessPiece[][] currBoard,
                            Collection<ChessMove> possibleMoves){
        //UP RIGHT
        if (myPosition.getRow() <= 6 && myPosition.getColumn() <=7){
            if (currBoard[myPosition.getRow() + 2][myPosition.getColumn() + 1] == null){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()+1);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
            else if (currBoard[myPosition.getRow() + 2][myPosition.getColumn() + 1].getTeamColor() != this.pieceColor){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()+1);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
        }

        //RIGHT UP
        if (myPosition.getRow() <= 7 && myPosition.getColumn() <=6){
            if (currBoard[myPosition.getRow() + 1][myPosition.getColumn() + 2] == null){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+2);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
            else if (currBoard[myPosition.getRow() + 1][myPosition.getColumn() + 2].getTeamColor() != this.pieceColor){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+2);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
        }

        //RIGHT DOWN
        if (myPosition.getRow() >= 2 && myPosition.getColumn() <=6){
            if (currBoard[myPosition.getRow() - 1][myPosition.getColumn() + 2] == null){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+2);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
            else if (currBoard[myPosition.getRow() - 1][myPosition.getColumn() + 2].getTeamColor() != this.pieceColor){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+2);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
        }

        //DOWN RIGHT
        if (myPosition.getRow() >= 3 && myPosition.getColumn() <=7){
            if (currBoard[myPosition.getRow() - 2][myPosition.getColumn() + 1] == null){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()+1);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
            else if (currBoard[myPosition.getRow() - 2][myPosition.getColumn() + 1].getTeamColor() != this.pieceColor){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()+1);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
        }

        //DOWN LEFT
        if (myPosition.getRow() >= 3 && myPosition.getColumn() >=2){
            if (currBoard[myPosition.getRow() - 2][myPosition.getColumn() - 1] == null){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()-1);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
            else if (currBoard[myPosition.getRow() - 2][myPosition.getColumn() - 1].getTeamColor() != this.pieceColor){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()-1);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
        }

        //LEFT DOWN
        if (myPosition.getRow() >= 2 && myPosition.getColumn() >=3){
            if (currBoard[myPosition.getRow() - 1][myPosition.getColumn() - 2] == null){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-2);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
            else if (currBoard[myPosition.getRow() - 1][myPosition.getColumn() - 2].getTeamColor() != this.pieceColor){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-2);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
        }

        //LEFT UP
        if (myPosition.getRow() <= 7 && myPosition.getColumn() >=3){
            if (currBoard[myPosition.getRow() + 1][myPosition.getColumn() - 2] == null){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-2);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
            else if (currBoard[myPosition.getRow() + 1][myPosition.getColumn() - 2].getTeamColor() != this.pieceColor){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-2);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
        }

        //UP LEFT
        if (myPosition.getRow() <= 6 && myPosition.getColumn() >=2){
            if (currBoard[myPosition.getRow() + 2][myPosition.getColumn() - 1] == null){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()-1);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
            else if (currBoard[myPosition.getRow() + 2][myPosition.getColumn() - 1].getTeamColor() != this.pieceColor){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()-1);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
        }
    }

    public void kingMoves(ChessPosition myPosition, ChessPiece[][] currBoard,
                          Collection<ChessMove> possibleMoves){
        //UP
        if (myPosition.getRow() <= 7) {
            if (currBoard[myPosition.getRow() + 1][myPosition.getColumn()] == null) {
                ChessPosition newMove = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
                possibleMoves.add(new ChessMove(myPosition, newMove, null));

            }
            else if (currBoard[myPosition.getRow() + 1][myPosition.getColumn()].pieceColor != this.pieceColor){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
        }

        //UP RIGHT
        if (myPosition.getRow() <= 7 && myPosition.getColumn() <= 7) {
            if (currBoard[myPosition.getRow() + 1][myPosition.getColumn() + 1] == null) {
                ChessPosition newMove = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));

            }
            else if (currBoard[myPosition.getRow() + 1][myPosition.getColumn() + 1].pieceColor != this.pieceColor){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
        }

        //RIGHT
        if (myPosition.getColumn() <= 7) {
            if (currBoard[myPosition.getRow()][myPosition.getColumn() + 1] == null) {
                ChessPosition newMove = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));

            }
            else if (currBoard[myPosition.getRow()][myPosition.getColumn() + 1].pieceColor != this.pieceColor){
                ChessPosition newMove = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
        }

        //DOWN RIGHT
        if (myPosition.getRow() >= 2 && myPosition.getColumn() <= 7) {
            if (currBoard[myPosition.getRow() - 1][myPosition.getColumn() + 1] == null) {
                ChessPosition newMove = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));

            }
            else if (currBoard[myPosition.getRow() - 1][myPosition.getColumn() + 1].pieceColor != this.pieceColor){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
        }

        //DOWN
        if (myPosition.getRow() >= 2) {
            if (currBoard[myPosition.getRow() - 1][myPosition.getColumn()] == null) {
                ChessPosition newMove = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
                possibleMoves.add(new ChessMove(myPosition, newMove, null));

            }
            else if (currBoard[myPosition.getRow() - 1][myPosition.getColumn()].pieceColor != this.pieceColor){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
        }

        //DOWN LEFT
        if (myPosition.getRow() >= 2 && myPosition.getColumn() >= 2) {
            if (currBoard[myPosition.getRow() - 1][myPosition.getColumn() - 1] == null) {
                ChessPosition newMove = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));

            }
            else if (currBoard[myPosition.getRow() - 1][myPosition.getColumn() - 1].pieceColor != this.pieceColor){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
        }

        //LEFT
        if (myPosition.getColumn() >= 2) {
            if (currBoard[myPosition.getRow()][myPosition.getColumn() - 1] == null) {
                ChessPosition newMove = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));

            }
            else if (currBoard[myPosition.getRow()][myPosition.getColumn() - 1].pieceColor != this.pieceColor){
                ChessPosition newMove = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
        }

        //UP LEFT
        if (myPosition.getRow() <= 7 && myPosition.getColumn() >= 2) {
            if (currBoard[myPosition.getRow() + 1][myPosition.getColumn() - 1] == null) {
                ChessPosition newMove = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));

            }
            else if (currBoard[myPosition.getRow() + 1][myPosition.getColumn() - 1].pieceColor != this.pieceColor){
                ChessPosition newMove = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
                possibleMoves.add(new ChessMove(myPosition, newMove, null));
            }
        }
    }

    public void pawnMoves(ChessPosition myPosition,
                          ChessPiece[][] currBoard, Collection<ChessMove> possibleMoves){
        //WHITE MOVES THAT GO UP THE BOARD
        if (this.pieceColor == ChessGame.TeamColor.WHITE) {
            int nextRow = myPosition.getRow() + 1;
            if (currBoard[nextRow][myPosition.getColumn()] == null){
                if (nextRow == 8){
                    ChessPosition newMove = new ChessPosition(nextRow, myPosition.getColumn());
                    possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.QUEEN));
                    possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.ROOK));
                }
                else {
                    ChessPosition newMove = new ChessPosition(nextRow, myPosition.getColumn());
                    possibleMoves.add(new ChessMove(myPosition, newMove, null));
                    //IF ON START POSITION, IT CAN MOVE 2!!!
                    if (myPosition.getRow() == 2){
                        if (currBoard[myPosition.getRow() + 2][myPosition.getColumn()] == null) {
                            ChessPosition newMove2 = new ChessPosition(nextRow + 1, myPosition.getColumn());
                            possibleMoves.add(new ChessMove(myPosition, newMove2, null));
                        }
                    }
                }
            }

            //CAPTURE UP RIGHT
            if (myPosition.getColumn() <= 7) {
                if (currBoard[nextRow][myPosition.getColumn() + 1] != null) {
                    if (currBoard[nextRow][myPosition.getColumn() + 1].getTeamColor() != this.pieceColor){
                        if (nextRow == 8){
                            ChessPosition newMove = new ChessPosition(nextRow, myPosition.getColumn() + 1);
                            possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.QUEEN));
                            possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.BISHOP));
                            possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.KNIGHT));
                            possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.ROOK));
                        }
                        else {
                            ChessPosition newMove = new ChessPosition(nextRow, myPosition.getColumn() + 1);
                            possibleMoves.add(new ChessMove(myPosition, newMove, null));
                        }
                    }
                }
            }
            //CAPTURE UP LEFT
            if (myPosition.getColumn() >= 2) {
                if (currBoard[nextRow][myPosition.getColumn() - 1] != null) {
                    if (currBoard[nextRow][myPosition.getColumn() - 1].getTeamColor() != this.pieceColor){
                        if (nextRow == 8){
                            ChessPosition newMove = new ChessPosition(nextRow, myPosition.getColumn() - 1);
                            possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.QUEEN));
                            possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.BISHOP));
                            possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.KNIGHT));
                            possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.ROOK));
                        }
                        else {
                            ChessPosition newMove = new ChessPosition(nextRow, myPosition.getColumn() - 1);
                            possibleMoves.add(new ChessMove(myPosition, newMove, null));
                        }
                    }
                }
            }
        }

        //BLACK MOVES THAT GO DOWN THE BOARD
        if (this.pieceColor == ChessGame.TeamColor.BLACK) {
            int nextRow = myPosition.getRow() - 1;
            if (currBoard[nextRow][myPosition.getColumn()] == null){
                if (nextRow == 1){
                    ChessPosition newMove = new ChessPosition(nextRow, myPosition.getColumn());
                    possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.QUEEN));
                    possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.ROOK));
                }
                else {
                    ChessPosition newMove = new ChessPosition(nextRow, myPosition.getColumn());
                    possibleMoves.add(new ChessMove(myPosition, newMove, null));
                    //IF ON START POSITION, IT CAN MOVE 2!!!
                    if (myPosition.getRow() == 7){
                        if (currBoard[myPosition.getRow() - 2][myPosition.getColumn()] == null) {
                            ChessPosition newMove2 = new ChessPosition(nextRow - 1, myPosition.getColumn());
                            possibleMoves.add(new ChessMove(myPosition, newMove2, null));
                        }
                    }
                }
            }

            //CAPTURE DOWN RIGHT
            if (myPosition.getColumn() <= 7) {
                if (currBoard[nextRow][myPosition.getColumn() + 1] != null) {
                    if (currBoard[nextRow][myPosition.getColumn() + 1].getTeamColor() != this.pieceColor){
                        if (nextRow == 1){
                            ChessPosition newMove = new ChessPosition(nextRow, myPosition.getColumn() + 1);
                            possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.QUEEN));
                            possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.BISHOP));
                            possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.KNIGHT));
                            possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.ROOK));
                        }
                        else {
                            ChessPosition newMove = new ChessPosition(nextRow, myPosition.getColumn() + 1);
                            possibleMoves.add(new ChessMove(myPosition, newMove, null));
                        }
                    }
                }
            }
            //CAPTURE UP LEFT
            if (myPosition.getColumn() >= 2) {
                if (currBoard[nextRow][myPosition.getColumn() - 1] != null) {
                    if (currBoard[nextRow][myPosition.getColumn() - 1].getTeamColor() != this.pieceColor){
                        if (nextRow == 1){
                            ChessPosition newMove = new ChessPosition(nextRow, myPosition.getColumn() - 1);
                            possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.QUEEN));
                            possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.BISHOP));
                            possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.KNIGHT));
                            possibleMoves.add(new ChessMove(myPosition, newMove, pieceType.ROOK));
                        }
                        else {
                            ChessPosition newMove = new ChessPosition(nextRow, myPosition.getColumn() - 1);
                            possibleMoves.add(new ChessMove(myPosition, newMove, null));
                        }
                    }
                }
            }
        }
    }
}
