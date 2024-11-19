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
        String text = BinaryStdIn.readString();
        int len = text.length();
        int currentChar = 0;
        int seqLength = 0;
        // Write how many 0s, then how many 1s, etc
        int i = 0;
        while (i < len){
            // Looks at next 16 bits
            for (int j = i; j < 16; j++) {
                // If we continue with the sequence of same value, increment
                if (text.charAt(j) == currentChar) {
                    seqLength++;
                } else {
                    // If the sequence ends, break
                    break;
                }
            }
            // Writes the # of 0s or 1s in the sequence
            BinaryStdOut.write(seqLength, BITS);
            // Switch current char
            currentChar = 1 - currentChar;
            // Increment i
            i += seqLength;
        }
        //identify the length of consecutive 0s or 1s
        // then write the number of 0s/1s as (number).0 or (number).1

        //ID the starting value: 0 or 1?
        // write that first
        // find the length of consecutive values like that, and then write that in binary
        // EX: you have 32 successive 0s. Would be written as "0", then "number__"
        // Actually maybe I should put a limit. 16, since this can be represented with only 4 bits.
        // Write UP TO 16 bits successively.

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