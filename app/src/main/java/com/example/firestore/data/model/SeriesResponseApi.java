package com.example.firestore.data.model;
import java.util.List;
import com.example.firestore.data.model.SerieDto;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class SeriesResponseApi {
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<SerieDto> results;
    public Integer getPage() {
        return page;
    }
    public void setPage(Integer page) {
        this.page = page;
    }
    public List<SerieDto> getResults() {
        return results;
    }
    public void setResults(List<SerieDto> results) {
        this.results = results;
    }
}

