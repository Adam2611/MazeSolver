/*PathFinder Program - this program creates a maze, either pseudo-randomly or from a file, and determines if there is a possible path from the start to the exit. 
 * If yes, it replaces the path with + signs to mark the path. 
 * It also validates user input, meaning that it will not crash easily if invalid input is entered
 * Furthermore, it gives the user the option of creating another maze if the first is not solvable



import java.util.*;
import java.io.*;                //importing relevant libraries

public class PathFinder{
  
  static Scanner scanner = new Scanner (System.in);
  static int counter = 0; 
  static int [] xvalues; //coordinate arrays          //static variables accessable for the entire class
  static int [] yvalues; 
  static String  MazeArray [][];
  
  public static void main (String[] args) throws Exception{ //MAIN METHOD
    
    int fileOrRandom=3;                               //declaring variables for main method 
    boolean checked=false; 
    
    Welcome();                                       //calls welcome method that outputs relevant information at the beginning
    
    System.out.println("Would you like to input the maze randomly or from a file? Type 1 or 2 respectively.");  //from file or randomly made
    
    while(fileOrRandom!=1&&fileOrRandom!=2){
      
      try{ 
        fileOrRandom = scanner.nextInt();                                     //user validaiton
        checked = false;
      }catch (Exception e){
        System.out.println("Please enter 1 or 2 respectively.");               //try catch block to catch if user enters a non integer number
        checked = true;
        scanner.nextLine();
      }
      if(!checked&&fileOrRandom!=1&&fileOrRandom!=2){                                  //if the number is not one or two AND it did not go through the catch exception
        System.out.println("Please enter 1 or 2 respectively.");
      }
    }
    
    if(fileOrRandom==1){                  //Random Maze          
      preRandom();                          //send it to the preRandom method 
    }
    
    else if(fileOrRandom==2){               //FILE MAZE 
      getFile();                              //send it to the getFile method
    }
    
  }//end of main method
  
//=========================================================================================================================================================================================================================================
  
  public static void Welcome (){                     //this method outputs welcome information and possibly other relevant information to user
    System.out.println("Welcome to the Maze Pathfinder Program! \n\nThis program can either create a random maze or accept input from a file to create one. Then the program will find a path from the start point to the end point in the maze.\nEnjoy! ");
    System.out.println("\n");
  }
  //========================================================================================================================================================================================================================================
  public static void preRandom() throws Exception{       //this method gets the rows and columns and validates them; then it sends it off to the fillRandom method       
    boolean validInput = false;        //to see if the input is valid or not
    int rows=0, columns=0;
    
    System.out.println("\nPlease enter the number of rows you want for the maze."); //asks for input for rows
    
    while(!validInput||rows<=2){                             //while validInput is false OR the number of rows is less than 2, it will loop
      rows = 3;                     //set value temporarily to number greater than 2 each time
      try{
        rows = scanner.nextInt();
        validInput = true;
      }catch(Exception e){                                 //catches exception if input is not an integer
        System.out.println("Please enter a valid integer.");
        validInput = false;                       //changes boolean condition
        scanner.nextLine();                      //clears the scanner
      }
      if(rows<=2&&rows>=0){
        System.out.println("Please try again. The maze must have more than 2 rows. Thank you.");  //maze must have more than 2 rows or else the starting point will be on the border
      }
      else if(rows<0){
        System.out.println("Please enter a positive integer.");
      }
    }
    
    System.out.println("\nPlease enter the number of columns you want for the maze."); //asks for input for columns
    
    validInput = false;
    while(!validInput||columns<=2){
      columns = 3;                      //set value to number greater than 2 temporarily
      try{
        columns = scanner.nextInt();                            //validating and reprompting
        validInput = true;
      }catch(Exception e){
        System.out.println("Please enter a valid number.");
        validInput = false;
        scanner.nextLine();
      }
      if(columns<=2&&columns>=0){
        System.out.println("Please try again. The maze must have more than 2 columns. Thank you.");
      }
      else if(columns<0){
        System.out.println("Please enter a positive integer.");
      }
    }
    
    xvalues = new int [(rows-2)*(columns-2)];         //setting the coordinate arrays to the max number of steps possible in the maze
    yvalues = new int [(rows-2)*(columns-2)];
    
    MazeArray = new String [rows][columns];             //creating the maze array and sending it off to fillRandom
    
    fillRandom(MazeArray, rows, columns);   
  }
  //========================================================================================================================================================================================================================================
  
  public static void getFile() throws Exception{       //this method gets the filename from the user and validates it; then it sends it off to the FileArray
    scanner.nextLine();
    String filename;
    int rows, columns;
    System.out.println("Please enter a file name. Thank you.");
    filename = scanner.nextLine();
    
    File file = new java.io.File("temp");              //setting the file instance to temp at first
    do{
      try{
        
        file = new java.io.File(filename);                 //recreating the file instance named 'filename'
        Scanner fileScanner = new Scanner(file);              //creating its scanner
        
        rows = fileScanner.nextInt();                  //getting input
        columns = fileScanner.nextInt();
        
        xvalues = new int [(rows-2)*(columns-2)];
        yvalues = new int [(rows-2)*(columns-2)];          //then sets the coordinate arrays to the max number of steps based on the rows and columns
        
        MazeArray = new String [rows][columns];            //initializing array before passing it to method
        fillFile(MazeArray, file, rows, columns);
        
      } catch(FileNotFoundException ex){                     //using a try catch block to make sure a valid file name is entered
        System.out.println("Please enter a valid file name. Thank you.");
        filename = scanner.nextLine();
      }      
    }while(!file.exists());                               //runs the loop if the file does not exist
  }  
//==========================================================================================================================================================================================================================================
  
  public static void fillRandom(String [][] array, int rows, int columns) throws Exception{     //this method creates random values (B, O, S, X) inside the maze
    int randomnum; 
    String [][] copyArray = new String[rows][columns];
    int XorY;
    int startPointX; 
    int startPointY;
    int endPointX=0;
    int endPointY=0;
    int decider = 0;                                         //shows that the pathMethod was called from this method
    int x = 0;
    int y = 0;
    
    for(int i = 0; i<rows; i++){
      for(int j = 0; j<columns; j++){
        randomnum = (int) (Math.random()*(2));          //filling all spaces randomly with either 'b' or 'o'
        if(randomnum == 0){
          array[i][j] = "B";
        }
        else if(randomnum == 1){
          array[i][j] = "O";
        }
      }
    }
    for(int i = 0; i<columns; i++){                    
      array[0][i] = "B";
    }
    for(int i = 0; i<columns; i++){                     //setting borders to 'b';
      array[rows-1][i] = "B";
    }
    for(int i = 0; i<rows; i++){                     
      array[i][0] = "B";
    }
    for(int i = 0; i<rows; i++){                    
      array[i][columns-1] = "B";
    }
    
    startPointY = (int) (Math.random()*(rows-2)+1);                      //startPoint x and y values to not on the border
    startPointX = (int) (Math.random()*(columns-2)+1);
    
    array[startPointY][startPointX] = "S";
    
    XorY = (int) (Math.random()*(2));
    
    if(XorY==0){
      endPointY = (int)(Math.random()*(rows-2)+1);  
      endPointX = 0;
    }                                                                  //setting endPoint on border of maze
    
    else if(XorY==1){
      endPointY=0;
      endPointX=(int)(Math.random()*(columns-2)+1);
    }
    
    array[endPointY][endPointX] = "X";            //setting the endpoint to X
    x=startPointX;
    y=startPointY;
    
    for(int i = 0; i<rows; i++){
      for(int j = 0; j<columns; j++){                       //copies an array for the pathMethod method
        copyArray[i][j] = array[i][j];
      }
    }
//    printArray(array);                              //prints original array
    pathMethod(x, y, rows, columns, startPointX, startPointY, endPointX, endPointY, copyArray, array, decider); //sends all relevant parameters to method
  }
  //==========================================================================================================================================================================================================================================
  
  public static void fillFile(String [][] array, File file, int rows, int columns) throws Exception{ //this method fills the array with input from a file
    String [] [] copyArray = new String[rows][columns];
    String sLine;
    char temp;
    int startPointX=0; 
    int startPointY=0; 
    int endPointX=0; 
    int endPointY=0;
    int decider=1;                             //shows the pathMethod was called from this method specifically for regenerator method
    int x = 0; 
    int y = 0; 
    Scanner fileScanner = new Scanner(file);       //creating a scanner for the file again
    
    for(int i = 0; i<6; i++){                    
      fileScanner.nextLine();                      //skipping first 6 lines of file (we dont need that info)
    }
    
    for(int i = 0 ; i<rows; i++){
      sLine = fileScanner.nextLine();
      for(int j = 0; j<columns; j++){                 //storing it into the array
        temp = sLine.charAt(j);                           // gets the char at a coordinate
        array[i][j] = String.valueOf(temp);             //turns the char back into a String to input into the maze array
      }
    }
    
    for(int i = 0; i<rows; i++){
      for(int j = 0; j<columns; j++){
        if(array[i][j].equals("S")){                   //finds the startpoint within the file
          startPointX = j;
          startPointY = i;
        }
        else if(array[i][j].equals("X")){             //find the endpoint within the file
          endPointX = j;
          endPointY = i;
        }
      }
    }
    x=startPointX;                               //sets x and y for the pathMethod method
    y=startPointY;
    
    for(int i = 0; i<rows; i++){
      for(int j = 0; j<columns; j++){                //creates a copy of the array for the pathMethod
        copyArray[i][j] = array[i][j];
      }
    }
 //   printArray(array);                              //prints array
    
    pathMethod(x, y, rows, columns, startPointX, startPointY, endPointX, endPointY, copyArray, array, decider);     //calls the pathMethod method; sends all relevant parameters
  }
  //==========================================================================================================================================================================================================================================
  public static void printArray(String [][] array){        //this method prints the array (the maze) on the console
    System.out.println("\nThe Unsolved Maze: \n");
    for(int i = 0; i<array.length; i++){
      for(int j = 0; j<array[i].length; j++){
        System.out.print(array[i][j]+"  ");
      }
      System.out.println();
    }
  }
  //===========================================================================================================================================================================================================================================
  
  //this method finds a path from the Starting Point to the EndPoint if possible by moving one up, down, left or right.
  public static void pathMethod (int x, int y, int rows, int columns, int startPointX, int startPointY, int endPointX, int endPointY, String [][]copyArray, String [][]realArray, int decider) throws Exception{ 
    
    String up = copyArray[y-1][x];
    String down = copyArray[y+1][x];
    String right = copyArray[y][x+1];                         //moves the coordinate up one, down one, right one, left one and stores it into up down left right; 
    String left = copyArray[y][x-1];
    
    if(up.equals("X")||down.equals("X")||right.equals("X")||left.equals("X")){    //if up right left or down is equal to the endpoint, the maze is solvable, and we can now print the final array
      System.out.println("\nThe maze is solvable!");
//      printFinalArray(realArray, counter, xvalues, yvalues);                  //solution is found already -  sends back to another method to print final solution
      regenerate(decider);                                                   //then sends to regenerate method where it asks if user would like to create another maze
    }
    
    else if(copyArray[startPointY+1][startPointX].equals("B")&&copyArray[startPointY-1][startPointX].equals("B")&&copyArray[startPointY][startPointX+1].equals("B")&&copyArray[startPointY][startPointX-1].equals("B")){
      
      System.out.println("\nSorry, your maze is unsolvable.\n\n");  //if at the start point we cannot go up down left or right - no possible moves - not solvable - end of maze    
      regenerate(decider);                                        //call the method that gives users the option to recreate the maze if it was unsolvable  
    }  
    
    else if(up.equals("O")){           //if the coordinate in the up direction is O
      counter=counter+1;              //incrementing counter so we can input it the coordinate arrays
      copyArray[y][x] = "V";         //changing the values to 'V' so we can know that we already visited it
      y=y-1;                        //moving coordinate up one 
      xvalues[counter]=x;          //storing this coordinate into the arrays with the counter
      yvalues[counter]=y;
      pathMethod(x, y, rows, columns, startPointX, startPointY, endPointX, endPointY, copyArray, realArray, decider); //recalling method
    }
    
    else if(down.equals("O")){      //if down = O
      copyArray[y][x] = "V";
      counter=counter+1;
      y=y+1; 
      xvalues[counter]=x;
      yvalues[counter]=y;
      pathMethod(x, y, rows, columns, startPointX, startPointY, endPointX, endPointY, copyArray, realArray, decider);
    }
    
    else if(right.equals("O")){      //if right equals O
      copyArray[y][x] = "V";
      counter=counter+1;
      x=x+1;
      xvalues[counter]=x;
      yvalues[counter]=y;
      pathMethod(x, y, rows, columns, startPointX, startPointY, endPointX, endPointY, copyArray, realArray, decider);
    }
    
    else if(left.equals("O")){         //if left equals O
      copyArray[y][x] = "V";
      counter=counter+1;
      x=x-1;
      xvalues[counter]=x;
      yvalues[counter]=y;
      pathMethod(x, y, rows, columns, startPointX, startPointY, endPointX, endPointY, copyArray, realArray, decider);
    }
    
    else {                                           //if neither up down left or right work
      for(int i = 0; i<rows; i++){                   //makes all the 'V's go back to O
        for(int j = 0; j<columns; j++){
          if(copyArray[i][j].equals("V")){
            copyArray[i][j] = "O";
          }
        }
      }
      copyArray[y][x] = "B";                    //make the coordinate that you cant make a move with B, so it shortens the possible path options
      for(int i = 0 ; i<counter; i++){
        xvalues[i] = 0;                        //resets the coordinate arrays back to 0
        yvalues[i] = 0;
      }
      counter=0;                              //resets counter back to 0;
      pathMethod(startPointX, startPointY, rows, columns, startPointX, startPointY, endPointX, endPointY, copyArray, realArray, decider);   //resends the startpoints instead of x and y; only thing different is that one coordinate is now marked "B"
    }    
  }  //end of method  
  //======================================================================================================================================================================================================================================
  
  public static void printFinalArray(String[][] realArray, int counter, int [] xvalues, int [] yvalues){   //method print the final solved maze if possible, with the + signs in the correct path
    System.out.println("\nThe Solved Maze: ");
    for(int i = 1; i<=counter; i++){
      realArray[yvalues[i]][xvalues[i]] = "+";                //changes the coordinate points to '+'
    }
    
    for(int i = 0; i<realArray.length; i++){
      for(int j = 0; j<realArray[i].length; j++){             //prints the array
        System.out.print(realArray[i][j]+"  ");
      }
      System.out.println();
    } 
  } 
  //=====================================================================================================================================================================================================================================
  public static void regenerate(int decider) throws Exception{
    String yesOrNo;
    int oneOrTwo=3;                   //meaningful variable names
    String input;
    
    if(decider==0){              //if the pathMethod was called from the fillRandom, it would skip a line of input for some reason. 
      scanner.nextLine();        //because of this, we must clear the scanner, ONLY when called by the fillRandom method, not the fillFile method
    } 
    System.out.println("\nWould you like to create another maze? \nPlease type 'yes' to continue. To exit, type anything else. \n");  //clear and concise prompts
    yesOrNo = scanner.nextLine();  
    
    if(yesOrNo.equalsIgnoreCase("yes")){       //if they want to create another maze to solve
      System.out.println("\nWould you like it to be randomly created or from a file? Please type 1 or 2 respectively to continue. To exit, type anything else.");
      input =scanner.nextLine();
      
      try{
        oneOrTwo = Integer.parseInt(input);
      } catch (Exception e){
      }
      
      if(oneOrTwo==1){ 
        preRandom();                //call the randomArray method to create it again
      }     
      else if(oneOrTwo==2){
        System.out.println("Please type anything to continue.");
        getFile();                              //sends program back to getFile method to reenter the file name
      }     
      else{
        System.out.println("\nThank you for using this program!");  //end of program
      }
    }
    else{
      System.out.println("\nThank you for using this program!");  //end of program
    }
  }//end of method
}//end of class
