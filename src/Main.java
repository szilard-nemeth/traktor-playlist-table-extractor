import java.io.File;
import java.io.IOException;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagNodeVisitor;

public class Main {


    public static void main(String[] args) throws IOException {
        HtmlCleaner cleaner = new HtmlCleaner();
        final String directoryString = args[0];
        
        File dir = new File(directoryString);
        File[] files = dir.listFiles();
        for (File file : files) {
            final ParserContext ctx = new ParserContext(directoryString, file.getName());
            TagNode node = cleaner.clean(file);
            node.traverse(new TagNodeVisitor() {
                @Override
                public boolean visit(TagNode parentNode, HtmlNode htmlNode) {
                    if (htmlNode instanceof TagNode) {
                        ctx.handleTag((TagNode) htmlNode);
                    }
                    return true;
                }
            });
            
            ctx.finish();
        }
    }
}
