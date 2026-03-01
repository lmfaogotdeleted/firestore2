package com.example.firestore.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// Mapeo de la respuesta que nos da la API para pelis.
public class MovieResponse {
    private List<MovieEntry> results;

    public List<MovieEntry> getResults() { return results; }

    public class MovieEntry {
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("original_language")
        @Expose
        private String originalLanguage;
        @SerializedName("overview")
        @Expose
        private String overview;
        @SerializedName("popularity")
        @Expose
        private Double popularity;
        @SerializedName("release_date")
        @Expose
        private String releaseDate;
        @SerializedName("title")
        @Expose
        private String title;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getOriginalLanguage() {
            return originalLanguage;
        }

        public void setOriginalLanguage(String originalLanguage) {
            this.originalLanguage = originalLanguage;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public Double getPopularity() {
            return popularity;
        }

        public void setPopularity(Double popularity) {
            this.popularity = popularity;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
