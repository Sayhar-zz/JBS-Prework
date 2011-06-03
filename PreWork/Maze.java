//x,y,p,q are in cell notation (in a 4x4 maze they will be <= 3)
//a,b,r,s are in point notation (in a 4x4 maze they will be <= 8)

//make use of the Point class only with private variables.
//eclipse edit
import java.awt.Point;
import java.lang.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;

public class Maze {
	private int x; //size across
	private int y; //size down
	private String bits; //string of 1's and 0's representing the maze
	
	public Maze (int x, int y) {
		this.x = x;
		this.y = y;
		this.bits = fullMaze();
	}
    //default constructor makes a maze that is totally "filled in" with all walls erected
	
	private String dummyMaze(){
	//x*y grid of all 1's. 
		int a = this.x * 2 + 1;
		int b = this.y * 2 + 1;
		int c = a * b;
		
		char[] out = new char[c];
		java.util.Arrays.fill(out,'1');
		out[3] = '0';
		return new String(out);
	}
	
	private String fullMaze(){
		int a = this.x;
		int b = this.y;
		//Creates a maze with all possible walls present, of the dimensions a x b
		String out = "";
		
		
		if (a < 1 || b < 1) {
			Error e = new Error("Maze too small");
			throw e;
		}
		a = (a * 2) + 1;
		b = (b * 2) + 1;
		//A 4x4 maze has 9 coordinates by 9 coordinates, for example.
		
		
        char[] topchars = new char[a];
        java.util.Arrays.fill(topchars, '1');
        String top = new String(topchars);
        
        out += top;
        b--;
        //We've now created the top row;
        while (b>0){
        	if (b % 2 == 0){ 
        		//if B is even, that means we are on a row when the walls look like this: |
                //alternate 1's and 0's
        		char[] row = new char[a];
        		for (int i=a; i>0; i = i-2){
        		//	System.out.println("i is" + i);
        			row[i-1] = '1';
        		}
        		for (int i= a -1; i>0; i=i-2){
        			row [i-1] = '0';
        		}
        	
        		out += new String(row);
        		
        	}
        	else{ //we are on a row where the walls look like this: -
        		out += top;
        	
        	}
        	
        	b--;
        }
        
	return out;
	}

	public String[] bitsFormatMaze(String toformat){
        //given a string, return a string array matching the size the maze. This is a helper function for pretty printing purposes
		int a = this.x * 2 + 1;
		int b = this.x * 2 + 1;
		String in = toformat;
		
		String[] out = new String[b]; //Turning a string into a matrix
		for (int i=b; i>0; i--){
			//going through each chunk of a chars which leaves us b rows
			int l = in.length();
			//System.out.println("String is " + l + " long and we are taking out substring " + (l-a));
			out[i-1] = in.substring(l-a);
			in = in.substring(0, l-a);
		}
		
		return out;
		//System.out.println("\n\n"+ this.bits);
		//done
	}
	
	public void printMaze(String toprint){
        //given a string of a maze, print it out
		String[] in = bitsFormatMaze(toprint);
		int a = this.x * 2 + 1;
		int b = this.y * 2 + 1;
		String[] out = new String[b];
		for(int i=b; i>0; i--){
			//for each row of in
			String s = in[i-1];
			s= s.replace('0', ' ');
			
			if (i%2 == 1){
				//if row is even (then use +-+ notation)
				char[] c = s.toCharArray();
				for (int k=a;k>0;k-=2){ //some 1's are +'s, some are -'s. This takes cares of the + signs.
					c[k-1] = '+';
				}
				s = new String(c);
				s= s.replace('1', '-');
			}
			else{
				//if row is odd (use | notation)
				s= s.replace('1', '|');
			}
			out[i-1]=s;
		}
		
		for(String s : out){
			System.out.println(s);
		}
		
	}
	
	public void bitsPrintMaze(){
        //takes the bitstring, formats it using bitsFormatMaze, then prints it out all nice.
		String[] in = bitsFormatMaze(this.bits);
		for (String s : in){
			System.out.println(s);	
		}	
	
	}
	
	public void load(String s){
		this.bits = s;
	}
	
	public void display(){
		this.printMaze(this.bits);
		
	}
	
