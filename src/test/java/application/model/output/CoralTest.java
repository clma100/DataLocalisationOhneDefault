package application.model.output;

import application.model.Annotations;
import application.model.outputFormats.coral.Coral;
import application.model.outputFormats.coral.CoralAnnotation;
import application.model.subsets.Bbox;
import application.model.subsets.Categories;
import application.model.subsets.Images;
import application.model.subsets.Polygon;
import application.services.parsingServices.CoralService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootConfiguration
public class CoralTest {
    ArrayList <Images> listImages;
    ArrayList <Categories> listCat;
    ArrayList <Annotations> listAnn;
    ArrayList <ArrayList <String>> infoLog;
    CoralService coralService;
    Annotations annotation1;
    Categories categorie1;
    Images image1;

    @Before
    public void setUp() {
        listAnn = new ArrayList <>();

        categorie1 = new Categories();
        categorie1.setId(1);
        categorie1.setName("categorie1");

        image1 = new Images();
        image1.setId(1);
        image1.setFileName("image1");
        image1.setHeight(400);
        image1.setWidth(300);

        Bbox bbox = new Bbox(2, 5, 6, 2);

        annotation1 = new Annotations();
        annotation1.setId(1);
        annotation1.setAbsoluteValue(1);
        annotation1.setPolygon(new Polygon());
        annotation1.setBbox(bbox);
        annotation1.setImage(image1);
        annotation1.setCategory(categorie1);
        listAnn.add(annotation1);
    }

    @After
    public void delete() {
        listAnn.clear();
    }

