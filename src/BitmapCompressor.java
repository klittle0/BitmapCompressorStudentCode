/******************************************************************************
 *  Compilation:  javac BitmapCompressor.java
 *  Execution:    java BitmapCompressor - < input.bin   (compress)
 *  Execution:    java BitmapCompressor + < input.bin   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   q32x48.bin
 *                q64x96.bin
 *                mystery.bin
 *
 *  Compress or expand binary input from standard input.
 *
 *  % java DumpBinary 0 < mystery.bin
 *  8000 bits
 *
 *  % java BitmapCompressor - < mystery.bin | java DumpBinary 0
 *  1240 bits
 ******************************************************************************/

/**
 *  The {@code BitmapCompressor} class provides static methods for compressing
 *  and expanding a binary bitmap input.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Zach Blick
 *  @author Kate Little
 */
public class BitmapCompressor {
    static final int BITS = 4;

    /**
     * Reads a sequence of bits from standard input, compresses them,
     * and writes the results to standard output.
     */
    public static void compress() {
        int currentChar = 0;
        int seqLength = 0;
        // Keep going until file is empty
        while (!BinaryStdIn.isEmpty()){
            int bit = BinaryStdIn.readInt(1);
            // Looks at next 15 bits
            int index = seqLength;
            // For up to 15 bits
            if (seqLength < 16) {
                // Check if it's equal to the current 0 or 1
                if (bit == currentChar) {
                    seqLength++;
                }
                // If not, write & reset everything
                else {
                    // Writes the # of 0s or 1s in the sequence
                    BinaryStdOut.write(seqLength, BITS);
                    // Switch current char
                    currentChar = 1 - currentChar;
                    seqLength = 0;
                }
            }
        }
        BinaryStdOut.close();
    }

    /**
     * Reads a sequence of bits from standard input, decodes it,
     * and writes the results to standard output.
     */
    public static void expand() {
        // get a section of 4 bits at a time, and
        int currentChar = 0;
        while (!BinaryStdIn.isEmpty()){
            // takes 4 bits at a time
            int section = BinaryStdIn.readInt(BITS);
            // Writes all bits
            for (int i = 0; i < section; i++){
                BinaryStdOut.write(currentChar, 1);
            }
            currentChar = 1 - currentChar;
        }
        BinaryStdOut.close();
    }

    /**
     * When executed at the command-line, run {@code compress()} if the command-line
     * argument is "-" and {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}