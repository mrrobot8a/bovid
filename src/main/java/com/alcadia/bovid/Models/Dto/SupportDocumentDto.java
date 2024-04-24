package com.alcadia.bovid.Models.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SupportDocumentDto {

    private Long id;

    private String fileName;

    private String urlFile;

}
