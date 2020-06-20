package application.services;

import application.model.Annotations;
import application.model.outputFormats.coco.Coco;
import application.model.outputFormats.coral.Coral;
import application.model.outputFormats.openImage.OpenImage5;
import application.model.outputFormats.pascal.Pascal;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;


@RunWith(PowerMockRunner.class)
@PrepareForTest(FileManagerService.class)
@SpringBootConfiguration
public class FileManagerTest {
    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();
    FileManagerService fileManagerService;
    MockMultipartFile multipart;

    @Before
    public void setUp() {
        fileManagerService = new FileManagerService();
    }

    @Test
    public void readFilesAndFoldersJsonTest() throws IOException {
        multipart = new MockMultipartFile("test", "coco.json", "application/octet-stream", new byte[0]);
        fileManagerService.readFilesAndFolders(multipart);
        Assert.assertEquals("'coco.json':", fileManagerService.getInfoLog().get(0).get(0));
        Assert.assertEquals("enthält keine Daten", fileManagerService.getInfoLog().get(0).get(1));
    }

    @Test
    public void readFilesAndFoldersXmlTest() throws IOException {
        multipart = new MockMultipartFile("test", "pascal.xml", "text/xml", new byte[0]);
        fileManagerService.readFilesAndFolders(multipart);
        Assert.assertEquals("'pascal.xml':", fileManagerService.getInfoLog().get(0).get(0));
        Assert.assertEquals("enthält keine Daten", fileManagerService.getInfoLog().get(0).get(1));
    }

    @Test
    public void readFilesAndFoldersCsvTest() throws IOException {
        multipart = new MockMultipartFile("test", "open.csv", "application/vnd.ms-excel", new byte[0]);
        fileManagerService.readFilesAndFolders(multipart);
        Assert.assertEquals("'open.csv':", fileManagerService.getInfoLog().get(0).get(0));
        Assert.assertEquals("enthält keine Daten", fileManagerService.getInfoLog().get(0).get(1));
    }

    @Test
    public void readFilesAndFoldersTxtTest() throws IOException {
        multipart = new MockMultipartFile("test", "coral.txt", "text/plain", new byte[0]);
        fileManagerService.readFilesAndFolders(multipart);
        Assert.assertEquals("'coral.txt':", fileManagerService.getInfoLog().get(0).get(0));
        Assert.assertEquals("enthält keine Daten", fileManagerService.getInfoLog().get(0).get(1));
    }

    @Test
    public void readFilesAndFoldersJpgTest() throws IOException {
        multipart = new MockMultipartFile("test", "image.jpg", "text/plain", new byte[0]);
        fileManagerService.readFilesAndFolders(multipart);
        Assert.assertEquals("'image.jpg':", fileManagerService.getInfoLog().get(0).get(0));
        Assert.assertEquals("enthält keine Daten", fileManagerService.getInfoLog().get(0).get(1));
    }

    @Test
    public void readFilesAndFoldersEmptyZipTest() throws IOException {
        multipart = new MockMultipartFile("test", "file.zip", "text/zip", new byte[0]);
        fileManagerService.readFilesAndFolders(multipart);
        Assert.assertEquals("'file.zip':", fileManagerService.getInfoLog().get(0).get(0));
        Assert.assertEquals("enthält keine Daten", fileManagerService.getInfoLog().get(0).get(1));
    }

    @Test
    public void readFilesAndFoldersZipNoEntryTest() throws Exception {
        multipart = new MockMultipartFile("test", "file.zip", "text/zip", "bytes".getBytes());
        FileManagerService fileManagerService_spy = PowerMockito.spy(new FileManagerService());

        ZipInputStream zipInputStream = PowerMockito.mock(ZipInputStream.class);
        whenNew(ZipInputStream.class).withAnyArguments().thenReturn(zipInputStream);
        PowerMockito.when(zipInputStream.getNextEntry()).thenReturn(null);

        fileManagerService_spy.readFilesAndFolders(multipart);

        PowerMockito.verifyPrivate(fileManagerService_spy).invoke("unzip", "file.zip");
    }

