
package assignment_robot;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Assignment_Robot {

    public static void main(String[] args) {
        System.out.println("\tThis is Rescue Robot. It can navigate in a space area of 30x30 with a path limit of 1 - 99.");
        System.out.println("There are obstacles in (12,15) (7,9) (4,19) (23,25)");
         System.out.println("==========================================================================================================");
               
        
        try {

            Scanner in = new Scanner(System.in);
            System.out.println("\nEnter the direction in alphapet number suquence e.g. R4L3F3B1: ");
            String path = in.nextLine();
            System.out.println("Enter a starting row: ");
            int srow = in.nextInt();
            System.out.println("Enter a starting colomn: ");
            int scol = in.nextInt();
            System.out.println("Enter a target row where robot need to be: ");
            int trow = in.nextInt();
            System.out.println("Enter a target colomn where robot need to be: ");
            int tcol = in.nextInt();
            CheckCode obj = new CheckCode(path);

            if (obj.checkcode(path) && srow >= 1 && scol >= 1) {

                navigateRoute obj1 = new navigateRoute();

                obj1.checkNavigation(srow, scol, path);

                int ans = obj1.navigateroute(srow, scol, trow, tcol, path);

                if (ans == 1) {
                    System.out.println(ans + " : successful operation ");
                } else if (ans == 0) {
                    System.out.println(ans + " : programmed path was incorrect ");
                } else if (ans == -1) {
                    System.out.println(ans + " : Rescue hitted a wall(obstacle) ");
                } else if (ans == -2) {
                    System.out.println(ans + " : Rescue went off the wall ");
                }
            } else {
                System.out.println("invalid direction or wrong srow/scol");
            }
        } catch (NumberFormatException e) {
            System.out.println("invalid direction, you have not included the steps values");
        } catch (InputMismatchException e) {
            System.out.println("invalid input");
        }

    }

}

class CheckCode {

    String path;

    CheckCode(String path) {
        this.path = path;
    }

    boolean checkcode(String path) {

        ArrayList arr1 = new ArrayList();
        String[] arr = path.split("");

        //to remove the numbers from the string
        for (int i = 0; i < arr.length; i++) {

            if (arr[i].equals("0") || arr[i].equals("1") || arr[i].equals("2") || arr[i].equals("3")
                    || arr[i].equals("4") || arr[i].equals("5") || arr[i].equals("6") || arr[i].equals("7")
                    || arr[i].equals("8") || arr[i].equals("9")) {//dont add anything

            } else {
                arr1.add(arr[i]);
            }
        }

        boolean xy = false;
        //checking if string contains the right directions
        for (int i = 0; i < arr1.size(); i++) {

            if ((arr1.get(i).equals("B")) || (arr1.get(i).equals("L"))
                    || (arr1.get(i).equals("R")) || (arr1.get(i).equals("F"))) {
                xy = true;

            } else {
                xy = false;
                break;
            }
        }
        //to check if the string starts with number, thus invalid
        for (int i = 0; i < 10; i++) {
            if (path.startsWith(Integer.toString(i))) {
                xy = false;
            }
        }

        return xy;
    }

}

class CheckNavigation {

    String path;

    CheckNavigation() {
    }

    //method for changing directions to x y axis

    String getDirectionXY(String path) {

        //B and F are directions for Y axis thus -Y identify backward while y+ identify forward
        if (path.contains("B") || path.contains("F")) {

            /*B or F is going to be replaced with -y or +y to indicate backward or forward, the same string is assigned to avoid any data loss; 
             space is for splitting later **/
            if (path.contains("B")) {
                path = path.replaceAll("B", " -Y");
            }
            if (path.contains("F")) {
                path = path.replaceAll("F", " +Y");
            }

        }
        //it will continue changing the prevoius path string to have all data on same string; now its for left and right indicated by +x and -x
        if (path.contains("R") || path.contains("L")) {
            if (path.contains("R")) {
                path = path.replaceAll("R", " +X");
            }

            if (path.contains("L")) {
                path = path.replaceAll("L", " -X");
            }

        }
        return path;  //this string contains only y or x variable and the path values
    }