	public int findIndex(int p, int q){
	//given coordinates (in cell notation), find the index of the bits-string that correspond to that cell	
		int b = y * 2 + 1;
		int r = p * 2 + 1;
		int s = q * 2 + 1;
		int index = 0;
		//then multiply s (down) by b (width of matrix)
		index += (s*b);
		//then add r to get the right index
		index += r;
		return index;
	}
	
	
	public char findCell(int p, int q){
		//given coordinates (p,q), find that cell in the bits-string
		//first convert to point notation from cell notation
		int index = findIndex(p,q);
		
		return bits.charAt(index);
		
	}
	
	
	public boolean isWall(int p1, int q1, int p2, int q2){
		//is there a wall between point a and point b?
		
		
		//	First, what is the delta? AKA which direction are we heading?
		
		int dX = p2-p1;
		int dY = q2-q1;
		int i = findIndex(p1,q1);
		
		if 		((java.lang.Math.abs(dY+dX) != 1) 
				|| (java.lang.Math.abs(dY) > 1) 
				|| (java.lang.Math.abs(dX) > 1)){
			throw new Error("impossible move");
		}
		//now that we have a delta, we can convert to index notation (where in the bitstring is it?) then modify due to delta
		else{
			i += dX; //if we have to shift right or left, we just do that
			i += (x*2 + 1)*dY; //and if we shift up or down, we do so by b steps
			
			// i is now the index for the boundary between the two points
		}
		//ok now we can look up if there is a wall at i
		if (bits.charAt(i) == '1'){
			return true;
		}
		else if (bits.charAt(i) == '0'){
			return false;
		}
		else {
			System.out.println("ERROR. Boundary is neither a 0 nor 1. This should never happen.");
			return true;
		
		}
	}
	
	
	public char[] nesw(int p, int q){
		//given position (p,q) (p across, q down), which of its possible neighbors exist? (weeds out walls, corners)
		char[] neswT = new char[4]; 
		//boolean true/false: position 0 = can you go north, position 1 = can you go east, etc
		//
		java.util.Arrays.fill(neswT, '0');
		if (p>0){
			neswT[3]= '1';
			//validMoves =+ (p-1,q);
			//you can go west
		}
		if (q>0){
			//you can go north
			neswT[0] = '1';
			//validMoves += (p, q-1);
		}
		if (p<this.x){
			neswT[1] = '1';
			//you can go east
			//validMoves =+ (p+1, q)
		}
		if (q < this.y){
			neswT[2] = '1';
			//you can go south
			//valid moves include (p, q+1)
		}
		return neswT;
	}
 
	public boolean solve(int p1, int q1, int p2, int q2){
		//given a start and end, is there a way to connect the two points in this maze?
		//stack of points visited so far in this thread.
		// each time you pop off the stack (backtrack), use the hashmap like so:
		// avoid is a cache of known bad cells. If you pop the cell b from the stack, then put b in the avoid cache
		// before each move, check the cache to make sure you do not make a move you've already disproved.
		Deque<Point> stack = new ArrayDeque<Point>();
		HashSet<Point> avoid = new HashSet<Point>();
		//avoid returns points you should NOT try. You've already gone there and exhausted all possible moves.
		//stack.push(new Point(p1,q1));
		stack = solver(new Point(p1,q1), new Point (p2,q2), stack, avoid);
		if (stack.peekFirst() != null){ 
			return true;
		}
		return false;
	}
	
	public void trace(int p1, int q1, int p2, int q2){
		//like solve except instead of returning T/F it returns a copy of the maze with the route traced out.
		Deque<Point> stack = new ArrayDeque<Point>();
		HashSet<Point> avoid = new HashSet<Point>();
		//avoid returns points you should NOT try. You've already gone there and exhausted all possible moves.
		//stack.push(new Point(p1,q1));
		stack = solver(new Point(p1,q1), new Point (p2,q2), stack, avoid);
		
		//now given the stack of solutions, paint the solution on a grid.
		//for each element of the stack, replace that cell in the bitstring with an x
		String outstring = new String(this.bits);
		for (Point point : stack){
			int p = point.x;
			int q = point.y;
			int i = findIndex(p,q);
			outstring = addRoute(outstring, i);
		}
		printMaze(outstring);
	}
	
	private String addRoute(String s, int index){
		//given a string and an index, replace the character at that index with an "X"
		s = s.substring(0,index) + "X" + s.substring(index+1);
		return s;
	}
	

