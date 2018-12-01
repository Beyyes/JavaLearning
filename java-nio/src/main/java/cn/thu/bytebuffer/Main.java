package cn.thu.bytebuffer;

import java.nio.ByteBuffer;

public class Main {

    public static void main(String[] args) {
        // what's the exist meaning of limit

        testByteBufferApi();
    }

    // wrap, duplicate, slice
    public static void testByteBufferApi() {

        ByteBuffer buffer = ByteBuffer.allocate(10);
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }

        buffer.position(3);
        buffer.limit(7);
        System.out.println("origin: " + buffer);

        ByteBuffer buffer1 = buffer.slice();
        System.out.println("slice: " + buffer1);

        ByteBuffer buffer2 = buffer.duplicate();
        System.out.println("duplicate: " + buffer2);

        ByteBuffer buffer3 = ByteBuffer.wrap(buffer.array());
        System.out.println("wrap: " + buffer3);

//        origin: java.nio.HeapByteBuffer[pos=3 lim=7 cap=10]
//        slice: java.nio.HeapByteBuffer[pos=0 lim=4 cap=4]
//        duplicate: java.nio.HeapByteBuffer[pos=3 lim=7 cap=10]
//        wrap: java.nio.HeapByteBuffer[pos=0 lim=10 cap=10]
    }
}
