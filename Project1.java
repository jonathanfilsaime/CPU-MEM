/**
 * @author Jonathan Fils-Aime, the King, Cash money
 *all me baby, maybe with some help from the math lab 
 *but otherwise all me
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;


public class Project1 {
    //process
    public static Scanner sc;
    public static PrintWriter pw;
    public static Process proc;
    public static int t;
    //registers
    public static int PC = 0;
    public static int SP = 999;
    public static int IR = 0;
    public static int AC = 0;
    public static int X = 0;
    public static int Y = 0;
    public static int temph;
    public static boolean user;
    //additional to kepp track, not pretty but effective
    public static int topStack = 999;
    public static int hold;
    public static int av;
    public static int check = 0;
    public static int track = 0;
    public static int sStackPointer = 1999;
    public static int port = 0;
    public static int timer;
    public static String temp;
    public static String nf;
    public static int count = 0;

    public static void main(String[] args) {
        
	            nf = args[0];
	            timer = Integer.parseInt(args[1]);
        try
	    {
        //fork child process(launch memory)
		Runtime rt = Runtime.getRuntime();
		proc = rt.exec("java mem");
        
        //communication objects
		InputStream is = proc.getInputStream();
		OutputStream os = proc.getOutputStream();

        //programm to be read
        String programName = ( nf );

        //pass program to memory
		pw = new PrintWriter(os);
		pw.printf(programName + "\n");
		pw.flush();

        //to recieve instruction from memory
		sc = new Scanner(is);
            
		do{
            //fetch
		    IR = read(PC);
            
            //execute
		    if(IR != 50){
			exe(IR);
		    }
                
		    //update PC
		    PC++;
		    timer--;
                
            //timer (process interrupr)
		    if(timer == 0 && user == true){
			user = false;
			write(SP, PC);
			SP--;
			temph = sStackPointer;
			sStackPointer = SP;
			SP = temph;
			PC = 1000;
		    }
		}
		while( IR != 50);

        if(check ==1)
        {
            System.out.println("Memory violation: accessing system address 1000 in user mode");
        }
            
		System.out.println("Process exited: " + 1);
            
	    }
        catch (Throwable t)
	    {
		t.printStackTrace();
	    }
    }
    
    //parse intstruction
    public static int read(int address){
        temp = new Integer(address).toString();
        pw.printf(temp + "\n");
        pw.flush();
        t = Integer.parseInt(sc.nextLine());
        return t;
    }
    
    //write to memory stack
    public static void write(int address, int data){
        String h = "w" + " " + address + " " + data + " " + "\n";
        pw.printf(h);
        pw.flush();
    }
    
    //check user stack or system stack
    public static boolean isUser(int pc){
        if(pc < 1000){
            user = true ;
        }
        return user;
    }
    
    //execution
    public static void exe(int IR){
    
	switch(IR){
	case 1 :
	    //Load the value into the AC
        PC++;
	    AC = read(PC);
	    break;
            
	case 2 :
	    //Load the value at the address into the AC
        PC++;
	    AC = read(read(PC));
	    break;
            
	case 3 :
	    //Load the value from the address found in the given address into the AC
        PC++;
	    AC =read(read(read(PC)));
	    break;
            
	case 4 :
	    //Load the value at (address+X) into the AC
        PC++;
        AC = read(read(PC)+ X);
	    break;
            
	case 5 :
	    //Load the value at (address+Y) into the AC
        PC++;
        AC = read(read(PC) + Y);
	    break;
            
	case 6 :
	    //Load from (Sp+X) into the AC (if SP is 990, and X is 1, load from 991).
        AC = read(SP + X + 1);
	    break;
            
	case 7 :
	    //Store the value in the AC into the address
        PC++;
	    int idx = read(PC);
	    write(idx, AC);
	    break;
            
	case 8 :
	    //Gets a random int from 1 to 100 into the AC
        int minimum = 1;
        int maximum = 100;
        AC = minimum + (int)(Math.random() * maximum);
	    break;
            
	case 9 :
	    //If port=1, writes AC as an int to the screen
            PC++;
            port = read(PC);
            
            if(port == 1){
                System.out.print(AC);
                if(AC >= 1000 && user == false){
                    check = 1;
                }
            }
                
	    //If port=2, writes AC as a char to the screen
            else if(port == 2){
                System.out.printf("%c", AC);
                if(AC >= 1000 && user == false){
                    check = 1;
                }
            }
            
            else
            {
		    System.out.println("Bad port number");
		    try{
			proc.waitFor();
			int exitVal = proc.exitValue();
			System.out.println("Process exited: " + exitVal);
		    }
		    catch (Throwable t)
			{
			    t.printStackTrace();
			}
		}
	    break;
            
	case 10 :
	    //Add the value in X to the AC
        AC += X;
	    break;
            
	case 11 :
	    //Add the value in Y to the AC
        AC += Y;
	    break;
            
	case 12 :
	    //Subtract the value in X from the AC
        AC -= X;
	    break;
            
	case 13 :
	    //Subtract the value in Y from the AC
        AC -= Y;
	    break;
            
	case 14 :
	    //Copy the value in the AC to X
        X = AC;
	    break;
            
	case 15 :
	    //Copy the value in X to the AC
        AC = X;
	    break;
            
	case 16 :
	    //Copy the value in the AC to Y
        Y = AC;
	    break;
            
	case 17 :
	    //Copy the value in Y to the AC
        AC = Y;
	    break;
            
	case 18 :
	    //Copy the value in AC to the SP
        write(SP, AC);
	    break;
            
	case 19 :
	    //Copy the value in SP to the AC
        AC = SP;
	    break;
            
	case 20 :
	    //Jump to the address
        PC++;
	    PC = read(PC);
	    PC--;
	    break;
            
	case 21 :
	    //Jump to the address only if the value in the AC is zero
        PC++;
        int dk = read(PC);
	    if(AC == 0){
                PC = dk;
                PC--;
            }
            break;
            
	case 22 :
	    //Jump to the address only if the value in the AC is not zero
        PC++;
        int dk1 = read(PC);
	    if(AC != 0){
                PC = dk1;
                PC--;
            }
	    break;
            
	case 23 :
	    //Push return address onto stack, jump to the address
        PC += 2;
        write(SP, PC);
        SP--;
        PC--;
        PC = read(PC);
        PC--;
	    break;
            
	case 24 :
	    //Pop return address from the stack, jump to the address
        SP++;
        PC = read(SP);
        PC--;
	    break;
            
	case 25 :
	    //Increment the value in X
        X++;
	    break;
            
	case 26 :
	    //Decrement the value in X
        X--;
	    break;
            
	case 27 :
	    //Push AC onto stack
        write(SP, AC);
        SP--;
	    break;
            
	case 28 :
	    //Pop from stack into AC
        SP++;
        AC = read(SP);
	    break;
            
	case 29 :
	    //Perform system call
        user = false;
        write(SP, PC);
        SP--;
        temph = sStackPointer;
        sStackPointer = SP;
        SP = temph;
        PC = 1499;
	    break;
            
	case 30 :
	    //Return from system call
        temph = sStackPointer;
        sStackPointer = SP;
        SP = temph;
        SP++;
        PC = read(SP);
        user = true;
	    break;
            
	case 50 :
	    //End execution
	    try{
		pw.printf("x" + "\n");
		pw.flush();
		proc.waitFor();
		int exitVal = proc.exitValue();
		System.out.println("Process exited: " + exitVal);
            }
	    catch (Throwable t)
		{
		    t.printStackTrace();
		}
	    break;
	default :
        }
    }
}