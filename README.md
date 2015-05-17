# vmaxflow

Implementation of the Push-Relabel Method 
for the Maximum Flow Problem

    Usage: java -jar vmaxflow.jar <filename> <outputfile>

Given Graph G = (V, A) with capacity function c(a) > 0 for all arc a in A.
Output is a flow function f over A, which is maximum.

Content example of an input file:

    # Node u, Node v, Capacity of arc uv
    0, 1, 15
    0, 3, 4
    1, 2, 12
    2, 3, 3
    2, 5, 7
    3, 4, 10
    4, 1, 5
    4, 5, 10


The node `u` in the first input line will be interpreted as the source vertex.
This is the node `0` in the example.
The node `v` in the last input line will be interpreted as the sink vertex.
This is the node `5` in the example.
All lines beginning with the character '#' will be interpreted as comment lines.

Content of output file:

    # Node u, Node v, Flow on arc uv, Capacity of arc uv
    0, 1, 10, 15
    0, 3, 4, 4
    1, 2, 10, 12
    2, 3, 3, 3
    2, 5, 7, 7
    3, 4, 7, 10
    4, 1, 0, 5
    4, 5, 7, 10

The value of the flow on an arc `uv` will be placed in the third column.

For an example of the code usage see the file `SimpleExample.java` in `junit` folder.