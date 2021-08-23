package com.cognizant.cde.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class IdentifyRabbitQueueService {

    Logger logger = LoggerFactory.getLogger(IdentifyRabbitQueueService.class);

    private final String RABBIT_QUEUE_GROUP = "com.rabbitmq";

    private final FileUtilityService fileUtilityService;

    public IdentifyRabbitQueueService(FileUtilityService fileUtilityService) {
        this.fileUtilityService = fileUtilityService;
    }


    private Optional<Path> findAndReturnMatchedPath(String findFile, String location) throws IOException {
        String glob = "glob:" + findFile;
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(glob);
        return Files.walk(Paths.get(location)).filter(pathMatcher::matches).findFirst();
    }
    private Boolean isRabbitQueueLib(String groupId) {
        return (groupId.trim().equalsIgnoreCase(RABBIT_QUEUE_GROUP));
    }

    private Boolean identifyRabbitQueueInPom(File pomFilePath) throws ParserConfigurationException, IOException, SAXException {
        Boolean isRabbitQueueFound = Boolean.FALSE;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(pomFilePath);
        document.getDocumentElement().normalize();
        NodeList dependenciesNodeList = document.getElementsByTagName("dependencies");
        for (int itr = 0; itr < dependenciesNodeList.getLength(); itr++) {
            Node node = dependenciesNodeList.item(itr);
            Boolean blFlg = Boolean.FALSE;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                NodeList dependencyNodeList = element.getElementsByTagName("dependency");
                for(int i=0;i<dependencyNodeList.getLength();i++) {
                    Node node1 = dependencyNodeList.item(i);
                    if (node1.getNodeType() == Node.ELEMENT_NODE) {
                        Element element1 = (Element) node1;
                        if (isRabbitQueueLib( element1.getElementsByTagName("groupId").item(0).getTextContent())) {
                            blFlg = Boolean.TRUE;
                            isRabbitQueueFound = true;
                            break;
                        }
                    }
                }
                if (blFlg) {
                    break;
                }
            }
        }
        return isRabbitQueueFound;
    }

    private Boolean identifyRabbitQueueInGradle(String gradlePath) {
        Path path = Paths.get(gradlePath);
        Boolean isRabbitQueueFound = Boolean.FALSE;
        try(Stream<String> streamOfLines = Files.lines(path)) {
            Optional <String> line = streamOfLines.filter(l ->
                    l.contains(RABBIT_QUEUE_GROUP))
                    .findFirst();
            isRabbitQueueFound = line.isPresent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isRabbitQueueFound;
    }

    public void processMQMigrate(String projectRootPath, String projectDestinationPath) throws IOException {

        fileUtilityService.copyDir(projectRootPath, projectDestinationPath);

        Optional<Path> pomPath = findAndReturnMatchedPath( projectDestinationPath + "/pom.xml", projectDestinationPath);
        Optional<Path> gradlePath = findAndReturnMatchedPath(  projectDestinationPath + "/build.gradle", projectDestinationPath);

        pomPath.ifPresent(path -> {
            try {
                logger.info( "POM :::: " + identifyRabbitQueueInPom(path.toFile()));
            } catch (ParserConfigurationException | IOException | SAXException e) {
                e.printStackTrace();
            }
        });

        gradlePath.ifPresent(path -> logger.info("Gradle ::: " + identifyRabbitQueueInGradle(path.toFile().getAbsolutePath())));
    }

}
