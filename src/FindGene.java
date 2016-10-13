
/**
 * Write a description of FindGeneSimpleAndTest here.
 * 
 * @author (Manuel Alonso) 
 * @version (Oct 2016)
 */
import edu.duke.*;

public class FindGene{
	//codon length
	static int codonLen = 3;
	//counting genes
	static int genesCount = 0;
	//storing genes
	static StorageResource genes_found = new StorageResource();
	public static void processGenes(StorageResource genes){
		int length_longest_gene = 0;
		int long_gene_threshold = 60;
		int count_long_genes = 0;
		double high_cgratio_threshold = 0.35;
		int count_high_cgratio_genes = 0;
		double highest_cgratio = 0;
		
		System.out.println("\nprocessGenes()\n");
		
		// Finding long genes
		System.out.println("Long genes (>" + long_gene_threshold + " bases):");
		for(String gene : genes.data()){
			int g_len = gene.length(); 
			if( g_len > long_gene_threshold){
				//count long genes
				count_long_genes++;
				//print gene
				System.out.println("  " + gene);
				// finding longest
				if(length_longest_gene < g_len){
					length_longest_gene = g_len;
				}
			}
		}
		System.out.println("  Number of long genes:" + count_long_genes);
		System.out.println("  Bases of longest gene:" + length_longest_gene);
		
		System.out.println("");
		
		// Finding high CG-ratio genes
		System.out.println("High CG-ratio genes (>" + high_cgratio_threshold + "):");
		for(String gene : genes.data()){
			double ratio = cgRatio(gene);
			if(ratio > high_cgratio_threshold){
				//count high cgratio genes
				count_high_cgratio_genes++;
				//print gene
				System.out.println("  " + gene);
				// finding highest
				if(ratio > highest_cgratio){
					highest_cgratio = ratio;
				}
			}
		}
		System.out.println("  Number high cg-ratio genes:" + count_high_cgratio_genes);
		System.out.println("  Highest cg-ratio:" + String.format( "%.2f", highest_cgratio));
	}
	public static int countCodonOccurrences(String gene, String codon){
		int occurrences = 0;
		int searchPos = 0;		
		if(gene.length() < 9 || codon.length() < 3){
			return -1;
		}
		while(true){
			searchPos = gene.indexOf(codon,searchPos);
			if(searchPos != -1 && searchPos <= gene.length() - codonLen * 2 && IsMultipleOfX(searchPos,3)){
				occurrences++;
				searchPos+=codonLen;
			}else{
				break;
			}
		}
		return occurrences;
	}
	public static float cgRatio(String gene){
		int cg = 0;
		float ratio = 0;
		for (char base: gene.toUpperCase().toCharArray()){
			if(base == 'C' || base == 'G'){
				cg++;	
			}
		}
		if(cg == 0  || gene.length() == 0){
			ratio = 0;
		}else{
			ratio = (float) cg/gene.length();
		}
		return ratio;
	}
	public static void PrintGenesFound(){
		for (String s : genes_found.data()){
			System.out.println(">" + s);
		}
	}
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
		int startCodonPos = FindCodon(dna, "ATG", searchFromPos);
		if(startCodonPos == -1){
			//System.out.println("LOG: v-- No start codon found. --v");
			return "";
		}
		int stopCodon1Pos = FindCodon(dna, "TAA", searchFromPos + codonLen);
		int stopCodon2Pos = FindCodon(dna, "TAG", searchFromPos + codonLen);
		int stopCodon3Pos = FindCodon(dna, "TGA", searchFromPos + codonLen);
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
		System.out.println("  DNA len: " + dna.length());
		int searchPos = 0;
		String gene = null;
		int codonLen = 3;
		dna = dna.toUpperCase();
		System.out.println("> dna: " + dna);
		while(gene != "" && searchPos < dna.length() - codonLen * 2 && searchPos != -1){
			gene = FindSingleGene(dna,searchPos);
			if(gene == ""){
				System.out.println("//");
				return ;
			}
			// StorageResource() object to save the genes
			genes_found.add(gene);
			System.out.println("  seq: " + gene);
			System.out.println("  pos: " + searchPos);
			System.out.println("  len: " + gene.length());
			System.out.println("  cgR: " + String.format( "%.2f", cgRatio(gene)));
			System.out.println(" CTGs: " + countCodonOccurrences(gene, "CTG"));
			genesCount++;
			searchPos = dna.indexOf(gene,searchPos) + gene.length();
			if(searchPos < dna.length() - codonLen * 2){
				System.out.println("  ---");	
			}
		}
		System.out.println("//");
	}
    public static void ReadDNAFile(String path){
    	// reading DNA from file (dna.txt)
        FileResource dnafile = new FileResource(path);
        //FindMultipleGenes(dnafile.asString());
        for (String dna: dnafile.lines()){
        	FindMultipleGenes(dna);
        }
    }
    public static void TestDNAs(){
    	FindMultipleGenes("ggatggggccctaaatgccctagcccgatgcccgggaaattttga");
    	/*
    	FindMultipleGenes("GGATGCCTAA");
    	FindMultipleGenes("GGATGCCCTAAATGCCCTAGCCCGATGCCCCCCTGA");
    	FindMultipleGenes("");
    	FindMultipleGenes("GGATGAACTGATAG");
    	FindMultipleGenes("GGATGAACCTGCTGAAACTGTAG");
    	*/
    	
    }
    public static void main(String[] args){
    	// Use only one of the following methods
    	//TestDNAs();
    	// OR 
    	//ReadDNAFile("brca1line.fa");
    	ReadDNAFile("brca1line_s.fa");
    	//ReadDNAFile("testseq.fa");
    	
    	System.out.println("\nTotal genes found: " + genesCount);
    	PrintGenesFound();
    	processGenes(genes_found);
    }
}



