package Practise;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FileStorageService {

    /*
     * userId -> (fileName -> FileEntry)
     */
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, FileEntry>> store =
            new ConcurrentHashMap<>();

    // configurable max versions per file
    private static final int MAX_VERSIONS = 5;

    // Upload creates a new version every time
    public void upload(String userId, String fileName, byte[] content) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(fileName, "fileName must not be null");
        Objects.requireNonNull(content, "content must not be null");

        ConcurrentHashMap<String, FileEntry> userFiles =
                store.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());

        FileEntry entry =
                userFiles.computeIfAbsent(fileName, k -> new FileEntry(MAX_VERSIONS));

        entry.addVersion(content);
    }

    // Download a specific version; if version <= 0 → latest
    public byte[] download(String userId, String fileName, int version) {
        ConcurrentHashMap<String, FileEntry> userFiles = store.get(userId);
        if (userFiles == null) return null;

        FileEntry entry = userFiles.get(fileName);
        if (entry == null) return null;

        return entry.getVersion(version);
    }

    // Delete all versions of a file
    public void delete(String userId, String fileName) {
        ConcurrentHashMap<String, FileEntry> userFiles = store.get(userId);
        if (userFiles != null) {
            userFiles.remove(fileName);
        }
    }

    // List all available versions (sorted)
    public List<Integer> listVersions(String userId, String fileName) {
        ConcurrentHashMap<String, FileEntry> userFiles = store.get(userId);
        if (userFiles == null) return Collections.emptyList();

        FileEntry entry = userFiles.get(fileName);
        if (entry == null) return Collections.emptyList();

        return entry.listVersions();
    }
}

/*
 * Represents a single file and its versions
 */
class FileEntry {

    private final AtomicInteger versionCounter = new AtomicInteger(0);
    private final ConcurrentSkipListMap<Integer, byte[]> versions =  new ConcurrentSkipListMap<>();

    private final int maxVersions;

    FileEntry(int maxVersions) {
        this.maxVersions = maxVersions;
    }

    // Atomic version creation + eviction
    synchronized void addVersion(byte[] content) {
        int version = versionCounter.incrementAndGet();
        versions.put(version, Arrays.copyOf(content, content.length));

        // Evict oldest versions if limit exceeded
        while (versions.size() > maxVersions) {
            versions.pollFirstEntry();
        }
    }

    // Get version (<=0 means latest)
    byte[] getVersion(int version) {
        if (versions.isEmpty()) return null;

        if (version <= 0) {
            Map.Entry<Integer, byte[]> last = versions.lastEntry();
            return Arrays.copyOf(last.getValue(), last.getValue().length);
        }

        byte[] data = versions.get(version);
        return data == null ? null : Arrays.copyOf(data, data.length);
    }

    List<Integer> listVersions() {
        return new ArrayList<>(versions.keySet());
    }
}
