package com.company.application.network;

import com.company.application.command_package.CommandPackage;
import com.company.compressor.Compressor;
import com.company.serialization.Serialization;

import java.io.*;
import java.net.Socket;


public class NetWorkUtils {

    public static CommandPackage receive(Socket sock) throws IOException {
        var inputStream = new DataInputStream(sock.getInputStream());
        var dataHeader = new byte[inputStream.readInt()];
        var dataAppend = new byte[inputStream.readInt()];

        inputStream.readFully(dataHeader);
        inputStream.readFully(dataAppend);
        var pack = (CommandPackage)Serialization.deserialize(Compressor.decompress(dataHeader));
        pack.setContentData(dataAppend);
        return pack;
    }

    public static void send(CommandPackage cmd, Socket sock) throws IOException {
        var outputStream = new DataOutputStream(sock.getOutputStream());
        var dataAppend = cmd.resetContentData();
        var dataHeader = Compressor.compress(Serialization.serialize(cmd));

        outputStream.writeInt(dataHeader.length);
        outputStream.writeInt(dataAppend.length);
        outputStream.write(dataHeader);
        outputStream.write(dataAppend);
        outputStream.flush();
    }

}
