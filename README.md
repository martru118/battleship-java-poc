# CSCI_2020U_Final_Project

Yong-Joon Baek, Cheyenne Carrier, Annanya Saha, Martin Troung, Michael Loo
Yong-Joon,CheyenneC-ontechu,anannyasaha,100708410-uoit,Michael-Loo-2000

repo url: https://github.com/CheyenneC-ontechu/CSCI_2020U_Final_Project
clone/download link: https://github.com/CheyenneC-ontechu/CSCI_2020U_Final_Project.git

## Contributions:
* __Gameplay__: Cheyenne, Yong-Joon, Michael
* __File I/O__: Anannya, Martin, Cheyenne
* __Gradle__: Yong-Joon
* __Multithreading__: Yong-Joon
* __Debugging & UI__: Everyone
* __README__: Martin, Michael

## Gradle Instructions:
`gradle build`

Run server first, then `gradle run`.

## Instructions for Battle Ship:
1. Launch the Game
..* If you wish to quit the game at any moment follow the instructions below in closing the popup and press the "EXIT AND SAVE THE GAME" button, which will save your current status of the game on the "current game.txt" file and close the application
..* If you wish to continue a previous game from last time (if you saved, instructions above) press the "RESUME THE PREVIOUS GAME" button
2. Press the "Start the game" button
3. Enter into the text field the coordinates you want to fire upon in the cormat of @# [@=row number and #=column number]
..* If you put coordinates that are invalid(not one the board or already hit) you will have to cler the old coordinates out and enter new       ones.
..* After the popup has come up, if you wish to close the popup for any reason, first press the "cancel" button and then the "ok" button
4. Continue to play until either you or the COMPUTER opponent sinks all the others ships
5. If you lose, you recieve no score
6. If you win you will get a score, which is how many turns you took (the lower the score the better)
7. You can then enter your name into the text field with the "Enter Name" and then press the "SEND SCORE" button to send your score to the server, which will then store the score in the "scores.csv" file
8. The game will then display to you the lowest score so far recorded (lower the better = took less turns to win)
9. There is a clock going on in the background when the game starts.
