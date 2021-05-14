package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private SimpleGraph<Country, DefaultEdge> grafo;
	private BordersDAO dao;
	private Map<Integer, Country> idMap;
	private Map<Country, Country> visita;
	private List<Country> percorsoMigliore;

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

	public int getNumberOfConnectedComponents() {
		if (grafo == null)
			throw new RuntimeException("Grafo non esistente");
		
		ConnectivityInspector<Country, DefaultEdge> ci = new ConnectivityInspector<Country, DefaultEdge>(grafo);
		return ci.connectedSets().size();
	}
	
	public List<Country> trovaPercorso(Country c1) {
		
		List<Country> percorso = new LinkedList<>();
		BreadthFirstIterator<Country, DefaultEdge> it = new BreadthFirstIterator<>(grafo, c1);
		
		visita = new HashMap<>();
		visita.put(c1, null);
		
		it.addTraversalListener(new TraversalListener<Country, DefaultEdge>(){

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
				Country country1 = grafo.getEdgeSource(e.getEdge());
				Country country2 = grafo.getEdgeTarget(e.getEdge());
				
				if(visita.containsKey(country1) && !visita.containsKey(country2)) {
					visita.put(country2, country1);
				} else if(visita.containsKey(country2) && !visita.containsKey(country1)) {
					visita.put(country1, country2);
				}
				
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Country> e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Country> e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		while(it.hasNext()) {
			percorso.add(it.next());
		}
		
		if(!visita.containsKey(c1) ) {
			return null;
		}
		
		return percorso;
	}
	
	
	//versione ricorsione
	
	public List<Country> trovaPercorso1(Country sorgente){
		this.percorsoMigliore = new LinkedList<Country>();
		List<Country> parziale = new LinkedList<>();
		parziale.add(sorgente);
		cerca(sorgente, parziale);
		return this.percorsoMigliore;
	}
	
	private void cerca(Country sorgente, List<Country> parziale) {
		
		//Caso terminale
		if(Graphs.neighborListOf(grafo, sorgente).size() == 0) {
			this.percorsoMigliore = new LinkedList<>(parziale);
			return;
		}
		
		//altrimenti, scorro i vicini dell'utlimo inserito e provo 
		//ad aggiungerli uno ad uno
		for(Country vicino : Graphs.neighborListOf(grafo, sorgente)) {
			if(!parziale.contains(vicino)) {
				parziale.add(vicino);
				cerca(vicino, parziale);
				parziale.remove(parziale.size()-1);
			}
		}
	}	
}
