package maks.molch.dmitr.interaction.file;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileObjectTest {

    @Test
    public void serializeTest() throws IOException, ClassNotFoundException {
        Path projectPath = Path.of("").toAbsolutePath();
        FileObject fileObject = FileInteractor.getFileObject(projectPath.toFile());
        byte[] bytes = fileObject.toBytes();
        FileObject fileObject1 = FileObject.fromBytes(bytes);
        System.out.println(fileObject1);
        assertEquals(fileObject.toString(), fileObject1.toString());
    }

}