    @Test
    public void printZweiBilderAnloDevTest() throws IOException {
        listImages = new ArrayList <>();
        listCat = new ArrayList <>();
        listAnn = new ArrayList <>();
        infoLog = new ArrayList <>();
        ArrayList <String> infoInner = new ArrayList <>();
        infoLog.add(infoInner);
        coralService = new CoralService(listImages, listCat, listAnn, infoLog);
        String path = "src/test/resources/zweiBilder.txt";
        Assert.assertEquals(0, listImages.size());
        Assert.assertEquals(0, listCat.size());
        Assert.assertEquals(0, listAnn.size());
        coralService.readCoralTxt(path);
        Assert.assertEquals(2, listImages.size());
        Assert.assertEquals(8, listCat.size());
        Assert.assertEquals(42, listAnn.size());

        Coral coral = new Coral(listAnn, true, true);
        Assert.assertEquals(42, coral.getCoralAnnotationArrayList().size());

        String anloDevCoral = "2018_0714_112502_024 0 c_hard_coral_encrusting -1 1070 343 1427 697\n" +
                "2018_0714_112502_024 1 c_hard_coral_encrusting -1 142 1393 415 1628\n" +
                "2018_0714_112502_024 2 c_hard_coral_mushroom -1 1139 717 1356 848\n" +
                "2018_0714_112502_024 3 c_hard_coral_submassive -1 875 549 1169 808\n" +
                "2018_0714_112502_024 4 c_hard_coral_submassive -1 1218 820 1359 916\n" +
                "2018_0714_112502_024 5 c_soft_coral -1 3016 2507 3835 3008\n" +
                "2018_0714_112502_024 6 c_soft_coral -1 46 1535 1273 2656\n" +
                "2018_0714_112502_024 7 c_soft_coral -1 140 802 1812 1947\n" +
                "2018_0714_112502_024 8 c_soft_coral -1 20 500 613 843\n" +
                "2018_0714_112502_024 9 c_soft_coral -1 0 1206 195 1802\n" +
                "2018_0714_112502_024 10 c_soft_coral -1 1538 1582 2244 2074\n" +
                "2018_0714_112502_024 11 c_soft_coral -1 1970 1282 2729 2025\n" +
                "2018_0714_112502_024 12 c_soft_coral -1 2251 898 3023 1512\n" +
                "2018_0714_112502_024 13 c_soft_coral -1 1633 842 2313 1355\n" +
                "2018_0714_112502_024 14 c_soft_coral -1 1775 663 1940 795\n" +
                "2018_0714_112502_024 15 c_soft_coral -1 1904 435 2133 706\n" +
                "2018_0714_112502_024 16 c_soft_coral -1 1785 563 1911 664\n" +
                "2018_0714_112502_024 17 c_soft_coral -1 1829 240 1987 413\n" +
                "2018_0714_112502_024 18 c_soft_coral -1 1378 526 1575 683\n" +
                "2018_0714_112502_024 19 c_soft_coral_gorgonian -1 2286 2229 2669 2858\n" +
                "2018_0714_112502_024 20 c_sponge_barrel -1 1336 1947 2250 2648\n" +
                "2018_0714_112540_049 0 c_hard_coral_boulder -1 2049 758 2302 1078\n" +
                "2018_0714_112540_049 1 c_hard_coral_boulder -1 2257 796 2439 891\n" +
                "2018_0714_112540_049 2 c_hard_coral_boulder -1 2212 704 2310 787\n" +
                "2018_0714_112540_049 3 c_hard_coral_boulder -1 2370 540 2798 941\n" +
                "2018_0714_112540_049 4 c_hard_coral_boulder -1 3034 248 3782 609\n" +
                "2018_0714_112540_049 5 c_hard_coral_boulder -1 3076 1078 3358 1296\n" +
                "2018_0714_112540_049 6 c_hard_coral_boulder -1 2891 951 3182 1162\n" +
                "2018_0714_112540_049 7 c_hard_coral_boulder -1 2568 991 2878 1295\n" +
                "2018_0714_112540_049 8 c_hard_coral_branching -1 3625 659 3936 967\n" +
                "2018_0714_112540_049 9 c_hard_coral_encrusting -1 1646 768 2109 1282\n" +
                "2018_0714_112540_049 10 c_hard_coral_encrusting -1 1159 451 1728 983\n" +
                "2018_0714_112540_049 11 c_hard_coral_submassive -1 3527 938 3711 1137\n" +
                "2018_0714_112540_049 12 c_soft_coral -1 3247 457 3687 897\n" +
                "2018_0714_112540_049 13 c_soft_coral -1 3790 807 4014 1030\n" +
                "2018_0714_112540_049 14 c_soft_coral -1 2648 590 3134 944\n" +
                "2018_0714_112540_049 15 c_soft_coral -1 2734 1194 2923 1377\n" +
                "2018_0714_112540_049 16 c_soft_coral -1 2686 962 2763 1018\n" +
                "2018_0714_112540_049 17 c_soft_coral -1 2259 985 2672 1166\n" +
                "2018_0714_112540_049 18 c_soft_coral -1 2355 1149 2486 1268\n" +
                "2018_0714_112540_049 19 c_soft_coral -1 2110 1072 2281 1237\n" +
                "2018_0714_112540_049 20 c_soft_coral -1 2282 1215 2357 1320";
        Assert.assertEquals(anloDevCoral, coral.printTxt(true, true));

        Coral coralSub = new Coral(listAnn, true, false);
        Assert.assertEquals(42, coral.getCoralAnnotationArrayList().size());
        String AnloSub = "2018_0714_112502_024;c_hard_coral_encrusting -1:357x354+1070+343,-1:273x235+142+1393\n" +
                "2018_0714_112502_024;c_hard_coral_mushroom -1:217x131+1139+717\n" +
                "2018_0714_112502_024;c_hard_coral_submassive -1:294x259+875+549,-1:141x96+1218+820\n" +
                "2018_0714_112502_024;c_soft_coral -1:819x501+3016+2507,-1:1227x1121+46+1535,-1:1672x1145+140+802,-1:593x343+20+500,-1:195x596+0+1206,-1:706x492+1538+1582,-1:759x743+1970+1282,-1:772x614+2251+898,-1:680x513+1633+842,-1:165x132+1775+663,-1:229x271+1904+435,-1:126x101+1785+563,-1:158x173+1829+240,-1:197x157+1378+526\n" +
                "2018_0714_112502_024;c_soft_coral_gorgonian -1:383x629+2286+2229\n" +
                "2018_0714_112502_024;c_sponge_barrel -1:914x701+1336+1947\n" +
                "2018_0714_112540_049;c_hard_coral_boulder -1:253x320+2049+758,-1:182x95+2257+796,-1:98x83+2212+704,-1:428x401+2370+540,-1:748x361+3034+248,-1:282x218+3076+1078,-1:291x211+2891+951,-1:310x304+2568+991\n" +
                "2018_0714_112540_049;c_hard_coral_branching -1:311x308+3625+659\n" +
                "2018_0714_112540_049;c_hard_coral_encrusting -1:463x514+1646+768,-1:569x532+1159+451\n" +
                "2018_0714_112540_049;c_hard_coral_submassive -1:184x199+3527+938\n" +
                "2018_0714_112540_049;c_soft_coral -1:440x440+3247+457,-1:224x223+3790+807,-1:486x354+2648+590,-1:189x183+2734+1194,-1:77x56+2686+962,-1:413x181+2259+985,-1:131x119+2355+1149,-1:171x165+2110+1072,-1:75x105+2282+1215";
        Assert.assertEquals(AnloSub, coralSub.printTxt(false, true));
        listImages.clear();
        listCat.clear();
        listAnn.clear();
        infoLog.clear();
    }

