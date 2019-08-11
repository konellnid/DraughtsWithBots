# DraughtsWithBots
Play draughts with adjustable bots (to be added, still working on it)
##### Random bot
Picks a random move from all possible moves

##### Random piece bot
Picks a random piece, then picks a random move from possible moves for this piece

___

## Naming convention 
###### (this needs to be unified around the code)
Those three are really mixed around the code:

* position - connected with the class Position
* tile - connected with the class TileWithPiece
* tile number - each tile has it's tileNumber, Move operates on tile numbers

_______________
* piece - checker/king
* checker - not promoted piece, every starting piece is a checker
* king - promoted piece, checker promotes to king after reaching the last row (for given color)
