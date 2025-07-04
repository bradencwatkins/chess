package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    //REMOVES PIECE AT A CERTAIN SQUARE
    public void removePiece(ChessPosition position){
        squares[position.getRow() - 1][position.getColumn() - 1] = null;
    }

    public ChessPosition returnKingPosition(ChessGame.TeamColor team){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (squares[i][j] != null) {
                    if (squares[i][j].getPieceType() == ChessPiece.PieceType.KING &&
                            squares[i][j].getTeamColor() == team) {
                        return new ChessPosition(i + 1, j + 1);
                    }
                }
            }
        }
        return null;
    }

    public Collection<ChessPosition> getAllPositions(ChessGame.TeamColor team){
        Collection<ChessPosition> piecePositions = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (squares[i][j] != null) {
                    if (squares[i][j].getTeamColor() == team) {
                        piecePositions.add(new ChessPosition(i + 1, j + 1));
                    }
                }
            }
        }

        return piecePositions;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }



    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        //SET WHITE PIECES
        squares[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        squares[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        squares[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        squares[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        squares[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        squares[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        squares[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        squares[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        //SET WHITE PAWNS
        for (int i = 0; i < 8; i++){
            squares[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }

        //SET BLACK PIECES
        squares[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        squares[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        squares[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        squares[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        squares[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        squares[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        squares[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        squares[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        //SET BLACK PAWNS
        for (int i = 0; i < 8; i++){
            squares[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
    }

    //FLIPS THE BOARD ON VERTICAL AXIS
    public void flipBoardVerticalAxis() {
        for (int row = 0; row < squares.length; row++) {
            for (int col = 0; col < squares[row].length / 2; col++) {
                ChessPiece temp = squares[row][col];
                squares[row][col] = squares[row][7 - col];
                squares[row][7 - col] = temp;
            }
        }
    }


    //REVERSES THE BOARD FOR BLACK PIECE USERS
    public void reverseBoard() {
        for (int row = 0; row < squares.length / 2; row++) {
            for (int col = 0; col < squares[row].length; col++) {
                ChessPiece temp = squares[row][col];
                squares[row][col] = squares[7 - row][7 - col];
                squares[7 - row][7 - col] = temp;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
}
