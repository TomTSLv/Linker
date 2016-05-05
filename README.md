#################################
#                               #
# OS Lab 1 Running Instructions # 
#                               #
#################################

Name: Tianshu Lv
NetID: tl1443

The Lab contains a Linker program which resembles linker in operating systems. It basically imports the definition list, user list and program text of each module and exports the updated symbol table of external variables and the updated address map.

The following is the instruction for compiling and running the program:

1. Download the OSLab1 directory from access.cims.nyu.edu or from NYU Classes.
2. Open the terminal and use cd command to enter the directory which contains all Java files. (e.g.: cd /Desktop/OSLab1)
3. Compile the Java project with command: javac Linker.java
4. Run the program with command: java Linker
   Then you can see terminal print out "Please enter the input:" to ask for input.
   Type the whole input below and after that, add a space and a period after the whole input and press Enter.
   The following is an example of a proper input:
   
   (e.g.: 
   Please enter the input:

   1 xy 2
   2 z 2 -1 xy 4 -1
   5 10043  56781  20004  80023  70014
   0
   1 z 1 2 3 -1
   6 80013  10004  10004  30004  10023  10102
   0
   1 z 1 -1
   2 50013  40004
   1 z 2
   2 xy 2 -1 z 1 -1
   3 80002  10014  20004 .
   )

5. The proper result will print out in terminal.
