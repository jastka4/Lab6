package tb.sockets.server;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private static int boardLengthX;
    private static int boardLengthY;

    private List<Battleship> battleships = new ArrayList<>();
    private static final int[] shipLengths = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};

    public Board(int boardLengthX, int boardLengthY)
    {
        this.boardLengthX = boardLengthX;
        this.boardLengthY = boardLengthY;
        this.addShips();
    }

    //TODO: fix generating ships
    private void addShips() {
        for (int length : shipLengths) {
            boolean added = false;
            Battleship battleship = new Battleship(length);
            while (!added) {
                int x = (int) (boardLengthX * Math.random());
                int y = (int) (boardLengthY * Math.random());
                boolean vertical = ((int) (10 * Math.random())) % 2 == 0;

                if (vertical) {
                    // Check for vertical space
                    boolean hasSpace = true;
                    for (int i = 0; i < length; i++){
                        if (y + i >= boardLengthY) {
                            hasSpace = false;
                            break;
                        }
                        if (!checkIfHasSpace(x, y + i)) {
                        hasSpace = false;
                        break;
                        }
                    }
                    if (!hasSpace) {
                        // No room there, check again
                        continue;
                    }
                    for (int i = 0; i < length; i++) {
                        //System.out.println(new Point(x, y + i));
                        battleship.addCoordinate(new Point(x, y + i));
                    }
                    added = true;
                } else {
                    // Check for horizontal space
                    boolean hasSpace = true;
                    for (int i = 0; i < length; i++) {
                        if (x + i >= boardLengthX) {
                            hasSpace = false;
                            break;
                        }
                        if(!checkIfHasSpace(x + i, y))
                        {
                            hasSpace = false;
                            break;
                        }
                    }
                    if (!hasSpace) {
                        // No room there, check again
                        continue;
                    }
                    for (int i = 0; i < length; i++) {
                        //System.out.println(new Point(x + i, y));
                        battleship.addCoordinate(new Point(x + i, y));
                    }
                    added = true;
                }
            }
            battleships.add(battleship);
        }
    }

    private boolean checkIfCoordinatesUsed(int x, int y)
    {
        for(Battleship battleship: battleships)
        {
            if(battleship.getCoordinates().contains(new Point(x,y)))
            {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfHasSpace(int x, int y)
    {
        for(int i = -1; i <= 1; i++)
        {
            for(int j = -1; j <= 1; j++)
            {
                if(checkIfCoordinatesUsed(x + i, y + j))
                {
                    return false;
                }
            }
        }
        return true;
    }

    public String getBoardAsString()
    {
        String boardAsString = "";
        for(int i = 0; i < boardLengthX; i++)
        {
            for (int j = 0; j < boardLengthY; j++)
            {
                boardAsString += (checkIfCoordinatesUsed(i,j)? "1" : "0");
            }
        }

        //test of the generated string
/*        for(int i = 0; i < 100; i++)
        {
            if(i % 10 == 0){
                System.out.print("\n");
            }
            System.out.print(boardAsString.charAt(i));
        }
            System.out.print('\n');*/
        return boardAsString;
    }

    public String getIfShipMissedHitOrSunken(int x, int y)
    {
        for(Battleship battleship: battleships)
        {
            if(battleship.getCoordinates().contains(new Point(x,y)))
            {
                battleship.addHit();
                if(battleship.checkIfSunken())
                {
                    return "s" + battleship.getCoordinatesAsString();
                }
                return "h" + x + y;
            }
        }
        return "m"+ x + y;
    }

    public boolean checkIfAllShipsSunken()
    {
        for(Battleship battleship: battleships)
        {
            if(battleship.checkIfSunken() == false)
            {
                return false;
            }
        }
        return true;
    }
}
