package com.semanticcrawler.impl;

import java.util.ArrayList;
import java.util.List;

import com.semanticcrawler.crawler.SemanticCrawler;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.OWL;

/**
 * SemanticCrawlerImpl
 */
public class SemanticCrawlerImpl implements SemanticCrawler {
    private List<String> visitedURIs;

    public SemanticCrawlerImpl() {
        this.visitedURIs = new ArrayList<>();
    }

    private boolean visited(String URI) {
        return visitedURIs.contains(URI);
    }

    private boolean isOwlSameAsProperty(Property property) {
        return property.getURI().equals(OWL.sameAs.getURI());
    }

    @Override
    public void search(Model graph, String resourceURI) {
        Model model = ModelFactory.createDefaultModel();
        model.read(resourceURI);

        Resource resource = model.getResource(resourceURI);

        StmtIterator statements = model.listStatements(resource, (Property)null, (RDFNode)null);
        while (statements.hasNext()) {
            Statement statement = statements.next();
            Property property = statement.getPredicate();
            RDFNode value = statement.getObject();
            
            if (isOwlSameAsProperty(property)) {
                String uri = value.asNode().getURI();
                try {
                    if (!visited(uri)) {
                        System.out.println(uri);
                        this.visitedURIs.add(uri);
                        search(graph, uri);
                    }
                } catch (Exception e) {
                    e.equals(null);
                }
            } else {
                graph.add(resource, statement.getPredicate(), statement.getObject());
            }
        }
    }
}