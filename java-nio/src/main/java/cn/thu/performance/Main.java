package cn.thu.performance;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Main {
    private static String fileName = "compare.tmp";
    static int num = 2000 * 2000;

    public static void main(String[] args) throws IOException {
        useStream();
        useNioByteBuffer();
        useMMap();
    }

    // using streaming io to read and write, about 1000ms
    public static void useStream() throws IOException {

        long startTime = System.currentTimeMillis();

        // buffer output stream
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
        for (int i = 0; i < num; i++) {
            dataOutputStream.writeInt(i);
        }
        dataOutputStream.close();

        int data = 0;

        // buffer input stream
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName)));
        try {
            while (true) {
                data = in.readInt();
            }
        } catch (EOFException e) {
            System.out.println("stream read complete: " + data);
        }
        in.close();
        long endTime = System.currentTimeMillis();
        System.out.println("buffer stream use time : " + (endTime - startTime));
        System.out.println();
    }

    // using NIO ByteBuffer. about 220ms
    public static void useNioByteBuffer() throws IOException {
        long startTime = System.currentTimeMillis();

        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        // NIO channel
        FileChannel fileChannel = fileOutputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(num * 4);
        for (int i = 0; i < num; i++) {
            buffer.putInt(i);
        }

        buffer.flip();

        fileChannel.write(buffer);
        fileChannel.close();

        ByteBuffer buffer1 = ByteBuffer.allocate(num * 4);
        FileInputStream in = new FileInputStream(fileName);
        FileChannel fin = in.getChannel();
        buffer1.clear();
        fin.read(buffer1);
        int data = 0;
        buffer1.flip();
        while (buffer1.hasRemaining()) {
            data = buffer1.getInt();
        }

        fin.close();
        System.out.println("NIO data size : " + data);
        long endTime = System.currentTimeMillis();
        System.out.println("NIO use time : " + (endTime - startTime));
        System.out.println();
    }


    // mmap about 65ms
    public static void useMMap() throws IOException {
        long startTime = System.currentTimeMillis();

        RandomAccessFile file = new RandomAccessFile(fileName, "rw");

        FileChannel fileChannel = file.getChannel();

        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, num * 4);

        // write file
        for (int i = 0; i < num; i++) {
            mappedByteBuffer.putInt(i);
        }
        fileChannel.close();

        int data = 0;
        RandomAccessFile file1 = new RandomAccessFile(fileName, "rw");
        FileChannel fc = file1.getChannel();
        MappedByteBuffer mappedByteBuffer1 = fc.map(FileChannel.MapMode.READ_WRITE, 0, file1.length());

        // read file
        while (mappedByteBuffer1.hasRemaining()) {
            data = mappedByteBuffer1.getInt();
        }
        fc.close();
        long endTime = System.currentTimeMillis();
        System.out.println("data size : " + data);
        System.out.println("mmap use time : " + (endTime - startTime));
    }
}
