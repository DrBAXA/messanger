package ua.bentleytek.messenger.service;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;

@Service
public class FileService {

    private static final Logger logger = Logger.getLogger(FileService.class);

    @Autowired
    ServletContext servletContext;

    /**
     * save given file to disc and return filename(with extension)
     * @param multipartFile file to saving
     * @param name filename
     * @return filename with extension
     */
    public String saveFile(MultipartFile multipartFile, String name){
        try {
            String path = servletContext.getRealPath("resources/img");
            String fileName = name + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            File file = new File(path + "/" + fileName);
            logger.debug("Save file " + file.getName());
            multipartFile.transferTo(file);
            return fileName;
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
            logger.debug(ioe, ioe);
            return null;
        }
    }
}
