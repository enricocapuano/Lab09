package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private SimpleGraph<Country, DefaultEdge> grafo;
	private BordersDAO dao;
	private Map<Integer, Country> idMap;

	public Model() {
		dao = new BordersDAO();
		idMap = new HashMap<>();
		dao.loadAllCountries(idMap);
	}
	
	public void creaGrafo(int anno) {
		grafo = new SimpleGraph<>(DefaultEdge.class);
		
		Graphs.addAllVertices(grafo, dao.getVertici(anno, idMap));
		
		//System.out.println(grafo.vertexSet().size());
		
		for(Border b : dao.getCountryPairs(anno, idMap)) {
			grafo.addEdge(b.getC1(), b.getC2());
		}
		
		//System.out.println(grafo.edgeSet().size());
	}

	public SimpleGraph<Country, DefaultEdge> getGrafo() {
		return grafo;
	}

	

}
