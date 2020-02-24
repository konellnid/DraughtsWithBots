# DraughtsWithBots
Play draughts with adjustable bots (to be added, still working on it)
##### Random bot
Picks a random move from all possible moves

##### Random piece bot
Picks a random piece, then picks a random move from possible moves for this piece

##### Minimax bot
Using minimax algorithm, picks the move that leads to best possible found position.
User can choose the depth of the search (depth=1 -> bot 'sees' all positions one move away from current position).
Exceeding the depth when beating is found option allows the bot to check positions as long as beating is found (beatings often happen one after another as if both players equally exchange pieces; also beatings limit the amount of possible moves, so it doesn't add that many positions to check).
PositionRater is used by Minimax bot to evaluate how good current position is. Draw situation is score zero, positive score means white has better position, negative - black has better position. Points are given for:
- each checker (10 by default)
- each king (20 by default)
- controlling the main diagonal with king - situation, when one player has at least one king on main diagonal, and the other player has none (having king on main diagonal allows to win 3v1 king situation using Petrov's triangle)
- for every checker, one point per row closer to promotion line (those checkers are closer to become kings)
Note that the rating is just a simple evaluation and may be often misleading.
Minimax bot checks EVERY position to given depth. Because of this, bot may slow down with many kings on the board, especially on 12x12 board. 

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
