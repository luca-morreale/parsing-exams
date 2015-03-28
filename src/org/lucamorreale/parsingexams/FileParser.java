/**
 *
 */
package org.lucamorreale.parsingexams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

/**
 * @author Luca Morreale
 *
 */
public class FileParser {

    private File file;
    private String[] patterns;
    private String[] results;


    public FileParser(File file){
        this.file = file;
    }

    public FileParser(File file, String[] pattern){
        this(file);
        this.patterns = pattern;
    }

    public void setPattern(String[] pattern){
        this.patterns = pattern;
    }

    public String[] getPattern(){
        return patterns;
    }

    public String extractData(){

        PDDocument doc = null;
        String data = "";

        if (file.getAbsolutePath().endsWith(".txt")) {

            try {
                data = new Scanner(file).useDelimiter("\\Z").next();
            } catch (FileNotFoundException e) {
                LOG.severe("File doesn't exist");
            }
        } else if (file.getAbsolutePath().endsWith(".pdf")) {

            try {
                doc = PDDocument.load(file);
                PDFTextStripper stripper = new PDFTextStripper();
                data = stripper.getText(doc);
                doc.close();
            } catch (IOException e) {
                LOG.severe(e.getMessage());
            }
        }

        return data;

    }

    public static String extracData(File file){

        PDDocument doc = null;
        String data = "";

        if (file.getAbsolutePath().endsWith(".txt")) {

            try {
                data = new Scanner(file).useDelimiter("\\Z").next();
            } catch (FileNotFoundException e) {
                LOG.severe("File doesn't exist");
            }
        } else if (file.getAbsolutePath().endsWith(".pdf")) {

            try {
                doc = PDDocument.load(file);
                PDFTextStripper stripper = new PDFTextStripper();
                data = stripper.getText(doc);
                doc.close();
            } catch (IOException e) {
                LOG.severe(e.getMessage());
            }
        }

        return data;
    }

    public String[] parseFile(String[] pattern){
        setPattern(pattern);
        return parseFile();
    }

    public String[] parseFile(){

        results = new String[patterns.length];
        String data = extractData();

        for (int i = 0; i < patterns.length; i++) {

            String scan[] = data.split(patterns[i]);
            if(scan.length >1){
                results[i] = scan[1].split("\n")[0];
                results[i] = clearString(results[i], patterns[i]);

            } else {
                results[i] = "";
            }
        }

        return results;
    }

    private String clearString(String s, String subStr){

        s = s.replaceFirst(subStr, "");
        s = s.replaceAll("[^a-zA-Z0-9.,]", " ");
        s = s.replaceAll("[^0-9., ]", "");
        s = s.trim();

        return s;
    }

    public String[] getResults(){
       return results;
    }

    private static Logger LOG = Logger.getLogger(FileParser.class.getName());
}