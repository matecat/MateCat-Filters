package com.matecat.converter.core.format.converters;

import com.matecat.converter.core.format.Format;
import com.matecat.converter.core.format.FormatNotSupportedException;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;

/**
 * Format converter based in a socket connection
 *
 * This kind of format converter acts as a communication interface when the format converter
 * is developed on other language or language or deployed in another machine.
 *
 * The converted is based on lazy connection: it does not request the connection until it receives a conversion
 * task, and it closes it after it finishes.
 *
 * The communication flow is as following
 * 1. Client sends the input format code
 * 2. Client sends the output format code
 * 3. Client sends the file length
 * 4. Client sends the file contents
 * 5. Server sends the status code
 *   - 0: Success
 *   - 1: Error: format not supported
 *   - 2: Error: file too big
 *   - 3: Error: file broken in the transfer
 *   - 4: Error: converted file is too big
 *   - 5: Error: internal server error
 *   - 6: Error: unknown error
 * If success:
 * 6. Server sends the converted file length
 * 7. Server sends the converted file contents
 *
 * Note: by default, the class does not use little endian encoding in the communication, which can be changed
 * with the 'useLittleEndian' parameter in the constructor.
 */
public abstract class SocketFormatConverter extends AbstractFormatConverter {

    // Connection variables
    private String host;
    private int port;

    // Server
    Socket server;
    boolean useLittleEndian;

    // Input / Output stream
    DataInputStream inputStream;
    DataOutputStream outputStream;


    /* CONSTRUCTORS */

    /**
     * Socket format constructor
     * @param host Server host
     * @param port Server port
     * @param useLittleEndian Use big endian encoding for the server
     */
    protected SocketFormatConverter(String host, int port, boolean useLittleEndian) {
        this.host = host;
        this.port = port;
        this.useLittleEndian = useLittleEndian;
    }

    /**
     * Socket format constructor, using the default value 'false' for useLittleEndian
     * @param host Server host
     * @param port Server port
     */
    protected SocketFormatConverter(String host, int port) {
        this(host, port, false);
    }


    /* CONNECTION FUNCTIONS */

    /**
     * Connect to the server, using the host and port variables defined in the converters' constructor
     * @throws IOException
     */
    private void connect() throws IOException {
        server = new Socket(host, port);
        inputStream = new DataInputStream(server.getInputStream());
        outputStream = new DataOutputStream(server.getOutputStream());
    }


    /**
     * Close the connection with the server
     * @throws IOException
     */
    private void close() throws IOException {
        if (inputStream != null)
            inputStream.close();
        if (outputStream != null)
            outputStream.close();
        if (server != null)
            server.close();
    }


    /* AUXILIARY BYTE FUNCTIONS */

    /**
     * Conver an integer into four bytes
     * @param msg Number to convert
     * @return Bytes representation of the number
     */
    protected byte[] toFourBytes(Integer msg) {
        return ByteBuffer.allocate(4).putInt(msg).array();
    }


    /**
     * Reorder the byte parameter into little endian
     * @param bytes Bytes to be reordered
     * @return Bytes in little endian order
     */
    protected byte[] toLittleEndian(final byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.array();
    }


    /* WRITE / READ OPERATIONS */

    /**
     * Send an integer
     * @param msg Integer to send
     * @throws IOException
     */
    private void write(Integer msg) throws IOException {
        byte[] bytes = toFourBytes(msg);
        if (useLittleEndian)
            bytes = toLittleEndian(bytes);
        outputStream.write(bytes);
    }


    /**
     * Send bytes
     * @param bytes Bytes to send
     * @throws IOException
     */
    private void write(byte[] bytes) throws IOException {
        if (useLittleEndian)
            bytes = toLittleEndian(bytes);
        outputStream.write(bytes);
    }


    /**
     * Send the format code of the format
     * @param format Format we want to send
     * @throws IOException
     */
    private void write(Format format) throws IOException {
        write(getFormatCode(format));
    }


    /**
     * Send a file
     * @param file File to send
     * @throws IOException
     */
    private void write(File file) throws IOException {

        // Obtain the bytes to send
        byte[] bytes = Files.readAllBytes(file.toPath());

        // Send the length of the contents
        write(bytes.length);

        // Send the contents
        write(bytes);

    }


    /**
     * Read an integer from the socket
     * @return Read integer
     * @throws IOException
     */
    private int readInt() throws IOException {
        return inputStream.readInt();
    }


    /**
     * Read a file from the socket
     * @param outputPath Path were the file will be saved
     * @return Read file
     * @throws IOException
     */
    private File readFile(String outputPath) throws IOException {

        // Read size
        int fileSize = readInt();

        // Read bytes
        byte[] bytes = new byte[fileSize];
        inputStream.readFully(bytes);

        // Write bytes in the specified address
        FileOutputStream fileOuputStream = new FileOutputStream(outputPath);
        fileOuputStream.write(bytes);
        fileOuputStream.close();

        // Create the file and check that it exists
        File outfile = new File(outputPath);
        if (!outfile.exists())
            throw new RuntimeException("The converted file could not be saved");

        // Return the file
        return outfile;
    }


    /* CONVERSION */

    /**
     * Get the format code of the given format
     * @param format Input format
     * @return Format's code
     */
    protected abstract int getFormatCode(Format format);


    /**
     * Convert a file
     * @param file Input file
     * @param outputFormat Format we want to convert the file to
     * @return Converted file
     */
    public File convert(final File file, Format outputFormat) {

        // Check that the file exist
        if (file == null  ||  !file.exists())
            throw new IllegalArgumentException("The given file cannot be null");

        // Parse the input format and check that the conversion is valid
        Format inputFormat = Format.getFormat(file);

        // Check that the conversion is possible
        if (!isConvertible(inputFormat, outputFormat))
            throw new FormatNotSupportedException(inputFormat, outputFormat);

        // Converted file
        File outfile = null;
        try {

            // Connect, set parameters and send the file
            connect();
            write(inputFormat);
            write(outputFormat);
            write(file);

            // Receive status code
            int statusCode = readInt();

            // Process errors
            if (statusCode != 0) {
                switch (statusCode) {
                    case 1:
                        throw new FormatNotSupportedException(inputFormat);
                    case 2:
                        throw new FileTooBigException(file.getName());
                    case 3:
                        throw new ConverterException("the file is broken");
                    case 4:
                        throw new FileTooBigException(file.getName() + "(converted version)");
                    case 5:
                        throw new ConverterException("internal server error");
                    default:
                        throw new ConverterException("unknown status code: " + statusCode);
                }
            }

            // Generate the output path
            String filename = file.getName();
            filename += "." + outputFormat.toString();
            String outputPath = file.getParentFile().getPath() + File.separator + filename;

            // Save the file
            outfile = readFile(outputPath);

        }

        // If the communication was not established
        catch (ConnectException e) {
            throw new ConverterException("it was not possible to connect with the conversion server");
        }

        // Other exceptions should not occur
        catch (IOException ignored) {}

        // Close the connection and readers
        finally {
            try {
                close();
            } catch (IOException ignored) { }
        }

        // Check that the file was converted
        if (outfile == null)
            throw new ConverterException("unknown error");

        // Return the file
        return outfile;
    }

}