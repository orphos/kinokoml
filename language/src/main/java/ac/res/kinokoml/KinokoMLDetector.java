package ac.res.kinokoml;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.oracle.truffle.api.TruffleFile;
import com.oracle.truffle.api.TruffleFile.FileTypeDetector;

public class KinokoMLDetector implements FileTypeDetector {

    @Override
    public String findMimeType(TruffleFile file) throws IOException {
        String name = file.getName();
        if (name != null && name.endsWith("kinokoml"))
            return KinokoMLLanguage.MIME_TYPE;
        String line = file.newBufferedReader().readLine();
        if (line != null && line.startsWith("#!") && (line.endsWith("/kimonoml") || line.endsWith(" kimonoml")))
            return KinokoMLLanguage.MIME_TYPE;
        return null;
    }

    @Override
    public Charset findEncoding(TruffleFile file) throws IOException {
        return StandardCharsets.UTF_8;
    }

}