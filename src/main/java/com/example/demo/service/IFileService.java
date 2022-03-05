package com.example.demo.service;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
	String uploadImage(MultipartFile image, Long accountId) throws IOException;

	File downloadImage(String nameImage) throws IOException;
}
