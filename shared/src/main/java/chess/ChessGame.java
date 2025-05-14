package chess;

import java.util.ArrayList;
import java.util.Collection;

//

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard chessBoard;
    private TeamColor currentTurn;
    public ChessGame() {
        currentTurn = TeamColor.WHITE;
        chessBoard = new ChessBoard();
        chessBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> possibleMoves = chessBoard.getPiece(startPosition).pieceMoves(chessBoard, startPosition);
        Collection<ChessMove> endMoves = new ArrayList<>();

        for (ChessMove move : possibleMoves){
            ChessPiece movingPiece = chessBoard.getPiece(startPosition);
            ChessPosition endPosition = move.getEndPosition();
            ChessPiece capturedPiece = chessBoard.getPiece(endPosition);

            //SIMULATE A MOVE
            chessBoard.removePiece(startPosition);
            chessBoard.addPiece(endPosition, movingPiece);

            if (!isInCheck(movingPiece.getTeamColor())){
                endMoves.add(move);
            }

            //UNDO THE MOVE
            chessBoard.removePiece(endPosition);
            chessBoard.addPiece(startPosition, movingPiece);
            if (capturedPiece != null) {
                chessBoard.addPiece(endPosition, capturedPiece);
            }
        }


        return endMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece movingPiece = chessBoard.getPiece(move.getStartPosition());

        //CHECK IF THE START POSITION HAS A PIECE
        if (movingPiece == null){
            throw new InvalidMoveException();
        }

        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        ChessGame.TeamColor pieceColor = movingPiece.getTeamColor();

        //IF MOVE IS A VALID MOVE AND IT IS THE RIGHT COLOR, MAKE MOVE
        //KING ALSO CANNOT BE IN CHECK
        if (validMoves.contains(move) && pieceColor == currentTurn &&
            !isInCheck(movingPiece.getTeamColor())){
            //HANDLE CASE WHERE PAWN IS PROMOTING
            if (movingPiece.getPieceType() == ChessPiece.PieceType.PAWN &&
                    (move.getEndPosition().getRow() == 8 || move.getEndPosition().getRow() == 1)){
                chessBoard.addPiece(move.getEndPosition(), new ChessPiece(movingPiece.getTeamColor(), move.getPromotionPiece()));
                chessBoard.removePiece(move.getStartPosition());
            }
            else {
                chessBoard.addPiece(move.getEndPosition(), movingPiece);
                chessBoard.removePiece(move.getStartPosition());
            }
            setTeamTurn(currentTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
        }
        else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = chessBoard.returnKingPosition(teamColor);
        TeamColor oppColor;
        if (teamColor == TeamColor.WHITE){
            oppColor = TeamColor.BLACK;
        }
        else {
            oppColor = TeamColor.WHITE;
        }

        //GET ALL ENEMY PIECE POSITIONS, THEN GET ALL POSSIBLE MOVES FOR THOSE PIECES
        Collection<ChessPosition> enemyPieces = chessBoard.getAllPositions(oppColor);
        Collection<ChessMove> enemyPieceMoves = new ArrayList<>();
        //ALL END POSITIONS FOR ALL MOVES FROM ALL ENEMY PIECES
        Collection<ChessPosition> targets = new ArrayList<>();

        for (ChessPosition position : enemyPieces){
            enemyPieceMoves.addAll(chessBoard.getPiece(position).pieceMoves(chessBoard, position));
        }
        for (ChessMove move : enemyPieceMoves){
            targets.add(move.getEndPosition());
        }

        if (targets.contains(kingPosition)){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
        //RETURN TRUE IF NO POSSIBLE MUVES PLUS IN CHECK
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
        //RETURN TRUE IF NO POSSIBLE MUVES PLUS NOT IN CHECK
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessBoard;
    }
}
