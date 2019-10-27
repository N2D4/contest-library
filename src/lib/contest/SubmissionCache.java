/* BEGIN-OPTIMIZER */ // we only want to include this with optimizers as only optimizers use this class during runtime

package lib.contest;

import lib.utils.tuples.Triple;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class SubmissionCache extends HashMap<Triple<Class<?>, String, Integer>, Object> {

    public static SubmissionCache readCache(Class<? extends AbstractSubmission> clss, Path path) throws IOException {
        if (!Files.exists(path)) return null;

        try (
                ZipInputStream zip = new ZipInputStream(Files.newInputStream(path))
        ) {
            zip.getNextEntry();
            try (ObjectInputStream in = new ObjectInputStream(zip)) {
                Object s = in.readObject();
                if (!AbstractSubmission.getCacheVersion(clss).equals(s)) return null;
                Object obj = in.readObject();
                if (obj instanceof Map) {
                    return (SubmissionCache) obj;
                } else {
                    return null;
                }
            }
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static void writeCache(Class<? extends AbstractSubmission> clss, SubmissionCache cache, Path path) throws IOException, IllegalAccessException {
        if (cache == null || cache.isEmpty()) {
            if (Files.exists(path)) Files.delete(path);
            return;
        }

        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(path))) {
            zip.putNextEntry(new ZipEntry("data.cache"));
            try (ObjectOutputStream out = new ObjectOutputStream(zip)) {
                out.writeObject(AbstractSubmission.getCacheVersion(clss));
                out.writeObject(cache);
                zip.closeEntry();
            }
        }
    }

    public static void writeCache(AbstractSubmission submission, Path path) throws IOException, IllegalAccessException {
        writeCache(submission.getClass(), submission.getCache(), path);
    }
}

/* END-OPTIMIZER */