	private Deque<Point> solver(Point start, Point end, Deque<Point> stack, HashSet<Point> avoid) {
        //iterative solver		
        //base case:
        if (start.equals(end)) { 
			stack.push(end);
			return stack;} //return the solution if we've solved the problem.
		
        
        //iterative case
        Point prev = stack.peekFirst();
		if (prev == null){
			//System.out.println("no previous point.");
			prev = start;
		} //deals with finding a previous point, especially if there is none (because this is the first instance of the iteration
		
		char[] toCheck = nesw(start.x, start.y);
		Point[] possibleMoves = new Point[4];
		int numMoves = 0;
		//the way the solver works is to create a list of possible moves, then move to the first possible move. 
        //"possible" is found by applying several constraints to the base "north east south west" directions
        //first filter uses "nesw" to filter out edges and corners. (if you're in the lower right corner, you can't move more right or more down)

		/*System.out.println("\n");
		System.out.println("prev = " + prev);
		System.out.println("we are at : " + start);
		System.out.println("stack so far:" + stack);
		System.out.println("avoid so far:" + avoid); */
        //^ Interesting debugging output
		
		Point n = new Point(start.x, start.y - 1);		            //constraints:
		if (	(toCheck[0] == '1') &&                              //is a legal position on the board, and is n, e, s, or w from this cell
				(isWall(start.x,start.y,n.x,n.y) == false) &&       //there are no walls between this cell and that cell
				!(prev.equals(n) ) &&                               //it's not a cell we've just been in
				!(avoid.contains(n)) )                              //it's not on our blacklist of moves we've tried that haven't worked out.
		{
			//if we can totally go north no problem
			//System.out.println(avoid.contains(n));
			possibleMoves[numMoves] = n;
			numMoves++;
		}
		
		
		Point e = new Point(start.x+1, start.y);
		if (	(toCheck[1] == '1') && 
				(isWall(start.x,start.y,e.x,e.y) == false) && 
				!(prev.equals(e)) &&
				!(avoid.contains(e))){
			//if we can totally go east no problem
			possibleMoves[numMoves] = e;
			numMoves++;
		}
		
		Point s = new Point(start.x, start.y+1);
		if (	(toCheck[2] == '1') && 
				(isWall(start.x,start.y,s.x,s.y) == false) && 
				!(prev.equals(s)) &&
				!(avoid.contains(s))){
			//if we can totally south
			possibleMoves[numMoves] = s;
			numMoves++;
		}
		
		
		Point w = new Point(start.x-1, start.y);
		if (	(toCheck[0] == '1') && 
				(isWall(start.x,start.y,w.x,w.y) == false) && 
				!(prev.equals(w)) &&
				!(avoid.contains(w))){
			//if we can totally go west no problem
			possibleMoves[numMoves] = w;
			numMoves++;
		}
		
		//now either numMoves > 0, in which case we can move forward, or it equals 0, which means we've hit a dead end.
		
		if (numMoves == 0){
			//if we have hit a dead end	
			try{
				Point a = stack.removeFirst();
				avoid.add(start);
				//System.out.println("hit a dead end. adding " + start + " to avoid queue");
				return solver(a,end, stack, avoid);
			}
			catch(Exception NoSuchElementException){
				//if the stack is empty, that means there's no solution
				System.out.println("Empty stack! No solution!");
				return stack;		
			}
		}
		
		else{
			//numMoves > 0. We can move forward
			Point m = possibleMoves[0];
			//System.out.println("Moving to point: " + m.x + "," + m.y);
			stack.addFirst(start);
		//	m.equals(obj)
			return solver(m, end, stack, avoid);
		}
		
		
	}
	
	
	
	//function to turn an index into point notation
	public Point indexToPoint(int index){
		
		int b = y * 2 + 1;
		
		//
		int r = (index%b);
		//r corresponds to the x-direction coordinate. 
		int s = (index - r)/b;
		//s correspond to the y-direction orodinate.
		
		Point p = new Point(r,s);
		return p;
	}
	
	public static void main(String[] args) {
		Maze m = new Maze(4,4);
		System.out.println(m.bits);
		System.out.println("\n\n\n");
		//m.bitsPrintMaze();
		//System.out.println("\n\n");
		//m.display();
		
		m.load("111111111" +
			   "1Y0010101" +
			   "111010101" +
			   "101000101" +
			   "111110101" +
			   "100000101" +
			   "101111101" +
			   "100000001" +
			   "111111111");
		System.out.println("\n\n\n");
		m.display();
		System.out.println("\n\n\n");
		System.out.println(m.nesw(0,0));
		System.out.println(m.findCell(0,0));
		
		System.out.println("\n\n\n");
		System.out.println("Is there a wall between 1,2 and 1,1?");
		System.out.println(m.isWall(1,2,1,1));
		System.out.println("\n\n\nSolve 0,0 to 3,0");
		System.out.println(m.solve(0,0,3,0));
		
		m.trace(0,0,3,0);
		
	} 
}



