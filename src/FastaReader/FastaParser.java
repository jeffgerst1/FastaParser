package FastaReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FastaParser {

    // FastaSequence class
    public static class FastaSequence {
        private String header;
        private String sequence;

        public FastaSequence(String header, String sequence) {
            this.header = header;
            this.sequence = sequence;
        }

        public String getHeader() {
            return header;
        }

        public String getSequence() {
            return sequence;
        }

        public float getGCRatio() {
            int GCcount = 0;
            for (char nucleotide : sequence.toCharArray()) {
                if (nucleotide == 'G' || nucleotide == 'C') {
                    GCcount++;
                }
            }
            return (float) GCcount / sequence.length();
        }
    }

    public static List<FastaSequence> readFastaFile(String filepath) throws IOException {
        List<FastaSequence> fastaSequences = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filepath));

        String header = null, line;
        StringBuilder sequence = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            if (line.startsWith(">")) {
                if (header != null) {
                    fastaSequences.add(new FastaSequence(header, sequence.toString()));
                }
                header = line.substring(1); // Remove '>'
                sequence.setLength(0); // Reset the sequence for the new header
            } else {
                sequence.append(line.trim());
            }
        }
        if (header != null) {
            fastaSequences.add(new FastaSequence(header, sequence.toString()));
        }

        reader.close();
        return fastaSequences;
    }

    public static void writeTableSummary(List<FastaSequence> list, File outputFile) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.write("Sequence_ID\tNumA\tNumC\tNumG\tNumT\n");

        for (FastaSequence fs : list) {
            String header = fs.getHeader();
            String sequence = fs.getSequence();
            int countA = 0, countC = 0, countG = 0, countT = 0;

            for (char nucleotide : sequence.toCharArray()) {
                switch (nucleotide) {
                    case 'A':
                        countA++;
                        break;
                    case 'C':
                        countC++;
                        break;
                    case 'G':
                        countG++;
                        break;
                    case 'T':
                        countT++;
                        break;
                }
            }

            writer.write(String.format("%s\t%d\t%d\t%d\t%d\n", header, countA, countC, countG, countT));
        }

        writer.close();
    }

    public static void main(String[] args) throws Exception {
        List<FastaSequence> fastaList = FastaParser.readFastaFile("c:\\pointsToSome\\FastaFile.txt");

        System.out.println("Raw FASTA Sequences:");
        for (FastaSequence fs : fastaList) {
            System.out.println("Header: " + fs.getHeader());
            System.out.println("Sequence: " + fs.getSequence());
            System.out.println(); // Print a blank line for better readability
        }

        File myFile = new File("c:\\yourFilePathHere\\out.txt");

        FastaParser.writeTableSummary(fastaList, myFile);
    }
}
