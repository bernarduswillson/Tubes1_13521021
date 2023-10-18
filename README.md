# Adversarial Adjacency-Strategy-Game with AI Bot

### Introduction
The “Adjacency” program was designed as an interactive and strategic board game that is played between two players. Using X’s and O’s on an 8x8 board outputted on a computer screen, the objective of the game is for a player to get more X’s than O’s in total or vice versa after a specific number of rounds.

The initial game starts with 4 X’s in the bottom left corner and 4 O’s in the top right corner.  The first player will begin by placing an X on an empty square (through clicking).  In turn, all O’s that are on adjacent squares are then replaced with X’s.  Play will continue with the second player taking his turn by placing an O on an empty square.  In turn, all X’s that are on adjacent squares are then replaced with O’s.

The game will continue until there are no more rounds left to play.  In the end, the player with the greater number of letters is the winner of the game.

The program also includes an AI Bot that can play against the player. The AI Bot can use minimax algorithm with alpha-beta pruning, simulated annealing algorithm, and genetic algorithm to determine the best move to make. The AI Bot can be set to play first or second. The AI Bot can also be set to battle anopther AI Bot.


### Installation (IntelliJ)
To get started, please install the latest version of the Java Development Kit (JDK) and please install a Java IDE such as <a href="https://www.jetbrains.com/idea/">IntelliJ</a>. Please note that the deployment instructions below use IntelliJ as the IDE.


### Deployment (IntelliJ)
1. Clone the repository through Git by running the following command:<br>
`https://github.com/bernarduswillson/Tubes1_13521021.git`, or simply download the repository.
2. Open the repository folder through IntelliJ.
3. Set up the JDK by going to File -> Project Structure -> Project tab. In the Project tab, go to Project SDK, click New, and browse to the location of your JDK folder.
4. NOTE: JavaFX has been removed starting from JDK 11, and it is now a standalone module. The JavaFX files needed to set up the Adjacency program are located in the repository folder itself. To set it up, go to File -> Project Structure -> Libraries tab.  In the Libraries tab, press the + button, browse to the "javafx-sdk/lib" folder in the repository, and add it to the list of libraries.
5. IMPORTANT: Go to Run -> Edit Configurations, and go to the VM options. In this line, please add in the full path to the lib folder of the javafx-sdk folder on your own computer, and then add the following line <br> `--add-modules=javafx.controls,javafx.fxml`.<br><br> 
For example, I added the following line to my VM options: `--module-path "C:\Ken's Work\CS Side Projects\Tubes1_13521021\javafx-sdk\lib" --add-modules=javafx.controls,javafx.fxml`
6. Open the Main class in the IntelliJ file interface.

### Deployment (Windows)
1. Make sure you have at least JDK 11 installed
2. Clone the repo and cd to the folder
```
git clone https://github.com/bernarduswillson/Tubes1_13521021.git
cd Tubes1_13521021
```
3. Download JavaFX 17 for windows
4. Extract the zip file and rename the folder to `javafx-sdk`
5. Replace the `javafx-sdk` folder in the repo with the one you just downloaded
6. Run the program
```
./run.bat
```

<hr>

### Program Instructions
1. Run the Main class to load the program, and the input window below will pop up. Input the names of Player 1 (X) and Player 2 (O) into their respective text fields.
Then, select the number of rounds (a number between 2 and 28) to play using the dropdown menu.
You can make the Bot start first.
2. Click Play, and the gameboard and scoreboard window will load. Player 1 (X) starts the game by clicking on an empty button. Any adjacent O’s will change to X's as a result. 
3. Then, Player 2 (O) has their turn by also clicking on an empty button. Any adjacent X’s will change to O's as a result. NOTE: This process is counted as 1 round (Player and Bot both taking their turns).
4. The game will continue until there are no more rounds left to play. In the end, the player with the greater number of letters is the winner of the game.
<hr>

### Members

| Name | NIM |
| --- | --- |
| Bernardus Willson | 13521021 |
| Raditya Naufal | 13521022 |
| Kenny Benaya | 13521023 |
| Kartini Copa | 13521026 |






