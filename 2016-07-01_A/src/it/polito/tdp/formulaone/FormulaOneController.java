package it.polito.tdp.formulaone;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.formulaone.model.Driver;
import it.polito.tdp.formulaone.model.Model;
import it.polito.tdp.formulaone.model.Season;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FormulaOneController {
	
	Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Season> boxAnno;

    @FXML
    private TextField textInputK;

    @FXML
    private TextArea txtResult;

    private Season anno = null; 
    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	txtResult.clear();
    	anno = boxAnno.getValue();
    	
    	if(anno!=null) {
    		
    		model.creaGrafo(anno);
    		txtResult.appendText("Il pilota migliore è  : " +model.getBestDriver());
    	}
    	else {
    		txtResult.appendText("Seleziona una season");
    	}

    }

    @FXML
    void doTrovaDreamTeam(ActionEvent event) {


    	txtResult.clear();
    	
    	
    	if(anno==boxAnno.getValue()) {
    		
    		try {
    		
    		txtResult.appendText("Dream team\n\n");
    		
    		for(Driver dri : model.dreamTeam(Integer.parseInt(this.textInputK.getText()))) 
    			txtResult.appendText("  - "+dri.toString()+"\n");
    		
    		}catch(NumberFormatException nfe) {
    			txtResult.appendText("Si prega di inserire un numero intero K");
    		}
    			
    	}
    	else {
    		
    		if(anno==null) 
    			txtResult.appendText("Seleziona una season");
    		
    		else
    	    	txtResult.appendText("Se vuoi cambiare season premi su 'Vittorie piloti' ");
    	}

    	
    }

    @FXML
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'FormulaOne.fxml'.";
        assert textInputK != null : "fx:id=\"textInputK\" was not injected: check your FXML file 'FormulaOne.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'FormulaOne.fxml'.";

    }
    
    public void setModel(Model model){
    	this.model = model;
    }

	public void caricaBox() {
		boxAnno.getItems().addAll(model.getSeasons());
		
		
	}
    
   
}
