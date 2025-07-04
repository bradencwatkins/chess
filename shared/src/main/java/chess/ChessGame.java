package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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
    private boolean gameOver = false;
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

    public boolean isOver() {
        return gameOver;
    }
    public void setGameOver(boolean over) {
        this.gameOver = over;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(chessBoard, chessGame.chessBoard) && currentTurn == chessGame.currentTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chessBoard, currentTurn);
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

        if (movingPiece == null) {
            throw new InvalidMoveException();
        }

        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        ChessGame.TeamColor pieceColor = movingPiece.getTeamColor();

        if (!validMoves.contains(move) || pieceColor != currentTurn) {
            throw new InvalidMoveException();
        }

        // Temporarily apply the move
        ChessPiece captured = chessBoard.getPiece(move.getEndPosition());
        chessBoard.removePiece(move.getStartPosition());
        chessBoard.addPiece(move.getEndPosition(),
                (movingPiece.getPieceType() == ChessPiece.PieceType.PAWN &&
                        (move.getEndPosition().getRow() == 1 || move.getEndPosition().getRow() == 8))
                        ? new ChessPiece(pieceColor, move.getPromotionPiece())
                        : movingPiece
        );

        boolean stillInCheck = isInCheck(pieceColor);

        // Undo the move
        chessBoard.removePiece(move.getEndPosition());
        if (captured != null) {
            chessBoard.addPiece(move.getEndPosition(), captured);
        }
        chessBoard.addPiece(move.getStartPosition(), movingPiece);

        if (stillInCheck) {
            throw new InvalidMoveException(); // Illegal because it leaves player in check
        }

        // Apply the move for real
        if (movingPiece.getPieceType() == ChessPiece.PieceType.PAWN &&
                (move.getEndPosition().getRow() == 1 || move.getEndPosition().getRow() == 8)) {
            chessBoard.addPiece(move.getEndPosition(), new ChessPiece(pieceColor, move.getPromotionPiece()));
        } else {
            chessBoard.addPiece(move.getEndPosition(), movingPiece);
        }
        chessBoard.removePiece(move.getStartPosition());

        setTeamTurn(currentTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
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
        Collection<ChessPosition> friendlyPieces = chessBoard.getAllPositions(teamColor);
        Collection<ChessMove> allMoves = new ArrayList<>();

        for (ChessPosition position : friendlyPieces){
            allMoves.addAll(validMoves(position));
        }

        if (allMoves.isEmpty() && isInCheck(teamColor)){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessPosition> friendlyPieces = chessBoard.getAllPositions(teamColor);
        Collection<ChessMove> allMoves = new ArrayList<>();

        for (ChessPosition position : friendlyPieces){
            allMoves.addAll(validMoves(position));
        }

        if (allMoves.isEmpty() && !isInCheck(teamColor)){
            return true;
        }
        else {
            return false;
        }
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
