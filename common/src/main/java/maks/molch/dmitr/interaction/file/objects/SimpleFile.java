package maks.molch.dmitr.interaction.file.objects;

/**
 * Some simple file
 */
public final class SimpleFile extends FileObject {
    public SimpleFile(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "File '%s'".formatted(this.getName());
    }
}
