package com.example.semantic;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.RDFNode;
import java.nio.charset.Charset;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.springframework.http.ResponseEntity;
import org.springframework.http.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class SmartController {
    public ArrayList<String> ListComponent = new ArrayList<String>();  // for name list
    public ArrayList<String> ListNames = new ArrayList<String>();  // 

    @GetMapping("/v1")
    public ResponseEntity<Map<String, Object>> getStrat(){
        try {
            // OntModel model = OpenOWL.OpenConnectOWL();

            System.out.println("Get Component"); 
            String queryString;
            queryString = "PREFIX ex:<http://localhost/computer#> "
                            +"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                            +"PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"
                            + "SELECT  (str(?name) as ?Component) "
                            + "where { ?x rdfs:subClassOf ex:ComputerParts."
                            + "?x rdfs:label ?name"
                            + " }";

            com.hp.hpl.jena.query.ResultSet results = OpenOWL.ExecSparQl(queryString); //all method ExecSparQl from OpenOWL class

            while (results.hasNext()) {

                    QuerySolution soln = results.nextSolution();
                    String Component = soln.getLiteral("Component").getString();
                    //test --
                    System.out.println("Computer " + Component.toString());
                    ListComponent.add(Component.toString());

                    RDFNode x = soln.get("Propertyval");

                    String xx = String.valueOf(x);

                    java.nio.ByteBuffer xxx = Charset.forName("UTF-8").encode(xx);

                    String xs = xxx.toString();

            }
             Map<String, Object> json = new HashMap<String, Object>();
                 json.put("message", "found data");
                 json.put("data", ListComponent);
                 HttpHeaders headers = new HttpHeaders();
                 headers.add("Content-Type", "application/json; charset=UTF-8");
                 return  (new ResponseEntity<Map<String, Object>>(json, headers, HttpStatus.OK));
            // ComponentList.removeAllItems(); //  combobox nameList
            // for (int i = 0; i < ListComponent.size(); i++) {
            //     // System.out.println(ListComponent.get(i));
            // }
        } catch (Exception ex) {
            Map<String, Object> json = new HashMap<String, Object>();
                 json.put("message", "error");
                 json.put("error", ex);
                 HttpHeaders headers = new HttpHeaders();
                 headers.add("Content-Type", "application/json; charset=UTF-8");
                 return  (new ResponseEntity<Map<String, Object>>(json, headers, HttpStatus.NOT_FOUND));  
            // ex.printStackTrace();
        }
        // return "test1";
    }
}