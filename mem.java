/**
 * @author Jonathan Fils-Aime, the King, Cash money
 *all me baby, maybe with some help from the math lab
 *but otherwise all me
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;


public class mem {
    public static int[] mem = new int[2000];
    public static String line;
    public static String temp = "";
    public static StringBuilder line1 = new StringBuilder();
    public static StringBuilder line2 = new StringBuilder();
    public static int i = 0;
    public static boolean ad;
    public static int add;
    public static FileReader fileReader;
    public static Scanner fl;
    public static String fln;
    public static int PC;
    public static int comp;
    public static int address;
    public static int data;
    public static int whiteSpace;
    public static int d;
    public static PrintWriter writer;
    
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        
        // rwite activity to log file for debugging
        writer = new PrintWriter("log.txt", "UTF-8");
        
        //read pc from CPU
        fl = new Scanner(System.in);
        fln = fl.nextLine();
        
        //open read and parse file into integer
        try {
            fileReader = new FileReader(fln);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            //parse line by line
            while((line = bufferedReader.readLine()) != null) {
                for (int x = 0; x < line.length(); x++) {
                    if (line.charAt(x) != ' ') {
                        line1.append(line.charAt(x));
                        if (line1.charAt(x) == '.') {
                            ad = true;
                            }
                        }
                        else{
                            break;
                            }
                    if (line1.length() != 0) {
                        temp = line1.toString();
                    }
                }
                if (ad) {
                    line1.deleteCharAt(0);
                    add = Integer.parseInt(line1.toString());
                }
                
                temp = line1.toString();
                
                //load in memory array
                if (line1.length() != 0) {
                    if (ad) {
                        i = add;
                        mem[i] = Integer.parseInt(temp);
                        ad = false;
                    } else {
                        mem[i] = Integer.parseInt(temp);
                        i++;
                    }
                    line1.setLength(0);
                }
            }
            
            bufferedReader.close();
            
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    

        
        do{
            
            fln = fl.nextLine();
            
            //write to log file for debuggin purposes
            writer.println("mem PC: " + fln);
    
            //parsing CPU command
            if(fln.charAt(0) == 'w'){
                whiteSpace = 0;
                write(fln);
            }
            else{
                PC = Integer.parseInt(fln);
                
                //write to log file for debuggin purposes
                writer.println("mem IR: " + mem[PC]);
                System.out.println(mem[PC]);
                }
        }while(true);   
    }
    
        
    
    //parse adn write to stack
    public static void write(String fln){
        
        for(comp = 1; comp < fln.length() ; comp++){
            
            if(fln.charAt(comp) == ' '){
                whiteSpace++;
                comp++;
            }
            
            if(whiteSpace == 1 )
                {
                    line1.append(fln.charAt(comp));
                }
            
            if(whiteSpace == 2)
            {
                line2.append(fln.charAt(comp));
            }
            
            if(whiteSpace == 3)
            {
                break;
            }
        }
        
        temp = line1.toString();
        address = Integer.parseInt(temp);
        line1.setLength(0);
        
        temp = line2.toString();
        data = Integer.parseInt(temp);
        line2.setLength(0);
        
        
        mem[address] = data;
        
        //write to log file for debuggin purposes
        writer.println("mem address: " + address + " mem value: " + mem[address]);
    }
}