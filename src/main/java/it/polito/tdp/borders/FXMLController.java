
package it.polito.tdp.borders;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    private ComboBox<Country> countryBox;


    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	txtResult.clear();
    	String year = txtAnno.getText();
    	int anno;
    	String s = "";
    	try {
    		anno = Integer.parseInt(year);
    	}catch(NumberFormatException e) {
    		txtResult.setText("Errore nella scrittura dell'anno");
    		throw new RuntimeException("Errore di scrittura");
    	}
    	
    	if(anno <= 2016 && anno >= 1816) {
    		model.creaGrafo(anno);
    		Graph<Country, DefaultEdge> grafo = model.getGrafo();
        	countryBox.getItems().addAll(grafo.vertexSet());
    		s += "# vertici : "+model.getGrafo().vertexSet().size()+"\n# archi : "
    				+model.getGrafo().edgeSet().size()+"\nComponenti connesse : "
    				+model.getNumberOfConnectedComponents()+"\n";
    		for(Country c : grafo.vertexSet()) {
    			s += c.getNome()+" "+grafo.degreeOf(c)+"\n";
    		}
    	}
    	else {
    		txtResult.setText("L'anno deve essere compreso tra il 1816 e il 2016");
    		return;
    	}
    	
    	txtResult.setText(s);
    	
    }
    
    @FXML
    void doTrova(ActionEvent event) {
    	txtResult.clear();
    	Country c = countryBox.getValue();
    	List<Country> percorso = model.trovaPercorso1(c);
    	String s = "Paesi raggiungibili: "+percorso.size()+"\n";
    	
    	for(Country cc : percorso) {
    		s += cc.getNome()+"\n";
    	}
    	
    	txtResult.setText(s);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
	   assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
       assert countryBox != null : "fx:id=\"CountryBox\" was not injected: check your FXML file 'Scene.fxml'.";
       assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
