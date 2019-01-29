package com.matecat.converter.core.winconverter;

import com.matecat.converter.core.Format;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class WinConverterClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(WinConverterClient.class);
    private static final int FILE_BUFFER_SIZE = 10*1024*1024; // 10 MB
    private static final int SOCKET_TIMEOUT = 5000; // in milliseconds
    private static final int FILE_CONVERSION_TIMEOUT = 15*60*1000; // in milliseconds

    private final Random random = new Random();
    private final InetSocketAddress address;

    // Supported extensions
    public static final List<Format> supportedFormats =
            Collections.unmodifiableList(Arrays.asList(

            // Word formats

            Format.DOC,   // 0
            Format.DOT,   // 1

            Format.DOCX,  // ...
            Format.DOCM,

            Format.DOTX,
            Format.DOTM,

            Format.RTF,

            // Excel formats

            Format.XLS,
            Format.XLT,

            Format.XLSX,
            Format.XLSM,

            Format.XLTX,
            Format.XLTM,

            // Powerpoint formats

            Format.PPT,
            Format.PPS,
            Format.POT,

            Format.PPTX,
            Format.PPTM,

            Format.PPSX,
            Format.PPSM,

            Format.POTX,
            Format.POTM,

            // PDF & OCR formats

            Format.PDF,
            Format.BMP,
            Format.GIF,
            Format.PNG,
            Format.JPEG,
            Format.TIFF
    ));

    public WinConverterClient(InetSocketAddress serverAddress) {
        this.address = serverAddress;
    }

    public File convert(final File file, Format outputFormat) throws IOException, WinConverterException {

        // Check that the file exist
        if (file == null  ||  !file.exists())
            throw new IllegalArgumentException("The given file cannot be null");

        // Parse the input format and check that the conversion is valid
        Format inputFormat = Format.getFormat(file);

        // Converted file
        File outFile = null;

        Socket server = null;
        DataInputStream inputStream = null;
        DataOutputStream outputStream = null;

        // Send a non-zero conversion ID to track conversions between
        // this converter and the Windows converter
        final int conversionId = random.nextInt(Integer.MAX_VALUE - 1) + 1;
        LOGGER.info("Using WinConverter at " + address.toString() + " (conversion id: " + conversionId + ")");

        try {

            // Connect to the server
            server = new Socket();
            server.setSoTimeout(SOCKET_TIMEOUT);
            server.connect(address, SOCKET_TIMEOUT);

            inputStream = new DataInputStream(server.getInputStream());
            outputStream = new DataOutputStream(server.getOutputStream());

            outputStream.writeInt(conversionId);

            // Send conversion input and output formats
            int inputFormatCode = supportedFormats.indexOf(inputFormat);
            int outputFormatCode = supportedFormats.indexOf(outputFormat);
            outputStream.writeInt(inputFormatCode);
            outputStream.writeInt(outputFormatCode);

            // Obtain the bytes to send
            // Send the file size
            outputStream.writeInt((int)file.length());

            // Create buffer for file sending/receiving
            byte[] chunk = new byte[FILE_BUFFER_SIZE];
            int chunkSize;

            // Send the source file
            FileInputStream fileInputStream = new FileInputStream(file);
            while ((chunkSize = fileInputStream.read(chunk)) != -1) {
                outputStream.write(chunk, 0, chunkSize);
            }
            fileInputStream.close();

            // Receive status code
            server.setSoTimeout(FILE_CONVERSION_TIMEOUT);
            int statusCode = inputStream.readInt();
            server.setSoTimeout(SOCKET_TIMEOUT);

            // Process errors
            if (statusCode != 0) {
                switch (statusCode) {
                    case 1:
                        throw new WinConverterException("WinConverter error "+ statusCode +": unknown file type received (sent "+ inputFormat + " and " + outputFormat + ")");
                    case 2:
                        throw new WinConverterException("WinConverter error "+ statusCode +": wrong source file size received");
                    case 3:
                        throw new WinConverterException("WinConverter error "+ statusCode +": error opening the source file (file broken?)");
                    case 4:
                        throw new WinConverterException("WinConverter error "+ statusCode +": converted file exceeds size limit");
                    case 5:
                        throw new WinConverterException("WinConverter error "+ statusCode +": internal error");
                    case 6:
                        throw new WinConverterException("WinConverter error "+ statusCode +": conversion from " + inputFormat + " to " + outputFormat + " is not supported");
                    default:
                        throw new WinConverterException("Unknown WinConverter error ("+ statusCode +")");
                }
            }

            // Read output file size
            int fileSize = inputStream.readInt();

            // Generate the output path
            String filename = file.getName();
            int lastDotIndex = filename.lastIndexOf(".");
            filename = file.getName().substring(0, lastDotIndex) + "." + outputFormat.toString();
            String outputPath = file.getParentFile().getPath() + File.separator + filename;
            outFile = new File(outputPath);

            // Read the output file and stream it to disk
            FileOutputStream fileOutputStream = new FileOutputStream(outFile);
            int writtenBytes = 0;
            while (writtenBytes < fileSize) {
                chunkSize = inputStream.read(chunk);
                fileOutputStream.write(chunk, 0, chunkSize);
                writtenBytes += chunkSize;
            }
            fileOutputStream.close();
        }

        // Close the connection and readers
        finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
                if (server != null) server.close();
            } catch (IOException ignored) { }
        }

        // Return the file
        return outFile;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public class WinConverterException extends Exception {
        public WinConverterException(String message) {
            super(message);
        }
    }

}