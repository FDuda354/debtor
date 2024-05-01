package pl.dudios.debtor.utils;

import com.github.slugify.Slugify;
import org.apache.commons.io.FilenameUtils;

public class SlugifyUtils {

    private SlugifyUtils() {
    }

    public static String slugifyFileName(String fileName) {
        String name = FilenameUtils.getBaseName(fileName);
        Slugify slg = new Slugify();
        String changedName = slg
                .withCustomReplacement("_", "-")
                .slugify(name);
        return changedName + "." + FilenameUtils.getExtension(fileName);
    }

    public static String slugifySlug(String slug) {
        return new Slugify()
                .withCustomReplacement("_", "-")
                .slugify(slug);
    }
}