package it.polito.tdp.formulaone.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.formulaone.model.Circuit;
import it.polito.tdp.formulaone.model.Constructor;
import it.polito.tdp.formulaone.model.Driver;
import it.polito.tdp.formulaone.model.Season;


public class FormulaOneDAO {

	public List<Integer> getAllYearsOfRace() {
		
		String sql = "SELECT year FROM races ORDER BY year" ;
		
		try {
			Connection conn = ConnectDB.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Integer> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(rs.getInt("year"));
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public List<Season> getAllSeasons() {
		
		String sql = "SELECT year, url FROM seasons ORDER BY year" ;
		
		try {
			Connection conn = ConnectDB.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Season> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(new Season(Year.of(rs.getInt("year")), rs.getString("url"))) ;
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Circuit> getAllCircuits() {

		String sql = "SELECT circuitId, name FROM circuits ORDER BY name";

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Circuit> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Circuit(rs.getInt("circuitId"), rs.getString("name")));
			}

			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public List<Constructor> getAllConstructors() {

		String sql = "SELECT constructorId, name FROM constructors ORDER BY name";

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Constructor> constructors = new ArrayList<>();
			while (rs.next()) {
				constructors.add(new Constructor(rs.getInt("constructorId"), rs.getString("name")));
			}

			conn.close();
			return constructors;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public void loadAllDriversSeason(Map<Integer, Driver> idDriverMap, Graph<Driver, DefaultWeightedEdge> grafo,
			Year year) {
		
		String sql = "SELECT d.driverId, d.driverRef, d.NUMBER, d.CODE, d.forename, d.surname, d.dob, d.nationality, d.url " + 
				"FROM results re, races ra, drivers d " + 
				"WHERE re.raceId = ra.raceId AND d.driverId = re.driverId AND ra.YEAR = ? AND re.POSITION IS NOT null " + 
				"GROUP BY re.driverId ";
		
		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year.getValue());

			ResultSet rs = st.executeQuery();

			
			while (rs.next()) {
				
				if(!idDriverMap.containsKey(rs.getInt("d.driverId"))) {
					
						Driver driver =  new Driver(rs.getInt("d.driverId"), rs.getString("d.driverRef"), rs.getInt("d.NUMBER"), rs.getString("d.CODE"), rs.getString("d.forename"), 
								rs.getString("d.surname"), rs.getDate("d.dob").toLocalDate(), rs.getString("d.nationality"), rs.getString("d.url"));
						//riempio la mappa
						idDriverMap.put(driver.getDriverId(), driver);
						//aggiungo il vertice
						grafo.addVertex(driver);
				}
				
			}

			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
		
	}

	public void loadAllEdges(Map<Integer, Driver> idDriverMap, Graph<Driver, DefaultWeightedEdge> grafo, Year year) {
		
		String sql = "SELECT re1.driverId AS id1, re2.driverId AS id2, SUM(case when re1.position > re2.position then 1 ELSE 0 END) AS peso " + 
				"FROM results  re1, results re2, races ra " + 
				"WHERE ra.YEAR = ? AND re1.raceId = ra.raceId and re2.raceId = ra.raceId " + 
				"and re1.POSITION IS NOT NULL and re2.POSITION IS NOT NULL " + 
				"AND not re2.driverId = re1.driverId  " + 
				"GROUP BY re1.driverId, re2.driverId " + 
				"HAVING peso>0 ";
		
		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year.getValue());

			ResultSet rs = st.executeQuery();

			
			while (rs.next()) {
				
				if(idDriverMap.containsKey(rs.getInt("id1")) && idDriverMap.containsKey(rs.getInt("id1"))) 
					    Graphs.addEdge(grafo, idDriverMap.get(rs.getInt("id1")), idDriverMap.get(rs.getInt("id2")), rs.getInt("peso"));
				
				
			}

			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
}
