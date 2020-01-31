package app;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class App {
    static private String namespace = "http://example.org/test/";
    public static void main(String[] args) throws Exception {
        Model model = ModelFactory.createDefaultModel();

        Resource subject = model.createResource(namespace + "message");
        Property property = model.createProperty(namespace + "says");
        subject.addProperty(property, "Hello World!", XSDDatatype.XSDstring);

        model.write(System.out);
    }
}