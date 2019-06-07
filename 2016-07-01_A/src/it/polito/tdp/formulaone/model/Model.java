package it.polito.tdp.formulaone.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.formulaone.db.FormulaOneDAO;

public class Model {
	
	
	private FormulaOneDAO dao;
	private Graph <Driver, DefaultWeightedEdge> grafo;
	private Map<Integer, Driver> idDriverMap;
	public Model() {
		this.dao = new FormulaOneDAO();
		idDriverMap = new HashMap<Integer, Driver>();
	}
	

	public List<Season> getSeasons() {
		// TODO Auto-generated method stub
		return dao.getAllSeasons();
	}


	public void creaGrafo(Season anno) {
		
		//creo il grafo
		grafo = new SimpleDirectedWeightedGraph<Driver, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//carica i vertici
		dao.loadAllDriversSeason(idDriverMap, grafo, anno.getYear());
		
		
		//carica gli archi
		
		
	}


}
