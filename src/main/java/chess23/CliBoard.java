package chess23;

import java.nio.file.Path;
import java.util.Scanner;

public class CliBoard {

    public CliBoard(Pieces pieces) {

        boolean exit = false;
        COLOUR turn = COLOUR.W;
        Scanner sc = new Scanner(System.in);
        StringBuilder gameString = new StringBuilder();
        int numberOfTurns = 0;

        System.out.println(Boards.displayBoard(pieces));

        while (!exit) {

            String[] move = Move.moveQuery(sc);

            if (move[0].equals("exit")) {
                System.out.println("Exiting the game. Thanks for playing!");
                break;
            }
            else if (move[0].equals("save")) {
                Path filePath = ChessIO.fileQuery(sc);
                if (ChessIO.saveGame(gameString.toString(), filePath))
                    System.out.println("Game saved successfully on path " + filePath);
                else
                    System.out.println("There was an error saving the file on the path " + filePath);
                continue;
            }

            if (!Coordinate.inBoard(new Coordinate(move[0])) || !Coordinate.inBoard(new Coordinate(move[1]))) {
                System.out.println("At least one of the given coordinates isn't in the board. Please try again!");
            }
            else {
                Coordinate origin = new Coordinate(move[0]);
                Coordinate destination = new Coordinate(move[1]);

                Piece piece = pieces.getPiece(origin);

                if (piece.equals(Piece.emptyPiece)) {
                    System.out.println("The origin coordinate doesn't contain a piece. Please try again!");
                } else {
                    if (piece.isValidMove(destination, turn)) {
                        pieces.makeMove(destination, piece);
                        if (turn == COLOUR.W) {
                            numberOfTurns++;
                            gameString.append(numberOfTurns).append(". ").append(ChessIO.moveString(pieces, destination, piece)).append(" ");
                        }
                        else {
                            gameString.append(ChessIO.moveString(pieces, destination, piece)).append(" ");
                        }
                        System.out.println(Boards.displayBoard(pieces));
                        if (pieces.isMate(COLOUR.not(turn))) {
                            System.out.println(turn + " win.");
                            exit = true;
                        }
                        else if (pieces.isStalemate(turn)) {
                            System.out.println("It's a draw by stalemate.");
                            exit = true;
                        }
                        else if (pieces.isDraw()){
                            System.out.println("It's a draw.");
                            exit = true;
                        }
                        else{
                            turn = COLOUR.not(turn);
                            System.out.println(turn.toString() + " to move.");
                        }
                    } else {
                        System.out.println("Invalid move provided.");
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Pieces pieces = new Pieces();
        pieces.setGUIGame(false);
        new CliBoard(pieces);
    }

}
