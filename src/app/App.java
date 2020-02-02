package app;

import com.semanticcrawler.crawler.SemanticCrawler;
import com.semanticcrawler.impl.SemanticCrawlerImpl;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class App {
    public static void main(String[] args) throws Exception {
        Model model = ModelFactory.createDefaultModel();

        SemanticCrawler crawler = new SemanticCrawlerImpl();
        crawler.search(model, "http://dbpedia.org/resource/Roger_Federer");

        model.write(System.out, "turtle");
    }
}