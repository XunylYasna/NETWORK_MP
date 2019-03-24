package sample;

public class Move {

    String gameString;

    public Move(String gameString, String move) {
        this.gameString = gameString;
        String[] process = move.split(" ");
        String position = process[1].substring(0,6);


        if(position.equals("before")){
            this.gameString = process[0] + this.gameString;
        }

        else{
            this.gameString = this.gameString + process[0];
        }
    }

    public String getGameString() {
        return gameString;
    }
}
