package songs.index.impl;

import java.io.File;
import java.util.*;

public class DirectoryCrawler {

    public static List<File> crawl(File f) {
        var ret = new LinkedList<File>();
        if (f.exists() && f.isDirectory()) {
            var current = new LinkedList<>(
                Arrays.asList(
                    Optional.ofNullable(f.listFiles()).orElse(new File[]{})
                )
            );
            while (!current.isEmpty()) {
                var head = current.pop();
                if (head.isDirectory()) {
                    current.addAll(
                        new LinkedList<>(
                            Arrays.asList(
                                Optional.ofNullable(head.listFiles()).orElse(new File[]{})
                            )
                        )
                    );
                } else {
                    ret.add(head);
                }
            }
        }
        return ret;
    }
}
