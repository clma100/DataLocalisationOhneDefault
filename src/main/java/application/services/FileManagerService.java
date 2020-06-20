package application.services;

import application.model.Annotations;
import application.model.outputFormats.coco.Coco;
import application.model.outputFormats.coral.Coral;
import application.model.outputFormats.openImage.OpenImage5;
import application.model.outputFormats.pascal.Pascal;
import application.services.parentClass.PrepService;
import application.services.parsingServices.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This class procedures all the needed function related to reading and creating files. Also part of it is to unzip
 * ZIP- Files and pass the read information to the required service of parsing. Moreover it creates all the out-
 * put folders and files.
 */
public class FileManagerService extends PrepService {

    public Path path = null;
    private int counterFiles = 0;

    public FileManagerService() {
        super();
    }

    /**
     * Reads all the single files and folders and extracts the bytes. So that it is passed to the
     * {@link #readSingleFile(String)} Method
     *
     * @param multipartFile, transferred file from controller
     * @throws IOException if action with file doesnt work
     */
    public void readFilesAndFolders(MultipartFile multipartFile) throws IOException {
        String orgFileName = multipartFile.getOriginalFilename();
        String filePath = orgFileName != null ? orgFileName.substring(orgFileName.lastIndexOf("\\") + 1) : "";
        ArrayList <String> infoFile = new ArrayList <>();
        infoFile.add("'" + filePath + "':");
        this.infoLog.add(infoFile);

        byte[] bytes;

        if ((bytes = multipartFile.getBytes()).length != 0) {
            path = Paths.get(Objects.requireNonNull(filePath));
            if (Objects.requireNonNull(multipartFile.getContentType()).contains("zip")) {
                java.nio.file.Files.write(path, bytes);
                unzip(filePath);
            } else {
                java.nio.file.Files.write(path, bytes);
                readSingleFile(multipartFile.getOriginalFilename());
            }
            java.nio.file.Files.delete(path);
            path = null;
        } else {
            infoFile.add("enthält keine Daten");
        }
    }

