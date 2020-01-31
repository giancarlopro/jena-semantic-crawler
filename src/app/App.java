package app;

import com.semanticcrawler.impl.SemanticCrawlerImpl;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class App {
    static private String namespace = "http://example.org/test/";
    public static void main(String[] args) throws Exception {
        Model model = ModelFactory.createDefaultModel();

        SemanticCrawlerImpl crawler = new SemanticCrawlerImpl();
        crawler.search(model, "http://dbpedia.org/resource/Zico");

        // model.write(System.out);
    }
}