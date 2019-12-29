package net.ddns.worldofjarcraft;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FileMigrater {


    public static void main(String[] args) throws IOException {
        if(args.length < 3){
            System.err.println("Too few arguments!");
            return;
        }
        String referenceTable = args[0];
        String JoinTable = args[1];
        String outptTable = args[2];
        FileOutputStream result = new FileOutputStream(outptTable);
        Scanner referenceScanner = new Scanner(new FileInputStream(referenceTable));
        String refLine = referenceScanner.nextLine();
        List<String> output = new LinkedList<>();
        boolean initialRun = true;
        while( refLine != null){
            Scanner join;
            if(initialRun){
                join = new Scanner(new FileInputStream(JoinTable));
                initialRun = false;
                String joinLine = join.nextLine();
                while(joinLine != null){
                    for(String str : refLine.split(";")){
                        joinLine = joinLine.replaceAll("(?i)"+str, str);
                    }

                    output.add(joinLine);
                    if(join.hasNextLine()) {
                        joinLine = join.nextLine();
                    }
                    else {
                        joinLine = null;
                    }
                }
                join.close();
            }
            else{
                List<String> outputDup = new LinkedList<>();
                for(String joinLine : output){
                    for(String str : refLine.split(";")) {
                        joinLine = joinLine.replaceAll("(?i)" + Pattern.quote(str), str);
                    }
                    outputDup.add(joinLine);
                }
                output = outputDup;
            }
            if(referenceScanner.hasNextLine()) {
                refLine = referenceScanner.nextLine();
            }
            else {
                refLine = null;
            }
        }
        referenceScanner.close();
        for(String str : output){
            String print = str + System.getProperty("line.separator");
            result.write(print.getBytes(StandardCharsets.ISO_8859_1));
        }
        result.close();
    }
}
