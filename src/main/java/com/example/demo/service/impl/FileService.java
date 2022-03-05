package com.example.demo.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dao.Account;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.service.IAccountService;
import com.example.demo.service.IFileService;
import com.example.demo.utils.FileManager;

@Component
public class FileService implements IFileService{
	
	@Autowired
	private IAccountService accountService;
	
	@Autowired
	private AccountRepository accountRepository;
	
	private FileManager fileManager = new FileManager();
	
	@Value("${upload.link}")
	private String link;
	
	@Override
	public String uploadImage(MultipartFile image, Long accountId) throws IOException {
		String path = link + "\\" + image.getOriginalFilename();
		fileManager.createNewMultiPartFile(path, image);
		Account account = accountService.findById(accountId);
		account.setAvatarUrl(image.getOriginalFilename());
		accountRepository.save(account);
		
		return path;
	}

	@Override
	public File downloadImage(String nameImage) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
