package com.example.firestore.data.model;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.example.firestore.data.model.PeliculaDto;
public class MovieResponseApi {
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<PeliculaDto> results;
    public Integer getPage() {
        return page;
    }
    public void setPage(Integer page) {
        this.page = page;
    }
    public List<PeliculaDto> getResults() {
        return results;
    }
    public void setResults(List<PeliculaDto> results) {
        this.results = results;
    }
}

