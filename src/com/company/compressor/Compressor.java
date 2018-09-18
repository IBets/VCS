package com.company.compressor;
import com.company.config.ArchiveSettings;
import com.company.config.ConfigurationManager;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.*;



public class Compressor {
    public static byte[] compress(byte[] bytesToCompress) {
        var deflater = new Deflater();
        deflater.setInput(bytesToCompress);
        deflater.finish();
        var bytesCompressed = new byte[Short.MAX_VALUE];
        var numberOfBytesAfterCompression = deflater.deflate(bytesCompressed);
        var returnValues = new byte[numberOfBytesAfterCompression];
        System.arraycopy(bytesCompressed, 0, returnValues, 0, numberOfBytesAfterCompression);
        return returnValues;
    }

    public static byte[] decompress(byte[] bytesToDecompress) {
        byte[] returnValues = null;

        var inflater = new Inflater();
        var numberOfBytesToDecompress = bytesToDecompress.length;
        inflater.setInput(bytesToDecompress, 0, numberOfBytesToDecompress);

        var bufferSizeInBytes = numberOfBytesToDecompress;
        var numberOfBytesDecompressedSoFar = 0;
        var bytesDecompressedSoFar = new ArrayList<Byte>();

        try {
            while (inflater.needsInput() == false) {
                var bytesDecompressedBuffer = new byte[bufferSizeInBytes];
                var numberOfBytesDecompressedThisTime = inflater.inflate(bytesDecompressedBuffer);
                numberOfBytesDecompressedSoFar += numberOfBytesDecompressedThisTime;
                for (int b = 0; b < numberOfBytesDecompressedThisTime; b++)
                    bytesDecompressedSoFar.add(bytesDecompressedBuffer[b]);
            }
            returnValues = new byte[bytesDecompressedSoFar.size()];
            for (int b = 0; b < returnValues.length; b++)
                returnValues[b] = (bytesDecompressedSoFar.get(b));

        }
        catch (DataFormatException dfe) {
            dfe.printStackTrace();
        }

        inflater.end();
        return returnValues;
    }

    public static byte[] compressFiles(File[] files)  {

        var path = (ArchiveSettings)ConfigurationManager.getSection("Archive");

        var byteArrayOutputStream = new ByteArrayOutputStream();
        var zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

        try {
            for (var file : files) {
                var fileName = Paths.get(path.Name).relativize(Paths.get(file.getAbsolutePath())).toString();

                zipOutputStream.putNextEntry(new ZipEntry(fileName));
                var fileInputStream = new FileInputStream(file);
                var buffer = new byte[(int) file.length()];
                fileInputStream.read(buffer);
                zipOutputStream.write(buffer);
            }
            zipOutputStream.closeEntry();
        } catch (Exception e){
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] compressFilesEx(File[] files)  {

        var path = (ArchiveSettings)ConfigurationManager.getSection("Archive");

        var byteArrayOutputStream = new ByteArrayOutputStream();
        var zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

        try {





            for (var file : files) {
                var fileName =  Paths.get(path.Name).relativize(Paths.get(file.getAbsolutePath())).toString();
                var id1 = fileName.indexOf('\\') + 1;
                var id2 = fileName.indexOf('\\', id1) + 1;

                zipOutputStream.putNextEntry(new ZipEntry(fileName.substring(id2)));
                var fileInputStream = new FileInputStream(file);
                var buffer = new byte[(int) file.length()];
                fileInputStream.read(buffer);
                zipOutputStream.write(buffer);
            }
            zipOutputStream.closeEntry();
        } catch (Exception e){
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }


    public static Map<String, byte[]> decompressFile(byte[] data){
        var files = new HashMap<String, byte[]>();

        if (data != null) {
            var byteArrayInputStream = new ByteArrayInputStream(data);
            var zipInputStream = new ZipInputStream(byteArrayInputStream);

            ZipEntry zipEntry;
            String fileName;
            try {
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    fileName = zipEntry.getName();
                    var byteArrayOutputStream = new ByteArrayOutputStream();
                    for (int c = zipInputStream.read(); c != -1; c = zipInputStream.read())
                        byteArrayOutputStream.write(c);
                    var file = byteArrayOutputStream.toByteArray();
                    files.put(fileName, file);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return files;
    }

}