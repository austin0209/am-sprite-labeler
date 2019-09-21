# am-sprite-labeler
A game development utility written with the LibGDX framework for labeling and loading in sprite sheets with ununiform sprite dimensions.
This application spits out a CSV that your program can read to load in sprites.

## Usage
You can download a runnable jar from the repo, or you can click [here](https://github.com/austin0209/am-sprite-labeler/raw/master/AM%20Sprite%20Labeler.jar). Note that this program has only been tested to work with Java versions 1.8-1.12.
Select each sprite with a rectangle and give it a name. Use the function keys to save/read files.

## Output:
Note that the program creates a folder in the active directory (the directory you run the JAR in) named "Sprite Sheet Saves." All output files are written to this folder, and all CSV's you want to read must be in this folder.

Each entry of the CSV will be formatted as follows:
>`x,y,width,height,name`

The x and y are positions of the sprite relative to the top left. The width and height are specified by each selection rectangle. The name is the name given to the sprite by the user.

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

