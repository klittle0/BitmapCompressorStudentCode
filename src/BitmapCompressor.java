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
    static final int BYTE = 8;
    static final int MAX = 255;
    static final int BIT = 1;
    /**
     * Reads a sequence of bits from standard input, compresses them,
     * and writes the results to standard output.
     */
    public static void compress() {
        int currentBit = 0;
        int seqLength = 0;
        // Check each bit until file is empty
        while (!BinaryStdIn.isEmpty()){
            int bit = BinaryStdIn.readInt(BIT);
            // Current bit needs to change from 0 to 1, or vice versa
            if (bit != currentBit){
                BinaryStdOut.write(seqLength, BYTE);
                seqLength = 0;
                // Switch current bit —> 0 to 1, or 1 to 0
                currentBit = 1 - currentBit;
            }
            // If we have maxed out, write out maximum & keep checking for current bit
            if (bit == currentBit && seqLength == MAX){
                BinaryStdOut.write(seqLength, BYTE);
                // Assume that there are more than 255 0s, so keep looking for 0s
                BinaryStdOut.write(0, BYTE);
                seqLength = 0;
            }
            seqLength++;

        }
        // Write out the remaining/final sequence
        BinaryStdOut.write(seqLength, BYTE);
        BinaryStdOut.close();
    }
    /**
     * Reads a sequence of bits from standard input, decodes it,
     * and writes the results to standard output.
     */
    public static void expand() {
        int currentBit = 0;
        while (!BinaryStdIn.isEmpty()){
            // Read a byte at a time
            int seqLength= BinaryStdIn.readInt(BYTE);
            // Print the full sequence of the current bit
            for (int i = 0; i < seqLength; i++){
                BinaryStdOut.write(currentBit, BIT);
            }
            currentBit = 1 - currentBit;
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