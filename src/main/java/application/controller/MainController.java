package application.controller;


import application.services.FileManagerService;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;


@Controller
@Data
public class MainController {

    private FileManagerService fileManager;
    private CountDownLatch latch;

    public MainController() {
        this.fileManager = new FileManagerService();
        this.latch = new CountDownLatch(0);
    }

    @GetMapping(path = "/")
    public RedirectView startDefault() {
        return new RedirectView("/start");
    }

    @GetMapping(path = "/start")
    public void start(Model info) {
        if (this.getFileManager().getInfoLog().size() != 0) {
            this.getFileManager().getInfoLog().clear();
        }
        info.addAttribute("numberAnnotations", this.fileManager.getAnnotationsList().size());
        info.addAttribute("numberCategories", this.fileManager.getCategoriesList().size());
        info.addAttribute("numberImages", this.fileManager.getImagesList().size());
    }

    @GetMapping(path = "/new")
    public RedirectView startNew() {
        this.fileManager.clear();
        return new RedirectView("/start");
    }

    @GetMapping(path = "/break")
    public RedirectView breakUpload() throws IOException, InterruptedException {
        latch = new CountDownLatch(1);
        latch.await();
        if (this.fileManager.path != null) {
            java.nio.file.Files.delete(this.fileManager.path);
            this.fileManager.path = null;
        }
        this.fileManager.clear();
        return new RedirectView("/start");
    }

    @GetMapping(path = "/back")
    public RedirectView back() {
        if (this.fileManager.getInfoLog().size() != 0) {
            this.fileManager.getInfoLog().clear();
        }
        return new RedirectView("/start");
    }

    @PostMapping(path = "/start")
    public void getFiles(Model info, MultipartFile[] files) throws IOException {
        if (latch.getCount() == 0) {
            for (MultipartFile multipartFile : files) {
                if (!multipartFile.isEmpty()) {
                    this.fileManager.readFilesAndFolders(multipartFile);
                } else {
                    ArrayList <String> empty = new ArrayList <>();
                    empty.add(multipartFile.getOriginalFilename() + " enthält keine Daten");
                    this.fileManager.getInfoLog().add(empty);
                }
            }
            info.addAttribute("load", 1);
            info.addAttribute("infoLog", this.fileManager.getInfoLog());
            info.addAttribute("numberAnnotations", this.fileManager.getAnnotationsList().size());
            info.addAttribute("numberCategories", this.fileManager.getCategoriesList().size());
            info.addAttribute("numberImages", this.fileManager.getImagesList().size());
        }
        latch.countDown();
    }

    @GetMapping(path = "/output")
    public void getOutput(Model info) {
        if (this.fileManager.getAnnotationsList().size() == 0) {
            info.addAttribute("nodata", "1");
        } else {
            this.fileManager.infoLog.clear();
        }
    }

    @PostMapping(path = "/output")
    public void postOutput(Model info, String pathOutput, String extension, String format, String value, String split, double[] splitNumbers) throws ParserConfigurationException, TransformerException, IOException {
        this.fileManager.createOutput(pathOutput, extension, format, value, split, splitNumbers);
        info.addAttribute("fileInfo", this.fileManager.getInfoLog());
    }
}