    //method for forward and backward movement 
    int[] F_B_Move(int col, int num, int pathLimit, int pathCount) {
        int lastDirectionY = 0;
        int preCol = 0;

        col = col + num; // advancing the col with the direction

        if (col > 1 && col < 30) { // if its inside the grid

            if ((pathCount + num) < 99) {// if the pathcount + direction will not exceed the pathlimit continue to calculate                       
                if (num < 0) // if this direction is -ve; i.e backward direction
                {
                    pathCount = pathCount - num;
                } else // forward direction
                {
                    pathCount = pathCount + num;
                }
            } else { // if the path will exced the limit reset the col and break, pathLimit=2 means break
                col = col - num;
                pathLimit = 2;
            }
        } else if (col < 1) { // if the direction is causing the robot to hit the wall
            if ((pathCount - num) < 99) { // check pathlimit first
                lastDirectionY = num; // the last direction that caused to hit the wall

                preCol = col - lastDirectionY;// to get the value of col before hitting the wall 

                while (preCol > 1) {// finding number of steps it took to reach the wall   
                    pathCount++;
                    preCol--;
                }

            } else {
                col = col - num;
                pathLimit = 2;
            }
        } else if (col >= 30) { // if the direction causing the robot to hit the wall is above 30
            if ((pathCount + num) < 99) {
                lastDirectionY = num; // the last direction that caused to hit the wall

                preCol = col - lastDirectionY;// to get the value of col before hitting the wall 

                while (preCol < 30) {// finding number of steps it took to reach the wall                 
                    pathCount++;
                    preCol++;
                }

            } else {
                col = col - num;
                pathLimit = 2;
            }
        }

        int[] arr = {col, pathCount, pathLimit};

        return arr;
    }

    //method for right left movement
    int[] R_L_Move(int row, int num, int pathLimit, int pathCount) {

        int lastDirectionX = 0;
        int preRow = 0;

        row = row + num; // advancing the col with the direction

        if (row > 1 && row < 30) {  //if the direction is inside the grid
            if ((pathCount + num) < 99) { // if the pathcount + direction will not exceed the pathlimit continue to calculate

                if (num < 0)// left direction i.e -ve
                {
                    pathCount = pathCount - num;
                } else // right direction
                {
                    pathCount = pathCount + num;
                }

            } else {// if the path is beyond the limit
                row = row - num;
                pathLimit = 2;
            }

        } else if (row < 1) {// if the robot hit the wall
            if ((pathCount - num) < 99) {//check pathlimit
                lastDirectionX = num; // last forward/backword direction that cause the collision

                preRow = row - lastDirectionX;// to get the value of col before hitting the wall 

                while (preRow > 1) {// finding number of steps it took to reach the wall

                    pathCount++;

                    preRow--;
                }

            } else {
                row = row - num;
                pathLimit = 2;
            }
        } else if (row >= 30) {// if the robot hit the wall
            if ((pathCount + num) < 99) {
                lastDirectionX = num;

                preRow = row - lastDirectionX;// to get the value of col before hitting the wall 

                while (preRow < 30) {// finding number of steps it took to reach the wall                 
                    pathCount++;
                    preRow++;
                }

            } else {
                row = row - num;
                pathLimit = 2;
            }
        }

        int[] arr = {row, pathCount, pathLimit};
        return arr;

    }

    /*method for checking obsticles in col; e.g if thereis obsticle in 12, 15 then this method is true if 
     row=12 and col with forward direction>15, or row=12 and col - backword direction is <=15 or col=15**/
    boolean checkObsticleCol(int num, int col, boolean obsRow, int obs) {
        boolean ans = false;
        if (num > 0) {
            ans = (col == obs) || (obsRow == true && col >= obs);
        } else {
            ans = (col == obs) || (obsRow == true && col + num <= obs);
        }
        return ans;
    }