    @Test
    public void printCorrectedAnnotationsTest() throws IOException {
        listImages = new ArrayList <>();
        listCat = new ArrayList <>();
        listAnn = new ArrayList <>();
        infoLog = new ArrayList <>();
        ArrayList <String> infoInner = new ArrayList <>();
        infoLog.add(infoInner);
        coralService = new CoralService(listImages, listCat, listAnn, infoLog);
        String path = "src/test/resources/corrected_annotations.txt";
        Assert.assertEquals(0, listImages.size());
        Assert.assertEquals(0, listCat.size());
        Assert.assertEquals(0, listAnn.size());
        coralService.readCoralTxt(path);
        Assert.assertEquals(12077, listAnn.size());

        ArrayList <Annotations> annotationsWithCertainImage = new ArrayList <>();
        for (Annotations annotation : listAnn) {
            if (annotation.getImage().getFileName().equals("2018_0714_112502_024")) {
                annotationsWithCertainImage.add(annotation);
            }
        }
        Assert.assertEquals(44, annotationsWithCertainImage.size());
        ArrayList <String> categoriesInThisImage = new ArrayList <>();
        for (Annotations annotations : annotationsWithCertainImage) {
            if (!categoriesInThisImage.contains(annotations.getCategory().getName())) {
                categoriesInThisImage.add(annotations.getCategory().getName());
            }
        }
        Assert.assertEquals(8, categoriesInThisImage.size());
        Assert.assertTrue(categoriesInThisImage.contains("c_sponge_barrel"));
        Assert.assertTrue(categoriesInThisImage.contains("c_soft_coral_gorgonian"));
        Assert.assertTrue(categoriesInThisImage.contains("c_soft_coral"));
        Assert.assertTrue(categoriesInThisImage.contains("c_hard_coral_submassive"));
        Assert.assertTrue(categoriesInThisImage.contains("c_hard_coral_mushroom"));
        Assert.assertTrue(categoriesInThisImage.contains("c_hard_coral_encrusting"));
        Assert.assertTrue(categoriesInThisImage.contains("c_hard_coral_boulder"));
        Assert.assertTrue(categoriesInThisImage.contains("c_hard_coral_branching"));

        Coral coral = new Coral(listAnn, true, true);
        Assert.assertEquals(12077, coral.getCoralAnnotationArrayList().size());

        ArrayList <CoralAnnotation> coralannotationsWithCertainImage = new ArrayList <>();
        for (CoralAnnotation coralAnnotation : coral.getCoralAnnotationArrayList()) {
            if (coralAnnotation.getImageName().equals("2018_0714_112502_024")) {
                coralannotationsWithCertainImage.add(coralAnnotation);
            }
        }
        Assert.assertEquals(44, coralannotationsWithCertainImage.size());
        for (CoralAnnotation coralAnnotation : coralannotationsWithCertainImage) {
            if (!categoriesInThisImage.contains(coralAnnotation.getSubstrate())) {
                categoriesInThisImage.add(coralAnnotation.getSubstrate());
            }
        }
        Assert.assertEquals(8, categoriesInThisImage.size());

        ArrayList <CoralAnnotation> coralannotationsWithCertainImage2 = new ArrayList <>();
        for (CoralAnnotation coralAnnotation : coral.getCoralAnnotationArrayList()) {
            if (coralAnnotation.getImageName().equals("2018_0712_073604_077")) {
                coralannotationsWithCertainImage2.add(coralAnnotation);
            }
        }
        Assert.assertEquals(19, coralannotationsWithCertainImage2.size());
        ArrayList <String> categoriesInThisImage2 = new ArrayList <>();
        for (CoralAnnotation coralAnnotation : coralannotationsWithCertainImage2) {
            if (!categoriesInThisImage2.contains(coralAnnotation.getSubstrate())) {
                categoriesInThisImage2.add(coralAnnotation.getSubstrate());
            }
        }
        Assert.assertEquals(6, categoriesInThisImage2.size());
        Assert.assertTrue(categoriesInThisImage2.contains("c_soft_coral_gorgonian"));
        Assert.assertTrue(categoriesInThisImage2.contains("c_soft_coral"));
        Assert.assertTrue(categoriesInThisImage2.contains("c_hard_coral_boulder"));
        Assert.assertTrue(categoriesInThisImage2.contains("c_hard_coral_encrusting"));
        Assert.assertTrue(categoriesInThisImage2.contains("c_algae_macro_or_leaves"));
        Assert.assertTrue(categoriesInThisImage2.contains("c_sponge"));

        listImages.clear();
        listCat.clear();
        listAnn.clear();
        infoLog.clear();
    }

