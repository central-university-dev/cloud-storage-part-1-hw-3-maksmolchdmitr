package maks.molch.dmitr.interaction.file;

import java.io.*;

public sealed class FileObject implements Serializable permits Directory, SimpleFile {
    private static final byte DIRECTORY_BYTE = 0;
    private static final byte FILE_BYTE = 1;

    private final String name;

    protected FileObject(String name) {
        this.name = name;
    }

    protected String getName() {
        return this.name;
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
        if (this instanceof Directory || this instanceof SimpleFile) {
            oos.writeByte(this instanceof Directory ? DIRECTORY_BYTE : FILE_BYTE);
            oos.writeObject(this);
        } else throw new IllegalStateException("Unsupported FileObject subclass: " + this.getClass());
        oos.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static FileObject fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream);
        byte objectType = ois.readByte();
        FileObject fileObject = getFileObject(objectType, ois);
        ois.close();
        return fileObject;
    }

    private static FileObject getFileObject(byte objectType, ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return switch (objectType) {
            case DIRECTORY_BYTE -> (Directory) ois.readObject();
            case FILE_BYTE -> (SimpleFile) ois.readObject();
            default -> throw new IllegalStateException("Unknown object type byte: " + objectType);
        };
    }
}
