
/**
 * Write a description of FindGeneSimpleAndTest here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
//import java.io.*;

public class FindGeneSimpleClass{
    public static String FindGeneSimple(String dna){
        String startcodon = "ATG";
        String stopcodon = "TAA";
        //String gene = "";
        int startcodonindex = dna.indexOf(startcodon);
        if(startcodonindex == -1){return "";}
        int stopcodonindex = dna.indexOf(stopcodon,startcodonindex + startcodon.length());
        if(stopcodonindex == -1){return "";}
        String geneseq = dna.substring(startcodonindex,stopcodonindex + stopcodon.length());
        if(geneseq.length() / 3 <= 0 || geneseq.length() % 3 != 0 ){return "";}
        return geneseq;
    }
    public static void main(String[] args){
        //File: dna.txt
        FileResource dnafile = new FileResource();
        for (String dna: dnafile.lines()){
            String gene = FindGeneSimple(dna);
            System.out.println("DNA: " + dna);
            if(gene == ""){
                System.out.println("Gene: None");
            }else{
                System.out.println("Gene: " + gene);
            }
            System.out.println("Gene Len: " + gene.length());
            System.out.println("//");
        }
    }
}