package com.recipeone.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {
	
	public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;
        String fileuploadFullUrl = uploadPath + "/" + savedFileName;

        File uploadDirectory = new File(uploadPath);
        if (!uploadDirectory.exists()) {
            // 경로에 폴더가 없으면 폴더 생성
            if (!uploadDirectory.mkdirs()) {
                throw new Exception("파일을 저장할 경로를 생성할 수 없습니다.");
            }
        }

        FileOutputStream fos = new FileOutputStream(fileuploadFullUrl);
        fos.write(fileData);
        fos.close();
        return savedFileName;
    }

    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);

        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}