    /**
     * Unzips and passes each file of the ZIP to {@link #readSingleFile(String)} Method
     *
     * @param filePath filepath of passed Multipartfile
     * @throws IOException if action with file doesnt work
     */
    private void unzip(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        ZipInputStream zipInputStream = new ZipInputStream(bufferedInputStream);
        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            String entryName = zipEntry.getName();
            if (!entryName.endsWith("/")) {
                this.getInfoLog().get(infoLog.size() - 1).add("'" + entryName.substring(entryName.lastIndexOf("\\") + 1) + "':");
                File file = new File(entryName);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                BufferedInputStream bufferedInputStream1 = new BufferedInputStream(zipInputStream);
                int singleBytes;
                while ((singleBytes = bufferedInputStream1.read()) > 0) {
                    fileOutputStream.write(singleBytes);
                }
                fileOutputStream.close();
                if (file.length() > 0) {
                    readSingleFile(entryName);
                } else {
                    this.getInfoLog().get(infoLog.size() - 1).add("enthält keine Daten");
                }
                java.nio.file.Files.delete(file.toPath());
            }
        }
        zipInputStream.closeEntry();
        zipInputStream.close();
        bufferedInputStream.close();
        fileInputStream.close();
    }

    /**
     * Extracts the content of the passed file and forwards it to the required parsing service.
     *
     * @param filePath, filepath of single file
     * @throws IOException if action with file doesnt work
     */
    private void readSingleFile(String filePath) throws IOException {
        String fileExtension = filePath.substring(filePath.lastIndexOf(".") + 1);
        if (fileExtension.equalsIgnoreCase("json")) {
            new CocoService(imagesList, categoriesList, annotationsList, infoLog).readCocoJson(filePath);
        } else if (fileExtension.equalsIgnoreCase("xml")) {
            new PascalService(imagesList, categoriesList, annotationsList, infoLog).readPascalXml(filePath);
        } else if (fileExtension.equalsIgnoreCase("txt")) {
            new CoralService(imagesList, categoriesList, annotationsList, infoLog).readCoralTxt(filePath);
        } else if (fileExtension.equalsIgnoreCase("csv")) {
            new OpenImage5Service(imagesList, categoriesList, annotationsList, infoLog).readOpenImageCsv(filePath);
        } else if (fileExtension.equalsIgnoreCase("jpg")) {
            new ImagesService(imagesList, categoriesList, annotationsList, infoLog).readImage(filePath);
        } else {
            this.infoLog.get(infoLog.size() - 1).add("Datentyp: '" + fileExtension + "' wird nicht bedient. Datei ignoriert.");
        }
    }

    /**
     * Creates the output files and folders according to the passed information from the controller
     *
     * @param path,         path where output is created
     * @param extension,    desired file extension (xml, csv, txt or json)
     * @param format,       desired format (coco, pascal, openImage, coralanloSub, coralanloDev, coralpixSub or coralpixDev)
     * @param value,        desired relativ or absolute values of polygon or Bounding Box
     * @param split,        desired kind of split (none, annotation, class, image, two or three)
     * @param splitNumbers, distribution of images per file
     * @throws IOException                  if action with file doesnt work
     * @throws ParserConfigurationException parsing doesnt work
     * @throws TransformerException         same here
     */
    public void createOutput(String path, String extension, String format, String value, String split, double[] splitNumbers) throws IOException, ParserConfigurationException, TransformerException {
        int annotationAt = 0;
        while (annotationAt < this.annotationsList.size()) {
            this.infoLog.add(new ArrayList <>());
            ArrayList <Annotations> inputSplitted = this.splitData(split, this.setAbsNumberOfSplit(splitNumbers), counterFiles, annotationAt);
            this.setKindOfValue(value, inputSplitted);
            annotationAt += inputSplitted.size();
            String fileContent = "";
            if (format.equals("pascal")) {
                ArrayList <String> fileContentArray = new Pascal(inputSplitted).printXml();
                for (String string : fileContentArray) {
                    createFile(path, format, extension, string);
                    counterFiles++;
                }
            } else {
                if (format.equals("coco")) {
                    fileContent = new Coco(imagesList, categoriesList, inputSplitted).printJson();
                } else if (format.contains("coral")) {
                    boolean dev = false;          //if development format is required, else submission
                    if (format.contains("Dev")) {
                        dev = true;
                    }
                    if (format.contains("anlo")) {
                        fileContent = new Coral(inputSplitted, true, dev).printTxt(dev, true);
                    } else {
                        Coral coral = new Coral(inputSplitted, false, dev);
                        if (coral.getCoralAnnotationArrayList().size() != 0) {
                            fileContent = coral.printTxt(dev, false);
                        } else {
                            this.getInfoLog().get(infoLog.size() - 1).add("Keine Konvertierung möglich: ImageCLEFcoral Pixel-wise Parsing task benötigt Polygon. Keine Polygonangaben vorhanden.");
                            return;
                        }
                    }
                } else if (format.equals("openImage")) {
                    fileContent = new OpenImage5(inputSplitted).printCsv();
                }
                createFile(path, format, extension, fileContent);
                counterFiles++;
            }
        }
        counterFiles = 0;
    }

    /**
     * creates a single output file and the output folder at the beginning
     */
    private void createFile(String pathRaw, String format, String extension, String fileContent) throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");
        LocalDateTime localDateTime = LocalDateTime.now();
        String dirName = format + "_" + localDateTime.format(dateTimeFormatter);
        pathRaw += dirName;

        if (counterFiles == 0) {
            File dir = new File(pathRaw);
            if (!dir.mkdir()) {
                this.infoLog.add(new ArrayList <>());
                this.infoLog.get(infoLog.size() - 1).add("Outputordner konnte nicht erstellt werden.");
                return;
            }
        }

        String fileName = "output" + counterFiles + "." + extension;
        File file = new File(pathRaw + "\\" + fileName);
        if (file.createNewFile()) {
            writeToFile(fileContent, file.getAbsolutePath());
            this.infoLog.get(infoLog.size() - 1).add("Outputdatei in '" + pathRaw);
        } else {
            this.infoLog.get(infoLog.size() - 1).add("Datei konnte nicht erstellt werden.");
        }
    }

    private void writeToFile(String input, String filePath) throws IOException {
        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.write(input);
        fileWriter.close();
    }
}