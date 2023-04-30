# Rescue_Robot_Project
This is a project to program a robot that can take directions as instructed and navigate to a certain location.

project description

In many hazardous operations where humans find it dangerous, and in these type of situations we use robots to do the task. One of the task the robot 
needs to do is to navigate to the target destination. And to do that, the robot is programmed with certain valid directions and the number of steps 
it can move in that direction. Assuming, the robot can move in any one of the four valid directions (Forward (F), Back (B), Left(L), Right (R)) 
followed by the number of steps it can move in that direction. The number of steps it can take is between 1 to 99. 

The robot needs to be programmed first with one of the valid four directions (F,B,L,R) and then followed by the number of steps. 

 

              Some examples of valid codes are  

F4L1B3,  R5F2, B7, L8F2R4B3, L1, R5 

               Some of the invalid codes are: 

                           12, LR, L2J2, K3F5, R12, F6L7R12, B5R8L+, L4-R3 

 

In this program assignment, 3 classes will be used. They are checkCode(String path), checkNavigate(int row, int col, String path),
navigateRoute(int srow, int scol, int trow, tcol, String path).  

 

Class checkCode(String path) 

 

This function returns if the input code is valid or invalid.  

 

checkNavigate(int row, int col, String path) 

 

A method in the class determines the number of steps it could take indicated by the path. 
Here as the robot navigates according to the direction given in the path, it will finally return the 
number of steps it took without going off the wall or hit an obstacle (wall). There is a possibility the robot may be stopped by an obstacle(wall) 
or going beyond the wall. If that happened, then the robot stops and it returns the number of steps it took. 

 

Int row, int col indicates the starting point of the grid. The path starts from (int row, int col). 

 

navigateRoute(int srow, int scol, int trow, tcol, String path).  

 

In this class, the number of steps a robot starting at position(srow, scol) takes to reach the target destination (trow, tcol) using the given path. 
If the robot hits a wall along the path then it returns a value -1 and if went off the wall, it then returns -2. On the other if it reaches not the 
target destination without hitting an obstacle(wall) or going beyond a wall, which implies the programmed path was incorrect, so in this case it returns 0. 
If it reaches the target destination as indicated by path, then it returns 1, which indicates it was a successful operation.	 

 
Write a program to implement the above classes.  

