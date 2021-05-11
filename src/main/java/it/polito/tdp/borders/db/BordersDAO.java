package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public void loadAllCountries(Map<Integer, Country> idMap) {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				//System.out.format("%d %s %s\n", rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
				Country c = new Country(rs.getString("StateAbb"), rs.getInt("CCode"), rs.getString("StateNme"));
				idMap.put(c.getCodice(), c);
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(int anno, Map<Integer, Country> idMap) {
		String sql = "SELECT con.state1no, con.state1ab, con.state2no, con.state2ab "
				+ "FROM contiguity con "
				+ "WHERE con.state1no > con.state2no AND con.year <= ? AND con.conttype = 1";
		
		List<Border> archi = new ArrayList<Border>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Country c1 = idMap.get(rs.getInt("state1no"));
				Country c2 = idMap.get(rs.getInt("state2no"));
				Border b = new Border(c1, c2, anno);
				archi.add(b);
			}
			
			conn.close();
			return archi;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Country> getVertici(int anno, Map<Integer, Country> idMap) {
		String sql = "SELECT DISTINCT cou.StateAbb, cou.CCode, cou.StateNme "
				+ "FROM contiguity con, country cou "
				+ "WHERE (con.state1no = cou.CCode OR con.state2no = cou.CCode) AND YEAR <= ? AND conttype = 1";
		
		List<Country> vertici = new ArrayList<Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				vertici.add(idMap.get(rs.getInt("CCode")));
			}
			
			conn.close();
			return vertici;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
		
}