    @Test
    public void readFilesAndFoldersZipEntryTest() throws Exception {
        multipart = new MockMultipartFile("test", "file.zip", "text/zip", "bytes".getBytes());
        ZipEntry zipEntry = new ZipEntry("test.txt");
        zipEntry.setSize("anything".getBytes().length);
        FileManagerService fileManagerService_spy = PowerMockito.spy(new FileManagerService());

        ZipInputStream zipInputStream = PowerMockito.mock(ZipInputStream.class);
        whenNew(ZipInputStream.class).withAnyArguments().thenReturn(zipInputStream);
        PowerMockito.when(zipInputStream.getNextEntry()).thenAnswer(new Answer <ZipEntry>() {
            final ZipEntry[] zipEntries = {zipEntry, null};
            int i = 0;

            public ZipEntry answer(InvocationOnMock invocationOnMock) {
                ZipEntry zipEntry1 = zipEntries[(i % 2)];
                i++;
                return zipEntry1;
            }
        });

        fileManagerService_spy.readFilesAndFolders(multipart);
        Assert.assertEquals("'file.zip':", fileManagerService_spy.getInfoLog().get(0).get(0));
        Assert.assertEquals("'test.txt':", fileManagerService_spy.getInfoLog().get(0).get(1));
        Assert.assertEquals("enthält keine Daten", fileManagerService_spy.getInfoLog().get(0).get(2));

        File file = spy(new File("test.txt"));
        whenNew(File.class).withAnyArguments().thenReturn(file);
        PowerMockito.when(file.length()).thenReturn(1L);
        fileManagerService_spy.readFilesAndFolders(multipart);
        PowerMockito.verifyPrivate(fileManagerService_spy).invoke("readSingleFile", "test.txt");
    }

    @Test
    public void readFilesAndFoldersOtherTest() throws IOException {
        multipart = new MockMultipartFile("test", "x.other", "text/plain", "test".getBytes());
        fileManagerService.readFilesAndFolders(multipart);
        Assert.assertEquals("'x.other':", fileManagerService.getInfoLog().get(0).get(0));
        Assert.assertEquals("Datentyp: 'other' wird nicht bedient. Datei ignoriert.", fileManagerService.getInfoLog().get(0).get(1));
    }

    @Test
    public void createOutputOpenImageTest() throws Exception {
        double[] splitNumbers = {100, 0, 0};
        FileManagerService fileManagerService = PowerMockito.spy(new FileManagerService());
        fileManagerService.getAnnotationsList().add(new Annotations());
        OpenImage5 openImage5 = PowerMockito.mock(OpenImage5.class);
        whenNew(OpenImage5.class).withAnyArguments().thenReturn(openImage5);
        when(openImage5.printCsv()).thenReturn("csv");
        fileManagerService.createOutput(temporaryFolder.getRoot().getAbsolutePath(), "xml", "openImage", "realtiv", "none", splitNumbers);

        Mockito.verify(fileManagerService).setKindOfValue(eq("realtiv"), eq(fileManagerService.annotationsList));
        Mockito.verify(fileManagerService).setAbsNumberOfSplit(eq(splitNumbers));
        PowerMockito.verifyNew(OpenImage5.class).withArguments(eq(fileManagerService.annotationsList));
        PowerMockito.verifyPrivate(fileManagerService).invoke("createFile", anyString(), eq("openImage"), eq("xml"), eq("csv"));
    }

    @Test
    public void createOutputCocoTest() throws Exception {
        double[] splitNumbers = {100, 0, 0};
        FileManagerService fileManagerService = PowerMockito.spy(new FileManagerService());
        Annotations annotations = new Annotations();
        annotations.setAbsoluteValue(1);
        fileManagerService.getAnnotationsList().add(annotations);
        Coco coco = PowerMockito.mock(Coco.class);
        whenNew(Coco.class).withAnyArguments().thenReturn(coco);
        when(coco.printJson()).thenReturn("coco");
        fileManagerService.createOutput(temporaryFolder.getRoot().getAbsolutePath(), "json", "coco", "absolut", "none", splitNumbers);

        Mockito.verify(fileManagerService).setKindOfValue(eq("absolut"), eq(fileManagerService.annotationsList));
        Mockito.verify(fileManagerService).setAbsNumberOfSplit(eq(splitNumbers));
        PowerMockito.verifyNew(Coco.class).withArguments(eq(fileManagerService.imagesList), eq(fileManagerService.categoriesList), eq(fileManagerService.annotationsList));
        PowerMockito.verifyPrivate(fileManagerService).invoke("createFile", anyString(), eq("coco"), eq("json"), eq("coco"));
    }

