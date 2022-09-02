package com.knightboost.apm.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;


public class FileUtil {

    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canRead()) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }

    public static BufferedReader openBuffer(File file){
        if (file == null)
            return null;
        if (!file.exists()) {
//            Log.d("file:" + file.getName() + " not exist");
            return null;
        }
        if (!file.canRead()) {
//            CpuLogger.c("file:" + file.getName() + " can not read");
            return null;
        }
        BufferedReader bufferedReader = null;
        try {
            FileReader fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader, 10000);
        } catch (Exception exception) {
//            CpuLogger.d(Log.getStackTraceString(null));
        }
        return bufferedReader;
    }
    /**
     * safety close bufferedReader
     * @param paramBufferedReader
     */
    public static void close(BufferedReader paramBufferedReader) {
        if (paramBufferedReader != null)
            try {
                paramBufferedReader.close();
                return;
            } catch (Exception exception) {
//                CpuLogger.d(Log.getStackTraceString(null));
            }
    }

    /**
     * Reads the contents of a file line by line to a List of Strings.
     * The file is always closed.
     *
     * @param file  the file to read, must not be {@code null}
     * @param encoding  the encoding to use, {@code null} means platform default
     * @return the list of Strings representing each line in the file, never {@code null}
     * @throws IOException in case of an I/O error
     * @since 2.3
     */
    public static List<String> readLines(File file, Charset encoding) throws IOException {
        InputStream in = null;
        try {
            in = openInputStream(file);
            return IOUtils.readLines(in, encoding);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * Reads the contents of a file line by line to a List of Strings. The file is always closed.
     *
     * @param file
     *            the file to read, must not be {@code null}
     * @param encoding
     *            the encoding to use, {@code null} means platform default
     * @return the list of Strings representing each line in the file, never {@code null}
     * @throws IOException
     *             in case of an I/O error
     */
    public static List<String> readLines(File file, String encoding) throws IOException {
        return readLines(file, Charset.forName(encoding));
    }

    /**
     * Reads the contents of a file line by line to a List of Strings using the default encoding for the VM.
     * The file is always closed.
     *
     * @param file  the file to read, must not be {@code null}
     * @return the list of Strings representing each line in the file, never {@code null}
     * @throws IOException in case of an I/O error
     * @since 1.3
     */
    public static List<String> readLines(File file) throws IOException {
        return readLines(file, Charset.defaultCharset());
    }

    /**
     * Returns an Iterator for the lines in a <code>File</code>.
     * <p>
     * This method opens an <code>InputStream</code> for the file.
     * When you have finished with the iterator you should close the stream
     * to free internal resources. This can be done by calling the
     * {@link LineIterator#close()} or
     * {@link LineIterator#closeQuietly(LineIterator)} method.
     * <p>
     * The recommended usage pattern is:
     * <pre>
     * LineIterator it = FileUtils.lineIterator(file, "UTF-8");
     * try {
     *   while (it.hasNext()) {
     *     String line = it.nextLine();
     *     /// do something with line
     *   }
     * } finally {
     *   LineIterator.closeQuietly(iterator);
     * }
     * </pre>
     * <p>
     * If an exception occurs during the creation of the iterator, the
     * underlying stream is closed.
     *
     * @param file  the file to open for input, must not be {@code null}
     * @param encoding  the encoding to use, {@code null} means platform default
     * @return an Iterator of the lines in the file, never {@code null}
     * @throws IOException in case of an I/O error (file closed)
     * @since 1.2
     */
    public static LineIterator lineIterator(File file, String encoding) throws IOException {
        InputStream in = null;
        try {
            in = openInputStream(file);
            return IOUtils.lineIterator(in, encoding);
        } catch (IOException ex) {
            IOUtils.closeQuietly(in);
            throw ex;
        } catch (RuntimeException ex) {
            IOUtils.closeQuietly(in);
            throw ex;
        }
    }

    /**
     * Returns an Iterator for the lines in a <code>File</code> using the default encoding for the VM.
     *
     * @param file  the file to open for input, must not be {@code null}
     * @return an Iterator of the lines in the file, never {@code null}
     * @throws IOException in case of an I/O error (file closed)
     * @since 1.3
     * @see #lineIterator(File, String)
     */
    public static LineIterator lineIterator(File file) throws IOException {
        return lineIterator(file, null);
    }

    private static void readLines(File file, ReadLineListener parama) {
        if (parama == null)
            return;
        BufferedReader bufferedReader;
        if ((bufferedReader = openBuffer(file)) == null)
            return;
        try {
            String str;
            while ((str = bufferedReader.readLine()) != null)
                parama.onLine(str);
        } catch (IOException iOException) {
            //
        }finally {
            IOUtils.closeQuietly(bufferedReader);
        }
    }

    public static void readLines(String paramString, ReadLineListener reader) {
        if (paramString == null)
            return;
        readLines(new File(paramString), reader);
    }

    public static interface ReadLineListener{
        boolean onLine(String text);
    }
}
