# am-sprite-labeler
A game development utility written with the LibGDX framework for labeling and loading in sprite sheets with ununiform sprite dimensions.
This application spits out a CSV that your program can read to load in sprites.

To use it, select each sprite with a rectangle and give it a name. Use the function keys to save/read files.

## Output:
Each entry of the CSV will be formatted as follows:
>`x,y,width,height,name`

The x and y are positions of the srite relative to the top left. The width and height are specified by each selection rectangle. The name is the name given to the sprite by the user.

This output can be parsed by your code to determine where each sprite is in a sprite sheet.

## Controls:
ws - zoom in/out

arrow keys - move camera

f1 - write to file (csv)

f2 - read from file (should be a csv in a directory called "Sprite Sheet Saves" which should be in the the active directory)

f3 - open sprite sheet

f11 - fullscreen

left click - select rectangle/resize if selected

right click - create rectangle (click and drag)

enter - rename selected rectangle

L - show/hide labels

del - delete selected rectangle