    @Test
    public void createOutputCoralAnloSubTest() throws Exception {
        double[] splitNumbers = {100, 0, 0};
        FileManagerService fileManagerService = PowerMockito.spy(new FileManagerService());
        Annotations annotations = new Annotations();
        annotations.setAbsoluteValue(1);
        fileManagerService.getAnnotationsList().add(annotations);
        Coral coral = PowerMockito.mock(Coral.class);
        whenNew(Coral.class).withAnyArguments().thenReturn(coral);
        when(coral.printTxt(eq(false), eq(true))).thenReturn("coralanloSub");
        fileManagerService.createOutput(temporaryFolder.getRoot().getAbsolutePath(), "txt", "coralanloSub", "absolut", "none", splitNumbers);

        Mockito.verify(fileManagerService).setKindOfValue(eq("absolut"), eq(fileManagerService.annotationsList));
        Mockito.verify(fileManagerService).setAbsNumberOfSplit(eq(splitNumbers));
        PowerMockito.verifyNew(Coral.class).withArguments(eq(fileManagerService.annotationsList), eq(true), eq(false));
        PowerMockito.verifyPrivate(fileManagerService).invoke("createFile", anyString(), eq("coralanloSub"), eq("txt"), eq("coralanloSub"));
    }

    @Test
    public void createOutputCoralAnloDevTest() throws Exception {
        double[] splitNumbers = {100, 0, 0};
        FileManagerService fileManagerService = PowerMockito.spy(new FileManagerService());
        Annotations annotations = new Annotations();
        annotations.setAbsoluteValue(1);
        fileManagerService.getAnnotationsList().add(annotations);
        Coral coral = PowerMockito.mock(Coral.class);
        whenNew(Coral.class).withAnyArguments().thenReturn(coral);
        when(coral.printTxt(eq(true), eq(true))).thenReturn("coralanloDev");
        fileManagerService.createOutput(temporaryFolder.getRoot().getAbsolutePath(), "txt", "coralanloDev", "absolut", "none", splitNumbers);

        Mockito.verify(fileManagerService).setKindOfValue(eq("absolut"), eq(fileManagerService.annotationsList));
        Mockito.verify(fileManagerService).setAbsNumberOfSplit(eq(splitNumbers));
        PowerMockito.verifyNew(Coral.class).withArguments(eq(fileManagerService.annotationsList), eq(true), eq(true));
        PowerMockito.verifyPrivate(fileManagerService).invoke("createFile", anyString(), eq("coralanloDev"), eq("txt"), eq("coralanloDev"));
    }

    @Test
    public void createOutputCoralPixSubTest() throws Exception {
        double[] splitNumbers = {100, 0, 0};
        FileManagerService fileManagerService = PowerMockito.spy(new FileManagerService());
        Annotations annotations = new Annotations();
        annotations.setAbsoluteValue(1);
        fileManagerService.getAnnotationsList().add(annotations);
        Coral coral = PowerMockito.mock(Coral.class);
        whenNew(Coral.class).withAnyArguments().thenReturn(coral);
        when(coral.printTxt(eq(false), eq(false))).thenReturn("coralanloDev");
        fileManagerService.createOutput(temporaryFolder.getRoot().getAbsolutePath(), "txt", "coralpixSub", "absolut", "none", splitNumbers);

        Mockito.verify(fileManagerService).setKindOfValue(eq("absolut"), eq(fileManagerService.annotationsList));
        Mockito.verify(fileManagerService).setAbsNumberOfSplit(eq(splitNumbers));
        PowerMockito.verifyNew(Coral.class).withArguments(eq(fileManagerService.annotationsList), eq(false), eq(false));
        Assert.assertEquals(0, coral.getCoralAnnotationArrayList().size());
        Assert.assertEquals("Keine Konvertierung möglich: ImageCLEFcoral Pixel-wise Parsing task benötigt Polygon. Keine Polygonangaben vorhanden.", fileManagerService.getInfoLog().get(0).get(0));
    }

