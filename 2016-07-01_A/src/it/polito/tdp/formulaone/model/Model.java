package it.polito.tdp.formulaone.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.spi.DirStateFactory.Result;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.formulaone.db.FormulaOneDAO;

public class Model {
	
	
	private FormulaOneDAO dao;
	private Graph <Driver, DefaultWeightedEdge> grafo;
	private Map<Integer, Driver> idDriverMap;
	private Set<Driver> dreamTeam;
	List<Driver> drivers;
	private int max;
	
	public Model() {
		
		this.dao = new FormulaOneDAO();
		
	}
	

	public List<Season> getSeasons() {
		// TODO Auto-generated method stub
		return dao.getAllSeasons();
	}


	public void creaGrafo(Season anno) {
		
		//creo il grafo e la idMap
		idDriverMap = new HashMap<Integer, Driver>();
		grafo = new SimpleDirectedWeightedGraph<Driver, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//carica i vertici
		dao.loadAllDriversSeason(idDriverMap, grafo, anno.getYear());
	
		//carica gli archi
		dao.loadAllEdges(idDriverMap, grafo, anno.getYear());
		
		System.out.println("Grafo creato : vertici  " +grafo.vertexSet().size() + " archi  "+grafo.edgeSet().size());
	}


	public Driver getBestDriver() {
	
		if(grafo!=null) {
			
			Driver best = null;
			int punti  = Integer.MIN_VALUE;
			for(Driver dri : grafo.vertexSet()) {
				if(grafo.outDegreeOf(dri) - grafo.inDegreeOf(dri) > punti) {
					punti = grafo.outDegreeOf(dri) - grafo.inDegreeOf(dri);
					best = dri;
				}
					
			}
			
			return best;
			
		}
		
		return null;
	}

	
	public Set<Driver> dreamTeam(int k){
		
		if(grafo!=null) {
		
		this.dreamTeam = new HashSet<Driver>();
		this.max = Integer.MAX_VALUE;
		Set<Driver> parziale = new HashSet<Driver>();
		drivers = new ArrayList<>(grafo.vertexSet());
	
		cerca(parziale, k ,0, drivers);
		
		return dreamTeam;
		
		}
		
		return null;
	}


	private void cerca(Set<Driver> parziale, int k, int L, List<Driver> drivers) {


		if(parziale.size()>k)
			return;
		
		//condizione di terminazione
		if(parziale.size() == k) {
			
		int punti = punteggio(parziale);
			
			if(punti<max) {
				
				dreamTeam  = new HashSet<Driver>(parziale);
				max = punti;
				return;
				
			}
			
		}
		
		if(L==drivers.size())
		  return;
		
		cerca(parziale, k, L+1, drivers);
		
		parziale.add(drivers.get(L));
		cerca(parziale, k, L+1, drivers);
		parziale.remove(drivers.get(L));
		
		
	}


	private int punteggio(Set<Driver> parziale) {
		
		
		Set<Driver> disp = new HashSet<>(drivers);
		disp.removeAll(parziale);
		
		int punti = 0;
		
		for(Driver par : parziale) {
		   for(Driver dis : disp) {
				if(grafo.containsEdge(dis, par))
		              punti += grafo.getEdgeWeight(grafo.getEdge(dis, par)); 
			}
		}
		
		return punti;
	}

}
