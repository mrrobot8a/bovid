package com.alcadia.bovid.Models.Dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageSimilarResponse {

    public static class SimilarImage {
        private String nombreImagen;
        private double similitud;

        public SimilarImage() {
        }

        public SimilarImage(String nombreImagen, double similitud) {
            this.nombreImagen = nombreImagen;
            this.similitud = similitud;
        }

        public String getNombreImagen() {
            return nombreImagen;
        }

        public void setNombreImagen(String nombreImagen) {
            this.nombreImagen = nombreImagen;
        }

        public double getSimilitud() {
            return similitud;
        }

        public void setSimilitud(double similitud) {
            this.similitud = similitud;
        }
    }

    @JsonProperty("similar_images")
    private List<SimilarImage> similarImages;

    public List<SimilarImage> getSimilarImages() {
        return similarImages;
    }

    public void setSimilarImages(List<SimilarImage> similarImages) {
        this.similarImages = similarImages;
    }
}
