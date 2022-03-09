import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BooleanSearchEngine implements SearchEngine {
    private final Map<String, List<PageEntry>> pagesWords = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        for (File pdfFile : Objects.requireNonNull(pdfsDir.listFiles())) {
            try (var doc = new PdfDocument(new PdfReader(pdfFile))) {
                for (int i = 1; i <= doc.getNumberOfPages(); i++) {
                    PdfPage pdfPage = doc.getPage(i);
                    var text = PdfTextExtractor.getTextFromPage(pdfPage);
                    var words = text.split("\\P{IsAlphabetic}+");
                    Set<String> setWords = Arrays.stream(words)
                            .map(String::toLowerCase)
                            .collect(Collectors.toSet());
                    for (String word : setWords) {
                        int count = (int) Arrays.stream(words).filter(word::equals).count();
                        String fileName = pdfFile.getName();
                        PageEntry pageEntry = new PageEntry(fileName, i, count);
                        if (!pagesWords.containsKey(word)) {
                            List<PageEntry> pageEntries = new ArrayList<>();
                            pageEntries.add(pageEntry);
                            pagesWords.put(word, pageEntries);
                        } else {
                            pagesWords.
                                    get(word).
                                    add(pageEntry);
                            pagesWords.
                                    get(word).
                                    sort(Collections.reverseOrder());
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        return pagesWords.get(word.toLowerCase());
    }


}