    /*method for checking obsticles in row; e.g if there is obsticle in 12, 15 then this method is true 
     if col=15 and row with forward direction >12, or col=15 and row - backword direction is <=12 or row=12**/
    boolean checkObsticleRow(int num, int row, boolean obsCol, int obs) {
        boolean ans = false;
        if (num > 0) {
            ans = (row == obs) || (obsCol == true && row >= obs);
        } else {
            ans = (row == obs) || (obsCol == true && row + num <= obs);
        }
        return ans;
    }

    void checkNavigation(int row, int col, String path) {

        int pathCount = 0; //number of steps it took
        int lastDirectionY = 0; // last colomn direction
        int preCol = 0;   // y coordinate before the  last Direction of Y is processed
        int lastDirectionX = 0;   // last row direction
        int preRow = 0;   // x coordinate before the  last Direction of x was added/subtracted
        int pathLimit = 0;  // path  exceeded the limit i.e 99
        boolean obsCol = false;
        boolean obsRow = false;
        boolean obsCol1 = false;
        boolean obsRow1 = false;
        boolean obsCol2 = false;
        boolean obsRow2 = false;
        boolean obsCol3 = false;
        boolean obsRow3 = false;
        // method for including x, y in the path  
        String direction = getDirectionXY(path);

        String[] arr = direction.split(" ");// seperating directions

        for (int i = 0; i < arr.length; i++) { //loop for adding or subtracting directions as dectated by the array

            if (arr[i].contains("Y")) {  // if the direction starts with y coordinate 

                // if the robot reach the walls then no need to proceed    
                if (row >= 30 || row <= 0 || col >= 30 || col < 1) {
                    break;
                } else { // if it does not reach the walls   
                    arr[i] = arr[i].replace("Y", ""); // removing  Y character as it already do the indication of which axis to go 
                    int num = Integer.parseInt(arr[i]);

                    //method for advancing the col
                    int[] ary = F_B_Move(col, num, pathLimit, pathCount);
                    col = ary[0];
                    pathCount = ary[1];
                    pathLimit = ary[2];

                    //checking obsticles (12,15) in the colomn
                    if (checkObsticleCol(num, col, obsRow, 15)) {
                        //this is when the robot is coming backward to the obsticle while x=12 i.e R9F18R2B10
                        if (obsRow == true && col - num >= 15) {
                            while (col < 15) {
                                pathCount--;
                                col++;
                            }
                        } //this is when the robot is going forward to the obsticle while x=12 i.e R11F19
                        else if (obsRow == true && col >= 15) {
                            while (col > 15) {
                                pathCount--;
                                col--;
                            }
                        }
                        obsCol = true;
                    }

                    //checking obsticles (7,9) in the colomn
                    if (checkObsticleCol(num, col, obsRow1, 9)) {
                        if (obsRow1 == true && col - num >= 9) {
                            while (col < 9) {
                                pathCount--;
                                col++;
                            }
                        } else if (obsRow1 == true && col >= 9) {
                            while (col > 9) {
                                pathCount--;
                                col--;
                            }
                        }
                        obsCol1 = true;
                    }

                    //checking obsticles (4,19) in the colomn
                    if (checkObsticleCol(num, col, obsRow2, 19)) {
                        if (obsRow2 == true && col - num >= 19) {
                            while (col < 19) {
                                pathCount--;
                                col++;
                            }
                        } else if (obsRow2 == true && col >= 19) {
                            while (col > 19) {
                                pathCount--;
                                col--;
                            }
                        }
                        obsCol2 = true;
                    }

                    //checking obsticles (23,25) in the colomn
                    if (checkObsticleCol(num, col, obsRow3, 25)) {
                        if (obsRow3 == true && col - num >= 25) {
                            while (col < 25) {
                                pathCount--;
                                col++;
                            }
                        } else if (obsRow3 == true && col >= 25) {
                            while (col > 25) {
                                pathCount--;
                                col--;
                            }
                        }
                        obsCol3 = true;
                    }
                }
            } else if (arr[i].contains("X")) { // if the direction starts with x coordinate 

                if (col >= 30 || col <= 0 || row >= 30 || row < 1) { //if the robot reach the walls then no need to proceed; 
                    break;
                } else { // if it does not reach the walls

                    arr[i] = arr[i].replace("X", ""); // removing the Y character as it already do the indication of which axis to go 
                    int num = Integer.parseInt(arr[i]);
                    //method for advancing the row
                    int[] arx = R_L_Move(row, num, pathLimit, pathCount);
                    row = arx[0];
                    pathCount = arx[1];
                    pathLimit = arx[2];

                    //checking obsticles (12,15) in the row
                    if (checkObsticleRow(num, row, obsCol, 12)) {
                        //this is when the robot is coming from left of the obstacle to the obstacle while y=15   
                        if (obsCol == true && row - num >= 12) {
                            while (row < 12) {
                                pathCount--;
                                row++;
                            }
                        } // this is when is the robot is coming from the right of the obstacle to the obstacle while y=15
                        else if (obsCol == true && row >= 12) {
                            while (row > 12) {
                                pathCount--;
                                row--;
                            }
                        }
                        obsRow = true;
                    }

                    //checking obsticles (7,9) in the row
                    if (checkObsticleRow(num, row, obsCol1, 7)) {
                        if (obsCol1 == true && row - num >= 7) {
                            while (row < 7) {
                                pathCount--;
                                row++;
                            }
                        } else if (obsCol1 == true && row >= 7) {
                            while (row > 7) {
                                pathCount--;
                                row--;
                            }
                        }
                        obsRow1 = true;
                    }

                    //checking obsticles (4,19) in the row
                    if (checkObsticleRow(num, row, obsCol2, 4)) {
                        if (obsCol2 == true && row - num >= 4) {
                            while (row < 4) {
                                pathCount--;
                                row++;
                            }
                        } else if (obsCol2 == true && row >= 4) {
                            while (row > 4) {
                                pathCount--;
                                row--;
                            }
                        }
                        obsRow2 = true;
                    }

                    //checking obsticles (23, 25) in the row
                    if (checkObsticleRow(num, row, obsCol3, 23)) {
                        if (obsCol3 == true && row - num >= 23) {
                            while (row < 23) {
                                pathCount--;
                                row++;
                            }
                        } else if (obsCol3 == true && row >= 23) {
                            while (row > 23) {
                                pathCount--;
                                row--;
                            }
                        }
                        obsRow3 = true;
                    }
                }

            }
            //path limit or obticle is reached break the loop
            if (pathLimit == 2 || (obsCol == true && obsRow == true) || (obsCol1 == true && obsRow1 == true)
                    || (obsCol2 == true && obsRow2 == true) || (obsCol3 == true && obsRow3 == true)) {
                break;
            }
        }

        if (pathLimit == 2) {
            System.out.println("the path is beyond the pathlimit, according to last direction, the path  = " + pathCount + " row = " + row + " colomn= " + col);
        } else if (row <= 0 || col <= 0 || row >= 30 || col >= 30) {
            System.out.println("Rescue hit the wall, the path = " + pathCount + " row = " + row + " colomn= " + col);
        } else if ((obsCol == true && obsRow == true) || (obsCol1 == true && obsRow1 == true) || (obsCol2 == true && obsRow2 == true) || (obsCol3 == true && obsRow3 == true)) {
            System.out.println("Rescue hit an obsticle, the path it took = " + pathCount + " row = " + row + " colomn= " + col);
        } else //when everything goes well
        {
            System.out.println("Succesfull operation, the path = " + pathCount + " current row of the robot= " + row + " current colomn of the robot= " + col);
        }
    }
}

