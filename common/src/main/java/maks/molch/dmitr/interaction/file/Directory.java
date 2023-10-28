package maks.molch.dmitr.interaction.file;

import java.util.ArrayList;
import java.util.List;

public final class Directory extends FileObject {
    private final List<FileObject> fileObjects = new ArrayList<>();

    public Directory(String name) {
        super(name);
    }

    public List<FileObject> getFileObjects() {
        return fileObjects;
    }

    @Override
    public String toString() {
        return toString(0);
    }

    private String toString(int depth) {
        StringBuilder res = new StringBuilder("Directory '%s'".formatted(this.getName()));
        for (FileObject fileObject : this.getFileObjects()) {
            res
                    .append("\n")
                    .append("---".repeat(depth+1))
                    .append(
                            fileObject instanceof Directory directory ? directory.toString(depth + 1) : fileObject.toString()
                    );
        }
        return res.toString();
    }

    public void addFileObject(FileObject fileObject) {
        fileObjects.add(fileObject);
    }
}
