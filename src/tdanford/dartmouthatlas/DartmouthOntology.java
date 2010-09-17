package tdanford.dartmouthatlas;

import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.sc.rdf.Prefixes;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import java.io.*;

public class DartmouthOntology  {
	
	public static void main(String[] args) { 
		try {
			DartmouthOntology ont = new DartmouthOntology();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Model core;
	private OntModel data;
	
	private File path = new File("/Users/tdanford/Documents/Ontologies/DartmouthAtlas");
	private File coreFile = new File(path, "core.owl");
	private File dataFile = new File(path, "data.owl");
	private String baseURIPath = "http://purl.org/dartmouthatlas/2010/1/";
	private String baseCoreURI = baseURIPath + "core.owl#"; 
	
	private Prefixes prefixes;
	
	public DartmouthOntology() throws IOException {
		prefixes = new Prefixes(Prefixes.DEFAULT);
		prefixes.addPrefix("dahc", baseCoreURI);
		core = ModelFactory.createMemModelMaker().createFreshModel();
		FileInputStream fis = new FileInputStream(coreFile);
		core.read(fis, baseCoreURI);
		fis.close();
		
		data = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		
		Ontology coreOntology = data.createOntology(baseURIPath + "core.owl");
		Ontology dataOntology = data.createOntology(baseURIPath + "data.owl");
		dataOntology.addImport(coreOntology);
		
		OutputStream dataOut = new FileOutputStream(dataFile);
		String base = baseCoreURI;
		String lang = "RDF/XML";
		//data.writeAll(dataOut, lang, base);
		data.write(dataOut);
		dataOut.close();
	}
	
	

}
