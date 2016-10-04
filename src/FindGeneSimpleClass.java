
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
        String geneseq = "";
        int search_startCodon_idx = 0;
        int search_stopCodon_idx = 0;
        while(search_startCodon_idx < dna.length()){
        	// start at 0
        	// if find: startcodon and stopcodon: start_idx = stopcodon+3 and loop again
        	// if no stopcodon: leave
        	int startcodonindex = dna.indexOf(startcodon,search_startCodon_idx);
            if(startcodonindex == -1){return "";}
            search_stopCodon_idx = startcodonindex + startcodon.length();
            int stopcodonindex = dna.indexOf(stopcodon, search_stopCodon_idx);
            if(stopcodonindex == -1){return "";}
            //System.out.println("-> "+" "+search_startCodon_idx+" "+startcodonindex+" "+search_stopCodon_idx+" "+stopcodonindex);
            while(stopcodonindex != -1 && search_stopCodon_idx < dna.length()){
            	geneseq = dna.substring(startcodonindex,stopcodonindex + stopcodon.length());
                if(geneseq.length() / 3 <= 0 || geneseq.length() % 3 != 0 ){
                	search_stopCodon_idx = stopcodonindex + 1;
                	if(search_stopCodon_idx >= dna.length()){break;}
                	//System.out.println("search_stopCodon_idx, stopcodonindex pos:"+" "+search_stopCodon_idx+", "+stopcodonindex);
                	stopcodonindex = dna.indexOf(stopcodon, search_stopCodon_idx);
                }else{
                	System.out.println("  gene: " + geneseq);
                	System.out.println("  ini: " + startcodonindex + " end: " + (stopcodonindex + startcodon.length() - 1) );
                	System.out.println("  bases: " + geneseq.length());
                	//System.out.println("//");
                	search_startCodon_idx = search_stopCodon_idx + 1; // to later break the while() once a valid gene its found.
                }
                if(search_startCodon_idx >= search_stopCodon_idx || search_startCodon_idx >= dna.length()){break;}
            }
            if(stopcodonindex == -1 || search_stopCodon_idx >= dna.length()){return "";}
        }
        return "dna end reached";
    }
    public static void main(String[] args){
        //File: dna.txt
        FileResource dnafile = new FileResource();
        System.out.println("Start search:");
        for (String dna: dnafile.lines()){
        	System.out.println("dna:" + dna);
            String gene = FindGeneSimple(dna);
            if(gene == ""){System.out.println("//");}
//            System.out.println("g:" + gene);
//            System.out.println("//");
//            System.out.println("DNA: " + dna);
//            if(gene == ""){
//                System.out.println("Gene: None");
//            }else{
//                System.out.println("Gene: " + gene);
//            }
//            System.out.println("Gene Len: " + gene.length());
//            System.out.println("//");
        }
    }
}