    @Test
    public void createOutputCoralPixDevTest() throws Exception {
        double[] splitNumbers = {100, 0, 0};
        FileManagerService fileManagerService = PowerMockito.spy(new FileManagerService());
        Annotations annotations = new Annotations();
        annotations.setAbsoluteValue(1);
        fileManagerService.getAnnotationsList().add(annotations);
        Coral coral = PowerMockito.mock(Coral.class);
        whenNew(Coral.class).withAnyArguments().thenReturn(coral);
        when(coral.printTxt(eq(false), eq(true))).thenReturn("coralanloDev");
        fileManagerService.createOutput(temporaryFolder.getRoot().getAbsolutePath(), "txt", "coralpixDev", "absolut", "none", splitNumbers);

        Mockito.verify(fileManagerService).setKindOfValue(eq("absolut"), eq(fileManagerService.annotationsList));
        Mockito.verify(fileManagerService).setAbsNumberOfSplit(eq(splitNumbers));
        PowerMockito.verifyNew(Coral.class).withArguments(eq(fileManagerService.annotationsList), eq(false), eq(true));
        Assert.assertEquals(0, coral.getCoralAnnotationArrayList().size());
        Assert.assertEquals("Keine Konvertierung möglich: ImageCLEFcoral Pixel-wise Parsing task benötigt Polygon. Keine Polygonangaben vorhanden.", fileManagerService.getInfoLog().get(0).get(0));
    }

    @Test
    public void createOutputPascalTest() throws Exception {
        double[] splitNumbers = {100, 0, 0};
        FileManagerService fileManagerService = PowerMockito.spy(new FileManagerService());
        Annotations annotations = new Annotations();
        annotations.setAbsoluteValue(1);
        fileManagerService.getAnnotationsList().add(annotations);
        Pascal pascal = PowerMockito.mock(Pascal.class);
        whenNew(Pascal.class).withAnyArguments().thenReturn(pascal);
        ArrayList <String> pascalArray = new ArrayList <>();
        when(pascal.printXml()).thenReturn(pascalArray);
        fileManagerService.createOutput(temporaryFolder.getRoot().getAbsolutePath(), "xml", "pascal", "absolut", "none", splitNumbers);

        Mockito.verify(fileManagerService).setKindOfValue(eq("absolut"), eq(fileManagerService.annotationsList));
        Mockito.verify(fileManagerService).setAbsNumberOfSplit(eq(splitNumbers));
        PowerMockito.verifyNew(Pascal.class).withArguments(eq(fileManagerService.annotationsList));
    }

    @Test
    public void noOutputNotConvertableFromAbsolutToRelativTest() throws Exception {
        double[] splitNumbers = {100, 0, 0};
        FileManagerService fileManagerService = PowerMockito.spy(new FileManagerService());
        Annotations annotations = new Annotations();
        annotations.setAbsoluteValue(1);
        fileManagerService.getAnnotationsList().add(annotations);
        fileManagerService.createOutput(temporaryFolder.getRoot().getAbsolutePath(), "xml", "pascal", "relativ", "none", splitNumbers);

        Mockito.verify(fileManagerService).setKindOfValue(eq("relativ"), any());
        Assert.assertEquals("Keine Konvertierung zu relativen Werten möglich.", fileManagerService.getInfoLog().get(0).get(0));
    }

    @Test
    public void noOutputNotConvertableFromRelativToAbsolutTest() throws Exception {
        double[] splitNumbers = {100, 0, 0};
        FileManagerService fileManagerService = PowerMockito.spy(new FileManagerService());
        Annotations annotations = new Annotations();
        annotations.setAbsoluteValue(0);
        fileManagerService.getAnnotationsList().add(annotations);
        fileManagerService.createOutput(temporaryFolder.getRoot().getAbsolutePath(), "xml", "pascal", "absolut", "none", splitNumbers);

        Mockito.verify(fileManagerService).setKindOfValue(eq("absolut"), any());
        Assert.assertEquals("Keine Konvertierung zu relativen Werten möglich.", fileManagerService.getInfoLog().get(0).get(0));
    }
}
