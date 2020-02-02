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

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
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
    
    private void collectData(Model modelToWrite, Model modelToRead, Resource subject, List<Resource> bNodes)
    {
        StmtIterator statements = modelToRead.listStatements();
        while(statements.hasNext())
        {
            Statement statement = statements.next();
            Resource statementSubject = statement.getSubject();
            RDFNode statementObject = statement.getObject();
            
            if(statementSubject.equals(subject)){
                modelToWrite.add(statement);
                if(statementObject.isAnon() && !bNodes.contains(statementObject.asResource()))
                {
                    bNodes.add(statementObject.asResource());
                    collectData(modelToWrite, modelToRead, statementObject.asResource(), bNodes);
                }
            }
        }
    }

    private String getLinkFromStatement(Resource origin, Statement statement)
    {
        if(statement.getSubject().equals(origin))
            return statement.getObject().asNode().getURI();
        return statement.getSubject().getURI();
    }

    @Override
    public void search(Model graph, String resourceURI) {
        this.visitedURIs.add(resourceURI);
        CharsetEncoder enc = Charset.forName("ISO-8859-1").newEncoder();
        if (!enc.canEncode(resourceURI)) return;

        System.out.println(resourceURI);

        Model model = ModelFactory.createDefaultModel();
        model.read(resourceURI);
        Resource resource = model.getResource(resourceURI);

        List<Resource> bNodes = new ArrayList<Resource>();
        collectData(graph, model, resource, bNodes);

        StmtIterator statements = model.listStatements();
        while (statements.hasNext()) {
            Statement statement = statements.next();
            Resource subject = statement.getSubject();
            RDFNode object = statement.getObject();

            Property property = statement.getPredicate();
            
            if (isOwlSameAsProperty(property) && (subject.equals(resource) || object.asResource().equals(resource))) {
                //Não é preciso verificar se o objeto é um nó em branco, pois isso já é feito no primeiro collectData
                if(subject.isAnon())
                    collectData(graph, model, subject, bNodes);
                else {
                    String nextURI = getLinkFromStatement(resource, statement);
                    try {
                        if (!visited(nextURI)) 
                            search(graph, nextURI);
                    } catch (Exception e) {
                        e.equals(null);
                    }
                }
            }
        }
    }
}