    @Test
    public void printZweiBilderRelativAnloDevTest() throws IOException {
        listImages = new ArrayList <>();
        listCat = new ArrayList <>();
        listAnn = new ArrayList <>();
        infoLog = new ArrayList <>();
        ArrayList <String> infoInner = new ArrayList <>();
        infoLog.add(infoInner);
        coralService = new CoralService(listImages, listCat, listAnn, infoLog);
        String path = "src/test/resources/zweiBilder.txt";
        Assert.assertEquals(0, listImages.size());
        Assert.assertEquals(0, listCat.size());
        Assert.assertEquals(0, listAnn.size());
        coralService.readCoralTxt(path);
        Assert.assertEquals(2, listImages.size());
        Assert.assertEquals(8, listCat.size());
        Assert.assertEquals(42, listAnn.size());

        Assert.assertEquals("2018_0714_112502_024", listImages.get(0).getFileName());
        Assert.assertEquals(-1, listImages.get(0).getWidth());
        Assert.assertEquals(-1, listImages.get(0).getHeight());
        Assert.assertEquals("2018_0714_112540_049", listImages.get(1).getFileName());
        Assert.assertEquals(-1, listImages.get(1).getWidth());
        Assert.assertEquals(-1, listImages.get(1).getHeight());

        listImages.get(0).setHeight(3024);
        listImages.get(0).setWidth(4032);

        coralService.setKindOfValue("relativ",listAnn);

        Coral coral = new Coral(listAnn, true, true);
        Assert.assertEquals(21, coral.getCoralAnnotationArrayList().size());

        String anloDevRelativ = "2018_0714_112502_024 0 c_hard_coral_encrusting -1 0.26537698 0.11342593 0.35391865 0.23048942\n" +
                "2018_0714_112502_024 1 c_hard_coral_encrusting -1 0.03521825 0.46064815 0.10292659 0.53835979\n" +
                "2018_0714_112502_024 2 c_hard_coral_mushroom -1 0.28249008 0.23710317 0.33630952 0.28042328\n" +
                "2018_0714_112502_024 3 c_hard_coral_submassive -1 0.21701389 0.18154762 0.28993056 0.26719577\n" +
                "2018_0714_112502_024 4 c_hard_coral_submassive -1 0.30208333 0.27116402 0.33705357 0.30291005\n" +
                "2018_0714_112502_024 5 c_soft_coral -1 0.74801587 0.82903439 0.95114087 0.99470899\n" +
                "2018_0714_112502_024 6 c_soft_coral -1 0.01140873 0.50760582 0.31572421 0.87830688\n" +
                "2018_0714_112502_024 7 c_soft_coral -1 0.03472222 0.26521164 0.44940476 0.64384921\n" +
                "2018_0714_112502_024 8 c_soft_coral -1 0.00496032 0.16534392 0.15203373 0.27876984\n" +
                "2018_0714_112502_024 9 c_soft_coral -1 0 0.39880952 0.0483631 0.59589947\n" +
                "2018_0714_112502_024 10 c_soft_coral -1 0.38144841 0.52314815 0.55654762 0.68584656\n" +
                "2018_0714_112502_024 11 c_soft_coral -1 0.48859127 0.4239418 0.67683532 0.66964286\n" +
                "2018_0714_112502_024 12 c_soft_coral -1 0.55828373 0.29695767 0.74975198 0.5\n" +
                "2018_0714_112502_024 13 c_soft_coral -1 0.40500992 0.27843915 0.57366071 0.44808201\n" +
                "2018_0714_112502_024 14 c_soft_coral -1 0.44022817 0.21924603 0.48115079 0.26289683\n" +
                "2018_0714_112502_024 15 c_soft_coral -1 0.47222222 0.14384921 0.52901786 0.23346561\n" +
                "2018_0714_112502_024 16 c_soft_coral -1 0.44270833 0.18617725 0.47395833 0.21957672\n" +
                "2018_0714_112502_024 17 c_soft_coral -1 0.45362103 0.07936508 0.49280754 0.13657407\n" +
                "2018_0714_112502_024 18 c_soft_coral -1 0.34176587 0.1739418 0.390625 0.22585979\n" +
                "2018_0714_112502_024 19 c_soft_coral_gorgonian -1 0.56696429 0.73710317 0.66195437 0.94510582\n" +
                "2018_0714_112502_024 20 c_sponge_barrel -1 0.33134921 0.64384921 0.55803571 0.87566138";
        Assert.assertEquals(anloDevRelativ, coral.printTxt(true, true));

        listImages.clear();
        listCat.clear();
        listAnn.clear();
        infoLog.clear();
    }

