package data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Model for JSON graph data.
 * Used by Jackson for JSON deserialization.
 */
public class GraphData {
    @JsonProperty("directed")
    private boolean directed;
    
    @JsonProperty("n")
    private int n;
    
    @JsonProperty("edges")
    private List<EdgeData> edges;
    
    @JsonProperty("source")
    private Integer source;
    
    @JsonProperty("weight_model")
    private String weightModel;
    
    // Default constructor required for Jackson
    public GraphData() {}
    
    public boolean isDirected() {
        return directed;
    }
    
    public void setDirected(boolean directed) {
        this.directed = directed;
    }
    
    public int getN() {
        return n;
    }
    
    public void setN(int n) {
        this.n = n;
    }
    
    public List<EdgeData> getEdges() {
        return edges;
    }
    
    public void setEdges(List<EdgeData> edges) {
        this.edges = edges;
    }
    
    public Integer getSource() {
        return source;
    }
    
    public void setSource(Integer source) {
        this.source = source;
    }
    
    // Used by Jackson for JSON deserialization
    public String getWeightModel() {
        return weightModel;
    }
    
    // Used by Jackson for JSON deserialization
    public void setWeightModel(String weightModel) {
        this.weightModel = weightModel;
    }
    
    /**
     * Edge data model.
     */
    public static class EdgeData {
        @JsonProperty("u")
        private int u;
        
        @JsonProperty("v")
        private int v;
        
        @JsonProperty("w")
        private int w;
        
        // Default constructor required for Jackson
        public EdgeData() {}
        
        // Used for manual edge creation
        public EdgeData(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
        
        public int getU() {
            return u;
        }
        
        public void setU(int u) {
            this.u = u;
        }
        
        public int getV() {
            return v;
        }
        
        public void setV(int v) {
            this.v = v;
        }
        
        public int getW() {
            return w;
        }
        
        public void setW(int w) {
            this.w = w;
        }
    }
}