class navigateRoute extends CheckNavigation {

    int srow, scol, trow, tcol;

    navigateRoute() {

    }

    // same method as before just have return value

    int[] checknavigation(int row, int col, String path) {

        int pathCount = 0;
        int lastDirectionY = 0;
        int preCol = 0;
        int lastDirectionX = 0;
        int preRow = 0;
        int pathLimit = 0;
        boolean obsCol = false;
        boolean obsRow = false;
        boolean obsCol1 = false;
        boolean obsRow1 = false;
        boolean obsCol2 = false;
        boolean obsRow2 = false;
        boolean obsCol3 = false;
        boolean obsRow3 = false;
        String direction = getDirectionXY(path);
        String[] arr = direction.split(" ");

        for (int i = 0; i < arr.length; i++) { //loop for adding or subtracting directions as dectated by the array

            if (arr[i].contains("Y")) {  // if the direction starts with y coordinate 

                // if the robot reach the walls then no need to proceed    
                if (row >= 30 || row <= 0 || col >= 30 || col < 1) {
                    break;
                } else { // if it does not reach the walls   
                    arr[i] = arr[i].replace("Y", ""); // removing  Y character as it already do the indication of which axis to go 
                    int num = Integer.parseInt(arr[i]);

                    //method for advancing the col
                    int[] ary = F_B_Move(col, num, pathLimit, pathCount);
                    col = ary[0];
                    pathCount = ary[1];
                    pathLimit = ary[2];

                    //checking obsticles (12,15)
                    if (checkObsticleCol(num, col, obsRow, 15)) {
                        if (obsRow == true && col - num >= 15) {
                            while (col < 15) {
                                pathCount--;
                                col++;
                            }
                        } else if (obsRow == true && col >= 15) {
                            while (col > 15) {
                                pathCount--;
                                col--;
                            }
                        }
                        obsCol = true;
                    }

                    //checking obsticles (7,9)
                    if (checkObsticleCol(num, col, obsRow1, 9)) {
                        if (obsRow1 == true && col - num >= 9) {
                            while (col < 9) {
                                pathCount--;
                                col++;
                            }
                        } else if (obsRow1 == true && col >= 9) {
                            while (col > 9) {
                                pathCount--;
                                col--;
                            }
                        }
                        obsCol1 = true;
                    }

                    //checking obsticles (4,19) in the colomn
                    if (checkObsticleCol(num, col, obsRow2, 19)) {
                        if (obsRow2 == true && col - num >= 19) {
                            while (col < 19) {
                                pathCount--;
                                col++;
                            }
                        } else if (obsRow2 == true && col >= 19) {
                            while (col > 19) {
                                pathCount--;
                                col--;
                            }
                        }
                        obsCol2 = true;
                    }

                    //checking obsticles (23,25) in the colomn
                    if (checkObsticleCol(num, col, obsRow3, 25)) {
                        if (obsRow3 == true && col - num >= 25) {
                            while (col < 25) {
                                pathCount--;
                                col++;
                            }
                        } else if (obsRow3 == true && col >= 25) {
                            while (col > 25) {
                                pathCount--;
                                col--;
                            }
                        }
                        obsCol3 = true;
                    }
                }
            } else if (arr[i].contains("X")) { // if the direction starts with x coordinate 

                if (col >= 30 || col <= 0 || row >= 30 || row < 1) { //if the robot reach the walls then no need to proceed; 
                    break;
                } else { // if it does not reach the walls

                    arr[i] = arr[i].replace("X", ""); // removing the Y character as it already do the indication of which axis to go 
                    int num = Integer.parseInt(arr[i]);
                    //method for advancing the row
                    int[] arx = R_L_Move(row, num, pathLimit, pathCount);
                    row = arx[0];
                    pathCount = arx[1];
                    pathLimit = arx[2];

                    //checking obsticles (12,15)
                    if (checkObsticleRow(num, row, obsCol, 12)) {
                        if (obsCol == true && row - num >= 12) {
                            while (row < 12) {
                                pathCount--;
                                row++;
                            }
                        } else if (obsCol == true && row >= 12) {
                            while (row > 12) {
                                pathCount--;
                                row--;
                            }
                        }
                        obsRow = true;
                    }

                    //checking obsticles (7,9)
                    if (checkObsticleRow(num, row, obsCol1, 7)) {
                        if (obsCol1 == true && row - num >= 7) {
                            while (row < 7) {
                                pathCount--;
                                row++;
                            }
                        } else if (obsCol1 == true && row >= 7) {
                            while (row > 7) {
                                pathCount--;
                                row--;
                            }
                        }
                        obsRow1 = true;
                    }

                    //checking obsticles (4,19) in the row
                    if (checkObsticleRow(num, row, obsCol2, 4)) {
                        if (obsCol2 == true && row - num >= 4) {
                            while (row < 4) {
                                pathCount--;
                                row++;
                            }
                        } else if (obsCol2 == true && row >= 4) {
                            while (row > 4) {
                                pathCount--;
                                row--;
                            }
                        }
                        obsRow2 = true;
                    }

                    //checking obsticles (23, 25) in the row
                    if (checkObsticleRow(num, row, obsCol3, 23)) {
                        if (obsCol3 == true && row - num >= 23) {
                            while (row < 23) {
                                pathCount--;
                                row++;
                            }
                        } else if (obsCol3 == true && row >= 23) {
                            while (row > 23) {
                                pathCount--;
                                row--;
                            }
                        }
                        obsRow3 = true;
                    }
                }

            }
            if (pathLimit == 2 || (obsCol == true && obsRow == true) || (obsCol1 == true && obsRow1 == true)
                    || (obsCol2 == true && obsRow2 == true) || (obsCol3 == true && obsRow3 == true)) { //path limit has reached break the loop
                break;
            }
        }

        int[] cordinate = new int[4];

        if (row <= 0 || col <= 0 || row >= 30 || col >= 30) {

            if (row == 0 || col == 0 || row == 30 || col == 30) {
                cordinate[0] = row;
                cordinate[1] = col;
                cordinate[2] = pathCount;
                cordinate[3] = 2; // exact hit of the wall
            } else {
                cordinate[0] = row;
                cordinate[1] = col;
                cordinate[2] = pathCount;
                cordinate[3] = 3; // beyond the wall
            }

        } else if ((obsCol == true && obsRow == true) || (obsCol1 == true && obsRow1 == true) || (obsCol2 == true && obsRow2 == true) || (obsCol3 == true && obsRow3 == true)) {
            cordinate[0] = row;
            cordinate[1] = col;
            cordinate[2] = pathCount;
            cordinate[3] = 4;// indicating obsticle in later method
        } else if ((row >= 1 && row < 30) && (col >= 1 && col < 30) && pathLimit != 2) {
            cordinate[0] = row;
            cordinate[1] = col;
            cordinate[2] = pathCount;
            cordinate[3] = 1;
        }
        return cordinate;

    }

    int navigateroute(int srow, int scol, int trow, int tcol, String path) {
        int[] cordinate = checknavigation(srow, scol, path);

        int row = cordinate[0];
        int col = cordinate[1];
        int pathCount = cordinate[2];
        int check = cordinate[3];
        int ans = 5;
        if (check == 1 && trow == row && tcol == col) {
            ans = 1;
        } else if (check == 2) {
            ans = -1; //it has hit a wall
        } else if (check == 3) {
            ans = -2; // goes beyond the wall
        } else if (check == 4) {
            ans = -1; // hit an obsticle
        } else if (check == 1 && (row != trow || col != tcol)) {
            ans = 0; // path error
        }
        return ans;
    }
}