    @Test
    public void coralConstruktorAnloDevTest() {
        Coral coral = new Coral(listAnn, true, true);
        Assert.assertEquals(1, coral.getCoralAnnotationArrayList().size());
        Assert.assertEquals(0, coral.getCoralAnnotationArrayList().get(0).getCount());
        Assert.assertEquals("image1", coral.getCoralAnnotationArrayList().get(0).getImageName());
        Assert.assertEquals("categorie1", coral.getCoralAnnotationArrayList().get(0).getSubstrate());
        Assert.assertEquals(-1, coral.getCoralAnnotationArrayList().get(0).getConfidence());
        Assert.assertEquals(1, coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().size());
        Assert.assertEquals(4, coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).size());
        Assert.assertEquals(java.util.Optional.of(2.0), java.util.Optional.ofNullable(coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).get(0)));
        Assert.assertEquals(java.util.Optional.of(3.0), java.util.Optional.ofNullable(coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).get(1)));
        Assert.assertEquals(java.util.Optional.of(8.0), java.util.Optional.ofNullable(coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).get(2)));
        Assert.assertEquals(java.util.Optional.of(5.0), java.util.Optional.ofNullable(coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).get(3)));
    }

    @Test
    public void coralConstruktorAnloCountsTest() {
        Annotations annotations = new Annotations();
        Images images = new Images();
        images.setFileName("image1");
        images.setId(1);
        annotations.setImage(images);
        listAnn.add(annotations);
        Coral coral1 = new Coral(listAnn, true, true);
        Assert.assertEquals(2, coral1.getCoralAnnotationArrayList().size());
        Assert.assertEquals(0, coral1.getCoralAnnotationArrayList().get(0).getCount());
        Assert.assertEquals(1, coral1.getCoralAnnotationArrayList().get(1).getCount());

        Annotations annotations2 = new Annotations();
        Images images2 = new Images();
        images2.setFileName("image2");
        images2.setId(2);
        annotations2.setImage(images2);
        listAnn.add(annotations2);
        Coral coral2 = new Coral(listAnn, true, true);
        Assert.assertEquals(3, coral2.getCoralAnnotationArrayList().size());
        Assert.assertEquals(0, coral2.getCoralAnnotationArrayList().get(0).getCount());
        Assert.assertEquals(1, coral2.getCoralAnnotationArrayList().get(1).getCount());
        Assert.assertEquals(0, coral2.getCoralAnnotationArrayList().get(2).getCount());
    }

    @Test
    public void coralConstruktorAnloSubTest() {
        Coral coral = new Coral(listAnn, true, false);
        Assert.assertEquals(1, coral.getCoralAnnotationArrayList().size());
        Assert.assertEquals(0, coral.getCoralAnnotationArrayList().get(0).getCount());
        Assert.assertEquals("image1", coral.getCoralAnnotationArrayList().get(0).getImageName());
        Assert.assertEquals("categorie1", coral.getCoralAnnotationArrayList().get(0).getSubstrate());
        Assert.assertEquals(-1, coral.getCoralAnnotationArrayList().get(0).getConfidence());
        Assert.assertEquals(1, coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().size());
        Assert.assertEquals(4, coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).size());
        Assert.assertEquals(java.util.Optional.of(6.0), java.util.Optional.ofNullable(coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).get(0)));
        Assert.assertEquals(java.util.Optional.of(2.0), java.util.Optional.ofNullable(coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).get(1)));
        Assert.assertEquals(java.util.Optional.of(2.0), java.util.Optional.ofNullable(coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).get(2)));
        Assert.assertEquals(java.util.Optional.of(3.0), java.util.Optional.ofNullable(coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).get(3)));
    }

    @Test
    public void coralConstruktorPixEmptyPolygonTest() {
        Coral coral = new Coral(listAnn, false, true);
        Assert.assertEquals(0, coral.getCoralAnnotationArrayList().size());
    }

    @Test
    public void coralConstruktorPixDevTest() {
        ArrayList <ArrayList <Double>> list = new ArrayList <>();
        ArrayList <Double> listInner = new ArrayList <>();
        listInner.add(3.0);
        listInner.add(5.0);
        listInner.add(7.7);
        listInner.add(5.7);
        listInner.add(2.0);
        listInner.add(7.2);
        list.add(listInner);
        Polygon polygon = new Polygon(list);
        annotation1.setPolygon(polygon);

        Coral coral = new Coral(listAnn, false, true);
        Assert.assertEquals(1, coral.getCoralAnnotationArrayList().size());
        Assert.assertEquals(0, coral.getCoralAnnotationArrayList().get(0).getCount());
        Assert.assertEquals("image1", coral.getCoralAnnotationArrayList().get(0).getImageName());
        Assert.assertEquals("categorie1", coral.getCoralAnnotationArrayList().get(0).getSubstrate());
        Assert.assertEquals(-1, coral.getCoralAnnotationArrayList().get(0).getConfidence());
        Assert.assertEquals(1, coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().size());
        Assert.assertEquals(6, coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).size());
        Assert.assertEquals(java.util.Optional.of(3.0), java.util.Optional.ofNullable(coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).get(0)));
        Assert.assertEquals(java.util.Optional.of(5.0), java.util.Optional.ofNullable(coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).get(1)));
        Assert.assertEquals(java.util.Optional.of(7.7), java.util.Optional.ofNullable(coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).get(2)));
        Assert.assertEquals(java.util.Optional.of(5.7), java.util.Optional.ofNullable(coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).get(3)));
        Assert.assertEquals(java.util.Optional.of(2.0), java.util.Optional.ofNullable(coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).get(4)));
        Assert.assertEquals(java.util.Optional.of(7.2), java.util.Optional.ofNullable(coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).get(5)));
    }

    @Test
    public void coralConstruktorPixSubTest() {
        ArrayList <ArrayList <Double>> list = new ArrayList <>();
        ArrayList <Double> listInner = new ArrayList <>();
        listInner.add(3.0);
        listInner.add(5.0);
        listInner.add(7.7);
        listInner.add(5.7);
        listInner.add(2.0);
        listInner.add(7.2);
        list.add(listInner);
        Polygon polygon = new Polygon(list);
        annotation1.setPolygon(polygon);

        Coral coral = new Coral(listAnn, false, false);
        Assert.assertEquals(1, coral.getCoralAnnotationArrayList().size());
        Assert.assertEquals(0, coral.getCoralAnnotationArrayList().get(0).getCount());
        Assert.assertEquals("image1", coral.getCoralAnnotationArrayList().get(0).getImageName());
        Assert.assertEquals("categorie1", coral.getCoralAnnotationArrayList().get(0).getSubstrate());
        Assert.assertEquals(-1, coral.getCoralAnnotationArrayList().get(0).getConfidence());
        Assert.assertEquals(1, coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().size());
        Assert.assertEquals(6, coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).size());
        Assert.assertEquals(java.util.Optional.of(3.0), java.util.Optional.ofNullable(coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).get(0)));
        Assert.assertEquals(java.util.Optional.of(5.0), java.util.Optional.ofNullable(coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).get(1)));
        Assert.assertEquals(java.util.Optional.of(7.7), java.util.Optional.ofNullable(coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).get(2)));
        Assert.assertEquals(java.util.Optional.of(5.7), java.util.Optional.ofNullable(coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).get(3)));
        Assert.assertEquals(java.util.Optional.of(2.0), java.util.Optional.ofNullable(coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).get(4)));
        Assert.assertEquals(java.util.Optional.of(7.2), java.util.Optional.ofNullable(coral.getCoralAnnotationArrayList().get(0).getPolygonOrBbox().get(0).get(5)));
    }

    @Test
    public void coralprintTxtAnloDev() {
        Coral coral = new Coral(listAnn, true, true);
        String coralAnloDev = "image1 0 categorie1 -1 2 3 8 5";
        Assert.assertEquals(coralAnloDev, coral.printTxt(true, true));
    }

    @Test
    public void printRelativValues() {
        Bbox bboxRelativ = new Bbox(0.02, 0.8, 0.7, 0.6);
        annotation1.setBbox(bboxRelativ);
        Coral coral = new Coral(listAnn, true, true);
        String coralRelativ = "image1 0 categorie1 -1 0.02 0.2 0.72 0.8";
        Assert.assertEquals(coralRelativ, coral.printTxt(true, true));
    }

    @Test
    public void coralprintTxtAnloDevTwo() {
        Annotations annotations = new Annotations();
        listAnn.add(annotations);
        Coral coral = new Coral(listAnn, true, true);
        String coralAnloDev = "image1 0 categorie1 -1 2 3 8 5\n" + "unknown 0 unknown -1 0 0 0 0";
        Assert.assertEquals(coralAnloDev, coral.printTxt(true, true));
    }

    @Test
    public void coralprintTxtAnloSub() {
        Coral coral = new Coral(listAnn, true, false);
        String coralAnloDev = "image1;categorie1 -1:6x2+2+3";
        Assert.assertEquals(coralAnloDev, coral.printTxt(false, true));
    }

    @Test
    public void coralprintTxtAnloSubTwo() {
        Annotations annotations = new Annotations();
        listAnn.add(annotations);
        Coral coral = new Coral(listAnn, true, false);
        String coralAnloDev = "image1;categorie1 -1:6x2+2+3\n" + "unknown;unknown -1:0x0+0+0";
        Assert.assertEquals(coralAnloDev, coral.printTxt(false, true));
        listAnn.add(annotations);
        Coral coral2 = new Coral(listAnn, true, false);
        String coralAnloDevTwo = "image1;categorie1 -1:6x2+2+3\n" + "unknown;unknown -1:0x0+0+0,-1:0x0+0+0";
        Assert.assertEquals(coralAnloDevTwo, coral2.printTxt(false, true));
    }

    @Test
    public void coralprintTxtPixDevEmptyPolygon() {
        Coral coral = new Coral(listAnn, false, true);
        String coralAnloDev = "";
        Assert.assertEquals(coralAnloDev, coral.printTxt(true, false));
    }

    @Test
    public void coralprintTxtPixDev() {
        ArrayList <ArrayList <Double>> list = new ArrayList <>();
        ArrayList <Double> listInner = new ArrayList <>();
        listInner.add(3.0);
        listInner.add(5.0);
        listInner.add(7.7);
        listInner.add(5.7);
        listInner.add(2.0);
        listInner.add(7.2);
        list.add(listInner);
        Polygon polygon = new Polygon(list);
        annotation1.setPolygon(polygon);

        Coral coral = new Coral(listAnn, false, true);
        String coralAnloDev = "image1 0 categorie1 -1 3 5 8 6 2 7";
        Assert.assertEquals(coralAnloDev, coral.printTxt(true, false));

        Annotations annotation2 = new Annotations();
        annotation2.setPolygon(polygon);
        listAnn.add(annotation2);
        Coral coral2 = new Coral(listAnn, false, true);
        String coralAnloDevTwo = "image1 0 categorie1 -1 3 5 8 6 2 7\n" + "unknown 0 unknown -1 3 5 8 6 2 7";
        Assert.assertEquals(coralAnloDevTwo, coral2.printTxt(true, false));
    }

    @Test
    public void coralprintTxtPixSub() {
        ArrayList <ArrayList <Double>> list = new ArrayList <>();
        ArrayList <Double> listInner = new ArrayList <>();
        listInner.add(3.0);
        listInner.add(5.0);
        listInner.add(7.7);
        listInner.add(5.7);
        listInner.add(2.0);
        listInner.add(7.2);
        list.add(listInner);
        Polygon polygon = new Polygon(list);
        annotation1.setPolygon(polygon);

        Coral coral = new Coral(listAnn, false, false);
        String coralAnloDev = "image1;categorie1 -1:3+5+8+6+2+7";
        Assert.assertEquals(coralAnloDev, coral.printTxt(false, false));

        Annotations annotation2 = new Annotations();
        annotation2.setPolygon(polygon);
        listAnn.add(annotation2);
        Coral coral2 = new Coral(listAnn, false, false);
        String coralAnloDevTwo = "image1;categorie1 -1:3+5+8+6+2+7\n" + "unknown;unknown -1:3+5+8+6+2+7";
        Assert.assertEquals(coralAnloDevTwo, coral2.printTxt(false, false));

        Annotations annotation3 = new Annotations();
        annotation3.setImage(image1);
        listAnn.add(annotation3);
        Coral coral3 = new Coral(listAnn, false, false);
        String coralAnloDevThree = "image1;categorie1 -1:3+5+8+6+2+7\n" + "unknown;unknown -1:3+5+8+6+2+7";
        Assert.assertEquals(coralAnloDevThree, coral3.printTxt(false, false));

        annotation3.setPolygon(polygon);
        Coral coral4 = new Coral(listAnn, false, false);
        String coralAnloDevFour = "image1;categorie1 -1:3+5+8+6+2+7\n" + "image1;unknown -1:3+5+8+6+2+7\n" + "unknown;unknown -1:3+5+8+6+2+7";
        Assert.assertEquals(coralAnloDevFour, coral4.printTxt(false, false));

        annotation3.setCategory(categorie1);
        Coral coral5 = new Coral(listAnn, false, false);
        String coralAnloDevFive = "image1;categorie1 -1:3+5+8+6+2+7,-1:3+5+8+6+2+7\n" + "unknown;unknown -1:3+5+8+6+2+7";
        Assert.assertEquals(coralAnloDevFive, coral5.printTxt(false, false));
    }
}
