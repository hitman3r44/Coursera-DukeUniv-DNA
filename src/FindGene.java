
/**
 * Write a description of FindGeneSimpleAndTest here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
//import java.io.*;

public class FindGene{
	public static boolean IsMultipleOfX(int num, int x){
		if(num < 3){return false;}
		if(num % x == 0  && num / x >= 1){return true;}
		return false;
	}
	public static int FindCodon(String dna, String codonSeq, int searchFromPos){
    	int codonIdx = dna.indexOf(codonSeq,searchFromPos);
		return codonIdx; 
	}
	public static int SelectStopCodon(int start, int stop1, int stop2, int stop3){
		int stopCodonPos = -1;
		// pick the shortest gene with codon-wise length and with codon pos != -1
		if(stop1 != -1 && IsMultipleOfX(stop1 - start, 3)){
			stopCodonPos = stop1;
			if(stop2 != -1 && IsMultipleOfX(stop2 - start, 3)){
				stopCodonPos = Math.min(stopCodonPos,stop2);
			}
			if(stop3 != -1 && IsMultipleOfX(stop3 - start, 3)){
				stopCodonPos = Math.min(stopCodonPos,stop3);
			}
		}else if(stop2 != -1 && IsMultipleOfX(stop2 - start, 3)){
			stopCodonPos = stop2;
			if(stop3 != -1 && IsMultipleOfX(stop3 - start, 3)){
				stopCodonPos = Math.min(stopCodonPos, stop3);
			}
		}else if(stop3 != -1 && IsMultipleOfX(stop3 - start, 3)){
			stopCodonPos = stop3;
		}
		return stopCodonPos;
	}
	public static String FindSingleGene(String dna, int searchFromPos){
		String gene = "";
		int codonLen = 3;
		int startCodonPos = FindCodon(dna, "ATG", searchFromPos);
		if(startCodonPos == -1){
			//System.out.println("LOG: v-- No start codon found. --v");
			return "";
		}
		int stopCodon1Pos = FindCodon(dna, "TAA", searchFromPos + codonLen);
		int stopCodon2Pos = FindCodon(dna, "TAA", searchFromPos + codonLen);
		int stopCodon3Pos = FindCodon(dna, "TAA", searchFromPos + codonLen);
		// return empty strings if no stop codon its found or
		// if none of the sequences found is divisible by 3.
		if(stopCodon1Pos == -1 && stopCodon2Pos == -1 && stopCodon3Pos == -1){
			//System.out.println("LOG: v-- No stop codon found. --v");
			return "";
		}
		if( !IsMultipleOfX(stopCodon1Pos - startCodonPos, 3) &&
			!IsMultipleOfX(stopCodon2Pos - startCodonPos, 3) &&
			!IsMultipleOfX(stopCodon3Pos - startCodonPos, 3)){
			//System.out.println("LOG: v-- Sequece(s) found do not have a codon-wise lenght. --v");
			return "";
		}
		// pick the shortest gene with codon-wise length and with codon pos != -1
		int stopCodonPos = SelectStopCodon(startCodonPos,stopCodon1Pos,stopCodon2Pos,stopCodon3Pos);
		if(stopCodonPos == -1){
			//System.out.println("LOG: v-- No valid stop codon/sequence found. --v");
			return "";
		}
		if(startCodonPos < dna.length() &&
          (stopCodonPos + codonLen) <= dna.length() &&
          startCodonPos < (stopCodonPos + codonLen) ){
			gene = dna.substring(startCodonPos,stopCodonPos + codonLen);	
		}
		return gene;
	}
	public static void FindMultipleGenes(String dna){
		// avoid procesing an empty string
		if(dna == ""){
			System.out.println("> dna: empty string");
			System.out.println("//");
			return;
		}
		int searchPos = 0;
		String gene = null;
		int codonLen = 3;
		System.out.println("> dna: " + dna);
		while(gene != "" && searchPos < dna.length() - codonLen * 2 && searchPos != -1){
			gene = FindSingleGene(dna,searchPos);
			if(gene == ""){
				System.out.println("//");
				return ;
			}
			System.out.println("  seq: " + gene);
			System.out.println("  pos: " + searchPos);
			System.out.println("  len: " + gene.length());
			searchPos = dna.indexOf(gene,searchPos) + gene.length();
			if(searchPos < dna.length() - codonLen * 2){
				System.out.println("  ---");	
			}
		}
		System.out.println("//");
	}
    public static void ReadDNAFile(){
    	// reading DNA from file (dna.txt)
        FileResource dnafile = new FileResource();
        for (String dna: dnafile.lines()){
        	// *** UPDATE METHODS USED HERE ***
        	System.out.println("dna:" + dna);
            //String gene = FindGeneSimple(dna);
            //if(gene == ""){System.out.println("//");}
        }
    }
    public static void TestDNAs(){
    	FindMultipleGenes("ATGCCTAA");
    	FindMultipleGenes("ATGCCCTAAATGCCCTAACCCG");
    	FindMultipleGenes("");
    	FindMultipleGenes("ATGCCCTAA");
    }
    public static void main(String[] args){
    	//TestDNAs(); // OK
    	ReadDNAFile(); // <-- work on implementing this method.
    }
}



