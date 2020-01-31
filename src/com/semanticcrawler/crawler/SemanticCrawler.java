package com.semanticcrawler.crawler;

import org.apache.jena.rdf.model.Model;

/**
 * SemanticCrawler
 */
public interface SemanticCrawler {
    void search(Model graph, String resourceURI);
}